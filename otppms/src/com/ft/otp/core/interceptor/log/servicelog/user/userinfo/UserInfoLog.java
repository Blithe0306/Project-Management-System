/**
 *Administrator
 */
package com.ft.otp.core.interceptor.log.servicelog.user.userinfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.aopalliance.intercept.MethodInvocation;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户功能模块日志集中记录类
 *
 * @Date in Jun 8, 2011,3:17:47 PM
 *
 * @author TBM
 */
public class UserInfoLog {

    private LogCommonObj commonObj = new LogCommonObj();

    /**
     * 记录用户操作日志
     * @Date in Jun 8, 2011,2:51:09 PM
     */
    public synchronized boolean addUserInfoLog(MethodInvocation invocation, String method) throws BaseException {
        int result = 0;
        boolean isOper = false;
        int acid = 0;
        int acobj = 0;
        String desc = "";
        List<String> descList = null;

        //添加用户
        if (StrTool.strEquals(method, AdmLogConstant.method_add)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_add;
            acobj = AdmLogConstant.log_obj_user;
            desc = descAddStr(invocation);
        }
        
        // 设置用户静态密码
        if (StrTool.strEquals(method, AdmLogConstant.method_updateStaticPass)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_edit;
            acobj = AdmLogConstant.log_obj_static_pwd;
            desc = descStrStaticPass(invocation);

        }

        //删除用户
        if (StrTool.strEquals(method, AdmLogConstant.method_delete)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_del;
            acobj = AdmLogConstant.log_obj_user;
            descList = getDescplist(invocation);
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
            return false;
        }
        //导入（批量导入）用户
        if (StrTool.strEquals(method, AdmLogConstant.method_batchimportUser)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_import;
            acobj = AdmLogConstant.log_obj_user;
            desc = descStr(invocation);
        }
        //导入（批量导入）用户,用户来源
        if (StrTool.strEquals(method, AdmLogConstant.method_batchImportUs)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_usersoure_import;
            acobj = AdmLogConstant.log_obj_user;
            descList = getDescList(invocation);
        }
        //导入用户时,批量更新用户,用户来源
        if (StrTool.strEquals(method, AdmLogConstant.method_batchUpdateUser)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_usersoure_update;
            acobj = AdmLogConstant.log_obj_user;
            descList = getDescList(invocation);
        }
        //LDAP,批量删除用户,用户来源
        if (StrTool.strEquals(method, AdmLogConstant.method_batchDelUser)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_usersoure_delete;
            acobj = AdmLogConstant.log_obj_user;
            descList = getDescList(invocation);
        }
        //锁定、解锁用户
        if (StrTool.strEquals(method, AdmLogConstant.method_updateUserLost)) {
            result = commonObj.operResult(invocation);
            Object[] parameters = invocation.getArguments();
            Object object = parameters[1];
            if (object instanceof Integer) {
                int lockMark = (Integer) object;
                if (lockMark == NumConstant.common_number_1) {
                    acid = AdmLogConstant.log_aid_lock;
                } else if (lockMark == NumConstant.common_number_2) {
                    acid = AdmLogConstant.log_aid_lock;
                } else {
                    acid = AdmLogConstant.log_aid_unlock;
                }
            }
            isOper = true;
            acobj = AdmLogConstant.log_obj_user;
            desc = descStr(invocation);
        }
        //启用，禁用用户
        if (StrTool.strEquals(method, AdmLogConstant.method_updateUserEnable)) {
            result = commonObj.operResult(invocation);
            Object[] parameters = invocation.getArguments();
            Object object = parameters[1];
            if (object instanceof Integer) {
                int enableMark = (Integer) object;
                if (enableMark == NumConstant.common_number_0) {
                    acid = AdmLogConstant.log_aid_disable;
                } else if (enableMark == NumConstant.common_number_1) {
                    acid = AdmLogConstant.log_aid_enable;
                }
            }
            isOper = true;
            acobj = AdmLogConstant.log_obj_user;
            desc = descStr(invocation);
        }

        //批量停用/启用
        if (StrTool.strEquals(method, AdmLogConstant.method_updateUserAbled)) {
            result = commonObj.operResult(invocation);
            Object[] parameters = invocation.getArguments();
            Object object1 = parameters[1];
            int mark = (Integer) object1;
            if (mark == NumConstant.common_number_1) {
                acid = AdmLogConstant.log_aid_enable;
            } else {
                acid = AdmLogConstant.log_aid_disable;
            }

            isOper = true;
            acobj = AdmLogConstant.log_obj_user;
            descList = getDescplist(invocation);
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
            return false;
        }

        //批量锁定/解锁
        if (StrTool.strEquals(method, AdmLogConstant.method_updateUserLocked)) {
            result = commonObj.operResult(invocation);
            Object[] parameters = invocation.getArguments();
            Object object1 = parameters[1];
            int mark = (Integer) object1;
            if (mark != NumConstant.common_number_0) {
                acid = AdmLogConstant.log_aid_lock;
            } else {
                acid = AdmLogConstant.log_aid_unlock;
            }
            isOper = true;
            acobj = AdmLogConstant.log_obj_user;
            descList = getDescplist(invocation);
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
            return false;
        }

        //变更组织机构
        if (StrTool.strEquals(method, AdmLogConstant.method_updateUserOrgunit)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_change;
            acobj = AdmLogConstant.log_obj_orgunit;
            descList = getDescListOrgunitNameStr(invocation);

        }

        // 设置Radius配置
        if (StrTool.strEquals(method, AdmLogConstant.method_batchSetRadId)) {
            result = commonObj.operResult(invocation);
            isOper = true;
            acid = AdmLogConstant.log_aid_add;
            acobj = AdmLogConstant.log_obj_conf_radius;
            descList = getDescListRadiusStr(invocation);
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
            return false;
        }

        // 设置后端认证
        if (StrTool.strEquals(method, AdmLogConstant.method_batchSetBackendId)) {
            result = commonObj.operResult(invocation);
            isOper = true;
            acid = AdmLogConstant.log_aid_set;
            acobj = AdmLogConstant.log_obj_auth_backend;
            descList = getDescListBackendStr(invocation);
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
            return false;
        }

        // 设置本地认证模式
        if (StrTool.strEquals(method, AdmLogConstant.method_batchSetLocalauth)) {
            result = commonObj.operResult(invocation);
            isOper = true;
            acid = AdmLogConstant.log_aid_set;
            acobj = AdmLogConstant.log_obj_auth_local;
            descList = getDescListLocalStr(invocation);
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
            return false;
        }

        if (isOper) {
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
        }
        return isOper;
    }
    
    private String descStr(MethodInvocation invocation) {
    	 StringBuilder desc = new StringBuilder();
         Object[] parameters = invocation.getArguments();
         Object object = parameters[0];
         if (object instanceof UserInfo) {
             UserInfo userInfo = (UserInfo) object;
             String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
             String account = Language.getLangValue("user_info_account", Language.getCurrLang(null), null) + colon;
             desc.append(account).append(userInfo.getUserId());
         }

         if (object instanceof ArrayList<?>) {
             List<?> utList = (ArrayList<?>) object;
             Iterator<?> iter = utList.iterator();
             while (iter.hasNext()) {
                 UserInfo userInfo = (UserInfo) iter.next();
                 desc.append(userInfo.getUserId()).append(",");
             }
         }

         // 日志表的描述长度为4000 如果大于4000 则...
         if (desc.toString().length() > 4000) {
             return desc.toString().substring(0, 3996) + "...";
         }
         return desc.toString();
    }

    /**
     * 提取用户信息
     * @Date in Jun 10, 2011,6:09:08 PM
     * @param invocation
     * @return
     */
    private String descAddStr(MethodInvocation invocation) {
        StringBuilder desc = new StringBuilder();
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        if (object instanceof UserInfo) {
            UserInfo userInfo = (UserInfo) object;
            String comma = Language.getLangValue("comma", Language.getCurrLang(null), null);
            String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
            String account = Language.getLangValue("user_info_account", Language.getCurrLang(null), null) + colon;
            String orgunitName = comma + Language.getLangValue("user_sel_org", Language.getCurrLang(null), null) + colon;
            String localAuthName = comma + Language.getLangValue("user_local_auth_mode", Language.getCurrLang(null), null) + colon;
            String backendName = comma + Language.getLangValue("user_whether_backend_auth", Language.getCurrLang(null), null) + colon;
            String radName = comma + Language.getLangValue("user_return_client_Rad_conf", Language.getCurrLang(null), null) + colon;
            
            // 用户账户
            desc.append(account).append(userInfo.getUserId());
            
            // 选择机构
            desc.append(orgunitName).append(userInfo.getDOrgunitName());
            
            // 真实姓名
            if (StrTool.strNotNull(userInfo.getRealName())) {
                String realname = comma
                        + Language.getLangValue("user_info_real_name", Language.getCurrLang(null), null) + colon;
                desc.append(realname).append(userInfo.getRealName());
            }
            
            // 本地认证模式
            if(userInfo.getLocalAuth() == 0){
            	desc.append(localAuthName).append(Language.getLangValue("local_auth_only_vd_tkn", Language.getCurrLang(null), null));
            }else if(userInfo.getLocalAuth() == 1){
            	desc.append(localAuthName).append(Language.getLangValue("local_auth_vd_pwd_tkn", Language.getCurrLang(null), null));
            }else{
            	desc.append(localAuthName).append(Language.getLangValue("local_auth_only_vd_pwd", Language.getCurrLang(null), null));
            }
            
            // 后端认证模式
            if(userInfo.getBackEndAuth() == 0){
            	desc.append(backendName).append(Language.getLangValue("backend_auth_default", Language.getCurrLang(null), null));
            }else if(userInfo.getBackEndAuth() == 1){
            	desc.append(backendName).append(Language.getLangValue("backend_auth_need", Language.getCurrLang(null), null));
            }else{
            	desc.append(backendName).append(Language.getLangValue("backend_auth_no_need", Language.getCurrLang(null), null));
            }
            
            // 返回Radius属性
            if(userInfo.getRadProfileId() == null){
            	desc.append(radName).append(Language.getLangValue("common_syntax_not_return", Language.getCurrLang(null), null));
            }else{
            	desc.append(radName).append(userInfo.getRadProfileName());
            }
            
            // 邮箱
            if (StrTool.strNotNull(userInfo.getEmail())) {
                String email = comma + Language.getLangValue("common_info_email", Language.getCurrLang(null), null)
                        + colon;
                desc.append(email).append(userInfo.getEmail());
            }
            
            // 手机
            if (StrTool.strNotNull(userInfo.getCellPhone())) {
                String mobile = comma + Language.getLangValue("common_info_mobile", Language.getCurrLang(null), null)
                        + colon;
                desc.append(mobile).append(userInfo.getCellPhone());
            }
        }

        if (object instanceof ArrayList<?>) {
            List<?> utList = (ArrayList<?>) object;
            Iterator<?> iter = utList.iterator();
            while (iter.hasNext()) {
                UserInfo userInfo = (UserInfo) iter.next();
                desc.append(userInfo.getUserId()).append(",");
            }
        }

        // 日志表的描述长度为4000 如果大于4000 则...
        if (desc.toString().length() > 4000) {
            return desc.toString().substring(0, 3996) + "...";
        }

        return desc.toString();
    }
    
    private String descStrStaticPass(MethodInvocation invocation) {
        String desc = "";
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        if (object instanceof UserInfo) {
            UserInfo userInfo = (UserInfo) object;
            String comma = Language.getLangValue("comma", Language.getCurrLang(null), null);
            String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
            String account = Language.getLangValue("user_info_account", Language.getCurrLang(null), null) + colon;

            desc = account + userInfo.getUserId();
            if (userInfo.getLocalAuth() == NumConstant.common_number_0) {
                String vdtkn = comma + Language.getLangValue("user_local_auth_mode", Language.getCurrLang(null), null)
                        + colon + Language.getLangValue("local_auth_only_vd_tkn", Language.getCurrLang(null), null);
                desc = desc + vdtkn;
            } else if (userInfo.getLocalAuth() == NumConstant.common_number_1) {
                String vdpwdtkn = comma
                        + Language.getLangValue("user_local_auth_mode", Language.getCurrLang(null), null) + colon
                        + Language.getLangValue("local_auth_vd_pwd_tkn", Language.getCurrLang(null), null);
                desc = desc + vdpwdtkn;
            } else if (userInfo.getLocalAuth() == NumConstant.common_number_2) {
                String vdpwd = comma + Language.getLangValue("user_local_auth_mode", Language.getCurrLang(null), null)
                        + colon + Language.getLangValue("local_auth_only_vd_pwd", Language.getCurrLang(null), null);

                desc = desc + vdpwd;
            } else if (userInfo.getLocalAuth() == NumConstant.common_number_3) {
                String novd = comma + Language.getLangValue("user_local_auth_mode", Language.getCurrLang(null), null)
                        + colon + Language.getLangValue("local_auth_no_vd", Language.getCurrLang(null), null);

                desc = desc + novd;
            }
        }
        return desc;
    }

    //变更组织机构
    public List<String> getDescListOrgunitNameStr(MethodInvocation invocation) {
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        List<String> descList = new ArrayList<String>();
        UserInfo userInfo = null;
        Iterator<?> iter = null;
        if (object instanceof UserInfo) {
            userInfo = (UserInfo) object;
            List<Object> list = new ArrayList<Object>();
            list.add(userInfo);
            iter = list.iterator();
            while (iter.hasNext()) {
                UserInfo uf = (UserInfo) iter.next();
                descList.add(Language.getLangValue("log_record_changed_user", Language.getCurrLang(null), null)
                        + uf.getUserId()
                        + Language.getLangValue("log_record_curr_org", Language.getCurrLang(null), null)
                        + uf.getCurrentOrgunitName() + Language.getLangValue("comma", Language.getCurrLang(null), null)
                        + Language.getLangValue("log_record_target_org", Language.getCurrLang(null), null)
                        + uf.getTargetOrgunitName());

            }
        }
        return descList;
    }

    //设置radius配置
    public List<String> getDescListRadiusStr(MethodInvocation invocation) {
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        List<String> descList = new ArrayList<String>();
        Iterator<?> iter = null;
        List<?> utList = null;
        if (object instanceof List<?>) {
            utList = (List<?>) object;
            iter = utList.iterator();
            while (iter.hasNext()) {
                UserInfo uf = (UserInfo) iter.next();
                String radName = "";
                if (!StrTool.objNotNull(uf.getRadProfileId())) {
                    uf.setRadProfileId(0);
                }
                if (uf.getRadProfileName() == "") {
                    radName = Language.getLangValue("common_syntax_not_return", Language.getCurrLang(null), null);
                } else {
                    radName = uf.getRadProfileName();
                }
                descList.add(Language.getLangValue("user_return_client_Rad_conf", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null) + radName
                        + Language.getLangValue("user_info_account_1", Language.getCurrLang(null), null)
                        + uf.getUserId());
            }
        }
        return descList;
    }

    //设置后端认证
    public List<String> getDescListBackendStr(MethodInvocation invocation) {
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        List<String> descList = new ArrayList<String>();
        Iterator<?> iter = null;
        List<?> utList = null;
        if (object instanceof List<?>) {
            utList = (List<?>) object;
            iter = utList.iterator();
            while (iter.hasNext()) {
                UserInfo uf = (UserInfo) iter.next();
                String radName = "";
                if (uf.getBackEndAuth() == NumConstant.common_number_0) {
                    radName = Language.getLangValue("backend_auth_default", Language.getCurrLang(null), null);
                } else if (uf.getBackEndAuth() == NumConstant.common_number_1) {
                    radName = Language.getLangValue("backend_auth_need", Language.getCurrLang(null), null);
                } else if (uf.getBackEndAuth() == NumConstant.common_number_2) {
                    radName = Language.getLangValue("backend_auth_no_need", Language.getCurrLang(null), null);
                }
                descList.add(Language.getLangValue("user_whether_backend_auth", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null) + radName
                        + Language.getLangValue("user_info_account_1", Language.getCurrLang(null), null)
                        + uf.getUserId());
            }
        }
        return descList;
    }

    //设置本地认证模式
    public List<String> getDescListLocalStr(MethodInvocation invocation) {
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        List<String> descList = new ArrayList<String>();
        Iterator<?> iter = null;
        List<?> utList = null;
        if (object instanceof List<?>) {
            utList = (List<?>) object;
            iter = utList.iterator();
            while (iter.hasNext()) {
                UserInfo uf = (UserInfo) iter.next();
                String radName = "";
                if (uf.getLocalAuth() == NumConstant.common_number_0) {
                    radName = Language.getLangValue("local_auth_only_vd_tkn", Language.getCurrLang(null), null);
                } else if (uf.getLocalAuth() == NumConstant.common_number_1) {
                    radName = Language.getLangValue("local_auth_vd_pwd_tkn", Language.getCurrLang(null), null);
                } else if (uf.getLocalAuth() == NumConstant.common_number_2) {
                    radName = Language.getLangValue("local_auth_only_vd_pwd", Language.getCurrLang(null), null);
                } else if (uf.getLocalAuth() == NumConstant.common_number_3) {
                    radName = Language.getLangValue("local_auth_no_vd", Language.getCurrLang(null), null);
                }
                descList.add(Language.getLangValue("user_local_auth_mode", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null) + radName
                        + Language.getLangValue("user_info_account_1", Language.getCurrLang(null), null)
                        + uf.getUserId());
            }
        }
        return descList;
    }

    /**
     * 提取用户列表信息
     * @Date in Jun 16, 2011,4:23:54 PM
     * @param invocation
     * @return
     */
    public List<String> getDescList(MethodInvocation invocation) {
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        List<String> descList = new ArrayList<String>();
        List<?> utList = null;
        UserInfo userInfo = null;
        Iterator<?> iter = null;
        if (object instanceof List<?>) {
            utList = (List<?>) object;
            iter = utList.iterator();
            while (iter.hasNext()) {
                userInfo = (UserInfo) iter.next();
                descList.add(Language.getLangValue("user_info_account", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null) + userInfo.getUserId());
            }
        }
        if (object instanceof UserInfo) {
            userInfo = (UserInfo) object;
            List<Object> list = new ArrayList<Object>();
            list.add(userInfo);
            iter = list.iterator();
            while (iter.hasNext()) {
                UserInfo uf = (UserInfo) iter.next();
                descList.add(Language.getLangValue("user_info_account", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null) + uf.getUserId());
            }
        }
        return descList;
    }

    /**
     * 获取用户列表信息
     * @param invocation
     * @return
     */
    private List<String> getDescplist(MethodInvocation invocation) {
        List<String> descpList = new ArrayList<String>();
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        Set<?> keys = null;
        String user = "";
        List<?> userList = null;
        Iterator<?> iter = null;
        if (object instanceof Set<?>) {
            keys = (Set<?>) object;
            iter = keys.iterator();
            while (iter.hasNext()) {
                user = (String) iter.next();
                user = user.split(":")[0];
                descpList.add(Language.getLangValue("user_info_account", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null) + user);
            }
        }
        if (object instanceof List<?>) {
            userList = (List<?>) object;
            iter = userList.iterator();
            UserInfo userInfo = null;
            while (iter.hasNext()) {
                userInfo = (UserInfo) iter.next();
                descpList.add(Language.getLangValue("user_info_account", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null) + userInfo.getUserId());
            }
        }
        return descpList;
    }
}
