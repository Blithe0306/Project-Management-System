/**
 *Administrator
 */
package com.ft.otp.manager.admin.role.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.admin.role.dao.IRoleInfoDao;
import com.ft.otp.manager.admin.role.entity.RoleInfo;
import com.ft.otp.manager.admin.role.service.IRoleInfoServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理员服务接口实现类
 *
 * @Date in Jun 29, 2011,3:08:33 PM
 *
 * @author TBM
 */
public class RoleInfoServImpl implements IRoleInfoServ {

    private IRoleInfoDao adminRoleDao = null;

    /**
     * @return the adminRoleDao
     */
    public IRoleInfoDao getAdminRoleDao() {
        return adminRoleDao;
    }

    /**
     * @param adminRoleDao the adminRoleDao to set
     */
    public void setAdminRoleDao(IRoleInfoDao adminRoleDao) {
        this.adminRoleDao = adminRoleDao;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        adminRoleDao.addObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#count(java.lang.Object)
     */
    public int count(Object object) throws BaseException {
        return adminRoleDao.count(object);
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
        RoleInfo adminRole = new RoleInfo();
        Iterator<?> iter = keys.iterator();
        int temp[] = new int[keys.size()];
        int i = 0;
        while (iter.hasNext()) {
            String id = (String) iter.next();
            temp[i] = StrTool.parseInt(id);
            i++;
        }
        adminRole.setBatchIdsInt(temp);
        adminRoleDao.delObj(adminRole);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#find(java.lang.Object)
     */
    public Object find(Object object) throws BaseException {
        return adminRoleDao.find(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg)
            throws BaseException {
        return adminRoleDao.query(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws Exception {
        adminRoleDao.updateObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.role.service.IAdmRoleServ#getAdmrolesList(java.lang.Object)
     */
    public List<?> getAdmrolesList(Object object) throws BaseException {
        return adminRoleDao.getAdmsrolesList(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.role.service.IRoleInfoServ#updateDsignate(java.lang.Object)
     */
    public void updateDsignate(Object object) throws BaseException {
        adminRoleDao.updateDsignate(object);
    }
    
    public int countDes(Object object) throws BaseException {
		return adminRoleDao.countDes(object);
	}
    
    public List<?> queryDes(Object object) throws BaseException {
		return adminRoleDao.queryDes(object);
	}

}
