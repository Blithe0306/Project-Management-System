/**
 *Administrator
 */
package com.ft.otp.core.interceptor;

import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.core.interceptor.log.actionlog.WriteConfLog;
import com.ft.otp.core.interceptor.log.actionlog.WriteTokenLog;
import com.ft.otp.manager.admin.role.action.RoleInfoAction;
import com.ft.otp.manager.admin.user.action.AdmUserAction;
import com.ft.otp.manager.authmgr.agent.action.AgentAction;
import com.ft.otp.manager.authmgr.agentconf.action.AgentConfAction;
import com.ft.otp.manager.authmgr.backend.action.BackendAction;
import com.ft.otp.manager.authmgr.server.action.ServerAction;
import com.ft.otp.manager.confinfo.config.action.AuthConfAction;
import com.ft.otp.manager.confinfo.config.action.CenterAction;
import com.ft.otp.manager.confinfo.config.action.CommConfAction;
import com.ft.otp.manager.confinfo.config.action.ConfMonitorConfigAction;
import com.ft.otp.manager.confinfo.config.action.DBConfAction;
import com.ft.otp.manager.confinfo.config.action.MonitorConfAction;
import com.ft.otp.manager.confinfo.config.action.OTPServerAction;
import com.ft.otp.manager.confinfo.config.action.PortalConfAction;
import com.ft.otp.manager.confinfo.config.action.TokenConfAction;
import com.ft.otp.manager.confinfo.config.action.UserConfAction;
import com.ft.otp.manager.confinfo.sms.action.SmsInfoAction;
import com.ft.otp.manager.customer.action.CustomerInfoAction;
import com.ft.otp.manager.data.action.DataBakAction;
import com.ft.otp.manager.lic.action.LicInfoAction;
import com.ft.otp.manager.monitor.action.MonitorAction;
import com.ft.otp.manager.orgunit.domain.action.DomainInfoAction;
import com.ft.otp.manager.orgunit.orgunit.action.OrgunitInfoAction;
import com.ft.otp.manager.prjinfo.action.PrjinfoAction;
import com.ft.otp.manager.project.action.ProjectAction;
import com.ft.otp.manager.resords.action.ResordsAction;
import com.ft.otp.manager.token.distmanager.action.DistManagerAction;
import com.ft.otp.manager.token.tokenimport.action.TokenImportAction;
import com.ft.otp.manager.user.userinfo.action.UserExportAction;
import com.ft.otp.manager.user.userinfo.action.UserImportAction;
import com.ft.otp.manager.user.userinfo.action.UserInfoAction;
import com.ft.otp.manager.user.userinfo.action.UserUnBindAction;
import com.ft.otp.util.tool.StrTool;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/**
 * 对于记录业务action操作记录日志
 * 说明：继承 MethodFilterInterceptor 类是AbstractInterceptor 的子类为了实现过滤不拦截的方法
 *
 * @Date in Feb 21, 2013,2:14:20 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class ActionLogInterceptor extends MethodFilterInterceptor {

    private static final long serialVersionUID = -3146472267008602717L;

    /**
     * 定义action中需要调用的方法
     */
    //设置默认域
    public static String METHOD_MODIFYDAUDOMAIN = "modifyDefaultDomain";
    //检测数据库连接
    private static final String METHOD_DBCONNTEST = "dbConnTest";
    //更新修改
    private static final String METHOD_MODIFY = "modify";
    private static final String METHOD_MODIFY_BASE = "modifyBase";
    //添加
    private static final String METHOD_ADD = "add";
    //解除代理服务器
    private static final String METHOD_UNBINDAS = "unBindAS";
    //绑定代理服务器
    private static final String METHOD_BINDAS = "selServer";
    private static final String METHOD_DELETE = "delete";
    //导入用户、批量解绑模版文件下载
    private static final String METHOD_DOWNLOADINI = "downLoadIni";
    //导入令牌
    private static final String METHOD_IMPORT_TOKEN = "importToken";

    //手机令牌在线分发
    private static final String METHOD_ONLINE_DIST = "onLineDistribute";
    //手机令牌离线分发
    private static final String METHOD_OFFLINE_DIST = "offLineActivate";
    // 生成手机令牌在线二维码
    private static final String METHOD_ONLINE_TWO_DIST = "onLineDist";
    // 生成手机令牌离线二维码
    private static final String METHOD_OFFLINE_TWO_DIST = "twoCodeActive";

    //迁移组织机构
    private static final String METHOD_MOVE_ORG = "moveOrgunit";
    //用户导出
    private static final String METHOD_USER_EXPORT = "exportUser";
    //数据备份
    private static final String METHOD_BAK = "bak";
    //修改配置
    private static final String METHOD_BAKCONFIG = "bakConfig";
    //更新授权
    private static final String METHOD_UPLIC = "upLic";

    //软件令牌分发

    /**
     * 基础拦截器
     */
    protected String doIntercept(ActionInvocation invo) throws Exception {
        Object action = invo.getAction();
        String method = invo.getProxy().getMethod();

        //在Action中的业务方法执行完成之后再进行拦截
        String result = invo.invoke();

        //域设置默认拦截
        if (action instanceof DomainInfoAction) {
            DomainInfoAction domainAction = (DomainInfoAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFYDAUDOMAIN)) {
                getWriteConfLog().writeDomainConf(domainAction, NumConstant.common_number_0);
            }
            if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
                getWriteConfLog().writeDomainConf(domainAction, NumConstant.common_number_1);
            }
        }

        //导入用户模版文件下载
        if (action instanceof UserImportAction) {
            UserImportAction importAction = (UserImportAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_DOWNLOADINI)) {
                getWriteConfLog().writeDownloadLog(importAction, NumConstant.common_number_0);
            }
        }

        //批量解绑模版文件下载
        if (action instanceof UserUnBindAction) {
            UserUnBindAction unBindAction = (UserUnBindAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_DOWNLOADINI)) {
                getWriteConfLog().writeUnBindDownloadLog(unBindAction, NumConstant.common_number_0);
            }
        }

        //认证代理拦截
        if (action instanceof AgentAction) {
            AgentAction agentAction = (AgentAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_UNBINDAS)) {
                getWriteConfLog().writeAgentConf(agentAction, NumConstant.common_number_0);
            }
            if (StrTool.strEqualsIgnoreCase(method, METHOD_DELETE)) {
                getWriteConfLog().writeAgentConf(agentAction, NumConstant.common_number_1);
            }
            if (StrTool.strEqualsIgnoreCase(method, METHOD_BINDAS)) {
                getWriteConfLog().writeAgentConf(agentAction, NumConstant.common_number_2);
            }
            if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
                getWriteConfLog().writeAgentConf(agentAction, NumConstant.common_number_3);
            }
        }

        //其它公共配置拦截
        if (action instanceof CommConfAction) {
            CommConfAction commAction = (CommConfAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
                getWriteConfLog().writeCommConf(commAction, NumConstant.common_number_0);
            }
        }
        //认证配置拦截
        if (action instanceof AuthConfAction) {
            AuthConfAction authAction = (AuthConfAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
                getWriteConfLog().writeAuthConf(authAction, NumConstant.common_number_0);
            }
        }
        //用户配置拦截
        if (action instanceof UserConfAction) {
            UserConfAction uconfAction = (UserConfAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
                getWriteConfLog().writeUserConf(uconfAction, NumConstant.common_number_0);
            }
        }
        //令牌配置拦截
        if (action instanceof TokenConfAction) {
            TokenConfAction tknConfAction = (TokenConfAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
                getWriteConfLog().writeTokenConf(tknConfAction, NumConstant.common_number_0);
            }
        }

        //管理中心配置拦截
        if (action instanceof CenterAction) {
            CenterAction centerAction = (CenterAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
                getWriteConfLog().writeCenterConf(centerAction, NumConstant.common_number_0);
            }
        }

        //OTP Server配置拦截
        if (action instanceof OTPServerAction) {
            OTPServerAction serverAction = (OTPServerAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
                getWriteConfLog().writeServerConf(serverAction, NumConstant.common_number_0);
            }
        }

        //用户门户配置拦截
        if (action instanceof PortalConfAction) {
            PortalConfAction protalAction = (PortalConfAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
                getWriteConfLog().writePortalConf(protalAction, NumConstant.common_number_0);
            }
        }

        //数据库配置拦截
        if (action instanceof DBConfAction) {
            DBConfAction dbConfAction = (DBConfAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_DBCONNTEST)) {
                getWriteConfLog().writeDbConf(dbConfAction, NumConstant.common_number_0);
            }
            if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
                getWriteConfLog().writeDbConf(dbConfAction, NumConstant.common_number_1);
            }
        }

        //数据备份
        if (action instanceof DataBakAction) {
            DataBakAction dataBakAction = (DataBakAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_BAK)) {
                getWriteConfLog().writeBakConf(dataBakAction, NumConstant.common_number_0);
            }
            if (StrTool.strEqualsIgnoreCase(method, METHOD_BAKCONFIG)) {
                getWriteConfLog().writeConf(dataBakAction, NumConstant.common_number_0);
            }
        }

        //监控预警策略配置
        if (action instanceof MonitorAction) {
            MonitorAction monitorAction = (MonitorAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
                getWriteConfLog().writeMonitorConf(monitorAction, NumConstant.common_number_0);
            }
        }

        //监控预警配置
        if (action instanceof MonitorConfAction) {
            MonitorConfAction monitorConfAction = (MonitorConfAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
                getWriteConfLog().writeMonitorConf(monitorConfAction, NumConstant.common_number_0);
            }
        }
        
        //双机热备运行监控配置
        if (action instanceof ConfMonitorConfigAction) {
            ConfMonitorConfigAction monitorConfigAction = (ConfMonitorConfigAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
                getWriteConfLog().writeConfMonitorConfig(monitorConfigAction, NumConstant.common_number_0);
            }
        }

        //令牌导入
        if (action instanceof TokenImportAction) {
            TokenImportAction importAction = (TokenImportAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_IMPORT_TOKEN)) {
                getWriteTokenLog().writeImportToken(importAction, NumConstant.common_number_0);
            }
        }

        //手机令牌分发
        if (action instanceof DistManagerAction) {
            if (StrTool.strEqualsIgnoreCase(method, METHOD_ONLINE_DIST)) {
                getWriteTokenLog().writeMobileDist(action, AdmLogConstant.log_aid_ondist);
            } else if (StrTool.strEqualsIgnoreCase(method, METHOD_OFFLINE_DIST)) {
                getWriteTokenLog().writeMobileDist(action, AdmLogConstant.log_aid_undist);
            } else if (StrTool.strEqualsIgnoreCase(method, METHOD_ONLINE_TWO_DIST)) {
            	getWriteTokenLog().writeMobileDist(action, AdmLogConstant.log_aid_online_dist);
            } else if (StrTool.strEqualsIgnoreCase(method, METHOD_OFFLINE_TWO_DIST)) { 
            	getWriteTokenLog().writeMobileDist(action, AdmLogConstant.log_aid_offline_dist);
            }
        }

        //角色权限修改
        if (action instanceof RoleInfoAction) {
            RoleInfoAction roleInfoAction = (RoleInfoAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
                getWriteConfLog().writeRoleInfoConf(roleInfoAction, NumConstant.common_number_0);
            }
        }

        //迁移组织机构与更新域
        if (action instanceof OrgunitInfoAction) {
            OrgunitInfoAction orgInfoAction = (OrgunitInfoAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_MOVE_ORG)) {
                getWriteConfLog().writeMoveOrgConf(orgInfoAction, NumConstant.common_number_0);
            }
            if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
                getWriteConfLog().writeMoveOrgConf(orgInfoAction, NumConstant.common_number_1);
            }
            if (StrTool.strEqualsIgnoreCase(method, METHOD_DELETE)) {
                getWriteConfLog().writeMoveOrgConf(orgInfoAction, NumConstant.common_number_2);
            }
            if (StrTool.strEqualsIgnoreCase(method, METHOD_ADD)) {
                getWriteConfLog().writeMoveOrgConf(orgInfoAction, NumConstant.common_number_3);
            }
        }
        
        // 更新管理员
        if (action instanceof AdmUserAction) {
        	AdmUserAction admUserAction = (AdmUserAction) action;
        	if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
                getWriteConfLog().writeAdminUser(admUserAction, NumConstant.common_number_0);
            }
        	if(StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY_BASE)){
        		getWriteConfLog().writeAdminUser(admUserAction, NumConstant.common_number_1);
        	}
        }
        
        // 更新用户
        if (action instanceof UserInfoAction) {
        	UserInfoAction userInfoAction = (UserInfoAction) action;
        	if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
                getWriteConfLog().writeUserInfo(userInfoAction, NumConstant.common_number_0);
            }
        }
        
        // 更新认证服务器
        if (action instanceof ServerAction) {
        	ServerAction serverAction = (ServerAction) action;
        	if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
                getWriteConfLog().writeServerInfo(serverAction, NumConstant.common_number_0);
            }
        }
        
        // 更新认证代理配置
        if (action instanceof AgentConfAction) {
        	AgentConfAction agentConfAction = (AgentConfAction) action;
        	if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
                getWriteConfLog().writeAgentConf(agentConfAction, NumConstant.common_number_0);
            }
        }
        
        // 更新后端认证
        if (action instanceof BackendAction) {
        	BackendAction backendAction = (BackendAction) action;
        	if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
                getWriteConfLog().writeBackend(backendAction, NumConstant.common_number_0);
            }
        }

        //导出用户
        if (action instanceof UserExportAction) {
            UserExportAction userExportAction = (UserExportAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_USER_EXPORT)) {
                getWriteConfLog().writeUserExportConf(userExportAction, NumConstant.common_number_0);
            }
        }

        // 更新授权
        if (action instanceof LicInfoAction) {
            LicInfoAction licInfoAction = (LicInfoAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_UPLIC)) {
                getWriteConfLog().writeUpLicConf(licInfoAction, NumConstant.common_number_0);
            }
        }
        // 删除管理员角色
        if (action instanceof RoleInfoAction) {
            RoleInfoAction roleInfoAction = (RoleInfoAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_DELETE)) {
                getWriteConfLog().writeRole(roleInfoAction, NumConstant.common_number_0);
            }
        }
        // 删除短信网关
        if (action instanceof SmsInfoAction) {
            SmsInfoAction smsInfoAction = (SmsInfoAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_DELETE)) {
                getWriteConfLog().writeSms(smsInfoAction, NumConstant.common_number_0);
            }
        }
        
        // 客户管理
        if (action instanceof CustomerInfoAction) {
        	CustomerInfoAction custInfoAction = (CustomerInfoAction) action;
        	if (StrTool.strEqualsIgnoreCase(method, METHOD_DELETE)) {
        		getWriteConfLog().writeCustomer(custInfoAction, NumConstant.common_number_0);
        	} else if (StrTool.strEqualsIgnoreCase(method, METHOD_ADD)) {
        		getWriteConfLog().writeCustomer(custInfoAction, NumConstant.common_number_1);
        	} else if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
        		getWriteConfLog().writeCustomer(custInfoAction, NumConstant.common_number_2);
        	}
        }
        
        // 定制项目
        if (action instanceof ProjectAction) {
        	ProjectAction prjAction = (ProjectAction) action;
        	if (StrTool.strEqualsIgnoreCase(method, METHOD_DELETE)) {
        		getWriteConfLog().writeProject(prjAction, NumConstant.common_number_0);
        	} else if (StrTool.strEqualsIgnoreCase(method, METHOD_ADD)) {
        		getWriteConfLog().writeProject(prjAction, NumConstant.common_number_1);
        	} else if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
        		getWriteConfLog().writeProject(prjAction, NumConstant.common_number_2);
        	}
        }
        
        //定制项目信息
        if (action instanceof PrjinfoAction) {
            PrjinfoAction prjinfoAction = (PrjinfoAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_DELETE)) {
                getWriteConfLog().writePrjinfo(prjinfoAction, NumConstant.common_number_0);
            } else if (StrTool.strEqualsIgnoreCase(method, METHOD_ADD)) {
                getWriteConfLog().writePrjinfo(prjinfoAction, NumConstant.common_number_1);
            } else if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
                getWriteConfLog().writePrjinfo(prjinfoAction, NumConstant.common_number_2);
            }
        }
        
      //定制上门记录
        if (action instanceof ResordsAction) {
            ResordsAction resordsAction = (ResordsAction) action;
            if (StrTool.strEqualsIgnoreCase(method, METHOD_DELETE)) {
                getWriteConfLog().writeResords(resordsAction, NumConstant.common_number_0);
            } else if (StrTool.strEqualsIgnoreCase(method, METHOD_ADD)) {
                getWriteConfLog().writeResords(resordsAction, NumConstant.common_number_1);
            } else if (StrTool.strEqualsIgnoreCase(method, METHOD_MODIFY)) {
                getWriteConfLog().writeResords(resordsAction, NumConstant.common_number_2);
            }
        }
        
        
        return result;
    }

    private WriteConfLog getWriteConfLog() {
        return new WriteConfLog();
    }

    private WriteTokenLog getWriteTokenLog() {
        return new WriteTokenLog();
    }

}
