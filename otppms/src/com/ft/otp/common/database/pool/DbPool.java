package com.ft.otp.common.database.pool;

import java.sql.Connection;

/**
 * 数据库连接池接口
 *
 * @Date in Apr 8, 2010,11:21:08 AM
 *
 * @author TBM
 */
public interface DbPool {

    /**
     * 初始化连接池
     * @Date in Apr 8, 2010,4:12:27 PM
     */
    void init() throws Exception;

    /**
     * 取得数据库连接
     * @Date in Apr 8, 2010,1:05:00 PM
     * @return
     */
    Connection getConnection() throws Exception;

    /**
     * 释放数据库连接
     * @Date in Apr 8, 2010,1:06:02 PM
     * @param conn
     */
    void freeConnection(Connection conn) throws Exception;

    /**
     * 销毁数据库连接池
     * @Date in Apr 8, 2010,1:06:43 PM
     */
    void destroy();

}