/**
 *Administrator
 */
package com.ft.otp.manager.data.action.aide;

import java.util.List;

import org.apache.log4j.Logger;

import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.config.TaskConfig;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.common.task.RegScheduler;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.confinfo.config.entity.ConfigInfo;
import com.ft.otp.manager.confinfo.config.service.IConfigInfoServ;
import com.ft.otp.manager.data.action.DataBakTask;
import com.ft.otp.manager.data.entity.DBBakConfInfo;
import com.ft.otp.manager.task.entity.TaskInfo;
import com.ft.otp.manager.task.service.ITaskInfoServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 备份帮助类
 *
 * @Date in Sep 25, 2013,4:49:31 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class DataBakAide {

    private Logger logger = Logger.getLogger(DataBakAide.class);

    // 公共配置服务接口
    private IConfigInfoServ confInfoServ = (IConfigInfoServ) AppContextMgr.getObject("confInfoServ");
    // 定制任务帮助类
    private ITaskInfoServ taskInfoServ = (ITaskInfoServ) AppContextMgr.getObject("taskInfoServ");

    /**
     * 系统启动时添加 自动备份日志任务
     * 方法说明
     * @Date in Sep 25, 2013,4:50:39 PM
     */
    public void addDataBakTask() {
        try {
            // 获取备份设置信息
            ConfigInfo config = new ConfigInfo();
            config.setConftype(ConfConstant.CONF_TYPE_BAK);
            List<?> confList = confInfoServ.queryConfInfo(config, new PageArgument());
            DBBakConfInfo bakParam = DBBakConfInfo.getCenterInfoByList(confList);

            // 获取定时器相关信息
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setSourcetype(3);
            taskInfo = (TaskInfo) taskInfoServ.find(taskInfo);
            bakParam.setTaskInfo(taskInfo);

            if (bakParam.getIstimeauto() == NumConstant.common_number_1) {
                TaskInfo useTaskInfo = taskInfo;

                //如果查询出来的定时信息为null 则需添加定时信息
                if (!StrTool.objNotNull(taskInfo)) {
                    useTaskInfo = bakParam.getTaskInfo();
                }

                DataBakTask dataBakTask = new DataBakTask(bakParam);
                String taskId = RegScheduler.addTask(TaskConfig.getScheduler(), dataBakTask, useTaskInfo);

                useTaskInfo.setTaskid(taskId);
                useTaskInfo.setEnabled(1);

                if (!StrTool.objNotNull(taskInfo)) {
                    taskInfoServ.addObj(useTaskInfo);
                } else {
                    taskInfoServ.updateObj(useTaskInfo);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
