/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.agent.dao;

import java.util.List;

import com.ft.otp.base.dao.IBaseDao;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;

/**
 *认证代理业务数据接口提供类
 *
 * @Date in Apr 11, 2011,2:01:45 PM
 *
 * @author ZJY
 */
public interface IAgentDao extends IBaseDao {

    /**
     * 启用，禁用认证管理状态
     * @Date in Jan 17, 2013,4:15:03 PM
     * @param object
     * @throws BaseException
     */
    public void updateEnabled(Object object) throws BaseException;
    
    /**
     * 根据认证代理配置ID查询
     * @param object
     * @return
     * @throws BaseException
     */
    public List<?> findAgent(Object object) throws BaseException;
}
