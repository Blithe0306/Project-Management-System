/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.config.entity;

import com.ft.otp.util.tool.StrTool;

/**
 * ConfigInfo 配置表实体
 *
 * @Date in Nov 15, 2012,4:50:56 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class ConfigInfo {

    private int id; //自增ID
    private String confname; //配置名称
    private String conftype; //配置类型，如：server、center
    private String confvalue; //配置值
    private int parentid; //继承的父id，保留字段，暂不使用
    private String descp; //描述

    public ConfigInfo() {
    }

    /**
     * @param confname
     * @param conftype
     * @param confvalue
     * @param parentid
     * @param descp
     */
    public ConfigInfo(String confname, String confvalue, String conftype, int parentid, String descp) {
        this.confname = confname;
        if (StrTool.strNotNull(confvalue)) {
            this.confvalue = "'" + confvalue + "'";
        } else {
            this.confvalue = "' '";
        }
        this.conftype = conftype;
        this.parentid = parentid;
        this.descp = StrTool.cleanXSS(descp);
    }

    public ConfigInfo(int id) {
        this.id = id;
    }

    public String getConfname() {
        return confname;
    }

    public void setConfname(String confname) {
        this.confname = confname;
    }

    public String getConftype() {
        return conftype;
    }

    public void setConftype(String conftype) {
        this.conftype = conftype;
    }

    public String getConfvalue() {
    	confvalue = confvalue.trim();
        return confvalue;
    }

    public void setConfvalue(String confvalue) {
        this.confvalue = confvalue;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = StrTool.cleanXSS(descp);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
