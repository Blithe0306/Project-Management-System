/**
 *Administrator
 */
package com.ft.otp.manager.data.action;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

import org.apache.log4j.Logger;

import com.ft.otp.manager.data.entity.DBBakConfInfo;
import com.ft.otp.manager.data.service.BakThread;

/**
 * 数据备份任务
 *
 * @Date in Mar 9, 2012,4:24:41 PM
 *
 * @version v1.0
 *
 * @author BYL
 */
public class DataBakTask extends Task {

    private Logger logger = Logger.getLogger(DataBakTask.class);

    private DBBakConfInfo dbBakConfInfo = null;

    public DataBakTask(DBBakConfInfo bakConfInfo) {
        dbBakConfInfo = bakConfInfo;
    }

    public void execute(TaskExecutionContext executor) throws RuntimeException {
        try {
            //确保线程单列
            BakThread bakThreak = BakThread.getInstance(dbBakConfInfo);
            bakThreak.run();
            //System.out.println(DateTool.getCurDate("yyyy-MM-dd HH:mm:ss")+"启动一个备份数据的线程");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        executor.pauseIfRequested();
        if (executor.isStopped()) {
            return;
        }

    }

}
