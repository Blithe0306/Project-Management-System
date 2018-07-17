/**
 *Administrator
 */
package com.ft.otp.base.service;

import java.util.*;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;

/**
 * 基础Service，提供常用业务流程处理接口，供实现。
 *
 * @Date in Apr 2, 2011,4:48:17 PM
 *
 * @author TBM
 */
public interface IBaseService {

    /**
     * 添加对象
     * @Date in Apr 2, 2011,4:49:24 PM
     * @param object
     * @throws BaseException
     */
    void addObj(Object object) throws BaseException;

    /**
     * 删除对象
     * @Date in Apr 2, 2011,4:49:52 PM
     * @param object
     * @throws BaseException
     */
    void delObj(Object object) throws BaseException;

    /**
     * 批量删除对象
     * @Date in Apr 2, 2011,4:49:58 PM
     * @param keys
     * @throws BaseException
     */
    void delObj(Set<?> keys) throws BaseException;

    /**
     * 更新对象
     * @Date in Apr 2, 2011,4:50:40 PM
     * @param object
     * @throws BaseException
     */
    void updateObj(Object object) throws Exception;

    /**
     * 查找一条数据
     * @Date in Mar 9, 2010,10:26:01 AM
     * @param object
     * @return
     */
    Object find(Object object) throws BaseException;

    /**
     * 查找一组数据
     * 支持模糊查询
     * @Date in Apr 2, 2011,4:51:56 PM
     * @return
     * @throws BaseException
     */
    List<?> query(Object object, PageArgument pageArg) throws BaseException;

    /**
     * 数据统计
     * 支持模糊统计
     * @Date in Apr 10, 2011,10:36:23 AM
     * @param queryForm
     * @return
     * @throws BaseException
     */
    int count(Object object) throws BaseException;

}
