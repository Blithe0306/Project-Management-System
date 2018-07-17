/**
 *Administrator
 */
package com.ft.otp.manager.monitor.task.aide;

import it.sauronsoftware.cron4j.Task;

import java.util.List;

import org.apache.log4j.Logger;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.config.TaskConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.mail.MailInfo;
import com.ft.otp.common.mail.SendMailUtil;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.common.sms.SendSMSTool;
import com.ft.otp.common.task.RegScheduler;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.admin.user.service.IAdminUserServ;
import com.ft.otp.manager.confinfo.config.entity.ConfigInfo;
import com.ft.otp.manager.confinfo.config.entity.MonitorConfig;
import com.ft.otp.manager.heartbeat.action.aide.HeartBeatActionAide;
import com.ft.otp.manager.heartbeat.monitorrecord.action.aide.MonitorRecordActionAide;
import com.ft.otp.manager.heartbeat.monitorrecord.entity.MonitorRecord;
import com.ft.otp.manager.monitor.entity.MonitorAndAdminInfo;
import com.ft.otp.manager.monitor.entity.MonitorInfo;
import com.ft.otp.manager.monitor.service.IMonitorServ;
import com.ft.otp.manager.monitor.task.app.AppMonitorTask;
import com.ft.otp.manager.monitor.task.base.BaseMonitorCommonTask;
import com.ft.otp.manager.monitor.task.base.BaseMonitorTask;
import com.ft.otp.manager.monitor.task.equipment.EquipmentMonitorTask;
import com.ft.otp.manager.task.entity.TaskInfo;
import com.ft.otp.manager.task.service.ITaskInfoServ;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.IpTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 监控预警定时任务执行帮助类
 *
 * @Date in Mar 7, 2013,10:33:55 AM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class MonitorTaskAide {

    private Logger logger = Logger.getLogger(MonitorTaskAide.class);
    // 监控预警服务接口
    private IMonitorServ monitorServ = (IMonitorServ) AppContextMgr.getObject("monitorServ");
    // 管理员服务接口
    private IAdminUserServ adminUserServ = (IAdminUserServ) AppContextMgr.getObject("adminUserServ");
    // 定时器任务服务接口
    private ITaskInfoServ taskInfoServ = (ITaskInfoServ) AppContextMgr.getObject("taskInfoServ");

    /**
     * 根据预警类型来发送消息  发送后要修改发送时间
     * 方法说明
     * @Date in Mar 13, 2013,3:30:47 PM
     * @param monitorType
     */
    public String sendMonitorInfo(String monitorType) {
        StringBuilder result = new StringBuilder();
        try {
            MonitorInfo monitorInfo = new MonitorInfo();
            monitorInfo.setConftype(monitorType);
            //查询还没有发送的消息
            List<?> mInfoList = monitorServ.queryMonitorInfo(monitorInfo, new PageArgument());

            if (!StrTool.listNotNull(mInfoList)) {
                return Language.getLangValue("monitor_info_empty", Language.getCurrLang(null), null);
            }

            MonitorInfo[] arrInfos = mInfoList.toArray(new MonitorInfo[mInfoList.size()]);
            for (MonitorInfo mInfo : arrInfos) {
                if (mInfo.getIssend() == 0) {
                    if (StrTool.strNotNull(mInfo.getEmail())) {
                        // 邮件发送
                        String[] toAddressEmail = mInfo.getEmail().split(";");
                        String emailTitle=Language.getLangValue(
                                "monitor_info_title", Language.getCurrLang(null), null);
                        //双机热备运行监控使用自己的邮件通知标题
                        if(StrTool.strEquals(ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT, monitorType)){
                            emailTitle = Language.getLangValue(
                                    "heart_beat_monitor_view_tip", Language.getCurrLang(null), null);
                        }
                        MailInfo mailInfo = SendMailUtil.getMailInfo(toAddressEmail,emailTitle , mInfo.getContent(), null);
                        if (SendMailUtil.emailSeed(mailInfo)) {
                            result.append(mInfo.getTitle());
                            result.append(Language.getLangValue("monitor_info_send_success",
                                    Language.getCurrLang(null), null));
                            result.append(Language.getLangValue("semicolon", Language.getCurrLang(null), null));

                            // 修改为已经发送状态
                            mInfo.setIssend(NumConstant.common_number_1);
                            // 发送后 修改发送时间
                            mInfo.setSendtime(DateTool.currentUTC());
                            monitorServ.updateObj(mInfo);
                        } else {
                            result.append(mInfo.getTitle());
                            result.append(Language.getLangValue("monitor_info_send_faild", Language.getCurrLang(null),
                                    null));
                        }

                    } else {
                        // 短信发送
                        result.append(mInfo.getTitle());
                        result.append(Language.getLangValue("monitor_note_send_success", Language.getCurrLang(null),
                                null));
                        result.append(Language.getLangValue("semicolon", Language.getCurrLang(null), null));

                        //短信内容含有空格符号(&nbsp;)时进行替换
                        String content = mInfo.getContent();
                        if(StrTool.strNotNull(content)){
                            content = content.replace("&nbsp;", "");
                        }
                        
                        // 发送短信
                        SendSMSTool.send(mInfo.getMobile(), content);

                        // 修改为已经发送状态
                        mInfo.setIssend(NumConstant.common_number_1);
                        // 发送后 修改发送时间
                        mInfo.setSendtime(DateTool.currentUTC());
                        monitorServ.updateObj(mInfo);
                    }
                    // 如果当前预警对象配置类型为 双击运行监控类型
                    if( ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT.equals(mInfo.getConftype())){
                        // 日志记录类
                        LogCommonObj commonObj = new LogCommonObj();
                        commonObj.addAdminLog(AdmLogConstant.log_aid_heart_beat_ware, AdmLogConstant.log_obj_heart_beat_warning, 
                                Language.getLangValue("auth_ser_hostip", Language.getCurrLang(null), null)
                                + Language.getLangValue("colon", Language.getCurrLang(null), null) + mInfo.getTitle(), null, 1);
                    }
                } else {
                    result.append(mInfo.getTitle());
                    result.append(Language.getLangValue("monitor_info_repeat", Language.getCurrLang(null), null));
                }
            }
        } catch (Exception e) {
            result.append(Language.getLangValue("monitor_info_send_faild", Language.getCurrLang(null), null));
            logger.error(e.getMessage(), e);
        }

        return result.toString();
    }

    /**
     * 根据类型来发送服务器恢复正常消息
     * @Date   Nov 21, 2014,11:02:04 AM
     * @author ZWX
     * @param monitorType 监控预警类型
     * @param content     发送内容
     * @param ipAddress     服务器IP地址     
     * @return
     * @return String
     * @throws
     */
    public String sendOkMonitorInfo(String monitorType,String content,String ipAddress) {
        StringBuilder result = new StringBuilder();
        try {
            MonitorInfo monitorInfo = new MonitorInfo();
            monitorInfo.setConftype(monitorType);
            monitorInfo.setTitle(ipAddress);
            monitorInfo.setConfid(NumConstant.common_number_4); //双机运行配置类型 configId=4
            //查询监控预警消息
            List<?> mInfoList = monitorServ.queryMonitorOkInfo(monitorInfo, new PageArgument());

            if (!StrTool.listNotNull(mInfoList)) {
                return Language.getLangValue("monitor_info_empty", Language.getCurrLang(null), null);
            }

            MonitorInfo[] arrInfos = mInfoList.toArray(new MonitorInfo[mInfoList.size()]);
            for (MonitorInfo mInfo : arrInfos) {
                //是否已发送
                if (StrTool.strNotNull(mInfo.getEmail())) {
                    // 邮件发送
                    String[] toAddressEmail = mInfo.getEmail().split(";");
                    String emailTitle=null;
                    //双机热备运行监控使用自己的邮件通知标题
                    if(StrTool.strEquals(ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT, monitorType)){
                        emailTitle = Language.getLangValue(
                                "heart_beat_monitor_view_ok_tip", Language.getCurrLang(null), null);
                    }
                    MailInfo mailInfo = SendMailUtil.getMailInfo(toAddressEmail,emailTitle , content, null);
                    if (SendMailUtil.emailSeed(mailInfo)) {
                        result.append(mInfo.getTitle());
                        result.append(Language.getLangValue(
                                "monitor_info_send_success",Language.getCurrLang(null), null));
                        result.append(Language.getLangValue("semicolon", Language.getCurrLang(null), null));

                        logger.info(result.toString());
                    } else {
                        result.append(mInfo.getTitle());
                        result.append(Language.getLangValue("monitor_info_send_faild", Language.getCurrLang(null),
                                null));
                    }

                } else {
                    // 短信发送
                    result.append(mInfo.getTitle());
                    result.append(Language.getLangValue("monitor_note_send_success", Language.getCurrLang(null),
                            null));
                    result.append(Language.getLangValue("semicolon", Language.getCurrLang(null), null));

                    // 发送短信
                    SendSMSTool.send(mInfo.getMobile(), content);

                    logger.info(result.toString());
                }
                // 上一次发送的时间在预警频率内,则记录管理员日志恢复成功
                if( ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT.equals(mInfo.getConftype())){
                    LogCommonObj commonObj = new LogCommonObj();
                    commonObj.addAdminLog(AdmLogConstant.log_aid_heart_beat_ware, AdmLogConstant.log_obj_heart_beat_ok, 
                            Language.getLangValue("auth_ser_hostip", Language.getCurrLang(null), null)
                            + Language.getLangValue("colon", Language.getCurrLang(null), null) + mInfo.getTitle(), null, 0);
                }
            }
        } catch (Exception e) {
            result.append(Language.getLangValue("monitor_info_send_faild", Language.getCurrLang(null), null));
            logger.error(e.getMessage(), e);
        }

        return result.toString();
    }

    /**
     * 将监控预警中的任务加入的任务表中,添加前需判断是否已经存在,存在修改、不存在添加
     * 
     * @Date in Jun 19, 2013,10:52:07 AM
     * @param loadType 0 加载所有的预警任务；1 基本预警任务、2 系统设备预警任务、3 应用系统预警任务
     * @throws Exception
     */
    public void addOrUpMonitorTask(int loadType) {
        try {
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setEnabled(-1);
            taskInfo.setSourcetype(-1);//添加不按照任务类型查询定时任务信息
            // 所有预警任务
            List<?> taskList = taskInfoServ.query(taskInfo, new PageArgument());

            // 基本预警 前1小时内令牌同步达到多少比例时预警信息
            TaskInfo baseTaskInfo = null;
            // 基本预警 未绑定令牌比例、多长时间过期预警任务
            TaskInfo baseCommonTaskInfo = null;
            // 设备预警任务
            TaskInfo sbTaskInfo = null;
            // 应用系统预警任务
            TaskInfo appTaskInfo = null;

            for (int i = 0; i < taskList.size(); i++) {
                TaskInfo tInfo = (TaskInfo) taskList.get(i);
                String taskName = tInfo.getTaskname();
                if (StrTool.strEquals(taskName, StrConstant.MONITOR_TASKNAME_BASE)) {
                    baseTaskInfo = tInfo;
                } else if (StrTool.strEquals(taskName, StrConstant.MONITOR_TASKNAME_EQUIPMENT)) {
                    sbTaskInfo = tInfo;
                } else if (StrTool.strEquals(taskName, StrConstant.MONITOR_TASKNAME_APP)) {
                    appTaskInfo = tInfo;
                } else if (StrTool.strEquals(taskName, StrConstant.MONITOR_TASKNAME_BASE_COMMON)) {
                    baseCommonTaskInfo = tInfo;
                }
            }

            // 基本预警
            if (loadType == 0 || loadType == 1) {
                // 前1小时内令牌同步达到多少比例时预警信息
                addOrUpdateTaskInfo(baseTaskInfo, ConfConstant.MONITOR_BASE_CONF, loadType);
                // 未绑定令牌比例、多长时间过期预警任务
                addOrUpdateTaskInfo(baseCommonTaskInfo, ConfConstant.MONITOR_BASE_CONF_COMMON, loadType);
            }

            // 设备预警任务
            if (loadType == 0 || loadType == 2) {
                addOrUpdateTaskInfo(sbTaskInfo, ConfConstant.MONITOR_SB_CONF, loadType);
            }

            // 应用系统预警任务
            if (loadType == 0 || loadType == 3) {
                addOrUpdateTaskInfo(appTaskInfo, ConfConstant.MONITOR_APP_CONF, loadType);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 根据任务对象 和 配置类型判断执行修改或者添加
     * 方法说明
     * @Date in Mar 8, 2013,2:38:33 PM
     * @param taskInfo
     * @param confType
     * @param loadType 加载类型 0 服务器启动时，其它更新时
     */
    private void addOrUpdateTaskInfo(TaskInfo taskInfo, String confType, int loadType) throws Exception {
        String timeIntervalName = "";// 定时器时间间隔属性名称
        String enabledName = "";// 是否启用配置属性名称
        String taskName = "";// 任务名称
        String taskDesc = "";// 任务描述
        Task taskObj = null;// task obj

        if (StrTool.strEquals(confType, ConfConstant.MONITOR_BASE_CONF)) {
            timeIntervalName = ConfConstant.MONITOR_BASE_TIMEINTERVAL;
            enabledName = ConfConstant.MONITOR_BASE_ENABLED;
            taskName = StrConstant.MONITOR_TASKNAME_BASE;
            //前1小时内令牌同步达到多少比例时预警信息
            taskDesc = Language.getLangValue("monitor_base_send_task_desc", Language.getCurrLang(null), null);
            taskObj = new BaseMonitorTask(taskInfo);

        } else if (StrTool.strEquals(confType, ConfConstant.MONITOR_BASE_CONF_COMMON)) {
            // 未绑定令牌比例、和多长时间过期预警任务 时间不受策略配置影响，但时间只在表中存放的 默认配置个60 * 24 * 2（即2天）
            timeIntervalName = ConfConstant.MONITOR_BASE_TIMEINTERVAL_COMMON;
            enabledName = ConfConstant.MONITOR_BASE_ENABLED;
            taskName = StrConstant.MONITOR_TASKNAME_BASE_COMMON;
            taskDesc = Language.getLangValue("monitor_base_common_task_desc", Language.getCurrLang(null), null);
            //未绑定令牌比例、多长时间过期预警任务
            taskObj = new BaseMonitorCommonTask(taskInfo);

            confType = ConfConstant.MONITOR_BASE_CONF;
        } else if (StrTool.strEquals(confType, ConfConstant.MONITOR_SB_CONF)) {
            timeIntervalName = ConfConstant.MONITOR_SB_TIMEINTERVAL;
            enabledName = ConfConstant.MONITOR_SB_ENABLED;
            taskName = StrConstant.MONITOR_TASKNAME_EQUIPMENT;
            taskDesc = Language.getLangValue("monitor_equipment_task_desc", Language.getCurrLang(null), null);
            taskObj = new EquipmentMonitorTask(taskInfo);

        } else if (StrTool.strEquals(confType, ConfConstant.MONITOR_APP_CONF)) {
            timeIntervalName = ConfConstant.MONITOR_APP_TIMEINTERVAL;
            enabledName = ConfConstant.MONITOR_APP_ENABLED;
            taskName = StrConstant.MONITOR_TASKNAME_APP;
            taskDesc = Language.getLangValue("monitor_app_task_desc", Language.getCurrLang(null), null);
            taskObj = new AppMonitorTask(taskInfo);

        }

        // 是否启用
        int baseEnabled = StrTool.parseInt(ConfDataFormat.getConfValue(confType, enabledName));

        if (baseEnabled == 1) {// 启用任务
            // 定时器执行的时间间隔 分钟
            int timeInterval = StrTool.parseInt(ConfDataFormat.getConfValue(confType, timeIntervalName));

            if (StrTool.objNotNull(taskInfo)) {// 修改或服务器启动
                if (loadType == 0 || taskInfo.getEnabled() == 0) {// 修改任务信息 将未启用任务启用 服务器启动加载任务
                    // 重新设置任务时间
                    setTimeByMinute(taskInfo, timeInterval);

                    // 修改启用状态
                    if (taskInfo.getEnabled() == 0) {
                        taskInfo.setEnabled(1);// 将任务改为启用状态
                    }

                    //添加定制器任务
                    String taskId = RegScheduler.addTask(TaskConfig.getScheduler(), taskObj, taskInfo.getTaskminute(),
                            taskInfo.getTaskhour(), taskInfo.getTaskday(), taskInfo.getTaskmonth(), taskInfo
                            .getTaskweek());
                    taskInfo.setTaskid(taskId);

                    //更新
                    taskInfoServ.updateObj(taskInfo);

                } else {// 配置修改
                    setTimeByMinute(taskInfo, timeInterval);
                    taskInfo.setEnabled(baseEnabled);
                    taskInfoServ.updateObj(taskInfo);

                    //更新定时器任务
                    RegScheduler.updateTask(TaskConfig.getScheduler(), taskInfo.getTaskid(), taskInfo.getTaskminute(),
                            taskInfo.getTaskhour(), taskInfo.getTaskday(), taskInfo.getTaskmonth(), taskInfo
                            .getTaskweek());
                }
            } else {// 添加
                taskInfo = new TaskInfo();
                setTimeByMinute(taskInfo, timeInterval);
                taskInfo.setTaskname(taskName);
                taskInfo.setEnabled(baseEnabled);

                //添加定制器任务
                String taskId = RegScheduler.addTask(TaskConfig.getScheduler(), taskObj, taskInfo.getTaskminute(),
                        taskInfo.getTaskhour(), taskInfo.getTaskday(), taskInfo.getTaskmonth(), taskInfo.getTaskweek());
                taskInfo.setTaskid(taskId);
                taskInfo.setDescp(taskDesc);
                // 设置和其它任务相同的属性
                setTaskInfoSameAttr(taskInfo);

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
     * 设置taskinfo 相同属性
     * 方法说明
     * @Date in Mar 8, 2013,10:38:09 AM
     * @param taskInfo
     */
    private void setTaskInfoSameAttr(TaskInfo taskInfo) {
        taskInfo.setSourceid(0);
        taskInfo.setSourcetype(1);
    }

    /**
     * 根据传入的分钟数 设置定时器时间
     * 方法说明
     * @Date in Mar 8, 2013,11:34:07 AM
     * @param taskInfo
     * @param minute
     */
    private void setTimeByMinute(TaskInfo taskInfo, int minute) {
        taskInfo.setTaskweek("*");
        taskInfo.setTaskmonth("*");

        if (minute >= 60 && minute < 60 * 24) {
            // 按小时 最大23小时
            taskInfo.setTaskmode1(0);
            taskInfo.setTaskmode2(1);
            taskInfo.setTaskhour("*/" + String.valueOf(minute / 60));
            taskInfo.setTaskminute(String.valueOf(minute % 60));
            taskInfo.setTaskday("*");
        } else if (minute / 60 >= 24 && minute / (60 * 24) < 28) {
            // 按天 最大28天
            taskInfo.setTaskmode1(3);
            taskInfo.setTaskmode2(0);
            taskInfo.setTaskhour("0");
            taskInfo.setTaskminute("0");
            taskInfo.setTaskday("*/" + minute / (60 * 24));
        } else {
            // 按分钟 最大59分钟
            taskInfo.setTaskmode1(0);
            taskInfo.setTaskmode2(2);
            taskInfo.setTaskhour("*");
            taskInfo.setTaskday("*");

            if (minute == 0) {
                taskInfo.setTaskminute("*");
            } else {
                taskInfo.setTaskminute("*/" + minute);
            }

        }
    }

    /**
     * 获取邮件或者是手机号信息 返回结果以;隔开
     * 方法说明
     * @Date in Mar 13, 2013,3:09:10 PM
     * @param sendType
     * @param monitorInfo
     * @return
     * @throws Exception
     */
    public String getEmailAndMobile(String sendType, MonitorInfo monitorInfo) throws Exception {
        String result = "";

        // 查询出该预警类型发送预警消息的管理员
        MonitorAndAdminInfo mAndAdminInfo = new MonitorAndAdminInfo();
        mAndAdminInfo.setConftype(monitorInfo.getConftype());
        List<?> allRecvUsers = monitorServ.queryMonitorAndAdmin(mAndAdminInfo, new PageArgument());

        if (StrTool.listNotNull(allRecvUsers)) {
            StringBuffer emailOrMobileSb = new StringBuffer();
            // 查询出所有的系统管理员信息
            List<?> admInfoS = adminUserServ.query(new AdminUser(), new PageArgument());
            MonitorAndAdminInfo[] arrMA = allRecvUsers.toArray(new MonitorAndAdminInfo[allRecvUsers.size()]);

            for (MonitorAndAdminInfo monitorAndAdminInfo : arrMA) {
                // 如果查询的所有管理员信息不为空
                if (StrTool.listNotNull(admInfoS)) {
                    AdminUser[] arrAdmuUsers = admInfoS.toArray(new AdminUser[admInfoS.size()]);
                    for (AdminUser adminUser : arrAdmuUsers) {

                        if (StrTool.strEquals(monitorAndAdminInfo.getAdminid(), adminUser.getAdminid())) {
                            // 如果有匹配的管理员ID
                            if (StrTool.strEquals(sendType, StrConstant.common_number_0)) {
                                // 邮件
                                if (StrTool.strNotNull(adminUser.getEmail())) {
                                    emailOrMobileSb.append(adminUser.getEmail() + ";");
                                }
                            } else {
                                // 短信
                                if (StrTool.strNotNull(adminUser.getCellphone())) {
                                    emailOrMobileSb.append(adminUser.getCellphone() + ";");
                                }
                            }
                            break;
                        }

                    }
                }
            }

            result = emailOrMobileSb.toString();
            if (StrTool.strNotNull(result)) {
                // 去掉最后一个;分号
                result = result.substring(0, result.length() - 1);
            }
        }

        return result;
    }

    /**
     * 设置预警信息对象的 邮件或者是短信号
     * 方法说明
     * @Date in Mar 13, 2013,3:10:48 PM
     * @param sendType
     * @param monitorInfo
     */
    public void setMonitorInfoSendAddr(String sendType, MonitorInfo monitorInfo) throws Exception {
        if (StrTool.objNotNull(monitorInfo)) {
            String emailOrMobileStrs = getEmailAndMobile(sendType, monitorInfo);
            if (StrTool.strEquals(sendType, StrConstant.common_number_0)) {
                // 邮件
                monitorInfo.setEmail(emailOrMobileStrs);
                monitorInfo.setMobile("");
            } else {
                // 短信
                monitorInfo.setMobile(emailOrMobileStrs);
                monitorInfo.setEmail("");
            }
        }
    }

    /**
     * 获取预警内容的标题
     * 方法说明
     * @Date in Mar 26, 2013,5:04:59 PM
     * @return
     */
    public String getContentTitle() {
        StringBuffer titleSb = new StringBuffer();
        titleSb.append(Language.getLangValue("monitor_info_hi", Language.getCurrLang(null), null));
        titleSb.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        titleSb.append(Language.getLangValue("monitor_info_title", Language.getCurrLang(null), null));
        titleSb.append("(");
        titleSb.append(Language.getLangValue("monitor_report_time", Language.getCurrLang(null), null));
        titleSb.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
        titleSb.append(DateTool.dateToStr(System.currentTimeMillis() / 1000, true));
        titleSb.append(")");
        titleSb.append("\n");

        return titleSb.toString();
    }

    /**
     * 根据confType和confName获取配置ID
     * 方法说明
     * @Date in Mar 7, 2013,2:00:52 PM
     * @param confType
     * @param confName
     * @return
     */
    public int getConfid(String confType, String confName) {
        int confId = 0;
        ConfigInfo confObj = ConfDataFormat.getConfInfo(confType, confName);
        if (StrTool.objNotNull(confObj)) {
            confId = confObj.getId();
        }

        return confId;
    }

    /**
     * 根据发送类型、配置类型、配置ID、title 来处理预警信息
     * 
     * @Date in Mar 13, 2013,4:22:36 PM
     * @param warnInfo
     *          预警信息内容
     * @param sendType
     *          发送方式
     * @param confType
     *          预警配置类型
     * @param confId
     *          配置ID 可以为空
     * @param title
     *          预警标题
     * @return
     * @throws Exception
     */
    public String warnInfoDeal(String warnInfo, String sendType, String confType, int confId, String title)
    throws Exception {
        String result = "";
        // 查询 信息记录
        MonitorInfo monitorInfo = new MonitorInfo();
        monitorInfo.setConftype(confType);
        if (confId != NumConstant.common_number_0) {
            // 系统基本预警需要此条件
            monitorInfo.setConfid(confId);
        }
        if(StrTool.strEquals(ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT, confType)){//如果是双机运行监控则添加按照ip条件查找
            monitorInfo.setTitle(title);
        }
        // 查询出最近发送的预警信息
        List<?> sbMonitorList = monitorServ.query(monitorInfo, new PageArgument());
        // 预警信息记录
        MonitorInfo mInfo = null;
        if (StrTool.listNotNull(sbMonitorList)) {
            mInfo = (MonitorInfo) sbMonitorList.get(0);
        }

        if (StrTool.strNotNull(warnInfo)) {// 有预警消息
            // 添加发送内容标题
            StringBuffer warnInfoSb = new StringBuffer();
            //不是双机运行监控功能在邮件内容中拼接
            //系统默认内容title
            if(!StrTool.strEquals(ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT, confType)){
                warnInfoSb.append(getContentTitle());
            }
            warnInfoSb.append(warnInfo);

            boolean isAddInfo = false;//是否新添加预警记录
            if (StrTool.objNotNull(mInfo)) {                //有预警的消息对象
                if (getIsSend(mInfo)) {//需要发送
                    // 添加一条发送信息
                    isAddInfo = true;
                } 
                else {
                    //时间上是不需要发送的但还需再判断下发送状态 如果是已发送状态 就不修改了，否则修改预警信息
                    if (mInfo.getIssend() == NumConstant.common_number_0) {
                        //还没有发送，可以修改信息
                        mInfo.setContent(warnInfoSb.toString());
                        mInfo.setSendtime(StrTool.timeSecondL());
                        // 修改邮件或短信的预警接收人
                        setMonitorInfoSendAddr(sendType, mInfo);
                        monitorServ.updateObj(mInfo);
                    }
                }
            } 
            //如果监控对象为null说明可能性
            else {
                isAddInfo = true;
            }

            if (isAddInfo) {
                // 添加新的预警信息
                monitorInfo.setTitle(title);
                monitorInfo.setContent(warnInfoSb.toString());
                // 设置为将要发送状态  0:不发送,1:发送
                monitorInfo.setIssend(NumConstant.common_number_0);
                monitorInfo.setSendtime(StrTool.timeSecondL());
                // 设置邮件或短信的预警接收人
                setMonitorInfoSendAddr(sendType, monitorInfo);

                monitorServ.addObj(monitorInfo);
            }
        } 
        // 无预警消息，要把未发送出去的消息删除掉
        else {
            if (StrTool.objNotNull(mInfo)) {
                // 如果为即将发送状态 则删除该预警记录
                if (mInfo.getIssend() == NumConstant.common_number_0) {

                    List<String> localIpLists = IpTool.localHostIp();

                    // 服务器以恢复启动并记录日志,以短信/邮件形式发送给接收人.
                    MonitorTaskAide taskAide = new MonitorTaskAide();
                    String isServer = HeartBeatActionAide.getInstance().validateMainServer();
                    String ipaddress = "";

                    //获取双机热备份运行监控配置信息
                    MonitorConfig monitorConfig = HeartBeatActionAide.getInstance().getMonitorConfig();
                    String enabled = monitorConfig.getEnabled();
                    //启用状态
                    if (StrTool.strNotNull(enabled) && enabled.equals(StrConstant.common_number_1)) {
                        //本机ip地址为服务器地址
                        if (localIpLists.contains(monitorConfig.getMainIpAddress())) {
                            ipaddress = monitorConfig.getMainIpAddress();
                        }else if(localIpLists.contains(monitorConfig.getSpareIpAddress())){
                            ipaddress = monitorConfig.getSpareIpAddress();
                        }
                    }

                    taskAide.okInfoDeal(ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT, ipaddress);

                    monitorServ.delObj(mInfo);
                }
            }

            result = title + Language.getLangValue("monitor_no_info", Language.getCurrLang(null), null)
            + Language.getLangValue("semicolon", Language.getCurrLang(null), null);
        }

        // 判断发送消息
        String sendResult = sendMonitorInfo(confType);
        if (!StrTool.strNotNull(result)) {
            result = sendResult;
        }

        return result;
    }
    /**
     * 当服务器恢复正常时,将恢复正常的信息发送给接收人
     * @Date   Nov 21, 2014,10:09:24 AM
     * @author ZWX
     * @param confType      配置类型
     * @param serverType    1:主服务器,2:从服务器
     * @param ipAddress     IP地址
     * @return
     * @throws Exception
     * @return String
     * @throws
     */
    public String okInfoDeal(String confType, String ipAddress)
    throws BaseException {
        String result = "";

        // 获取预警方式 从缓存中获取
        //configInfo表中 通过 conftype_confname --> confValue
        String sendType = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT,
                ConfConstant.MONITOR_HEART_BEAT_SEND_TYPE);

        // 预警信息
        StringBuffer okMsg = new StringBuffer();
        String international = null;
        
        //通过IP地址判断是主从服务器
        MonitorConfig monitorConfig = HeartBeatActionAide.getInstance().getMonitorConfig();
        String interFlag = "1"; //主从服务器  1:主,2:从
        if(StrTool.objNotNull(monitorConfig)){
            if(ipAddress.equals(monitorConfig.getSpareIpAddress())){
                interFlag = "2";
            }
        }
        
        //0：email 恢复正常
        if(StrConstant.common_number_0.equals(sendType)){
            international = "heart_beat_monitor_task_info_ok_";
        }
        //1: mobile 恢复正常
        else {
            international = "heart_beat_monitor_task_info_mobile_ok_";
        }
        //获取国际化信息进行拼接
        String okMsg_=Language.getLangValue(international+interFlag, Language.getCurrLang(null), null);
        if(StrTool.strNotNull(okMsg_)){
            okMsg_=okMsg_.replace("#IP#", ipAddress).replace("#TIME#", DateTool.getCurDate("yyyy-MM-dd HH:mm:ss"));
        }
        okMsg.append(okMsg_);
        
        logger.info(okMsg);

        // 判断发送消息
        String sendResult = sendOkMonitorInfo(confType, okMsg.toString(),ipAddress);
        if (!StrTool.strNotNull(result)) {
            result = sendResult;
        }
        return result;
    }

    /**
     * 根据上一次的发送时间判断此消息是否需要发送
     * 
     * @Date in Mar 13, 2013,3:22:57 PM
     * @param monitorInfo
     * @return true 发送，false 不发送
     * @throws Exception
     */
    public boolean getIsSend(MonitorInfo monitorInfo) throws Exception {
        long oldSendTime = monitorInfo.getSendtime();
        // 发送时间间隔 分钟
        int interval = 0;
        String confType = monitorInfo.getConftype();

        // 应用系统监控
        if (StrTool.strEquals(confType, ConfConstant.MONITOR_APP_CONF)) {
            interval = NumConstant.MONITOR_APP_SEND_TIMEINTERVAL;
        } 
        // 设备监控预警
        else if (StrTool.strEquals(confType, ConfConstant.MONITOR_SB_CONF)) {
            interval = NumConstant.MONITOR_SB_SEND_TIMEINTERVAL;
        }
        // 基本预警
        else if (StrTool.strEquals(confType, ConfConstant.MONITOR_BASE_CONF)) {
            String title = monitorInfo.getTitle();
            if (StrTool.strEquals(title, Language.getLangValue("monitor_base_expire_title", Language.getCurrLang(null),
                    null))) {// 将要过期天数
                interval = NumConstant.MONITOR_BASE_REMAINDAYS_SEND_TIMEINTERVAL;
            } else if (StrTool.strEquals(title, Language.getLangValue("monitor_base_unbind_title", Language
                    .getCurrLang(null), null))) {// 未绑定令牌比例
                interval = NumConstant.MONITOR_BASE_UNBINDLOWER_SEND_TIMEINTERVAL;
            } else if (StrTool.strEquals(title, Language.getLangValue("monitor_base_hour_sync_title", Language
                    .getCurrLang(null), null))) {// 1小时同步比例
                interval = NumConstant.MONITOR_BASE_SYNCUPPER_SEND_TIMEINTERVAL;
            } else {
                interval = 0;
            }
        } 
        //双机热备运行监控
        else if(StrTool.strEquals(confType, ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT)){
            interval = NumConstant.MONITOR_HEART_BEAT_CONF_SEND_TIMEINTERVAL;
        } else {
            interval = 0;
        }

        // 下一次的发送时间
        long time = oldSendTime + interval * 60;
        // 当上一次的发送时间等于0 或者是下一次发送时间大于当前时间时，则发送该信息
        if (oldSendTime == 0 || time < DateTool.currentUTC()) {
            return true;
        } else {
            return false;
        }
    }

}