/**
 *Administrator
 */
package com.ft.otp.manager.logs.userlog.service.impl;

import java.util.List;
import java.util.Set;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.BaseService;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.logs.userlog.dao.IUserLogDao;
import com.ft.otp.manager.logs.userlog.service.IUserLogServ;

/**
 * 用户日志业务接口实现类功能说明
 *
 * @Date in May 4, 2011,4:53:49 PM
 *
 * @author ZJY
 */
public class UserLogServImpl extends BaseService implements IUserLogServ {

    private IUserLogDao userLogDao;

    /**
     * @return the userLogDao
     */
    public IUserLogDao getUserLogDao() {
        return userLogDao;
    }

    /**
     * @param userLogDao the userLogDao to set
     */
    public void setUserLogDao(IUserLogDao userLogDao) {
        this.userLogDao = userLogDao;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#count(com.ft.otp.base.form.BaseQueryForm)
     */
    public int count(Object object) throws BaseException {
        return userLogDao.count(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
        userLogDao.delObj(object);
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
        return userLogDao.find(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#query(com.ft.otp.base.form.BaseQueryForm, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg)
            throws BaseException {
        return userLogDao.query(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
    }

}
