/**
 *Administrator
 */
package com.ft.otp.manager.user.userinfo.form;

import com.ft.otp.base.form.BaseQueryForm;

/**
 * 用户查询Form
 *
 * @Date in Apr 20, 2011,10:54:55 AM
 *
 * @author TBM
 */
public class UInfoQueryForm extends BaseQueryForm {


    private String dOrgunitId = ""; // domainId:orgunitId
    private String userId = null; //用户账号
    private String realName = null;      //真实姓名 
    private String token=""; //令牌号
    private int orgFlag = 0;
    private int enabled = -1;
    private int locked = -1;
    private int backEndAuth = -1;
    private int localAuth = -1;
    private int bindState = 0;
    
    public int getBindState() {
		return bindState;
	}
	public void setBindState(int bindState) {
		this.bindState = bindState;
	}
	public int getBackEndAuth() {
		return backEndAuth;
	}
	public void setBackEndAuth(int backEndAuth) {
		this.backEndAuth = backEndAuth;
	}
	public int getLocalAuth() {
		return localAuth;
	}
	public void setLocalAuth(int localAuth) {
		this.localAuth = localAuth;
	}
	public int getEnabled() {
		return enabled;
	}
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	public int getLocked() {
		return locked;
	}
	public void setLocked(int locked) {
		this.locked = locked;
	}
	public UInfoQueryForm(){
    	
    }
	public int getOrgFlag() {
		return orgFlag;
	}

	public void setOrgFlag(int orgFlag) {
		this.orgFlag = orgFlag;
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
		this.token = token;
	}

}
