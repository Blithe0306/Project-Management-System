package com.ft.otp.manager.confinfo.usersource.entity;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.manager.task.entity.TaskInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户来源配置属性集合类
 * @Date in Jun 7, 2011,11:18:22 AM *
 * @author ZJY
 */
public class UserSourceInfo extends BaseEntity {

    private TaskInfo taskInfo = new TaskInfo();

    private int id;
    //用户来源名称 LDAP、DB、DOMAIN
    private String sourcename = null;
    //用户来源类型，数据库：0，LDAP：1，Domino：2
    private int sourcetype;
    //数据库类型，目前DB使用，表示DB类型，如：mysql(0)、oracle(1)、postgresql(2)、sqlserver(3)
    private int dbtype;
    //存储设备主机地址，DB、LDAP、DOMINO使用
    private String serveraddr = null;
    //端口，DB、LDAP、DOMINO使用，DB根据不同的数据库类型，默认端口不同，LDAP默认389/636，DOMINO默认63148
    private int port;
    //帐号，DB、LDAP、DOMINO使用
    private String username = null;
    //密码，DB、LDAP、DOMINO使用
    private String pwd = null;
    //数据库名称，DB使用
    private String dbname = null;
    //远程数据库用户表名称，DB使用
    private String dbtablename = null;
    //查询条件，LDAP使用，默认：(&(objectCategory=person)(objectClass=user))
    private String filter = null;
    //LDAP目录结构，LDAP使用，例如：CN=Users,DC=ft,DC=page
    private String basedn = null;
    //DOMINO连接驱动，Domino使用，默认Lotus NotesSQL Driver (*.nsf)
    private String dominodriver = null;
    //DOMINO命名文件，如names.nsf，Domino使用
    private String namesfile = null;
    //LDAP连接超时秒数,默认30，单位秒
    private int timeout;
    private Integer domainid = null;//用户来源所属的域
    private Integer orgunitid = null;//用户来源所属的组织机构
    private String descp;//描述
    
    //根DN
    private String rootdn = null; 
    
    //本地数据库用户表属性名
    private String localuserattr;
    //远程存储设备用户来源属性名
    private String sourceuserattr;

   
    private String oldsourcename;
    
    //本地数据库表属性和远程数据库表属性对应关系字符串 userid:userid,realname:realname
    private String mapingAttr;
    
    //是否启用定时更新功能
    private int timingState = 0;  //0、禁用  1、启用
    
    //域和组织机构的拼接字符串
    private String orgunitIds;
    //域和组织机构的拼接显示名称字符串
    private String  orgunitNames;
   
    
    private String domainname;
    private String orgunitname;
    
    
    //yyf add
    private int isupdateou;//是否更新LDAP的组织机构结构，  0:不更新，1:更新
    private int upinvaliduser;//是否更新无效的用户(已经禁用或已经过期)，0:否，1:是
    
    
    private int localusermark; //Ldap 用户被删除时，0：不处理本地用户，1：禁用本地用户 2：删除本地用户
    private int issyncuserinfo; //是否同步用户信息， 0：否 1：是

    public int getIssyncuserinfo() {
        return issyncuserinfo;
    }

    public void setIssyncuserinfo(int issyncuserinfo) {
        this.issyncuserinfo = issyncuserinfo;
    }

    /**
     * @return the taskInfo
     */
    public TaskInfo getTaskInfo() {
        return taskInfo;
    }

    /**
     * @param taskInfo the taskInfo to set
     */
    public void setTaskInfo(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the sourcename
     */
    public String getSourcename() {
        return sourcename;
    }

    /**
     * @param sourcename the sourcename to set
     */
    public void setSourcename(String sourcename) {
        this.sourcename = sourcename;
    }

    /**
     * @return the sourcetype
     */
    public int getSourcetype() {
        return sourcetype;
    }

    /**
     * @param sourcetype the sourcetype to set
     */
    public void setSourcetype(int sourcetype) {
        this.sourcetype = sourcetype;
    }

    /**
     * @return the dbtype
     */
    public int getDbtype() {
        return dbtype;
    }

    /**
     * @param dbtype the dbtype to set
     */
    public void setDbtype(int dbtype) {
        this.dbtype = dbtype;
    }

    /**
     * @return the serveraddr
     */
    public String getServeraddr() {
        return serveraddr;
    }

    /**
     * @param serveraddr the serveraddr to set
     */
    public void setServeraddr(String serveraddr) {
        this.serveraddr = serveraddr;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the dbname
     */
    public String getDbname() {
        return dbname;
    }

    /**
     * @param dbname the dbname to set
     */
    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    /**
     * @return the dbtablename
     */
    public String getDbtablename() {
        return dbtablename;
    }

    /**
     * @param dbtablename the dbtablename to set
     */
    public void setDbtablename(String dbtablename) {
        this.dbtablename = dbtablename;
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
     * @return the basedn
     */
    public String getBasedn() {
        return basedn;
    }

    /**
     * @param basedn the basedn to set
     */
    public void setBasedn(String basedn) {
        this.basedn = basedn;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getDominodriver() {
        return dominodriver;
    }

    public void setDominodriver(String dominodriver) {
        this.dominodriver = dominodriver;
    }

    /**
     * @return the namesfile
     */
    public String getNamesfile() {
        return namesfile;
    }

    /**
     * @param namesfile the namesfile to set
     */
    public void setNamesfile(String namesfile) {
        this.namesfile = namesfile;
    }

    /**
     * @return the timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * @return the descp
     */
    public String getDescp() {
        return descp;
    }

    public String getRootdn() {
        return rootdn;
    }

    public void setRootdn(String rootdn) {
        this.rootdn = rootdn;
    }

    /**
     * @param descp the descp to set
     */
    public void setDescp(String descp) {
        this.descp = StrTool.cleanXSS(descp);
    }

    /**
     * @return the localuserattr
     */
    public String getLocaluserattr() {
        return localuserattr;
    }

    /**
     * @param localuserattr the localuserattr to set
     */
    public void setLocaluserattr(String localuserattr) {
        this.localuserattr = localuserattr;
    }

    /**
     * @return the sourceuserattr
     */
    public String getSourceuserattr() {
        return sourceuserattr;
    }

    /**
     * @param sourceuserattr the sourceuserattr to set
     */
    public void setSourceuserattr(String sourceuserattr) {
        this.sourceuserattr = sourceuserattr;
    }

 
 

    /**
     * @return the timingState
     */
    public int getTimingState() {
        return timingState;
    }

    /**
     * @param timingState the timingState to set
     */
    public void setTimingState(int timingState) {
        this.timingState = timingState;
    }

    /**
     * @return the oldsourcename
     */
    public String getOldsourcename() {
        return oldsourcename;
    }

    /**
     * @param oldsourcename the oldsourcename to set
     */
    public void setOldsourcename(String oldsourcename) {
        this.oldsourcename = oldsourcename;
    }

    /**
     * @return the mapingAttr
     */
    public String getMapingAttr() {
        return mapingAttr;
    }

    /**
     * @param mapingAttr the mapingAttr to set
     */
    public void setMapingAttr(String mapingAttr) {
        this.mapingAttr = mapingAttr;
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
        if (StrTool.strNotNull(getOrgunitname())) {
            orgunitNames = getDomainname() +"-->"+getOrgunitname();
        }else {
            orgunitNames = getDomainname();
        }
        return orgunitNames;
    }

    /**
     * @param orgunitNames the orgunitNames to set
     */
    public void setOrgunitNames(String orgunitNames) {
        this.orgunitNames = orgunitNames;
    }

    /**
     * @return the domainname
     */
    public String getDomainname() {
        return domainname;
    }

    /**
     * @param domainname the domainname to set
     */
    public void setDomainname(String domainname) {
        this.domainname = domainname;
    }

    /**
     * @return the orgunitname
     */
    public String getOrgunitname() {
        return orgunitname;
    }

    /**
     * @param orgunitname the orgunitname to set
     */
    public void setOrgunitname(String orgunitname) {
        this.orgunitname = orgunitname;
    }

    public int getIsupdateou() {
        return isupdateou;
    }

    public void setIsupdateou(int isupdateou) {
        this.isupdateou = isupdateou;
    }

    public int getUpinvaliduser() {
        return upinvaliduser;
    }

    public void setUpinvaliduser(int upinvaliduser) {
        this.upinvaliduser = upinvaliduser;
    }

    public int getLocalusermark() {
        return localusermark;
    }

    public void setLocalusermark(int localusermark) {
        this.localusermark = localusermark;
    }

}
