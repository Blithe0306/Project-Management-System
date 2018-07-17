/**
 *Administrator
 */
package com.ft.otp.core.interceptor.log.servicelog.authmgr.backend;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.aopalliance.intercept.MethodInvocation;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.config.AgentPubConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.manager.authmgr.backend.entity.BackendInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 后端认证日志记录
 *
 * @Date in Feb 22, 2013,4:36:59 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class BackendLog {

    private LogCommonObj commonObj = new LogCommonObj();

    /**
     * 添加后端认证日志
     * @Date in Feb 22, 2013,4:41:19 PM
     * @param invocation
     * @param method
     * @param linkUser
     * @return
     * @throws BaseException
     */
    public synchronized boolean addBackendLog(MethodInvocation invocation, String method)
            throws BaseException {
        int result = 0;
        boolean isOper = false;
        int acid = 0;
        int acobj = 0;
        String desc = "";
        List<String> descList = null;

        //添加后端记录日志
        if (StrTool.strEquals(method, AdmLogConstant.method_add)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_add;
            acobj = AdmLogConstant.log_obj_auth_backend;
            desc = descAddStr(invocation);
        }
        //删除后端认证记录日志
        if (StrTool.strEquals(method, AdmLogConstant.method_delete)) {
            result = commonObj.operResult(invocation);
            isOper = true;
            acid = AdmLogConstant.log_aid_del;
            acobj = AdmLogConstant.log_obj_auth_backend;
            desc = getKeyId(invocation);
        }
        //启用/禁用后端认证记录日志
        if (StrTool.strEquals(method, AdmLogConstant.method_upEnabled)) {
            result = commonObj.operResult(invocation);
            Object[] parameters = invocation.getArguments();
            Object object1 = parameters[1];
            int mark = (Integer) object1;
            if (mark == NumConstant.common_number_1) {
                acid = AdmLogConstant.log_aid_enable;
            } else {
                acid = AdmLogConstant.log_aid_disable;
            }
            isOper = true;
            acobj = AdmLogConstant.log_obj_auth_backend;
            desc = descStr(invocation);
        }
        if (isOper) {
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
        }
        return isOper;
    }
    
    public String descStr(MethodInvocation invocation) {
        String desc = "";
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        if (object instanceof BackendInfo) {
            BackendInfo backend = (BackendInfo) object;
            desc = Language.getLangValue("log_record_config_name", Language.getCurrLang(null), null)
                    + backend.getBackendname();
        }
        return desc;
    }

    public String descAddStr(MethodInvocation invocation) {
    	StringBuilder desc = new StringBuilder();
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        if (object instanceof BackendInfo) {
            BackendInfo backend = (BackendInfo) object;
            String comma = Language.getLangValue("comma", Language.getCurrLang(null), null);
            String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
            // 配置名称
            desc.append(Language.getLangValue("log_record_config_name", Language.getCurrLang(null), null)
                    + backend.getBackendname()
                    + comma);
            
            // 后端认证类型
            String backendType = "";
            if(backend.getBackendtype() == 0){
            	backendType = "Radius";
            }else{
            	backendType = "AD";
            }
            desc.append(Language.getLangValue("auth_bk_type", Language.getCurrLang(null), null) + colon
                    + backendType + comma);
            
            // 后端服务器IP
            desc.append(Language.getLangValue("auth_bk_host", Language.getCurrLang(null), null) + colon
                    + backend.getHost() + comma);
            
            // 备后端服务器IP
            if(StrTool.strNotNull(backend.getSparehost())){
            	desc.append(Language.getLangValue("auth_bk_sparehost", Language.getCurrLang(null), null) + colon
                        + backend.getSparehost() + comma);
            }
            
            // 端口
            desc.append(Language.getLangValue("auth_bk_port", Language.getCurrLang(null), null) + colon
                    + backend.getPort() + comma);
            
            // 转发策略
            String policy = "";
            if(backend.getPolicy() == 1){
            	policy = Language.getLangValue("auth_bk_local_user_not_exist_unbound", Language.getCurrLang(null), null);
            }else if(backend.getPolicy() == 9){
                policy = Language.getLangValue("auth_bk_local_user_not_exist_unbound", Language.getCurrLang(null), null)
                +comma
                +Language.getLangValue("auth_bk_local_auth_succ", Language.getCurrLang(null), null);
            }else{
            	policy = Language.getLangValue("auth_bk_local_auth_succ", Language.getCurrLang(null), null);
            }
            desc.append(Language.getLangValue("auth_bk_policy", Language.getCurrLang(null), null) + colon
                    + policy + comma);
            
            // 域名
            if(backend.getBackendtype() == 1){
            	desc.append(Language.getLangValue("auth_bk_domain", Language.getCurrLang(null), null) + colon
                        + backend.getDelimiter() + comma);
            }
            
            // 连接超时时间（秒）
            desc.append(Language.getLangValue("auth_bk_timeout", Language.getCurrLang(null), null) + colon
                    + backend.getTimeout() + comma);
            
            // 连接超时重试次数
            desc.append(Language.getLangValue("auth_bk_retrycnt", Language.getCurrLang(null), null) + colon
                    + backend.getRetrycnt());
            
        }
        return desc.toString();
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
                sBuilder.append(AgentPubConfig.getBackendValue(Integer.parseInt(keyId))).append("，");
            }
        }
        String keyId = sBuilder.toString();
        if (keyId.endsWith("，")) {
            keyId = keyId.substring(0, keyId.length() - 1);
        }
        return keyId;
    }
}
