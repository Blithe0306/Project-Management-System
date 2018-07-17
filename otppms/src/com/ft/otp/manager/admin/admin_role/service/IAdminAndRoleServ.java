/**
 *Administrator
 */
package com.ft.otp.manager.admin.admin_role.service;

import java.util.List;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;

/**
 * 管理员-角色关系业务接口类功能说明
 *
 * @Date in Jul 1, 2011,3:39:57 PM
 *
 * @author ZJY
 */
public interface IAdminAndRoleServ extends IBaseService {

    /**
     * 添加角色对应的权限
     */
    void addAdminAndRole(List<?> list) throws BaseException;

    /***
     * 更新角色对应的权限
     */
    void updateAdminAndRole(List<?> list) throws BaseException;

    /**
     * 所有的角色对应的所有的管理员
     */
    List<?> queryAdmins(Object object) throws BaseException;
}
