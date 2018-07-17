/**
 *Administrator
 */
package com.ft.otp.core.interceptor.log.servicelog.token.distmanager;

import java.util.List;

import org.aopalliance.intercept.MethodInvocation;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.manager.token.distmanager.entity.DistManagerInfo;
import com.ft.otp.util.tool.StrTool;

/**
 *令牌分发管理日志类
 *
 * @Date in Jun 14, 2011,2:55:09 PM
 *
 * @author ZJY
 */
public class DistmanagerLog {

    private LogCommonObj commonObj = new LogCommonObj();

    /**
     * 记录令牌分发模块日志
     * @Date in Jun 14, 2011,2:57:31 PM
     * @param invocation
     * @param method
     * @param linkUser
     * @return
     * @throws BaseException
     */
    public synchronized boolean addDistManagerLog(MethodInvocation invocation, String method)
            throws BaseException {
        int result = 0;
        boolean isOper = false;
        int acid = 0;
        int acobj = 0;
        String desc = "";
        List<String> descList = null;

        //设定令牌标识码、重置
        if (StrTool.strEquals(method, AdmLogConstant.method_update)) {
            result = commonObj.operResult(invocation);
            Object[] parameters = invocation.getArguments();
            Object object = parameters[0];
            if (object instanceof DistManagerInfo) {
                DistManagerInfo distManagerInfo = (DistManagerInfo) object;
                if (distManagerInfo.getMark() == NumConstant.common_number_0) {
                    acid = AdmLogConstant.log_aid_rest;
                } else if (distManagerInfo.getMark() == NumConstant.common_number_1) {
                    acid = AdmLogConstant.log_aid_setmark;
                }
            }

            isOper = true;
            acobj = AdmLogConstant.log_obj_mob_token;
            desc = descStr(invocation);
        }
        if (isOper) {
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
        }

        return isOper;
    }

    /**
     * 提取令牌分发信息
     * @Date in Jun 10, 2011,6:09:08 PM
     * @param invocation
     * @return
     */
    private String descStr(MethodInvocation invocation) {
        String desc = "";
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        if (object instanceof DistManagerInfo) {
            DistManagerInfo distManagerInfo = (DistManagerInfo) object;
            desc = Language.getLangValue("tkn_comm_tknum", Language.getCurrLang(null), null) 
            	+ Language.getLangValue("colon", Language.getCurrLang(null), null)
            	+ distManagerInfo.getToken();
        }

        return desc;
    }

}
