/**
 *Administrator
 */
package com.ft.otp.manager.user.userinfo.action.aide;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.orgunit.orgunit.entity.OrgunitInfo;
import com.ft.otp.manager.orgunit.orgunit.service.IOrgunitInfoServ;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.token.service.ITokenServ;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.manager.user.userinfo.service.IUserInfoServ;
import com.ft.otp.manager.user_token.entity.UserToken;
import com.ft.otp.manager.user_token.service.IUserTokenServ;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.excel.ExcelCss;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.PwdEncTool;
import com.ft.otp.util.tool.StrTool;

/**
 * UserImportAction 辅助类
 * 
 * @Date in May 11, 2011,2:48:46 PM
 * 
 * @author TBM
 */
public class UserImportActionAide {

    // 令牌服务接口
    private ITokenServ tokenServ = (ITokenServ) AppContextMgr.getObject("tokenServ");

    // 用户令牌服务接口
    private IUserTokenServ userTokenServ = (IUserTokenServ) AppContextMgr.getObject("userTokenServ");

    // 组织机构服务接口
    private IOrgunitInfoServ orgunitInfoServ = (IOrgunitInfoServ) AppContextMgr.getObject("orgunitInfoServ");

    private UserBindActionAide bindAide = null;

    public UserImportActionAide() {
        if (null == bindAide) {
            bindAide = new UserBindActionAide();
        }
    }

    /**
     * 生成Excel用户模板导入文件
     * 
     * @Date in May 11, 2011,2:50:00 PM
     * @author TBM
     */
    public void createUserXls(String filePath, String usrAttr, HttpServletRequest request) throws Exception {
        // 新建立一个jxl文件
        OutputStream oStream = new FileOutputStream(filePath);
        // 创建Excel工作薄
        WritableWorkbook workbook = Workbook.createWorkbook(oStream);

        String[] usrAttrArr = getUserAttr(usrAttr, request);
        // 属性个数
        int len = usrAttrArr.length;

        // 创建Excel工作表，创建Sheet
        WritableSheet sheet = workbook.createSheet(Language.getLangStr(request, "user_import_user_file"), 0);

        // 合并单元格，放置标题
        sheet.mergeCells(NumConstant.common_number_0, NumConstant.common_number_0, len - 1, 1);
        Label header = new Label(NumConstant.common_number_0, NumConstant.common_number_0, Language.getLangStr(request,
                "user_import_user_file"), ExcelCss.getHeader());
        // 写入头
        sheet.addCell(header);

        // 创建标题列
        Label label = null;
        for (int i = 0; i < len; i++) {
            String attrName = usrAttrArr[i];
            label = new Label(i, NumConstant.common_number_2, attrName, ExcelCss.getTitle());
            sheet.addCell(label);

            // 设置列宽
            sheet.setColumnView(i, 25);
        }
        // 设置行高
        // sheet.setRowView(2, 400);

        // 写入数据
        workbook.write();
        // 关闭文件
        workbook.close();
        oStream.close();
    }

    /**
     * 资源文件下载
     * 
     * @Date in May 11, 2011,4:57:32 PM
     * @param fileName
     * @param filePath
     * @param response
     * @throws Exception
     * @author TBM
     */
    public void downLoadFile(String fileName, String filePath, HttpServletResponse response) throws Exception {
        // filePath是指欲下载的文件的路径。
        File file = new File(filePath);

        // 以流的形式下载文件。
        InputStream iStream = new BufferedInputStream(new FileInputStream(filePath));
        byte[] buffer = new byte[iStream.available()];
        iStream.read(buffer);
        iStream.close();

        // 清空response
        response.reset();
        // 设置response的Header
        fileName = new String(fileName.getBytes("gbk"), "iso8859-1");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.addHeader("Content-Length", String.valueOf(file.length()));

        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
    }

    /**
     * 返回选中的模板属性关键字
     * 
     * @Date in May 11, 2011,3:22:14 PM
     * @param usrAttr
     * @return
     * @author TBM
     */
    public String[] getUserAttr(String usrAttr, HttpServletRequest request) {
        String[] usrAttrArr = null;
        if (StrTool.strNotNull(usrAttr)) {
            usrAttrArr = new String[1];
            usrAttrArr[NumConstant.common_number_0] = Language.getLangStr(request, "user_info_account");
        }

        String[] arr = usrAttr.split(",");
        int len = arr.length;
        usrAttrArr = new String[len];
        for (int i = 0; i < len; i++) {
            String arrName = "";
            String arrStr = arr[i];
            int num = StrTool.parseInt(arrStr);
            switch (num) {
                case 1:
                    arrName = Language.getLangStr(request, "user_info_account");
                    break;
                case 2:
                    arrName = Language.getLangStr(request, "user_info_document_num");
                    break;
                case 3:
                    arrName = Language.getLangStr(request, "user_info_real_name");
                    break;
                case 4:
                    arrName = Language.getLangStr(request, "user_static_pwd");
                    break;
                case 5:
                    arrName = Language.getLangStr(request, "user_test_phone");
                    break;
                case 6:
                    arrName = Language.getLangStr(request, "common_info_email");
                    break;
                case 44:
                    arrName = Language.getLangStr(request, "common_info_tel");
                    break;
                case 45:
                    arrName = Language.getLangStr(request, "common_info_address");
                    break;
                case 7:
                    arrName = Language.getLangStr(request, "user_info_document_type");
                    break;
                case 8:
                    arrName = Language.getLangStr(request, "domain_orgunit_num");
                    break;
                case 9:
                    arrName = Language.getLangStr(request, "common_title_orgunit");
                    break;
                case 20:
                    arrName = Language.getLangStr(request, "tkn_comm_tknum");
                    break;
                case 25:
                    arrName = Language.getLangStr(request, "user_export_expiration_time");
                    break;
                default:
                    break;
            }

            usrAttrArr[i] = arrName;
        }

        return usrAttrArr;
    }

    /**
     * 多个资源文件同时上传，读取Excel用户信息，存入数据库表中 将失败记录写入 Excel失败记录中
     * 
     * @Date in May 12, 2011,11:24:08 AM
     * @param userImportServ
     * @param userFile
     * @param domainId
     * @param filePath
     * @return Map
     * @throws Exception
     * @author TBM
     */
    public Map<String, Object> fileUpLoad(IUserInfoServ userImportServ, File userFile, String domainId,
            String filePath, HttpServletRequest request) throws Exception {
        Map<String, Object> map = null;
        InputStream iStream = new BufferedInputStream(new FileInputStream(userFile));
        Workbook workbook = Workbook.getWorkbook(iStream);
        // 支持多个工作薄的导入
        int num = workbook.getNumberOfSheets();
        for (int i = 0; i < num; i++) {
            // 第i个sheet
            Sheet sheet = workbook.getSheet(i);
            // 总行数
            int row = sheet.getRows();
            // 总列数
            int column = sheet.getColumns();

            if (row <= NumConstant.common_number_3 || column <= NumConstant.common_number_0) {
                return map;
            }

            int[] attrArr = getExcelAttr(sheet, NumConstant.common_number_2, column, request); // 标题

            // 判断是否是合法文件模板
            boolean vt = isValidateFile(attrArr);
            if (vt) {// 合法
                if (StrTool.arrNotNull(attrArr)) {
                    int domainID = StrTool.strNotNull(domainId) ? StrTool.parseInt(domainId) : NumConstant.common_number_0;
                    map = excelList(sheet, row, column, attrArr, orgunitInfoServ, userImportServ, domainID, filePath,
                            request);
                }
                break;
            } else {
                map = null;// 非法文件
                break;
            }
        }

        return map;
    }

    /**
     * 返回Excel属性名数组
     * 
     * @Date in May 12, 2011,1:27:57 PM
     * @param sheet
     * @param row
     * @param column
     * @return int[]
     * @author TBM
     */
    public int[] getExcelAttr(Sheet sheet, int row, int column, HttpServletRequest request) {
        int[] attrArr = new int[column];
        Cell cell = null;
        for (int j = 0; j < column; j++) {
            cell = sheet.getCell(j, row);
            String attrName = cell.getContents();
            if (StrTool.strEquals(attrName, Language.getLangStr(request, "user_info_account"))) {
                attrArr[j] = 1;
            } else if (StrTool.strEquals(attrName, Language.getLangStr(request, "user_info_document_type"))) {
                attrArr[j] = 7;
            } else if (StrTool.strEquals(attrName, Language.getLangStr(request, "user_info_document_num"))) {
                attrArr[j] = 2;
            } else if (StrTool.strEquals(attrName, Language.getLangStr(request, "user_info_real_name"))) {
                attrArr[j] = 3;
            } else if (StrTool.strEquals(attrName, Language.getLangStr(request, "user_static_pwd"))) {
                attrArr[j] = 4;
            } else if (StrTool.strEquals(attrName, Language.getLangStr(request, "user_test_phone"))) {
                attrArr[j] = 5;
            } else if (StrTool.strEquals(attrName, Language.getLangStr(request, "common_info_email"))) {
                attrArr[j] = 6;
            } else if (StrTool.strEquals(attrName, Language.getLangStr(request, "domain_orgunit_num"))) {
                attrArr[j] = 8;
            } else if (StrTool.strEquals(attrName, Language.getLangStr(request, "common_title_orgunit"))) {
                attrArr[j] = 9;
            } else if (StrTool.strEquals(attrName, Language.getLangStr(request, "tkn_comm_tknum"))) {
                attrArr[j] = 20;
            } else if (StrTool.strEquals(attrName, Language.getLangStr(request, "common_info_tel"))) {
                attrArr[j] = 44;
            } else if (StrTool.strEquals(attrName, Language.getLangStr(request, "common_info_address"))) {
                attrArr[j] = 45;
            }
            // 判断是否有额外的列
            if (attrArr[j] == 0) {
                return null;
            }
        }
        return attrArr;
    }

    /**
     * 验证导入记录中的数据合法性 并记录每一列的失败结果
     * 
     * @param crrentRowNumber
     *            指定行码
     * @param dataSourceSheet
     *            数据源
     * @param column
     *            列数 每一个记录有多个列值
     * @param columnArr
     *            每一列意义码 return
     */
    public Object[] dataValidate(int crrentRowNumber, Sheet dataSourceSheet, int column, int[] columnArr,
            HttpServletRequest request) {
        Object[] results = new Object[NumConstant.common_number_2];
        boolean rowResult = true;// 该记录是否合法数据
        StringBuffer faildReason = new StringBuffer(" "); // 失败的原因
        boolean[] columnResults = new boolean[columnArr.length]; // 每一列的判断结果
        String[] columnFaildReasons = new String[columnArr.length]; // 每一列的失败原因
        Sheet sheet = dataSourceSheet;
        for (int j = 0; j < column; j++) {// 每一列
            String value = sheet.getCell(j, crrentRowNumber).getContents();
            int attrNum = columnArr[j];
            switch (attrNum) {
                case 1:
                    // 验证 用户账号 1-20个字母或数字或字母数字组合 非空
                    boolean column1Result = StrTool.strNotNull(value)
                            && (value.length() < 64 && value.trim().length() > NumConstant.common_number_0)
                            && value.matches("([0-9A-Za-z_.@-]){1,64}$");
                    columnResults[j] = column1Result;// 存储该列的判断结果
                    columnFaildReasons[j] = column1Result ? Language.getLangStr(request, "user_data_legal") : Language
                            .getLangStr(request, "user_vd_import_error_1");
                    break;
                case 7:
                    // 验证 证件类型
                    boolean column7Result = StrTool
                            .strEquals(value, Language.getLangStr(request, "user_info_user_num"));
                    columnResults[j] = column7Result;// 存储该列的判断结果
                    columnFaildReasons[j] = column7Result ? Language.getLangStr(request, "user_data_legal") : Language
                            .getLangStr(request, "user_vd_import_error_2");
                    break;
                case 2:
                    // 验证 证件编号值
                    boolean column2Result = (!StrTool.strNotNull(value)) || value.matches("([A-Za-z0-9\\-]){0,64}$");
                    columnResults[j] = column2Result;// 存储该列的判断结果
                    columnFaildReasons[j] = column2Result ? Language.getLangStr(request, "user_data_legal") : Language
                            .getLangStr(request, "user_vd_import_error_3");
                    break;
                case 3:
                    // 验证真实姓名
                    boolean column3Result = value.length() < 64;
                    columnResults[j] = column3Result;// 存储该列的判断结果
                    columnFaildReasons[j] = column3Result ? Language.getLangStr(request, "user_data_legal") : Language
                            .getLangStr(request, "user_vd_import_error_4");
                    break;
                case 4:
                    // 验证静态密码
                    boolean column4Result = ((value.length() >= NumConstant.common_number_4 && value
                            .length() <= 32))
                            || !StrTool.strNotNull(value);
                    columnResults[j] = column4Result;// 存储该列的判断结果
                    columnFaildReasons[j] = column4Result ? Language.getLangStr(request, "user_data_legal") : Language
                            .getLangStr(request, "user_vd_import_error_5");
                    break;
                case 5:
                    // 验证手机号
                    boolean column5Result = value.matches("^[0-9\\-\\+\\)\\( ]{0,64}$") || !StrTool.strNotNull(value);
                    columnResults[j] = column5Result;// 存储该列的判断结果
                    columnFaildReasons[j] = column5Result ? Language.getLangStr(request, "user_data_legal") : Language
                            .getLangStr(request, "user_vd_import_error_6");
                    break;
                case 6:
                    // 验证邮箱
                    boolean column6Result = value
                            .matches("^(?=\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$).{0,255}$")
                            || !StrTool.strNotNull(value);
                    columnResults[j] = column6Result;// 存储该列的判断结果
                    columnFaildReasons[j] = column6Result ? Language.getLangStr(request, "user_data_legal") : Language
                            .getLangStr(request, "user_vd_import_error_7");
                    break;
                case 44:
                    // 验证电话
                    boolean column44Result = value.matches("([0-9\\-]){0,64}$") || !StrTool.strNotNull(value);

                    columnResults[j] = column44Result;// 存储该列的判断结果
                    columnFaildReasons[j] = column44Result ? Language.getLangStr(request, "user_data_legal") : Language
                            .getLangStr(request, "user_vd_import_error_8");
                    break;
                case 45:
                    // 验证通讯地址
                    boolean column45Result = value.length() < 64;
                    columnResults[j] = column45Result;// 存储该列的判断结果
                    columnFaildReasons[j] = column45Result ? Language.getLangStr(request, "user_data_legal") : Language
                            .getLangStr(request, "user_vd_import_error_9");
                    break;
                case 8:
                    // 验证组织机构编号
                    boolean column8Result = (value.matches("^\\S+$") && value.length() < 31)
                            || !StrTool.strNotNull(value);
                    columnResults[j] = column8Result;// 存储该列的判断结果
                    columnFaildReasons[j] = column8Result ? Language.getLangStr(request, "user_data_legal") : Language
                            .getLangStr(request, "user_vd_import_error_10");
                    break;
                case 9:
                    // 验证组织机构
                    boolean column9Result = (value.matches("^\\S+$") && value.length() < 128)
                            || !StrTool.strNotNull(value);
                    columnResults[j] = column9Result;// 存储该列的判断结果
                    columnFaildReasons[j] = column9Result ? Language.getLangStr(request, "user_data_legal") : Language
                            .getLangStr(request, "user_vd_import_error_11");
                    break;
                case 20:
                    // 验证令牌号
                    boolean column20Result = value.matches("^[a-z0-9A-Z]{0,20}$") || !StrTool.strNotNull(value);
                    columnResults[j] = column20Result;// 存储该列的判断结果
                    columnFaildReasons[j] = column20Result ? Language.getLangStr(request, "user_data_legal") : Language
                            .getLangStr(request, "user_vd_import_error_12");
                    break;
                default:
                    break;
            }
        }

        // 计算计算验证结果
        for (int j = 0; j < column; j++) {
            if (!columnResults[j]) {
                rowResult = false;
                faildReason.append(columnFaildReasons[j] + " ");
            }
        }
        results[NumConstant.common_number_0] = rowResult;
        results[NumConstant.common_number_1] = faildReason.toString();
        return results;
    }

    /**
     * 形成失败记录 加入到 失败列表中 并且将失败记录批量写入到xls中 （每1000条 批量写入一次）
     * 
     * @param sheet
     *            数据源
     * @param crrentRowNumber
     *            当前行
     * @param faildRecordList
     *            失败记录
     * @param column
     *            总列数
     * @param faildReason
     *            每一列的失败原因
     * 
     */
    public void faildRowList(Sheet sheet, int crrentRowNumber, List<Object> faildRecordList, int column,
            String faildReason) {
        String[] columnContents = new String[column + 1]; // 每一条记录的所有列值 包括失败原因
        Cell cell = null;

        for (int j = 0; j < column; j++) {// 每一列 的数据放入结果数组中
            cell = sheet.getCell(j, crrentRowNumber);
            columnContents[j] = cell.getContents();
        }
        // 设置失败的原因
        columnContents[column] = faildReason;

        faildRecordList.add(columnContents); // 整理失败记录列表

    }

    /**
     * 获得该行记录的用户数据
     * 
     * @param sheet
     * @param crrentRowNumber
     * @param column
     * @param attrArr
     * @return UserInfo
     */
    public UserInfo getUserInfoData(Sheet sheet, int crrentRowNumber, int column, int[] attrArr, int domainId) {
        Cell cell = null;
        UserInfo userInfo = new UserInfo();
        for (int j = 0; j < column; j++) {
            cell = sheet.getCell(j, crrentRowNumber);
            String value = cell.getContents();
            int attrNum = attrArr[j];
            userInfo = createUInfo(userInfo, value, attrNum); // excel 中每行数据
            // 创建一个用户 有重复项
        }
        userInfo.setDomainId(domainId);
        userInfo.setRadProfileId(null); // **导入的用
        return userInfo;
    }

    /**
     * 获得指定用户数据中的 用户令牌关系
     * 
     * @param userInfo
     * @return UserToken
     */
    public UserToken getUserTokenData(UserInfo userInfo) {
        UserToken ut = new UserToken();
        ut.setUserId(userInfo.getUserId());
        ut.setToken(userInfo.getToken());
        ut.setDomainId(userInfo.getDomainId());
        ut.setBindTime(DateTool.dateToInt(new Date()));
        if (StrTool.objNotNull(userInfo.getOrgunitId())) {
            ut.setOrguserId(userInfo.getOrgunitId());
        } else {
            ut.setOrguserId(0);
        }
        if (StrTool.objNotNull(userInfo.getNewOrgunitId())) {
            ut.setOrgunitId(userInfo.getNewOrgunitId());
        } else {
            ut.setOrgunitId(0);
        }
        return ut;
    }

    /**
     * 依据组织机构编号和组织机构名称 找到唯一一个织机构对象 否则返回null 注意：不限定某个域下的机构
     * 
     * @param orgunitNumber
     * @param orgunitName
     * @param orgunitInfoServ
     * @return OrgunitInfo
     * @throws BaseException
     */
    public OrgunitInfo getOnlyOrgunit(int domainId, String orgunitNumber, String orgunitName,
            IOrgunitInfoServ orgunitInfoServ) throws BaseException {
        OrgunitInfo oiQuery = new OrgunitInfo();
        if(StrTool.strNotNull(orgunitNumber)){
        	oiQuery.setOrgunitNumber(orgunitNumber);
        }
        oiQuery.setOrgunitName(orgunitName);
        List<?> orgunitList = orgunitInfoServ.queryWholeList(oiQuery);
        if (StrTool.listNotNull(orgunitList)) {
            int size = orgunitList.size();
            if (size > NumConstant.common_number_0) { // 如果 找到该域下的唯一机构
                int num = NumConstant.common_number_0;
                int index = NumConstant.common_number_0;// 返回对象下标
                OrgunitInfo[] orgs = orgunitList.toArray(new OrgunitInfo[size]);
                for (int i = 0; i < size; i++) {
                    if (orgs[i].getDomainId() == domainId) {
                        num++;
                        index = i;
                    }
                }
                if (num == NumConstant.common_number_1) { // 如果唯一
                    return orgs[index];
                } else {
                    return new OrgunitInfo();
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    /**
     * 判断重复项
     * 
     * @param uList
     *            有效的用户列表 即将入库的用户列表 要参与用户重复
     * @param utList
     *            有效的用户令牌关系列表 即将入库的用户列表 要参与用户令牌关系重复
     * @param index
     *            验证项 1用户账号重复 2用户关系重复
     * @param userInfo
     *            参考项 return
     */
    public boolean twinIndex(int index, List<?> uList, List<?> utList, UserInfo userInfo) {
        boolean resultTag = false;// 无效 不重复
        if (index == 1) {
            String userId = userInfo.getUserId();
            int domainId = userInfo.getDomainId();
            Iterator<?> it = uList.iterator();
            while (it.hasNext()) {
                UserInfo ui = (UserInfo) it.next();
                if (StrTool.strEquals(ui.getUserId(), userId) && domainId == ui.getDomainId()) {// 如果已存在
                    resultTag = true;
                }
            }
        }
        if (index == 2) {
            String userId = userInfo.getUserId();
            int domainId = userInfo.getDomainId();
            String token = userInfo.getToken();
            Iterator<?> it = utList.iterator();
            while (it.hasNext()) {
                UserToken ut = (UserToken) it.next();
                if (StrTool.strEquals(ut.getUserId(), userId) && domainId == ut.getDomainId()
                        && StrTool.strEquals(ut.getToken(), token)) {// 如果已存在
                    resultTag = true;
                }
            }
        }

        return resultTag;
    }

    /**
     * 导入文件是否存在多个相同用户且都有令牌，此时用户绑定数目加1
     * 
     * @param uList
     *            有效的用户列表 即将入库的用户列表 要参与用户重复
     * @param utList
     *            有效的用户令牌关系列表 即将入库的用户列表 要参与用户令牌关系重复
     * @param count
     *            用户已绑定令牌个数
     * @param userInfo
     *            参考项 return
     */
    public int twinToIndex(List<?> uList, List<?> utList, UserInfo userInfo, int count) {
        String userId = userInfo.getUserId();
        int domainId = userInfo.getDomainId();
        String token = userInfo.getToken();
        Iterator<?> it = utList.iterator();
        while (it.hasNext()) {
            UserToken ut = (UserToken) it.next();
            if (StrTool.strEquals(ut.getUserId(), userId) && domainId == ut.getDomainId()
                    && StrTool.strNotNull(ut.getToken()) && StrTool.strNotNull(token)) {// 如果已存在
                count++;
                break;
            }
        }
        return count;
    }

    /**
     * 判断改行记录中的（用户信息和用户令牌关系信息）是否可以导入数据库中 返回的结果中包括4个信息
     * 
     * @param crrentRowNumber
     *            指定行码
     * @param dataSourceSheet
     *            数据源
     * @param column
     *            列数 每一个记录有多个列值
     * @param columnArr
     *            每一列意义码
     * @param uList
     *            有效的用户列表 即将入库的用户列表 要参与用户重复
     * @param utList
     *            有效的用户令牌关系列表 即将入库的用户列表 要参与用户令牌关系重复 return Object[]
     *            第一个是用户信息是否可以写入数据库中uTag 第二个是用户令牌信息是否可以写入数据库中utTag
     *            第三个是用户UserInfo对象 其中包括了用户令牌关系 第四个是改行记录失败的原因 第五个是是否绑定了令牌 bindTag
     */
    public Object[] utWritebleInfo(int crrentRowNumber, Sheet dataSourceSheet, int column, int[] columnArr,
            int domainId, IOrgunitInfoServ orgunitInfoServ, IUserInfoServ userInfoServ, ITokenServ tokenServ,
            List<UserInfo> uList, List<UserToken> utList, HttpServletRequest request) throws BaseException {
        Object[] results = new Object[NumConstant.common_number_5];
        UserInfo userInfo = getUserInfoData(dataSourceSheet, crrentRowNumber, column, columnArr, domainId);
        boolean uTag = false;// 用户信息不可写入数据库中
        boolean utTag = false;// 用户令牌关系不可写入数据库中
        StringBuffer faildReason = new StringBuffer(" "); // 失败的原因

        // 机构的合法性 4种合法情况
        boolean orgRight = false; // 机构不合法
        // 第1种 机构编号及机构名称都是空的 视为域
        if ((!StrTool.strNotNull(userInfo.getOrgunitNumber())) && (!StrTool.strNotNull(userInfo.getOrgunitName()))) {
            orgRight = true;
            userInfo.setOrgunitId(null);
        } else {
            // 第2种 （机构编号+机构名称）均非空 能在指定的域下并且现有的数据库中组织机构表中确定唯一的组织机构
            // 第3种 机构编号空 机构名称非空 找到了对应的唯一一个机构名称
            // 第4种 机构编号非空 机构名称空 找到了对应的唯一一个机构编号
            OrgunitInfo orgunitInfoResult = getOnlyOrgunit(userInfo.getDomainId(), userInfo.getOrgunitNumber(),
                    userInfo.getOrgunitName(), orgunitInfoServ);
            if (StrTool.objNotNull(orgunitInfoResult)) { // 如果找到了唯一
            	if(orgunitInfoResult.getOrgunitId() == NumConstant.common_number_0 && 
            			orgunitInfoResult.getDomainId() == NumConstant.common_number_0){
                    faildReason.append(Language.getLangStr(request, "user_vd_import_error_22"));
                    results[NumConstant.common_number_0] = false;
                    results[NumConstant.common_number_1] = false;
                    results[NumConstant.common_number_2] = userInfo;
                    results[NumConstant.common_number_3] = faildReason.toString();
                    results[NumConstant.common_number_4] = false;
                    return results;
            	}else{
            		orgRight = true;
                    userInfo.setOrgunitId(orgunitInfoResult.getOrgunitId());
                    userInfo.setOrgunitNumber(orgunitInfoResult.getOrgunitNumber());
            	}
            } else {
                faildReason.append(Language.getLangStr(request, "user_vd_import_error_13"));
                results[NumConstant.common_number_0] = false;
                results[NumConstant.common_number_1] = false;
                results[NumConstant.common_number_2] = userInfo;
                results[NumConstant.common_number_3] = faildReason.toString();
                results[NumConstant.common_number_4] = false;
                return results;
            }
        }

        // 是否已存在该用户
        boolean isExsit = false; // 不存在
        boolean orgNumExsit = true; // 如果用户存在，判断导入用户的组织机构编号与存在的是否相同
        if (orgRight) {
            Map<String, Boolean> map = userIsExsit(userInfo, isExsit, uTag, faildReason, uList, utList, userInfoServ,
                    request, orgNumExsit);
            isExsit = map.get("isExsit");
            uTag = map.get("uTag");
            orgNumExsit = map.get("orgNumExsit");
        }
        
        if(!orgNumExsit){
            results[NumConstant.common_number_0] = false;
            results[NumConstant.common_number_1] = false;
            results[NumConstant.common_number_2] = userInfo;
            results[NumConstant.common_number_3] = faildReason.toString();
            results[NumConstant.common_number_4] = false;
            return results;
        }

        // 是否绑定了令牌
        boolean bindToken = false;// 没有绑定
        if (StrTool.strNotNull(userInfo.getToken())) {// 绑定了令牌
            bindToken = true;
        } else {
            bindToken = false;
        }

        // 令牌是否有效
        boolean tokenAvailable = false;// 令牌无效
        if (bindToken) {
        	faildReason = new StringBuffer(" ");
            tokenAvailable = tokenAvail(userInfo, tokenAvailable, faildReason, request);

            // 用户存在，令牌无效
            if (tokenAvailable == false && isExsit == true) {
                results[NumConstant.common_number_0] = false;
                results[NumConstant.common_number_1] = false;
                results[NumConstant.common_number_2] = userInfo;
                results[NumConstant.common_number_3] = faildReason.toString();
                results[NumConstant.common_number_4] = true;
                return results;
            }
        } else {
            // 如果用户存在，且没有绑定令牌，直接return
            // 相同的域下不能有相同的用户账号
            if (isExsit == true) {
                results[NumConstant.common_number_0] = false;
                results[NumConstant.common_number_1] = false;
                results[NumConstant.common_number_2] = userInfo;
                results[NumConstant.common_number_3] = faildReason.toString();
                results[NumConstant.common_number_4] = false;
                return results;
            }
        }
        
        // 绑定关系是否已经存在
        boolean utIsExsit = false;// 不存在
        if (bindToken && tokenAvailable && isExsit) { // 绑定了令牌 令牌有效 用户存在
            utIsExsit = userTokenIsEx(userInfo, utIsExsit, faildReason, uList, utList, request);

            // 该用户令牌重复绑定，直接return
            if (utIsExsit == true) {
                results[NumConstant.common_number_0] = false;
                results[NumConstant.common_number_1] = false;
                results[NumConstant.common_number_2] = userInfo;
                results[NumConstant.common_number_3] = faildReason.toString();
                results[NumConstant.common_number_4] = true;
                return results;
            }
        }

        // 绑定令牌次数上限
        boolean userTokenBindCount = false;// 用户绑定令牌次数没有到达上限
        boolean tokenUserBindCount = false;// 令牌绑定用户次数没有到达上限
        // 令牌存在，令牌有效
        if (bindToken && tokenAvailable) {
            Map<String, Boolean> map = tokenUserBind(userInfo, userTokenBindCount, tokenUserBindCount, faildReason,
                    uList, utList, request);
            userTokenBindCount = map.get("userTokenBindCount");
            tokenUserBindCount = map.get("tokenUserBindCount");

            // 用户存在
            // 该用户绑定次数上限或该令牌绑定次数上限，直接return
            if (isExsit) {
                if (userTokenBindCount == true) {
                    results[NumConstant.common_number_0] = false;
                    results[NumConstant.common_number_1] = false;
                    results[NumConstant.common_number_2] = userInfo;
                    results[NumConstant.common_number_3] = faildReason.toString();
                    results[NumConstant.common_number_4] = true;
                    return results;
                }
                if (tokenUserBindCount == true) {
                    results[NumConstant.common_number_0] = false;
                    results[NumConstant.common_number_1] = false;
                    results[NumConstant.common_number_2] = userInfo;
                    results[NumConstant.common_number_3] = faildReason.toString();
                    results[NumConstant.common_number_4] = true;
                    return results;
                }
            }
        }

        // 如果带有令牌、用户令牌的绑定次数均没有达到上限、用户令牌关系没绑定记录； 用户令牌关系可以写入
        if (isExsit && bindToken && !userTokenBindCount && !tokenUserBindCount && !utIsExsit && tokenAvailable) {
            utTag = true;
        }

        // 组织机构合法用户不存在; 用户可以写入
        if (orgRight == true && isExsit == false) {
            uTag = true;
            if (bindToken && !userTokenBindCount && !tokenUserBindCount && !utIsExsit && tokenAvailable) {
                utTag = true;
            }
        }
        results[NumConstant.common_number_0] = uTag;
        results[NumConstant.common_number_1] = utTag;
        results[NumConstant.common_number_2] = userInfo;
        results[NumConstant.common_number_3] = faildReason.toString();
        results[NumConstant.common_number_4] = bindToken;

        return results;
    }

    /**
     * 令牌是否有效
     * 
     * @param userInfo
     * @param tokenAvailable
     * @param faildReason
     * @return
     * @throws BaseException
     */
    public boolean tokenAvail(UserInfo userInfo, boolean tokenAvailable, StringBuffer faildReason,
            HttpServletRequest request) throws BaseException {
        TokenInfo tiQuery = new TokenInfo();
        tiQuery.setToken(userInfo.getToken());
        TokenInfo ti = (TokenInfo) tokenServ.find(tiQuery);
        if (StrTool.objNotNull(ti)) {

            // 未作废令牌
            if (ti.getLogout() == NumConstant.common_number_0) {
                // 暂存一下令牌的组织机构ID
                userInfo.setNewOrgunitId(ti.getOrgunitid());
                // 令牌为默认域下
                if (ti.getOrgunitid() == NumConstant.common_number_0) {

                    // 如果令牌域与用户域相同，用户与令牌可以绑定
                    if (ti.getDomainid() == userInfo.getDomainId()) {
                        tokenAvailable = true;
                    } else {
                        tokenAvailable = false;
                        faildReason.append(Language.getLangStr(request, "user_vd_import_error_15"));
                    }
                } else {

                    // 令牌机构ID不为空
                    // 如果用户与令牌的域ID相同
                    // 因为令牌为域下时，机构ID默认为0，所以强转类型不会出现异常
                    if (ti.getDomainid() == userInfo.getDomainId()) {

                        // 如果导入用户的为默认域下，用户与令牌可以绑定
                        if (userInfo.getOrgunitId() == null) {
                        	if((int) ti.getOrgunitid() != NumConstant.common_number_0){
                        		tokenAvailable = false;
                                faildReason.append(Language.getLangStr(request, "user_vd_import_error_16"));
                        	}else{
                        		tokenAvailable = true;
                        	}
                        } else if (userInfo.getOrgunitId() == (int) ti.getOrgunitid()) { // 如果导入用户与令牌组织机构相同，用户令牌可以绑定
                            tokenAvailable = true;
                        } else {
                            tokenAvailable = false;
                            faildReason.append(Language.getLangStr(request, "user_vd_import_error_16"));
                        }
                    } else {
                        tokenAvailable = false;
                        faildReason.append(Language.getLangStr(request, "user_vd_import_error_15"));
                    }
                }
            } else {
                tokenAvailable = false;
                faildReason.append(Language.getLangStr(request, "user_vd_import_error_17"));
            }
        } else {
            tokenAvailable = false;
            faildReason.append(Language.getLangStr(request, "user_vd_import_error_18"));
        }
        return tokenAvailable;
    }

    /**
     * 判断绑定关系是否已经存在
     * 
     * @param userInfo
     * @param utIsExsit
     * @param faildReason
     * @param uList
     * @param utList
     * @return
     * @throws BaseException
     */
    public boolean userTokenIsEx(UserInfo userInfo, boolean utIsExsit, StringBuffer faildReason, List<UserInfo> uList,
            List<UserToken> utList, HttpServletRequest request) throws BaseException {

        // 才有必要判断绑定关系是否存在
        UserToken utQuery = new UserToken();
        utQuery.setUserId(userInfo.getUserId());
        utQuery.setDomainId(userInfo.getDomainId());
        utQuery.setToken(userInfo.getToken());
        int utCount = userTokenServ.count(utQuery);
        if (utCount > NumConstant.common_number_0) { // 数据库中
            utIsExsit = true;
            faildReason.append(Language.getLangStr(request, "user_vd_import_error_21"));
        } else {
            boolean reBindTag = twinIndex(NumConstant.common_number_2, uList, utList, userInfo);// 在有效的列表中判断
            if (reBindTag) {
                utIsExsit = true;
                faildReason.append(Language.getLangStr(request, "user_vd_import_error_21"));
            }
        }
        return utIsExsit;
    }

    /**
     * 判断是否已存在该用户
     * 
     * @param userInfo
     * @param isExsit
     * @param uTag
     * @param faildReason
     * @param uList
     * @param utList
     * @param userInfoServ
     * @return
     * @throws BaseException
     */
    public Map<String, Boolean> userIsExsit(UserInfo userInfo, boolean isExsit, boolean uTag, StringBuffer faildReason,
            List<UserInfo> uList, List<UserToken> utList, IUserInfoServ userInfoServ, HttpServletRequest request, boolean orgNumExsit)
            throws BaseException {
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        UserInfo uiQuery = new UserInfo();
        uiQuery.setUserId(userInfo.getUserId());
        uiQuery.setDomainId(userInfo.getDomainId());
        UserInfo ui = (UserInfo)userInfoServ.find(uiQuery);
        if (StrTool.objNotNull(ui)) { // 数据库中
        	if(ui.getOrgunitId() == NumConstant.common_number_0){ // 为域下用户
        		if(userInfo.getOrgunitNumber() != null && !"".equals(userInfo.getOrgunitNumber())){
        			orgNumExsit = false;
        		}
        	}else{ // 非域下
        		OrgunitInfo oiQuery = new OrgunitInfo();
                oiQuery.setOrgunitId(ui.getOrgunitId());
                oiQuery = (OrgunitInfo) orgunitInfoServ.find(oiQuery);
                if(!StrTool.strEquals(oiQuery.getOrgunitNumber(), userInfo.getOrgunitNumber())){
                	orgNumExsit = false;
                }
        	}
            isExsit = true;
            faildReason.append(Language.getLangStr(request, "user_vd_import_error_14"));
        } else {
            boolean isValidateTag = twinIndex(NumConstant.common_number_1, uList, utList, userInfo);// 在有效的列表中判断
            if (isValidateTag) { // 如果有重复项
                isExsit = true;
                faildReason.append(Language.getLangStr(request, "user_vd_import_error_14"));
            } else {
                uTag = true;
            }
        }
        map.put("isExsit", isExsit);
        map.put("uTag", uTag);
        map.put("orgNumExsit", orgNumExsit);
        return map;
    }

    /**
     * 判断绑定令牌次数上限
     * 
     * @param userInfo
     * @param userTokenBindCount
     * @param tokenUserBindCount
     * @param faildReason
     * @param uList
     * @param utList
     * @throws BaseException
     */
    public Map<String, Boolean> tokenUserBind(UserInfo userInfo, boolean userTokenBindCount,
            boolean tokenUserBindCount, StringBuffer faildReason, List<UserInfo> uList, List<UserToken> utList,
            HttpServletRequest request) throws BaseException {
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        // 一个用户最多可以绑定令牌数目
        int usrMaxBindTkn = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER,
                ConfConstant.CORE_MAX_BIND_TOKENS));
        // 一个令牌最多可以被用户绑定数目
        int tknMaxBindUsr = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER,
                ConfConstant.CORE_MAX_BIND_USERS));
        String userIds = userInfo.getUserId() + ":" + userInfo.getDomainId() + ":" + userInfo.getOrgunitId();
        int utCount = bindAide.utBindCount(userTokenServ, userIds, null, utList);
        int tuCount = bindAide.utBindCount(userTokenServ, null, userInfo.getToken(), utList);

        // 导入文件是否存在多个相同用户且都有令牌，此时用户绑定数目加1
        utCount = twinToIndex(uList, utList, userInfo, utCount);// 在有效的列表中判断
        if (usrMaxBindTkn <= utCount) {
            userTokenBindCount = true;// 达到上限
            faildReason.append(Language.getLangStr(request, "user_vd_import_error_19"));
        }
        if (tknMaxBindUsr <= tuCount) {
            tokenUserBindCount = true;// 达到上限
            faildReason.append(Language.getLangStr(request, "user_vd_import_error_20"));
        }
        map.put("userTokenBindCount", userTokenBindCount);
        map.put("tokenUserBindCount", tokenUserBindCount);
        return map;
    }

    /**
     * 将Excel表中数据存入List
     * 
     * @Date in May 12, 2011,2:20:25 PM
     * @param sheet
     * @param row
     * @param column
     * @param attrArr
     * @param domainId
     * @return
     * @author TBM
     */
    public Map<String, Object> excelList(Sheet sheet, int row, int column, int[] attrArr,
            IOrgunitInfoServ orgunitInfoServ, IUserInfoServ userImportServ, int domainId, String filePath,
            HttpServletRequest request) throws BaseException {
        Map<String, Object> map = new HashMap<String, Object>();
        List<UserInfo> uAddList = new ArrayList<UserInfo>(); // 该列表
        // 是要导入到数据库中的用户列表
        List<UserToken> utList = new ArrayList<UserToken>(); // 该列表是
        // 要导入到数据库中的用户令牌关系列表
        List<Object> faildRecordList = new ArrayList<Object>(); // 失败的导入记录
        // 添加上失败原因
        // 该列表将来要写入
        // 导入失败文件中

        // 一个Sheet总的用户行数
        int totalUsr = row - 3;
        // 添加用户总数
        int tAddUsr = 0;

        // 绑定成功用户、令牌总数
        int tUsrTknOK = 0;

        // 失败记录数
        int faildRecord = 0;

        int[] result = null;

        for (int i = 3; i < row; i++) { // 每一条记录
            Object[] results = dataValidate(i, sheet, column, attrArr, request); // 待完善
            boolean rowResult = ((Boolean) results[0]).booleanValue(); // 行数据判断总结果

            if (rowResult) {// 如果数据认证合法
                // 验证 并返回有效数据
                Object[] utResults = utWritebleInfo(i, sheet, column, attrArr, domainId, orgunitInfoServ,
                        userImportServ, tokenServ, uAddList, utList, request);

                boolean uTag = ((Boolean) utResults[0]).booleanValue();
                boolean utTag = ((Boolean) utResults[1]).booleanValue();
                UserInfo userInfo = (UserInfo) utResults[2];
                String faildReason = (String) utResults[3];
                boolean bindToken = ((Boolean) utResults[4]).booleanValue();

                if (bindToken) {// 绑定了令牌
                    if ((!uTag) && (!utTag)) {// (1)用户、用户令牌关系都不可写入数据库
                        faildRecord++;
                        faildRowList(sheet, i, faildRecordList, column, faildReason);// 将失败记录 加入到faildRecordList
                    }
                    if (uTag && utTag) {// (2)如果用户、用户令牌关系都可以写入数据库 不写失败原因
                        uAddList.add(userInfo);// 整理用户数据列表
                        UserToken ut = getUserTokenData(userInfo);
                        utList.add(ut); // 整理用户令牌关系数据
                    }
                    if (uTag && !utTag) {// (3)如果用户数据可写入 用户令牌关系不可写入
                        uAddList.add(userInfo);// 整理用户数据列表
                        faildRecord++;
                        faildRowList(sheet, i, faildRecordList, column, faildReason);// 将失败记录 加入到faildRecordList
                    }
                    if (!uTag && utTag) {// (4)如果用户数据不可写入 用户令牌关系可写入 视为成功
                        // 所以不用写失败原因
                        UserToken ut = getUserTokenData(userInfo);
                        utList.add(ut); // 整理用户令牌关系数据
                    }
                } else {
                    if (uTag) {
                        uAddList.add(userInfo);// 整理用户数据列表
                    } else {
                        faildRecord++;
                        faildRowList(sheet, i, faildRecordList, column, faildReason);// 将失败记录 加入到faildRecordList
                    }
                }
            } else { // 如果数据认证不合法
                String faildReason = (String) results[1]; // 该行每列的数据失败原因
                faildRecord++;
                // 将失败记录,加入到faildRecordList
                faildRowList(sheet, i, faildRecordList, column, faildReason);
            }
        } // end for

        // 批量写入数据库
        result = batchOutputDao(userImportServ, uAddList, utList);
        tAddUsr += result[0];
        tUsrTknOK += result[1];
        // 写入失败记录文件中
        faildRecord = faildRecordList.size();
        if (faildRecord > NumConstant.common_number_0) {
            createFaildExcel(faildRecordList, filePath, attrArr, request);
        }

        // 输出结果
        map.put("totalUsr", totalUsr);
        map.put("tAddUsr", tAddUsr);
        map.put("tUsrTknOK", tUsrTknOK);
        map.put("faildRecord", faildRecord);

        return map;
    }

    /**
     * Excel数据批量写入数据库
     * 
     * @Date in May 12, 2011,6:52:41 PM
     * @param uAddList
     * @param uEditList
     * @param utList
     * @param gpList
     * @param ugList
     * @return
     * @author TBM
     */
    private int[] batchOutputDao(IUserInfoServ userImportServ, List<?> uAddList, List<?> utList) {
        int[] result = new int[2];
        // 用户
        if (StrTool.listNotNull(uAddList)) {
            try {
                userImportServ.batchimportUser(uAddList);
                result[0] = uAddList.size();
            } catch (BaseException ex) {
                result[0] = 0;
            }
        }
        // 用户令牌关系
        if (StrTool.listNotNull(utList)) {
            try {
                userTokenServ.addUsrTkn(utList);

                // 令牌绑定后是否迁移至用户所在的组织机构下。0否，1是;
                String TK_BIND_IS_CHANGE_ORG = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER,
                        ConfConstant.TK_BIND_IS_CHANGE_ORG);

                // 判断令牌是否为域下令牌；判断令牌ID是否为NULL
                if (StrTool.strEquals(TK_BIND_IS_CHANGE_ORG, StrConstant.common_number_1)) {
                    for (int i = 0; i < utList.size(); i++) {
                        UserToken userToken = new UserToken();
                        userToken = (UserToken) utList.get(i);

                        // 机构迁移
                        if (userToken.getOrgunitId() != userToken.getOrguserId()) {
                            if (userToken.getOrgunitId() == NumConstant.common_number_0) {
                                TokenInfo ti = new TokenInfo();
                                ti.setToken(userToken.getToken());
                                ti.setDomainid(userToken.getDomainId());
                                if (userToken.getOrguserId() == NumConstant.common_number_0) {
                                    ti.setOrgunitid(null);
                                } else {
                                    ti.setOrgunitid(userToken.getOrguserId());
                                }
                                tokenServ.updateTokenOrg(ti);
                            }
                        }
                    }
                }
                result[1] = utList.size();
            } catch (BaseException ex) {
                result[1] = 0;
            }
        }

        return result;
    }

    /**
     * 生成失败记录文件
     * 
     * @param faildRecordList
     * @param filePath
     * @param usrAttr
     */
    public void createFaildExcel(List<Object> faildRecordList, String filePath, int[] usrAttr,
            HttpServletRequest request) {
        try {
            // 新建立一个jxl文件
            OutputStream oStream = new FileOutputStream(filePath);
            // 创建Excel工作薄
            WritableWorkbook workbook = Workbook.createWorkbook(oStream);

            // 属性个数
            int len = usrAttr.length;

            // 创建Excel工作表，创建Sheet
            WritableSheet sheet = workbook.createSheet(Language.getLangStr(request, "user_detailed_information"), 0);

            // 合并单元格，放置标题
            sheet.mergeCells(0, 0, len, 1);
            Label header = new Label(0, 0, Language.getLangStr(request, "user_detailed_information"), ExcelCss
                    .getHeader());
            // 写入头
            sheet.addCell(header);

            // 创建标题列
            Label label = null;
            for (int i = 0; i < len + 1; i++) {
                if (i == len) {
                    label = new Label(i, 2, Language.getLangStr(request, "user_fail_reason"), ExcelCss.getTitle());
                    sheet.addCell(label);
                } else {
                    String arrName = "";
                    switch (usrAttr[i]) {
                        case 1:
                            arrName = Language.getLangStr(request, "user_info_account");
                            break;
                        case 2:
                            arrName = Language.getLangStr(request, "user_info_document_num");
                            break;
                        case 3:
                            arrName = Language.getLangStr(request, "user_info_real_name");
                            break;
                        case 4:
                            arrName = Language.getLangStr(request, "user_static_pwd");
                            break;
                        case 5:
                            arrName = Language.getLangStr(request, "user_test_phone");
                            break;
                        case 6:
                            arrName = Language.getLangStr(request, "common_info_email");
                            break;
                        case 44:
                            arrName = Language.getLangStr(request, "common_info_tel");
                            break;
                        case 45:
                            arrName = Language.getLangStr(request, "common_info_address");
                            break;
                        case 7:
                            arrName = Language.getLangStr(request, "user_info_document_type");
                            break;
                        case 8:
                            arrName = Language.getLangStr(request, "domain_orgunit_num");// domainid,orgunitid,
                            break;
                        case 9:
                            arrName = Language.getLangStr(request, "common_title_orgunit");// 域名-->组织机构名
                            break;
                        case 20:
                            arrName = Language.getLangStr(request, "tkn_comm_tknum");
                            break;
                        case 25:
                            arrName = Language.getLangStr(request, "user_export_expiration_time");
                            break;
                        default:
                            break;
                    }
                    label = new Label(i, 2, arrName, ExcelCss.getTitle());
                    sheet.addCell(label);
                }
                // 设置列宽
                sheet.setColumnView(i, 25);
            }

            // 数据逐条写入
            for (int k = 0; k < faildRecordList.size(); k++) {
                String[] colValues = (String[]) faildRecordList.get(k);
                for (int j = 0; j < len + 1; j++) {
                    label = new Label(j, 3 + k, colValues[j], ExcelCss.getNormolCell());
                    sheet.addCell(label);
                }
            }

            // 设置行高
            // sheet.setRowView(2, 400);

            // 写入数据
            workbook.write();
            // 关闭文件
            workbook.close();
            oStream.close();
        } catch (Exception ex) {

        }
    }

    /**
     * 用户数据准确性校验
     * 
     * @Date in May 12, 2011,3:04:12 PM
     * @return
     */
    public int uInfoCheck(String userId, IUserInfoServ userImportServ) throws BaseException {
        // 用户名检查，不存在添加，存在更新数据，并且绑定令牌，如果令牌已经绑定则跳过
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        int usrCount = userImportServ.countBind(userInfo);
        if (usrCount > 0) {
            return NumConstant.common_number_1;
        }

        return NumConstant.common_number_0;
    }

    public void importUser() {

    }

    /**
     * 将Excel表中属性值存入UserInfo实体对象
     * 
     * @Date in May 12, 2011,2:21:03 PM
     * @param userInfo
     * @param value
     * @param attrNum
     * @return UserInfo
     */
    public UserInfo createUInfo(UserInfo userInfo, String value, int attrNum) {
        if (attrNum == 1) {
            userInfo.setUserId(value);
        } else if (attrNum == 7) {
            userInfo.setPapersType(0);// 证件类型 ： 员工编号 0
        } else if (attrNum == 2) {
            userInfo.setPapersNumber(value);
        } else if (attrNum == 3) {
            userInfo.setRealName(value);
        } else if (attrNum == 4) {
            // 对PWD进行加密处理
            userInfo.setPwd(PwdEncTool.encPwd(value));
        } else if (attrNum == 5) {
            userInfo.setCellPhone(value);
        } else if (attrNum == 6) {
            userInfo.setEmail(value);
        } else if (attrNum == 44) {
            userInfo.setTel(value);
        } else if (attrNum == 45) {
            userInfo.setAddress(value);
        } else if (attrNum == 8) {
            userInfo.setOrgunitNumber(value);
        } else if (attrNum == 9) {
            userInfo.setOrgunitName(value);
        } else if (attrNum == 20) {
            userInfo.setToken(value);
        } else if (attrNum == 25) {
            // 过期时间需要处理
            int expireTime = 0;
            if (!StrTool.strEquals(value, "")) {
                expireTime = DateTool.dateToInt(DateTool.strToDateFull(value));
            }
            userInfo.setExpireTime(expireTime);
        }

        return userInfo;
    }

    /**
     * 合法的模板文件判断
     * 
     * @param values
     * @return boolean
     */
    public boolean isValidateFile(int[] values) {
        if (values != null) {
            int sum = 0;
            int length = values.length;
            for (int i = 0; i < length; i++) {
                if (values[i] == NumConstant.common_number_1) {// 有用户id 字段
                    sum = sum + values[i];
                }
              //  if (values[i] == NumConstant.common_number_9) {// 有组织机构 字段
              //      sum = sum + values[i];
              //  }
            }
            if (sum == 1) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public static void main(String[] args) {
        // String filePath = "C://1305169172204.xls";
        // try {
        // fileUpLoad(filePath);
        // } catch (Exception ex) {
        // ex.printStackTrace();
        // }
    }

}
