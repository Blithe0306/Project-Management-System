package com.ft.otp.manager.project.service.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.BaseService;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.project.dao.IProjectResultDao;
import com.ft.otp.manager.project.service.IProjectResultServ;

public class ProjectResultServImpl extends BaseService implements
		IProjectResultServ {

	private IProjectResultDao projectResultDao;
	
	public IProjectResultDao getProjectResultDao() {
		return projectResultDao;
	}

	public void setProjectResultDao(IProjectResultDao projectResultDao) {
		this.projectResultDao = projectResultDao;
	}

	@Override
	public void addObj(Object object) throws BaseException {
		projectResultDao.addObj(object);
	}

	@Override
	public int count(Object object) throws BaseException {
		return projectResultDao.count(object);
	}

	@Override
	public void delObj(Object object) throws BaseException {
		projectResultDao.delObj(object);
	}

	@Override
	public void delObj(Set<?> keys) throws BaseException {
		projectResultDao.delObj(keys);
	}

	@Override
	public Object find(Object object) throws BaseException {
		return projectResultDao.find(object);
	}

	@Override
	public List<?> query(Object object, PageArgument pageArg)
			throws BaseException {
		return projectResultDao.query(object, pageArg);
	}

	@Override
	public void updateObj(Object object) throws Exception {
		projectResultDao.updateObj(object);
	}

}
