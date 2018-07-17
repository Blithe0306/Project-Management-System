/**
 *Administrator
 */
package com.ft.otp.core.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.core.interceptor.log.servicelog.admin.role.RoleInfoLog;
import com.ft.otp.core.interceptor.log.servicelog.admin.user.AdminUserLog;
import com.ft.otp.core.interceptor.log.servicelog.authmgr.agent.AgentInfoLog;
import com.ft.otp.core.interceptor.log.servicelog.authmgr.agentconf.AgentConfLog;
import com.ft.otp.core.interceptor.log.servicelog.authmgr.backend.BackendLog;
import com.ft.otp.core.interceptor.log.servicelog.authmgr.server.ServerInfoLog;
import com.ft.otp.core.interceptor.log.servicelog.confinfo.access.AccessLog;
import com.ft.otp.core.interceptor.log.servicelog.confinfo.email.EmailLog;
import com.ft.otp.core.interceptor.log.servicelog.confinfo.notice.NoticeLog;
import com.ft.otp.core.interceptor.log.servicelog.confinfo.radius.RadAttrLog;
import com.ft.otp.core.interceptor.log.servicelog.confinfo.radius.RadProfileLog;
import com.ft.otp.core.interceptor.log.servicelog.confinfo.sms.SmsLog;
import com.ft.otp.core.interceptor.log.servicelog.confinfo.usersource.TimeUpdateUSLog;
import com.ft.otp.core.interceptor.log.servicelog.confinfo.usersource.UsersourceLog;
import com.ft.otp.core.interceptor.log.servicelog.domain.DomainLog;
import com.ft.otp.core.interceptor.log.servicelog.orgunit.OrgunitLog;
import com.ft.otp.core.interceptor.log.servicelog.token.TokenLog;
import com.ft.otp.core.interceptor.log.servicelog.token.distmanager.DistmanagerLog;
import com.ft.otp.core.interceptor.log.servicelog.user.userinfo.UserInfoLog;
import com.ft.otp.core.interceptor.log.servicelog.usertoken.UserTokenLog;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理员操作日志集中记录类 service method拦截器
 * 
 * @Date in Jun 8, 2011,1:48:08 PM
 * 
 * @author TBM
 */
public class ServiceLogInterceptor implements MethodInterceptor {

    private static final long serialVersionUID = 1582937035367083458L;
    private Logger logger = Logger.getLogger(ServiceLogInterceptor.class);

    DomainLog domainLog = null;
    UserInfoLog userInfoLog = null;
    OrgunitLog orgunitLog = null;

    TokenLog tokenLog = null;
    UserTokenLog userTokenLog = null;
    ServerInfoLog serverInfoLog = null;
    AgentInfoLog agentInfoLog = null;

    DistmanagerLog distmanagerLog = null;
    RoleInfoLog roleInfoLog = null;
    AdminUserLog adminUserLog = null;

    AgentConfLog agentConfLog = null;
    BackendLog backendLog = null;
    EmailLog emailLog = null;
    SmsLog smsLog = null;
    AccessLog accessLog = null;
    RadProfileLog radProLog = null;
    RadAttrLog radAttrLog = null;
    NoticeLog noticeLog = null;
    UsersourceLog usourecLog = null;
    TimeUpdateUSLog timingLog = null;

    public ServiceLogInterceptor() {
        domainLog = new DomainLog();
        userInfoLog = new UserInfoLog();
        userTokenLog = new UserTokenLog();
        orgunitLog = new OrgunitLog();

        tokenLog = new TokenLog();
        distmanagerLog = new DistmanagerLog();
        serverInfoLog = new ServerInfoLog();
        agentInfoLog = new AgentInfoLog();

        roleInfoLog = new RoleInfoLog();
        adminUserLog = new AdminUserLog();

        agentConfLog = new AgentConfLog();
        backendLog = new BackendLog();
        emailLog = new EmailLog();
        smsLog = new SmsLog();
        accessLog = new AccessLog();
        radProLog = new RadProfileLog();
        radAttrLog = new RadAttrLog();
        noticeLog = new NoticeLog();
        usourecLog = new UsersourceLog();
        timingLog = new TimeUpdateUSLog();
    }

    /**
     * 管理员行为拦截，日志记录
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 不进行拦截处理的方法
        String method = invocation.getMethod().getName();
        if (StrTool.strEquals(method, StrConstant.METHOD_QUERY) || StrTool.strEquals(method, StrConstant.METHOD_FIND)
                || StrTool.strEquals(method, StrConstant.METHOD_COUNT)) {
            return invocation.proceed();
        }

        boolean result = logWrite(invocation);
        if (result) {
            if (method.equals(StrConstant.METHOD_IMPORT_TOKEN)) {
                return 0;
            }
            return null;
        } else {
            return invocation.proceed();
        }
    }

    /**
     * 日志入库
     * 
     * @Date in Jun 10, 2011,11:59:04 AM
     * @param joinpoint
     * @param result
     */
    private boolean logWrite(MethodInvocation invocation) {
        boolean isOper = false;
        try {
            // 进行拦截的类
            Object[] classes = invocation.getThis().getClass().getInterfaces();
            String classObj = classes[0].toString();
            classObj = classObj.substring(classObj.lastIndexOf(".") + 1, classObj.length());
            // 进行拦截的方法
            String method = invocation.getMethod().getName();

            // 用户模块日志
            if (StrTool.strEquals(classObj, AdmLogConstant.if_iuserinfoserv)) {
                isOper = userInfoLog.addUserInfoLog(invocation, method);
            }

            // 令牌管理模块日志
            else if (StrTool.strEquals(classObj, AdmLogConstant.if_itokenserv)) {
                isOper = tokenLog.addTokenLog(invocation, method);
            }
            // 用户-令牌模块日志
            else if (StrTool.strEquals(classObj, AdmLogConstant.if_iusertokenserv)) {
                isOper = userTokenLog.addUserTokenLog(invocation, method);
            }
            // 令牌分发模块日志
            else if (StrTool.strEquals(classObj, AdmLogConstant.if_idistmanagerserv)) {
                isOper = distmanagerLog.addDistManagerLog(invocation, method);
            }
            // 管理员角色接口
            else if (StrTool.strEquals(classObj, AdmLogConstant.if_iroleinfoserv)) {
                isOper = roleInfoLog.addRoleInfoLog(invocation, method);
            }
            // 管理员用户接口
            else if (StrTool.strEquals(classObj, AdmLogConstant.if_iadminuserserv)) {
                isOper = adminUserLog.addAdminUserLog(invocation, method);
            }
            // 域管理接口
            else if (StrTool.strEquals(classObj, AdmLogConstant.if_idomaininfoserv)) {
                isOper = domainLog.addDomainLog(invocation, method);
            }
            // 组织机构接口
            else if (StrTool.strEquals(classObj, AdmLogConstant.if_iorgunitinfoserv)) {
                isOper = orgunitLog.addOrgunitLog(invocation, method);
            }

            /**
             * 认证代理模块日志记录
             */
            // 认证服务器模块日志
            else if (StrTool.strEquals(classObj, AdmLogConstant.if_iserverserv)) {
                isOper = serverInfoLog.addServerInfoLog(invocation, method);
            }
            // 认证代理模块日志
            else if (StrTool.strEquals(classObj, AdmLogConstant.if_iagentserv)) {
                isOper = agentInfoLog.addAgentInfoLog(invocation, method);
            }

            // 认证代理配置日志
            else if (StrTool.strEquals(classObj, AdmLogConstant.if_iagentconfserv)) {
                isOper = agentConfLog.addAgentConfLog(invocation, method);
            }
            // 后端认证模块日志
            else if (StrTool.strEquals(classObj, AdmLogConstant.if_ibackendserv)) {
                isOper = backendLog.addBackendLog(invocation, method);
            }
            //邮件服务器配置日志
            else if (StrTool.strEquals(classObj, AdmLogConstant.if_iemailconfserv)) {
                isOper = emailLog.addEmailLog(invocation, method);
            }
            //短信网关配置日志
            else if (StrTool.strEquals(classObj, AdmLogConstant.if_ismsconfserv)) {
                isOper = smsLog.addSmsLog(invocation, method);
            }
            //访问控制策略日志
            else if (StrTool.strEquals(classObj, AdmLogConstant.if_iaccessconserv)) {
                isOper = accessLog.addAccessLog(invocation, method);
            }
            //Radius配置日志
            else if (StrTool.strEquals(classObj, AdmLogConstant.if_iradprofileserv)) {
                isOper = radProLog.addRadProLog(invocation, method);
            }
            //Radius属性配置日志
            else if (StrTool.strEquals(classObj, AdmLogConstant.if_iradattrserv)) {
                isOper = radAttrLog.addRadAttrLog(invocation, method);
            }
            //通知公告日志
            else if (StrTool.strEquals(classObj, AdmLogConstant.if_inoticeserserv)) {
                isOper = noticeLog.addNoticeLog(invocation, method);
            }
            //用户来源日志
            else if (StrTool.strEquals(classObj, AdmLogConstant.if_iusersourceserv)) {
                isOper = usourecLog.addSourceLog(invocation, method);
            }
            //用户来源定时
            else if (StrTool.strEquals(classObj, AdmLogConstant.if_itaskinfoserv)) {
                // 只有用户来源操作才会记录此日志，如果是 定时删除日志和定时备份则不记录直接返回true，里面会判断
                isOper = timingLog.addTimingLog(invocation, method);
            }
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }

        return isOper;
    }

}
