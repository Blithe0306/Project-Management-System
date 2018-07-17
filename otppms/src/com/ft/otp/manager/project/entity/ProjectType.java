/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 * @Date Dec 24, 2014,10:21:20 PM
 */
package com.ft.otp.manager.project.entity;

import java.util.List;

/**
 * 定制类型
 * @Date Dec 24, 2014,10:21:20 PM
 * @version v1.0
 * @author ZWX
 */
public class ProjectType {

	private String	typeid;		// 定制编号
	private String	typeName;	// 定制编号名称
	private String	version;	// 基础版本

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
