/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.agentconf.service;

import java.util.List;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;
import com.ft.otp.common.page.PageArgument;

/**
 * 认证代理配置表业务接口
 *
 * @Date in Jan 28, 2013,4:13:35 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public interface IAgentConfServ extends IBaseService {

    /**
     * 根据登录保护代理配置类型查询配置信息
     * @Date in Jan 30, 2013,10:02:34 AM
     * @param object
     * @param pageArg
     * @return
     * @throws BaseException
     */
    public List<?> queryConfList(Object object, PageArgument pageArg) throws BaseException;
}
