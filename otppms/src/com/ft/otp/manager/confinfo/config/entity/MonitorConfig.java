package com.ft.otp.manager.confinfo.config.entity;
/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 * @Date 2014年7月18日,下午4:25:04
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.manager.task.entity.TaskInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 监控预警配置实体
 *
 * @Date 2014年7月18日,下午4:25:04
 * @version v1.0
 * @author WYJ
 */
public class MonitorConfig extends BaseEntity {

    private int id;//主键
    private String mainIpAddress;//主服务器Ip地址
    private String spareIpAddress;//从服务器Ip地址
    private String adminId;//管理员外键
    private String enabled;//是否启用预警监控
    private String timeInterval;//定时器时间间隔
    private String sendtype;//预警通知方式
    private String port;//主服务器运行服务端口号
    
    
    //一下两个属性用于记录日志
    private String[] baserecvusers; // 预警接收人
    private String[] oldbaserecvusers;// 旧的预警接收人

    /**
     * 由配置列表得到MonitorConfig对象
     * @Date in 2014年7月23日 09:24:50
     * @param monitorInfo
     * @param configList
     * @return
     */
    public static MonitorConfig getMonitorConfigByList(List<?> configList) {
        MonitorConfig monitorConfig = new MonitorConfig();
        if(StrTool.listNotNull(configList)){
            Iterator<?> iter = configList.iterator();
            while (iter.hasNext()) {
                ConfigInfo config = (ConfigInfo) iter.next();
                String configName = config.getConfname();
                String configValue = config.getConfvalue();
                String configType = config.getConftype();
                if (StrTool.strNotNull(configName)) {
                    if (configType.equals(ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT)) {
                        if (configName.equals(ConfConstant.MONITOR_HEART_BEAT_ENABLED)) {
                            monitorConfig.setEnabled(configValue);
                        } else if (configName.equals(ConfConstant.MONITOR_HEART_BEAT_MAIN_IP)) {
                            monitorConfig.setMainIpAddress(configValue);
                        } else if (configName.equals(ConfConstant.MONITOR_HEART_BEAT_SPARE_IP)) {
                            monitorConfig.setSpareIpAddress(configValue);
                        } else if (configName.equals(ConfConstant.MONITOR_HEART_BEAT_ADMIN_ID)) {
                            monitorConfig.setAdminId(configValue);
                        } else if (configName.equals(ConfConstant.MONITOR_HEART_BEAT_SEND_TYPE)) {
                            monitorConfig.setSendtype(configValue);
                        } else if (configName.equals(ConfConstant.MONITOR_HEART_BEAT_TIMEINTERVAL)) {
                            monitorConfig.setTimeInterval(configValue);
                        } else if(configName.equals(ConfConstant.MONITOR_HEART_BEAT_PORT)){
                            monitorConfig.setPort(configValue);
                        }
                    }
                }
            }
        }
        return monitorConfig;
    }

    /**
     * 由MonitorConf配置对象得到配置列表
     * @Date in Aug 19, 2013,2:54:46 PM
     * @param monitorConfInfo
     * @return
     */
    public static List<Object> getListByMonitorConfig(MonitorConfig monitorConfig) {
        List<Object> configList = null;
        if (StrTool.objNotNull(monitorConfig)) {
            configList = new ArrayList<Object>();
            // 基本预警配置
            ConfigInfo baseenabledConf = new ConfigInfo(ConfConstant.MONITOR_HEART_BEAT_ENABLED,
                    monitorConfig.getEnabled(), ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT,
                    NumConstant.common_number_0, "");
            ConfigInfo basesendtypeConf = new ConfigInfo(ConfConstant.MONITOR_HEART_BEAT_SEND_TYPE,
                    monitorConfig.getSendtype(), ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT,
                    NumConstant.common_number_0, "");

            ConfigInfo baseremaindaysConf = new ConfigInfo(ConfConstant.MONITOR_HEART_BEAT_MAIN_IP,
                    monitorConfig.getMainIpAddress(), ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT,
                    NumConstant.common_number_0, "");

            ConfigInfo baseunbindlowerConf = new ConfigInfo(ConfConstant.MONITOR_HEART_BEAT_SPARE_IP,
                    monitorConfig.getSpareIpAddress(), ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT,
                    NumConstant.common_number_0, "");

            ConfigInfo basesyncupperConf = new ConfigInfo(ConfConstant.MONITOR_HEART_BEAT_ADMIN_ID,
                    monitorConfig.getAdminId(), ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT,
                    NumConstant.common_number_0, "");

            ConfigInfo basetimeintervalConf = new ConfigInfo(ConfConstant.MONITOR_HEART_BEAT_TIMEINTERVAL,
                    monitorConfig.getTimeInterval(), ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT,
                    NumConstant.common_number_0, "");
            ConfigInfo portConf= new ConfigInfo(ConfConstant.MONITOR_HEART_BEAT_PORT, monitorConfig.getPort(),
                    ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT,NumConstant.common_number_0, "");
            configList.add(basesyncupperConf);
            configList.add(baseunbindlowerConf);
            configList.add(baseremaindaysConf);
            configList.add(basesendtypeConf);
            configList.add(baseenabledConf);
            configList.add(basetimeintervalConf);
            configList.add(portConf);
        }
        return configList;
    }

    /**
     * 定时器对象
     */
    private TaskInfo taskInfo = null;//定时器对象

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMainIpAddress() {
        return mainIpAddress;
    }

    public void setMainIpAddress(String mainIpAddress) {
        this.mainIpAddress = mainIpAddress;
    }

    public String getSpareIpAddress() {
        return spareIpAddress;
    }

    public void setSpareIpAddress(String spareIpAddress) {
        this.spareIpAddress = spareIpAddress;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        String temp=null;
        if(StrTool.strNotNull(adminId)){
            if(adminId.indexOf(",")>0){
             String [] admins= adminId.split(",");
             for(String s:admins){
                 temp = temp==null?s.trim():temp+","+s.trim();
             }
            }else{
                temp =adminId.trim();
            }
        }
        this.adminId = temp;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getSendtype() {
        return sendtype;
    }

    public void setSendtype(String sendtype) {
        this.sendtype = sendtype;
    }
    
    public TaskInfo getTaskInfo() {
        if (taskInfo == null) {
            taskInfo = new TaskInfo();
        }

        if (!StrTool.strNotNull(taskInfo.getTaskminute())) {
            taskInfo.setTaskminute(getTimeInterval());
        }

        if (!StrTool.strNotNull(taskInfo.getTaskhour())) {
            taskInfo.setTaskhour("*");
        }

        if (!StrTool.strNotNull(taskInfo.getTaskday())) {
            taskInfo.setTaskday("*");
        }

        if (!StrTool.strNotNull(taskInfo.getTaskweek())) {
            taskInfo.setTaskweek("*");
        }

        if (!StrTool.strNotNull(taskInfo.getTaskmonth())) {
            taskInfo.setTaskmonth("*");
        }

        taskInfo.setSourcetype(NumConstant.common_number_4);//4定时运行监控
        taskInfo.setSourceid(NumConstant.common_number_0);
        taskInfo.setTaskname(StrConstant.HEART_BEAT_MONITOR);
        taskInfo.setDescp(Language.getLangValue("heart_beat_monitor_task_desc", Language.getCurrLang(null), null));
        return taskInfo;
    }

    public void setTaskInfo(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }

    public String[] getBaserecvusers() {
        return baserecvusers;
    }

    public void setBaserecvusers(String[] baserecvusers) {
        this.baserecvusers = baserecvusers;
    }

    public String[] getOldbaserecvusers() {
        return oldbaserecvusers;
    }

    public void setOldbaserecvusers(String[] oldbaserecvusers) {
        this.oldbaserecvusers = oldbaserecvusers;
    }

}
