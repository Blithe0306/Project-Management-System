/**
 *Administrator
 */
package com.ft.otp.core.interceptor.log.servicelog.confinfo.radius;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.aopalliance.intercept.MethodInvocation;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.config.PubConfConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.manager.confinfo.radius.entity.RadProfileInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * RADIUS配置日志记录
 *
 * @Date in Feb 22, 2013,9:50:40 AM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class RadProfileLog {

    private LogCommonObj commonObj = new LogCommonObj();

    /**
     * Radius配置日志记录
     * @Date in Feb 22, 2013,9:59:37 AM
     * @param invocation
     * @param method
     * @param linkUser
     * @return
     * @throws BaseException
     */
    public synchronized boolean addRadProLog(MethodInvocation invocation, String method)
            throws BaseException {
        int result = 0;
        boolean isOper = false;
        int acid = 0;
        int acobj = 0;
        String desc = "";
        List<String> descList = null;

        //添加RADIUS配置
        if (StrTool.strEquals(method, AdmLogConstant.method_add)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_add;
            acobj = AdmLogConstant.log_obj_conf_radius;
            desc = descStr(invocation);
        }
        //修改RADIUS配置
        if (StrTool.strEquals(method, AdmLogConstant.method_update)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_edit;
            acobj = AdmLogConstant.log_obj_conf_radius;
            desc = descStr(invocation);
        }
        //删除RADIUS配置
        if (StrTool.strEquals(method, AdmLogConstant.method_delete)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_del;
            acobj = AdmLogConstant.log_obj_conf_radius;
            desc = getKeyId(invocation);
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
        if (object instanceof RadProfileInfo) {
            RadProfileInfo radProInfo = (RadProfileInfo) object;
            desc = Language.getLangValue("log_record_config_name", Language.getCurrLang(null), null)
                    + radProInfo.getProfileName();
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
            sBuilder.append(Language.getLangValue("log_record_config_name", Language.getCurrLang(null), null));
            while (iter.hasNext()) {
                String keyId = (String) iter.next();
                sBuilder.append(PubConfConfig.getRadProValue(Integer.parseInt(keyId))).append("，");
            }
        }
        String keyId = sBuilder.toString();
        if (keyId.endsWith("，")) {
            keyId = keyId.substring(0, keyId.length() - 1);
        }
        return keyId;
    }
}
