/**
 *Administrator
 */
package com.ft.otp.manager.user.userinfo.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.orgunit.domain.service.IDomainInfoServ;
import com.ft.otp.manager.orgunit.orgunit.service.IOrgunitInfoServ;
import com.ft.otp.manager.token.action.aide.TokenActionAide;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.token.service.ITokenServ;
import com.ft.otp.manager.user.userinfo.action.aide.UInfoActionAide;
import com.ft.otp.manager.user.userinfo.action.aide.UserBindActionAide;
import com.ft.otp.manager.user.userinfo.action.aide.UserImportActionAide;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.manager.user.userinfo.form.BindUTQForm;
import com.ft.otp.manager.user.userinfo.service.IUserInfoServ;
import com.ft.otp.manager.user_token.entity.UserToken;
import com.ft.otp.manager.user_token.service.IUserTokenServ;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.pdf.UserTnkPdfUtil;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户绑定令牌业务控制Action
 *
 * @Date in May 4, 2011,1:48:09 PM
 *
 * @author TBM
 */
public class UserBindAction extends BaseAction {

    private static final long serialVersionUID = -3724405181297496193L;

    private Logger logger = Logger.getLogger(UserBindAction.class);

    private IUserInfoServ userBindServ = null;
    //令牌的辅助类
    private TokenActionAide aideToken = new TokenActionAide();

    private static List<?> utList;
    //令牌服务接口
    private ITokenServ tokenServ = (ITokenServ) AppContextMgr.getObject("tokenServ");
    private IDomainInfoServ domainInfoServ = (IDomainInfoServ) AppContextMgr.getObject("domainInfoServ");
    private IOrgunitInfoServ orgunitInfoServ = (IOrgunitInfoServ) AppContextMgr.getObject("orgunitInfoServ");
    private UInfoActionAide uInfoAide = new UInfoActionAide();

    //用户令牌服务接口
    private IUserTokenServ userTokenServ = (IUserTokenServ) AppContextMgr.getObject("userTokenServ");
    private UserBindActionAide aide = new UserBindActionAide();
    private UserImportActionAide importActionAide = new UserImportActionAide();
    private BindUTQForm queryForm;

    /**
     * @return the userBindServ
     */
    public IUserInfoServ getUserBindServ() {
        return userBindServ;
    }

    /**
     * @param userBindServ the userBindServ to set
     */
    public void setUserBindServ(IUserInfoServ userBindServ) {
        this.userBindServ = userBindServ;
    }

    /**
     * @return the queryForm
     */
    public BindUTQForm getQueryForm() {
        return queryForm;
    }

    /**
     * @param queryForm the queryForm to set
     */
    public void setQueryForm(BindUTQForm queryForm) {
        this.queryForm = queryForm;
    }

    /**
     * 取出QueryForm中的实体
     * @Date in May 5, 2011,6:30:40 PM
     * @param agentQueryForm
     * @return
     */
    private UserInfo getUserInfo(BindUTQForm queryForm) {
        UserInfo uInfo = new UserInfo();
        if (StrTool.objNotNull(queryForm)) {
            uInfo = queryForm.getUserInfo();
            if (queryForm.getDOrgunitId().indexOf(",") != -1) { //这个条件一定成立

                // 定义一个数组，目的：封装用户的机构ID，便于多个用户查询与机构相同的令牌
                int orgunitids[] = new int[queryForm.getDOrgunitId().split(",").length];
                String domainids[] = new String[queryForm.getDOrgunitId().split(",").length];
                boolean flag = true;
                for (int i = 0; i < queryForm.getDOrgunitId().split(",").length; i++) {

                    // 取出传过来的用户机构ID号放到自定义的数组里
                    domainids[i] = queryForm.getDOrgunitId().split(",")[i].split(":")[0];
                    orgunitids[i] = Integer.parseInt(queryForm.getDOrgunitId().split(",")[i].split(":")[1]);

                    // 组织机构ID集合中是否有等于0，意味着是否为域下
                    if (flag
                            && Integer.parseInt(queryForm.getDOrgunitId().split(",")[i].split(":")[1]) == NumConstant.common_number_0) {
                        flag = false;
                    }
                }

                // 如果非域下机构
                if (flag) {
                    uInfo.setOrgFlag(2); // 拼接相关SQL
                }
                uInfo.setOrgunitIds(orgunitids);

                // 取出传过来的用户的DOMAINID，用户域相同
                if (StrTool.strNotNull(queryForm.getDOrgunitId().split(",")[0].split(":")[0])) {
                    uInfo.setDomainId(Integer.parseInt(queryForm.getDOrgunitId().split(",")[0].split(":")[0]));
                }

            } else {
                uInfo.setDomainId(0);
                uInfo.setOrgunitId(0); //目的是不让其参加 查询条件判断
            }
            uInfo.setUsbind(queryForm.getUsbindState());
        } else {
            uInfo.setUserId(null);
            uInfo.setRealName(null);
            uInfo.setToken(null);
            uInfo.setDomainId(0);
            uInfo.setOrgunitId(0);//0是不参与条件匹配

        }
        String curLoginUserId = (String) super.getCurLoginUser();//获得当前管理员id号
        String curLoginUserRoleMark = (String) super.getCurLoginUserRole(); //当前管理员所拥有的角色 对应角色表中的rolemark字段
        if (StrTool.strEquals(curLoginUserRoleMark, "ADMIN")) {//如果是超级管理员
            uInfo.setIsFliterTag(null); //不根据组织机构顾虑

        } else {
            uInfo.setIsFliterTag(1); //根据组织机构顾虑
            uInfo.setCurrentAdminId(curLoginUserId);
        }
        return uInfo;
    }

    /**
     * 取出QueryForm中的实体
     * @Date in May 5, 2011,6:30:40 PM
     * @param agentQueryForm
     * @return
     */
    private TokenInfo getTokenInfo(BindUTQForm queryForm) {
        TokenInfo tokenInfo = new TokenInfo();
        if (StrTool.objNotNull(queryForm)) { // 不是空的 证明是查询  
            tokenInfo = queryForm.getTokenInfo();
            tokenInfo.setBind(queryForm.getBindState()); // 给令牌状态赋值
            tokenInfo.setLogout(NumConstant.common_number_0); // 未作废
            if (queryForm.getDOrgunitId().indexOf(",") != -1) { //这个条件一定成立

                // 定义一个数组，目的：封装用户的机构ID，便于多个用户查询与机构相同的令牌
                int orgunitids[] = new int[queryForm.getDOrgunitId().split(",").length];
                for (int i = 0; i < queryForm.getDOrgunitId().split(",").length; i++) {

                    // 取出传过来的用户机构ID号放到自定义的数组里
                    orgunitids[i] = StrTool.parseInt(queryForm.getDOrgunitId().split(",")[i].split(":")[1]);
                }
                tokenInfo.setOrgunitIds(orgunitids);

                // 取出传过来的用户的DOMAINID，用户域相同
                if (StrTool.strNotNull(queryForm.getDOrgunitId().split(",")[0].split(":")[0])) {
                    tokenInfo.setDomainid(Integer.parseInt(queryForm.getDOrgunitId().split(",")[0].split(":")[0]));
                }
            } else {
                tokenInfo.setDomainid(null);
            }
        } else {
            tokenInfo.setDomainid(null);
        }

        String curLoginUserId = (String) super.getCurLoginUser();//获得当前管理员id号
        String curLoginUserRoleMark = (String) super.getCurLoginUserRole(); //当前管理员所拥有的角色 对应角色表中的rolemark字段
        if (StrTool.strEquals(curLoginUserRoleMark, "ADMIN")) {//如果是超级管理员
            tokenInfo.setIsFliterTag(null); //不根据组织机构顾虑

        } else {
            tokenInfo.setIsFliterTag(1); //根据组织机构顾虑
            tokenInfo.setCurrentAdminId(curLoginUserId);
        }
        tokenInfo.setOrgFlag(1);// orgFlag的值决定拼接哪些SQL
        return tokenInfo;
    }

    /**
     * 用户查询
     * @Date in May 5, 2011,7:10:33 PM
     * @return
     */
    public String usrQuery() {
        if (isNeedClearForm()) {
            queryForm = null;
        }
        try {
            String operate = request.getParameter("operType");
            
            // 初始进入批量绑定，不让查询用户信息
            if(StrTool.strEquals(operate, "1") && !StrTool.objNotNull(queryForm)){
            	return null;
            }
            PageArgument pageArg = pageArgument();
            List<?> uiList = query(pageArg);
            //返回JSON格式查询数据
            String jsonStr = JsonTool.getJsonFromList(pageArg.getTotalRow(), uiList, pageArg);
            setResponseWrite(jsonStr);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 处理用户数据查询
     * @Date in Apr 20, 2011,12:04:36 PM
     * @param pageArg
     */
    private List<?> query(PageArgument pageArg) {
        List<?> uiList = null;
        try {
            uiList = userBindServ.queryBind(getUserInfo(queryForm), pageArg);

            //设置该用户列表中的 域对象、机构对象、域组织机构id串、域组织机构名字串 
            uiList = setDOList(uiList, domainInfoServ, orgunitInfoServ);

        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return uiList;
    }

    /**
     * 设置该用户列表中的 域对象、机构对象、域组织机构id串、域组织机构名字串
     * @Date in Apr 28, 2011,8:23:44 AM
     * @param uiList
     * @param utList
     * @return
     */
    public List<?> setDOList(List<?> uiList, IDomainInfoServ domainInfoServ, IOrgunitInfoServ orgunitInfoServ) {
        List<UserInfo> uInfoList = new ArrayList<UserInfo>();
        Iterator<?> it = uiList.iterator();
        while (it.hasNext()) {
            UserInfo ui = (UserInfo) it.next();
            UserInfo userInfo = uInfoAide.setDOstr(ui);
            uInfoList.add(userInfo);
        }

        return uInfoList;
    }

    /**
     * 用户、令牌信息分页处理
     */
    public String page() {
        String appMark = request.getParameter("appMark");
        PageArgument pageArg = getArgument(request, 0);

        if (StrTool.strEquals(appMark, "1")) {
            query2(pageArg);
        } else {
            query(pageArg);
        }

        return null;
    }

    /**
     * 分页统计
     * 分页处理
     * @Date in May 4, 2011,2:06:04 PM
     * @return
     * @throws BaseException
     */
    private PageArgument pageArgument() throws BaseException {
        int totalRow = userBindServ.countBind(getUserInfo(queryForm));
        PageArgument pageArg = getArgument(totalRow);

        return pageArg;
    }

    /**
     * 令牌查询
     * @Date in May 5, 2011,11:36:33 AM
     * @return
     */
    public String tknQuery() {
        //        String token = request.getParameter("token");
        //        String producttype = request.getParameter("producttype");
        //        String pType = request.getParameter("pType");
        //        String bindState = request.getParameter("bindState");
        //        String enableState = request.getParameter("enableState");
        //        String lockedState = request.getParameter("lockedState");
        //        String lostState = request.getParameter("lostState");
        //
        //        this.createQForm();
        //        if (StrTool.strNotNull(token)) {
        //            queryForm.setToken(token);
        //        }
        //        if (StrTool.strNotNull(producttype)) {
        //            queryForm.setProducttype(StrTool.parseInt(producttype));
        //        }
        //        if (StrTool.strNotNull(pType)) {
        //            queryForm.setPhysicaltype(StrTool.parseInt(pType));
        //        }
        //        if (StrTool.strNotNull(bindState)) {
        //            queryForm.setBindState(StrTool.parseInt(bindState));
        //        }
        //        if (StrTool.strNotNull(enableState)) {
        //            queryForm.setEnableState(StrTool.parseInt(enableState));
        //        }
        //        if (StrTool.strNotNull(lockedState)) {
        //            queryForm.setLockedState(StrTool.parseInt(lockedState));
        //        }
        //        if (StrTool.strNotNull(lostState)) {
        //            queryForm.setLostState(StrTool.parseInt(lostState));
        //        }
        if (isNeedClearForm()) {
            queryForm = null;
        }
        try {
            String dOrgunitId = request.getParameter("dOrgunitId");
            String operate = request.getParameter("operType");
            if (StrTool.strNotNull(dOrgunitId)) {
                queryForm = new BindUTQForm();
                queryForm.setDOrgunitId(dOrgunitId);
            }
            
            // 初始进入批量绑定，不让查询令牌信息
            if(StrTool.strEquals(operate, "1") && !StrTool.objNotNull(queryForm)){
            	return null;
            }
            int total = tokenServ.countBC(getTokenInfo(queryForm)); //统计数量
            PageArgument pageArg = getArgument(total);
            pageArg.setCurPage(getPage());
            pageArg.setPageSize(getPagesize());
            List<TokenInfo> tokenList = (List<TokenInfo>) tokenServ.queryBC(getTokenInfo(queryForm), pageArg);

            //Start 插入组织机构名构
            for (TokenInfo tokenInfo : tokenList) {
                tokenInfo = aideToken.setDomainAndOrgunitName(tokenInfo);
            }
            //End 插入组织机构名构
            //返回JSON格式查询数据
            String jsonStr = JsonTool.getJsonFromList(pageArg.getTotalRow(), tokenList, pageArg);
            setResponseWrite(jsonStr);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 处理令牌数据查询
     * @Date in May 5, 2011,11:36:50 AM
     * @param pageArg
     */
    private List<?> query2(PageArgument pageArg) {
        List<?> tokenList = null;
        try {
            tokenList = tokenServ.queryBC(getTokenInfo(queryForm), pageArg);

        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        return tokenList;
    }

    /**
     * 批量绑定用户与令牌
     * @throws IOException 
     * @Date in May 5, 2011,7:32:58 PM
     */
    public String batchBindUT() throws IOException {
        String[] usrIdArr = null; //这里包含了 userId:domainId:orgunitId,userId:domainId:orgunitId,形式
        String[] tokenArr = null;
        String bindTypeStr = request.getParameter("bindType");
        int bindType = StrTool.parseInt(bindTypeStr);
        /**
         * 两种操作方式
         * 0 表示批量操作checkBox选中的数据
         * 1 表示批量操作根据条件查询所得的数据
         */
        String usrOperSel = request.getParameter("usrOperSel");//用户操作方式
        String tknOperSel = request.getParameter("tknOperSel");//令牌操作方式

        //checkBox选中的数据
        String usrBoxKey = request.getParameter("userArr");//这里包含了 userId:domainId:orgunitId,userId:domainId:orgunitId,形式
        String tknBoxKey = request.getParameter("tokenArr");

        try {
            // 0 方式一，用户
            if (StrTool.strNotNull(usrOperSel) && StrTool.strEquals(usrOperSel, StrConstant.common_number_0)) {
                if (StrTool.strNotNull(usrBoxKey)) {
                    usrBoxKey = uInfoAide.replaceUserId(usrBoxKey);
                    usrIdArr = usrBoxKey.split(",");
                }
            } else {
                // 1 方式二,操作对象:本次查询所有记录
                UserInfo uiQuery = getUserInfo(queryForm);
                uiQuery.setUpPageTag(0);//不分页
                usrIdArr = aide.getUserArr(userBindServ, uiQuery);
            }

            // 0 方式一，令牌
            if (StrTool.strNotNull(tknOperSel) && StrTool.strEquals(tknOperSel, StrConstant.common_number_0)) {
                if (StrTool.strNotNull(tknOperSel)) {
                    tokenArr = tknBoxKey.split(",");
                }
            } else {
                // 1 方式二,操作对象:本次查询所有记录
                TokenInfo tInfo = getTokenInfo(queryForm);
                tokenArr = aide.getTokenArr(tokenServ, tInfo);
            }
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        // 批量操作的用户、令牌数据是否为空
        if (!StrTool.arrNotNull(usrIdArr) || !StrTool.arrNotNull(tokenArr)) {
            //返回JSON格式实体数据
            String jsonStr = JsonTool.getJsonFromObj(0, aide.getMegEntity(-1, StrConstant.USR_TKN_ERR_04, 0, 0));
            response.getWriter().write(jsonStr);
            return null;
        }

        //一个用户最多可以绑定令牌数目
        int usrMaxBindTkn = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER,
                ConfConstant.CORE_MAX_BIND_TOKENS));
        //一个令牌最多可以被用户绑定数目
        int tknMaxBindUsr = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER,
                ConfConstant.CORE_MAX_BIND_USERS));

        //取得包含用户令牌列表、操作失败信息的Map
        Map<String, Object> map = null;
        int[] arrNum = new int[2];
        String errStr = null;
        try {
            map = aide.getUTList(usrIdArr, tokenArr, bindType, userTokenServ, usrMaxBindTkn, tknMaxBindUsr, tokenServ);
            arrNum = (int[]) map.get(StrConstant.USR_TKN_NUM);
            utList = (List<?>) map.get(StrConstant.USR_TKN_LIST);//里面已经包含了domainId、用户机构ID、令牌机构ID、绑定时间
            errStr = (String) map.get(StrConstant.USR_TKN_ERR);
            if (!StrTool.listNotNull(utList)) {
                errStr = output_tips(request, errStr);

                // 返回JSON格式实体数据
                String jsonStr = JsonTool.getJsonFromObj(0, aide.getMegEntity(-1, errStr, arrNum[0], arrNum[1]));
                response.getWriter().write(jsonStr);
                return null;
            }

            // 迁移令牌
            // 绑定类型为6，表示用户绑定令牌数目为1，一个令牌最多只绑定一个用户，此时令牌可迁移；
            if (bindType == NumConstant.common_number_6) {
                Iterator<?> it = utList.iterator();
                while (it.hasNext()) {
                    UserToken uk = (UserToken) it.next();

                    // 判断令牌是否为域下令牌；判断令牌ID是否为NULL
                    if (uk.getOrgunitId() == NumConstant.common_number_0) {
                        TokenInfo ti = new TokenInfo();
                        ti.setToken(uk.getToken());
                        ti.setDomainid(uk.getDomainId());
                        if (uk.getOrguserId() == NumConstant.common_number_0) {
                            ti.setOrgunitid(null);
                        } else {
                            ti.setOrgunitid(uk.getOrguserId());
                        }
                        tokenServ.updateTokenOrg(ti);
                    }
                }
            }

            // 添加用户、令牌的绑定关系
            userTokenServ.addUsrTkn(utList);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        //返回JSON格式实体数据
        String jsonStr = JsonTool.getJsonFromObj(0, aide.getMegEntity(0, null, utList.size(), arrNum[1]));
        response.getWriter().write(jsonStr);
        return null;
    }

    /**
     * 输出批量绑定令牌检查操作失败信息至客户端
     * @Date in May 7, 2011,1:03:47 PM
     * @param request
     * @param errList
     * @return
     */
    private String output_tips(HttpServletRequest request, String errStr) {
        String resultStr = "";
        if (StrTool.strNotNull(errStr)) {
            resultStr = errStr;
        }

        return resultStr;
    }

    /**
     * 批量绑定结果输出功能
     */
    public String batchBindOutPut() {
        String outType = request.getParameter("outType"); //输出类型
        String bindTypeStr = request.getParameter("bindType"); //绑定方式
        String userNum = request.getParameter("ok_userNum"); //绑定用户个数
        String tokenNum = request.getParameter("ok_tokenNum"); //绑定令牌个数

        String filePath = "";
        String fileName = "";
        String usrAttr = "1,20,"; //1、用户 20、令牌
        // Object object = null;//CommonConfig.getValue(StrConstant.USER_TOKEN_BIND_RESULT);
        // List<?> userTnkList = null;
        boolean flag = false;
        PrintWriter out = null;
        try {
            out = response.getWriter();
            /*if (StrTool.objNotNull(object)) {
                if (object instanceof List<?>) {
                    userTnkList = (List<?>) object;
                }
            }*/

            if (StrTool.listNotNull(utList)) {
                //Excel文件
                if (StrTool.strEquals(outType, StrConstant.common_number_1)) {
                    fileName = StrTool.timeMillis() + Constant.FILE_XLS;
                    filePath = appPath(Constant.WEB_TEMP_FILE_USER, "/excel/");
                    aide.createUserTnkXls(filePath, fileName, usrAttr, utList, bindTypeStr, importActionAide, request);
                    flag = true;

                    //pdf文件
                } else if (StrTool.strEquals(outType, StrConstant.common_number_2)) {
                    fileName = StrTool.timeMillis() + Constant.FILE_PDF;
                    filePath = appPath(Constant.WEB_TEMP_FILE_USER, "/pdf/");
                    flag = UserTnkPdfUtil.createUserTnkPdf(filePath, fileName, utList, usrAttr, userNum, tokenNum);

                    //html文件
                } else if (StrTool.strEquals(outType, StrConstant.common_number_3)) {
                    fileName = StrTool.timeMillis() + Constant.FILE_HTML;
                    filePath = appPath(Constant.WEB_TEMP_FILE_USER, "/html/");
                    flag = aide.createUserTnkHtml(filePath, fileName, usrAttr, utList, request);

                    //csv文件
                } else if (StrTool.strEquals(outType, StrConstant.common_number_4)) {
                    fileName = StrTool.timeMillis() + Constant.FILE_CSV;
                    filePath = appPath(Constant.WEB_TEMP_FILE_USER, "/csv/");
                    flag = aide.createUserTnkCsv(filePath, fileName, utList, request);
                }
            }

            if (flag) {
                out.print(fileName);
                out.flush();
            } else {
                out.print("false");
                out.flush();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            out.print("false");
            out.flush();
            return null;
        } finally {
            if (out != null) {
                out.close();
            }
        }

        return null;
    }

    /**
     * 根据不同的文件类型，获取不同目录下不同格式的文件
     * 方法说明
     * @Date in Feb 9, 2012,3:33:45 PM
     * @return
     */
    public String downLoad() {
        String fileName = request.getParameter("fileName");
        String filePath = "";
        if (fileName.endsWith(Constant.FILE_HTML)) {
            filePath = appPath(Constant.WEB_TEMP_FILE_USER, "/html/");
        } else if (fileName.endsWith(Constant.FILE_PDF)) {
            filePath = appPath(Constant.WEB_TEMP_FILE_USER, "/pdf/");
        } else if (fileName.endsWith(Constant.FILE_XLS)) {
            filePath = appPath(Constant.WEB_TEMP_FILE_USER, "/excel/");
        } else if (fileName.endsWith(Constant.FILE_CSV)) {
            filePath = appPath(Constant.WEB_TEMP_FILE_USER, "/csv/");
        }

        filePath = filePath + fileName;
        try {
            importActionAide.downLoadFile(fileName, filePath, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    //    /*
    //     * 统计用户已绑定令牌、令牌已被用户绑定的次数 的情况
    //     * 
    //     */
    //    public String countUserTokenStuation() {
    //        try {
    //        	String userId = request.getParameter("userId");
    //            String tokens = request.getParameter("tokens"); //token,koken 形式    
    //            
    //            String[] tokenArray= tokens.split(",");
    //            
    //            int tknMaxBindUsr = CommonConfig.sysMaxbindusers();//用户最大绑定次数
    //            int usrMaxBindTkn = CommonConfig.sysMaxbindtokens();//令牌最大绑定次数
    //            
    //            //判断用户是否超限次数
    //            int usrCount=aide.utBindCount(userTokenServ,userId,null);//用户当前已绑定次数
    //            int userLeftBindNum = usrMaxBindTkn - usrCount;
    //            if(userLeftBindNum<=0){ //如果 该用户的绑定次数已经超限
    //            	outPutOperResult(Constant.alert_error, "用户绑定次数已上限！");
    //            	return null;
    //            }
    //            //判断令牌绑定次数是否超限
    //            int isExsit=0;//0表示不存在超限令牌
    //            String exceptionToken="";
    //            for(int i=0;i<tokenArray.length;i++){
    //            	int tokenCount=aide.utBindCount(userTokenServ,null,tokenArray[i]);//令牌当前已绑定次数
    //	            int tknLeftBindNum=tknMaxBindUsr-tokenCount;
    //	            if(tknLeftBindNum<=0){
    //	            	exceptionToken+=tokenArray[i]+",";
    //	            }
    //            }
    //            if(isExsit!=0){
    //            	outPutOperResult(Constant.alert_error, exceptionToken+" 令牌绑定次数已上限！");
    //            	return null;
    //            }else{
    //            	return null;
    //            }
    //            
    //        } catch (Exception ex) {
    //            log.error(ExceptionCode.USER_BIND, ex);
    //            return null;
    //        }
    //
    //        
    //    }

    public String evaluaDomain() {
        return null;
    }

}
