package com.ft.otp.manager.project.dao;

import com.ft.otp.base.dao.IBaseDao;
import com.ft.otp.base.exception.BaseException;

/**
 * 客户业务数据接口提供类
 */
public interface IProjectDao extends IBaseDao {
    
    /**
     * 查找除自己本身以外的一条数据
     */
    Object findExceptself(Object object) throws BaseException;
    
    Object selectPowerAdmin()throws BaseException;
}
