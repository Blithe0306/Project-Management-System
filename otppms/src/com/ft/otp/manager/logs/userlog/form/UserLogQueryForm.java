/**
 *Administrator
 */
package com.ft.otp.manager.logs.userlog.form;

import java.util.Date;
import com.ft.otp.base.form.BaseQueryForm;
import com.ft.otp.manager.logs.userlog.entity.UserLogInfo;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户日志查询Form类功能说明
 *
 * @Date in May 4, 2011,4:51:59 PM
 *
 * @author ZJY
 */
public class UserLogQueryForm extends BaseQueryForm {

    private UserLogInfo userLogInfo = new UserLogInfo();

    private int actionResult; //操作结果
    private String clientIp; //操作客户端IP地址
    private String userid; //用户名
    private String token; //令牌号   
    private String startDate; //时间
    private String endDate;

    /**
     * @return the userLogInfo
     */
    public UserLogInfo getUserLogInfo() {
        return userLogInfo;
    }

    /**
     * @param userLogInfo the userLogInfo to set
     */
    public void setUserLogInfo(UserLogInfo userLogInfo) {
        this.userLogInfo = userLogInfo;
    }

    /**
     * @return the userid
     */
    public String getUserid() {
        return userid;
    }

    /**
     * @param userid the userid to set
     */
    public void setUserid(String userid) {
        this.userLogInfo.setUserid(userid);
        this.userid = userid;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.userLogInfo.setToken(token);
        this.token = token;
    }

    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        Date ds = null;
        if (StrTool.strNotNull(startDate)) {
            int mark = 0;
            if (startDate.length() == 10) {
                mark = 1;
            }
            ds = DateTool.strToDate(startDate, mark);
        }
        int time = DateTool.dateToInt(ds);
        this.userLogInfo.setStartDate(time);
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(String endDate) {
        Date ds = null;
        if (StrTool.strNotNull(endDate)) {
            int mark = 0;
            if (endDate.length() == 10) {
                mark = 1;
            }
            ds = DateTool.strToDate(endDate, mark);
        }
        int time = DateTool.dateToInt(ds);

        this.userLogInfo.setEndDate(time);
        this.endDate = endDate;
    }

    /**
     * @return the actionResult
     */
    public int getActionResult() {
        return actionResult;
    }

    /**
     * @param actionResult the actionResult to set
     */
    public void setActionResult(int actionResult) {
        this.actionResult = actionResult;
        this.userLogInfo.setActionresult(actionResult);
    }

    /**
     * @return the clientIp
     */
    public String getClientIp() {
        return clientIp;
    }

    /**
     * @param clientIp the clientIp to set
     */
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
        this.userLogInfo.setClientip(clientIp);
    }

}
