/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.agentconf.service.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.BaseService;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.authmgr.agentconf.dao.IAgentConfDao;
import com.ft.otp.manager.authmgr.agentconf.service.IAgentConfServ;

/**
 * 认证代理配置表业务接口实现
 *
 * @Date in Jan 28, 2013,4:15:39 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class AgentConfServImpl extends BaseService implements IAgentConfServ {

    private IAgentConfDao agentConfDao;
    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        agentConfDao.addObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#count(java.lang.Object)
     */
    public int count(Object object) throws BaseException {
        return agentConfDao.count(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#delObj(java.util.Set)
     */
    public void delObj(Set<?> keys) throws BaseException {
        agentConfDao.delObj(keys);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#find(java.lang.Object)
     */
    public Object find(Object object) throws BaseException {
        return agentConfDao.find(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return agentConfDao.query(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws Exception {
        agentConfDao.updateObj(object);
    }

    public IAgentConfDao getAgentConfDao() {
        return agentConfDao;
    }

    public void setAgentConfDao(IAgentConfDao agentConfDao) {
        this.agentConfDao = agentConfDao;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.authmgr.agentconf.service.IAgentConfServ#queryConfList(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> queryConfList(Object object, PageArgument pageArg) throws BaseException {
        return agentConfDao.queryConfList(object, pageArg);
    }

}
