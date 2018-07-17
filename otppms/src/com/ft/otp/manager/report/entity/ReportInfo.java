/**
 *Administrator
 */
package com.ft.otp.manager.report.entity;

/**
 * 报表管理数据实体类说明
 *
 * @Date in Jan 30, 2013,2:16:04 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class ReportInfo {
    /**
     * 业务报表
     */
    private long authSumCount;// 认证总量
    private long authSuccessCount;// 认证成功量
    private long authFaildCount;// 认证失败量

    private long syncSumCount;// 同步的总量
    private long syncSuccessCount;// 同步成功量
    private long syncFaildCount;// 同步失败量

    private long accessSumCount;// 访问总量
    private long accessSuccessCount;// 访问成功量
    private long accessFaildCount;// 访问失败量
    /**
     * 用户报表
     */
    private long commonLong1;// 公用数量1  新增用户数/使用静态密码认证用户数
    private long commonLong2;// 公用数量2  绑定用户数/使用OTP认证用户数
    private long commonLong3;// 公用数量3  锁定用户数/使用静态密码+OTP认证用户数
    private long commonLong4;// 公用数量4  不进行本地认证用户数量
    /**
     * 令牌报表
     */
    private long tokenSumCount;// 令牌总量
    private long bindCount;// 绑定数量
    private long enableTknCount;// 启用数量
    private long lockTknCount;// 锁定数量
    private long loseTknCount;// 挂失数量
    private long invalidTknCount;// 作废数量

    private long enableEmpin;// 启用应急口令量

    private long weekExpiredTnkCount;// 一周内过期数量
    private long monthExpiredTnkCount;// 一月内过期数量
    private long quarterExpiredTnkCount;// 一季度内过期数量
    private long moreExpiredTnkCount;// 大于三个月过期数量

    /**
     * @return the authSumCount
     */
    public long getAuthSumCount() {
        return authSumCount;
    }

    /**
     * @param authSumCount the authSumCount to set
     */
    public void setAuthSumCount(long authSumCount) {
        this.authSumCount = authSumCount;
    }

    /**
     * @return the authSuccessCount
     */
    public long getAuthSuccessCount() {
        return authSuccessCount;
    }

    /**
     * @param authSuccessCount the authSuccessCount to set
     */
    public void setAuthSuccessCount(long authSuccessCount) {
        this.authSuccessCount = authSuccessCount;
    }

    /**
     * @return the authFaildCount
     */
    public long getAuthFaildCount() {
        return authFaildCount;
    }

    /**
     * @param authFaildCount the authFaildCount to set
     */
    public void setAuthFaildCount(long authFaildCount) {
        this.authFaildCount = authFaildCount;
    }

    /**
     * @return the syncSumCount
     */
    public long getSyncSumCount() {
        return syncSumCount;
    }

    /**
     * @param syncSumCount the syncSumCount to set
     */
    public void setSyncSumCount(long syncSumCount) {
        this.syncSumCount = syncSumCount;
    }

    /**
     * @return the syncSuccessCount
     */
    public long getSyncSuccessCount() {
        return syncSuccessCount;
    }

    /**
     * @param syncSuccessCount the syncSuccessCount to set
     */
    public void setSyncSuccessCount(long syncSuccessCount) {
        this.syncSuccessCount = syncSuccessCount;
    }

    /**
     * @return the syncFaildCount
     */
    public long getSyncFaildCount() {
        return syncFaildCount;
    }

    /**
     * @param syncFaildCount the syncFaildCount to set
     */
    public void setSyncFaildCount(long syncFaildCount) {
        this.syncFaildCount = syncFaildCount;
    }

    /**
     * @return the accessSumCount
     */
    public long getAccessSumCount() {
        return accessSumCount;
    }

    /**
     * @param accessSumCount the accessSumCount to set
     */
    public void setAccessSumCount(long accessSumCount) {
        this.accessSumCount = accessSumCount;
    }

    /**
     * @return the accessSuccessCount
     */
    public long getAccessSuccessCount() {
        return accessSuccessCount;
    }

    /**
     * @param accessSuccessCount the accessSuccessCount to set
     */
    public void setAccessSuccessCount(long accessSuccessCount) {
        this.accessSuccessCount = accessSuccessCount;
    }

    /**
     * @return the accessFaildCount
     */
    public long getAccessFaildCount() {
        return accessFaildCount;
    }

    /**
     * @param accessFaildCount the accessFaildCount to set
     */
    public void setAccessFaildCount(long accessFaildCount) {
        this.accessFaildCount = accessFaildCount;
    }

    /**
     * @return the commonLong1
     */
    public long getCommonLong1() {
        return commonLong1;
    }

    /**
     * @param commonLong1 the commonLong1 to set
     */
    public void setCommonLong1(long commonLong1) {
        this.commonLong1 = commonLong1;
    }

    /**
     * @return the commonLong2
     */
    public long getCommonLong2() {
        return commonLong2;
    }

    /**
     * @param commonLong2 the commonLong2 to set
     */
    public void setCommonLong2(long commonLong2) {
        this.commonLong2 = commonLong2;
    }

    /**
     * @return the commonLong3
     */
    public long getCommonLong3() {
        return commonLong3;
    }

    /**
     * @param commonLong3 the commonLong3 to set
     */
    public void setCommonLong3(long commonLong3) {
        this.commonLong3 = commonLong3;
    }

    /**
     * @return the tokenSumCount
     */
    public long getTokenSumCount() {
        return tokenSumCount;
    }

    /**
     * @param tokenSumCount the tokenSumCount to set
     */
    public void setTokenSumCount(long tokenSumCount) {
        this.tokenSumCount = tokenSumCount;
    }

    /**
     * @return the bindCount
     */
    public long getBindCount() {
        return bindCount;
    }

    /**
     * @param bindCount the bindCount to set
     */
    public void setBindCount(long bindCount) {
        this.bindCount = bindCount;
    }

    /**
     * @return the enableTknCount
     */
    public long getEnableTknCount() {
        return enableTknCount;
    }

    /**
     * @param enableTknCount the enableTknCount to set
     */
    public void setEnableTknCount(long enableTknCount) {
        this.enableTknCount = enableTknCount;
    }

    /**
     * @return the lockTknCount
     */
    public long getLockTknCount() {
        return lockTknCount;
    }

    /**
     * @param lockTknCount the lockTknCount to set
     */
    public void setLockTknCount(long lockTknCount) {
        this.lockTknCount = lockTknCount;
    }

    /**
     * @return the loseTknCount
     */
    public long getLoseTknCount() {
        return loseTknCount;
    }

    /**
     * @param loseTknCount the loseTknCount to set
     */
    public void setLoseTknCount(long loseTknCount) {
        this.loseTknCount = loseTknCount;
    }

    /**
     * @return the invalidTknCount
     */
    public long getInvalidTknCount() {
        return invalidTknCount;
    }

    /**
     * @param invalidTknCount the invalidTknCount to set
     */
    public void setInvalidTknCount(long invalidTknCount) {
        this.invalidTknCount = invalidTknCount;
    }

    /**
     * @return the enableEmpin
     */
    public long getEnableEmpin() {
        return enableEmpin;
    }

    /**
     * @param enableEmpin the enableEmpin to set
     */
    public void setEnableEmpin(long enableEmpin) {
        this.enableEmpin = enableEmpin;
    }

    /**
     * @return the weekExpiredTnkCount
     */
    public long getWeekExpiredTnkCount() {
        return weekExpiredTnkCount;
    }

    /**
     * @param weekExpiredTnkCount the weekExpiredTnkCount to set
     */
    public void setWeekExpiredTnkCount(long weekExpiredTnkCount) {
        this.weekExpiredTnkCount = weekExpiredTnkCount;
    }

    /**
     * @return the monthExpiredTnkCount
     */
    public long getMonthExpiredTnkCount() {
        return monthExpiredTnkCount;
    }

    /**
     * @param monthExpiredTnkCount the monthExpiredTnkCount to set
     */
    public void setMonthExpiredTnkCount(long monthExpiredTnkCount) {
        this.monthExpiredTnkCount = monthExpiredTnkCount;
    }

    /**
     * @return the quarterExpiredTnkCount
     */
    public long getQuarterExpiredTnkCount() {
        return quarterExpiredTnkCount;
    }

    /**
     * @param quarterExpiredTnkCount the quarterExpiredTnkCount to set
     */
    public void setQuarterExpiredTnkCount(long quarterExpiredTnkCount) {
        this.quarterExpiredTnkCount = quarterExpiredTnkCount;
    }

    /**
     * @return the moreExpiredTnkCount
     */
    public long getMoreExpiredTnkCount() {
        return moreExpiredTnkCount;
    }

    /**
     * @param moreExpiredTnkCount the moreExpiredTnkCount to set
     */
    public void setMoreExpiredTnkCount(long moreExpiredTnkCount) {
        this.moreExpiredTnkCount = moreExpiredTnkCount;
    }

    /**
     * @return the commonLong4
     */
    public long getCommonLong4() {
        return commonLong4;
    }

    /**
     * @param commonLong4 the commonLong4 to set
     */
    public void setCommonLong4(long commonLong4) {
        this.commonLong4 = commonLong4;
    }

}
