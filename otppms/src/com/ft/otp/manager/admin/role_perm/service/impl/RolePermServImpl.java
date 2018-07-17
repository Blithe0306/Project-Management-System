/**
 *Administrator
 */
package com.ft.otp.manager.admin.role_perm.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.admin.role_perm.dao.IRolePermDao;
import com.ft.otp.manager.admin.role_perm.entity.RolePerm;
import com.ft.otp.manager.admin.role_perm.service.IRolePermServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 角色-权限业务接口实现类功能说明
 *
 * @Date in Jul 1, 2011,3:39:29 PM
 *
 * @author ZJY
 */
public class RolePermServImpl implements IRolePermServ {

    private IRolePermDao rolePermDao;

    /**
     * @return the rolePermDao
     */
    public IRolePermDao getRolePermDao() {
        return rolePermDao;
    }

    /**
     * @param rolePermDao the rolePermDao to set
     */
    public void setRolePermDao(IRolePermDao rolePermDao) {
        this.rolePermDao = rolePermDao;
    }

    public void addObj(Object object) throws BaseException {
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
        rolePermDao.delObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#delObj(java.util.Set)
     */
    public void delObj(Set<?> keys) throws BaseException {
        RolePerm rolePerm = new RolePerm();
        Iterator<?> iter = keys.iterator();
        int temp[] = new int[keys.size()];
        int i = 0;
        while (iter.hasNext()) {
            String roleid = (String) iter.next();
            temp[i] = StrTool.parseInt(roleid);
            i++;
        }
        rolePerm.setBatchIdsInt(temp);
        rolePermDao.delObj(rolePerm);
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
    public List<?> query(Object object, PageArgument pageArg)
            throws BaseException {
        return rolePermDao.query(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.role_perm.service.IRolePermServ#addRolePerm(java.util.List)
     */
    public void addRolePerm(List<?> list) throws BaseException {
        rolePermDao.addRolePerm(list);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.role_perm.service.IRolePermServ#updateRolePerm(java.util.List)
     */
    public void updateRolePerm(List<?> list) throws BaseException {
        rolePermDao.updateRolePerm(list);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.role_perm.service.IRolePermServ#queryAdmPerms(java.lang.Object)
     */
    public List<?> queryAdmPerms(Object object) throws BaseException {
        return rolePermDao.queryAdmPerms(object);
    }

}
