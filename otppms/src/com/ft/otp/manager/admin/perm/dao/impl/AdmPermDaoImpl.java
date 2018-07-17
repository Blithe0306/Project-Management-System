/**
 *Administrator
 */
package com.ft.otp.manager.admin.perm.dao.impl;

import java.util.List;
import java.util.Set;
import com.ft.otp.base.dao.namespace.AdminNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.admin.perm.dao.IAdmPermDao;
import com.ft.otp.manager.admin.perm.entity.AdminPerm;

/**
 * 管理员权限DAO接口实现
 *
 * @Date in Jun 29, 2011,5:04:56 PM
 *
 * @author TBM
 */
public class AdmPermDaoImpl extends BaseSqlMapDAO implements IAdmPermDao {

    protected String getNameSpace() {
        return AdminNSpace.ADMIN_PERM_NS;
    }

    private AdminPerm getAdminPerm(Object object) {
        AdminPerm adminPerm = (AdminPerm) object;
        if (null == adminPerm) {
            adminPerm = new AdminPerm();
        }
        return adminPerm;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#count(java.lang.Object)
     */
    public int count(Object object) throws BaseException {
        AdminPerm adminPerm = getAdminPerm(object);
        return (Integer) queryForObject(AdminNSpace.ADMIN_PERM_COUNT_AP, adminPerm);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
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
    public List<?> query(Object object, PageArgument pageArg)
            throws BaseException {
        AdminPerm adminPerm = getAdminPerm(object);
        return queryForList(AdminNSpace.ADMIN_PERM_SELECT_AP, adminPerm);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.perm.dao.IAdmPermDao#queryRolePerm(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> queryRolePerm(Object object, PageArgument pageArg) throws BaseException {
        AdminPerm adminPerm = getAdminPerm(object);
        return queryForList(AdminNSpace.ADMIN_PERM_SELECT_ROLEAP, adminPerm);
    }

}
