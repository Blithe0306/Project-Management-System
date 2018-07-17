/**
 *Administrator
 */
package com.ft.otp.core.interceptor.log.servicelog.usertoken;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.manager.user_token.entity.UserToken;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户令牌对应模块日志集中记录类
 *
 * @Date in Jun 11, 2011,11:13:50 AM
 *
 * @author TBM
 */
public class UserTokenLog {

    private LogCommonObj commonObj = new LogCommonObj();

    /**
     * 记录用户令牌对应操作日志
     * @Date in Jun 11, 2011,11:15:41 AM
     * @param invocation
     * @param method
     * @param linkUser
     * @return
     * @throws BaseException
     */
    public synchronized boolean addUserTokenLog(MethodInvocation invocation, String method) throws BaseException {
        int result = 0;
        boolean isOper = false;
        int acid = 0;
        int acobj = 0;
        String desc = "";
        List<String> descList = null;

        //绑定令牌、添加用户时给用户分配令牌
        if (StrTool.strEquals(method, AdmLogConstant.method_addUsrTkn)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_bind;
            //          acobj = AdmLogConstant.log_obj_user_token;
            descList = getDescList(invocation);
        }
        //解绑令牌
        if (StrTool.strEquals(method, AdmLogConstant.method_delete)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_unbind;
            //          acobj = AdmLogConstant.log_obj_user_token;
            descList = getDescList(invocation);
        }
        //批量解绑令牌
        if (StrTool.strEquals(method, AdmLogConstant.method_batchUnBindUT)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_unbind;
            //          acobj = AdmLogConstant.log_obj_user_token;
            descList = getDescList(invocation);
        }

        if (isOper) {
            commonObj.addAdminLog(acid, acobj, desc, descList, result);
        }
        return isOper;
    }

    /**
     * 绑定令牌时用户令牌对应列表
     * @Date in Jun 11, 2011,11:29:57 AM
     * @param invocation
     * @return
     */
    public List<String> getDescList(MethodInvocation invocation) {
        List<String> descList = new ArrayList<String>();
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        UserToken userToken = null;
        String userId = "";
        String token = "";
        List<?> utList = null;
        Iterator<?> iter = null;
        String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
        String relation = Language.getLangValue("log_record_user_tkn_relation", Language.getCurrLang(null), null);
        if (object instanceof List<?>) {
            utList = (List<?>) object;
            iter = utList.iterator();
            while (iter.hasNext()) {
                userToken = (UserToken) iter.next();
                userId = userToken.getUserId();
                token = userToken.getToken();
                if(!StrTool.objNotNull(userToken.getDomainId())){
                	relation = Language.getLangValue("log_record_admin_tkn_relation", Language.getCurrLang(null), null);
                }
                descList.add(relation + "==>" + userId + colon + token);
            }
        } else if (object instanceof UserToken) {
            userToken = (UserToken) object;
            if(userToken.getDomainId() == -1){
            	relation = Language.getLangValue("log_record_admin_tkn_relation", Language.getCurrLang(null), null);
            }
            utList = userToken.getTokenIds();
            iter = utList.iterator();
            while (iter.hasNext()) {
                token = (String) iter.next();
                descList.add(relation + "==>" + userToken.getUserId() + colon + token);
            }
        }

        return descList;
    }

}
