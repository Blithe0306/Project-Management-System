/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 * @Date 2014年7月25日,上午9:05:53
 */
package com.ft.otp.manager.heartbeat.task;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.config.ConfConfig;
import com.ft.otp.manager.confinfo.config.entity.MonitorConfig;
import com.ft.otp.manager.heartbeat.action.aide.HeartBeatActionAide;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.StrTool;

/**
 * 从服务器定时检查数据库的双机热备运行监控配置和缓存中的配置信息相比较有没有变化 如果有变化需要更新调度任务中
 *
 * @Date 2014年7月25日,上午9:05:53
 * @version v1.0
 * @author WYJ
 */

public class ClientCheckTask extends Task {
    public void execute(TaskExecutionContext arg0) throws RuntimeException {
        UpdateCacheAndTaskThread updateCacheAndTaskThread = new UpdateCacheAndTaskThread();
        Thread thread = new Thread(updateCacheAndTaskThread);
        thread.start();   
    }

    /**
     * 
     * 从服务器更新缓存和定时任务调度信息
     *
     * @Date 2014年7月24日,下午2:31:11
     * @version v1.0
     * @author WYJ
     */
    class UpdateCacheAndTaskThread implements Runnable{
        public void run() {
            // 重新加载配置缓存 使缓存中数据和数据表的配置信息保持一致
            boolean baseIsUpdate = false;
            MonitorConfig monitorConfig=null;
            try {
                monitorConfig = HeartBeatActionAide.getInstance().getMonitorConfig();
            } catch (BaseException ex) {
                ex.printStackTrace();
            }
            // 如果各个预警配置的执行时间段、定时器启用与否改变则重新加载预警相关任务
            if (StrTool.objNotNull(monitorConfig) && !StrTool.strEquals(
                    ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT,
                    ConfConstant.MONITOR_HEART_BEAT_TIMEINTERVAL), monitorConfig.getTimeInterval())
                    || !StrTool.strEquals(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT,
                            ConfConstant.MONITOR_HEART_BEAT_ENABLED), monitorConfig.getEnabled())) {
                baseIsUpdate = true;
            }
            
            if (baseIsUpdate) {
                ConfConfig.clear();
                ConfConfig.loadConfConfig();
                // 重新加载预警的判断要放到加载配置的后边 
                // 重新加载定时器的监控预警任务
                HeartBeatActionAide.getInstance().addOrUpMonitorTask(NumConstant.common_number_4);
            }
        }
        
    }
}
