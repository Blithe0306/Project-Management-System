/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.sms.entity;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.util.tool.StrTool;

/**
 * 短信网关配置实体
 * @Date in Nov 20, 2012,10:56:33 AM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class SmsInfo extends BaseEntity {

    private int id; //自增ID
    private String smsname; //短信网关配置名称
    private int requesttype; //短信网关请求方式：http(0)，https(1)
    private int sendtype; //短信数据发送方式：GET(0)，POST(1)
    private String host; //短信网关地址
    private String username; //短信网关用户名
    private String pwd; //短信网关密码
    private int enabled; //是否启用短信网关，0否，1是
    private int priority; //优先级，高、中、低
    private int retrysend; //短信发送失败重试发送的次数
    private String descp; //描述
    private String callPhone; //测试手机号
    private String message; //测试短信内容
    private String testSMS; //测试

    //短信发送参数配置
    private String accountattr;
    private String passwdattr;
    private String phoneattr;
    private String messageattr;
    private String paramannex;
    private String sendresult;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRequesttype() {
        return requesttype;
    }

    public void setRequesttype(int requesttype) {
        this.requesttype = requesttype;
    }

    public int getSendtype() {
        return sendtype;
    }

    public void setSendtype(int sendtype) {
        this.sendtype = sendtype;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getRetrysend() {
        return retrysend;
    }

    public void setRetrysend(int retrysend) {
        this.retrysend = retrysend;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = StrTool.cleanXSS(descp);
    }

    public String getCallPhone() {
        return callPhone;
    }

    public void setCallPhone(String callPhone) {
        this.callPhone = callPhone;
    }

    public String getTestSMS() {
        return testSMS;
    }

    public void setTestSMS(String testSMS) {
        this.testSMS = testSMS;
    }

    public String getSmsname() {
        return smsname;
    }

    public void setSmsname(String smsname) {
        this.smsname = smsname;
    }

    public String getAccountattr() {
        return accountattr;
    }

    public void setAccountattr(String accountattr) {
        this.accountattr = accountattr;
    }

    public String getPasswdattr() {
        return passwdattr;
    }

    public void setPasswdattr(String passwdattr) {
        this.passwdattr = passwdattr;
    }

    public String getPhoneattr() {
        return phoneattr;
    }

    public void setPhoneattr(String phoneattr) {
        this.phoneattr = phoneattr;
    }

    public String getMessageattr() {
        return messageattr;
    }

    public void setMessageattr(String messageattr) {
        this.messageattr = messageattr;
    }

    public String getParamannex() {
        return paramannex;
    }

    public void setParamannex(String paramannex) {
        this.paramannex = paramannex;
    }

    public String getSendresult() {
        return sendresult;
    }

    public void setSendresult(String sendresult) {
        this.sendresult = sendresult;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
