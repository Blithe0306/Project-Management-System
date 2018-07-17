/**
 *Administrator
 */
package com.ft.otp.manager.admin.admin_role.entity;

import com.ft.otp.base.entity.BaseEntity;

/**
 *管理员-角色关系实体类说明
 *
 * @Date in Jul 1, 2011,3:29:22 PM
 *
 * @author ZJY
 */
public class AdminAndRole extends BaseEntity {
    
    private String adminId;
    private int roleId;
    
    
    
    
    private String roleName;
    private String rolemark;

    private String adminIds[];

    /**
     * @return the adminId
     */
    public String getAdminId() {
        return adminId;
    }

    /**
     * @param adminId the adminId to set
     */
    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    /**
     * @return the roleId
     */
    public int getRoleId() {
        return roleId;
    }

    /**
     * @param roleId the roleId to set
     */
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    /**
     * @return the adminIds
     */
    public String[] getAdminIds() {
        return adminIds;
    }

    /**
     * @param adminIds the adminIds to set
     */
    public void setAdminIds(String[] adminIds) {
        this.adminIds = adminIds;
    }

    /**
     * @return the roleName
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * @param roleName the roleName to set
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * @return the rolemark
     */
    public String getRolemark() {
        return rolemark;
    }

    /**
     * @param rolemark the rolemark to set
     */
    public void setRolemark(String rolemark) {
        this.rolemark = rolemark;
    }

}
