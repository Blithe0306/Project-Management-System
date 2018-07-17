/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.backend.service.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.BaseService;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.authmgr.backend.dao.IBackendDao;
import com.ft.otp.manager.authmgr.backend.entity.BackendInfo;
import com.ft.otp.manager.authmgr.backend.service.IBackendServ;

/**
 * 后端认证Serv实现类
 *
 * @Date in Jan 21, 2013,8:20:31 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class BackendServImpl extends BaseService implements IBackendServ {
    
    private IBackendDao backendDao;

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        backendDao.addObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#count(java.lang.Object)
     */
    public int count(Object object) throws BaseException {
        return backendDao.count(object);
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
        backendDao.delObj(keys);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#find(java.lang.Object)
     */
    public Object find(Object object) throws BaseException {
        return backendDao.find(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return backendDao.query(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws Exception {
        backendDao.updateObj(object);
    }

    public IBackendDao getBackendDao() {
        return backendDao;
    }

    public void setBackendDao(IBackendDao backendDao) {
        this.backendDao = backendDao;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.authmgr.backend.service.IBackendServ#queryUKIsExist(java.lang.Object)
     */
    public Object queryUKIsExist(Object object) throws BaseException {
        return backendDao.queryUKIsExist(object);
    }
    
    public void updateEnabled(BackendInfo backendInfo, int enabled) throws BaseException {
    	backendDao.updateEnabled(backendInfo, enabled);
    }

}
