/**
 *Administrator
 */
package com.ft.otp.core.interceptor.log.actionlog;

import java.util.ArrayList;
import java.util.List;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.config.DomainConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.actionlog.helper.WriteLogHelper;
import com.ft.otp.manager.admin.role.action.RoleInfoAction;
import com.ft.otp.manager.admin.user.action.AdmUserAction;
import com.ft.otp.manager.authmgr.agent.action.AgentAction;
import com.ft.otp.manager.authmgr.agentconf.action.AgentConfAction;
import com.ft.otp.manager.authmgr.agentserver.entity.AgentServerInfo;
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
import com.ft.otp.manager.confinfo.config.entity.OTPServerConfInfo;
import com.ft.otp.manager.confinfo.sms.action.SmsInfoAction;
import com.ft.otp.manager.customer.action.CustomerInfoAction;
import com.ft.otp.manager.data.action.DataBakAction;
import com.ft.otp.manager.data.entity.DBBakConfInfo;
import com.ft.otp.manager.lic.action.LicInfoAction;
import com.ft.otp.manager.monitor.action.MonitorAction;
import com.ft.otp.manager.orgunit.domain.action.DomainInfoAction;
import com.ft.otp.manager.orgunit.domain.entity.DomainInfo;
import com.ft.otp.manager.orgunit.orgunit.action.OrgunitInfoAction;
import com.ft.otp.manager.orgunit.orgunit.entity.OrgunitInfo;
import com.ft.otp.manager.prjinfo.action.PrjinfoAction;
import com.ft.otp.manager.project.action.ProjectAction;
import com.ft.otp.manager.resords.action.ResordsAction;
import com.ft.otp.manager.user.userinfo.action.UserExportAction;
import com.ft.otp.manager.user.userinfo.action.UserImportAction;
import com.ft.otp.manager.user.userinfo.action.UserInfoAction;
import com.ft.otp.manager.user.userinfo.action.UserUnBindAction;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 拦截业务action日志记录
 *
 * @Date in Feb 21, 2013,2:12:36 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class WriteConfLog {

    //引入日志帮助类
    private WriteLogHelper helper = new WriteLogHelper();

    int acid = 0, acobj = 0;
    int result = 0;
    boolean actionResult;
    String desc = "";
    List<String> descList = null;

    //域管理
    public void writeDomainConf(DomainInfoAction domainAction, int method) throws BaseException {
        String operater = helper.getCurUser(domainAction.getSession());
        //设置默认域
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_setdefault;
            desc = Language.getLangValue("domain_info_name", Language.getCurrLang(null), null)
                    + Language.getLangValue("colon", Language.getCurrLang(null), null)
                    + DomainConfig.getValue(domainAction.getDomainInfo().getDomainId());
        }
        //更新域
        if (method == NumConstant.common_number_1) {
            acid = AdmLogConstant.log_aid_edit;
            desc = helper.writeDomainInfoStr(operater, domainAction);
        }

        acobj = AdmLogConstant.log_obj_domain;
        actionResult = domainAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }

    //认证代理日志
    public void writeAgentConf(AgentAction agentAction, int method) throws BaseException {
        String operater = helper.getCurUser(agentAction.getSession());
        AgentServerInfo agentServerInfo = null;
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_unbind;
            acobj = AdmLogConstant.log_obj_auth_server;
            agentServerInfo = agentAction.getAgentServerInfo();
            if (StrTool.objNotNull(agentServerInfo)) {
                descList = new ArrayList<String>();
                List<?> hostipList = agentServerInfo.getAgentHostList();
                if (StrTool.listNotNull(hostipList)) {
                    for (int i = 0; i < hostipList.size(); i++) {
                        descList.add(Language.getLangValue("log_record_agent_ip", Language.getCurrLang(null), null)
                                + agentServerInfo.getAgentipaddr()
                                + Language.getLangValue("log_record_server_ip", Language.getCurrLang(null), null)
                                + (String) hostipList.get(i));
                    }
                }
            }
            actionResult = agentAction.getActionResult();
            helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
            agentServerInfo = null;
        } else if (method == NumConstant.common_number_2) {
            acid = AdmLogConstant.log_aid_bind;
            acobj = AdmLogConstant.log_obj_auth_server;
            agentServerInfo = agentAction.getAgentServerInfo();
            if (StrTool.objNotNull(agentServerInfo)) {
                descList = new ArrayList<String>();
                List<?> hostipList = agentServerInfo.getAgentHostList();
                if (StrTool.listNotNull(hostipList)) {
                    for (int i = 0; i < hostipList.size(); i++) {
                        descList.add(Language.getLangValue("log_record_agent_ip", Language.getCurrLang(null), null)
                                + agentServerInfo.getAgentipaddr()
                                + Language.getLangValue("log_record_server_ip", Language.getCurrLang(null), null)
                                + (String) hostipList.get(i));
                    }
                }
            }
            actionResult = agentAction.getActionResult();
            helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
            agentServerInfo = null;
        }else if (method == NumConstant.common_number_3) {
        	acid = AdmLogConstant.log_aid_edit;
            desc = helper.writeAgentStr(operater, agentAction);
            acobj = AdmLogConstant.log_obj_auth_agent;
            actionResult = agentAction.getActionResult();
            helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
            agentServerInfo = null;
        }

    }

    //其它公共配置记录日志
    public void writeCommConf(CommConfAction commAction, int method) throws BaseException {
        String operater = helper.getCurUser(commAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_edit;
        }
        desc = helper.writeCommConfStr(operater, commAction);
        acobj = AdmLogConstant.log_obj_conf_comm;
        actionResult = commAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }

    //认证配置记录日志
    public void writeAuthConf(AuthConfAction authAction, int method) throws BaseException {
        String operater = helper.getCurUser(authAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_edit;
        }
        desc = helper.writeAuthConfStr(operater, authAction);
        acobj = AdmLogConstant.log_obj_conf_auth;
        actionResult = authAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }

    //用户配置记录日志
    public void writeUserConf(UserConfAction uconfAction, int method) throws BaseException {
        String operater = helper.getCurUser(uconfAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_edit;
        }
        desc = helper.writeUserConfStr(operater, uconfAction);
        acobj = AdmLogConstant.log_obj_conf_user;
        actionResult = uconfAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }

    //令牌配置记录日志
    public void writeTokenConf(TokenConfAction tknConfAction, int method) throws BaseException {
        String operater = helper.getCurUser(tknConfAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_edit;
        }
        desc = helper.writeTokenConfStr(operater, tknConfAction);
        acobj = AdmLogConstant.log_obj_conf_token;
        actionResult = tknConfAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }

    //OTP Server配置日志
    public void writeServerConf(OTPServerAction serverAction, int method) throws BaseException {
        String operater = helper.getCurUser(serverAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_edit;
        }

        acobj = AdmLogConstant.log_obj_conf_serv;
        actionResult = serverAction.getActionResult();
        OTPServerConfInfo serverConfInfo = serverAction.getServerInfo();
        OTPServerConfInfo oldServerConfInfo = serverAction.getOldServerConfInfo();
        if (null == serverConfInfo || null == oldServerConfInfo) {
            return;
        }

        int i = 0;
        StringBuilder sbr = new StringBuilder();
        sbr.append(operater).append(
                Language.getLangValue("log_otp_server_record_edit", Language.getCurrLang(null), null)).append(
                Language.getLangValue("log_edit_content", Language.getCurrLang(null), null)).append("</br>");
        if (!StrTool.strEquals(oldServerConfInfo.getCacheclearpolicy(), serverConfInfo.getCacheclearpolicy())) {
            if (serverConfInfo.getCacheclearpolicy().equals("0")) {
                sbr.append(
                        Language.getLangValue("otpserconf_cache_clean_policy", Language.getCurrLang(null), null)
                                + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                        Language.getLangValue("log_record_modify", Language.getCurrLang(null), null)).append("FIFO")
                        .append("</br>");
            } else if (serverConfInfo.getCacheclearpolicy().equals("1")) {
                sbr.append(
                        Language.getLangValue("otpserconf_cache_clean_policy", Language.getCurrLang(null), null)
                                + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                        Language.getLangValue("log_record_modify", Language.getCurrLang(null), null)).append("LRU")
                        .append("</br>");
            } else {
                sbr.append(
                        Language.getLangValue("otpserconf_cache_clean_policy", Language.getCurrLang(null), null)
                                + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                        Language.getLangValue("log_record_modify", Language.getCurrLang(null), null)).append("LFU")
                        .append("</br>");
            }
            i++;
        }
        if (!StrTool.strEquals(oldServerConfInfo.getCachetunersleeptime(), serverConfInfo.getCachetunersleeptime())) {
            sbr.append(
                    Language.getLangValue("otpserconf_cache_tuner_sleeptime", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                    oldServerConfInfo.getCachetunersleeptime()).append(
                    Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                    serverConfInfo.getCachetunersleeptime()).append("</br>");
            i++;
        }

        if (!StrTool.strEquals(oldServerConfInfo.getCachemaxsize(), serverConfInfo.getCachemaxsize())) {
            sbr.append(
                    Language.getLangValue("otpserconf_cache_maxsize", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                    oldServerConfInfo.getCachemaxsize()).append(
                    Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                    serverConfInfo.getCachemaxsize()).append("</br>");
            i++;
        }
        if (!StrTool.strEquals(oldServerConfInfo.getCacheexpiretime(), serverConfInfo.getCacheexpiretime())) {
            sbr.append(
                    Language.getLangValue("otpserconf_cache_expire_time", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null)).append(
                    oldServerConfInfo.getCacheexpiretime()).append(
                    Language.getLangValue("log_modified_to", Language.getCurrLang(null), null)).append(
                    serverConfInfo.getCacheexpiretime()).append("</br>");
            i++;
        }
        if (i == 0) {
            return;
        }
        serverAction.setOldServerConfInfo(null);
        serverAction.setServerInfo(null);
        helper.getLogCommonObj().addAdminLog(acid, acobj, sbr.toString(), descList, actionResult ? 0 : 1);
    }

    //记录管理中心配置日志
    public void writeCenterConf(CenterAction centerAction, int method) throws BaseException {
        String operater = helper.getCurUser(centerAction.getSession());
        String oper = centerAction.getOldcenterInfo().getOper();
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_edit;
        }
        if (!StrTool.strEquals(oper, "trustip")) {
            acobj = AdmLogConstant.log_obj_conf_center;
        } else {
            acobj = AdmLogConstant.log_obj_trustip_check;
        }
        actionResult = centerAction.getActionResult();
        desc = helper.writeCenterConfStr(operater, centerAction);
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }

    //记录用户门户配置日志
    public void writePortalConf(PortalConfAction portalAction, int method) throws BaseException {
        String operater = helper.getCurUser(portalAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_edit;
        }
        desc = helper.writePortalConfStr(operater, portalAction);
        acobj = AdmLogConstant.log_obj_conf_portal;
        actionResult = portalAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }

    //记录数据库配置日志
    public void writeDbConf(DBConfAction dbConfAction, int method) throws BaseException {
        String operater = helper.getCurUser(dbConfAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_testconn;
            desc = Language.getLangValue("dbconf_ip", Language.getCurrLang(null), null)
                    + dbConfAction.getDbConfInfo().getIp();
        }
        if (method == NumConstant.common_number_1) {
            acid = AdmLogConstant.log_aid_edit;
            desc = helper.writeDbConfStr(operater, dbConfAction);
        }

        acobj = AdmLogConstant.log_obj_conf_db;
        actionResult = dbConfAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }

    //记录数据库备份日志
    public void writeBakConf(DataBakAction dataBakAction, int method) throws BaseException {

        DBBakConfInfo bakParam = dataBakAction.getOldDbBakConfInfo();
        if (bakParam.getLogFlag() == NumConstant.common_number_4) {
            String operater = helper.getCurUser(dataBakAction.getSession());
            String zipFileName = "otpdbV4_" + DateTool.getCurDate("yyyyMMddHHmmss") + Constant.FILE_ZIP;
            String remoteName = "";
            if (bakParam.getIsremote() == NumConstant.common_number_0) {
                remoteName = Language.getLangValue("databak_local", Language.getCurrLang(null), null);
            } else {
                remoteName = Language.getLangValue("databak_vd_protocol", Language.getCurrLang(null), null);
            }
            acid = AdmLogConstant.log_aid_bak;
            desc = Language.getLangValue("databak_vd_reomote_bak", Language.getCurrLang(null), null)
                    + Language.getLangValue("colon", Language.getCurrLang(null), null) + remoteName
                    + Language.getLangValue("comma", Language.getCurrLang(null), null)
                    + Language.getLangValue("databak_vd_name", Language.getCurrLang(null), null)
                    + Language.getLangValue("colon", Language.getCurrLang(null), null) + zipFileName;
            acobj = AdmLogConstant.log_obj_data;
            actionResult = dataBakAction.getActionResult();
            helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
        }
    }

    //修改备份信息
    public void writeConf(DataBakAction dataBakAction, int method) throws BaseException {
        DBBakConfInfo bakParam = dataBakAction.getOldDbBakConfInfo();
        String operater = helper.getCurUser(dataBakAction.getSession());
        acid = AdmLogConstant.log_aid_edit;
        String remoteName = "";
        if (bakParam.getIsremote() == NumConstant.common_number_0) {
            remoteName = Language.getLangValue("databak_local", Language.getCurrLang(null), null);
        } else {
            remoteName = Language.getLangValue("databak_vd_protocol", Language.getCurrLang(null), null);
        }
        desc = Language.getLangValue("databak_vd_reomote_bak", Language.getCurrLang(null), null)
                + Language.getLangValue("colon", Language.getCurrLang(null), null) + remoteName;
        acobj = AdmLogConstant.log_obj_bakdata;
        actionResult = dataBakAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }

    //监控预警策略配置日志
    public void writeMonitorConf(MonitorAction monitorAction, int method) throws BaseException {
        String operater = helper.getCurUser(monitorAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_edit;
        }

        acobj = AdmLogConstant.log_obj_monitor_conf;
        actionResult = monitorAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }

    //监控预警配置日志
    public void writeMonitorConf(MonitorConfAction monitorConfAction, int method) throws BaseException {
        String operater = helper.getCurUser(monitorConfAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_edit;
        }

        acobj = AdmLogConstant.log_obj_monitor_conf;
        actionResult = monitorConfAction.getActionResult();
        desc = helper.writeMonitorConfStr(operater, monitorConfAction);
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }
    
    //双机热备运行监控配置日志
    public void writeConfMonitorConfig(ConfMonitorConfigAction monitorConfigAction, int method) throws BaseException {
        String operater = helper.getCurUser(monitorConfigAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_edit;
        }

        acobj = AdmLogConstant.log_obj_heart_beat_ware;
        actionResult = monitorConfigAction.getActionResult();
        desc = helper.writeMonitorConfigStr(operater, monitorConfigAction);
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }

    //模版文件下载日志
    public void writeDownloadLog(UserImportAction importAction, int method) throws BaseException {
        String operater = helper.getCurUser(importAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_download;
        }

        acobj = AdmLogConstant.log_obj_temp_file;
        actionResult = importAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }

    //模版文件下载日志,批量解绑
    public void writeUnBindDownloadLog(UserUnBindAction unBindAction, int method) throws BaseException {
        String operater = helper.getCurUser(unBindAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_download;
        }

        acobj = AdmLogConstant.log_obj_unbind_file;
        actionResult = unBindAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }

    //权限编辑操作
    public void writeRoleInfoConf(RoleInfoAction roleInfoAction, int method) throws BaseException {
        String operater = helper.getCurUser(roleInfoAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_edit;
        }
        acobj = AdmLogConstant.log_obj_adminrole;
        actionResult = roleInfoAction.getActionResult();
        desc = helper.writeRoleInfoStr(operater, roleInfoAction);
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }

    //组织机构（迁移、编辑、删除、添加）
    public void writeMoveOrgConf(OrgunitInfoAction orgInfoAction, int method) throws BaseException {
        String operater = helper.getCurUser(orgInfoAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_move;
            desc = helper.writeMoveOrgInfoStr(operater, orgInfoAction);
            acobj = AdmLogConstant.log_obj_orgunit;
        }
        if (method == NumConstant.common_number_1) {
            acid = AdmLogConstant.log_aid_edit;
            desc = helper.writeUpdateOrgInfoStr(operater, orgInfoAction);
            OrgunitInfo orgInfo = orgInfoAction.getOldOrgInfo();
            DomainInfo domainInfo = orgInfoAction.getOldDomainInfo();
            if (StrTool.objNotNull(orgInfo)) {
                acobj = AdmLogConstant.log_obj_orgunit;
            }
            if (StrTool.objNotNull(domainInfo)) {
                acobj = AdmLogConstant.log_obj_domain;
            }
        }
        if (method == NumConstant.common_number_2) {
            acid = AdmLogConstant.log_aid_del;
            desc = helper.writedeleteOrgInfoStr(operater, orgInfoAction);
            acobj = AdmLogConstant.log_obj_orgunit;
        }
        if (method == NumConstant.common_number_3) {
            acid = AdmLogConstant.log_aid_add;
            desc = helper.writeaddOrgInfoStr(operater, orgInfoAction);
            OrgunitInfo orgInfo = orgInfoAction.getOldOrgInfo();
            if (StrTool.objNotNull(orgInfo)) {
                acobj = AdmLogConstant.log_obj_orgunit;
            }
        }
        actionResult = orgInfoAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }
    
    // 管理员编辑
    public void writeAdminUser(AdmUserAction admUserAction, int method) throws BaseException {
        String operater = helper.getCurUser(admUserAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_edit;
            desc = helper.writeAdmUserStr(operater, admUserAction);
            acobj = AdmLogConstant.log_obj_admin;
        }else if(method == NumConstant.common_number_1){
        	acid = AdmLogConstant.log_aid_edit;
        	desc = helper.writeAdmUserBaseStr(operater, admUserAction);
        	acobj = AdmLogConstant.log_obj_admin;
        }
        actionResult = admUserAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }
    
    // 用户编辑
    public void writeUserInfo(UserInfoAction userInfoAction, int method) throws BaseException {
        String operater = helper.getCurUser(userInfoAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_edit;
            desc = helper.writeUserStr(operater, userInfoAction);
            acobj = AdmLogConstant.log_obj_user;
        }
        actionResult = userInfoAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }
    
    // 认证服务器编辑
    public void writeServerInfo(ServerAction serverAction, int method) throws BaseException {
        String operater = helper.getCurUser(serverAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_edit;
            desc = helper.writeServerStr(operater, serverAction);
            acobj = AdmLogConstant.log_obj_auth_server;
        }
        actionResult = serverAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }
    
    // 认证代理配置
    public void writeAgentConf(AgentConfAction agentConfAction, int method) throws BaseException {
        String operater = helper.getCurUser(agentConfAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_edit;
            desc = helper.writeAgentConfStr(operater, agentConfAction);
            acobj = AdmLogConstant.log_obj_auth_conf;
        }
        actionResult = agentConfAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }
    
    // 后端认证
    public void writeBackend(BackendAction backendAction, int method) throws BaseException {
        String operater = helper.getCurUser(backendAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_edit;
            desc = helper.writeBackendStr(operater, backendAction);
            acobj = AdmLogConstant.log_obj_auth_backend;
        }
        actionResult = backendAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }

    //导出用户
    public void writeUserExportConf(UserExportAction userExportAction, int method) throws BaseException {
        String operater = helper.getCurUser(userExportAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_export;
            desc = helper.writeUserExportStr(operater, userExportAction);
            acobj = AdmLogConstant.log_obj_user;
        }
        actionResult = userExportAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }

    //更新授权
    public void writeUpLicConf(LicInfoAction licInfoAction, int method) throws BaseException {
        String operater = helper.getCurUser(licInfoAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_edit;
            desc = helper.writeUpLicStr(operater, licInfoAction);
            acobj = AdmLogConstant.log_obj_conf_lic;
        }
        actionResult = licInfoAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }

    //删除角色
    public void writeRole(RoleInfoAction roleInfoAction, int method) throws BaseException {
        String operater = helper.getCurUser(roleInfoAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_del;
            desc = helper.writeRole(operater, roleInfoAction);
            acobj = AdmLogConstant.log_obj_adminrole;
        }
        actionResult = roleInfoAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }

    //删除短信网关
    public void writeSms(SmsInfoAction smsInfoAction, int method) throws BaseException {
        String operater = helper.getCurUser(smsInfoAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_del;
            desc = helper.writeSms(operater, smsInfoAction);
            acobj = AdmLogConstant.log_obj_conf_sms;
        }
        actionResult = smsInfoAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }
    
    //客户
    public void writeCustomer(CustomerInfoAction custInfoAction, int method) throws BaseException {
    	String operater = helper.getCurUser(custInfoAction.getSession());
    	if (method == NumConstant.common_number_0) {
    		acid = AdmLogConstant.log_aid_del;
    		desc = helper.writeDelCustomer(operater, custInfoAction);
    		acobj = AdmLogConstant.log_obj_customer;
    	} 
    	else if (method == NumConstant.common_number_1) {
    		acid = AdmLogConstant.log_aid_add;
    		desc = helper.writeAddCustomer(operater, custInfoAction);
    		acobj = AdmLogConstant.log_obj_customer;
    	}
    	else if (method == NumConstant.common_number_2) {
    		acid = AdmLogConstant.log_aid_edit;
    		desc = helper.writeAddCustomer(operater, custInfoAction);
    		acobj = AdmLogConstant.log_obj_customer;
    	}
    	actionResult = custInfoAction.getActionResult();
    	helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }
    
    //定制项目
    public void writeProject(ProjectAction prjAction, int method) throws BaseException {
    	String operater = helper.getCurUser(prjAction.getSession());
    	if (method == NumConstant.common_number_0) {
    		acid = AdmLogConstant.log_aid_del;
    		desc = helper.writeDelPrj(operater, prjAction);
    		acobj = AdmLogConstant.log_obj_project;
    	} 
    	else if (method == NumConstant.common_number_1) {
    		acid = AdmLogConstant.log_aid_add;
    		desc = helper.writeAddPrj(operater, prjAction);
    		acobj = AdmLogConstant.log_obj_project;
    	}
    	else if (method == NumConstant.common_number_2) {
    		acid = AdmLogConstant.log_aid_edit;
    		desc = helper.writeEditPrj(operater, prjAction);
    		acobj = AdmLogConstant.log_obj_project;
    	}
    	actionResult = prjAction.getActionResult();
    	helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }
    
    //定制项目信息
    public void writePrjinfo(PrjinfoAction prjinfoAction, int method) throws BaseException {
        String operater = helper.getCurUser(prjinfoAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_del;
            desc = helper.writeDelPrjinfo(operater, prjinfoAction);
            acobj = AdmLogConstant.log_obj_prjinfo;
        } 
        else if (method == NumConstant.common_number_1) {
            acid = AdmLogConstant.log_aid_add;
            desc = helper.writeAddPrjinfo(operater, prjinfoAction);
            acobj = AdmLogConstant.log_obj_prjinfo;
        }
        else if (method == NumConstant.common_number_2) {
            acid = AdmLogConstant.log_aid_edit;
            desc = helper.writeEditPrjinfo(operater, prjinfoAction);
            acobj = AdmLogConstant.log_obj_prjinfo;
        }
        actionResult = prjinfoAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }

    //上门记录
    public void writeResords(ResordsAction resordsAction, int method) throws BaseException {
        String operater = helper.getCurUser(resordsAction.getSession());
        if (method == NumConstant.common_number_0) {
            acid = AdmLogConstant.log_aid_del;
            desc = helper.writeDelResords(operater, resordsAction);
            acobj = AdmLogConstant.log_obj_resords;
        } 
        else if (method == NumConstant.common_number_1) {
            acid = AdmLogConstant.log_aid_add;
            desc = helper.writeAddResords(operater, resordsAction);
            acobj = AdmLogConstant.log_obj_resords;
        }
        else if (method == NumConstant.common_number_2) {
            acid = AdmLogConstant.log_aid_edit;
            desc = helper.writeEditResords(operater, resordsAction);
            acobj = AdmLogConstant.log_obj_resords;
        }
        actionResult = resordsAction.getActionResult();
        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }
}
