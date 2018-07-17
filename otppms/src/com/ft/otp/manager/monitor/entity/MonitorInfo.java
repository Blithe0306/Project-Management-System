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
public class MonitorInfo extends BaseEntity {

    private long id;// 主键
    private String email;// 管理员的邮件地址，使用;分割
    private String mobile; // 管理员的手机号，使用;分割
    private int confid; // 配置编号
    private String conftype;// 预警类型即预警策略配置类型
    private String title; // 标题（发送信息摘要）
    private String content; // 发送内容
    private long sendtime = 0; // 发送时间
    private int issend = 0; // 是否需要发送邮件或短信，0不发送，1发送

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the issend
     */
    public int getIssend() {
        return issend;
    }

    /**
     * @param issend the issend to set
     */
    public void setIssend(int issend) {
        this.issend = issend;
    }

    /**
     * @return the confid
     */
    public int getConfid() {
        return confid;
    }

    /**
     * @param confid the confid to set
     */
    public void setConfid(int confid) {
        this.confid = confid;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the sendtime
     */
    public long getSendtime() {
        return sendtime;
    }

    /**
     * @param sendtime the sendtime to set
     */
    public void setSendtime(long sendtime) {
        this.sendtime = sendtime;
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
