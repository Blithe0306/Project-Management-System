/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.agentconf.entity;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.util.tool.StrTool;

/**
 * 认证代理配置表实体信息
 *
 * @Date in Jan 28, 2013,4:19:24 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class AgentConfInfo extends BaseEntity {
    
    private int id; //主键，自增
    private String confname; //配置名称
    private int type = 0; //登录保护代理配置类型；0：windows登录保护代理；1：linux登录保护代理。
    private int userformat = 0; //用户名格式，单选；0：“user@ip”格式；1：“user”格式；2：“user@domain”格式； 3：“domain user”格式
    private int localprotect = 0; //本地登录保护，单选；0：不保护；1：保护本地账号；2：保护域账号；3：同时保护本地账号和域账号。Windows登录保护代理使用
    private int remoteprotect = 0; //远程登录保护，单选；0：不保护；1：保护本地账号；2：保护域账号；3：同时保护本地账号和域账号。Windows登录保护代理使用
    private int uacprotect = 0; //UAC登录保护，单选；0：不保护；1：保护本地账号；2：保护域账号；3：同时保护本地账号和域账号。Windows登录保护代理使用
    private int unboundlogin = 0; //是否允许未绑定令牌的用户登录，0否，1是
    private String descp; //描述信息
    
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getConfname() {
        return confname;
    }
    public void setConfname(String confname) {
        this.confname = confname;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getUserformat() {
        return userformat;
    }
    public void setUserformat(int userformat) {
        this.userformat = userformat;
    }
    public int getLocalprotect() {
        return localprotect;
    }
    public void setLocalprotect(int localprotect) {
        this.localprotect = localprotect;
    }
    public int getRemoteprotect() {
        return remoteprotect;
    }
    public void setRemoteprotect(int remoteprotect) {
        this.remoteprotect = remoteprotect;
    }
    public int getUacprotect() {
        return uacprotect;
    }
    public void setUacprotect(int uacprotect) {
        this.uacprotect = uacprotect;
    }
    public int getUnboundlogin() {
        return unboundlogin;
    }
    public void setUnboundlogin(int unboundlogin) {
        this.unboundlogin = unboundlogin;
    }
    public String getDescp() {
        return descp;
    }
    public void setDescp(String descp) {
        this.descp = StrTool.cleanXSS(descp);
    }

}
