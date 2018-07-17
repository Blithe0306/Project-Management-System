/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 * @Date Dec 25, 2014,8:59:16 AM
 */
package com.ft.otp.manager.prjinfo.entity;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.common.config.PrjinfoTypeConfig;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 类、接口等说明
 *
 * @Date Dec 25, 2014,8:59:16 AM
 * @version v1.0
 * @author ZWX
 */
public class Prjinfo  extends BaseEntity{

    private String id;
    private String prjdesc;
    private String type;
    private String typeStr;
    private String prjid;
    private String prjname;
    private String svn;
    private String bug;
    private String path;
    private String content;
    private String developer;
    private String tester;
    private long createtime;
    private String createtimeStr;
    private long updatetime;
    private String operator;
    private String results;
    private int startTime;
    private int endTime;

    public String getCreatetimeStr() {

        if (getCreatetime() != 0) {
            return DateTool.dateToStr(getCreatetime(), true);
        }
        return createtimeStr;
    }
    public void setCreatetimeStr(String createtimeStr) {
        this.createtimeStr = createtimeStr;
    }
    public String getTypeStr() {
        if (StrTool.strNotNull(getType())) {
            PrjinfoType bean = PrjinfoTypeConfig.getTypeMap().get(getType());
            return typeStr = bean.getTypeName();
        }
        return typeStr;
    }
    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
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
    public String getPrjname() {
        return prjname;
    }
    public void setPrjname(String prjname) {
        this.prjname = prjname;
    }
    public long getUpdatetime() {
        return updatetime;
    }
    public void setUpdatetime(long updatetime) {
        this.updatetime = updatetime;
    }
    public String getResults() {
        return results;
    }
    public void setResults(String results) {
        this.results = results;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPrjdesc() {
        return prjdesc;
    }
    public void setPrjdesc(String prjdesc) {
        this.prjdesc = prjdesc;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getPrjid() {
        return prjid;
    }
    public void setPrjid(String prjid) {
        this.prjid = prjid;
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
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getDeveloper() {
        return developer;
    }
    public void setDeveloper(String developer) {
        this.developer = developer;
    }
    public String getTester() {
        return tester;
    }
    public void setTester(String tester) {
        this.tester = tester;
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
}
