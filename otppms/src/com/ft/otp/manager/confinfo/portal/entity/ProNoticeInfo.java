/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.portal.entity;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户门户系统公告通知实体
 *
 * @Date in Apr 27, 2013,1:44:52 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class ProNoticeInfo extends BaseEntity {
    
    private int id; 
    private int systype = -1; //通知级别
    private String createuser; //创建者
    private int createtime; //通知创建时间
    private int expiretime; //通知过期时间
    private String title; //通知标题
    private String content; //通知内容
    
    private String tempDeathTime;
    private String createtimeStr = "";
    private String expiretimeStr = "";
    
    
    private int starttime;
    private int endtime;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getSystype() {
        return systype;
    }
    public void setSystype(int systype) {
        this.systype = systype;
    }
    public String getCreateuser() {
        return createuser;
    }
    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }
    public int getCreatetime() {
        return createtime;
    }
    public void setCreatetime(int createtime) {
        this.createtime = createtime;
    }
    public int getExpiretime() {
        return expiretime;
    }
    public void setExpiretime(int expiretime) {
        this.expiretime = expiretime;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = StrTool.cleanXSS(content);
    }
    
    public String getCreatetimeStr() {
        if (getCreatetime() != 0) {
            return DateTool.dateToStr(getCreatetime(), true);
        }
        return createtimeStr;
    }
    
    public String getExpiretimeStr() {
        if (getExpiretime() != 0) {
            return DateTool.dateToStr(getExpiretime(), true);
        }
        return expiretimeStr;
    }
    public String getTempDeathTime() {
        return tempDeathTime;
    }
    public void setTempDeathTime(String tempDeathTime) {
        this.tempDeathTime = tempDeathTime;
        if (StrTool.strNotNull(tempDeathTime)) {
            this.expiretime = DateTool.dateToInt(DateTool.stringToDate(tempDeathTime));
        }
    }
    public int getStarttime() {
        return starttime;
    }
    public void setStarttime(int starttime) {
        this.starttime = starttime;
    }
    public int getEndtime() {
        return endtime;
    }
    public void setEndtime(int endtime) {
        this.endtime = endtime;
    }

}
