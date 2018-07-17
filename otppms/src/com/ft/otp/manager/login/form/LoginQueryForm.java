/**
 *Administrator
 */
package com.ft.otp.manager.login.form;

import com.ft.otp.base.form.BaseQueryForm;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理员帐户查询Form
 *
 * @Date in Apr 17, 2011,4:23:30 PM
 *
 * @author TBM
 */
public class LoginQueryForm extends BaseQueryForm {

    private String userId; //用户名
    private String password; //密码
    private String checkCode; //验证码
    private String pin; //应急口令或动态口令
    private String oldPin;//上一次认证需要同步的pin
    private int localAuth; //本地登录方式 0 静态密码 、1 OTP 、2 静态密码+OTP

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        int exchangeTag = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_COMMON,
                ConfConstant.USERID_FORMAT_TYPE));
        // 小写转换
        if (StrTool.strNotNull(userId) && exchangeTag == NumConstant.common_number_1) {
            userId = userId.toLowerCase();
        }

        this.userId = StrTool.cleanXSS(userId);
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = StrTool.cleanXSS(password);
    }

    /**
     * @return the checkCode
     */
    public String getCheckCode() {
        return checkCode;
    }

    /**
     * @param checkCode the checkCode to set
     */
    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    /**
     * @return the pin
     */
    public String getPin() {
        return pin;
    }

    /**
     * @param pin the pin to set
     */
    public void setPin(String pin) {
        this.pin = pin;
    }

    /**
     * @return the localAuth
     */
    public int getLocalAuth() {
        return localAuth;
    }

    /**
     * @param localAuth the localAuth to set
     */
    public void setLocalAuth(int localAuth) {
        this.localAuth = localAuth;
    }

    /**
     * @return the oldPin
     */
    public String getOldPin() {
        return oldPin;
    }

    /**
     * @param oldPin the oldPin to set
     */
    public void setOldPin(String oldPin) {
        this.oldPin = oldPin;
    }

}
