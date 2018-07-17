/**
 *Administrator
 */
package com.ft.otp.manager.monitor.entity;

import com.ft.otp.base.entity.BaseEntity;

/**
 * 监控预警发送信息和发送人对应关系实体对象
 *
 * @Date in Mar 5, 2013,11:05:09 AM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class MonitorAndAdminInfo extends BaseEntity {

    private String adminid; // 发送人即管理员的ID
    private String conftype;// 预警类型即预警策略配置类型

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
     * @return the conftype
     */
    public String getConftype() {
        return conftype;
    }

    /**
     * @param conftype the conftype to set
     */
    public void setConftype(String conftype) {
        this.conftype = conftype;
    }

}
