/**
 *Administrator
 */
package com.ft.otp.manager.admin.admin_perm.service.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.admin.admin_perm.dao.IAdminPermDao;
import com.ft.otp.manager.admin.admin_perm.entity.AdminPermCode;
import com.ft.otp.manager.admin.admin_perm.service.IAdminPermServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理员常用功能业务 实现类
 *
 * @Date in May 14, 2013,2:27:57 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class AdminPermServImpl implements IAdminPermServ {

    private IAdminPermDao adminPermCodeDao = null;

    /**
     * @return the adminPermCodeDao
     */
    public IAdminPermDao getAdminPermCodeDao() {
        return adminPermCodeDao;
    }

    /**
     * @param adminPermCodeDao the adminPermCodeDao to set
     */
    public void setAdminPermCodeDao(IAdminPermDao adminPermCodeDao) {
        this.adminPermCodeDao = adminPermCodeDao;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        adminPermCodeDao.addObj(object);
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
        adminPermCodeDao.delObj(object);
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
        return adminPermCodeDao.query(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws Exception {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.admin_perm.service.IAdminPermServ#isOftenUsed(com.ft.otp.manager.admin.admin_perm.entity.AdminPermCode)
     */
    public boolean isOftenUsed(AdminPermCode adminPermCode) throws BaseException {
        if (StrTool.listNotNull(adminPermCodeDao.query(adminPermCode, new PageArgument()))) {
            //如果返回结果不为空则是常用
            return true;
        }
        return false;
    }

}
