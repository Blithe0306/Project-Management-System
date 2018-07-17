/**
 *Administrator
 */
package com.ft.otp.manager.monitor.service;

import java.util.List;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;
import com.ft.otp.common.page.PageArgument;

/**
 * 系统监控功能模块业务集中处理接口
 *
 * @Date in Mar 5, 2013,11:40:28 AM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public interface IMonitorServ extends IBaseService {

    /**
     * 查询预警类型和管理员
     */
    List<?> queryMonitorAndAdmin(Object object, PageArgument pageArg) throws BaseException;

    /**
     * 批量修改预警类型和管理员
     * @Date in Nov 19, 2012,9:44:47 AM
     * @param list
     * @throws BaseException
     */
    public void batchUpdateMonitorAndAdmin(List<Object> list) throws BaseException;

    /**
     * 批量添加预警类型和管理员
     * 方法说明
     * @Date in Mar 5, 2013,1:15:04 PM
     * @param list
     * @throws BaseException
     */
    public void batchAddMonitorAndAdmin(List<Object> list) throws BaseException;

    /**
     * 批量删除预警类型和管理员
     * 方法说明
     * @Date in Mar 5, 2013,1:15:04 PM
     * @param list
     * @throws BaseException
     */
    public void batchDelMonitorAndAdmin(List<Object> list) throws BaseException;

    // **********************************关系表 end
    /**
     * 查询预警信息
     */
    List<?> queryMonitorInfo(Object object, PageArgument pageArg) throws BaseException;
    /**
     * 查询预警信息(不含IsSend是否发送属性)
     */
    List<?> queryMonitorOkInfo(Object object, PageArgument pageArg) throws BaseException;
    /**
     * 批量修改预警信息
     * @Date in Nov 19, 2012,9:44:47 AM
     * @param list
     * @throws BaseException
     */
    public void batchUpdateMonitorInfo(List<Object> list) throws BaseException;

    /**
     * 批量添加预警信息
     * 方法说明
     * @Date in Mar 5, 2013,1:15:04 PM
     * @param list
     * @throws BaseException
     */
    public void batchAddMonitorInfo(List<Object> list) throws BaseException;

    /**
     * 批量删除预警信息
     * 方法说明
     * @Date in Mar 5, 2013,1:15:04 PM
     * @param list
     * @throws BaseException
     */
    public void batchDelMonitorInfo(List<Object> list) throws BaseException;

}
