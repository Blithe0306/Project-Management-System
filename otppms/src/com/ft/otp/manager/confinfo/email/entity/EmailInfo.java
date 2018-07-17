/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.email.entity;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.util.tool.StrTool;

/**
 * 邮件服务器配置实体
 *
 * @Date in Nov 19, 2012,11:51:12 AM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class EmailInfo extends BaseEntity {

    private int id; //自增ID
    private String sender; //发送人名称或是主题称谓
    private String account; //系统发送邮件账号
    private String pwd; //系统发送邮件账号密码
    private String servname; //邮件服务器名称
    private String host; //邮件服务器
    private int port; //服务器端口
    private int validated; //表示邮件发送时是否需要对发送帐号的身份进行验证，默认不检查。0否，1是
    private int isdefault; //是否为默认邮件服务器，0 否，1 是
    private String descp; //描述

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account.trim();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender.trim();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getServname() {
        return servname;
    }

    public void setServname(String servname) {
        this.servname = servname.trim();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host.trim();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getValidated() {
        return validated;
    }

    public void setValidated(int validated) {
        this.validated = validated;
    }

    public int getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(int isdefault) {
        this.isdefault = isdefault;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = StrTool.cleanXSS(descp);
    }
}
