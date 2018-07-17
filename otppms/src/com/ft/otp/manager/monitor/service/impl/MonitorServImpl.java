/**
 *Administrator
 */
package com.ft.otp.manager.monitor.service.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.BaseService;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.monitor.dao.IMonitorDao;
import com.ft.otp.manager.monitor.service.IMonitorServ;

/**
 * 监控预警 service功能实现类
 *
 * @Date in Mar 5, 2013,2:05:45 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class MonitorServImpl extends BaseService implements IMonitorServ {
    private IMonitorDao monitorDao;

    /* (non-Javadoc)
     * @see com.ft.otp.manager.monitor.service.IMonitorServ#batchAddMonitorAndAdmin(java.util.List)
     */
    public void batchAddMonitorAndAdmin(List<Object> list) throws BaseException {
        monitorDao.addMonitorAndAdmin(list);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.monitor.service.IMonitorServ#batchDelMonitorAndAdmin(java.util.List)
     */
    public void batchDelMonitorAndAdmin(List<Object> list) throws BaseException {
        monitorDao.delMonitorAndAdmin(list);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.monitor.service.IMonitorServ#batchUpdateMonitorInfo(java.util.List)
     */
    public void batchUpdateMonitorAndAdmin(List<Object> list) throws BaseException {
        monitorDao.updateMonitorAndAdmin(list);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.monitor.service.IMonitorServ#queryMonitorAndAdmin(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> queryMonitorAndAdmin(Object object, PageArgument pageArg) throws BaseException {
        return monitorDao.queryMonitorAndAdmin(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        monitorDao.addObj(object);
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
        monitorDao.delObj(object);
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
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return monitorDao.query(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws Exception {
        monitorDao.updateObj(object);
    }

    /**
     * @return the monitorDao
     */
    public IMonitorDao getMonitorDao() {
        return monitorDao;
    }

    /**
     * @param monitorDao the monitorDao to set
     */
    public void setMonitorDao(IMonitorDao monitorDao) {
        this.monitorDao = monitorDao;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.monitor.service.IMonitorServ#batchAddMonitorInfo(java.util.List)
     */
    public void batchAddMonitorInfo(List<Object> list) throws BaseException {
        monitorDao.addMonitorInfo(list);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.monitor.service.IMonitorServ#batchDelMonitorInfo(java.util.List)
     */
    public void batchDelMonitorInfo(List<Object> list) throws BaseException {
        monitorDao.delMonitorInfo(list);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.monitor.service.IMonitorServ#batchUpdateMonitorAndAdmin(java.util.List)
     */
    public void batchUpdateMonitorInfo(List<Object> list) throws BaseException {
        monitorDao.updateMonitorInfo(list);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.monitor.service.IMonitorServ#queryMonitorInfo(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> queryMonitorInfo(Object object, PageArgument pageArg) throws BaseException {
        return monitorDao.queryMonitorInfo(object, pageArg);
    }

    public List<?> queryMonitorOkInfo(Object object, PageArgument pageArg) throws BaseException {
        return monitorDao.queryMonitorOkInfo(object, pageArg);
    }

}
