/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.agentconf.dao.impl;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ft.otp.base.dao.namespace.AuthMgrNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.authmgr.agentconf.dao.IAgentConfDao;
import com.ft.otp.manager.authmgr.agentconf.entity.AgentConfInfo;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 认证代理配置表DAO接口实现
 *
 * @Date in Jan 28, 2013,4:17:58 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class AgentConfDaoImpl extends BaseSqlMapDAO implements IAgentConfDao {
    
    protected String getNameSpace() {
        return AuthMgrNSpace.AGENTCONF_INFO_NS;
    }
    
    private AgentConfInfo getAgentConfInfo(Object obj) {
        AgentConfInfo agentConfInfo = (AgentConfInfo)obj;
        if (agentConfInfo == null) {
            agentConfInfo = new AgentConfInfo();
        }
        return agentConfInfo;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        AgentConfInfo agentConf = getAgentConfInfo(object);
        insert(AuthMgrNSpace.AGENTCONF_INFO_INSERT_ATCONF, agentConf);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#count(java.lang.Object)
     */
    public int count(Object object) throws BaseException {
        AgentConfInfo agentConf = getAgentConfInfo(object);
        return (Integer) queryForObject(AuthMgrNSpace.AGENTCONF_INFO_COUNT_ATCONF, agentConf);
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
                AgentConfInfo agentConf = new AgentConfInfo();
                Iterator<?> iter = set.iterator();
                while (iter.hasNext()) {
                    String id = (String) iter.next();
                    agentConf.setId(Integer.parseInt(id));
                    delete(AuthMgrNSpace.AGENTCONF_INFO_DELETE_ATCONF, agentConf);
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
        AgentConfInfo agentConf = getAgentConfInfo(object);
        return queryForObject(AuthMgrNSpace.AGENTCONF_INFO_FIND_ATCONF, agentConf);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        AgentConfInfo agentConf = getAgentConfInfo(object);
        agentConf.setStartRow(pageArg.getStartRow());
        agentConf.setPageSize(pageArg.getPageSize());
        return queryForList(AuthMgrNSpace.AGENTCONF_INFO_SELECT_ATCONF, agentConf);

    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
        AgentConfInfo agentConf = getAgentConfInfo(object);
        update(AuthMgrNSpace.AGENTCONF_INFO_UPDATE_ATCONF, agentConf);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.authmgr.agentconf.dao.IAgentConfDao#queryConfList(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> queryConfList(Object object, PageArgument pageArg) throws BaseException {
        AgentConfInfo agentConf = getAgentConfInfo(object);
        agentConf.setStartRow(pageArg.getStartRow());
        agentConf.setPageSize(pageArg.getPageSize());
        return queryForList(AuthMgrNSpace.AGENTCONF_INFO_SELECT_ATCONFLIST, agentConf);

    }

}
