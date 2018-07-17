/**
 *qiuju
 */
package com.ft.otp.manager.resords.entity;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 实体类
 */
public class Resords extends BaseEntity {
    private int id;
    private String recordUser; // 记录人员
    private String cutomer; // 使用项目的客户
    private String prjid; // 项目编号
    private int recordtime; // 上门记录的时间
    private int endrecordtime; // 上门记录的时间
    private long createtime; // 添加时间
    private String operator; // 添加人
    private String reason; // 理由
    private String results; // 结果
    private String remark;

    private String recordtimeStr; //
    private String endrecordtimeStr; //

	public String getEndrecordtimeStr() {
		if(!StrTool.strNotNull(endrecordtimeStr)){
			endrecordtimeStr = getEndrecordtime()==0?"":DateTool.dateToStr(getEndrecordtime(), true);
		}
		return endrecordtimeStr;
	}

	public void setEndrecordtimeStr(String endrecordtimeStr) {
		this.endrecordtimeStr = endrecordtimeStr;
	}

	public String getRecordtimeStr() {
        return recordtimeStr;
    }

    public void setRecordtimeStr(String recordtimeStr) {
        this.recordtimeStr = recordtimeStr;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecordUser() {
        return recordUser;
    }

    public void setRecordUser(String recordUser) {
        this.recordUser = recordUser;
    }

    public String getCutomer() {
        return cutomer;
    }

    public void setCutomer(String cutomer) {
        this.cutomer = cutomer;
    }

    public String getPrjid() {
        return prjid;
    }

    public void setPrjid(String prjid) {
        this.prjid = prjid;
    }

    public int getRecordtime() {
        return recordtime;
    }

    public void setRecordtime(int recordtime) {
        this.recordtime = recordtime;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getRecordtimeshowStr() {
        return DateTool.dateToStr(getRecordtime(), true);
    }

	public int getEndrecordtime() {
		return endrecordtime;
	}

	public void setEndrecordtime(int endrecordtime) {
		this.endrecordtime = endrecordtime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
