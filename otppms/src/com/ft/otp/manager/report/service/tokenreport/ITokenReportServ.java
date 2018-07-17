/**
 *Administrator
 */
package com.ft.otp.manager.report.service.tokenreport;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.manager.report.entity.ReportInfo;
import com.ft.otp.manager.report.form.ReportQueryForm;
import com.ft.otp.manager.report.service.IBaseExportReportServ;

/**
 * 令牌报表接口功能说明
 *
 * @Date in Jan 30, 2013,1:55:58 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public interface ITokenReportServ extends IBaseExportReportServ {

    /**
     * 令牌报表 获取令牌状态统计数据
     * 方法说明
     * @Date in Jan 31, 2013,5:21:34 PM
     * @param reportQueryForm
     * @return
     * @throws BaseException
     */
    String getReportTknCountDataXmlByState(ReportQueryForm reportQueryForm, HttpServletRequest request)
            throws BaseException;

    /**
     * 令牌报表 获取令牌启用应急口令统计数据
     * 方法说明
     * @Date in Jan 31, 2013,5:24:35 PM
     * @param reportQueryForm
     * @return
     * @throws BaseException
     */
    String getReportTknCountDataXmlByEmpin(ReportQueryForm reportQueryForm, HttpServletRequest request)
            throws BaseException;

    /**
     * 令牌报表 获取令牌过期时间的统计数据
     * 方法说明
     * @Date in Jan 31, 2013,5:27:32 PM
     * @param reportQueryForm
     * @return
     * @throws BaseException
     */
    String getReportTknCountDataXmlByExpired(ReportQueryForm reportQueryForm, HttpServletRequest request)
            throws BaseException;

    /**
     * 获取预警中用到 过期令牌
     * 方法说明
     * @Date in Mar 7, 2013,5:10:38 PM
     * @param reportQueryForm
     * @return
     * @throws BaseException
     */
    List<?> getReportTknByExpired(ReportQueryForm reportQueryForm) throws BaseException;

    /**
     * 获取监控预警中统计绑定令牌的数量
     * 方法说明
     * @Date in Mar 7, 2013,5:10:38 PM
     * @param reportQueryForm
     * @return
     * @throws BaseException
     */
    Object getReportBindTknCount(ReportQueryForm reportQueryForm) throws BaseException;

    /**
     * 获取监控预警中统计一段时间内令牌的同步次数
     * 方法说明
     * @Date in Mar 7, 2013,5:10:38 PM
     * @param reportQueryForm
     * @return
     * @throws BaseException
     */
    int getReportSynTknCountByTime(ReportQueryForm reportQueryForm) throws BaseException;

    /**
     * 首页 --获取令牌数据
     * 方法说明
     * @Date in Jan 31, 2013,5:24:35 PM
     * @param reportQueryForm
     *        查询Form
     * @param request
     *        多语言使用request
     * @param reportInfo
     *        查询结果实体 如果为null 则查询
     * @return
     * @throws BaseException
     */
    String getTokenReportXmlAtHome(ReportQueryForm reportQueryForm, HttpServletRequest request, ReportInfo reportInfo)
            throws BaseException;

    /**
     * 获取令牌状态统计的查询实体
     * 方法说明
     * @Date in Sep 24, 2013,5:50:48 PM
     * @param reportQueryForm
     * @return
     */
    ReportInfo getTokenStateReportInfo(ReportQueryForm reportQueryForm) throws BaseException;
}
