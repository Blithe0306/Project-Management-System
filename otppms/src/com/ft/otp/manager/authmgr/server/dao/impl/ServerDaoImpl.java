/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.server.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import com.ft.otp.base.dao.namespace.AuthMgrNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.DbConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.database.pool.DbEnv;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.authmgr.server.dao.IServerDao;
import com.ft.otp.manager.authmgr.server.entity.ServerInfo;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 认证服务器业务方法实现类 - DAO
 *
 * @Date in Apr 11, 2011,2:02:21 PM
 *
 * @author ZJY
 */
public class ServerDaoImpl extends BaseSqlMapDAO implements IServerDao {

    protected String getNameSpace() {
        return AuthMgrNSpace.SERVER_INFO_NS;
    }

    private ServerInfo getServerInfo(Object object) {
        ServerInfo serverInfo = (ServerInfo) object;
        if (serverInfo == null) {
            serverInfo = new ServerInfo();
        }
        return serverInfo;
    }

    public void addObj(Object object) throws BaseException {
        ServerInfo serverInfo = getServerInfo(object);
        insert(AuthMgrNSpace.SERVER_INFO_INSERT_SR, serverInfo);
    }

    public int count(Object object) throws BaseException {
        ServerInfo serverInfo = getServerInfo(object);
        return (Integer) queryForObject(AuthMgrNSpace.SERVER_INFO_COUNT_SR, serverInfo);
    }

    public void delObj(Object object) throws BaseException {
        ServerInfo serverInfo = getServerInfo(object);
        delete(AuthMgrNSpace.SERVER_INFO_DELETE_SR, serverInfo);
    }

    public void delObj(Set<?> keys) throws BaseException {

    }

    public Object find(Object object) throws BaseException {
        ServerInfo serverInfo = getServerInfo(object);
        return queryForObject(AuthMgrNSpace.SERVER_INFO_FIND_SR, serverInfo);
    }

    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        ServerInfo serverInfo = getServerInfo(object);
        serverInfo.setPageSize(pageArg.getPageSize());
        serverInfo.setStartRow(pageArg.getStartRow());
        if (DbEnv.getDbtype().equals(DbConstant.DB_TYPE_SYBASE)) {
            return queryForList(AuthMgrNSpace.SERVER_INFO_SELECT_SR, serverInfo, pageArg.getStartRow(), pageArg
                    .getPageSize());
        } else {
            return queryForList(AuthMgrNSpace.SERVER_INFO_SELECT_SR, serverInfo);
        }
    }

    public void updateObj(Object object) throws BaseException {
        ServerInfo serverInfo = getServerInfo(object);
        update(AuthMgrNSpace.SERVER_INFO_UPDATE_SR, serverInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.authmgr.server.dao.IServerDao#updateList(java.util.List)
     */
    public void updateList(final List<?> list) throws BaseException {
        //批量更新服务器表中的licid和chnum
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                ServerInfo serverInfo = null;
                int batch = 0;
                executor.startBatch();
                for (int i = 0; i < list.size(); i++) {
                    serverInfo = (ServerInfo) list.get(i);
                    update(AuthMgrNSpace.SERVER_INFO_UPDATE_SR, serverInfo);
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

    public void updateHostIp(Object object) throws Exception {
        ServerInfo serverInfo = getServerInfo(object);
        update(AuthMgrNSpace.SERVER_INFO_UPDATE_IP, serverInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.authmgr.server.dao.IServerDao#updateNewLicId(java.lang.Object)
     */
    public void updateNewLicId(Object object) throws Exception {
        ServerInfo serverInfo = getServerInfo(object);
        update(AuthMgrNSpace.SERVER_INFO_UPDATE_LICID, serverInfo);
    }

}
