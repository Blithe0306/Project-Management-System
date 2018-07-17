/**
 *Administrator
 */
package com.ft.otp.core.interceptor.log.servicelog.authmgr.agent;

import java.util.List;

import org.aopalliance.intercept.MethodInvocation;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.manager.authmgr.agent.entity.AgentInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 认证代理日志类功能说明
 *
 * @Date in Jun 14, 2011,3:45:37 PM
 *
 * @author ZJY
 */
public class AgentInfoLog {
    private LogCommonObj commonObj = new LogCommonObj();

    /**
     * 记录认证代理操作日志
     */
    public synchronized boolean addAgentInfoLog(MethodInvocation invocation, String method)
            throws BaseException {
        int result = 0;
        boolean isOper = false;
        int acid = 0;
        int acobj = 0;
        String desc = "";
        List<String> descList = null;

        //添加认证代理
        if (StrTool.strEquals(method, AdmLogConstant.method_add)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_add;
            acobj = AdmLogConstant.log_obj_auth_agent;
            desc = descStr(invocation);
        }
        //删除认证代理
        if (StrTool.strEquals(method, AdmLogConstant.method_delete)) {
            result = commonObj.operResult(invocation);
            acid = AdmLogConstant.log_aid_del;
            acobj = AdmLogConstant.log_obj_auth_agent;
            isOper = true;
            desc = Language.getLangValue("log_record_auth_agent_ip", Language.getCurrLang(null), null)
                    + commonObj.getKeyId(invocation);
        }

        //启用禁用认证代理
        if (StrTool.strEquals(method, AdmLogConstant.method_updateenabled)) {
            result = commonObj.operResult(invocation);
            Object[] parameters = invocation.getArguments();
            Object object = parameters[0];
            if (object instanceof AgentInfo) {
                AgentInfo agent = (AgentInfo) object;
                int enableMark = agent.getEnabled();
                if (enableMark == 1) {
                    acid = AdmLogConstant.log_aid_enable;
                } else {
                    acid = AdmLogConstant.log_aid_disable;
                }
            }
            isOper = true;
            acobj = AdmLogConstant.log_obj_auth_agent;
            desc = getDescStr(invocation);
        }

        if (isOper) {
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
        }
        return isOper;
    }

    /**
     * 提取认证代理信息
     * @Date in Jun 14, 2011,3:49:11 PM
     * @param invocation
     * @return
     */
    public String getDescStr(MethodInvocation invocation) {
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        AgentInfo agentInfo = null;
        String desc = "";
        if (object instanceof AgentInfo) {
            agentInfo = (AgentInfo) object;
            desc = Language.getLangValue("log_record_auth_agent_ip", Language.getCurrLang(null), null)
                    + agentInfo.getAgentipaddr();

        }

        return desc;
    }
    
    /**
     * 提取认证代理信息
     * @author LXH
     * @date Mar 10, 2014 4:46:19 PM
     * @param invocation
     * @return
     */
    private String descStr(MethodInvocation invocation) {
        StringBuilder desc = new StringBuilder();
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        if (object instanceof AgentInfo) {
        	AgentInfo agentInfo = (AgentInfo) object;
            String comma = Language.getLangValue("comma", Language.getCurrLang(null), null);
            List<?> hostiList = agentInfo.getHostIps();
            String hostIPStr = "";
            
            // 认证代理名称
            if(StrTool.strNotNull(agentInfo.getAgentname())){
                desc.append(Language.getLangValue("auth_agent_agentname", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null)
                        + agentInfo.getAgentname()
                        + comma);
            }
            
            
            if (StrTool.listNotNull(hostiList)) {
            	
            	// 服务器列表
                for (int i = 0; i < hostiList.size(); i++) {
                    hostIPStr = hostIPStr + hostiList.get(i) + "，";
                }
                desc.append(Language.getLangValue("auth_agent_agentip", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null) + agentInfo.getAgentipaddr()
                        + Language.getLangValue("log_record_ass_server", Language.getCurrLang(null), null)
                        + hostIPStr);
            } else {
            	desc.append(Language.getLangValue("auth_agent_agentip", Language.getCurrLang(null), null)
                                + Language.getLangValue("colon", Language.getCurrLang(null), null)
                                + agentInfo.getAgentipaddr()
                                + comma);
            }
            
            // 认证代理类型
            desc.append(Language.getLangValue("auth_agent_agent_type", Language.getCurrLang(null), null)
                    + Language.getLangValue("colon", Language.getCurrLang(null), null)
                    + agentInfo.getAgenttypeStr());
            
            // 认证代理配置
            if(StrTool.strNotNull(agentInfo.getAgentconfStr())){
            	desc.append(Language.getLangValue("auth_agent_agentconf", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null)
                        + agentInfo.getAgentconfStr()
                        + comma);
            }
            
            // 登录保护认证代理宽限期
            if(agentInfo.getFlag() == 1){
            	desc.append(Language.getLangValue("auth_grace_period", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null)
                        + agentInfo.getGraceperiodStr()
                        + comma);
            }
            
            // 描述
            String descp = "";
            if(StrTool.strNotNull(agentInfo.getDescp())){
            	descp = agentInfo.getDescp();
            }else{
            	descp = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
            }
            desc.append(Language.getLangValue("common_syntax_desc", Language.getCurrLang(null), null)
                    + Language.getLangValue("colon", Language.getCurrLang(null), null)
                    + descp);
        }

        return desc.toString();
    }

    public String getagenttypeStr(int agenttype) {
        if (agenttype == 1) {
            return Language.getLangValue("auth_agent_ext_rad_client", Language.getCurrLang(null), null);
        } else if (agenttype == 2) {
            return Language.getLangValue("auth_agent_standard_rad_client", Language.getCurrLang(null), null);
        } else if (agenttype == 4) {
            return Language.getLangValue("auth_agent_wins_login_protect", Language.getCurrLang(null), null);
        } else if (agenttype == 8) {
            return Language.getLangValue("auth_agent_linux_login_protect", Language.getCurrLang(null), null);
        } else if (agenttype == 16) {
            return Language.getLangValue("auth_agent_iis_agent", Language.getCurrLang(null), null);
        } else if (agenttype == 32) {
            return Language.getLangValue("auth_agent_apache_agent", Language.getCurrLang(null), null);
        } else if (agenttype == 64) {
            return Language.getLangValue("auth_agent_soap_client", Language.getCurrLang(null), null);
        } else if (agenttype == 128) {
            return Language.getLangValue("auth_agent_http_client", Language.getCurrLang(null), null);
        } else if (agenttype == 256) {
            return Language.getLangValue("auth_agent_https_client", Language.getCurrLang(null), null);
        }
        return "";
    }
}
