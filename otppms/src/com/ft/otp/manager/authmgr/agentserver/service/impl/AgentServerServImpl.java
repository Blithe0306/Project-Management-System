/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.agentserver.service.impl;

import java.util.List;
import java.util.Set;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.authmgr.agentserver.dao.IAgentServerDao;
import com.ft.otp.manager.authmgr.agentserver.service.IAgentServerServ;

/**
 *  代理和服务器关系业务逻辑接口提供类-service
 *
 * @Date in Apr 13, 2011,4:08:01 PM
 *
 * @author ZJY
 */
public class AgentServerServImpl implements IAgentServerServ {

    private IAgentServerDao agentServerDao;

    /**
     * @return the agentServerDao
     */
    public IAgentServerDao getAgentServerDao() {
        return agentServerDao;
    }

    /**
     * @param agentServerDao the agentServerDao to set
     */
    public void setAgentServerDao(IAgentServerDao agentServerDao) {
        this.agentServerDao = agentServerDao;
    }

    public void addObj(Object object) throws BaseException {
        agentServerDao.addObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#count(com.ft.otp.base.form.BaseQueryForm)
     */
    public int count(Object object) throws BaseException {
        return 0;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
        agentServerDao.delObj(object);

    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#delObj(java.util.Set)
     */
    public void delObj(Set<?> keys) throws BaseException {

    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#find(java.lang.Object)
     */
    public Object find(Object object) throws BaseException {
        return null;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#query(com.ft.otp.base.form.BaseQueryForm, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg)
            throws BaseException {
        return agentServerDao.query(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.authmgr.agentserver.service.IAgentServerServ#addAgentHost(java.util.List)
     */
    public void addAgentHost(List<Object> ahList) throws BaseException {
        agentServerDao.addAgentHost(ahList);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.authmgr.agentserver.service.IAgentServerServ#queryServORAgent(java.lang.Object)
     */
    public List<?> queryServORAgent(Object object) throws BaseException {
        return agentServerDao.queryServORAgent(object);
    }

}
