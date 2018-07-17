/**
 *Administrator
 */
package com.ft.otp.manager.logs.adminlog.service.impl;

import java.util.List;
import java.util.Set;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.BaseService;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.logs.adminlog.dao.IAdminLogDao;
import com.ft.otp.manager.logs.adminlog.service.IAdminLogServ;

/**
 * 管理员日志业务接口实现类功能说明
 *
 * @Date in May 3, 2011,1:19:34 PM
 *
 * @author ZJY
 */
public class AdminLogServImpl extends BaseService implements IAdminLogServ {

    private IAdminLogDao adminLogDao;

    /**
     * @return the adminLogDao
     */
    public IAdminLogDao getAdminLogDao() {
        return adminLogDao;
    }

    /**
     * @param adminLogDao the adminLogDao to set
     */
    public void setAdminLogDao(IAdminLogDao adminLogDao) {
        this.adminLogDao = adminLogDao;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        adminLogDao.addObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#count(com.ft.otp.base.form.BaseQueryForm)
     */
    public int count(Object object) throws BaseException {
        return adminLogDao.count(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
        adminLogDao.delObj(object);
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
        return adminLogDao.find(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#query(com.ft.otp.base.form.BaseQueryForm, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg)
            throws BaseException {
        return adminLogDao.query(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
    }

}
