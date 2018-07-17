/**
 *Administrator
 */
package com.ft.otp.manager.monitor.task.base;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

import java.util.List;

import org.apache.log4j.Logger;

import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.monitor.task.aide.MonitorTaskAide;
import com.ft.otp.manager.report.entity.ReportInfo;
import com.ft.otp.manager.report.form.ReportQueryForm;
import com.ft.otp.manager.report.service.tokenreport.ITokenReportServ;
import com.ft.otp.manager.task.entity.TaskInfo;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 监控预警 基本预警定时器执行任务类 
 * 此定时器只监控基本预警中的 令牌将要过期天数 和未绑定令牌比例
 *
 * @Date in Mar 6, 2013,4:20:17 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class BaseMonitorCommonTask extends Task {

    private Logger logger = Logger.getLogger(BaseMonitorCommonTask.class);

    /* (non-Javadoc)
     * @see it.sauronsoftware.cron4j.Task#execute(it.sauronsoftware.cron4j.TaskExecutionContext)
     */
    // 令牌报表接口
    private ITokenReportServ tokenReportServ = (ITokenReportServ) AppContextMgr.getObject("tokenReportServ");
    private TaskInfo taskInfo = null;
    // 预警信息发送和更新帮助类
    private MonitorTaskAide taskAide = new MonitorTaskAide();

    /**
     * 初始化方法
     * @param object
     */
    public BaseMonitorCommonTask(Object object) {
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

                // 管理中心 令牌按配置时间过期预警信息
                String remainDaysMsg = getRemainDaysWarnInfo(sendType);
                result += taskAide.warnInfoDeal(remainDaysMsg, sendType, confType, taskAide.getConfid(confType,
                        ConfConstant.MONITOR_BASE_REMAINDAYS), Language.getLangValue("monitor_base_expire_title",
                        Language.getCurrLang(null), null));

                // 管理中心 未绑定令牌比例预警信息
                String unbindLowerMsg = getUnbindLowerWarnInfo(sendType);

                result += taskAide.warnInfoDeal(unbindLowerMsg, sendType, confType, taskAide.getConfid(confType,
                        ConfConstant.MONITOR_BASE_UNBINDLOWER), Language.getLangValue("monitor_base_unbind_title",
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
     * 获取一段时间之后将要过期的令牌预警信息
     * 方法说明
     * @Date in Mar 13, 2013,3:56:45 PM
     * @param sendType 发送方式
     * @return
     * @throws Exception
     */
    private String getRemainDaysWarnInfo(String sendType) throws Exception {
        StringBuilder remainDaysMsg = new StringBuilder();

        int remainDays = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.MONITOR_BASE_CONF,
                ConfConstant.MONITOR_BASE_REMAINDAYS));
        ReportQueryForm reportQueryForm = new ReportQueryForm();
        reportQueryForm.setMonthExpireTime(DateTool.currentUTC() + remainDays * 86400);
        List<?> tknList = tokenReportServ.getReportTknByExpired(reportQueryForm);

        if (StrTool.listNotNull(tknList)) {
            remainDaysMsg.append(Language.getLangValue("monitor_base_have", Language.getCurrLang(null), null));
            remainDaysMsg.append(tknList.size());
            remainDaysMsg.append(Language.getLangValue("monitor_base_token_at", Language.getCurrLang(null), null));
            remainDaysMsg.append(remainDays);
            remainDaysMsg.append(Language.getLangValue("monitor_base_day_after", Language.getCurrLang(null), null));
            //remainDaysMsg.append("(" + DateTool.getCurDate("yyyy-MM-dd") + ")");
            remainDaysMsg.append(Language.getLangValue("monitor_base_expire", Language.getCurrLang(null), null));
        }

        return remainDaysMsg.toString();
    }

    /**
     * 获取未绑定令牌预警信息
     * 方法说明
     * @Date in Mar 13, 2013,3:58:46 PM
     * @param sendType 发送方式
     * @return
     * @throws Exception
     */
    private String getUnbindLowerWarnInfo(String sendType) throws Exception {
        // 获取绑定的和所有的令牌数量
        ReportInfo reportInfo = (ReportInfo) tokenReportServ.getReportBindTknCount(new ReportQueryForm());

        if (!StrTool.objNotNull(reportInfo)) {
            return null;
        }

        StringBuilder unbindLowerMsg = new StringBuilder();
        long tknSum = reportInfo.getTokenSumCount();
        // 配置中的未绑定令牌比
        int unBindPercent = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.MONITOR_BASE_CONF,
                ConfConstant.MONITOR_BASE_UNBINDLOWER));

        // 当前库中未绑定令牌比
        long currUnBind = (tknSum - reportInfo.getBindCount()) * 100 / tknSum;
        if (currUnBind < unBindPercent) {
            // 小于配置的未绑定比例
            unbindLowerMsg.append(Language
                    .getLangValue("monitor_base_unbind_percent", Language.getCurrLang(null), null));
            unbindLowerMsg.append(currUnBind);
            unbindLowerMsg.append("%，");
            unbindLowerMsg.append(Language.getLangValue("monitor_base_place_view", Language.getCurrLang(null), null));
        }

        return unbindLowerMsg.toString();
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
