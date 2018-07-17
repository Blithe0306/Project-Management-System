package com.ft.otp.manager.orgunit.orgunit.entity;

import java.util.List;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.manager.orgunit.domain.entity.DomainInfo;
import com.ft.otp.manager.orgunit.orgunit.action.aide.TreeOrgunitInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 组织机构实体类
 *
 * @Date in January 21,2013,  10:00:00 AM
 *
 * @author BYL
 */
public class OrgunitInfo extends BaseEntity  {

	private int orgunitId;
    private String orgunitNumber;
    private String orgunitName;
    private int parentId;
    private int domainId;
    private String descp;
    private int createTime;
    
    private List<?> admins;
    private TreeOrgunitInfo parentOrgunitInfo; //父组织机构 可能是域信息 也可能是组织机构 所以用中间model 展示时使用
    private DomainInfo domainInfo; 
    private List<?> children=null; //直接子节点列表
    
    private String orgParentName; //组织机构父名称
    private String oldOrgName; //旧组织机构名称
    
    public OrgunitInfo() {

    }
    
    public OrgunitInfo(int orgunitId,String orgunitNumber,String orgunitName,int parentId,int domainId,String descp) {
    	this.orgunitId=orgunitId;
    	this.orgunitNumber=orgunitNumber;
    	this.orgunitName=orgunitName;
    	this.parentId=parentId;
    	this.domainId=domainId;
    	this.descp=StrTool.cleanXSS(descp);
    }

	/**
	 * @return the orgunitId
	 */
	public int getOrgunitId() {
		return orgunitId;
	}

	/**
	 * @param orgunitId the orgunitId to set
	 */
	public void setOrgunitId(int orgunitId) {
		this.orgunitId = orgunitId;
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
	 * @return the parentId
	 */
	public int getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(int parentId) {
		this.parentId = parentId;
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
	 * @return the children
	 */
	public List<?> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<?> children) {
		this.children = children;
	}

	public List<?> getAdmins() {
		return admins;
	}

	public void setAdmins(List<?> admins) {
		this.admins = admins;
	}

	/**
	 * @return the parentOrgunitInfo
	 */
	public TreeOrgunitInfo getParentOrgunitInfo() {
		return parentOrgunitInfo;
	}

	/**
	 * @param parentOrgunitInfo the parentOrgunitInfo to set
	 */
	public void setParentOrgunitInfo(TreeOrgunitInfo parentOrgunitInfo) {
		this.parentOrgunitInfo = parentOrgunitInfo;
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

    public String getOrgParentName() {
        return orgParentName;
    }

    public void setOrgParentName(String orgParentName) {
        this.orgParentName = orgParentName;
    }

    public String getOldOrgName() {
        return oldOrgName;
    }

    public void setOldOrgName(String oldOrgName) {
        this.oldOrgName = oldOrgName;
    }



}
