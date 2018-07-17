/**
 *Administrator
 */
package com.ft.otp.core.interceptor.log.servicelog.orgunit;

import java.util.List;

import org.aopalliance.intercept.MethodInvocation;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.manager.orgunit.orgunit.entity.OrgunitInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 组织机构操作日志记录
 *
 * @Date in Jun 20, 2013,7:36:10 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class OrgunitLog {

    private LogCommonObj commonObj = new LogCommonObj();

    public synchronized boolean addOrgunitLog(MethodInvocation invocation, String method)
            throws BaseException {
        int result = 0;
        boolean isOper = false;
        int acid = 0;
        int acobj = 0;
        String desc = "";
        List<String> descList = null;

        //添加组织机构
        if (StrTool.strEquals(method, AdmLogConstant.method_add)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_add;
            acobj = AdmLogConstant.log_obj_orgunit;
            desc = descStr(invocation);
        }
        //删除组织机构
        if (StrTool.strEquals(method, AdmLogConstant.method_delete)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_del;
            acobj = AdmLogConstant.log_obj_orgunit;
            desc = descStr1(invocation);
        }

        if (isOper) {
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
        }
        return isOper;
    }

    public String descStr1(MethodInvocation invocation) {
        String desc = "";
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        if (object instanceof OrgunitInfo) {
            OrgunitInfo orgunitInfo = (OrgunitInfo) object;
            desc = orgunitInfo.getOrgunitName();
        }
        return desc;
    }

    public String descStr(MethodInvocation invocation) {
        String desc = "";
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
        String str = Language.getLangValue("org_add_child_org", Language.getCurrLang(null), null) + colon;
        if (object instanceof OrgunitInfo) {
            OrgunitInfo orgunitInfo = (OrgunitInfo) object;
            String pname = orgunitInfo.getOrgParentName();
            desc = pname + str + orgunitInfo.getOrgunitName();
        }
        return desc;
    }
}
