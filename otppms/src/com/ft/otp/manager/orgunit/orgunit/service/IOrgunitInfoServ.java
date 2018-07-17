package com.ft.otp.manager.orgunit.orgunit.service;

import java.util.List;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;

/**
 * 组织机构数据业务接口提供类
 *
 * @Date in January 22, 2013,10:00:00 PM
 *
 * @author BYL
 */
public interface IOrgunitInfoServ extends IBaseService{

	/**
     * 查找全部记录
     * @param object
     * @return List
     * @throws BaseException
     */
	public List<?> queryWholeList(Object object) throws BaseException;
	
	/**
	 * 根据域ID查出该域下组织机构信息
	 * @param object
	 * @return
	 * @throws BaseException
	 */
	public List<?> queryOrgunit(Object object) throws BaseException;
	
	/**
	 * 根据机构名构查询
	 * @author LXH
	 * @date Sep 3, 2014 6:34:51 PM
	 * @param object
	 * @return
	 * @throws BaseException
	 */
	Object findOG(Object object) throws BaseException;
	
	 /**
     * 根据组织机构ID查出该组织机构下组织机构信息
     * @param object
     * @return
     * @throws BaseException
     */
    public List<?> queryOrgSonunit(Object object) throws BaseException;
}
