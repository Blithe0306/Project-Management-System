package com.ft.otp.manager.customer.service;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;

/**
 * 客户业务逻辑接口提供类
 */
public interface ICustomerInfoServ extends IBaseService {
    public Object findMaxCustid(Object object) throws BaseException ;
    public Object findObj(Object object) throws BaseException ;
}
