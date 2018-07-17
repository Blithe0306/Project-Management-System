package com.ft.otp.manager.orgunit.domain.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.orgunit.domain.dao.IDomainInfoDao;
import com.ft.otp.manager.orgunit.domain.entity.DomainInfo;
import com.ft.otp.manager.orgunit.domain.service.IDomainInfoServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 域数据业务接口提供类
 *
 * @Date in January 17, 2013,14:00:00 PM
 *
 * @author BYL
 */
public class DomainInfoServImpl implements IDomainInfoServ {

	private IDomainInfoDao domainInfoDao;
	 
    public IDomainInfoDao getDomainInfoDao() {
        return domainInfoDao;
    }
	
    public void setDomainInfoDao(IDomainInfoDao domainInfoDao) {
        this.domainInfoDao = domainInfoDao;
    }
    
    /** 
	 * 增加 域
	 * @param object
	 * @throws BaseException
	 */
	public void addObj(Object object) throws BaseException {
		domainInfoDao.addObj(object);

	}

	/** 
	 * 计算数量
	 * @param object
	 * @throws BaseException
	 * @return
	 */
	public int count(Object object) throws BaseException {
		return domainInfoDao.count(object);
	}

	/** 
	 * 删除域
	 * @param keys
	 * @throws BaseException
	 */
	public void delObj(Object object) throws BaseException {
		domainInfoDao.delObj(object);
	}

	/** 
	 * 删除域
	 * @param keys
	 * @throws BaseException
	 */
	public void delObj(Set<?> keys) throws BaseException {
		if(StrTool.objNotNull(keys)){
			Iterator<?> iterator = keys.iterator();
	        DomainInfo domainInfo = new DomainInfo();
	        int[] temp = new int[keys.size()];
	        int i = 0;
	        while (iterator.hasNext()) {
	            temp[i] = StrTool.parseInt((String)iterator.next());
	            i++;
	        }
	        domainInfo.setBatchIdsInt(temp);
	        if(StrTool.objNotNull(domainInfo.getBatchIdsInt())){
	        	domainInfoDao.delObj(domainInfo);
	        }
		}    
	}

	/** 
	 * 指定域查找
	 * @param object
	 * @throws BaseException
	 * @return
	 */
	public Object find(Object object) throws BaseException {
		return domainInfoDao.find(object);
	}

	/** 
	 *  域列表查找 分页
	 *  @param object
	 *  @param pageArg
	 *  @throws BaseException
	 *  @return
	 */
	public List<?> query(Object object, PageArgument pageArg)
			throws BaseException {
		return domainInfoDao.query(object, pageArg);
	}

	/** 
	 * 更新域
	 * @param object
	 * @throws BaseException
	 */
	public void updateObj(Object object) throws Exception {
		domainInfoDao.updateObj(object);
	}
	
}
