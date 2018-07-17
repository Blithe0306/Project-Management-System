/**
 *Administrator
 */
package com.ft.otp.core.interceptor.log.servicelog.authmgr.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.manager.authmgr.server.entity.ServerInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 认证服务器日志类功能说明
 *
 * @Date in Jun 14, 2011,3:45:37 PM
 *
 * @author ZJY
 */
public class ServerInfoLog {
    private LogCommonObj commonObj = new LogCommonObj();

    /**
     * 记录认证服务器操作日志
     */
    public synchronized boolean addServerInfoLog(MethodInvocation invocation, String method)
            throws BaseException {
        int result = 0;
        boolean isOper = false;
        int acid = 0;
        int acobj = 0;
        String desc = "";
        List<String> descList = null;

        //添加认证服务器
        if (StrTool.strEquals(method, AdmLogConstant.method_add)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_add;
            acobj = AdmLogConstant.log_obj_auth_server;
            descList = getDescList(invocation);
        }
        
        //删除认证服务器
        if (StrTool.strEquals(method, AdmLogConstant.method_delete)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_del;
            acobj = AdmLogConstant.log_obj_auth_server;
            desc = Language.getLangValue("auth_ser_hostname", Language.getCurrLang(null), null) 
            	+ Language.getLangValue("colon", Language.getCurrLang(null), null)
            	+ commonObj.getKeyId(invocation);
        }

        if (isOper) {
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
        }
        return isOper;
    }

    /**
     * 提取认证服务器信息
     * @Date in Jun 14, 2011,3:49:11 PM
     * @param invocation
     * @return
     */
    public List<String> getDescList(MethodInvocation invocation) {
        List<String> descList = new ArrayList<String>();
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        ServerInfo serverInfo = null;
        String serverId = "";
        List<?> utList = null;
        Iterator<?> iter = null;
        if (object instanceof List<?>) {
            utList = (List<?>) object;
            iter = utList.iterator();
            while (iter.hasNext()) {
                serverInfo = (ServerInfo) iter.next();
                serverId = serverInfo.getHostipaddr();
                descList.add(serverId);
            }
        }
        if (object instanceof ServerInfo) {
            serverInfo = (ServerInfo) object;
            String comma = Language.getLangValue("comma", Language.getCurrLang(null), null);
            String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
            String sername = Language.getLangValue("auth_ser_hostname", Language.getCurrLang(null), null) + colon;
            String hostip = comma + Language.getLangValue("auth_ser_hostip", Language.getCurrLang(null), null) + colon;
            String priority = comma + Language.getLangValue("auth_ser_priority", Language.getCurrLang(null), null) + colon;
            String websername = comma + Language.getLangValue("auth_ser_webservicename", Language.getCurrLang(null), null) + colon;
            String desc = comma + Language.getLangValue("common_syntax_desc", Language.getCurrLang(null), null) + colon;
            String authport = comma + Language.getLangValue("auth_ser_authport", Language.getCurrLang(null), null)
                    + colon; // 扩展Radius认证端口
            String syncport = comma + Language.getLangValue("auth_ser_syncport", Language.getCurrLang(null), null)
                    + colon; // 扩展Radius同步端口
            String radport = comma
                    + Language.getLangValue("auth_ser_radius_authport", Language.getCurrLang(null), null) + colon; // 标准Radius认证端口
            String soapport = comma + Language.getLangValue("auth_ser_soap_port", Language.getCurrLang(null), null)
                    + colon;
            String ftradiusenabled = comma + Language.getLangValue("auth_ser_ftradius_enabled", Language.getCurrLang(null), null)
					+ colon; // 启用扩展Radius服务
            String radiusenabled = comma + Language.getLangValue("auth_ser_radius_enabled", Language.getCurrLang(null), null)
            		+ colon; // 启用标准Radius服务
            String yesDes = Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null);
            String noDes = Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null);

            // 优先级
            String sel_priority = "";
            if(serverInfo.getPriority() == 0){
            	sel_priority = Language.getLangValue("common_syntax_advanced", Language.getCurrLang(null), null);
            }else if(serverInfo.getPriority() == 1){
            	sel_priority = Language.getLangValue("common_syntax_ordinary", Language.getCurrLang(null), null);
            }else{
            	sel_priority = Language.getLangValue("common_syntax_low", Language.getCurrLang(null), null);
            }
            
            // 启用扩展Radius服务
            String descFtrad = "";
            if(serverInfo.getFtradiusenabled() == NumConstant.common_number_1){
            	descFtrad =  ftradiusenabled + yesDes + authport + serverInfo.getAuthport() + syncport + serverInfo.getSyncport();
            }else{
            	descFtrad = ftradiusenabled + noDes;
            }
            
            // 启用标准Radius服务
            String descRad = "";
            if(serverInfo.getRadiusenabled() == NumConstant.common_number_1){
            	descRad =  radiusenabled + yesDes + radport + serverInfo.getRadauthport();
            }else{
            	descRad = radiusenabled + noDes;
            }
            
            // 描述
            String descp = "";
            if(StrTool.strNotNull(serverInfo.getDescp())){
            	descp = desc + serverInfo.getDescp();
            }
            
            descList.add(sername + serverInfo.getHostname() + hostip + serverInfo.getHostipaddr() + priority + sel_priority 
            		+ descFtrad + descRad + soapport + serverInfo.getSoapport() + websername + serverInfo.getWebservicename()
            		+ descp);
        }

        return descList;
    }
}
