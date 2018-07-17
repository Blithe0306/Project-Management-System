/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.backend.entity;

import com.ft.otp.base.entity.BaseEntity;

/**
 * 后端认证实体类
 * @Date in Jan 21, 2013,6:38:53 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class BackendInfo extends BaseEntity {

    private int id; //后端认证ID
    private int backendtype; //后端认证类型，0：Radius；1、AD
    private String backendname; //后端认证配置的名称
    private String host; //后端服务器IP地址
    private int port; //端口，Radius默认为1812，AD默认为389
    private String sparehost; // 备后端服务器IP地址
    private int priority; //优先级，手动输入，1-1024之间的数字，数字越小优先级越高，1表示最大优先级。
    private String basedn; //LDAP目录结构，LDAP使用，例如：CN=Users,DC=ft,DC=page
    private String filter; //查询条件，LDAP使用
    private String pubkey; //共享密钥，Radius使用
    private int timeout; //连接超时时间，秒
    private int retrycnt; //重试次数，连接超时的重试次数
    private int usernamerule; //指定AD域名：包含2个选项：0：不指定；1：指定；
    private String delimiter = "@"; //匹配分割符，如：@、-等
    private int enabled = 0; //是否启用后端认证，0：否；1：是    
    private int policy = 0;//转发策略
    private int domainid; //域ID，表示使用该后端认证配置的域

    private String domainStr;

	public String getSparehost() {
		return sparehost;
	}

	public void setSparehost(String sparehost) {
		this.sparehost = sparehost;
	}

	public String getDomainStr() {
        return domainStr;
    }

    public void setDomainStr(String domainStr) {
        this.domainStr = domainStr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBackendtype() {
        return backendtype;
    }

    public void setBackendtype(int backendtype) {
        this.backendtype = backendtype;
    }

    public String getBackendname() {
        return backendname;
    }

    public void setBackendname(String backendname) {
        this.backendname = backendname;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getBasedn() {
        return basedn;
    }

    public void setBasedn(String basedn) {
        this.basedn = basedn;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getPubkey() {
        return pubkey;
    }

    public void setPubkey(String pubkey) {
        this.pubkey = pubkey;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getRetrycnt() {
        return retrycnt;
    }

    public void setRetrycnt(int retrycnt) {
        this.retrycnt = retrycnt;
    }

    public int getUsernamerule() {
        return usernamerule;
    }

    public void setUsernamerule(int usernamerule) {
        this.usernamerule = usernamerule;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public int getPolicy() {
        return policy;
    }

    public void setPolicy(int policy) {
        this.policy = policy;
    }

    public int getDomainid() {
        return domainid;
    }

    public void setDomainid(int domainid) {
        this.domainid = domainid;
    }
}
