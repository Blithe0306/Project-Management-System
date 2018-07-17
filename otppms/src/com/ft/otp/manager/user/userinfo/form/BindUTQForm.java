/**
 *Administrator
 */
package com.ft.otp.manager.user.userinfo.form;

import com.ft.otp.base.form.BaseQueryForm;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 绑定用户令牌查询Form
 *
 * @Date in May 5, 2011,4:34:14 PM
 *
 * @author TBM
 */
public class BindUTQForm extends BaseQueryForm {

    private UserInfo userInfo = new UserInfo();// 用户
    private TokenInfo tokenInfo = new TokenInfo();// 令牌
    private AdminUser adminInfo = new AdminUser();// 管理员

    private String userId = null;
    private String realName = null;
    private String dOrgunitId = ""; // domainId:orgunitId
    //域ID
    private int domainId = 0;
    //绑定类型
    private int bindType = 0;
    //产品类型(c100,c200,c300,c400,...)
    private int producttype = 0;

    //物理类型(0 硬件令牌，1 手机令牌，2 软件令牌,3 国密令牌(注:非物理类型，已废除。)
    //        4 刮刮卡令牌,5 矩阵卡令牌,6 短信令牌,7 SD卡令牌)
    private int physicaltype = 0;

    /**
     * 令牌状态
     */
    //启用状态（所有状态、启用、未启用）
    private int enableState = 0;

    //绑定状态（所有状态、绑定、未绑定）
    private int bindState = 0;
    
    //用户与令牌绑定状态（所有状态、绑定、未绑定）
    private int usbindState = 0;
    
    //锁定状态（所有状态、锁定、未锁定）
    private int lockedState = 0;

    //挂失状态（所有状态、挂失、未挂失）
    private int lostState = 0;

    //注销状态（所有状态、注销、未注销）
    private int logoutState = 0;

	public int getUsbindState() {
		return usbindState;
	}

	public void setUsbindState(int usbindState) {
		this.usbindState = usbindState;
		userInfo.setUsbind(usbindState);
	}

	public int getDomainId() {
		return domainId;
	}

	public void setDomainId(int domainId) {
		this.domainId = domainId;
	}

	/**
     * @return the userInfo
     */
    public UserInfo getUserInfo() {
        return userInfo;
    }

    /**
     * @param userInfo the userInfo to set
     */
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * @return the realName
     */
    public String getRealName() {
        return realName;
    }

    /**
     * @param realName the realName to set
     */
    public void setRealName(String realName) {
        this.realName = realName;
        userInfo.setRealName(StrTool.trim(realName));
    }

    /**
     * @return the tokenInfo
     */
    public TokenInfo getTokenInfo() {
        return tokenInfo;
    }

    /**
     * @param tokenInfo the tokenInfo to set
     */
    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
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
        this.userId = userId;
        userInfo.setUserId(StrTool.trim(userId));
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        tokenInfo.setToken(token);
    }
    
    /**
     * @param tokenStart the tokenStart to set
     */
    public void setTokenStart(String tokenStart) {
        tokenInfo.setTokenStart(tokenStart);
    }
    
    /**
     * @param tokenEnd the tokenEnd to set
     */
    public void setTokenEnd(String tokenEnd) {
        tokenInfo.setTokenEnd(tokenEnd);
    }

    /**
     * @return the bindType
     */
    public int getBindType() {
        return bindType;
    }

    /**
     * @param bindType the bindType to set
     */
    public void setBindType(int bindType) {
        this.bindType = bindType;
    }

    /**
     * @return the producttype
     */
    public int getProducttype() {
        return producttype;
    }

    /**
     * @param producttype the producttype to set
     */
    public void setProducttype(int producttype) {
        this.producttype = producttype;
        tokenInfo.setProducttype(producttype);
    }

    /**
     * @return the physicaltype
     */
    public int getPhysicaltype() {
        return physicaltype;
    }

    /**
     * @param physicaltype the physicaltype to set
     */
    public void setPhysicaltype(int physicaltype) {
        this.physicaltype = physicaltype;
        tokenInfo.setPhysicaltype(physicaltype);
    }

    /**
     * @return the enableState
     */
    public int getEnableState() {
        return enableState;
    }

    /**
     * @param enableState the enableState to set
     */
    public void setEnableState(int enableState) {
        this.enableState = enableState;
        tokenInfo.setEnabled(enableState);
    }

    /**
     * @return the bindState
     */
    public int getBindState() {
        return bindState;
    }

    /**
     * @param bindState the bindState to set
     */
    public void setBindState(int bindState) {
        this.bindState = bindState;
        tokenInfo.setBind(bindState);
    }

    /**
     * @return the lockedState
     */
    public int getLockedState() {
        return lockedState;
    }

    /**
     * @param lockedState the lockedState to set
     */
    public void setLockedState(int lockedState) {
        this.lockedState = lockedState;
        tokenInfo.setLocked(lockedState);
    }

    /**
     * @return the lostState
     */
    public int getLostState() {
        return lostState;
    }

    /**
     * @param lostState the lostState to set
     */
    public void setLostState(int lostState) {
        tokenInfo.setLost(lostState);
        this.lostState = lostState;
    }

    /**
     * @return the logoutState
     */
    public int getLogoutState() {
        return logoutState;
    }

    /**
     * @param logoutState the logoutState to set
     */
    public void setLogoutState(int logoutState) {

        this.logoutState = logoutState;
        tokenInfo.setLogout(logoutState);
    }

    /**
     * @return the dOrgunitId
     */
    public String getDOrgunitId() {
        return dOrgunitId;
    }

    /**
     * @param orgunitId the dOrgunitId to set
     */
    public void setDOrgunitId(String orgunitId) {
        dOrgunitId = orgunitId;
    }

    /**
     * @return the adminInfo
     */
    public AdminUser getAdminInfo() {
        return adminInfo;
    }

    /**
     * @param adminInfo the adminInfo to set
     */
    public void setAdminInfo(AdminUser adminInfo) {
        this.adminInfo = adminInfo;
    }

}
