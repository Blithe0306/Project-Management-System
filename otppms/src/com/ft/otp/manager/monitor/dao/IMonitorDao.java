/**
 *Administrator
 */
package com.ft.otp.manager.monitor.dao;

import java.util.List;

import com.ft.otp.base.dao.IBaseDao;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;

/**
 * 监控预警数据库操作类 接口
 *
 * @Date in Mar 5, 2013,1:17:12 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public interface IMonitorDao extends IBaseDao {
    /**
     * 查询预警类型和管理员
     */
    List<?> queryMonitorAndAdmin(Object object, PageArgument pageArg) throws BaseException;

    /**
     * 添加预警类型和管理员
     * 方法说明
     * @Date in Mar 5, 2013,1:15:04 PM
     * @param list
     * @throws BaseException
     */
    public void addMonitorAndAdmin(List<Object> list) throws BaseException;

    /**
     * 修改预警类型和管理员
     * @Date in Nov 19, 2012,9:44:47 AM
     * @param list
     * @throws BaseException
     */
    public void updateMonitorAndAdmin(List<Object> list) throws BaseException;

    /**
     * 删除预警类型和管理员
     * 方法说明
     * @Date in Mar 5, 2013,2:01:37 PM
     * @param list
     * @throws BaseException
     */
    public void delMonitorAndAdmin(List<Object> list) throws BaseException;

    // 关系表end
    /**
     * 添加预警信息
     * 方法说明
     * @Date in Mar 5, 2013,1:15:04 PM
     * @param list
     * @throws BaseException
     */
    public void addMonitorInfo(List<Object> list) throws BaseException;

    /**
     * 删除预警信息
     * 方法说明
     * @Date in Mar 5, 2013,2:01:37 PM
     * @param list
     * @throws BaseException
     */
    public void delMonitorInfo(List<Object> list) throws BaseException;

    /**
     * 修改预警信息
     * 方法说明
     * @Date in Mar 5, 2013,2:01:37 PM
     * @param list
     * @throws BaseException
     */
    public void updateMonitorInfo(List<Object> list) throws BaseException;

    /**
     * 查询预警信息
     * 方法说明
     * @Date in Mar 5, 2013,2:01:37 PM
     * @param list
     * @throws BaseException
     */
    public List<?> queryMonitorInfo(Object object, PageArgument pageArg) throws BaseException;
    
    /**
     * 查询预警信息
     * 方法说明
     * @Date in Mar 5, 2013,2:01:37 PM
     * @param list
     * @throws BaseException
     */
    public List<?> queryMonitorOkInfo(Object object, PageArgument pageArg) throws BaseException;
}
