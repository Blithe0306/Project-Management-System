/**
 *Administrator
 */
package com.ft.otp.manager.admin.user.action.aide;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.ft.otp.base.dao.namespace.AdminNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.mail.MailInfo;
import com.ft.otp.common.mail.SendMailUtil;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.admin_role.entity.AdminAndRole;
import com.ft.otp.manager.admin.admin_role.service.IAdminAndRoleServ;
import com.ft.otp.manager.admin.role.entity.RoleInfo;
import com.ft.otp.manager.admin.role.service.IRoleInfoServ;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.admin.user.service.IAdminUserServ;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.token.service.ITokenServ;
import com.ft.otp.manager.user_token.entity.UserToken;
import com.ft.otp.manager.user_token.service.IUserTokenServ;
import com.ft.otp.util.alg.AlgHelper;
import com.ft.otp.util.alg.base.digest.IDigest;
import com.ft.otp.util.alg.base.digest.MD5;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.PwdEncTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理员实现的辅助类功能说明
 *
 * @Date in Jul 4, 2011,2:49:36 PM
 *
 * @author ZJY
 */
public class AdminUserActionAide {

    private Logger logger = Logger.getLogger(AdminUserActionAide.class);

    // 管理员服务接口
    private IAdminUserServ adminUserServ = (IAdminUserServ) AppContextMgr.getObject("adminUserServ");
    // 用户令牌服务接口
    private IUserTokenServ userTokenServ = (IUserTokenServ) AppContextMgr.getObject("userTokenServ");
    // 令牌服务接口
    private ITokenServ tokenServ = (ITokenServ) AppContextMgr.getObject("tokenServ");

    /**
     * 添加管理员对应的角色Id
     * @Date in Jul 4, 2011,3:54:06 PM
     * @param adminid
     * @param adminroleList
     * @return admList
     * @throws BaseException
     */
    public List<?> getAdminAndRole(String adminid, List<?> adminroleList) throws BaseException {
        List<Object> admList = new ArrayList<Object>();
        AdminAndRole adminAndRole = null;
        if (StrTool.listNotNull(adminroleList)) {
            for (int i = 0; i < adminroleList.size(); i++) {
                adminAndRole = new AdminAndRole();
                adminAndRole.setAdminId(adminid);
                adminAndRole.setRoleId(StrTool.parseInt((String) adminroleList.get(i)));
                admList.add(adminAndRole);
            }
        }
        return admList;
    }

    /**
     * 查询管理员对应的角色列表
     * @Date in Jul 4, 2011,3:54:06 PM
     * @param adminAndRoleServ
     * @param adminuser
     * @return roleList
     * @throws BaseException
     */
    public List<?> getRoleServ(IAdminAndRoleServ adminAndRoleServ, AdminUser adminuser) throws BaseException {
        PageArgument pageArg = new PageArgument();
        AdminAndRole adminAndRole = new AdminAndRole();
        adminAndRole.setAdminId(adminuser.getAdminid());
        return adminAndRoleServ.query(adminAndRole, pageArg);
    }

    /**
     * 查询当前登录管理员下级管理员列表(包含自己)
     * @param queryUser
     * @param pageArgument
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<?> getCurrLoginUserChildUsers(AdminUser queryUser, PageArgument pageArgument) throws BaseException {
        if (!StrTool.objNotNull(queryUser)) {
            queryUser = new AdminUser();
        }

        if (StrTool.objNotNull(pageArgument)) {
            queryUser.setPageSize(pageArgument.getPageSize());
            queryUser.setStartRow(pageArgument.getStartRow());
        }

        // 查找自己和自己的创建的管理员
        List<?> childUsers = adminUserServ.getChildAdmins(queryUser, pageArgument,
                AdminNSpace.ADMIN_SELECT_SELF_CHILDREN_OU);

        return childUsers;
    }

    /**
     * 获取管理员对应的角色
     * @Date in Jul 5, 2011,3:17:09 PM
     * @param roleList
     * @param adminUserServ
     * @return rolesList
     * @throws BaseException 
     * 
     */
    public List<?> getAdminRoleList(List<?> roleList, IRoleInfoServ admRoleServ) throws BaseException {
        AdminAndRole adminAndRole = null;
        RoleInfo adminRole = new RoleInfo();
        int temp[] = new int[roleList.size()];
        for (int i = 0; i < roleList.size(); i++) {
            adminAndRole = (AdminAndRole) roleList.get(i);
            temp[i] = adminAndRole.getRoleId();
        }
        adminRole.setBatchIdsInt(temp);
        List<?> rolesList = admRoleServ.getAdmrolesList(adminRole);

        return rolesList;
    }

    /**
     * 发送管理员激活、找回密码、获取应急口令的邮件
     * @Date in Apr 27, 2013,8:16:47 PM
     * @param adminUser
     * @param request
     * @param sendType 0 管理员激活、1 找回密码、2 获取应急口令
     * @return
     */
    public boolean activeMailSend(AdminUser adminUser, HttpServletRequest request, int sendType) {
        String[] toAddress = new String[1];
        toAddress[0] = adminUser.getEmail();

        String subject = "";
        String content = "";
        if (sendType == NumConstant.common_number_0) {
            subject = Language.getLangStr(request, "admin_sys_activation");
            content = getContent(adminUser, request, sendType);
        } else if (sendType == NumConstant.common_number_1) {
            subject = Language.getLangStr(request, "admin_sys_find_pwd");
            content = getContent(adminUser, request, sendType);
        } else if (sendType == NumConstant.common_number_2) {
            subject = Language.getLangStr(request, "admin_sys_get_pin");
            content = getPinContent(adminUser, request);
        }

        MailInfo mailInfo = SendMailUtil.getMailInfo(toAddress, subject, content, null);
        return SendMailUtil.emailSeed(mailInfo);
    }

    /**
     * 管理员激活或获取静态密码短信内容
     * @Date in Apr 5, 2013,10:20:47 AM
     * @param adminUser
     * @param roleInfo
     * @param request
     * @param sendType 0 激活、1 获取密码
     * @return
     */
    private String getContent(AdminUser adminUser, HttpServletRequest request, int sendType) {
        StringBuilder str = new StringBuilder();
        String adminName = adminUser.getRealname();
        if (!StrTool.strNotNull(adminName)) {
            adminName = adminUser.getAdminid();
        }

        String scheme = request.getScheme();
        String host = request.getServerName();
        int port = request.getServerPort();
        String appName = request.getContextPath();
        String password = adminUser.getPwd();
        // password = PwdEncTool.desEncPwd(password);

        IDigest md5 = new MD5();
        md5.update(password.getBytes());
        String md5Str = AlgHelper.bytes2HexString(md5.digest()).toLowerCase();

        String url = scheme + "://" + host + ":" + port + appName;
        String lang = (String) request.getSession(true).getAttribute(Constant.LANGUAGE_SESSION_KEY);
        if (!StrTool.strNotNull(lang)) {
            lang = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_COMMON, ConfConstant.DEFAULT_SYSTEM_LANGUAGE);
        }

        String param = "/manager/admin/user/adminUser!validEmail.action?userid=" + adminUser.getAdminid() + "&id="
                + md5Str + "&lang=" + lang + "&source=";
        url += param;

        str.append(Language.getLangStr(request, "admin_hello")).append(
                "<strong>" + adminName + "</strong>" + "<br><br>");
        if (sendType == NumConstant.common_number_0) {// 激活
            url += "0";
            str.append(Language.getLangStr(request, "admin_add_already") + "<br>");
            str.append(Language.getLangStr(request, "admin_activate_admuser"));
            str.append(Language.getLangStr(request, "admin_activate_message") + "<br><br>");
            str.append("<strong>" + Language.getLangStr(request, "admin_activate_link") + "</strong><a href='" + url
                    + "' title='" + Language.getLangStr(request, "admin_activate_link_message") + "'>");
        } else {// 静态密码
            url += "1";
            str.append(Language.getLangStr(request, "admin_link_find_pass"));
            str.append("<br><br>");
            str.append("<strong>" + Language.getLangStr(request, "admin_find_link") + "</strong><a href='" + url
                    + "' title='" + Language.getLangStr(request, "admin_link_find_pass_tip") + "'>");
        }
        str.append(url).append("</a>");

        return str.toString();
    }

    /**
     * 获取管理员应急口令短信内容
     * @Date in Apr 27, 2013,8:30:15 PM
     * @param adminUser
     * @return
     */
    private String getPinContent(AdminUser adminUser, HttpServletRequest request) {
        StringBuilder str = new StringBuilder();

        String adminName = adminUser.getRealname();
        if (!StrTool.strNotNull(adminName)) {
            adminName = adminUser.getAdminid();
        }

        str.append(Language.getLangStr(request, "admin_hello")).append(
                "<strong>" + adminName + "</strong>" + "<br><br>");
        str.append(Language.getLangStr(request, "admin_emer_password"));

        String[] result = getAdminUserPin(adminUser, request);
        str.append(result[0]);
        if (StrTool.strNotNull(result[1])) {
            str.append("，" + Language.getLangStr(request, "admin_reason_message") + result[1]);
        }

        return str.toString();
    }

    /**
     * 获取管理员绑定的令牌的应急口令
     * 如果绑定多个令牌获取最后绑定的令牌的应急口令
     * 方法说明
     * @Date in Apr 27, 2013,8:51:46 PM
     * @param adminUser
     * @return String[] 0 pin 、1 未获取到应急口令原因
     */
    private String[] getAdminUserPin(AdminUser adminUser, HttpServletRequest request) {
        String[] pin = new String[2];
        pin[0] = Language.getLangStr(request, "admin_null"); //pin 码
        pin[1] = ""; //未获取到原因
        try {
            // 中间表中查询出该管理员绑定的令牌
            UserToken userToken = new UserToken();
            userToken.setUserId(adminUser.getAdminid());
            List<?> utList = userTokenServ.selectAdminTokens(userToken);

            if (!StrTool.listNotNull(utList)) {
                pin[1] = Language.getLangStr(request, "admin_unbind_token");
                return pin;
            }

            int maxBindId = 0;// 最大绑定ID
            TokenInfo tokenInfo = new TokenInfo();

            UserToken[] arrAdms = utList.toArray(new UserToken[utList.size()]);
            for (int i = 0; i < arrAdms.length; i++) {
                UserToken uToken = arrAdms[i];

                if (maxBindId < uToken.getBindId()
                        || StrTool.strEquals(pin[0], Language.getLangStr(request, "admin_null"))) {
                    maxBindId = uToken.getBindId();
                    tokenInfo.setToken(uToken.getToken());
                    tokenInfo.setLogout(NumConstant.common_number_0);// 未作废
                    // 查询绑定令牌详细信息
                    tokenInfo = (TokenInfo) tokenServ.find(tokenInfo);

                    if (tokenInfo.getEnabled() == NumConstant.common_number_0) {
                        pin[1] = Language.getLangStr(request, "admin_bind_token_unenable");
                        return pin;
                    }

                    if (tokenInfo.getLocked() != NumConstant.common_number_0) {
                        pin[1] = Language.getLangStr(request, "admin_bind_token_lock");
                        return pin;
                    }

                    if (tokenInfo.getLost() == NumConstant.common_number_1
                            && tokenInfo.getAuthmethod() == NumConstant.common_number_0) {
                        pin[1] = Language.getLangStr(request, "admin_bind_token_loss");
                        return pin;
                    }

                    if (tokenInfo.getLogout() == NumConstant.common_number_1) {
                        pin[1] = Language.getLangStr(request, "admin_bind_token_cancel");
                        return pin;
                    }

                    if (tokenInfo.getAuthmethod() == NumConstant.common_number_0) {
                        pin[1] = Language.getLangStr(request, "admin_bind_token_attest");
                        return pin;
                    }

                    if (StrTool.strNotNull(tokenInfo.getEmpin())) {
                        // 解密应急口令
                        pin[0] = PwdEncTool.descryPwdStr(tokenInfo.getEmpin());
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return pin;
    }

    /**
     * 设置管理员绑定的令牌
     * 
     * @Date in Apr 23, 2013,5:19:30 PM
     * @param adminUser
     * @param tokens
     * @return
     */
    public AdminUser setAdminTokens(AdminUser adminUser, List<?> tokens) {
        if (StrTool.listNotNull(tokens)) {
            int atCount = 0;
            UserToken userToken = null;
            List<Object> arrTkn = new ArrayList<Object>();
            for (int i = 0; i < tokens.size(); i++) {
                userToken = (UserToken) tokens.get(i);

                //管理员绑定的令牌域为null
                if (StrTool.strEquals(adminUser.getAdminid(), userToken.getUserId())
                        && !StrTool.objNotNull(userToken.getDomainId())) {
                    arrTkn.add(userToken);
                    atCount++;
                }
            }

            if (atCount > 0) {
                adminUser.setTokens(arrTkn);
            }
        }

        return adminUser;
    }

}
