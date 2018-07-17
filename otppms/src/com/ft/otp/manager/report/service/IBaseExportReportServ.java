/**
 *Administrator
 */
package com.ft.otp.manager.report.service;

import javax.servlet.http.HttpServletRequest;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.manager.report.form.ReportQueryForm;

/**
 * 报表导出基础接口
 *
 * @Date in Aug 10, 2013,10:51:57 AM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public interface IBaseExportReportServ {
    /**
     * 导出详细报表
     * 
     * @Date in Aug 10, 2013,10:54:30 AM
     * @param reportQueryForm
     * @param exportType
     *        导出类型
     * @param excelPath
     *        excel导出路径
     *        @param request
     * @return 
     *        成功返回 excel路径 否则返回null
     */
    public String exportDetailReport(ReportQueryForm reportQueryForm, String exportType, String excelPath,
            HttpServletRequest request) throws BaseException;
}
