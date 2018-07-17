/**
 *Administrator
 */
package com.ft.otp.manager.admin.user.entity;

import java.util.List;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理员实体类功能说明
 *
 * @Date in May 5, 2011,10:58:31 AM
 *
 * @author ZJY
 */
public class AdminUser extends BaseEntity {

    //数据库属性
    private String adminid; //管理员登录账户
    private String realname; //管理员真实名称
    private String rolenameStr;//查询的角色名称
    private int domainid;// 查询的域
    private int orgunitid;//查询的组织机构
    /*
     *本地认证模式
     * 0 只验证静态密码
     * 1 只验证OTP
     * 2 验证静态密码和OTP
     */
    private int localauth = 0;//只验证静态密码
    private String pwd; //加密后的密码，RC4加密
    private int pwdsettime; //密码设置时间，可以定期提示修改，为0永不提示
    private int locked; //管理员是否锁定 0、未锁定 1、临时锁定 2、永久锁定
    private int temploginerrcnt; //临时锁定连续错误次数
    private int longloginerrcnt; //永久锁定连续错误次数
    private int loginlocktime; //临时或永久锁定时间戳，UTC秒数
    private int logintime; //上次登时时间
    private int logincnt; //总计登时次数
    private int getpwddeath; // 静态密码找回有效期，0为不找回状态

    private String createuser; //管理员创建者
    private int enabled; //是否启用 0、禁用 1、启用
    private String email; //邮箱地址
    private String address; //通讯地址
    private String tel; //电话
    private String cellphone; //手机
    private int createtime; //创建时间
    private String descp; //描述信息

    //扩展属性（非数据库属性）
    private List<?> adminRoles; //管理员角色列表
    private List<?> hidAdminRoles;

    private int bind = 0; //用户是否绑定令牌0全部 1未绑定 2已绑定
    private List<?> tokens; //用户绑定的令牌信息
    private List<?> hiddenTkns;//用户绑定的令牌隐藏域，对于更换令牌时与新选择的令牌比对。
    private String token; //令牌号 用于查询

    private String pwdOld; //用于修改密码
    private String pwdConf; //确认密码
    private String querySource = "";//查询来源
    private String logintimeStr = "";
    private String createtimeStr = "";
    private String pwdsettimeStr = "";
    private String loginlocktimeStr = "";

    private String orgunitIds = "";//组织机构id串  域id：组织机构id 键值对 domainid1:0,domainid1：orgunitid1,domainid2:orgunitid2,domainid3:orgunitid3
    private String orgunitNames = "";//组织机构id串 机构1,机构2,机构3, 格式 可空
    
    private String isRecordLog;//是否记录日志，0为登录时不记录日志
    private List<?> orgunitIdList;
    
    private String reAdminId = "";  // 用于找回密码,账号
    private String rePwd = "";   // 用于找回密码,密码
    
	public String getReAdminId() {
		return reAdminId;
	}

	public void setReAdminId(String reAdminId) {
		this.reAdminId = StrTool.cleanActivate(reAdminId);
	}

	public String getRePwd() {
		return rePwd;
	}

	public void setRePwd(String rePwd) {
		this.rePwd = StrTool.cleanActivate(rePwd);
	}

	public List<?> getOrgunitIdList() {
		return orgunitIdList;
	}

	public void setOrgunitIdList(List<?> orgunitIdList) {
		this.orgunitIdList = orgunitIdList;
	}

	public AdminUser() {

    }
    
    public int getGetpwddeath() {
        return getpwddeath;
    }

    public void setGetpwddeath(int getpwddeath) {
        this.getpwddeath = getpwddeath;
    }

    /**
     * @return the pwdsettime
     */
    public long getPwdsettime() {
        return pwdsettime;
    }

    /**
     * @return the locked
     */
    public int getLocked() {
        return locked;
    }

    /**
     * @param locked the locked to set
     */
    public void setLocked(int locked) {
        this.locked = locked;
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

    /**
     * @return the adminRoles
     */
    public List<?> getAdminRoles() {
        return adminRoles;
    }

    /**
     * @param adminRoles the adminRoles to set
     */
    public void setAdminRoles(List<?> adminRoles) {
        this.adminRoles = adminRoles;
    }

    /**
     * @return the hidAdminRoles
     */
    public List<?> getHidAdminRoles() {
        return hidAdminRoles;
    }

    /**
     * @param hidAdminRoles the hidAdminRoles to set
     */
    public void setHidAdminRoles(List<?> hidAdminRoles) {
        this.hidAdminRoles = hidAdminRoles;
    }

    /**
     * @return the createuser
     */
    public String getCreateuser() {
        return createuser;
    }

    /**
     * @param createuser the createuser to set
     */
    public void setCreateuser(String createuser) {
        this.createuser = createuser;
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
     * @return the pwdOld
     */
    public String getPwdOld() {
        return pwdOld;
    }

    /**
     * @param pwdOld the pwdOld to set
     */
    public void setPwdOld(String pwdOld) {
        this.pwdOld = pwdOld;
    }

    /**
     * @return the pwdConf
     */
    public String getPwdConf() {
        return pwdConf;
    }

    /**
     * @param pwdConf the pwdConf to set
     */
    public void setPwdConf(String pwdConf) {
        this.pwdConf = pwdConf;
    }

    /**
     * @return the querySource
     */
    public String getQuerySource() {
        return querySource;
    }

    /**
     * @param querySource the querySource to set
     */
    public void setQuerySource(String querySource) {
        this.querySource = querySource;
    }

    /**
     * @return the adminid
     */
    public String getAdminid() {
        return adminid;
    }

    /**
     * @param adminid the adminid to set
     */
    public void setAdminid(String adminid) {
        int exchangeTag = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_COMMON,
                ConfConstant.USERID_FORMAT_TYPE));

        // 小写转换
        if (StrTool.strNotNull(adminid) && exchangeTag == NumConstant.common_number_1) {
            adminid = adminid.toLowerCase();
        }
        this.adminid = adminid;
    }

    /**
     * @return the realname
     */
    public String getRealname() {
        return realname;
    }

    /**
     * @param realname the realname to set
     */
    public void setRealname(String realname) {
        this.realname = realname;
    }

    /**
     * @return the pwd
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * @param pwd the pwd to set
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * @return the temploginerrcnt
     */
    public int getTemploginerrcnt() {
        return temploginerrcnt;
    }

    /**
     * @param temploginerrcnt the temploginerrcnt to set
     */
    public void setTemploginerrcnt(int temploginerrcnt) {
        this.temploginerrcnt = temploginerrcnt;
    }

    /**
     * @return the longloginerrcnt
     */
    public int getLongloginerrcnt() {
        return longloginerrcnt;
    }

    /**
     * @param longloginerrcnt the longloginerrcnt to set
     */
    public void setLongloginerrcnt(int longloginerrcnt) {
        this.longloginerrcnt = longloginerrcnt;
    }

    /**
     * @return the loginlocktime
     */
    public int getLoginlocktime() {
        return loginlocktime;
    }

    /**
     * @param loginlocktime the loginlocktime to set
     */
    public void setLoginlocktime(int loginlocktime) {
        this.loginlocktime = loginlocktime;
    }

    /**
     * @return the logintime
     */
    public int getLogintime() {
        return logintime;
    }

    /**
     * @param logintime the logintime to set
     */
    public void setLogintime(int logintime) {
        this.logintime = logintime;
    }

    /**
     * @return the logincnt
     */
    public int getLogincnt() {
        return logincnt;
    }

    /**
     * @param logincnt the logincnt to set
     */
    public void setLogincnt(int logincnt) {
        this.logincnt = logincnt;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the tel
     */
    public String getTel() {
        return tel;
    }

    /**
     * @param tel the tel to set
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * @return the cellphone
     */
    public String getCellphone() {
        return cellphone;
    }

    /**
     * @param cellphone the cellphone to set
     */
    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    /**
     * @return the createtime
     */
    public int getCreatetime() {
        return createtime;
    }

    /**
     * @param createtime the createtime to set
     */
    public void setCreatetime(int createtime) {
        this.createtime = createtime;
    }

    /**
     * @param pwdsettime the pwdsettime to set
     */
    public void setPwdsettime(int pwdsettime) {
        this.pwdsettime = pwdsettime;
    }

    /**
     * @return the createtimeStr
     */
    public String getCreatetimeStr() {
        if (getCreatetime() != 0) {
            return DateTool.dateToStr(getCreatetime(), true);
        }
        return createtimeStr;
    }

    /**
     * @return the pwdsettimeStr
     */
    public String getPwdsettimeStr() {
        if (getPwdsettime() != 0) {
            return DateTool.dateToStr(getPwdsettime(), true);
        }
        return pwdsettimeStr;
    }

    /**
     * @return the loginlocktimeStr
     */
    public String getLoginlocktimeStr() {
        return loginlocktimeStr;
    }

    public void setLoginlocktimeStr(String loginlocktimeStr) {
        this.loginlocktimeStr = loginlocktimeStr;
    }

    /**
     * @return the orgunitIds
     */
    public String getOrgunitIds() {
        return orgunitIds;
    }

    /**
     * @param orgunitIds the orgunitIds to set
     */
    public void setOrgunitIds(String orgunitIds) {
        this.orgunitIds = orgunitIds;
    }

    /**
     * @return the orgunitNames
     */
    public String getOrgunitNames() {
        return orgunitNames;
    }

    /**
     * @param orgunitNames the orgunitNames to set
     */
    public void setOrgunitNames(String orgunitNames) {
        this.orgunitNames = orgunitNames;
    }

    /**
     * @return the bind
     */
    public int getBind() {
        return bind;
    }

    /**
     * @param bind the bind to set
     */
    public void setBind(int bind) {
        this.bind = bind;
    }

    /**
     * @return the tokens
     */
    public List<?> getTokens() {
        return tokens;
    }

    /**
     * @param tokens the tokens to set
     */
    public void setTokens(List<?> tokens) {
        this.tokens = tokens;
    }

    /**
     * @return the hiddenTkns
     */
    public List<?> getHiddenTkns() {
        return hiddenTkns;
    }

    /**
     * @param hiddenTkns the hiddenTkns to set
     */
    public void setHiddenTkns(List<?> hiddenTkns) {
        this.hiddenTkns = hiddenTkns;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the localauth
     */
    public int getLocalauth() {
        return localauth;
    }

    /**
     * @param localauth the localauth to set
     */
    public void setLocalauth(int localauth) {
        this.localauth = localauth;
    }

    /**
     * @return the logintimeStr
     */
    public String getLogintimeStr() {
        if (getLogintime() != 0) {
            return DateTool.dateToStr(getLogintime(), true);
        }
        return logintimeStr;
    }

    /**
     * @param logintimeStr the logintimeStr to set
     */
    public void setLogintimeStr(String logintimeStr) {
        this.logintimeStr = logintimeStr;
    }

    /**
     * @return the rolenameStr
     */
    public String getRolenameStr() {
        return rolenameStr;
    }

    /**
     * @param rolenameStr the rolenameStr to set
     */
    public void setRolenameStr(String rolenameStr) {
        this.rolenameStr = rolenameStr;
    }

    /**
     * @return the domainid
     */
    public int getDomainid() {
        return domainid;
    }

    /**
     * @param domainid the domainid to set
     */
    public void setDomainid(int domainid) {
        this.domainid = domainid;
    }

    /**
     * @return the orgunitid
     */
    public int getOrgunitid() {
        return orgunitid;
    }

    /**
     * @param orgunitid the orgunitid to set
     */
    public void setOrgunitid(int orgunitid) {
        this.orgunitid = orgunitid;
    }

    public String getIsRecordLog() {
        return isRecordLog;
    }

    public void setIsRecordLog(String isRecordLog) {
        this.isRecordLog = isRecordLog;
    }

}
