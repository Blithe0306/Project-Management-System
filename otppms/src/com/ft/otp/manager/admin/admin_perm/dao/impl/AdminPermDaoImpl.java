/**
 *Administrator
 */
package com.ft.otp.manager.admin.admin_perm.dao.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.dao.namespace.AdminNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.admin.admin_perm.dao.IAdminPermDao;
import com.ft.otp.manager.admin.admin_perm.entity.AdminPermCode;

/**
 * 管理员常用功能DAO 实现类
 *
 * @Date in May 14, 2013,2:26:49 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class AdminPermDaoImpl extends BaseSqlMapDAO implements IAdminPermDao {

    protected String getNameSpace() {
        return AdminNSpace.ADMIN_PERM_NS;
    }

    private AdminPermCode getAdminPermCode(Object object) {
        AdminPermCode adminPermCode = (AdminPermCode) object;
        if (null == adminPermCode) {
            adminPermCode = new AdminPermCode();
        }
        return adminPermCode;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        AdminPermCode adminPermCode = getAdminPermCode(object);
        insert(AdminNSpace.ADMIN_PERM_ADD_PERMCODE, adminPermCode);
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
        AdminPermCode adminPermCode = getAdminPermCode(object);
        delete(AdminNSpace.ADMIN_PERM_DEL_PERMCODE, adminPermCode);
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
        return null;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        AdminPermCode adminPermCode = getAdminPermCode(object);
        return queryForList(AdminNSpace.ADMIN_PERM_FIND_PERMCODE, adminPermCode);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws Exception {
    }

}
