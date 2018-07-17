/**
 *Administrator
 */
package com.ft.otp.manager.admin.user.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.admin.user.dao.IAdminUserDao;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.admin.user.service.IAdminUserServ;

/**
 * 管理员用户业务方法实现类
 *
 * @Date in Apr 17, 2011,1:17:09 PM
 *
 * @author TBM
 */
public class AdminUserServImpl implements IAdminUserServ {

    private IAdminUserDao adminUserDao;

    /**
     * @return the adminUserDao
     */
    public IAdminUserDao getAdminUserDao() {
        return adminUserDao;
    }

    /**
     * @param adminUserDao the adminUserDao to set
     */
    public void setAdminUserDao(IAdminUserDao adminUserDao) {
        this.adminUserDao = adminUserDao;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        adminUserDao.addObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#count(com.ft.otp.base.form.BaseQueryForm)
     */
    public int count(Object object) throws BaseException {
        return adminUserDao.count(object);
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
        Iterator<?> iterator = keys.iterator();
        AdminUser adminUser = new AdminUser();
        String[] temp = new String[keys.size()];
        int i = 0;
        while (iterator.hasNext()) {
            String adminid = (String) iterator.next();
            temp[i] = adminid;
            i++;
        }
        adminUser.setBatchIds(temp);
        adminUserDao.delObj(adminUser);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#find(java.lang.Object)
     */
    public Object find(Object object) throws BaseException {
        return adminUserDao.find(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#query(com.ft.otp.base.form.BaseQueryForm, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return adminUserDao.query(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws Exception {
        adminUserDao.updateObj(object);
    }
    
    public void updatePass(Object object) throws Exception {
        adminUserDao.updatePass(object);
    }
    
    public void updatePwddeath(Object object) throws Exception {
        adminUserDao.updatePwddeath(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.user.service.IAdminUserServ#disabledAdmin(java.lang.String, int)
     */
    public void updateEnabled(String adminid, int enabled) throws BaseException {
        adminUserDao.updateEnabled(adminid, enabled);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.user.service.IAdminUserServ#updateLocked(java.lang.Object)
     */
    public void updateLocked(Object object) throws BaseException {
        adminUserDao.updateLocked(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.user.service.IAdminUserServ#updatePassword(java.lang.Object)
     */
    public void updatePassword(Object object) throws BaseException {
        adminUserDao.updatePassword(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.user.service.IAdminUserServ#getChildAdmins(java.lang.Object, com.ft.otp.common.page.PageArgument, java.lang.String)
     */
    public List<?> getChildAdmins(Object object, PageArgument pageArg, String method) throws BaseException {
        return adminUserDao.getChildAdmins(object, pageArg, method);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.user.service.IAdminUserServ#queryAdminDesignate()
     */
    public List<?> queryAdminDesignate(Object object) throws BaseException {
        return adminUserDao.queryAdminDesignate(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.user.service.IAdminUserServ#findSuperDesignate(java.lang.Object)
     */
    public Object findSuperDesignate(Object object) throws BaseException {
        return adminUserDao.findSuperDesignate(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.user.service.IAdminUserServ#updateDsignate(java.lang.Object)
     */
    public void updateDsignate(Object object) throws BaseException {
        adminUserDao.updateDsignate(object);
    }

    /**
     * 查询管理员过滤掉超级管理员
     * @param object
     * @param pageArg
     * @throws BaseException
     * @return List
     */
    public List<?> queryExceptPowerAdmin(Object object, PageArgument pageArg) throws BaseException {
        return adminUserDao.queryExceptPowerAdmin(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.user.service.IAdminUserServ#updateAdminUser(java.lang.Object)
     */
    public void updateAdminUser(Object object) throws BaseException {
        adminUserDao.updateAU(object);
    }
    
    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.user.service.IAdminUserServ#updateAdminUser(java.lang.Object)
     */
    public List<?> selectAmdEmail(Object object) throws BaseException {
        return adminUserDao.selectAmdEmail(object);
    }

}
