/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 * @Date 2014年7月21日,下午5:32:42
 */
package com.ft.otp.manager.heartbeat.monitorrecord.entity;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.util.tool.DateTool;

/**
 * 类、接口等说明
 *
 * @Date 2014年7月21日,下午5:32:42
 * @version v1.0
 * @author WYJ
 */
public class MonitorRecord extends BaseEntity {

    private int id;//主键
    private String ipAddress;//IP地址
    private String serverType;//服务器类型 1：主服务     2：从服务器
    private String runningState;//服务器运行状态 1：正常  0：异常
    private int recordTime;//记录时间
    
    private String  recordTimeStr = "";
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getIpAddress() {
        return ipAddress;
    }
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    public String getServerType() {
        return serverType;
    }
    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getRunningState() {
        return runningState;
    }
    public void setRunningState(String runningState) {
        this.runningState = runningState;
    }
    public int getRecordTime() {
        return recordTime;
    }
    public void setRecordTime(int recordTime) {
        this.recordTime = recordTime;
    }
    public String getRecordTimeStr() {
        if (getRecordTime() != 0) {
            return DateTool.dateToStr(getRecordTime(), true);
        }
        return recordTimeStr;
    }
    
}
