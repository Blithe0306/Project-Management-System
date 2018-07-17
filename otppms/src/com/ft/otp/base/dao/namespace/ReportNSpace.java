/**
 *Administrator
 */
package com.ft.otp.base.dao.namespace;

/**
 * 报表命名空间类功能说明
 *
 * @Date in Apr 6, 2011,4:05:59 PM
 *
 * @author TBM
 */
public class ReportNSpace {
    // 命名空间
    public static final String REPORT_INFO_NS = "report_info";

    // 业务报表——认证服务器认证同步量统计
    public static final String REPORT_INFO_QUERY_AUTH_COUNT = "reportAuthCount";
    // 业务报表——认证服务器时段认证量统计
    public static final String REPORT_INFO_QUERY_AUTH_COUNT_TIME = "reportAuthCountByTime";
    // 业务报表——用户门户访问量统计
    public static final String REPORT_INFO_QUERY_PORTAL_COUNT = "reportPortalCount";

    // 用户报表——数量统计
    public static final String REPORT_INFO_QUERY_USER_COUNT = "reportUserCount";
    // 首先——用户数量统计
    public static final String REPORT_INFO_QUERY_USER_COUNT_MAIN = "reportUserCountToMain";
    // 用户报表——认证类型用户数统计
    public static final String REPORT_INFO_QUERY_USER_COUNT_AUTHTYPE = "reportUserCountByAuthType";

    // 令牌报表——状态统计
    public static final String REPORT_INFO_QUERY_TOKEN_COUNT_STATE = "reportTknCountByState";
    // 令牌报表——启用应急口令统计
    public static final String REPORT_INFO_QUERY_TOKEN_COUNT_EMPIN = "reportTknCountByEmpin";
    // 令牌报表——过期时间统计
    public static final String REPORT_INFO_QUERY_TOKEN_COUNT_EXPIRED = "reportTknCountByExpired";

    // 预警统计——查询将要过期的令牌
    public static final String REPORT_INFO_QUERY_TOKEN_BY_EXPIRED = "reportTknsByExpired";
    // 预警统计——统计绑定令牌的数量
    public static final String REPORT_COUNT_BIND_TOKEN = "reportbindTknCount";
    // 预警统计——统计一段时间内令牌的同步次数
    public static final String REPORT_COUNT_SYN_TOKEN_BY_TIME = "reportSynTknCountByTime";

    // 导出获取数据相关
    public static final String EXPORT_REPORT_AUTH_SYNC = "selectAuthSync";
    public static final String EXPORT_REPORT_USER_COUNT = "selectUserCount";
    public static final String EXPORT_REPORT_USER_AUTHTYPE = "selectUserAuthType";
    public static final String EXPORT_REPORT_TOKEN_COUNT = "selectStateToken";
    public static final String EXPORT_REPORT_TOKEN_EXPIRED = "selectTokenByExpired";
}
