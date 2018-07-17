/**
 *Administrator
 */
package com.ft.otp.common;

/**
 * 管理中心与认证服务器交互常量标识码
 *
 * @Date in Jun 22, 2011,2:00:14 PM
 *
 * @author TBM
 */
public class AuthConstant {

    //多语言前缀标识字符串
    public static final String LANG_CODE_KEY = "lang_key_";

    /**
     * 服务器认证返回常用结果码
     */
    //操作成功
    public static final String OCRA_SUCC_CODE = "0000";
    //需要验证PIN(静态密码)
    public static final String OCRA_NEEDPIN_CODE = "31";
    //令牌需要同步
    public static final String OCRA_TKNSYNC_CODE = "13";

    //代理配置文件为空或初始化失败，结束后续所有交互操作
    public static final String OCRA_INIT_FAIL_CODE = "9999";

    //OCRA交互式认证时保持本次完整会话
    public static final String OCRA_AGNET_OBJ = "ocraAgent";
    //OCRA交互式认证时返回的结果
    public static final String OCRA_QS_RESULT = "result";

}
