/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.access.entity;

import com.ft.otp.base.entity.BaseEntity;

/**
 * 访问控制策略实体
 *
 * @Date in Dec 27, 2012,5:33:28 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class AccessInfo extends BaseEntity {
    
    private int id;
    private String startip; //开始访问IP
    private String endip; //结束访问IP
    private String allowIP;
    
    private int clientapptype = 0;//客户端应用的类型，0:管理中心，1:用户门户，为了识别与服务器通讯的客户端类型
    private int systype = 0;//服务器系统类型，0：管理中心，1：认证服务器
    private int expiretime;
    private int updatetime;
    
    private int enabled;//IP访问控制策略0停用，1启用
    private int clientappport = 0;//客户端应用的端口
    private String clientservpath;//管理中心请求用户门户设备状态信息的servlet名称路径：otpportal/servlet=?&requestMark=?

    public String getEndip() {
        return endip;
    }

    public void setEndip(String endip) {
        this.endip = endip;
    }

    public String getStartip() {
        return startip;
    }

    public void setStartip(String startip) {
        this.startip = startip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAllowIP() {
        return allowIP;
    }

    public void setAllowIP(String allowIP) {
        this.allowIP = allowIP;
    }

    public int getSystype() {
        return systype;
    }

    public void setSystype(int systype) {
        this.systype = systype;
    }

    public int getExpiretime() {
        return expiretime;
    }

    public void setExpiretime(int expiretime) {
        this.expiretime = expiretime;
    }

    public int getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(int updatetime) {
        this.updatetime = updatetime;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public int getClientapptype() {
        return clientapptype;
    }

    public void setClientapptype(int clientapptype) {
        this.clientapptype = clientapptype;
    }

    public int getClientappport() {
        return clientappport;
    }

    public void setClientappport(int clientappport) {
        this.clientappport = clientappport;
    }

    public String getClientservpath() {
        return clientservpath;
    }

    public void setClientservpath(String clientservpath) {
        this.clientservpath = clientservpath;
    }

}
