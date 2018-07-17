/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.agent.service;

import java.util.List;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;
import com.ft.otp.common.page.PageArgument;

/**
 * 认证代理业务逻辑接口提供类
 *
 * @Date in Apr 11, 2011,2:02:45 PM
 *
 * @author ZJY
 */
public interface IAgentServ extends IBaseService {

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
