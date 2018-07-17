package com.ft.otp.manager.login.entity;

import java.util.Map;

import com.ft.otp.common.ConfConstant;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.StrTool;

/**
 * 
 * 在线用户实体类
 * 存放于session中用户信息
 *
 * @Date in Apr 18, 2011,1:18:49 PM
 *
 * @author TBM
 */
public class LinkUser {

    private String userId; //用户名
    private String remoteAddr; //客户端IP 
    private long loginTime; //登录时间(秒) 
    private String sessionId; //Session ID  
    private String language; //管理员选择的语言
    private String roleName; //角色名称

    private Map<String, Object> permMap = null; //管理员权限集合String permCode,RolePerm obj
    //权限URL集合，用户可访问的URL，用于访问业务Action时链接的检查
    private Map<String, String> urlMap = null;

    private int percent; //进度条百分比
    private int lastPercent; //上一次的进度条百分比记录

    /**
     * 管理员首页显示信息
     */
    private String realName;//管理员名称
    private String oldLoginTime;//上一次登录时间
    private int logincnt;//累计登录次数
    private int pwdUseDays;//静态密码已使用天数
    private int isWarnUpdataPwd;//是否提示管理员修改密码 0 不提示、1 提示
    private String roleNameLink;//角色链接

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    /**
     * @return the loginTime
     */
    public long getLoginTime() {
        return loginTime;
    }

    /**
     * @param loginTime the loginTime to set
     */
    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the roleName
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * @param roleName the roleName to set
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * @return the percent
     */
    public int getPercent() {
        return percent;
    }

    /**
     * @param percent the percent to set
     */
    public void setPercent(int percent) {
        this.percent = percent;
    }

    /**
     * @return the permMap
     */
    public Map<String, Object> getPermMap() {
        return permMap;
    }

    /**
     * @param permMap the permMap to set
     */
    public void setPermMap(Map<String, Object> permMap) {
        this.permMap = permMap;
    }

    /**
     * @return the lastPercent
     */
    public int getLastPercent() {
        return lastPercent;
    }

    /**
     * @param lastPercent the lastPercent to set
     */
    public void setLastPercent(int lastPercent) {
        this.lastPercent = lastPercent;
    }

    /**
     * @return the realName
     */
    public String getRealName() {
        if (StrTool.strEquals(realName, "")) {
            realName = userId;
        }
        return realName;
    }

    /**
     * @param realName the realName to set
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * @return the oldLoginTime
     */
    public String getOldLoginTime() {
        return oldLoginTime;
    }

    /**
     * @param oldLoginTime the oldLoginTime to set
     */
    public void setOldLoginTime(String oldLoginTime) {
        this.oldLoginTime = oldLoginTime;
    }

    /**
     * @return the logincnt
     */
    public int getLogincnt() {
        return logincnt;
    }

    /**
     * @param logincnt the logincnt to set
     */
    public void setLogincnt(int logincnt) {
        this.logincnt = logincnt;
    }

    /**
     * @return the pwdUseDays
     */
    public int getPwdUseDays() {
        return pwdUseDays;
    }

    /**
     * @param pwdUseDays the pwdUseDays to set
     */
    public void setPwdUseDays(int pwdUseDays) {
        if (pwdUseDays != 0) {
            // 计算到当前时间的天数
            int times = StrTool.timeSecond() - pwdUseDays;
            // 差值utc秒数
            pwdUseDays = times / (24 * 60 * 60);
        }
        this.pwdUseDays = pwdUseDays;
    }

    /**
     * @return the roleNameLink
     */
    public String getRoleNameLink() {
        return roleNameLink;
    }

    /**
     * @param roleNameLink the roleNameLink to set
     */
    public void setRoleNameLink(String roleNameLink) {
        this.roleNameLink = roleNameLink;
    }

    /**
     * @return the isWarnUpdataPwd
     */
    public int getIsWarnUpdataPwd() {
        return isWarnUpdataPwd;
    }

    /**
     * @param isWarnUpdataPwd the isWarnUpdataPwd to set
     */
    public void setIsWarnUpdataPwd(int isWarnUpdataPwd) {
        String confVal = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_CENTER, ConfConstant.PASSWD_UPDATE_PERIOD);
        int period = 0;
        if (StrTool.strNotNull(confVal)) {
            period = Integer.parseInt(confVal);
        }

        int val = 0;
        if (period == 0 || period == 65535) {
            val = 0;
        } else {
            if (this.pwdUseDays > period) {
                val = 1;
            } else {
                val = 0;
            }
        }

        this.isWarnUpdataPwd = val;
    }

    /**
     * @return the urlMap
     */
    public Map<String, String> getUrlMap() {
        return urlMap;
    }

    /**
     * @param urlMap the urlMap to set
     */
    public void setUrlMap(Map<String, String> urlMap) {
        this.urlMap = urlMap;
    }

}
