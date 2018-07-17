/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 * @Date 2014年7月22日,上午10:36:36
 */
package com.ft.otp.manager.heartbeat.monitorrecord.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.manager.heartbeat.monitorrecord.action.aide.MonitorRecordActionAide;
import com.ft.otp.manager.heartbeat.monitorrecord.entity.MonitorRecord;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 双机热备运行监控记录
 *
 * @Date 2014年7月22日,上午10:36:36
 * @version v1.0
 * @author WYJ
 */
public class MonitorRecordAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = -1430807425042246547L;
    
    private Logger logger = Logger.getLogger(MonitorRecordAction.class);
    
    private MonitorRecord monitorRecord;
    /**
     * 获取服务器运行状态列表集合
     * @Date   Oct 28, 2014,1:53:34 PM
     * @author WYJ
     * @return
     * @return List<MonitorRecord>
     * @throws
     */
    public List<MonitorRecord> map2List(){
        List<MonitorRecord> list=new ArrayList<MonitorRecord>();
        if(MonitorRecordActionAide.monitorRecordMap.size()>0){
            for (String key : MonitorRecordActionAide.monitorRecordMap.keySet()) {
                    list.add(MonitorRecordActionAide.monitorRecordMap.get(key));
               }
        }
        return list;
    }
    
    /**
     * 查询预警记录信息
     * @Date   2014年9月18日,上午9:11:04
     * @author WYJ
     * @return
     * @return String
     * @throws
     */
    public String getMonitorsInfo(){
        List<MonitorRecord> monitorRecordList = null;
        StringBuffer msg= new StringBuffer();
        String monitorAdminId=ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_MONITOR_HEART_BEAT,
                ConfConstant.MONITOR_HEART_BEAT_ADMIN_ID);
        try {
                monitorRecordList = map2List();
                if(StrTool.listNotNull(monitorRecordList)){
                    for(MonitorRecord m: monitorRecordList){
                        if(m.getRunningState().equals(StrConstant.common_number_0)){
                            String stype=m.getServerType();
                            if(StrTool.strNotNull(String.valueOf(msg))){
                                msg.append(Language.getLangStr(request, "comma"));
                                msg.append(Language.getLangStr(request, "heart_beat_monitor_home_view_"+stype).replace("#IP#", m.getIpAddress()).replace("#TIME#", DateTool.dateToStr(m.getRecordTime(),true)));
                            }else{
                                msg.append(Language.getLangStr(request, "heart_beat_monitor_home_view_"+stype).replace("#IP#", m.getIpAddress()).replace("#TIME#", DateTool.dateToStr(m.getRecordTime(),true)));
                            }
                        }
                    }
                    //主服务器【192.168.16.83】、从服务器【192.168.16.84】出现异常。请告知管理员admin。
                    if(null != msg && msg.length()>0){
                        msg.append(Language.getLangStr(request, "heart_beat_monitor_home_view_3").replace("#MANAGER#", monitorAdminId));
                    }
                }
            setResponseWrite(String.valueOf(msg));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        
        return null;
    }
  
    
    
    public String init() {
    
        List<?> resultList = map2List();//query(pageArg);
        String jsonStr = JsonTool.getJsonFromList(new Long(100), resultList, monitorRecord);
        setResponseWrite(jsonStr);
        return null;
    }

    public String add() {
        return null;
    }

    public String delete() {
        return null;
    }

    public String modify() {
        return null;
    }

    public String page() {
        return null;
    }

    public String find() {
        return null;
    }

    public String view() {
        return null;
    }

}
