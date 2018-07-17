/**
 *Administrator
 */
package com.ft.otp.manager.admin.role.entity;

import java.util.List;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理员角色实体
 *
 * @Date in Jun 29, 2011,3:01:56 PM
 *
 * @author TBM
 */
public class RoleInfo extends BaseEntity {

    private int roleId;
    private String roleName;
    private String rolemark;
    private String createuser;
    private long createtime;
    private long modifytime;
    private String descp;

    private String querySource = ""; //查询角色的来源，角色列表查询、添加管理员时选择角色

    private List<?> adminPerms; //管理员权限列表

    private String createtimeStr;
    private String modifytimeStr;
    private String oldRoleName;

    private String roletext;//修改权限信息，用于日志显示

    private int startTime;
    private int endTime;

    public RoleInfo() {
        roleName = "";
        rolemark = "";
        createuser = "";
        descp = "";
        querySource = "";
        oldRoleName = "";
    }

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
     * @return the descp
     */
    public String getDescp() {
        return descp;
    }

    /**
     * @param descp the descp to set
     */
    public void setDescp(String descp) {
        this.descp = StrTool.cleanXSS(descp);
    }

    /**
     * @return the adminPerms
     */
    public List<?> getAdminPerms() {
        return adminPerms;
    }

    /**
     * @param adminPerms the adminPerms to set
     */
    public void setAdminPerms(List<?> adminPerms) {
        this.adminPerms = adminPerms;
    }

    /**
     * @return the createuser
     */
    public String getCreateuser() {
        return createuser;
    }

    /**
     * @param createuser the createuser to set
     */
    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    /**
     * @return the createtime
     */
    public long getCreatetime() {
        return createtime;
    }

    /**
     * @param createtime the createtime to set
     */
    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    /**
     * @return the modifytime
     */
    public long getModifytime() {
        return modifytime;
    }

    /**
     * @param modifytime the modifytime to set
     */
    public void setModifytime(long modifytime) {
        this.modifytime = modifytime;
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

    /**
     * @return the querySource
     */
    public String getQuerySource() {
        return querySource;
    }

    /**
     * @param querySource the querySource to set
     */
    public void setQuerySource(String querySource) {
        this.querySource = querySource;
    }

    /**
     * @return the createtimeStr
     */
    public String getCreatetimeStr() {
        if (getCreatetime() != 0) {
            return DateTool.dateToStr(getCreatetime(), true);
        }
        return createtimeStr;
    }

    /**
     * @return the modifytimeStr
     */
    public String getModifytimeStr() {
        if (getModifytime() != 0) {
            return DateTool.dateToStr(getModifytime(), true);
        }
        return modifytimeStr;
    }

    /**
     * @return the oldRoleName
     */
    public String getOldRoleName() {
        return oldRoleName;
    }

    /**
     * @param oldRoleName the oldRoleName to set
     */
    public void setOldRoleName(String oldRoleName) {
        this.oldRoleName = oldRoleName;
    }

    public String getRoletext() {
        return roletext;
    }

    public void setRoletext(String roletext) {
        this.roletext = roletext;
    }

    /**
     * @return the startTime
     */
    public int getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public int getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

}
