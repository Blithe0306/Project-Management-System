/**
 *Administrator
 */
package com.ft.otp.manager.monitor.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.manager.confinfo.config.entity.ConfigInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 监控预警策略配置实体对象
 *
 * @Date in Mar 1, 2013,1:44:29 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class MonitorConfInfo extends BaseEntity {

    /**
     * 监控预警策略基本配置
     */
    private String baseenabled; // 是否启用预警
    private String basesendtype; // 预警方式
    private String[] baserecvusers; // 预警接收人
    private String[] oldbaserecvusers;// 旧的预警接收人
    private String baseremaindays; // 令牌将要过期天数
    private String baseunbindlower; // 未绑定的令牌比例
    private String basesyncupper; // 1小时内令牌同步达到多少比例（次数/用户数）
    private String basetimeinterval; // 定时器执行的时间间隔 单位分钟

    /**
     * 设备监控预警策略配置
     */
    private String sbenabled; // 是否启用预警
    private String sbsendtype; // 预警方式
    private String[] sbrecvusers; // 预警接收人
    private String[] oldsbrecvusers;// 旧的预警接收人
    private String sbcpuupper; // CPU的上限阀值
    private String sbdiskupper; // 磁盘的上限阀值  
    private String sbmemupper; // 内存空间占用率的上限阀值
    private String sbtimeinterval; // 定时器执行的时间间隔 单位分钟

    /**
     * 应用系统监控预警策略配置
     */
    private String appenabled; // 是否启用预警
    private String appsendtype; // 预警方式
    private String[] apprecvusers; // 预警接收人
    private String[] oldapprecvusers;// 旧的预警接收人
    private String apptimeinterval; // 定时器执行的时间间隔 单位分钟

    /**
     * 由配置列表得到MonitorConfInfo对象
     * 
     * @Date in Mar 1, 2013,2:26:06 PM
     * @param monitorInfo
     * @param configList
     * @return
     */
    public static MonitorConfInfo getMonitorInfoByList(MonitorConfInfo monitorInfo, List<?> configList) {
        Iterator<?> iter = configList.iterator();
        while (iter.hasNext()) {
            ConfigInfo config = (ConfigInfo) iter.next();
            String configName = config.getConfname();
            String configValue = config.getConfvalue();
            String configType = config.getConftype();
            if (StrTool.strNotNull(configName)) {
                if (configType.equals(ConfConstant.MONITOR_APP_CONF)) {
                    if (configName.equals(ConfConstant.MONITOR_APP_ENABLED)) {
                        monitorInfo.setAppenabled(configValue);
                    } else if (configName.equals(ConfConstant.MONITOR_APP_SEND_TYPE)) {
                        monitorInfo.setAppsendtype(configValue);
                    } else if (configName.equals(ConfConstant.MONITOR_APP_TIMEINTERVAL)) {
                        monitorInfo.setApptimeinterval(configValue);
                    }
                } else if (configType.equals(ConfConstant.MONITOR_BASE_CONF)) {
                    if (configName.equals(ConfConstant.MONITOR_BASE_ENABLED)) {
                        monitorInfo.setBaseenabled(configValue);
                    } else if (configName.equals(ConfConstant.MONITOR_BASE_REMAINDAYS)) {
                        monitorInfo.setBaseremaindays(configValue);
                    } else if (configName.equals(ConfConstant.MONITOR_BASE_SENDTYPE)) {
                        monitorInfo.setBasesendtype(configValue);
                    } else if (configName.equals(ConfConstant.MONITOR_BASE_SYNCUPPER)) {
                        monitorInfo.setBasesyncupper(configValue);
                    } else if (configName.equals(ConfConstant.MONITOR_BASE_TIMEINTERVAL)) {
                        monitorInfo.setBasetimeinterval(configValue);
                    } else if (configName.equals(ConfConstant.MONITOR_BASE_UNBINDLOWER)) {
                        monitorInfo.setBaseunbindlower(configValue);
                    }
                } else if (configType.equals(ConfConstant.MONITOR_SB_CONF)) {
                    if (configName.equals(ConfConstant.MONITOR_SB_CPUUPPER)) {
                        monitorInfo.setSbcpuupper(configValue);
                    } else if (configName.equals(ConfConstant.MONITOR_SB_DISKUPPER)) {
                        monitorInfo.setSbdiskupper(configValue);
                    } else if (configName.equals(ConfConstant.MONITOR_SB_ENABLED)) {
                        monitorInfo.setSbenabled(configValue);
                    } else if (configName.equals(ConfConstant.MONITOR_SB_MEMUPPER)) {
                        monitorInfo.setSbmemupper(configValue);
                    } else if (configName.equals(ConfConstant.MONITOR_SB_SEND_TYPE)) {
                        monitorInfo.setSbsendtype(configValue);
                    } else if (configName.equals(ConfConstant.MONITOR_SB_TIMEINTERVAL)) {
                        monitorInfo.setSbtimeinterval(configValue);
                    }
                }
            }
        }
        return monitorInfo;
    }

    /**
     * 由CoreConfInfo配置对象得到配置列表
     * @Date in Nov 26, 2012,4:51:18 PM
     * @param dbConfInfo
     * @return
     */
    public static List<Object> getListByMonitorInfo(MonitorConfInfo monitorConfInfo) {
        List<Object> configList = null;
        if (StrTool.objNotNull(monitorConfInfo)) {
            configList = new ArrayList<Object>();

            // 基本预警配置
            if (StrTool.strNotNull(monitorConfInfo.getBaseenabled())) {
                ConfigInfo baseenabledConf = new ConfigInfo(ConfConstant.MONITOR_BASE_ENABLED, monitorConfInfo
                        .getBaseenabled(), ConfConstant.MONITOR_BASE_CONF, NumConstant.common_number_0, "是否启用预警");
                configList.add(baseenabledConf);
            }
            if (StrTool.strNotNull(monitorConfInfo.getBasesendtype())) {
                ConfigInfo basesendtypeConf = new ConfigInfo(ConfConstant.MONITOR_BASE_SENDTYPE, monitorConfInfo
                        .getBasesendtype(), ConfConstant.MONITOR_BASE_CONF, NumConstant.common_number_0, "预警方式");
                configList.add(basesendtypeConf);
            }
            if (StrTool.strNotNull(monitorConfInfo.getBaseremaindays())) {
                ConfigInfo baseremaindaysConf = new ConfigInfo(ConfConstant.MONITOR_BASE_REMAINDAYS, monitorConfInfo
                        .getBaseremaindays(), ConfConstant.MONITOR_BASE_CONF, NumConstant.common_number_0, "令牌将要过期天数");
                configList.add(baseremaindaysConf);
            }
            if (StrTool.strNotNull(monitorConfInfo.getBaseunbindlower())) {
                ConfigInfo baseunbindlowerConf = new ConfigInfo(ConfConstant.MONITOR_BASE_UNBINDLOWER, monitorConfInfo
                        .getBaseunbindlower(), ConfConstant.MONITOR_BASE_CONF, NumConstant.common_number_0, "未绑定的令牌比例");
                configList.add(baseunbindlowerConf);
            }
            if (StrTool.strNotNull(monitorConfInfo.getBasesyncupper())) {
                ConfigInfo basesyncupperConf = new ConfigInfo(ConfConstant.MONITOR_BASE_SYNCUPPER, monitorConfInfo
                        .getBasesyncupper(), ConfConstant.MONITOR_BASE_CONF, NumConstant.common_number_0,
                        "1小时内令牌同步达到多少比例（次数/用户数）");
                configList.add(basesyncupperConf);
            }
            if (StrTool.strNotNull(monitorConfInfo.getBasetimeinterval())) {
                ConfigInfo basetimeintervalConf = new ConfigInfo(ConfConstant.MONITOR_BASE_TIMEINTERVAL,
                        monitorConfInfo.getBasetimeinterval(), ConfConstant.MONITOR_BASE_CONF,
                        NumConstant.common_number_0, "定时器执行的时间间隔 单位分钟");
                configList.add(basetimeintervalConf);
            }

            // 设备预警配置
            if (StrTool.strNotNull(monitorConfInfo.getSbenabled())) {
                ConfigInfo sbenabledConf = new ConfigInfo(ConfConstant.MONITOR_SB_ENABLED, monitorConfInfo
                        .getSbenabled(), ConfConstant.MONITOR_SB_CONF, NumConstant.common_number_0, "是否启用预警");
                configList.add(sbenabledConf);
            }
            if (StrTool.strNotNull(monitorConfInfo.getSbsendtype())) {
                ConfigInfo sbsendtypeConf = new ConfigInfo(ConfConstant.MONITOR_SB_SEND_TYPE, monitorConfInfo
                        .getSbsendtype(), ConfConstant.MONITOR_SB_CONF, NumConstant.common_number_0, "预警方式");
                configList.add(sbsendtypeConf);
            }
            if (StrTool.strNotNull(monitorConfInfo.getSbcpuupper())) {
                ConfigInfo sbcpuupperConf = new ConfigInfo(ConfConstant.MONITOR_SB_CPUUPPER, monitorConfInfo
                        .getSbcpuupper(), ConfConstant.MONITOR_SB_CONF, NumConstant.common_number_0, "CPU的上限阀值");
                configList.add(sbcpuupperConf);
            }
            if (StrTool.strNotNull(monitorConfInfo.getSbdiskupper())) {
                ConfigInfo sbdiskupperConf = new ConfigInfo(ConfConstant.MONITOR_SB_DISKUPPER, monitorConfInfo
                        .getSbdiskupper(), ConfConstant.MONITOR_SB_CONF, NumConstant.common_number_0, "磁盘的上限阀值");
                configList.add(sbdiskupperConf);
            }
            if (StrTool.strNotNull(monitorConfInfo.getSbmemupper())) {
                ConfigInfo sbmemupperConf = new ConfigInfo(ConfConstant.MONITOR_SB_MEMUPPER, monitorConfInfo
                        .getSbmemupper(), ConfConstant.MONITOR_SB_CONF, NumConstant.common_number_0, "内存空间占用率的上限阀值");
                configList.add(sbmemupperConf);
            }
            if (StrTool.strNotNull(monitorConfInfo.getSbtimeinterval())) {
                ConfigInfo sbtimeintervalConf = new ConfigInfo(ConfConstant.MONITOR_SB_TIMEINTERVAL, monitorConfInfo
                        .getSbtimeinterval(), ConfConstant.MONITOR_SB_CONF, NumConstant.common_number_0,
                        "定时器执行的时间间隔 单位分钟");
                configList.add(sbtimeintervalConf);
            }

            // 应用预警配置
            if (StrTool.strNotNull(monitorConfInfo.getAppenabled())) {
                ConfigInfo appenabledConf = new ConfigInfo(ConfConstant.MONITOR_APP_ENABLED, monitorConfInfo
                        .getAppenabled(), ConfConstant.MONITOR_APP_CONF, NumConstant.common_number_0, "是否启用预警");
                configList.add(appenabledConf);
            }
            if (StrTool.strNotNull(monitorConfInfo.getAppsendtype())) {
                ConfigInfo appsendtypeConf = new ConfigInfo(ConfConstant.MONITOR_APP_SEND_TYPE, monitorConfInfo
                        .getAppsendtype(), ConfConstant.MONITOR_APP_CONF, NumConstant.common_number_0, "预警方式");
                configList.add(appsendtypeConf);
            }
            if (StrTool.strNotNull(monitorConfInfo.getApptimeinterval())) {
                ConfigInfo apptimeintervalConf = new ConfigInfo(ConfConstant.MONITOR_APP_TIMEINTERVAL, monitorConfInfo
                        .getApptimeinterval(), ConfConstant.MONITOR_APP_CONF, NumConstant.common_number_0,
                        "定时器执行的时间间隔 单位分钟");
                configList.add(apptimeintervalConf);
            }

        }
        return configList;
    }

    /**
     * @return the baseenabled
     */
    public String getBaseenabled() {
        return baseenabled;
    }

    /**
     * @param baseenabled the baseenabled to set
     */
    public void setBaseenabled(String baseenabled) {
        this.baseenabled = baseenabled;
    }

    /**
     * @return the basesendtype
     */
    public String getBasesendtype() {
        return basesendtype;
    }

    /**
     * @param basesendtype the basesendtype to set
     */
    public void setBasesendtype(String basesendtype) {
        this.basesendtype = basesendtype;
    }

    /**
     * @return the baseremaindays
     */
    public String getBaseremaindays() {
        return baseremaindays;
    }

    /**
     * @param baseremaindays the baseremaindays to set
     */
    public void setBaseremaindays(String baseremaindays) {
        this.baseremaindays = baseremaindays;
    }

    /**
     * @return the baseunbindlower
     */
    public String getBaseunbindlower() {
        return baseunbindlower;
    }

    /**
     * @param baseunbindlower the baseunbindlower to set
     */
    public void setBaseunbindlower(String baseunbindlower) {
        this.baseunbindlower = baseunbindlower;
    }

    /**
     * @return the basesyncupper
     */
    public String getBasesyncupper() {
        return basesyncupper;
    }

    /**
     * @param basesyncupper the basesyncupper to set
     */
    public void setBasesyncupper(String basesyncupper) {
        this.basesyncupper = basesyncupper;
    }

    /**
     * @return the basetimeinterval
     */
    public String getBasetimeinterval() {
        return basetimeinterval;
    }

    /**
     * @param basetimeinterval the basetimeinterval to set
     */
    public void setBasetimeinterval(String basetimeinterval) {
        this.basetimeinterval = basetimeinterval;
    }

    /**
     * @return the sbenabled
     */
    public String getSbenabled() {
        return sbenabled;
    }

    /**
     * @param sbenabled the sbenabled to set
     */
    public void setSbenabled(String sbenabled) {
        this.sbenabled = sbenabled;
    }

    /**
     * @return the sbsendtype
     */
    public String getSbsendtype() {
        return sbsendtype;
    }

    /**
     * @param sbsendtype the sbsendtype to set
     */
    public void setSbsendtype(String sbsendtype) {
        this.sbsendtype = sbsendtype;
    }

    /**
     * @return the sbcpuupper
     */
    public String getSbcpuupper() {
        return sbcpuupper;
    }

    /**
     * @param sbcpuupper the sbcpuupper to set
     */
    public void setSbcpuupper(String sbcpuupper) {
        this.sbcpuupper = sbcpuupper;
    }

    /**
     * @return the sbdiskupper
     */
    public String getSbdiskupper() {
        return sbdiskupper;
    }

    /**
     * @param sbdiskupper the sbdiskupper to set
     */
    public void setSbdiskupper(String sbdiskupper) {
        this.sbdiskupper = sbdiskupper;
    }

    /**
     * @return the sbmemupper
     */
    public String getSbmemupper() {
        return sbmemupper;
    }

    /**
     * @param sbmemupper the sbmemupper to set
     */
    public void setSbmemupper(String sbmemupper) {
        this.sbmemupper = sbmemupper;
    }

    /**
     * @return the sbtimeinterval
     */
    public String getSbtimeinterval() {
        return sbtimeinterval;
    }

    /**
     * @param sbtimeinterval the sbtimeinterval to set
     */
    public void setSbtimeinterval(String sbtimeinterval) {
        this.sbtimeinterval = sbtimeinterval;
    }

    /**
     * @return the appenabled
     */
    public String getAppenabled() {
        return appenabled;
    }

    /**
     * @param appenabled the appenabled to set
     */
    public void setAppenabled(String appenabled) {
        this.appenabled = appenabled;
    }

    /**
     * @return the appsendtype
     */
    public String getAppsendtype() {
        return appsendtype;
    }

    /**
     * @param appsendtype the appsendtype to set
     */
    public void setAppsendtype(String appsendtype) {
        this.appsendtype = appsendtype;
    }

    /**
     * @return the apptimeinterval
     */
    public String getApptimeinterval() {
        return apptimeinterval;
    }

    /**
     * @param apptimeinterval the apptimeinterval to set
     */
    public void setApptimeinterval(String apptimeinterval) {
        this.apptimeinterval = apptimeinterval;
    }

    /**
     * @return the baserecvusers
     */
    public String[] getBaserecvusers() {
        return baserecvusers == null ? new String[] {} : baserecvusers;
    }

    /**
     * @param baserecvusers the baserecvusers to set
     */
    public void setBaserecvusers(String[] baserecvusers) {
        this.baserecvusers = baserecvusers;
    }

    /**
     * @return the sbrecvusers
     */
    public String[] getSbrecvusers() {
        return sbrecvusers == null ? new String[] {} : sbrecvusers;
    }

    /**
     * @param sbrecvusers the sbrecvusers to set
     */
    public void setSbrecvusers(String[] sbrecvusers) {
        this.sbrecvusers = sbrecvusers;
    }

    /**
     * @return the apprecvusers
     */
    public String[] getApprecvusers() {
        return apprecvusers == null ? new String[] {} : apprecvusers;
    }

    /**
     * @param apprecvusers the apprecvusers to set
     */
    public void setApprecvusers(String[] apprecvusers) {
        this.apprecvusers = apprecvusers;
    }

    /**
     * @return the oldbaserecvusers
     */
    public String[] getOldbaserecvusers() {
        return oldbaserecvusers;
    }

    /**
     * @param oldbaserecvusers the oldbaserecvusers to set
     */
    public void setOldbaserecvusers(String[] oldbaserecvusers) {
        this.oldbaserecvusers = oldbaserecvusers;
    }

    /**
     * @return the oldsbrecvusers
     */
    public String[] getOldsbrecvusers() {
        return oldsbrecvusers;
    }

    /**
     * @param oldsbrecvusers the oldsbrecvusers to set
     */
    public void setOldsbrecvusers(String[] oldsbrecvusers) {
        this.oldsbrecvusers = oldsbrecvusers;
    }

    /**
     * @return the oldapprecvusers
     */
    public String[] getOldapprecvusers() {
        return oldapprecvusers;
    }

    /**
     * @param oldapprecvusers the oldapprecvusers to set
     */
    public void setOldapprecvusers(String[] oldapprecvusers) {
        this.oldapprecvusers = oldapprecvusers;
    }

}
