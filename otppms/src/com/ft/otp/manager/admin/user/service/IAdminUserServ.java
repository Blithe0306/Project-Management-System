/**
 *Administrator
 */
package com.ft.otp.manager.admin.user.service;

import java.util.List;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;
import com.ft.otp.common.page.PageArgument;

/**
 * 管理员用户业务逻辑接口提供类
 *
 * @Date in Apr 17, 2011,1:15:16 PM
 *
 * @author TBM
 */
public interface IAdminUserServ extends IBaseService {

    /**
     * 根据管理员查找
     */
    List<?> getChildAdmins(Object object, PageArgument pageArg, String method) throws BaseException;

    /**
     * 启用、禁用莫个管理员
     */
    void updateEnabled(String adminid, int enabled) throws BaseException;

    /**
     * 错误登录超过三次将锁定管理员账号
     */
    void updateLocked(Object object) throws BaseException;

    /**
     * 修改管理员密码
     */
    void updatePassword(Object object) throws BaseException;

    /**
     * 查询角色创建人的指派人
     */
    List<?> queryAdminDesignate(Object object) throws BaseException;

    /**
     * 查找变更人上上级用户
     */
    Object findSuperDesignate(Object object) throws BaseException;

    /**
     * 变更创建人操作
     */
    void updateDsignate(Object object) throws BaseException;

    /**
     * 查询管理员过滤掉超级管理员
     * @param object
     * @param pageArg
     * @throws BaseException
     * @return List
     * 
     */
    List<?> queryExceptPowerAdmin(Object object, PageArgument pageArg) throws BaseException;

    /**
     * 更新管理员登录时间和登录次数
     */
    void updateAdminUser(Object object) throws BaseException;
    
    /**
     * 更新对象
     * @param object
     * @throws BaseException
     */
    void updatePass(Object object) throws Exception;
    
    /**
     * 更新静态密码找回有效期
     * @param object
     * @throws BaseException
     */
    void updatePwddeath(Object object) throws Exception;
    
    /**
     * 根据用户查询用户邮箱
     */
    List<?> selectAmdEmail(Object object) throws BaseException;
}
