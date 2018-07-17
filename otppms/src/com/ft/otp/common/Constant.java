/**
 *Administrator
 */
package com.ft.otp.common;

/**
 * 系统级配置文件常量
 * 此类用于存放涉及到系统配置的常量
 *
 * @Date in Apr 2, 2011,6:35:27 PM
 *
 * @author TBM
 */
public class Constant {

    //类文件根目录
    public static String BASE_CLASS_PATH;
    //空格转换之后的根目录
    public static String BASE_CLASS_PATHSTR;
    //配置文件存放目录
    public static String WEB_CONFIG_PATH;

    //数据库连接池配置文件.properties形式
    public static String PROXOOL_PROPERTIES;
    //功能树配置文件
    public static String MENU_TREE;
    //管理系统必须的配置文件
    public static String SYSTEM_CONF_XML;

    //RADIUS配置属性文件
    public static String RADIUS_ATTR_XML;
    //令牌导入多厂商配置文件
    public static String VENDOR_CONF_XML;
    
    public static String DEPARTMENT_CONF_XML;
    
    //定制类型
    public static String PROJECT_TYPE_CONF_XML;
    //定制信息归类
    public static String PRJINFO_TYPE_CONF_XML;

    //数据备份参数配置文件
    public static String BAK_PARAM_CONF_XML;

    //数据备份文件
    public static String DATA_BAK_CONF_XML;

    //webapp路径 
    public static String WEB_APP_PATH;

    //iBatis DTD格式文件存放目录
    public static String IBATIS_DTD_AGENT_PATH;
    //iBatis核心配置文件
    public static String IBATIS_CONFIG;
    //SQL MAP文件路径
    public static String IBATIS_SQLMAP;

    /**
     * 数据库SQL脚本文件存放目录
     */
    public static String WEB_SQLCONFIG_PATH;
    
    //数据库升级脚本存放目录
    public static String WEB_UPGRADE_SQL_FILE_PATH;

    public static String FILE_SQL_ORACLE;
    public static String FILE_SQL_MYSQL;
    public static String FILE_SQL_DB2;
    public static String FILE_SQL_SYBASE;
    public static String FILE_SQL_SQLSERVER;
    public static String FILE_SQL_POSTGRES;
    public static String FILE_SQL_INIT;

    /**
     * 多语言资源路径常量
     */
    public static String LANGUAGE_PATH;

    /**
     * 系统配置文件上下文件路径初始化
     * 
     * @Date in Apr 28, 2013,2:33:21 PM
     * @param basePath
     */
    public static void setBasePath(String basePath) {
        WEB_APP_PATH = basePath;

        BASE_CLASS_PATH = basePath + "/WEB-INF/classes/";

        BASE_CLASS_PATHSTR = BASE_CLASS_PATH.replaceAll("%20", " ");
        WEB_CONFIG_PATH = BASE_CLASS_PATHSTR.replaceAll("WEB-INF/classes/", "WEB-INF/conf/");
        PROXOOL_PROPERTIES = BASE_CLASS_PATHSTR + "proxool.properties";
        MENU_TREE = WEB_CONFIG_PATH + "menu-tree-data.xml";
        SYSTEM_CONF_XML = WEB_CONFIG_PATH + "centerconf.xml";
        RADIUS_ATTR_XML = WEB_CONFIG_PATH + "radius_attribute.xml";
        VENDOR_CONF_XML = WEB_CONFIG_PATH + "vendor_conf.xml";
        DEPARTMENT_CONF_XML = WEB_CONFIG_PATH + "dept_conf.xml";
        PROJECT_TYPE_CONF_XML = WEB_CONFIG_PATH + "project_type_conf.xml";
        PRJINFO_TYPE_CONF_XML = WEB_CONFIG_PATH + "prjinfo_type_conf.xml";
        

        DATA_BAK_CONF_XML = WEB_CONFIG_PATH + "bak_tables-conf.xml";

        WEB_SQLCONFIG_PATH = BASE_CLASS_PATHSTR.replaceAll("WEB-INF/classes/", "WEB-INF/sqls_js/");
        FILE_SQL_ORACLE = WEB_SQLCONFIG_PATH + "otpdb_v3_oracle.sql";
        FILE_SQL_MYSQL = WEB_SQLCONFIG_PATH + "otpdb_v3_mysql.sql";
        FILE_SQL_DB2 = WEB_SQLCONFIG_PATH + "otpdb_v3_db2.sql";
        FILE_SQL_SYBASE = WEB_SQLCONFIG_PATH + "otpdb_v3_sybase.sql";
        FILE_SQL_SQLSERVER = WEB_SQLCONFIG_PATH + "otpdb_v3_sqlServer.sql";
        FILE_SQL_POSTGRES = WEB_SQLCONFIG_PATH + "otpdb_v3_postgresql.sql";
        FILE_SQL_INIT = WEB_SQLCONFIG_PATH + "otpdb_v3_init.sql";
        
        WEB_UPGRADE_SQL_FILE_PATH = BASE_CLASS_PATHSTR.replaceAll("WEB-INF/classes/", "WEB-INF/sqls_js/upgrade/");

        LANGUAGE_PATH = BASE_CLASS_PATHSTR + "language/";

        IBATIS_DTD_AGENT_PATH = BASE_CLASS_PATHSTR + "ibatis/";
        IBATIS_CONFIG = IBATIS_DTD_AGENT_PATH + "sqlMapConfig.xml";
        IBATIS_SQLMAP = IBATIS_DTD_AGENT_PATH + "sqlmap";
    }

    /**
     * 集成安装包认证服务器与Web应用的根目录
     */
    public static final String ROOT_OTP_AUTH_SERVICE = "OTPAuthService";
    public static final String ROOT_OTP_WEB_SERVICE = "OTPWebService";
    public static final String ROOT_OTP_AUTH_SERVICE_L = "otpauthservice";
    public static final String ROOT_OTP_WEB_SERVICE_L = "otpwebservice";

    /**
     * 服务器返回码做多语言提示时的前缀
     */
    public static final String LANG_CODE_KEY = "server_errcode_";

    /**
     * WEB应用生成的临时文件存放目录
     */
    //用户导入导出临时的模板或数据文件
    public static final String WEB_TEMP_FILE_USER = "/temp_file/user/";

    //代理配置文件ACF临时存放目录
    public static final String WEB_TEMP_FILE_ACF = "/temp_file/acf/";

    //令牌导入文件目录
    public static final String WEB_TEMP_FILE_TOKEN_IMPORT = "/temp_file/token/import/";

    //统计相关文件临时存放目录
    public static final String WEB_TEMP_FILE_REPORT = "/temp_file/chart/";

    //软件令牌分发临时存放目录
    public static final String WEB_TEMP_FILE_STF = "/temp_file/soft/";

    //种子导入文件存放临时目录
    public static final String WEB_TEMP_FILE_TOKENIMPORT = "/temp_file/tokenimport/";

    //远程数据备份的临时相对使用
    public static final String WEB_TEMP_DATA_BAK = "/temp_file/databak/";
    
    //二维码图片存放目录
    public static final String WEB_TEMP_FILE_BARCODE = "/temp_file/barcode/";
    

    public static final String WEB_TEMP_FILE_MOBILE_TYPE = "/temp_file/mobiletype/mtype.txt";

    //用户门户项目
    public static String PORTAL_NAME = "/otpportal/WEB-INF";
    public static String DB_CONF_FILE = "/conf/dbconf.properties";
    public static String DB_PROXOOL_FILE = "/conf/proxool.properties";

    /**
     * 语言地域标识
     */
    //中国
    public static String zh_CN = "zh_CN";
    //美国
    public static String en_US = "en_US";
    //日本
    public static String ja_JP = "ja_JP";
    //阿拉伯
    public static String ar_SA = "ar_SA";
    //台湾
    public static String zh_TW = "zh_TW";
    //荷兰
    public static String nl_NL = "nl_NL";
    //澳大利亚
    public static String en_AU = "en_AU";
    //加拿大 EN
    public static String en_CA = "en_CA";
    //英国
    public static String en_GB = "en_GB";
    //加拿大 FR
    public static String fr_CA = "fr_CA";
    //法国
    public static String fr_FR = "fr_FR";
    //德国
    public static String de_DE = "de_DE";
    //以色列
    public static String he_IL = "he_IL";
    //印度
    public static String hi_IN = "hi_IN";
    //意大利
    public static String it_IT = "it_IT";
    //韩国
    public static String ko_KR = "ko_KR";
    //西班牙
    public static String es_ES = "es_ES";
    //巴西
    public static String pt_BR = "pt_BR";
    //瑞典
    public static String sv_SE = "sv_SE";
    //泰国
    public static String th_TH = "th_TH";
    //泰国
    public static String th_TH_TH = "th_TH_TH";

    //默认资源标识
    public static final String DEFAULT_LANGUAGE = "en_US";

    //DES KEY
    public static final String DES_KEY = "yoxixinchungedeyongsheng";

    //acf文件加密密钥
    public static final String OTP_SERV_CLI_SECRET = "eW4&3A?Df%1S6#qT";

    //当前在线用户语言标识，与用户Session绑定，用于区别不同用户选择的语言方式(中文｜英文｜...)不同
    public static final String LANGUAGE_SESSION_KEY = "language_session_key";

    //是否修改了语言（导致会话语言与默认语言不一致）
    public static final String SET_NEW_LANGUAGE = "set_new_language";
    
    //Session标识，通过该标识取得当前在线用户实体信息
    public static final String SESSION_MARK = "session_mark";

    //当前登录用户
    public static final String CUR_LOGINUSER = "curLoginUser";

    //当前登录用户所属(拥有)的角色标识：rolemark
    public static final String CUR_LOGINUSER_ROLE = "curLoginUserRole";

    public static final String CUR_LOGINUSER_ROLE_MAP = "curLoginUserRoleMap";

    //初始化加载时数据库是否连接成功
    public static final String DATABASE_IF_CONN = "isCanConn";
    //数据库连接结果，此标识用于数据库连接异常时，退出系统的当前操作
    public static boolean DATABASE_CONN_RESULT = true;

    //初始化加载时授权是否存在并且有效
    public static final String LICENCE_IF_EFFECTIVE = "licIsNull";
    //初始化加载是否已经创建过管理检查
    public static final String DATABASE_IF_SUPERMAN = "isSuperMan";
    //是否需要修改本地IP为真实IP，true需要，false不需要
    public static final String DATABASE_IF_LOCALIP = "isLocalIp";
    //初始化记载是否已经配置过邮件服务器
    public static final String EMAILSERVER_IF_CONF = "isConfEmailServer";
    public static int DATABASE_CONN = 0;

    /**
     * 特殊的文件名
     * 数据备份时生成的指定文件
     */
    public static final String DATABAK_INFO = "info";

    /**
     * 手机令牌在线分发Action地址
     */
    public static final String MOBILE_DIST = "/mtprov.do";

    /**
     * 文件格式
     */
    public static final String FILE_XLS = ".xls";
    public static final String FILE_CSV = ".csv";
    public static final String FILE_XML = ".xml";
    public static final String FILE_PDF = ".pdf";
    public static final String FILE_HTML = ".html";
    public static final String FILE_TXT = ".txt";
    public static final String FILE_ACF = ".acf";
    public static final String FILE_BIN = ".bin";
    public static final String FILE_ZIP = ".zip";
    public static final String FILE_P12 = ".p12";
    public static final String FILE_STF = ".stf";
    public static final String FILE_SQL = ".sql";
    
    /**
     * 数据库系统版本
     */
    public static final String DB_SQL_VERSION_4_0_0 = "4.0.0";
    public static final String DB_SQL_VERSION_4_0_1 = "4.0.1";

    /**
     * OTP Center管理员日志级别
     * 0 无日志记录
     * 1 标准日志记录
     * 2 完整日志记录
     */
    public static final int ADMIN_LOG_MODE = 1;

    /**
     * 页面上几种不同方式的提示类型 alert
     */
    public static String alert_none = "none";
    public static String alert_succ = "success";
    public static String alert_error = "error";
    public static String alert_question = "question";
    public static String alert_warn = "warn";
    public static String alert_sendSucc = "sendSucc";

    /**
     * 模块标识码
     */
    public static String HOME_CODE = "00";
    public static String ADMIN_CODE = "01";
    public static String USER_CODE = "02";
    public static String TOKEN_CODE = "03";
    public static String AUTH_CODE = "04";
    public static String CONFIG_CODE = "05";
    public static String LOG_CODE = "06";
    public static String REPORT_CODE = "07";
    public static String ORGUN_CODE = "08";
    public static String MONITOR_CODE = "09";
    public static String DATA_CODE = "10";

    /**
     * 系统对外提供的dbconf.properties文件常量
     */
    public static final String DB_CONF_CONF = "/dbconf.properties"; //系统对外提供的数据库配置文件
    public static final String DB_TYPE = "db_type"; //数据库类型
    public static final String DB_PWD_ENCRYPT = "db_pwd_encrypt"; //数据库连接密码是否加密

    public static final String DB_IP = "db_0_ip"; //IP地址
    public static final String DB_PORT = "db_0_port"; //端口
    public static final String DB_NAME = "db_0_name"; //数据库名称
    public static final String DB_USERNAME = "db_0_username"; //用户名
    public static final String DB_PASSWORD = "db_0_password"; //密码

}
