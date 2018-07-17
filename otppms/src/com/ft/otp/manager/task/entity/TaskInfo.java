/**
 *Administrator
 */
package com.ft.otp.manager.task.entity;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户来源定时时间信息实体类
 *
 * @Date in Mar 9, 2012,9:38:17 AM
 *
 * @version v1.0
 *
 * @author ZJY
 */
public class TaskInfo extends BaseEntity {

    private String taskname; //定时任务名称标题，主键
    private int sourceid; //与用户来源关联ID，非用户来源定时任务配置为0
    private int sourcetype; //定时任务类型，0：更新用户定时任务；1:监控预警任务；2:定时删除日志任务；3:定时备份任务；4:双机热备运行监控任务；
    private int taskmode1; //定时时间设置方式 1按月；2按周；3按天 
    private int taskmode2; //1 按每n小时，2 按每n分钟，0 每天的n点n分
    private String taskminute; //分钟
    private String taskhour; //小时
    private String taskday; //天
    private String taskweek; //星期
    private String taskmonth; //月份
    private int enabled = 0; //任务状态 1、启动状态  0、停止状态
    private String taskid = ""; //任务会话ID
    private String descp;
    private int logFlag = 0; // service是否记录日志标志

    public int getLogFlag() {
		return logFlag;
	}

	public void setLogFlag(int logFlag) {
		this.logFlag = logFlag;
	}

	//按天设置,选择按每n小时执行
    private String selAccHour = "";

    /**
     * @return the taskname
     */
    public String getTaskname() {
        return taskname;
    }

    /**
     * @param taskname the taskname to set
     */
    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    /**
     * @return the sourceid
     */
    public int getSourceid() {
        return sourceid;
    }

    /**
     * @param sourceid the sourceid to set
     */
    public void setSourceid(int sourceid) {
        this.sourceid = sourceid;
    }

    /**
     * @return the sourcetype
     */
    public int getSourcetype() {
        return sourcetype;
    }

    /**
     * @param sourcetype the sourcetype to set
     */
    public void setSourcetype(int sourcetype) {
        this.sourcetype = sourcetype;
    }

    /**
     * @return the taskmode1
     */
    public int getTaskmode1() {
        return taskmode1;
    }

    /**
     * @param taskmode1 the taskmode1 to set
     */
    public void setTaskmode1(int taskmode1) {
        this.taskmode1 = taskmode1;
    }

    /**
     * @return the taskmode2
     */
    public int getTaskmode2() {
        return taskmode2;
    }

    /**
     * @param taskmode2 the taskmode2 to set
     */
    public void setTaskmode2(int taskmode2) {
        this.taskmode2 = taskmode2;
    }

    /**
     * @return the taskminute
     */
    public String getTaskminute() {
        return taskminute;
    }

    /**
     * @param taskminute the taskminute to set
     */
    public void setTaskminute(String taskminute) {
        this.taskminute = taskminute;
    }

    /**
     * @return the taskhour
     */
    public String getTaskhour() {
        return taskhour;
    }

    /**
     * @param taskhour the taskhour to set
     */
    public void setTaskhour(String taskhour) {
        this.taskhour = taskhour;
    }

    /**
     * @return the taskday
     */
    public String getTaskday() {
        return taskday;
    }

    /**
     * @param taskday the taskday to set
     */
    public void setTaskday(String taskday) {
        this.taskday = taskday;
    }

    /**
     * @return the taskweek
     */
    public String getTaskweek() {
        return taskweek;
    }

    /**
     * @param taskweek the taskweek to set
     */
    public void setTaskweek(String taskweek) {
        this.taskweek = taskweek;
    }

    /**
     * @return the taskmonth
     */
    public String getTaskmonth() {
        return taskmonth;
    }

    /**
     * @param taskmonth the taskmonth to set
     */
    public void setTaskmonth(String taskmonth) {
        this.taskmonth = taskmonth;
    }

    /**
     * @return the enabled
     */
    public int getEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the taskid
     */
    public String getTaskid() {
        return taskid;
    }

    /**
     * @param taskid the taskid to set
     */
    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

 
    /**
     * @return the selAccHour
     */
    public String getSelAccHour() {
        return selAccHour;
    }

    /**
     * @param selAccHour the selAccHour to set
     */
    public void setSelAccHour(String selAccHour) {
        this.selAccHour = selAccHour;
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

}
