/**
 *Administrator
 */
package com.ft.otp.manager.report.entity;

/**
 * 首页统计提供的 统计信息信息存取类
 *
 * @Date in May 11, 2013,1:44:40 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class ReportMessage {
    private static long userReportTime = 0; //用户统计时间
    private static long tknReportTime = 0; //令牌统计时间
    private static ReportInfo userReportData = null;//用户统计数据
    private static ReportInfo tokenReportData = null;//用户统计数据

    /**
     * @return the userReportTime
     */
    public static long getUserReportTime() {
        return userReportTime;
    }

    /**
     * @param userReportTime the userReportTime to set
     */
    public static void setUserReportTime(long userReportTime) {
        ReportMessage.userReportTime = userReportTime;
    }

    /**
     * @return the tknReportTime
     */
    public static long getTknReportTime() {
        return tknReportTime;
    }

    /**
     * @param tknReportTime the tknReportTime to set
     */
    public static void setTknReportTime(long tknReportTime) {
        ReportMessage.tknReportTime = tknReportTime;
    }

    /**
     * @return the userReportData
     */
    public static ReportInfo getUserReportData() {
        return userReportData;
    }

    /**
     * @param userReportData the userReportData to set
     */
    public static void setUserReportData(ReportInfo userReportData) {
        ReportMessage.userReportData = userReportData;
    }

    /**
     * @return the tokenReportData
     */
    public static ReportInfo getTokenReportData() {
        return tokenReportData;
    }

    /**
     * @param tokenReportData the tokenReportData to set
     */
    public static void setTokenReportData(ReportInfo tokenReportData) {
        ReportMessage.tokenReportData = tokenReportData;
    }
}
