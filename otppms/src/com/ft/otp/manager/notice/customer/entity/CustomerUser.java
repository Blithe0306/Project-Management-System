/**
 *Administrator
 */
package com.ft.otp.manager.notice.customer.entity;

import com.ft.otp.base.entity.BaseEntity;
 
/**
 * 派单监视人
 *
 * @Date in April 23, 2015,11:24:49 AM
 *
 * @version v1.0
 *
 * @author ZWX
 */
public class CustomerUser extends BaseEntity {

	private String userid;
	private String realname;
	private String projectId;
	private String oldprojectId;
    private String userEmail; 

    public String getOldprojectId() {
		return oldprojectId;
	}
	public void setOldprojectId(String oldprojectId) {
		this.oldprojectId = oldprojectId;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
}
