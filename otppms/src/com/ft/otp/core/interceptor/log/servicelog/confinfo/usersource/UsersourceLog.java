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
import com.ft.otp.common.config.PubConfConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.manager.confinfo.usersource.entity.UserSourceInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户来源日志记录
 *
 * @Date in Sep 17, 2013,9:33:13 AM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class UsersourceLog {

    private LogCommonObj commonObj = new LogCommonObj();

    /**
     * 添加用户来源配置
     * @Date in Sep 17, 2013,9:38:44 AM
     * @param invocation
     * @param method
     * @return
     * @throws BaseException
     */
    public synchronized boolean addSourceLog(MethodInvocation invocation, String method) throws BaseException {
        int result = 0;
        boolean isOper = false;
        int acid = 0;
        int acobj = 0;
        String desc = "";
        List<String> descList = null;

        //添加用户来源
        if (StrTool.strEquals(method, AdmLogConstant.method_add)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_add;
            acobj = AdmLogConstant.log_obj_usersource;
            desc = addDescStr(invocation);
        }

        //修改用户来源
        if (StrTool.strEquals(method, AdmLogConstant.method_update)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_edit;
            acobj = AdmLogConstant.log_obj_usersource;
            desc = addDescStr(invocation);
        }

        //删除用户来源
        if (StrTool.strEquals(method, AdmLogConstant.method_delete)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_del;
            acobj = AdmLogConstant.log_obj_usersource;
            desc = getKeyId(invocation);
        }

        if (isOper) {
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
        }
        return isOper;
    }
    
    /**
     * 封装用户来源配置日志信息
     * @Date in Sep 17, 2013,9:39:09 AM
     * @param invocation
     * @return
     */
    public String addDescStr(MethodInvocation invocation) {
        String desc = "";
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        if (object instanceof UserSourceInfo) {
            UserSourceInfo usource = (UserSourceInfo) object;
            String comma = Language.getLangValue("comma", Language.getCurrLang(null), null);
            String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
            String sourcename = Language.getLangValue("usource_name", Language.getCurrLang(null), null) + colon;
            String sourcetype = comma + Language.getLangValue("usource_type", Language.getCurrLang(null), null);
            String sType = "";
            if (usource.getSourcetype() == 0) {
                sType = Language.getLangValue("usource_remote_db", Language.getCurrLang(null), null);
            }else if (usource.getSourcetype() == 1) {
                sType = "LDAP";
            }
            desc = sourcename + usource.getSourcename() + sourcetype + sType;
        }
        return desc;
    }
    
    public String descStr(MethodInvocation invocation) {
        String desc = "";
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        if (object instanceof UserSourceInfo) {
            UserSourceInfo usource = (UserSourceInfo) object;
            String sourcename = Language.getLangValue("usource_name", Language.getCurrLang(null), null);
            String mto = Language.getLangValue("log_modified_to", Language.getCurrLang(null), null);
            if (!StrTool.strEquals(PubConfConfig.getUsourceValue(usource.getId()), usource.getSourcename())) {
                desc += sourcename + mto + usource.getUsername();
            }
        }
        return desc;
    }
    
    public String getKeyId(MethodInvocation invocation) {
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        StringBuilder sBuilder = new StringBuilder();
        String comma = Language.getLangValue("comma", Language.getCurrLang(null), null);
        if (object instanceof Set<?>) {
            Set<?> set = (Set<?>) object;
            Iterator<?> iter = set.iterator();
            while (iter.hasNext()) {
                String keyId = (String) iter.next();
                sBuilder.append(PubConfConfig.getUsourceValue(Integer.parseInt(keyId))).append(comma);
            }
        }
        String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
        String sourcename = Language.getLangValue("usource_name", Language.getCurrLang(null), null) + colon;
        String keyId = sBuilder.toString();
        if (keyId.endsWith(comma)) {
            keyId = sourcename + keyId.substring(0, keyId.length() - 1);
        }
        return keyId;
    }

}
