/**
 *Administrator
 */
package com.ft.otp.core.interceptor.log.servicelog.confinfo.sms;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.aopalliance.intercept.MethodInvocation;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.config.SmsInfoConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.manager.confinfo.sms.entity.SmsInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 配置管理 短信网关配置日志记录
 *
 * @Date in Feb 21, 2013,4:32:52 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class SmsLog {

    private LogCommonObj commonObj = new LogCommonObj();

    /**
     * 记录短信网关配置操作日志
     * @Date in Feb 21, 2013,4:36:51 PM
     * @param invocation
     * @param method
     * @param linkUser
     * @return
     * @throws BaseException
     */
    public synchronized boolean addSmsLog(MethodInvocation invocation, String method)
            throws BaseException {
        int result = 0;
        boolean isOper = false;
        int acid = 0;
        int acobj = 0;
        String desc = "";
        List<String> descList = null;

        //添加短信网关配置
        if (StrTool.strEquals(method, AdmLogConstant.method_add)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_add;
            acobj = AdmLogConstant.log_obj_conf_sms;
            desc = descStr(invocation);
        }

        //修改短信网关配置
        if (StrTool.strEquals(method, AdmLogConstant.method_update)) {
            result = commonObj.operResult(invocation);
            isOper = true;
            acid = AdmLogConstant.log_aid_edit;
            acobj = AdmLogConstant.log_obj_conf_sms;
            desc = descStr(invocation);
        }
        
        //启用/禁用短信网关配置
        if (StrTool.strEquals(method, AdmLogConstant.method_enable)) {
            result = commonObj.operResult(invocation);
            Object[] parameters = invocation.getArguments();
            Object object = parameters[0];
            if (object instanceof SmsInfo) {
            	SmsInfo smsInfo = (SmsInfo) object;
                if (smsInfo.getEnabled() == NumConstant.common_number_0) {
                    acid = AdmLogConstant.log_aid_disable;
                } else if (smsInfo.getEnabled() == NumConstant.common_number_1) {
                    acid = AdmLogConstant.log_aid_enable;
                }
            }
            isOper = true;
            acobj = AdmLogConstant.log_obj_conf_sms;
            desc = descStr(invocation);
        }
        

        if (isOper) {
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
        }
        return isOper;
    }

    /**
     * 封装短信网关配置日志
     * @Date in Feb 21, 2013,4:51:50 PM
     * @param invocation
     * @return
     */
    public String descStr(MethodInvocation invocation) {
        String desc = "";
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        if (object instanceof SmsInfo) {
            SmsInfo smsInfo = (SmsInfo) object;
            desc = Language.getLangValue("log_record_config_name", Language.getCurrLang(null), null)
                    + smsInfo.getSmsname();
        }
        return desc;
    }

    public String getKeyId(MethodInvocation invocation) {
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        StringBuilder sBuilder = new StringBuilder();
        if (object instanceof Set<?>) {
            Set<?> set = (Set<?>) object;
            Iterator<?> iter = set.iterator();
            while (iter.hasNext()) {
                String keyId = (String) iter.next();
                sBuilder.append(SmsInfoConfig.getSmsInfo(Integer.parseInt(keyId)).getSmsname()).append("，");
            }
        }
        String keyId = sBuilder.toString();
        if (keyId.endsWith("，")) {
            keyId = keyId.substring(0, keyId.length() - 1);
        }
        return keyId;
    }

}
