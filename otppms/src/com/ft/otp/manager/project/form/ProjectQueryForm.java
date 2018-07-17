/**
 *Administrator
 */
package com.ft.otp.manager.project.form;

import java.util.Date;

import com.ft.otp.base.form.BaseQueryForm;
import com.ft.otp.manager.project.entity.Project;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;


/**
 * 定制项目查询表单
 * @Date Dec 23, 2014,2:55:02 PM
 * @version v1.0
 * @author ZWX
 */
public class ProjectQueryForm extends BaseQueryForm {

  //保存实体的查询条件
    Project project = new Project();

    private int actionResult; //操作结果
    private int startTime; //开始时间
    private int endTime; //结束时间
    private String id ;
    private String prjid; //项目编号
    private String prjname; //项目名称
    private String custname; //客户名称
    private String custid; //客户编号
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    /**
     * @param startTime the startLogTime to set
     * @throws ParseException 
     */
    public void setStartTime(String startTime) {

        Date ds = null;
        if (StrTool.strNotNull(startTime)) {
            int mark = 0;
            if (startTime.length() == 10) {
                mark = 1;
            }
            ds = DateTool.strToDate(startTime, mark);
        }

        int time = DateTool.dateToInt(ds);
        this.project.setStartTime(time);
        this.startTime = time;
    }

    /**
     * @param endTime the endLogTime to set
     */
    public void setEndTime(String endTime) {

        Date es = null;
        if (StrTool.strNotNull(endTime)) {
            int mark = 0;
            if (endTime.length() == 10) {
                mark = 2;
            }
            es = DateTool.strToDate(endTime, mark);
        }

        int time = DateTool.dateToInt(es);
        this.project.setEndTime(time);
        this.endTime = time;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public int getActionResult() {
        return actionResult;
    }

    public void setActionResult(int actionResult) {
        this.actionResult = actionResult;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
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
}
