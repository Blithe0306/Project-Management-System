/**
 *Administrator
 */
package com.ft.otp.manager.monitor.task.app;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

import java.util.List;

import org.apache.log4j.Logger;

import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.authmgr.server.entity.ServerInfo;
import com.ft.otp.manager.authmgr.server.service.IServerServ;
import com.ft.otp.manager.confinfo.access.entity.AccessInfo;
import com.ft.otp.manager.confinfo.access.service.IAccessConServ;
import com.ft.otp.manager.monitor.action.aide.MonitorActionAide;
import com.ft.otp.manager.monitor.entity.EquipmentInfo;
import com.ft.otp.manager.monitor.task.aide.MonitorTaskAide;
import com.ft.otp.manager.task.entity.TaskInfo;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.StrTool;

/**
 * 监控预警 应用系统预警定时器执行任务类
 *
 * @Date in Mar 6, 2013,4:20:08 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class AppMonitorTask extends Task {
    private Logger logger = Logger.getLogger(AppMonitorTask.class);

    /* (non-Javadoc)
     * @see it.sauronsoftware.cron4j.Task#execute(it.sauronsoftware.cron4j.TaskExecutionContext)
     */
    // 预警信息发送和更新帮助类
    private MonitorTaskAide taskAide = new MonitorTaskAide();
    // 监控预警获取其它设备信息请求帮助类
    private MonitorActionAide actionAide = new MonitorActionAide();
    // 访问控制策略服务接口
    private static IAccessConServ accessConServ = (IAccessConServ) AppContextMgr.getObject("accessConServ");
    // 认证服务器服务接口
    private IServerServ serverServ = (IServerServ) AppContextMgr.getObject("serverServ");

    private TaskInfo taskInfo = null;

    /**
     * 初始化方法
     * @param object
     */
    public AppMonitorTask(Object object) {
        if (object instanceof TaskInfo) {
            taskInfo = (TaskInfo) object;
        }
    }

    /**
     * 主执行方法
     */
    public void execute(TaskExecutionContext executor) throws RuntimeException {
        String result = "";
        try {
            // 如果预警为启用状态
            if (StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.MONITOR_APP_CONF,
                    ConfConstant.MONITOR_APP_ENABLED)) == 1) {

                // 获取预警方式
                String sendType = ConfDataFormat.getConfValue(ConfConstant.MONITOR_APP_CONF,
                        ConfConstant.MONITOR_APP_SEND_TYPE);

                // 用户门户运行状态
                String portalMsg = getPortalStateInfo(sendType);
                // 认证服务器运行状态
                String serverMsg = getServerStateInfo(sendType);
                // 组合消息
                StringBuffer sendMsg = new StringBuffer();
                if (StrTool.strNotNull(portalMsg) || StrTool.strNotNull(serverMsg)) {
                    sendMsg.append(portalMsg);
                    sendMsg.append(serverMsg);
                }

                result = taskAide.warnInfoDeal(sendMsg.toString(), sendType, ConfConstant.MONITOR_APP_CONF, 0, Language
                        .getLangValue("monitor_app_title", Language.getCurrLang(null), null));
            } else {
                result = Language.getLangValue("monitor_app_no_enabled", Language.getCurrLang(null), null);
            }

            //System.out.println("应用系统监控预警执行定时任务，执行时间" + DateTool.dateToStr(System.currentTimeMillis() / 1000, true)
            //        + ",执行结果==>" + result);
            executor.setStatusMessage(result);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        executor.pauseIfRequested();
        if (executor.isStopped()) {
            return;
        }
    }

    /**
     * 获取用户门户的应用系统状况
     * 方法说明
     * @Date in Mar 12, 2013,1:30:50 PM
     * @param sendType  根据发送类型不同 预警信息不同
     * @return
     * @throws Exception
     */
    private String getPortalStateInfo(String sendType) throws Exception {
        StringBuffer sbPortal = new StringBuffer();

        // 获取用户门户的访问信任IP
        AccessInfo accessInfoQueryForm = new AccessInfo();
        accessInfoQueryForm.setSystype(NumConstant.common_number_1);
        accessInfoQueryForm.setClientapptype(1);
        List<?> portalList = accessConServ.query(accessInfoQueryForm, new PageArgument());

        if (StrTool.listNotNull(portalList)) {
            AccessInfo[] arrPortals = portalList.toArray(new AccessInfo[portalList.size()]);
            // 异常IP
            String errorIps = "";
            for (AccessInfo accessInfo : arrPortals) {
                EquipmentInfo eInfo = new EquipmentInfo();
                String ipAddr = accessInfo.getStartip();
                eInfo.setIpAddr(ipAddr);
                eInfo.setClientServPath(eInfo.getClientServPath());
                eInfo.setPort(eInfo.getPort());
                eInfo.setServerType(StrConstant.SERVER_TYPE_PORTAL);
                // 用户门户
                try {
                    // 有的用户门户可能会请求异常
                    String portalOnlineCount = actionAide.getRequestInfo(eInfo, NumConstant.common_number_3);
                    // 读取请求的返回值时 有换行符
                    if (!StrTool.strNotNull(portalOnlineCount) || StrTool.strEquals(portalOnlineCount, "-1")) {
                        // 如果获取在线用户数为 null 或者是""
                        errorIps += ipAddr + "，";
                    }
                } catch (Exception e) {
                    errorIps += ipAddr + "，";
                }
            }

            if (StrTool.strNotNull(errorIps)) {
                // ip地址为:
                sbPortal.append(Language.getLangValue("monitor_app_ip", Language.getCurrLang(null), null));
                sbPortal.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
                sbPortal.append(errorIps.substring(0, errorIps.length() - 1));
                sbPortal.append(Language.getLangValue("monitor_app_portal_info", Language.getCurrLang(null), null));
                sbPortal.append("\n");
            }
        }

        return sbPortal.toString();
    }

    /**
     * 获取认证服务器的应用系统状况
     * 方法说明
     * @Date in Mar 14, 2013,10:10:59 AM
     * @param sendType  根据发送类型不同 预警信息不同
     * @return
     * @throws Exception
     */
    private String getServerStateInfo(String sendType) throws Exception {
        StringBuffer sbServer = new StringBuffer();
        // 获取所有认证服务器
        List<?> serverList = serverServ.query(new ServerInfo(), new PageArgument());

        if (StrTool.listNotNull(serverList)) {
            ServerInfo[] arrServers = serverList.toArray(new ServerInfo[serverList.size()]);
            String errorIps = "";

            for (ServerInfo serverInfo : arrServers) {
                EquipmentInfo eInfo = new EquipmentInfo();
                String ipAddr = serverInfo.getHostipaddr();
                eInfo.setIpAddr(serverInfo.getHostipaddr());
                eInfo.setPort(serverInfo.getSoapport());
                eInfo.setClientServPath(serverInfo.getWebservicename());
                eInfo.setServerType(StrConstant.SERVER_TYPE_SERVER);

                //获取服务器运行状态
                try {
                    String runState = actionAide.getRequestInfo(eInfo, NumConstant.common_number_3);
                    if (StrTool.strEquals(runState, "0")) {
                        errorIps += ipAddr + "，";
                    }
                } catch (Exception e) {
                    errorIps += ipAddr + "，";
                }
            }
            if (StrTool.strNotNull(errorIps)) {
                // ip地址为:
                sbServer.append(Language.getLangValue("monitor_app_ip", Language.getCurrLang(null), null));
                sbServer.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
                sbServer.append(errorIps.substring(0, errorIps.length() - 1));
                sbServer.append(Language.getLangValue("monitor_app_server_info", Language.getCurrLang(null), null));
                sbServer.append("\n");
            }
        }

        return sbServer.toString();
    }

    /* (non-Javadoc)
     * @see it.sauronsoftware.cron4j.Task#canBePaused()
     */
    public boolean canBePaused() {
        return super.canBePaused();
    }

    /* (non-Javadoc)
     * @see it.sauronsoftware.cron4j.Task#canBeStopped()
     */
    public boolean canBeStopped() {
        return super.canBeStopped();
    }

    /* (non-Javadoc)
     * @see it.sauronsoftware.cron4j.Task#supportsCompletenessTracking()
     */
    public boolean supportsCompletenessTracking() {
        return super.supportsCompletenessTracking();
    }

    /* (non-Javadoc)
     * @see it.sauronsoftware.cron4j.Task#supportsStatusTracking()
     */
    public boolean supportsStatusTracking() {
        return super.supportsStatusTracking();
    }
}
