package com.ft.otp.manager.admin.admin_orgunit.dao.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.dao.namespace.AdminNSpace;
import com.ft.otp.base.dao.namespace.DomainNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.admin.admin_orgunit.dao.IAdminAndOrgunitDao;
import com.ft.otp.manager.admin.admin_orgunit.entity.AdminAndOrgunit;
import com.ft.otp.manager.orgunit.domain.entity.DomainInfo;

/**
 * 管理员-组织机构关系DAO接口类功能说明
 *
 * @Date in January 24, 2013,10:00:00 AM
 *
 * @author BYL
 */
public class AdminAndOrgunitDaoImpl extends BaseSqlMapDAO implements
        IAdminAndOrgunitDao {

    protected String getNameSpace() {
        return AdminNSpace.ADMIN_AND_ORGUNIT_NS;
    }

    private AdminAndOrgunit getAdminAndOrgunit(Object object) {
    	AdminAndOrgunit adminAndOrgunit = (AdminAndOrgunit) object;
        if (null == adminAndOrgunit) {
        	adminAndOrgunit = new AdminAndOrgunit();
        }
        return adminAndOrgunit;
    }

	public List<?> queryAdminAndOrgunit(Object object) throws BaseException {
		AdminAndOrgunit adminAndOrgunit= getAdminAndOrgunit(object);
		return queryForList(AdminNSpace.ADMIN_ORGUNIT_SELECT_AAO, adminAndOrgunit);
	}

	public void addObj(Object object) throws BaseException {
		AdminAndOrgunit adminAndOrgunit = getAdminAndOrgunit(object);
        insert(AdminNSpace.ADMIN_ORGUNIT_INSERT_AO, adminAndOrgunit);
	}

	public int count(Object object) throws BaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	public void delObj(Object object) throws BaseException {
		AdminAndOrgunit adminAndOrgunit=(AdminAndOrgunit)getAdminAndOrgunit(object);
		delete(AdminNSpace.ADMIN_ORGUNIT_DELETE,adminAndOrgunit);
		
	}

	public void delObj(Set<?> set) throws BaseException {
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
         for (int i = 0; i < list.size(); i++) {
        	 addObj(list.get(i));
         }
	}



}
