/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 * @Date 2014年7月17日,下午3:34:28
 */
package com.ft.otp.manager.heartbeat.action.aide;

import it.sauronsoftware.cron4j.Task;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.config.TaskConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.common.task.RegScheduler;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.confinfo.config.entity.ConfigInfo;
import com.ft.otp.manager.confinfo.config.entity.MonitorConfig;
import com.ft.otp.manager.confinfo.config.service.IConfigInfoServ;
import com.ft.otp.manager.heartbeat.monitorrecord.action.aide.MonitorRecordActionAide;
import com.ft.otp.manager.heartbeat.monitorrecord.entity.MonitorRecord;
import com.ft.otp.manager.heartbeat.task.ClientCheckTask;
import com.ft.otp.manager.heartbeat.task.MonitorTask;
import com.ft.otp.manager.monitor.entity.MonitorInfo;
import com.ft.otp.manager.monitor.service.IMonitorServ;
import com.ft.otp.manager.monitor.task.aide.MonitorTaskAide;
import com.ft.otp.manager.task.entity.TaskInfo;
import com.ft.otp.manager.task.service.ITaskInfoServ;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.IpTool;
import com.ft.otp.util.tool.StrTool;
import com.ibm.db2.jcc.b.i;

/**
 *双机热备运行监控 帮助类
 *
 * @Date 2014年7月17日,下午3:34:28
 * @version v1.0
 * @author WYJ
 */
public class HeartBeatActionAide {
    private Logger logger = Logger.getLogger(HeartBeatActionAide.class);
    //访问时间 Map -> (key,value) ->(ip,访问时间(毫秒))   
    //clear条件
    //1.time_interval(更新周期),2.IP地址修改,2.启用/禁用
    private static ConcurrentHashMap<String, Date> visitTimeMap = null;
    //任务调度id集合 存放web容器的定时任务 ，不能从数据库中取 因双机热备时生成在各自web容器内的任务调度id是不一样的
    private static HashMap<String, String> schedulerTaskIdMap = null;
    //私有静态变量 -用户单例模式
    private static HeartBeatActionAide instance = null;
    // 公共配置服务接口
    private IConfigInfoServ confInfoServ = (IConfigInfoServ) AppContextMgr.getObject("confInfoServ");
    // 定制任务帮助类
    private ITaskInfoServ taskInfoServ = (ITaskInfoServ) AppContextMgr.getObject("taskInfoServ");
    // 监控预警服务接口
    private IMonitorServ monitorServ = (IMonitorServ) AppContextMgr.getObject("monitorServ");

    //服务器多久检查一次
    private static long APPOINT_TIME=1000*60*60;//1小时
    //服务器响应地址
    public static final String SERVER_ACTION_URL="/otpcenter/service/heartbeat!getRunningState.action";
    //测试目标服务器是否存在
    public static final String TEST_SERVER_ACTION_URL="/otpcenter/service/heartbeat!testServerState.action";

    public static final String SERVER_ACTION_CHAR=":";

    public static final String SERVER_ACTION_HTTP="http://";

    public static final String SERVER_REQUEST_METHOD="GET";

    /**
     * 私有构造
     */
    private HeartBeatActionAide() {

    }

    /**
     * 清空所有缓存对象
     * @Date   Nov 28, 2014,6:10:41 PM
     * @author ZWX
     * @return void
     * @throws
     */
    public static synchronized void clear() {
        instance = null;
        visitTimeMap = null;
        schedulerTaskIdMap = null;
    }

    /**
     * 获取单例实例
     * @Date   2014年7月17日,下午3:43:23
     * @author WYJ
     * @return
     * @return HeartBeatActionAide
     * @throws
     */
    public static synchronized HeartBeatActionAide getInstance() {
        if (null == instance) {
            instance = new HeartBeatActionAide();
            if (null == visitTimeMap) {
                visitTimeMap = new ConcurrentHashMap<String, Date>();
            }
            if(null == schedulerTaskIdMap){
                schedulerTaskIdMap = new HashMap<String, String>();
            }
        }
        return instance;
    }

    /**
     * 获取map
     * @Date   2014年7月17日,下午5:03:37
     * @author WYJ
     * @return
     * @return ConcurrentHashMap<String,Date>
     * @throws
     */
    public ConcurrentHashMap<String, Date> getVisitTimeMap() {
        return visitTimeMap;
    }

    /**
     * 获取map
     * @Date   2014年7月17日,下午5:03:37
     * @author WYJ
     * @return
     * @return HashMap<String,Date>
     * @throws
     */
    public HashMap<String, String> getSchedulerTaskIdMap() {
        return schedulerTaskIdMap;
    }

    /**
     * 设置访问时间
     * @Date   2014年7月17日,下午3:51:14
     * @author WYJ
     * @param ipAddress
     * @return void
     * @throws
     */
    public synchronized void updateSchedulerTaskIdMap(String taskName,String schedulerTaskId) {
        schedulerTaskIdMap.put(taskName, schedulerTaskId);
    }

    /**
     * 设置调度信息
     * @Date   2014年7月17日,下午3:51:14
     * @author WYJ
     * @param ipAddress
     * @return void
     * @throws
     */
    public synchronized void updateVisiTime(String ipAddress) {
        visitTimeMap.put(ipAddress, new Date());
    }

    /**
     * 移除调度信息
     * @Date   2014年7月17日,下午3:51:14
     * @author WYJ
     * @param ipAddress
     * @return void
     * @throws
     */
    public synchronized void removeSchedulerTaskIdMap(String taskName) {
        visitTimeMap.remove(taskName);
    }

    /**
     * 系统启动时添加双机热备运行监控任务
     * 方法说明
     * @Date in 2014年7月22日 14:47:52
     */
    public void addMonitorTask() {
        try {
            // 获取备份设置信息
            MonitorConfig monitorConfig = getMonitorConfig();
            // 获取定时器相关信息
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setSourcetype(NumConstant.common_number_4);
            taskInfo = (TaskInfo) taskInfoServ.find(taskInfo);
            //将任务信息set到系统监控配置信息对象中
            monitorConfig.setTaskInfo(taskInfo);
            //服务为启用状态
            if (Integer.valueOf(monitorConfig.getEnabled()) == NumConstant.common_number_1) {
                TaskInfo heartBeatTaskInfo = taskInfo;
                //如果查询出来的定时信息为null 则需添加定时信息
                if (!StrTool.objNotNull(taskInfo)) {
                    heartBeatTaskInfo = monitorConfig.getTaskInfo();
                }
                //创建监控任务对象
                Task monitorTask = new MonitorTask();
                //设置任务调度时间
                setTimeByMinute(heartBeatTaskInfo, Integer.valueOf(monitorConfig.getTimeInterval()));
                //当前任务添加至任务管理中 
                String taskId = RegScheduler.addTask(TaskConfig.getScheduler(), monitorTask, heartBeatTaskInfo);

                //把任务id放入任务调度id集合(map)中 存储
                HeartBeatActionAide hba=HeartBeatActionAide.getInstance();
                //put(常量,taskId)
                hba.updateSchedulerTaskIdMap(StrConstant.HEART_BEAT_MONITOR, taskId);

                //将任务id,是否启用状态 设置为 启用
                heartBeatTaskInfo.setTaskid(taskId);
                heartBeatTaskInfo.setEnabled(NumConstant.common_number_1);
                if (!StrTool.objNotNull(taskInfo)) {
                    taskInfoServ.addObj(heartBeatTaskInfo);
                } else {
                    taskInfoServ.updateObj(heartBeatTaskInfo);
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 系统启动时添加从服务器的调度任务和主服务器任务同步
     * 方法说明
     * @Date in 2014年7月22日 14:47:52
     */
    public void addSchedulerSyncTask() {
        try {
            // 获取备份设置信息
            MonitorConfig monitorConfig = getMonitorConfig();
            //如果是从服务器
            if(IpTool.localHostIp().contains(monitorConfig.getSpareIpAddress())){
                // 获取定时器相关信息
                //从数据库从查出任务
                TaskInfo useTaskInfo = monitorConfig.getTaskInfo();

                ClientCheckTask clientCheckTask = new ClientCheckTask();
                //设置任务调度时间 一分钟一次
                setTimeByMinute(useTaskInfo, 1);
                RegScheduler.addTask(TaskConfig.getScheduler(), clientCheckTask, useTaskInfo);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 获取双机热备份运行监控配置信息
     * @Date   2014年7月23日,下午2:25:17
     * @author WYJ
     * @return
     * @throws BaseException
     * @return MonitorConfig
     * @throws
     */
    public MonitorConfig getMonitorConfig() throws BaseException {
        // 获取运行监控设置信息
        ConfigInfo config = new ConfigInfo();
        config.setConftype(ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT);
        List<?> confList = confInfoServ.queryConfInfo(config, new PageArgument());
        MonitorConfig monitorConfig = MonitorConfig.getMonitorConfigByList(confList);

        return monitorConfig;
    }

    /**
     * @throws Exception 
     * 服务器运行状态更改操作 如异常则发送 邮件/短信 给管理员
     * @Date   2014年7月21日,下午6:11:03
     * @author WYJ
     * @param ipAddress ip地址 
     * @param runningState 运行状态 0：异常 1：正常
     * @param serverType 1：主服务器   2：从服务器
     * @return void
     * @throws
     */
    public void dealSerRunRecord(String ipAddress, String runningState, String serverType) throws Exception {
        MonitorTaskAide taskAide = new MonitorTaskAide();
        //封装指定服务器的bean信息
        MonitorRecord mrVo = packageMonitorRecord(ipAddress.trim(), serverType, runningState);
        //更新另一台服务器运行状况
        //首页冒泡提示 使用
        MonitorRecordActionAide.monitorRecordMap.put(ipAddress.trim(),mrVo);

        if (StrConstant.common_number_0.equals(runningState)) {//运行异常发消息通知管理员

            // 获取预警方式 从缓存中获取
            //configInfo表中 通过 conftype_confname --> confValue
            String sendType = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT,
                    ConfConstant.MONITOR_HEART_BEAT_SEND_TYPE);

            // 预警信息
            StringBuffer warnMsg = new StringBuffer();
            String international = null;
            //0：email
            if(StrConstant.common_number_0.equals(sendType)){
                international = "heart_beat_monitor_task_info_";
            }
            //1: mobile
            else {
                international = "heart_beat_monitor_task_info_mobile_";
            }
            //获取国际化信息进行拼接
            String warnMsg_=Language.getLangValue(international+serverType, Language.getCurrLang(null), null);
            if(StrTool.strNotNull(warnMsg_)){
                warnMsg_=warnMsg_.replace("#IP#", ipAddress).replace("#TIME#", DateTool.dateToStr(mrVo.getRecordTime(),true));
            }
            warnMsg.append(warnMsg_);

            //发送预警消息
            String message=taskAide.warnInfoDeal(warnMsg.toString(), sendType, ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT, 
                    NumConstant.common_number_4, ipAddress);
            logger.error(message);

        }else{
            //主从服务器运行正常，检查数据库中最近一条主从服务器的异常信息是否在异常消息通知频率内 如果在则进行删除。
            //删除后就可以在从服务器发生异常后立即提醒，不需要等到超出频率时间才进行提醒
            //此情况是考虑到服务器数据库之间是主从备份时，从服务器发生异常恢复后，主服务器数据库中还存在有一条频率内的异常消息，导致从服务器恢复立即再次异常而不能进行异常提醒
            getMonitorInfo(ipAddress);
        }
    }

    /**
     * 将监控预警中的任务加入的任务表中,添加前需判断是否已经存在,存在修改、不存在添加
     * 
     * @Date in 2014年7月23日 15:08:04
     * @param loadType
     * @throws Exception
     */
    public void addOrUpMonitorTask(int loadType) {
        try {
            // 获取定时器相关信息
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setSourcetype(loadType);
            taskInfo = (TaskInfo) taskInfoServ.find(taskInfo);
            //双机热备运行监控任务调度
            addOrUpdateTaskInfo(taskInfo, ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 根据任务对象 和 配置类型判断执行修改或者添加
     * 方法说明
     * @Date in 2014年7月23日 15:03:14
     * @param taskInfo
     * @param confType
     */
    private  void addOrUpdateTaskInfo(TaskInfo taskInfo, String confType) throws Exception {
        // 定时器时间间隔属性名称
        String timeIntervalName = ConfConstant.MONITOR_HEART_BEAT_TIMEINTERVAL;
        // 是否启用配置属性名称
        String enabledName = ConfConstant.MONITOR_HEART_BEAT_ENABLED;
        // 任务名称
        String taskName = StrConstant.MONITOR_HEARTBEAT_TASKNAME;
        // 任务描述
        String taskDesc = Language.getLangValue("heart_beat_monitor_task_desc", Language.getCurrLang(null), null); 

        Task taskObj = new MonitorTask();
        // 缓存中是否为启用 
        int baseEnabled = StrTool.parseInt(ConfDataFormat.getConfValue(confType, enabledName));
        if (baseEnabled==NumConstant.common_number_1){// 启用任务
            // 定时器执行的时间间隔 分钟
            int timeInterval = StrTool.parseInt(ConfDataFormat.getConfValue(confType, timeIntervalName));

            if (StrTool.objNotNull(taskInfo)) {// 修改或服务器启动
                int nowEnabled=taskInfo.getEnabled();
                // 修改任务信息 将未启用任务启用 服务器启动加载任务
                if (nowEnabled==NumConstant.common_number_0) {
                    // 重新设置任务时间
                    setTimeByMinute(taskInfo, timeInterval);
                    // 修改启用状态
                    if (taskInfo.getEnabled() == NumConstant.common_number_0) {
                        taskInfo.setEnabled(NumConstant.common_number_1);// 将任务改为启用状态
                    }

                    //添加定制器任务
                    String taskId=addSchedulerTask(taskObj, taskInfo);
                    taskInfo.setTaskid(taskId);
                    HeartBeatActionAide hba=HeartBeatActionAide.getInstance();
                    hba.updateSchedulerTaskIdMap(StrConstant.HEART_BEAT_MONITOR, taskId);
                    //更新
                    taskInfoServ.updateObj(taskInfo);

                } else {// 配置修改
                    setTimeByMinute(taskInfo, timeInterval);
                    taskInfo.setEnabled(baseEnabled);
                    taskInfoServ.updateObj(taskInfo);
                    HeartBeatActionAide hba=HeartBeatActionAide.getInstance();
                    HashMap<String, String> hashMap=hba.getSchedulerTaskIdMap();
                    String taskId=hashMap.get(StrConstant.HEART_BEAT_MONITOR);
                    //更新定时器任务
                    RegScheduler.updateTask(TaskConfig.getScheduler(), taskId, taskInfo.getTaskminute(),
                            taskInfo.getTaskhour(), taskInfo.getTaskday(), taskInfo.getTaskmonth(),
                            taskInfo.getTaskweek());
                    //TODO 移除服务器的ip访问时间记录
//                  HeartBeatActionAide.getInstance().getVisitTimeMap().clear();
                    //记录主从服务器状态的Map 清空
                    MonitorRecordActionAide.monitorRecordMap.clear();
                }
            } else {// 添加
                addTask(timeInterval,taskName,taskDesc,baseEnabled,taskObj);
            }
        } else {// 禁用任务
            disableTask(taskInfo,baseEnabled);
        }
    }

    /**
     * 添加任务
     * @Date   2014年7月24日,下午3:23:29
     * @author WYJ
     * @param timeInterval 任务执行周期（分钟）
     * @param taskName 任务名称
     * @param taskDesc 任务描述
     * @param baseEnabled 任务是否启用
     * @param taskObj 调度任务对象
     * @throws BaseException
     * @return void
     * @throws
     */
    private void addTask(int timeInterval,String taskName,String taskDesc,int baseEnabled,Task taskObj) throws BaseException{
        TaskInfo taskInfo= new TaskInfo();
        setTimeByMinute(taskInfo, timeInterval);
        taskInfo.setTaskname(taskName);
        taskInfo.setEnabled(baseEnabled);
        taskInfo.setDescp(taskDesc);
        //添加定制器任务
        String taskId = addSchedulerTask(taskObj, taskInfo);
        taskInfo.setSourcetype(NumConstant.common_number_4);
        taskInfo.setTaskid(taskId);
        taskInfoServ.addObj(taskInfo);

        HeartBeatActionAide hba=HeartBeatActionAide.getInstance();
        hba.updateSchedulerTaskIdMap(StrConstant.HEART_BEAT_MONITOR, taskId);
    }

    /**
     * 添加调度任务 
     * @Date   2014年7月24日,下午3:28:28
     * @author WYJ
     * @param taskObj
     * @param taskInfo
     * @return
     * @return String 调度任务ID
     * @throws
     */
    public String addSchedulerTask(Task taskObj,TaskInfo taskInfo){
        //添加定制器任务
        String taskId = RegScheduler.addTask(TaskConfig.getScheduler(), taskObj, taskInfo.getTaskminute(),
                taskInfo.getTaskhour(), taskInfo.getTaskday(), taskInfo.getTaskmonth(), taskInfo.getTaskweek());
        return taskId;
    }

    /**
     * 禁用任务
     * @Date   2014年7月24日,下午3:20:34
     * @author WYJ
     * @param taskInfo
     * @return void
     * @throws Exception
     */
    private void disableTask(TaskInfo taskInfo,int baseEnabled) throws Exception{
        if (StrTool.objNotNull(taskInfo)) {
            // 如果是已经启用的预警任务 现在禁用了
            if (baseEnabled == 0) {
                // 删除预警任务
                HeartBeatActionAide hba=HeartBeatActionAide.getInstance();
                String taskId=hba.getSchedulerTaskIdMap().get(StrConstant.HEART_BEAT_MONITOR);
                RegScheduler.delTask(TaskConfig.getScheduler(), taskId);
                taskInfo.setEnabled(NumConstant.common_number_0);
                // 修改为不启用状态
                taskInfoServ.updateObj(taskInfo);
                hba.removeSchedulerTaskIdMap(StrConstant.HEART_BEAT_MONITOR);

                //移除从服务器的ip访问时间记录
                HeartBeatActionAide.getInstance().getVisitTimeMap().clear();
                //清除主从服务器状态的Map
                MonitorRecordActionAide.monitorRecordMap.clear();
            }
        }
    }

    /**
     * 根据传入的分钟数 设置定时器时间
     * 方法说明
     * @Date 2014年7月28日 09:53:29
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
     * 验证当前计算机是不是主服务器
     * @Date   2014年7月21日,下午4:51:54
     * @author WYJ
     * @return
     * @return String 0获取 服务器IP失败  1本机为主服务器 2本机为备份服务器
     * @throws
     */
    public String validateMainServer() {
        String isServer = StrConstant.common_number_0;
        List<String> localIpLists = IpTool.localHostIp();
        if(StrTool.listNotNull(localIpLists)){
            try {
                //获取双机热备份运行监控配置信息
                MonitorConfig monitorConfig = getMonitorConfig();
                String enabled = monitorConfig.getEnabled();
                //启用状态
                if (StrTool.strNotNull(enabled) && enabled.equals(StrConstant.common_number_1)) {
                    //本机ip地址为服务器地址
                    if (localIpLists.contains(monitorConfig.getMainIpAddress())) {
                        isServer = StrConstant.common_number_1;
                    }else if(localIpLists.contains(monitorConfig.getSpareIpAddress())){
                        isServer = StrConstant.common_number_2;
                    }
                }
            } catch (BaseException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        return isServer;
    }

    /**
     * 从服务器运行此方法
     * @Date   2014年7月24日,下午3:14:21
     * @author WYJ
     * @throws Exception
     * @return void
     * @throws
     */
    public void doClientJob() throws Exception {
        //获取系统监控配置信息
        MonitorConfig monitorConfig=getMonitorConfig();
        //获取主服务器IP地址
        String mainIpAddress=monitorConfig.getMainIpAddress();
        //获取端口号
        String port = monitorConfig.getPort();
        //从服务器IP地址
        String spareIpAddress=monitorConfig.getSpareIpAddress();
        //进行拼接 URL地址
        //  http://mainIpAddress:18004/otpcenter/service/heartbeat!getRunningState.action
        StringBuffer httpUrl=new StringBuffer();
        httpUrl.append(SERVER_ACTION_HTTP).append(mainIpAddress.trim());
        httpUrl.append(SERVER_ACTION_CHAR).append(port.trim()).append(SERVER_ACTION_URL);

        HttpURLConnection conn =null;
        String runningState="";
        try{
            URL url = new URL(httpUrl.toString()); //创建URL对象
            //返回一个URLConnection对象，它表示到URL所引用的远程对象的连接
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000); //设置连接超时为5秒
            conn.setRequestMethod(SERVER_REQUEST_METHOD); //设定请求方式  GET
            conn.connect(); //建立到远程对象的实际连接
            int response_code = conn.getResponseCode();
            //如果获取的连接的相应码为200 标识目标服务运行正常
            if (response_code == HttpURLConnection.HTTP_OK) {
                //标识目标服务器运行正常
                runningState=StrConstant.common_number_1;
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            //标识目标服务器运行异常
            runningState=StrConstant.common_number_0;
        }finally {
            if(conn != null){
                conn.disconnect(); //中断连接
            }
            // 保存目标服务器运行情况
            dealSerRunRecord(mainIpAddress, runningState, StrConstant.common_number_1);

            //检查从服务器的最新的一条异常信息是否在异常提醒频率内，如果在则进行删除
            getMonitorInfo(spareIpAddress);
        }
    }

    /**
     * @throws Exception 
     * @throws BaseException 
     * @throws Exception 
     * @throws BaseException 
     * @throws NumberFormatException 
     * 服务器运行检查
     * @Date   2014年7月21日,下午6:35:08
     * @author WYJ
     * @return void
     * @throws
     */
    public void doServerJob() throws Exception{
        ConcurrentHashMap<String, Date> visitTimeMap = HeartBeatActionAide.getInstance().getVisitTimeMap();
        try {
            if(StrTool.mapNotNull(visitTimeMap)){
                Set<String> ipAddressSet = visitTimeMap.keySet();
                for(String ipAddress:ipAddressSet){

                    Date visitTime=visitTimeMap.get(ipAddress);
                    Date nowTime=new Date();
                    long differTime = nowTime.getTime()-visitTime.getTime();
                    APPOINT_TIME = Long.valueOf(String.valueOf(Integer.valueOf(getMonitorConfig().getTimeInterval())*60*1000));
                    //访问时间和当前时间的差值 与定时器频率求差
                    int hour=(int)(differTime - APPOINT_TIME);
                    //处理从服务器没有访问（更新状态为异常，发邮件或短息通知）
                    if(hour>0){
                        dealSerRunRecord(ipAddress,StrConstant.common_number_0,StrConstant.common_number_2);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally{
            //检查主服务器的最新的一条异常信息是否在异常提醒频率内，如果在则进行删除
            getMonitorInfo(getMonitorConfig().getMainIpAddress());
        }
    }



    /**
     * 封装MonitorRecord信息
     * @Date   2014年9月12日,下午4:08:52
     * @author WYJ
     * @param ipAddress ip地址
     * @param serverType 服务器类型 1主服务器 2备份服务器（从服务器）
     * @param runningState 运行状态 0异常 1正常
     * @return
     * @return MonitorRecord
     * @throws
     */
    public MonitorRecord packageMonitorRecord(String ipAddress,String serverType,String runningState){
        MonitorRecord monitorRecord = new MonitorRecord();
        monitorRecord.setIpAddress(ipAddress);
        monitorRecord.setServerType(serverType);
        monitorRecord.setRunningState(runningState);
        monitorRecord.setRecordTime(StrTool.timeSecond());
        return monitorRecord;
    }

    /**
     * 处理预警频率内的预警信息是否存在，如果存在则将这条预警信息删除
     * @Date   Oct 30, 2014,9:36:19 AM
     * @author WYJ
     * @param ipaddress
     * @return
     * @throws BaseException
     * @return MonitorInfo
     * @throws
     */
    public void getMonitorInfo(String ipaddress) throws BaseException{
        MonitorInfo monitorInfo = new MonitorInfo();
        monitorInfo.setConftype(ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT);
        // 系统基本预警需要此条件
        monitorInfo.setConfid(NumConstant.common_number_4);
        monitorInfo.setTitle(ipaddress);
        // 查询出最近发送的预警信息
        List<?> sbMonitorList = monitorServ.query(monitorInfo, new PageArgument());
        // 预警信息记录
        MonitorInfo mInfo = null;
        if (StrTool.listNotNull(sbMonitorList)) {
            mInfo = (MonitorInfo) sbMonitorList.get(0);
            long oldSendTime = mInfo.getSendtime();
            // 下一次的发送时间周期
            long time = NumConstant.MONITOR_HEART_BEAT_CONF_SEND_TIMEINTERVAL*60;
            // 当 上一次发送的时间在预警频率内则进行删除
            //(此情况对应为当服务器发生异常并提醒后，管理员对服务进行了恢复.恢复成功后服务器在第一个预警频率内再次发生异常时)
            if (DateTool.currentUTC()-oldSendTime < time) {

                // 服务器以恢复启动并记录日志,以短信/邮件形式发送给接收人.
                MonitorTaskAide taskAide = new MonitorTaskAide();
                taskAide.okInfoDeal(ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT, ipaddress);

                monitorServ.delObj(mInfo);
            }
        }
    }
}
