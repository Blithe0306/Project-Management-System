/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.agentconf.dao;

import java.util.List;

import com.ft.otp.base.dao.IBaseDao;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;

/**
 * 认证代理配置表DAO接口
 *
 * @Date in Jan 28, 2013,4:16:28 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public interface IAgentConfDao extends IBaseDao {

    /**
     * 根据登录保护代理配置类型查询配置信息
     * @Date in Jan 30, 2013,10:06:24 AM
     * @param object
     * @param pageArg
     * @return
     * @throws BaseException
     */
    public List<?> queryConfList(Object object, PageArgument pageArg) throws BaseException;
}
