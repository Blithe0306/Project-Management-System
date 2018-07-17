/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.access.dao.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.dao.namespace.AccessNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.confinfo.access.dao.IAccessConDao;
import com.ft.otp.manager.confinfo.access.entity.AccessInfo;

/**
 * 访问控制策略DAO实现
 *
 * @Date in Dec 27, 2012,5:43:45 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class AccessConDaoImpl extends BaseSqlMapDAO implements IAccessConDao {

    protected String getNameSpace() {
        return AccessNSpace.ACCESS_INFO;
    }
    
    private AccessInfo getAccessInfo(Object object) {
        AccessInfo account = (AccessInfo)object;
        if (null == account) {
            account = new AccessInfo();
        }
        return account;
    }
    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        AccessInfo accessInfo = getAccessInfo(object);
        insert(AccessNSpace.ADD_ACCESS_INFO, accessInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#count(java.lang.Object)
     */
    public int count(Object object) throws BaseException {
        return 0;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
        AccessInfo accessInfo = getAccessInfo(object);
        delete(AccessNSpace.DELETE_ACCESS_INFO, accessInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#delObj(java.util.Set)
     */
    public void delObj(Set<?> set) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#find(java.lang.Object)
     */
    public Object find(Object object) throws BaseException {
        AccessInfo accessInfo = getAccessInfo(object);
        return queryForObject(AccessNSpace.FIND_ACCESS_INFO, accessInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        AccessInfo accessInfo = getAccessInfo(object);
        accessInfo.setStartRow(pageArg.getStartRow());
        accessInfo.setPageSize(pageArg.getPageSize());
        List<?> list = queryForList(AccessNSpace.SELECT_ACCESS_INFO, accessInfo);
        return list;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.confinfo.access.dao.IAccessConDao#queryIPDList(java.lang.Object)
     */
    public List<?> queryIPDList(Object object, PageArgument pageArg) throws BaseException {
        AccessInfo accessInfo = getAccessInfo(object);
        accessInfo.setStartRow(pageArg.getStartRow());
        accessInfo.setPageSize(pageArg.getPageSize());
        List<?> list = queryForList(AccessNSpace.SELECT_ACCESS_IPD, accessInfo);
        return list;
    }

}
