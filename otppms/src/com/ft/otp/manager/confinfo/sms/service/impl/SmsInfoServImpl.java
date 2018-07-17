/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.sms.service.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.BaseService;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.confinfo.sms.dao.ISmsInfoDao;
import com.ft.otp.manager.confinfo.sms.service.ISmsInfoServ;

/**
 * 短信网关配置
 *
 * @Date in Nov 21, 2012,3:51:10 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class SmsInfoServImpl extends BaseService implements ISmsInfoServ {

    private ISmsInfoDao smsDao;
    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        smsDao.addObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#count(java.lang.Object)
     */
    public int count(Object object) throws BaseException {
        return smsDao.count(object);
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
        smsDao.delObj(keys);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#find(java.lang.Object)
     */
    public Object find(Object object) throws BaseException {
        return smsDao.find(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return smsDao.query(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws Exception {
        smsDao.updateObj(object);
    }

    public ISmsInfoDao getSmsDao() {
        return smsDao;
    }

    public void setSmsDao(ISmsInfoDao smsDao) {
        this.smsDao = smsDao;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.confinfo.sms.service.ISmsInfoServ#updateEnabled(java.lang.Object)
     */
    public void updateEnabled(Object object) throws BaseException {
        smsDao.updateEnabled(object);
    }
    
}
