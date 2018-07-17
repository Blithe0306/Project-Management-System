/**
 *Administrator
 */
package com.ft.otp.common.task;

import com.ft.otp.manager.task.entity.TaskInfo;

import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.Task;

/**
 * 类、接口等说明
 *
 * @Date in Mar 5, 2012,5:06:05 PM
 *
 * @version v1.0
 *
 * @author ZJY
 */
public class RegScheduler {
    /**
     * 
     * 方法说明
     * 添加任务
     * @Date in Mar 13, 2012,6:56:19 PM
     * @param scheduler
     * @param task
     * @param minSt
     * @param hourStr
     * @param dayStr
     * @param monthStr
     * @param weekStr
     * @return
     */
    public static String addTask(Scheduler scheduler, Task task, String minSt, String hourStr, String dayStr,
            String monthStr, String weekStr) {
        if (scheduler == null) {
            scheduler = new Scheduler();
        }
        String schedId = scheduler.schedule(getTime(minSt, hourStr, dayStr, monthStr, weekStr), task);
        return schedId;
    }

    /**
     * 
     * 方法说明
     * 添加任务
     * @Date in Mar 13, 2012,6:56:19 PM
     * @param scheduler
     * @param task
     * @param taskInfo
     * @return
     */
    public static String addTask(Scheduler scheduler, Task task, TaskInfo taskInfo) {
        if (scheduler == null) {
            scheduler = new Scheduler();
        }
        String schedId = scheduler.schedule(getTime(taskInfo.getTaskminute(), taskInfo.getTaskhour(), taskInfo
                .getTaskday(), taskInfo.getTaskmonth(), taskInfo.getTaskweek()), task);
        return schedId;
    }

    /**
     * 方法说明
     * 更新任务
     * @Date in Mar 13, 2012,6:56:32 PM
     * @param scheduler
     * @param id
     * @param minSt
     * @param hourStr
     * @param dayStr
     * @param monthStr
     * @param weekStr
     */
    public static void updateTask(Scheduler scheduler, String id, String minSt, String hourStr, String dayStr,
            String monthStr, String weekStr) {
        if (scheduler == null) {
            scheduler = new Scheduler();
        }
        scheduler.reschedule(id, getTime(minSt, hourStr, dayStr, monthStr, weekStr));
    }

    /**
     * 方法说明
     * 停止任务
     * @Date in Mar 13, 2012,6:56:43 PM
     * @param scheduler
     * @param id
     */
    public static void delTask(Scheduler scheduler, String id) {
        if (scheduler == null) {
            scheduler = new Scheduler();
        }
        scheduler.deschedule(id);
    }

    /**
     * 方法说明
     * 执行任务参数封装
     * @Date in Mar 13, 2012,6:56:43 PM
     * @param minSt
     * @param hourStr
     * @param dayStr
     * @param monthStr
     * @param weekStr
     */
    private static String getTime(String minSt, String hourStr, String dayStr, String monthStr, String weekStr) {
        String timStr = minSt + " " + hourStr + " " + dayStr + " " + monthStr + " " + weekStr;
        return timStr;
    }
}
