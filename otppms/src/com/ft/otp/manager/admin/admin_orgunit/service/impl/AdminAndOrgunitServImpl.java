package com.ft.otp.manager.admin.admin_orgunit.service.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.admin.admin_orgunit.dao.IAdminAndOrgunitDao;
import com.ft.otp.manager.admin.admin_orgunit.service.IAdminAndOrgunitServ;

/**
 * 管理员-组织机构关系DAO接口类功能说明
 *
 * @Date in January 24, 2013,10:00:00 AM
 *
 * @author BYL
 */
public class AdminAndOrgunitServImpl implements IAdminAndOrgunitServ {

    private IAdminAndOrgunitDao adminAndOrgunitDao;
    
	/**
	 * @return the adminAndOrgunitDao
	 */
	public IAdminAndOrgunitDao getAdminAndOrgunitDao() {
		return adminAndOrgunitDao;
	}

	/**
	 * @param adminAndOrgunitDao the adminAndOrgunitDao to set
	 */
	public void setAdminAndOrgunitDao(IAdminAndOrgunitDao adminAndOrgunitDao) {
		this.adminAndOrgunitDao = adminAndOrgunitDao;
	}

	public List<?> queryAdminAndOrgunitByAdminId(Object object) throws BaseException {
		return adminAndOrgunitDao.queryAdminAndOrgunit(object);
	}

	public List<?> queryAdminAndOrgunitByDomainId(Object object) throws BaseException {
		return adminAndOrgunitDao.queryAdminAndOrgunit(object);
	}

	public List<?> queryAdminAndOrgunitByOrgunitId(Object object) throws BaseException {
		return adminAndOrgunitDao.queryAdminAndOrgunit(object);
	}

	public void addObj(Object object) throws BaseException {
		adminAndOrgunitDao.addObj(object);
	}

	public int count(Object object) throws BaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	public void delObj(Object object) throws BaseException {
		adminAndOrgunitDao.delObj(object);
	}

	public void delObj(Set<?> keys) throws BaseException {
		// TODO Auto-generated method stub
		
	}

	public Object find(Object object) throws BaseException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<?> query(Object object, PageArgument pageArg) throws BaseException {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateObj(Object object) throws BaseException {
		// TODO Auto-generated method stub
		
	}

	public void addObjs(List<?> list) throws BaseException {
			adminAndOrgunitDao.addObjs(list);
	}


}
