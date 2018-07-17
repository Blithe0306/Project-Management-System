/**
 * <pre>
 * Title: 		LdapInfo.java
 * Project: 	otpcenter4.0
 * Type:		com.ft.otp.manager.config.center.usersource.entity.LdapInfo
 * Author:		YYF
 * Create:	 	2011-6-15 上午08:36:39
 * Copyright: 	Copyright (c) 2011
 * Company:		
 * <pre>
 */
package com.ft.otp.manager.confinfo.usersource.dao;

/**
 * LDAP查询属性信息实体
 * 
 * @author YYF
 * @version 1.0, 2011-6-15
 */
public class LdapInfo {

    private String ldapIp = null;
    private int ldapPort;
    private String ldapUser = null;
    private String ldapPass = null;
    private String ldapDn = null;
    private String filter = null;
    private int timeout;
    private String ldapAccouts;

    /**
     * @return the ldapIp
     */
    public String getLdapIp() {
        return ldapIp;
    }

    /**
     * @param ldapIp the ldapIp to set
     */
    public void setLdapIp(String ldapIp) {
        this.ldapIp = ldapIp;
    }

    /**
     * @return the ldapPort
     */
    public int getLdapPort() {
        return ldapPort;
    }

    /**
     * @param ldapPort the ldapPort to set
     */
    public void setLdapPort(int ldapPort) {
        this.ldapPort = ldapPort;
    }

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * @return the ldapUser
     */
    public String getLdapUser() {
        return ldapUser;
    }

    /**
     * @param ldapUser the ldapUser to set
     */
    public void setLdapUser(String ldapUser) {
        this.ldapUser = ldapUser;
    }

    /**
     * @return the ldapPass
     */
    public String getLdapPass() {
        return ldapPass;
    }

    /**
     * @param ldapPass the ldapPass to set
     */
    public void setLdapPass(String ldapPass) {
        this.ldapPass = ldapPass;
    }

    /**
     * @return the ldapDn
     */
    public String getLdapDn() {
        return ldapDn;
    }

    /**
     * @param ldapDn the ldapDn to set
     */
    public void setLdapDn(String ldapDn) {
        this.ldapDn = ldapDn;
    }

    /**
     * @return the filter
     */
    public String getFilter() {
        return filter;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }

    /**
     * @return the timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * @return the ldapAccouts
     */
    public String getLdapAccouts() {
        return ldapAccouts;
    }

    /**
     * @param ldapAccouts the ldapAccouts to set
     */
    public void setLdapAccouts(String ldapAccouts) {
        this.ldapAccouts = ldapAccouts;
    }

    //	/**
    //	 * 由LdapInfo配置对象得到配置列表
    //	 * @param ldapInfo LdapInfo配置对象
    //	 * @return 配置列表
    //	 */
    //	public static List<Object> getListByLdapInfo(LdapInfo ldapInfo)
    //	{
    //		List<Object> confList = null;
    //		if(StrTool.objNotNull(ldapInfo))
    //		{
    //			confList = new ArrayList<Object>();
    //			confList.add(new ConfInfo("type",ldapInfo.getType(),-1,"",""));
    //			confList.add(new ConfInfo("domain",ldapInfo.getDomain(),-1,"",""));
    //			confList.add(new ConfInfo("port",ldapInfo.getPort(),-1,"",""));
    //			confList.add(new ConfInfo("user",ldapInfo.getUser(),-1,"",""));
    //			confList.add(new ConfInfo("password",ldapInfo.getPassword(),-1,"",""));
    //			confList.add(new ConfInfo("filter",ldapInfo.getFilter(),-1,"",""));
    //			confList.add(new ConfInfo("base_dn",ldapInfo.getBase_dn(),-1,"",""));
    //			confList.add(new ConfInfo("account_attr",ldapInfo.getAccount_attr(),-1,"",""));	
    //			confList.add(new ConfInfo("timeout",ldapInfo.getTimeout(),-1,"",""));
    //			confList.add(new ConfInfo("ssl",ldapInfo.getSsl(),-1,"",""));
    //		}
    //		return confList;
    //	}
    //	

}
