/**
 *Administrator
 */
package com.ft.otp.manager.login.action;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.common.soap.LangResultCode;
import com.ft.otp.common.soap.MessageBean;
import com.ft.otp.common.soap.code.SoapAttribute;
import com.ft.otp.common.soap.help.AuthHelper;
import com.ft.otp.common.soap.help.SyncHelper;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.admin_role.entity.AdminAndRole;
import com.ft.otp.manager.admin.admin_role.service.IAdminAndRoleServ;
import com.ft.otp.manager.admin.role.entity.RoleInfo;
import com.ft.otp.manager.admin.role.service.IRoleInfoServ;
import com.ft.otp.manager.admin.role_perm.entity.RolePerm;
import com.ft.otp.manager.admin.role_perm.service.IRolePermServ;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.admin.user.service.IAdminUserServ;
import com.ft.otp.manager.login.entity.LinkUser;
import com.ft.otp.manager.login.form.LoginQueryForm;
import com.ft.otp.manager.login.service.LoginService;
import com.ft.otp.manager.login.service.OnlineUsers;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.token.service.ITokenServ;
import com.ft.otp.manager.user_token.entity.UserToken;
import com.ft.otp.manager.user_token.service.IUserTokenServ;
import com.ft.otp.util.alg.AlgHelper;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.PwdEncTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理员登录
 * 
 * @Date in Apr 17, 2011,12:27:56 PM
 * 
 * @author TBM
 */
public class LoginAction extends BaseAction {

    private static final long serialVersionUID = 565955754673443726L;

    private Logger logger = Logger.getLogger(LoginAction.class);

    // 角色-权限接口
    private IRolePermServ rolePermServ = (IRolePermServ) AppContextMgr.getObject("rolePermServ");
    // 管理员服务接口
    private IAdminUserServ adminUserServ = (IAdminUserServ) AppContextMgr.getObject("adminUserServ");
    //用户令牌服务接口
    private IUserTokenServ userTokenServ = (IUserTokenServ) AppContextMgr.getObject("userTokenServ");
    //令牌服务接口
    private ITokenServ tokenServ = (ITokenServ) AppContextMgr.getObject("tokenServ");

    //令牌认证帮助类
    private AuthHelper authHelper = new AuthHelper();
    //令牌同步帮助类
    private SyncHelper syncHelper = new SyncHelper();

    // 管理员-角色关系服务接口
    public IAdminAndRoleServ adminAndRoleServ = (IAdminAndRoleServ) AppContextMgr.getObject("adminAndRoleServ");
    // 管理员角色接口
    private IRoleInfoServ roleInfoServ = (IRoleInfoServ) AppContextMgr.getObject("roleInfoServ");
    //登录业务处理服务
    private LoginService loginService = new LoginService();
    // 日志记录类
    private LogCommonObj commonObj = new LogCommonObj();

    private LoginQueryForm queryForm;

    /**
     * @return the queryForm
     */
    public LoginQueryForm getQueryForm() {
        return queryForm;
    }

    /**
     * @param queryForm
     *            the queryForm to set
     */
    public void setQueryForm(LoginQueryForm queryForm) {
        this.queryForm = queryForm;
    }

    /**
     * 登录业务处理
     * 
     * @Date in Apr 17, 2011,12:29:12 PM
     * @return
     * @throws BaseException
     */
    public String login() throws BaseException {
        if (null == queryForm) {
            return INPUT;
        }

        String langSel = request.getParameter("language");//语言
        int localAuth = queryForm.getLocalAuth();
        // 静态密码登录需先检查验证码
        if (localAuth == NumConstant.common_number_0) {
            String checkCode = queryForm.getCheckCode();
            HttpSession session = request.getSession();
            String sessionCode = (String) session.getAttribute(StrConstant.VALIDATE_CODE);
            if (!StrTool.strEqualsIgnoreCase(checkCode, sessionCode)) {
                outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "login_verify_code_error"));
                return null;
            }
        }

        String result = "";
        try {
            AdminUser adminUser = new AdminUser();
            adminUser.setAdminid(queryForm.getUserId());
            adminUser = (AdminUser) adminUserServ.find(adminUser);
            if (null == adminUser) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "login_username_or_pwd_err"));
                return null;
            }
            
            // 判断本地登录方式
            if(adminUser.getLocalauth() != NumConstant.common_number_0){
            	if(!StrTool.strNotNull(queryForm.getPin())){
            		outPutOperResult(Constant.alert_error, Language.getLangStr(request, "login_vd_otp_pin_tip"));
                    return null;
            	}
            }

            // 是否区分大小写判断
            int exchangeTag = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_COMMON,
                    ConfConstant.USERID_FORMAT_TYPE));

            // 不转换 要大小写匹配
            if (exchangeTag == NumConstant.common_number_0) {
                if (!StrTool.strEquals(adminUser.getAdminid(), queryForm.getUserId())) {
                    outPutOperResult(Constant.alert_error, Language.getLangStr(request, "login_username_or_pwd_err"));
                    return null;
                }
            }

            // 获取管理员角色 
            String[] otpRole = getCurLoginUserRole(adminUser.getAdminid());

            // 判断是否是超级管理员 以及超级管理员是否被禁用了
            result = isSuperAdminCheck(otpRole);
            if (StrTool.strNotNull(result)) {
                outPutOperResult(Constant.alert_error, result);
                return null;
            }

            // 账号被上级管理员禁用后不能登录
            if (adminUser.getEnabled() == 0) {
                String enabledErrorMsg = "";
                if (adminUser.getLogincnt() > 0) {
                    enabledErrorMsg = Language.getLangStr(request, "login_account_is_disabled");
                } else {
                    if (ConfDataFormat.getSysConfEmailEnabled()) {
                        //如果是超级管理员未激活也可以登录
                        if (!StrTool.strEquals(otpRole[0], StrConstant.SUPER_ADMIN)) {
                            enabledErrorMsg = Language.getLangStr(request, "login_account_not_activated");
                        }
                    } else {
                        enabledErrorMsg = Language.getLangStr(request, "login_account_is_disabled");
                    }
                }

                if (StrTool.strNotNull(enabledErrorMsg)) {
                    outPutOperResult(Constant.alert_warn, enabledErrorMsg);
                    return null;
                }
            } else if (adminUser.getEnabled() == NumConstant.common_number_2) {// 重置密码后的状态
                // 密码验证
                result = pwdCheck(adminUser);
                if (StrTool.strNotNull(result)) {
                    outPutOperResult(Constant.alert_error, result);
                } else {
                    // 需要修改密码 改后为启用
                    outPutOperResult("resetpwd", adminUser.getPwd());
                }
                return null;
            }

            // 静态密码登录 或 静态密码+OTP登录
            if (localAuth == NumConstant.common_number_0 || localAuth == NumConstant.common_number_2) {
                //密码验证
                result = pwdCheck(adminUser);
                if (StrTool.strNotNull(result)) {
                    outPutOperResult(Constant.alert_error, result);
                    return null;
                }
            }

            // 动态口令 或 静态密码+OTP登录
            if (localAuth == NumConstant.common_number_1 || localAuth == NumConstant.common_number_2) {
                //OTP验证
                String otpAuthRet[] = otpAuth(adminUser);
                if (StrTool.strNotNull(otpAuthRet[1])) {
                    if (otpAuthRet[0].equals(StrConstant.common_number_1)) {// 需要同步
                        outPutOperResult(StrConstant.common_number_1, otpAuthRet[1]);
                        return null;
                    } else {
                        outPutOperResult(Constant.alert_error, otpAuthRet[1]);
                        return null;
                    }
                }
            }

            //账号重复登录检查
            result = acctCheck();
            if (StrTool.strNotNull(result)) {
                outPutOperResult(Constant.alert_warn, result);
                return null;
            }

            // 权限初始化
            Object[] permObjs = getAdminPermList(queryForm.getUserId());
            if (!StrTool.objNotNull(permObjs) || !StrTool.mapNotNull((HashMap<String, Object>) permObjs[0])) {
                outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "login_not_permission"));
                return null;
            }

            // Session 初始化
            sessionInit(adminUser, permObjs, otpRole, langSel);
            commonObj.addAdminLog(AdmLogConstant.log_aid_login, AdmLogConstant.log_obj_otpserver, Language.getLangStr(
                    request, "admin_info_account")
                    + Language.getLangStr(request, "colon") + queryForm.getUserId(), null, 0);

            // 登录次数计算累加
            int logincnt = adminUser.getLogincnt();
            logincnt++;
            adminUser.setLogincnt(logincnt);
            // 登录时间
            adminUser.setLogintime(StrTool.timeSecond());
            adminUserServ.updateAdminUser(adminUser);

            outPutOperResult(Constant.alert_succ, null);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            commonObj.addAdminLog(AdmLogConstant.log_aid_login, AdmLogConstant.log_obj_otpserver, Language.getLangStr(
                    request, "admin_info_account")
                    + Language.getLangStr(request, "colon") + queryForm.getUserId(), null, 1);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "login_username_or_pwd_err"));
        }

        return null;
    }

    /**
     * 判断是否是超级管理员 以及超级管理员是否被禁用了
     * 
     * @Date in Jul 11, 2013,1:21:18 PM
     * @param otpRole
     * @return
     */
    private String isSuperAdminCheck(String[] otpRole) {
        if (!StrTool.arrNotNull(otpRole)) {
            return null;
        }

        if (StrTool.strEquals(otpRole[0], StrConstant.SUPER_ADMIN)) {// 如果是超级管理员
            // 获取配置的 是否禁止使用超级管理员 y 是 、n 否
            String confVal = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_CENTER, ConfConstant.PROHIBIT_ADMIN);
            if (StrTool.strEquals(confVal, "y")) {
                return Language.getLangStr(request, "login_account_is_disabled");
            }
        }

        return null;
    }

    /**
     * 密码验证
     * 
     * @Date in Jun 8, 2012,1:20:50 PM
     * @param adminUser
     * @return
     * @throws Exception
     */
    private String pwdCheck(AdminUser adminUser) throws Exception {
        //密码永久锁定最大错误重试次数
        int longErrCount = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_CENTER,
                ConfConstant.LOGIN_ERROR_RETRY_LONG));
        //密码临时锁定最大错误重试次数
        int tempErrCount = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_CENTER,
                ConfConstant.LOGIN_ERROR_RETRY_TEMP));

        int curLongCnt = adminUser.getLongloginerrcnt();
        int curTempCnt = adminUser.getTemploginerrcnt();
        int locked = adminUser.getLocked();

        if (locked == NumConstant.common_number_2
                || (curLongCnt >= longErrCount && longErrCount != NumConstant.TEMP_LONG_LOCKED_NUM_65535)) {//已永久锁定
            commonObj.addAdminLoginLog(AdmLogConstant.log_aid_perm_lock, AdmLogConstant.log_obj_admin_amount, Language
                    .getLangStr(request, "admin_info_account")
                    + Language.getLangStr(request, "colon") + queryForm.getUserId(), null, 1, adminUser.getAdminid());
            return Language.getLangStr(request, "login_account_is_perm_locked");
        }
        if (locked == NumConstant.common_number_1
                || (curTempCnt >= tempErrCount && tempErrCount != NumConstant.TEMP_LONG_LOCKED_NUM_65535)) {//已临时锁定
            if (loginService.ifUnLockUser(adminUser) != NumConstant.common_number_0) {
                commonObj.addAdminLoginLog(AdmLogConstant.log_aid_temp_lock, AdmLogConstant.log_obj_admin_amount,
                        Language.getLangStr(request, "admin_info_account") + Language.getLangStr(request, "colon")
                                + queryForm.getUserId(), null, 1, adminUser.getAdminid());
                return Language.getLangStr(request, "login_account_locked_try_again_later");
            }
        }

        //静态密码比对
        //将密码解密为明文
        String password = PwdEncTool.descryPwdStr(adminUser.getPwd());
        MessageDigest md5 = MessageDigest.getInstance("md5");
        //明文密码进行md5加密
        md5.update(password.getBytes());
        byte[] pwdMd5 = md5.digest();
        String hexPwdMd5 = AlgHelper.bytes2HexString(pwdMd5);
        //使用md5加密后的密码和前台js的md5加密后的密码比对
        if (StrTool.strEqualsIgnoreCase(queryForm.getPassword(), hexPwdMd5)) {
            loginService.unLockedUser(adminUser);//认证成功，清0
            return null;
        }
        
        
        if (StrTool.strEquals(adminUser.getPwd(), password)) {
            loginService.unLockedUser(adminUser);//认证成功，清0
            return null;
        }

        loginService.updatePwdErrCnt(adminUser);//更新错误重试次数
        int retryCnt = loginService.getRetryCnt(adminUser.getTemploginerrcnt(), adminUser.getLongloginerrcnt());
        if (retryCnt == NumConstant.common_number_na_1) {
            commonObj.addAdminLoginLog(AdmLogConstant.log_aid_perm_lock, AdmLogConstant.log_obj_admin_amount, Language
                    .getLangStr(request, "admin_info_account")
                    + Language.getLangStr(request, "colon") + queryForm.getUserId(), null, 1, adminUser.getAdminid());
            return Language.getLangStr(request, "login_account_is_perm_locked");
        } else if (retryCnt == NumConstant.common_number_0) {
            commonObj.addAdminLoginLog(AdmLogConstant.log_aid_temp_lock, AdmLogConstant.log_obj_admin_amount, Language
                    .getLangStr(request, "admin_info_account")
                    + Language.getLangStr(request, "colon") + queryForm.getUserId(), null, 1, adminUser.getAdminid());
            return Language.getLangStr(request, "login_account_locked_try_again_later");
        } else {
            commonObj.addAdminLoginLog(AdmLogConstant.log_aid_login, AdmLogConstant.log_obj_pwd_error, Language
                    .getLangStr(request, "admin_info_account")
                    + Language.getLangStr(request, "colon") + queryForm.getUserId(), null, 1, adminUser.getAdminid());
            return Language.getLangStr(request, "login_uname_pwd_err_surplus_retry_num") + "<font color=red><strong>"
                    + retryCnt + "</strong></font>" + Language.getLangStr(request, "login_seq");
        }
    }

    /**
     * OTP验证
     * 
     * @Date in May 4, 2013,1:38:34 PM
     * @param adminUser
     * @return result[0] 与认证服务器交互后返回的码 result[1] 验证信息
     * @throws Exception
     */
    private String[] otpAuth(AdminUser adminUser) throws Exception {
        String[] retArr = new String[2];
        retArr[0] = "0";
        //管理员锁定状态判断
        int locked = adminUser.getLocked();
        if (locked == NumConstant.common_number_2) {//已永久锁定
            retArr[1] = Language.getLangStr(request, "login_account_is_perm_locked");
            return retArr;
        }
        if (locked == NumConstant.common_number_1) {//已临时锁定
            retArr[1] = Language.getLangStr(request, "login_account_locked_try_again_later");
            return retArr;
        }

        //查询管理员绑定的令牌
        UserToken queryUserToken = new UserToken();
        queryUserToken.setUserId(adminUser.getAdminid());
        List<?> tokenInfos = userTokenServ.selectAdminTokens(queryUserToken);
        if (!StrTool.listNotNull(tokenInfos)) {
            retArr[1] = Language.getLangStr(request, "login_account_not_bind_tkn");
            return retArr;
        }

        //管理员最多只能绑定一支令牌
        UserToken userToken = (UserToken) tokenInfos.get(0);
        //获取绑定的令牌信息
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setToken(userToken.getToken());
        tokenInfo = (TokenInfo) tokenServ.find(tokenInfo);

        //认证或同步请求
        MessageBean messageBean = null;
        int[] reSresult = null;
        if (StrTool.strNotNull(queryForm.getOldPin())) {//同步
            messageBean = new MessageBean(tokenInfo.getToken(), queryForm.getOldPin(), queryForm.getPin());
            int syncResult = syncHelper.syncToken(messageBean);
            if (syncResult == SoapAttribute.OTP_SUCCESS) {
                return retArr;
            } else {
                retArr[1] = Language.getLangStr(request, "tkn_sync_error")
                        + LangResultCode.getLangStr(request, syncResult);
                return retArr;
            }
        } else {//认证
            messageBean = new MessageBean(tokenInfo.getToken(), queryForm.getPin(), null, null);
            reSresult = authHelper.authTest(messageBean);
        }

        if (reSresult[0] == SoapAttribute.OTP_SUCCESS) {
            return retArr;
        } else {
            String message = Language.getLangStr(request, "login_tkn_auth_err")
                    + LangResultCode.getLangStr(request, reSresult[0]);
            retArr[0] = String.valueOf(reSresult[0]);
            if (reSresult[0] == NumConstant.common_number_1) {//需要同步
                message += Language.getLangStr(request, "comma")
                        + Language.getLangStr(request, "tkn_please_in_next_dynamic_pwd");
                retArr[1] = message;
                return retArr;
            }

            if (reSresult[1] > 0) {
                message += Language.getLangStr(request, "login_residual_error_retry") + reSresult[1];
            }
            retArr[1] = message;

            return retArr;
        }
    }

    /**
     * 帐号重复登录检查
     * 
     * @Date in Jun 7, 2012,1:49:49 PM
     * @return
     */
    private String acctCheck() {
        String userId = queryForm.getUserId();
        LinkUser linkUser = getLinkUser();
        //如果会话不相同，客户端地址也不同，则如果已经有相同的用户帐号登录系统，
        //前者被后者取代，前者自动下线
        if (!StrTool.objNotNull(linkUser)) {
            List<?> users = OnlineUsers.getOnlineUsers();
            if (StrTool.listNotNull(users)) {
                Iterator<?> iter = users.iterator();
                while (iter.hasNext()) {
                    linkUser = (LinkUser) iter.next();
                    String lUserId = linkUser.getUserId();
                    if (StrTool.strEquals(userId, lUserId)) {
                        String sessId = linkUser.getSessionId();
                        OnlineUsers.remove(sessId);
                        OnlineUsers.addRemoveSession(sessId, sessId);
                        break;
                    }
                }
            }
        }

        return null;
    }

    /**
     * 多语言选择
     * 
     * @Date in Apr 19, 2011,9:38:43 AM
     * @return
     */
    public String language() {
        String language = request.getParameter("language");
        // 设置用户当前会话语言
        request.getSession(true).setAttribute(Constant.LANGUAGE_SESSION_KEY, language);

        return INPUT;
    }

    /**
     * 会话设置
     * 
     * @Date in Jul 11, 2013,1:31:03 PM
     * @param adminUser
     * @param permObjs
     * @param otpRole
     * @throws BaseException
     */
    private void sessionInit(AdminUser adminUser, Object[] permObjs, String[] otpRole, String language)
            throws BaseException {
        HttpSession session = request.getSession(true);
        String sessionId = session.getId();

        String sessionLang = (String) session.getAttribute(Constant.LANGUAGE_SESSION_KEY);
        if (!StrTool.strNotNull(sessionLang)) {
            session.setAttribute(Constant.LANGUAGE_SESSION_KEY, language);
        }
        LinkUser linkUser = OnlineUsers.getUser(sessionId);
        if (!StrTool.objNotNull(linkUser)) {
            linkUser = new LinkUser();
            linkUser.setUserId(adminUser.getAdminid());
            linkUser.setSessionId(sessionId);
            linkUser.setLanguage(language);
            // 登录时间(当前时间：秒)
            linkUser.setLoginTime(StrTool.timeSecond());
            // 设置管理员权限
            linkUser.setPermMap((HashMap<String, Object>) permObjs[0]);
            linkUser.setUrlMap((HashMap<String, String>) permObjs[1]);

            //将当前登录用户保存到session中以便其它地方直接通过session.getAttribute()获取
            session.setAttribute(Constant.CUR_LOGINUSER, linkUser.getUserId());

            //客户端访问地址
            linkUser.setRemoteAddr(request.getRemoteAddr());

            String roleMark = "";
            String roleName = "";
            String roleNameLink = "";
            if (null != otpRole) {
                roleMark = otpRole[0];
                roleName = otpRole[1];
                roleNameLink = otpRole[2];
            }
            linkUser.setRoleName(roleName);

            //设置管理员首页显示信息相关
            linkUser.setRealName(adminUser.getRealname());
            linkUser.setOldLoginTime(adminUser.getLogintimeStr());
            linkUser.setLogincnt(adminUser.getLogincnt() + 1);//加上本次的
            linkUser.setPwdUseDays((int) adminUser.getPwdsettime());
            linkUser.setIsWarnUpdataPwd(0);
            linkUser.setRoleNameLink(roleNameLink);

            // 管理员角色
            session.setAttribute(Constant.CUR_LOGINUSER_ROLE, roleMark);
            session.setAttribute(Constant.SESSION_MARK, linkUser);

            // 添加一个在线用户
            OnlineUsers.add(linkUser);

            // 设置会话过期时间
            int sessionTime = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_COMMON,
                    ConfConstant.SESSION_EFFECTIVELY_TIME));
            sessionTime = sessionTime * 60;
            session.setMaxInactiveInterval(sessionTime);
        }
    }

    /**
     * 查询某个管理员对应的权限
     */
    private Object[] getAdminPermList(String adminId) throws BaseException {
        Object[] objects = new Object[2];
        List<?> permsList = null;

        RolePerm rolePerm = new RolePerm();
        rolePerm.setAdminId(adminId);
        permsList = rolePermServ.queryAdmPerms(rolePerm);
        if (!StrTool.listNotNull(permsList)) {
            return objects;
        }

        Map<String, Object> perMap = new HashMap<String, Object>();
        Map<String, String> urlMap = new HashMap<String, String>();
        for (Iterator<?> it = permsList.iterator(); it.hasNext();) {
            RolePerm rPerm = (RolePerm) it.next();
            perMap.put(rPerm.getPermcode(), rPerm);

            String permLink = rPerm.getPermlink();
            if (StrTool.strNotNull(permLink)) {
                String[] keyArr = permLink.split(",");
                for (String key : keyArr) {
                    urlMap.put(key, rPerm.getPermcode());
                }
            }
        }

        objects[0] = perMap;
        objects[1] = urlMap;

        return objects;
    }

    /**
     * 查询管理员对应的角色
     */
    public Map<String, String> getAdminRole(String adminId) throws BaseException {
        AdminAndRole adminAndRole = new AdminAndRole();
        Map<String, String> map = new HashMap<String, String>();
        String[] admins = { adminId };
        adminAndRole.setAdminIds(admins);
        List<?> adminroList = adminAndRoleServ.queryAdmins(adminAndRole);
        int roleIds[] = new int[adminroList.size()];
        if (StrTool.listNotNull(adminroList)) {
            for (int i = 0; i < adminroList.size(); i++) {
                AdminAndRole andRole = (AdminAndRole) adminroList.get(i);
                roleIds[i] = andRole.getRoleId();
            }
            RoleInfo role = new RoleInfo();
            role.setBatchIdsInt(roleIds);
            List<?> roleList = roleInfoServ.getAdmrolesList(role);
            if (StrTool.listNotNull(roleList)) {
                for (int i = 0; i < roleList.size(); i++) {
                    RoleInfo aRole = (RoleInfo) roleList.get(i);
                    map.put(aRole.getRoleName(), aRole.getRoleName());
                }
            }
        }

        return map;
    }

    /**   
     * 根据用户名称查询当前登录用户的角色，超级管理员、普通管理员
     * 
     * @Date in Apr 24, 2012,4:59:41 PM
     * @return 
     *      arr[0] 角色表示区分是否是超级管理员角色
     *      arr[1] 角色名称
     *      arr[2] 角色名称和角色ID链接
     */
    public String[] getCurLoginUserRole(String adminId) {
        String[] arr = new String[3];
        arr[0] = "";
        arr[1] = "";
        arr[2] = "";
        try {
            //根据角色ID查询角色
            AdminAndRole otpRole = new AdminAndRole();
            otpRole.setAdminId(adminId);

            List<?> list = adminAndRoleServ.query(otpRole, new PageArgument());
            if (!StrTool.listNotNull(list)) {
                return arr;
            }

            for (int i = 0; i < list.size(); i++) {
                otpRole = (AdminAndRole) list.get(i);
                if (StrTool.strNotNull(otpRole.getRolemark()) && !StrTool.strNotNull(arr[0])) {
                    arr[0] = otpRole.getRolemark();
                }
                arr[1] += "[" + otpRole.getRoleName() + "] ";
                arr[2] += "<a href='javascript:viewRole(\"" + otpRole.getRoleId() + "\");'>[" + otpRole.getRoleName()
                        + "] </a> ";
            }
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        return arr;
    }

    /**
     * 根据登录账户 获取用户信息
     * 方法说明
     * @Date in May 4, 2013,12:09:57 PM
     * @return
     */
    public String getAdmUserLocalAuth() {
        try {
            String userId = request.getParameter("userId");
            if (!StrTool.strNotNull(userId)) {
                outPutOperResult(Constant.alert_warn, null);
                return null;
            }

            AdminUser adminUser = new AdminUser();
            adminUser.setAdminid(userId);
            adminUser = (AdminUser) adminUserServ.find(adminUser);

            if (!StrTool.objNotNull(adminUser)) {
                outPutOperResult(Constant.alert_warn, null);
                return null;
            }

            outPutOperResult(Constant.alert_succ, adminUser);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "login_get_admin_logintype_err"));
        }

        return null;
    }

}
