/**
 *Administrator
 */
package com.ft.otp.manager.admin.user.dao.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import com.ft.otp.base.dao.namespace.AdminNSpace;
import com.ft.otp.base.dao.namespace.UserNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.DbConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.database.pool.DbEnv;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.admin.user.dao.IAdminUserDao;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 管理员用户业务数据接口实现类
 *
 * @Date in Apr 17, 2011,1:20:37 PM
 *
 * @author TBM
 */
public class AdminUserDaoImpl extends BaseSqlMapDAO implements IAdminUserDao {

    protected String getNameSpace() {
        return AdminNSpace.ADMIN_USER_NS;
    }

    private AdminUser getAdminUser(Object object) {
        AdminUser adminUser = (AdminUser) object;
        if (null == adminUser) {
            adminUser = new AdminUser();
        }
        return adminUser;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        AdminUser adminUser = getAdminUser(object);
        insert(AdminNSpace.ADMIN_USER_INSERT_AU, adminUser);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#count(com.ft.otp.base.form.BaseQueryForm)
     */
    public int count(Object object) throws BaseException {
        AdminUser adminUser = getAdminUser(object);
        return (Integer) queryForObject(AdminNSpace.ADMIN_USER_COUNT_AU, adminUser);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
        AdminUser adminUser = getAdminUser(object);
        delete(AdminNSpace.ADMIN_USER_DELETE_AU, adminUser);
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
        AdminUser adminUser = getAdminUser(object);
        return queryForObject(AdminNSpace.ADMIN_USER_FIND_AU, adminUser);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#query(com.ft.otp.base.form.BaseQueryForm, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        AdminUser adminUser = getAdminUser(object);
        adminUser.setStartRow(pageArg.getStartRow());
        adminUser.setPageSize(pageArg.getPageSize());
        String dbtype = DbEnv.getDbtype();
        if (dbtype.equals(DbConstant.DB_TYPE_SYBASE)) {
            return queryForList(AdminNSpace.ADMIN_USER_SELECT_AU, adminUser, pageArg.getStartRow(), pageArg
                    .getPageSize());
        } else {
            if (adminUser.getStartRow() < 0) {
                adminUser.setStartRow(0);
            }
            return queryForList(AdminNSpace.ADMIN_USER_SELECT_AU, adminUser);
        }

    }

    public List<?> queryExceptPowerAdmin(Object object, PageArgument pageArg) throws BaseException {
        AdminUser adminUser = getAdminUser(object);
        adminUser.setStartRow(pageArg.getStartRow());
        adminUser.setPageSize(pageArg.getPageSize());
        String dbtype = DbEnv.getDbtype();
        if (dbtype.equals(DbConstant.DB_TYPE_SYBASE)) {
            return queryForList(AdminNSpace.ADMIN_USER_SELECT_AUEPA, adminUser, pageArg.getStartRow(), pageArg
                    .getPageSize());
        } else {
            if (adminUser.getStartRow() < 0) {
                adminUser.setStartRow(0);
            }
            return queryForList(AdminNSpace.ADMIN_USER_SELECT_AUEPA, adminUser);
        }

    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws Exception {
        AdminUser adminUser = getAdminUser(object);
        update(AdminNSpace.ADMIN_USER_UPDATE_AU, adminUser);
    }
    
    public void updatePass(Object object) throws Exception {
        AdminUser adminUser = getAdminUser(object);
        update(AdminNSpace.ADMIN_USER_UPDATE_AP, adminUser);
    }
    
    public void updatePwddeath(Object object) throws Exception {
        AdminUser adminUser = getAdminUser(object);
        update(AdminNSpace.ADMIN_USER_UPDATE_PWDEATH, adminUser);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.user.dao.IAdminUserDao#getChildAdmins(java.lang.Object)
     */
    public List<?> getChildAdmins(Object object, PageArgument pageArgument, String method) throws BaseException {
        AdminUser adminUser = getAdminUser(object);
        return queryForList(method, adminUser);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.user.dao.IAdminUserDao#disabledAdmin(java.lang.String, int)
     */
    public void updateEnabled(String adminid, int enabled) throws BaseException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("enabled", enabled);
        map.put("adminid", adminid);
        update(AdminNSpace.ADMIN_USER_ENABLED_AU, map);

    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.user.dao.IAdminUserDao#updateLocked(java.lang.Object)
     */
    public void updateLocked(Object object) throws BaseException {
        AdminUser adminUser = getAdminUser(object);
        update(AdminNSpace.ADMIN_USER_LOCKED_AU, adminUser);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.user.dao.IAdminUserDao#updatePassword(java.lang.Object)
     */
    public void updatePassword(Object object) throws BaseException {
        AdminUser adminUser = getAdminUser(object);
        update(AdminNSpace.PASSWORD_INFO_UPDATE_AU, adminUser);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.user.dao.IAdminUserDao#queryAdminDesignate(java.lang.Object)
     */
    public List<?> queryAdminDesignate(Object object) throws BaseException {
        AdminUser adminUser = getAdminUser(object);
        return queryForList(AdminNSpace.ADMIN_SELECT_CHANGE_CREATER_AU, adminUser);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.user.dao.IAdminUserDao#findSuperDesignate(java.lang.Object)
     */
    public Object findSuperDesignate(Object object) throws BaseException {
        AdminUser adminUser = getAdminUser(object);
        return queryForObject(AdminNSpace.ADMIN_FIND_SUPER_DESIGNATE, adminUser);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.user.dao.IAdminUserDao#updateDsignate(java.lang.Object)
     */
    public void updateDsignate(final Object object) throws BaseException {
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                List<Object> list = (List<Object>) object;
                int batch = 0;
                executor.startBatch();
                for (int i = 0; i < list.size(); i++) {
                    AdminUser otpUser = (AdminUser) list.get(i);
                    update(AdminNSpace.ADMIN_MODIFY_DESIGNATE, otpUser);
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
     * @see com.ft.otp.manager.admin.user.dao.IAdminUserDao#updateAU(java.lang.Object)
     */
    public void updateAU(Object object) throws BaseException {
        AdminUser adminUser = getAdminUser(object);
        update(AdminNSpace.ADMIN_MODIFY_AU, adminUser);
    }
    
    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.user.dao.IAdminUserDao#updateAU(java.lang.Object)
     */
    public List<?> selectAmdEmail(Object object) throws BaseException {
    	AdminUser adminUser = getAdminUser(object);
        return queryForList(AdminNSpace.ADMIN_INFO_QUERYUI_EMAIL, adminUser);
    }

}
