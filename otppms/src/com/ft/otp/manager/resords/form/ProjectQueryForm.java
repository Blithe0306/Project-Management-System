/**
 *qiuju
 */
package com.ft.otp.manager.resords.form;

import com.ft.otp.manager.resords.entity.Resords;

/**
 * 类、接口等说明
 *
 * @Date in 2015-5-12,上午10:48:48
 *
 * @version v1.0
 *
 * @author qiuju
 */
public class ProjectQueryForm {
    
    private Resords resords= new Resords();
    private String recordUser; // 记录人员
    private String prjid; // 项目编号


    public Resords getResords() {
        return resords;
    }
    public void setResords(Resords resords) {
        this.resords = resords;
    }
    public String getRecordUser() {
		return recordUser;
	}
	public void setRecordUser(String recordUser) {
        resords.setRecordUser(recordUser.trim());
    }
    public String getPrjid() {
        return prjid;
    }
    public void setPrjid(String prjid) {
        prjid = prjid.trim();
        this.prjid = prjid;
        resords.setPrjid(prjid);
    }
   
}
