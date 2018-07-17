/**
 *Administrator
 */
package com.ft.otp.core.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.ft.otp.common.config.OtpPermConfig;
import com.ft.otp.manager.login.entity.LinkUser;
import com.ft.otp.manager.login.service.OnlineUsers;
import com.ft.otp.util.tool.StrTool;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 权限拦截器，对管理员权限进行拦截(处理没有权限访问的url)
 *
 * @Date in Jul 1, 2011,4:03:49 PM
 *
 * @author TBM
 */
public class PermInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 3131899427751400500L;

    /**
     * 基础拦截器
     */
    public String intercept(ActionInvocation invo) throws Exception {
        try {
            HttpServletRequest request = ServletActionContext.getRequest();
            HttpSession session = request.getSession();
            LinkUser linkUser = OnlineUsers.getUser(session.getId());

            //请求URL
            String url = request.getServletPath();

            if (url.indexOf("/login!") != -1 || url.indexOf("/logout") > -1 || url.indexOf("/install/") > -1
                    || url.indexOf("/validationCode") > -1 || url.indexOf("/language/langAction!") > -1
                    || url.indexOf("/manager/lic/license!upLic.action") > -1) {
                return invo.invoke();
            }

            //获取action名称
            String actionName = invo.getInvocationContext().getName();
            //获取方法名称
            String methodName = invo.getProxy().getMethod();
            if (StrTool.strEquals(actionName, "userInfo")) {// 用户
                if (StrTool.strEqualsIgnoreCase(methodName, "batchOper")) {
                    String oper = request.getParameter("oper");
                    if (StrTool.strNotNull(oper)) {
                        url = url + "?oper=" + oper;
                    } else {
                        return "urlerror";
                    }
                }
            } else if (StrTool.strEquals(actionName, "token")) {// 令牌
                if (StrTool.strEqualsIgnoreCase(methodName, "modifyBatch")) {
                    String oper = request.getParameter("oper");
                    if (StrTool.strNotNull(oper)) {
                        url = url + "?oper=" + oper;
                    } else {
                        return "urlerror";
                    }
                } else if (StrTool.strEqualsIgnoreCase(methodName, "modify")) {
                    String operType = request.getParameter("operType");
                    if (StrTool.strNotNull(operType)) {
                        url = url + "?operType=" + operType;
                    } else {
                        return "urlerror";
                    }

                    String sign = request.getParameter("sign");
                    if (StrTool.strNotNull(sign)) {
                        url = url + "&sign=" + sign;
                    }
                }
            } else if (StrTool.strEquals(actionName, "userConfAction")
                    || StrTool.strEquals(actionName, "tokenConfAction") || StrTool.strEquals(actionName, "distManager")
                    || StrTool.strEquals(actionName, "center")) {// 手机令牌分发 用户配置 手机
                if (StrTool.strEqualsIgnoreCase(methodName, "modify")) {
                    String oper = request.getParameter("oper");
                    if (StrTool.strNotNull(oper)) {
                        url = url + "?oper=" + oper;
                    }
                }
            }

            //只拦截属于权限范围内的URL
            Map<String, String> allUrlMap = OtpPermConfig.getPermMap();
            if (StrTool.mapNotNull(allUrlMap)) {
                if (!allUrlMap.containsKey(url)) {
                    return invo.invoke();
                }
            }

            //权限检查，根据可以访问的URL链接进行检查
            boolean isPerm = true;
            if (null != linkUser) {
                Map<String, String> urlMap = linkUser.getUrlMap();
                if (url.indexOf(".action") != -1 && url.indexOf("!") != -1) {
                    String code = urlMap.get(url);
                    if (StrTool.strNotNull(code)) {
                        isPerm = true;
                    } else {
                        isPerm = false;
                    }

                }
            } else {
                isPerm = false;
            }

            //没有权限访问的链接（707异常）
            if (!isPerm) {
                return Action.NONE;
            }
        } catch (Exception e) {
            StrTool.print("PermInterceptor Error!");
        }

        return invo.invoke();
    }
}
