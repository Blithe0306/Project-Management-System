/**
 *Administrator
 */
package com.ft.otp.manager.admin.role_perm.entity;

import java.util.List;

import com.ft.otp.base.entity.BaseEntity;

/**
 * 角色-权限关系实体类说明
 *
 * @Date in Jul 1, 2011,3:29:22 PM
 *
 * @author ZJY
 */
public class RolePerm extends BaseEntity {

    private int roleId;
    private String permcode;
    private String[] perms;
    private List<?> selectedList;

    private String adminId;

    private String srcname;
    private String keymark;
    private String permlink;

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
     * @return the permcode
     */
    public String getPermcode() {
        return permcode;
    }

    /**
     * @param permcode the permcode to set
     */
    public void setPermcode(String permcode) {
        this.permcode = permcode;
    }

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
     * @return the selectedList
     */
    public List<?> getSelectedList() {
        return selectedList;
    }

    /**
     * @param selectedList the selectedList to set
     */
    public void setSelectedList(List<?> selectedList) {
        this.selectedList = selectedList;
    }

    /**
     * @return the perms
     */
    public String[] getPerms() {
        return perms;
    }

    /**
     * @param perms the perms to set
     */
    public void setPerms(String[] perms) {
        this.perms = perms;
    }

    /**
     * @return the srcname
     */
    public String getSrcname() {
        return srcname;
    }

    /**
     * @param srcname the srcname to set
     */
    public void setSrcname(String srcname) {
        this.srcname = srcname;
    }

    /**
     * @return the keymark
     */
    public String getKeymark() {
        return keymark;
    }

    /**
     * @param keymark the keymark to set
     */
    public void setKeymark(String keymark) {
        this.keymark = keymark;
    }

    /**
     * @return the permlink
     */
    public String getPermlink() {
        return permlink;
    }

    /**
     * @param permlink the permlink to set
     */
    public void setPermlink(String permlink) {
        this.permlink = permlink;
    }

}
