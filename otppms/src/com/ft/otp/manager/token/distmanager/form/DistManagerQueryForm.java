/**
 *Administrator
 */
package com.ft.otp.manager.token.distmanager.form;

import java.util.Date;

import com.ft.otp.base.form.BaseQueryForm;
import com.ft.otp.manager.token.distmanager.entity.DistManagerInfo;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 令牌分发查询form类功能说明
 *
 * @Date in Apr 18, 2011,11:24:01 AM
 *
 * @author ZJY
 */
public class DistManagerQueryForm extends BaseQueryForm {
    private DistManagerInfo distManagerInfo = new DistManagerInfo();

    private String userName; //用户名 
    private String tokenStr; //令牌号   
    private int provtype = -2; //分发方式 默认查询所有 -2   
    private int actived; //状态（已激活、未激活）
    private String phoneudid; //令牌标识码 
    private String startTime; //开始时间
    private String endTime; //结束时间

    /**
     * @return the phoneudid
     */
    public String getPhoneudid() {
        return phoneudid;
    }

    /**
     * @param phoneudid the phoneudid to set
     */
    public void setPhoneudid(String phoneudid) {
        this.phoneudid = phoneudid;
        this.distManagerInfo.setPhoneudid(phoneudid);
    }

    /**
     * @return the distManagerInfo
     */
    public DistManagerInfo getDistManagerInfo() {
        return distManagerInfo;
    }

    /**
     * @param distManagerInfo the distManagerInfo to set
     */
    public void setDistManagerInfo(DistManagerInfo distManagerInfo) {
        this.distManagerInfo = distManagerInfo;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName.trim();
        this.distManagerInfo.setUserName(this.userName);

    }

    /**
     * @return the tokenStr
     */
    public String getTokenStr() {
        return tokenStr;
    }

    /**
     * @param tokenStr the tokenStr to set
     */
    public void setTokenStr(String tokenStr) {
        this.tokenStr = tokenStr.trim();
        this.distManagerInfo.setToken(this.tokenStr);

    }

    /**
     * @return the provtype
     */
    public int getProvtype() {
        return provtype;
    }

    /**
     * @param provtype the provtype to set
     */
    public void setProvtype(int provtype) {
        this.provtype = provtype;
        this.distManagerInfo.setProvtype(this.provtype);
    }

    /**
     * @return the actived
     */
    public int getActived() {
        return actived;
    }

    /**
     * @param actived the actived to set
     */
    public void setActived(int actived) {
        this.actived = actived;
        this.distManagerInfo.setActived(this.actived);
    }

    /**
     * @return the startLogTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startLogTime the startLogTime to set
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
        this.distManagerInfo.setStartTime(time);
    }

    /**
     * @return the endLogTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endLogTime the endLogTime to set
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
        this.distManagerInfo.setEndTime(time);
    }

}
