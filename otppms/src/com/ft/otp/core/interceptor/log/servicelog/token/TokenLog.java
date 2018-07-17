/**
 *Administrator
 */
package com.ft.otp.core.interceptor.log.servicelog.token;

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
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.orgunit.orgunit.entity.OrgunitInfo;
import com.ft.otp.manager.orgunit.orgunit.service.IOrgunitInfoServ;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 令牌日志操作记录类功能说明
 *
 * @Date in Jun 14, 2011,1:08:50 PM
 *
 * @author ZJY
 */
public class TokenLog {
    private LogCommonObj commonObj = new LogCommonObj();
    private IOrgunitInfoServ orgunitInfoServ = (IOrgunitInfoServ) AppContextMgr.getObject("orgunitInfoServ");

    /**
     * 记录令牌操作日志
     * 
     */
    public synchronized boolean addTokenLog(MethodInvocation invocation, String method) throws BaseException {
        int result = 0;
        boolean isOper = false;
        int acid = 0;
        int acobj = 0;
        String desc = "";
        List<String> descList = null;

        // 针对令牌操作，操作下拉列表
        if (StrTool.strEquals(method, AdmLogConstant.method_updateTokenState)) {
            result = commonObj.operResult(invocation);
            Object[] parameters = invocation.getArguments();
            Object object0 = parameters[0];
            TokenInfo tokenInfo = null;
            if (object0 instanceof TokenInfo) {
                tokenInfo = (TokenInfo) object0;
                if (tokenInfo.getEnabled() > -1) {
                    if (tokenInfo.getEnabled() == NumConstant.common_number_0) {
                        acid = AdmLogConstant.log_aid_disable; // 停用
                    } else {
                        acid = AdmLogConstant.log_aid_enable; // 启用
                    }
                } else if (tokenInfo.getLocked() > -1) {
                    if (tokenInfo.getLocked() == NumConstant.common_number_0) {
                        acid = AdmLogConstant.log_aid_unlock; // 解锁
                    } else {
                        acid = AdmLogConstant.log_aid_lock; // 锁定
                    }
                } else if (tokenInfo.getLost() > -1) {
                    if (tokenInfo.getLost() == NumConstant.common_number_0) {
                        acid = AdmLogConstant.log_aid_unlost; // 解挂
                    } else {
                        acid = AdmLogConstant.log_aid_lost; // 挂失
                    }
                } else if (tokenInfo.getLogout() > -1) {
                    acid = AdmLogConstant.log_aid_logout; // 作废
                }
            } else {
                Object object1 = parameters[1];
                int mark = (Integer) object1;
                if (mark == NumConstant.common_number_0) {
                    acid = AdmLogConstant.log_aid_enable; // 启用
                } else if (mark == NumConstant.common_number_1) {
                    acid = AdmLogConstant.log_aid_disable; // 停用
                } else if (mark == NumConstant.common_number_2) {
                    acid = AdmLogConstant.log_aid_lock; // 锁定
                } else if (mark == NumConstant.common_number_3) {
                    acid = AdmLogConstant.log_aid_unlock; // 解锁
                } else if (mark == NumConstant.common_number_4) {
                    acid = AdmLogConstant.log_aid_lost; // 挂失
                } else if (mark == NumConstant.common_number_5) {
                    acid = AdmLogConstant.log_aid_unlost; // 解挂
                } else if (mark == NumConstant.common_number_6) {
                    acid = AdmLogConstant.log_aid_logout; // 作废
                } else if (mark == NumConstant.common_number_8) {
                    acid = AdmLogConstant.log_aid_del; // 删除
                } else if (mark == NumConstant.common_number_9) {
                    acid = AdmLogConstant.log_aid_cancel; // 取消分配
                }
            }
            isOper = true;
            acobj = AdmLogConstant.log_obj_tkn;
            descList = getDescListTokStateStr(invocation);
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
            return false;
        }

        // 取消分配，操作下拉列表； 令牌分配；
        if (StrTool.strEquals(method, AdmLogConstant.method_updateTokenOrg)) {
            result = commonObj.operResult(invocation);
            Object[] parameters = invocation.getArguments();
            Object object0 = parameters[0];
            TokenInfo tokenInfo = null;
            if (object0 instanceof TokenInfo) {
                tokenInfo = (TokenInfo) object0;
                if (tokenInfo.getLogFlag() == NumConstant.common_number_1) { // 区分是否记录日志
                    acid = AdmLogConstant.log_aid_cancel;
                    isOper = true;
                    acobj = AdmLogConstant.log_obj_tkn;
                    descList = getDescListTokStateStr(invocation);
                    commonObj.addAdminLog(acid, acobj, desc, descList, result);
                }
            } else { // 令牌分配
                acid = AdmLogConstant.log_aid_assign;
                isOper = true;
                acobj = AdmLogConstant.log_obj_tkn;
                descList = getDescListAsTknkStr(invocation);
                commonObj.addAdminLog(acid, acobj, desc, descList, result);
            }
            return false;
        }

        // 设置应急口令
        if (StrTool.strEquals(method, AdmLogConstant.method_updateObj)) {
            result = commonObj.operResult(invocation);
            acid = AdmLogConstant.log_aid_set;
            isOper = true;
            acobj = AdmLogConstant.log_obj_mergency;
            descList = getDescListMergencyStr(invocation);
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
            return false;
        }

        //编辑令牌（启用、停用）
        if (StrTool.strEquals(method, AdmLogConstant.method_updateTokenEnable)) {
            result = commonObj.operResult(invocation);
            Object[] parameters = invocation.getArguments();
            Object object0 = parameters[0];
            Object object1 = parameters[1];
            Set<?> keys = null;
            int mark = (Integer) object1;
            if (mark == NumConstant.common_number_1) {
                if (object0 instanceof Set<?>) {
                    keys = (Set<?>) object0;
                    if (keys.size() > 1) {
                        acid = AdmLogConstant.log_aid_b_enable;
                    } else {
                        acid = AdmLogConstant.log_aid_enable;
                    }
                }
            } else {
                if (object0 instanceof Set<?>) {
                    keys = (Set<?>) object0;
                    if (keys.size() > 1) {
                        acid = AdmLogConstant.log_aid_b_disable;
                    } else {
                        acid = AdmLogConstant.log_aid_disable;
                    }
                }
            }

            isOper = true;
            acobj = AdmLogConstant.log_obj_tkn;
            descList = getDescplist(invocation);
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
            return false;
        }
        //锁定、解锁
        if (StrTool.strEquals(method, AdmLogConstant.method_updateTokenLocked)) {
            result = commonObj.operResult(invocation);
            Object[] parameters = invocation.getArguments();
            Object object0 = parameters[0];
            Object object1 = parameters[1];
            Set<?> keys = null;
            int mark = (Integer) object1;
            if (mark == 1 || mark == 2) {
                if (object0 instanceof Set<?>) {
                    keys = (Set<?>) object0;
                    if (keys.size() > 1) {
                        acid = AdmLogConstant.log_aid_b_lock;
                    } else {
                        acid = AdmLogConstant.log_aid_lock;
                    }
                }
            } else {
                if (object0 instanceof Set<?>) {
                    keys = (Set<?>) object0;
                    if (keys.size() > 1) {
                        acid = AdmLogConstant.log_aid_b_unlock;
                    } else {
                        acid = AdmLogConstant.log_aid_unlock;
                    }
                }
            }

            isOper = true;
            acobj = AdmLogConstant.log_obj_tkn;
            descList = getDescplist(invocation);
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
            return false;
        }
        //挂失、解挂
        if (StrTool.strEquals(method, AdmLogConstant.method_updateTokenLost)) {
            result = commonObj.operResult(invocation);
            Object[] parameters = invocation.getArguments();
            Object object0 = parameters[0];
            Object object1 = parameters[1];
            Set<?> keys = null;
            int mark = (Integer) object1;
            if (mark == NumConstant.common_number_1) {
                if (object0 instanceof Set<?>) {
                    keys = (Set<?>) object0;
                    if (keys.size() > 1) {
                        acid = AdmLogConstant.log_aid_b_lost;
                    } else {
                        acid = AdmLogConstant.log_aid_lost;
                    }
                }
            } else {
                if (object0 instanceof Set<?>) {
                    keys = (Set<?>) object0;
                    if (keys.size() > 1) {
                        acid = AdmLogConstant.log_aid_b_unlost;
                    } else {
                        acid = AdmLogConstant.log_aid_unlost;

                    }
                }
            }

            isOper = true;
            acobj = AdmLogConstant.log_obj_tkn;
            descList = getDescplist(invocation);
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
            return false;
        }
        //注销
        if (StrTool.strEquals(method, AdmLogConstant.method_updateTokenLogout)) {
            result = commonObj.operResult(invocation);
            Object[] parameters = invocation.getArguments();
            Object object0 = parameters[0];
            Object object1 = parameters[1];
            Set<?> keys = null;
            int mark = (Integer) object1;
            if (mark == NumConstant.common_number_1) {
                if (object0 instanceof Set<?>) {
                    keys = (Set<?>) object0;
                    if (keys.size() > 1) {
                        acid = AdmLogConstant.log_aid_b_logout;
                    } else {
                        acid = AdmLogConstant.log_aid_logout;
                    }
                }
            }

            isOper = true;
            acobj = AdmLogConstant.log_obj_tkn;
            descList = getDescplist(invocation);
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
            return false;
        }
        //删除令牌
        if (StrTool.strEquals(method, AdmLogConstant.method_delete)) {
            result = commonObj.operResult(invocation);
            acid = AdmLogConstant.log_aid_del;

            isOper = true;
            acobj = AdmLogConstant.log_obj_tkn;
            descList = getDescplist(invocation);
        }

        //取消分配
        if (StrTool.strEquals(method, AdmLogConstant.method_updateTokenCancel)) {
            result = commonObj.operResult(invocation);
            acid = AdmLogConstant.log_aid_cancel;

            isOper = true;
            acobj = AdmLogConstant.log_obj_tkn;
            descList = getDescplist(invocation);
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
            return false;
        }
        //软件令牌分发
        if (StrTool.strEquals(method, AdmLogConstant.method_updateSoftTkn)) {
            result = commonObj.operResult(invocation);
            acid = AdmLogConstant.log_aid_dist;

            isOper = true;
            acobj = AdmLogConstant.log_obj_soft_tkn;
            desc = Language.getLangValue("log_record_dist_token", Language.getCurrLang(null), null)
                    + descStr(invocation);
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
        if (object instanceof TokenInfo) {
            TokenInfo tokenInfo = (TokenInfo) object;
            desc = tokenInfo.getToken();
        }
        return desc;
    }

    /**
    * 获取令牌列表信息
    */

    private List<String> getDescplist(MethodInvocation invocation) {
        List<String> descpList = new ArrayList<String>();
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        Set<?> keys = null;
        String token = "";
        List<?> tokenList = null;
        Iterator<?> iter = null;
        if (object instanceof Set<?>) {
            keys = (Set<?>) object;
            iter = keys.iterator();
            while (iter.hasNext()) {
                token = (String) iter.next();
                descpList.add(Language.getLangValue("tkn_comm_tknum", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null) +token);
            }
        }
        if (object instanceof List<?>) {
            tokenList = (List<?>) object;
            iter = tokenList.iterator();
            TokenInfo tokenInfo = null;
            while (iter.hasNext()) {
                tokenInfo = (TokenInfo) iter.next();
                descpList.add(Language.getLangValue("tkn_comm_tknum", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null) +tokenInfo.getToken());
            }
        }
        return descpList;
    }

    /**
     * 令牌状态
     * @param invocation
     * @return
     */
    private List<String> getDescListTokStateStr(MethodInvocation invocation) {
        List<String> descpList = new ArrayList<String>();
        Object[] parameters = invocation.getArguments();
        List<?> tokenList = null;
        Iterator<?> iter = null;
        Object object = parameters[0];
        if (object instanceof TokenInfo) {
            TokenInfo tokenInfo = (TokenInfo) object;
            String[] tokens = tokenInfo.getBatchIds();
            String token = "";
            for (int i = 0; i < tokens.length; i++) {
                token += tokens[i] + ",";
            }
            if (StrTool.strNotNull(token)) {
                token = token.substring(0, token.length() - 1);
            }
            descpList.add(Language.getLangValue("tkn_comm_tknum", Language.getCurrLang(null), null)
                    + Language.getLangValue("colon", Language.getCurrLang(null), null) + token);
        } else if (object instanceof List<?>) {
            tokenList = (List<?>) object;
            iter = tokenList.iterator();
            TokenInfo tokenInfo = null;
            while (iter.hasNext()) {
                tokenInfo = (TokenInfo) iter.next();
                descpList.add(Language.getLangValue("tkn_comm_tknum", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null) + tokenInfo.getToken());
            }
        }
        return descpList;
    }

    /**
     * 应急口令
     * @param invocation
     * @return
     */
    private List<String> getDescListMergencyStr(MethodInvocation invocation) {
        List<String> descpList = new ArrayList<String>();
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        if (object instanceof TokenInfo) {
            TokenInfo tokenInfo = (TokenInfo) object;
            String autName = "";
            if (tokenInfo.getAuthmethod() == NumConstant.common_number_1) {
                autName = Language.getLangValue("tkn_emerg_pwd_auth", Language.getCurrLang(null), null);
            } else {
                autName = Language.getLangValue("tkn_dynamic_pwd_auth", Language.getCurrLang(null), null);
            }
            descpList.add(Language.getLangValue("tkn_auth_method", Language.getCurrLang(null), null)
                    + Language.getLangValue("colon", Language.getCurrLang(null), null) + autName
                    + Language.getLangValue("comma", Language.getCurrLang(null), null)
                    + Language.getLangValue("tkn_comm_tknum", Language.getCurrLang(null), null)
                    + Language.getLangValue("colon", Language.getCurrLang(null), null) + tokenInfo.getToken());
        }
        return descpList;
    }

    private List<String> getDescListAsTknkStr(MethodInvocation invocation) throws BaseException {
        List<String> descpList = new ArrayList<String>();
        Object[] parameters = invocation.getArguments();
        List<?> tokenList = null;
        Iterator<?> iter = null;
        Object object = parameters[0];
        if (object instanceof List<?>) {
            tokenList = (List<?>) object;
            iter = tokenList.iterator();
            TokenInfo tokenInfo = null;
            OrgunitInfo orgunitInfo = new OrgunitInfo();
            while (iter.hasNext()) {
                tokenInfo = (TokenInfo) iter.next();
                orgunitInfo.setOrgunitId(tokenInfo.getOrgunitid());
                OrgunitInfo orgInfo = (OrgunitInfo) orgunitInfoServ.find(orgunitInfo);

                descpList.add(Language.getLangValue("tkn_comm_tknum", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null) + tokenInfo.getToken()
                        + Language.getLangValue("comma", Language.getCurrLang(null), null)
                        + Language.getLangValue("tkn_allot_org_one", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null)
                        + tokenInfo.getOldOrgunitName()
                        + Language.getLangValue("comma", Language.getCurrLang(null), null)
                        + Language.getLangValue("tkn_allot_org_target", Language.getCurrLang(null), null)
                        + Language.getLangValue("colon", Language.getCurrLang(null), null) + orgInfo.getOrgunitName());
            }
        }
        return descpList;
    }

}
