/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.agentserver.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ft.otp.base.dao.namespace.AuthMgrNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.authmgr.agentserver.dao.IAgentServerDao;
import com.ft.otp.manager.authmgr.agentserver.entity.AgentServerInfo;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 代理和服务器关系业务方法实现类 - DAO
 *
 * @Date in Apr 13, 2011,4:05:04 PM
 *
 * @author ZJY
 */
public class AgentServerDaoImpl extends BaseSqlMapDAO implements
        IAgentServerDao {

    protected String getNameSpace() {
        return AuthMgrNSpace.AGENT_SERVER_INFO_NS;
    }

    private AgentServerInfo getAgentServerInfo(Object object) {
        AgentServerInfo agentServerInfo = (AgentServerInfo) object;
        if (null == agentServerInfo) {
            agentServerInfo = new AgentServerInfo();
        }
        return agentServerInfo;
    }

    public void addObj(Object object) throws BaseException {
        AgentServerInfo agentServerInfo = getAgentServerInfo(object);
        insert(AuthMgrNSpace.AGENT_SERVER_INFO_INSERT_AS, agentServerInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#count(com.ft.otp.base.form.BaseQueryForm)
     */
    public int count(Object object) throws BaseException {
        return 0;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
        AgentServerInfo agentServerInfo = getAgentServerInfo(object);
        delete(AuthMgrNSpace.AGENT_SERVER_INFO_DELETE_AS, agentServerInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#delObj(java.util.Set)
     */
    public void delObj(Set<?> keys) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#find(java.lang.Object)
     */
    public Object find(Object object) throws BaseException {
        return null;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#query(com.ft.otp.base.form.BaseQueryForm, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg)
            throws BaseException {
        AgentServerInfo agentServerInfo = getAgentServerInfo(object);
        return queryForList(AuthMgrNSpace.AGENT_SERVER_INFO_SELECT_AS,
                agentServerInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.authmgr.agentserver.dao.IAgentServerDao#addAgentHost(java.util.List)
     */
    public void addAgentHost(final List<Object> ahList) throws BaseException {
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor)
                    throws SQLException {
                int batch = 0;
                AgentServerInfo aServerInfo = null;

                executor.startBatch();
                for (int i = 0; i < ahList.size(); i++) {
                    aServerInfo = (AgentServerInfo) ahList.get(i);
                    insert(AuthMgrNSpace.AGENT_SERVER_INFO_INSERT_AS,
                            aServerInfo);

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
     * @see com.ft.otp.manager.authmgr.agentserver.dao.IAgentServerDao#queryServORAgent(java.lang.Object)
     */
    public List<?> queryServORAgent(Object object) throws BaseException {
        AgentServerInfo agentServerInfo = getAgentServerInfo(object);
        return queryForList(AuthMgrNSpace.SELECTAS_JOIN_SERV, agentServerInfo);
    }

}
