/**
 *Administrator
 */
package com.ft.otp.manager.user_token.entity;

import java.util.List;

import com.ft.otp.base.entity.BaseEntity;

/**
 * 用户-令牌实体
 *
 * @Date in Apr 21, 2011,6:37:00 PM
 *
 * @author TBM
 */
public class UserToken extends BaseEntity {
    private int bindId;
    private String userId = "";
    private String token = "";

    private List<?> userIds = null;
    private List<?> tokenIds = null;
    private List<?> domainIds = null;

    private Integer domainId = null;
    private int orgunitId; //令牌机构ID
    private int orguserId; //用户机构ID
    private int bindTime; //绑定时间

    /**
     * 关联查询显示令牌的几个主要属性
     */

    private int producttype = -1; //产品类型
    private int otplen = 6; //令牌长度
    private int physicaltype = -1; //物理类型（手机令牌、硬件令牌）  
    private int intervaltime = -1; //自变周期
    private int validtime = 0; //令牌有效期

    private int bindTag = 1;//1多用户绑定的令牌 0单用户绑定的令牌
    private int oneToken = 0;
    private int isNullDomain = -1; //判断令牌号是否被管理员绑定；0：否；1：是；
    
	public int getIsNullDomain() {
		return isNullDomain;
	}

	public void setIsNullDomain(int isNullDomain) {
		this.isNullDomain = isNullDomain;
	}

	public int getOneToken() {
		return oneToken;
	}

	public void setOneToken(int oneToken) {
		this.oneToken = oneToken;
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
     * @return the userIds
     */
    public List<?> getUserIds() {
        return userIds;
    }

    /**
     * @param userIds the userIds to set
     */
    public void setUserIds(List<?> userIds) {
        this.userIds = userIds;
    }

    /**
     * @return the intervaltime
     */
    public int getIntervaltime() {
        return intervaltime;
    }

    /**
     * @param intervaltime the intervaltime to set
     */
    public void setIntervaltime(int intervaltime) {
        this.intervaltime = intervaltime;
    }

    /**
     * @return the tokenIds
     */
    public List<?> getTokenIds() {
        return tokenIds;
    }

    /**
     * @param tokenIds the tokenIds to set
     */
    public void setTokenIds(List<?> tokenIds) {
        this.tokenIds = tokenIds;
    }

    /**
     * @return the tokenType
     */

    /**
     * @return the otplen
     */
    public int getOtplen() {
        return otplen;
    }

    /**
     * @param otplen the otplen to set
     */
    public void setOtplen(int otplen) {
        this.otplen = otplen;
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
    }

    /**
     * @return the bindId
     */
    public int getBindId() {
        return bindId;
    }

    /**
     * @param bindId the bindId to set
     */
    public void setBindId(int bindId) {
        this.bindId = bindId;
    }

    /**
     * @return the validtime
     */
    public int getValidtime() {
        return validtime;
    }

    /**
     * @param validtime the validtime to set
     */
    public void setValidtime(int validtime) {
        this.validtime = validtime;
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

    /**
     * @return the bindTime
     */
    public int getBindTime() {
        return bindTime;
    }

    /**
     * @param bindTime the bindTime to set
     */
    public void setBindTime(int bindTime) {
        this.bindTime = bindTime;
    }

    /**
     * @return the bindTag
     */
    public int getBindTag() {
        return bindTag;
    }

    /**
     * @param bindTag the bindTag to set
     */
    public void setBindTag(int bindTag) {
        this.bindTag = bindTag;
    }

    /**
     * @return the domainIds
     */
    public List<?> getDomainIds() {
        return domainIds;
    }

    /**
     * @param domainIds the domainIds to set
     */
    public void setDomainIds(List<?> domainIds) {
        this.domainIds = domainIds;
    }

	public int getOrgunitId() {
		return orgunitId;
	}

	public void setOrgunitId(int orgunitId) {
		this.orgunitId = orgunitId;
	}

	public int getOrguserId() {
		return orguserId;
	}

	public void setOrguserId(int orguserId) {
		this.orguserId = orguserId;
	}
}
