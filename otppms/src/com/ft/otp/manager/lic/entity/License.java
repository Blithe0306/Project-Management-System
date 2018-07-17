/**
 *Administrator
 */
package com.ft.otp.manager.lic.entity;

import com.ft.otp.util.tool.StrTool;

/**
 * 对应存放解析完成的授权信息的实体对象
 *
 * @Date in Feb 19, 2013,4:28:33 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class License {

    private String licId;//授权ID
    private String issuer;//授权发行者，发行厂商
    private String owner;//授予的客户
    private String descp;//授权描述

    private int startTime;//授权起始时间
    private int expireTime;//授权结束时间
    private int tokenCount;//令牌总量
    private int licType; //授权类型
    private int serverNodes; //认证服务器节点数
    private String licInfo;//授权信息

    private int mobileTokenNum; //手机令牌个数
    private int softTokenNum; //软件令牌个数
    private int cardTokenNum; //卡片令牌个数
    private int smsTokenNum; //短信令牌个数

    /**
     * @return the licId
     */
    public String getLicId() {
        return licId;
    }

    /**
     * @param licId the licId to set
     */
    public void setLicId(String licId) {
        this.licId = licId;
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
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return the descp
     */
    public String getDescp() {
        return descp;
    }

    /**
     * @param descp the descp to set
     */
    public void setDescp(String descp) {
        this.descp = StrTool.cleanXSS(descp);
    }

    /**
     * @return the startTime
     */
    public int getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the expireTime
     */
    public int getExpireTime() {
        return expireTime;
    }

    /**
     * @param expireTime the expireTime to set
     */
    public void setExpireTime(int expireTime) {
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
     * @return the licType
     */
    public int getLicType() {
        return licType;
    }

    /**
     * @param licType the licType to set
     */
    public void setLicType(int licType) {
        this.licType = licType;
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
     * @return the licInfo
     */
    public String getLicInfo() {
        return licInfo;
    }

    /**
     * @param licInfo the licInfo to set
     */
    public void setLicInfo(String licInfo) {
        this.licInfo = licInfo;
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
     * @return the cardTokenNum
     */
    public int getCardTokenNum() {
        return cardTokenNum;
    }

    /**
     * @param cardTokenNum the cardTokenNum to set
     */
    public void setCardTokenNum(int cardTokenNum) {
        this.cardTokenNum = cardTokenNum;
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

}
