/**
 *Administrator
 */
package com.ft.otp.base.dao.namespace;

/**
 * 监控预警管理命名空间
 *
 * @Date in Mar 5, 2013,1:22:07 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class MonitorNSpace {
    // 监控预警
    public static final String MONITOR_INFO_NS = "monitor_info";

    // 添加预警类型&管理员
    public static final String MONITOR_ADMIN_INFO_ADD = "addMonitorAndAdminInfo";
    // 更新预警类型&管理员
    public static final String MONITOR_ADMIN_INFO_UPDATE = "upMonitorAndAdminInfo";
    // 删除预警类型&管理员
    public static final String MONITOR_ADMIN_INFO_DELETE = "delMonitorAndAdminInfo";
    // 查询预警类型&管理员
    public static final String MONITOR_ADMIN_INFO_QUERY = "queryMonitorAndAdminInfo";

    // 添加预警信息
    public static final String MONITOR_INFO_ADD = "addMonitorInfo";
    // 更新预警信息
    public static final String MONITOR_INFO_UPDATE = "updateMonitorInfo";
    // 删除预警信息
    public static final String MONITOR_INFO_DELETE = "deleteMonitorInfo";
    // 查询预警信息
    public static final String MONITOR_INFO_QUERY = "queryMonitorInfo";
    // 查询预警监控信息(不含IsSend属性)
    public static final String MONITOR_INFO_QUERY_OK = "queryMonitorOkInfo";
    // 查询某一类预警信息 最新发送的一条
    public static final String MONITOR_INFO_QUERY_MAX_SENDTIME = "queryMaxSendtimeMonitorInfo";
}
