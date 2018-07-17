package com.ft.otp.manager.project.entity;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

public class ProjectResult extends BaseEntity {
	
	private int id; 
	private String	prjid;
	private String results;
	private String operator;
	private String createtime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPrjid() {
		return prjid;
	}
	public void setPrjid(String prjid) {
		this.prjid = prjid;
	}
	public String getResults() {
		return results;
	}
	public void setResults(String results) {
		this.results = results;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	} 
}
