/**
 *Administrator
 */
package com.ft.otp.manager.token.entity;

import java.util.List;
import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.manager.tokenspec.entity.TokenSpec;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 令牌对象实体类
 *
 * @Date in Apr 18, 2011,11:24:50 AM
 *
 * @author ZJY
 */
public class TokenInfo extends BaseEntity {

    private String token = null; //令牌号

    private int enabled = -1; //是否启用（0、未启用 1、启用）
    private int locked = -1; //是否锁定（0、未锁定 1、临时锁定 2、永久锁定）
    private int lost = -1; //是否挂失（0、未挂失 1、挂失）
    private int logout = -1; //是否作废（0、未作废 1、作废）

    private String pubkey = null;
    private String authnum = null;
    private int authmethod = 0; //令牌认证方式 0、认证动态口令 ，1、认证应急口令
    private String empin = ""; //令牌应急口令 RC4加密
    private int empindeath = 0; //应急口令过期UTC秒数
    private int expiretime = 0; //令牌过期UTC秒数
    private int syncwnd = 10;
    private int temploginerrcnt = 0;
    private int longloginerrcnt = 0;
    private int loginlocktime = 0;
    private int driftcount = -1;

    //物理类型(0 硬件令牌，1 手机令牌，2 软件令牌,3 国密令牌(注:非物理类型，已废除。)
    //4 刮刮卡令牌,5 矩阵卡令牌,6 短信令牌,7 SD卡令牌)
    private int physicaltype = -1;
    private int producttype = -1; //令牌产品类型
    private String specid = ""; //令牌规格 
    private Integer domainid = null; //令牌所属的域
    private int[] domainids = null;//多个域
    private Integer orgunitid = null; //令牌所属的组织机构
    private String[] orgunitids = null; //多个组织机构
    private int gensmstime; //短信口令产生时间，UTC秒数
    private int importtime; //导入时间
    private int distributetime;//软件令牌分发时间
    private String crauthnum = null;
    private int authtime = 0;
    private int crauthtime = 0;
    private String authotp = "";
    private String crauthotp = "";
    private String crauthdata = "";
    private String cractivecode = "";
    private int pubkeystate = -2; //查询所有 -1 c100 c200 不适应，0 未激活，1 激活未认证，2 已激活
    private String newpubkey = "";
    private int cractivetime = 0;
    private int cractivecount = 0;
    private String vendorid = "-1";

    //手机令牌表字段
    private String pubkeyfactor; //手机令牌种子因子
    private String exttype;

    private int bind = -1;

    //统计字段
    private int prdLabel;
    private int phyLabel;
    private int countValue;

    //令牌过期时间是>= 还是<=  0:过期时间<= 选择的时间 、1： 过期时间>= 选择的时间
    private int expiretimeType = 0;
    // 0表示使用两个普通口令进行同步、1使用挑战型口令+普通口令进行同步、2使用两个挑战应答口令同步
    private int syncmode = 0; 

    //令牌起始序列号
    private String tokenStart;
    private String tokenEnd;
    private String empindeathStr;
    //应急口令过期最大有效期(从管理中心配置中获取)
    private String empindeathMaxStr;

    private String domainOrgunitName;
    //手机令牌分发默认PIN码
    public String softtoken_distribute_pwd;

    private List<?> userIds = null;

    private Integer isFliterTag = null; // null不过滤令牌列表 超级管理员使用 1过滤根据管理员id过滤用户 
    private String currentAdminId; //当前管理员id 根据此id过滤 当前管理员所管理的机构或域下的令牌

    private int bindTag;//绑定状态标示 展示时用到 

    private Integer newDomainId; //新令牌所属的域
    private Integer newOrgunitId; //新令牌所属的组织机构

    private int upPageTag = 1;//1分页 0不分页

    private TokenSpec tokenSpec = null;
    private List<?> tokenIds = null;

    /**
     * 多厂商集成，提供给第三方的令牌属性
     */
    private String tokenSN;
    private String privData;
    //private String vendorId;
    private String vendorName;
    private int expired;
    private int[] orgunitIds;
    private String[] domainIds;
    private int auflag = 0; // 判断SQL拼接，0代表用户绑定令牌；1代表管理员绑定令牌
    private int tflag = 0; // 交换令牌时判断SQL拼接，0代表不拼接；1代表拼接
    private int orgFlag = 0;
    private String userId;
    private int nowtime = 0;
    private int logFlag; // 日志记录传值参数
    private String oldOrgunitName = null;
    
    public String getOldOrgunitName() {
		return oldOrgunitName;
	}

	public void setOldOrgunitName(String oldOrgunitName) {
		this.oldOrgunitName = oldOrgunitName;
	}

	public int getLogFlag() {
		return logFlag;
	}

	public void setLogFlag(int logFlag) {
		this.logFlag = logFlag;
	}

	public int getNowtime() {
		return nowtime;
	}

	public void setNowtime(int nowtime) {
		this.nowtime = nowtime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<?> getTokenIds() {
        return tokenIds;
    }

    public void setTokenIds(List<?> tokenIds) {
        this.tokenIds = tokenIds;
    }

    public int getOrgFlag() {
        return orgFlag;
    }

    public void setOrgFlag(int orgFlag) {
        this.orgFlag = orgFlag;
    }

    public int getTflag() {
        return tflag;
    }

    public void setTflag(int tflag) {
        this.tflag = tflag;
    }

    public int getAuflag() {
        return auflag;
    }

    public void setAuflag(int auflag) {
        this.auflag = auflag;
    }

    public String[] getDomainIds() {
        return domainIds;
    }

    public void setDomainIds(String[] domainIds) {
        this.domainIds = domainIds;
    }

    public int[] getOrgunitIds() {
        return orgunitIds;
    }

    public void setOrgunitIds(int[] orgunitIds) {
        this.orgunitIds = orgunitIds;
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
     * @return the pubkey
     */
    public String getPubkey() {
        return pubkey;
    }

    /**
     * @param pubkey the pubkey to set
     */
    public void setPubkey(String pubkey) {
        this.pubkey = pubkey;
    }

    /**
     * @return the authnum
     */
    public String getAuthnum() {
        return authnum;
    }

    /**
     * @param authnum the authnum to set
     */
    public void setAuthnum(String authnum) {
        this.authnum = authnum;
    }

    public String getExpiretimeStr() {
        if (getExpiretime() != 0) {
            return DateTool.dateConvertStr(getExpiretime());
        }
        return "";
    }
    
    public String getExpiretimeView() {
        if (getExpiretime() != 0) {
            return DateTool.dateToStr(getExpiretime(), true);
        }
        return "";
    }

    /**
     * @return the syncwnd
     */
    public int getSyncwnd() {
        return syncwnd;
    }

    /**
     * @param syncwnd the syncwnd to set
     */
    public void setSyncwnd(int syncwnd) {
        this.syncwnd = syncwnd;
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
     * @return the lost
     */
    public int getLost() {
        return lost;
    }

    /**
     * @param lost the lost to set
     */
    public void setLost(int lost) {
        this.lost = lost;
    }

    /**
     * @return the logout
     */
    public int getLogout() {
        return logout;
    }

    /**
     * @param logout the logout to set
     */
    public void setLogout(int logout) {
        this.logout = logout;
    }

    /**
     * @return the physicaltype
     */
    public int getPhysicaltype() {
        return physicaltype;
    }

    /**
     * @param physicaltype the physicaltype to set
     */
    public void setPhysicaltype(int physicaltype) {
        this.physicaltype = physicaltype;
    }

    /**
     * @return the pubkeyfactor
     */
    public String getPubkeyfactor() {
        return pubkeyfactor;
    }

    /**
     * @param pubkeyfactor the pubkeyfactor to set
     */
    public void setPubkeyfactor(String pubkeyfactor) {
        this.pubkeyfactor = pubkeyfactor;
    }

    /**
     * @return the exttype
     */
    public String getExttype() {
        return exttype;
    }

    /**
     * @param exttype the exttype to set
     */
    public void setExttype(String exttype) {
        this.exttype = exttype;
    }

    /**
     * @return the userIds
     */
    public List<?> getUserIds() {
        return userIds;
    }

    /**
     * @param userIds the userIds to set
     */
    public void setUserIds(List<?> userIds) {
        this.userIds = userIds;
    }

    /**
     * @return the authmethod
     */
    public int getAuthmethod() {
        return authmethod;
    }

    /**
     * @param authmethod the authmethod to set
     */
    public void setAuthmethod(int authmethod) {
        this.authmethod = authmethod;
    }

    /**
     * @return the empin
     */
    public String getEmpin() {
        return empin;
    }

    /**
     * @param empin the empin to set
     */
    public void setEmpin(String empin) {
        this.empin = empin;
    }

    /**
     * @return the expiretime
     */
    public int getExpiretime() {
        return expiretime;
    }

    /**
     * @param expiretime the expiretime to set
     */
    public void setExpiretime(int expiretime) {
        this.expiretime = expiretime;
    }

    public String getEmpindeathStr() {
        if (getEmpindeath() != 0) {
            empindeathStr = DateTool.dateToStr(getEmpindeath(), true);
        }
        return empindeathStr;
    }

    /**
     * 应急口令时间
     * 
     * @Date in Apr 5, 2013,10:49:45 AM
     * @return
     */
    public int getEmpindeath() {
        if (StrTool.strNotNull(empindeathStr)) {
            empindeath = DateTool.dateToInt(DateTool.strToDateFull(empindeathStr));
        }
        return empindeath;
    }

    /**
     * @param empindeath the empindeath to set
     */
    public void setEmpindeath(int empindeath) {
        this.empindeath = empindeath;
    }

    /**
     * @return the producttype
     */
    public int getProducttype() {
        return producttype;
    }

    /**
     * @param producttype the producttype to set
     */
    public void setProducttype(int producttype) {
        this.producttype = producttype;
    }

    /**
     * @return the specid
     */
    public String getSpecid() {
        return specid;
    }

    /**
     * @param specid the specid to set
     */
    public void setSpecid(String specid) {
        this.specid = specid;
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

    public String getLoginlocktimeStr() {
        if (getLoginlocktime() != 0) {
            return DateTool.dateToStr(getLoginlocktime(), true);
        }
        return "";
    }

    /**
     * @return the driftcount
     */
    public int getDriftcount() {
        return driftcount;
    }

    /**
     * @param driftcount the driftcount to set
     */
    public void setDriftcount(int driftcount) {
        this.driftcount = driftcount;
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
     * @return the prdLabel
     */
    public int getPrdLabel() {
        return prdLabel;
    }

    /**
     * @param prdLabel the prdLabel to set
     */
    public void setPrdLabel(int prdLabel) {
        this.prdLabel = prdLabel;
    }

    /**
     * @return the phyLabel
     */
    public int getPhyLabel() {
        return phyLabel;
    }

    /**
     * @param phyLabel the phyLabel to set
     */
    public void setPhyLabel(int phyLabel) {
        this.phyLabel = phyLabel;
    }

    /**
     * @return the countValue
     */
    public int getCountValue() {
        return countValue;
    }

    /**
     * @param countValue the countValue to set
     */
    public void setCountValue(int countValue) {
        this.countValue = countValue;
    }

    /**
     * @return the domainid
     */
    public Integer getDomainid() {
        return domainid;
    }

    /**
     * @param domainid the domainid to set
     */
    public void setDomainid(Integer domainid) {
        this.domainid = domainid;
    }

    /**
     * @return the orgunitid
     */
    public Integer getOrgunitid() {
        return orgunitid;
    }

    /**
     * @param orgunitid the orgunitid to set
     */
    public void setOrgunitid(Integer orgunitid) {
        this.orgunitid = orgunitid;
    }

    /**
     * @return the gensmstime
     */
    public int getGensmstime() {
        return gensmstime;
    }

    /**
     * @param gensmstime the gensmstime to set
     */
    public void setGensmstime(int gensmstime) {
        this.gensmstime = gensmstime;
    }

    /**
     * @return the importtime
     */
    public int getImporttime() {
        return importtime;
    }

    /**
     * @param importtime the importtime to set
     */
    public void setImporttime(int importtime) {
        this.importtime = importtime;
    }

    /**
     * @return the tokenStart
     */
    public String getTokenStart() {
        return tokenStart;
    }

    /**
     * @param tokenStart the tokenStart to set
     */
    public void setTokenStart(String tokenStart) {
        this.tokenStart = tokenStart;
    }

    /**
     * @return the tokenEnd
     */
    public String getTokenEnd() {
        return tokenEnd;
    }

    /**
     * @param tokenEnd the tokenEnd to set
     */
    public void setTokenEnd(String tokenEnd) {
        this.tokenEnd = tokenEnd;
    }

    /**
     * @param empindeathStr the empindeathStr to set
     */
    public void setEmpindeathStr(String empindeathStr) {
        this.empindeathStr = empindeathStr;
    }

    /**
     * @return the empindeathMaxStr
     */
    public String getEmpindeathMaxStr() {
        return empindeathMaxStr;
    }

    /**
     * @param empindeathMaxStr the empindeathMaxStr to set
     */
    public void setEmpindeathMaxStr(String empindeathMaxStr) {
        this.empindeathMaxStr = empindeathMaxStr;
    }

    public String getDomainOrgunitName() {
        return domainOrgunitName;
    }

    public void setDomainOrgunitName(String domainOrgunitName) {
        this.domainOrgunitName = domainOrgunitName;
    }

    /**
     * @return the distributetime
     */
    public int getDistributetime() {
        return distributetime;
    }

    /**
     * @param distributetime the distributetime to set
     */
    public void setDistributetime(int distributetime) {
        this.distributetime = distributetime;
    }

    /**
     * @return the softtoken_distribute_pwd
     */
    public String getSofttoken_distribute_pwd() {
        return softtoken_distribute_pwd;
    }

    /**
     * @param softtoken_distribute_pwd the softtoken_distribute_pwd to set
     */
    public void setSofttoken_distribute_pwd(String softtoken_distribute_pwd) {
        this.softtoken_distribute_pwd = softtoken_distribute_pwd;
    }

    /**
     * @return the isFliterTag
     */
    public Integer getIsFliterTag() {
        return isFliterTag;
    }

    /**
     * @param isFliterTag the isFliterTag to set
     */
    public void setIsFliterTag(Integer isFliterTag) {
        this.isFliterTag = isFliterTag;
    }

    /**
     * @return the currentAdminId
     */
    public String getCurrentAdminId() {
        return currentAdminId;
    }

    /**
     * @param currentAdminId the currentAdminId to set
     */
    public void setCurrentAdminId(String currentAdminId) {
        this.currentAdminId = currentAdminId;
    }

    /**
     * @return the bindTag
     */
    public int getBindTag() {
        return bindTag;
    }

    /**
     * @param bindTag the bindTag to set
     */
    public void setBindTag(int bindTag) {
        this.bindTag = bindTag;
    }

    public String getCrauthnum() {
		return crauthnum;
	}

	public void setCrauthnum(String crauthnum) {
		this.crauthnum = crauthnum;
	}

	/**
     * @return the authtime
     */
    public int getAuthtime() {
        return authtime;
    }

    /**
     * @param authtime the authtime to set
     */
    public void setAuthtime(int authtime) {
        this.authtime = authtime;
    }

    /**
     * @return the crauthtime
     */
    public int getCrauthtime() {
        return crauthtime;
    }

    /**
     * @param crauthtime the crauthtime to set
     */
    public void setCrauthtime(int crauthtime) {
        this.crauthtime = crauthtime;
    }

    /**
     * @return the authotp
     */
    public String getAuthotp() {
        return authotp;
    }

    /**
     * @param authotp the authotp to set
     */
    public void setAuthotp(String authotp) {
        this.authotp = authotp;
    }

    /**
     * @return the crauthotp
     */
    public String getCrauthotp() {
        return crauthotp;
    }

    /**
     * @param crauthotp the crauthotp to set
     */
    public void setCrauthotp(String crauthotp) {
        this.crauthotp = crauthotp;
    }

    /**
     * @return the crauthdata
     */
    public String getCrauthdata() {
        return crauthdata;
    }

    /**
     * @param crauthdata the crauthdata to set
     */
    public void setCrauthdata(String crauthdata) {
        this.crauthdata = crauthdata;
    }

    /**
     * @return the cractivecode
     */
    public String getCractivecode() {
        return cractivecode;
    }

    /**
     * @param cractivecode the cractivecode to set
     */
    public void setCractivecode(String cractivecode) {
        this.cractivecode = cractivecode;
    }

    /**
     * @return the pubkeystate
     */
    public int getPubkeystate() {
        return pubkeystate;
    }

    /**
     * @param pubkeystate the pubkeystate to set
     */
    public void setPubkeystate(int pubkeystate) {
        this.pubkeystate = pubkeystate;
    }

    /**
     * @return the newpubkey
     */
    public String getNewpubkey() {
        return newpubkey;
    }

    /**
     * @param newpubkey the newpubkey to set
     */
    public void setNewpubkey(String newpubkey) {
        this.newpubkey = newpubkey;
    }

    /**
     * @return the cractivetime
     */
    public int getCractivetime() {
        return cractivetime;
    }

    /**
     * @param cractivetime the cractivetime to set
     */
    public void setCractivetime(int cractivetime) {
        this.cractivetime = cractivetime;
    }

    /**
     * @return the cractivecount
     */
    public int getCractivecount() {
        return cractivecount;
    }

    /**
     * @param cractivecount the cractivecount to set
     */
    public void setCractivecount(int cractivecount) {
        this.cractivecount = cractivecount;
    }

    /**
     * @return the upPageTag
     */
    public int getUpPageTag() {
        return upPageTag;
    }

    /**
     * @param upPageTag the upPageTag to set
     */
    public void setUpPageTag(int upPageTag) {
        this.upPageTag = upPageTag;
    }

    /**
     * @return the vendorid
     */
    public String getVendorid() {
        return vendorid;
    }

    /**
     * @param vendorid the vendorid to set
     */
    public void setVendorid(String vendorid) {
        this.vendorid = vendorid;
    }

    /**
     * @return the newDomainId
     */
    public Integer getNewDomainId() {
        return newDomainId;
    }

    /**
     * @param newDomainId the newDomainId to set
     */
    public void setNewDomainId(Integer newDomainId) {
        this.newDomainId = newDomainId;
    }

    /**
     * @return the newOrgunitId
     */
    public Integer getNewOrgunitId() {
        return newOrgunitId;
    }

    /**
     * @param newOrgunitId the newOrgunitId to set
     */
    public void setNewOrgunitId(Integer newOrgunitId) {
        this.newOrgunitId = newOrgunitId;
    }

    /**
     * @return the tokenSpec
     */
    public TokenSpec getTokenSpec() {
        return tokenSpec;
    }

    /**
     * @param tokenSpec the tokenSpec to set
     */
    public void setTokenSpec(TokenSpec tokenSpec) {
        this.tokenSpec = tokenSpec;
    }

    /**
     * @return the tokenSN
     */
    public String getTokenSN() {
        return tokenSN;
    }

    /**
     * @param tokenSN the tokenSN to set
     */
    public void setTokenSN(String tokenSN) {
        this.tokenSN = tokenSN;
    }

    /**
     * @return the privData
     */
    public String getPrivData() {
        return privData;
    }

    /**
     * @param privData the privData to set
     */
    public void setPrivData(String privData) {
        this.privData = privData;
    }

    //    /**
    //     * @return the vendorId
    //     */
    //    public String getVendorId() {
    //        return vendorId;
    //    }
    //
    //    /**
    //     * @param vendorId the vendorId to set
    //     */
    //    public void setVendorId(String vendorId) {
    //        this.vendorId = vendorId;
    //    }

    /**
     * @return the vendorName
     */
    public String getVendorName() {
        return vendorName;
    }

    /**
     * @param vendorName the vendorName to set
     */
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    /**
     * @return the expired
     */
    public int getExpired() {
        return expired;
    }

    /**
     * @param expired the expired to set
     */
    public void setExpired(int expired) {
        this.expired = expired;
    }

    public int[] getDomainids() {
		return domainids;
	}

	public void setDomainids(int[] domainids) {
		this.domainids = domainids;
	}

	/**
     * @return the orgunitids
     */
    public String[] getOrgunitids() {
        return orgunitids;
    }

    /**
     * @param orgunitids the orgunitids to set
     */
    public void setOrgunitids(String[] orgunitids) {
        this.orgunitids = orgunitids;
    }

    /**
     * @return the expiretimeType
     */
    public int getExpiretimeType() {
        return expiretimeType;
    }

    /**
     * @param expiretimeType the expiretimeType to set
     */
    public void setExpiretimeType(int expiretimeType) {
        this.expiretimeType = expiretimeType;
    }

    public int getSyncmode() {
        return syncmode;
    }

    public void setSyncmode(int syncmode) {
        this.syncmode = syncmode;
    }
}
