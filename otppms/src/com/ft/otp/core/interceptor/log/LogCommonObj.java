/**
 *Administrator
 */
package com.ft.otp.core.interceptor.log;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.StrConstant;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.login.entity.LinkUser;
import com.ft.otp.manager.login.service.OnlineUsers;
import com.ft.otp.manager.logs.adminlog.entity.AdminLogInfo;
import com.ft.otp.manager.logs.adminlog.service.IAdminLogServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 日志记录抽象出来的公共的方法，用于完成相关的操作
 *
 * @Date in Jun 10, 2011,9:43:37 AM
 *
 * @author TBM
 */
public class LogCommonObj {

    private Logger logger = Logger.getLogger(LogCommonObj.class);
    //管理员日志服务接口
    IAdminLogServ adminLogServ = (IAdminLogServ) AppContextMgr.getObject("adminLogServ");

    /**
     * 添加管理员日志
     * @Date in Jun 11, 2011,11:25:06 AM
     * @param operator
     * @param acid
     * @param acobj
     * @param desc 单一操作描述信息
     * @param descList 批量操作描述信息列表
     * @param result
     * @throws BaseException
     */
    public void addAdminLog(int acid, int acobj, String desc, List<String> descList, int result) {
    
        addResultLog(acid, acobj, desc, descList, result, null);
    }
    /**
     * 管理员登录记录日志
     * @Date in Sep 27, 2013,3:24:40 PM
     * @param acid
     * @param acobj
     * @param desc
     * @param descList
     * @param result
     * @param operator
     */
    public void addAdminLoginLog(int acid, int acobj, String desc, List<String> descList, int result, String operator) {
        
        addResultLog(acid, acobj, desc, descList, result, operator);
    }
    
    public void addResultLog(int acid, int acobj, String desc, List<String> descList, int result, String operator) {
        try {
        	HttpServletRequest request = null;
            if (ServletActionContext.getContext()!=null) {//会话为null，取request对象，获取session
                request = ServletActionContext.getRequest();
            }
            String operUser = "";
            String sessionId = "";
            String clientIP = "";
            if (null != request) {
                HttpSession session = request.getSession();
                if (StrTool.strNotNull(request.getParameter("sessionId"))) {//如果有指定的sessionId; 那么重置一下
                    sessionId = request.getParameter("sessionId");
                } else {
                    if (null != session) {
                        sessionId = session.getId();
                    }
                }
                clientIP = request.getRemoteHost();
            }

            // 获取在线用户
            if (StrTool.strNotNull(sessionId)) {
                LinkUser linkUser = OnlineUsers.getUser(sessionId);
                if (null != linkUser) {
                    operUser = linkUser.getUserId();
                }else {
                    if (null != operator) {
                        operUser = operator;
                    }
                }
            }
            if (!StrTool.strNotNull(clientIP) || clientIP.indexOf(":") != -1) {// 获取本地ipv6 ip为0:0:0:0:0:1此处转换 
                clientIP = StrConstant.LOCALHOST_IP;
            }

            AdminLogInfo logInfo = new AdminLogInfo();
            logInfo.setOperator(operUser);
            logInfo.setActionid(acid);
            logInfo.setLogtime(StrTool.timeSecond());
            logInfo.setActionobject(acobj);
            logInfo.setActionresult(result);
            logInfo.setClientip(clientIP);
            if (StrTool.strNotNull(desc)) {
                logInfo.setDescp(desc);
            }
            if (StrTool.listNotNull(descList)) {
                logInfo.setDescList(descList);
            }

            adminLogServ.addObj(logInfo);
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    /**
     * 被拦截方法的执行结果
     * @Date in Jun 10, 2011,6:07:39 PM
     * @param invocation
     * @return
     */
    public int operResult(MethodInvocation invocation) {
        int result = 0;
        try {
            Object object = invocation.proceed();
            if (object instanceof Boolean) {
                boolean bol = (Boolean) object;
                if (!bol) {
                    result = 1;
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
            result = 1;
        }

        return result;
    }

    /**
     * 删除数据的标识keyId
     * @Date in Jun 10, 2011,6:30:49 PM
     * @param request
     * @return
     */
    public String getKeyId(MethodInvocation invocation) {
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        StringBuilder sBuilder = new StringBuilder();
        if (object instanceof Set<?>) {
            Set<String> set = (Set<String>) object;
            Iterator<String> iter = set.iterator();
            while (iter.hasNext()) {
                String keyId = iter.next();
                if (keyId.indexOf(":") != -1) {
                    keyId = keyId.split(":")[0];
                }
                sBuilder.append(keyId).append(",");
            }
        }
        String keyId = sBuilder.toString();
        if (keyId.endsWith(",")) {
            keyId = keyId.substring(0, keyId.length() - 1);
        }
        return keyId;
    }

}
