/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.server.dao;

import java.util.List;

import com.ft.otp.base.dao.IBaseDao;
import com.ft.otp.base.exception.BaseException;

/**
 * 认证服务器业务数据接口提供类
 *
 * @Date in Apr 11, 2011,2:01:45 PM
 *
 * @author ZJY
 */
public interface IServerDao extends IBaseDao {

    void updateList(List<?> list) throws BaseException;

    /**
     * 修改服务器IP
     * 
     * @Date in May 16, 2013,9:51:40 AM
     * @param object
     * @throws Exception
     */
    void updateHostIp(Object object) throws Exception;
    
    /**
     * 将最新授权文件LicID更新到认证服务器
     * @Date in May 27, 2013,5:02:23 PM
     * @param object
     * @throws Exception
     */
    void updateNewLicId(Object object) throws Exception;
    
}
