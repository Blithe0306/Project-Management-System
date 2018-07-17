package com.ft.otp.common.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.ft.otp.common.Constant;
import com.ft.otp.common.DbConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.database.DBconnection;
import com.ft.otp.common.database.DbFactory;
import com.ft.otp.common.database.pool.DbEnv;
import com.ft.otp.util.properties.BaseProperties;
import com.ft.otp.util.tool.PwdEncTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 数据库连接池配置初始化
 * 
 * @Date in Apr 8, 2010,3:08:54 PM
 * 
 * @author TBM
 */
public class ProxoolConfig {

    private Logger logger = Logger.getLogger(ProxoolConfig.class);
    private static Logger sLogger = Logger.getLogger(ProxoolConfig.class);
    private static ConcurrentHashMap<Object, Object> configMap;
    private static ProxoolConfig config = null;
    private static BaseProperties properties;
    private static volatile boolean loadResult = false;
    private static String filePath = "";

    private ProxoolConfig() {
        configMap = new ConcurrentHashMap<Object, Object>();
        properties = new BaseProperties();
        InputStream iStream = null;
        try {
            filePath = Constant.WEB_CONFIG_PATH + Constant.DB_CONF_CONF;
            iStream = new FileInputStream(filePath);
            properties.load(iStream);
            configMap.putAll(properties);
            properties.clear();

            iStream = new FileInputStream(Constant.PROXOOL_PROPERTIES);
            properties.load(iStream);

            long beginTime = System.currentTimeMillis();
            while (true) {
                try {
                    int result = dbConnUse();
                    if (result == 0) {
                        loadResult = true;
                        return;
                    } else {
                        logger
                                .error("Config:Load/Read the dbconfig.properties file failure or can't connect to the database!");
                        if (result == -1) {
                            if ((System.currentTimeMillis() - beginTime) < 60 * 1000) {
                                Thread.sleep(5 * 1000);
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception ex) {
            logger.error("Config:Load the database pool configuration file failure!", ex);
        } finally {
            if (null != iStream) {
                try {
                    iStream.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    /**
     * 根据config.properties的配置进行数据库连接测试
     * 
     * @Date in Jun 1, 2013,2:16:00 PM
     * @return
     */
    private synchronized static int dbConnUse() {
        int result = -1;
        String dbType = getConfigValue(Constant.DB_TYPE);
        if (!StrTool.strNotNull(dbType)
                || (!StrTool.strEquals(dbType, StrConstant.common_number_0)
                        && !StrTool.strEquals(dbType, StrConstant.common_number_1)
                        && !StrTool.strEquals(dbType, StrConstant.common_number_2)
                        && !StrTool.strEquals(dbType, StrConstant.common_number_3) && !StrTool.strEquals(dbType,
                        StrConstant.common_number_4))) {
            sLogger.error("db Type not support:" + dbType);
            return result;
        }

        String driver = ""; //数据库连接驱动
        String url = ""; //数据库连接URL
        String dbUser = ""; //数据库用户名
        String dbPwd = ""; //数据库密码

        String dbIp = ""; //数据库IP地址
        String dbIp2 = ""; //Oracle RAC模式副IP
        String dbPort = ""; //数据库端口
        String dbName = ""; //数据库名称

        int i = 0;
        while (true) {
            String tempIp = Constant.DB_IP;
            String tempPort = Constant.DB_PORT;
            String tempName = Constant.DB_NAME;
            String tempUser = Constant.DB_USERNAME;
            String tempPwd = Constant.DB_PASSWORD;
            if (i > 0) {
                tempIp = tempIp.replace(StrConstant.common_number_0, String.valueOf(i));
                tempPort = tempPort.replace(StrConstant.common_number_0, String.valueOf(i));
                tempName = tempName.replace(StrConstant.common_number_0, String.valueOf(i));
                tempUser = tempUser.replace(StrConstant.common_number_0, String.valueOf(i));
                tempPwd = tempPwd.replace(StrConstant.common_number_0, String.valueOf(i));
            }
            dbIp = getConfigValue(tempIp);
            if (!StrTool.strNotNull(dbIp)) {
                sLogger.error("db IP config " + tempIp + " is empty");
                return result;
            }
            dbPort = getConfigValue(tempPort);
            if (!StrTool.strNotNull(dbPort) && i == 0) {
                sLogger.error("db port empty");
                return result;
            }
            dbName = getConfigValue(tempName);
            if (!StrTool.strNotNull(dbName) && i == 0) {
                sLogger.error("db name empty");
                return result;
            }
            dbUser = getConfigValue(tempUser);
            if (!StrTool.strNotNull(dbUser) && i == 0) {
                sLogger.error("db user empty");
                return result;
            }
            i++;

            dbPwd = getConfigValue(tempPwd);
            String pwdEnc = getConfigValue(Constant.DB_PWD_ENCRYPT);
            if (StrTool.strEqualsIgnoreCase(pwdEnc, StrConstant.common_yes) && !StrTool.strNotNull(dbPwd)) {
                sLogger.error("db pwd empty");
                return result;
            }

            if (StrTool.strEquals(dbType, StrConstant.common_number_2)) {//Oracle RAC
                if (StrTool.indexOf(dbIp, ",")) {
                    String[] arr = dbIp.split(",");
                    dbIp = arr[0];
                    dbIp2 = arr[1];
                } else {
                    sLogger.error("db IP error of Oracle RAC: not contain IP2");
                    return result;
                }
            }

            //获取Driver和Url
            String[] dbArr = DbEnv.getDbDriverUrl(dbType, dbIp, dbIp2, dbPort, dbName);
            driver = dbArr[0];
            url = dbArr[1];
            if (!StrTool.strNotNull(driver) || !StrTool.strNotNull(url)) {
                sLogger.error("get driver and url failure");
                return result;
            }

            boolean decResult = false;
            byte[] bKey = DbEnv.genPwdEncKey();
            if (StrTool.strEqualsIgnoreCase(pwdEnc, StrConstant.common_yes)) {
                //解密数据库连接密码
                String decPwd = PwdEncTool.decDbPasswd(dbPwd, bKey);
                if (StrTool.strNotNull(decPwd)) {
                    dbPwd = decPwd;
                    decResult = true;
                }
            }

            //连接测试
            result = DBconnection.testDBConn(driver, url, dbUser, dbPwd, true);
            if (result != 0) {
                if (StrTool.strEquals(dbType, StrConstant.common_number_2)) {
                    return result;
                }

                continue; //继续连接下一个数据库
            } else {
                properties.setProperty(DbConstant.DRIVER_CLASS, driver);
                properties.setProperty(DbConstant.DRIVER_URL, url);
                properties.setProperty(DbConstant.USER, dbUser);
                properties.setProperty(DbConstant.PASSWORD, dbPwd);

                if (StrTool.strEqualsIgnoreCase(dbType, StrConstant.common_number_0)
                        || StrTool.strEqualsIgnoreCase(dbType, StrConstant.common_number_1)
                        || StrTool.strEqualsIgnoreCase(dbType, StrConstant.common_number_2)) {
                    properties.setProperty(DbConstant.PROXOOL_TEST_BEFORE_USE, "true");
                    if (StrTool.strEqualsIgnoreCase(dbType, StrConstant.common_number_1)
                            || StrTool.strEqualsIgnoreCase(dbType, StrConstant.common_number_2)) {
                        properties.setProperty(DbConstant.HOUSE_KEEPING_TEST_SQL, "select CURRENT_DATE from dual");
                        if (StrTool.strEqualsIgnoreCase(dbType, StrConstant.common_number_2)) {
                            properties.setProperty(DbConstant.ORACLE_RAC_MODE, StrConstant.common_y);
                        } else {
                            properties.setProperty(DbConstant.ORACLE_RAC_MODE, StrConstant.common_n);
                        }
                    } else {
                        properties.setProperty(DbConstant.HOUSE_KEEPING_TEST_SQL, "select CURRENT_DATE");
                    }
                }

                if (StrTool.strEqualsIgnoreCase(pwdEnc, StrConstant.common_yes) && !decResult) {
                    //对dbconf文件密码进行加密处理
                    outPutDbConf(tempPwd, dbPwd);
                }

                return result;
            }
        }
    }

    /**
     * 输出dbconf文件的密码加密串
     * 
     * @Date in Jul 5, 2013,10:28:41 AM
     * @param dbPwd
     */
    private static void outPutDbConf(String pwdKey, String dbPwd) {
        BaseProperties properties = new BaseProperties();
        InputStream iStream = null;
        OutputStream outStream = null;
        try {
            iStream = new FileInputStream(filePath);
            properties.load(iStream);
            outStream = new FileOutputStream(filePath);

            //加密数据库连接密码
            String encPwd = PwdEncTool.encDbPasswd(dbPwd, DbEnv.genPwdEncKey());
            if (StrTool.strNotNull(encPwd)) {
                dbPwd = encPwd;
            } else {
                return;
            }

            properties.setProperty(pwdKey, dbPwd);
            properties.store(outStream, null);
        } catch (Exception e) {
            sLogger.error("Read/Write the dbconf.properties file failure!", e);
        } finally {
            if (null != iStream) {
                try {
                    iStream.close();
                } catch (IOException ex) {
                }
            }
            if (null != outStream) {
                try {
                    outStream.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    public static ProxoolConfig loadDBConfig() {
        if (config != null) {
            return config;
        }

        synchronized (ProxoolConfig.class) {
            if (config == null) {
                config = new ProxoolConfig();
            }
            return config;
        }
    }

    /**
     * 切换连接成功的数据库
     * 
     * @Date in Jun 1, 2013,5:26:49 PM
     * @return
     */
    public synchronized static boolean databaseShift() {
        return dbConnUse() == 0;
    }

    /**
     * 取得key对应的Properties文件中的值
     * 
     * @Date in Apr 8, 2010,3:52:41 PM
     * @param key
     * @return
     */
    public static String getProperties(String key) {
        return properties.getProperty(key);
    }

    /**
     * 返回一个Properties
     */
    public static Properties getProperties() {
        if (properties == null) {
            loadDBConfig();
        }

        return properties;
    }

    /**
     * 根据key取得configMap Value
     * 
     * @Date in Mar 30, 2010,4:26:51 PM
     * @param key
     * @return
     */
    public static String getConfigValue(String key) {
        if (!StrTool.strNotNull(key)) {
            return null;
        }
        if (!StrTool.mapNotNull(configMap)) {
            return null;
        }

        return (String) configMap.get(key);
    }

    /**
     * 加载结果
     * 
     * @Date in Feb 19, 2013,11:25:02 AM
     * @return
     */
    public static boolean getLoadResult() {
        return loadResult;
    }

    /**
     * 清空Map，重置config
     * 
     * @Date in Mar 30, 2010,4:27:29 PM
     */
    public static void destroyed() {
        if (null != config) {
            configMap.clear();
            properties.clear();
            config = null;
            loadResult = false;

            DbFactory.destroyed();//销毁数据库连接池
        }
    }

    /**
     * 重新加载
     * 
     * @Date in Jun 6, 2013,3:46:46 PM
     */
    public synchronized static void reload() {
        destroyed();
        loadDBConfig();
    }

}
