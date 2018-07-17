/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 * @Date Dec 23, 2014,2:44:44 PM
 */
package com.ft.otp.manager.project.service.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.project.dao.IProjectDao;
import com.ft.otp.manager.project.service.IProjectServ;

/**
 * 类、接口等说明
 *
 * @Date Dec 23, 2014,2:44:44 PM
 * @version v1.0
 * @author ZWX
 */
public class ProjectServImpl implements IProjectServ {
    
    private IProjectDao projectDao = null;
    
    public IProjectDao getProjectDao() {
        return projectDao;
    }

    public void setProjectDao(IProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    public void addObj(Object object) throws BaseException {
        projectDao.addObj(object);
    }

    public int count(Object object) throws BaseException {
        return projectDao.count(object);
    }

    public void delObj(Object object) throws BaseException {
    }

    public void delObj(Set<?> set) throws BaseException {
        projectDao.delObj(set);
    }

    public Object find(Object object) throws BaseException {
        return projectDao.find(object);
    }

    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return projectDao.query(object, pageArg);
    }

    public void updateObj(Object object) throws Exception {
        projectDao.updateObj(object);
    }

	@Override
	public Object findExceptself(Object object) throws BaseException {
		return projectDao.findExceptself(object);
	}

	@Override
	public Object selectPowerAdmin() throws BaseException {
		return projectDao.selectPowerAdmin();
	}

}
