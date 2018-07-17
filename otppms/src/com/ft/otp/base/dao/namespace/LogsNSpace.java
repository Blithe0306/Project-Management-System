/**
 *Administrator
 */
package com.ft.otp.base.dao.namespace;

/**
 * 命名空间和引用标识类功能说明
 *
 * @Date in Apr 6, 2011,4:05:43 PM
 *
 * @author TBM
 */
public class LogsNSpace {

    /**
     * 管理员日志
     */
    //命名空间
    //管理员日志命名空间
    public static final String ADMIN_LOG_NS = "admin_log";
    //用户日志命名空间
    public static final String USER_LOG_NS = "user_log";

    //引用标识
    //统计管理员日志数量
    public static final String ADMIN_LOG_COUNT_AL = "countAL";
    //查询管理员日志
    public static final String ADMIN_LOG_SELECT_AL = "selectAL";
    //添加管理员日志
    public static final String ADMIN_LOG_INSERT_AL = "insertAL";
    //查看管理员日志
    public static final String ADMIN_LOG_FIND_AL = "findAL";
    //删除管理员日志
    public static final String ADMIN_LOG_DELETE = "deleleLog";

    /**
     * 用户日志
     */

    //引用标识
    //统计用户日志数量
    public static final String USER_LOG_COUNT_UL = "countUL";
    //查询用户日志
    public static final String USER_LOG_SELECT_UL = "selectUL";
    //查看用户日志
    public static final String USER_LOG_FIND_UL = "findUL";
    //删除用户日志
    public static final String USER_LOG_DELETE = "deleleLog";

}
