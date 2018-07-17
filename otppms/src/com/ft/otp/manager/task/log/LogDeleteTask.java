/**
 *Administrator
 */
package com.ft.otp.manager.task.log;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.confinfo.config.entity.ConfigInfo;
import com.ft.otp.manager.confinfo.config.service.IConfigInfoServ;
import com.ft.otp.manager.data.entity.DBBakConfInfo;
import com.ft.otp.manager.data.entity.TableInfo;
import com.ft.otp.manager.data.service.DataBakServ;
import com.ft.otp.manager.logs.adminlog.entity.AdminLogInfo;
import com.ft.otp.manager.logs.adminlog.service.IAdminLogServ;
import com.ft.otp.manager.logs.userlog.entity.UserLogInfo;
import com.ft.otp.manager.logs.userlog.service.IUserLogServ;
import com.ft.otp.manager.task.entity.TaskInfo;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 定时删除日志定时器执行任务类
 *
 * @Date in Mar 6, 2013,4:20:17 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class LogDeleteTask extends Task {

    private Logger logger = Logger.getLogger(LogDeleteTask.class);

    /* (non-Javadoc)
     * @see it.sauronsoftware.cron4j.Task#execute(it.sauronsoftware.cron4j.TaskExecutionContext)
     */
    //管理员日志服务接口
    private IAdminLogServ adminLogServ = null;
    //用户日志服务接口
    private IUserLogServ userLogServ = null;
    //公共配置服务接口
    private IConfigInfoServ confInfoServ = (IConfigInfoServ) AppContextMgr.getObject("confInfoServ");
    private TaskInfo taskInfo = null;

    /**
     * 初始化方法
     * @param object
     */
    public LogDeleteTask(Object object) {
        adminLogServ = (IAdminLogServ) AppContextMgr.getObject("adminLogServ");
        userLogServ = (IUserLogServ) AppContextMgr.getObject("userLogServ");
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
            String confType = ConfConstant.CONF_TYPE_COMMON;
            // 如果日志定时删除为启用状态
            if (StrTool.parseInt(ConfDataFormat.getConfValue(confType, ConfConstant.LOG_TIMING_ENABLED)) == 1) {
                // 获取日志保留的时间
                String timeInterval = ConfDataFormat.getConfValue(confType, ConfConstant.LOG_TIMING_DELETE);
                int timeIntervalNum = StrTool.parseInt(timeInterval);
                if (timeIntervalNum > 0) {
                    UserLogInfo userLogInfo = new UserLogInfo();
                    // 设置日志保留超过设置天数的日志时间
                    int delLogTime = StrTool.parseInt(String.valueOf(DateTool.currentUTC() - timeIntervalNum * 24 * 60
                            * 60));
                    userLogInfo.setLogtime(delLogTime);
                    AdminLogInfo adminLogInfo = new AdminLogInfo();
                    adminLogInfo.setLogtime(delLogTime);

                    // 判断是否需要备份
                    String isBakLog = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_COMMON,
                            ConfConstant.LOG_IS_BAK);//
                    if (StrTool.strEquals(isBakLog, StrConstant.common_number_1)) {// 备份
                        bakLog(String.valueOf(delLogTime));// 只备份将要删除的日志
                    }

                    userLogServ.delObj(userLogInfo);
                    adminLogServ.delObj(adminLogInfo);
                    //System.out.println("定时删除日志一次");
                }
            }
            executor.setStatusMessage(result);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        executor.pauseIfRequested();
        if (executor.isStopped()) {
            return;
        }
    }

    /**
     * 备份传入条件值
     * 
     * @Date in Dec 17, 2013,9:54:23 AM
     * @param conditon
     */
    private void bakLog(String conditon) {
        try {
            Map<String, TableInfo> tableInfos = DataBakServ.getTablesAndCols(2);//获得配置的表信息，只获取日志表

            //获取备份参数信息
            ConfigInfo config = new ConfigInfo();
            config.setConftype(ConfConstant.CONF_TYPE_BAK);
            List<?> confList = confInfoServ.queryConfInfo(config, new PageArgument());
            DBBakConfInfo dbBakConfInfo = DBBakConfInfo.getCenterInfoByList(confList);

            DataBakServ.createBakFile(tableInfos, dbBakConfInfo, conditon);//备份
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
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
