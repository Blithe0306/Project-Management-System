/**
 *Administrator
 */
package com.ft.otp.manager.user.userinfo.entity;

import java.util.List;
import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.config.PubConfConfig;
import com.ft.otp.manager.orgunit.domain.entity.DomainInfo;
import com.ft.otp.manager.orgunit.orgunit.entity.OrgunitInfo;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户实体类
 *
 * @Date in Apr 20, 2013,17:00:02 AM
 * @author BYL
 * 
 */
public class UserInfo extends BaseEntity {

    private String userId;
    private String realName = "";
    
    /*
     *本地认证模式
     * 0只验证OTP
     * 1验证静态密码和OTP
     * 2只验证静态密码
     * 3不进行本地认证
     */
    private int localAuth = 0;//本地认证
    private String pwd; //静态密码，RC4加密存储
    private int pwdDeath; //静态密码过期UTC秒数，0永不过期
    private int getpwdDeath;//静态密码找回有效期，0为不找回状态
    private int papersType;//证件类型，0：员工编号，……其它待扩展
    private String papersNumber;//证件编号，默认指员工编号
    private String email ;
    private String address ;
    private String tel ;
    private String cellPhone ;
    private int locked ;//是否锁定（0、未锁定 1、临时锁定 2、永久锁定）
    
    private int tempLoginErrCnt; //临时锁定连续错误次数
    private int longLoginErrCnt;
    private int loginLockTime;
    private int loginCnt;
    private int lastLoginTime;
    
    private int backEndAuth;
    private int enabled=1; //是否启用 用户 1是启用
    private int domainId;
    private Integer orgunitId;
    private Integer radProfileId;
    private int createTime;
    
    //扩展
    private String dOrgunitId; //domainId:orgunitId 值对
    private String dOrgunitName;// domainName-->orgunitName
    private DomainInfo domainInfo;//用户所属域信息
    private OrgunitInfo orgunitInfo;//用户所属组织机构信息
    private int bind = 0; //用户是否绑定令牌0全部 1未绑定 2已绑定
    private int usbind = 0; //用户是否绑定令牌0全部 1未绑定 2已绑定
    private int usbindTag;
    private List<?> tokens; //用户绑定的令牌信息
    private List<?> userIds; //用户集
    private List<?> hiddenTkns;//用户绑定的令牌隐藏域，对于更换令牌时与新选择的令牌比对。
    private String token; //令牌号 用于查询
    
    private Integer isFliterTag=null; // null不过滤用户列表 超级管理员使用 1过滤根据管理员id过滤用户 
    private String currentAdminId; //当前管理员id 根据此id过滤 当前管理员所管理的机构或域下的用户
    
    private List<?> reTokens;//多用户绑定令牌 同时与其他用户绑定的令牌
    private List<?> leftTokens;//单用户绑定的令牌
    
    private int physicalType;
    private int productType;
    private int expireTime; //过期时间
    private int otpLen;
    private int intervalTime; //时间间隔 变化周期 
    private String domainName;
    private String orgunitName;
    
    private String orgunitNumber;
    private int orgFlag = 0;
    private Integer newOrgunitId; //批量变更组织机构时应用
    

    //记录日志时使用到
    private String currentOrgunitName;
    private String targetOrgunitName;
    
    private int [] orgunitIds;
    private int [] domainIds;
    
	public List<?> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<?> userIds) {
		this.userIds = userIds;
	}

	public int getOrgFlag() {
		return orgFlag;
	}

	public void setOrgFlag(int orgFlag) {
		this.orgFlag = orgFlag;
	}

	public int[] getOrgunitIds() {
		return orgunitIds;
	}

	public void setOrgunitIds(int[] orgunitIds) {
		this.orgunitIds = orgunitIds;
	}
	
	public int[] getDomainIds() {
		return domainIds;
	}

	public void setDomainIds(int[] domainIds) {
		this.domainIds = domainIds;
	}

	public int getUsbindTag() {
		return usbindTag;
	}

	public void setUsbindTag(int usbindTag) {
		this.usbindTag = usbindTag;
	}

	private int upPageTag=1;//是否分页 1分页  0不分页
    
    public int getUsbind() {
		return usbind;
	}

	public void setUsbind(int usbind) {
		this.usbind = usbind;
	}

	public UserInfo(){
    	
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
		int exchangeTag = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_COMMON,
                ConfConstant.USERID_FORMAT_TYPE));
		
		// 小写转换
		if(StrTool.strNotNull(userId) && exchangeTag == NumConstant.common_number_1){
			userId = userId.toLowerCase();
		}
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
	 * @return the pwd
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * @param pwd the pwd to set
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	/**
	 * @return the pwdDeath
	 */
	public int getPwdDeath() {
		return pwdDeath;
	}

	/**
	 * @param pwdDeath the pwdDeath to set
	 */
	public void setPwdDeath(int pwdDeath) {
		this.pwdDeath = pwdDeath;
	}

	/**
	 * @return the getpwdDeath
	 */
	public int getGetpwdDeath() {
		return getpwdDeath;
	}

	/**
	 * @param getpwdDeath the getpwdDeath to set
	 */
	public void setGetpwdDeath(int getpwdDeath) {
		this.getpwdDeath = getpwdDeath;
	}

	/**
	 * @return the papersType
	 */
	public int getPapersType() {
		return papersType;
	}

	/**
	 * @param papersType the papersType to set
	 */
	public void setPapersType(int papersType) {
		this.papersType = papersType;
	}

	/**
	 * @return the papersNumber
	 */
	public String getPapersNumber() {
		return papersNumber;
	}

	/**
	 * @param papersNumber the papersNumber to set
	 */
	public void setPapersNumber(String papersNumber) {
		this.papersNumber = papersNumber;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}


	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * @return the cellPhone
	 */
	public String getCellPhone() {
		return cellPhone;
	}

	/**
	 * @param cellPhone the cellPhone to set
	 */
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	/**
	 * @return the locked
	 */
	public int getLocked() {
		return locked;
	}

	/**
	 * @param locked the locked to set
	 */
	public void setLocked(int locked) {
		this.locked = locked;
	}

	/**
	 * @return the tempLoginErrCnt
	 */
	public int getTempLoginErrCnt() {
		return tempLoginErrCnt;
	}

	/**
	 * @param tempLoginErrCnt the tempLoginErrCnt to set
	 */
	public void setTempLoginErrCnt(int tempLoginErrCnt) {
		this.tempLoginErrCnt = tempLoginErrCnt;
	}

	/**
	 * @return the longLoginErrCnt
	 */
	public int getLongLoginErrCnt() {
		return longLoginErrCnt;
	}

	/**
	 * @param longLoginErrCnt the longLoginErrCnt to set
	 */
	public void setLongLoginErrCnt(int longLoginErrCnt) {
		this.longLoginErrCnt = longLoginErrCnt;
	}

	/**
	 * @return the loginLockTime
	 */
	public int getLoginLockTime() {
		return loginLockTime;
	}

	/**
	 * @param loginLockTime the loginLockTime to set
	 */
	public void setLoginLockTime(int loginLockTime) {
		this.loginLockTime = loginLockTime;
	}

	/**
	 * @return the loginCnt
	 */
	public int getLoginCnt() {
		return loginCnt;
	}

	/**
	 * @param loginCnt the loginCnt to set
	 */
	public void setLoginCnt(int loginCnt) {
		this.loginCnt = loginCnt;
	}

	/**
	 * @return the lastLoginTime
	 */
	public int getLastLoginTime() {
		return lastLoginTime;
	}
	
	/*
	 * 锁定时间的字符串格式
	 */
	public String getLoginLockTimeStr() {
        if (getLoginLockTime() != 0) {
            return DateTool.dateToStr(getLoginLockTime(), true);
        }
        return "";
    }

	/*
	 * 锁定时间的字符串格式
	 */
	public String getPwdDeathStr() {
        if (getPwdDeath() != 0) {
            return DateTool.dateToStr(getPwdDeath(), true);
        }
        return "";
    }
	
	/**
	 * @param lastLoginTime the lastLoginTime to set
	 */
	public void setLastLoginTime(int lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	/**
	 * @return the backEndAuth
	 */
	public int getBackEndAuth() {
		return backEndAuth;
	}

	/**
	 * @param backEndAuth the backEndAuth to set
	 */
	public void setBackEndAuth(int backEndAuth) {
		this.backEndAuth = backEndAuth;
	}

	/**
	 * @return the enabled
	 */
	public int getEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the domainId
	 */
	public int getDomainId() {
		return domainId;
	}

	/**
	 * @param domainId the domainId to set
	 */
	public void setDomainId(int domainId) {
		this.domainId = domainId;
	}


	/**
	 * @return the createTime
	 */
	public int getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the tokens
	 */
	public List<?> getTokens() {
		return tokens;
	}

	/**
	 * @param tokens the tokens to set
	 */
	public void setTokens(List<?> tokens) {
		this.tokens = tokens;
	}


	/**
	 * @return the hiddenTkns
	 */
	public List<?> getHiddenTkns() {
		return hiddenTkns;
	}

	/**
	 * @param hiddenTkns the hiddenTkns to set
	 */
	public void setHiddenTkns(List<?> hiddenTkns) {
		this.hiddenTkns = hiddenTkns;
	}

	/**
	 * @return the bind
	 */
	public int getBind() {
		return bind;
	}

	/**
	 * @param bind the bind to set
	 */
	public void setBind(int bind) {
		this.bind = bind;
	}

	/**
	 * @return the domainInfo
	 */
	public DomainInfo getDomainInfo() {
		return domainInfo;
	}

	/**
	 * @param domainInfo the domainInfo to set
	 */
	public void setDomainInfo(DomainInfo domainInfo) {
		this.domainInfo = domainInfo;
	}

	/**
	 * @return the orgunitInfo
	 */
	public OrgunitInfo getOrgunitInfo() {
		return orgunitInfo;
	}

	/**
	 * @param orgunitInfo the orgunitInfo to set
	 */
	public void setOrgunitInfo(OrgunitInfo orgunitInfo) {
		this.orgunitInfo = orgunitInfo;
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
	 * @return the orgunitId
	 */
	public Integer getOrgunitId() {
		return orgunitId;
	}

	/**
	 * @param orgunitId the orgunitId to set
	 */
	public void setOrgunitId(Integer orgunitId) {
		this.orgunitId = orgunitId;
	}

	/**
	 * @return the radProfileId
	 */
	public Integer getRadProfileId() {
		return radProfileId;
	}

	/**
	 * @param radProfileId the radProfileId to set
	 */
	public void setRadProfileId(Integer radProfileId) {
		this.radProfileId = radProfileId;
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

	/**
	 * @return the dOrgunitName
	 */
	public String getDOrgunitName() {
		return dOrgunitName;
	}

	/**
	 * @param orgunitName the dOrgunitName to set
	 */
	public void setDOrgunitName(String orgunitName) {
		dOrgunitName = orgunitName;
	}



	/**
	 * @return the isFliterTag
	 */
	public Integer getIsFliterTag() {
		return isFliterTag;
	}

	/**
	 * @param isFliterTag the isFliterTag to set
	 */
	public void setIsFliterTag(Integer isFliterTag) {
		this.isFliterTag = isFliterTag;
	}

	/**
	 * @return the currentAdminId
	 */
	public String getCurrentAdminId() {
		return currentAdminId;
	}

	/**
	 * @param currentAdminId the currentAdminId to set
	 */
	public void setCurrentAdminId(String currentAdminId) {
		this.currentAdminId = currentAdminId;
	}

	/**
	 * @return the reTokens
	 */
	public List<?> getReTokens() {
		return reTokens;
	}

	/**
	 * @param reTokens the reTokens to set
	 */
	public void setReTokens(List<?> reTokens) {
		this.reTokens = reTokens;
	}

	/**
	 * @return the leftTokens
	 */
	public List<?> getLeftTokens() {
		return leftTokens;
	}

	/**
	 * @param leftTokens the leftTokens to set
	 */
	public void setLeftTokens(List<?> leftTokens) {
		this.leftTokens = leftTokens;
	}

	/**
	 * @return the physicalType
	 */
	public int getPhysicalType() {
		return physicalType;
	}

	/**
	 * @param physicalType the physicalType to set
	 */
	public void setPhysicalType(int physicalType) {
		this.physicalType = physicalType;
	}

	/**
	 * @return the productType
	 */
	public int getProductType() {
		return productType;
	}

	/**
	 * @param productType the productType to set
	 */
	public void setProductType(int productType) {
		this.productType = productType;
	}

	/**
	 * @return the expireTime
	 */
	public int getExpireTime() {
		return expireTime;
	}

	/**
	 * @param expireTime the expireTime to set
	 */
	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}

	/**
	 * @return the otpLen
	 */
	public int getOtpLen() {
		return otpLen;
	}

	/**
	 * @param otpLen the otpLen to set
	 */
	public void setOtpLen(int otpLen) {
		this.otpLen = otpLen;
	}

	/**
	 * @return the intervalTime
	 */
	public int getIntervalTime() {
		return intervalTime;
	}

	/**
	 * @param intervalTime the intervalTime to set
	 */
	public void setIntervalTime(int intervalTime) {
		this.intervalTime = intervalTime;
	}

	/**
	 * @return the domainName
	 */
	public String getDomainName() {
		return domainName;
	}

	/**
	 * @param domainName the domainName to set
	 */
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	/**
	 * @return the orgunitName
	 */
	public String getOrgunitName() {
		return orgunitName;
	}

	/**
	 * @param orgunitName the orgunitName to set
	 */
	public void setOrgunitName(String orgunitName) {
		this.orgunitName = orgunitName;
	}

	/**
	 * @return the orgunitNumber
	 */
	public String getOrgunitNumber() {
		return orgunitNumber;
	}

	/**
	 * @param orgunitNumber the orgunitNumber to set
	 */
	public void setOrgunitNumber(String orgunitNumber) {
		this.orgunitNumber = orgunitNumber;
	}

    /**
     * @return the currentOrgunitName
     */
    public String getCurrentOrgunitName() {
        return currentOrgunitName;
    }

    /**
     * @param currentOrgunitName the currentOrgunitName to set
     */
    public void setCurrentOrgunitName(String currentOrgunitName) {
        this.currentOrgunitName = currentOrgunitName;
    }
	/**
	 * @return the upPageTag
	 */
	public int getUpPageTag() {
		return upPageTag;
	}

	/**
	 * @param upPageTag the upPageTag to set
	 */
	public void setUpPageTag(int upPageTag) {
		this.upPageTag = upPageTag;
	}


	/**
	 * @return the newOrgunitId
	 */
	public Integer getNewOrgunitId() {
		return newOrgunitId;
	}

	/**
	 * @param newOrgunitId the newOrgunitId to set
	 */
	public void setNewOrgunitId(Integer newOrgunitId) {
		this.newOrgunitId = newOrgunitId;
	}



    /**
     * @return the targetOrgunitName
     */
    public String getTargetOrgunitName() {
        return targetOrgunitName;
    }

    /**
     * @param targetOrgunitName the targetOrgunitName to set
     */
    public void setTargetOrgunitName(String targetOrgunitName) {
        this.targetOrgunitName = targetOrgunitName;
    }


    public String getRadProfileName() {
        return PubConfConfig.getRadProValue(radProfileId);
    }
}
