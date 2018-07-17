/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 * @Date Aug 26, 2014,7:26:30 PM
 */
package com.ft.otp.common.config;

import java.io.File;

import org.apache.log4j.Logger;

import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.DbConstant;
import com.ft.otp.common.database.DBconnection;
import com.ft.otp.common.database.pool.DbEnv;
import com.ft.otp.manager.confinfo.config.entity.DBConfInfo;
import com.ft.otp.util.tool.StrTool;


/**
 * 数据库升级配置
 * @Date Aug 26, 2014,7:26:30 PM
 * @version v1.0
 * @author YXS
 */
public class DBUpGradeConfig {
    
    private Logger logger = Logger.getLogger(DBUpGradeConfig.class);
    private static DBUpGradeConfig config = null;
    private DBConfInfo dbConfInfo;
    
    private DBUpGradeConfig() {
        boolean upresult = false;
        try {
            dbConfInfo = DBConfInfo.getDbConfInfo();
            if (!StrTool.objNotNull(dbConfInfo)) {
                return;
            }
            //添加URL和驱动
            if (!addDiver()) {
                return;
            }
            String driver = dbConfInfo.getDriver();
            String url = dbConfInfo.getUrl();
            String dbUser = dbConfInfo.getUsername();
            String dbPwd = ProxoolConfig.getProperties(DbConstant.PASSWORD);
            //连接测试
            int result = DBconnection.testDBConn(driver, url, dbUser, dbPwd, true);
            if (result != 0) {
                return;
            }
            
            //当前数据库版本
            String cversion = ConfConfig.getConfValue(ConfConstant.CONF_TYPE_COMMON + "_"
                    + ConfConstant.SQLS_VERSION);
            //数据库类型
            String dbType = dbConfInfo.getDbtype();
            if (!StrTool.strNotNull(cversion) || !StrTool.strNotNull(dbType)) {
                return;
            }
            String upSqlFileStr = upgradeSqlStr(dbType, cversion);
            if (!StrTool.strNotNull(upSqlFileStr)) {
                return;
            }
            //针对数据库升级脚本路径
            String path = Constant.WEB_UPGRADE_SQL_FILE_PATH;
            String upSqlPath = path + upSqlFileStr;
            
            //公共升级脚本
            String commUpPath = "";
            if (StrTool.strEquals(cversion, Constant.DB_SQL_VERSION_4_0_0)) {
                commUpPath = path + "otpdb_upgrade_v4.0.0.sql";
            } else if (StrTool.strEquals(cversion, Constant.DB_SQL_VERSION_4_0_1)) {
                commUpPath = path + "otpdb_upgrade_v4.0.1.sql";
            }
            
            File upSqlFile = new File(upSqlPath);
            File commUpFile = new File(commUpPath);
            if (!upSqlFile.exists() || !commUpFile.exists()) { //未找到对应的SQL升级脚本文件！
                return;
            }
            
            // 检查是否需要升级表结构（4.2恢复4.0或4.0.1的数据）
            if(DBconnection.checkTableSql(driver, url, dbUser, dbPwd)){
            	upresult = DBconnection.importDBTable(driver, url, dbUser, dbPwd, upSqlPath);
                if (!upresult) {
                    return;
                }
            }
            upresult = DBconnection.importDBTable(driver, url, dbUser, dbPwd, commUpPath);
            
            ConfConfig.reLoad();
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
        }
        
    }
    
    /**
     * 根据数据库版本及数据库类型选择升级脚本
     * @Date   Aug 27, 2014,2:37:56 PM
     * @param dbtype
     * @param sqlversion
     * @return String
     */
    public String upgradeSqlStr(String dbtype, String sqlversion) {
        String sqlFile = "";
        //4.0.0版本对应升级脚本
        if (StrTool.strEquals(sqlversion, Constant.DB_SQL_VERSION_4_0_0)) {
            if (DbConstant.DB_TYPE_MYSQL.equalsIgnoreCase(dbtype)) {
                sqlFile = "otpdb_upgrade_v4.0.0_mysql.sql";
            } else if (DbConstant.DB_TYPE_POSTGRESQL.equalsIgnoreCase(dbtype)) {
                sqlFile = "otpdb_upgrade_v4.0.0_postgresql.sql";
            } else if (DbConstant.DB_TYPE_ORACLE.equalsIgnoreCase(dbtype)) {
                sqlFile = "otpdb_upgrade_v4.0.0_oracle.sql";
            } else if (DbConstant.DB_TYPE_SQLSERVER.equalsIgnoreCase(dbtype)) {
                sqlFile = "otpdb_upgrade_v4.0.0_sqlserver.sql";
            }
        //4.0.1版本对应升级脚本
        } else if (StrTool.strEquals(sqlversion, Constant.DB_SQL_VERSION_4_0_1)) {
            if (DbConstant.DB_TYPE_MYSQL.equalsIgnoreCase(dbtype)) {
                sqlFile = "otpdb_upgrade_v4.0.1_mysql.sql";
            } else if (DbConstant.DB_TYPE_POSTGRESQL.equalsIgnoreCase(dbtype)) {
                sqlFile = "otpdb_upgrade_v4.0.1_postgresql.sql";
            } else if (DbConstant.DB_TYPE_ORACLE.equalsIgnoreCase(dbtype)) {
                sqlFile = "otpdb_upgrade_v4.0.1_oracle.sql";
            } else if (DbConstant.DB_TYPE_SQLSERVER.equalsIgnoreCase(dbtype)) {
                sqlFile = "otpdb_upgrade_v4.0.1_sqlserver.sql";
            }
        }
        return sqlFile;
    }
    
    /**
     * 设置数据库连接信息到实体对象
     * @param dbtype
     * @param dbip
     * @param dbport
     * @param dbname
     * @param dbuser
     * @param dbpassword
     * @return
     */
    private boolean addDiver() {
        String dbType = dbConfInfo.getDbtype();
        String ip = dbConfInfo.getIp();
        String ip2 = dbConfInfo.getViceip();
        String port = dbConfInfo.getPort();
        String dbName = dbConfInfo.getDbname();

        dbType = DbEnv.getDbTypeNum(dbType, dbConfInfo.getDual());
        String[] retArr = DbEnv.getDbDriverUrl(dbType, ip, ip2, port, dbName);
        if (!StrTool.strNotNull(retArr[0]) || !StrTool.strNotNull(retArr[1])) {
            return false;
        }

        dbConfInfo.setDriver(retArr[0]);
        dbConfInfo.setUrl(retArr[1]);

        return true;
    }
    
    //加载
    public static DBUpGradeConfig loadDbUpGradeConfig() {
        if (config != null) {
            return config;
        }
        synchronized (DBUpGradeConfig.class) {
            if (config == null) {
                config = new DBUpGradeConfig();
            }
            return config;
        }
    }
    
    /**
     * 清空Map，重置config
     * @author LXH
     * @date Dec 2, 2014 2:40:30 PM
     */
    public static void clear() {
        if (null != config) {
            config = null;
        }
    }
    
}
