/**
 *Administrator
 */
package com.ft.otp.manager.admin.admin_role.dao;

import java.util.List;

import com.ft.otp.base.dao.IBaseDao;
import com.ft.otp.base.exception.BaseException;

/**
 * 管理员-角色关系DAO接口类功能说明
 *
 * @Date in Jul 1, 2011,3:36:30 PM
 *
 * @author ZJY
 */
public interface IAdminAndRoleDao extends IBaseDao {
    /***
     * 添加管理员对应的角色
     */
    void addAdminAndRole(List<?> list) throws BaseException;

    /***
     * 更新管理员对应的角色
     */
    void updateAdminAndRole(List<?> list) throws BaseException;

    /**
     * 所有的角色对应的所有的管理员
     */
    List<?> queryAdmins(Object object) throws BaseException;

}
