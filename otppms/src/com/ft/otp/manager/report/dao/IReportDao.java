/**
 *Administrator
 */
package com.ft.otp.manager.report.dao;

import java.util.List;

import com.ft.otp.base.dao.IBaseDao;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.manager.report.form.ReportQueryForm;

/**
 * 类、接口等说明
 *
 * @Date in Jan 26, 2013,11:02:44 AM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public interface IReportDao extends IBaseDao {

    /**
     * 业务报表 获取认证服务器认证同步量统计数据
     * 方法说明
     * @Date in Jan 31, 2013,5:21:34 PM
     * @param reportQueryForm
     * @return
     * @throws BaseException
     */
    Object getReportAuthCountData(ReportQueryForm reportQueryForm) throws BaseException;

    /**
     * 业务报表 获取认证服务器时段认证量统计数据
     * 方法说明
     * @Date in Jan 31, 2013,5:24:35 PM
     * @param reportQueryForm
     * @return
     * @throws BaseException
     */
    Object getReportAuthCountDataByTime(ReportQueryForm reportQueryForm) throws BaseException;

    /**
     * 业务报表 获取用户门户访问量统计数据
     * 方法说明
     * @Date in Jan 31, 2013,5:27:32 PM
     * @param reportQueryForm
     * @return
     * @throws BaseException
     */
    Object getReportPortalCountData(ReportQueryForm reportQueryForm) throws BaseException;

    /**
     * 用户报表 获取用户报表的数量统计数据
     * 方法说明
     * @Date in Jan 30, 2013,11:21:22 AM
     * @param reportQueryForm
     * @return
     * @throws BaseException
     */
    Object getReportUserCountData(ReportQueryForm reportQueryForm) throws BaseException;
    
    /**
     * 首页，用户报表 获取用户报表的数量统计数据
     * @author LXH
     * @date Nov 29, 2014 3:25:31 PM
     * @param reportQueryForm
     * @return
     * @throws BaseException
     */
    Object getReportUserCountToMain(ReportQueryForm reportQueryForm) throws BaseException;

    /**
     * 用户报表 获取用户报表的各种认证类型的用户数量统计数据
     * 方法说明
     * @Date in Jan 31, 2013,11:44:45 AM
     * @param reportQueryForm
     * @return
     * @throws BaseException
     */
    Object getReportUserCountDataByAuthType(ReportQueryForm reportQueryForm) throws BaseException;

    /**
     * 令牌报表 获取令牌状态统计数据
     * 方法说明
     * @Date in Jan 31, 2013,5:21:34 PM
     * @param reportQueryForm
     * @return
     * @throws BaseException
     */
    Object getReportTknCountDataByState(ReportQueryForm reportQueryForm) throws BaseException;

    /**
     * 令牌报表 获取令牌启用应急口令统计数据
     * 方法说明
     * @Date in Jan 31, 2013,5:24:35 PM
     * @param reportQueryForm
     * @return
     * @throws BaseException
     */
    Object getReportTknCountDataByEmpin(ReportQueryForm reportQueryForm) throws BaseException;

    /**
     * 令牌报表 获取令牌过期时间的统计数据
     * 方法说明
     * @Date in Jan 31, 2013,5:27:32 PM
     * @param reportQueryForm
     * @return
     * @throws BaseException
     */
    Object getReportTknCountDataByExpired(ReportQueryForm reportQueryForm) throws BaseException;

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
     * 获取导出的数据
     * 方法说明
     * @Date in Aug 12, 2013,7:26:09 PM
     * @param reportQueryForm
     * @param operType
     *        统计数据类型  1：获取认证同步数据；2：用户数量；3：用户认证方式；4：令牌数量；
     * @return
     * @throws BaseException
     */
    List<?> getExportData(ReportQueryForm reportQueryForm, int operType) throws BaseException;
}
