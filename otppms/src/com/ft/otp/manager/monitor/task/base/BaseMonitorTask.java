/**
 *Administrator
 */
package com.ft.otp.manager.monitor.task.base;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

import org.apache.log4j.Logger;

import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.monitor.task.aide.MonitorTaskAide;
import com.ft.otp.manager.report.form.ReportQueryForm;
import com.ft.otp.manager.report.service.tokenreport.ITokenReportServ;
import com.ft.otp.manager.task.entity.TaskInfo;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.manager.user.userinfo.service.IUserInfoServ;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 监控预警 基本预警定时器执行任务类 
 * 此定时器只监控基本预警中的 1小时内令牌同步比例
 *
 * @Date in Mar 6, 2013,4:20:17 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class BaseMonitorTask extends Task {

    private Logger logger = Logger.getLogger(BaseMonitorTask.class);

    /* (non-Javadoc)
     * @see it.sauronsoftware.cron4j.Task#execute(it.sauronsoftware.cron4j.TaskExecutionContext)
     */
    // 令牌报表接口
    private ITokenReportServ tokenReportServ = (ITokenReportServ) AppContextMgr.getObject("tokenReportServ");
    // 用户服务接口
    private IUserInfoServ userInfoServ = (IUserInfoServ) AppContextMgr.getObject("userInfoServ");
    private TaskInfo taskInfo = null;
    // 预警信息发送和更新帮助类
    private MonitorTaskAide taskAide = new MonitorTaskAide();

    /**
     * 初始化方法
     * @param object
     */
    public BaseMonitorTask(Object object) {
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
            String confType = ConfConstant.MONITOR_BASE_CONF;
            // 如果预警为启用状态
            if (StrTool.parseInt(ConfDataFormat.getConfValue(confType, ConfConstant.MONITOR_BASE_ENABLED)) == 1) {
                // 获取预警方式
                String sendType = ConfDataFormat.getConfValue(confType, ConfConstant.MONITOR_BASE_SENDTYPE);

                // 管理中心 前1小时内令牌同步达到多少比例时预警信息
                String syncupperMsg = getSyncUpperWarnInfo(sendType);

                result += taskAide.warnInfoDeal(syncupperMsg, sendType, confType, taskAide.getConfid(confType,
                        ConfConstant.MONITOR_BASE_SYNCUPPER), Language.getLangValue("monitor_base_hour_sync_title",
                        Language.getCurrLang(null), null));

            } else {
                result = Language.getLangValue("monitor_base_no_enabled", Language.getCurrLang(null), null);
            }

            //System.out.println("系统基本信息监控预警执行定时任务，执行时间" + DateTool.dateToStr(System.currentTimeMillis() / 1000, true)
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
     * 获取同步相关预警信息
     * 方法说明
     * @Date in Mar 13, 2013,4:08:53 PM
     * @param sendType
     * @return
     * @throws Exception
     */
    private String getSyncUpperWarnInfo(String sendType) throws Exception {
        StringBuilder syncupperMsg = new StringBuilder();

        // 获取上一个小时同步的用户数
        ReportQueryForm reportQueryForm = new ReportQueryForm();
        reportQueryForm.setBeginDateLong(DateTool.currentUTC() - 60 * 60);
        reportQueryForm.setEndDateLong(DateTool.currentUTC());
        int synTknCount = tokenReportServ.getReportSynTknCountByTime(reportQueryForm);

        // 总用户数
        int userCount = userInfoServ.countBind(new UserInfo());
        if (userCount == 0) {
            return null;
        }

        // 配置的用户同步比例
        int confPercent = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.MONITOR_BASE_CONF,
                ConfConstant.MONITOR_BASE_SYNCUPPER));

        if (synTknCount * 100 / userCount > confPercent) {
            syncupperMsg.append(Language.getLangValue("monitor_base_sys_at", Language.getCurrLang(null), null));
            syncupperMsg.append(DateTool.dateToStr(reportQueryForm.getBeginDateLong(), true));
            syncupperMsg.append(Language.getLangValue("monitor_base_to", Language.getCurrLang(null), null));
            syncupperMsg.append(DateTool.dateToStr(reportQueryForm.getEndDateLong(), true));

            syncupperMsg.append(Language.getLangValue("monitor_base_time_sync_percent", Language.getCurrLang(null),
                    null));
            syncupperMsg.append(synTknCount * 100 / userCount);
            syncupperMsg.append("%，");
            syncupperMsg.append(Language.getLangValue("monitor_base_place_view", Language.getCurrLang(null), null));
        }

        return syncupperMsg.toString();
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
