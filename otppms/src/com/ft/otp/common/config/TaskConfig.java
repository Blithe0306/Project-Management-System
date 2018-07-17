/**
 *Administrator
 */
package com.ft.otp.common.config;

import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.Task;

import java.util.List;

import org.apache.log4j.Logger;

import com.ft.otp.common.page.PageArgument;
import com.ft.otp.common.task.RegScheduler;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.confinfo.usersource.action.UserSourceTask;
import com.ft.otp.manager.data.action.aide.DataBakAide;
import com.ft.otp.manager.heartbeat.action.aide.HeartBeatActionAide;
import com.ft.otp.manager.monitor.task.aide.MonitorTaskAide;
import com.ft.otp.manager.task.entity.TaskInfo;
import com.ft.otp.manager.task.log.LogTaskAide;
import com.ft.otp.manager.task.service.ITaskInfoServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 定时执行任务类信息
 *
 * @Date in Feb 15, 2012,11:32:01 AM
 *
 * @version v1.0
 *
 * @author ZJY
 */
public class TaskConfig {

    private static Logger logger = Logger.getLogger(TaskConfig.class);

    private ITaskInfoServ taskInfoServ = (ITaskInfoServ) AppContextMgr.getObject("taskInfoServ");
    // 监控预警帮助类
    private MonitorTaskAide monitorTaskAide = new MonitorTaskAide();
    // 定时备份帮助类
    private DataBakAide dataBakAide = new DataBakAide();
    // 定时删除日志帮助类
    private LogTaskAide logTaskAide = new LogTaskAide();
    private static TaskConfig config = null;
    private static Scheduler scheduler = null;

    public TaskConfig() {
        scheduler = new Scheduler();
        try {
            // 添加或者更新监控预警相关任务到定时器中
            monitorTaskAide.addOrUpMonitorTask(0);

            // 添加或者更新定时删除日志任务到定时器中
            logTaskAide.addLogTask(0);

            // 定时备份任务添加
            dataBakAide.addDataBakTask();
            
            //定时监控从服务状态
            HeartBeatActionAide.getInstance().addMonitorTask();
            
            //定时使从服务器上任务调度和主服务器任务调度同步
            HeartBeatActionAide.getInstance().addSchedulerSyncTask();

            //用户来源定时任务处理
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setEnabled(1);
            // 查询出所有是启用状态的任务
            List<?> taskList = taskInfoServ.query(taskInfo, new PageArgument());
            if (StrTool.listNotNull(taskList)) {
                // 启动所有是启动状态的任务
                for (int i = 0; i < taskList.size(); i++) {
                    TaskInfo tInfo = (TaskInfo) taskList.get(i);
                    Task taskObject = null;
                    if (tInfo.getSourcetype() == 0) {
                        // 用户来源定时任务
                        taskObject = new UserSourceTask(tInfo);
                        String strId = RegScheduler.addTask(scheduler, taskObject, tInfo);
                        tInfo.setTaskid(strId);
                    }
                }
            }
            // 启动任务
            scheduler.start();
            TaskConfig.setScheduler(scheduler);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    public static TaskConfig loadTaskConfig() {
        if (config != null) {
            return config;
        }

        synchronized (TaskConfig.class) {
            if (config == null) {
                config = new TaskConfig();
            }
            return config;
        }
    }

    /**
     * @return the scheduler
     */
    public static Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * @param scheduler the scheduler to set
     */
    public static void setScheduler(Scheduler scheduler) {
        TaskConfig.scheduler = scheduler;
    }

    public static void stop() {
        try {
            if (null != scheduler) {
                scheduler.stop();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 重新加载定时任务执行器
     * 方法说明
     * @Date in Mar 9, 2013,4:39:26 PM
     */
    public synchronized static void reload() {
        stop();
        config = null;
        loadTaskConfig();
    }

}
