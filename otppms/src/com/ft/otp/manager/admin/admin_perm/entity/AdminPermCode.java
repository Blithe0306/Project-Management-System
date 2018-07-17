/**
 *Administrator
 */
package com.ft.otp.manager.admin.admin_perm.entity;

import com.ft.otp.base.entity.BaseEntity;

/**
 * 管理员常用功能 实体类
 *
 * @Date in May 14, 2013,2:24:54 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class AdminPermCode extends BaseEntity {
    private String adminid = null; //管理员ID
    private String permcode = null;//权限码

    /**
     * @return the adminid
     */
    public String getAdminid() {
        return adminid;
    }

    /**
     * @param adminid the adminid to set
     */
    public void setAdminid(String adminid) {
        this.adminid = adminid;
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

}
