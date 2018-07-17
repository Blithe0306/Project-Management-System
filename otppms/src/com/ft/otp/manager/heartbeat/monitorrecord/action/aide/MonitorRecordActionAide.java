/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 * @Date Oct 27, 2014,4:42:36 PM
 */
package com.ft.otp.manager.heartbeat.monitorrecord.action.aide;

import java.util.concurrent.ConcurrentHashMap;

import com.ft.otp.manager.heartbeat.monitorrecord.entity.MonitorRecord;

/**
 * MonitorRecord集合帮助类
 *
 * @Date Oct 27, 2014,4:42:36 PM
 * @version v1.0
 * @author WYJ
 */
public class MonitorRecordActionAide {
    
    public static ConcurrentHashMap <String,MonitorRecord> monitorRecordMap = new ConcurrentHashMap<String,MonitorRecord>();
    
    
}
