package com.ft.otp.manager.orgunit.orgunit.dao.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.dao.namespace.OrgunitNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.orgunit.orgunit.dao.IOrgunitInfoDao;
import com.ft.otp.manager.orgunit.orgunit.entity.OrgunitInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 组织机构数据业务接口提供类
 *
 * @Date in January 17, 2013,14:00:00 PM
 *
 * @author BYL
 */
public class OrgunitInfoDaoImpl extends BaseSqlMapDAO implements IOrgunitInfoDao {

	protected String getNameSpace() {
        return OrgunitNSpace.ORGUNIT_NS;
    }
	
	/**
	 * 转换对象
	 * @object
	 * @return OrgunitInfo
	 */
	private OrgunitInfo getOrgunitInfo(Object object) {
		OrgunitInfo orgunitInfo = (OrgunitInfo)object;
        return StrTool.objNotNull(orgunitInfo)?orgunitInfo:new OrgunitInfo();
    }
	
	/** 
	 * 增加 组织机构
	 * @param object
	 * @throws BaseException
	 */
	public void addObj(Object object) throws BaseException {
		OrgunitInfo orgunitInfo = getOrgunitInfo(object);
        insert(OrgunitNSpace.ORGUNIT_INFO_INSERT_OI, orgunitInfo);
	}

	/** 
	 * 计算数量
	 * @param object
	 * @return int
	 * @throws BaseException
	 */
	public int count(Object object) throws BaseException {
		OrgunitInfo orgunitInfo = getOrgunitInfo(object);
		return (Integer) queryForObject(OrgunitNSpace.ORGUNIT_INFO_COUNT_OI, orgunitInfo);
	}

	/** 
	 * 删除组织机构
	 * @param object
	 * @throws BaseException
	 */
	public void delObj(Object object) throws BaseException {
		OrgunitInfo orgunitInfo = getOrgunitInfo(object);
        delete(OrgunitNSpace.ORGUNIT_INFO_DELETE_OI, orgunitInfo);
	}

	public void delObj(Set<?> set) throws BaseException {
	}

	/** 
	 * 指定组织机构查找
	 * @param object
	 * @return Object
	 * @throws BaseException
	 */
	public Object find(Object object) throws BaseException {
		OrgunitInfo orgunitInfo = getOrgunitInfo(object);
        return queryForObject(OrgunitNSpace.ORGUNIT_INFO_FIND_OI, orgunitInfo);
	}

	public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return null;
	}

	/** 
	 * 更新组织机构
	 * @param object
	 * @throws BaseException
	 */
	public void updateObj(Object object) throws BaseException {
		OrgunitInfo orgunitInfo = getOrgunitInfo(object);
        update(OrgunitNSpace.ORGUNIT_INFO_UPDATE_OI, orgunitInfo);
	}
	
	/** 
	 * 查询全部记录 不分页
	 * @param object
	 * @return List
	 * @throws BaseException
	 */
	public List<?> queryWholeList(Object object) throws BaseException{
		OrgunitInfo orgunitInfo = getOrgunitInfo(object);
		return queryForList(OrgunitNSpace.ORGUNIT_INFO_SELECT_WOI, orgunitInfo);
	}
	
	/**
	 * 根据域ID查出该域下组织机构信息
	 * @param object
	 * @return
	 * @throws BaseException
	 */
	public List<?> queryOrgunit(Object object) throws BaseException{
		OrgunitInfo orgunitInfo = getOrgunitInfo(object);
		return queryForList(OrgunitNSpace.ORGUNIT_INFO_SELECT_ORG, orgunitInfo);
	}
	
	/**
	 * 根据机构名构查询
	 */
	public Object findOG(Object object) throws BaseException {
		OrgunitInfo orgunitInfo = getOrgunitInfo(object);
        return queryForObject(OrgunitNSpace.ORGUNIT_INFO_FIND_OR, orgunitInfo);
	}

	//查询组织机构下组织机构
    public List<?> queryOrgSonunit(Object object) throws BaseException {
        OrgunitInfo orgunitInfo = getOrgunitInfo(object);
        return queryForList(OrgunitNSpace.ORGUNIT_INFO_SELECT_ORG_SONORG, orgunitInfo);
    }
}
