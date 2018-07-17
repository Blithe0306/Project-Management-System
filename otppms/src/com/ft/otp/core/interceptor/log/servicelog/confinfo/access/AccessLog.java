/**
 *Administrator
 */
package com.ft.otp.core.interceptor.log.servicelog.confinfo.access;

import java.util.List;

import org.aopalliance.intercept.MethodInvocation;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.manager.confinfo.access.entity.AccessInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 访问控制策略日志记录
 *
 * @Date in Feb 22, 2013,1:25:54 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class AccessLog {

    private LogCommonObj commonObj = new LogCommonObj();

    /**
     * 添加访问控制策略日志
     * @Date in Feb 22, 2013,1:30:10 PM
     * @param invocation
     * @param method
     * @param linkUser
     * @return
     * @throws BaseException
     */
    public synchronized boolean addAccessLog(MethodInvocation invocation, String method)
            throws BaseException {
        int result = 0;
        boolean isOper = false;
        int acid = 0;
        int acobj = 0;
        String desc = "";
        List<String> descList = null;

        //添加访问控制策略
        if (StrTool.strEquals(method, AdmLogConstant.method_add)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_add;
            acobj = AdmLogConstant.log_obj_conf_access;
            desc = descStr(invocation);
        }
        //访问控制策略启停
        if (StrTool.strEquals(method, AdmLogConstant.method_update)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            Object[] parameters = invocation.getArguments();
            Object object = parameters[0];
            if (object instanceof AccessInfo) {
                AccessInfo accessInfo = (AccessInfo) object;
                if (accessInfo.getEnabled() == NumConstant.common_number_0) {
                    acid = AdmLogConstant.log_aid_disable;
                } else if (accessInfo.getEnabled() == NumConstant.common_number_1) {
                    acid = AdmLogConstant.log_aid_enable;
                }
            }
            acobj = AdmLogConstant.log_obj_conf_access;
        }

        if (isOper) {
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
        }
        return isOper;
    }

    /**
     * 封装访问控制策略日志描述信息
     * @Date in Feb 22, 2013,1:29:39 PM
     * @param invocation
     * @return
     */
    public String descStr(MethodInvocation invocation) {
        String desc = "";
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        if (object instanceof AccessInfo) {
            AccessInfo accessInfo = (AccessInfo) object;
            if (StrTool.strNotNull(accessInfo.getStartip()) && StrTool.strNotNull(accessInfo.getEndip())) {
                desc = Language.getLangValue("log_record_access_ipduan", Language.getCurrLang(null), null)
                        + accessInfo.getStartip() + "-" + accessInfo.getEndip();
            } else {
                desc = Language.getLangValue("log_record_access_ip", Language.getCurrLang(null), null)
                        + accessInfo.getStartip();
            }
        }
        return desc;
    }

}
