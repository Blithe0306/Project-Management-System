/**
 *Administrator
 */
package com.ft.otp.manager.admin.role_perm.dao;

import java.util.List;

import com.ft.otp.base.dao.IBaseDao;
import com.ft.otp.base.exception.BaseException;

/**
 * 角色-权限关系DAO接口类功能说明
 *
 * @Date in Jul 1, 2011,3:36:30 PM
 *
 * @author ZJY
 */
public interface IRolePermDao extends IBaseDao {
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
