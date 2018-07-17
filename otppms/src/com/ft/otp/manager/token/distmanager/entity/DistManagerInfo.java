/**
 *Administrator
 */
package com.ft.otp.manager.token.distmanager.entity;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.util.tool.DateTool;

/**
 * 令牌分发对象实体类
 *
 * @Date in Apr 18, 2011,11:24:50 AM
 *
 * @author ZJY
 */
public class DistManagerInfo extends BaseEntity {

    private String token; //令牌号
    private String pubkeyfactor; //手机令牌种子因子
    private String phoneudid; //令牌标识码 
    private String activepass; //激活密码
    private int apdeath = 0; //激活密码的过期时间(秒数)
    private int apretry = -1; //激活密码的重试次数
    private int actived = 0; //是否激活过、是否成功下载过种子 0 没有，1已经激活
    private int activetime = 0; //激活时间
    private int provtype = -2; //令牌种子分发模式 -2 查询所有、-1未分发、1 在线分发、0离线分发 
    private String exttype; //令牌基本信息组合类型(00--49)
    private int emFlag = 0; // 决定令牌分发页面，是否显示邮件发送标志；0：不显示；1：显示；
    private String userName; //用户
    private int distParam; //分发站点访问参数

    public int getEmFlag() {
        return emFlag;
    }

    public void setEmFlag(int emFlag) {
        this.emFlag = emFlag;
    }

    /**
     * @return the urlp1
     */

    private String activeCode; //如果是离线分发,则需要产生激活码
    private String distUrl; //分发的url，从配置获取
    private int mark = 0; //记录日志时区分操作是设定标识码还是重置0 重置 1 设定标识码

    private int startTime;
    private int endTime;

    private Integer isFliterTag = null; // null不过滤用户列表 超级管理员使用 1过滤根据管理员id过滤用户 
    private String currentAdminId; //当前管理员id 根据此id过滤 当前管理员所管理的机构或域下的用户

    public Integer getIsFliterTag() {
        return isFliterTag;
    }

    public void setIsFliterTag(Integer isFliterTag) {
        this.isFliterTag = isFliterTag;
    }

    public String getCurrentAdminId() {
        return currentAdminId;
    }

    public void setCurrentAdminId(String currentAdminId) {
        this.currentAdminId = currentAdminId;
    }

    /**
     * @return the activeCode
     */
    public String getActiveCode() {
        return activeCode;
    }

    /**
     * @param activeCode the activeCode to set
     */
    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    /**
     * @return the distParam
     */
    public int getDistParam() {
        return distParam;
    }

    /**
     * @param distParam the distParam to set
     */
    public void setDistParam(int distParam) {
        this.distParam = distParam;
    }

    public DistManagerInfo() {
        this.actived = -1;
        this.provtype = -2; //所有
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPubkeyfactor() {
        return pubkeyfactor;
    }

    public void setPubkeyfactor(String pubkeyfactor) {
        this.pubkeyfactor = pubkeyfactor;
    }

    public String getPhoneudid() {
        return phoneudid;
    }

    public void setPhoneudid(String phoneudid) {
        if (null != phoneudid && !"".equals(phoneudid)) {
            if (phoneudid.indexOf(" ") != -1) {
                phoneudid = phoneudid.replace(" ", "");
            }
        }
        this.phoneudid = phoneudid;
    }

    public String getActivepass() {
        return activepass;
    }

    public void setActivepass(String activepass) {
        this.activepass = activepass;
    }

    public String getActivetimeStr() {
        long timeZone = new Long(getActivetime());
        if (timeZone == 0) {
            return "";
        } else {
            return DateTool.dateConvertStr(timeZone);
        }
    }

    public String getApdeathStr() {
        long di = new Long(getApdeath() + "000");
        if (di == 0) {
            return "";
        } else {
            return DateTool.dateToStr(di, false);
        }
    }

    public int getApdeath() {
        return apdeath;
    }

    public void setApdeath(int apdeath) {
        this.apdeath = apdeath;
    }

    public int getApretry() {
        return apretry;
    }

    public void setApretry(int apretry) {
        this.apretry = apretry;
    }

    public int getActived() {
        return actived;
    }

    public void setActived(int actived) {
        this.actived = actived;
    }

    public int getProvtype() {
        return provtype;
    }

    public void setProvtype(int provtype) {
        this.provtype = provtype;
    }

    public String getExttype() {
        return exttype;
    }

    public void setExttype(String exttype) {
        this.exttype = exttype;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getActivetime() {
        return activetime;
    }

    public void setActivetime(int activetime) {
        this.activetime = activetime;
    }

    /**
     * @return the distUrl
     */
    public String getDistUrl() {
        return distUrl;
    }

    /**
     * @param distUrl the distUrl to set
     */
    public void setDistUrl(String distUrl) {
        this.distUrl = distUrl;
    }

    /**
     * @return the mark
     */
    public int getMark() {
        return mark;
    }

    /**
     * @param mark the mark to set
     */
    public void setMark(int mark) {
        this.mark = mark;
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
