/**
 *Administrator
 */
package com.ft.otp.core.interceptor.log.servicelog.authmgr.agentconf;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.aopalliance.intercept.MethodInvocation;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.config.AgentPubConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.manager.authmgr.agentconf.entity.AgentConfInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 认证代理配置记录日志类
 *
 * @Date in Feb 22, 2013,5:36:55 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class AgentConfLog {

    private LogCommonObj commonObj = new LogCommonObj();

    public synchronized boolean addAgentConfLog(MethodInvocation invocation, String method)
            throws BaseException {
        int result = 0;
        boolean isOper = false;
        int acid = 0;
        int acobj = 0;
        String desc = "";
        List<String> descList = null;

        //添加认证代理配置记录日志
        if (StrTool.strEquals(method, AdmLogConstant.method_add)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_add;
            acobj = AdmLogConstant.log_obj_auth_conf;
            desc = descStr(invocation);
        }
        //删除认证代理配置记录日志
        if (StrTool.strEquals(method, AdmLogConstant.method_delete)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_del;
            acobj = AdmLogConstant.log_obj_auth_conf;
            desc = getKeyId(invocation);
        }

        if (isOper) {
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
        }
        return isOper;
    }

    public String descStr(MethodInvocation invocation) {
    	StringBuilder desc = new StringBuilder();
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        if (object instanceof AgentConfInfo) {
            AgentConfInfo agentConf = (AgentConfInfo) object;
            String comma = Language.getLangValue("comma", Language.getCurrLang(null), null);
            String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
            
            // 配置名称
            desc.append(Language.getLangValue("log_record_config_name", Language.getCurrLang(null), null)
                    + agentConf.getConfname() + comma);
            
            // 代理配置类型
            if(agentConf.getType() == 0){
            	desc.append(Language.getLangValue("auth_conf_agent_type", Language.getCurrLang(null), null)
            			+ colon
                        + Language.getLangValue("auth_conf_win_login_pro", Language.getCurrLang(null), null) 
                        + comma);
            }else{
            	desc.append(Language.getLangValue("auth_conf_agent_type", Language.getCurrLang(null), null)
            			+ colon
                        + Language.getLangValue("auth_conf_linux_login_pro", Language.getCurrLang(null), null)
                        + comma);
            }
            
            // 用户名格式
            String userformat = "";
            if(agentConf.getUserformat() == 0){
            	userformat = "user@ip";
            }else if(agentConf.getUserformat() == 1){
            	userformat = "user";
            }else{
            	userformat = "user@domain";
            }
            desc.append(Language.getLangValue("auth_conf_uname_format", Language.getCurrLang(null), null)
            		+ colon
                    + userformat 
                    + comma);
            
            if(agentConf.getType() == 0){
            	
            	// 本地登录
            	desc.append(Language.getLangValue("auth_conf_local_protect", Language.getCurrLang(null), null)
            			+ colon
                        + getProtectType(agentConf.getLocalprotect())
                        + comma);
            	
                // 远程登录
            	desc.append(Language.getLangValue("auth_conf_remote_protect", Language.getCurrLang(null), null)
            			+ colon
                        + getProtectType(agentConf.getRemoteprotect())
                        + comma);
            	
                // UAC登录
            	desc.append(Language.getLangValue("auth_conf_uac_protect", Language.getCurrLang(null), null)
            			+ colon
                        + getProtectType(agentConf.getUacprotect()) 
                        + comma);
            }else{
            	// 本地登录
            	desc.append(Language.getLangValue("auth_conf_local_protect", Language.getCurrLang(null), null)
            			+ colon
                        + Language.getLangValue("auth_conf_hand_protection", Language.getCurrLang(null), null)
                        + comma);
            	
                // 远程登录
            	desc.append(Language.getLangValue("auth_conf_remote_protect", Language.getCurrLang(null), null)
            			+ colon
                        + Language.getLangValue("auth_conf_hand_protection", Language.getCurrLang(null), null)
                        + comma);
            	
                // UAC登录
            	desc.append(Language.getLangValue("auth_conf_uac_protect", Language.getCurrLang(null), null)
            			+ colon
                        + Language.getLangValue("auth_conf_vd_not_support", Language.getCurrLang(null), null)
                        + comma);
            }
            
            // 允许未绑定令牌的用户登录
            String unboundLogin = "";
            if(agentConf.getUnboundlogin() == 1){
            	unboundLogin = Language.getLangValue("common_syntax_yes", Language.getCurrLang(null), null);
            }else{
            	unboundLogin = Language.getLangValue("common_syntax_no", Language.getCurrLang(null), null);
            }
            desc.append(Language.getLangValue("auth_conf_unbound_login", Language.getCurrLang(null), null)
            		+ colon
                    + unboundLogin
                    + comma);
            
            // 描述
            String descp = "";
            if(StrTool.strNotNull(agentConf.getDescp())){
            	descp = agentConf.getDescp();
            }else{
            	descp = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
            }
            desc.append(Language.getLangValue("common_syntax_desc", Language.getCurrLang(null), null)
            		+ colon
                    + descp);
        }
        return desc.toString();
    }
    
    public String getProtectType(int flag){
    	String protectType = "";
    	if(flag == 0){
    		protectType = Language.getLangValue("auth_conf_no_protect", Language.getCurrLang(null), null);
    	}else if(flag == 1){
    		protectType = Language.getLangValue("auth_conf_protect_local_account", Language.getCurrLang(null), null);
    	}else if(flag == 2){
    		protectType = Language.getLangValue("auth_conf_protect_domain_account", Language.getCurrLang(null), null);
    	}else{
    		protectType = Language.getLangValue("auth_conf_pro_local_domain_account", Language.getCurrLang(null), null);
    	}
    	return protectType;
    }

    public String getKeyId(MethodInvocation invocation) {
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        StringBuilder sBuilder = new StringBuilder();
        if (object instanceof Set<?>) {
            Set<?> set = (Set<?>) object;
            Iterator<?> iter = set.iterator();
            sBuilder.append(Language.getLangValue("log_record_config_name", Language.getCurrLang(null), null));
            while (iter.hasNext()) {
                String keyId = (String) iter.next();
                sBuilder.append(AgentPubConfig.getAgentConfValue(Integer.parseInt(keyId))).append("，");
            }
        }
        String keyId = sBuilder.toString();
        if (keyId.endsWith("，")) {
            keyId = keyId.substring(0, keyId.length() - 1);
        }
        return keyId;
    }
}
