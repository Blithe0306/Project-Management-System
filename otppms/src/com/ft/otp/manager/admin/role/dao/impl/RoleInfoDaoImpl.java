/**
 *Administrator
 */
package com.ft.otp.manager.admin.role.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ft.otp.base.dao.namespace.AdminNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.DbConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.database.pool.DbEnv;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.admin.admin_role.entity.AdminAndRole;
import com.ft.otp.manager.admin.role.dao.IRoleInfoDao;
import com.ft.otp.manager.admin.role.entity.RoleInfo;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 管理员角色DAO接口实现
 *
 * @Date in Jun 29, 2011,3:11:50 PM
 *
 * @author TBM
 */
public class RoleInfoDaoImpl extends BaseSqlMapDAO implements IRoleInfoDao {

    protected String getNameSpace() {
        return AdminNSpace.ADMIN_ROLE_NS;
    }

    private RoleInfo getAdminRole(Object object) {
        RoleInfo adminRole = (RoleInfo) object;
        if (null == adminRole) {
            adminRole = new RoleInfo();
        }
        return adminRole;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        RoleInfo adminRole = getAdminRole(object);
        insert(AdminNSpace.ADMIN_ROLE_INSERT_AR, adminRole);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#count(java.lang.Object)
     */
    public int count(Object object) throws BaseException {
        RoleInfo adminRole = getAdminRole(object);
        return (Integer) queryForObject(AdminNSpace.ADMIN_ROLE_COUNT_AR, adminRole);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
        RoleInfo adminRole = getAdminRole(object);
        delete(AdminNSpace.ADMIN_ROLE_DELETE_AR, adminRole);
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
        RoleInfo adminRole = getAdminRole(object);
        return queryForObject(AdminNSpace.ADMIN_ROLE_FIND_AR, adminRole);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        RoleInfo adminRole = getAdminRole(object);
        adminRole.setStartRow(pageArg.getStartRow());
        adminRole.setPageSize(pageArg.getPageSize());
        String dbtype = DbEnv.getDbtype();
        if (dbtype.equals(DbConstant.DB_TYPE_SYBASE)) {
            return queryForList(AdminNSpace.ADMIN_ROLE_SELECT_AR, adminRole, pageArg.getStartRow(), pageArg
                    .getPageSize());
        } else {
            return queryForList(AdminNSpace.ADMIN_ROLE_SELECT_AR, adminRole);
        }

    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
        RoleInfo adminRole = getAdminRole(object);
        update(AdminNSpace.ADMIN_ROLE_UPDATE_AR, adminRole);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.role.dao.IAdmRoleDao#getAdmsrolesList(java.lang.Object)
     */
    public List<?> getAdmsrolesList(Object object) throws BaseException {
        RoleInfo adminRole = getAdminRole(object);
        return queryForList(AdminNSpace.ADMIN_ROLE_ARS, adminRole);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.role.dao.IRoleInfoDao#updateDsignate(java.lang.Object)
     */

    public void updateDsignate(final Object object) throws BaseException {
        final List<RoleInfo> list = (List<RoleInfo>) object;
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                int batch = 0;
                executor.startBatch();
                for (int i = 0; i < list.size(); i++) {
                    RoleInfo role = (RoleInfo) list.get(i);
                    update(AdminNSpace.ROLE_UPDATEDDESIGNATE_AR, role);
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
    
    public int countDes(Object object) throws BaseException {
    	RoleInfo roleInfo = getAdminRole(object);
        return (Integer) queryForObject(AdminNSpace.ADMIN_AND_ADMIN_COUNT_ROLE, roleInfo);
	}
    
    public List<?> queryDes(Object object) throws BaseException {
    	RoleInfo roleInfo = getAdminRole(object);
        return queryForList(AdminNSpace.ADMIN_AND_ADMIN_ALL_ROLE, roleInfo);
    }

}
