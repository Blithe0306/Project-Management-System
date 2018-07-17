/**
 *Administrator
 */
package com.ft.otp.manager.admin.role.service;

import java.util.List;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;

/**
 * 管理员角色服务接口
 *
 * @Date in Jun 29, 2011,3:08:15 PM
 *
 * @author TBM
 */
public interface IRoleInfoServ extends IBaseService {
    List<?> getAdmrolesList(Object object) throws BaseException;

    /**
     * 变更角色创建人
     * 方法说明
     * @Date in Jun 12, 2012,9:56:06 AM
     * @param object
     * @throws BaseException
     */
    void updateDsignate(Object object) throws BaseException;
    
    /**
     * 根据管理员ID查出角色总数
     * @param object
     * @return
     * @throws BaseException
     */
    int countDes(Object object) throws BaseException;
    
    /**
     * 根据管理员ID查出角色ID集合
     * @param object
     * @return
     * @throws BaseException
     */
    List<?> queryDes(Object object) throws BaseException;
}
