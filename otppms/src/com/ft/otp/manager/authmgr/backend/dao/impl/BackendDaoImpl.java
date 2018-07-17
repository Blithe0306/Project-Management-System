/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.backend.dao.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ft.otp.base.dao.namespace.AdminNSpace;
import com.ft.otp.base.dao.namespace.AuthMgrNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.authmgr.backend.dao.IBackendDao;
import com.ft.otp.manager.authmgr.backend.entity.BackendInfo;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 后端认证DAO实现类
 *
 * @Date in Jan 21, 2013,8:21:01 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class BackendDaoImpl extends BaseSqlMapDAO implements IBackendDao {

    protected String getNameSpace() {
        return AuthMgrNSpace.Backend_INFO_NS;
    }
    
    private BackendInfo getBackendInfo(Object obj) {
        BackendInfo backendInfo = (BackendInfo)obj;
        if (backendInfo == null) {
            backendInfo = new BackendInfo();
        }
        return backendInfo;
    }
    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        BackendInfo backendInfo = getBackendInfo(object);
        insert(AuthMgrNSpace.Backend_INFO_ADD_BD, backendInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#count(java.lang.Object)
     */
    public int count(Object object) throws BaseException {
        BackendInfo backendInfo = getBackendInfo(object);
        return (Integer) queryForObject(AuthMgrNSpace.Backend_INFO_COUNT_BD, backendInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#delObj(java.util.Set)
     */
    public void delObj(final Set<?> set) throws BaseException {
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor)
                    throws SQLException {
                int batch = 0;
                executor.startBatch();
                BackendInfo backend = new BackendInfo();
                Iterator<?> iter = set.iterator();
                while (iter.hasNext()) {
                    String id = (String) iter.next();
                    backend.setId(Integer.parseInt(id));
                    delete(AuthMgrNSpace.Backend_INFO_DEL_BD, backend);
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
     * @see com.ft.otp.base.dao.IBaseDao#find(java.lang.Object)
     */
    public Object find(Object object) throws BaseException {
        BackendInfo backendInfo = getBackendInfo(object);
        return queryForObject(AuthMgrNSpace.Backend_INFO_FIND_BD, backendInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        BackendInfo backendInfo = getBackendInfo(object);
        backendInfo.setStartRow(pageArg.getStartRow());
        backendInfo.setPageSize(pageArg.getPageSize());
        return queryForList(AuthMgrNSpace.Backend_INFO_SELECT_BD, backendInfo, pageArg.getStartRow(), pageArg.getPageSize());
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
        BackendInfo backendInfo = getBackendInfo(object);
        update(AuthMgrNSpace.Backend_INFO_UPDATE_BD, backendInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.authmgr.backend.dao.IBackendDao#queryUKIsExist(java.lang.Object)
     */
    public Object queryUKIsExist(Object object) throws BaseException {
        BackendInfo backendInfo = getBackendInfo(object);
        return queryForObject(AuthMgrNSpace.Backend_INFO_FIND_UKBD, backendInfo);
    }
    
    public void updateEnabled(BackendInfo backendInfo, int enabled) throws BaseException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("enabled", enabled);
        map.put("id", backendInfo.getId());
        update(AuthMgrNSpace.Backend_INFO_ENABLED_BD, map);

    }

}
