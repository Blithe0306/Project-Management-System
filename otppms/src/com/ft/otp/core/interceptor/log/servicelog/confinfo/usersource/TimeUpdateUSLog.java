/**
 *Administrator
 */
package com.ft.otp.core.interceptor.log.servicelog.confinfo.usersource;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.aopalliance.intercept.MethodInvocation;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.confinfo.usersource.entity.UserSourceInfo;
import com.ft.otp.manager.confinfo.usersource.service.IUserSourceServ;
import com.ft.otp.manager.task.entity.TaskInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 定时更新用户来源日志记录
 *
 * @Date in Sep 17, 2013,1:57:55 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class TimeUpdateUSLog {

    private LogCommonObj commonObj = new LogCommonObj();
    
    public synchronized boolean addTimingLog(MethodInvocation invocation, String method) throws BaseException {
        int result = 0;
        boolean isOper = false;
        int acid = 0;
        int acobj = 0;
        String desc = "";
        List<String> descList = null;

        //添加定时任务
        if (StrTool.strEquals(method, AdmLogConstant.method_add)) {
            result = commonObj.operResult(invocation);
            Object[] parameters = invocation.getArguments();
            Object object0 = parameters[0];
            if (object0 instanceof TaskInfo) {
            	TaskInfo taskInfo = (TaskInfo) object0;
                if (taskInfo.getLogFlag() == NumConstant.common_number_1) { // 区分是否记录日志
		            isOper = true;
		            acid = AdmLogConstant.log_aid_add;
		            acobj = AdmLogConstant.log_obj_timing_update_us;
		            desc = addDescStr(invocation);
                }else{// 此处必须返回true
                   return true;
                }
            }
        }

        //修改定时任务
        if (StrTool.strEquals(method, AdmLogConstant.method_update)) {
            result = commonObj.operResult(invocation);
            Object[] parameters = invocation.getArguments();
            Object object0 = parameters[0];
            if (object0 instanceof TaskInfo) {
            	TaskInfo taskInfo = (TaskInfo) object0;
                if (taskInfo.getLogFlag() == NumConstant.common_number_1) { // 区分是否记录日志
		            isOper = true;
		            acid = AdmLogConstant.log_aid_edit;
		            acobj = AdmLogConstant.log_obj_timing_update_us;
		            desc = addDescStr(invocation);
                }else{// 此处必须返回true
                    return true;
                }
            }
        }
        
        // 禁用定时任务
        if (StrTool.strEquals(method, AdmLogConstant.method_delete)) {
        	result = commonObj.operResult(invocation);
        	
        	isOper = true;
            acid = AdmLogConstant.log_aid_disable;
            acobj = AdmLogConstant.log_obj_timing_update_us;
            desc = delDescStr(invocation);
        }

        if (isOper) {
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
        }
        return isOper;
    }
    
    /**
     * 禁用定时任务，封装日志
     * @author LXH
     * @date Nov 5, 2014 5:13:59 PM
     * @param invocation
     * @return
     * @throws BaseException 
     */
    public String delDescStr(MethodInvocation invocation) throws BaseException {
    	IUserSourceServ userSourceServ = (IUserSourceServ) AppContextMgr.getObject("userSourceServ");
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        StringBuilder sbr = new StringBuilder();
        String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
        String sourcename = Language.getLangValue("usource_name", Language.getCurrLang(null), null) + colon;
        String name = "";
        if (object instanceof Set<?>) {
            Set<Integer> set = (Set<Integer>) object;
            Iterator<Integer> iter = set.iterator();
            UserSourceInfo userSourceInfo = new UserSourceInfo();
            while (iter.hasNext()) {
            	int sourceid = iter.next();
            	userSourceInfo.setId(sourceid);
            	userSourceInfo.setSourcetype(-1);
            	// 查出用户来源
            	userSourceInfo = (UserSourceInfo) userSourceServ.find(userSourceInfo);
            	// 取出用户来源名称
            	name = userSourceInfo.getSourcename() + ",";
            }
        }
        if (StrTool.strNotNull(name)) {
        	name = name.substring(0, name.length() - 1);
        	sbr.append(sourcename + name);
        }
        return sbr.toString();
    }
    
    /**
     * 封装定时任务日志
     * @Date in Sep 17, 2013,2:49:13 PM
     * @param invocation
     * @return
     */
    public String addDescStr(MethodInvocation invocation) {
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        StringBuilder sbr = new StringBuilder();
        if (object instanceof TaskInfo) {
            TaskInfo taskInfo = (TaskInfo) object;
            String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
            String sourcename = Language.getLangValue("usource_name", Language.getCurrLang(null), null) + colon;
            
            sbr.append(sourcename + taskInfo.getTaskname()).append("</br>");
            String tenabled = Language.getLangValue("usource_is_timing_update", Language.getCurrLang(null), null)+ colon;
            if (taskInfo.getEnabled() == 0) {
                sbr.append(tenabled).append(Language.getLangValue("common_syntax_disabled", Language.getCurrLang(null), null));
            }else {
                sbr.append(tenabled).append(Language.getLangValue("common_syntax_enable", Language.getCurrLang(null), null)).append("</br>");
                String type = Language.getLangValue("usource_timing_oper_type", Language.getCurrLang(null), null)+ colon;
                if (taskInfo.getTaskmode1() == 1) {
                    sbr.append(type).append(Language.getLangValue("usource_set_of_days", Language.getCurrLang(null), null));
                    
                    if (taskInfo.getTaskmode2() == 1) {
                        sbr.append(Language.getLangValue("usource_timing_oper_point", Language.getCurrLang(null), null)).append("</br>");
                        sbr.append(taskInfo.getTaskhour());
                        sbr.append(Language.getLangValue("common_syntax_hour", Language.getCurrLang(null), null));
                    }else if(taskInfo.getTaskmode2() == 2) {
                    	String taskHour = taskInfo.getTaskhour();
                    	if(taskInfo.getTaskhour().indexOf("/") != -1){
                    		taskHour = taskHour.split("/")[1];
                    	}
                        sbr.append(Language.getLangValue("usource_sel_every_n_hours", Language.getCurrLang(null), null)).append("</br>");
                        sbr.append(Language.getLangValue("usource_timing_oper_interval", Language.getCurrLang(null), null)).append(colon).append(taskHour);
                        sbr.append(Language.getLangValue("common_syntax_hour", Language.getCurrLang(null), null));
                    }
                }else if (taskInfo.getTaskmode1() == 2) {
                    sbr.append(type).append(Language.getLangValue("usource_set_weekly", Language.getCurrLang(null), null)).append("</br>");
                    sbr.append(Language.getLangValue("usource_timing_oper_date", Language.getCurrLang(null), null)).append(colon);
                    if (StrTool.strNotNull(taskInfo.getTaskweek())) {
                        String[] weekAttr = (taskInfo.getTaskweek()).split(",");
                        for(int i=0; i<weekAttr.length; i++) {
                            sbr.append(getWeek(StrTool.parseInt(weekAttr[i]))).append("</br>");
                        }
                    }
                    
                }
            }
        }
        return sbr.toString();
    }
    /**
     * 获取周
     * @Date in Sep 17, 2013,2:49:29 PM
     * @param week
     * @return
     */
    public String getWeek(int week) {
        String weekStr = "";
        switch(week){
            case 1:
                weekStr = Language.getLangValue("common_date_monday", Language.getCurrLang(null), null);
                break;
            case 2:
                weekStr = Language.getLangValue("common_date_tuesday", Language.getCurrLang(null), null);
                break;
            case 3:
                weekStr = Language.getLangValue("common_date_wednesday", Language.getCurrLang(null), null);
                break;
            case 4:
                weekStr = Language.getLangValue("common_date_thursday", Language.getCurrLang(null), null);
                break;
            case 5:
                weekStr = Language.getLangValue("common_date_friday", Language.getCurrLang(null), null);
                break;
            case 6:
                weekStr = Language.getLangValue("common_date_saturday", Language.getCurrLang(null), null);
                break;
            case 0:
                weekStr = Language.getLangValue("common_date_sunday", Language.getCurrLang(null), null);
                break;
        }
        return weekStr;
    }
}
