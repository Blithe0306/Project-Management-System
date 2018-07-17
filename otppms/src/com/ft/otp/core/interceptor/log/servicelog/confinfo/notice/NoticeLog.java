/**
 *Administrator
 */
package com.ft.otp.core.interceptor.log.servicelog.confinfo.notice;

import java.util.List;

import org.aopalliance.intercept.MethodInvocation;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.config.PubConfConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.manager.confinfo.portal.entity.ProNoticeInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 通知记录日志
 *
 * @Date in May 9, 2013,5:02:12 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class NoticeLog {

    private LogCommonObj commonObj = new LogCommonObj();

    public synchronized boolean addNoticeLog(MethodInvocation invocation, String method)
            throws BaseException {
        int result = 0;
        boolean isOper = false;
        int acid = 0;
        int acobj = 0;
        String desc = "";
        List<String> descList = null;

        //添加通知公告
        if (StrTool.strEquals(method, AdmLogConstant.method_add)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_add;
            acobj = AdmLogConstant.log_obj_conf_notice;
            desc = descStr(invocation);
        }

        //修改通知公告
        if (StrTool.strEquals(method, AdmLogConstant.method_update)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_edit;
            acobj = AdmLogConstant.log_obj_conf_notice;
            desc = descStr(invocation);
        }

        //删除通知公告
        if (StrTool.strEquals(method, AdmLogConstant.method_delete)) {
            result = commonObj.operResult(invocation);

            isOper = true;
            acid = AdmLogConstant.log_aid_del;
            acobj = AdmLogConstant.log_obj_conf_notice;
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
        if (object instanceof ProNoticeInfo) {
            ProNoticeInfo notice = (ProNoticeInfo) object;
            String comma = Language.getLangValue("comma", Language.getCurrLang(null), null);
            String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
            String title = Language.getLangValue("notice_title_text", Language.getCurrLang(null), null) + colon;
            String deathtime = comma + Language.getLangValue("notice_death_time", Language.getCurrLang(null), null)
                    + colon;
            String body = comma + Language.getLangValue("notice_content", Language.getCurrLang(null), null) + colon;

            desc = title + notice.getTitle() + deathtime + notice.getTempDeathTime() + body + notice.getContent();
        }
        return desc;
    }

    public String getKeyId(MethodInvocation invocation) {
        Object[] parameters = invocation.getArguments();
        Object object = parameters[0];
        StringBuilder sBuilder = new StringBuilder();
        if (object instanceof ProNoticeInfo) {
            ProNoticeInfo notice = (ProNoticeInfo) object;
            int ids[] = notice.getBatchIdsInt();
            for (int i = 0; i < ids.length; i++) {
                sBuilder.append(PubConfConfig.getNoticeTitle(ids[i])).append(",");
            }
        }
        String keyId = sBuilder.toString();
        if (keyId.endsWith(",")) {
            keyId = keyId.substring(0, keyId.length() - 1);
        }
        return keyId;
    }
}
