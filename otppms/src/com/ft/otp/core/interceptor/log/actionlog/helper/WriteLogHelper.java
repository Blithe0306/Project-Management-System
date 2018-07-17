/**
 *Administrator
 */
package com.ft.otp.core.interceptor.log.actionlog.helper;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.ft.otp.common.Constant;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.manager.admin.role.action.RoleInfoAction;
import com.ft.otp.manager.admin.role.entity.RoleInfo;
import com.ft.otp.manager.admin.user.action.AdmUserAction;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.authmgr.agent.action.AgentAction;
import com.ft.otp.manager.authmgr.agent.entity.AgentInfo;
import com.ft.otp.manager.authmgr.agentconf.action.AgentConfAction;
import com.ft.otp.manager.authmgr.agentconf.entity.AgentConfInfo;
import com.ft.otp.manager.authmgr.backend.action.BackendAction;
import com.ft.otp.manager.authmgr.backend.entity.BackendInfo;
import com.ft.otp.manager.authmgr.server.action.ServerAction;
import com.ft.otp.manager.authmgr.server.entity.ServerInfo;
import com.ft.otp.manager.confinfo.config.action.AuthConfAction;
import com.ft.otp.manager.confinfo.config.action.CenterAction;
import com.ft.otp.manager.confinfo.config.action.CommConfAction;
import com.ft.otp.manager.confinfo.config.action.ConfMonitorConfigAction;
import com.ft.otp.manager.confinfo.config.action.DBConfAction;
import com.ft.otp.manager.confinfo.config.action.MonitorConfAction;
import com.ft.otp.manager.confinfo.config.action.PortalConfAction;
import com.ft.otp.manager.confinfo.config.action.TokenConfAction;
import com.ft.otp.manager.confinfo.config.action.UserConfAction;
import com.ft.otp.manager.confinfo.config.entity.AuthConfInfo;
import com.ft.otp.manager.confinfo.config.entity.CenterConfInfo;
import com.ft.otp.manager.confinfo.config.entity.CommonConfInfo;
import com.ft.otp.manager.confinfo.config.entity.DBConfInfo;
import com.ft.otp.manager.confinfo.config.entity.MonitorConf;
import com.ft.otp.manager.confinfo.config.entity.MonitorConfig;
import com.ft.otp.manager.confinfo.config.entity.PortalInfo;
import com.ft.otp.manager.confinfo.config.entity.TokenConfInfo;
import com.ft.otp.manager.confinfo.config.entity.UserConfInfo;
import com.ft.otp.manager.confinfo.sms.action.SmsInfoAction;
import com.ft.otp.manager.customer.action.CustomerInfoAction;
import com.ft.otp.manager.lic.action.LicInfoAction;
import com.ft.otp.manager.lic.entity.LicInfo;
import com.ft.otp.manager.orgunit.domain.action.DomainInfoAction;
import com.ft.otp.manager.orgunit.domain.entity.DomainInfo;
import com.ft.otp.manager.orgunit.orgunit.action.OrgunitInfoAction;
import com.ft.otp.manager.orgunit.orgunit.entity.OrgunitInfo;
import com.ft.otp.manager.prjinfo.action.PrjinfoAction;
import com.ft.otp.manager.project.action.ProjectAction;
import com.ft.otp.manager.resords.action.ResordsAction;
import com.ft.otp.manager.user.userinfo.action.UserExportAction;
import com.ft.otp.manager.user.userinfo.action.UserInfoAction;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 拦截ACTION方法写日志的帮助类
 *
 * @Date in Feb 21, 2013,2:36:19 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class WriteLogHelper {

    /**
     * 获取当前用户
     * @Date in Feb 21, 2013,2:36:44 PM
     * @param session
     * @return
     */
    public String getCurUser(HttpSession session) {
        String curUser = null;
        if (null != session) {
            curUser = (String) session.getAttribute(Constant.CUR_LOGINUSER);
        }
        if (!StrTool.strNotNull(curUser)) {
            curUser = "";
        }
        return curUser;
    }

    public LogCommonObj getLogCommonObj() {
        return new LogCommonObj();
    }

    /**
     * 其它公共配置日志记录
     * @Date in Jul 30, 2013,11:19:29 AM
     * @param curUser
     * @param commConfAction
     * @return
     */
    public String writeCommConfStr(String curUser, CommConfAction commConfAction) {
        CommonConfInfo commInfo = commConfAction.getCommInfo();
        CommonConfInfo oldCommInfo = commConfAction.getOldCommInfo();
        if (null == commInfo || null == oldCommInfo) {
            return null;
        }

        int i = 0;
        StringBuilder sbr = new StringBuilder();
        sbr.append(curUser).append(Language.getLangValue("log_comm_record_edit", Language.getCurrLang(null), null))
                .append(Language.getLangValue("log_edit_content", Language.getCurrLang(null), null)).append("</br>");
        if (!StrTool.strEquals(oldCommInfo.getSessioneffectivelytime(), commInfo.getSessioneffectivelytime())) {
            sbr.append(
                    Language.getLangValue("comm_session_eff_time", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                    oldCommInfo.getSessioneffectivelytime()).append(
                    Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                    commInfo.getSessioneffectivelytime()).append("</br>");
            i++;
        }
        if (!StrTool.strEquals(oldCommInfo.getLoglevel(), commInfo.getLoglevel())) {
            if (commInfo.getLoglevel().equals("0")) {
                sbr.append(
                        Language.getLangValue("comm_log_level", Language.getCurrLang(null), null)
                                + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                        Language.getLangValue("log_record_modify", Language.getCurrLang(null), null)).append(
                        Language.getLangValue("comm_key_log_mode", Language.getCurrLang(null), null)).append("</br>");
            } else if (commInfo.getLoglevel().equals("1")) {
                sbr.append(
                        Language.getLangValue("comm_log_level", Language.getCurrLang(null), null)
                                + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                        Language.getLangValue("log_record_modify", Language.getCurrLang(null), null)).append(
                        Language.getLangValue("comm_full_log_mode", Language.getCurrLang(null), null)).append("</br>");
            } else if (commInfo.getLoglevel().equals("-1")) {
                sbr.append(
                        Language.getLangValue("comm_log_level", Language.getCurrLang(null), null)
                                + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                        Language.getLangValue("log_record_modify", Language.getCurrLang(null), null)).append(
                        Language.getLangValue("comm_no_record_log", Language.getCurrLang(null), null)).append("</br>");
            }
            i++;
        }
        if (!StrTool.strEquals(commInfo.getLogtimingenabled(), oldCommInfo.getLogtimingenabled())) {
            if (commInfo.getLogtimingenabled().equals("0")) {
                sbr.append(Language.getLangValue("comm_log_delete_enable_span", Language.getCurrLang(null), null))
                		.append(Language.getLangValue("colon", Language.getCurrLang(null), null))
                		.append(Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null))
                        .append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null))
                        .append(Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null))
                        .append("</br>");
            } else {
                sbr.append(Language.getLangValue("comm_log_delete_enable_span", Language.getCurrLang(null), null))
                		.append(Language.getLangValue("colon", Language.getCurrLang(null), null))
                		.append(Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null))
                        .append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                                Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null)).append(
                                "</br>");
            }
            i++;
        }
        if (!StrTool.strEquals(commInfo.getLogtimingdelete(), oldCommInfo.getLogtimingdelete())) {
            sbr.append(
                    Language.getLangValue("comm_log_delete_days_span", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                    oldCommInfo.getLogtimingdelete()).append(
                    Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                    commInfo.getLogtimingdelete()).append("</br>");
            i++;
        }
        if (!StrTool.strEquals(commInfo.getLogisbak(), oldCommInfo.getLogisbak())) {
            sbr
                    .append(
                            Language.getLangValue("comm_log_delete_is_bak", Language.getCurrLang(null), null)
                                    + Language.getLangValue("colon", Language.getCurrLang(null), null))
                    .append(
                            Language
                                    .getLangValue(
                                            StrTool.strEquals(oldCommInfo.getLogisbak(), "0") ? "common_syntax_no" : "common_syntax_yes",
                                            Language.getCurrLang(null), null))
                    .append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null))
                    .append(
                            Language
                                    .getLangValue(
                                            StrTool.strEquals(commInfo.getLogisbak(), "0") ? "common_syntax_no" : "common_syntax_yes",
                                            Language.getCurrLang(null), null)).append("</br>");
            i++;
        }

        if (i == 0) {
            return null;
        }
        commConfAction.setCommInfo(null);
        commConfAction.setOldCommInfo(null);

        return sbr.toString();
    }

    /**
     * 认证基本配置
     * @Date in Jul 29, 2013,4:38:18 PM
     * @param curUser
     * @param authConfAction
     * @return
     */
    public String writeAuthConfStr(String curUser, AuthConfAction authConfAction) {
        AuthConfInfo authConfInfo = authConfAction.getAuthConfInfo();
        AuthConfInfo oldAuthConfInfo = authConfAction.getOldAuthConfInfo();
        if (null == authConfInfo || null == oldAuthConfInfo) {
            return null;
        }

        int i = 0;
        StringBuilder sbr = new StringBuilder();
        if("initpeap".equals(oldAuthConfInfo.getOper())){
        	sbr.append(curUser).append(Language.getLangValue("log_auth_wireless_edit", Language.getCurrLang(null), null))
            .append(Language.getLangValue("log_edit_content", Language.getCurrLang(null), null)).append("</br>");
		    if (!StrTool.strEquals(oldAuthConfInfo.getPropeapadbled(), authConfInfo.getPropeapadbled())) {
		        String oldStr = "";
		        if (oldAuthConfInfo.getPropeapadbled().equals("n")) {
		            oldStr = Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null);
		        } else {
		            oldStr = Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null);
		        } 
		        String str = "";
		        if (authConfInfo.getPropeapadbled().equals("n")) {
		            str = Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null);
		        } else{
		            str = Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null);
		        }
		        sbr.append(
		                Language.getLangValue("authconf_adbled_peap", Language.getCurrLang(null), null)
		                        + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(oldStr).append(
		                Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(str).append(
		                "</br>");
		        i++;
		    }
		    
		    if (!StrTool.strEquals(oldAuthConfInfo.getPropeaplocked(), authConfInfo.getPropeaplocked())) {
		        String oldStr = "";
		        if (oldAuthConfInfo.getPropeaplocked().equals("n")) {
		            oldStr = Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null);
		        } else {
		            oldStr = Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null);
		        } 
		        String str = "";
		        if (authConfInfo.getPropeaplocked().equals("n")) {
		            str = Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null);
		        } else{
		            str = Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null);
		        }
		        sbr.append(
		                Language.getLangValue("authconf_locked_peap", Language.getCurrLang(null), null)
		                        + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(oldStr).append(
		                Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(str).append(
		                "</br>");
		        i++;
		    }
        }else{
        	sbr.append(curUser).append(Language.getLangValue("log_auth_record_edit", Language.getCurrLang(null), null))
            .append(Language.getLangValue("log_edit_content", Language.getCurrLang(null), null)).append("</br>");
		    if (!StrTool.strEquals(oldAuthConfInfo.getHotpauthwnd(), authConfInfo.getHotpauthwnd())) {
		        sbr.append(
		                Language.getLangValue("authconf_hotp_auth_wnd", Language.getCurrLang(null), null)
		                        + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
		                oldAuthConfInfo.getHotpauthwnd()).append(
		                Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
		                authConfInfo.getHotpauthwnd()).append("</br>");
		        i++;
		    }
		
		    if (!StrTool.strEquals(oldAuthConfInfo.getTotpauthwnd(), authConfInfo.getTotpauthwnd())) {
		        sbr.append(
		                Language.getLangValue("authconf_totp_auth_wnd", Language.getCurrLang(null), null)
		                        + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
		                oldAuthConfInfo.getTotpauthwnd()).append(
		                Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
		                authConfInfo.getTotpauthwnd()).append("</br>");
		        i++;
		    }
		
		    if (!StrTool.strEquals(oldAuthConfInfo.getHotpadjustwnd(), authConfInfo.getHotpadjustwnd())) {
		        sbr.append(
		                Language.getLangValue("authconf_hotp_adjust_wnd", Language.getCurrLang(null), null)
		                        + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
		                oldAuthConfInfo.getHotpadjustwnd()).append(
		                Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
		                authConfInfo.getHotpadjustwnd()).append("</br>");
		        i++;
		    }
		    if (!StrTool.strEquals(oldAuthConfInfo.getTotpadjustwnd(), authConfInfo.getTotpadjustwnd())) {
		        sbr.append(
		                Language.getLangValue("authconf_totp_adjust_wnd", Language.getCurrLang(null), null)
		                        + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
		                oldAuthConfInfo.getTotpadjustwnd()).append(
		                Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
		                authConfInfo.getTotpadjustwnd()).append("</br>");
		        i++;
		    }
		    if (!StrTool.strEquals(oldAuthConfInfo.getHotpsyncwnd(), authConfInfo.getHotpsyncwnd())) {
		        sbr.append(
		                Language.getLangValue("authconf_hotp_sync_wnd", Language.getCurrLang(null), null)
		                        + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
		                oldAuthConfInfo.getHotpsyncwnd()).append(
		                Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
		                authConfInfo.getHotpsyncwnd()).append("</br>");
		        i++;
		    }
		    if (!StrTool.strEquals(oldAuthConfInfo.getTotpsyncwnd(), authConfInfo.getTotpsyncwnd())) {
		        sbr.append(
		                Language.getLangValue("authconf_totp_sync_wnd", Language.getCurrLang(null), null)
		                        + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
		                oldAuthConfInfo.getTotpsyncwnd()).append(
		                Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
		                authConfInfo.getTotpsyncwnd()).append("</br>");
		        i++;
		    }
		    if (!StrTool.strEquals(oldAuthConfInfo.getWndadjustperiod(), authConfInfo.getWndadjustperiod())) {
		        sbr.append(
		                Language.getLangValue("authconf_wnd_adjust_period", Language.getCurrLang(null), null)
		                        + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
		                oldAuthConfInfo.getWndadjustperiod()).append(
		                Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
		                authConfInfo.getWndadjustperiod()).append("</br>");
		        i++;
		    }
		
		    if (!StrTool.strEquals(oldAuthConfInfo.getWndadjustmode(), authConfInfo.getWndadjustmode())) {
		        String oldStr = "";
		        if ("0".equals(oldAuthConfInfo.getWndadjustmode())) {
		            oldStr = Language.getLangValue("wnd_no_adjust", Language.getCurrLang(null), null);
		        } else if ("1".equals(oldAuthConfInfo.getWndadjustmode())) {
		            oldStr = Language.getLangValue("wnd_exceed_adjust_noadd", Language.getCurrLang(null), null);
		        } else if ("2".equals(oldAuthConfInfo.getWndadjustmode())) {
		            oldStr = Language.getLangValue("wnd_exceed_adjust_add", Language.getCurrLang(null), null);
		        }
		        String str = "";
		        if ("0".equals(authConfInfo.getWndadjustmode())) {
		            str = Language.getLangValue("wnd_no_adjust", Language.getCurrLang(null), null);
		        } else if ("1".equals(authConfInfo.getWndadjustmode())) {
		            str = Language.getLangValue("wnd_exceed_adjust_noadd", Language.getCurrLang(null), null);
		        } else if ("2".equals(authConfInfo.getWndadjustmode())) {
		            str = Language.getLangValue("wnd_exceed_adjust_add", Language.getCurrLang(null), null);
		        }
		        sbr.append(
		                Language.getLangValue("authconf_wnd_adjust_mode", Language.getCurrLang(null), null)
		                        + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(oldStr).append(
		                Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(str).append(
		                "</br>");
		        i++;
		    }
		
		    if (!StrTool.strEquals(oldAuthConfInfo.getRetryotptimeinterval(), authConfInfo.getRetryotptimeinterval())) {
		        sbr.append(
		                Language.getLangValue("authconf_retry_interval", Language.getCurrLang(null), null)
		                        + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
		                oldAuthConfInfo.getRetryotptimeinterval()).append(
		                Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
		                authConfInfo.getRetryotptimeinterval()).append("</br>");
		        i++;
		    }
		    if (!StrTool.strEquals(oldAuthConfInfo.getTemplockretry(), authConfInfo.getTemplockretry())) {
		        sbr.append(
		                Language.getLangValue("authconf_temp_lock_retry", Language.getCurrLang(null), null)
		                        + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
		                oldAuthConfInfo.getTemplockretry()).append(
		                Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
		                authConfInfo.getTemplockretry()).append("</br>");
		        i++;
		    }
		    if (!StrTool.strEquals(oldAuthConfInfo.getTemplockexpire(), authConfInfo.getTemplockexpire())) {
		        sbr.append(
		                Language.getLangValue("authconf_temp_lock_expire", Language.getCurrLang(null), null)
		                        + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
		                oldAuthConfInfo.getTemplockexpire()).append(
		                Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
		                authConfInfo.getTemplockexpire()).append("</br>");
		        i++;
		    }
		    if (!StrTool.strEquals(oldAuthConfInfo.getMaxretry(), authConfInfo.getMaxretry())) {
		        sbr.append(
		                Language.getLangValue("authconf_max_retry", Language.getCurrLang(null), null)
		                        + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
		                oldAuthConfInfo.getMaxretry()).append(
		                Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
		                authConfInfo.getMaxretry()).append("</br>");
		        i++;
		    }
        }
        
        if (i == 0) {
            return null;
        }
        authConfAction.setAuthConfInfo(null);
        authConfAction.setOldAuthConfInfo(null);

        return sbr.toString();
    }

    /**
     * 用户配置日志记录
     * @Date in Jul 30, 2013,11:19:04 AM
     * @param curUser
     * @param userConfAction
     * @return
     */
    public String writeUserConfStr(String curUser, UserConfAction userConfAction) {
        UserConfInfo uconfInfo = userConfAction.getUserConfInfo();
        UserConfInfo olduconfInfo = userConfAction.getOldUserConfInfo();

        if (null == uconfInfo || null == olduconfInfo) {
            return null;
        }

        int i = 0;
        StringBuilder sbr = new StringBuilder();
        String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
        String modified = Language.getLangValue("log_modified_to", Language.getCurrLang(null), null);
        String oper = "";
        if (StrTool.strEquals(olduconfInfo.getOper(), "utknconf")) {
            oper = Language.getLangValue("common_menu_config_usertkn_bind", Language.getCurrLang(null), null);
        } else {
            oper = Language.getLangValue("common_menu_config_user_dfpwd", Language.getCurrLang(null), null);
        }
        sbr.append(curUser).append(oper).append(
                Language.getLangValue("log_edit_content", Language.getCurrLang(null), null)).append("</br>");

        if (StrTool.strEquals(olduconfInfo.getOper(), "utknconf")) {
        	// 每个用户最大可绑定令牌数
            if (!StrTool.strEquals(olduconfInfo.getMaxbindtokens(), uconfInfo.getMaxbindtokens())) {
                sbr.append(
                        Language.getLangValue("userconf_max_bind_tks", Language.getCurrLang(null), null)
                                + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                        olduconfInfo.getMaxbindtokens()).append(
                        Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                        uconfInfo.getMaxbindtokens()).append("</br>");
                i++;
            }
            
            // 绑定用户时，如果用户不存在，是否新增该用户
            if (!StrTool.strEquals(olduconfInfo.getAdduserwhenbind(), uconfInfo.getAdduserwhenbind())) {
                if (uconfInfo.getAdduserwhenbind().equals("y")) {
                    sbr.append(Language.getLangValue("userconf_adduser_when_bind", Language.getCurrLang(null), null))
                    		.append(colon)
                    		.append(Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null))
                            .append(modified)
                            .append(Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null))
                            .append("</br>");
                } else {
                    sbr.append(Language.getLangValue("userconf_adduser_when_bind", Language.getCurrLang(null), null))
                            .append(colon)
                    		.append(Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null))
                            .append(modified)
                            .append(Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null))
                            .append("</br>");
                }
                i++;
            }
            
            //  绑定用户时，是否需要验证令牌的口令
            if (!StrTool.strEquals(olduconfInfo.getMaxbindusers(), uconfInfo.getMaxbindusers())) {
                sbr.append(
                        Language.getLangValue("userconf_authotp_when_bind", Language.getCurrLang(null), null)
                                + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                        olduconfInfo.getMaxbindusers()).append(
                        Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                        uconfInfo.getMaxbindusers()).append("</br>");
                i++;
            }
            // 绑定用户时，是否需要验证令牌的口令
            if (!StrTool.strEquals(olduconfInfo.getAuthotpwhenbind(), uconfInfo.getAuthotpwhenbind())) {
                if (uconfInfo.getAuthotpwhenbind().equals("0")) {
                    sbr
                            .append(Language.getLangValue("userconf_authotp_when_bind", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("common_syntax_no_need", Language.getCurrLang(null), null))
                            .append("</br>");
                } else if (uconfInfo.getAuthotpwhenbind().equals("1")) {
                    sbr		.append(Language.getLangValue("userconf_authotp_when_bind", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("common_syntax_need", Language.getCurrLang(null), null))
                            .append("</br>");
                } else {
                    sbr		.append(Language.getLangValue("userconf_authotp_when_bind", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("common_syntax_optional", Language.getCurrLang(null), null))
                            .append("</br>");
                }
                
                i++;
            }
            
            // 令牌绑定后是否迁移至用户所在的机构
            if (!StrTool.strEquals(olduconfInfo.getTkbindischangeorg(), uconfInfo.getTkbindischangeorg())) {
                if (uconfInfo.getTkbindischangeorg().equals("1")) {
                    sbr.append(Language.getLangValue("userconf_tkbind_ischange_org", Language.getCurrLang(null), null))
                    		.append(colon)
                    		.append(Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null))
                            .append(modified)
                            .append(Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null))
                            .append("</br>");
                } else {
                    sbr.append(Language.getLangValue("userconf_tkbind_ischange_org", Language.getCurrLang(null), null))
                            .append(colon)
                    		.append(Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null))
                            .append(modified)
                            .append(Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null))
                            .append("</br>");
                }
                i++;
            }
            
            // 令牌解绑后是否回到所属的企业
            if (!StrTool.strEquals(olduconfInfo.getTkunbindischangeorg(), uconfInfo.getTkunbindischangeorg())) {
                if (uconfInfo.getTkunbindischangeorg().equals("1")) {
                    sbr.append(
                            Language.getLangValue("userconf_tkunbind_ischange_org", Language.getCurrLang(null), null))
                            .append(colon)
                    		.append(Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null))
                            .append(modified)
                            .append(Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null))
                            .append("</br>");
                } else {
                    sbr.append(
                            Language.getLangValue("userconf_tkunbind_ischange_org", Language.getCurrLang(null), null))
                            .append(colon)
                    		.append(Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null))
                            .append(modified)
                            .append(Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null))
                            .append("</br>");
                }
                i++;
            }
            
            // 解绑令牌，被解绑令牌是否停用
            if (!StrTool.strEquals(olduconfInfo.getUnbindselect(), uconfInfo.getUnbindselect())) {
                if (uconfInfo.getUnbindselect().equals("1")) {
                    sbr.append(Language.getLangValue("userconf_unbind_select", Language.getCurrLang(null), null))
                            .append(colon)
                    		.append(Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null))
                            .append(modified)
                            .append(Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null))
                            .append("</br>");
                } else {
                    sbr.append(Language.getLangValue("userconf_unbind_select", Language.getCurrLang(null), null))
                            .append(colon)
                    		.append(Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null))
                            .append(modified)
                            .append(Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null))
                            .append("</br>");
                }
                i++;
            }
            
         // 更换令牌，设置被更换令牌的状态
            if (!StrTool.strEquals(olduconfInfo.getReplaceselect(), uconfInfo.getReplaceselect())) {
                if (uconfInfo.getReplaceselect().equals("0")) {  //继续使用
                    sbr.append(Language.getLangValue("userconf_replace_select", Language.getCurrLang(null), null))
                    .append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null))
                    .append(Language.getLangValue("user_tkn_rep_continue_use", Language.getCurrLang(null), null))
                    .append("</br>");
                } else if (uconfInfo.getReplaceselect().equals("1")) {//停用
                    sbr.append(Language.getLangValue("userconf_replace_select", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                                    Language.getLangValue("tkn_comm_disable", Language.getCurrLang(null), null))
                            .append("</br>");
                } else {    //作废
                    sbr.append(Language.getLangValue("userconf_replace_select", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                                    Language.getLangValue("tkn_comm_invalid", Language.getCurrLang(null), null))
                            .append("</br>");
                }
                i++;
            }
        } else if (StrTool.strEquals(olduconfInfo.getOper(), "upwdconf")) {
            if (!StrTool.strEquals(olduconfInfo.getDefaultuserpwd(), uconfInfo.getDefaultuserpwd())) {
                sbr.append(
                        Language.getLangValue("userconf_def_user_pwd", Language.getCurrLang(null), null)
                                + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                        olduconfInfo.getDefaultuserpwd()).append(
                        Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                        uconfInfo.getDefaultuserpwd()).append("</br>");
                i++;
            }
            // 默认的本地认证模式
            if (!StrTool.strEquals(olduconfInfo.getDefaultlocalauth(), uconfInfo.getDefaultlocalauth())) {
                if (uconfInfo.getDefaultlocalauth().equals("0")) {
                    sbr.append(Language.getLangValue("userconf_def_localauth", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("local_auth_only_vd_tkn", Language.getCurrLang(null), null))
                            .append("</br>");
                } else if (uconfInfo.getDefaultlocalauth().equals("1")) {
                    sbr.append(Language.getLangValue("userconf_def_localauth", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("local_auth_vd_pwd_tkn", Language.getCurrLang(null), null))
                            .append("</br>");
                } else {
                    sbr.append(Language.getLangValue("userconf_def_localauth", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("local_auth_only_vd_pwd", Language.getCurrLang(null), null))
                            .append("</br>");
                }
                i++;
            }
            // 默认的后端认证模式
            if (!StrTool.strEquals(olduconfInfo.getDefaultbackendauth(), uconfInfo.getDefaultbackendauth())) {
                if (uconfInfo.getDefaultbackendauth().equals("0")) {
                    sbr.append(Language.getLangValue("userconf_def_backendauth", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("backend_auth_default", Language.getCurrLang(null), null))
                            .append("</br>");
                } else if (uconfInfo.getDefaultbackendauth().equals("1")) {
                    sbr.append(Language.getLangValue("userconf_def_backendauth", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("backend_auth_need", Language.getCurrLang(null), null))
                            .append("</br>");
                } else {
                    sbr.append(Language.getLangValue("userconf_def_backendauth", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("backend_auth_no_need", Language.getCurrLang(null), null))
                            .append("</br>");
                }
                i++;
            }
        }

        if (i == 0) {
          //无更新
            sbr.append(Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null))
            .append(Language.getLangValue("adm_perm_000002", Language.getCurrLang(null), null));
        }
        userConfAction.setUserConfInfo(null);
        userConfAction.setOldUserConfInfo(null);

        return sbr.toString();
    }

    /**
     * 令牌配置日志记录
     * @Date in Jul 30, 2013,11:18:39 AM
     * @param curUser
     * @param tknConfAction
     * @return
     */
    public String writeTokenConfStr(String curUser, TokenConfAction tknConfAction) {
        TokenConfInfo tknConfInfo = tknConfAction.getTokenConfInfo();
        TokenConfInfo oldtknConfInfo = tknConfAction.getOldTokenConfInfo();
        if (null == tknConfInfo || null == oldtknConfInfo) {
            return null;
        }

        int i = 0;
        StringBuilder sbr = new StringBuilder();
        String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
        String comma = Language.getLangValue("comma", Language.getCurrLang(null), null);
        String modified = Language.getLangValue("log_modified_to", Language.getCurrLang(null), null);
        String oper = "";
        if (StrTool.strEquals(oldtknConfInfo.getOper(), "softtkn")) {
            oper = Language.getLangValue("common_menu_config_soft_tkn", Language.getCurrLang(null), null);
        } else if (StrTool.strEquals(oldtknConfInfo.getOper(), "mobiletkn")) {
            oper = Language.getLangValue("common_menu_config_mobile_tkn", Language.getCurrLang(null), null);
        } else if (StrTool.strEquals(oldtknConfInfo.getOper(), "smstkn")) {
            oper = Language.getLangValue("common_menu_config_sms_tkn", Language.getCurrLang(null), null);
        } else if (StrTool.strEquals(oldtknConfInfo.getOper(), "emeypin")) {
            oper = Language.getLangValue("common_menu_config_emey_pin", Language.getCurrLang(null), null);
        }
        sbr.append(curUser).append(oper).append(
                Language.getLangValue("log_edit_content", Language.getCurrLang(null), null)).append("</br>");

        if (StrTool.strEquals(oldtknConfInfo.getOper(), "softtkn")) {
            if (!StrTool.strEquals(oldtknConfInfo.getSofttkdistpwd(), tknConfInfo.getSofttkdistpwd())) {
                sbr.append(
                        Language.getLangValue("tknconf_soft_tkn_dist_pin", Language.getCurrLang(null), null)
                                + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                        oldtknConfInfo.getSofttkdistpwd()).append(modified).append(
                        tknConfInfo.getSofttkdistpwd()).append("</br>");
                i++;
            }
        } else if (StrTool.strEquals(oldtknConfInfo.getOper(), "mobiletkn")) {
        	
        	// 激活密码有效周期(天)
            if (!StrTool.strEquals(tknConfInfo.getApperiod(), oldtknConfInfo.getApperiod())) {
                sbr.append(
                        Language.getLangValue("distconf_ap_period", Language.getCurrLang(null), null)
                                + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                        oldtknConfInfo.getApperiod()).append(modified).append(
                        tknConfInfo.getApperiod()).append("</br>");
                i++;
            }
            
            // 激活密码重试次数
            if (!StrTool.strEquals(tknConfInfo.getApretry(), oldtknConfInfo.getApretry())) {
                sbr.append(
                        Language.getLangValue("distconf_ap_retry", Language.getCurrLang(null), null)
                                + colon).append(oldtknConfInfo.getApretry()).append(modified).append(
                        tknConfInfo.getApretry()).append("</br>");
                i++;
            }
            
            // 默认激活密码
            if (!StrTool.strEquals(tknConfInfo.getDefultap(), oldtknConfInfo.getDefultap())) {
                sbr.append(
                        Language.getLangValue("distconf_defult_ap", Language.getCurrLang(null), null)
                                + colon).append(oldtknConfInfo.getDefultap()).append(modified).append(
                        tknConfInfo.getDefultap()).append("</br>");
                i++;
            }
            
            // 启用短信发送离线分发信息
            if(!StrTool.strEquals(tknConfInfo.getApsmssend(), oldtknConfInfo.getApsmssend())){
            	if (tknConfInfo.getApsmssend().equals("1")) {
                    sbr.append(Language.getLangValue("distconf_ap_sms_send", Language.getCurrLang(null), null))
                    		.append(colon)
                    		.append(Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null))
                    		.append(modified)
                            .append(Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null))
                            .append("</br>");
                    
                    // 离线激活短信提示信息
                    sbr.append(Language.getLangValue("distconf_ap_sms_send", Language.getCurrLang(null), null))
                    		.append(colon)
                    		.append(tknConfInfo.getMobileactivatecodemessage())
                    		.append("</br>");
                } else {
                    sbr.append(Language.getLangValue("distconf_ap_sms_send", Language.getCurrLang(null), null))
                    		.append(colon)
                    		.append(Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null))
                    		.append(modified)
                            .append(Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null))
                            .append("</br>");
                }
                i++;
            }else{
            	if(tknConfInfo.getApsmssend().equals("1")){
            		if(!StrTool.strEquals(tknConfInfo.getMobileactivatecodemessage(), oldtknConfInfo.getMobileactivatecodemessage())){
                		// 离线激活短信提示信息
                        sbr.append(Language.getLangValue("distconf_ap_sms_send", Language.getCurrLang(null), null))
                        		.append(colon)
                        		.append(oldtknConfInfo.getMobileactivatecodemessage())
                        		.append(modified)
                        		.append(tknConfInfo.getMobileactivatecodemessage())
                        		.append("</br>");
                        i++;
                	}
            	}
            }
            
            // 启用邮件发送分发信息
            if(!StrTool.strEquals(tknConfInfo.getDistemailsend(), oldtknConfInfo.getDistemailsend())){
            	if (tknConfInfo.getDistemailsend().equals("1")) {
                    sbr.append(Language.getLangValue("distconf_email_send", Language.getCurrLang(null), null))
                    		.append(colon)
                    		.append(Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null))
                    		.append(modified)
                            .append(Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null))
                            .append("</br>");
                } else {
                    sbr.append(Language.getLangValue("distconf_email_send", Language.getCurrLang(null), null))
                    		.append(colon)
                    		.append(Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null))
                    		.append(modified)
                            .append(Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null))
                            .append("</br>");
                }
                i++;
            }
            
            // 分发站点访问类型
            if(!StrTool.strEquals(tknConfInfo.getSitetype(), oldtknConfInfo.getSitetype())){
            	String sitetype = "";
            	if("1".equals(tknConfInfo.getSitetype())){
            		sitetype = "http";
            	}else if("2".equals(tknConfInfo.getSitetype())){
            		sitetype = "https";
            	}else{
            		sitetype = "http;https";
            	}
            	sbr.append(Language.getLangValue("distconf_site_type", Language.getCurrLang(null), null))
            		.append(Language.getLangValue("log_record_modify", Language.getCurrLang(null), null))
            		.append(sitetype).append("</br>");
            	i++;
            }
            
            // 分发站点访问URL
            if(!StrTool.strEquals(tknConfInfo.getSiteurl(), oldtknConfInfo.getSiteurl())){
            	sbr.append(Language.getLangValue("distconf_site_url", Language.getCurrLang(null), null))
            	.append(colon)
                .append(oldtknConfInfo.getSiteurl())
                .append(modified)
                .append(tknConfInfo.getSiteurl())
                .append("</br>");
            	i++;
            }
            
            // 是否启用分发站点
            if (!StrTool.strEquals(tknConfInfo.getSiteenabled(), oldtknConfInfo.getSiteenabled())) {
                if (tknConfInfo.getSiteenabled().equals("y")) {
                    sbr.append(Language.getLangValue("distconf_site_enabled", Language.getCurrLang(null), null))
                    		.append(colon)
                    		.append(Language.getLangValue("common_syntax_close", Language.getCurrLang(null), null))
                            .append(modified)
                            .append(Language.getLangValue("common_syntax_enable", Language.getCurrLang(null), null))
                            .append("</br>");
                } else {
                    sbr.append(Language.getLangValue("distconf_site_enabled", Language.getCurrLang(null), null))
                            .append(colon)
                    		.append(Language.getLangValue("common_syntax_enable", Language.getCurrLang(null), null))
                            .append(modified)
                            .append(Language.getLangValue("common_syntax_close", Language.getCurrLang(null), null))
                            .append("</br>");
                }
                i++;
            }
        } else if (StrTool.strEquals(oldtknConfInfo.getOper(), "smstkn")) {

            if (!StrTool.strEquals(oldtknConfInfo.getSmstokenauthexpire(), tknConfInfo.getSmstokenauthexpire())) {
                sbr.append(
                        Language.getLangValue("smstoken_conf_sms_tkn_auth_expire", Language.getCurrLang(null),
                                null)).append(colon).append(oldtknConfInfo.getSmstokenauthexpire()).append(modified)
                                .append(tknConfInfo.getSmstokenauthexpire()).append("</br>");
                i++;
            }

            if (!StrTool.strEquals(oldtknConfInfo.getSmstokengenexpire(), tknConfInfo.getSmstokengenexpire())) {
                sbr.append(
                        Language.getLangValue("smstoken_conf_sms_tkn_gen_expire", Language.getCurrLang(null), null)
                                + colon).append(oldtknConfInfo.getSmstokengenexpire()).append(modified)
                                .append(tknConfInfo.getSmstokengenexpire()).append("</br>");
                i++;
            }

            if (!StrTool.strEquals(oldtknConfInfo.getSmsotpseedmessage(), tknConfInfo.getSmsotpseedmessage())) {
                sbr.append(
                        Language.getLangValue("smstoken_conf_sms_otp_seed_message", Language.getCurrLang(null), null)
                                + colon).append(oldtknConfInfo.getSmsotpseedmessage()).append(modified)
                                .append(tknConfInfo.getSmsotpseedmessage()).append("</br>");
                i++;
            }
        } else if (StrTool.strEquals(oldtknConfInfo.getOper(), "emeypin")) {
            if (!StrTool.strEquals(oldtknConfInfo.getTokenempin2otp(), tknConfInfo.getTokenempin2otp())) {
                if (tknConfInfo.getTokenempin2otp().equals("1")) {
                    sbr.append(
                            Language.getLangValue("emey_empin2otp", Language.getCurrLang(null), null)
                                    + colon).append(
                            Language.getLangValue("log_record_select_the", Language.getCurrLang(null), null)).append(
                            Language.getLangValue("emey_empin2otp_sel", Language.getCurrLang(null), null)).append(
                            "</br>");
                } else {
                    sbr.append(
                            Language.getLangValue("emey_empin2otp", Language.getCurrLang(null), null)
                                    + colon).append(Language.getLangValue("log_record_select_the", Language.getCurrLang(null), null)).append(
                            Language.getLangValue("emey_empin2otp_sel2", Language.getCurrLang(null), null)).append(
                            "</br>");
                }
                i++;
            }

            if (!StrTool.strEquals(oldtknConfInfo.getEpassdefvalidtime(), tknConfInfo.getEpassdefvalidtime())) {
                sbr.append(
                        Language.getLangValue("emey_pass_def_validtime", Language.getCurrLang(null), null)
                                + colon).append(oldtknConfInfo.getEpassdefvalidtime()).append(modified)
                                .append(tknConfInfo.getEpassdefvalidtime()).append("</br>");
                i++;
            }
            if (!StrTool.strEquals(oldtknConfInfo.getEpassmaxvalidtime(), tknConfInfo.getEpassmaxvalidtime())) {
                sbr.append(
                        Language.getLangValue("emey_pass_max_validtime", Language.getCurrLang(null), null)
                                + colon).append(oldtknConfInfo.getEpassmaxvalidtime()).append(modified)
                                .append(tknConfInfo.getEpassmaxvalidtime()).append("</br>");
                i++;
            }
        }

        if (i == 0) {
            return null;
        }
        tknConfAction.setTokenConfInfo(null);
        tknConfAction.setOldTokenConfInfo(null);
        return sbr.toString();
    }

    /**
     * 监控预警配置封装
     * @param operater
     * @param monitorConfAction
     * @return
     */
    public String writeMonitorConfStr(String operater, MonitorConfAction monitorConfAction) {
        StringBuilder sbr = new StringBuilder();
        MonitorConf monitorConf = monitorConfAction.getMonitorConfInfo();
        MonitorConf oldMonitorConf = monitorConfAction.getOldMonitorConfInfo();
        int i = 0;
        sbr.append(Language.getLangValue("common_menu_config_monitor_conf", Language.getCurrLang(null), null)).append(
                Language.getLangValue("log_edit_content", Language.getCurrLang(null), null)).append("</br>");
        if (monitorConf == null || oldMonitorConf == null) {
            return null;
        }

        // 是否启用预警
        if (!StrTool.strEquals(oldMonitorConf.getBaseenabled(), monitorConf.getBaseenabled())) {
            if (StrTool.strEquals("1", monitorConf.getBaseenabled())) { // 启用
                sbr.append(Language.getLangValue("monitor_whether_enable", Language.getCurrLang(null), null))
                		.append(Language.getLangValue("colon", Language.getCurrLang(null), null))
                		.append(Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null))
                		.append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null))
                		.append(Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null))
                		.append("</br>");
            } else {
            	sbr.append(Language.getLangValue("monitor_whether_enable", Language.getCurrLang(null), null))
		        		.append(Language.getLangValue("colon", Language.getCurrLang(null), null))
		        		.append(Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null))
		        		.append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null))
		        		.append(Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null))
		        		.append("</br>");
            }
            i++;
        }

        // 预警方式
        if (!StrTool.strEquals(oldMonitorConf.getBasesendtype(), monitorConf.getBasesendtype())) {
            if (StrTool.strEquals("0", monitorConf.getBasesendtype())) { // 邮件
                sbr.append(
                        Language.getLangValue("monitor_method", Language.getCurrLang(null), null)
                                + Language.getLangValue("comma", Language.getCurrLang(null), null)).append(
                        Language.getLangValue("log_record_modify", Language.getCurrLang(null), null)).append(
                        Language.getLangValue("monitor_email", Language.getCurrLang(null), null)).append("</br>");
            } else {
                sbr.append(
                        Language.getLangValue("monitor_method", Language.getCurrLang(null), null)
                                + Language.getLangValue("comma", Language.getCurrLang(null), null)).append(
                        Language.getLangValue("log_record_modify", Language.getCurrLang(null), null)).append(
                        Language.getLangValue("monitor_sms", Language.getCurrLang(null), null)).append("</br>");
            }
            i++;
        }

        // 预警接收人
        Arrays.sort(oldMonitorConf.getBaserecvusers()); // 排序
        Arrays.sort(monitorConf.getBaserecvusers());
        if (!Arrays.equals(oldMonitorConf.getBaserecvusers(), monitorConf.getBaserecvusers())) {

            String adminUser = "";
            for (int j = 0; j < monitorConf.getBaserecvusers().length; j++) {
                adminUser += monitorConf.getBaserecvusers()[j] + ",";
            }
            adminUser = adminUser.substring(0, adminUser.length() - 1);
            sbr.append(
                    Language.getLangValue("monitor_warning_receiver", Language.getCurrLang(null), null)
                            + Language.getLangValue("comma", Language.getCurrLang(null), null)).append(
                    Language.getLangValue("log_record_modify", Language.getCurrLang(null), null)).append(adminUser)
                    .append("</br>");
            i++;
        }

        // 令牌将要过期天数
        if (!StrTool.strEquals(oldMonitorConf.getBaseremaindays(), monitorConf.getBaseremaindays())) {
            sbr.append(
                    Language.getLangValue("monitor_tkn_expire", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                    oldMonitorConf.getBaseremaindays()).append(
                    Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                    monitorConf.getBaseremaindays()).append("</br>");
            i++;
        }

        // 未绑定的令牌比例
        if (!StrTool.strEquals(oldMonitorConf.getBaseunbindlower(), monitorConf.getBaseunbindlower())) {
            sbr.append(
                    Language.getLangValue("monitor_unbound_tkn_rate", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                    oldMonitorConf.getBaseunbindlower()).append(
                    Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                    monitorConf.getBaseunbindlower()).append("</br>");
            i++;
        }

        // 1小时内令牌同步比例（次数/用户数）
        if (!StrTool.strEquals(oldMonitorConf.getBasesyncupper(), monitorConf.getBasesyncupper())) {
            sbr.append(
                    Language.getLangValue("monitor_tkn_within_one_hour_sync_rate", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                    oldMonitorConf.getBasesyncupper()).append(
                    Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                    monitorConf.getBasesyncupper()).append("</br>");
            i++;
        }

        if (i == 0) {
            return null;
        }

        monitorConfAction.setMonitorConfInfo(null);
        monitorConfAction.setOldMonitorConfInfo(null);

        return sbr.toString();
    }
    
    
    /**
     * 双机热备运行监控预警配置封装
     * @param operater
     * @param monitorConfAction
     * @return
     */
    public String writeMonitorConfigStr(String operater, ConfMonitorConfigAction monitorConfigAction) {
        StringBuilder sbr = new StringBuilder();
        MonitorConfig monitorConfig = monitorConfigAction.getMonitorConfig();
        MonitorConfig oldMonitorConfig = monitorConfigAction.getOldMonitorConfInfo();
        int i = 0;
        sbr.append(Language.getLangValue("common_menu_config_heart_beat_monitor", Language.getCurrLang(null), null)).append(
                Language.getLangValue("log_edit_content", Language.getCurrLang(null), null)).append("</br>");
        if (monitorConfig == null || oldMonitorConfig == null) {
            return null;
        }
        
        // 是否启用预警
        if (!StrTool.strEquals(oldMonitorConfig.getEnabled(), monitorConfig.getEnabled())) {
            if (StrTool.strEquals("1", monitorConfig.getEnabled())) { // 启用
                sbr.append(Language.getLangValue("monitor_whether_enable", Language.getCurrLang(null), null))
                        .append(Language.getLangValue("colon", Language.getCurrLang(null), null))
                        .append(Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null))
                        .append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null))
                        .append(Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null))
                        .append("</br>");
            } else {//禁用
                sbr.append(Language.getLangValue("monitor_whether_enable", Language.getCurrLang(null), null))
                        .append(Language.getLangValue("colon", Language.getCurrLang(null), null))
                        .append(Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null))
                        .append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null))
                        .append(Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null))
                        .append("</br>");
            }
            i++;
        }

        // 预警方式
        if (!StrTool.strEquals(oldMonitorConfig.getSendtype(), monitorConfig.getSendtype())) {
            if (StrTool.strEquals("0", monitorConfig.getSendtype())) { //邮件
                sbr.append(
                        Language.getLangValue("monitor_method", Language.getCurrLang(null), null))
                        .append(Language.getLangValue("colon", Language.getCurrLang(null), null)+
                                Language.getLangValue("monitor_sms", Language.getCurrLang(null), null)
                                + Language.getLangValue("comma", Language.getCurrLang(null), null)).append(
                        Language.getLangValue("log_record_modify", Language.getCurrLang(null), null)).append(
                        Language.getLangValue("monitor_email", Language.getCurrLang(null), null)).append("</br>");
            } else {//短信
                sbr.append(
                        Language.getLangValue("monitor_method", Language.getCurrLang(null), null))
                        .append(Language.getLangValue("colon", Language.getCurrLang(null), null)+
                                Language.getLangValue("monitor_email", Language.getCurrLang(null), null)
                                + Language.getLangValue("comma", Language.getCurrLang(null), null)).append(
                        Language.getLangValue("log_record_modify", Language.getCurrLang(null), null)).append(
                        Language.getLangValue("monitor_sms", Language.getCurrLang(null), null)).append("</br>");
            }
            i++;
        }

        // 预警接收人
        Arrays.sort(oldMonitorConfig.getBaserecvusers()); // 排序
        Arrays.sort(monitorConfig.getBaserecvusers());
        if (!Arrays.equals(oldMonitorConfig.getBaserecvusers(), monitorConfig.getBaserecvusers())) {

            String adminUser = "";
            for (int j = 0; j < monitorConfig.getBaserecvusers().length; j++) {
                adminUser += monitorConfig.getBaserecvusers()[j] + ",";
            }
            adminUser = adminUser.substring(0, adminUser.length() - 1);
            sbr.append(
                    Language.getLangValue("monitor_warning_receiver", Language.getCurrLang(null), null)
                            + Language.getLangValue("comma", Language.getCurrLang(null), null)).append(
                    Language.getLangValue("log_record_modify", Language.getCurrLang(null), null)).append(adminUser)
                    .append("</br>");
            i++;
        }

        // 主服务器IP地址
        if (!StrTool.strEquals(oldMonitorConfig.getMainIpAddress(), monitorConfig.getMainIpAddress())) {
            sbr.append(
                    Language.getLangValue("heart_beat_warn_main_ip", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                                    oldMonitorConfig.getMainIpAddress()).append(
                    Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                            monitorConfig.getMainIpAddress()).append("</br>");
            i++;
        }

        // 从服务器IP地址
        if (!StrTool.strEquals(oldMonitorConfig.getSpareIpAddress(), monitorConfig.getSpareIpAddress())) {
            sbr.append(
                    Language.getLangValue("heart_beat_warn_spare_ip", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                    oldMonitorConfig.getSpareIpAddress()).append(
                    Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                    monitorConfig.getSpareIpAddress()).append("</br>");
            i++;
        }

        // 定时器时间间隔（分）
        if (!StrTool.strEquals(oldMonitorConfig.getTimeInterval(), monitorConfig.getTimeInterval())) {
            sbr.append(
                    Language.getLangValue("monitor_timer_interval", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                                    oldMonitorConfig.getTimeInterval()).append(
                    Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                            monitorConfig.getTimeInterval()).append("</br>");
            i++;
        }

        if (i == 0) {
            return null;
        }

        monitorConfigAction.setMonitorConfig(null);
        monitorConfigAction.setOldMonitorConfInfo(null);

        return sbr.toString();
    }

    /**
     * 
     * 方法说明
     * 管理中心配置日志封装
     * @Date in Mar 27, 2013,8:21:04 PM
     * @param coreConfInfo
     * @param oldCoreConfInfo
     * @return
     */
    public String writeCenterConfStr(String operater, CenterAction centerAction) {
        CenterConfInfo centerConfInfo = centerAction.getCenterInfo();
        CenterConfInfo oldCenterConfInfo = centerAction.getOldcenterInfo();
        int i = 0;
        StringBuilder sbr = new StringBuilder();
        String oper = "";
        if (StrTool.strEquals(oldCenterConfInfo.getOper(), "adminconf")) {
            oper = Language.getLangValue("common_menu_config_admin", Language.getCurrLang(null), null);
        } else if (StrTool.strEquals(oldCenterConfInfo.getOper(), "authser")) {
            oper = Language.getLangValue("common_menu_config_auth_ser_sel", Language.getCurrLang(null), null);
        }
        sbr.append(operater).append(oper).append(
                Language.getLangValue("log_edit_content", Language.getCurrLang(null), null)).append("</br>");

        if (centerConfInfo == null || oldCenterConfInfo == null) {
            return null;
        }

        if (StrTool.strEquals(oldCenterConfInfo.getOper(), "adminconf")) {

            if (!StrTool.strEquals(oldCenterConfInfo.getLoginerrorretrytemp(), centerConfInfo.getLoginerrorretrytemp())) {
                sbr.append(
                        Language.getLangValue("center_login_error_retry", Language.getCurrLang(null), null)
                                + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                        oldCenterConfInfo.getLoginerrorretrytemp()).append(
                        Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                        centerConfInfo.getLoginerrorretrytemp()).append("</br>");
                i++;
            }
            if (!StrTool.strEquals(oldCenterConfInfo.getLoginerrorretrylong(), centerConfInfo.getLoginerrorretrylong())) {
                sbr.append(
                        Language.getLangValue("center_login_err_retry_long", Language.getCurrLang(null), null)
                                + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                        oldCenterConfInfo.getLoginerrorretrylong()).append(
                        Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                        centerConfInfo.getLoginerrorretrylong()).append("</br>");
                i++;
            }
            if (!StrTool.strEquals(oldCenterConfInfo.getLoginlockexpire(), centerConfInfo.getLoginlockexpire())) {
                sbr.append(
                        Language.getLangValue("center_login_lock_expire", Language.getCurrLang(null), null)
                                + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                        oldCenterConfInfo.getLoginlockexpire()).append(
                        Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                        centerConfInfo.getLoginlockexpire()).append("</br>");
                i++;
            }
            if (!StrTool.strEquals(oldCenterConfInfo.getPasswdupdateperiod(), centerConfInfo.getPasswdupdateperiod())) {
                sbr.append(
                        Language.getLangValue("center_update_period", Language.getCurrLang(null), null)
                                + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                        oldCenterConfInfo.getPasswdupdateperiod()).append(
                        Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                        centerConfInfo.getPasswdupdateperiod()).append("</br>");
                i++;
            }

            if (!StrTool.strEquals(oldCenterConfInfo.getProhibitadmin(), centerConfInfo.getProhibitadmin())) {
                if (centerConfInfo.getProhibitadmin().equals("y")) {
                    sbr.append(
                            Language.getLangValue("center_prohibit_admin", Language.getCurrLang(null), null)
                                    + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                            Language.getLangValue("log_record_modify", Language.getCurrLang(null), null)).append(
                            Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null)).append(
                            "</br>");
                } else {
                    sbr.append(
                            Language.getLangValue("center_prohibit_admin", Language.getCurrLang(null), null)
                                    + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                            Language.getLangValue("log_record_modify", Language.getCurrLang(null), null)).append(
                            Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null))
                            .append("</br>");
                }
                i++;
            }
        } else if (StrTool.strEquals(oldCenterConfInfo.getOper(), "authser")) {

            if (!StrTool.strEquals(oldCenterConfInfo.getMainhostipaddr(), centerConfInfo.getMainhostipaddr())) {
                sbr.append(
                        Language.getLangValue("center_main_hostipaddr", Language.getCurrLang(null), null)
                                + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                        oldCenterConfInfo.getMainhostipaddr()).append(
                        Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                        centerConfInfo.getMainhostipaddr()).append("</br>");
                i++;
            }
            if (!StrTool.strEquals(oldCenterConfInfo.getSparehostipaddr(), centerConfInfo.getSparehostipaddr())) {
                sbr.append(
                        Language.getLangValue("center_spare_hostipaddr", Language.getCurrLang(null), null)
                                + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                        oldCenterConfInfo.getSparehostipaddr()).append(
                        Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                        centerConfInfo.getSparehostipaddr()).append("</br>");
                i++;
            }
        } else if (StrTool.strEquals(oldCenterConfInfo.getOper(), "trustip")) {
            if (!StrTool.strEquals(oldCenterConfInfo.getTrustipenabled(), centerConfInfo.getTrustipenabled())) {
                String operText = Language.getLangValue("trustip_ip_access_cont_strategy", Language.getCurrLang(null),
                        null)
                        + Language.getLangValue("common_syntax_status", Language.getCurrLang(null), null);
                if (centerConfInfo.getTrustipenabled().equals("0")) {
                		sbr.append(operText + Language.getLangValue("colon", Language.getCurrLang(null), null))
                			.append(Language.getLangValue("common_syntax_enable", Language.getCurrLang(null), null))
                			.append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null))
                			.append(Language.getLangValue("common_syntax_disabled", Language.getCurrLang(null), null))
                            .append("</br>");
                } else {
                    sbr.append(operText + Language.getLangValue("colon", Language.getCurrLang(null), null))
                    		.append(Language.getLangValue("common_syntax_disabled", Language.getCurrLang(null), null))
                			.append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null))
                    		.append(Language.getLangValue("common_syntax_enable", Language.getCurrLang(null), null))
                            .append("</br>");
                }
                i++;
            }
        }

        if (i == 0) {
            return null;
        }

        centerAction.setCenterInfo(null);
        centerAction.setOldcenterInfo(null);

        return sbr.toString();
    }

    /**
     * 用户门户日志记录
     */
    public String writePortalConfStr(String operater, PortalConfAction portalConfAction) {
        PortalInfo portalInfo = portalConfAction.getPortalInfo();
        PortalInfo oldPortalInfo = portalConfAction.getOldPortalInfo();
        int i = 0;
        StringBuilder sbr = new StringBuilder();
        sbr.append(operater).append(Language.getLangValue("log_portal_record_edit", Language.getCurrLang(null), null))
                .append(Language.getLangValue("log_edit_content", Language.getCurrLang(null), null)).append("</br>");
        if (portalInfo == null || oldPortalInfo == null) {
            return null;
        }
        //登陆初始密码 日志记录
        if(StrTool.strEquals(oldPortalInfo.getOper(), "initpwd")){
        	Object[] objs = getLogDescContent(sbr, portalInfo, oldPortalInfo,i);
        	i = (Integer)objs[0];	//
        	sbr = (StringBuilder)objs[1];
        }else if(StrTool.strEquals(oldPortalInfo.getOper(), "conf")){
        	// 用户门户
        	//启用用户门户
            if (!StrTool.strEquals(portalInfo.getSelfservice(), oldPortalInfo.getSelfservice())) {
                if (portalInfo.getSelfservice().equals("0")) {
                    sbr.append(Language.getLangValue("portal_self_service_enable", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("colon", Language.getCurrLang(null), null))
                    		.append(Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null))
                    		.append(Language.getLangValue("comma", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("log_record_modify", Language.getCurrLang(null), null)).append(
                                    Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null)).append(
                                    "</br>");
                } else {
                    sbr.append(Language.getLangValue("portal_self_service_enable", Language.getCurrLang(null), null))
                    		.append(Language.getLangValue("colon", Language.getCurrLang(null), null))
                    		.append(Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null))
                    		.append(Language.getLangValue("comma", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("log_record_modify", Language.getCurrLang(null), null)).append(
                                    Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null)).append(
                                    "</br>");
                }
                i++;
            }else{
            	//启用用户门户：否
            	if ("0".equals(portalInfo.getSelfservice())) {
                    sbr.append(Language.getLangValue("portal_self_service_enable", Language.getCurrLang(null), null))
                    		.append(Language.getLangValue("colon", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null)).append(
                                    "</br>");
                } 
            	//启用用户门户：是
            	else {
                    sbr.append(Language.getLangValue("portal_self_service_enable", Language.getCurrLang(null), null))
                    		.append(Language.getLangValue("colon", Language.getCurrLang(null), null))
                            .append(Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null)).append(
                                    "</br>");
                }
                i++;
            }
          //用户门户开启功能
            if(!StrTool.strEquals(portalInfo.getOpenfunctionconfig().trim(),
            		oldPortalInfo.getOpenfunctionconfig().trim()) && !"".equals(portalInfo.getOpenfunctionconfig().trim())){
            	//获取"用户门户开启功能"的功能列表
            	String openFunsCode = portalInfo.getOpenfunctionconfig();
            	if(!"".equals(openFunsCode.trim())){
            		//解析国际化编码
                	String interOpenFuncStr = analysisCode2Inter(openFunsCode);
                	
                	//用户门户开启功能
        			sbr.append(Language.getLangValue("portal_open_fun_config", Language.getCurrLang(null), null));
        			//：
        			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        			sbr.append(interOpenFuncStr);
        			sbr.append("</br>");
        			i++;
            	}
            }
            //若相同则展示原有开启功能内容
            else{
            	//启用用户门户开启功能时记录日志
            	if (portalInfo.getSelfservice().equals("1")) {
            		//获取"用户门户开启功能"的功能列表
                	String openFunsCode = portalInfo.getOpenfunctionconfig();
                	String interOpenFuncStr = null;
                	if("".equals(openFunsCode)){
                		interOpenFuncStr = "[无]";
                	}else{
                		interOpenFuncStr = analysisCode2Inter(openFunsCode);
                	}
                	
                	//用户门户开启功能
        			sbr.append(Language.getLangValue("portal_open_fun_config", Language.getCurrLang(null), null));
        			//：
        			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        			sbr.append(interOpenFuncStr);
        			sbr.append("</br>");
        			i++;
            	}
            }
        	//密码找回邮件链接有效周期（小时）
        	if (!StrTool.strEquals(portalInfo.getPwdemailactiveexpire(), oldPortalInfo.getPwdemailactiveexpire())) {
                sbr.append(
                        Language.getLangValue("portal_pwd_email_expire", Language.getCurrLang(null), null)
                                + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                        oldPortalInfo.getPwdemailactiveexpire()).append(
                        Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                        portalInfo.getPwdemailactiveexpire()).append("</br>");
                i++;
            }else{
            	 sbr.append(
                         Language.getLangValue("portal_pwd_email_expire", Language.getCurrLang(null), null)
                                 + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                         portalInfo.getPwdemailactiveexpire()).append("</br>");
                 i++;
            }
        }

        if (i == 0) {
            return null;
        }

        portalConfAction.setPortalInfo(null);
        portalConfAction.setOldPortalInfo(null);

        return sbr.toString();
    }
    
    /**
     * 获取用户门户开启功能编码
     * @author ZWX
     * @param openFunsCode
     * @return
     */
    public String analysisCode2Inter(String openFunsCode){
    	
    	//获取 功能code 数组
    	String[] opens = openFunsCode.split(",");
    	String[] opensInters = null;
    	if(opens != null && opens.length != 0){
    		//创建国家化数组
    		opensInters = new String[opens.length];
    		
    		for(int i=0 ; i < opens.length; i++){
    			opensInters[i] = getOpenfunctionInter(opens[i]);
        	}
    	}
    	return Arrays.toString(opensInters);
    }
    
    /**
     * 获取用户开启功能国际化代码
     * @author ZWX
     * @param index
     * @return
     */
    public String getOpenfunctionInter(String keyStr){
    	int key = Integer.parseInt(keyStr);
    	String international = null;
    	switch (key) {
		case 1001:
			international = "portal_bind_user_tkn";
			break;
		case 1002:
			international = "portal_modify_pin";
			break;
		case 1003:
			international = "portal_replace_tkn";
			break;
		case 1004:
			international = "portal_unlock_tkn";
			break;
		case 1005:
			international = "portal_tkn_activation";
			break;
		case 1006:
			international = "portal_tkn_synch";
			break;
		case 1007:
			international = "portal_tkn_losed";
			break;
		case 1008:
			international = "portal_tkn_unlosed";
			break;
		case 1009:
			international = "portal_get_an_unlock_code";
			break;
		case 1010:
			international = "portal_get_two_unlockcode";
			break;
		case 1011:
			//lang_common_zh_CN.properties 
			international = "common_menu_tkn_mobile_dist";
			break;
		case 1012:
			international = "portal_self_binding_tel_tk";
			break;
		case 1013:
			international = "portal_sms_tkn_dist";
			break;
		case 1014:
			international = "portal_self_binding_sms_tk";
			break;
		case 1015:
			international = "portal_software_tkn_dist";
			break;
		case 1016:
			international = "portal_self_binding_soft_tk";
			break;
		case 1017:
			international = "portal_tkn_auth_test";
			break;
		case 1018:
			international = "portal_trans_sign_auth_test";
			break;
		case 1019:
			international = "portal_edit_uinfo";
			break;
		}
    	return Language.getLangValue(international, Language.getCurrLang(null), null);
    }
    
    /**
     * 用户门户配置-登陆初始密码 日志记录
     * @author ZWX
     * @param sbr 日志内容
     * @param portalInfo 门户配置新对象
     * @param oldPortalInfo 门户配置旧对象
     * @param i 标记值（int）
     * @return
     */
    public Object[] getLogDescContent(StringBuilder sbr,PortalInfo portalInfo,PortalInfo oldPortalInfo,int i){
    	Object[] objs = new Object[2];

    	objs[0] = i;
    	//1:默认密码,2:邮件激活,3:短信验证,4:AD密码验证
    	//初始密码登录验证方式 发生变化
    	if(!StrTool.strEquals(portalInfo.getInitpwdloginverifytype(),oldPortalInfo.getInitpwdloginverifytype())){
    		//修改为 默认密码时
    		if("1".equals(portalInfo.getInitpwdloginverifytype())){
    			//初始密码登录验证方式
    			sbr.append(Language.getLangValue("portal_init_pwd_login_verify_type", Language.getCurrLang(null), null));
    			//，修改为：
    			sbr.append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null));
    			//默认密码
    			sbr.append(Language.getLangValue("portal_init_pwd_defpwd", Language.getCurrLang(null), null));
        		sbr.append("</br>");
    		}
    		//邮箱验证
    		else if("2".equals(portalInfo.getInitpwdloginverifytype())){
    			//初始密码登录验证方式
    			sbr.append(Language.getLangValue("portal_init_pwd_login_verify_type", Language.getCurrLang(null), null));
    			//，修改为：
    			sbr.append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null));
    			//邮件发送激活密码
    			sbr.append(Language.getLangValue("portal_init_pwd_send_email", Language.getCurrLang(null), null));
        		sbr.append("</br>");
    			
        		if(!StrTool.strEquals(portalInfo.getInitpwdemailactexpire(), oldPortalInfo.getInitpwdemailactexpire())){
        			//激活密码验证有效期（小时）
        			sbr.append(Language.getLangValue("portal_init_pwd_email_expire", Language.getCurrLang(null), null));
        			//：
        			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        			sbr.append(oldPortalInfo.getInitpwdemailactexpire());
        			//，修改为：
        			sbr.append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null));
        			sbr.append(portalInfo.getInitpwdemailactexpire());
        		}else{
        			//激活密码验证有效期（小时）
        			sbr.append(Language.getLangValue("portal_init_pwd_email_expire", Language.getCurrLang(null), null));
        			//：
        			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        			sbr.append(portalInfo.getInitpwdemailactexpire());
        		}
    		}
    		//短信发送验证码
    		else if("3".equals(portalInfo.getInitpwdloginverifytype())){
    			//初始密码登录验证方式
    			sbr.append(Language.getLangValue("portal_init_pwd_login_verify_type", Language.getCurrLang(null), null));
    			//，修改为：
    			sbr.append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null));
    			//短信发送验证码
    			sbr.append(Language.getLangValue("portal_init_pwd_send_sms", Language.getCurrLang(null), null));
        		sbr.append("</br>");
        		if(!StrTool.strEquals(portalInfo.getInitpwdsmsverifyexpire(), oldPortalInfo.getInitpwdsmsverifyexpire())){
        			//短信码验证有效期（分钟）
        			sbr.append(Language.getLangValue("portal_init_pwd_sms_expire", Language.getCurrLang(null), null));
        			//：
        			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        			sbr.append(oldPortalInfo.getInitpwdsmsverifyexpire());
        			//，修改为：
        			sbr.append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null));
        			sbr.append(portalInfo.getInitpwdsmsverifyexpire());
        		}else{
        			//短信码验证有效期（分钟）
        			sbr.append(Language.getLangValue("portal_init_pwd_sms_expire", Language.getCurrLang(null), null));
        			//：
        			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        			sbr.append(portalInfo.getInitpwdsmsverifyexpire());
        		}
    		}
    		//AD密码验证
    		else if("4".equals(portalInfo.getInitpwdloginverifytype())){
    			//初始密码登录验证方式
    			sbr.append(Language.getLangValue("portal_init_pwd_login_verify_type", Language.getCurrLang(null), null));
    			//，修改为：
    			sbr.append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null));
    			//AD密码验证
    			sbr.append(Language.getLangValue("portal_init_pwd_verify_ad_pwd", Language.getCurrLang(null), null));
        		sbr.append("</br>");
        		
        		if(!StrTool.strEquals(portalInfo.getAdserverip(), oldPortalInfo.getAdserverip())){
        			//服务器IP
        			sbr.append(Language.getLangValue("portal_ad_verify_pwd_ip", Language.getCurrLang(null), null));
        			//：
        			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        			if(StrTool.strNotNull(oldPortalInfo.getAdserverip())){
        				sbr.append(oldPortalInfo.getAdserverip());
        			}else{
        				sbr.append(Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null));
        			}
        			//，修改为：
        			sbr.append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null));
        			sbr.append(portalInfo.getAdserverip());
        			sbr.append("</br>");
        		}else{
        			//服务器IP
        			sbr.append(Language.getLangValue("portal_ad_verify_pwd_ip", Language.getCurrLang(null), null));
        			//：
        			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        			sbr.append("</br>");
        		}
        		if(!StrTool.strEquals(portalInfo.getAdserverport(), oldPortalInfo.getAdserverport())){
        			//服务器端口
        			sbr.append(Language.getLangValue("portal_ad_verify_pwd_port", Language.getCurrLang(null), null));
        			//：
        			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        			sbr.append(oldPortalInfo.getAdserverport());
        			//，修改为：
        			sbr.append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null));
        			sbr.append(portalInfo.getAdserverport());
        			sbr.append("</br>");
        		}else{
        			//服务器端口
        			sbr.append(Language.getLangValue("portal_ad_verify_pwd_port", Language.getCurrLang(null), null));
        			//：
        			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        			sbr.append(portalInfo.getAdserverport());
        			sbr.append("</br>");
        		}
        		if(!StrTool.strEquals(portalInfo.getAdserverdn(), oldPortalInfo.getAdserverdn())){
        			//根DN
        			sbr.append(Language.getLangValue("usource_vd_root_dn", Language.getCurrLang(null), null));
        			//：
        			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        			sbr.append(oldPortalInfo.getAdserverdn());
        			//，修改为：
        			sbr.append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null));
        			sbr.append(portalInfo.getAdserverdn());
        			sbr.append("</br>");
        		}else{
        			//根DN
        			sbr.append(Language.getLangValue("usource_vd_root_dn", Language.getCurrLang(null), null));
        			//：
        			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        			sbr.append(portalInfo.getAdserverdn());
        			sbr.append("</br>");
        		}
    		}
    		objs[0] = ++i;
    	}
    	//若初始密码登录验证方式相同
    	else{
    		//邮箱验证
    		if("2".equals(portalInfo.getInitpwdloginverifytype())){
    			
    			//初始密码登录验证方式
    			sbr.append(Language.getLangValue("portal_init_pwd_login_verify_type", Language.getCurrLang(null), null));
    			//：
    			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
    			//邮件发送激活密码
    			sbr.append(Language.getLangValue("portal_init_pwd_send_email", Language.getCurrLang(null), null));
        		sbr.append("</br>");
    			
        		if(!StrTool.strEquals(portalInfo.getInitpwdemailactexpire(), oldPortalInfo.getInitpwdemailactexpire())){
        			//激活密码验证有效期（小时）
        			sbr.append(Language.getLangValue("portal_init_pwd_email_expire", Language.getCurrLang(null), null));
        			//：
        			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        			sbr.append(oldPortalInfo.getInitpwdemailactexpire());
        			//，修改为：
        			sbr.append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null));
        			sbr.append(portalInfo.getInitpwdemailactexpire());
        		}else{
        			//激活密码验证有效期（小时）
        			sbr.append(Language.getLangValue("portal_init_pwd_email_expire", Language.getCurrLang(null), null));
        			//：
        			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        			sbr.append(portalInfo.getInitpwdemailactexpire());
        			sbr.append("</br>");
        		}
        		objs[0] = ++i;
    		}
    		//短信发送验证码
    		else if("3".equals(portalInfo.getInitpwdloginverifytype())){
    			//初始密码登录验证方式
    			sbr.append(Language.getLangValue("portal_init_pwd_login_verify_type", Language.getCurrLang(null), null));
    			//：
    			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
    			//短信发送验证码
    			sbr.append(Language.getLangValue("portal_init_pwd_send_sms", Language.getCurrLang(null), null));
        		sbr.append("</br>");
    			
        		if(!StrTool.strEquals(portalInfo.getInitpwdsmsverifyexpire(), oldPortalInfo.getInitpwdsmsverifyexpire())){
        			//短信码验证有效期（分钟）
        			sbr.append(Language.getLangValue("portal_init_pwd_sms_expire", Language.getCurrLang(null), null));
        			//：
        			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        			sbr.append(oldPortalInfo.getInitpwdsmsverifyexpire());
        			//，修改为：
        			sbr.append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null));
        			sbr.append(portalInfo.getInitpwdsmsverifyexpire());
        			sbr.append("</br>");
        		}else{
        			//短信码验证有效期（分钟）
        			sbr.append(Language.getLangValue("portal_init_pwd_sms_expire", Language.getCurrLang(null), null));
        			//：
        			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        			sbr.append(portalInfo.getInitpwdsmsverifyexpire());
        			sbr.append("</br>");
        		}
        		objs[0] = ++i;
    		}
    		//AD密码验证
    		else if("4".equals(portalInfo.getInitpwdloginverifytype())){
        		//初始密码登录验证方式
    			sbr.append(Language.getLangValue("portal_init_pwd_login_verify_type", Language.getCurrLang(null), null));
    			//：
    			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
    			//AD密码验证
    			sbr.append(Language.getLangValue("portal_init_pwd_verify_ad_pwd", Language.getCurrLang(null), null));
        		sbr.append("</br>");
        		
        		if(!StrTool.strEquals(portalInfo.getAdserverip(), oldPortalInfo.getAdserverip())){
        			//服务器IP
        			sbr.append(Language.getLangValue("portal_ad_verify_pwd_ip", Language.getCurrLang(null), null));
        			//：
        			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        			sbr.append(oldPortalInfo.getAdserverip());
        			//，修改为：
        			sbr.append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null));
        			sbr.append(portalInfo.getAdserverip());
        			sbr.append("</br>");
        		}else{
        			//服务器IP
        			sbr.append(Language.getLangValue("portal_ad_verify_pwd_ip", Language.getCurrLang(null), null));
        			//：
        			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        			sbr.append(portalInfo.getAdserverip());
        			sbr.append("</br>");
        		}
        		objs[0] = ++i;
        		if(!StrTool.strEquals(portalInfo.getAdserverport(), oldPortalInfo.getAdserverport())){
        			//服务器端口
        			sbr.append(Language.getLangValue("portal_ad_verify_pwd_port", Language.getCurrLang(null), null));
        			//：
        			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        			sbr.append(oldPortalInfo.getAdserverport());
        			//，修改为：
        			sbr.append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null));
        			sbr.append(portalInfo.getAdserverport());
        			sbr.append("</br>");
        		}else{
        			//服务器端口
        			sbr.append(Language.getLangValue("portal_ad_verify_pwd_port", Language.getCurrLang(null), null));
        			//：
        			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        			sbr.append(portalInfo.getAdserverport());
        			sbr.append("</br>");
        		}
        		objs[0] = ++i;
        		if(!StrTool.strEquals(portalInfo.getAdserverdn(), oldPortalInfo.getAdserverdn())){
        			//根DN
        			sbr.append(Language.getLangValue("usource_vd_root_dn", Language.getCurrLang(null), null));
        			//：
        			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        			sbr.append(oldPortalInfo.getAdserverdn());
        			//，修改为：
        			sbr.append(Language.getLangValue("log_modified_to", Language.getCurrLang(null), null));
        			sbr.append(portalInfo.getAdserverdn());
        			sbr.append("</br>");
        		}else{
        			//根DN
        			sbr.append(Language.getLangValue("usource_vd_root_dn", Language.getCurrLang(null), null));
        			//：
        			sbr.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        			sbr.append(portalInfo.getAdserverdn());
        			sbr.append("</br>");
        		}
        		objs[0] = ++i;
    		}
    	}
    	objs[1] = sbr;
    	return objs;
    }

    /**
     * 数据库配置日志记录
     */
    public String writeDbConfStr(String operater, DBConfAction dbConfAction) {
        DBConfInfo dbConfInfo = dbConfAction.getDbConfInfo();
        DBConfInfo oldConfInfo = dbConfAction.getOldConfInfo();
        int i = 0;
        StringBuilder sbr = new StringBuilder();
        sbr.append(operater).append(Language.getLangValue("log_db_record_edit", Language.getCurrLang(null), null))
                .append(Language.getLangValue("log_edit_content", Language.getCurrLang(null), null)).append("</br>");
        if (dbConfInfo == null || oldConfInfo == null) {
            return null;
        }
        if (!StrTool.strEquals(dbConfInfo.getDbtype(), oldConfInfo.getDbtype())) {
            sbr.append(
                    Language.getLangValue("dbconf_type", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                    oldConfInfo.getDbtype()).append(
                    Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                    dbConfInfo.getDbtype()).append("</br>");
            i++;
        }

        if (!StrTool.strEquals(dbConfInfo.getIp(), oldConfInfo.getIp())) {
            sbr.append(
                    Language.getLangValue("dbconf_ip", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                    oldConfInfo.getIp()).append(
                    Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                    dbConfInfo.getIp()).append("</br>");
            i++;
        }
        if (!StrTool.strEquals(dbConfInfo.getPort(), oldConfInfo.getPort())) {
            sbr.append(
                    Language.getLangValue("dbconf_port", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                    oldConfInfo.getPort()).append(
                    Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                    dbConfInfo.getPort()).append("</br>");
            i++;
        }
        if (!StrTool.strEquals(dbConfInfo.getDbname(), oldConfInfo.getDbname())) {
            sbr.append(
                    Language.getLangValue("dbconf_dbname", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                    oldConfInfo.getDbname()).append(
                    Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                    dbConfInfo.getDbname()).append("</br>");
            i++;
        }
        if (!StrTool.strEquals(dbConfInfo.getUsername(), oldConfInfo.getUsername())) {
            sbr.append(
                    Language.getLangValue("dbconf_username", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                    oldConfInfo.getUsername()).append(
                    Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                    dbConfInfo.getUsername()).append("</br>");
            i++;
        }

        if (i == 0) {
            return null;
        }

        dbConfAction.setDbConfInfo(null);
        dbConfAction.setOldConfInfo(null);

        return sbr.toString();

    }

    //编辑权限
    public String writeRoleInfoStr(String operater, RoleInfoAction roleInfoAction) {
        RoleInfo rInfo = roleInfoAction.getOldroleInfo();
        StringBuilder sbr = new StringBuilder();
        String text = rInfo.getRoletext();
        if (text.endsWith("，")) {
            text = text.substring(0, text.length() - 1);
        }

        String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
        String eidttext = Language.getLangValue("log_edit_content", Language.getCurrLang(null), null);
        String rolename = Language.getLangValue("admin_role_name", Language.getCurrLang(null), null) + colon;
        String descp = Language.getLangValue("log_record_descp", Language.getCurrLang(null), null);
        String oldName = Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null);
        sbr.append(operater).append(eidttext).append("</br>");
        sbr.append(rolename).append(rInfo.getOldRoleName());
        if (!StrTool.strEquals(rInfo.getOldRoleName(), rInfo.getRoleName())) {
            sbr.append(Language.getLangValue("comma", Language.getCurrLang(null), null)).append(oldName).append(
                    rInfo.getRoleName());
        }
        sbr.append("</br>");
        sbr.append(text);
        sbr.append(descp).append(rInfo.getDescp()).append("</br>");
        return sbr.toString();
    }

    //迁移组织机构
    public String writeMoveOrgInfoStr(String operater, OrgunitInfoAction orgInfoAction) {
        OrgunitInfo orgInfo = orgInfoAction.getOldOrgInfo();
        StringBuilder sbr = new StringBuilder();
        String org = Language.getLangValue("domain_orgunit", Language.getCurrLang(null), null);
        String moveorg = Language.getLangValue("log_record_move_org", Language.getCurrLang(null), null);
        if (moveorg.endsWith(",")) {
            moveorg = moveorg.substring(0, moveorg.length() - 1);
        }
        sbr.append(org + orgInfo.getOrgunitName() + moveorg + orgInfo.getOrgParentName());
        return sbr.toString();
    }
    
    //编辑管理员
    public String writeAdmUserStr(String operater, AdmUserAction admUserAction) {
        AdminUser adminUser = admUserAction.getAdminUser();
        AdminUser oldAdminUser = admUserAction.getOldAdminUser();
        StringBuilder sbr = new StringBuilder();
        
        // 管理员账号
        sbr.append(Language.getLangValue("admin_info_account", Language.getCurrLang(null), null)
            	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + adminUser.getAdminid()
            	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        
        // 真实姓名
        String realName = "";
        if (StrTool.strNotNull(adminUser.getRealname())) {
        	realName = adminUser.getRealname();
        }else{
        	realName = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        }
        if(StrTool.strEqualsToNull(adminUser.getRealname(), oldAdminUser.getRealname())){
        	sbr.append( Language.getLangValue("common_info_realname", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + realName
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }else{
            String oldRealName = "";
            if (StrTool.strNotNull(oldAdminUser.getRealname())) {
            	oldRealName = oldAdminUser.getRealname();
            }else{
            	oldRealName = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)
            			+ Language.getLangValue("comma", Language.getCurrLang(null), null);
            }
        	sbr.append( Language.getLangValue("common_info_realname", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldRealName
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ realName
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }
        
        // 密码验证方式
        String localauth = "";
        if(adminUser.getLocalauth() == 0){
        	localauth = Language.getLangValue("login_mode_only_vd_pwd", Language.getCurrLang(null), null);
        }else if(adminUser.getLocalauth() == 1){
        	localauth = Language.getLangValue("login_mode_only_vd_pin", Language.getCurrLang(null), null);
        }else{
        	localauth = Language.getLangValue("login_mode_vd_pwd_pin", Language.getCurrLang(null), null);
        }
        if(adminUser.getLocalauth() == oldAdminUser.getLocalauth()){
        	sbr.append( Language.getLangValue("admin_login_mode", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + localauth
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }else{
            String oldLocalauth = "";
            if(oldAdminUser.getLocalauth() == 0){
            	oldLocalauth = Language.getLangValue("login_mode_only_vd_pwd", Language.getCurrLang(null), null);
            }else if(adminUser.getLocalauth() == 1){
            	oldLocalauth = Language.getLangValue("login_mode_only_vd_pin", Language.getCurrLang(null), null);
            }else{
            	oldLocalauth = Language.getLangValue("login_mode_vd_pwd_pin", Language.getCurrLang(null), null);
            }
        	sbr.append( Language.getLangValue("admin_login_mode", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldLocalauth
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ localauth
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }
        
        // 选择角色
        if(StrTool.isArrEquals(adminUser.getAdminRoles(), oldAdminUser.getAdminRoles())){
        	sbr.append( Language.getLangValue("admin_info_sel_role", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + adminUser.getRolenameStr());
        }else{
        	sbr.append( Language.getLangValue("admin_info_sel_role", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldAdminUser.getRolenameStr()
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ adminUser.getRolenameStr()
        			);
        }
        
        // 选择机构
//        if(StrTool.isArrEquals(adminUser.getOrgunitIdList(), oldAdminUser.getOrgunitIdList())){
//        	String orgunitName = "";
//        	if (StrTool.strNotNull(adminUser.getOrgunitNames())) {
//            	orgunitName = adminUser.getOrgunitNames().replace(",", "，") + "，";
//            }else{
//            	orgunitName = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)
//            			+ Language.getLangValue("comma", Language.getCurrLang(null), null);
//            }
//            sbr.append( Language.getLangValue("user_sel_org", Language.getCurrLang(null), null)
//                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + orgunitName);
//        }else{
//        	if(!StrTool.listNotNull(adminUser.getOrgunitIdList()) && !StrTool.listNotNull(oldAdminUser.getOrgunitIdList())){
//        		sbr.append( Language.getLangValue("user_sel_org", Language.getCurrLang(null), null)
//                    	+ Language.getLangValue("colon", Language.getCurrLang(null), null)
//                    	+ Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)
//            			+ Language.getLangValue("comma", Language.getCurrLang(null), null)
//            			);
//        	}else{
//        		String orgunitName = "";
//            	if (StrTool.strNotNull(adminUser.getOrgunitNames())) {
//                	orgunitName = adminUser.getOrgunitNames().replace(",", "，") + "，";
//                }else{
//                	orgunitName = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)
//                			+ Language.getLangValue("comma", Language.getCurrLang(null), null);
//                }
//            	
//            	String oldOrgunitName = "";
//            	if (StrTool.strNotNull(oldAdminUser.getOrgunitNames())) {
//            		oldOrgunitName = oldAdminUser.getOrgunitNames().replace(",", "，");
//                }else{
//                	oldOrgunitName = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)
//                			+ Language.getLangValue("comma", Language.getCurrLang(null), null);
//                }
//            	sbr.append( Language.getLangValue("user_sel_org", Language.getCurrLang(null), null)
//                    	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldOrgunitName
//                    	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
//                		+ orgunitName);
//        	}
//        }
        
        // 邮箱
        String email = "";
        if (StrTool.strNotNull(adminUser.getEmail())) {
        	email = adminUser.getEmail();
        }else{
        	email = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        }
        if(StrTool.strEqualsToNull(adminUser.getEmail(), oldAdminUser.getEmail())){
        	sbr.append( Language.getLangValue("common_info_email", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + email
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }else{
            String oldEmail = "";
            if (StrTool.strNotNull(oldAdminUser.getEmail())) {
            	oldEmail = oldAdminUser.getEmail();
            }else{
            	oldEmail = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)
            			+ Language.getLangValue("comma", Language.getCurrLang(null), null);
            }
        	sbr.append( Language.getLangValue("common_info_email", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldEmail
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ email
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }
        
        // 手机
        String cellphone = "";
        if (StrTool.strNotNull(adminUser.getCellphone())) {
        	cellphone = adminUser.getCellphone();
        }else{
        	cellphone = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        }
        if(StrTool.strEqualsToNull(adminUser.getCellphone(), oldAdminUser.getCellphone())){
        	sbr.append( Language.getLangValue("common_info_mobile", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + cellphone
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }else{
            String oldCellphone = "";
            if (StrTool.strNotNull(oldAdminUser.getCellphone())) {
            	oldCellphone = oldAdminUser.getCellphone();
            }else{
            	oldCellphone = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
            }
        	sbr.append( Language.getLangValue("common_info_mobile", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldCellphone
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ cellphone
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }
        
        // 描述
        String descp = "";
        if (StrTool.strNotNull(adminUser.getDescp())) {
        	descp = adminUser.getDescp();
        }else{
        	descp = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        }
        if(StrTool.strEqualsToNull(adminUser.getDescp(), oldAdminUser.getDescp())){
        	sbr.append( Language.getLangValue("admin_info_descp", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + descp);
        }else{
            String oldDescp = "";
            if (StrTool.strNotNull(oldAdminUser.getDescp())) {
            	oldDescp = oldAdminUser.getDescp();
            }else{
            	oldDescp = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)
            			+ Language.getLangValue("comma", Language.getCurrLang(null), null);
            }
        	sbr.append( Language.getLangValue("admin_info_descp", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldDescp
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ descp);
        }
        return sbr.toString();
    }
    
    //编辑个人信息
    public String writeAdmUserBaseStr(String operater, AdmUserAction admUserAction) {
        AdminUser adminUser = admUserAction.getAdminUser();
        AdminUser oldAdminUser = admUserAction.getOldAdminUser();
        StringBuilder sbr = new StringBuilder();
        
        // 管理员账号
        sbr.append(Language.getLangValue("admin_info_account", Language.getCurrLang(null), null)
            	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + adminUser.getAdminid()
            	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        
        // 真实姓名
        String realName = "";
        if (StrTool.strNotNull(adminUser.getRealname())) {
        	realName = adminUser.getRealname();
        }else{
        	realName = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        }
        if(StrTool.strEqualsToNull(adminUser.getRealname(), oldAdminUser.getRealname())){
        	sbr.append( Language.getLangValue("common_info_realname", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + realName
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }else{
            String oldRealName = "";
            if (StrTool.strNotNull(oldAdminUser.getRealname())) {
            	oldRealName = oldAdminUser.getRealname();
            }else{
            	oldRealName = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)
            			+ Language.getLangValue("comma", Language.getCurrLang(null), null);
            }
        	sbr.append( Language.getLangValue("common_info_realname", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldRealName
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ realName
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }
        
        // 邮箱
        String email = "";
        if (StrTool.strNotNull(adminUser.getEmail())) {
        	email = adminUser.getEmail();
        }else{
        	email = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        }
        if(StrTool.strEqualsToNull(adminUser.getEmail(), oldAdminUser.getEmail())){
        	sbr.append( Language.getLangValue("common_info_email", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + email
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }else{
            String oldEmail = "";
            if (StrTool.strNotNull(oldAdminUser.getEmail())) {
            	oldEmail = oldAdminUser.getEmail();
            }else{
            	oldEmail = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)
            			+ Language.getLangValue("comma", Language.getCurrLang(null), null);
            }
        	sbr.append( Language.getLangValue("common_info_email", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldEmail
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ email
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }
        
        // 手机
        String cellphone = "";
        if (StrTool.strNotNull(adminUser.getCellphone())) {
        	cellphone = adminUser.getCellphone();
        }else{
        	cellphone = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        }
        if(StrTool.strEqualsToNull(adminUser.getCellphone(), oldAdminUser.getCellphone())){
        	sbr.append( Language.getLangValue("common_info_mobile", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + cellphone
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }else{
            String oldCellphone = "";
            if (StrTool.strNotNull(oldAdminUser.getCellphone())) {
            	oldCellphone = oldAdminUser.getCellphone();
            }else{
            	oldCellphone = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
            }
        	sbr.append( Language.getLangValue("common_info_mobile", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldCellphone
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ cellphone
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }
        
        // 描述
        String descp = "";
        if (StrTool.strNotNull(adminUser.getDescp())) {
        	descp = adminUser.getDescp();
        }else{
        	descp = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        }
        if(StrTool.strEqualsToNull(adminUser.getDescp(), oldAdminUser.getDescp())){
        	sbr.append( Language.getLangValue("admin_info_descp", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + descp);
        }else{
            String oldDescp = "";
            if (StrTool.strNotNull(oldAdminUser.getDescp())) {
            	oldDescp = oldAdminUser.getDescp();
            }else{
            	oldDescp = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)
            			+ Language.getLangValue("comma", Language.getCurrLang(null), null);
            }
        	sbr.append( Language.getLangValue("admin_info_descp", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldDescp
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ descp);
        }
        return sbr.toString();
    }
    
    //编辑用户
    public String writeUserStr(String operater, UserInfoAction userInfoAction) {
        UserInfo userInfo = userInfoAction.getUserInfo();
        UserInfo oldUserInfo = userInfoAction.getOldUserinfo();
        StringBuilder sbr = new StringBuilder();
        
        // 管理员账号
        sbr.append(Language.getLangValue("user_info_account", Language.getCurrLang(null), null)
            	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + userInfo.getUserId()
            	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        
        // 组织机构
        sbr.append(Language.getLangValue("user_sel_org", Language.getCurrLang(null), null)
            	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + userInfo.getDOrgunitName()
            	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        
        // 真实姓名
        String readName = "";
    	if(StrTool.strNotNull(userInfo.getRealName())){
    		readName = userInfo.getRealName();
    	}else{
    		readName = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
    	}
        if(StrTool.strEqualsToNull(userInfo.getRealName(), oldUserInfo.getRealName())){
        	sbr.append(Language.getLangValue("user_info_real_name", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + readName
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }else{
        	String oldReadName = "";
            if (StrTool.strNotNull(oldUserInfo.getRealName())) {
            	oldReadName = oldUserInfo.getRealName();
            }else{
            	oldReadName = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)
            			+ Language.getLangValue("comma", Language.getCurrLang(null), null);
            }
        	sbr.append( Language.getLangValue("user_info_real_name", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldReadName
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ readName
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }
        
        // 本地认证模式
        String localAuthName = "";
        if(userInfo.getLocalAuth() == 0){
        	localAuthName = Language.getLangValue("local_auth_only_vd_tkn", Language.getCurrLang(null), null);
        }else if(userInfo.getLocalAuth() == 1){
        	localAuthName = Language.getLangValue("local_auth_vd_pwd_tkn", Language.getCurrLang(null), null);
        }else{
        	localAuthName = Language.getLangValue("local_auth_only_vd_pwd", Language.getCurrLang(null), null);
        }
        if(userInfo.getLocalAuth() == oldUserInfo.getLocalAuth()){
        	sbr.append(Language.getLangValue("user_local_auth_mode", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + localAuthName
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }else{
        	String oldLocalAuthName = "";
            if(oldUserInfo.getLocalAuth() == 0){
            	oldLocalAuthName = Language.getLangValue("local_auth_only_vd_tkn", Language.getCurrLang(null), null);
            }else if(oldUserInfo.getLocalAuth() == 1){
            	oldLocalAuthName = Language.getLangValue("local_auth_vd_pwd_tkn", Language.getCurrLang(null), null);
            }else{
            	oldLocalAuthName = Language.getLangValue("local_auth_only_vd_pwd", Language.getCurrLang(null), null);
            }
        	sbr.append( Language.getLangValue("user_local_auth_mode", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldLocalAuthName
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ localAuthName
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }
        
        // 后端认证模式
        String backEndName = "";
        if(userInfo.getBackEndAuth() == 0){
        	backEndName = Language.getLangValue("backend_auth_default", Language.getCurrLang(null), null);
        }else if(userInfo.getBackEndAuth() == 1){
        	backEndName = Language.getLangValue("backend_auth_need", Language.getCurrLang(null), null);
        }else{
        	backEndName = Language.getLangValue("backend_auth_no_need", Language.getCurrLang(null), null);
        }
        if(userInfo.getBackEndAuth() == oldUserInfo.getBackEndAuth()){
        	sbr.append(Language.getLangValue("user_whether_backend_auth", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + backEndName
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }else{
        	String oldBackEndName = "";
            if(oldUserInfo.getBackEndAuth() == 0){
            	oldBackEndName = Language.getLangValue("backend_auth_default", Language.getCurrLang(null), null);
            }else if(oldUserInfo.getBackEndAuth() == 1){
            	oldBackEndName = Language.getLangValue("backend_auth_need", Language.getCurrLang(null), null);
            }else{
            	oldBackEndName = Language.getLangValue("backend_auth_no_need", Language.getCurrLang(null), null);
            }
            sbr.append( Language.getLangValue("user_whether_backend_auth", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldBackEndName
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ backEndName
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }
        
        // 返回Radius属性
        int radProfileId = 0;
        int oldRadProfileId = 0;
        if(userInfo.getRadProfileId() != null){
        	radProfileId = userInfo.getRadProfileId();
    	}
    	if(oldUserInfo.getRadProfileId() != null){
    		oldRadProfileId = oldUserInfo.getRadProfileId();
    	}
        String radProfileName = "";
    	if(radProfileId == 0){
    		radProfileName = Language.getLangValue("common_syntax_not_return", Language.getCurrLang(null), null);
    	}else{
    		radProfileName = userInfo.getRadProfileName();
    	}
        if(radProfileId == oldRadProfileId){
        	sbr.append(Language.getLangValue("user_return_client_Rad_conf", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + radProfileName
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }else{
        	String oldRadProfileName = "";
        	if(oldRadProfileId == 0){
        		oldRadProfileName = Language.getLangValue("common_syntax_not_return", Language.getCurrLang(null), null);
        	}else{
        		oldRadProfileName = oldUserInfo.getRadProfileName();
        	}
        	sbr.append( Language.getLangValue("user_return_client_Rad_conf", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldRadProfileName
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ radProfileName
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }
        
        // 邮箱
        String email = "";
    	if(StrTool.strNotNull(userInfo.getEmail())){
    		email = userInfo.getEmail();
    	}else{
    		email = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
    	}
        if(StrTool.strEqualsToNull(userInfo.getEmail(), oldUserInfo.getEmail())){
        	sbr.append(Language.getLangValue("common_info_email", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + email
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }else{
        	String oldEmail = "";
        	if(StrTool.strNotNull(oldUserInfo.getEmail())){
        		oldEmail = oldUserInfo.getEmail();
        	}else{
        		oldEmail = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        	}
        	sbr.append( Language.getLangValue("common_info_email", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldEmail
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ email
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }
        
        // 手机
        String cellPhone = "";
    	if(StrTool.strNotNull(userInfo.getCellPhone())){
    		cellPhone = userInfo.getCellPhone();
    	}else{
    		cellPhone = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
    	}
        if(StrTool.strEqualsToNull(userInfo.getCellPhone(), oldUserInfo.getCellPhone())){
        	sbr.append(Language.getLangValue("common_info_mobile", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + cellPhone
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }else{
        	String oldCellPhone = "";
        	if(StrTool.strNotNull(oldUserInfo.getCellPhone())){
        		oldCellPhone = oldUserInfo.getCellPhone();
        	}else{
        		oldCellPhone = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        	}
        	sbr.append( Language.getLangValue("common_info_mobile", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldCellPhone
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ cellPhone);
        }
        
        return sbr.toString();
    }
    
    //编辑认证服务器
    public String writeServerStr(String operater, ServerAction serverAction) {
    	ServerInfo serverInfo = serverAction.getServerInfo();
    	ServerInfo oldServerInfo = serverAction.getOldServerInfo();
        StringBuilder sbr = new StringBuilder();
        
        // 服务器IP
        sbr.append(Language.getLangValue("auth_ser_hostip", Language.getCurrLang(null), null)
            	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + serverInfo.getHostipaddr()
            	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        
        // 服务器名称
        if(StrTool.strEquals(serverInfo.getHostname(), oldServerInfo.getHostname())){
        	sbr.append(Language.getLangValue("auth_ser_hostname", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + serverInfo.getHostname()
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }else{
        	sbr.append( Language.getLangValue("auth_ser_hostname", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldServerInfo.getHostname()
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ serverInfo.getHostname()
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }
        
        // 优先级
        String priority = "";
        if(serverInfo.getPriority() == 0){
        	priority = Language.getLangValue("common_syntax_advanced", Language.getCurrLang(null), null);
        }else if(serverInfo.getPriority() == 1){
        	priority = Language.getLangValue("common_syntax_ordinary", Language.getCurrLang(null), null);
        }else{
        	priority = Language.getLangValue("common_syntax_low", Language.getCurrLang(null), null);
        }
        if(serverInfo.getPriority() == oldServerInfo.getPriority()){
        	sbr.append(Language.getLangValue("auth_ser_priority", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + priority
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }else{
        	String oldPriority = "";
            if(oldServerInfo.getPriority() == 0){
            	oldPriority = Language.getLangValue("common_syntax_advanced", Language.getCurrLang(null), null);
            }else if(oldServerInfo.getPriority() == 1){
            	oldPriority = Language.getLangValue("common_syntax_ordinary", Language.getCurrLang(null), null);
            }else{
            	oldPriority = Language.getLangValue("common_syntax_low", Language.getCurrLang(null), null);
            }
        	sbr.append( Language.getLangValue("auth_ser_priority", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldPriority
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ priority
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }
        
        // 启用扩展Radius服务
        String ftradiusenabled = "";
        if(serverInfo.getFtradiusenabled() == 0){
        	ftradiusenabled = Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null);
        }else{
        	ftradiusenabled = Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null);
        }
        if(serverInfo.getFtradiusenabled() == oldServerInfo.getFtradiusenabled()){
        	sbr.append(Language.getLangValue("auth_ser_ftradius_enabled", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + ftradiusenabled
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }else{
        	String oldFtradiusenabled = "";
            if(oldServerInfo.getFtradiusenabled() == 0){
            	oldFtradiusenabled = Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null);
            }else{
            	oldFtradiusenabled = Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null);
            }
        	sbr.append( Language.getLangValue("auth_ser_ftradius_enabled", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldFtradiusenabled
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ ftradiusenabled
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }
        
        if(serverInfo.getFtradiusenabled() == 1){
        	
        	// 扩展Radius认证端口
        	if(serverInfo.getAuthport() == oldServerInfo.getAuthport()){
        		sbr.append(Language.getLangValue("auth_ser_authport", Language.getCurrLang(null), null)
                    	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + serverInfo.getAuthport()
                    	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        	}else{
        		sbr.append( Language.getLangValue("auth_ser_authport", Language.getCurrLang(null), null)
                    	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldServerInfo.getAuthport()
                    	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                    	+ serverInfo.getAuthport()
                    	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        	}
            
            // 扩展Radius同步端口
        	if(serverInfo.getSyncport() == oldServerInfo.getSyncport()){
        		sbr.append(Language.getLangValue("auth_ser_syncport", Language.getCurrLang(null), null)
                    	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + serverInfo.getSyncport()
                    	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        	}else{
        		sbr.append( Language.getLangValue("auth_ser_syncport", Language.getCurrLang(null), null)
                    	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldServerInfo.getSyncport()
                    	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                    	+ serverInfo.getSyncport()
                    	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        	}
        }
        
        // 启用标准Radius服务
        String radiusenabled = "";
        if(serverInfo.getRadiusenabled() == 0){
        	radiusenabled = Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null);
        }else{
        	radiusenabled = Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null);
        }
        if(serverInfo.getRadiusenabled() == oldServerInfo.getRadiusenabled()){
        	sbr.append(Language.getLangValue("auth_ser_radius_enabled", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + radiusenabled
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }else{
        	String oldRadiusenabled = "";
            if(oldServerInfo.getRadiusenabled() == 0){
            	oldRadiusenabled = Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null);
            }else{
            	oldRadiusenabled = Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null);
            }
        	sbr.append( Language.getLangValue("auth_ser_radius_enabled", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldRadiusenabled
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ radiusenabled
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        }
        
        if(serverInfo.getRadiusenabled() == 1){

        	// 标准Radius认证端口
        	if(serverInfo.getRadauthport() == oldServerInfo.getRadauthport()){
        		sbr.append(Language.getLangValue("auth_ser_radius_authport", Language.getCurrLang(null), null)
                    	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + serverInfo.getSyncport()
                    	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        	}else{
        		sbr.append(Language.getLangValue("auth_ser_radius_authport", Language.getCurrLang(null), null)
                    	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldServerInfo.getRadauthport()
                    	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                    	+ serverInfo.getRadauthport()
                    	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
        	}
        }
        
        // SOAP服务端口
        if(serverInfo.getSoapport() == oldServerInfo.getSoapport()){
    		sbr.append(Language.getLangValue("auth_ser_soap_port", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + serverInfo.getSoapport()
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
    	}else{
    		sbr.append(Language.getLangValue("auth_ser_soap_port", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldServerInfo.getSoapport()
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ serverInfo.getSoapport()
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
    	}
        
        // WebService名称
        if(StrTool.strEquals(serverInfo.getWebservicename(),oldServerInfo.getWebservicename())){
    		sbr.append(Language.getLangValue("auth_ser_webservicename", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + serverInfo.getWebservicename()
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
    	}else{
    		sbr.append(Language.getLangValue("auth_ser_webservicename", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldServerInfo.getWebservicename()
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ serverInfo.getWebservicename()
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
    	}
        
        // 描述
        String descp = "";
        if(StrTool.strNotNull(serverInfo.getDescp())){
        	descp = serverInfo.getDescp();
        }else{
        	descp = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        }
        if(StrTool.strEqualsToNull(serverInfo.getDescp(),oldServerInfo.getDescp())){
    		sbr.append(Language.getLangValue("common_syntax_desc", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + descp);
    	}else{
    		String oldDescp = "";
            if(StrTool.strNotNull(oldServerInfo.getDescp())){
            	oldDescp = oldServerInfo.getDescp();
            }else{
            	oldDescp = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)
            			+ Language.getLangValue("comma", Language.getCurrLang(null), null);
            }
    		sbr.append(Language.getLangValue("common_syntax_desc", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + oldDescp
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ descp);
    	}
        
        return sbr.toString();
    }
    
    //编辑认证代理配
    public String writeAgentConfStr(String operater, AgentConfAction agentConfAction) {
    	AgentConfInfo agentConfInfo = agentConfAction.getAgentConfInfo();
    	AgentConfInfo oldAgentConfInfo = agentConfAction.getOldAgentConfInfo();
        StringBuilder sbr = new StringBuilder();
        String comma = Language.getLangValue("comma", Language.getCurrLang(null), null);
        String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
        
        // 配置名称
        if(StrTool.strEquals(agentConfInfo.getConfname(), oldAgentConfInfo.getConfname())){
        	sbr.append(Language.getLangValue("log_record_config_name", Language.getCurrLang(null), null)
                	+ agentConfInfo.getConfname()
                	+ comma);
        }else{
        	sbr.append(Language.getLangValue("log_record_config_name", Language.getCurrLang(null), null)
                	+ oldAgentConfInfo.getConfname()
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ agentConfInfo.getConfname()
                	+ comma);
        }
        
        // 代理配置类型
        String agentType = "";
    	if(agentConfInfo.getType() == 0){
    		agentType = Language.getLangValue("auth_conf_win_login_pro", Language.getCurrLang(null), null);
    	}else{
    		agentType = Language.getLangValue("auth_conf_linux_login_pro", Language.getCurrLang(null), null);
    	}
        if(agentConfInfo.getType() == oldAgentConfInfo.getType()){
        	sbr.append(Language.getLangValue("auth_conf_agent_type", Language.getCurrLang(null), null)
        			+ colon
                	+ agentType
                	+ comma);
        }else{
        	String oldAgentType = "";
        	if(oldAgentConfInfo.getType() == 0){
        		oldAgentType = Language.getLangValue("auth_conf_win_login_pro", Language.getCurrLang(null), null);
        	}else{
        		oldAgentType = Language.getLangValue("auth_conf_linux_login_pro", Language.getCurrLang(null), null);
        	}
        	sbr.append(Language.getLangValue("auth_conf_agent_type", Language.getCurrLang(null), null)
        			+ colon
                	+ oldAgentType
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ agentType
                	+ comma);
        }
        
        // 用户名格式
        if(agentConfInfo.getUserformat() == oldAgentConfInfo.getUserformat()){
        	sbr.append(Language.getLangValue("auth_conf_uname_format", Language.getCurrLang(null), null)
        			+ colon
                	+ userFormat(agentConfInfo.getUserformat())
                	+ comma);
        }else{
        	sbr.append(Language.getLangValue("auth_conf_uname_format", Language.getCurrLang(null), null)
        			+ colon
                	+ userFormat(oldAgentConfInfo.getUserformat())
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ userFormat(agentConfInfo.getUserformat())
                	+ comma);
        }
        
        //WINDOWS
        if(agentConfInfo.getType() == 0){
        	// WINDOWS
        	if(agentConfInfo.getType() == oldAgentConfInfo.getType()){
        		// 本地登录
    	        if(agentConfInfo.getLocalprotect() == oldAgentConfInfo.getLocalprotect()){
    	        	sbr.append(Language.getLangValue("auth_conf_local_protect", Language.getCurrLang(null), null)
    	        			+ colon
    	                	+ getProtectType(agentConfInfo.getLocalprotect())
    	                	+ comma);
    	        }else{
    	        	sbr.append(Language.getLangValue("auth_conf_local_protect", Language.getCurrLang(null), null)
    	        			+ colon
    	                	+ getProtectType(oldAgentConfInfo.getLocalprotect())
    	                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
    	                	+ getProtectType(agentConfInfo.getLocalprotect())
    	                	+ comma);
    	        }
    	        
    	        // 远程登录
    	        if(agentConfInfo.getRemoteprotect() == oldAgentConfInfo.getRemoteprotect()){
    	        	sbr.append(Language.getLangValue("auth_conf_remote_protect", Language.getCurrLang(null), null)
    	        			+ colon
    	                	+ getProtectType(agentConfInfo.getRemoteprotect())
    	                	+ comma);
    	        }else{
    	        	sbr.append(Language.getLangValue("auth_conf_remote_protect", Language.getCurrLang(null), null)
    	        			+ colon
    	                	+ getProtectType(oldAgentConfInfo.getRemoteprotect())
    	                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
    	                	+ getProtectType(agentConfInfo.getRemoteprotect())
    	                	+ comma);
    	        }
    	        
    	        // UAC登录
    	        if(agentConfInfo.getUacprotect() == oldAgentConfInfo.getUacprotect()){
    	        	sbr.append(Language.getLangValue("auth_conf_uac_protect", Language.getCurrLang(null), null)
    	        			+ colon
    	                	+ getProtectType(agentConfInfo.getUacprotect())
    	                	+ comma);
    	        }else{
    	        	sbr.append(Language.getLangValue("auth_conf_uac_protect", Language.getCurrLang(null), null)
    	        			+ colon
    	                	+ getProtectType(oldAgentConfInfo.getUacprotect())
    	                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
    	                	+ getProtectType(agentConfInfo.getUacprotect())
    	                	+ comma);
    	        }
        	}else{ // LIN转WINDOWS
        		// 本地登录
	        	sbr.append(Language.getLangValue("auth_conf_local_protect", Language.getCurrLang(null), null)
	        			+ colon
	                	+ Language.getLangValue("auth_conf_hand_protection", Language.getCurrLang(null), null)
	                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
	                	+ getProtectType(agentConfInfo.getLocalprotect())
	                	+ comma);
	        	
	        	// 远程登录
	        	sbr.append(Language.getLangValue("auth_conf_remote_protect", Language.getCurrLang(null), null)
	        			+ colon
	                	+ Language.getLangValue("auth_conf_hand_protection", Language.getCurrLang(null), null)
	                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
	                	+ getProtectType(agentConfInfo.getRemoteprotect())
	                	+ comma);
	        	
	        	// UAC登录
	        	sbr.append(Language.getLangValue("auth_conf_uac_protect", Language.getCurrLang(null), null)
	        			+ colon
	                	+ Language.getLangValue("auth_conf_vd_not_support", Language.getCurrLang(null), null)
	                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
	                	+ getProtectType(agentConfInfo.getUacprotect())
	                	+ comma);
        	}
        }else{ // LIN
        	// LIN
        	if(agentConfInfo.getType() == oldAgentConfInfo.getType()){
        		// 本地登录
        		sbr.append(Language.getLangValue("auth_conf_local_protect", Language.getCurrLang(null), null)
	        			+ colon
	                	+ Language.getLangValue("auth_conf_hand_protection", Language.getCurrLang(null), null)
	                	+ comma);
        		
        		// 远程登录
        		sbr.append(Language.getLangValue("auth_conf_remote_protect", Language.getCurrLang(null), null)
	        			+ colon
	                	+ Language.getLangValue("auth_conf_hand_protection", Language.getCurrLang(null), null)
	                	+ comma);
        		
        		// UAC登录
        		sbr.append(Language.getLangValue("auth_conf_uac_protect", Language.getCurrLang(null), null)
	        			+ colon
	                	+ Language.getLangValue("auth_conf_vd_not_support", Language.getCurrLang(null), null)
	                	+ comma);
        	}else{ //WINDOWS转LIN
        		// 本地登录
	        	sbr.append(Language.getLangValue("auth_conf_local_protect", Language.getCurrLang(null), null)
	        			+ colon
	                	+ getProtectType(oldAgentConfInfo.getLocalprotect())
	                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
	                	+ Language.getLangValue("auth_conf_hand_protection", Language.getCurrLang(null), null)
	                	+ comma);
	        	
	        	// 远程登录
	        	sbr.append(Language.getLangValue("auth_conf_remote_protect", Language.getCurrLang(null), null)
	        			+ colon
	                	+ getProtectType(oldAgentConfInfo.getRemoteprotect())
	                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
	                	+ Language.getLangValue("auth_conf_hand_protection", Language.getCurrLang(null), null)
	                	+ comma);
	        	
	        	// UAC登录
	        	sbr.append(Language.getLangValue("auth_conf_uac_protect", Language.getCurrLang(null), null)
	        			+ colon
	                	+ getProtectType(oldAgentConfInfo.getUacprotect())
	                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
	                	+ Language.getLangValue("auth_conf_vd_not_support", Language.getCurrLang(null), null)
	                	+ comma);
    	        
        	}
        }
        
        // 允许未绑定令牌的用户登录
        String unboundLogin = "";
        if(agentConfInfo.getUnboundlogin() == 1){
        	unboundLogin = Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null);
        }else{
        	unboundLogin = Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null);
        }
        if(agentConfInfo.getUnboundlogin() == oldAgentConfInfo.getUnboundlogin()){
        	sbr.append(Language.getLangValue("auth_conf_unbound_login", Language.getCurrLang(null), null)
        			+ colon
                	+ unboundLogin
                	+ comma);
        }else{
        	String oldUnboundLogin = "";
            if(oldAgentConfInfo.getUnboundlogin() == 1){
            	oldUnboundLogin = Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null);
            }else{
            	oldUnboundLogin = Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null);
            }
        	sbr.append(Language.getLangValue("auth_conf_unbound_login", Language.getCurrLang(null), null)
        			+ colon
                	+ oldUnboundLogin
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ unboundLogin
                	+ comma);
        }
        
        // 描述
        String descp = "";
        if(StrTool.strNotNull(agentConfInfo.getDescp())){
        	descp = agentConfInfo.getDescp();
        }else{
        	descp = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        }
        if(StrTool.strEqualsToNull(agentConfInfo.getDescp(),oldAgentConfInfo.getDescp())){
    		sbr.append(Language.getLangValue("common_syntax_desc", Language.getCurrLang(null), null)
    				+ colon
    				+ descp);
    	}else{
    		String oldDescp = "";
            if(StrTool.strNotNull(oldAgentConfInfo.getDescp())){
            	oldDescp = oldAgentConfInfo.getDescp();
            }else{
            	oldDescp = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)
            			+ Language.getLangValue("comma", Language.getCurrLang(null), null);
            }
    		sbr.append(Language.getLangValue("common_syntax_desc", Language.getCurrLang(null), null)
                	+ colon
                	+ oldDescp
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ descp);
    	}
        return sbr.toString();
    }
    
    //编辑认证代理配
    public String writeBackendStr(String operater, BackendAction backendAction) {
    	BackendInfo backendInfo = backendAction.getBackendInfo();
    	BackendInfo oldBackendInfo = backendAction.getOldBackendInfo();
        StringBuilder sbr = new StringBuilder();
        String comma = Language.getLangValue("comma", Language.getCurrLang(null), null);
        String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
        
        // 配置名称
        if(StrTool.strEquals(backendInfo.getBackendname(), oldBackendInfo.getBackendname())){
        	sbr.append(Language.getLangValue("log_record_config_name", Language.getCurrLang(null), null)
    				+ backendInfo.getBackendname() + comma);
        }else{
        	sbr.append(Language.getLangValue("log_record_config_name", Language.getCurrLang(null), null)
                	+ oldBackendInfo.getBackendname()
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ backendInfo.getBackendname() + comma);
        }
        
        // 后端认证类型
        if(backendInfo.getBackendtype() == oldBackendInfo.getBackendtype()){
        	sbr.append(Language.getLangValue("auth_bk_type", Language.getCurrLang(null), null)
        			+ colon
    				+ backendType(backendInfo.getBackendtype()) + comma);
        }else{
        	sbr.append(Language.getLangValue("auth_bk_type", Language.getCurrLang(null), null)
        			+ colon
                	+ backendType(oldBackendInfo.getBackendtype())
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ backendType(backendInfo.getBackendtype()) + comma);
        }
        
        // 后端服务器IP
        if(StrTool.strEquals(backendInfo.getHost(), oldBackendInfo.getHost())){
        	sbr.append(Language.getLangValue("auth_bk_host", Language.getCurrLang(null), null)
        			+ colon
    				+ backendInfo.getHost() + comma);
        }else{
        	sbr.append(Language.getLangValue("auth_bk_host", Language.getCurrLang(null), null)
        			+ colon
                	+ oldBackendInfo.getHost()
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ backendInfo.getHost() + comma);
        }
        
        // 备后端服务器IP
        String spareHost = "";
        if(StrTool.strNotNull(backendInfo.getSparehost())){
        	spareHost = backendInfo.getSparehost();
        }else{
        	spareHost = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        }
        if(StrTool.strEquals(backendInfo.getSparehost(), oldBackendInfo.getSparehost())){
        	sbr.append(Language.getLangValue("auth_bk_sparehost", Language.getCurrLang(null), null)
        			+ colon
    				+ spareHost + comma);
        }else{
        	String oldSpareHost = "";
            if(StrTool.strNotNull(oldBackendInfo.getSparehost())){
            	oldSpareHost = oldBackendInfo.getSparehost();
            }else{
            	oldSpareHost = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
            }
        	sbr.append(Language.getLangValue("auth_bk_sparehost", Language.getCurrLang(null), null)
        			+ colon
                	+ oldSpareHost
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ spareHost + comma);
        }
        
        // 端口
        if(backendInfo.getPort() == oldBackendInfo.getPort()){
        	sbr.append(Language.getLangValue("auth_bk_port", Language.getCurrLang(null), null)
        			+ colon
        			+ backendInfo.getPort() 
        			+ comma);
        }else{
        	sbr.append(Language.getLangValue("auth_bk_port", Language.getCurrLang(null), null)
        			+ colon
                	+ oldBackendInfo.getPort()
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ backendInfo.getPort() + comma);
        }
        
        // 转发策略
        if(backendInfo.getPolicy() == oldBackendInfo.getPolicy()){
        	sbr.append(Language.getLangValue("auth_bk_policy", Language.getCurrLang(null), null)
        			+ colon
        			+ policy(backendInfo.getPolicy()) 
                    + comma);
        }else{
        	sbr.append(Language.getLangValue("auth_bk_policy", Language.getCurrLang(null), null)
        			+ colon
        			+ policy(oldBackendInfo.getPolicy()) 
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ policy(backendInfo.getPolicy()) + comma);
        }
        
        // 域名
        if(backendInfo.getBackendtype() == 1){ // 最新是AD
        	if(backendInfo.getBackendtype() == oldBackendInfo.getBackendtype()){
        		// AD---AD，显示域名
        		if(StrTool.strEquals(backendInfo.getDelimiter(), oldBackendInfo.getDelimiter())){
        			sbr.append(Language.getLangValue("auth_bk_domain", Language.getCurrLang(null), null)
                			+ colon
                			+ backendInfo.getDelimiter()
                			+ comma);
        		}else{
        			sbr.append(Language.getLangValue("auth_bk_domain", Language.getCurrLang(null), null)
                			+ colon
                        	+ oldBackendInfo.getDelimiter()
                        	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                        	+ backendInfo.getDelimiter() + comma);
        		}
        	}else{
        		// Radius---AD，显示域名
        		sbr.append(Language.getLangValue("auth_bk_domain", Language.getCurrLang(null), null)
            			+ colon
            			+ backendInfo.getDelimiter()
            			+ comma);
        	}
        }
        
        // 连接超时时间（秒）
        if(backendInfo.getTimeout() == oldBackendInfo.getTimeout()){
        	sbr.append(Language.getLangValue("auth_bk_timeout", Language.getCurrLang(null), null)
        			+ colon
        			+ backendInfo.getTimeout()
        			+ comma);
        }else{
        	sbr.append(Language.getLangValue("auth_bk_timeout", Language.getCurrLang(null), null)
        			+ colon
                	+ oldBackendInfo.getTimeout()
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ backendInfo.getTimeout() + comma);
        }
        
        // 连接超时重试次数
        if(backendInfo.getRetrycnt() == oldBackendInfo.getRetrycnt()){
        	sbr.append(Language.getLangValue("auth_bk_retrycnt", Language.getCurrLang(null), null)
        			+ colon
        			+ backendInfo.getRetrycnt());
        }else{
        	sbr.append(Language.getLangValue("auth_bk_retrycnt", Language.getCurrLang(null), null)
        			+ colon
                	+ oldBackendInfo.getRetrycnt()
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ backendInfo.getRetrycnt());
        }
        return sbr.toString();
    }
    
    /**
     * 转发策略
     * @author LXH
     * @date Mar 11, 2014 2:29:35 PM
     * @param flag
     * @return
     */
    public String policy(int flag){
    	String policy = "";
        if(flag == 1){
        	policy = Language.getLangValue("auth_bk_local_user_not_exist_unbound", Language.getCurrLang(null), null);
        }else if(flag == 9){
            policy = Language.getLangValue("auth_bk_local_user_not_exist_unbound", Language.getCurrLang(null), null)
            +Language.getLangValue("comma", Language.getCurrLang(null), null)
            +Language.getLangValue("auth_bk_local_auth_succ", Language.getCurrLang(null), null);
        }
        else{
        	policy = Language.getLangValue("auth_bk_local_auth_succ", Language.getCurrLang(null), null);
        }
    	return policy;
    }
    
    /**
     * 后端认证类型
     * @author LXH
     * @date Mar 11, 2014 2:12:19 PM
     * @param flag
     * @return
     */
    public String backendType(int flag){
    	String backendType = "";
        if(flag == 0){
        	backendType = "Radius";
        }else{
        	backendType = "AD";
        }
    	return backendType;
    }
    
    /**
     * 获取用户名格式
     * @author LXH
     * @date Mar 11, 2014 10:57:06 AM
     * @param flag
     * @return
     */
    public String userFormat(int flag){
    	String format = "";
    	if(flag == 0){
    		format = "user@ip";
        }else if(flag == 1){
        	format = "user";
        }else{
        	format = "user@domain";
        }
    	return format;
    }
    
    /**
     * 获取登录状态
     * @author LXH
     * @date Mar 11, 2014 10:57:45 AM
     * @param flag
     * @return
     */
    public String getProtectType(int flag){
    	String protectType = "";
    	if(flag == 0){
    		protectType = Language.getLangValue("auth_conf_no_protect", Language.getCurrLang(null), null);
    	}else if(flag == 1){
    		protectType = Language.getLangValue("auth_conf_protect_local_account", Language.getCurrLang(null), null);
    	}else if(flag == 2){
    		protectType = Language.getLangValue("auth_conf_protect_domain_account", Language.getCurrLang(null), null);
    	}else{
    		protectType = Language.getLangValue("auth_conf_pro_local_domain_account", Language.getCurrLang(null), null);
    	}
    	return protectType;
    }
    
    //编辑认证代理
    public String writeAgentStr(String operater, AgentAction agentAction) {
    	AgentInfo agentInfo = agentAction.getAgentInfo();
    	AgentInfo oldAgentInfo = agentAction.getOldAgentInfo();
        StringBuilder sbr = new StringBuilder();
        String comma = Language.getLangValue("comma", Language.getCurrLang(null), null);
        String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
        //认证代理名称
        if(StrTool.strEquals(agentInfo.getAgentname(), oldAgentInfo.getAgentname())){
            sbr.append(Language.getLangValue("auth_agent_agentname", Language.getCurrLang(null), null)
                    + colon
                    + agentInfo.getAgentname()+comma);
        }else{
            sbr.append(Language.getLangValue("auth_agent_agentname", Language.getCurrLang(null), null)
                    + colon
                    + oldAgentInfo.getAgentname()
                    + Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                    + agentInfo.getAgentname()+comma);
        }
        // 认证代理IP
        sbr.append(Language.getLangValue("auth_agent_agentip", Language.getCurrLang(null), null)
            	+ colon + agentInfo.getAgentipaddr()
            	+ comma);
        
        // 服务器列表
        List<?> hostiList = agentInfo.getHostIps();
        List<?> oldHostiList = oldAgentInfo.getHostIps();
    	String hostIPStr = "";
        if (StrTool.listNotNull(hostiList)) {
            for (int i = 0; i < hostiList.size(); i++) {
                hostIPStr = hostIPStr + hostiList.get(i) + "，";
            }
        }else{
        	hostIPStr = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null) + comma;
        }
        if(StrTool.isArrEquals(hostiList, oldHostiList)){
            sbr.append(Language.getLangValue("auth_agent_server_list", Language.getCurrLang(null), null)
                	+ colon + hostIPStr);
        }else{
        	if(!StrTool.listNotNull(hostiList) && !StrTool.listNotNull(oldHostiList)){
        		sbr.append( Language.getLangValue("auth_agent_server_list", Language.getCurrLang(null), null)
                    	+ colon
                    	+ Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)
                    	+ comma);
        	}else{
        		String oldHostIPStr = "";
                if (StrTool.listNotNull(oldHostiList)) {
                    for (int i = 0; i < oldHostiList.size(); i++) {
                    	oldHostIPStr = oldHostIPStr + oldHostiList.get(i) + "，";
                    }
                }else{
                	oldHostIPStr = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null) + comma;
                }
        		sbr.append( Language.getLangValue("auth_agent_server_list", Language.getCurrLang(null), null)
                    	+ colon + oldHostIPStr
                    	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                    	+ hostIPStr);
        	}
        }
        
        // 认证代理类型
        if(agentInfo.getAgenttype() == oldAgentInfo.getAgenttype()){
        	sbr.append(Language.getLangValue("auth_agent_agent_type", Language.getCurrLang(null), null)
                	+ colon
                	+ agentInfo.getAgenttypeStr());
        }else{
        	sbr.append(Language.getLangValue("auth_agent_agent_type", Language.getCurrLang(null), null)
                	+ colon + oldAgentInfo.getAgenttypeStr()
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ agentInfo.getAgenttypeStr());
        }
        
        // 认证代理配置
        String agentconfStr = "";
        if(agentInfo.getAgentconfid() == oldAgentInfo.getAgentconfid()){
        	if(oldAgentInfo.getAgentconfid() == 0){
        		agentconfStr = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        	}else{
        		agentconfStr = oldAgentInfo.getAgentconfStr();
        	}
        	sbr.append(Language.getLangValue("auth_agent_agentconf", Language.getCurrLang(null), null)
                    + colon
                    + agentconfStr
                    + comma);
        }else{
        	if(oldAgentInfo.getAgentconfid() == 0){
        		agentconfStr = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null) + comma;
        	}else{
        		agentconfStr = oldAgentInfo.getAgentconfStr();
        	}
        	sbr.append(Language.getLangValue("auth_agent_agentconf", Language.getCurrLang(null), null)
                	+ colon + agentconfStr
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ agentInfo.getAgentconfStr()
                	+ comma);
        }
        
        // 登录保护认证代理宽限期
        if(agentInfo.getFlag() == 1){
        	if(agentInfo.getGraceperiod() == oldAgentInfo.getGraceperiod()){
        		sbr.append(Language.getLangValue("auth_grace_period", Language.getCurrLang(null), null)
                        + colon
                        + agentInfo.getGraceperiodStr()
                        + comma);
        	}else{
        		String oldGraceperiodStr = "";
        		if(oldAgentInfo.getGraceperiod() == 0){
        			oldGraceperiodStr = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null) + comma;
        		}else{
        			oldGraceperiodStr = oldAgentInfo.getGraceperiodStr();
        		}
        		sbr.append(Language.getLangValue("auth_grace_period", Language.getCurrLang(null), null)
                    	+ colon + oldGraceperiodStr
                    	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                    	+ agentInfo.getGraceperiodStr()
                    	+ comma);
        	}
        }
        
        // 描述
        String descp = "";
        if(StrTool.strNotNull(agentInfo.getDescp())){
        	descp = agentInfo.getDescp();
        }else{
        	descp = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        }
        if(StrTool.strEqualsToNull(agentInfo.getDescp(), oldAgentInfo.getDescp())){
        	sbr.append(Language.getLangValue("common_syntax_desc", Language.getCurrLang(null), null)
                    + colon
                    + descp);
        }else{
        	String oldDescp = "";
            if(StrTool.strNotNull(oldAgentInfo.getDescp())){
            	oldDescp = oldAgentInfo.getDescp();
            }else{
            	oldDescp = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
            }
        	sbr.append(Language.getLangValue("common_syntax_desc", Language.getCurrLang(null), null)
                	+ colon + oldDescp
                	+ Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                	+ descp);
        }
        
        return sbr.toString();
    }
    
    //导出用户
    public String writeUserExportStr(String operater, UserExportAction userExportAction) {
        StringBuilder sbr = new StringBuilder();
        UserInfo userInfo = userExportAction.getOldUserInfo();
        String usExport = Language.getLangValue("user_export_user_total", Language.getCurrLang(null), null);
        String tiao = Language.getLangValue("page_records", Language.getCurrLang(null), null);
        String usExportErr = Language.getLangValue("user_export_validate_error", Language.getCurrLang(null), null);
        if (StrTool.listNotNull(userInfo.getUserIds())) {
            sbr.append(usExport + userInfo.getUserIds().size() + tiao);
        } else {
            sbr.append(usExportErr);
        }
        return sbr.toString();
    }

    //更新授权
    public String writeUpLicStr(String operater, LicInfoAction licInfoAction) {
        StringBuilder sbr = new StringBuilder();
        LicInfo licInfo = licInfoAction.getOldLicInfo();
        String lictype = Language.getLangValue("auth_ser_lic_file", Language.getCurrLang(null), null);
        String evalType = Language.getLangValue("lic_type_eval", Language.getCurrLang(null), null);
        String busiType = Language.getLangValue("lic_type_busi", Language.getCurrLang(null), null);
        String advancedType = Language.getLangValue("lic_type_advanced", Language.getCurrLang(null), null);
        String update = Language.getLangValue("log_info_lic_update", Language.getCurrLang(null), null);
        String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
        if (licInfo.getLictype() == 0) { // 评估
            if (licInfo.getOldLicType() == 1) { // 企业
                sbr.append(lictype + colon + evalType + update + busiType);
            } else if (licInfo.getOldLicType() == 2) { // 高级
                sbr.append(lictype + colon + evalType + update + advancedType);
            } else {
                sbr.append(lictype + colon + evalType + update + evalType);
            }
        } else if (licInfo.getLictype() == 1) { // 企业
            if (licInfo.getOldLicType() == 2) { // 高级
                sbr.append(lictype + colon + busiType + update + advancedType);
            } else {
                sbr.append(lictype + colon + busiType + update + busiType);
            }
        } else if (licInfo.getLictype() == 2)  { // 高级
            if (licInfo.getOldLicType() == 1) { // 企业
                sbr.append(lictype + colon + advancedType + update + busiType);
            } else {
                sbr.append(lictype + colon + advancedType + update + advancedType);
            }
        }else{
        	if (licInfo.getOldLicType() == 1) { // 没有授权信息
                sbr.append(lictype + colon + update + busiType);
            } else if(licInfo.getOldLicType() == 2){
                sbr.append(lictype + colon + update + advancedType);
            } else{
            	sbr.append(lictype + colon + update + evalType);
            }
        }

        return sbr.toString();
    }

    //删除角色
    public String writeRole(String operater, RoleInfoAction roleInfoAction) {
        StringBuilder sbr = new StringBuilder();
        List<RoleInfo> roleList = roleInfoAction.getRolestr();
        String roleNameStr = "";
        if (StrTool.listNotNull(roleList)) {
            for (int i = 0; i < roleList.size(); i++) {
                RoleInfo roleInfo = roleList.get(i);
                roleNameStr += roleInfo.getRoleName() + ",";
            }
            sbr.append(Language.getLangValue("admin_role_name", Language.getCurrLang(null), null)
                    + Language.getLangValue("colon", Language.getCurrLang(null), null)
                    + roleNameStr.substring(0, roleNameStr.length() - 1));
        }

        return sbr.toString();
    }

    //删除短信网关
    public String writeSms(String operater, SmsInfoAction smsInfoAction) {
        StringBuilder sbr = new StringBuilder();
        String smsName = smsInfoAction.getSmsNameArr();
        if (StrTool.strNotNull(smsName)) {
            sbr.append(Language.getLangValue("sms_name", Language.getCurrLang(null), null)
                    + Language.getLangValue("colon", Language.getCurrLang(null), null)
                    + smsName.substring(0, smsName.length() - 1));
        }
        return sbr.toString();
    }
    
    //删除客户
    public String writeDelCustomer(String operater, CustomerInfoAction custInfoAction) {
    	StringBuilder sbr = new StringBuilder();
		sbr.append("删除了 ");
		sbr.append(custInfoAction.getCustomerIds());
		sbr.append(" 条客户！");
    	return sbr.toString();
    }
    
    //添加客户
    public String writeAddCustomer(String operater, CustomerInfoAction custInfoAction) {
    	StringBuilder sbr = new StringBuilder();
    	sbr.append("客户编号: ");
		sbr.append(custInfoAction.getCustInfo().getCustid());
		sbr.append(", 客户名称: ");
		sbr.append(custInfoAction.getCustInfo().getCustname());
    	return sbr.toString();
    }
    
    //删除定制项目
    public String writeDelPrj(String operater, ProjectAction prjAction) {
    	StringBuilder sbr = new StringBuilder();
    	sbr.append("删除了 ");
    	sbr.append(prjAction.getPrjIds());
    	sbr.append(" 条定制项目！");
    	return sbr.toString();
    }
    
    //添加定制项目
    public String writeAddPrj(String operater, ProjectAction prjAction) {
    	StringBuilder sbr = new StringBuilder();
    	sbr.append("项目名称: ");
    	sbr.append(prjAction.getProject().getPrjname());
    	sbr.append(", 项目编号: ");
    	sbr.append(prjAction.getProject().getPrjid());
    	sbr.append(", 定制类型: ");
    	sbr.append(prjAction.getProject().getTypeStr());
    	sbr.append(", 基础版本: ");
    	sbr.append(prjAction.getProject().getTypeversion());
    	sbr.append(", 客户名称: ");
    	sbr.append(prjAction.getProject().getCustname());
    	return sbr.toString();
    }
    
  //更新定制项目
    public String writeEditPrj(String operater, ProjectAction prjAction) {
        StringBuilder sbr = new StringBuilder();
        if(!StrTool.strEquals(prjAction.getOldProject().getPrjname() , prjAction.getProject().getPrjname())){
            sbr.append("项目名称: ");
            sbr.append(prjAction.getOldProject().getPrjname());
            sbr.append(",更新为: ");
            sbr.append(prjAction.getProject().getPrjname()+"<br/>");
        }
        if(!StrTool.strEquals(prjAction.getOldProject().getPrjid() , prjAction.getProject().getPrjid())){
            sbr.append("项目编号: ");
            sbr.append(prjAction.getOldProject().getPrjid());
            sbr.append(",更新为: ");
            sbr.append(prjAction.getProject().getPrjid()+"<br/>");
        }
        if(!StrTool.strEquals(prjAction.getOldProject().getTypeStr() , prjAction.getProject().getTypeStr())){
            sbr.append("定制类型: ");
            sbr.append(prjAction.getOldProject().getTypeStr());
            sbr.append(",更新为: ");
            sbr.append(prjAction.getProject().getTypeStr()+"<br/>");
        }
        if(!StrTool.strEquals(prjAction.getOldProject().getTypeversion() , prjAction.getProject().getTypeversion())){
            sbr.append("基础版本: ");
            sbr.append(prjAction.getOldProject().getTypeversion());
            sbr.append(",更新为: ");
            sbr.append( prjAction.getProject().getTypeversion()+"<br/>");
        }
        if(!StrTool.strEquals(prjAction.getOldProject().getCustname() , prjAction.getProject().getCustname())){
            sbr.append("客户名称: ");
            sbr.append(prjAction.getOldProject().getCustname());
            sbr.append(",更新为: ");
            sbr.append( prjAction.getProject().getCustname());
        }
        return sbr.toString();
    }
    //删除定制项目信息
    public String writeDelPrjinfo(String operater, PrjinfoAction prjinfoAction) {
        StringBuilder sbr = new StringBuilder();
        sbr.append("删除了 ");
        sbr.append(prjinfoAction.getPrjinfoIds());
        sbr.append(" 条定制项目信息！");
        return sbr.toString();
    }
    
    //添加定制项目
    public String writeAddPrjinfo(String operater, PrjinfoAction prjinfoAction) {
        
        StringBuilder sbr = new StringBuilder();
        sbr.append("定制项目摘要: ");
        sbr.append(prjinfoAction.getPrjinfo().getPrjdesc());
        sbr.append(", 定制项目归类: ");
        sbr.append(prjinfoAction.getPrjinfo().getTypeStr());
        sbr.append(", 信息svn: ");
        sbr.append(prjinfoAction.getPrjinfo().getSvn());
        sbr.append(", 测试结果: ");
        sbr.append(prjinfoAction.getPrjinfo().getResults());
        sbr.append(", 创建日期: ");
        sbr.append(prjinfoAction.getPrjinfo().getCreatetimeStr());
        return sbr.toString();
    }
  //更新定制信息
    public String writeEditPrjinfo(String operater, PrjinfoAction prjinfoAction) {
        StringBuilder sbr = new StringBuilder();

        if(!StrTool.strEquals(prjinfoAction.getOldPrjinfo().getPrjdesc(),  prjinfoAction.getPrjinfo().getPrjdesc())){
            sbr.append("定制项目摘要: ");
            sbr.append(prjinfoAction.getOldPrjinfo().getPrjdesc());
            sbr.append(",更新为: ");
            sbr.append(prjinfoAction.getPrjinfo().getPrjdesc()+"<br/>");
        }
        if(!StrTool.strEquals(prjinfoAction.getOldPrjinfo().getType(),prjinfoAction.getPrjinfo().getType())){
            sbr.append("定制项目归类: ");
            sbr.append(prjinfoAction.getOldPrjinfo().getTypeStr());
            sbr.append(",更新为: ");
            sbr.append(prjinfoAction.getPrjinfo().getTypeStr()+"<br/>");
        }
        if(!StrTool.strEquals(prjinfoAction.getOldPrjinfo().getSvn(),prjinfoAction.getPrjinfo().getSvn())){
            sbr.append("信息svn: ");
            sbr.append(prjinfoAction.getOldPrjinfo().getSvn());
            sbr.append(",更新为: ");
            sbr.append(prjinfoAction.getPrjinfo().getSvn()+"<br/>");
        }
        if(!StrTool.strEquals(prjinfoAction.getOldPrjinfo().getResults(),prjinfoAction.getPrjinfo().getResults())){
            sbr.append("测试结果: ");
            sbr.append(prjinfoAction.getOldPrjinfo().getResults());
            sbr.append(",更新为: ");
            sbr.append(prjinfoAction.getPrjinfo().getResults());
        }
        return sbr.toString();
    }
    //删除上门记录
    public String writeDelResords(String operater, ResordsAction resordsAction) {
        StringBuilder sbr = new StringBuilder();
        sbr.append("删除了 ");
        sbr.append(resordsAction.getResordsIds());
        sbr.append(" 条上门记录！");
        return sbr.toString();
    }
    //添加上门记录
    public String writeAddResords(String operater, ResordsAction resordsAction) {

        StringBuilder sbr = new StringBuilder();
        sbr.append("项目名称:");
        sbr.append(resordsAction.getResords().getPrjid());
        if(StrTool.strNotNull(resordsAction.getResords().getRecordUser())){
        	sbr.append(",<br/>上门人员:");
        	sbr.append(resordsAction.getResords().getRecordUser());
        }
        sbr.append(",<br/>上门开始时间:");
        sbr.append(resordsAction.getResords().getRecordtimeStr());
        sbr.append(",<br/>上门结束时间:");
        sbr.append(resordsAction.getResords().getEndrecordtimeStr());
        if(StrTool.strNotNull(resordsAction.getResords().getReason())){
        	sbr.append(",<br/>上门原因:");
            sbr.append(resordsAction.getResords().getReason());
        }
        if(StrTool.strNotNull(resordsAction.getResords().getResults())){
        	sbr.append(",<br/>上门成果:");
            sbr.append(resordsAction.getResords().getResults());
        }
        if(StrTool.strNotNull(resordsAction.getResords().getRemark())){
        	sbr.append(",<br/>备注:");
            sbr.append(resordsAction.getResords().getRemark());
        }
        return sbr.toString();
    }

    //更新上门记录
    public String writeEditResords(String operater, ResordsAction resordsAction) {
    	int index = 0	;	//计数器
        StringBuilder sbr = new StringBuilder();
        if(!StrTool.strEquals(resordsAction.getOldResords().getPrjid() , resordsAction.getResords().getPrjid())){
            sbr.append("项目名称:");
            sbr.append(resordsAction.getOldResords().getPrjid());
            sbr.append(",更新为:");
            sbr.append(resordsAction.getResords().getPrjid()+"<br/>");
            index++;
        }
        if(StrTool.strNotNull(resordsAction.getResords().getRecordUser()) &&
        		!StrTool.strEquals(resordsAction.getOldResords().getRecordUser() , resordsAction.getResords().getRecordUser())){
            sbr.append("上门人员:");
            sbr.append(resordsAction.getOldResords().getRecordUser());
            sbr.append(",更新为:");
            sbr.append(resordsAction.getResords().getRecordUser()+"<br/>");
            index++;
        }
        if(StrTool.strNotNull(resordsAction.getResords().getRecordtimeStr()) &&
        		!StrTool.strEquals(resordsAction.getOldResords().getRecordtimeshowStr() , resordsAction.getResords().getRecordtimeStr())){
            sbr.append("上门开始时间:");
            sbr.append(resordsAction.getOldResords().getRecordtimeshowStr());
            sbr.append(",<br/>更新为:");
            sbr.append(resordsAction.getResords().getRecordtimeStr()+"<br/>");
            index++;
        }
        if(StrTool.strNotNull(resordsAction.getResords().getEndrecordtimeStr()) &&
        		!StrTool.strEquals(resordsAction.getOldResords().getEndrecordtimeStr() , resordsAction.getResords().getEndrecordtimeStr())){
            sbr.append("上门结束时间:");
            sbr.append(resordsAction.getOldResords().getEndrecordtimeStr());
            sbr.append(",<br/>更新为:");
            sbr.append(resordsAction.getResords().getEndrecordtimeStr()+"<br/>");
            index++;
        }
        if(StrTool.strNotNull(resordsAction.getResords().getReason()) &&
        		!StrTool.strEquals(resordsAction.getOldResords().getReason() , resordsAction.getResords().getReason())){
            sbr.append("上门原因:");
            sbr.append(resordsAction.getOldResords().getReason());
            sbr.append(",更新为:");
            sbr.append(resordsAction.getResords().getReason()+"<br/>");
            index++;
        }
        if(StrTool.strNotNull(resordsAction.getResords().getResults()) &&
        		!StrTool.strEquals(resordsAction.getOldResords().getResults(), resordsAction.getResords().getResults())){
            sbr.append("上门成果:");
            sbr.append(resordsAction.getOldResords().getResults());
            sbr.append(",更新为:");
            sbr.append(resordsAction.getResords().getResults()+"<br/>");
            index++;
        }

        if(StrTool.strNotNull(resordsAction.getResords().getRemark()) &&
        		!StrTool.strEquals(resordsAction.getOldResords().getRemark(), resordsAction.getResords().getRemark())){
            sbr.append("备注:");
            sbr.append(resordsAction.getOldResords().getRemark());
            sbr.append(",更新为:");
            sbr.append(resordsAction.getResords().getRemark());
            index++;
        }
        if(index==0){
        	sbr.append("无更新内容");
        }
        return sbr.toString();
    }

    //更新组织机构
    public String writeUpdateOrgInfoStr(String operater, OrgunitInfoAction orgInfoAction) {
        OrgunitInfo orgInfo = orgInfoAction.getOldOrgInfo();
        DomainInfo domainInfo = orgInfoAction.getOldDomainInfo();
        StringBuilder sbr = new StringBuilder();
        if (StrTool.objNotNull(orgInfo)) {
        	OrgunitInfo oldOrgInfo = orgInfoAction.getOrgInfo();
        	
        	// 机构编号
        	if (StrTool.strEquals(oldOrgInfo.getOrgunitNumber(), orgInfo.getOrgunitNumber())) {
        		String orgunitNumber = "";
        		if(StrTool.strNotNull(orgInfo.getOrgunitNumber())){
        			orgunitNumber = orgInfo.getOrgunitNumber();
        		}else{
        			orgunitNumber = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        		}
        		sbr.append(Language.getLangValue("org_code", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null)
                        + orgunitNumber + "，");
        	}else{
        		String orgunitNumber = "";
        		if(StrTool.strNotNull(orgInfo.getOrgunitNumber())){
        			orgunitNumber = orgInfo.getOrgunitNumber();
        		}else{
        			orgunitNumber = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        		}
        		String oldOrgunitNumber = "";
        		if(StrTool.strNotNull(oldOrgInfo.getOrgunitNumber())){
        			oldOrgunitNumber = oldOrgInfo.getOrgunitNumber();
        		}else{
        			oldOrgunitNumber = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)+ "，";
        		}
        		sbr.append(Language.getLangValue("org_code", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null)
                        + oldOrgunitNumber
                        + Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                        + orgunitNumber + "，");
        	}
        	
        	// 机构名称
            if (StrTool.strEquals(orgInfo.getOldOrgName(), orgInfo.getOrgunitName())) {
            	sbr.append(Language.getLangValue("org_name", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null)
                        + orgInfo.getOldOrgName() + "，");
            } else {
                sbr.append(Language.getLangValue("org_name", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null) + orgInfo.getOldOrgName()
                        + Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                        + orgInfo.getOrgunitName() + "，");
            }
            
            //　选择管理员
         // 该机构的管理员
        	if (StrTool.isArrEquals(orgInfo.getAdmins(), oldOrgInfo.getAdmins())) {
        		if(StrTool.listNotNull(oldOrgInfo.getAdmins())){
        			String orgAdmin = "";
        			for (int i=0 ; i<oldOrgInfo.getAdmins().size(); i++){
        				orgAdmin += (String)oldOrgInfo.getAdmins().get(i)+"，";
        			}
        			sbr.append(Language.getLangValue("org_be_manager_admin", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)
                            + orgAdmin);
        		}else{
        			sbr.append(Language.getLangValue("org_be_manager_admin", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)
                            + Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null));
        		}
        	}else{
        		String oldOrgAdmin = "";
        		if(!StrTool.listNotNull(oldOrgInfo.getAdmins())){
        			oldOrgAdmin = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)+ "，";
        		}else{
        			for (int i=0; i<oldOrgInfo.getAdmins().size(); i++){
        				oldOrgAdmin += (String)oldOrgInfo.getAdmins().get(i)+"，";
        			}
        		}
        		
        		String orgAdmin = "";
        		if(!StrTool.listNotNull(orgInfo.getAdmins())){
        			orgAdmin = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null) + "，";
        		}else{
        			for (int i=0; i<orgInfo.getAdmins().size(); i++){
        				orgAdmin += (String)orgInfo.getAdmins().get(i)+"，";
        			}
        		}
        		
        		if(!StrTool.listNotNull(orgInfo.getAdmins()) && !StrTool.listNotNull(oldOrgInfo.getAdmins())){
        			sbr.append(Language.getLangValue("org_be_manager_admin", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)
                            + Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null) + "，");
        		}else{
        			sbr.append(Language.getLangValue("org_be_manager_admin", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)
                            + oldOrgAdmin
                            + Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                            + orgAdmin);
        		}
        	}
            
            // 描述
        	if (StrTool.strEquals(orgInfo.getDescp(), oldOrgInfo.getDescp())) {
        		String desc = "";
        		if(!StrTool.strNotNull(orgInfo.getDescp())){
        			desc = orgInfo.getDescp();
        		}else{
        			desc = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        		}
        		sbr.append(Language.getLangValue("org_description", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null)
                        + desc);
        	}else{
        		String desc = "";
        		if(StrTool.strNotNull(orgInfo.getDescp())){
        			desc = orgInfo.getDescp();
        		}else{
        			desc = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        		}
        		String oldDesc = "";
        		if(StrTool.strNotNull(oldOrgInfo.getDescp())){
        			oldDesc = oldOrgInfo.getDescp();
        		}else{
        			oldDesc = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)+ "，";
        		}
        		sbr.append(Language.getLangValue("org_description", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null)
                        + oldDesc
                        + Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                        + desc);
        	}
        }
        if (StrTool.objNotNull(domainInfo)) {
        	DomainInfo oldDomain = orgInfoAction.getDomainIn();
            
        	// 企业标识writeUserConfStr
        	if (StrTool.strEquals(oldDomain.getDomainSn(), domainInfo.getDomainSn())) {
        		String domainSn = "";
        		if(StrTool.strNotNull(domainInfo.getDomainSn())){
        			domainSn = domainInfo.getDomainSn();
        		}else{
        			domainSn = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        		}
        		sbr.append(Language.getLangValue("domain_info_code", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null)
                        + domainSn + "，");
        	}else{
        		String domainSn = "";
        		if(StrTool.strNotNull(domainInfo.getDomainSn())){
        			domainSn = domainInfo.getDomainSn();
        		}else{
        			domainSn = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        		}
        		String oldDomainSn = "";
        		if(StrTool.strNotNull(oldDomain.getDomainSn())){
        			oldDomainSn = oldDomain.getDomainSn();
        		}else{
        			oldDomainSn = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)+ "，";
        		}
        		sbr.append(Language.getLangValue("domain_info_code", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null)
                        + oldDomainSn
                        + Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                        + domainSn + "，");
        	}
        	
        	// 企业名称
        	if (StrTool.strEquals(domainInfo.getDomainName(), domainInfo.getOldDomainName())) {
                sbr.append(Language.getLangValue("domain_info_name", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null)
                        + domainInfo.getOldDomainName() + "，");
            } else {
                sbr.append(Language.getLangValue("domain_info_name", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null)
                        + domainInfo.getOldDomainName()
                        + Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                        + domainInfo.getDomainName()+ "，");
            }
            
        	// 该机构的管理员
        	if (StrTool.isArrEquals(domainInfo.getAdminIds(), oldDomain.getAdminIds())) {
        		if(StrTool.listNotNull(oldDomain.getAdminIds())){
        			String orgAdmin = "";
        			for (int i=0 ; i<oldDomain.getAdminIds().size(); i++){
        				orgAdmin += (String)oldDomain.getAdminIds().get(i)+"，";
        			}
        			sbr.append(Language.getLangValue("org_be_manager_admin", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)
                            + orgAdmin);
        		}else{
        			sbr.append(Language.getLangValue("org_be_manager_admin", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)
                            + Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null));
        		}
        	}else{
        		String oldOrgAdmin = "";
        		if(!StrTool.listNotNull(oldDomain.getAdminIds())){
        			oldOrgAdmin = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)+ "，";
        		}else{
        			for (int i=0; i<oldDomain.getAdminIds().size(); i++){
        				oldOrgAdmin += (String)oldDomain.getAdminIds().get(i)+"，";
        			}
        		}
        		
        		String orgAdmin = "";
        		if(!StrTool.listNotNull(domainInfo.getAdminIds())){
        			orgAdmin = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null) + "，";
        		}else{
        			for (int i=0; i<domainInfo.getAdminIds().size(); i++){
        				orgAdmin += (String)domainInfo.getAdminIds().get(i)+"，";
        			}
        		}
        		
        		if(!StrTool.listNotNull(domainInfo.getAdminIds()) && !StrTool.listNotNull(oldDomain.getAdminIds())){
        			sbr.append(Language.getLangValue("org_be_manager_admin", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)
                            + Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null) + "，");
        		}else{
        			sbr.append(Language.getLangValue("org_be_manager_admin", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)
                            + oldOrgAdmin
                            + Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                            + orgAdmin);
        		}
        	}
        	
        	// 描述
        	if (StrTool.strEquals(oldDomain.getDescp(), domainInfo.getDescp())) {
        		String desc = "";
        		if(!StrTool.strNotNull(domainInfo.getDescp())){
        			desc = domainInfo.getDescp();
        		}else{
        			desc = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        		}
        		sbr.append(Language.getLangValue("org_description", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null)
                        + desc);
        	}else{
        		String desc = "";
        		if(StrTool.strNotNull(domainInfo.getDescp())){
        			desc = domainInfo.getDescp();
        		}else{
        			desc = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
        		}
        		String oldDesc = "";
        		if(StrTool.strNotNull(oldDomain.getDescp())){
        			oldDesc = oldDomain.getDescp();
        		}else{
        			oldDesc = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)+ "，";
        		}
        		sbr.append(Language.getLangValue("org_description", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null)
                        + oldDesc
                        + Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                        + desc);
        	}
        }
        return sbr.toString();
    }

    // 删除组织机构
    public String writedeleteOrgInfoStr(String operater, OrgunitInfoAction orgInfoAction) {
        StringBuilder sbr = new StringBuilder();
        OrgunitInfo orgInfo = orgInfoAction.getOldOrgInfo();
        if (StrTool.objNotNull(orgInfo)) {
            sbr.append(Language.getLangValue("org_name", Language.getCurrLang(null), null)
                    + Language.getLangValue("colon", Language.getCurrLang(null), null) + orgInfo.getOrgunitName());
        }
        return sbr.toString();
    }

    // 添加组织机构
    public String writeaddOrgInfoStr(String operater, OrgunitInfoAction orgInfoAction) {
        StringBuilder sbr = new StringBuilder();
        OrgunitInfo orgInfo = orgInfoAction.getOldOrgInfo();
        String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
        String str = Language.getLangValue("org_add_child_org", Language.getCurrLang(null), null) + colon;
        if (StrTool.objNotNull(orgInfo)) {
            sbr.append(Language.getLangValue("org_parent_org", Language.getCurrLang(null), null) + colon
                    + orgInfo.getOrgParentName() + Language.getLangValue("comma", Language.getCurrLang(null), null)
                    + str + orgInfo.getOrgunitName() + Language.getLangValue("comma", Language.getCurrLang(null), null));
            
            // 机构编号
            String orgunitNumber = "";
            if(StrTool.strNotNull(orgInfo.getOrgunitNumber())){
            	orgunitNumber = orgInfo.getOrgunitNumber();
            }else{
            	orgunitNumber = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
            }
            sbr.append(Language.getLangValue("org_code", Language.getCurrLang(null), null) + colon
                    + orgunitNumber + Language.getLangValue("comma", Language.getCurrLang(null), null));
            
            // 选择管理员
            String admins = "";
            if(StrTool.listNotNull(orgInfo.getAdmins())){
            	for (int i=0; i<orgInfo.getAdmins().size(); i++){
            		admins += orgInfo.getAdmins().get(i) + "，";
            	}
            }else{
            	admins = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)+ "，";
            }
            sbr.append(Language.getLangValue("org_be_manager_admin", Language.getCurrLang(null), null) + colon
                    + admins);
            
            // 描述
            String desc = "";
            if(StrTool.strNotNull(orgInfo.getDescp())){
            	desc = orgInfo.getDescp();
            }else{
            	desc = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
            }
            sbr.append(Language.getLangValue("org_description", Language.getCurrLang(null), null) + colon
                    + desc);
        }
        return sbr.toString();
    }

    //更新域名称
    public String writeDomainInfoStr(String operater, DomainInfoAction domainAction) {
        DomainInfo domainInfo = domainAction.getDomainInfo();
        StringBuilder sbr = new StringBuilder();
        if (StrTool.strEquals(domainInfo.getDomainName(), domainInfo.getOldDomainName())) {
            sbr.append(Language.getLangValue("log_record_domain_no_change", Language.getCurrLang(null), null));
        } else {
            sbr.append(domainInfo.getOldDomainName()
                    + Language.getLangValue("log_record_change_to", Language.getCurrLang(null), null)
                    + domainInfo.getDomainName());
        }
        return sbr.toString();
    }
}
