/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.portal.form;

import java.util.Date;

import com.ft.otp.base.form.BaseQueryForm;
import com.ft.otp.manager.confinfo.portal.entity.ProNoticeInfo;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 系统通知公告查询实体
 *
 * @Date in Apr 27, 2013,3:58:42 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class NoticeQueryForm extends BaseQueryForm {

    ProNoticeInfo noticeInfo = new ProNoticeInfo();
    
    private int systype = -1;//通知等级
    private String title;//通知标题
    private String createuser;//通知创建者
    
    private String startNoticeTime; //通知创建时间   
    private String endNoticeTime;//通知有效时间
    
    private String starttime;
    private String endtime;

    public ProNoticeInfo getNoticeInfo() {
        return noticeInfo;
    }

    public void setNoticeInfo(ProNoticeInfo noticeInfo) {
        this.noticeInfo = noticeInfo;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        title = title.trim();
        this.title = title;
        noticeInfo.setTitle(title);
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        createuser = createuser.trim();
        this.createuser = createuser;
        noticeInfo.setCreateuser(createuser);
    }

    public String getStartNoticeTime() {
        return startNoticeTime;
    }

    public void setStartNoticeTime(String startNoticeTime) {
        Date date = DateTool.strToDate(startNoticeTime, 0);
        this.noticeInfo.setCreatetime(DateTool.dateToInt(date));
        this.startNoticeTime = startNoticeTime;
    }

    public String getEndNoticeTime() {
        return endNoticeTime;
    }

    public void setEndNoticeTime(String endNoticeTime) {
        Date date = DateTool.strToDate(endNoticeTime, 0);
        this.noticeInfo.setExpiretime(DateTool.dateToInt(date));
        this.endNoticeTime = endNoticeTime;
    }

    public int getSystype() {
        return systype;
    }

    public void setSystype(int systype) {
        this.systype = systype;
        noticeInfo.setSystype(systype);
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        Date date = DateTool.strToDate(starttime, 0);
        this.noticeInfo.setStarttime(DateTool.dateToInt(date));
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        Date date = DateTool.strToDate(endtime, 0);
        this.noticeInfo.setEndtime(DateTool.dateToInt(date));
        this.endtime = endtime;
    }


}
