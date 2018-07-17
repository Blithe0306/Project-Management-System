/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.config.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.util.tool.StrTool;

/**
 * 监控预警配置
 *
 * @Date in Aug 19, 2013,2:49:43 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class MonitorConf extends BaseEntity {

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
     * 由配置列表得到MonitorConf对象
     * @Date in Aug 19, 2013,2:55:05 PM
     * @param monitorInfo
     * @param configList
     * @return
     */
    public static MonitorConf getMonitorInfoByList(List<?> configList) {
        MonitorConf monitorInfo = new MonitorConf();
        Iterator<?> iter = configList.iterator();
        while (iter.hasNext()) {
            ConfigInfo config = (ConfigInfo) iter.next();
            String configName = config.getConfname();
            String configValue = config.getConfvalue();
            String configType = config.getConftype();
            if (StrTool.strNotNull(configName)) {
                if (configType.equals(ConfConstant.MONITOR_BASE_CONF)) {
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
                }
            }
        }
        return monitorInfo;
    }

    /**
     * 由MonitorConf配置对象得到配置列表
     * @Date in Aug 19, 2013,2:54:46 PM
     * @param monitorConfInfo
     * @return
     */
    public static List<Object> getListByMonitorInfo(MonitorConf monitorConfInfo) {
        List<Object> configList = null;
        if (StrTool.objNotNull(monitorConfInfo)) {
            configList = new ArrayList<Object>();
            // 基本预警配置
            ConfigInfo baseenabledConf = new ConfigInfo(ConfConstant.MONITOR_BASE_ENABLED, monitorConfInfo
                    .getBaseenabled(), ConfConstant.MONITOR_BASE_CONF, NumConstant.common_number_0, "");
            ConfigInfo basesendtypeConf = new ConfigInfo(ConfConstant.MONITOR_BASE_SENDTYPE, monitorConfInfo
                    .getBasesendtype(), ConfConstant.MONITOR_BASE_CONF, NumConstant.common_number_0, "");
            
            ConfigInfo baseremaindaysConf = new ConfigInfo(ConfConstant.MONITOR_BASE_REMAINDAYS, monitorConfInfo
                    .getBaseremaindays(), ConfConstant.MONITOR_BASE_CONF, NumConstant.common_number_0, "");
            
            ConfigInfo baseunbindlowerConf = new ConfigInfo(ConfConstant.MONITOR_BASE_UNBINDLOWER, monitorConfInfo
                    .getBaseunbindlower(), ConfConstant.MONITOR_BASE_CONF, NumConstant.common_number_0, "");
            
            ConfigInfo basesyncupperConf = new ConfigInfo(ConfConstant.MONITOR_BASE_SYNCUPPER, monitorConfInfo
                    .getBasesyncupper(), ConfConstant.MONITOR_BASE_CONF, NumConstant.common_number_0, "");
            
            ConfigInfo basetimeintervalConf = new ConfigInfo(ConfConstant.MONITOR_BASE_TIMEINTERVAL, monitorConfInfo
                    .getBasetimeinterval(), ConfConstant.MONITOR_BASE_CONF, NumConstant.common_number_0, "");
            
            configList.add(basesyncupperConf);
            configList.add(baseunbindlowerConf);
            configList.add(baseremaindaysConf);
            configList.add(basesendtypeConf);
            configList.add(baseenabledConf);
            configList.add(basetimeintervalConf);

        }
        return configList;
    }

    public String getBaseenabled() {
        return baseenabled;
    }

    public void setBaseenabled(String baseenabled) {
        this.baseenabled = baseenabled;
    }

    public String getBasesendtype() {
        return basesendtype;
    }

    public void setBasesendtype(String basesendtype) {
        this.basesendtype = basesendtype;
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

    public String getBaseremaindays() {
        return baseremaindays;
    }

    public void setBaseremaindays(String baseremaindays) {
        this.baseremaindays = baseremaindays;
    }

    public String getBaseunbindlower() {
        return baseunbindlower;
    }

    public void setBaseunbindlower(String baseunbindlower) {
        this.baseunbindlower = baseunbindlower;
    }

    public String getBasesyncupper() {
        return basesyncupper;
    }

    public void setBasesyncupper(String basesyncupper) {
        this.basesyncupper = basesyncupper;
    }

    public String getBasetimeinterval() {
        return basetimeinterval;
    }

    public void setBasetimeinterval(String basetimeinterval) {
        this.basetimeinterval = basetimeinterval;
    }

}
