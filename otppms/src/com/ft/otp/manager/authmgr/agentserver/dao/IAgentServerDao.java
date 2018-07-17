/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.agentserver.dao;

import java.util.List;
import com.ft.otp.base.dao.IBaseDao;
import com.ft.otp.base.exception.BaseException;

/**
 *  代理-服务器关系业务数据接口提供类
 *
 * @Date in Apr 13, 2011,4:06:36 PM
 *
 * @author ZJY
 */
public interface IAgentServerDao extends IBaseDao {

    /**
     * 批量添加认证代理与认证服务器对应关系
     * @Date in Jun 16, 2011,12:02:17 PM
     * @param ahList
     * @throws BaseException
     */
    void addAgentHost(List<Object> ahList) throws BaseException;

    /**
     * 根据代理IP关联查询对应的认证服务器信息
     * 或
     * 根据认证服务器IP关联查询对应的代理信息
     * @Date in Jun 17, 2011,11:01:12 AM
     * @param object
     * @return
     * @throws BaseException
     */
    List<?> queryServORAgent(Object object) throws BaseException;

}
