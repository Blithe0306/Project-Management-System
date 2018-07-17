/**
 *Administrator
 */
package com.ft.otp.common;

/**
 * 
 * 此类用于存放配置表中的属性常量
 * 
 * @Date in Apr 2, 2011,6:35:27 PM
 * 
 * @author TBM
 */
public class ConfConstant {

    /**
     * 配置类型
     */
    public static final String CONF_TYPE_COMMON = "common";
    public static final String CONF_TYPE_AUTH = "auth";
    public static final String CONF_TYPE_USER = "user";
    public static final String CONF_TYPE_TOKEN = "token";
    public static final String CONF_TYPE_CENTER = "center";
    public static final String CONF_TYPE_PORTAL = "portal";
    public static final String CONF_TYPE_BAK = "bak";
    public static final String CONF_TYPE_MONITOR_HEART_BEAT="warn_heart_beat";

    /**
     * 系统公共配置 其它公共配置
     */
    public static final String SQLS_VERSION = "sqlsversion";
    public static final String SESSION_EFFECTIVELY_TIME = "session_effectively_time";
    public static final String LOG_LEVEL = "log_level";
    public static final String LOG_TIMING_ENABLED = "log_timing_enabled";
    public static final String LOG_TIMING_DELETE = "log_timing_delete_validity";
    public static final String LOG_IS_BAK = "log_is_bak";
    public static final String DEFAULT_SYSTEM_LANGUAGE = "default_system_language";//系统使用的语言标识
    public static final String SEED_PRIVATE_KEY_RANDOM = "seed_private_key_random";
    public static final String USERID_FORMAT_TYPE = "userid_format_type";
    public static final String DEFAULT_DOMAIN_ID = "default_domain_id";
    public static final String DB_DATA_CREATE_TYPE = "db_data_create_type";

    /**
     * 认证基本配置
     */
    //1 事件型令牌窗口策略
    public static final String CORE_HOTP_AUTH_WND = "hotp_auth_wnd";
    public static final String CORE_HOTP_ADJUST_WND = "hotp_adjust_wnd";
    public static final String CORE_HOTP_SYNC_WND = "hotp_sync_wnd";
    //2 时间型令牌窗口策略
    public static final String CORE_TOTP_AUTH_WND = "totp_auth_wnd";
    public static final String CORE_tOTP_ADJUST_WND = "totp_adjust_wnd";
    public static final String CORE_TOTP_SYNC_WND = "totp_sync_wnd";

    public static final String CORE_WND_ADJUST_PERIOD = "wnd_adjust_period";
    public static final String CORE_WND_ADJUST_MODE = "wnd_adjust_mode";
    public static final String CORE_RETRY_OTP_TIMEINTERVAL = "retry_otp_timeinterval";
    public static final String CORE_TEMP_LOCK_RETRY = "temp_lock_retry";
    public static final String CORE_MAX_RETRY = "max_retry";
    public static final String CORE_TEMP_LOCK_EXPIRE = "temp_lock_expire";

    /**
     * 用户配置
     */
    public static final String CORE_MAX_BIND_TOKENS = "max_bind_tokens";
    public static final String CORE_ADD_USER_WHEN_BIND = "add_user_when_bind";
    public static final String CORE_MAX_BIND_USERS = "max_bind_users";
    public static final String TK_BIND_IS_CHANGE_ORG = "token_bind_is_change_org";
    public static final String TK_UNBIND_IS_CHANGE_ORG = "token_unbind_is_change_org";
    public static final String CORE_UNBIND_STATE_SELECT = "unbind_state_select";
    public static final String CORE_REPLACE_STATE_SELECT = "replace_state_select";
    public static final String CORE_AUTH_OTP_WHEN_BIND = "auth_otp_when_bind";

    public static final String DEFAULT_USER_PWD = "default_user_pwd";
    public static final String DEFAULT_LOCALAUTH = "default_localauth";
    public static final String DEFAULT_BACKENDAUTH = "default_backendauth";

    // AD同步属性设置
    public static final String AD_CONF = "adattr";
    public static final String LOCAL_ATTR_USERID = "local_attr_userid";
    public static final String LOCAL_ATTR_REALNAME = "local_attr_realname";
    public static final String LOCAL_ATTR_EMAIL = "local_attr_email";
    public static final String LOCAL_ATTR_ADDRESS = "local_attr_address";
    public static final String LOCAL_ATTR_TEL = "local_attr_tel";
    public static final String LOCAL_ATTR_CELLPHONE = "local_attr_cellphone";
    public static final String LOCAL_ATTR_ENABLED = "local_attr_enabled";
    public static final String LOCAL_ATTR_OU = "local_attr_ou";

    /**
     * 令牌配置
     */
    // 1软件令牌配置
    public static final String SOFT_TK_DIST_PWD = "softtoken_distribute_pwd";
    // 2激活密码策略
    public static final String AP_PERIOD = "ap_period"; // 激活密码的有效周期
    public static final String AP_RETRY = "ap_retry"; // 激活密码的重试次数
    public static final String DEFAULT_AP = "defult_ap"; // 默认激活密码
    public static final String AP_SMS_SEND = "ap_sms_send"; // 激活密码是否通过短信分发
    public static final String SMS_MOBILE_ACTIVATE_CODE_MESSAGE = "mobile_activate_code_message";
    public static final String SMS_MOBILE_ONLINE_DIST_MESSAGE = "mobile_online_dist_message";
    public static final String DIST_EMAIL_SEND = "dist_email_send";//单用户绑定令牌邮件发送
    // 3分发站点策略
    public static final String SITE_ENABLED = "site_enabled"; // 分发站点是否启用 ，启用(y)、未启用(n)
    public static final String SITE_TYPE = "site_type"; // 分发站点的类型(http(1)、https(2)、all(3))
    public static final String SITE_URL = "site_url"; // 默认的分发URL
    // 激活密码产生方式使用现有的(1) 重新生成：随机的(2)、默认的(4)、手动输入(8)
    public static final String AP_GEN_METHOD = "ap_gen_method";
    // URL带的参数,参数可能有:用户ID(1)，令牌ID(2)
    public static final String URL_PARAMS = "url_params";

    // 4短信令牌配置
    public static final String SMS_TOKEN_AUTH_EXPIRE = "sms_token_auth_expire";
    public static final String SMS_TOKEN_GEN_EXPIRE = "sms_token_gen_expire";
    public static final String SMS_OTP_SEED_MESSAGE = "sms_otp_seed_message";
    
    public static final String SMS_TOKEN_REQ_MORE_ATTR = "sms_token_req_more_attr";
    public static final String SMS_TOKEN_REQ_MORE_VAL = "sms_token_req_more_attr_val";
    public static final String SMS_OTP_REQ_SEND_CHECK = "sms_token_req_send_before_check";
    public static final String SMS_OTP_REQ_RETURN_CODE = "sms_token_req_return_code_domain";
    
    //5、应急口令
    public static final String CORE_TOKEN_EMPIN2OTP = "token_empin2otp";
    public static final String CORE_EMPIN_OTP_LENEQ = "empin_otp_leneq";
    public static final String EMERGENCY_PASS_DEF_VALIDTIME = "emergency_pass_def_validtime";
    public static final String EMERGENCY_PASS_MAX_VALIDTIME = "emergency_pass_max_validtime";

    /**
     * 管理中心配置
     */
    public static final String LOGIN_ERROR_RETRY_TEMP = "login_error_retry_temp";
    public static final String LOGIN_ERROR_RETRY_LONG = "login_error_retry_long";
    public static final String LOGIN_LOCK_EXPIRE = "login_lock_expire";
    public static final String PASSWD_UPDATE_PERIOD = "passwd_update_period";
    public static final String PROHIBIT_ADMIN = "prohibit_admin";
    public static final String EMAIL_ACTIVATE_ENABLED = "email_activate_enabled";

    // 认证服务器配置策略
    public static final String MAIN_HOSTIPADDR = "main_hostipaddr";
    public static final String SPARE_HOSTIPADDR = "spare_hostipaddr";

    public static final String ENABLED_TRUSTIP_CHECK = "enabled_trust_ip_check";

    /**
     * 用户门户配置
     */
    public static final String PORTAL_CONFIG = "portal";

    public static final String SELF_SERVICE_ENABLE = "self_service_enable";
    public static final String PWD_EMAIL_ACTIVE_EXPIRE = "pwd_email_active_expire";
    public static final String OPEN_FUNCTION_CONFIG = "open_function_config";
    public static final String INIT_PWD_LOGIN_VERIFY_TYPE = "init_pwd_login_verify_type";
    public static final String INIT_PWD_EMAIL_ACT_EXPIRE = "init_pwd_email_active_expire";
    public static final String INIT_PWD_SMS_VERIFY_EXPIRE = "init_pwd_sms_verify_expire";
    public static final String AD_VERIFY_pwd_IP = "ad_verify_pwd_ip";
    public static final String AD_VERIFY_pwd_port = "ad_verify_pwd_port";
    public static final String AD_VERIFY_PWD_DN = "ad_verify_pwd_dn";
    
    // PEAP
    public static final String RADIUS_PEAP_ENABLED = "enabled_peap";
    public static final String RADIUS_PEAP_LOCKED = "locked_peap";

    /**
     * 数据库配置属性
     */
    //数据库配置标识
    public static final String DATEBASE_CONF = "database";

    // 数据库类型
    public static final String DB_TYPE = "db_type";
    // 数据库ip
    public static final String DB_IP = "db_ip";
    // 数据库端口
    public static final String DB_PORT = "db_port";
    // 数据库名称
    public static final String DB_NAME = "db_dbname";
    // 连接用户名
    public static final String DB_USER = "db_username";
    // 密码
    public static final String DB_PASSWORD = "db_passwd";
    // 副IP
    public static final String DB_IP2 = "db_ip_2";
    // 双机
    public static final String DB_DUAL = "db_dual";

    /**
     * 用户来源更新字段属性
     */
    //用户来源标识
    public static final String USERSOURCE_CONF = "'ldap','database','domino'";

    public static final String USER_SOURCE_USERID = "userid";
    public static final String USER_SOURCE_REALNAME = "realname";
    public static final String USER_SOURCE_PIN = "pwd";
    public static final String USER_SOURCE_EMAIL = "email";
    public static final String USER_SOURCE_ADDRESS = "address";
    public static final String USER_SOURCE_TEL = "tel";
    public static final String USER_SOURCE_CELLPHONE = "cellPhone";

    /**
     * 短信网关配置属性
     */
    public static final String SMS_TYPE = "type";;
    public static final String SMS_REGISTER = "register";
    public static final String SMS_HOST = "host";
    public static final String SMS_PORT = "port";
    public static final String SMS_USERNAME = "username";
    public static final String SMS_PASSWORD = "password";
    public static final String SMS_PARAMUNAME = "paramuname";
    public static final String SMS_PARAMPWD = "parampwd";
    public static final String SMS_PHONE = "phone";
    public static final String SMS_MESSAGE = "message";
    public static final String SMS_ANNEX = "annex";
    public static final String SMS_ENABLED = "enabled";

    /**
     * OTP Server 配置
     */
    public static final String OTPSERVER_CONFIG = "server";

    public static final String CACHE_CLEAR_POLICY = "cache_clear_policy";
    public static final String CACHE_TUNER_SLEEPTIME = "cache_tuner_sleeptime";
    public static final String CACHE_MAXSIZE = "cache_maxsize";
    public static final String CACHE_EXPIRE_TIME = "cache_expire_time";

    /**
     * 监控预警策略配置
     */
    public static final String MONITOR_BASE_CONF = "warn_base";
    public static final String MONITOR_SB_CONF = "warn_sb";
    public static final String MONITOR_APP_CONF = "warn_app";

    public static final String MONITOR_BASE_CONF_COMMON = "warn_base_common";
    // 基本配置
    public static final String MONITOR_BASE_ENABLED = "enabled";
    public static final String MONITOR_BASE_SENDTYPE = "send_type";
    public static final String MONITOR_BASE_REMAINDAYS = "remain_days";
    public static final String MONITOR_BASE_UNBINDLOWER = "unbind_lower";
    public static final String MONITOR_BASE_SYNCUPPER = "syn_cupper";
    public static final String MONITOR_BASE_TIMEINTERVAL = "time_interval";
    public static final String MONITOR_BASE_TIMEINTERVAL_COMMON = "time_interval_common";
    // 设备监控预警配置
    public static final String MONITOR_SB_ENABLED = "enabled";
    public static final String MONITOR_SB_SEND_TYPE = "send_type";
    public static final String MONITOR_SB_CPUUPPER = "cpu_upper";
    public static final String MONITOR_SB_DISKUPPER = "disk_upper";
    public static final String MONITOR_SB_MEMUPPER = "mem_upper";
    public static final String MONITOR_SB_TIMEINTERVAL = "time_interval";
    // 应用系统监控预警配置
    public static final String MONITOR_APP_ENABLED = "enabled";
    public static final String MONITOR_APP_SEND_TYPE = "send_type";
    public static final String MONITOR_APP_TIMEINTERVAL = "time_interval";

    //短信令牌配置 基本功能配置
    public static final String SMS_CONFIG = "sms";

    /**
     * 数据库备份配置
     */
    public static final String DBBAK_IS_TIME_AUTO = "is_time_auto";
    public static final String DBBAK_IS_BAK_LOG = "is_bak_Log";
    public static final String DBBAK_IS_REMOTE = "is_remote";
    public static final String DBBAK_DIR = "dir";
    public static final String TEMP_TEMP_DIR = "temp_dir";
    public static final String DBBAK_SERVER_IP = "server_ip";
    public static final String DBBAK_PORT = "port";
    public static final String DBBAK_USER = "user";
    public static final String DBBAK_PASSWORD = "password";
    
    //双机热备运行监控配置
    public static final String MONITOR_HEART_BEAT_MAIN_IP="main_ip";//主服务器Ip地址
    public static final String MONITOR_HEART_BEAT_SPARE_IP="spare_ip";//从服务器Ip地址
    public static final String MONITOR_HEART_BEAT_ADMIN_ID="adminid";//管理员外键
    public static final String MONITOR_HEART_BEAT_ENABLED="enabled";//是否启用预警监控
    public static final String MONITOR_HEART_BEAT_TIMEINTERVAL="time_interval";//定时器时间间隔
    public static final String MONITOR_HEART_BEAT_SEND_TYPE="send_type";//预警通知方式
    public static final String MONITOR_HEART_BEAT_PORT="port";//端口号

}
