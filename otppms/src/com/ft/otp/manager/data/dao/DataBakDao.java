package com.ft.otp.manager.data.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.DbConstant;
import com.ft.otp.common.database.DbFactory;
import com.ft.otp.common.database.pool.DbEnv;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.data.entity.TableInfo;
import com.ft.otp.util.tool.StrTool;

public class DataBakDao extends BaseSqlMapDAO {
    private Logger logger = Logger.getLogger(DataBakDao.class);
    private static DataBakDao instance = new DataBakDao();

    private Connection conn;
    private Statement stmt;
    private ResultSet rs;

    private DataBakDao() {
    }

    public static DataBakDao getInstance() {
        return instance;
    }

    /**
     * 从相应的表中得出相应的数据 一次获取所有数据
     * @param tableName
     * @param columns
     * @return ResultSet
     * 
     * @throws BaseException
     */
    public ResultSet getTableData(String tableName, String columns) throws BaseException {
        try {
            DbFactory dbFactory = new DbFactory();
            conn = dbFactory.getConnection();
            stmt = conn.createStatement();

            String sql = "select " + columns + " from " + tableName;
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            close();
            logger.error(e.getMessage(), e);
        }
        return rs;
    }

    /**
     * 从相应的表中得出相应的数据 分页查询数据;只支持 otppms_tokeninfo,otppms_admin_log,otppms_loginfo
     * 
     * @Date in Dec 16, 2013,5:38:40 PM
     * @param tableName
     * @param columns
     * @param condition
     *          查询条件
     * @param pageArg
     * @return
     * @throws BaseException
     */
    public ResultSet getTableData(String tableName, String columns, String condition, PageArgument pageArg)
            throws BaseException {
        try {
            DbFactory dbFactory = new DbFactory();
            conn = dbFactory.getConnection();
            stmt = conn.createStatement();

            StringBuilder sql = new StringBuilder();
            String dbType = DbEnv.getDbtype();
            if (StrTool.strEquals(dbType, DbConstant.DB_TYPE_MYSQL)
                    || StrTool.strEquals(dbType, DbConstant.DB_TYPE_POSTGRESQL)) {// mysql 和 pg 数据库
                if (StrTool.strEqualsIgnoreCase(tableName, DbConstant.DB_TABLE_NAME_TOKENINFO)) {// otppms_tokeninfo

                    sql.append("select " + columns + " from otppms_tokeninfo order by token asc LIMIT "
                            + pageArg.getPageSize() + " OFFSET " + pageArg.getStartRow());

                } else if (StrTool.strEqualsIgnoreCase(tableName, DbConstant.DB_TABLE_NAME_LOGINFO)
                        || StrTool.strEqualsIgnoreCase(tableName, DbConstant.DB_TABLE_NAME_ADMINLOG)) {// log

                    sql.append("select " + columns + " from " + tableName + " where 1=1 ");
                    if (StrTool.strNotNull(condition)) {
                        sql.append(" and logtime<=" + condition);
                    }
                    sql.append(" order by logtime desc LIMIT " + pageArg.getPageSize() + " OFFSET ").append(
                            pageArg.getStartRow());
                }

            } else if (StrTool.strEquals(dbType, DbConstant.DB_TYPE_DB2)) {//db2
                if (StrTool.strEqualsIgnoreCase(tableName, DbConstant.DB_TABLE_NAME_TOKENINFO)) {// otppms_tokeninfo

                    sql
                            .append("select "
                                    + columns
                                    + " from (select t.*,ROWNUMBER() OVER() as rn from (select tn.* from otppms_tokeninfo tn  where 1=1 order by  tn.token asc) t) tt where tt.rn > "
                                    + pageArg.getStartRow() + " AND tt.rn <= "
                                    + (pageArg.getStartRow() + pageArg.getPageSize()) + " order by token asc ");

                } else if (StrTool.strEqualsIgnoreCase(tableName, DbConstant.DB_TABLE_NAME_LOGINFO)
                        || StrTool.strEqualsIgnoreCase(tableName, DbConstant.DB_TABLE_NAME_ADMINLOG)) {// log

                    sql.append("select " + columns + " from (select t.*,ROWNUMBER() OVER() as rn from (select "
                            + columns + " from " + tableName + " where 1=1 ");
                    if (StrTool.strNotNull(condition)) {
                        sql.append(" and logtime<=" + condition);
                    }
                    sql.append(" order by logtime desc) t) tt where tt.rn > " + pageArg.getStartRow()
                            + " AND tt.rn <= " + (pageArg.getStartRow() + pageArg.getPageSize())
                            + " order by logtime desc");
                }
            } else if (StrTool.strEquals(dbType, DbConstant.DB_TYPE_ORACLE)) {// oracle
                if (StrTool.strEqualsIgnoreCase(tableName, DbConstant.DB_TABLE_NAME_TOKENINFO)) {// otppms_tokeninfo

                    sql
                            .append("select "
                                    + columns
                                    + " from (select  rownum as rn,  t.* from (select tn.*from otppms_tokeninfo tn where 1=1 order by  tn.token asc) t where rownum < = "
                                    + (pageArg.getStartRow() + pageArg.getPageSize()) + ") tt where rn > "
                                    + pageArg.getStartRow() + " order by token asc ");

                } else if (StrTool.strEqualsIgnoreCase(tableName, DbConstant.DB_TABLE_NAME_LOGINFO)
                        || StrTool.strEqualsIgnoreCase(tableName, DbConstant.DB_TABLE_NAME_ADMINLOG)) {// log

                    sql.append("select " + columns + " from ( select rownum as rn, t.* from ( select " + columns
                            + " from " + tableName + " where 1=1 ");
                    if (StrTool.strNotNull(condition)) {
                        sql.append(" and logtime<=" + condition);
                    }
                    sql.append(" order by logtime desc) t where rownum < = "
                            + (pageArg.getStartRow() + pageArg.getPageSize()) + ") tt where rn > "
                            + pageArg.getStartRow() + " order by logtime desc ");

                }
            } else if (StrTool.strEquals(dbType, DbConstant.DB_TYPE_SQLSERVER)) {// sql server
                if (StrTool.strEqualsIgnoreCase(tableName, DbConstant.DB_TABLE_NAME_TOKENINFO)) {// otppms_tokeninfo

                    sql
                            .append("SELECT "
                                    + columns
                                    + " from (select top "
                                    + pageArg.getPageSize()
                                    + " tn.* from otppms_tokeninfo tn where 1=1 and tn.token not in (select top "
                                    + pageArg.getStartRow()
                                    + " tn.token from otppms_tokeninfo tn where 1=1 order by tn.token asc) order by tn.token asc) tn order by tn.token asc");

                } else if (StrTool.strEqualsIgnoreCase(tableName, DbConstant.DB_TABLE_NAME_LOGINFO)
                        || StrTool.strEqualsIgnoreCase(tableName, DbConstant.DB_TABLE_NAME_ADMINLOG)) {// log

                    sql.append("select " + columns + " from (select top " + pageArg.getPageSize() + " * from "
                            + tableName + " where 1=1 ");
                    if (StrTool.strNotNull(condition)) {
                        sql.append(" and logtime<=" + condition);
                    }
                    sql.append("and id not in (select top " + pageArg.getStartRow() + " id  from " + tableName
                            + " where 1=1 ");
                    if (StrTool.strNotNull(condition)) {
                        sql.append(" and logtime<=" + condition);
                    }
                    sql.append(" order by logtime desc) order by logtime desc) otppms_admininfo order by logtime desc ");
                }
            }

            // 需要将数据量大的表 采用分页处理
            rs = stmt.executeQuery(sql.toString());
        } catch (SQLException e) {
            close();
            logger.error(e.getMessage(), e);
        }

        return rs;
    }

    /**
     * 从相应的表中得出此表数据总行数
     * 
     * @param tableName
     * @param condition
     *          条件，如：日志表可能会有结束时间条件，如无条件传入null
     * @return int
     * 
     * @throws BaseException
     */
    public int getTableCount(String tableName, String condition) throws BaseException {
        int sumCount = 0;
        try {
            DbFactory dbFactory = new DbFactory();
            conn = dbFactory.getConnection();

            stmt = conn.createStatement();

            // 查询此表数据总行数
            String sql = "select count(*) as totalnum from " + tableName;
            if (StrTool.strNotNull(condition)) {
                sql += condition;
            }
            rs = stmt.executeQuery(sql);

            if (StrTool.objNotNull(rs) && rs.next()) {
                sumCount = Integer.parseInt(rs.getString("totalnum"));
            }

            // 关闭相关数据库操作对象
            close();
        } catch (SQLException e) {
            close();
            logger.error(e.getMessage(), e);
        }

        return sumCount;
    }

    /**
     * 关闭结果集、操作句柄和连接
     */
    public void close() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

    }

    /**
     * pg数据库 设置序列的值
     * 方法说明
     * @Date in Jan 11, 2013,5:54:41 PM
     * @param conn
     * @param tableInfo
     * @return
     */
    public boolean execSetSequenceVal(Connection conn, TableInfo tableInfo) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String autoIncColumn = tableInfo.getAutoIncrementedColumn();
            if (StrTool.strNotNull(autoIncColumn)) {
                String tableName = tableInfo.getName();
                String sql = "SELECT setval('" + tableName + "_" + autoIncColumn + "_seq', (select max("
                        + autoIncColumn + ") from " + tableName + "), true);";
                stmt.execute(sql);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                }
            }
        }
        return true;
    }

    /**
     * 启用或者是禁用触发器状态
     * 方法说明
     * @Date in Jan 14, 2013,2:56:41 PM
     * @param conn
     * @param tableName
     * @param isUsed
     * @return
     */
    public boolean execChangeStateTrigger(Connection conn, TableInfo ti, boolean isUsed) {
        Statement stmt = null;
        try {
            if (!StrTool.strEquals("", ti.getAutoIncrementedColumn())) {
                stmt = conn.createStatement();
                String sql = "";
                if (isUsed) {

                    //恢复后 找到最大的id值 将squence.CURRVAL 的值与id值 对齐

                    if (StrTool.strNotNull(ti.getAutoIncrementedColumn()) && StrTool.strNotNull(ti.getSequence())) { //有自增字段 并且 有序列

                        //最大id号
                        int maxId = 0;
                        sql = "select max(" + ti.getAutoIncrementedColumn() + ") as maxid from  " + ti.getName();
                        ResultSet rs0 = stmt.executeQuery(sql);
                        if (rs0.next()) {
                            maxId = rs0.getInt("maxid");
                        }
                        if (rs0 != null) {
                            rs0.close();
                        }

                        if (maxId != 0) {

                            sql = "drop sequence " + ti.getSequence();
                            stmt.execute(sql);
                            sql = "create sequence " + ti.getSequence() + " minvalue 1 " + " start with " + (maxId + 1)
                                    + " increment by 1" + " nomaxvalue " + " nocycle " + " cache 20";
                            stmt.execute(sql);

                            /*
                            //序列值  此处nextval是当前序列值+1  
                            int squenceValue=0;
                            sql="select "+ti.getSequence()+".nextval as squenceValue from  dual";
                            ResultSet rs1=stmt.executeQuery(sql);
                            if(rs1.next()){
                            	squenceValue=rs1.getInt("squenceValue");			                	
                            }	
                            if(rs1!=null){
                            	rs1.close();
                            }
                            
                            if(maxId-squenceValue!=0){ //如果没有对齐id
                                //计算序列步长
                                String incrementValue=String.valueOf(maxId-squenceValue) ;
                                
                                //修改序列步长
                                sql="alter sequence "+ti.getSequence()
                                    +" minvalue 1 "
                                    +" increment by "+incrementValue
                                   // +" nomaxvalue "
                                    +" nocycle "
                                    +" cache 20";
                                stmt.execute(sql);
                                
                                //将原来的序列值 与当前最大id对齐
                                sql="select ftp_domaininfo_id_seq.nextval  from  dual";
                                stmt.execute(sql);
                                
                                //将步长变为1
                                sql="alter sequence "+ti.getSequence()
                                +" minvalue 1"
                                +" increment by 1"
                                //+" nomaxvalue "
                                +" nocycle "
                                +" cache 20";
                                stmt.execute(sql);
                            }
                            */
                        }
                    }

                    // 启用触发器
                    sql = "alter table " + ti.getName() + " enable all triggers";
                    stmt.execute(sql);

                } else {
                    // 禁用触发器
                    sql = "alter table " + ti.getName() + " disable all triggers";
                    stmt.execute(sql);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                }
            }
        }
        return true;
    }

    /**
     * 启用或者是禁用自增长状态  只能同时设置一张表
     * 方法说明
     * @Date in Jan 14, 2013,2:56:41 PM
     * @param conn
     * @param tableName
     * @param isUsed
     * @return
     */
    public boolean execChangeIdentityState(Connection conn, String tableName, boolean isOpen) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();

            if (StrTool.strNotNull(tableName)) {
                String sql = "";
                if (!isOpen) {
                    // 设置为on时可以插入主键 但 必须先设置一次为off 不能直接设置为on
                    stmt.execute("SET IDENTITY_INSERT " + tableName + " OFF");
                    sql = "SET IDENTITY_INSERT " + tableName + " ON";
                } else {
                    // 打开自动增长
                    sql = "SET IDENTITY_INSERT " + tableName + " OFF";
                }
                stmt.execute(sql);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                }
            }
        }
        return true;
    }

    /**
     * 执行insert语句
     * @Date in Jan 14, 2013,2:56:41 PM
     * @param conn
     * @param tableName
     * @param insertSqls
     * @return
     */
    public boolean execSql(Connection conn, String tableName, List<String> insertSqls) {
        boolean result = false;
        try {
            Statement stmt = conn.createStatement();
            //stmt.execute("delete from "+tableName); //先清空数据      	

            //stmt.execute("truncate table "+tableName); //先清空数据 受主外键限制 

            //String[] sqlArrInserts = insertSqls.split("(;\\s*\\r\\n)|(;\\s*\\n)");
            if (!StrTool.listNotNull(insertSqls)) {
                return true;
            }
            String[] sqlArrInserts = insertSqls.toArray(new String[insertSqls.size()]);
            for (int i = 0; i < sqlArrInserts.length; i++) {
                String mysqlStr = sqlArrInserts[i];
                if (!mysqlStr.equals(" ")) {
                    //if (i == sqlArrInserts.length - 1) {
                    //mysqlStr = mysqlStr.replace(";", "");
                    //}
                    stmt.addBatch(mysqlStr);
                }
            }

            stmt.executeBatch();
            result = true;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            result = false;
        }
        return result;
    }

}
