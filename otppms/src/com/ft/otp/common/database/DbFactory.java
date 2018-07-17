package com.ft.otp.common.database;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.logicalcobwebs.proxool.ProxoolDataSource;

import com.ft.otp.common.Constant;
import com.ft.otp.common.config.ProxoolConfig;
import com.ft.otp.common.database.pool.DbPool;
import com.ft.otp.common.database.pool.impl.DbPoolImpl;

/**
 * 
 * 数据库连接工厂
 *
 * @Date in Apr 2, 2011,5:51:44 PM
 *
 * @author TBM
 */
public class DbFactory extends ProxoolDataSource {

    private Logger logger = Logger.getLogger(DbFactory.class);
    private static DbPool dbPool = new DbPoolImpl();
    private static volatile boolean shiftPower = false;

    public DbFactory() {

    }

    /**
     * 初始化连接池
     * 
     * @Date in Apr 8, 2010,4:59:27 PM
     * @return
     */
    public void init() {
        try {
            dbPool.init();
        } catch (Exception ex) {
            logger.error("Database Pool init Failed:", ex);
        }
    }

    /**
     * 返回连接池中的数据库连接
     * 
     * @Date in Apr 8, 2010,5:05:37 PM
     * @return
     * @throws PoolException
     */
    public Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            conn = dbPool.getConnection();
            if (null != conn) {
                Constant.DATABASE_CONN_RESULT = true;
            }
        } catch (Exception ex) {
            logger.error("DataBase Get Pool Connection Error:" + ex.getMessage());
            if (!shiftPower) {
                shiftPower = true;
                if (ProxoolConfig.databaseShift()) {
                    destroyed();
                    init();
                } else {
                    Constant.DATABASE_CONN_RESULT = false;
                }
                shiftPower = false;
            }
        }

        return conn;
    }

    /**
     * 释放数据库连接
     * 
     * @Date in Apr 8, 2010,5:07:14 PM
     * @param conn
     * @throws PoolException
     */
    public void freeConnection(Connection conn) {
        try {
            dbPool.freeConnection(conn);
        } catch (Exception ex) {
            logger.error("DataBase Free Pool Connection Error:", ex);
        }
    }

    /**
     * 销毁数据库连接池
     * 
     * @Date in Apr 8, 2010,6:32:50 PM
     */
    public static void destroyed() {
        dbPool.destroy();
    }

}