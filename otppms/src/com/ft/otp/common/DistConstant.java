/**
 *TBM
 */
package com.ft.otp.common;

/**
 * 手机令牌在线分发错误返回码
 *
 * @Date in Aug 29, 2013,9:12:55 AM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class DistConstant {

    // 成功
    public static final int OTPR_CORE_DISP_OK = 0;
    //URL参数错误,无用户名和令牌号信息
    public static final int OTPR_CORE_DISP_PARANOUK = 1;
    //URL参数错误,无激活密码信息
    public static final int OTPR_CORE_DISP_PARANOAP = 2;
    //用户不存在
    public static final int OTPR_CORE_DISP_NOUSER = 3;
    //用户未绑定令牌
    public static final int OTPR_CORE_DISP_UNOT = 4;
    //用户令牌绑定关系错误
    public static final int OTPR_CORE_DISP_NOUT = 5;
    //令牌不存在
    public static final int OTPR_CORE_DISP_NOTOKEN = 6;
    //令牌已被激活
    public static final int OTPR_CORE_DISP_TOKENA = 7;
    //手机标识码不匹配
    public static final int OTPR_CORE_DISP_UDIDE = 8;
    //激活密码为空
    public static final int OTPR_CORE_DISP_APNULL = 9;
    //激活密码已过期
    public static final int OTPR_CORE_DISP_APEXP = 10;
    //验证错误次数超过最大值
    public static final int OTPR_CORE_DISP_APERRORMAX = 11;
    //激活密码错误
    public static final int OTPR_CORE_DISP_APERROR = 12;
    //运行时错误
    public static final int OTPR_CORE_DISP_RUNRROR = 13;
    //站点访问类型错误  
    public static final int OTPR_CORE_DISP_PROTOCOLERROR = 14;
    //下载站点已关闭
    public static final int OTPR_CORE_DISP_SITECLOSED = 15;
    //令牌未分发
    public static final int OTPR_CORE_DISP_NODISP = 16;

}
