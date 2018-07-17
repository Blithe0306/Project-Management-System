/**
 *Administrator
 */
package com.ft.otp.common.soap.code;

/**
 * SOAP请求方法、请求属性定义
 *
 * @Date in May 21, 2013,2:41:47 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class SoapAttribute {

    /**
     * 本地定义的一些错误码
     */
    public static final int LOCAL_URL_NULL = 1000; //请求失败，与OTP认证服务器建立连接失败

    public static final int OTP_SUCCESS = 0; //业务请求成功

    /**
     * 请求方法
     */
    public static final String REQUEST_METHOD_AUTH = "auth";//认证请求
    public static final String REQUEST_METHOD_CHALLENGECODE = "getChallengeCode";//获取挑战码
    public static final String REQUEST_METHOD_VERIFYRESPONSECODE = "verifyResponseCode";//验证应答码
    public static final String REQUEST_METHOD_SYNC = "sync"; //令牌同步
    public static final String REQUEST_METHOD_DEVICEINFO = "getDeviceInfo"; //获取设备信息
    public static final String REQUEST_METHOD_GENPUK1 = "genPUK1"; //获取一级解锁码
    public static final String REQUEST_METHOD_GENPUK2 = "genPUK2"; //获取二级解锁码
    public static final String REQUEST_METHOD_GENAC = "genAC"; //获取挑战应答令牌激活码    
    public static final String REQUEST_METHOD_OFFLINEACTIVATE = "offLineActivate"; //手机令牌离线激活
    public static final String REQUEST_METHOD_ONLINEDISTRIBUTE = "onLineDistribute"; //手机令牌在线分发
    public static final String REQUEST_METHOD_ONLINEACTIVATE = "onLineActivate"; //手机令牌在线激活
    public static final String REQUEST_METHOD_SERVERSTATE = "serverState"; //获取服务器状态
    public static final String REQUEST_METHOD_GENACDATA = "genAcData"; //获取手机令牌二维码激活数据

    /**
     * 请求服务器更新配置相关
     */
    public static final String REQUEST_METHOD_RECONFIG = "reloadConf"; //请求服务器更新配置

    public static final int RECONFIG_TYPE_10001 = 10001; //授权文件信息
    public static final int RECONFIG_TYPE_10002 = 10002; //认证服务器的配置(初始化更改服务器IP地址时)
    public static final int RECONFIG_TYPE_10003 = 10003; //系统基础配置
    public static final int RECONFIG_TYPE_10004 = 10004; //域数据
    public static final int RECONFIG_TYPE_10005 = 10005; //Radius属性配置
    public static final int RECONFIG_TYPE_10006 = 10006; //认证代理，认证代理与服务器关联关系配置
    public static final int RECONFIG_TYPE_10007 = 10007; //认证代理的配置数据
    public static final int RECONFIG_TYPE_10008 = 10008; //令牌规格
    public static final int RECONFIG_TYPE_10009 = 10009; //短信网关列表
    public static final int RECONFIG_TYPE_10010 = 10010; //后端(第三方)认证配置
    public static final int RECONFIG_TYPE_10011 = 10011; //服务器删除，停止认证服务器
    public static final int RECONFIG_TYPE_10012 = 10012; //组织机构数据更新
    public static final int RECONFIG_TYPE_10013 = 10013; //信任IP

}
