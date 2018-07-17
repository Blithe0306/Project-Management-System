/**
 *Administrator
 */
package com.ft.otp.manager.notice.customer.service.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.notice.customer.dao.ICustomerUserDao;
import com.ft.otp.manager.notice.customer.service.ICustomerUserServ;

/**
 * 派单监视通知业务接口实现
 *
 * @Date in May 24, 2012,2:24:13 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class CustomerUserImpl implements ICustomerUserServ {

    private ICustomerUserDao customDao = null;

    /**
     * @return the dao
     */
    public ICustomerUserDao getCustomDao() {
        return customDao;
    }

    /**
     * @param dao the dao to set
     */
    public void setCustomDao(ICustomerUserDao customDao) {
        this.customDao = customDao;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        customDao.addObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#count(java.lang.Object)
     */
    public int count(Object object) throws BaseException {
        return 0;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
        customDao.delObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#delObj(java.util.Set)
     */
    public void delObj(Set<?> keys) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#find(java.lang.Object)
     */
    public Object find(Object object) throws BaseException {
        return customDao.find(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return customDao.query(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws Exception {
    	customDao.updateObj(object);
    }

}
