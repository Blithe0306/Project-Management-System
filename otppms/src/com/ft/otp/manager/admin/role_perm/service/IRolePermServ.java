/**
 *Administrator
 */
package com.ft.otp.manager.admin.role_perm.service;

import java.util.List;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;

/**
 * 角色-权限业务接口类功能说明
 *
 * @Date in Jul 1, 2011,3:39:57 PM
 *
 * @author ZJY
 */
public interface IRolePermServ extends IBaseService {
    /***
     * 添加角色对应的权限
     */
    void addRolePerm(List<?> list) throws BaseException;

    /***
     * 更新角色对应的权限
     */
    void updateRolePerm(List<?> list) throws BaseException;

    /**
     * 根据管理员获取角色以及角色对应的所有权限
     */
    List<?> queryAdmPerms(Object object) throws BaseException;
}
