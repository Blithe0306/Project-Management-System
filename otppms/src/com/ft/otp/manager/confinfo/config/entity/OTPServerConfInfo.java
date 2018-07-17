/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.config.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.util.tool.StrTool;

/**
 * OTP Server配置实体信息
 *
 * @Date in Mar 11, 2013,3:07:44 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class OTPServerConfInfo {

    private String cacheclearpolicy; //指定缓存清理策略实现类，0: FIFO，1:LRU，2:LFU
    private String cachetunersleeptime;//隔多长时间执行缓存的优化和清理等，单位秒
    private String cachemaxsize; //缓存中可以存储的对象最大数量
    private String cacheexpiretime;//最大空闲会话超时时间，超时的会话会被清除，单位秒
    
    /**
     * 有配置列表得到OTP Server配置对象
     * @Date in Mar 11, 2013,3:40:42 PM
     * @param configList
     * @return
     */
    public static OTPServerConfInfo getOTPServerInfoByList(List<?> configList) {
        OTPServerConfInfo otpserverInfo = new OTPServerConfInfo();
        Iterator<?> iter = configList.iterator();
        while(iter.hasNext()) {
            ConfigInfo config = (ConfigInfo) iter.next();
            String configName = config.getConfname();
            String configValue = config.getConfvalue();
            if (StrTool.strNotNull(configName)) {
                if (configName.equals(ConfConstant.CACHE_CLEAR_POLICY)) {
                    otpserverInfo.setCacheclearpolicy(configValue);
                } else if (configName.equals(ConfConstant.CACHE_TUNER_SLEEPTIME)) {
                    otpserverInfo.setCachetunersleeptime(configValue);
                } else if (configName.equals(ConfConstant.CACHE_MAXSIZE)) {
                    otpserverInfo.setCachemaxsize(configValue);
                } else if (configName.equals(ConfConstant.CACHE_EXPIRE_TIME)) {
                    otpserverInfo.setCacheexpiretime(configValue);
                } 
            }
        }
        return otpserverInfo;
    }
    
    /**
     * 由OTPServerConfInfo 配置对象得到配置列表
     * @Date in Mar 11, 2013,3:53:03 PM
     * @param otpserverInfo
     * @return
     */
    public static List<Object> getListByOTPServerInfo(OTPServerConfInfo otpserverInfo) {
        List<Object> configList = null;
        if (StrTool.objNotNull(otpserverInfo)) {
            configList = new ArrayList<Object>();
            
            ConfigInfo clearpolicyConf = new ConfigInfo(ConfConstant.CACHE_CLEAR_POLICY, otpserverInfo.getCacheclearpolicy(), 
                    ConfConstant.OTPSERVER_CONFIG, NumConstant.common_number_0, "");
            ConfigInfo tunersleeptimeConf = new ConfigInfo(ConfConstant.CACHE_TUNER_SLEEPTIME, otpserverInfo.getCachetunersleeptime(), 
                    ConfConstant.OTPSERVER_CONFIG, NumConstant.common_number_0, "");
            ConfigInfo maxsizeConf = new ConfigInfo(ConfConstant.CACHE_MAXSIZE, otpserverInfo.getCachemaxsize(), 
                    ConfConstant.OTPSERVER_CONFIG, NumConstant.common_number_0, "");
            ConfigInfo expiretimeConf = new ConfigInfo(ConfConstant.CACHE_EXPIRE_TIME, otpserverInfo.getCacheexpiretime(), 
                    ConfConstant.OTPSERVER_CONFIG, NumConstant.common_number_0, "");
                       
            configList.add(clearpolicyConf);
            configList.add(tunersleeptimeConf);
            configList.add(maxsizeConf);
            configList.add(expiretimeConf);
        }
        return configList;
    }
    
    public String getCacheclearpolicy() {
        return cacheclearpolicy;
    }
    public void setCacheclearpolicy(String cacheclearpolicy) {
        this.cacheclearpolicy = cacheclearpolicy;
    }
    public String getCachetunersleeptime() {
        return cachetunersleeptime;
    }
    public void setCachetunersleeptime(String cachetunersleeptime) {
        this.cachetunersleeptime = cachetunersleeptime;
    }
    public String getCachemaxsize() {
        return cachemaxsize;
    }
    public void setCachemaxsize(String cachemaxsize) {
        this.cachemaxsize = cachemaxsize;
    }
    public String getCacheexpiretime() {
        return cacheexpiretime;
    }
    public void setCacheexpiretime(String cacheexpiretime) {
        this.cacheexpiretime = cacheexpiretime;
    }

}
