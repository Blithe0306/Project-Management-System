/**
 *Administrator
 */
package com.ft.otp.common;

/**
 * 授权参数常量类型
 *
 * @Date in Feb 20, 2013,3:06:50 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class LicenseConstant {

    /**
     * License对象相关属性
     */
    //授权发行者，发行厂商
    public static final String OTP_SERVER_LIC_ISSUER = "otp-server.lic-issuer";
    //授予的客户
    public static final String OTP_SERVER_LIC_OWNER = "otp-server.lic-owner";
    //授权起始时间
    public static final String OTP_SERVER_LIC_STARTTIME = "otp-server.lic-startTime";
    //授权结束时间
    public static final String OTP_SERVER_LIC_EXPIRETIME = "otp-server.lic-expireTime";
    //令牌总量
    public static final String OTP_SERVER_LIC_TOKENCOUNT = "otp-server.lic-tokenCount";
    //授权类型
    public static final String OTP_SERVER_LIC_LICTYPE = "otp-server.lic-licType";
    //认证服务器节点数
    public static final String OTP_SERVER_LIC_SERVERNODES = "otp-server.lic-serverNodes";
    //手机令牌个数
    public static final String OTP_SERVER_LIC_MOBILETOKENNUM = "otp-server.lic-mobileTokenNum";
    //软件令牌个数
    public static final String OTP_SERVER_LIC_SOFTTOKENNUM = "otp-server.lic-softTokenNum";
    //卡片令牌个数
    public static final String OTP_SERVER_LIC_CARDTOKENNUM = "otp-server.lic-cardTokenNum";
    //短信令牌个数
    public static final String OTP_SERVER_LIC_SMSTOKENNUM = "otp-server.lic-smsTokenNum";

    /**
     * 授权类型版本类型
     */
    public static final int LICENSE_TYPE_EVALUATION = 0; //评估版
    public static final int LICENSE_TYPE_ENTERPRISE = 1; //企业版
    public static final int LICENSE_TYPE_PREMIUM = 2; //高级版

}
