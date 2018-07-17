/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.server.service;

import java.util.List;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;

/**
 * 认证服务器业务逻辑接口提供类
 *
 * @Date in Apr 11, 2011,2:02:45 PM
 *
 * @author ZJY
 */
public interface IServerServ extends IBaseService {

    /**
     * 更新授权时，批量更新服务器中的licId值
     * @Date in Jun 17, 2011,9:42:04 AM
     * @param list
     * @throws BaseException
     */
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
