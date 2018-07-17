package com.ft.otp.manager.customer.entity;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.common.config.DepartmentConfig;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 客户实体类
 */
public class CustomerInfo extends BaseEntity {

	private int		id = -1;
	private String	custid;			// 客户编号
	private String	custname;		// 客户名称
	private String	dept;			// 所属部门
	private String	deptStr;		// 所属部门
	private String	contacts;		// 联系方式，可多人
	private long	createtime;		// 添加时间
	private String	createtimeStr;	// 添加时间
	private String	operator;		// 添加人

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCustid() {
		return custid;
	}

	public void setCustid(String custid) {
		this.custid = custid;
	}

	public String getCustname() {
		return custname;
	}

	public void setCustname(String custname) {
		this.custname = custname;
	}

	public String getDept() {
		return dept;
	}

	public String getDeptStr() {
		if (StrTool.strNotNull(getDept())) {
			DepartmentInfo deptinfo = DepartmentConfig.getDeptMap().get(dept);
			if(StrTool.objNotNull(deptinfo)){
				return deptinfo.getDeptName();
			}
		}
		return deptStr;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public long getCreatetime() {
		return createtime;
	}

	public String getCreatetimeStr() {
		if (getCreatetime() != 0) {
			return DateTool.dateToStr(getCreatetime(), true);
		}
		return createtimeStr;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

}
