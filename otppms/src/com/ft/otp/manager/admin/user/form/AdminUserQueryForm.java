/**
 *Administrator
 */
package com.ft.otp.manager.admin.user.form;

import com.ft.otp.base.form.BaseQueryForm;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.util.tool.StrTool;

/**
 *管理员查询Form 类功能说明
 *
 * @Date in May 5, 2011,10:57:56 AM
 *
 * @author TBM
 */
public class AdminUserQueryForm extends BaseQueryForm {

    private AdminUser adminUser = new AdminUser();
    private String adminid = null;
    private String realname = null;
    private String roleName = null;// 管理员角色
    private String orgunitIds;//域和组织机构的拼接字符串 比如格式1:1
    private String createuser = null;
    private int locked = -1;

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
        adminUser.setLocked(locked);
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
        adminUser.setCreateuser(createuser.trim());
    }

    /**
     * @return the adminUser
     */
    public AdminUser getAdminUser() {
        return adminUser;
    }

    /**
     * @param adminUser the adminUser to set
     */
    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    /**
     * @return the adminid
     */
    public String getAdminid() {
        return adminid;
    }

    /**
     * @return the realname
     */
    public String getRealname() {
        return realname;
    }

    /**
     * @param realname the realname to set
     */
    public void setRealname(String realname) {
        adminUser.setRealname(realname.trim());
        this.realname = realname;
    }

    /**
     * @param adminid the adminid to set
     */
    public void setAdminid(String adminid) {
        this.adminid = adminid;
        adminUser.setAdminid(adminid.trim());

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
        this.adminUser.setRolenameStr(roleName.trim());
    }

    /**
     * @return the orgunitIds
     */
    public String getOrgunitIds() {
        return orgunitIds;
    }

    /**
     * @param orgunitIds the orgunitIds to set
     */
    public void setOrgunitIds(String orgunitIds) {
        this.orgunitIds = orgunitIds;
        if (StrTool.strNotNull(orgunitIds)) {
            if (orgunitIds.indexOf(":") != -1) {
                String[] arrIds = orgunitIds.split(",");
                int queryDomainId = StrTool.parseInt(arrIds[0].split(":")[0]);
                int queryOrgunitId = StrTool.parseInt(arrIds[0].split(":")[1]);
                this.adminUser.setDomainid(queryDomainId);
                this.adminUser.setOrgunitid(queryOrgunitId);
            }
        }
    }

}
