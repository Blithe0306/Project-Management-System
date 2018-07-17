/**
 *Administrator
 */
package com.ft.otp.manager.logs.adminlog.entity;

import java.util.List;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.util.tool.DateTool;

/**
 * 管理员日志实体类功能说明
 *
 * @Date in May 4, 2011,3:52:30 PM
 *
 * @author ZJY
 */
public class AdminLogInfo extends BaseEntity {

    
    private int id;          //日志ID 自增主键
    private String operator;    //产生日志记录的操作管理员
    private int logtime;        //记录操作时间
    private int actionid;       //操作标识ID
    private int actionresult = -1;   //操作结果
    private int actionobject;   //操作对象
    private String clientip;    //操作客户端IP地址
    private String descp;       //操作对象描述
    private String hashcode ="";//日志摘要值
    private String actionDesc;
    
    public String getActionDesc() {
		return actionDesc;
	}

	public void setActionDesc(String actionDesc) {
		this.actionDesc = actionDesc;
	}

	private List<String> descList;

    private int startLogTime;
    private int endLogTime;
    
    
    private int[] lidArr; //存放log_action_id_状态码
    private int[] lobjArr; //存放log_action_obj_状态码

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    //日志时间由int秒数转换为时间格式
    public String getLogTimeStr() {
        return DateTool.dateToStr(logtime, true);
    }

    public int getActionresult() {
        return actionresult;
    }

    public void setActionresult(int actionresult) {
        this.actionresult = actionresult;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    /**
     * @return the logtime
     */
    public int getLogtime() {
        return logtime;
    }

    /**
     * @param logtime the logtime to set
     */
    public void setLogtime(int logtime) {
        this.logtime = logtime;
    }

    /**
     * @return the actionid
     */
    public int getActionid() {
        return actionid;
    }

    /**
     * @param actionid the actionid to set
     */
    public void setActionid(int actionid) {
        this.actionid = actionid;
    }

    /**
     * @return the actionobject
     */
    public int getActionobject() {
        return actionobject;
    }

    /**
     * @param actionobject the actionobject to set
     */
    public void setActionobject(int actionobject) {
        this.actionobject = actionobject;
    }

    /**
     * @return the startLogTime
     */
    public int getStartLogTime() {
        return startLogTime;
    }

    /**
     * @param startLogTime the startLogTime to set
     */
    public void setStartLogTime(int startLogTime) {
        this.startLogTime = startLogTime;
    }

    /**
     * @return the endLogTime
     */
    public int getEndLogTime() {
        return endLogTime;
    }

    /**
     * @param endLogTime the endLogTime to set
     */
    public void setEndLogTime(int endLogTime) {
        this.endLogTime = endLogTime;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientip() {
        return clientip;
    }

    public void setClientip(String clientip) {
        this.clientip = clientip;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    /**
     * @return the descList
     */
    public List<String> getDescList() {
        return descList;
    }

    /**
     * @param descList the descList to set
     */
    public void setDescList(List<String> descList) {
        this.descList = descList;
    }

    public int[] getLidArr() {
        return lidArr;
    }

    public void setLidArr(int[] lidArr) {
        this.lidArr = lidArr;
    }

    public int[] getLobjArr() {
        return lobjArr;
    }

    public void setLobjArr(int[] lobjArr) {
        this.lobjArr = lobjArr;
    }

}
