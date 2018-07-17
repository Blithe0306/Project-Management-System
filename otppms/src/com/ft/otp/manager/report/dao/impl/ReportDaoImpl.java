/**
 *Administrator
 */
package com.ft.otp.manager.report.dao.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.dao.namespace.ReportNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.report.dao.IReportDao;
import com.ft.otp.manager.report.form.ReportQueryForm;

/**
 * 报表管理 DAO 类功能说明
 *
 * @Date in Jan 26, 2013,11:03:22 AM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class ReportDaoImpl extends BaseSqlMapDAO implements IReportDao {

    // 获取sqlmap中的命名空间
    protected String getNameSpace() {
        return ReportNSpace.REPORT_INFO_NS;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.dao.IReportDao#getReportCountData(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public Object getReportUserCountData(ReportQueryForm reportQueryForm) throws BaseException {
        return queryForObject(ReportNSpace.REPORT_INFO_QUERY_USER_COUNT, reportQueryForm);
    }
    
    public Object getReportUserCountToMain(ReportQueryForm reportQueryForm) throws BaseException {
        return queryForObject(ReportNSpace.REPORT_INFO_QUERY_USER_COUNT_MAIN, reportQueryForm);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.dao.IReportDao#getReportUserCountDataByAuthType(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public Object getReportUserCountDataByAuthType(ReportQueryForm reportQueryForm) throws BaseException {
        return queryForObject(ReportNSpace.REPORT_INFO_QUERY_USER_COUNT_AUTHTYPE, reportQueryForm);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.dao.IReportDao#getReportTknCountData(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public Object getReportTknCountDataByEmpin(ReportQueryForm reportQueryForm) throws BaseException {
        return queryForObject(ReportNSpace.REPORT_INFO_QUERY_TOKEN_COUNT_EMPIN, reportQueryForm);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.dao.IReportDao#getReportTknCountDataByExpired(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public Object getReportTknCountDataByExpired(ReportQueryForm reportQueryForm) throws BaseException {
        return queryForObject(ReportNSpace.REPORT_INFO_QUERY_TOKEN_COUNT_EXPIRED, reportQueryForm);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.dao.IReportDao#getReportTknCountDataByState(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public Object getReportTknCountDataByState(ReportQueryForm reportQueryForm) throws BaseException {
        return queryForObject(ReportNSpace.REPORT_INFO_QUERY_TOKEN_COUNT_STATE, reportQueryForm);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.dao.IReportDao#getReportAuthCountData(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public Object getReportAuthCountData(ReportQueryForm reportQueryForm) throws BaseException {
        return queryForObject(ReportNSpace.REPORT_INFO_QUERY_AUTH_COUNT, reportQueryForm);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.dao.IReportDao#getReportAuthCountDataByTime(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public Object getReportAuthCountDataByTime(ReportQueryForm reportQueryForm) throws BaseException {
        return queryForObject(ReportNSpace.REPORT_INFO_QUERY_AUTH_COUNT_TIME, reportQueryForm);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.dao.IReportDao#getReportPortalCountData(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public Object getReportPortalCountData(ReportQueryForm reportQueryForm) throws BaseException {
        return queryForObject(ReportNSpace.REPORT_INFO_QUERY_PORTAL_COUNT, reportQueryForm);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.dao.IReportDao#getReportTknByExpired(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public List<?> getReportTknByExpired(ReportQueryForm reportQueryForm) throws BaseException {
        return queryForList(ReportNSpace.REPORT_INFO_QUERY_TOKEN_BY_EXPIRED, reportQueryForm);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.dao.IReportDao#getReportBindTknCount(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public Object getReportBindTknCount(ReportQueryForm reportQueryForm) throws BaseException {
        return queryForObject(ReportNSpace.REPORT_COUNT_BIND_TOKEN, reportQueryForm);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.dao.IReportDao#getReportSynTknCountByTime(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public int getReportSynTknCountByTime(ReportQueryForm reportQueryForm) throws BaseException {
        return (Integer) queryForObject(ReportNSpace.REPORT_COUNT_SYN_TOKEN_BY_TIME, reportQueryForm);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#count(java.lang.Object)
     */
    public int count(Object object) throws BaseException {
        return 0;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#delObj(java.util.Set)
     */
    public void delObj(Set<?> set) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#find(java.lang.Object)
     */
    public Object find(Object object) throws BaseException {
        return null;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return null;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.dao.IReportDao#getExportData(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public List<?> getExportData(ReportQueryForm reportQueryForm, int operType) throws BaseException {
        if (operType == 1) {// 获取认证同步数据
            return queryForList(ReportNSpace.EXPORT_REPORT_AUTH_SYNC, reportQueryForm);
        } else if (operType == 2) {// 用户数量
            return queryForList(ReportNSpace.EXPORT_REPORT_USER_COUNT, reportQueryForm);
        } else if (operType == 3) {// 用户认证方式
            return queryForList(ReportNSpace.EXPORT_REPORT_USER_AUTHTYPE, reportQueryForm);
        } else if (operType == 4) {// 令牌数量
            return queryForList(ReportNSpace.EXPORT_REPORT_TOKEN_COUNT, reportQueryForm);
        } else if (operType == 5) {// 令牌过期时间统计
            return queryForList(ReportNSpace.EXPORT_REPORT_TOKEN_EXPIRED, reportQueryForm);
        }

        return null;
    }

}
