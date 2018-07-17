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
 * 其它公共配置实体信息
 *
 * @Date in Jul 26, 2013,6:09:43 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class CommonConfInfo extends BaseEntity {

    private String sessioneffectivelytime = null; //系统会话有效时间，分钟
    private String loglevel = null; //系统日志级别，0关键日志模式，1完整日志模式
    private String defaultsystemlanguage; //系统使用的语言设置
    private String logtimingdelete; //日志定时删除时间设置
    private String logtimingenabled; //日志定时删除是否启用
    private String logisbak = "0";// 是否备份定时删除日志 0:不备份、1：备份
    private String defaultdomainid; //系统默认域配置

    /**
     * 由配置列表获取 实体对象信息
     * @Date in Jul 27, 2013,9:43:50 AM
     * @param configList
     * @return
     */
    public static CommonConfInfo getCommInfoList(List<?> configList) {
        CommonConfInfo commInfo = new CommonConfInfo();
        Iterator<?> iter = configList.iterator();
        while (iter.hasNext()) {
            ConfigInfo config = (ConfigInfo) iter.next();
            String configName = config.getConfname();
            String configValue = config.getConfvalue();
            if (StrTool.strNotNull(configName)) {
                if (configName.equals(ConfConstant.SESSION_EFFECTIVELY_TIME)) {
                    commInfo.setSessioneffectivelytime(configValue);
                } else if (configName.equals(ConfConstant.LOG_LEVEL)) {
                    commInfo.setLoglevel(configValue);
                } else if (configName.equals(ConfConstant.LOG_TIMING_ENABLED)) {
                    commInfo.setLogtimingenabled(configValue);
                } else if (configName.equals(ConfConstant.LOG_TIMING_DELETE)) {
                    commInfo.setLogtimingdelete(configValue);
                } else if (configName.equals(ConfConstant.LOG_IS_BAK)) {
                    commInfo.setLogisbak(configValue);
                } else if (configName.equals(ConfConstant.DEFAULT_SYSTEM_LANGUAGE)) {
                    commInfo.setDefaultsystemlanguage(configValue);
                }
            }
        }
        return commInfo;
    }

    /**
     * 由配置对象获取配置列表
     */
    public static List<Object> getListByCommInfo(CommonConfInfo commInfo) {
        List<Object> configList = null;
        if (StrTool.objNotNull(commInfo)) {
            configList = new ArrayList<Object>();

            ConfigInfo sessEffTimeConf = new ConfigInfo(ConfConstant.SESSION_EFFECTIVELY_TIME, commInfo
                    .getSessioneffectivelytime(), ConfConstant.CONF_TYPE_COMMON, NumConstant.common_number_0, "");
            ConfigInfo logLevelConf = new ConfigInfo(ConfConstant.LOG_LEVEL, commInfo.getLoglevel(),
                    ConfConstant.CONF_TYPE_COMMON, NumConstant.common_number_0, "");
            ConfigInfo logTimeEnabledConf = new ConfigInfo(ConfConstant.LOG_TIMING_ENABLED, commInfo
                    .getLogtimingenabled(), ConfConstant.CONF_TYPE_COMMON, NumConstant.common_number_0, "");
            ConfigInfo logTimeDelConf = new ConfigInfo(ConfConstant.LOG_TIMING_DELETE, commInfo.getLogtimingdelete(),
                    ConfConstant.CONF_TYPE_COMMON, NumConstant.common_number_0, "");
            ConfigInfo logIsBakConf = new ConfigInfo(ConfConstant.LOG_IS_BAK, commInfo.getLogisbak(),
                    ConfConstant.CONF_TYPE_COMMON, NumConstant.common_number_0, "");
            ConfigInfo langConf = new ConfigInfo(ConfConstant.DEFAULT_SYSTEM_LANGUAGE, commInfo
                    .getDefaultsystemlanguage(), ConfConstant.CONF_TYPE_COMMON, NumConstant.common_number_0, "");

            configList.add(sessEffTimeConf);
            configList.add(logLevelConf);
            configList.add(logTimeEnabledConf);
            configList.add(logTimeDelConf);
            configList.add(logIsBakConf);
            configList.add(langConf);
        }
        return configList;
    }

    public String getSessioneffectivelytime() {
        return sessioneffectivelytime;
    }

    public void setSessioneffectivelytime(String sessioneffectivelytime) {
        this.sessioneffectivelytime = sessioneffectivelytime;
    }

    public String getLoglevel() {
        return loglevel;
    }

    public void setLoglevel(String loglevel) {
        this.loglevel = loglevel;
    }

    public String getDefaultsystemlanguage() {
        return defaultsystemlanguage;
    }

    public void setDefaultsystemlanguage(String defaultsystemlanguage) {
        this.defaultsystemlanguage = defaultsystemlanguage;
    }

    public String getLogtimingdelete() {
        return logtimingdelete;
    }

    public void setLogtimingdelete(String logtimingdelete) {
        this.logtimingdelete = logtimingdelete;
    }

    public String getLogtimingenabled() {
        return logtimingenabled;
    }

    public void setLogtimingenabled(String logtimingenabled) {
        this.logtimingenabled = logtimingenabled;
    }

    public String getDefaultdomainid() {
        return defaultdomainid;
    }

    public void setDefaultdomainid(String defaultdomainid) {
        this.defaultdomainid = defaultdomainid;
    }

    /**
     * @return the logisbak
     */
    public String getLogisbak() {
        return logisbak;
    }

    /**
     * @param logisbak the logisbak to set
     */
    public void setLogisbak(String logisbak) {
        this.logisbak = logisbak;
    }

}
