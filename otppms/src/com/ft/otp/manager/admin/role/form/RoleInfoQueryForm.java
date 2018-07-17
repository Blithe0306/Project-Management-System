/**
 *Administrator
 */
package com.ft.otp.manager.admin.role.form;

import java.util.Date;

import com.ft.otp.manager.admin.role.entity.RoleInfo;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理员角色查询Form
 *
 * @Date in Jun 29, 2011,3:02:10 PM
 *
 * @author TBM
 */
public class RoleInfoQueryForm {

    private RoleInfo adminRole = new RoleInfo();
    private String roleName = null;
    private String startTime; //开始时间
    private String endTime; //结束时间
    private String createUser;//创建人

    /**
     * @return the createUser
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser the createUser to set
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
        adminRole.setCreateuser(createUser.trim());
    }

    /**
     * @return the adminRole
     */
    public RoleInfo getAdminRole() {
        return adminRole;
    }

    /**
     * @param adminRole the adminRole to set
     */
    public void setAdminRole(RoleInfo adminRole) {
        this.adminRole = adminRole;
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
        adminRole.setRoleName(roleName.trim());
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;

        Date ds = null;
        if (StrTool.strNotNull(startTime)) {
            int mark = 0;
            if (startTime.length() == 10) {
                mark = 1;
            }
            ds = DateTool.strToDate(startTime, mark);
        }

        int time = DateTool.dateToInt(ds);
        this.adminRole.setStartTime(time);
    }

    /**
     * @return the endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;

        Date es = null;
        if (StrTool.strNotNull(endTime)) {
            int mark = 0;
            if (endTime.length() == 10) {
                mark = 2;
            }
            es = DateTool.strToDate(endTime, mark);
        }

        int time = DateTool.dateToInt(es);
        this.adminRole.setEndTime(time);
    }

    /**
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }

}
