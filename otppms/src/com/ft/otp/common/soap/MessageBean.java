/**
 *Administrator
 */
package com.ft.otp.common.soap;

/**
 * 页面提交的业务请求数据实体
 *
 * @Date in May 21, 2013,2:45:11 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class MessageBean {

    private String userId;//用户名
    private String tokenSN; //令牌号
    private String otp; //动态口令、手机令牌激活密码
    private String nextOtp; //下一次动态口令
    private String challengeCode; //挑战码
    private String responseCode; //应答码
    private String udid; //手机UDID

    public MessageBean() {

    }

    /**
     * 令牌同步时使用该构造器
     * @param pUserId
     * @param pOtp
     * @param pNextOtp
     */
    public MessageBean(String pTokenSN, String pOtp, String pNextOtp) {
        this.tokenSN = pTokenSN;
        this.otp = pOtp;
        this.nextOtp = pNextOtp;
    }

    /**
     * 令牌认证时使用该构造器
     * @param pUserId
     * @param pTokenSN
     * @param pOtp
     * @param pChallengeCode 非c300令牌可以为空
     * @param pResponseCode 非c300令牌可以为空
     */
    public MessageBean(String pTokenSN, String pOtp, String pChallengeCode, String pResponseCode) {
        this.tokenSN = pTokenSN;
        this.otp = pOtp;
        this.challengeCode = pChallengeCode;
        this.responseCode = pResponseCode;
    }

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

    /**
     * @return the otp
     */
    public String getOtp() {
        return otp;
    }

    /**
     * @param otp the otp to set
     */
    public void setOtp(String otp) {
        this.otp = otp;
    }

    /**
     * @return the nextOtp
     */
    public String getNextOtp() {
        return nextOtp;
    }

    /**
     * @param nextOtp the nextOtp to set
     */
    public void setNextOtp(String nextOtp) {
        this.nextOtp = nextOtp;
    }

    /**
     * @return the tokenSN
     */
    public String getTokenSN() {
        return tokenSN;
    }

    /**
     * @param tokenSN the tokenSN to set
     */
    public void setTokenSN(String tokenSN) {
        this.tokenSN = tokenSN;
    }

    /**
     * @return the challengeCode
     */
    public String getChallengeCode() {
        return challengeCode;
    }

    /**
     * @param challengeCode the challengeCode to set
     */
    public void setChallengeCode(String challengeCode) {
        this.challengeCode = challengeCode;
    }

    /**
     * @return the responseCode
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * @param responseCode the responseCode to set
     */
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * @return the udid
     */
    public String getUdid() {
        return udid;
    }

    /**
     * @param udid the udid to set
     */
    public void setUdid(String udid) {
        this.udid = udid;
    }

}
