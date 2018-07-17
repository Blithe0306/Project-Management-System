package com.ft.otp.manager.orgunit.orgunit.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.orgunit.orgunit.dao.IOrgunitInfoDao;
import com.ft.otp.manager.orgunit.orgunit.entity.OrgunitInfo;
import com.ft.otp.manager.orgunit.orgunit.service.IOrgunitInfoServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 组织机构数据业务接口提供类
 *
 * @Date in January 22, 2013,10:00:00 PM
 *
 * @author BYL
 */
public class OrgunitInfoServImpl implements IOrgunitInfoServ {

	private IOrgunitInfoDao orgunitInfoDao;
	 
    public IOrgunitInfoDao getOrgunitInfoDao() {
        return orgunitInfoDao;
    }
	
    public void setOrgunitInfoDao(IOrgunitInfoDao orgunitInfoDao) {
        this.orgunitInfoDao = orgunitInfoDao;
    }
    
    /** 
	 * 增加 组织机构
	 * @param object
	 * @throws BaseException
	 */
	public void addObj(Object object) throws BaseException {
		orgunitInfoDao.addObj(object);
	}

	/** 
	 * 计算数量
	 * @param object
	 * @return int
	 * @throws BaseException
	 */
	public int count(Object object) throws BaseException {
		return orgunitInfoDao.count(object);
	}

	/** 
	 * 删除组织机构
	 * @param object
	 * @throws BaseException
	 */
	public void delObj(Object object) throws BaseException {
		orgunitInfoDao.delObj(object);
	}

	/** 
	 * 删除组织机构
	 * @param keys
	 * @throws BaseException
	 */
	public void delObj(Set<?> keys) throws BaseException {
		Iterator<?> iterator = keys.iterator();
        OrgunitInfo orgunitInfo = new OrgunitInfo();
        int[] temp = new int[keys.size()];
        int i = 0;
        while (iterator.hasNext()) {
            temp[i] = StrTool.parseInt((String)iterator.next());
            i++;
        }
        orgunitInfo.setBatchIdsInt(temp);
        if(StrTool.objNotNull(orgunitInfo.getBatchIdsInt())){
        	orgunitInfoDao.delObj(orgunitInfo);
        }
	}

	/** 
	 * 指定组织机构查找
	 * @param object
	 * @return Object
	 * @throws BaseException
	 */
	public Object find(Object object) throws BaseException {
		return orgunitInfoDao.find(object);
	}

	/** 
	 * 查找组织机构
	 * @param object
	 * @param pageArg
	 * @return List
	 * @throws BaseException
	 */
	public List<?> query(Object object, PageArgument pageArg)
			throws BaseException {
		return orgunitInfoDao.query(object, pageArg);
	}

	/** 
	 * 更新组织机构
	 * @param object
	 * @throws Exception
	 */
	public void updateObj(Object object) throws Exception {
		orgunitInfoDao.updateObj(object);
	}
	
	/** 
	 * 查询全部记录 不分页
	 * @param object
	 * @return List
	 * @throws BaseException
	 */
	public List<?> queryWholeList(Object object) throws BaseException {
		return orgunitInfoDao.queryWholeList(object);
	}
	
	/**
	 * 根据域ID查出该域下组织机构信息
	 * @param object
	 * @return
	 * @throws BaseException
	 */
	public List<?> queryOrgunit(Object object) throws BaseException {
		return orgunitInfoDao.queryOrgunit(object);
	}

	/**
	 * 根据机构名构查询
	 */
	public Object findOG(Object object) throws BaseException {
		return orgunitInfoDao.findOG(object);
	}

    public List<?> queryOrgSonunit(Object object) throws BaseException {
        return orgunitInfoDao.queryOrgSonunit(object);
    }
}
