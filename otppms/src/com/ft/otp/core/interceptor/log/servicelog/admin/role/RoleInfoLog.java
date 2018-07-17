/**
 *Administrator
 */
package com.ft.otp.core.interceptor.log.servicelog.admin.role;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.manager.admin.role.entity.RoleInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理员角色功能模块日志集中记录类
 *
 * @Date in Dec 21, 2011,3:07:52 PM
 *
 * @author ZJY
 */
public class RoleInfoLog {
    private LogCommonObj commonObj = new LogCommonObj();

    /**
     * 记录管理员角色操作日志
     * @Date in Jun 8, 2011,2:51:09 PM
     * @author zjy
     */
    public synchronized boolean addRoleInfoLog(MethodInvocation invocation, String method)
            throws BaseException {
        int result = 0, acid = 0, acobj = 0;
        boolean isOper = false;
        String desc = "";
        List<String> descList = null;

        //添加管理员角色
        if (StrTool.strEquals(method, AdmLogConstant.method_add)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_add;
            acobj = AdmLogConstant.log_obj_adminrole;
            desc = descStr(invocation);

        }

        //变更创建人
        if (StrTool.strEquals(method, AdmLogConstant.method_updateDesignate)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_dsignate;
            descList = getDescplist(invocation);
        }

        if (isOper) {
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
        }
        return isOper;
    }

    /**
     * 提取管理员角色信息
     * @Date in Dec 21, 2011,3:24:30 PM
     * @param invocation
     * @return list
     * @author zjy
     */
    private String descStr(MethodInvocation invocation) {
        String desc = "";
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        if (object instanceof RoleInfo) {
            RoleInfo adminRole = (RoleInfo) object;
            desc = Language.getLangValue("admin_role_name", Language.getCurrLang(null), null)
                    + Language.getLangValue("colon", Language.getCurrLang(null), null) + adminRole.getRoleName();
        }
        return desc;
    }

    private List<String> getDescplist(MethodInvocation invocation) {
        List<String> descpList = new ArrayList<String>();
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        List<?> roleList = null;
        Iterator<?> iter = null;
        String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
        String str = Language.getLangValue("log_record_change_creator", Language.getCurrLang(null), null) + colon;
        if (object instanceof List<?>) {
            roleList = (List<?>) object;
            iter = roleList.iterator();
            RoleInfo roleInfo = null;
            while (iter.hasNext()) {
                roleInfo = (RoleInfo) iter.next();
                String creator = roleInfo.getCreateuser();
                descpList.add(Language.getLangValue("admin_role", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null) + roleInfo.getRoleName()
                        + Language.getLangValue("comma", Language.getCurrLang(null), null) + str + creator);
            }
        }
        return descpList;
    }
}
