/**
 *Administrator
 */
package com.ft.otp.common.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.common.soap.code.SoapAttribute;
import com.ft.otp.common.soap.help.SyncHelper;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.confinfo.config.entity.ConfigInfo;
import com.ft.otp.manager.confinfo.config.service.IConfigInfoServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 系统配置管理数据初始化
 *
 * @Date in Mar 2, 2013,2:32:51 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class ConfConfig {

    private Logger logger = Logger.getLogger(ConfConfig.class);

    /**
     * key = confType value = 所有为confType(比如：productType) 的一个List集合
     */
    private static Map<String, List<ConfigInfo>> confObjectsMap;
    /**
     * key = confType + confValue; value = confName
     */
    private static Map<Object, Object> confNameMap;
    /**
     * key = confType +"_"+ confName; value = confValue
     */
    private static Map<Object, Object> confValueMap;
    /**
     * key = confType +"_"+ confName; value = ConfigInfo
     */
    private static Map<Object, Object> confMap;

    private static ConfConfig config = null;
    //配置服务接口
    private IConfigInfoServ confInfoServ;

    private ConfConfig() {
        confNameMap = new TreeMap<Object, Object>();
        confObjectsMap = new TreeMap<String, List<ConfigInfo>>();
        confValueMap = new TreeMap<Object, Object>();
        confMap = new TreeMap<Object, Object>();

        confInfoServ = (IConfigInfoServ) AppContextMgr.getObject("confInfoServ");
        try {
            List<?> confList = confInfoServ.queryConfInfo(new ConfigInfo(), new PageArgument());
            Iterator<?> it = confList.iterator();
            List<ConfigInfo> objList = null;
            while (it.hasNext()) {
                ConfigInfo conf = (ConfigInfo) it.next();
                String confName = conf.getConfname();
                String confType = conf.getConftype();
                String confValue = conf.getConfvalue();
                confNameMap.put(confType + confValue, confName);
                confValueMap.put(confType + "_" + confName, confValue);
                confMap.put(confType + "_" + confName, conf);
                if (null == confObjectsMap.get(confType)) {
                    objList = new ArrayList<ConfigInfo>();
                    objList.add(conf);
                    confObjectsMap.put(confType, objList);
                } else {
                    objList = confObjectsMap.get(confType);
                    objList.add(conf);
                    confObjectsMap.put(confType, objList);
                }
            }
        } catch (BaseException ex) {
            logger.error("Load the otpconf Table info Failure!", ex);
        }
    }

    public static ConfConfig loadConfConfig() {
        if (config != null) {
            return config;
        }
        synchronized (ConfConfig.class) {
            if (config == null) {
                config = new ConfConfig();
            }
            return config;
        }
    }

    /**
     * 根据key取得Value
     * @Date in Mar 30, 2010,4:26:51 PM
     * @param key
     * @return
     */
    public static String getConfName(String key) {
        if (!StrTool.mapNotNull(confNameMap)) {
            return "";
        }

        return (String) confNameMap.get(key);
    }

    /**
     * 根据key取得Value
     * @Date in Jun 12, 2012,11:00:31 AM
     * @param key
     * @return
     */
    public static String getConfValue(String key) {
        if (!StrTool.mapNotNull(confValueMap)) {
            return "";
        }

        return (String) confValueMap.get(key);
    }

    /**
     * 根据key 取得value 
     * 方法说明
     * @Date in Mar 6, 2013,5:09:04 PM
     * @param key
     * @return
     */
    public static ConfigInfo getConfInfo(String key) {
        if (!StrTool.mapNotNull(confMap)) {
            return null;
        }
        return (ConfigInfo) confMap.get(key);
    }

    /**
     * 根据key取得Value "ConfigInfo List"
     * @Date in Apr 28, 2012,10:50:37 AM
     * @param key
     * @return
     */
    public static List<ConfigInfo> getConfList(String key) {
        if (!StrTool.mapNotNull(confObjectsMap)) {
            return null;
        }

        return confObjectsMap.get(key);
    }

    /**
     * 清空Map，重置config
     * @Date in Mar 30, 2010,4:27:29 PM
     */
    public static void clear() {
        if (null != config) {
            confNameMap.clear();
            confMap.clear();
            confObjectsMap.clear();
            confValueMap.clear();
            config = null;
        }
    }

    /**
     * 重新加载otpconf表数据
     * 
     * @Date in May 17, 2012,3:19:45 PM
     */
    public static void reLoad() {
        clear();
        loadConfConfig();

        SyncHelper.replaceConf(SoapAttribute.RECONFIG_TYPE_10003);
    }

}
