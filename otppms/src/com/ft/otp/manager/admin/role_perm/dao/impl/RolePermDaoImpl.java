/**
 *Administrator
 */
package com.ft.otp.manager.admin.role_perm.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ft.otp.base.dao.namespace.AdminNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.admin.role_perm.dao.IRolePermDao;
import com.ft.otp.manager.admin.role_perm.entity.RolePerm;
import com.ft.otp.util.tool.StrTool;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 角色-权限关系DAO实现类功能说明
 *
 * @Date in Jul 1, 2011,3:37:28 PM
 *
 * @author ZJY
 */
public class RolePermDaoImpl extends BaseSqlMapDAO implements IRolePermDao {

    protected String getNameSpace() {
        return AdminNSpace.ROLE_PERM_NS;
    }

    private RolePerm getRolePerm(Object object) {
        RolePerm rolePerm = (RolePerm) object;
        if (null == rolePerm) {
            rolePerm = new RolePerm();
        }
        return rolePerm;
    }

    public void addObj(Object object) throws BaseException {
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
        RolePerm rolePerm = getRolePerm(object);
        delete(AdminNSpace.ROLE_PERM_DELETE_RP, rolePerm);
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
        RolePerm rolePerm = getRolePerm(object);
        return queryForList(AdminNSpace.ROLE_PERM_SELECT_RP, rolePerm);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.role_perm.dao.IRolePermDao#addRolePerm(java.util.List)
     */
    public void addRolePerm(final List<?> list) throws BaseException {
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {

                RolePerm rolePerm = null;
                int batch = 0;
                executor.startBatch();
                for (int i = 0; i < list.size(); i++) {
                    rolePerm = (RolePerm) list.get(i);
                    insert(AdminNSpace.ROLE_PERM_INSERT_RP, rolePerm);
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

    public void updateRolePerm(final List<?> list) throws BaseException {
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                RolePerm del_rolePerm = null;
                RolePerm rolePerm = null;
                int batch = 0;
                executor.startBatch();
                String temp[] = new String[1];
                del_rolePerm = (RolePerm) list.get(0);
                temp[0] = StrTool.intToString(del_rolePerm.getRoleId());
                int[] test = { del_rolePerm.getRoleId() };
                del_rolePerm.setBatchIdsInt(test);
                del_rolePerm.setBatchIds(temp);
                delete(AdminNSpace.ROLE_PERM_DELETE_RP, del_rolePerm);
                if (StrTool.strNotNull(del_rolePerm.getPermcode())) {
                    for (int i = 0; i < list.size(); i++) {
                        rolePerm = (RolePerm) list.get(i);
                        insert(AdminNSpace.ROLE_PERM_INSERT_RP, rolePerm);
                        batch++;
                        if (batch == NumConstant.batchCount) {
                            executor.executeBatch();
                            batch = 0;
                        }
                    }
                }
                executor.executeBatch();
                return null;
            }
        });
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.admin.role_perm.dao.IRolePermDao#queryAdmPerms(java.lang.Object)
     */
    public List<?> queryAdmPerms(Object object) throws BaseException {
        RolePerm rolePerm = getRolePerm(object);
        return queryForList(AdminNSpace.ADMIN_ROLE_PERM_SELECT_ARP, rolePerm);
    }

}
