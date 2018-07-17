package com.ft.otp.manager.orgunit.domain.entity;

import java.util.List;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.StrTool;

/**
 * 域实体类
 *
 * @Date in January 17, 2013,14:00:00 PM
 *
 * @author BYL
 */
public class DomainInfo extends BaseEntity {

	private int domainId;
    private String domainSn;
    private String domainName;
    private String descp = "";
    private int createTime;
    
    private List<?> orgunitInfos;
    
    private List<?> adminIds; //管理该域的管理员 ID号
    private String adminId; //
    private int isFilterTag=0; //0否 1根据管理员id获得该管理员所管理的域
    
    private String oldDomainName; //原域名称
    
    public DomainInfo() {

    }
    
    public DomainInfo(int domainId,String domainSn,String domainName,String descp) {
    	this.domainId=domainId;
    	this.domainSn=domainSn;
    	this.domainName=domainName;
    	
    	this.descp=StrTool.cleanXSS(descp);
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
	 * @return the domainSn
	 */
	public String getDomainSn() {
		return domainSn;
	}

	/**
	 * @param domainSn the domainSn to set
	 */
	public void setDomainSn(String domainSn) {
		int exchangeTag = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_COMMON,
                ConfConstant.USERID_FORMAT_TYPE));
		
		// 小写转换
		if(StrTool.strNotNull(domainSn) && exchangeTag == NumConstant.common_number_1){
			domainSn = domainSn.toLowerCase();
		}
		this.domainSn = domainSn;
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
	 * @return the descp
	 */
	public String getDescp() {
		return descp;
	}

	/**
	 * @param descp the descp to set
	 */
	public void setDescp(String descp) {
		this.descp = StrTool.cleanXSS(descp);
	}

	/**
	 * @return the orgunitInfos
	 */
	public List<?> getOrgunitInfos() {
		return orgunitInfos;
	}

	/**
	 * @param orgunitInfos the orgunitInfos to set
	 */
	public void setOrgunitInfos(List<?> orgunitInfos) {
		this.orgunitInfos = orgunitInfos;
	}

	/**
	 * @return the adminIds
	 */
	public List<?> getAdminIds() {
		return adminIds;
	}

	/**
	 * @param adminIds the adminIds to set
	 */
	public void setAdminIds(List<?> adminIds) {
		this.adminIds = adminIds;
	}


	/**
	 * @return the isFilterTag
	 */
	public int getIsFilterTag() {
		return isFilterTag;
	}

	/**
	 * @param isFilterTag the isFilterTag to set
	 */
	public void setIsFilterTag(int isFilterTag) {
		this.isFilterTag = isFilterTag;
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

    public String getOldDomainName() {
        return oldDomainName;
    }

    public void setOldDomainName(String oldDomainName) {
        this.oldDomainName = oldDomainName;
    }
}
