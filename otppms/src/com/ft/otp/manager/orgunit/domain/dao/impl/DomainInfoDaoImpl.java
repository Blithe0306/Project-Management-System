package com.ft.otp.manager.orgunit.domain.dao.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.dao.namespace.DomainNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.orgunit.domain.dao.IDomainInfoDao;
import com.ft.otp.manager.orgunit.domain.entity.DomainInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 域数据业务接口提供类
 *
 * @Date in January 17, 2013,14:00:00 PM
 *
 * @author BYL
 */
public class DomainInfoDaoImpl extends BaseSqlMapDAO implements IDomainInfoDao {

	protected String getNameSpace() {
        return DomainNSpace.DOMAIN_NS;
    }
	
	/**
	 * 转换成域对象
	 * @param object
	 * @return
	 */
	private DomainInfo getDomainInfo(Object object) {
		DomainInfo domainInfo = (DomainInfo) object;
        return StrTool.objNotNull(domainInfo)?domainInfo:(new DomainInfo());
    }
	
	/** 
	 * 增加 域
	 * @param object
	 * @throws BaseException
	 */
	public void addObj(Object object) throws BaseException {
		DomainInfo domainInfo = getDomainInfo(object);
        insert(DomainNSpace.DOMAIN_INFO_INSERT_DI, domainInfo);
	}

	/** 
	 * 计算数量
	 * @param object
	 * @throws BaseException
	 * @return
	 */
	public int count(Object object) throws BaseException {
		DomainInfo domainInfo = getDomainInfo(object);
		return (Integer) queryForObject(DomainNSpace.DOMAIN_INFO_COUNT_DI, domainInfo);
	}

	/** 
	 * 删除域
	 * @param object
	 * @throws BaseException
	 */
	public void delObj(Object object) throws BaseException {
		DomainInfo domainInfo = getDomainInfo(object);
        delete(DomainNSpace.DOMAIN_INFO_DELETE_DI, domainInfo);
	}

	public void delObj(Set<?> set) throws BaseException {
		
	}

	/** 
	 * 指定域查找
	 * @param object
	 * @throws BaseException
	 * @return
	 */
	public Object find(Object object) throws BaseException {
		DomainInfo domainInfo = getDomainInfo(object);
        return queryForObject(DomainNSpace.DOMAIN_INFO_FIND_DI, domainInfo);
	}

	/** 
	 *  域列表查找 分页
	 *  @param object
	 *  @param pageArg
	 *  @throws BaseException
	 *  @return
	 */
	public List<?> query(Object object, PageArgument pageArg) throws BaseException {
		DomainInfo domainInfo = getDomainInfo(object);
		domainInfo.setStartRow(pageArg.getStartRow());
		domainInfo.setPageSize(pageArg.getPageSize());
        if (domainInfo.getStartRow() < 0) {
        	domainInfo.setStartRow(0);
        }
        return queryForList(DomainNSpace.DOMAIN_INFO_SELECT_DI, domainInfo);
	}

	/** 
	 * 更新域
	 * @param object
	 * @throws BaseException
	 */
	public void updateObj(Object object) throws BaseException {
		DomainInfo domainInfo = getDomainInfo(object);
        update(DomainNSpace.DOMAIN_INFO_UPDATE_DI, domainInfo);
	}
}
