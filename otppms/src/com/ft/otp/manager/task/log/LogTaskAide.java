/**
 *Administrator
 */
package com.ft.otp.manager.task.log;

import it.sauronsoftware.cron4j.Task;

import org.apache.log4j.Logger;

import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.config.TaskConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.task.RegScheduler;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.task.entity.TaskInfo;
import com.ft.otp.manager.task.service.ITaskInfoServ;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.StrTool;

/**
 * 日志定时任务执行帮助类
 *
 * @Date in Apr 11, 2013,4:04:07 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class LogTaskAide {

    private Logger logger = Logger.getLogger(LogTaskAide.class);
    // 定时器任务服务接口
    private ITaskInfoServ taskInfoServ = (ITaskInfoServ) AppContextMgr.getObject("taskInfoServ");

    /**
     * 将日志定时任务加入的任务表中,添加前需判断是否已经存在,存在修改、不存在添加
     * @param loadType 0 启动时加载 ，1 配置中加载
     * @Date in Apr 11, 2013,4:17:52 PM
     * @throws Exception
     */
    public void addLogTask(int loadType) {
        try {
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setEnabled(-1);
            taskInfo.setTaskname(StrConstant.LOG_TASKNAME_DELETE);
            taskInfo.setSourcetype(NumConstant.common_number_2);

            // 日志任务
            TaskInfo logTaskInfo = (TaskInfo) taskInfoServ.find(taskInfo);

            // 定时删除日志任务
            addOrUpdateTaskInfo(logTaskInfo, loadType);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 根据任务对象 和 配置类型判断执行修改或者添加
     * 方法说明
     * @Date in Mar 8, 2013,2:38:33 PM
     * @param taskInfo
     * @param loadType
     * @param confType
     */
    private void addOrUpdateTaskInfo(TaskInfo taskInfo, int loadType) throws Exception {
        // 获取是否启用
        int baseEnabled = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_COMMON,
                ConfConstant.LOG_TIMING_ENABLED));

        if (baseEnabled == 1) {// 启用任务
            Task taskObj = new LogDeleteTask(taskInfo);
            if (StrTool.objNotNull(taskInfo)) {// 修改或服务器启动
                if (loadType == 0 || taskInfo.getEnabled() == 0) {// 修改任务信息 将未启用任务启用 服务器启动加载任务
                    // 修改启用状态
                    if (taskInfo.getEnabled() == 0) {
                        taskInfo.setEnabled(1);// 将任务改为启用状态
                    }

                    // 更新  只更新状态 不用重新更新任务时间
                    taskInfoServ.updateObj(taskInfo);

                    //添加定制器任务
                    RegScheduler.addTask(TaskConfig.getScheduler(), taskObj, taskInfo.getTaskminute(), taskInfo
                            .getTaskhour(), taskInfo.getTaskday(), taskInfo.getTaskmonth(), taskInfo.getTaskweek());

                } else {// 配置修改  不用更新定时器时间
                    taskInfo.setEnabled(baseEnabled);
                    taskInfoServ.updateObj(taskInfo);

                    //更新定时器任务
                    RegScheduler.updateTask(TaskConfig.getScheduler(), taskInfo.getTaskid(), taskInfo.getTaskminute(),
                            taskInfo.getTaskhour(), taskInfo.getTaskday(), taskInfo.getTaskmonth(), taskInfo
                                    .getTaskweek());
                }
            } else {// 添加
                taskInfo = new TaskInfo();
                setTimeByMinute(taskInfo);
                // 任务名称
                taskInfo.setTaskname(StrConstant.LOG_TASKNAME_DELETE);
                taskInfo.setEnabled(baseEnabled);

                //添加定制器任务
                String taskId = RegScheduler.addTask(TaskConfig.getScheduler(), taskObj, taskInfo.getTaskminute(),
                        taskInfo.getTaskhour(), taskInfo.getTaskday(), taskInfo.getTaskmonth(), taskInfo.getTaskweek());
                taskInfo.setTaskid(taskId);
                // 任务描述
                taskInfo.setDescp(Language.getLangValue("task_log_delete_desc", Language.getCurrLang(null), null));
                // 设置和其它任务相同的属性

                taskInfoServ.addObj(taskInfo);
            }
        } else {// 禁用任务
            if (StrTool.objNotNull(taskInfo)) {
                // 如果是已经启用的预警任务 现在禁用了
                if (taskInfo.getEnabled() == 1 && baseEnabled == 0) {
                    // 删除预警任务
                    RegScheduler.delTask(TaskConfig.getScheduler(), taskInfo.getTaskid());
                    taskInfo.setEnabled(0);
                    // 修改为不启用状态
                    taskInfoServ.updateObj(taskInfo);
                }
            }
        }
    }

    /**
     * 设置定时器执行时间
     * 方法说明
     * @Date in Mar 8, 2013,11:34:07 AM
     * @param taskInfo
     * @param minute
     */
    private void setTimeByMinute(TaskInfo taskInfo) {
        // 设定每天在0时0分 删除日志一次
        taskInfo.setTaskmode1(3);
        taskInfo.setTaskmode2(0);
        taskInfo.setTaskhour("0");
        taskInfo.setTaskminute("0");
        taskInfo.setTaskday("*");
        taskInfo.setTaskmonth("*");
        taskInfo.setTaskweek("*");
        taskInfo.setSourceid(0);
        taskInfo.setSourcetype(2);
    }

}
