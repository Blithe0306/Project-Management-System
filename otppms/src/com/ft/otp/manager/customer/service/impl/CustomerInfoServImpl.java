package com.ft.otp.manager.customer.service.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.customer.dao.ICustomerInfoDao;
import com.ft.otp.manager.customer.service.ICustomerInfoServ;

/**
 * 客户业务逻辑实现类
 */
public class CustomerInfoServImpl implements ICustomerInfoServ {

    private ICustomerInfoDao custInfoDao = null;

    public ICustomerInfoDao getCustInfoDao() {
		return custInfoDao;
	}

	public void setCustInfoDao(ICustomerInfoDao custInfoDao) {
		this.custInfoDao = custInfoDao;
	}

	/* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
    	custInfoDao.addObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#count(com.ft.otp.base.form.BaseQueryForm)
     */
    public int count(Object object) throws BaseException {
        return custInfoDao.count(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#delObj(java.util.Set)
     */
    public void delObj(Set<?> keys) throws BaseException {
    	custInfoDao.delObj(keys);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#find(java.lang.Object)
     */
    public Object find(Object object) throws BaseException {
        return custInfoDao.find(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#query(com.ft.otp.base.form.BaseQueryForm, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg)
            throws BaseException {
        return custInfoDao.query(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws Exception {
    	custInfoDao.updateObj(object);
    }

    public Object findMaxCustid(Object object) throws BaseException {
       return custInfoDao.findMaxCustid(object);
    }

    public Object findObj(Object object) throws BaseException {
        return custInfoDao.findObj(object);
    }

}
