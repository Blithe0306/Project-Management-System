/**
 *Administrator
 */
package com.ft.otp.manager.admin.perm.entity;

import java.util.List;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理员权限实体对象类
 *
 * @Date in Jun 29, 2011,4:42:28 PM
 *
 * @author TBM
 */
public class AdminPerm extends BaseEntity {

    private String permCode;
    private String permLink;
    private String srcname;
    private String keymark;
    private String descp;

    private String id = "";
    private String text = "";
    private String icon = "";
    private String attributes = "";
    private List<?> children = null;
    private String statusName = "";
    private String checked = "";
 
    private int roleId;//角色ID
    
    /**
     * @return the permCode
     */
    public String getPermCode() {
        return permCode;
    }

    /**
     * @param permCode the permCode to set
     */
    public void setPermCode(String permCode) {
        this.permCode = permCode;
    }

    /**
     * @return the permLink
     */
    public String getPermLink() {
        return permLink;
    }

    /**
     * @param permLink the permLink to set
     */
    public void setPermLink(String permLink) {
        this.permLink = permLink;
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
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

 

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return the attributes
     */
    public String getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the children
     */
    public List<?> getChildren() {
        return children;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(List<?> children) {
        this.children = children;
    }

     

    /**
     * @return the statusName
     */
    public String getStatusName() {
        return statusName;
    }

    /**
     * @param statusName the statusName to set
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
 

    /**
     * @return the checked
     */
    public String getChecked() {
        return checked;
    }

    /**
     * @param checked the checked to set
     */
    public void setChecked(String checked) {
        this.checked = checked;
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

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

}
