package com.ft.otp.manager.confinfo.radius.entity;

import com.ft.otp.base.entity.BaseEntity;

/**
 * RADIUS配置实体
 * 
 * @Date in Oct 29, 2012,3:18:57 PM
 * 
 * @version v1.0
 * 
 * @author YXS
 */
public class RadProfileInfo extends BaseEntity {

	private int profileId = -1;
	private String profileName;// RADIUS配置名称
	private String profileDesc;// RADIUS配置描述

	public int getProfileId() {
		return profileId;
	}

	public void setProfileId(int profileId) {
		this.profileId = profileId;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getProfileDesc() {
		return profileDesc;
	}

	public void setProfileDesc(String profileDesc) {
		this.profileDesc = profileDesc;
	}

}
