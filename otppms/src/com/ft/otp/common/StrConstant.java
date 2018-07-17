/**
 *Administrator
 */
package com.ft.otp.common;

/**
 * 字符串常量类，用于工程内静态字符串常量的统一定义
 * 
 * 该类有别于Constant类，
 * Constant是系统级的全局常量类，该类只用于工程内静态字符串常量
 *
 * @Date in May 7, 2011,12:06:55 PM
 *
 * @author TBM
 */
public class StrConstant {

    /**
     * 公用的数字标识符，用于字符形式数值对比等，包括阿拉伯数据0-9
     */
    public static String common_number_0 = "0";
    public static String common_number_1 = "1";
    public static String common_number_2 = "2";
    public static String common_number_3 = "3";
    public static String common_number_4 = "4";
    public static String common_number_5 = "5";
    public static String common_number_6 = "6";
    public static String common_number_7 = "7";
    public static String common_number_8 = "8";
    public static String common_number_9 = "9";

    /**
     * 常量 yes、y是，no、n否
     */
    public static String common_yes = "yes";
    public static String common_no = "no";

    public static String common_y = "y";
    public static String common_n = "n";

    /*
     * 定义特殊符号
     */
    public static String common_char1 = "&";
    public static String common_char2 = "[]";

    /**
     * 用户、令牌批量绑定返回的Map中多个对象Key标识
     */
    //用户令牌列表标识
    public static final String USR_TKN_LIST = "usr_tkn_list";
    //用户令牌计数标识
    public static final String USR_TKN_NUM = "usr_tkn_num";
    //用户交换令牌是否成功标识
    public static final String USR_TKN_SUCC = "usr_tkn_succ";
    //用户交换令牌,选择令牌时令牌被绑定次数超出最大限制的令牌串
    public static final String USR_TKN_TOKENS = "usr_tkn_tokens";
    //用户令牌操作失败信息标识
    public static final String USR_TKN_ERR = "usr_tkn_err";

    //绑定成功
    public static final String USR_TKN_ERR_00 = "oper_err00";

    //用户超出最大绑定令牌数据限制提示
    public static final String USR_TKN_ERR_01 = "oper_err01";
    //令牌被绑定次数超出最大限制提示
    public static final String USR_TKN_ERR_02 = "oper_err02";
    //单选的用户和令牌已经绑定提示
    public static final String USR_TKN_ERR_03 = "oper_err03";
    //用户或令牌为空或未选择，不能执行批量绑定
    public static final String USR_TKN_ERR_04 = "oper_err04";

    //令牌绑定次数超出最大限制或用户令牌已经绑定提示
    public static final String USR_TKN_ERR_05 = "oper_err05";
    //令牌为空或未选择，不能执行批量绑定
    public static final String USR_TKN_ERR_06 = "oper_err06";

    //令牌与用户机构ID不相同
    public static final String USR_TKN_ERR_07 = "oper_err07";

    //新增服务器时，对checknum进行加密
    public static final String KEY = "4c1a53f46ebaeca9f3b7c6d5593e107c";

    //存放进session中的验证码
    public static final String VALIDATE_CODE = "validateCode";

    /**
     * 管理员权限树相关资源KEY
     */
    public static final String ADM_PERM_KEY = "adm_perm_";

    /**
     * 记录日志时在service层不进行拦截的记录日志的方法名
     */
    public static final String METHOD_QUERY = "query";
    public static final String METHOD_FIND = "find";
    public static final String METHOD_COUNT = "count";

    /**
     * 拦截器拦截到返回值为int类型的方法名
     */
    public static final String METHOD_IMPORT_TOKEN = "importToken";

    /**
     * 统计的几种类型
     */
    public static final String COUNT_BY_PRODUCTTYPE = "prdtype";
    public static final String COUNT_BY_PHYSICALTYPE = "phytype";

    /**
     * 统计的方式
     */
    public static final String COUNT_BY_TYPE = "type";
    public static final String COUNT_BY_STATE = "state";
    public static final String COUNT_BY_VIEW = "view";

    /**
     * 用户令牌的绑定结果
     */
    public static final String USER_TOKEN_BIND_RESULT = "userTknResult";

    /**
     * 令牌、规格、手机令牌的标识
     */
    public static final String TYPE_Token = "token";
    public static final String TYPE_SPEC = "spec";
    public static final String TYPE_MOBILE = "mobile";

    /**
     * 补码方式
     */
    public static final String FILTYPE_PKCS5PADDING = "PKCS5Padding";
    public static final String FILTYPE_PKCS7PADDING = "PKCS7Padding";
    public static final String FILTYPE_ISO10126PADDING = "ISO10126Padding";

    /**
     * 超级管理员标识
     */
    public static final String SUPER_ADMIN = "ADMIN";

    /**
     * 管理员日志查看类别
     */
    public static final String LOG_ALL_LOG = "060001"; //所有管理员日志
    public static final String LOG_MY_LOG = "060002"; //个人日志
    public static final String LOG_MY_TO_LOG = "060003"; //个人及个人创建的管理员日志

    /**
     * 用户日志查看类别
     */
    public static final String LOG_USER_ALL_LOG = "060101"; //查看所有用户日志
    public static final String LOG_USER_MY_LOG = "060102"; //查看所管理的组织机构下的用户日志

    //配置属性类型
    public static final String CONFTYPE_CENTER = "center";
    public static final String CENTER_EMERGENCY_PASS_DEF_VALIDTIME = "emergency_pass_def_validtime";
    public static final String CENTER_EMERGENCY_PASS_MAX_VALIDTIME = "emergency_pass_max_validtime";
    public static final String CENTER_DEFAULT_USER_PWD = "default_user_pwd";

    /**
     * 监控预警服务器类型
     */
    public static final String SERVER_TYPE_CENTER = "0";
    public static final String SERVER_TYPE_PORTAL = "1";
    public static final String SERVER_TYPE_SERVER = "2";
    public static final String LOCALHOST_IP = "127.0.0.1";
    public static final String CORE_EMPIN_OTP_LENEQ = "empin_otp_leneq";
    public static final String CORE_DEFAULTAP = "softtoken_distribute_pwd";

    //监控预警任务名称
    public static final String MONITOR_TASKNAME_EQUIPMENT = "equipment_monitor_task";
    public static final String MONITOR_TASKNAME_APP = "app_monitor_task";
    public static final String MONITOR_TASKNAME_BASE = "base_monitor_task";
    public static final String MONITOR_TASKNAME_BASE_COMMON = "base_common_monitor_task";
    public static final String MONITOR_HEARTBEAT_TASKNAME = "heart_beat_monitor_task";

    /**
     * 日志定时删除任务名称
     */
    public static final String LOG_TASKNAME_DELETE = "log_delete_task";

    /**
     * 定时备份任务名称
     */
    public static final String BAK_SCHEDULED_BACKUP = "scheduled_backup";
    
    /**
     * 双机热备运行监控预警
     */
    public static final String HEART_BEAT_MONITOR = "heart_beat_monitor_task";

}
