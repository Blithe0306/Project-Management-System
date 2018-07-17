/**
 *Administrator
 */
package com.ft.otp.manager.report.service.operationreport;

import javax.servlet.http.HttpServletRequest;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.manager.report.form.ReportQueryForm;
import com.ft.otp.manager.report.service.IBaseExportReportServ;

/**
 * 业务报表接口功能说明
 *
 * @Date in Jan 30, 2013,1:59:35 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public interface IOperationReportServ extends IBaseExportReportServ {
    /**
     * 业务报表 获取认证服务器认证同步量统计数据
     * 方法说明
     * @Date in Jan 31, 2013,5:21:34 PM
     * @param reportQueryForm
     * @return
     * @throws BaseException
     */
    String getReportAuthCountDataXml(ReportQueryForm reportQueryForm, HttpServletRequest request) throws BaseException;

    /**
     * 业务报表 获取认证服务器或者是用户门户时段认证量统计数据
     * 方法说明
     * @Date in Jan 31, 2013,5:24:35 PM
     * @param reportQueryForm
     * @param mark
     * @return
     * @throws BaseException
     */
    String getReportAuthCountDataXmlByTime(ReportQueryForm reportQueryForm, int markm, HttpServletRequest request)
            throws BaseException;

    /**
     * 业务报表 获取用户门户访问量统计数据
     * 方法说明
     * @Date in Jan 31, 2013,5:27:32 PM
     * @param reportQueryForm
     * @return
     * @throws BaseException
     */
    String getReportPortalCountDataXml(ReportQueryForm reportQueryForm, HttpServletRequest request)
            throws BaseException;
}
