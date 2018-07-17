/**
 *Administrator
 */
package com.ft.otp.manager.logs.adminlog.form;

import java.util.Date;
import com.ft.otp.base.form.BaseQueryForm;
import com.ft.otp.manager.logs.adminlog.entity.AdminLogInfo;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理员日志查询Form说明
 *
 * @Date in May 3, 2011,1:18:29 PM
 *
 * @author ZJY
 */
public class AdminLogQueryForm extends BaseQueryForm {

    //保存实体的查询条件
    AdminLogInfo adminLogInfo = new AdminLogInfo();

    private int actionResult; //操作结果
    private String clientIp; //操作客户端IP地址
    private String startLogTime; //开始时间
    private String endLogTime; //结束时间
    private String operator; //操作人员
    private String descp; //日志描述

    /**
     * @return the operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * @param operator the operator to set
     */
    public void setOperator(String operator) {
        this.adminLogInfo.setOperator(operator);
        this.operator = operator;
    }

    /**
     * @return the adminLogInfo
     */
    public AdminLogInfo getAdminLogInfo() {
        return adminLogInfo;
    }

    /**
     * @param adminLogInfo the adminLogInfo to set
     */
    public void setAdminLogInfo(AdminLogInfo adminLogInfo) {
        this.adminLogInfo = adminLogInfo;
    }

    /**
     * @return the startLogTime
     */
    public String getStartLogTime() {
        return startLogTime;
    }

    /**
     * @param startLogTime the startLogTime to set
     * @throws ParseException 
     */
    public void setStartLogTime(String startLogTime) {
        this.startLogTime = startLogTime;

        Date ds = null;
        if (StrTool.strNotNull(startLogTime)) {
            int mark = 0;
            if (startLogTime.length() == 10) {
                mark = 1;
            }
            ds = DateTool.strToDate(startLogTime, mark);
        }

        int time = DateTool.dateToInt(ds);
        this.adminLogInfo.setStartLogTime(time);
    }

    /**
     * @return the endLogTime
     */
    public String getEndLogTime() {
        return endLogTime;
    }

    /**
     * @param endLogTime the endLogTime to set
     */
    public void setEndLogTime(String endLogTime) {
        this.endLogTime = endLogTime;

        Date es = null;
        if (StrTool.strNotNull(endLogTime)) {
            int mark = 0;
            if (endLogTime.length() == 10) {
                mark = 2;
            }
            es = DateTool.strToDate(endLogTime, mark);
        }

        int time = DateTool.dateToInt(es);
        this.adminLogInfo.setEndLogTime(time);
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.adminLogInfo.setDescp(descp);
        this.descp = descp;
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
        this.adminLogInfo.setActionresult(actionResult);
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
        this.adminLogInfo.setClientip(clientIp);
    }

}
