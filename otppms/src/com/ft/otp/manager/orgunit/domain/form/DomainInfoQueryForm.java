package com.ft.otp.manager.orgunit.domain.form;

import com.ft.otp.base.form.BaseQueryForm;
import com.ft.otp.manager.orgunit.domain.entity.DomainInfo;

/**
 *域 查询Form 类说明
 *
 * @Date in January 17, 2013,14:00:00 PM
 *
 * @author BYL
 */
public class DomainInfoQueryForm extends BaseQueryForm {
	
	private DomainInfo domainInfo = new DomainInfo();
	private String domainSn =null;
	private String domainName =null;
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
	 * @return the domainSn
	 */
	public String getDomainSn() {
		return domainSn;
	}

	/**
	 * @param domainSn the domainSn to set
	 */
	public void setDomainSn(String domainSn) {
		this.domainSn = domainSn;
		domainInfo.setDomainSn(domainSn.trim());
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
		domainInfo.setDomainName(domainName.trim());
	}
}
