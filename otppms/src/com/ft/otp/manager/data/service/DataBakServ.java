package com.ft.otp.manager.data.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.dom4j.Document;
import org.dom4j.Element;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.Constant;
import com.ft.otp.common.DbConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.database.DbFactory;
import com.ft.otp.common.database.pool.DbEnv;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.data.dao.DataBakDao;
import com.ft.otp.manager.data.entity.DBBakConfInfo;
import com.ft.otp.manager.data.entity.TableInfo;
import com.ft.otp.util.tool.CreateFileUtil;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.FileUtils;
import com.ft.otp.util.tool.StrTool;
import com.ft.otp.util.xml.XmlUtil;
import com.ft.otp.util.zip.ZipCompress;

public class DataBakServ {

    private static Logger logger = Logger.getLogger(DataBakServ.class);

    /**
     * 判断是否存在配置信息
     * @return boolean 
     */
    public static boolean isExsitConfigParam(DBBakConfInfo bakParam) {
        boolean result = false; //不存在
        int isTimeAutoStr = bakParam.getIstimeauto();
        if (isTimeAutoStr == 0 || isTimeAutoStr == 1) {//存在
            result = true;
        }

        return result;
    }

    /**
     * 从xml配置文件中获取标准数据库表和字段以及序列等信息
     * 方法说明
     * @Date in Dec 28, 2012,11:25:01 AM
     * @param isBakLog 是否备份日志数据 true备份 flase不备份
     * @return
     * 
     * @throws BaseException
     * 
     */
    public static Map<String, TableInfo> getTablesAndCols(int isBakLog) throws BaseException {
        Map<String, TableInfo> tableInfoMap = new HashMap<String, TableInfo>();//返回值
        initTableInfos(isBakLog, null, tableInfoMap);//将数据放入map中
        return tableInfoMap;
    }

    /**
     * 备份数据
     * @param tableInfoMap
     * @param param
     *          备份相关配置信息
     * @param conditionVal
     *          需要的条件    
     * @throws Exception 
     * @retrun int  
     *          0 : 失败 其他原因
     * 			1 : 失败 空条件异常 配置信息或链接参数异常
     *          2 ：失败 目录不存在
     *          3 ：失败 远程备份失败 多源于远程上传文件 或 没有上传权限
     *          4 ：成功 本地或远程
     *           
     */
    public static int createBakFile(Map<String, TableInfo> tableInfoMap, DBBakConfInfo param, String conditionVal)
            throws Exception {

        int resultTag = 0;

        if (!StrTool.mapNotNull(tableInfoMap) || !StrTool.objNotNull(param)) {
            resultTag = NumConstant.common_number_1;
            return resultTag; //条件异常
        }

        //每个表创建表一个文件并写入数据
        String filePath = "";//文件存放目录
        if (param.getIsremote() == NumConstant.common_number_1) { //如果是远程
            filePath = param.getTempdir();
        } else {
            filePath = param.getDir();
        }

        //矫正路径
        filePath = CreateFileUtil.correctPath(filePath);

        //路径是否存在	
        boolean isExsit = CreateFileUtil.isFileExists(filePath);
        if (!isExsit) {
            resultTag = NumConstant.common_number_2;
            return resultTag;
        }

        //生成临时目录  赞存生成的sql文件   
        String tempSqlfilePath = filePath + File.separatorChar + "tempbak" + File.separatorChar;
        CreateFileUtil.createOrDelDir(tempSqlfilePath);

        //生成sql文件
        Iterator<String> it = tableInfoMap.keySet().iterator();
        TableInfo tinfo = null;
        int totalTablesInsertCount = 0;//所有insert语句数量
        while (it.hasNext()) {
            tinfo = (TableInfo) tableInfoMap.get(it.next());
            String tableName = tinfo.getName();
            String fileName = tableName + Constant.FILE_SQL;

            CreateFileUtil.deleteFile(tempSqlfilePath + fileName);//删除已存在的文件            
            int tableInserCount = writeDataToFile(tempSqlfilePath, fileName, tinfo, conditionVal);//向sql文件写入数据
            totalTablesInsertCount += tableInserCount;//累积数量

        }

        //生成描述性文件.txt类型
        String content = "total insert count : " + totalTablesInsertCount;
        OutputStream output = CreateFileUtil.getFileOutStream(tempSqlfilePath
                + (Constant.DATABAK_INFO + Constant.FILE_TXT));
        IOUtils.write(content, output, "utf-8");
        FileUtils.close(output);

        //压缩为zip文件
        String zipFileName = "otpdbV4_" + DateTool.getCurDate("yyyyMMddHHmmss") + Constant.FILE_ZIP;
        String zipPath = filePath + File.separatorChar + zipFileName;
        ZipCompress.zip(tempSqlfilePath, zipPath);

        resultTag = NumConstant.common_number_4; //备份成功

        //如果是远程 那么上传到远程服务器
        if (param.getIsremote() == NumConstant.common_number_1) {
            FtpUpload fu = FtpUpload.getInstance();
            boolean result = fu.upload(param, zipPath, zipFileName);//上传到远程服务器        	
            if (result) {
                FileUtils.clearPath(zipPath);//清空本地临时目录
            } else { //如果失败了
                resultTag = NumConstant.common_number_3; //远程备份失败
                if (param.getIstimeauto() == NumConstant.common_number_1) {//如果是定时备份 3次重传次数
                    for (int i = 0; i < 3; i++) {
                        result = fu.upload(param, zipPath, zipFileName);//上传到远程服务器
                        if (result) {
                            resultTag = NumConstant.common_number_4;
                            break;
                        }
                    } //end for
                }
            }
        }
        FileUtils.clearPath(tempSqlfilePath);//清空sql所在目录

        return resultTag;// 返回结果 有问题  视情况 允许失败
    }

    /**
     * 
     * 数据写入文件中
     * 并返回生成的insert语句数量
     * @param filePath 路径
     * @param fileName 
     * @param tinfo 
     * @param condition
     *          需要的条件               
     * @throws IOException 
     * @throws SQLException 
     * @retrun int  生成的insert语句数
     *           
     */
    public static int writeDataToFile(String filePath, String fileName, TableInfo tinfo, String conditionVal)
            throws BaseException, IOException {
        int insertCount = 0;// 总备份行数
        //OutputStream output = new FileOutputStream(new File(filePath, fileName));
        OutputStream output = CreateFileUtil.getFileOutStream(filePath + fileName);
        String tableName = tinfo.getName();
        String columns = tinfo.getColumns(); //该表的字段

        DataBakDao dataBakdao = null;
        try {
            dataBakdao = DataBakDao.getInstance();

            int isPage = 0;//是否需要分页
            int loop = 0;//大数据量时需要 分页查询数据
            int totalCount = 0;
            //此三张表数据量大是需要分页备份的otppms_tokeninfo、otppms_loginfo、 otppms_admin_log
            if (StrTool.strEqualsIgnoreCase(tableName, DbConstant.DB_TABLE_NAME_TOKENINFO)) {
                totalCount = dataBakdao.getTableCount(tableName, null);
            } else if (StrTool.strEqualsIgnoreCase(tableName, DbConstant.DB_TABLE_NAME_LOGINFO)
                    || StrTool.strEqualsIgnoreCase(tableName, DbConstant.DB_TABLE_NAME_ADMINLOG)) {
                String condition = null;
                if (StrTool.strNotNull(conditionVal)) {
                    condition = " where logtime<=" + conditionVal;
                }
                totalCount = dataBakdao.getTableCount(tableName, condition);
            }

            // 判断是否需要分页，以及分页数
            if (totalCount == 0) {//不分页
                loop = 1;
                isPage = 0;
            } else {//取1次1w条数据需要查询的次数 
                if (totalCount % NumConstant.batchCount_10000 == 0) {
                    loop = totalCount / NumConstant.batchCount_10000;
                } else {
                    loop = totalCount / NumConstant.batchCount_10000 + 1;
                }
                isPage = 1;// 需要分页
            }

            //循环次数
            ResultSet rs = null;
            for (int i = 0; i < loop; i++) {
                if (isPage == 1) {//需要分页
                    // 组装分页参数
                    PageArgument pageArg = new PageArgument();
                    pageArg.setCurPage(i);
                    if (i == loop - 1 && (totalCount % NumConstant.batchCount_10000 != 0)) {
                        pageArg.setPageSize(totalCount % NumConstant.batchCount_10000);
                    } else {
                        pageArg.setPageSize(NumConstant.batchCount_10000);
                    }
                    pageArg.setStartRow(i * NumConstant.batchCount_10000);

                    // 查询分页结果集
                    rs = dataBakdao.getTableData(tableName, columns, conditionVal, pageArg); //分页返回结果集
                } else {
                    rs = dataBakdao.getTableData(tableName, columns); //直接返回所有结果集
                }

                if (StrTool.objNotNull(rs)) {
                    String[] colArray = columns.split(",");
                    while (rs.next()) {
                        StringBuffer insertStr = new StringBuffer();
                        insertStr.append("insert into " + tableName + "(" + columns + ") values (");
                        int length = colArray.length;
                        for (int j = 0; j < length; j++) {
                            String columnValue = rs.getString(colArray[j]);
                            if (StrTool.objNotNull(columnValue) && !StrTool.strEquals(columnValue, "null")) {
                                columnValue = columnValue.replace("'", "''"); //单引号 换成双引号
                                if (rs.getMetaData().getColumnType(j + 1) == NumConstant.common_number_4
                                        || rs.getMetaData().getColumnType(j + 1) == NumConstant.common_number_2) { // 2或4代表INTEGER,12代表String
                                    insertStr.append(columnValue);
                                } else {
                                    insertStr.append("'" + columnValue + "'");
                                }
                            } else {
                                insertStr.append("null");
                            }
                            if (j != (length - 1)) { //不是最后一个
                                insertStr.append(",");
                            } else {//最后一个 回车换行 保证每行都是一个sql
                                insertStr.append(")\r\n");
                            }
                        }
                        // 写入文件
                        IOUtils.write(insertStr, output, "utf-8");
                        insertCount++;
                    } //end while
                    //rs.close();
                }
                dataBakdao.close();//关闭结果集、句柄和连接

            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            if (dataBakdao != null) {
                dataBakdao.close();
            }
        }
        FileUtils.close(output);

        return insertCount;
    }

    /**
     * 将配的表读入list、map中 
     * 其中list是按序排放的
     * @param isBakLog 是否读取日志文件
     * @param tableList 
     * @param tableInfoMap 
     * 
     */
    public static void initTableInfos(int isBakLog, List<TableInfo> tableList, Map<String, TableInfo> tableInfoMap) {
        Document document = XmlUtil.getDocument(Constant.DATA_BAK_CONF_XML);
        Element root = document.getRootElement();
        Iterator<?> products = root.elementIterator();

        TableInfo tableInfo = null; //变量引用

        while (products.hasNext()) {
            Element elem = (Element) products.next();
            Iterator<?> tables = elem.elementIterator();
            while (tables.hasNext()) {
                Element tableEle = (Element) tables.next();
                tableInfo = new TableInfo();

                //logdata
                Element logdata = tableEle.element("logdata");
                if (StrTool.objNotNull(logdata)) { //日志文件
                    if (isBakLog == NumConstant.common_number_0) {//如果不备份
                        continue; //结束本次循环
                    }
                }
                
                // 只备份日志表
                if (isBakLog == NumConstant.common_number_2) {
                    if (!StrTool.objNotNull(logdata)) {
                        continue;//结束本次循环
                    }
                }

                //name
                String tableName = tableEle.element("name").getText();
                if (StrTool.strNotNull(tableName)) {
                    tableInfo.setName(tableName);
                }

                // auto-incremented
                Element autoIncrementElement = tableEle.element("auto-incremented");
                if (StrTool.objNotNull(autoIncrementElement)) {
                    String autoIncrementColumn = autoIncrementElement.getText();
                    if (StrTool.strNotNull(autoIncrementColumn)) {
                        tableInfo.setAutoIncrementedColumn(autoIncrementColumn);
                    }
                }

                // columns
                Element columnEle = tableEle.element("columns");
                if (StrTool.objNotNull(columnEle)) {
                    String columns = columnEle.getText();
                    if (StrTool.strNotNull(columns)) {

                        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                        Matcher m = p.matcher(columns);
                        columns = m.replaceAll(""); //去除回车换行符

                        tableInfo.setColumns(columns);
                    }
                }

                // sequence
                Element sequenceElement = tableEle.element("sequence");
                if (StrTool.objNotNull(sequenceElement)) {
                    String sequence = sequenceElement.getText();
                    if (StrTool.strNotNull(sequence)) {
                        tableInfo.setSequence(sequence);
                    }
                }

                if (StrTool.objNotNull(tableInfoMap)) {
                    tableInfoMap.put(tableInfo.getName(), tableInfo);
                }
                if (StrTool.objNotNull(tableList)) {
                    tableList.add(tableInfo);
                }
            } //end while
        } //end products while
    }

    /**
     * 从xml配置文件中获取标准数据库表和字段以及序列等信息
     * 方法说明
     * @Date in Dec 28, 2012,11:25:01 AM
     * @param isBakLog 是否备份日志数据 true备份 flase不备份
     * @return
     * 
     * @throws BaseException
     * 
     */
    public static List<TableInfo> getTablesAndColsList(int isBakLog) throws BaseException {
        List<TableInfo> tableInfoList = new ArrayList<TableInfo>();//返回值
        initTableInfos(isBakLog, tableInfoList, null);//将数据放入列表中
        return tableInfoList;
    }

    /**
     * 读取sql文件
     * @Date in Dec 22, 2011,11:40:25 AM
     * @param sqlEntry 对应相应的sql文件
     * @return
     */
    public static List<String> loadSql(ZipFile zipFile, ZipEntry sqlEntry) {

        if (!StrTool.objNotNull(sqlEntry)) {
            return null;
        }

        List<String> sqlList = new ArrayList<String>();
        try {
            InputStreamReader in = new InputStreamReader(zipFile.getInputStream(sqlEntry), "utf-8");
            BufferedReader sqlFileIn = new BufferedReader(in);
            String line = "";
            while ((line = sqlFileIn.readLine()) != null) {
                sqlList.add(line);
            }
            sqlFileIn.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        //return sqlSb.toString();
        return sqlList;
    }

    /**
     * 还原数据前 先将数据库的自增id机制关闭
     * 还原数据-批量按表排序执行insert 语句
     * 
     * @param filePath  zip文件路径包括文件名
     * @param isCreateTable   是否创建表
     * @return boolean        true 成功 false 失败
     * 
     * @throws BaseException 
     * @throws SQLException 
     * @throws IOException 
     */
    public static boolean revertSql(String filePath) throws BaseException, SQLException, IOException {
        boolean result = true;
        int isRevertLog = NumConstant.common_number_1;//读取日志表
        List<TableInfo> tableNames = getTablesAndColsList(isRevertLog); //表顺序,配置中正顺序

        if (!StrTool.objNotNull(tableNames)) {
            result = false; //恢复失败  没有配置信息 
        }

        ZipFile zipFile = new ZipFile(filePath);
        if (!StrTool.objNotNull(zipFile)) {
            return false; //恢复失败  没找到zip恢复文件
        }

        String infoFileName = "";
        for (Enumeration<?> entries = zipFile.getEntries(); entries.hasMoreElements();) {
            ZipEntry temp_entry = (ZipEntry) entries.nextElement();
            File file = new File(temp_entry.getName());
            String fileName = file.getName();
            // 如果有特殊的指定文件
            if (StrTool.strEquals(fileName.toLowerCase(), Constant.DATABAK_INFO.toLowerCase() + Constant.FILE_TXT)) {
                infoFileName = fileName;
                break;
            }
        }

        if (!StrTool.strNotNull(infoFileName)) {
            return false; //恢复失败 不是正确备份文件 
        }

        /*这里可以扩展 对指定文件的读取*/
        String dbtype = DbEnv.getDbtype(); //当前数据库 类型
        DbFactory dbFactory = new DbFactory(); //连接工厂
        Connection conn = dbFactory.getConnection(); //取得连接
        DataBakDao dataBakdao = DataBakDao.getInstance(); //操作句柄

        // 先倒序删除数据库中的数据
        for (int i = tableNames.size() - 1; i >= 0; i--) {
            TableInfo ti = (TableInfo) tableNames.get(i);
            String tableName = ti.getName();
            Statement stmt = conn.createStatement();
            stmt.execute("delete from " + tableName); //先清空数据    
            stmt.executeBatch();
        }

        //按序还原数据
        Iterator<TableInfo> it = tableNames.iterator();
        while (it.hasNext()) {
            TableInfo ti = (TableInfo) it.next();
            String tableName = ti.getName();

            //还原前 破坏机制
            if (StrTool.strEquals(dbtype, DbConstant.DB_TYPE_ORACLE)) {
                dataBakdao.execChangeStateTrigger(conn, ti, false);
            } else if (StrTool.strEquals(dbtype, DbConstant.DB_TYPE_MSSQL)) {
                dataBakdao.execChangeIdentityState(conn, tableName, false);
            }

            //找到对应表sql文件
            ZipEntry entry = null;
            for (Enumeration<?> entries = zipFile.getEntries(); entries.hasMoreElements();) {
                ZipEntry temp_entry = (ZipEntry) entries.nextElement();
                File sqlFile = new File(temp_entry.getName());

                String fileName = sqlFile.getName();
                // 如果文件名一至
                if (StrTool.strEquals(fileName.toLowerCase(), tableName.toLowerCase() + Constant.FILE_SQL)) {
                    entry = temp_entry;
                    break;
                }
            }

            //获得sql语句
            List<String> sqlList = loadSql(zipFile, entry);

            //恢复数据
            boolean tableResult = dataBakdao.execSql(conn, tableName, sqlList);

            if (!tableResult) {//失败
                result = false;
            }

            //还原数据之后 恢复机制
            if (StrTool.strEquals(dbtype, DbConstant.DB_TYPE_POSTGRESQL)) {
                dataBakdao.execSetSequenceVal(conn, ti);
            } else if (StrTool.strEquals(dbtype, DbConstant.DB_TYPE_ORACLE)) {
                dataBakdao.execChangeStateTrigger(conn, ti, true);
            } else if (StrTool.strEquals(dbtype, DbConstant.DB_TYPE_MSSQL)) {
                dataBakdao.execChangeIdentityState(conn, tableName, true);
            }
        }

        if (StrTool.objNotNull(conn)) {
            conn.close();
        }

        return result;
    }

}
