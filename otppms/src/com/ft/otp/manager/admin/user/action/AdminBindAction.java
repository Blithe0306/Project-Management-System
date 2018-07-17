/**
 *Administrator
 */
package com.ft.otp.manager.admin.user.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import com.ft.otp.base.action.BaseAction;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.user.action.aide.AdminBindActionAide;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.token.action.aide.TokenActionAide;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.token.service.ITokenServ;
import com.ft.otp.manager.user.userinfo.form.BindUTQForm;
import com.ft.otp.manager.user_token.entity.UserToken;
import com.ft.otp.manager.user_token.service.IUserTokenServ;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理员绑定令牌业务控制Action
 * 注：此业务类和用户绑定令牌业务类相似
 *
 * @Date in Apr 26, 2013,3:06:43 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class AdminBindAction extends BaseAction {

    private static final long serialVersionUID = -3724405181297496193L;

    private Logger logger = Logger.getLogger(AdminBindAction.class);

    //令牌服务接口
    private ITokenServ tokenServ = (ITokenServ) AppContextMgr.getObject("tokenServ");

    //令牌的辅助类
    private TokenActionAide aideToken = new TokenActionAide();

    //用户令牌服务接口
    private IUserTokenServ userTokenServ = (IUserTokenServ) AppContextMgr.getObject("userTokenServ");

    private int usrMaxBindTkn = 1;
    private int tknMaxBindUsr = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER,
            ConfConstant.CORE_MAX_BIND_USERS));

    private BindUTQForm queryForm = null;
    private AdminUser adminUser = null;

    private AdminBindActionAide aide = null;

    public AdminBindAction() {
        if (null == aide) {
            aide = new AdminBindActionAide();
        }
    }

    /**
     * 取出QueryForm中的实体
     * @Date in May 5, 2011,6:30:40 PM
     * @param agentQueryForm
     * @return
     */
    private TokenInfo getTokenInfo(BindUTQForm queryForm) {
        TokenInfo tokenInfo = new TokenInfo();
        try {
            String adminId = request.getParameter("userId");
            // 判断是否是超级管理员 判断的是选择的管理员 不是当前登录的管理员
            boolean isAdminRole = aide.isAdminRoleByAdminId(adminId);

            if (StrTool.objNotNull(queryForm)) { // 查询令牌列表
                tokenInfo = queryForm.getTokenInfo();

                // 管理员绑定令牌时 只能绑定域下边的令牌
                if (queryForm.getDOrgunitId().indexOf(",") != -1) {// 选中了域机构查询条件
                    String dOrgunitidStr = queryForm.getDOrgunitId().split(",")[0]; //userInfo.getDOrgunitId()  domainId:orgunitId,

                    // 如果是超级管理员可以任意查询 否则 只能查询该管理员管理的域
                    String queryDomainId = dOrgunitidStr.split(":")[0];
                    if (isAdminRole) {
                        tokenInfo.setDomainid(StrTool.parseInt(queryDomainId)); //设置域id
                    } else {
                        // 获取管理的域
                        int[] domains = aide.getAdminDomains(adminId);

                        // 判断查询域是否在 管理的域中
                        boolean isHave = false;
                        for (int domain : domains) {
                            if (StrTool.parseInt(queryDomainId) == domain) {
                                tokenInfo.setDomainid(StrTool.parseInt(queryDomainId)); //设置域id
                                isHave = true;
                                break;
                            }
                        }
                        if (!isHave) {
                            tokenInfo.setDomainid(-1);// 没有符合的  此处就是-1 sql 为 domainid = -1 就是不让查询出令牌
                        }
                    }
                } else {// 没有选择域机构查询条件 当前管理员管理的域 超级管理员的话所有的域
                    if (isAdminRole) {//如果有超级管理员角色 域和组织机构都不过滤
                        tokenInfo.setDomainid(null);
                        tokenInfo.setDomainids(null);
                    } else {
                        // 普通管理员管理的域
                        tokenInfo.setDomainids(aide.getAdminDomains(adminId));
                    }
                }
            } else { // 初始化令牌列表  当前登录管理员管理的域
                if (isAdminRole) {//如果有超级管理员角色 域和组织机构都不过滤
                    tokenInfo.setDomainid(null);
                    tokenInfo.setDomainids(null);
                } else {
                    tokenInfo.setDomainids(aide.getAdminDomains(adminId));
                }
                // 初始化令牌列表显示未绑定的令牌
                tokenInfo.setBind(0);
            }
            tokenInfo.setOrgunitid(null);//目的是不让其参加查询条件判断  如果是null 则查询组织机构是 null的
            tokenInfo.setTflag(1);// sql org is null
            tokenInfo.setLogout(NumConstant.common_number_0);//未注销的
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return tokenInfo;
    }

    /**
     * 令牌查询
     * @Date in May 5, 2011,11:36:33 AM
     * @return
     */
    public String tknQuery() {
        if (isNeedClearForm()) {
            queryForm = null;
        }
        try {
            int total = tokenServ.countBC(getTokenInfo(queryForm)); //统计数量
            PageArgument pageArg = getArgument(total);
            pageArg.setCurPage(getPage());
            pageArg.setPageSize(getPagesize());
            List<?> tokenList = tokenServ.queryBC(getTokenInfo(queryForm), pageArg);

            // Start 插入组织机构名构
            for (TokenInfo tokenInfo : (List<TokenInfo>) tokenList) {
                tokenInfo = aideToken.setDomainAndOrgunitName(tokenInfo);
            }
            // End 插入组织机构名构

            // 返回JSON格式查询数据
            String jsonStr = JsonTool.getJsonFromList(pageArg.getTotalRow(), tokenList, pageArg);
            setResponseWrite(jsonStr);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 令牌信息分页处理
     */
    public String page() {
        return null;
    }

    /**
     * 批量绑定管理员与令牌  管理员此处是单绑定
     * @Date in May 5, 2011,7:32:58 PM
     */
    public String batchBindUT() {
        try {
            // checkBox选中的数据
            String usrBoxKey = request.getParameter("userArr");//这里包含了 userId:domainId,userId:domainId,形式
            String tknBoxKey = request.getParameter("tokenArr");
            String userId = request.getParameter("userId");

            String[] usrIdArr = null; //这里包含了 userId:domainId,userId:domainId,形式
            List<String> tknList = new ArrayList<String>();

            if (StrTool.strNotNull(usrBoxKey)) {
                usrBoxKey = aide.replaceUserId(usrBoxKey);
                usrIdArr = usrBoxKey.split(",");
            }

            if (StrTool.strNotNull(tknBoxKey)) {
                String[] tokenArr = tknBoxKey.split(",");
                for (String tkn : tokenArr) {
                    tknList.add(tkn);
                }
            }

            // 操作的管理员、令牌数据是否为空
            if (!StrTool.arrNotNull(usrIdArr) || !StrTool.listNotNull(tknList)) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "admin_bind_err_oper_null"));
                return null;
            }

            // 进行绑定操作
            AdminUser adminInfo = new AdminUser();
            adminInfo.setAdminid(userId);
            String result = aide.addUT(adminInfo, tknList, tknMaxBindUsr, usrMaxBindTkn, request);

            if (StrTool.strNotNull(result)) {
                outPutOperResult(Constant.alert_error, result);
            } else {
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "tkn_bind_success_tip"));
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_bind_error_tip"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 更换令牌操作
     * @Date in Apr 27, 2011,5:26:20 PM
     * @return
     */
    public String bindChangeTkn() {
        try {
            List<?> tokens = adminUser.getTokens();
            List<?> hidTkns = adminUser.getHiddenTkns();

            if (StrTool.isArrEquals(tokens, hidTkns)) {
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "tkn_replace_success_tip"));
                return null;
            }

            // 如果任意一个令牌集合为空则不执行
            if (!StrTool.listNotNull(hidTkns) || !StrTool.listNotNull(tokens)) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_replace_error_tip"));
                return null;
            }

            // 旧令牌列表不为空，新令牌不为空，既执行解绑又执行绑定操作
            String result = aide.upUTBind_2(hidTkns, tokens, adminUser, tknMaxBindUsr, usrMaxBindTkn, request);
            if (StrTool.strNotNull(result)) {
                outPutOperResult(Constant.alert_error, result);
            } else {
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "tkn_replace_success_tip"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_replace_error_tip"));
        }

        return null;
    }

    /**
     * 解除管理员与令牌的绑定关系
     * @Date in May 3, 2011,9:41:15 AM
     * @return
     * @throws Exception 
     */
    public String unBindUT() {
        String tokenIds = request.getParameter("tokenIds");
        UserToken userToken = new UserToken();
        // 解绑单支令牌、多支令牌的标识
        String unbindNum = request.getParameter("unbindNum");
        try {
            adminUser.setAdminid(aide.replaceUserId(adminUser.getAdminid()));//将用户名中存在的[]符号转换为&
            if (StrTool.strNotNull(unbindNum) && StrTool.strEquals(unbindNum, "1")) { //单只解绑
                List<?> tokens = adminUser.getTokens();
                userToken.setTokenIds(tokens);
                userToken.setUserId(adminUser.getAdminid());
                userToken.setDomainId(-1);

                // 令牌解绑后， 被解绑令牌是否停用
                updateState(tokens);

                userTokenServ.delObj(userToken);
            } else { // 多只解绑
                if (StrTool.strNotNull(tokenIds)) {
                    String[] keys = tokenIds.split(",");
                    // 一个用户绑定了多个令牌，选择令牌进行解绑
                    List<String> keyList = Arrays.asList(keys);
                    userToken.setTokenIds(keyList);
                    userToken.setUserId(adminUser.getAdminid());
                    userToken.setDomainId(-1);

                    // 令牌解绑后， 被解绑令牌是否停用
                    updateState(keyList);

                    userTokenServ.delObj(userToken);
                }
            }

            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "tkn_unbind_success_tip"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_unbind_error_tip"));
        }

        return null;
    }

    /**
     * 判断解绑令牌，被解绑令牌是否停用
     * 
     */
    public void updateState(List<?> tokenIds) throws Exception {
        // 判断解绑令牌，被解绑令牌是否停用，1：是；0：否
        int rstate = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER,
                ConfConstant.CORE_UNBIND_STATE_SELECT));
        if (rstate == NumConstant.common_number_1) {

            for (int i = 0; i < tokenIds.size(); i++) {
                String token = (String) tokenIds.get(i);
                UserToken userTkn = new UserToken();
                userTkn.setToken(token);

                // 查看此令牌是否被其它用户绑定
                int count = userTokenServ.count(userTkn);
                if (count == NumConstant.common_number_1) {
                    TokenInfo tokenInfo = new TokenInfo();
                    tokenInfo.setToken(token);
                    tokenInfo = TokenActionAide.getTokenInfo(tokenInfo, 1);//停用
                    tokenServ.updateTokenState(tokenInfo);
                }
            }
        }
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
     * @return the adminUser
     */
    public AdminUser getAdminUser() {
        return adminUser;
    }

    /**
     * @param adminUser the adminUser to set
     */
    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

}
