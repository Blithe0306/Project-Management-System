/**
 *Administrator
 */
package com.ft.otp.core.filter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.config.ProxoolConfig;
import com.ft.otp.common.config.TrustIpConfig;
import com.ft.otp.manager.confinfo.access.entity.AccessInfo;
import com.ft.otp.manager.login.entity.LinkUser;
import com.ft.otp.manager.login.service.OnlineUsers;
import com.ft.otp.manager.token.distmanager.action.ActivateMTAction;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.IpTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 系统安全保护拦截器
 *
 * @Date in Oct 12, 2011,10:08:52 AM
 *
 * @author TBM
 */
public class SafeFilter implements Filter {

    private static boolean is_enabled;
    private static final String IS_SAFE_FILTER = "IS_SAFE_FILTER";

    public SafeFilter() {
        is_enabled = true;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        String filterStr = filterConfig.getInitParameter(IS_SAFE_FILTER);
        if (!StringUtils.isEmpty(filterStr)) {
            is_enabled = Boolean.valueOf(filterStr).booleanValue();
        }
    }

    /**
     * 系统关键项安全检查、拦截、过滤方法
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException,
            ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (!is_enabled) {
            filter.doFilter(httpRequest, httpResponse);
            return;
        }

        String url = httpRequest.getServletPath();//URL请求串
        HttpSession session = httpRequest.getSession();
        if (null == session) {
            session = httpRequest.getSession(true);
            return;
        }
        String sessionId = session.getId();
        LinkUser linkUser = OnlineUsers.getUser(sessionId);

        //手机令牌在线分发业务请求
        if (url.indexOf(Constant.MOBILE_DIST) > -1) {
            ActivateMTAction mtAction = new ActivateMTAction();
            String result = mtAction.execute(httpRequest);

            OutputStream oStream = httpResponse.getOutputStream();
            try {
                oStream.write(result.getBytes(), 0, result.getBytes().length);
                httpResponse.flushBuffer();
            } catch (Exception e) {
            } finally {
                if (oStream != null) {
                    oStream.close();
                }
            }

            return;
        }

        //IP访问控制策略启用状态
        String ipCheckEnable = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_CENTER,
                ConfConstant.ENABLED_TRUSTIP_CHECK);
        //如启用，则客户端受信任的IP检查
        if (StrTool.strEquals(ipCheckEnable, StrConstant.common_number_1)) {
            //客户端受信任的IP检查
            String clientIp = IpTool.getIpAddr(httpRequest);//客户端IP
            boolean ifTrustIp = trustIpCheck(clientIp);
            if (!ifTrustIp && null == linkUser) {
                if (url.indexOf("/login") > -1 || url.indexOf("/logout") > -1
                        || url.indexOf("/manager/common/js/") > -1 || url.indexOf("/manager/common/css/") > -1
                        || url.indexOf("/index.jsp") > -1 || url.indexOf("/install/") > -1
                        || url.indexOf("/images/") > -1 || url.indexOf("/errorview.jsp") > -1
                        || url.indexOf("/error.jsp") > -1) {
                    if (url.indexOf("/errorview.jsp") > -1 || url.indexOf("/error.jsp") > -1) {
                        filter.doFilter(httpRequest, httpResponse);
                        return;
                    }
                    httpRequest.getRequestDispatcher("/error.jsp?errcode=707").forward(httpRequest, httpResponse);
                    return;
                }
            }
        }

        Boolean dbConn = (Boolean) httpRequest.getSession().getServletContext().getAttribute(Constant.DATABASE_IF_CONN);
        Boolean licCheck = (Boolean) httpRequest.getSession().getServletContext().getAttribute(
                Constant.LICENCE_IF_EFFECTIVE);
        Boolean isAdmin = (Boolean) httpRequest.getSession().getServletContext().getAttribute(
                Constant.DATABASE_IF_SUPERMAN);
        Boolean isLocal = (Boolean) httpRequest.getSession().getServletContext().getAttribute(
                Constant.DATABASE_IF_LOCALIP);
        Boolean isConfEmailServ = (Boolean) httpRequest.getSession().getServletContext().getAttribute(
                Constant.EMAILSERVER_IF_CONF);

        boolean dbConnRet = false;
        boolean licCheckRet = false;
        boolean isAdminRet = false;
        boolean isLocalRet = false;
        boolean isConfEmailServRet = false;
        boolean dbSrcType = ConfDataFormat.getDbDataSrcType();

        if (null != dbConn) {
            dbConnRet = dbConn;
        }
        if (null != licCheck) {
            licCheckRet = licCheck;
        }
        if (null != isAdmin) {
            isAdminRet = isAdmin;
        }
        if (null != isLocal) {
            isLocalRet = isLocal;
        }
        if (null != isConfEmailServ) {
            isConfEmailServRet = isConfEmailServ;
        }

        boolean runDbConn = Constant.DATABASE_CONN_RESULT;//系统运行时数据库连接状态
        if (url.indexOf("/validationCode") > -1 
                || url.indexOf("/service/heartbeat!testServerState.action")>-1
                || url.indexOf("/service/heartbeat!getRunningState.action")>-1
                || url.indexOf("/login") > -1
                || url.indexOf("/logout") > -1
                || url.indexOf("/manager/common/js/") > -1
                || url.indexOf("/manager/common/css/") > -1
                || url.indexOf("/index.jsp") > -1
                || url.indexOf("/install/") > -1
                || url.indexOf("/images/") > -1
                || url.indexOf("/errorview.jsp") > -1
                || url.indexOf("/error.jsp") > -1
                || url.indexOf("/manager/lic/license!upLic.action") > -1
                || url.indexOf("/manager/admin/user/adminUser!getPwdDeath.action") > -1
                || url.indexOf("/manager/admin/user/adminUser!setInitAdminPwd.action") > -1
                || url.indexOf("/manager/admin/user/adminUser!addUnionAdmin.action") > -1
                || url.indexOf("/manager/admin/user/adminUser!getAdminPwd.action") > -1
                || url.indexOf("/manager/admin/user/adminUser!validEmail.action") > -1
                || url.indexOf("/manager/authmgr/server/authServer!modifyLocalIp.action") > -1
                || url.indexOf("/manager/authmgr/server/authServer!isLocalIp.action") > -1
                || url.indexOf("/manager/data/databak!revert.action") > -1
                || url.indexOf("/config/center!reloadConf.action") > -1
                || url.indexOf("/manager/common/language/langAction!getLangTagVal.action") > -1
                || (url.indexOf("/manager/main/layout.jsp") > -1 && (!dbConnRet || !licCheckRet || !isAdminRet
                        || !isConfEmailServRet || isLocalRet))) {// getLangTagVal.action zxh添加多语言请求可以通过
            if ((url.indexOf("/login") > -1 || url.indexOf("/manager/main/layout.jsp") > -1)
                    && (!dbConnRet || !licCheckRet || !isAdminRet || !isConfEmailServRet || isLocalRet || dbSrcType)) {
                if (httpRequest.getHeader("x-requested-with") != null
                        && httpRequest.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
                    //如果是AJAX请求响应头会有x-requested-with  
                    httpResponse.setHeader("sessionstatus", "sessionOut");//在响应头设置session状态 
                    httpResponse.getWriter().print("sessionOut");

                    return;
                }

                httpRequest.getRequestDispatcher("/install/index.jsp").forward(httpRequest, httpResponse);
                return;
            }
            if (dbConnRet
                    && licCheckRet
                    && isAdminRet
                    && !isLocalRet
                    && isConfEmailServRet
                    && !dbSrcType
                    && (url.indexOf("/install/index.jsp") > -1 || url.indexOf("/install/init_select.jsp") > -1
                            || url.indexOf("/install/dbrecover.jsp") > -1 || url.indexOf("/install/uploadLic.jsp") > -1
                            || url.indexOf("/install/adminpage.jsp") > -1 || url.indexOf("/install/local_ip.jsp") > -1
                            || url.indexOf("/install/dbpage.jsp") > -1 || url.indexOf("/install/languagepage.jsp") > -1
                            || url.indexOf("/install/finish.jsp") > -1
                            || url.indexOf("/manager/admin/user/adminUser!addUnionAdmin.action") > -1 || url
                            .indexOf("/manager/authmgr/server/authServer!modifyLocalIp.action") > -1)) {
                httpRequest.getRequestDispatcher("/index.jsp").forward(httpRequest, httpResponse);
                return;
            }

            if ((url.indexOf("/errorview.jsp") > -1 || url.indexOf("/error.jsp") > -1) && !runDbConn) {
                runDbConn = ProxoolConfig.databaseShift();
                if (runDbConn) {
                    Constant.DATABASE_CONN_RESULT = runDbConn;

                    httpRequest.getRequestDispatcher("/index.jsp").forward(httpRequest, httpResponse);
                    return;
                } else {
                    filter.doFilter(httpRequest, httpResponse);
                    return;
                }
            }

            if (dbConnRet && licCheckRet && isAdminRet && !isLocalRet && !runDbConn) {
                if (httpRequest.getHeader("x-requested-with") != null
                        && httpRequest.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
                    //如果是AJAX请求响应头会有x-requested-with  
                    httpResponse.setHeader("sessionstatus", "sessionOut");//在响应头设置session状态 
                    httpResponse.getWriter().print("sessionOut");

                    return;
                } else {
                    httpRequest.getRequestDispatcher("/error.jsp?errcode=303").forward(httpRequest, httpResponse);
                    return;
                }
            }

            filter.doFilter(httpRequest, httpResponse);
            return;
        }

        //2、会话检查、包括同一帐号是否异地登录检查
        boolean invalidation = true;
        if (null != linkUser) {
            long loginTime = linkUser.getLoginTime();
            long currTime = StrTool.timeSecondL();
            long time = currTime - loginTime;
            int sessionMax = session.getMaxInactiveInterval();
            if (time > sessionMax) {
                //注掉下列一行，就可以实现只要在操作状态，始终维护会话状态
                //invalidation = false;
            }
        } else {
            invalidation = false;
        }

        //会话过期，要求重新登录
        if (dbConnRet && licCheckRet && isAdminRet && !isLocalRet && !invalidation) {
            OnlineUsers.remove(sessionId);
            String removeSession = OnlineUsers.getRemoveSession(sessionId);
            if (StrTool.strEquals(sessionId, removeSession)) {
                httpRequest.setAttribute("invalidation", true);
                OnlineUsers.removeSession(removeSession);
            }

            if (httpRequest.getHeader("x-requested-with") != null
                    && httpRequest.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
                //如果是AJAX请求响应头会有x-requested-with  
                httpResponse.setHeader("sessionstatus", "sessionOut");//在响应头设置session状态 
                httpResponse.getWriter().print("sessionOut");

                return;
            }

            if (StrTool.strNotNull(url)) {
                httpRequest.getRequestDispatcher("/index.jsp").forward(httpRequest, httpResponse);
            } else {
                filter.doFilter(httpRequest, httpResponse);
            }

            return;
        } else if (!dbConnRet || !licCheckRet || !isAdminRet || isLocalRet) {
            if (httpRequest.getHeader("x-requested-with") != null
                    && httpRequest.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
                //如果是AJAX请求响应头会有x-requested-with  
                httpResponse.setHeader("sessionstatus", "sessionOut");//在响应头设置session状态 
                httpResponse.getWriter().print("sessionOut");

                return;
            } else {
                httpRequest.getRequestDispatcher("/install/index.jsp").forward(httpRequest, httpResponse);
                return;
            }
        } else if (dbConnRet && licCheckRet && isAdminRet && !isLocalRet && !runDbConn) {
            if (httpRequest.getHeader("x-requested-with") != null
                    && httpRequest.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
                //如果是AJAX请求响应头会有x-requested-with  
                httpResponse.setHeader("sessionstatus", "sessionOut");//在响应头设置session状态 
                httpResponse.getWriter().print("sessionOut");

                return;
            } else {
                httpRequest.getRequestDispatcher("/error.jsp?errcode=303").forward(httpRequest, httpResponse);
                return;
            }
        }

        filter.doFilter(httpRequest, httpResponse);
        return;
    }

    /**
     * 销毁
     */
    public void destroy() {
    }

    /**
     * 客户端访问管理中心的信任IP检查
     * 
     * @Date in Aug 15, 2013,1:27:00 PM
     * @return
     */
    private boolean trustIpCheck(String clientIp) {
        if (StrTool.strEquals("127.0.0.1", clientIp) || StrTool.strEquals("localhost", clientIp)) {
            return true;
        }

        boolean result = false;
        Map<Integer, Object> valueMap = TrustIpConfig.trustipMap;
        if (!StrTool.mapNotNull(valueMap)) {
            return result;
        }
        for (Object key : valueMap.keySet()) {
            AccessInfo accessInfo = (AccessInfo) valueMap.get(key);
            if (StrTool.strEquals(clientIp, accessInfo.getStartip())) {
                result = true;
                break;
            } else {
                if (StrTool.strNotNull(accessInfo.getEndip())) {
                    if (betweenIP(accessInfo.getStartip(), accessInfo.getEndip(), clientIp)) {
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 验证IP是否存在IP段
     * 
     * @Date in Aug 15, 2013,5:54:00 PM
     * @param start
     * @param end
     * @param current
     * @return
     */
    public boolean betweenIP(String start, String end, String current) {
        boolean result = false;

        start = start.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
        start = start.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
        start = start.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");
        start = start.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");

        if (StrTool.strNotNull(end)) {
            end = end.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
            end = end.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
            end = end.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");
            end = end.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");
        }

        current = current.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
        current = current.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
        current = current.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");
        current = current.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");

        if ((current.compareTo(start) >= 0) && (current.compareTo(end) <= 0)) {
            result = true;
        }

        return result;
    }

    /**
     * 获取真实客户端IP地址
     * 
     * @Date in Aug 22, 2013,1:47:04 PM
     * @param request
     * @return
     */
    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

}
