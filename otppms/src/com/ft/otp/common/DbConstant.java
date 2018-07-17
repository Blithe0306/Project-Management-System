package com.ft.otp.common;

/**
 * 数据库连接池常量类
 *
 * @Date in Apr 8, 2010,3:36:06 PM
 *
 * @author TBM
 */
public class DbConstant {
    /**
     * 数据库连接池别名
     */
    //池名称前缀
    public static final String DB_POOL_NAME = "proxool.";

    //池名称
    public static final String DB_POOL_NAME_TO = "DBPool";

    //数据库密码加密密钥
    public static String DB_PASS_ENCRYPT_KEY = "FAF6686D630E307E8EE7DEFAB310DBD5";

    //ProxoolDriver
    public static final String DB_CLASS_NAME = "org.logicalcobwebs.proxool.ProxoolDriver";

    /**
     * 相关连接信息
     */
    //连接池标识(别名)
    public static final String ALIAS = "jdbc-0.proxool.alias";

    //连接URL
    public static final String DRIVER_URL = "jdbc-0.proxool.driver-url";

    //数据库驱动
    public static final String DRIVER_CLASS = "jdbc-0.proxool.driver-class";

    //数据库用户
    public static final String USER = "jdbc-0.user";

    //密码
    public static final String PASSWORD = "jdbc-0.password";

    //允许最大连接数，超过了这个连接，再有请求时，就排在队列中
    public static final String MAXIMUM_CONNECTION_COUNT = "jdbc-0.proxool.maximum-connection-count";

    //最小连接数
    public static final String MINIMUM_CONNECTION_COUNT = "jdbc-0.proxool.minimum-connection-count";

    //最少保持的空闲连接数
    public static final String MINIMUM_PROTOTYPE_COUNT = "jdbc-0.proxool.prototype-count";

    //自动侦察各个连接状态的时间间隔(毫秒),侦察到空闲的连接就马上回收,超时的销毁
    public static final String HOUSE_KEEPING_SLEEP_TIME = "jdbc-0.proxool.house-keeping-sleep-time";

    //单个连接最大存活时间
    public static final String MAXIMUM_ACTIVE_TIME = "jdbc-0.proxool.maximum-active-time";

    //如果为true,那么每个被执行的SQL语句将会在执行期被log记录
    public static final String TRACE = "jdbc-0.proxool.trace";

    //日志统计跟踪类型，参数“ERROR”或“INFO”
    public static final String PROXOOL_STATISTICS_LOG_LEVEL = "jdbc-0.proxool.statistics-log-level";

    //连接池使用状况统计。 参数“10s,1m,1d”
    public static final String PROXOOL_STATISTICS = "jdbc-0.proxool.statistics";

    //在使用之前是否进行测试 true | false
    public static final String PROXOOL_TEST_BEFORE_USE = "jdbc-0.proxool.testBeforeUse";

    //设置自动重连
    public static final String HOUSE_KEEPING_TEST_SQL = "jdbc-0.proxool.house-keeping-test-sql";

    //Oracle数据库是否使用了RAC连接模式，y是，n否，只针对Oracle数据库
    public static final String ORACLE_RAC_MODE = "jdbc-0.rac.mode";

    /**
     * 数据库驱动
     */
    public static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
    public static final String SQLSERVER_DRIVER = "net.sourceforge.jtds.jdbc.Driver";
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";
    public static final String DB2_DRIVER = "com.ibm.db2.jcc.DB2Driver";
    public static final String SYBASE_DRIVER = "com.sybase.jdbc3.jdbc.SybDriver";

    /**
     * 数据库类型
     */
    public static final String DB_TYPE_ORACLE = "oracle";
    public static final String DB_TYPE_SQLSERVER = "sqlserver";
    public static final String DB_TYPE_MSSQL = "mssql";
    public static final String DB_TYPE_POSTGRESQL = "postgresql";
    public static final String DB_TYPE_MYSQL = "mysql";
    public static final String DB_TYPE_DB2 = "db2";
    public static final String DB_TYPE_SYBASE = "sybase";

    /**
     * 支持分页备份的表名
     */
    public static final String DB_TABLE_NAME_TOKENINFO = "otppms_tokeninfo";
    public static final String DB_TABLE_NAME_LOGINFO = "otppms_loginfo";
    public static final String DB_TABLE_NAME_ADMINLOG = "otppms_admin_log";

}
