/*
  索引：idx_表名（或表名缩写）_字段名（或字段名缩写）
  外键：fk_本表名（或表名缩写）_关联表名（或表名缩写）_字段名（或字段名缩写）
  唯一索引：uk_表名（或表名缩写）_唯一健名（可以是字段名缩写）
*/

-- otppms_domaininfo --
create table otppms_domaininfo (
  domainid int IDENTITY(1,1) not null,
  domainsn varchar(64) default '',
  domainname varchar(128) default '', 
  descp varchar(255) default '',
  createtime int not null default 0,
  primary key (domainid),
  CONSTRAINT uk_domaininfo_domainname unique (domainname)
);

-- otppms_orgunitinfo --
create table otppms_orgunitinfo (
  orgunitid int identity(1,1) not null,
  orgunitnumber varchar(32) default '',
  orgunitname varchar(128) not null default '' ,
  parentid int default 0 ,
  domainid int not null default 0,
  descp varchar(255) default '',
  createtime int not null default 0,
  primary key (orgunitid),
  CONSTRAINT uk_orgunitinfo_orgunitname_parentid unique (orgunitname, parentid,domainid),
  CONSTRAINT fk_orgunitinfo_domaininfo_domainid FOREIGN KEY (domainid) REFERENCES otppms_domaininfo(domainid)  ON DELETE CASCADE ON UPDATE CASCADE
);

-- otppms_rad_profile --
create table otppms_rad_profile (
  id int identity(1,1) not null,
  profilename varchar(64) not null default '',
  descp varchar(255) default '',
  primary key (id),
  CONSTRAINT uk_rad_profile_profilename unique (profilename)
);

-- otppms_userinfo --
create table otppms_userinfo (
  userid varchar(64) not null default '',
  realname varchar(64) default '',
  localauth int not null default 0,
  pwd varchar(64) default '',
  pwddeath int default 0,
  getpwddeath int default 0 ,
  initactpwd varchar(64) default '',
  initactpwddeath int default 0 ,
  paperstype int default 0 ,
  papersnumber varchar(64)  default '',
  email varchar(255)  default '',
  address varchar(128)  default '',
  tel varchar(64)  default '',
  cellphone  varchar(64)  default '',
  locked int default 0,
  temploginerrcnt int default 0,
  longloginerrcnt int default 0,
  loginlocktime int default 0,
  logincnt int default 0,
  lastlogintime int default 0,
  backendauth int default 0,
  enabled int default 1,
  domainid int not null,
  orgunitid int,
  radprofileid int,
  createtime int default 0,
  primary key (userid,domainid),
  constraint fk_userinfo_domaininfo_domainid foreign key (domainid) references otppms_domaininfo(domainid) on delete cascade on update cascade,
  constraint fk_userinfo_orgunitinfo_orgunitid foreign key (orgunitid) references otppms_orgunitinfo(orgunitid) ON DELETE NO ACTION ON UPDATE NO ACTION,
  constraint fk_userinfo_rad_profile_radprofileid foreign key (radprofileid) references otppms_rad_profile(id) on delete cascade on update cascade
);
create index idx_userinfo_locked on otppms_userinfo (locked);
create index idx_userinfo_backendauth on otppms_userinfo (backendauth);
create index idx_userinfo_enabled on otppms_userinfo (enabled);
create index idx_userinfo_localauth on otppms_userinfo (localauth);
   
-- otppms_tokenspec --
create table otppms_tokenspec (
  specid varchar(32) not null,
  tokentype int default 0,
  algid int default 0,
  signsuite varchar(64) default '',
  cvssuite varchar(64) default '',
  crsuite varchar(64) default '',
  atid varchar(16) default '',
  otplen int default 0,
  intervaltime int default 0,
  begintime int default 0,
  maxauthcnt int default 0,
  initauthnum int default 0,
  haformat int default 0,
  halen int default 0,
  cardrow int default 0,
  cardcol int default 0,
  rowstart varchar(2) default 'A',
  colstart varchar(2) default '1',
  updatemode int default 0,
  updatelimit int default 0,
  updateresplen int default 0,
  puk1mode int default 0,
  puk1len int default 0,
  puk1itv int default 0,
  puk2mode int default 0,
  puk2len int default 0,
  puk2itv int default 0,
  maxcounter int default 0,
  syncmode int default 0,
  descp varchar(255),
  primary key (specid)
);

-- otppms_tokeninfo --
create table otppms_tokeninfo (
  token varchar(32) not null,
  enabled int default 0,
  locked int default 0,
  lost int default  0,
  logout int default 0,
  pubkey varchar(1024) not null default '',
  authnum varchar(32) default '0',
  authmethod int default 0,
  empin varchar(32) default '',
  empindeath int default 0,
  expiretime  int default 0,
  syncwnd int default 0,
  temploginerrcnt int default 0,
  longloginerrcnt int default 0,
  loginlocktime int default 0,
  driftcount int default 0,
  physicaltype int default 0,
  producttype int default 0,
  specid varchar(32) not null,
  domainid int not null,
  orgunitid int,
  gensmstime int default 0,
  importtime int default 0,
  distributetime int default 0,
  crauthnum varchar(32) default '0',
  authtime int default 0,
  crauthtime int default 0,
  authotp varchar(32) default '',
  crauthotp varchar(32) default '',
  crauthdata varchar(32) default '',
  cractivecode varchar(32) default '',
  pubkeystate int default 0,
  newpubkey varchar(1024) default 0,
  cractivetime int default 0,
  cractivecount int default 0,
  vendorid varchar(32) not null default '',
  primary key (token),
  constraint fk_tokeninfo_domaininfo_domainid foreign key(domainid) references otppms_domaininfo(domainid) on delete cascade on update cascade,
  constraint fk_tokeninfo_orgunitinfo_orgunitid foreign key(orgunitid) references otppms_orgunitinfo(orgunitid) ON DELETE NO ACTION ON UPDATE NO ACTION,
  constraint fk_tokeninfo_tokenspec_specid foreign key(specid) references otppms_tokenspec(specid) on delete cascade on update cascade
);
create index idx_tokeninfo_enabled on otppms_tokeninfo (enabled);
create index idx_tokeninfo_locked on otppms_tokeninfo (locked);
create index idx_tokeninfo_lost on otppms_tokeninfo (lost);
create index idx_tokeninfo_logout on otppms_tokeninfo (logout);
create index idx_tokeninfo_authmethod on otppms_tokeninfo (authmethod);
create index idx_tokeninfo_physicaltype on otppms_tokeninfo (physicaltype);
create index idx_tokeninfo_producttype on otppms_tokeninfo (producttype);

-- otppms_tokenext --
create table otppms_tokenext (
  token varchar(32) not null,
  pubkeyfactor varchar(128) default '',
  phoneudid varchar(32) default '',
  activepass varchar(32) default '',
  apdeath int default 0,
  apretry int default 0,
  actived int default 0,
  activetime int default 0,
  provtype int default 0,
  exttype varchar(16) default '',
  primary key (token),
  constraint fk_tokenext_tokeninfo_token foreign key(token) references otppms_tokeninfo(token) on delete cascade on update cascade
);
create index idx_tokenext_actived on otppms_tokenext(actived);
create index idx_tokenext_provtype on otppms_tokenext(provtype);

-- otppms_user_token --
create table otppms_user_token (
  bindid int identity(1,1) not null,
  userid varchar(64) not null,
  token varchar(32) not null,
  domainid int,
  bindtime int not null default 0,
  primary key (bindid),
  constraint uk_user_token unique(userid, token, domainid),
  constraint fk_user_token_tokeninfo_token foreign key(token) references otppms_tokeninfo(token) ON DELETE CASCADE ON UPDATE CASCADE,
  constraint fk_user_token_domaininfo_domainid foreign key(domainid) references otppms_domaininfo(domainid) ON DELETE NO ACTION ON UPDATE NO ACTION
);

-- otppms_hostinfo --
create table otppms_hostinfo (
  hostipaddr varchar(64) not null,
  hostname varchar(64),
  confid int default 0,
  priority int default 0,
  licid varchar(32) default '',
  descp varchar(255) default '',
  ftradiusenabled int default 1,
  protocoltype varchar(32) not null default 'udp',
  authport int not null default 1915,
  syncport int not null default 1916,
  radiusenabled int not null default 1,
  radauthport int not null default 1812,
  httpenabled int default 0,
  httpport int default 18080,
  httpsenabled int default 0,
  httpsport int default 18443,
  keystorepwd varchar(64) default '',
  certificatepwd varchar(64) default '',
  keystoreinstance varchar(32) default '',
  keystorerootpath varchar(4000) default '',
  soapenabled int not null default 1,
  soapport int not null default 18081,
  webservicename varchar(32) not null default 'otpwebservice',
  primary key (hostipaddr)
);

-- otppms_agentconf --
create table otppms_agentconf (
  id int identity(1,1) not null,
  confname varchar(64) not null,
  type int not null default 0,
  userformat int default 0,
  localprotect int default 0,
  remoteprotect int default 0,
  uacprotect int default 0,
  unboundlogin int default 0,
  descp varchar(255) default '',
  primary key (id),
  constraint uk_agentconf_confname unique(confname)
);

-- otppms_agentinfo --
create table otppms_agentinfo  (
  agentipaddr varchar(64) not null ,
  agentname varchar(64) default '',
  pubkey varchar(64) not null default '',
  enabled int not null default 1,
  descp varchar(255) default '',
  agenttype int not null default 0,
  agentconfid int default 0,
  graceperiod int default 0,
  primary key (agentipaddr)
);

-- otppms_agent_host --
create table otppms_agent_host (
  agentipaddr varchar(64) not null,
  hostipaddr varchar(64) not null,
  primary key (agentipaddr, hostipaddr),
  constraint fk_agent_host_agentinfo_agentipaddr foreign key (agentipaddr) references otppms_agentinfo(agentipaddr) on delete cascade on update cascade,
  constraint fk_agent_host_hostinfo_hostipaddr foreign key (hostipaddr) references otppms_hostinfo(hostipaddr) on delete cascade on update cascade
);

-- otppms_licinfo --
create table otppms_licinfo(
  licid varchar(32) not null,
  licinfo varchar(2048) not null,
  lictype int not null,
  issuer varchar(64) not null,
  customerid varchar(64) not null,
  licstate int not null default 1,
  licupdatetime int not null default 0,
  primary key (licid)
);

-- otppms_loginfo --
create table otppms_loginfo (
  id int identity(1,1) not null,
  userid varchar(64) default '',
  token varchar(32) default '',
  serverip varchar(64) default '',
  clientip varchar(64) default '',
  actionid int not null,
  actionresult int not null,
  logtime int not null,
  logcontent varchar(255) default '',
  hashcode varchar(255) default 0,
  domainid int default 0,
  domainname varchar(64) default '',
  orgunitid int default 0,
  orgunitname varchar(64) default '',
  moduletype int not null default 0,
  vendorid varchar(32) default '',
  primary key (id)
);

-- otppms_admininfo --
create table otppms_admininfo (
  adminid varchar(64) not null,
  realname varchar(64) default '',
  localauth int not null default 0,
  pwd varchar(64) not null,
  pwdsettime int not null default 0,
  getpwddeath int default 0,
  locked int default 0,
  temploginerrcnt int default 0,
  longloginerrcnt int default 0,
  loginlocktime int default 0,
  logintime int default 0,
  logincnt int default 0,
  createuser varchar (64) not null,
  enabled int default 1,
  email varchar(255) default '',
  address varchar(128) default '',
  tel varchar(64) default '',
  cellphone varchar(64) default '',
  createtime int not null,
  descp varchar(255) default '',
  primary key (adminid)
);

-- otppms_admin_orgunit --
create table otppms_admin_orgunit (
  adminid varchar(64) not null,
  domainid int not null,
  orgunitid int,
  constraint fk_admin_orgunit_admininfo_adminid foreign key (adminid) references otppms_admininfo(adminid) on delete cascade on update cascade,
  constraint fk_admin_orgunit_domaininfo_domainid foreign key (domainid) references otppms_domaininfo(domainid) on delete NO ACTION on update NO ACTION,
  constraint fk_admin_orgunit_orgunitinfo_orgunitid foreign key (orgunitid) references  otppms_orgunitinfo(orgunitid) ON DELETE CASCADE ON UPDATE CASCADE
);

-- otppms_perminfo --
create table otppms_perminfo (
  permcode varchar(64) not null,
  permlink varchar(255) default '',
  srcname varchar(255) default '',
  keymark varchar(64) default '',
  descp varchar(255) default '',
  primary key (permcode)
);

-- otppms_roleinfo --
create table otppms_roleinfo (
  roleid int identity(1,1) not null,
  rolename varchar(64) not null default '',
  rolemark varchar(64) default '',
  createuser  varchar(64) not null default '',
  createtime int not null default 0,
  modifytime int not null default 0,
  descp varchar(255) default '',
  primary key (roleid),
--  constraint fk_roleinfo_admininfo_createuser foreign key (createuser) references otppms_admininfo(adminid) on delete cascade on update cascade
);

-- otppms_role_perm --
create table otppms_role_perm (
  roleid int not null,
  permcode varchar(64) not null,
  primary key (roleid, permcode),
  constraint fk_role_perm_roleinfo_roleid foreign key (roleid) references otppms_roleinfo(roleid) on delete cascade on update cascade,
  constraint fk_role_perm_perminfo_permcode foreign key (permcode) references otppms_perminfo(permcode) on delete cascade on update cascade
);

-- otppms_admin_role --
create table otppms_admin_role (
  adminid varchar(64) not null,
  roleid int not null,
  primary key (adminid, roleid),
  constraint fk_admin_role_admininfo_adminid foreign key (adminid) references otppms_admininfo(adminid) on delete cascade on update cascade,
  constraint fk_admin_role_roleinfo_roleid foreign key (roleid) references otppms_roleinfo(roleid) ON DELETE CASCADE ON UPDATE CASCADE
);

-- otppms_admin_log --
create table otppms_admin_log (
  id int identity(1,1) not null,
  operator varchar(64) default '',
  logtime int not null,
  actionid int not null,
  actionresult int not null,
  actionobject int default 0,
  clientip varchar(64) default '',
  descp varchar(4000) default '',
  hashcode varchar(255) default '',
  primary key (id)
);

-- otppms_taskinfo --
create table otppms_taskinfo (
  taskname varchar(128) not null default '',
  sourceid int not null,
  sourcetype int not null default 0,
  taskmode1 int not null,
  taskmode2 int not null,
  taskminute varchar(64) default '',
  taskhour varchar(64) default '',
  taskday varchar(64) default '',
  taskmonth varchar(64) default '',
  taskweek varchar(64) default '',
  enabled int default 1,
  taskid varchar(128) not null default '',
  descp varchar(255) default '',
  primary key  (taskname),
  constraint uk_taskinfo unique(taskid, sourcetype)
);

-- otppms_configinfo --
create table otppms_configinfo (
  id int identity(1,1) not null,
  confname varchar(64) not null ,
  conftype varchar(64) not null,
  confvalue varchar(128) not null ,
  parentid int default -1 ,
  descp varchar(255) default '',
  primary key (id),
  constraint uk_configinfo_confname_conftype_confvalue unique(confname,conftype,confvalue)
);

-- otppms_usersource --
create table otppms_usersource (
  id int identity(1,1) not null,
  sourcename varchar(64) not null default '',
  sourcetype int not null default 0,
  dbtype int default 0,
  serveraddr varchar(64) default '',
  port int default 0,
  username varchar(64) not null default '',
  pwd varchar(64) default '',
  dbname varchar(64) default '',
  dbtablename varchar(64) default '',
  filter varchar(255) default '',
  basedn varchar(255) default '',
  rootdn varchar(255) default '',
  dominodriver varchar(64) default '',
  namesfile varchar(64) default '',
  timeout int default 30,
  isupdateou int default 0,
  upinvaliduser int default 0,
  domainid int not null,
  orgunitid int,
  localusermark int default 0,
  issyncuserinfo int default 0,
  descp varchar(255) default '',
  primary key (id),
  constraint uk_usersource_sourcename unique(sourcename)
);

-- otppms_usersource_attr --
create table otppms_usersource_attr (
  sourceid int not null,
  localuserattr varchar(64) default '',
  sourceuserattr varchar(64) default '',
  primary key (sourceid, localuserattr, sourceuserattr),
  constraint otppms_usersource_attr_sourceid foreign key (sourceid) references otppms_usersource(id) on delete cascade on update cascade
);

-- otppms_emailinfo --
create table otppms_emailinfo (
  id int identity(1,1) not null,
  servname varchar(128) not null default '',
  account varchar(64) not null default '',
  pwd varchar(64) default '',
  sender varchar(64) not null default '',
  host varchar(64) not null default '',
  port int not null default 25,
  validated int default 0,
  isdefault int default 0,
  descp varchar(255) default '',
  primary key (id),
  constraint uk_emailinfo_servname unique(servname)
);

-- otppms_smsinfo --
create table otppms_smsinfo (
  id int identity(1,1) not null,
  smsname varchar(64) not null default '',
  sendtype int default 0,
  host varchar(128) not null default '',
  username varchar(64) not null default '',
  pwd varchar(64) not null default '',
  accountattr varchar(32) not null default '',
  passwdattr varchar(32) not null default '',
  phoneattr varchar(32) not null default '',
  messageattr varchar(32) default '',
  paramannex varchar(128) default '',
  sendresult varchar(255) default '',
  enabled int default 0,
  priority int default 0,
  retrysend int default 0,
  descp varchar(255) default '',
  primary key (id),
  constraint uk_smsinfo_smsname unique(smsname)
);

-- otppms_attrinfo --
create table otppms_attrinfo (
  attrtype int not null default 0,
  attrid varchar(32) not null default '',
  attrname varchar(64) not null default '',
  attrvalue varchar(128) not null default '',
  valuetype varchar(32) not null default '',
  profileid int not null,
  vendorid int not null default -1,
  primary key (attrid, profileid,vendorid),
  constraint fk_attrinfo_rad_profile_profileid foreign key (profileid) references otppms_rad_profile(id) on delete cascade on update cascade
);

-- otppms_trustipinfo --
create table otppms_trustipinfo (
  id int identity(1,1) not null,
  systype int not null default 0,
  clientapptype int not null default 0,
  startip varchar(64) not null default '',
  endip varchar(64) default '',
  expiretime int default 0,
  updatetime int default 0,
  enabled int default 0,
  clientappport int default 0,
  clientservpath varchar(64) default '',
  primary key (id) 
);

-- otppms_portal_notice --
create table otppms_portal_notice (
  id int identity(1,1) not null,
  systype int not null default 0,
  createuser varchar(64) default '',
  createtime int default 0,
  expiretime int default 0,
  content text  default '',
  title varchar(128) not null,
  primary key (id)
);

-- otppms_backend --
create table otppms_backend (
  id int identity(1,1) not null,
  backendtype int not null default 0,
  backendname varchar(64) not null,
  host varchar(64) not null default '',
  sparehost varchar(64) default '',
  port int not null default 389,
  priority int default 0,
  basedn varchar(255) default '',
  filter varchar(255) default '(&(objectCategory=person)(objectClass=user))',
  pubkey varchar(255) default '',  
  timeout int default 30,
  retrycnt int default 0,
  usernamerule int default 0,
  delimiter varchar(32) default '@',
  enabled int default 0,
  policy int default 0,
  domainid int default 0,
  primary key (id),
  constraint uk_backend_backendname unique(backendname),
  constraint uk_backend_host_port_domainid unique(host,port,domainid)
);

-- otppms_monitor_admin --
create table otppms_monitor_admin (
  adminid varchar(64) not null,
  conftype varchar(64) not null,
  primary key (adminid, conftype),
  constraint fk_monitor_admin_admininfo_adminid foreign key (adminid) references otppms_admininfo(adminid) on delete cascade on update cascade
);

-- otppms_monitorinfo --
create table otppms_monitorinfo (
  id int identity(1,1) not null,
  email varchar(1024),
  mobile varchar(1024),
  confid int default 0,
  conftype varchar(64) not null,
  title varchar(128) default '',
  content text default '',
  sendtime int default 0,
  issend int default 0,
  primary key (id)
);

-- otppms_admin_perm --
create table otppms_admin_perm (
	adminid varchar(64) not null,
	permcode varchar(64) not null,
	primary key(adminid,permcode),
	constraint fk_admin_perm_admininfo_admid  foreign key (adminid) references otppms_admininfo (adminid) on delete cascade on update cascade,
	constraint fk_admin_perm_perminfo_percode  foreign key (permcode) references otppms_perminfo (permcode) on delete cascade on update cascade
);
