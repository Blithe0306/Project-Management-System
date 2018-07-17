/**
 *Administrator
 */
package com.ft.otp.manager.token.action;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.common.Constant;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.soap.LangResultCode;
import com.ft.otp.common.soap.MessageBean;
import com.ft.otp.common.soap.code.SoapAttribute;
import com.ft.otp.common.soap.help.AuthHelper;
import com.ft.otp.common.soap.help.SyncHelper;
import com.ft.otp.util.tool.StrTool;

/**
 * 动态口令认证、同步业务处理Action
 *
 * @Date in May 22, 2013,7:25:12 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class AuthAction extends BaseAction {

    private AuthHelper authHelper = new AuthHelper();
    private SyncHelper syncHelper = new SyncHelper();
    private MessageBean messageBean = null;

    public MessageBean getMessageBean() {
        if (null == messageBean) {
            messageBean = new MessageBean();
        }

        return messageBean;
    }

    public void setMessageBean(MessageBean messageBean) {
        this.messageBean = messageBean;
    }

    private static final long serialVersionUID = 4623755349328101279L;

    /**
     * 令牌认证测试
     * @Date in Jun 23, 2011,2:04:34 PM
     * @return
     */
    public String tokenAuth() {
        if (null == messageBean) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_auth_request_param_is_null"));
            return null;
        }

        String message = Language.getLangStr(request, "tkn_auth_success");
        //口令认证测试
        int[] result = authHelper.authTest(messageBean);
        if (result[0] == SoapAttribute.OTP_SUCCESS) {
            outPutOperResult(Constant.alert_succ, message);
        } else {
            message = Language.getLangStr(request, "tkn_auth_error") + LangResultCode.getLangStr(request, result[0]);
            if (result[1] > 0) {
                message += Language.getLangStr(request, "tkn_auth_surplus_retry_num") + result[1];
            }
            outPutOperResult(Constant.alert_error, message);
        }

        return null;
    }

    /**
     * 令牌同步
     * @Date in Jun 23, 2011,2:05:24 PM
     * @return
     */
    public String tokenSync() {
        if (null == messageBean) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_auth_request_param_is_null"));
            return null;
        }

        int result = syncHelper.syncToken(messageBean);
        String message = Language.getLangStr(request, "tkn_sync_success");
        if (result == SoapAttribute.OTP_SUCCESS) {
            outPutOperResult(Constant.alert_succ, message);
        } else {
            message = Language.getLangStr(request, "tkn_sync_error") + LangResultCode.getLangStr(request, result);
            outPutOperResult(Constant.alert_error, message);
        }

        return null;
    }

    /**
     * 获取挑战值
     * @Date in Jun 23, 2011,2:05:55 PM
     * @return
     */
    public String requestQS() {
        if (null == messageBean) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_auth_request_param_is_null"));
            return null;
        }

        String[] resultArr = authHelper.getChallengeCode(messageBean);
        String message = null;
        int resultCode = StrTool.parseInt(resultArr[0]);
        if (resultCode == SoapAttribute.OTP_SUCCESS) {
            message = resultArr[1];
            outPutOperResult(Constant.alert_succ, message);
        } else {
            message = Language.getLangStr(request, "tkn_get_challenge_code_error")
                    + LangResultCode.getLangStr(request, resultCode);
            outPutOperResult(Constant.alert_error, message);
        }

        return null;
    }

    /**
     * 验证应答码
     * @Date in Jun 23, 2011,2:12:16 PM
     * @return
     */
    public String verifyCR() {
        if (null == messageBean) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_auth_request_param_is_null"));
            return null;
        }

        int[] result = authHelper.verifyResponseCode(messageBean);
        String message = Language.getLangStr(request, "tkn_vd_response_code_success");
        if (result[0] == SoapAttribute.OTP_SUCCESS) {
            outPutOperResult(Constant.alert_succ, message);
        } else {
            message = Language.getLangStr(request, "tkn_vd_response_code_error")
                    + LangResultCode.getLangStr(request, result[0]);
            if (result[1] > 0) {
                message += Language.getLangStr(request, "tkn_auth_surplus_retry_num") + result[1];
            }
            outPutOperResult(Constant.alert_error, message);
        }

        return null;
    }

    /**
     * 获取一级解锁码
     */
    public String genPUK1() {
        if (null == messageBean) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_auth_request_param_is_null"));
            return null;
        }

        String[] resultArr = authHelper.genPUK1(messageBean);
        String message = null;
        int resultCode = StrTool.parseInt(resultArr[0]);
        if (resultCode == SoapAttribute.OTP_SUCCESS) {
            message = resultArr[1];
            outPutOperResult(Constant.alert_succ, message);
        } else {
            message = Language.getLangStr(request, "tkn_get_puk1_faild");
            outPutOperResult(Constant.alert_error, message);
        }

        return null;
    }

    /**
     * 获取二级解锁码
     */
    public String genPUK2() {
        if (null == messageBean) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_auth_request_param_is_null"));
            return null;
        }

        String[] resultArr = authHelper.genPUK2(messageBean);
        String message = null;
        int resultCode = StrTool.parseInt(resultArr[0]);
        if (resultCode == SoapAttribute.OTP_SUCCESS) {
            message = resultArr[1];
            outPutOperResult(Constant.alert_succ, message);
        } else {
            message = Language.getLangStr(request, "tkn_get_puk2_faild");
            outPutOperResult(Constant.alert_error, message);
        }

        return null;
    }

    /**
     * 获取挑战应答令牌激活码
     *
     * @Date in Jun 4, 2013,2:59:27 PM
     * @return
     */
    public String genAC() {
        if (null == messageBean) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_auth_request_param_is_null"));
            return null;
        }

        String[] resultArr = authHelper.genAC(messageBean);
        String message = null;
        int resultCode = StrTool.parseInt(resultArr[0]);
        if (resultCode == SoapAttribute.OTP_SUCCESS) {
            message = resultArr[1];
            outPutOperResult(Constant.alert_succ, message);
        } else {
            message = Language.getLangStr(request, "tkn_get_ac_faild");
            outPutOperResult(Constant.alert_error, message);
        }

        return null;
    }

}
