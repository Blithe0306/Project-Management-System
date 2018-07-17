/**
 *Administrator
 */
package com.ft.otp.manager.user_token.form;

import com.ft.otp.base.form.BaseQueryForm;
import com.ft.otp.manager.user_token.entity.UserToken;

/**
 * 用户与令牌对应关系查询Form
 *
 * @Date in Apr 26, 2011,3:33:58 PM
 *
 * @author TBM
 */
public class UTokenQueryForm extends BaseQueryForm {

    private UserToken userToken = new UserToken();

    private String userId = "";
    private String token = "";
    private Integer domainId = null;

    /**
     * @return the userToken
     */
    public UserToken getUserToken() {
        return userToken;
    }

    /**
     * @param userToken the userToken to set
     */
    public void setUserToken(UserToken userToken) {
        this.userToken = userToken;
    }

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
        userToken.setUserId(userId);
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        userToken.setToken(token);
    }

    /**
     * @return the domainId
     */
    public Integer getDomainId() {
        return domainId;
    }

    /**
     * @param domainId the domainId to set
     */
    public void setDomainId(Integer domainId) {
        this.domainId = domainId;
    }

}
