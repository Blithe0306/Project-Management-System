/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.server.entity;

import java.util.List;
import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.util.tool.StrTool;

/**
 * 认证服务器实体类功能说明
 *
 * @Date in Apr 11, 2011,1:52:09 PM
 *
 * @author ZJY
 */
public class ServerInfo extends BaseEntity {

    private String hostipaddr = null;
    private String newHostipaddr = null;
    private int confid = 0;
    private int priority = 0; //优先级别，2低、1中、0高
    private String descp = null;
    private String licid;
    private String hostname;//认证服务器名称
    private List<?> agentAddrips = null; //服务器关联的代理

    private int ftradiusenabled;//启用FT扩展Radius服务，是(1)，否(0)
    private String protocoltype = "udp"; //服务器使用的通信协议tcp|udp，界面上不显示此项配置，默认udp
    private int authport; //服务器认证及业务端口
    private int syncport; //服务器同步处理端口
    private int radiusenabled; //启用标准Radius服务，是(1)，否(0)
    private int radauthport; //标准Radius服务认证端口，默认1812
    private int httpenabled; //启用http服务，是(1)，否(0)
    private int httpport; //HTTP服务端口
    private int httpsenabled; //启用https服务，是(1)，否(0)
    private int httpsport; //HTTPS服务端口
    private String keystorepwd; //HTTPS服务端密钥容器密码
    private String certificatepwd; //HTTPS私钥证书密码
    private String keystoreinstance; //HTTPS服务端证书类型：PKCS12｜JKS
    private String keystorerootpath; //HTTPS服务端证书，上传证书，读取证书数据存储至此字段
    private int soapenabled; //启用SOAP服务支持，是(1)，否(0)
    private int soapport; //SOAP协议端口
    private String webservicename = "otpwebservice"; //客户端访问WebService的名称
    private int licType;

    public int getLicType() {
        return licType;
    }

    public void setLicType(int licType) {
        this.licType = licType;
    }

    public String getLicid() {
        return licid;
    }

    public void setLicid(String licid) {
        this.licid = licid;
    }

    /**
     * @return the confid
     */
    public int getConfid() {
        return confid;
    }

    /**
     * @param confid the confid to set
     */
    public void setConfid(int confid) {
        this.confid = confid;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int nPriority) {
        this.priority = nPriority;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String strDescp) {
        this.descp = StrTool.cleanXSS(strDescp);
    }

    /**
     * @return the agentAddrips
     */
    public List<?> getAgentAddrips() {
        return agentAddrips;
    }

    /**
     * @param agentAddrips the agentAddrips to set
     */
    public void setAgentAddrips(List<?> agentAddrips) {
        this.agentAddrips = agentAddrips;
    }

    public String getHostipaddr() {
        return hostipaddr;
    }

    public void setHostipaddr(String hostipaddr) {
        this.hostipaddr = hostipaddr;
    }

    public String getProtocoltype() {
        return protocoltype;
    }

    public void setProtocoltype(String protocoltype) {
        this.protocoltype = protocoltype;
    }

    public int getAuthport() {
        return authport;
    }

    public void setAuthport(int authport) {
        this.authport = authport;
    }

    public int getSyncport() {
        return syncport;
    }

    public void setSyncport(int syncport) {
        this.syncport = syncport;
    }

    public int getRadauthport() {
        return radauthport;
    }

    public void setRadauthport(int radauthport) {
        this.radauthport = radauthport;
    }

    public int getHttpport() {
        return httpport;
    }

    public void setHttpport(int httpport) {
        this.httpport = httpport;
    }

    public int getHttpsport() {
        return httpsport;
    }

    public void setHttpsport(int httpsport) {
        this.httpsport = httpsport;
    }

    public String getKeystorepwd() {
        return keystorepwd;
    }

    public void setKeystorepwd(String keystorepwd) {
        this.keystorepwd = keystorepwd;
    }

    public String getCertificatepwd() {
        return certificatepwd;
    }

    public void setCertificatepwd(String certificatepwd) {
        this.certificatepwd = certificatepwd;
    }

    public String getKeystoreinstance() {
        return keystoreinstance;
    }

    public void setKeystoreinstance(String keystoreinstance) {
        this.keystoreinstance = keystoreinstance;
    }

    public String getKeystorerootpath() {
        return keystorerootpath;
    }

    public void setKeystorerootpath(String keystorerootpath) {
        this.keystorerootpath = keystorerootpath;
    }

    public int getFtradiusenabled() {
        return ftradiusenabled;
    }

    public void setFtradiusenabled(int ftradiusenabled) {
        this.ftradiusenabled = ftradiusenabled;
    }

    public int getRadiusenabled() {
        return radiusenabled;
    }

    public void setRadiusenabled(int radiusenabled) {
        this.radiusenabled = radiusenabled;
    }

    public int getHttpenabled() {
        return httpenabled;
    }

    public void setHttpenabled(int httpenabled) {
        this.httpenabled = httpenabled;
    }

    public int getHttpsenabled() {
        return httpsenabled;
    }

    public void setHttpsenabled(int httpsenabled) {
        this.httpsenabled = httpsenabled;
    }

    public int getSoapenabled() {
        return soapenabled;
    }

    public void setSoapenabled(int soapenabled) {
        this.soapenabled = soapenabled;
    }

    public int getSoapport() {
        return soapport;
    }

    public void setSoapport(int soapport) {
        this.soapport = soapport;
    }

    public String getWebservicename() {
        return webservicename;
    }

    public void setWebservicename(String webservicename) {
        this.webservicename = webservicename;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getNewHostipaddr() {
        return newHostipaddr;
    }

    public void setNewHostipaddr(String newHostipaddr) {
        this.newHostipaddr = newHostipaddr;
    }

}
