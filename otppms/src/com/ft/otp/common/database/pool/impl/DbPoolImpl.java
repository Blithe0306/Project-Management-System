package com.ft.otp.common.database.pool.impl;

import java.sql.Connection;
import java.sql.DriverManager;

import org.logicalcobwebs.proxool.ProxoolFacade;
import org.logicalcobwebs.proxool.configuration.PropertyConfigurator;

import com.ft.otp.common.DbConstant;
import com.ft.otp.common.config.ProxoolConfig;
import com.ft.otp.common.database.pool.DbEnv;
import com.ft.otp.common.database.pool.DbPool;

/**
 * 连接池具体实现
 *
 * @Date in Apr 8, 2010,1:01:54 PM
 *
 * @author TBM
 */
public class DbPoolImpl implements DbPool {

    public void destroy() {
        ProxoolFacade.shutdown(0);
    }

    public synchronized void freeConnection(Connection conn) throws Exception {
        if (conn != null) {
            conn.close();
        }
    }

    public synchronized Connection getConnection() throws Exception {
        Connection conn = null;

        String alias = ProxoolConfig.getProperties(DbConstant.ALIAS);

        Class.forName(DbConstant.DB_CLASS_NAME);
        conn = DriverManager.getConnection(DbConstant.DB_POOL_NAME + alias);

        return conn;
    }

    public void init() throws Exception {
        PropertyConfigurator.configure(DbEnv.getProperties());
        //加载.xml文件
        //JAXPConfigurator.configure("E:\\MyWork\\QuerySystem\\src\\proxool.xml", false);
    }

}