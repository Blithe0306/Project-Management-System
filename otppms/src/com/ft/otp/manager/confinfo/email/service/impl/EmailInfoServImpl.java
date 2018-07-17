/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.email.service.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.BaseService;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.confinfo.email.dao.IEmailInfoDao;
import com.ft.otp.manager.confinfo.email.service.IEmailInfoServ;

/**
 * 类、接口等说明
 *
 * @Date in Nov 19, 2012,1:20:06 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class EmailInfoServImpl extends BaseService implements IEmailInfoServ {
    
    private IEmailInfoDao emailDao;

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        emailDao.addObj(object);
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
        emailDao.delObj(object);
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
        return emailDao.find(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return emailDao.query(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws Exception {
        emailDao.updateObj(object);
    }

    public IEmailInfoDao getEmailDao() {
        return emailDao;
    }

    public void setEmailDao(IEmailInfoDao emailDao) {
        this.emailDao = emailDao;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.confinfo.email.service.IEmailInfoServ#updateIsdefaultOE(java.lang.Object)
     */
    public void updateIsdefaultOE(Object object) throws BaseException {
        emailDao.updateIsdefaultOE(object);
    }

}
