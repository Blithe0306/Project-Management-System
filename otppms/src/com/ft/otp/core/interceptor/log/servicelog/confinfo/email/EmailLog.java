/**
 *Administrator
 */
package com.ft.otp.core.interceptor.log.servicelog.confinfo.email;

import java.util.List;

import org.aopalliance.intercept.MethodInvocation;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.config.PubConfConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.manager.confinfo.email.entity.EmailInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 配置管理 邮件服务器配置操作记录
 *
 * @Date in Feb 20, 2013,4:33:24 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class EmailLog {
    private LogCommonObj commonObj = new LogCommonObj();

    /**
     * 记录邮件服务器配置操作日志
     * @Date in Feb 20, 2013,5:28:20 PM
     * @param invocation
     * @param method
     * @param linkUser
     * @return
     * @throws BaseException
     */
    public synchronized boolean addEmailLog(MethodInvocation invocation, String method)
            throws BaseException {
        int result = 0;
        boolean isOper = false;
        int acid = 0;
        int acobj = 0;
        String desc = "";
        List<String> descList = null;

        //添加邮箱
        if (StrTool.strEquals(method, AdmLogConstant.method_add)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_add;
            acobj = AdmLogConstant.log_obj_conf_mail;
            desc = descStr(invocation);
        }

        //修改邮箱
        if (StrTool.strEquals(method, AdmLogConstant.method_update)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_edit;
            acobj = AdmLogConstant.log_obj_conf_mail;
            desc = descStr(invocation);
        }

        //删除邮箱
        if (StrTool.strEquals(method, AdmLogConstant.method_delete)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_del;
            acobj = AdmLogConstant.log_obj_conf_mail;
            desc = getKeyId(invocation);
        }
        //设置默认邮箱
        if (StrTool.strEquals(method, AdmLogConstant.method_updateIsdefaultOE)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_setdefault;
            acobj = AdmLogConstant.log_obj_conf_mail;
            desc = descStr(invocation);
        }

        if (isOper) {
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
        }
        return isOper;
    }

    /**
     * 封装邮件服务器描述信息
     * @Date in Feb 20, 2013,5:28:45 PM
     * @param invocation
     * @return
     */
    public String descStr(MethodInvocation invocation) {
        String desc = "";
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        if (object instanceof EmailInfo) {
            EmailInfo email = (EmailInfo) object;
            String comma = Language.getLangValue("comma", Language.getCurrLang(null), null);
            String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
            String serhost = Language.getLangValue("email_host", Language.getCurrLang(null), null) + colon;
            String emailname = comma + Language.getLangValue("log_record_email_name", Language.getCurrLang(null), null)
                    + colon;
            desc = serhost + email.getHost() + emailname + email.getServname();
        }
        return desc;
    }

    /**
     * 根据Key取得Map中值
     * @Date in Feb 21, 2013,7:38:52 PM
     * @param invocation
     * @return
     */
    public String getKeyId(MethodInvocation invocation) {
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        StringBuilder sBuilder = new StringBuilder();
        if (object instanceof EmailInfo) {
            EmailInfo emailInfo = (EmailInfo) object;
            int ids[] = emailInfo.getBatchIdsInt();
            for (int i = 0; i < ids.length; i++) {
                sBuilder.append(PubConfConfig.getEmailValue(ids[i])).append("，");
            }
        }
        String keyId = sBuilder.toString();
        if (keyId.endsWith("，")) {
            keyId = keyId.substring(0, keyId.length() - 1);
        }
        return keyId;
    }
}
