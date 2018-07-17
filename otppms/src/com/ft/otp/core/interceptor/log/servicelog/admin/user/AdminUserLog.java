/**
 *Administrator
 */
package com.ft.otp.core.interceptor.log.servicelog.admin.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.manager.admin.admin_role.entity.AdminAndRole;
import com.ft.otp.manager.admin.user.action.aide.AdminUserActionAide;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理员用户操作集中记录日志操作类功能说明
 *
 * @Date in Dec 21, 2011,3:08:03 PM
 *
 * @author ZJY
 */
public class AdminUserLog {
    /**
     * 记录管理员操作日志
     */
    private LogCommonObj commonObj = new LogCommonObj();

    public synchronized boolean addAdminUserLog(MethodInvocation invocation, String method)
            throws BaseException {
        int result = 0, acid = 0, acobj = 0;
        boolean isOper = false;
        String desc = "";
        List<String> descList = null;

        String isRecordLog = "";
        //添加管理员用户
        if (StrTool.strEquals(method, AdmLogConstant.method_add)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_add;
            acobj = AdmLogConstant.log_obj_admin;
            desc = descAddStr(invocation);
        }

        //删除管理员用户
        if (StrTool.strEquals(method, AdmLogConstant.method_delete)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_del;
            acobj = AdmLogConstant.log_obj_admin;
            desc = descAdmin(invocation);
        }
        //禁用、启用管理员
        if (StrTool.strEquals(method, AdmLogConstant.method_updateEnabled)) {
            result = commonObj.operResult(invocation);
            Object[] parameters = invocation.getArguments();
            Object object = parameters[1];
            if (object instanceof Integer) {
                int enableMark = (Integer) object;
                if (enableMark == 1) {
                    acid = AdmLogConstant.log_aid_enable;
                } else {
                    acid = AdmLogConstant.log_aid_disable;
                }
            }
            isOper = true;

            acobj = AdmLogConstant.log_obj_admin;
            desc = descStr(invocation);
        }
        //锁定，解锁管理员
        if (StrTool.strEquals(method, AdmLogConstant.method_updateLocked)) {
            result = commonObj.operResult(invocation);
            Object[] parameters = invocation.getArguments();
            Object object = parameters[0];
            if (object instanceof AdminUser) {
                AdminUser adminUser = (AdminUser) object;
                isRecordLog = adminUser.getIsRecordLog();
                int enableMark = adminUser.getLocked();
                if (enableMark == NumConstant.common_number_0) {
                    acid = AdmLogConstant.log_aid_unlock;
                } else {
                    acid = AdmLogConstant.log_aid_lock;
                }
            }
            isOper = true;
            acobj = AdmLogConstant.log_obj_admin;
            desc = descStr(invocation);
        }
        //修改管理员密码
        if (StrTool.strEquals(method, AdmLogConstant.method_updatePassword)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_edit;
            acobj = AdmLogConstant.log_obj_admin_pwd;
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
            if(!StrTool.strEquals(isRecordLog, StrConstant.common_number_0)) {
                commonObj.addAdminLog(acid, acobj, desc, descList, result);
            }
        }
        return isOper;
    }
    
    /**
     * 提取管理员用户信息
     * @author LXH
     * @date Mar 7, 2014 4:26:54 PM
     * @param invocation
     * @return
     */
    private String descStr(MethodInvocation invocation) {
        String desc = "";
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        if (object instanceof AdminUser) {
            AdminUser adminUser = (AdminUser) object;
            desc = Language.getLangValue("admin_info_account", Language.getCurrLang(null), null)
            	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + adminUser.getAdminid();
        }
        if (object instanceof String) {
            desc = Language.getLangValue("admin_info_account", Language.getCurrLang(null), null)
        	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + (String) object;
        }
        return desc;
    }

    /**
     * 提取管理员用户信息
     * @throws BaseException 
     */
    private String descAddStr(MethodInvocation invocation) throws BaseException {
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        StringBuilder sbr = new StringBuilder();
        if (object instanceof AdminUser) {
            AdminUser adminUser = (AdminUser) object;
            // 管理员账号
            sbr.append( Language.getLangValue("admin_info_account", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + adminUser.getAdminid()
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
            
            // 真实姓名
            String realName = "";
            if (StrTool.strNotNull(adminUser.getRealname())) {
            	realName = adminUser.getRealname();
            }else{
            	realName = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
            }
            sbr.append( Language.getLangValue("common_info_realname", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + realName
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
            
            // 密码验证方式
            String localauth = "";
            if(adminUser.getLocalauth() == 0){
            	localauth = Language.getLangValue("login_mode_only_vd_pwd", Language.getCurrLang(null), null);
            }else if(adminUser.getLocalauth() == 1){
            	localauth = Language.getLangValue("login_mode_only_vd_pin", Language.getCurrLang(null), null);
            }else{
            	localauth = Language.getLangValue("login_mode_vd_pwd_pin", Language.getCurrLang(null), null);
            }
            sbr.append( Language.getLangValue("admin_login_mode", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + localauth
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
            
            // 选择角色
            String roles = "";
            if (StrTool.strNotNull(adminUser.getRolenameStr())) {
            	roles = adminUser.getRolenameStr();
            }else{
            	roles = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)
            			+ Language.getLangValue("comma", Language.getCurrLang(null), null);
            }
            sbr.append( Language.getLangValue("admin_info_sel_role", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + roles);
            
            // 选择机构
//            String orgunitName = "";
//            if (StrTool.strNotNull(adminUser.getOrgunitNames())) {
//            	orgunitName = adminUser.getOrgunitNames() + ",";
//            }else{
//            	orgunitName = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null)
//            			+ Language.getLangValue("comma", Language.getCurrLang(null), null);
//            }
//            sbr.append( Language.getLangValue("user_sel_org", Language.getCurrLang(null), null)
//                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + orgunitName);
            
            // 邮箱
            String email = "";
            if (StrTool.strNotNull(adminUser.getEmail())) {
            	email = adminUser.getEmail();
            }else{
            	email = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
            }
            sbr.append( Language.getLangValue("common_info_email", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + email
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
            
            // 手机
            String calPhone = "";
            if (StrTool.strNotNull(adminUser.getCellphone())) {
            	calPhone = adminUser.getCellphone();
            }else{
            	calPhone = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
            }
            sbr.append( Language.getLangValue("common_info_mobile", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + calPhone
                	+ Language.getLangValue("comma", Language.getCurrLang(null), null));
            
            // 描述
            String desc = "";
            if (StrTool.strNotNull(adminUser.getDescp())) {
            	desc = adminUser.getDescp();
            }else{
            	desc = Language.getLangValue("common_syntax_nothing", Language.getCurrLang(null), null);
            }
            sbr.append( Language.getLangValue("admin_info_descp", Language.getCurrLang(null), null)
                	+ Language.getLangValue("colon", Language.getCurrLang(null), null) + desc);
        }
        return sbr.toString();
    }
    
    /**
     *  管理员删除日志信息
     * @param invocation
     * @return
     */
    private String descAdmin(MethodInvocation invocation) {
        String desc = "";
        desc = Language.getLangValue("admin_info_account", Language.getCurrLang(null), null)
    		+ Language.getLangValue("colon", Language.getCurrLang(null), null) 
    		+ commonObj.getKeyId(invocation);
        return desc;
    }

    private List<String> getDescplist(MethodInvocation invocation) {
        List<String> descpList = new ArrayList<String>();
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        List<?> adminList = null;
        Iterator<?> iter = null;
        String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
        String str = Language.getLangValue("log_record_change_creator", Language.getCurrLang(null), null) + colon;
        if (object instanceof List<?>) {
            adminList = (List<?>) object;
            iter = adminList.iterator();
            AdminUser otpUser = null;
            while (iter.hasNext()) {
                otpUser = (AdminUser) iter.next();
                String creator = otpUser.getCreateuser();
                descpList.add(Language.getLangValue("admin_info", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null) + otpUser.getAdminid()
                        + Language.getLangValue("comma", Language.getCurrLang(null), null) + str + creator);
            }
        }
        return descpList;
    }
}
