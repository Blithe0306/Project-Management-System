package com.ft.otp.manager.confinfo.radius.service.impl;

import java.util.List;
import java.util.Set;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.BaseService;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.confinfo.radius.dao.IRadProfileDao;
import com.ft.otp.manager.confinfo.radius.service.IRadProfileServ;

/**
 * RADIUS配置实现
 * 
 * @Date in Oct 29, 2012,4:38:36 PM
 * 
 * @version v1.0
 * 
 * @author YXS
 */
public class RadProfileServImpl extends BaseService implements IRadProfileServ {

	private IRadProfileDao radProfileDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.service.IBaseService#addObj(java.lang.Object)
	 */
	public void addObj(Object object) throws BaseException {
		radProfileDao.addObj(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.service.IBaseService#count(java.lang.Object)
	 */
	public int count(Object object) throws BaseException {
		return radProfileDao.count(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.service.IBaseService#delObj(java.lang.Object)
	 */
	public void delObj(Object object) throws BaseException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.service.IBaseService#delObj(java.util.Set)
	 */
	public void delObj(Set<?> keys) throws BaseException {
		radProfileDao.delObj(keys);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.service.IBaseService#find(java.lang.Object)
	 */
	public Object find(Object object) throws BaseException {
		return radProfileDao.find(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.service.IBaseService#query(java.lang.Object,
	 *      com.ft.otp.common.page.PageArgument)
	 */
	public List<?> query(Object object, PageArgument pageArg)
			throws BaseException {
		return radProfileDao.query(object, pageArg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
	 */
	public void updateObj(Object object) throws Exception {
		radProfileDao.updateObj(object);
	}

	public IRadProfileDao getRadProfileDao() {
		return radProfileDao;
	}

	public void setRadProfileDao(IRadProfileDao radProfileDao) {
		this.radProfileDao = radProfileDao;
	}

}
