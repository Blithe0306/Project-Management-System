/**
 *Administrator
 */
package com.ft.otp.manager.admin.admin_role.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.admin.admin_role.dao.IAdminAndRoleDao;
import com.ft.otp.manager.admin.admin_role.entity.AdminAndRole;
import com.ft.otp.manager.admin.admin_role.service.IAdminAndRoleServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理员-角色关系业务接口实现类功能说明
 *
 * @Date in Jul 1, 2011,3:39:29 PM
 *
 * @author ZJY
 */
public class AdminAndRoleServImpl implements IAdminAndRoleServ {

    private IAdminAndRoleDao adminAndRoleDao;

    /**
     * @return the adminAndRoleDao
     */
    public IAdminAndRoleDao getAdminAndRoleDao() {
        return adminAndRoleDao;
    }

    /**
     * @param adminAndRoleDao the adminAndRoleDao to set
     */
    public void setAdminAndRoleDao(IAdminAndRoleDao adminAndRoleDao) {
        this.adminAndRoleDao = adminAndRoleDao;
    }

    public void addObj(Object object) throws BaseException {
        adminAndRoleDao.addObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#count(java.lang.Object)
     */
    public int count(Object object) throws BaseException {
        return adminAndRoleDao.count(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
        adminAndRoleDao.delObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#delObj(java.util.Set)
     */
    public void delObj(Set<?> keys) throws BaseException {
        AdminAndRole adminAndRole = new AdminAndRole();
        Iterator<?> iter = keys.iterator();
        int temp[] = new int[keys.size()];
        int i = 0;
        while (iter.hasNext()) {
            String roleid = (String) iter.next();
            temp[i] = StrTool.parseInt(roleid);
            i++;
        }
        adminAndRole.setBatchIdsInt(temp);
        adminAndRoleDao.delObj(adminAndRole);

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
        return adminAndRoleDao.query(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.role_perm.service.IRolePermServ#addRolePerm(java.util.List)
     */
    public void addAdminAndRole(List<?> list) throws BaseException {
        adminAndRoleDao.addAdminAndRole(list);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.admin_role.service.IAdminAndRoleServ#updateAdminAndRole(java.util.List)
     */
    public void updateAdminAndRole(List<?> list) throws BaseException {
        adminAndRoleDao.updateAdminAndRole(list);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.admin_role.service.IAdminAndRoleServ#queryAdmins(java.lang.Object)
     */
    public List<?> queryAdmins(Object object) throws BaseException {
        return adminAndRoleDao.queryAdmins(object);
    }

}
