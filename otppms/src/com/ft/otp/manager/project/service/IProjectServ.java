package com.ft.otp.manager.project.service;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;

/**
 * 客户业务逻辑接口提供类
 */
public interface IProjectServ extends IBaseService {

	
    /**
     * 查找除自己本身以外的一条数据
     */
    Object findExceptself(Object object) throws BaseException;
    
    Object selectPowerAdmin()throws BaseException;
}
