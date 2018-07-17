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
 * 认证基本配置实体信息
 *
 * @Date in Jul 27, 2013,11:20:17 AM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class AuthConfInfo extends BaseEntity {

    private String hotpauthwnd; //HOTP令牌认证窗口
    private String totpauthwnd; //TOTP令牌认证窗口
    private String hotpadjustwnd; //HOTP令牌调整窗口
    private String totpadjustwnd; //TOTP令牌调整窗口
    private String hotpsyncwnd; //HOTP令牌同步窗口
    private String totpsyncwnd; //TOTP令牌同步窗口
    private String wndadjustperiod; //窗口调整周期
    private String wndadjustmode; //窗口调整模式，0:不调整；1:超过窗口调整周期进行调整，不累计；2: 超过窗口调整周期进行调整，连续累计
    private String retryotptimeinterval;//允许重放的时间间隔（0 不允许重放、其它：秒数，表示在多少秒内允许重放）
    private String templockretry; //用户或令牌认证失败最大重试次数，-1表示无限制。
    private String templockexpire; //用户或令牌被锁定多少秒后自动解锁，界面上处理时可不用秒，用分钟或小时，秒不便于使用者计算。
    private String maxretry; //如果连续多次达到用户或令牌认证失败最大重试次数导致用户或令牌多次锁定(自动解锁后继续重试)，
                            //重试次数累计达到最大许可的重试次数，则用户或令牌永久锁定，永久锁定解锁需要寻求管理员。
    private String propeapadbled = null; //是否禁用PEAP，y是，n否
    private String propeaplocked = null; //是否锁定用户，y是，n否
    private String oper; // 操作类型
    /** 
     * 获取认证配置对象
     */
    public static AuthConfInfo getAuthInfoByList(List<?> configList) {
        AuthConfInfo authInfo = new AuthConfInfo();
        Iterator<?> iter = configList.iterator();
        while (iter.hasNext()) {
            ConfigInfo config = (ConfigInfo) iter.next();
            String configName = config.getConfname();
            String configValue = config.getConfvalue();
            if (StrTool.strNotNull(configName)) {
                if (configName.equals(ConfConstant.CORE_HOTP_AUTH_WND)) {
                    authInfo.setHotpauthwnd(configValue);
                } else if (configName.equals(ConfConstant.CORE_TOTP_AUTH_WND)) {
                    authInfo.setTotpauthwnd(configValue);
                } else if (configName.equals(ConfConstant.CORE_HOTP_ADJUST_WND)) {
                    authInfo.setHotpadjustwnd(configValue);
                } else if (configName.equals(ConfConstant.CORE_tOTP_ADJUST_WND)) {
                    authInfo.setTotpadjustwnd(configValue);
                } else if (configName.equals(ConfConstant.CORE_HOTP_SYNC_WND)) {
                    authInfo.setHotpsyncwnd(configValue);
                } else if (configName.equals(ConfConstant.CORE_TOTP_SYNC_WND)) {
                    authInfo.setTotpsyncwnd(configValue);
                } else if (configName.equals(ConfConstant.CORE_WND_ADJUST_PERIOD)) {
                    authInfo.setWndadjustperiod(StrTool.intToString(StrTool.parseInt(configValue) / 7 / 24 / 60 / 60));
                } else if (configName.equals(ConfConstant.CORE_WND_ADJUST_MODE)) {
                    authInfo.setWndadjustmode(configValue);
                } else if (configName.equals(ConfConstant.CORE_RETRY_OTP_TIMEINTERVAL)) {
                    authInfo.setRetryotptimeinterval(configValue);
                } else if (configName.equals(ConfConstant.CORE_TEMP_LOCK_RETRY)) {
                    authInfo.setTemplockretry(configValue);
                } else if (configName.equals(ConfConstant.CORE_TEMP_LOCK_EXPIRE)) {
                    authInfo.setTemplockexpire(StrTool.intToString(StrTool.parseInt(configValue) / 60));
                } else if (configName.equals(ConfConstant.CORE_MAX_RETRY)) {
                    authInfo.setMaxretry(configValue);
                } else if (configName.equals(ConfConstant.RADIUS_PEAP_ENABLED)) {
                	authInfo.setPropeapadbled(configValue);
                } else if (configName.equals(ConfConstant.RADIUS_PEAP_LOCKED)) {
                	authInfo.setPropeaplocked(configValue);
                }
            }
        }
        return authInfo;
    }
    
    /**
     * 由配置对象获取配置列表
     * @Date in Jul 27, 2013,1:48:08 PM
     * @param authInfo
     * @return
     */
    public static List<Object> getListByAuthInfo(AuthConfInfo authInfo, String oper) {
        List<Object> configList = null;
        if (StrTool.objNotNull(authInfo)) {
            configList = new ArrayList<Object>();
            // 基本配置
            if (StrTool.strEquals(oper, "initconf")) {
            	ConfigInfo hotpauthwndConf = new ConfigInfo(ConfConstant.CORE_HOTP_AUTH_WND, authInfo.getHotpauthwnd(),
                        ConfConstant.CONF_TYPE_AUTH, NumConstant.common_number_0, "");
                ConfigInfo totpauthwndConf = new ConfigInfo(ConfConstant.CORE_TOTP_AUTH_WND, authInfo.getTotpauthwnd(),
                        ConfConstant.CONF_TYPE_AUTH, NumConstant.common_number_0, "");
                ConfigInfo hotpsyncwndConf = new ConfigInfo(ConfConstant.CORE_HOTP_SYNC_WND, authInfo.getHotpsyncwnd(),
                        ConfConstant.CONF_TYPE_AUTH, NumConstant.common_number_0, "");
                ConfigInfo totpsyncwndConf = new ConfigInfo(ConfConstant.CORE_TOTP_SYNC_WND, authInfo.getTotpsyncwnd(),
                        ConfConstant.CONF_TYPE_AUTH, NumConstant.common_number_0, "");
                ConfigInfo hotpadjustwndConf = new ConfigInfo(ConfConstant.CORE_HOTP_ADJUST_WND, authInfo.getHotpadjustwnd(), 
                        ConfConstant.CONF_TYPE_AUTH, NumConstant.common_number_0, "");
                ConfigInfo totpadjustwndConf = new ConfigInfo(ConfConstant.CORE_tOTP_ADJUST_WND, authInfo.getTotpadjustwnd(),
                        ConfConstant.CONF_TYPE_AUTH, NumConstant.common_number_0, "");
                ConfigInfo wndadjustmodeConf = new ConfigInfo(ConfConstant.CORE_WND_ADJUST_MODE, authInfo.getWndadjustmode(),
                        ConfConstant.CONF_TYPE_AUTH, NumConstant.common_number_0, "");
                ConfigInfo wndadjustperiodConf = new ConfigInfo(ConfConstant.CORE_WND_ADJUST_PERIOD, 
                        StrTool.intToString(StrTool.parseInt(authInfo.getWndadjustperiod()) * 7 * 24 * 60 * 60),
                        ConfConstant.CONF_TYPE_AUTH, NumConstant.common_number_0, "");
                ConfigInfo retryotptimeConf = new ConfigInfo(ConfConstant.CORE_RETRY_OTP_TIMEINTERVAL, authInfo.getRetryotptimeinterval(),
                        ConfConstant.CONF_TYPE_AUTH, NumConstant.common_number_0, "");
                ConfigInfo templockretryConf = new ConfigInfo(ConfConstant.CORE_TEMP_LOCK_RETRY, authInfo.getTemplockretry(),
                         ConfConstant.CONF_TYPE_AUTH, NumConstant.common_number_0, "");
                ConfigInfo templockexpireConf = new ConfigInfo(ConfConstant.CORE_TEMP_LOCK_EXPIRE, 
                        StrTool.intToString(StrTool.parseInt(authInfo.getTemplockexpire()) * 60), 
                        ConfConstant.CONF_TYPE_AUTH, NumConstant.common_number_0, "");
                ConfigInfo maxretryConf = new ConfigInfo(ConfConstant.CORE_MAX_RETRY, authInfo.getMaxretry(),
                        ConfConstant.CONF_TYPE_AUTH, NumConstant.common_number_0, "");
                configList.add(hotpauthwndConf);
                configList.add(totpauthwndConf);
                configList.add(hotpsyncwndConf);
                configList.add(totpsyncwndConf);
                configList.add(hotpadjustwndConf);
                configList.add(totpadjustwndConf);
                configList.add(wndadjustmodeConf);
                configList.add(wndadjustperiodConf);
                configList.add(retryotptimeConf);
                configList.add(templockretryConf);
                configList.add(maxretryConf);
                configList.add(templockexpireConf);
            }else if(StrTool.strEquals(oper, "initpeap")){ // 无线路由安全配置
            	ConfigInfo propeapadbledConf = new ConfigInfo(ConfConstant.RADIUS_PEAP_ENABLED, authInfo.getPropeapadbled(), 
                        ConfConstant.CONF_TYPE_AUTH, NumConstant.common_number_0, "");
                ConfigInfo propeaplockedConf = new ConfigInfo(ConfConstant.RADIUS_PEAP_LOCKED, authInfo.getPropeaplocked(), 
                        ConfConstant.CONF_TYPE_AUTH, NumConstant.common_number_0, "");
            	configList.add(propeapadbledConf);
                configList.add(propeaplockedConf);
            }
        }
        
        return configList;
    }
    
    public String getPropeaplocked() {
		return propeaplocked;
	}

	public void setPropeaplocked(String propeaplocked) {
		this.propeaplocked = propeaplocked;
	}

	public String getPropeapadbled() {
		return propeapadbled;
	}

	public void setPropeapadbled(String propeapadbled) {
		this.propeapadbled = propeapadbled;
	}

	public String getHotpauthwnd() {
        return hotpauthwnd;
    }
    public void setHotpauthwnd(String hotpauthwnd) {
        this.hotpauthwnd = hotpauthwnd;
    }
    public String getTotpauthwnd() {
        return totpauthwnd;
    }
    public void setTotpauthwnd(String totpauthwnd) {
        this.totpauthwnd = totpauthwnd;
    }
    public String getHotpadjustwnd() {
        return hotpadjustwnd;
    }
    public void setHotpadjustwnd(String hotpadjustwnd) {
        this.hotpadjustwnd = hotpadjustwnd;
    }
    public String getTotpadjustwnd() {
        return totpadjustwnd;
    }
    public void setTotpadjustwnd(String totpadjustwnd) {
        this.totpadjustwnd = totpadjustwnd;
    }
    public String getHotpsyncwnd() {
        return hotpsyncwnd;
    }
    public void setHotpsyncwnd(String hotpsyncwnd) {
        this.hotpsyncwnd = hotpsyncwnd;
    }
    public String getTotpsyncwnd() {
        return totpsyncwnd;
    }
    public void setTotpsyncwnd(String totpsyncwnd) {
        this.totpsyncwnd = totpsyncwnd;
    }
    public String getWndadjustperiod() {
        return wndadjustperiod;
    }
    public void setWndadjustperiod(String wndadjustperiod) {
        this.wndadjustperiod = wndadjustperiod;
    }
    public String getWndadjustmode() {
        return wndadjustmode;
    }
    public void setWndadjustmode(String wndadjustmode) {
        this.wndadjustmode = wndadjustmode;
    }
    public String getRetryotptimeinterval() {
        return retryotptimeinterval;
    }
    public void setRetryotptimeinterval(String retryotptimeinterval) {
        this.retryotptimeinterval = retryotptimeinterval;
    }
    public String getTemplockretry() {
        return templockretry;
    }
    public void setTemplockretry(String templockretry) {
        this.templockretry = templockretry;
    }
    public String getTemplockexpire() {
        return templockexpire;
    }
    public void setTemplockexpire(String templockexpire) {
        this.templockexpire = templockexpire;
    }
    public String getMaxretry() {
        return maxretry;
    }
    public void setMaxretry(String maxretry) {
        this.maxretry = maxretry;
    }

	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}
}
