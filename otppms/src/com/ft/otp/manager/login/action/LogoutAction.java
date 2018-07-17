/**
 *Administrator
 */
package com.ft.otp.manager.login.action;

import javax.servlet.http.HttpSession;
import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.config.ConfConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.LogCommonObj;

import com.ft.otp.manager.login.service.OnlineUsers;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理员安全退出 Action
 *
 * @Date in Apr 17, 2011,12:28:16 PM
 *
 * @author TBM
 */
public class LogoutAction extends BaseAction {

    private static final long serialVersionUID = -6677085887638095255L;
    //日志记录类
    LogCommonObj commonObj = new LogCommonObj();

    /**
     * 退出
     * 销毁相关信息
     * @Date in Apr 18, 2011,1:59:18 PM
     * @return
     * @throws BaseException 
     */
    public String logout() throws BaseException {
        String topage = request.getParameter("topage");
        HttpSession session = getSession();
        try {
        	// 日志记录
        	if(StrTool.strNotNull(super.getCurLoginUser())){
        		commonObj.addAdminLoginLog(AdmLogConstant.log_aid_lgout, AdmLogConstant.log_obj_otpserver, Language.getLangStr(
                        request, "admin_info_account")
                        + Language.getLangStr(request, "colon") + super.getCurLoginUser(), null, 0, super.getCurLoginUser());
        	}
            if (null != session) {
                OnlineUsers.remove(session.getId());
            }
        } catch (Exception e) {
            commonObj.addAdminLoginLog(AdmLogConstant.log_aid_lgout, AdmLogConstant.log_obj_otpserver, Language.getLangStr(
                    request, "admin_info_account")
                    + Language.getLangStr(request, "colon") + super.getCurLoginUser(), null, 1, super.getCurLoginUser());
        }
        if (StrTool.strEquals(topage, "init")) {
            session.getServletContext().setAttribute(Constant.DATABASE_IF_CONN, false);
            session.getServletContext().setAttribute(Constant.LICENCE_IF_EFFECTIVE, false);
            session.getServletContext().setAttribute(Constant.DATABASE_IF_SUPERMAN, false);
            session.getServletContext().setAttribute(Constant.DATABASE_IF_LOCALIP, true);
            session.getServletContext().setAttribute(Constant.EMAILSERVER_IF_CONF, false);

            return "init";
        }
        
        String isChangeLang = (String)request.getSession(true).getAttribute(Constant.SET_NEW_LANGUAGE);
        if (StrTool.strEquals(isChangeLang, "yes")) {
            request.getSession().invalidate();
        }
        
        return SUCCESS;
    }

}
