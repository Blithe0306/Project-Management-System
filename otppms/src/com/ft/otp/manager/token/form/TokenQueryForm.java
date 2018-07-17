/**
 *Administrator
 */
package com.ft.otp.manager.token.form;

import java.util.Calendar;

import com.ft.otp.base.form.BaseQueryForm;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 令牌查询form类功能说明
 *
 * @Date in Apr 18, 2011,11:24:01 AM
 *
 * @author ZJY
 */
public class TokenQueryForm extends BaseQueryForm {

    TokenInfo tokenInfo = new TokenInfo();
    private String tokenStr;
    //产品类型(c100,c200,c300,c400,...)
    private int producttype;

    //物理类型(0 硬件令牌，1 手机令牌，2 软件令牌,3 国密令牌(注:非物理类型，已废除。)
    //        4 刮刮卡令牌,5 矩阵卡令牌,6 短信令牌,7 SD卡令牌)
    private int physicaltype;

    //令牌过期类型
    private int expiretimeType = -1; //-1：所有、0：已过期、1：一周内过期、2：一个月内过期、3：一个季度内过期、4：大于三个月过期

    //操作对象(本页选中的记录、本次查询所有记录)
    private int operObj;

    //批量执行的操作状态
    private int operBatch;

    //单个域Id
    private String domaind;
    //单个组织机构ID
    private String orgunitid;

    //域和组织机构的拼接字符串 比如格式1:1
    private String orgunitIds;

    /**
     * 令牌状态
     */
    //启用状态（所有状态、启用、未启用）
    private int enableState;

    //绑定状态（所有状态、绑定、未绑定）
    private int bindState;

    //锁定状态（所有状态、锁定、未锁定）
    private int lockedState;

    //挂失状态（所有状态、挂失、未挂失）
    private int lostState;

    //注销状态（所有状态、注销、未注销）
    private int logoutState;

    //激活状态（所有状态、初始化、激活未认证、激活已认证）
    private int pubkeyState = -2; //查询所有 -1 c100 c200 不适应，0 未激活，1 激活未认证，2 已激活

    private int tknState;

    private String tokenStart;
    private String tokenEnd;

    private String token;//令牌号

    private String vendorId;//厂商ID

    //批量查询的令牌号
    private String batchTknSn;
    private int orgFlag = 0;
    private String userId;
    
    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getOrgFlag() {
        return orgFlag;
    }

    public void setOrgFlag(int orgFlag) {
        this.orgFlag = orgFlag;
        tokenInfo.setOrgFlag(orgFlag);
    }

    /**
     * @return the tokenInfo
     */
    public TokenInfo getTokenInfo() {
        return tokenInfo;
    }

    /**
     * @param tokenInfo the tokenInfo to set
     */
    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
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
        if (StrTool.strNotNull(tokenStart)) {
            tokenInfo.setTokenStart(tokenStart.trim());
        }
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
        if (StrTool.strNotNull(tokenEnd)) {
            tokenInfo.setTokenEnd(tokenEnd.trim());
        }

    }

    /**
     * @return the tokenType
     */
    public int getProducttype() {
        return producttype;
    }

    /**
     * @param tokenType the tokenType to set
     */
    public void setProducttype(int producttype) {
        this.producttype = producttype;
        tokenInfo.setProducttype(this.producttype);
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
        this.tokenInfo.setPhysicaltype(this.physicaltype);

    }

    /**
     * @return the enableState
     */
    public int getEnableState() {
        return enableState;
    }

    /**
     * @param enableState the enableState to set
     */
    public void setEnableState(int enableState) {
        this.enableState = enableState;
        tokenInfo.setEnabled(this.enableState);
    }

    /**
     * @return the bindState
     */
    public int getBindState() {
        return bindState;
    }

    /**
     * @param bindState the bindState to set
     */
    public void setBindState(int bindState) {
        this.bindState = bindState;
        tokenInfo.setBind(this.bindState);
    }

    /**
     * @return the lockedState
     */
    public int getLockedState() {
        return lockedState;
    }

    /**
     * @param lockedState the lockedState to set
     */
    public void setLockedState(int lockedState) {
        this.lockedState = lockedState;
        tokenInfo.setLocked(this.lockedState);
    }

    /**
     * @return the lostState
     */
    public int getLostState() {
        return lostState;
    }

    /**
     * @param lostState the lostState to set
     */
    public void setLostState(int lostState) {
        this.lostState = lostState;
        tokenInfo.setLost(this.lostState);
    }

    /**
     * @return the logoutState
     */
    public int getLogoutState() {
        return logoutState;
    }

    /**
     * @param logoutState the logoutState to set
     */
    public void setLogoutState(int logoutState) {
        this.logoutState = logoutState;
        tokenInfo.setLogout(this.logoutState);

    }

    /**
     * @return the operObj
     */
    public int getOperObj() {
        return operObj;
    }

    /**
     * @param operObj the operObj to set
     */
    public void setOperObj(int operObj) {
        this.operObj = operObj;
    }

    /**
     * @return the operBatch
     */
    public int getOperBatch() {
        return operBatch;
    }

    /**
     * @param operBatch the operBatch to set
     */
    public void setOperBatch(int operBatch) {
        this.operBatch = operBatch;
    }

    /**
     * @return the tknState
     */
    public int getTknState() {
        return tknState;
    }

    /**
     * @param tknState the tknState to set
     */
    public void setTknState(int tknState) {
        this.tknState = tknState;

    }

    /**
     * @return the tokenStr
     */
    public String getTokenStr() {
        return tokenStr;
    }

    /**
     * @param tokenStr the tokenStr to set
     */
    public void setTokenStr(String tokenStr) {
        this.tokenStr = tokenStr;
        if (StrTool.strNotNull(tokenStr)) {
            tokenInfo.setToken(tokenStr.trim());
        }
    }

    /**
     * @return the batchTknSn
     */
    public String getBatchTknSn() {
        return batchTknSn;
    }

    /**
     * @param batchTknSn the batchTknSn to set
     */
    public void setBatchTknSn(String batchTknSn) {
        this.batchTknSn = batchTknSn;
    }

    /**
     * @return the domaind
     */
    public String getDomaind() {
        return domaind;
    }

    /**
     * @param domaind the domaind to set
     */
    public void setDomaind(String domaind) {
        this.domaind = domaind;
        if (StrTool.strNotNull(domaind)) {
            tokenInfo.setDomainid(StrTool.parseInt(domaind));
        }
    }

    /**
     * @return the orgunitid
     */
    public String getOrgunitid() {
        return orgunitid;
    }

    /**
     * @param orgunitid the orgunitid to set
     */
    public void setOrgunitid(String orgunitid) {
        if (StrTool.strNotNull(orgunitid) && !StrTool.strEquals(orgunitid, StrConstant.common_number_0)) {
            tokenInfo.setOrgunitid(StrTool.parseInt(orgunitid));
        }
        this.orgunitid = orgunitid;
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
     * @return the pubkeyState
     */
    public int getPubkeyState() {
        return pubkeyState;
    }

    /**
     * @param pubkeyState the pubkeyState to set
     */
    public void setPubkeyState(int pubkeyState) {
        this.pubkeyState = pubkeyState;
        tokenInfo.setPubkeystate(this.pubkeyState);
    }

    /**
     * @return the vendorId
     */
    public String getVendorId() {
        return vendorId;
    }

    /**
     * @param vendorId the vendorId to set
     */
    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
        tokenInfo.setVendorid(this.vendorId);
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
        if (expiretimeType != NumConstant.common_number_na_1) {
            int expiretime = 0;
            Calendar calendar = Calendar.getInstance();
            expiretime = DateTool.dateToInt(calendar.getTime());
            this.tokenInfo.setNowtime(expiretime);
            this.tokenInfo.setExpiretimeType(NumConstant.common_number_1); // 过期时间 <= 选择的时间
            if (expiretimeType == NumConstant.common_number_0) {// 已过期
                // 已过期的就是当前时间
            	this.tokenInfo.setExpiretimeType(NumConstant.common_number_0);
            } else if (expiretimeType == NumConstant.common_number_1) { // 一周内过期
                calendar.add(Calendar.WEEK_OF_MONTH, 1);
            } else if (expiretimeType == NumConstant.common_number_2) {// 一个月内过期
                calendar.add(Calendar.MONTH, 1);
            } else if (expiretimeType == NumConstant.common_number_3) {// 一个季度内过期
                calendar.add(Calendar.MONTH, 3);
            } else if (expiretimeType == NumConstant.common_number_4) {// 大于三个月过期
                calendar.add(Calendar.MONTH, 3);
                this.tokenInfo.setExpiretimeType(NumConstant.common_number_2); // 过期时间 >= 选择的时间
            }
            expiretime = DateTool.dateToInt(calendar.getTime());
            this.tokenInfo.setExpiretime(expiretime);
        }
    }
}
