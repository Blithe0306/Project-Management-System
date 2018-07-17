/**
 *Administrator
 */
package com.ft.otp.manager.tokenspec.entity;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.util.tool.StrTool;

/**
 * 令牌规格实体对象
 *
 * @Date in Oct 31, 2012,2:42:13 PM
 *
 * @version v1.0
 *
 * @author ZJY
 */
public class TokenSpec extends BaseEntity {

    private String specid = ""; //令牌规格 
    private int tokenType = -1; //令牌类型
    private int algid = 0;//令牌算法
    private String signsuite;//交易签名suite
    private String cvssuite;//主机认证suite
    private String crsuite;//挑战应答suite
    private String atid; //手机令牌组合类型(00、01、02、03、04、05、06、07)
    private int otplen; //动态口令长度
    private int intervaltime; //时间间隔
    private int begintime;//起始时间
    private int maxauthcnt = 0;//最大的认证次数（刮刮卡、矩阵卡）
    private int initauthnum = 0;//初始认证基数（刮刮卡）
    private int haformat = 0;//主机认证刮刮卡主机认证码格式
    private int halen = 0;//主机认证码长度
    private int cardrow = 0;//矩阵卡行数
    private int cardcol = 0;//矩阵卡列数
    private String rowstart = "";//矩阵卡行起始字符
    private String colstart = "";//矩阵卡列起始字符
    //密钥更新模式（disable(0)、令牌及服务器共同主导(1)、令牌主导(2)、服务器主导(3)）
    private int updatemode = 0;
    private int updatelimit = 0;//密钥更新次数（0：不限制、1：只能一次）
    //密钥更新应答长度（对于密钥更新模式为令牌及服务器共同主导（该值为8）
    //和服务器主导时（该值为12（工行C300）或 8（稠州银行Z300）），该值为非0正值）
    private int updateresplen = 0;
    private int puk1mode = 0;//一级解锁码解锁模式（disable(0)、时间型(1)、挑战型(2)）
    private int puk1len = 0;//一级解锁码解锁码长度（6位、8位等)
    private int puk1itv = 0;//一级解锁码时钟周期（7200秒等，当模式为时间型时有效）
    private int puk2mode = 0;//二级解锁码解锁模式（disable（0）、时间型（1）、挑战型（2））
    private int puk2len = 0;//二级解锁码解锁码长度（6位、8位等）
    private int puk2itv = 0;//二级解锁码时钟周期（7200秒等，当模式为时间型时有效）
    private int maxcounter = 0;//T内C令牌的周期最大计数值
    //0和1表示使用两个普通口令进行同步、2使用挑战型口令+普通口令进行同步、4使用两个挑战应答口令同步
    private int syncmode = 0;
    private String descp;//描述

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
     * @return the tokenType
     */
    public int getTokenType() {
        return tokenType;
    }

    /**
     * @param tokenType the tokenType to set
     */
    public void setTokenType(int tokenType) {
        this.tokenType = tokenType;
    }

    /**
     * @return the algid
     */
    public int getAlgid() {
        return algid;
    }

    /**
     * @param algid the algid to set
     */
    public void setAlgid(int algid) {
        this.algid = algid;
    }

    /**
     * @return the signsuite
     */
    public String getSignsuite() {
        return signsuite;
    }

    /**
     * @param signsuite the signsuite to set
     */
    public void setSignsuite(String signsuite) {
        this.signsuite = signsuite;
    }

    /**
     * @return the cvssuite
     */
    public String getCvssuite() {
        return cvssuite;
    }

    /**
     * @param cvssuite the cvssuite to set
     */
    public void setCvssuite(String cvssuite) {
        this.cvssuite = cvssuite;
    }

    /**
     * @return the crsuite
     */
    public String getCrsuite() {
        return crsuite;
    }

    /**
     * @param crsuite the crsuite to set
     */
    public void setCrsuite(String crsuite) {
        this.crsuite = crsuite;
    }

    /**
     * @return the atid
     */
    public String getAtid() {
        return atid;
    }

    /**
     * @param atid the atid to set
     */
    public void setAtid(String atid) {
        this.atid = atid;
    }

    /**
     * @return the otplen
     */
    public int getOtplen() {
        return otplen;
    }

    /**
     * @param otplen the otplen to set
     */
    public void setOtplen(int otplen) {
        this.otplen = otplen;
    }

    /**
     * @return the intervaltime
     */
    public int getIntervaltime() {
        return intervaltime;
    }

    /**
     * @param intervaltime the intervaltime to set
     */
    public void setIntervaltime(int intervaltime) {
        this.intervaltime = intervaltime;
    }

    /**
     * @return the begintime
     */
    public int getBegintime() {
        return begintime;
    }

    /**
     * @param begintime the begintime to set
     */
    public void setBegintime(int begintime) {
        this.begintime = begintime;
    }

    /**
     * @return the maxauthcnt
     */
    public int getMaxauthcnt() {
        return maxauthcnt;
    }

    /**
     * @param maxauthcnt the maxauthcnt to set
     */
    public void setMaxauthcnt(int maxauthcnt) {
        this.maxauthcnt = maxauthcnt;
    }

    /**
     * @return the initauthnum
     */
    public int getInitauthnum() {
        return initauthnum;
    }

    /**
     * @param initauthnum the initauthnum to set
     */
    public void setInitauthnum(int initauthnum) {
        this.initauthnum = initauthnum;
    }

    /**
     * @return the haformat
     */
    public int getHaformat() {
        return haformat;
    }

    /**
     * @param haformat the haformat to set
     */
    public void setHaformat(int haformat) {
        this.haformat = haformat;
    }

    /**
     * @return the halen
     */
    public int getHalen() {
        return halen;
    }

    /**
     * @param halen the halen to set
     */
    public void setHalen(int halen) {
        this.halen = halen;
    }

    /**
     * @return the cardrow
     */
    public int getCardrow() {
        return cardrow;
    }

    /**
     * @param cardrow the cardrow to set
     */
    public void setCardrow(int cardrow) {
        this.cardrow = cardrow;
    }

    /**
     * @return the cardcol
     */
    public int getCardcol() {
        return cardcol;
    }

    /**
     * @param cardcol the cardcol to set
     */
    public void setCardcol(int cardcol) {
        this.cardcol = cardcol;
    }

    /**
     * @return the rowstart
     */
    public String getRowstart() {
        return rowstart;
    }

    /**
     * @param rowstart the rowstart to set
     */
    public void setRowstart(String rowstart) {
        this.rowstart = rowstart;
    }

    /**
     * @return the colstart
     */
    public String getColstart() {
        return colstart;
    }

    /**
     * @param colstart the colstart to set
     */
    public void setColstart(String colstart) {
        this.colstart = colstart;
    }

    /**
     * @return the updatemode
     */
    public int getUpdatemode() {
        return updatemode;
    }

    /**
     * @param updatemode the updatemode to set
     */
    public void setUpdatemode(int updatemode) {
        this.updatemode = updatemode;
    }

    /**
     * @return the updatelimit
     */
    public int getUpdatelimit() {
        return updatelimit;
    }

    /**
     * @param updatelimit the updatelimit to set
     */
    public void setUpdatelimit(int updatelimit) {
        this.updatelimit = updatelimit;
    }

    /**
     * @return the updateresplen
     */
    public int getUpdateresplen() {
        return updateresplen;
    }

    /**
     * @param updateresplen the updateresplen to set
     */
    public void setUpdateresplen(int updateresplen) {
        this.updateresplen = updateresplen;
    }

    /**
     * @return the puk1mode
     */
    public int getPuk1mode() {
        return puk1mode;
    }

    /**
     * @param puk1mode the puk1mode to set
     */
    public void setPuk1mode(int puk1mode) {
        this.puk1mode = puk1mode;
    }

    /**
     * @return the puk1len
     */
    public int getPuk1len() {
        return puk1len;
    }

    /**
     * @param puk1len the puk1len to set
     */
    public void setPuk1len(int puk1len) {
        this.puk1len = puk1len;
    }

    /**
     * @return the puk1itv
     */
    public int getPuk1itv() {
        return puk1itv;
    }

    /**
     * @param puk1itv the puk1itv to set
     */
    public void setPuk1itv(int puk1itv) {
        this.puk1itv = puk1itv;
    }

    /**
     * @return the puk2mode
     */
    public int getPuk2mode() {
        return puk2mode;
    }

    /**
     * @param puk2mode the puk2mode to set
     */
    public void setPuk2mode(int puk2mode) {
        this.puk2mode = puk2mode;
    }

    /**
     * @return the puk2len
     */
    public int getPuk2len() {
        return puk2len;
    }

    /**
     * @param puk2len the puk2len to set
     */
    public void setPuk2len(int puk2len) {
        this.puk2len = puk2len;
    }

    /**
     * @return the puk2itv
     */
    public int getPuk2itv() {
        return puk2itv;
    }

    /**
     * @param puk2itv the puk2itv to set
     */
    public void setPuk2itv(int puk2itv) {
        this.puk2itv = puk2itv;
    }

    /**
     * @return the maxcounter
     */
    public int getMaxcounter() {
        return maxcounter;
    }

    /**
     * @param maxcounter the maxcounter to set
     */
    public void setMaxcounter(int maxcounter) {
        this.maxcounter = maxcounter;
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
     * @return the syncmode
     */
    public int getSyncmode() {
        return syncmode;
    }

    /**
     * @param syncmode the syncmode to set
     */
    public void setSyncmode(int syncmode) {
        this.syncmode = syncmode;
    }

}
