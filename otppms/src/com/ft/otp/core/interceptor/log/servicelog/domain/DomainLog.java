/**
 *Administrator
 */
package com.ft.otp.core.interceptor.log.servicelog.domain;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.aopalliance.intercept.MethodInvocation;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.config.DomainConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.manager.orgunit.domain.entity.DomainInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 组织机构域管理日志记录
 *
 * @Date in Feb 25, 2013,2:37:11 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class DomainLog {

    private LogCommonObj commonObj = new LogCommonObj();

    /**
     * 域管理日志记录
     * @Date in Feb 25, 2013,2:47:31 PM
     * @param invocation
     * @param method
     * @param linkUser
     * @return
     * @throws BaseException
     */
    public synchronized boolean addDomainLog(MethodInvocation invocation, String method)
            throws BaseException {
        int result = 0;
        boolean isOper = false;
        int acid = 0;
        int acobj = 0;
        String desc = "";
        List<String> descList = null;

        //添加域记录
        if (StrTool.strEquals(method, AdmLogConstant.method_add)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_add;
            acobj = AdmLogConstant.log_obj_domain;
            desc = descStr(invocation);
        }

        //删除域记录
        if (StrTool.strEquals(method, AdmLogConstant.method_delete)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_del;
            acobj = AdmLogConstant.log_obj_domain;
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
        if (object instanceof DomainInfo) {
            DomainInfo domain = (DomainInfo) object;
            desc = Language.getLangValue("domain_info_name", Language.getCurrLang(null), null)
                    + Language.getLangValue("colon", Language.getCurrLang(null), null) + domain.getDomainName();
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
            sBuilder.append(Language.getLangValue("domain_info_name", Language.getCurrLang(null), null)
                    + Language.getLangValue("colon", Language.getCurrLang(null), null));
            while (iter.hasNext()) {
                String keyId = (String) iter.next();
                sBuilder.append(DomainConfig.getValue(Integer.parseInt(keyId))).append("，");
            }
        }
        String keyId = sBuilder.toString();
        if (keyId.endsWith("，")) {
            keyId = keyId.substring(0, keyId.length() - 1);
        }
        return keyId;
    }
}
