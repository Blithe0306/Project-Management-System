/**
 *Administrator
 */
package com.ft.otp.manager.user.userinfo.action.aide;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
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
import com.ft.otp.manager.token.action.aide.TokenActionAide;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.token.service.ITokenServ;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.manager.user.userinfo.service.IUserInfoServ;
import com.ft.otp.manager.user_token.entity.UserToken;
import com.ft.otp.manager.user_token.service.IUserTokenServ;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.excel.ExcelCss;
import com.ft.otp.util.tool.StrTool;

/**
 * UserUnBindAction 辅助类
 *
 * @Date in May 16, 2011,4:23:09 PM
 *
 * @author TBM
 */
public class UserUnBindActionAide {

    //用户令牌服务接口
    private IUserTokenServ userTokenServ = (IUserTokenServ) AppContextMgr.getObject("userTokenServ");
    private IUserInfoServ userInfoServ = (IUserInfoServ) AppContextMgr.getObject("userInfoServ");
    private ITokenServ tokenServ = (ITokenServ) AppContextMgr.getObject("tokenServ");

    private UserBindActionAide bindAide = null;

    public UserUnBindActionAide() {
        if (null == bindAide) {
            bindAide = new UserBindActionAide();
        }
    }

    /**
     * 多个资源文件同时上传，读取Excel用户、令牌信息，执行解绑操作
     * @param filePath
     * @throws Exception
     */
    public Map<String, Object> fileUpLoad(File userFile, String filePath, HttpServletRequest request, int[] attrArr) throws Exception {
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
            if (StrTool.arrNotNull(attrArr)) {
                map = excelList(sheet, row, column, attrArr, filePath, request);
            }
           
        }
        return map;
    }
    
    /**
     * 解绑数据信息检查
     * @param sheet
     * @param row
     * @param column
     * @param attrArr
     * @return
     * @throws Exception 
     */
    public Map<String, Object> excelList(Sheet sheet, int row, int column, int[] attrArr,
             String filePath, HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        // 是要导入到数据库中的用户列表
        List<Object> utList = new ArrayList<Object>(); // 该列表是
        List<String> keyList = new ArrayList<String>();
        // 要导入到数据库中的用户令牌关系列表
        List<Object> faildRecordList = new ArrayList<Object>(); // 失败的导入记录
        // 添加上失败原因
        // 该列表将来要写入
        // 导入失败文件中

        // 一个Sheet总的用户行数
        int totalUsr = row - 3;
        // 解绑用户总数
        int tBindUsr = 0;

        // 失败记录数
        int faildRecord = 0;

        int[] result = null;

        for (int i = 3; i < row; i++) { // 每一条记录
            Object[] results = dataValidate(i, sheet, column, attrArr, request); // 待完善
            boolean rowResult = ((Boolean) results[0]).booleanValue(); // 行数据判断总结果

            if (rowResult) {// 如果数据认证合法
                // 验证 并返回有效数据
                Object[] utResults = utWritebleInfo(i, sheet, column, attrArr, utList, request);
                boolean utTag = ((Boolean) utResults[0]).booleanValue();
                UserInfo userInfo = (UserInfo) utResults[2];
                String faildReason = (String) utResults[1];
                if (!utTag) {// (1)用户令牌关系都不可更新数据库
                    faildRecord++;
                    faildRowList(sheet, i, faildRecordList, column, faildReason);// 将失败记录 加入到faildRecordList
                }else{
                    UserToken ut = getUserTokenData(userInfo);
                    keyList.add(userInfo.getToken());
                    utList.add(ut); // 整理用户令牌关系数据
                }
            } else { // 如果数据认证不合法
                String faildReason = (String) results[1]; // 该行每列的数据失败原因
                faildRecord++;
                // 将失败记录,加入到faildRecordList
                faildRowList(sheet, i, faildRecordList, column, faildReason);
            }
        } // end for

        // 批量写入数据库
        result = batchOutputDao(utList,keyList);
        tBindUsr += result[0];
        // 写入失败记录文件中
        faildRecord = faildRecordList.size();
        if (faildRecord > NumConstant.common_number_0) {
            createFaildExcel(faildRecordList, filePath, attrArr, request);
        }

        // 输出结果
        map.put("totalUsr", totalUsr);
        map.put("tBindUsr", tBindUsr);
        map.put("faildRecord", faildRecord);

        return map;
    }
    
    /**
     * 将Excel表中属性值存入UserInfo实体对象
     * @param userInfo
     * @param value
     * @param attrNum
     * @return UserInfo
     */
    public UserInfo getUserInfoData(Sheet sheet, int crrentRowNumber, int column, int[] attrArr) {
        Cell cell = null;
        UserInfo userInfo = new UserInfo();
        for (int j = 0; j < column; j++) {
            cell = sheet.getCell(j, crrentRowNumber);
            String value = cell.getContents();
            int attrNum = attrArr[j];
            userInfo = createUInfo(userInfo, value, attrNum); // excel 中每行数据
            // 创建一个用户 有重复项
        }
        userInfo.setRadProfileId(null); // **导入的用
        return userInfo;
    }
    
    public UserInfo createUInfo(UserInfo userInfo, String value, int attrNum) {
        if (attrNum == 1) {
            userInfo.setUserId(value);
        } else if (attrNum == 20) {
            userInfo.setToken(value);
        }
        return userInfo;
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
    public Map<String, Boolean> userIsExsit(UserInfo userInfo, boolean isExsit, StringBuffer faildReason,
    		List<Object> utList, HttpServletRequest request)
            throws BaseException {
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        UserInfo uiQuery = new UserInfo();
        uiQuery.setUserId(userInfo.getUserId());
        uiQuery.setDomainId(userInfo.getDomainId());
        Object ui = userInfoServ.find(uiQuery);
        if (StrTool.objNotNull(ui)) { // 数据库中存在
            isExsit = true;
        } else {
            isExsit = false;
            faildReason.append(Language.getLangStr(request, "user_vd_import_error_23"));
        }
        map.put("isExsit", isExsit);
        return map;
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
        UserToken tiQuery = new UserToken();
        tiQuery.setToken(userInfo.getToken());
        tiQuery.setUserId(userInfo.getUserId());
        UserToken ti = (UserToken) userTokenServ.find(tiQuery);
        if (StrTool.objNotNull(ti)) {
        	tokenAvailable = true;
        } else {
            tokenAvailable = false;
            faildReason.append(Language.getLangStr(request, "user_vd_import_error_24"));
        }
        return tokenAvailable;
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
     *            第1个是用户令牌信息是否可以写入数据库中utTag
     *            第3个是用户UserInfo对象 其中包括了用户令牌关系 第2个是改行记录失败的原因 
     */
    public Object[] utWritebleInfo(int crrentRowNumber, Sheet dataSourceSheet, int column, int[] columnArr,
    		List<Object> utList, HttpServletRequest request) throws BaseException {
        Object[] results = new Object[NumConstant.common_number_3];
        boolean utTag = false;// 用户令牌关系不可写入数据库中
        StringBuffer faildReason = new StringBuffer(" "); // 失败的原因
        UserInfo userInfo = getUserInfoData(dataSourceSheet, crrentRowNumber, column, columnArr);
        
        // 是否已存在该用户
        boolean isExsit = true; // 存在
        Map<String, Boolean> map = userIsExsit(userInfo, isExsit, faildReason, utList, request);
        isExsit = map.get("isExsit");
        if(!isExsit){
        	results[NumConstant.common_number_0] = false;
            results[NumConstant.common_number_2] = userInfo;
            results[NumConstant.common_number_1] = faildReason.toString();
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
            tokenAvailable = tokenAvail(userInfo, tokenAvailable, faildReason, request);

            // 用户存在，令牌无效
            if (tokenAvailable == false && isExsit == true) {
                results[NumConstant.common_number_0] = false;
                results[NumConstant.common_number_2] = userInfo;
                results[NumConstant.common_number_1] = faildReason.toString();
                return results;
            }
        } else {
            // 如果用户存在，且没有绑定令牌，直接return
            if (isExsit) {
            	faildReason.append(Language.getLangStr(request, "user_vd_import_error_25"));
                results[NumConstant.common_number_0] = false;
                results[NumConstant.common_number_2] = userInfo;
                results[NumConstant.common_number_1] = faildReason.toString();
                return results;
            }
        }

        // 如果带有令牌、用户令牌的绑定次数均没有达到上限、用户令牌关系没绑定记录； 用户令牌关系可以写入
        if (isExsit && bindToken && tokenAvailable) {
            utTag = true;
        }

        results[NumConstant.common_number_0] = utTag;
        results[NumConstant.common_number_2] = userInfo;
        results[NumConstant.common_number_1] = faildReason.toString();

        return results;
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
     * 获得指定用户数据中的 用户令牌关系
     * 
     * @param userInfo
     * @return UserToken
     * @throws BaseException 
     */
    public UserToken getUserTokenData(UserInfo userInfo) throws BaseException {
        UserToken ut = new UserToken();
        ut.setUserId(userInfo.getUserId());
        ut.setToken(userInfo.getToken());
        
        // 取出解绑用户的域ID
        UserInfo user = new UserInfo();
        user.setUserId(userInfo.getUserId());
        user = (UserInfo)userInfoServ.find(user);
        if(StrTool.objNotNull(user)){
        	ut.setDomainId(user.getDomainId());
        }else{
        	ut.setDomainId(null);
        }
        return ut;
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
     * Excel数据批量写入数据库
     * @param uAddList
     * @param keyList
     * @return
     * @author TBM
     */
    private int[] batchOutputDao( List<Object> utList, List<String> keyList) throws Exception {
        int[] result = new int[1];
        // 用户令牌关系
        if (StrTool.listNotNull(utList)) {
            try {
            	// 令牌解绑，是否将该令牌放回域中
                updateBind(utList);

                // 令牌解绑后， 被解绑令牌是否停用
                updateState(keyList);

                //批量解绑数据
                userTokenServ.batchUnBindUT(utList);
                result[0] = utList.size();
            } catch (BaseException ex) {
                result[0] = 0;
            }
        }

        return result;
    }
    
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
                        case 20:
                            arrName = Language.getLangStr(request, "tkn_comm_tknum");
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

            // 写入数据
            workbook.write();
            // 关闭文件
            workbook.close();
            oStream.close();
        } catch (Exception ex) {

        }
    }

    /**
     * 令牌解绑后，将该令牌放回域中
     * @param userInfo
     * @throws Exception
     */
    public void updateBind(List<Object> utList) throws Exception {

        // 令牌解绑后是否回到所属的域中。0，否，1是
        if (StrTool.strEquals(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER,
                ConfConstant.TK_UNBIND_IS_CHANGE_ORG), StrConstant.common_number_1)) {
            UserToken userToken = null;
            UserToken userIn = null;
            // 取出LIST中用户与令牌的信息
            if (StrTool.listNotNull(utList)) {
                for (int k = 0; k < utList.size(); k++) {
                    userToken = (UserToken) utList.get(k);

                    // 查出所有令牌与用户之间关系数据
                    List<?> userList = userTokenServ.selObjs(userToken);
                    if (StrTool.listNotNull(userList)) {
                        for (int i = 0; i < userList.size(); i++) {
                            userIn = (UserToken) userList.get(i);

                            // 判断令牌是否是管理员绑定，管理员绑定时isNullDomain：0代表无管理员绑定；1代表有管理员绑定
                            if (userIn.getIsNullDomain() == 0) {
                                TokenInfo ti = new TokenInfo();
                                ti.setToken(userIn.getToken());
                                ti.setDomainid(userIn.getDomainId());
                                // 令牌放回域中，机构ID置为NULL
                                ti.setOrgunitid(null);
                                tokenServ.updateTokenOrg(ti);
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 判断解绑令牌，被解绑令牌是否停用
     * 
     */
    public void updateState(List<String> tokenIds) throws Exception {

        // 判断解绑令牌，被解绑令牌是否停用，1：是；0：否
        int rstate = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER,
                ConfConstant.CORE_UNBIND_STATE_SELECT));
        if (rstate == NumConstant.common_number_1) {
            for (int i = 0; i < tokenIds.size(); i++) {
                String token = tokenIds.get(i);
                UserToken userToken = new UserToken();
                userToken.setToken(token);

                // 查看此令牌是否被其它用户绑定
                int count = userTokenServ.count(userToken);

                // 等于1代表只有一个用户绑定
                if (count == NumConstant.common_number_1) {
                    TokenInfo tokenInfo = new TokenInfo();
                    tokenInfo.setToken(token);
                    tokenInfo = TokenActionAide.getTokenInfo(tokenInfo, 1);
                    tokenServ.updateTokenState(tokenInfo);
                }
            }
        }
    }

}
