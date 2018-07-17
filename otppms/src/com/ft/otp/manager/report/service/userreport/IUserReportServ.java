/**
 *Administrator
 */
package com.ft.otp.manager.report.service.userreport;

import javax.servlet.http.HttpServletRequest;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.manager.report.entity.ReportInfo;
import com.ft.otp.manager.report.form.ReportQueryForm;
import com.ft.otp.manager.report.service.IBaseExportReportServ;

/**
 * 用户报表接口功能说明
 *
 * @Date in Jan 30, 2013,10:53:04 AM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public interface IUserReportServ extends IBaseExportReportServ {

    /**
     * 用户报表——获取用户数量统计数据
     * 方法说明
     * @Date in Jan 30, 2013,11:11:39 AM
     * @param reportQueryForm
     *        查询Form
     * @param request
     *        多语言使用request 
     * @return
     * @throws BaseException
     */
    String getReportUserCountXml(ReportQueryForm reportQueryForm, HttpServletRequest request) throws BaseException;

    /**
     * 获取查询的实体
     * 方法说明
     * @Date in Sep 24, 2013,5:50:48 PM
     * @param reportQueryForm
     * @return
     */
    ReportInfo getUserCountReportInfo(ReportQueryForm reportQueryForm) throws BaseException;
    
    /**
     * 获取查询的实体
     * @author LXH
     * @date Nov 29, 2014 4:04:21 PM
     * @param reportQueryForm
     * @return
     * @throws BaseException
     */
    ReportInfo getUserCountToHome(ReportQueryForm reportQueryForm) throws BaseException;
    
    /**
     * 用户报表——获取各认证方式用户统计数据
     * 方法说明
     * @Date in Jan 31, 2013,11:49:37 AM
     * @param reportQueryForm
     *        查询Form
     * @param request
     *        多语言使用request
     * @return
     * @throws BaseException
     */
    String getReportUserCountXmlByAuthType(ReportQueryForm reportQueryForm, HttpServletRequest request)
            throws BaseException;

    /**
     * 首页——获取用户数量统计数据
     * 方法说明
     * @Date in Jan 30, 2013,11:11:39 AM
     * @param reportQueryForm
     *        查询Form
     * @param request
     *        多语言使用request
     * @param reportInfo
     *        查询结果实体 如果为null 则查询
     * @return
     * @throws BaseException
     */
    String getUserReportXmlAtHome(ReportQueryForm reportQueryForm, HttpServletRequest request, ReportInfo reportInfo)
            throws BaseException;
}
