/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.usersource.action;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

import java.util.Map;

import org.apache.log4j.Logger;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.confinfo.usersource.entity.UserSourceInfo;
import com.ft.otp.manager.confinfo.usersource.service.IUserSourceServ;
import com.ft.otp.manager.task.entity.TaskInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 类、接口等说明
 *
 * @Date in Mar 9, 2012,4:24:41 PM
 *
 * @version v1.0
 *
 * @author ZJY
 */
public class UserSourceTask extends Task {

    private Logger logger = Logger.getLogger(UserSourceTask.class);
    /* (non-Javadoc)
     * @see it.sauronsoftware.cron4j.Task#execute(it.sauronsoftware.cron4j.TaskExecutionContext)
     */
    private IUserSourceServ userSourceServ;
    private TaskInfo taskInfo = null;

    public UserSourceTask(Object object) {
        userSourceServ = (IUserSourceServ) AppContextMgr.getObject("userSourceServ");
        if (object instanceof TaskInfo) {
            taskInfo = (TaskInfo) object;
        }
    }

    public void execute(TaskExecutionContext executor) throws RuntimeException {
        try {
            UserSourceInfo userSourceInfo = new UserSourceInfo();
            userSourceInfo.setId(taskInfo.getSourceid());
            userSourceInfo.setSourcename(taskInfo.getTaskname());
            userSourceInfo.setSourcetype(-1);
            UserSourceInfo usupInfo = userSourceServ.getUserSourceInfo(userSourceInfo);
            Map<String, Object> map = null;
            String updateMessage = "";
            try {
                map = userSourceServ.updateUserInfo(usupInfo);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                updateMessage = "执行定时操作失败！";
            }

            if (StrTool.mapNotNull(map)) {
                if (StrTool.strNotNull((String) map.get("updateResultStr"))) {
                    updateMessage = map.get("updateResultStr") + "";
                } else {
                    if (StrTool.strEquals((String) map.get("tableNameNull"), "tableNameNull")) {
                        updateMessage = "没有得到数据来源表名！";
                    } else if (StrTool.strEquals((String) map.get("columnNull"), "columnNull")) {
                        updateMessage = "没有得到字段对应关系！";
                    } else if (StrTool.strEquals((String) map.get("connNull"), "connNull")) {
                        updateMessage = "没有得到数据库连接！";
                    } else if (StrTool.strEquals((String) map.get("ldapNull"), "ldapNull")) {
                        updateMessage = "没有得到LDAP信息，更新用户失败！";
                    } else if (StrTool.strEquals((String) map.get("usInfoNotExist"), "usInfoNotExist")) {
                        updateMessage = "此用户来源不存在用户信息！";
                    } else if (StrTool.strEquals((String) map.get("usNull"), "usNull")) {
                        updateMessage = "没有得到用户来源信息，更新用户失败！";
                    }
                }
                //System.out.println("用户来源执行定时任务，执行时间" + DateTool.dateToStr(System.currentTimeMillis() / 1000, true)
                //        + ",执行结果==>用户来源" + userSourceInfo.getSourcename() + updateMessage);
                executor.setStatusMessage(updateMessage);
            } else {
                //System.out.println(updateMessage);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }

        } catch (BaseException ex) {
            ex.printStackTrace();
        }
        executor.pauseIfRequested();
        if (executor.isStopped()) {
            return;
        }

    }

    public boolean canBePaused() {
        return super.canBePaused();
    }

    public boolean canBeStopped() {
        return super.canBeStopped();
    }

    public boolean supportsCompletenessTracking() {
        return super.supportsCompletenessTracking();
    }

    public boolean supportsStatusTracking() {
        return super.supportsStatusTracking();
    }

}
