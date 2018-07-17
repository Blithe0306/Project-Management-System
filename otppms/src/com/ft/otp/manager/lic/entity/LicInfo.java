/**
 *Administrator
 */
package com.ft.otp.manager.lic.entity;

import com.ft.otp.util.tool.DateTool;

/**
 * 授权信息实体对象
 *
 * @Date in Feb 19, 2013,2:30:43 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class LicInfo {

    private String licid;//授权ID
    private String licinfo;//授权信息
    private int lictype;//授权类型，评估、企业、高级
    private String issuer;//发行者
    private String customerid;//授权客户ID
    private int licstate = -1;//使用状态，0停用，1使用
    private int licupdatetime;//授权更新时间UTC秒

    private String startTime;//授权起始时间
    private String expireTime;//授权结束时间
    private int tokenCount;//令牌总量
    private int serverNodes; //认证服务器节点数

    private int mobileTokenNum; //手机令牌个数
    private int softTokenNum; //软件令牌个数
    private int smsTokenNum; //短信令牌个数
    private String licupdateTimeStr; //授权更新时间
    private int oldLicType;

    public int getOldLicType() {
		return oldLicType;
	}

	public void setOldLicType(int oldLicType) {
		this.oldLicType = oldLicType;
	}

	/**
     * 授权信息填充
     * 
     * @Date in May 18, 2013,10:29:53 AM
     * @param licInfo
     * @param license
     */
    public void getLicInfo(LicInfo licInfo, License license) {
        licInfo.setLicupdateTimeStr(DateTool.dateConvertStr(licInfo.getLicupdatetime()));
        licInfo.setStartTime(DateTool.dateConvertStr(license.getStartTime()));
        licInfo.setExpireTime(DateTool.dateConvertStr(license.getExpireTime()));
        licInfo.setTokenCount(license.getTokenCount());
        licInfo.setServerNodes(license.getServerNodes());
        licInfo.setMobileTokenNum(license.getMobileTokenNum());
        licInfo.setSoftTokenNum(license.getSoftTokenNum());
        licInfo.setSmsTokenNum(license.getSmsTokenNum());
    }

    /**
     * @return the licid
     */
    public String getLicid() {
        return licid;
    }

    /**
     * @param licid the licid to set
     */
    public void setLicid(String licid) {
        this.licid = licid;
    }

    /**
     * @return the licinfo
     */
    public String getLicinfo() {
        return licinfo;
    }

    /**
     * @param licinfo the licinfo to set
     */
    public void setLicinfo(String licinfo) {
        this.licinfo = licinfo;
    }

    /**
     * @return the lictype
     */
    public int getLictype() {
        return lictype;
    }

    /**
     * @param lictype the lictype to set
     */
    public void setLictype(int lictype) {
        this.lictype = lictype;
    }

    /**
     * @return the issuer
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * @param issuer the issuer to set
     */
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    /**
     * @return the customerid
     */
    public String getCustomerid() {
        return customerid;
    }

    /**
     * @param customerid the customerid to set
     */
    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    /**
     * @return the licstate
     */
    public int getLicstate() {
        return licstate;
    }

    /**
     * @param licstate the licstate to set
     */
    public void setLicstate(int licstate) {
        this.licstate = licstate;
    }

    /**
     * @return the licupdatetime
     */
    public int getLicupdatetime() {
        return licupdatetime;
    }

    /**
     * @param licupdatetime the licupdatetime to set
     */
    public void setLicupdatetime(int licupdatetime) {
        this.licupdatetime = licupdatetime;
    }

    /**
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the expireTime
     */
    public String getExpireTime() {
        return expireTime;
    }

    /**
     * @param expireTime the expireTime to set
     */
    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * @return the tokenCount
     */
    public int getTokenCount() {
        return tokenCount;
    }

    /**
     * @param tokenCount the tokenCount to set
     */
    public void setTokenCount(int tokenCount) {
        this.tokenCount = tokenCount;
    }

    /**
     * @return the serverNodes
     */
    public int getServerNodes() {
        return serverNodes;
    }

    /**
     * @param serverNodes the serverNodes to set
     */
    public void setServerNodes(int serverNodes) {
        this.serverNodes = serverNodes;
    }

    /**
     * @return the mobileTokenNum
     */
    public int getMobileTokenNum() {
        return mobileTokenNum;
    }

    /**
     * @param mobileTokenNum the mobileTokenNum to set
     */
    public void setMobileTokenNum(int mobileTokenNum) {
        this.mobileTokenNum = mobileTokenNum;
    }

    /**
     * @return the softTokenNum
     */
    public int getSoftTokenNum() {
        return softTokenNum;
    }

    /**
     * @param softTokenNum the softTokenNum to set
     */
    public void setSoftTokenNum(int softTokenNum) {
        this.softTokenNum = softTokenNum;
    }

    /**
     * @return the smsTokenNum
     */
    public int getSmsTokenNum() {
        return smsTokenNum;
    }

    /**
     * @param smsTokenNum the smsTokenNum to set
     */
    public void setSmsTokenNum(int smsTokenNum) {
        this.smsTokenNum = smsTokenNum;
    }

    public String getLicupdateTimeStr() {
        return licupdateTimeStr;
    }

    public void setLicupdateTimeStr(String licupdateTimeStr) {
        this.licupdateTimeStr = licupdateTimeStr;
    }


}
