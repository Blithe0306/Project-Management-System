package com.ft.otp.manager.customer.dao;

import com.ft.otp.base.dao.IBaseDao;
import com.ft.otp.base.exception.BaseException;

/**
 * 客户业务数据接口提供类
 */
public interface ICustomerInfoDao extends IBaseDao {
    public Object findMaxCustid(Object object) throws BaseException ;
    public Object findObj(Object object) throws BaseException ;
    
}
