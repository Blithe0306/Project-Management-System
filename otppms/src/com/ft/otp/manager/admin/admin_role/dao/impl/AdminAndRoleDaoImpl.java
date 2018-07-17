/**
 *Administrator
 */
package com.ft.otp.manager.admin.admin_role.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ft.otp.base.dao.namespace.AdminNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.admin.admin_role.dao.IAdminAndRoleDao;
import com.ft.otp.manager.admin.admin_role.entity.AdminAndRole;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 *管理员-角色关系DAO实现类功能说明
 *
 * @Date in Jul 1, 2011,3:37:28 PM
 *
 * @author ZJY
 */
public class AdminAndRoleDaoImpl extends BaseSqlMapDAO implements IAdminAndRoleDao {

    protected String getNameSpace() {
        return AdminNSpace.ADMIN_AND_ROLE_NS;
    }

    private AdminAndRole getAdminAndRole(Object object) {
        AdminAndRole adminAndRole = (AdminAndRole) object;
        if (null == adminAndRole) {
            adminAndRole = new AdminAndRole();
        }
        return adminAndRole;
    }

    public void addObj(Object object) throws BaseException {
        insert(AdminNSpace.ADMIN_AND_ROLE_INSERT_ANR, getAdminAndRole(object));
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#count(java.lang.Object)
     */
    public int count(Object object) throws BaseException {
        AdminAndRole adminAndRole = getAdminAndRole(object);
        return (Integer) queryForObject(AdminNSpace.ADMIN_AND_ROLE_COUNT_ANR, adminAndRole);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
        AdminAndRole adminAndRole = getAdminAndRole(object);
        delete(AdminNSpace.ADMIN_AND_ROLE_DELETE_ANR, adminAndRole);
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
        AdminAndRole adminAndRole = getAdminAndRole(object);
        return queryForList(AdminNSpace.ADMIN_AND_ROLE_SELECT_ANR, adminAndRole);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.role_perm.dao.IRolePermDao#addRolePerm(java.util.List)
     */
    public void addAdminAndRole(final List<?> list) throws BaseException {
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {

                AdminAndRole adminAndRole = null;
                int batch = 0;
                executor.startBatch();
                for (int i = 0; i < list.size(); i++) {
                    adminAndRole = (AdminAndRole) list.get(i);
                    insert(AdminNSpace.ADMIN_AND_ROLE_INSERT_ANR, adminAndRole);
                    batch++;
                    if (batch == NumConstant.batchCount) {
                        executor.executeBatch();
                        batch = 0;
                    }
                }

                executor.executeBatch();
                return null;
            }
        });
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.admin_role.dao.IAdminAndRoleDao#updateRolePerm(java.util.List)
     */
    public void updateAdminAndRole(final List<?> list) throws BaseException {
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {

                AdminAndRole del_adminAndRole = null;
                AdminAndRole adminAndRole = null;
                int batch = 0;
                executor.startBatch();
                String temp[] = new String[1];
                del_adminAndRole = (AdminAndRole) list.get(0);
                temp[0] = del_adminAndRole.getAdminId();
                del_adminAndRole.setBatchIds(temp);
                delete(AdminNSpace.ADMIN_AND_ROLE_DELETE_ANR, del_adminAndRole);
                for (int i = 0; i < list.size(); i++) {
                    adminAndRole = (AdminAndRole) list.get(i);
                    insert(AdminNSpace.ADMIN_AND_ROLE_INSERT_ANR, adminAndRole);
                    batch++;
                    if (batch == NumConstant.batchCount) {
                        executor.executeBatch();
                        batch = 0;
                    }
                }

                executor.executeBatch();
                return null;
            }
        });
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.admin_role.dao.IAdminAndRoleDao#queryAdmins(java.lang.Object)
     */
    public List<?> queryAdmins(Object object) throws BaseException {
        AdminAndRole adminAndRole = getAdminAndRole(object);
        return queryForList(AdminNSpace.ADMIN_AND_ROLE_ALL_ANR, adminAndRole);
    }
}
