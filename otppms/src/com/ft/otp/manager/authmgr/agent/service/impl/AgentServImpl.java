/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.agent.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.authmgr.agent.dao.IAgentDao;
import com.ft.otp.manager.authmgr.agent.entity.AgentInfo;
import com.ft.otp.manager.authmgr.agent.service.IAgentServ;

/**
 * 认证代理业务逻辑接口实现类-service
 *
 * @Date in Apr 11, 2011,2:03:42 PM
 *
 * @author ZJY
 */
public class AgentServImpl implements IAgentServ {

    private IAgentDao agentDao;

    /**
     * @return the agentDao
     */
    public IAgentDao getAgentDao() {
        return agentDao;
    }

    /**
     * @param agentDao the agentDao to set
     */
    public void setAgentDao(IAgentDao agentDao) {
        this.agentDao = agentDao;
    }

    public void addObj(Object object) throws BaseException {
        agentDao.addObj(object);
    }

    public int count(Object object) throws BaseException {
        return agentDao.count(object);
    }

    public void delObj(Object object) throws BaseException {

    }

    public void delObj(Set<?> keys) throws BaseException {
        AgentInfo agentInfo = new AgentInfo();
        String[] tempStr = new String[keys.size()];
        int i = 0;
        for (Iterator<?> it = keys.iterator(); it.hasNext();) {
            String id = (String) it.next();
            tempStr[i] = id;
            i++;
        }
        agentInfo.setBatchIds(tempStr);
        agentDao.delObj(agentInfo);
    }

    public Object find(Object object) throws BaseException {
        return agentDao.find(object);
    }

    public List<?> query(Object object, PageArgument pageArg)
            throws BaseException {
        return agentDao.query(object, pageArg);
    }

    public void updateObj(Object object) throws Exception {
        agentDao.updateObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.authmgr.agent.service.IAgentServ#updateEnabled(java.lang.Object)
     */
    public void updateEnabled(Object object) throws BaseException {
        agentDao.updateEnabled(object);
    }

	public List<?> findAgent(Object object) throws BaseException {
		// TODO Auto-generated method stub
		return agentDao.findAgent(object);
	}

}
