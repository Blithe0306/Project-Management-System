/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 * @Date 2014年7月18日,下午2:04:46
 */
package com.ft.otp.manager.heartbeat.task;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

import org.apache.log4j.Logger;

import com.ft.otp.manager.heartbeat.action.aide.MonitorThread;

/**
 * 双机热备运行监控任务
 *
 * @Date 2014年7月18日,下午2:04:46
 * @version v1.0
 * @author WYJ
 */
public class MonitorTask extends Task {

    private Logger logger = Logger.getLogger(MonitorTask.class);
    // 预警信息发送和更新帮助类
    public void execute(TaskExecutionContext executor) throws RuntimeException {
        try {
            //确保线程单列
            MonitorThread monitorThread = MonitorThread.getInstance();
            Thread thread = new Thread(monitorThread);
            thread.start();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        executor.pauseIfRequested();
        if (executor.isStopped()) {
            return;
        }
    }
    
}
