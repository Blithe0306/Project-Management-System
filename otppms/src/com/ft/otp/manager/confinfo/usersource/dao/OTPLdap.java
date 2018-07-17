package com.ft.otp.manager.confinfo.usersource.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import org.apache.log4j.Logger;
import com.ft.otp.util.tool.StrTool;

/**
 * LDAP操作类
 * 
 * @Date in Jul 15, 2010,3:18:55 PM
 * 
 * @author TBM
 */
public class OTPLdap {

    private static Logger log = Logger.getLogger(OTPLdap.class);

    private static LdapContext ctx = null;

    private static final String OTP_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
    private static final String OTP_AUTH_METHOD = "simple";
    private static String ldapUrl = null;
    private static String ldapUser = null;
    private static String ldapPwd = null;

    public OTPLdap() {
        ctx = null;
    }

    /**
     * LDAP 连接测试
     * 
     * @Date in Sep 6, 2013,1:32:03 PM
     * @param ldapInfo
     * @return
     * @throws Exception
     */
    public boolean initConn(LdapInfo ldapInfo) throws Exception {
        boolean result = false;
        if (null != ctx) {
            return true;
        }

        String domain = null;
        String port = null;
        String user = null;
        String passwd = null;
        if (null != ldapInfo) {
            domain = ldapInfo.getLdapIp();
            port = StrTool.intToString(ldapInfo.getLdapPort());
            user = ldapInfo.getLdapUser();
            passwd = ldapInfo.getLdapPass();
        }

        if (!StrTool.strNotNull(domain) || !StrTool.strNotNull(port) || !StrTool.strNotNull(user)
                || !StrTool.strNotNull(passwd)) {
            return false;
        }

        //LDAP URL
        String url = "ldap://" + domain + ":" + port;
        ldapUrl = url;
        ldapUser = user;
        ldapPwd = passwd;

        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, OTP_CONTEXT_FACTORY);
        env.put(Context.SECURITY_AUTHENTICATION, OTP_AUTH_METHOD);
        env.put(Context.SECURITY_PRINCIPAL, ldapUser);
        env.put(Context.SECURITY_CREDENTIALS, ldapPwd);
        env.put(Context.PROVIDER_URL, ldapUrl);

        try {
            ctx = new InitialLdapContext(env, null);
            result = true;
        } catch (Exception e) {
            throw e;
        }

        return result;
    }

    /**
     * 查找
     * 
     * @param baseDN
     *            查找的base DN(在哪个目录下查找？)
     * @param filter
     *            过滤条件
     * @param returnAttr 属性名集合
     * @return 返回属性值的一个列表，列表的每一项是数组，数组中每一项对应属性名数组中的项
     */
    public List<String[]> search(LdapInfo ldapInfo, String[] returnAttr) throws Exception {
        boolean result = initConn(ldapInfo);

        if (!result || null == ctx) {
            return null;
        }

        String baseDN = ldapInfo.getLdapDn();
        String filter = ldapInfo.getFilter();

        if (!StrTool.strNotNull(baseDN) || !StrTool.strNotNull(filter)) {
            return null;
        }

        List<String[]> retList = new ArrayList<String[]>();
        NamingEnumeration list;
        try {
            list = ctx.list(baseDN);

            while (list.hasMoreElements()) {
                NameClassPair entry = (NameClassPair) list.next();
                StringBuffer buf = new StringBuffer(512);
                buf.append(entry.getName());
                buf.append(",");
                buf.append(baseDN);
                List<String[]> sub = this.searchSub(buf.toString(), filter, returnAttr);

                retList.addAll(sub);
            }
        } catch (NamingException e) {
            log.error(e.getMessage(), e);
        }

        return retList;
    }

    public List<String[]> searchSub(String baseDN, String filter, String[] returnedAtts) {
        if (ctx == null) {
            return null;
        }

        List<String[]> list = new ArrayList<String[]>();

        try {
            SearchControls searchCtls = new SearchControls();
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            int returnedAttsLength = returnedAtts.length;

            if (returnedAttsLength == 0) {
                returnedAtts = new String[1];
            }
            searchCtls.setReturningAttributes(returnedAtts);

            NamingEnumeration<SearchResult> answer = ctx.search(baseDN, filter, searchCtls);
            while (answer.hasMoreElements()) {
                // 此处相当于取数据表中一行的值
                SearchResult sr = (SearchResult) answer.next();
                // 此处和表的区别如果没有那个属性 返回null 
                Attributes attrs = sr.getAttributes();
                if (attrs != null) {
                    try {
                        //UserInfo userInfo = new UserInfo();
                        String[] attValues = new String[returnedAttsLength];
                        for (int h = 0; h < returnedAttsLength; h++) {
                            // 此处相当于取数据表中一行中一列的值
                            Attribute attr = attrs.get(returnedAtts[h].trim());
                            if (null != attr) {
                                attValues[h] = attr.get().toString().trim();
                            } else {
                                attValues[h] = "";
                            }

                        }
                        if (StrTool.arrNotNull(attValues)) {
                            list.add(attValues);
                        }
                    } catch (NamingException e) {
                        log.error("Problem listing membership: ", e);
                    }
                }
            }
            answer.close();
        } catch (NamingException e) {
            log.error("Problem searching directory: ", e);
        }

        return list;
    }

    /**
     * 查找可访问属性列表
     * 
     * @param baseDN
     * @param filter
     * @param returnAttr
     * @return
     */
    public List<String> searchAccountAttr(String baseDN, String filter) {
        if (ctx == null) {
            return null;
        }

        List<String> list = new ArrayList<String>();
        try {
            SearchControls searchCtls = new SearchControls();
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            searchCtls.setReturningAttributes(new String[] { "yang", "you", "feng" });
            NamingEnumeration<SearchResult> answer = ctx.search(baseDN, filter, searchCtls);
            while (answer.hasMoreElements()) {
                // 此处相当于取数据表中一行的值
                SearchResult sr = (SearchResult) answer.next();
                Attributes attrs = sr.getAttributes();

                if (attrs != null) {
                    NamingEnumeration<String> ne = attrs.getIDs();
                    while (ne.hasMoreElements()) {
                        String acc_attr = (String) ne.nextElement();
                        //System.out.println("***$得到的属性值是：" + acc_attr);
                        list.add(acc_attr);
                    }
                }
            }
            answer.close();
        } catch (NamingException e) {
            log.error("Problem searching directory: ", e);
        }

        return list;
    }

    /**
     * LDAP返回初始化
     * 
     * @Date in Sep 6, 2013,1:37:59 PM
     */
    synchronized public void unInit() {
        try {
            if (ctx != null) {
                ctx.close();
                ctx = null;
            }
        } catch (NamingException e) {
            log.error(e.getMessage(), e);
        }
    }

}