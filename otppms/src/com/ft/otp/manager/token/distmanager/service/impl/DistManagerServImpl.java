/**
 *Administrator
 */
package com.ft.otp.manager.token.distmanager.service.impl;

import java.util.*;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.token.distmanager.dao.IDistManagerDao;
import com.ft.otp.manager.token.distmanager.service.IDistManagerServ;

/**
 * 令牌分发管理业务接口实现类
 *
 * @Date in Apr 18, 2011,11:23:03 AM
 *
 * @author ZJY
 */
public class DistManagerServImpl implements IDistManagerServ {

    private IDistManagerDao distManagerDao = null;

    /**
     * @return the distManagerDao
     */
    public IDistManagerDao getDistManagerDao() {
        return distManagerDao;
    }

    /**
     * @param distManagerDao the distManagerDao to set
     */
    public void setDistManagerDao(IDistManagerDao distManagerDao) {
        this.distManagerDao = distManagerDao;
    }

    public void addObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#count(com.ft.otp.base.form.BaseQueryForm)
     */
    public int count(Object object) throws BaseException {
        return distManagerDao.count(object);
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

    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#find(java.lang.Object)
     */
    public Object find(Object object) throws BaseException {
        return distManagerDao.find(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#query(com.ft.otp.base.form.BaseQueryForm, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {

        return distManagerDao.query(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws Exception {
        distManagerDao.updateObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.token.distmanager.service.IDistManagerServ#importExtToken(java.util.List)
     */
    public int importExtToken(List<Object> objList) throws BaseException {
        return distManagerDao.importExtToken(objList);
    }

}
