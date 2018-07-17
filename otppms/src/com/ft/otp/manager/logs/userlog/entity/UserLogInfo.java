/**
 *Administrator
 */
package com.ft.otp.manager.logs.userlog.entity;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.util.tool.DateTool;

/**
 * 用户日志实体类功能说明
 *
 * @Date in May 4, 2011,4:51:09 PM
 *
 * @author ZJY
 */
public class UserLogInfo extends BaseEntity {
    private int id;             //日志ID，自增
    private String userid;      //产生此条日志的用户帐号
    private String token;       //产生此条日志的令牌序列号
    private String clientip;    //产生此条日志的认证代理IP
    private String serverip;//认证服务器IP
    private int actionid;       //操作标识ID
    private int actionresult = -1;   //操作结果，0失败，1成功
    private int logtime;        //产生此条日志的时间戳
    private String logcontent;  //日志内容
    private String hashcode ="";//日志内容的摘要值
    private String vendorid ="";
    private int domainid;       //用户所属的域ID
    private String domainname;  //用户所属的域名称
    private int orgunitid;      //用户所属的组织机构
    private String orgunitname; //用户所属的组织机构名称
    private int moduletype;     //日志记录的来源，0认证服务器，1用户门户
    private int startDate;
    private int endDate;
    
    private String actionidOper; //操作名称

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * @return the logcontent
     */
    public String getLogcontent() {
        return logcontent;
    }

    /**
     * @param logcontent the logcontent to set
     */
    public void setLogcontent(String logcontent) {
        this.logcontent = logcontent;
    }

    //对日志时间转换
    public String getLogTimeStr() {
        return DateTool.dateToStr(getLogtime(), true);
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientip() {
        return clientip;
    }

    public void setClientip(String clientip) {
        this.clientip = clientip;
    }

    public int getActionid() {
        return actionid;
    }

    public void setActionid(int actionid) {
        this.actionid = actionid;
    }

    public int getActionresult() {
        return actionresult;
    }

    public void setActionresult(int actionresult) {
        this.actionresult = actionresult;
    }

    public int getLogtime() {
        return logtime;
    }

    public void setLogtime(int logtime) {
        this.logtime = logtime;
    }

    public int getDomainid() {
        return domainid;
    }

    public void setDomainid(int domainid) {
        this.domainid = domainid;
    }

    public String getDomainname() {
        return domainname;
    }

    public void setDomainname(String domainname) {
        this.domainname = domainname;
    }

    public int getOrgunitid() {
        return orgunitid;
    }

    public void setOrgunitid(int orgunitid) {
        this.orgunitid = orgunitid;
    }

    public String getOrgunitname() {
        return orgunitname;
    }

    public void setOrgunitname(String orgunitname) {
        this.orgunitname = orgunitname;
    }

    public int getModuletype() {
        return moduletype;
    }

    public void setModuletype(int moduletype) {
        this.moduletype = moduletype;
    }

    /**
     * @return the vendorid
     */
    public String getVendorid() {
        return vendorid;
    }

    /**
     * @param vendorid the vendorid to set
     */
    public void setVendorid(String vendorid) {
        this.vendorid = vendorid;
    }

    public String getActionidOper() {
        return actionidOper;
    }

    public void setActionidOper(String actionidOper) {
        this.actionidOper = actionidOper;
    }

    public String getServerip() {
        return serverip;
    }

    public void setServerip(String serverip) {
        this.serverip = serverip;
    }

}
