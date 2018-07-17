package com.ft.otp.common.database.pool;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ft.otp.common.Constant;
import com.ft.otp.common.DbConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.config.ProxoolConfig;
import com.ft.otp.manager.confinfo.config.entity.DBConfInfo;
import com.ft.otp.util.alg.AlgHelper;
import com.ft.otp.util.properties.BaseProperties;
import com.ft.otp.util.tool.PwdEncTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 连接池配置信息设置
 *
 * @Date in Apr 8, 2010,11:20:59 AM
 *
 * @author TBM
 */
public class DbEnv {

    private static Logger logger = Logger.getLogger(DbEnv.class);
    private static String dbtype;

    /**
     * 返回proxool数据库连接配置信息
     * @Date in Apr 7, 2011,7:54:26 PM
     * @return
     */
    public static Properties getProperties() {
        Properties properties = ProxoolConfig.getProperties();
        return properties;
    }

    /**
     * 返回一个数据库类型
     * @Date in Apr 6, 2011,9:39:21 AM
     * @return
     */
    public static String getDbtype() {
        if (StrTool.strNotNull(dbtype)) {
            return dbtype;
        } else {
            return loadDBType();
        }
    }

    /**
     * 取得数据库类型
     * @Date in Apr 6, 2011,9:36:06 AM
     * @return
     */
    private static String loadDBType() {
        try {
            Properties properties = ProxoolConfig.getProperties();
            String value = null;
            Collection<Object> values = properties.values();
            for (Iterator<Object> it = values.iterator(); it.hasNext();) {
                String v = (String) it.next();
                if (v.startsWith("jdbc") && v.split(":").length >= 4) {
                    value = v;
                }
            }
            if (value == null) {
                return "";
            }
            if (value.indexOf("sqlserver") != -1) {
                return "mssql";
            } else if (value.indexOf("mysql") != -1) {
                return "mysql";
            } else if (value.indexOf("oracle") != -1) {
                return "oracle";
            } else if (value.indexOf("postgresql") != -1) {
                return "postgresql";
            } else if (value.indexOf("db2") != -1) {
                return "db2";
            } else if (value.indexOf("sybase") != -1) {
                return "sybase";
            } else {
                return "";
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "";
        }
    }

    /**
     * 根据数据库类型返回对应的配置数数值
     * 
     * @Date in Jul 5, 2013,8:48:49 AM
     * @param dbType
     * @param racMode
     * @return
     */
    public static String getDbTypeNum(String dbType, String racMode) {
        if (StrTool.strEqualsIgnoreCase(dbType, DbConstant.DB_TYPE_ORACLE)) {
            if (StrTool.strEqualsIgnoreCase(racMode, StrConstant.common_y)) {
                return StrConstant.common_number_2;
            } else {
                return StrConstant.common_number_1;
            }
        } else if (StrTool.strEqualsIgnoreCase(dbType, DbConstant.DB_TYPE_SQLSERVER)) {
            return StrConstant.common_number_3;
        } else if (StrTool.strEqualsIgnoreCase(dbType, DbConstant.DB_TYPE_POSTGRESQL)) {
            return StrConstant.common_number_4;
        } else {
            return StrConstant.common_number_0;
        }
    }

    /**
     * 组织数据库的驱动和URL，进行数据库连接调用
     * 
     * @Date in Apr 3, 2013,11:53:32 AM
     * @param dbType
     * @param ip
     * @param port
     * @param dbName
     * @return
     */
    public static String[] getDbDriverUrl(String dbType, String ip, String ip2, String port, String dbName) {
        String driver = "";
        String url = "";
        String[] arr = new String[2];

        if (StrTool.strEquals(StrConstant.common_number_0, dbType)) { //mySql
            driver = DbConstant.MYSQL_DRIVER;
            url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
        } else if (StrTool.strEquals(StrConstant.common_number_1, dbType)) { //Oracle
            driver = DbConstant.ORACLE_DRIVER;
            url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + dbName;
        } else if (StrTool.strEquals(StrConstant.common_number_2, dbType)) { //Oracle RAC
            driver = DbConstant.ORACLE_DRIVER;
            if (StrTool.strNotNull(ip2)) {
                url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=" + ip + ")(PORT=" + port
                        + "))(ADDRESS=(PROTOCOL=TCP)(HOST=" + ip2 + ")(PORT=" + port
                        + "))(LOAD_BALANCE=yes)(FAILOVER=ON)(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=" + dbName
                        + ")))";
            } else {
                url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + dbName;
            }
        } else if (StrTool.strEquals(StrConstant.common_number_3, dbType)) { //sqlServer
            driver = DbConstant.SQLSERVER_DRIVER;
            url = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName;
        } else if (StrTool.strEquals(StrConstant.common_number_4, dbType)) { //postgreSql
            driver = DbConstant.POSTGRESQL_DRIVER;
            url = "jdbc:postgresql://" + ip + ":" + port + "/" + dbName;
        }

        arr[0] = driver;
        arr[1] = url;

        return arr;
    }

    /**
     * 数据库连接配置信息写入dbconf文件
     * 
     * @Date in Jul 5, 2013,2:22:02 PM
     * @param dbConfInfo
     * @param filePath
     * @return
     */
    public static boolean dbConfToProperties(DBConfInfo dbConfInfo, String filePath) {
        if (!StrTool.strNotNull(filePath)) {
            return false;
        }

        //连接池配置信息写入文件
        Properties bps = new BaseProperties();
        InputStream iStream = null;
        OutputStream ous = null;
        try {
            iStream = new FileInputStream(filePath);
            bps.load(iStream);
            ous = new FileOutputStream(filePath);

            bps.setProperty(Constant.DB_TYPE, getDbTypeNum(dbConfInfo.getDbtype(), dbConfInfo.getDual()));
            String pwdEnc = ProxoolConfig.getConfigValue(Constant.DB_PWD_ENCRYPT);
            bps.setProperty(Constant.DB_PWD_ENCRYPT, pwdEnc);

            bps.setProperty(Constant.DB_IP, dbConfInfo.getIp());
            bps.setProperty(Constant.DB_PORT, dbConfInfo.getPort());
            bps.setProperty(Constant.DB_NAME, dbConfInfo.getDbname());
            bps.setProperty(Constant.DB_USERNAME, dbConfInfo.getUsername());

            String password = dbConfInfo.getPasswd();
            if (StrTool.strEqualsIgnoreCase(pwdEnc, StrConstant.common_yes)) {
                byte[] bKey = DbEnv.genPwdEncKey();

                //加密数据库连接密码
                String encPwd = PwdEncTool.encDbPasswd(password, bKey);
                if (StrTool.strNotNull(encPwd)) {
                    password = encPwd;
                }
            }
            bps.setProperty(Constant.DB_PASSWORD, password);

            bps.store(ous, null);
            return true;
        } catch (Exception ex) {
            logger.error("OutputStream Error:Proxool config output failed!", ex);
        } finally {

            try {
                if (null != iStream) {
                    iStream.close();
                }
                if (ous != null) {
                    ous.close();
                }
            } catch (Exception ex) {
            }
        }

        return false;//连接池配置信息写入文件失败
    }

    /**
     * 数据库密码加密密钥
     * 
     * @Date in Jul 5, 2013,9:24:52 AM
     * @param dbUser
     * @return
     */
    public static byte[] genPwdEncKey() {
        return AlgHelper.hexStringToBytes(DbConstant.DB_PASS_ENCRYPT_KEY);
    }

}
