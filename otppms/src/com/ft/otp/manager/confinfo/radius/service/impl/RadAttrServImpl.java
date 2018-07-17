package com.ft.otp.manager.confinfo.radius.service.impl;

import java.util.List;
import java.util.Set;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.BaseService;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.confinfo.radius.dao.IRadAttrDao;
import com.ft.otp.manager.confinfo.radius.service.IRadAttrServ;

/**
 * RADIUS配置属性管理实现
 * 
 * @Date in Oct 30, 2012,4:09:05 PM
 * 
 * @version v1.0
 * 
 * @author YXS
 */
public class RadAttrServImpl extends BaseService implements IRadAttrServ {

	private IRadAttrDao radAttrDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.service.IBaseService#addObj(java.lang.Object)
	 */
	public void addObj(Object object) throws BaseException {
		radAttrDao.addObj(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.service.IBaseService#count(java.lang.Object)
	 */
	public int count(Object object) throws BaseException {
		return radAttrDao.count(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.service.IBaseService#delObj(java.lang.Object)
	 */
	public void delObj(Object object) throws BaseException {
		radAttrDao.delObj(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.service.IBaseService#delObj(java.util.Set)
	 */
	public void delObj(Set<?> keys) throws BaseException {
		radAttrDao.delObj(keys);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.service.IBaseService#find(java.lang.Object)
	 */
	public Object find(Object object) throws BaseException {
		return radAttrDao.find(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.service.IBaseService#query(java.lang.Object,
	 *      com.ft.otp.common.page.PageArgument)
	 */
	public List<?> query(Object object, PageArgument pageArg)
			throws BaseException {
		return radAttrDao.query(object, pageArg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
	 */
	public void updateObj(Object object) throws Exception {
		radAttrDao.updateObj(object);
	}

	public IRadAttrDao getRadAttrDao() {
		return radAttrDao;
	}

	public void setRadAttrDao(IRadAttrDao radAttrDao) {
		this.radAttrDao = radAttrDao;
	}

}
