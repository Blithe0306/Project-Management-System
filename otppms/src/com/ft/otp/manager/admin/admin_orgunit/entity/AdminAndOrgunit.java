package com.ft.otp.manager.admin.admin_orgunit.entity;

/**
 *管理员-组织机构关系实体类说明
 *
 * @Date in January 24, 2013,10:00:00 AM
 *
 * @author ZJY
 */
public class AdminAndOrgunit {

	private String adminId;
	private Integer orgunitId=null;
	private int domainId;
	
	public AdminAndOrgunit() {

	}
	
	public AdminAndOrgunit(String adminId,Integer orgunitId,int domainId) {
		this.adminId=adminId;
		this.orgunitId=orgunitId;
		this.domainId=domainId;
	}

	/**
	 * @return the adminId
	 */
	public String getAdminId() {
		return adminId;
	}

	/**
	 * @param adminId the adminId to set
	 */
	public void setAdminId(String adminId) {
		this.adminId = adminId;
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
}
