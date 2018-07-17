/*
  索引：idx_表名（或表名缩写）_字段名（或字段名缩写）
  外键：fk_本表名（或表名缩写）_关联表名（或表名缩写）_字段名（或字段名缩写）
  唯一索引：uk_表名（或表名缩写）_唯一健名（可以是字段名缩写）
*/
set sql_mode="no_auto_value_on_zero";

-- otppms_domaininfo --
create table if not exists otppms_domaininfo (
  domainid int(10) not null auto_increment,
  domainsn varchar(64) default '',
  domainname varchar(128)  default '',  
  descp varchar(255) default '',
  createtime int(10) not null default 0,
  primary key (domainid)
) engine=innodb;
create unique index uk_domaininfo_domainname on otppms_domaininfo(domainname);

-- otppms_orgunitinfo --
create table if not exists otppms_orgunitinfo (
  orgunitid int(10) not null auto_increment,
  orgunitnumber varchar(32) default '',
  orgunitname varchar(128) not null default '' ,
  parentid int(10) default 0 ,
  domainid int(10) not null default 0,
  descp varchar(255) default '',
  createtime int(10) not null default 0,
  primary key (orgunitid)
) engine=innodb;
create unique index uk_orgunitinfo_orgunitname_parentid on otppms_orgunitinfo(orgunitname, parentid,domainid);
alter table otppms_orgunitinfo add constraint fk_orgunitinfo_domaininfo_domainid 
foreign key(domainid) references otppms_domaininfo(domainid) on delete cascade on update cascade ;

-- otppms_rad_profile --
create table if not exists otppms_rad_profile (
  id int(10) not null auto_increment,
  profilename varchar(64) not null default '',
  descp varchar(255) default '',
  primary key (id)
) engine=innodb;
create unique index uk_rad_profile_profilename on otppms_rad_profile(profilename);

-- otppms_userinfo --
create table if not exists otppms_userinfo (
  userid varchar(64) BINARY not null default '',
  realname varchar(64) default '',
  localauth int(10) not null default 0,
  pwd varchar(64)   default '',
  pwddeath int(10) default 0,
  getpwddeath int(10) default 0 ,
  initactpwd varchar(64)   default '',
  initactpwddeath int(10) default 0,
  paperstype int(10) default 0 ,
  papersnumber varchar(64)  default '',
  email varchar(255)  default '',
  address varchar(128)  default '',
  tel varchar(64)  default '',
  cellphone  varchar(64)  default '',
  locked int(10) default 0,
  temploginerrcnt int(10) default 0,
  longloginerrcnt int(10) default 0,
  loginlocktime int(10) default 0,
  logincnt int(10) default 0,
  lastlogintime int(10) default 0,
  backendauth int(10) default 0,
  enabled int(10) default 1,
  domainid int(10) not null,
  orgunitid int(10),
  radprofileid int(10),
  createtime int(10) default 0,
  primary key (userid,domainid)
) engine=innodb;
create index idx_userinfo_locked on otppms_userinfo (locked);
create index idx_userinfo_backendauth on otppms_userinfo (backendauth);
create index idx_userinfo_enabled on otppms_userinfo (enabled);
create index idx_userinfo_localauth on otppms_userinfo (localauth);
alter table otppms_userinfo add constraint fk_userinfo_domaininfo_domainid 
foreign key (domainid) references otppms_domaininfo(domainid) on delete cascade on update cascade;
alter table otppms_userinfo add constraint fk_userinfo_orgunitinfo_orgunitid 
foreign key (orgunitid) references otppms_orgunitinfo(orgunitid) on delete cascade on update cascade;
alter table otppms_userinfo add constraint fk_userinfo_rad_profile_radprofileid 
foreign key (radprofileid) references otppms_rad_profile(id) on delete cascade on update cascade;
    
-- otppms_tokenspec --
create table if not exists otppms_tokenspec (
  specid varchar(32) not null,
  tokentype int(10) default 0,
  algid int(10) default 0,
  signsuite varchar(64) default '',
  cvssuite varchar(64) default '',
  crsuite varchar(64) default '',
  atid varchar(16) default '',
  otplen int(10) default 0,
  intervaltime int(10) default 0,
  begintime int(10) default 0,
  maxauthcnt int(10) default 0,
  initauthnum int(10) default 0,
  haformat int(10) default 0,
  halen int(10) default 0,
  cardrow int(10) default 0,
  cardcol int(10) default 0,
  rowstart varchar(2) default 'A',
  colstart varchar(2) default '1',
  updatemode int(10) default 0,
  updatelimit int(10) default 0,
  updateresplen int(10) default 0,
  puk1mode int(10) default 0,
  puk1len int(10) default 0,
  puk1itv int(10) default 0,
  puk2mode int(10) default 0,
  puk2len int(10) default 0,
  puk2itv int(10) default 0,
  maxcounter int(10) default 0,
  syncmode int(10) default 0,
  descp varchar(255),
  primary key (specid)
) engine=innodb;

-- otppms_tokeninfo --
create table if not exists otppms_tokeninfo (
  token varchar(32) not null,
  enabled int(10) default 0,
  locked int(10) default 0,
  lost int(10) default  0,
  logout int(10) default 0,
  pubkey varchar(1024) not null default '',
  authnum varchar(32) default '0',
  authmethod int(10) default 0,
  empin varchar(32) default '',
  empindeath int(10) default 0,
  expiretime  int(10) default 0,
  syncwnd int(10) default 0,
  temploginerrcnt int(10) default 0,
  longloginerrcnt int(10) default 0,
  loginlocktime int(10) default 0,
  driftcount int(10) default 0,
  physicaltype int(10) default 0,
  producttype int(10) default 0,
  specid varchar(32) not null,
  domainid int(10) not null,
  orgunitid int(10),
  gensmstime int(10) default 0,
  importtime int(10) default 0,
  distributetime int(10) default 0,
  crauthnum varchar(32) default '0',
  authtime int(10) default 0,
  crauthtime int(10) default 0,
  authotp varchar(32) default '',
  crauthotp varchar(32) default '',
  crauthdata varchar(32) default '',
  cractivecode varchar(32) default '',
  pubkeystate int(10) default 0,
  newpubkey varchar(1024) default 0,
  cractivetime int(10) default 0,
  cractivecount int(10) default 0,
  vendorid varchar(32) not null default '',
  primary key (token)
) engine=innodb;
create index idx_tokeninfo_enabled on otppms_tokeninfo (enabled);
create index idx_tokeninfo_locked on otppms_tokeninfo (locked);
create index idx_tokeninfo_lost on otppms_tokeninfo (lost);
create index idx_tokeninfo_logout on otppms_tokeninfo (logout);
create index idx_tokeninfo_authmethod on otppms_tokeninfo (authmethod);
create index idx_tokeninfo_physicaltype on otppms_tokeninfo (physicaltype);
create index idx_tokeninfo_producttype on otppms_tokeninfo (producttype);
alter table otppms_tokeninfo add constraint fk_tokeninfo_domaininfo_domainid 
foreign key(domainid) references otppms_domaininfo(domainid) on delete cascade on update cascade ;
alter table otppms_tokeninfo add constraint fk_tokeninfo_orgunitinfo_orgunitid 
foreign key(orgunitid) references otppms_orgunitinfo(orgunitid) on delete cascade on update cascade ;
alter table otppms_tokeninfo add constraint fk_tokeninfo_tokenspec_specid 
foreign key(specid) references otppms_tokenspec(specid) on delete cascade on update cascade ;

-- otppms_tokenext --
create table if not exists otppms_tokenext (
  token varchar(32) not null,
  pubkeyfactor varchar(128) default '',
  phoneudid varchar(32) default '',
  activepass varchar(32) default '',
  apdeath int(10) default 0,
  apretry int(10) default 0,
  actived int(10) default 0,
  activetime int(10) default 0,
  provtype int(10) default 0,
  exttype varchar(16) default '',
  primary key (token)  
) engine=innodb;
create index idx_tokenext_actived on otppms_tokenext(actived);
create index idx_tokenext_provtype on otppms_tokenext(provtype);
alter table otppms_tokenext add constraint fk_tokenext_tokeninfo_token 
foreign key(token) references otppms_tokeninfo(token) on delete cascade on update cascade;

-- otppms_user_token --
create table if not exists otppms_user_token (
  bindid int(10) not null auto_increment,
  userid varchar(64) not null,
  token varchar(32) not null,
  domainid int(10),
  bindtime int(10) not null default 0,
  primary key (bindid)
) engine=innodb;
create unique index uk_user_token on otppms_user_token(userid, token, domainid);
alter table otppms_user_token add constraint fk_user_token_tokeninfo_token 
foreign key(token) references otppms_tokeninfo(token) on delete cascade on update cascade;
alter table otppms_user_token add constraint fk_user_token_domaininfo_domainid 
foreign key(domainid) references otppms_domaininfo(domainid) on delete cascade on update cascade;

-- otppms_hostinfo --
create table if not exists otppms_hostinfo (
  hostipaddr varchar(64) not null,
  hostname varchar(64),
  confid int(10) default 0,
  priority int(10) default 0,
  licid varchar(32) default '',
  descp varchar(255) default '',
  ftradiusenabled int(10) default 1,
  protocoltype varchar(32) not null default 'udp',
  authport int(10) not null default 1915,
  syncport int(10) not null default 1916,
  radiusenabled int(10) not null default 1,
  radauthport int(10) not null default 1812,
  httpenabled int(10) default 0,
  httpport int(10) default 18080,
  httpsenabled int(10) default 0,
  httpsport int(10) default 18443,
  keystorepwd varchar(64) default '',
  certificatepwd varchar(64) default '',
  keystoreinstance varchar(32) default '',
  keystorerootpath varchar(4000) default '',
  soapenabled int(10) not null default 1,
  soapport int(10) not null default 18081,
  webservicename varchar(32) not null default 'otpwebservice',
  primary key (hostipaddr)
) engine=innodb;

-- otppms_agentconf --
create table otppms_agentconf (
  id int(10) not null auto_increment,
  confname varchar(64) not null,
  type int(10) not null default 0,
  userformat int(10) default 0,
  localprotect int(10) default 0,
  remoteprotect int(10) default 0,
  uacprotect int(10) default 0,
  unboundlogin int(10) default 0,
  descp varchar(255) default '',
  primary key (id)  
) engine=innodb;
create unique index uk_agentconf_confname on otppms_agentconf(confname);

-- otppms_agentinfo --
create table if not exists otppms_agentinfo  (
  agentipaddr varchar(64) not null ,
  agentname varchar(64) default '',
  pubkey varchar(64) not null default '',
  enabled int(10) not null default 1,
  descp varchar(255) default '',
  agenttype int(10) not null default 0,
  agentconfid int(10) default 0,
  graceperiod int(10) default 0,
  primary key (agentipaddr)
) engine=innodb;

-- otppms_agent_host --
create table if not exists otppms_agent_host (
  agentipaddr varchar(64) not null,
  hostipaddr varchar(64) not null,
  primary key (agentipaddr, hostipaddr)
) engine=innodb;
alter table otppms_agent_host add constraint fk_agent_host_agentinfo_agentipaddr 
foreign key (agentipaddr) references otppms_agentinfo(agentipaddr) on delete cascade on update cascade;
alter table otppms_agent_host add constraint fk_agent_host_hostinfo_hostipaddr 
foreign key (hostipaddr) references otppms_hostinfo(hostipaddr) on delete cascade on update cascade;

-- otppms_licinfo --
create table if not exists otppms_licinfo(
  licid varchar(32) not null,
  licinfo varchar(2048) not null,
  lictype int(10) not null,
  issuer varchar(64) not null,
  customerid varchar(64) not null,
  licstate int(10) not null default 1,
  licupdatetime int(10) not null default 0,
  primary key (licid)
)engine=innodb;

-- otppms_loginfo --
create table if not exists otppms_loginfo (
  id int(10) not null auto_increment,
  userid varchar(64) default '',
  token varchar(32) default '',
  serverip varchar(64) default '',
  clientip varchar(64) default '',
  actionid int(10) not null,
  actionresult int(10) not null,
  logtime int(10) not null,
  logcontent varchar(255) default '',
  hashcode varchar(255) default 0,
  domainid int(10) default 0,
  domainname varchar(64) default '',
  orgunitid int(10) default 0,
  orgunitname varchar(64) default '',
  moduletype int(10) not null default 0,
  vendorid varchar(32) default '',
  primary key (id)
) engine=innodb;

-- otppms_admininfo --
create table if not exists otppms_admininfo (
  adminid varchar(64) not null,
  realname varchar(64) default '',
  localauth int(10) not null default 0,
  pwd varchar(64) not null,
  pwdsettime int(10) not null default 0,
  getpwddeath int(10) default 0,
  locked int(10) default 0,
  temploginerrcnt int(10) default 0,
  longloginerrcnt int(10) default 0,
  loginlocktime int(10) default 0,
  logintime int(10) default 0,
  logincnt int(10) default 0,
  createuser varchar (64) not null,
  enabled int(10) default 1,
  email varchar(255) default '',
  address varchar(128) default '',
  tel varchar(64) default '',
  cellphone varchar(64) default '',
  createtime int(10) not null,
  descp varchar(255) default '',
  primary key (adminid)
) engine=innodb;

-- otppms_admin_orgunit --
create table if not exists otppms_admin_orgunit (
  adminid varchar(64) not null,
  domainid int(10) not null,
  orgunitid int(10)
 
) engine=innodb;
alter table otppms_admin_orgunit add constraint fk_admin_orgunit_admininfo_adminid 
foreign key (adminid) references otppms_admininfo(adminid) on delete cascade on update cascade;
alter table otppms_admin_orgunit add constraint fk_admin_orgunit_domaininfo_domainid 
foreign key (domainid) references otppms_domaininfo(domainid) on delete cascade on update cascade;
alter table otppms_admin_orgunit add constraint fk_admin_orgunit_orgunitinfo_orgunitid 
foreign key (orgunitid) references  otppms_orgunitinfo(orgunitid) on delete cascade on update cascade;

-- otppms_perminfo --
create table if not exists otppms_perminfo (
  permcode varchar(64) not null,
  permlink varchar(255) default '',
  srcname varchar(255) default '',
  keymark varchar(64) default '',
  descp varchar(255) default '',
  primary key (permcode)
) engine=innodb;

-- otppms_roleinfo --
create table if not exists otppms_roleinfo (
  roleid int(10) not null auto_increment,
  rolename varchar(64) not null default '',
  rolemark varchar(64) default '',
  createuser  varchar(64) not null default '',
  createtime int(10) not null default 0,
  modifytime int(10) not null default 0,
  descp varchar(255) default '',
  primary key (roleid)
) engine=innodb;
-- alter table otppms_roleinfo add constraint fk_roleinfo_admininfo_createuser 
-- foreign key (createuser) references otppms_admininfo(adminid) on delete cascade on update cascade;

-- otppms_role_perm --
create table if not exists otppms_role_perm (
  roleid int(10) not null,
  permcode varchar(64) not null,
  primary key (roleid, permcode)
) engine=innodb;
alter table otppms_role_perm add	constraint fk_role_perm_roleinfo_roleid 
foreign key (roleid) references otppms_roleinfo(roleid) on delete cascade on update cascade;
alter table otppms_role_perm add constraint fk_role_perm_perminfo_permcode 
foreign key (permcode) references otppms_perminfo(permcode) on delete cascade on update cascade;

-- otppms_admin_role --
create table if not exists otppms_admin_role (
  adminid varchar(64) not null,
  roleid int(10) not null,
  primary key (adminid, roleid)
) engine=innodb;
alter table otppms_admin_role add constraint fk_admin_role_admininfo_adminid 
foreign key (adminid) references otppms_admininfo(adminid) on delete cascade on update cascade;
alter table otppms_admin_role add constraint fk_admin_role_roleinfo_roleid 
foreign key (roleid) references otppms_roleinfo(roleid) on delete cascade on update cascade;

-- otppms_admin_log --
create table if not exists otppms_admin_log (
  id int(10) not null auto_increment,
  operator varchar(64) default '',
  logtime int(10) not null,
  actionid int(10) not null,
  actionresult int(10) not null,
  actionobject int(10) default 0,
  clientip varchar(64) default '',
  descp varchar(4000) default '',
  hashcode varchar(255) default '',
  primary key (id)
) engine=innodb;

-- otppms_taskinfo --
create table if not exists otppms_taskinfo (
  taskname varchar(128) not null default '',
  sourceid int(10) not null,
  sourcetype int(10) not null default 0,
  taskmode1 int(10) not null,
  taskmode2 int(10) not null,
  taskminute varchar(64) default '',
  taskhour varchar(64) default '',
  taskday varchar(64) default '',
  taskmonth varchar(64) default '',
  taskweek varchar(64) default '',
  enabled int(10) default 1,
  taskid varchar(128) not null default '',
  descp varchar(255) default '',
  primary key  (taskname)
) engine=innodb;
create unique index uk_taskinfo on otppms_taskinfo (taskid, sourcetype);

-- otppms_configinfo --
create table if not exists otppms_configinfo (
  id int(10) not null auto_increment,
  confname varchar(64) not null ,
  conftype varchar(64) not null,
  confvalue varchar(128) not null ,
  parentid int(10) default -1 ,
  descp varchar(255) default '',
  primary key (id)
) engine=innodb;
create unique index uk_configinfo_confname_conftype_confvalue on otppms_configinfo(confname,conftype,confvalue); 

-- otppms_usersource --
create table otppms_usersource (
  id int(10) not null auto_increment,
  sourcename varchar(64) not null default '',
  sourcetype int(10) not null default 0,
  dbtype int(10) default 0,
  serveraddr varchar(64) default '',
  port int(10) default 0,
  username varchar(64) not null default '',
  pwd varchar(64) default '',
  dbname varchar(64) default '',
  dbtablename varchar(64) default '',
  filter varchar(255) default '',
  basedn varchar(255) default '',
  rootdn varchar(255) default '',
  dominodriver varchar(64) default '',
  namesfile varchar(64) default '',
  timeout int(10) default 30,
  isupdateou int(10) default 0,
  upinvaliduser int(10) default 0,
  domainid int(10) not null,
  orgunitid int(10),
  localusermark int(10) default 0,
  issyncuserinfo int(10) default 0,
  descp varchar(255) default '',
  primary key (id)
) engine=innodb;
create unique index uk_usersource_sourcename on otppms_usersource(sourcename);

-- otppms_usersource_attr --
create table otppms_usersource_attr (
  sourceid int(10) not null,
  localuserattr varchar(64) default '',
  sourceuserattr varchar(64) default '',
  primary key (sourceid, localuserattr, sourceuserattr)
) engine=innodb;
alter table otppms_usersource_attr add constraint otppms_usersource_attr_sourceid 
foreign key (sourceid) references otppms_usersource(id) on delete cascade on update cascade;

-- otppms_emailinfo --
create table otppms_emailinfo (
  id int(10) not null auto_increment,
  servname varchar(128) not null default '',
  account varchar(64) not null default '',
  pwd varchar(64) default '',
  sender varchar(64) not null default '',
  host varchar(64) not null default '',
  port int(10) not null default 25,
  validated int(10) default 0,
  isdefault int(10) default 0,
  descp varchar(255) default '',
  primary key (id)
) engine=innodb;
create unique index uk_emailinfo_servname on otppms_emailinfo(servname);

-- otppms_smsinfo --
create table otppms_smsinfo (
  id int(10) not null auto_increment,
  smsname varchar(64) not null default '',
  sendtype int(10) default 0,
  host varchar(128) not null default '',
  username varchar(64) not null default '',
  pwd varchar(64) not null default '',
  accountattr varchar(32) not null default '',
  passwdattr varchar(32) not null default '',
  phoneattr varchar(32) not null default '',
  messageattr varchar(32) default '',
  paramannex varchar(128) default '',
  sendresult varchar(255) default '',
  enabled int(10) default 0,
  priority int(10) default 0,
  retrysend int(10) default 0,
  descp varchar(255) default '',
  primary key (id)
) engine=innodb;
create unique index uk_smsinfo_smsname on otppms_smsinfo(smsname);

-- otppms_attrinfo --
create table otppms_attrinfo (
  attrtype int(10) not null default 0,
  attrid varchar(32) not null default '',
  attrname varchar(64) not null default '',
  attrvalue varchar(128) not null default '',
  valuetype varchar(32) not null default '',
  profileid int(10) not null,
  vendorid int(10) not null default -1,
  primary key (attrid, profileid,vendorid) 
) engine=innodb;
alter table otppms_attrinfo add constraint fk_attrinfo_rad_profile_profileid 
foreign key (profileid) references otppms_rad_profile(id) on delete cascade on update cascade;

-- otppms_trustipinfo --
create table otppms_trustipinfo (
  id int(10) not null auto_increment ,
  systype int(10) not null default 0,
  clientapptype int(10) not null default 0,
  startip varchar(64) not null default '',
  endip varchar(64) default '',
  expiretime int(10) default 0,
  updatetime int(10) default 0,
  enabled int(10) default 0,
  clientappport int(10) default 0,
  clientservpath varchar(64) default '',
  primary key (id) 
) engine=innodb;

-- otppms_portal_notice --
create table otppms_portal_notice (
  id int(10) not null auto_increment,
  systype int(10) not null default 0,
  createuser varchar(64) default '',
  createtime int(10) default 0,
  expiretime int(10) default 0,
  content text  default '',
  title varchar(128) not null,
  primary key (id)
) engine=innodb;

-- otppms_backend --
create table otppms_backend (
  id int(10) not null auto_increment,
  backendtype int(10) not null default 0,
  backendname varchar(64) not null,
  host varchar(64) not null default '',
  sparehost varchar(64) default '',
  port int(10) not null default 389,
  priority int(10) default 0,
  basedn varchar(255) default '',
  filter varchar(255) default '(&(objectCategory=person)(objectClass=user))',
  pubkey varchar(255) default '',  
  timeout int(10) default 30,
  retrycnt int(10) default 0,
  usernamerule int(10) default 0,
  delimiter varchar(32) default '@',
  enabled int(10) default 0,
  policy int(10) default 0,
  domainid int(10) default 0,
  primary key (id)
) engine=innodb;
create unique index uk_backend_backendname on otppms_backend(backendname);
create unique index uk_backend_host_port_domainid on otppms_backend(host,port,domainid);

-- otppms_monitor_admin --
create table if not exists otppms_monitor_admin (
  adminid varchar(64) not null,
  conftype varchar(64) not null,
  primary key (adminid, conftype)
) engine=innodb;
alter table otppms_monitor_admin add constraint fk_monitor_admin_admininfo_adminid 
foreign key (adminid) references otppms_admininfo(adminid) on delete cascade on update cascade;

-- otppms_monitorinfo --
create table if not exists otppms_monitorinfo (
  id int(10) not null auto_increment,
  email varchar(1024),
  mobile varchar(1024),
  confid int(10) default 0,
  conftype varchar(64) not null,
  title varchar(128) default '',
  content text default '',
  sendtime int(10) default 0,
  issend int(10) default 0,
  primary key (id)
) engine=innodb;

-- otppms_admin_perm -- 
create table if not exists otppms_admin_perm (
	adminid varchar(64) not null,
	permcode varchar(64) not null,
	primary key (adminid, permcode)
)engine=innodb;
alter table otppms_admin_perm add constraint fk_admin_perm_admininfo_admid 
foreign key (adminid) references otppms_admininfo(adminid) on delete cascade on update cascade;
alter table otppms_admin_perm add constraint fk_admin_perm_perminfo_percode 
foreign key (permcode) references otppms_perminfo(permcode) on delete cascade on update cascade;


CREATE TABLE otppms_customer (
  id int(11) NOT NULL AUTO_INCREMENT,
  custid varchar(64) DEFAULT NULL,
  custname varchar(64) DEFAULT NULL,
  dept varchar(64) DEFAULT NULL,
  contacts text,
  createtime int(10) DEFAULT NULL,
  operator varchar(64) DEFAULT '',
  primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE otppms_prjinfo (
  id int(11) NOT NULL AUTO_INCREMENT,
  prjdesc varchar(1024) DEFAULT NULL,
  type varchar(64) DEFAULT NULL,
  prjid varchar(64) DEFAULT NULL,
  svn varchar(2048) DEFAULT NULL,
  bug varchar(512) DEFAULT NULL,
  path varchar(2048) DEFAULT NULL,
  content text,
  developer varchar(1024) DEFAULT NULL,
  tester varchar(1024) DEFAULT NULL,
  createtime int(10) DEFAULT NULL,
  operator varchar(64) DEFAULT NULL,
  results varchar(1024) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE otppms_project (
  id int(11) NOT NULL AUTO_INCREMENT,
  prjid varchar(64) DEFAULT NULL,
  prjname varchar(1024) DEFAULT NULL,
  custid int(10) DEFAULT NULL,
  type varchar(64) DEFAULT NULL,
  typeversion varchar(32) DEFAULT NULL,
  prjstate varchar(1024) DEFAULT NULL,
  needdept varchar(1024) DEFAULT NULL,
  sales varchar(1024) DEFAULT NULL,
  techsupport varchar(1024) DEFAULT NULL,
  ifpay varchar(64) DEFAULT NULL,
  svn varchar(2048) DEFAULT NULL,
  bug varchar(2048) DEFAULT NULL,
  prjdesc text,
  createtime int(10) DEFAULT NULL,
  operator varchar(64) DEFAULT NULL,
  updatetime int(10) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE otppms_cutomer_user (
  Id int(11) NOT NULL AUTO_INCREMENT,
  userid varchar(64) DEFAULT NULL ,
  projectid varchar(255) DEFAULT NULL ,
  useremail varchar(255) DEFAULT NULL ,
  PRIMARY KEY (Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `otppms_project_result`
-- ----------------------------
CREATE TABLE otppms_project_result (
  Id int(11) NOT NULL AUTO_INCREMENT,
  prjid varchar(64) DEFAULT NULL,
  results text ,
  operator varchar(255) DEFAULT NULL,
  createtime varchar(255) DEFAULT NULL,
  PRIMARY KEY (Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `otppms_record_back`
-- ----------------------------
CREATE TABLE otppms_record_back (
  Id int(11) NOT NULL AUTO_INCREMENT,
  recordtime int(11) DEFAULT NULL,
  endrecordtime int(11) DEFAULT NULL,
  recordUser varchar(512) DEFAULT NULL,
  cutomer varchar(255) DEFAULT NULL,
  reason text,
  results text,
  operator varchar(255) DEFAULT NULL,
  createtime varchar(255) DEFAULT NULL,
  prjid varchar(1024) DEFAULT NULL,
  remark varchar(4096) DEFAULT NULL,
  PRIMARY KEY (Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

