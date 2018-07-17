package com.ft.otp.util.conf;

import java.util.ArrayList;
import java.util.List;

import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.config.ConfConfig;
import com.ft.otp.manager.confinfo.config.entity.ConfigInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 说明
 * 
 * 本类的主要功能是根据OtpConf数据表中的关键值：confid、confvalue、confname、confvalueid，
 * 取得一个或一组配置项的数据
 * 
 * 本类封装了OtpConfConfig类，直接从缓存数据中获取配置数据
 *
 * @Date in Apr 28, 2012,11:26:10 AM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class ConfDataFormat {

    /**
     * 根据confType 配置类型、confValue配置值，
     * 取得配置名称 confName
     * 
     * @Date in Apr 28, 2012,12:55:50 PM
     * @param confType 配置对象类型 type
     * @param confValue 配置对象值 value
     * @return
     */
    public static String getConfName(String confType, Object confValue) {
        String confName = null;

        String key = null;
        if (StrTool.strNotNull(confType) && null != confValue) {
            key = confType + confValue;
        } else {
            return String.valueOf(confValue);
        }

        confName = ConfConfig.getConfName(key);
        if (!StrTool.strNotNull(confName)) {
            return "";
        }

        return confName;
    }

    /**
     * 根据confType 配置类型、confName配置名称，
     * 取得配置值 confValue
     * 
     * @Date in Jun 12, 2012,11:02:53 AM
     * @param confType 配置类型
     * @param confName 配置名称
     * @return
     */
    public static String getConfValue(String confType, Object confName) {
        String confVal = null;

        String key = null;
        if (StrTool.strNotNull(confType) && null != confName) {
            key = confType + "_" + confName;
        } else {
            return String.valueOf(confName);
        }

        confVal = ConfConfig.getConfValue(key);
        if (!StrTool.strNotNull(confVal)) {
            return "";
        }

        return confVal;
    }

    /**
     * 根据confType 配置类型、confName配置名称，
     * 取得配置对象信息
     * 
     * @Date in Apr 1, 2013,2:43:54 PM
     * @param confType
     * @param confName
     * @return
     */
    public static ConfigInfo getConfInfo(String confType, Object confName) {
        ConfigInfo configInfo = null;
        String key = null;
        if (StrTool.strNotNull(confType) && null != confName) {
            key = confType + "_" + confName;
        } else {
            return new ConfigInfo();
        }

        configInfo = ConfConfig.getConfInfo(key);
        if (null == configInfo) {
            return new ConfigInfo();
        }

        return configInfo;
    }

    /**
     * 根据confType 配置类型
     * 取得一组配置列表数据
     * 
     * @Date in May 7, 2012,4:41:17 PM
     * @param confType 配置类型标志
     * @return
     */
    public static List<ConfigInfo> getConfList(String confType) {
        if (!StrTool.strNotNull(confType)) {
            return new ArrayList<ConfigInfo>();
        }

        return ConfConfig.getConfList(confType);
    }

    /**
     * 获取系统管理员是否使用邮件的方式进行激活
     * 
     * @Date in Jul 3, 2013,2:36:17 PM
     * @return true 激活，false 不激活
     */
    public static boolean getSysConfEmailEnabled() {
        String confval = getConfValue(ConfConstant.CONF_TYPE_CENTER, ConfConstant.EMAIL_ACTIVATE_ENABLED);
        if (StrTool.strEquals(confval, StrConstant.common_number_1)) {
            return true;
        }

        return false;
    }

    /**
     * 判断是否为恢复数据
     * 
     * @Date in Sep 7, 2013,4:18:18 PM
     * @return
     */
    public static boolean getDbDataSrcType() {
        String confval = getConfValue(ConfConstant.CONF_TYPE_COMMON, ConfConstant.DB_DATA_CREATE_TYPE);
        if (StrTool.strEquals(confval, StrConstant.common_number_1)) {
            return true;
        }

        return false;
    }

    /**
     * 获取多语言标识
     * 
     * @Date in Sep 14, 2013,11:01:56 AM
     * @return
     */
    public static boolean getLangConfVal() {
        String confval = getConfValue(ConfConstant.CONF_TYPE_COMMON, ConfConstant.DEFAULT_SYSTEM_LANGUAGE);
        if (StrTool.strNotNull(confval)) {
            return true;
        }

        return false;
    }

}
