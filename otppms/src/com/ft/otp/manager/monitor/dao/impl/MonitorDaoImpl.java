/**
 *Administrator
 */
package com.ft.otp.manager.monitor.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ft.otp.base.dao.namespace.MonitorNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.monitor.dao.IMonitorDao;
import com.ft.otp.manager.monitor.entity.MonitorAndAdminInfo;
import com.ft.otp.manager.monitor.entity.MonitorInfo;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 监控预警 DAO 实现类
 *
 * @Date in Mar 5, 2013,1:20:25 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class MonitorDaoImpl extends BaseSqlMapDAO implements IMonitorDao {

    protected String getNameSpace() {
        return MonitorNSpace.MONITOR_INFO_NS;
    }

    private MonitorAndAdminInfo getMonitorAndAdminInfo(Object object) {
        MonitorAndAdminInfo monitorInfo = (MonitorAndAdminInfo) object;
        if (monitorInfo == null) {
            monitorInfo = new MonitorAndAdminInfo();
        }
        return monitorInfo;
    }

    private MonitorInfo getMonitorInfo(Object object) {
        MonitorInfo monitorInfo = (MonitorInfo) object;
        if (monitorInfo == null) {
            monitorInfo = new MonitorInfo();
        }
        return monitorInfo;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.monitor.dao.IMonitorDao#addMonitorAndAdmin(java.util.List)
     */
    public void addMonitorAndAdmin(final List<Object> list) throws BaseException {
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                MonitorAndAdminInfo mInfo = null;
                int batch = 0;
                executor.startBatch();
                for (int i = 0; i < list.size(); i++) {
                    mInfo = (MonitorAndAdminInfo) list.get(i);
                    insert(MonitorNSpace.MONITOR_ADMIN_INFO_ADD, mInfo);
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
     * @see com.ft.otp.manager.monitor.dao.IMonitorDao#queryMonitorAndAdmin(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> queryMonitorAndAdmin(Object object, PageArgument pageArg) throws BaseException {
        MonitorAndAdminInfo monitorAndAdminInfo = getMonitorAndAdminInfo(object);
        return queryForList(MonitorNSpace.MONITOR_ADMIN_INFO_QUERY, monitorAndAdminInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.monitor.dao.IMonitorDao#updateMonitorInfo(java.util.List)
     */
    public void updateMonitorAndAdmin(final List<Object> list) throws BaseException {
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                MonitorAndAdminInfo mInfo = null;
                int batch = 0;
                executor.startBatch();
                for (int i = 0; i < list.size(); i++) {
                    mInfo = (MonitorAndAdminInfo) list.get(i);
                    update(MonitorNSpace.MONITOR_ADMIN_INFO_UPDATE, mInfo);
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
     * @see com.ft.otp.manager.monitor.dao.IMonitorDao#delMonitorAndAdmin(java.util.List)
     */
    public void delMonitorAndAdmin(final List<Object> list) throws BaseException {
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                MonitorAndAdminInfo mInfo = null;
                int batch = 0;
                executor.startBatch();
                for (int i = 0; i < list.size(); i++) {
                    mInfo = (MonitorAndAdminInfo) list.get(i);
                    delete(MonitorNSpace.MONITOR_ADMIN_INFO_DELETE, mInfo);
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

    // ***********************************关系表 end
    /* (non-Javadoc)
     * @see com.ft.otp.manager.monitor.dao.IMonitorDao#addMonitorAndAdmin(java.util.List)
     */
    public void addMonitorInfo(final List<Object> list) throws BaseException {
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                MonitorInfo mInfo = null;
                int batch = 0;
                executor.startBatch();
                for (int i = 0; i < list.size(); i++) {
                    mInfo = (MonitorInfo) list.get(i);
                    insert(MonitorNSpace.MONITOR_INFO_ADD, mInfo);
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
     * @see com.ft.otp.manager.monitor.dao.IMonitorDao#queryMonitorAndAdmin(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> queryMonitorInfo(Object object, PageArgument pageArg) throws BaseException {
        MonitorInfo monitorInfo = getMonitorInfo(object);
        return queryForList(MonitorNSpace.MONITOR_INFO_QUERY, monitorInfo);
    }
    
    public List<?> queryMonitorOkInfo(Object object, PageArgument pageArg) throws BaseException {
        MonitorInfo monitorInfo = getMonitorInfo(object);
        return queryForList(MonitorNSpace.MONITOR_INFO_QUERY_OK, monitorInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.monitor.dao.IMonitorDao#updateMonitorInfo(java.util.List)
     */
    public void updateMonitorInfo(final List<Object> list) throws BaseException {
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                MonitorInfo mInfo = null;
                int batch = 0;
                executor.startBatch();
                for (int i = 0; i < list.size(); i++) {
                    mInfo = (MonitorInfo) list.get(i);
                    update(MonitorNSpace.MONITOR_INFO_UPDATE, mInfo);
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
     * @see com.ft.otp.manager.monitor.dao.IMonitorDao#delMonitorAndAdmin(java.util.List)
     */
    public void delMonitorInfo(final List<Object> list) throws BaseException {
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                MonitorInfo mInfo = null;
                int batch = 0;
                executor.startBatch();
                for (int i = 0; i < list.size(); i++) {
                    mInfo = (MonitorInfo) list.get(i);
                    delete(MonitorNSpace.MONITOR_INFO_DELETE, mInfo);
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
     * @see com.ft.otp.base.dao.IBaseDao#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        MonitorInfo monitorInfo = getMonitorInfo(object);
        insert(MonitorNSpace.MONITOR_INFO_ADD, monitorInfo);
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
        MonitorInfo monitorInfo = getMonitorInfo(object);
        delete(MonitorNSpace.MONITOR_INFO_DELETE, monitorInfo);
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
        MonitorInfo monitorInfo = getMonitorInfo(object);
        return queryForList(MonitorNSpace.MONITOR_INFO_QUERY_MAX_SENDTIME, monitorInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
        MonitorInfo monitorInfo = getMonitorInfo(object);
        update(MonitorNSpace.MONITOR_INFO_UPDATE, monitorInfo);
    }
}
