/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.agent.dao.impl;

import java.util.List;
import java.util.Set;
import com.ft.otp.base.dao.namespace.AuthMgrNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.DbConstant;
import com.ft.otp.common.database.pool.DbEnv;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.authmgr.agent.dao.IAgentDao;
import com.ft.otp.manager.authmgr.agent.entity.AgentInfo;

/**
 * 认证代理业务方法实现类 - DAO
 *
 * @Date in Apr 11, 2011,2:02:21 PM
 *
 * @author ZJY
 */
public class AgentDaoImpl extends BaseSqlMapDAO implements IAgentDao {

    protected String getNameSpace() {
        return AuthMgrNSpace.AGENT_INFO_NS;
    }

    private AgentInfo getAgentInfo(Object object) {
        AgentInfo agentInfo = (AgentInfo) object;
        if (agentInfo == null) {
            agentInfo = new AgentInfo();
        }
        return agentInfo;
    }

    public void addObj(Object object) throws BaseException {
        AgentInfo agentInfo = getAgentInfo(object);
        insert(AuthMgrNSpace.AGENT_INFO_INSERT_AT, agentInfo);
    }

    public int count(Object object) throws BaseException {
        AgentInfo agentInfo = getAgentInfo(object);
        return (Integer) queryForObject(AuthMgrNSpace.AGENT_INFO_COUNT_AT,
                agentInfo);
    }

    public void delObj(Object object) throws BaseException {
        AgentInfo agentInfo = getAgentInfo(object);
        delete(AuthMgrNSpace.AGENT_INFO_DELETE_AT, agentInfo);
    }

    public void delObj(Set<?> keys) throws BaseException {

    }

    public Object find(Object object) throws BaseException {
        AgentInfo agentInfo = getAgentInfo(object);
        return queryForObject(AuthMgrNSpace.AGENT_INFO_FIND_AT, agentInfo);
    }

    public List<?> query(Object object, PageArgument pageArg)
            throws BaseException {
        AgentInfo agentInfo = getAgentInfo(object);
        agentInfo.setPageSize(pageArg.getPageSize());
        agentInfo.setStartRow(pageArg.getStartRow());
        if (DbEnv.getDbtype().equals(DbConstant.DB_TYPE_SYBASE)) {
            return queryForList(AuthMgrNSpace.AGENT_INFO_SELECT_AT, agentInfo,
                    pageArg.getStartRow(), pageArg.getPageSize());
        } else {
            return queryForList(AuthMgrNSpace.AGENT_INFO_SELECT_AT, agentInfo);
        }

    }

    public void updateObj(Object object) throws BaseException {
        AgentInfo agentInfo = getAgentInfo(object);
        update(AuthMgrNSpace.AGENT_INFO_UPDATE_AT, agentInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.authmgr.agent.dao.IAgentDao#updateEnabled(java.lang.Object)
     */
    public void updateEnabled(Object object) throws BaseException {
        AgentInfo agentInfo = getAgentInfo(object);
        update(AuthMgrNSpace.AGENT_INFO_UPDATE_ENABLED, agentInfo);
    }
    
    public List<?> findAgent(Object object)
    	throws BaseException {
		AgentInfo agentInfo = getAgentInfo(object);
		return queryForList(AuthMgrNSpace.AGENT_INFO_FIND_AG, agentInfo);
    }
}
