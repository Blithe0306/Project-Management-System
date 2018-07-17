package com.ft.otp.manager.project.entity;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.common.config.ProjectTypeConfig;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 定制项目实体类 tab name : otppms_project
 */
public class Project extends BaseEntity {

	private String	id;
	private String	prjid;
	private String	prjname;
	private String	custid;
	private String	custname;
	private String	type;
	private String	typeStr;
	private String	typeversion;
	private String	prjstate;
	private String	needdept;
	private String	sales;
	private String	techsupport;
	private String	ifpay	= "0";
	private String	ifpayStr;
	private String	svn;
	private String	bug;
	private String	prjdesc;
	private long	createtime;
	private String	createtimeStr;	// 添加时间
	private int		startTime;
	private int		endTime;
	private String	operator;
	private long	updatetime;
	private String	updatetimeStr;

	public String getTypeStr() {
		if (StrTool.strNotNull(getType())) {
			ProjectType typeBean = ProjectTypeConfig.getTypeMap().get(getType());
			return typeStr = typeBean.getTypeName();
		}
		return typeStr;
	}

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

	public String getCreatetimeStr() {

		if (getCreatetime() != 0) {
			return DateTool.dateToStr(getCreatetime(), true);
		}
		return createtimeStr;
	}

	public void setCreatetimeStr(String createtimeStr) {
		this.createtimeStr = createtimeStr;
	}

	public String getCustname() {
		return custname;
	}

	public void setCustname(String custname) {
		this.custname = custname;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public String getIfpay() {
		return ifpay;
	}

	public void setIfpay(String ifpay) {
		this.ifpay = ifpay;
	}

	public String getPrjid() {
		return prjid;
	}

	public void setPrjid(String prjid) {
		this.prjid = prjid;
	}

	public String getPrjname() {
		return prjname;
	}

	public void setPrjname(String prjname) {
		this.prjname = prjname;
	}

	public String getCustid() {
		return custid;
	}

	public void setCustid(String custid) {
		this.custid = custid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeversion() {
		return typeversion;
	}

	public void setTypeversion(String typeversion) {
		this.typeversion = typeversion;
	}

	public String getPrjstate() {
		return prjstate;
	}

	public void setPrjstate(String prjstate) {
		this.prjstate = prjstate;
	}
	public String getPrjstateStr(){
		String prjstateStr = "";
		if(StrTool.strNotNull(prjstate)){
			int prjstateInt = Integer.parseInt(prjstate);
			switch (prjstateInt) {
			case 0:
				prjstateStr = "新立项";
				break;
			case 1:
				prjstateStr = "需求";
				break;
			case 2:
				prjstateStr = "设计";
				break;
			case 3:
				prjstateStr = "开发";
				break;
			case 4:
				prjstateStr = "测试";
				break;
			case 5:
				prjstateStr = "完成";
				break;
			case 6:
				prjstateStr = "反馈";
				break;
			}
		}
		return prjstateStr;
	}

	public String getNeeddept() {
		return needdept;
	}

	public void setNeeddept(String needdept) {
		this.needdept = needdept;
	}

	public String getSales() {
		return sales;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}

	public String getTechsupport() {
		return techsupport;
	}

	public void setTechsupport(String techsupport) {
		this.techsupport = techsupport;
	}

	public String getSvn() {
		return svn;
	}

	public void setSvn(String svn) {
		this.svn = svn;
	}

	public String getBug() {
		return bug;
	}

	public void setBug(String bug) {
		this.bug = bug;
	}

	public String getPrjdesc() {
		return prjdesc;
	}

	public void setPrjdesc(String prjdesc) {
		this.prjdesc = prjdesc;
	}

	public long getCreatetime() {
		return createtime;
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

	public long getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIfpayStr() {
		if ("1".equals(getIfpay())) {
			ifpayStr = "否";
		}
		else {
			ifpayStr = "是";
		}
		return ifpayStr;
	}

	public void setIfpayStr(String ifpayStr) {
		this.ifpayStr = ifpayStr;
	}

	public String getUpdatetimeStr() {
		
		if (getUpdatetime() != 0) {
			return DateTool.dateToStr(getUpdatetime(), true);
		}
		return updatetimeStr;
	}

	public void setUpdatetimeStr(String updatetimeStr) {
		this.updatetimeStr = updatetimeStr;
	}
	
}
