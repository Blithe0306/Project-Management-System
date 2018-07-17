/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.access.service.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.BaseService;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.confinfo.access.dao.IAccessConDao;
import com.ft.otp.manager.confinfo.access.service.IAccessConServ;

/**
 * 访问控制策略service接口实现
 *
 * @Date in Dec 27, 2012,5:42:11 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class AccessConServImpl extends BaseService implements IAccessConServ {

    private IAccessConDao accessConDao;
    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        accessConDao.addObj(object);
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
        accessConDao.delObj(object);
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
        return accessConDao.find(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return accessConDao.query(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws Exception {
        accessConDao.updateObj(object);
    }

    public IAccessConDao getAccessConDao() {
        return accessConDao;
    }

    public void setAccessConDao(IAccessConDao accessConDao) {
        this.accessConDao = accessConDao;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.confinfo.access.service.IAccessConServ#queryIPDList(java.lang.Object)
     */
    public List<?> queryIPDList(Object object, PageArgument pageArg) throws BaseException {
        return accessConDao.queryIPDList(object, pageArg);
    }

}
