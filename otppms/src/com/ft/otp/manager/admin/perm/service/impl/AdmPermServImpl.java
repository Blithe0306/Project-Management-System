/**
 *Administrator
 */
package com.ft.otp.manager.admin.perm.service.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.admin.perm.dao.IAdmPermDao;
import com.ft.otp.manager.admin.perm.service.IAdmPermServ;

/**
 * 管理员权限服务接口实现
 *
 * @Date in Jun 29, 2011,5:01:49 PM
 *
 * @author TBM
 */
public class AdmPermServImpl implements IAdmPermServ {

    private IAdmPermDao adminPermDao = null;

    /**
     * @return the adminPermDao
     */
    public IAdmPermDao getAdminPermDao() {
        return adminPermDao;
    }

    /**
     * @param adminPermDao the adminPermDao to set
     */
    public void setAdminPermDao(IAdmPermDao adminPermDao) {
        this.adminPermDao = adminPermDao;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#count(java.lang.Object)
     */
    public int count(Object object) throws BaseException {
        return adminPermDao.count(object);
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
        return null;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg)
            throws BaseException {
        return adminPermDao.query(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.perm.service.IAdmPermServ#queryRolePerm(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> queryRolePerm(Object object, PageArgument pageArg) throws BaseException {
        return adminPermDao.queryRolePerm(object, pageArg);
    }

}
