-- otppms_domaininfo --
CREATE TABLE otppms_domaininfo (
  domainid  serial NOT NULL,  
  domainsn VARCHAR(64) DEFAULT ''::bpchar, 
  domainname VARCHAR(128) NOT NULL DEFAULT ''::bpchar,  
  descp VARCHAR(255) DEFAULT ''::bpchar, 
  createtime INTEGER NOT NULL DEFAULT 0,
  CONSTRAINT pk_domaininfo_domainid PRIMARY KEY  (domainid),
  CONSTRAINT uk_domaininfo_domainname unique (domainname)
)with (oids=false);
  
-- otppms_orgunitinfo --
CREATE TABLE otppms_orgunitinfo (
  orgunitid  serial NOT NULL,
  orgunitnumber VARCHAR(32) DEFAULT ''::bpchar, 
  orgunitname VARCHAR(128) NOT NULL DEFAULT ''::bpchar, 
  parentid INTEGER DEFAULT 0 ,
  domainid INTEGER NOT NULL DEFAULT 0,
  descp VARCHAR(255) DEFAULT ''::bpchar, 
  createtime INTEGER NOT NULL DEFAULT 0,
  CONSTRAINT  pk_orgunitinfo_orgunitid PRIMARY KEY  (orgunitid),
  CONSTRAINT  uk_orgunitinfo_orgunitname_parentid unique (orgunitname, parentid,domainid),
  CONSTRAINT  fk_orgunitinfo_domaininfo_domainid FOREIGN KEY(domainid) REFERENCES otppms_domaininfo (domainid) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE
)with (oids=false);
 
 
-- otppms_rad_profile --
CREATE TABLE  otppms_rad_profile (
  id serial NOT NULL,
  profilename VARCHAR(64) NOT NULL DEFAULT ''::bpchar, 
  descp VARCHAR(255) DEFAULT ''::bpchar, 
  CONSTRAINT  pk_rad_profile_id PRIMARY KEY  (id),
  CONSTRAINT  uk_rad_profile_profilename unique (profilename)
)with (oids=false);
 
-- otppms_userinfo --
CREATE TABLE  otppms_userinfo (
  userid VARCHAR(64) NOT NULL,   
  realname VARCHAR(64)  DEFAULT ''::bpchar, 
  localauth INTEGER NOT NULL DEFAULT 0 ,      
  pwd VARCHAR(64)   DEFAULT ''::bpchar,  
  pwddeath INTEGER  DEFAULT 0 ,
  getpwddeath INTEGER DEFAULT 0 ,
  initactpwd VARCHAR(64)   DEFAULT ''::bpchar,
  initactpwddeath INTEGER DEFAULT 0 ,
  paperstype INTEGER DEFAULT 0 ,
  papersnumber VARCHAR(64)  DEFAULT ''::bpchar,  
  email VARCHAR(255)  DEFAULT ''::bpchar,  
  address VARCHAR(128)  DEFAULT ''::bpchar,  
  tel VARCHAR(64)  DEFAULT ''::bpchar,  
  cellphone  VARCHAR(64)  DEFAULT ''::bpchar,  
  locked INTEGER DEFAULT 0,
  temploginerrcnt INTEGER DEFAULT 0,
  longloginerrcnt INTEGER DEFAULT 0,
  loginlocktime INTEGER DEFAULT 0,
  logincnt INTEGER DEFAULT 0,
  lastlogintime INTEGER DEFAULT 0,
  backendauth INTEGER DEFAULT 0,
  enabled INTEGER DEFAULT 1,
  domainid INTEGER NOT NULL,
  orgunitid INTEGER,
  radprofileid INTEGER,
  createtime INTEGER DEFAULT 0,
  CONSTRAINT pk_userinfo_userid PRIMARY KEY  (userid,domainid),
  CONSTRAINT fk_userinfo_domaininfo_domainid FOREIGN KEY (domainid) REFERENCES otppms_domaininfo(domainid) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_userinfo_orgunitinfo_orgunitid FOREIGN KEY (orgunitid) REFERENCES otppms_orgunitinfo(orgunitid)MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_userinfo_rad_profile_radprofileid FOREIGN KEY (radprofileid) REFERENCES otppms_rad_profile(id) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE
)with (oids=false);
CREATE INDEX idx_userinfo_locked on otppms_userinfo USING btree(locked);
CREATE INDEX idx_userinfo_backendauth on otppms_userinfo USING btree(backendauth);
CREATE INDEX idx_userinfo_enabled on otppms_userinfo USING btree(enabled);
CREATE INDEX idx_userinfo_localauth on otppms_userinfo USING btree(localauth);

-- otppms_tokenspec --
CREATE TABLE  otppms_tokenspec (
  specid VARCHAR(32) NOT NULL,
  tokentype INTEGER   DEFAULT 0,
  algid INTEGER   DEFAULT 0,
  signsuite VARCHAR(64)  DEFAULT ''::bpchar,
  cvssuite VARCHAR(64)  DEFAULT ''::bpchar,
  crsuite VARCHAR(64)  DEFAULT ''::bpchar,
  atid VARCHAR(16) DEFAULT ''::bpchar,
  otplen INTEGER   DEFAULT 0,
  intervaltime INTEGER   DEFAULT 0,
  begintime INTEGER   DEFAULT 0,
  maxauthcnt INTEGER   DEFAULT 0,
  initauthnum INTEGER   DEFAULT 0,
  haformat INTEGER   DEFAULT 0,
  halen INTEGER   DEFAULT 0,
  cardrow INTEGER   DEFAULT 0,
  cardcol INTEGER   DEFAULT 0,
  rowstart VARCHAR(2)  DEFAULT 'A',
  colstart VARCHAR(2)  DEFAULT '1',
  updatemode INTEGER DEFAULT 0,
  updatelimit INTEGER DEFAULT 0,
  updateresplen INTEGER DEFAULT 0,
  puk1mode INTEGER DEFAULT 0,
  puk1len INTEGER DEFAULT 0,
  puk1itv INTEGER DEFAULT 0,
  puk2mode INTEGER DEFAULT 0,
  puk2len INTEGER DEFAULT 0,
  puk2itv INTEGER DEFAULT 0,
  maxcounter INTEGER DEFAULT 0,
  syncmode INTEGER DEFAULT 0,
  descp VARCHAR(255),
 CONSTRAINT pk_tokenspec_specid PRIMARY KEY  (specid)
)with (oids=false);

-- otppms_tokeninfo --
CREATE TABLE  otppms_tokeninfo (
  token VARCHAR(32) NOT NULL,
  enabled INTEGER DEFAULT 0,
  locked INTEGER DEFAULT 0,
  lost INTEGER DEFAULT  0,
  logout INTEGER DEFAULT 0,
  pubkey VARCHAR(1024) NOT NULL DEFAULT ''::bpchar,
  authnum VARCHAR(32) DEFAULT '0',
  authmethod INTEGER DEFAULT 0,
  empin VARCHAR(32) DEFAULT ''::bpchar,
  empindeath INTEGER DEFAULT 0,
  expiretime  INTEGER DEFAULT 0,
  syncwnd INTEGER DEFAULT 0,
  temploginerrcnt INTEGER DEFAULT 0,
  longloginerrcnt INTEGER DEFAULT 0,
  loginlocktime INTEGER DEFAULT 0,
  driftcount INTEGER DEFAULT 0,
  physicaltype INTEGER DEFAULT 0,
  producttype INTEGER DEFAULT 0,
  specid VARCHAR(32) NOT NULL,
  domainid INTEGER NOT NULL,
  orgunitid INTEGER,
  gensmstime INTEGER DEFAULT 0,
  importtime INTEGER DEFAULT 0,
  distributetime INTEGER DEFAULT 0,
  crauthnum VARCHAR(32) DEFAULT '0',
  authtime INTEGER DEFAULT 0,
  crauthtime INTEGER DEFAULT 0,
  authotp VARCHAR(32) DEFAULT '',
  crauthotp VARCHAR(32) DEFAULT '',
  crauthdata VARCHAR(32) DEFAULT '',
  cractivecode VARCHAR(32) DEFAULT '',
  pubkeystate INTEGER DEFAULT 0,
  newpubkey VARCHAR(1024) DEFAULT 0,
  cractivetime INTEGER DEFAULT 0,
  cractivecount INTEGER DEFAULT 0,
  vendorid VARCHAR(32) NOT NULL DEFAULT ''::bpchar,
  CONSTRAINT pk_token PRIMARY KEY  (token),
  CONSTRAINT fk_tokeninfo_domaininfo_domainid FOREIGN KEY(domainid) REFERENCES otppms_domaininfo(domainid) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_tokeninfo_orgunitinfo_orgunitid FOREIGN KEY(orgunitid) REFERENCES otppms_orgunitinfo(orgunitid) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_tokeninfo_tokenspec_specid FOREIGN KEY(specid) REFERENCES otppms_tokenspec(specid) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE
)with (oids=false);
CREATE INDEX idx_token_enabled ON otppms_tokeninfo USING btree(enabled);
CREATE INDEX idx_token_locked ON otppms_tokeninfo USING btree(locked);
CREATE INDEX idx_token_lost ON otppms_tokeninfo USING btree(lost);
CREATE INDEX idx_token_logout ON otppms_tokeninfo USING btree(logout);
CREATE INDEX idx_token_authmethod ON otppms_tokeninfo USING btree(authmethod);
CREATE INDEX idx_token_physicaltype ON otppms_tokeninfo USING btree(physicaltype);
CREATE INDEX idx_token_producttype ON otppms_tokeninfo USING btree(producttype);

-- otppms_tokenext --
CREATE TABLE  otppms_tokenext (
  token VARCHAR(32) NOT NULL  ,
  pubkeyfactor VARCHAR(128)  DEFAULT ''::bpchar,
  phoneudid VARCHAR(32)  DEFAULT ''::bpchar,
  activepass VARCHAR(32)  DEFAULT ''::bpchar,
  apdeath INTEGER   DEFAULT 0 ,
  apretry INTEGER   DEFAULT 0,
  actived INTEGER   DEFAULT 0 ,
  activetime INTEGER  DEFAULT 0 ,
  provtype INTEGER  DEFAULT 0 ,
  exttype VARCHAR(16)  DEFAULT ''::bpchar,
  CONSTRAINT pk_tokenext_token PRIMARY KEY  (token),
  CONSTRAINT fk_tokenext_token FOREIGN KEY (token) REFERENCES otppms_tokeninfo (token) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE
)with (oids=false);
  CREATE INDEX idx_tokenext_actived ON otppms_tokenext USING btree(actived);
  CREATE INDEX idx_tokenext_provtype ON otppms_tokenext USING btree(provtype);
  

-- otppms_user_token --
CREATE TABLE  otppms_user_token (
  bindid serial NOT NULL,
  userid VARCHAR(64) NOT NULL,
  token VARCHAR(32) NOT NULL,
  domainid INTEGER,
  bindtime INTEGER NOT NULL DEFAULT 0,
  CONSTRAINT pk_user_token_bindid PRIMARY KEY  (bindid),
  CONSTRAINT uk_user_token unique (userid,token,domainid),
  CONSTRAINT fk_user_token_tokeninfo_token FOREIGN KEY (token) REFERENCES otppms_tokeninfo (token) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_user_token_domaininfo_domainid FOREIGN KEY (domainid) REFERENCES otppms_domaininfo (domainid) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE
)with (oids=false);

-- otppms_hostinfo --
CREATE TABLE otppms_hostinfo (
  hostipaddr VARCHAR(64) NOT NULL,
  hostname VARCHAR(64),
  confid INTEGER DEFAULT 0,
  priority INTEGER DEFAULT 0,
  licid VARCHAR(32) DEFAULT ''::bpchar,
  descp VARCHAR(255)DEFAULT ''::bpchar,
  ftradiusenabled INTEGER DEFAULT 1,
  protocoltype VARCHAR(32) NOT NULL DEFAULT 'udp',
  authport INTEGER NOT NULL DEFAULT 1915,
  syncport INTEGER NOT NULL DEFAULT 1916,
  radiusenabled INTEGER NOT NULL DEFAULT 1,
  radauthport INTEGER NOT NULL DEFAULT 1812,
  httpenabled INTEGER DEFAULT 0,
  httpport INTEGER DEFAULT 18080,
  httpsenabled INTEGER DEFAULT 0,
  httpsport INTEGER DEFAULT 18443,
  keystorepwd VARCHAR(64) DEFAULT ''::bpchar,
  certificatepwd VARCHAR(64) DEFAULT ''::bpchar,
  keystoreinstance VARCHAR(32) DEFAULT ''::bpchar,
  keystorerootpath VARCHAR(4000) DEFAULT ''::bpchar,
  soapenabled INTEGER NOT NULL DEFAULT 1,
  soapport INTEGER NOT NULL DEFAULT 18081,
  webservicename VARCHAR(32) NOT NULL DEFAULT 'otpwebservice',
  CONSTRAINT pk_hostinfo_hostipaddr PRIMARY KEY  (hostipaddr)
)with (oids=false);

-- otppms_agentconf --
CREATE TABLE otppms_agentconf (
  id serial NOT NULL,
  confname VARCHAR(64) NOT NULL,
  type INTEGER NOT NULL DEFAULT 0,
  userformat INTEGER DEFAULT 0,
  localprotect INTEGER DEFAULT 0,
  remoteprotect INTEGER DEFAULT 0,
  uacprotect INTEGER DEFAULT 0,
  unboundlogin INTEGER DEFAULT 0,
  descp VARCHAR(255) DEFAULT ''::bpchar,
  CONSTRAINT pk_agentconf_id PRIMARY KEY  (id),
  CONSTRAINT uk_agentconf_confname unique (confname)
)with (oids=false);
 
-- otppms_agentinfo -- 
CREATE TABLE otppms_agentinfo  (
  agentipaddr VARCHAR(64) NOT NULL ,
  agentname VARCHAR(64) DEFAULT ''::bpchar,
  pubkey VARCHAR(64) NOT NULL DEFAULT ''::bpchar,
  enabled INTEGER NOT NULL default 1,
  descp VARCHAR(255) DEFAULT ''::bpchar,
  agenttype INTEGER NOT NULL default 0,
  agentconfid INTEGER DEFAULT 0,
  graceperiod INTEGER DEFAULT 0,
  CONSTRAINT pk_agentinfo_agentipaddr PRIMARY KEY  (agentipaddr)
)with (oids=false);
 
-- otppms_agent_host --
CREATE TABLE   otppms_agent_host (
  agentipaddr VARCHAR(64) NOT NULL  ,
  hostipaddr VARCHAR(64) NOT NULL ,
  CONSTRAINT pk_agent_host PRIMARY KEY  (agentipaddr,hostipaddr),
  CONSTRAINT fk_agent_host_agentinfo_agentipaddr FOREIGN KEY (agentipaddr) REFERENCES otppms_agentinfo (agentipaddr) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_agent_host_hostinfo_hostipaddr FOREIGN KEY (hostipaddr) REFERENCES otppms_hostinfo (hostipaddr) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE
)with (oids=false);
 
-- otppms_licinfo -- 
CREATE TABLE  otppms_licinfo(
	licid VARCHAR(32) NOT NULL,
	licinfo VARCHAR(2048) NOT NULL,
	lictype INTEGER  NOT NULL,
	issuer VARCHAR(64) NOT NULL,
	customerid VARCHAR(64) NOT NULL,
	licstate INTEGER NOT NULL DEFAULT 1,
	licupdatetime INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT pk_licinfo_licid PRIMARY KEY (licid)
)with (oids=false);

-- otppms_loginfo --
CREATE TABLE otppms_loginfo (
  id serial NOT NULL,
  userid VARCHAR(64) DEFAULT ''::bpchar,
  token VARCHAR(32) DEFAULT ''::bpchar,
  serverip VARCHAR(64) DEFAULT ''::bpchar,
  clientip VARCHAR(64) DEFAULT ''::bpchar,
  actionid INTEGER NOT NULL,
  actionresult INTEGER NOT NULL,
  logtime INTEGER NOT NULL,
  logcontent VARCHAR(255) DEFAULT ''::bpchar,
  hashcode VARCHAR(255) DEFAULT 0,
  domainid INTEGER DEFAULT 0,
  domainname VARCHAR(64) DEFAULT ''::bpchar,
  orgunitid INTEGER DEFAULT 0,
  orgunitname VARCHAR(64) DEFAULT ''::bpchar,
  moduletype INTEGER NOT NULL DEFAULT 0,
  vendorid VARCHAR(32) DEFAULT ''::bpchar,
  CONSTRAINT pk_loginfo_id PRIMARY KEY (id)
)with (oids=false);

-- otppms_admininfo --
CREATE TABLE  otppms_admininfo (
	  adminid VARCHAR(64) NOT NULL,
	  realname VARCHAR(64) DEFAULT ''::bpchar,
	  localauth INTEGER NOT NULL DEFAULT 0 ,
	  pwd VARCHAR(64) NOT NULL,
	  pwdsettime INTEGER NOT NULL DEFAULT 0,
	  getpwddeath INTEGER DEFAULT 0,
	  locked INTEGER DEFAULT 0,
	  temploginerrcnt INTEGER DEFAULT 0,
	  longloginerrcnt INTEGER DEFAULT 0,
	  loginlocktime INTEGER DEFAULT 0,
	  logintime INTEGER DEFAULT 0,
	  logincnt INTEGER DEFAULT 0,
	  createuser VARCHAR (64) NOT NULL,
	  enabled INTEGER DEFAULT 1,
	  email VARCHAR(255) DEFAULT ''::bpchar,
	  address VARCHAR(128) DEFAULT ''::bpchar,
	  tel VARCHAR(64) DEFAULT ''::bpchar,
	  cellphone VARCHAR(64) DEFAULT ''::bpchar,
	  createtime INTEGER NOT NULL,
	  descp VARCHAR(255) DEFAULT ''::bpchar,
    CONSTRAINT pk_admininfo_adminid PRIMARY KEY (adminid)
)with (oids=false); 
 
-- otppms_admin_orgunit --
CREATE TABLE  otppms_admin_orgunit (
  adminid VARCHAR(64) NOT NULL,
  domainid INTEGER  NOT NULL,
  orgunitid INTEGER,
  CONSTRAINT fk_admin_orgunit_admininfo_adminid FOREIGN KEY (adminid) REFERENCES otppms_admininfo(adminid) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_admin_orgunit_domaininfo_domainid FOREIGN KEY (domainid) REFERENCES otppms_domaininfo(domainid) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_admin_orgunit_orgunitinfo_orgunitid FOREIGN KEY (orgunitid) REFERENCES  otppms_orgunitinfo(orgunitid) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE  
)with (oids=false); 

 
-- otppms_perminfo --
CREATE TABLE  otppms_perminfo (
	permcode VARCHAR(64) NOT NULL,
	permlink VARCHAR(255) DEFAULT ''::bpchar,
	srcname VARCHAR(255)  DEFAULT ''::bpchar,
	keymark VARCHAR(64)  DEFAULT ''::bpchar,
    descp VARCHAR(255)  DEFAULT ''::bpchar,
    CONSTRAINT pk_perminfo_permcode PRIMARY KEY (permcode)
)with (oids=false);

-- otppms_roleinfo --
CREATE TABLE  otppms_roleinfo (
    roleid serial NOT NULL,
    rolename VARCHAR(64) NOT NULL DEFAULT ''::bpchar,
    rolemark VARCHAR(64)  DEFAULT ''::bpchar,
    createuser  VARCHAR (64)  NOT NULL  DEFAULT ''::bpchar,
    createtime INTEGER  NOT NULL  DEFAULT 0 ,
    modifytime INTEGER NOT NULL  DEFAULT 0 ,
    descp VARCHAR(255)  DEFAULT ''::bpchar,
    CONSTRAINT pk_role_roleid PRIMARY KEY (roleid)
)with (oids=false);

-- otppms_role_perm --
CREATE TABLE  otppms_role_perm (
	roleid INTEGER  NOT NULL,
	permcode VARCHAR(64) NOT NULL,
	CONSTRAINT pk_role_perm PRIMARY KEY (roleid, permcode),
	CONSTRAINT fk_role_perm_roleinfo_roleid FOREIGN KEY (roleid) REFERENCES  otppms_roleinfo(roleid) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_role_perm_perminfo_permcode FOREIGN KEY (permcode) REFERENCES  otppms_perminfo (permcode) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE
)with (oids=false);


-- otppms_admin_role --
CREATE TABLE  otppms_admin_role (
    adminid VARCHAR(64) NOT NULL,
    roleid INTEGER  NOT NULL,
    CONSTRAINT pk_admin_role PRIMARY KEY (adminid,roleid),
    CONSTRAINT fk_admin_role_admininfo_adminid FOREIGN KEY (adminid) REFERENCES otppms_admininfo(adminid) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_admin_role_roleinfo_roleid FOREIGN KEY (roleid) REFERENCES otppms_roleinfo(roleid) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE
)with (oids=false);

-- otppms_admin_log --	
CREATE TABLE   otppms_admin_log (
  id serial NOT NULL,
  operator VARCHAR(64) default '',
  logtime INTEGER  NOT NULL,
  actionid INTEGER  NOT NULL,
  actionresult INTEGER  NOT NULL,
  actionobject INTEGER  NOT NULL,
  clientip   VARCHAR(64)  DEFAULT ''::bpchar,
  descp VARCHAR(4000)  DEFAULT ''::bpchar,
  hashcode VARCHAR(255)  DEFAULT ''::bpchar,
  CONSTRAINT pk_admin_log_id PRIMARY KEY  (id)
)with (oids=false);

-- otppms_taskinfo --	
CREATE TABLE otppms_taskinfo (
  taskname VARCHAR(128) NOT NULL DEFAULT ''::bpchar,
  sourceid INTEGER NOT NULL,
  sourcetype INTEGER NOT NULL DEFAULT 0,
  taskmode1 INTEGER NOT NULL,
  taskmode2 INTEGER NOT NULL,
  taskminute VARCHAR(64) DEFAULT ''::bpchar,
  taskhour VARCHAR(64) DEFAULT ''::bpchar,
  taskday VARCHAR(64) DEFAULT ''::bpchar,
  taskmonth VARCHAR(64) DEFAULT ''::bpchar,
  taskweek VARCHAR(64) DEFAULT ''::bpchar,
  enabled INTEGER DEFAULT 1,
  taskid VARCHAR(128) NOT NULL DEFAULT ''::bpchar,
  descp VARCHAR(255) DEFAULT ''::bpchar,
  CONSTRAINT pk_taskinfo_taskname PRIMARY KEY  (taskname)
)with (oids=false);


-- otppms_configinfo --
CREATE TABLE  otppms_configinfo (
    id  serial NOT NULL,
    confname VARCHAR(64) NOT NULL ,
    conftype VARCHAR(64) NOT NULL,
    confvalue VARCHAR(128) NOT NULL ,
    parentid INTEGER    DEFAULT -1,
    descp VARCHAR(255)  DEFAULT ''::bpchar,
    CONSTRAINT pk_configinfo_id PRIMARY KEY (id),
    CONSTRAINT uk_configinfo_confname_conftype_confvalue unique (confname,conftype,confvalue)
)with (oids=false); 
 
-- otppms_usersource --
CREATE TABLE  otppms_usersource (
  id serial NOT NULL  ,
  sourcename VARCHAR(64)  NOT NULL DEFAULT ''::bpchar,
  sourcetype INTEGER  NOT NULL DEFAULT 0,
  dbtype INTEGER default 0,
  serveraddr VARCHAR(64) DEFAULT ''::bpchar,
  port INTEGER default 0,
  username VARCHAR(64)  NOT NULL DEFAULT ''::bpchar,
  pwd VARCHAR(64) DEFAULT ''::bpchar,
  dbname VARCHAR(64) DEFAULT ''::bpchar,
  dbtablename VARCHAR(64) DEFAULT ''::bpchar,
  filter VARCHAR(255) DEFAULT ''::bpchar,
  basedn VARCHAR(255) DEFAULT ''::bpchar,
  rootdn VARCHAR(255) DEFAULT ''::bpchar,
  dominodriver VARCHAR(64) DEFAULT ''::bpchar,
  namesfile VARCHAR(64) DEFAULT ''::bpchar,
  timeout INTEGER DEFAULT 30,
  isupdateou INTEGER DEFAULT 0,
  upinvaliduser INTEGER DEFAULT 0,
  domainid INTEGER  NOT NULL,
  orgunitid INTEGER,
  localusermark INTEGER DEFAULT 0,
  issyncuserinfo INTEGER DEFAULT 0,
  descp VARCHAR(255) DEFAULT ''::bpchar,
  CONSTRAINT pk_usersource_id PRIMARY KEY (id),
  CONSTRAINT uk_usersource_sourcename UNIQUE (sourcename)
)with (oids=false); 
 

-- otppms_usersource_attr --
CREATE TABLE  otppms_usersource_attr (
  sourceid INTEGER  NOT NULL,
  localuserattr VARCHAR(64)  DEFAULT ''::bpchar,
  sourceuserattr VARCHAR(64) DEFAULT ''::bpchar,
  CONSTRAINT pk_usersource_attr  PRIMARY KEY (sourceid, localuserattr, sourceuserattr),
  CONSTRAINT fk_usersource_attr_sourceid FOREIGN KEY (sourceid) REFERENCES otppms_usersource(id) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE
)with (oids=false); 
 

-- otppms_emailinfo --
CREATE TABLE  otppms_emailinfo (
  id serial NOT NULL  ,
  servname VARCHAR(128)NOT NULL DEFAULT ''::bpchar,
  account VARCHAR(64) NOT NULL DEFAULT ''::bpchar,
  pwd VARCHAR(64) DEFAULT ''::bpchar,
  sender VARCHAR(64) NOT NULL DEFAULT ''::bpchar,
  host VARCHAR(64) NOT NULL DEFAULT ''::bpchar,
  port INTEGER NOT NULL DEFAULT 25,
  validated INTEGER DEFAULT 0,
  isdefault INTEGER DEFAULT 0,
  descp VARCHAR(255) DEFAULT ''::bpchar,
  CONSTRAINT pk_emailinfo_id  PRIMARY KEY (id),
  CONSTRAINT uk_emailinfo_servname UNIQUE (servname)
)with (oids=false); 
 

-- otppms_smsinfo --
CREATE TABLE  otppms_smsinfo (
  id serial NOT NULL  ,
  smsname VARCHAR(64) NOT NULL DEFAULT ''::bpchar,
  sendtype INTEGER DEFAULT 0,
  host VARCHAR(128) NOT NULL DEFAULT ''::bpchar,
  username VARCHAR(64) NOT NULL DEFAULT ''::bpchar,
  pwd VARCHAR(64) NOT NULL DEFAULT ''::bpchar,
  accountattr VARCHAR(32) NOT NULL DEFAULT ''::bpchar,
  passwdattr VARCHAR(32) NOT NULL DEFAULT ''::bpchar,
  phoneattr VARCHAR(32) NOT NULL DEFAULT ''::bpchar,
  messageattr VARCHAR(32) DEFAULT ''::bpchar,
  paramannex VARCHAR(128) DEFAULT ''::bpchar,
  sendresult VARCHAR(255) DEFAULT ''::bpchar,
  enabled INTEGER DEFAULT 0,
  priority INTEGER DEFAULT 0,
  retrysend INTEGER DEFAULT 0,
  descp VARCHAR(255) DEFAULT ''::bpchar,
  CONSTRAINT pk_smsinfo_id  PRIMARY KEY (id),
  CONSTRAINT uk_smsinfo_smsname UNIQUE (smsname)
)with (oids=false); 
 

-- otppms_attrinfo --
CREATE TABLE  otppms_attrinfo (
  attrtype INTEGER NOT NULL DEFAULT 0,
  attrid VARCHAR(32) NOT NULL DEFAULT ''::bpchar,
  attrname VARCHAR(64) NOT NULL DEFAULT ''::bpchar,
  attrvalue VARCHAR(128) NOT NULL  DEFAULT ''::bpchar,
  valuetype VARCHAR(32) NOT NULL DEFAULT ''::bpchar,
  profileid INTEGER NOT NULL,
  vendorid INTEGER NOT NULL DEFAULT -1,
  CONSTRAINT pk_attrinfo_attrid_profileid  PRIMARY KEY (attrid, profileid,vendorid),
  CONSTRAINT fk_attrinfo_rad_profile_profileid FOREIGN KEY (profileid) REFERENCES otppms_rad_profile(id) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE
)with (oids=false); 
 

-- otppms_trustipinfo --
CREATE TABLE  otppms_trustipinfo (
  id serial NOT NULL  ,
  systype INTEGER NOT NULL DEFAULT 0,
  clientapptype INTEGER NOT NULL default 0,
  startip VARCHAR(64) NOT NULL DEFAULT ''::bpchar,
  endip VARCHAR(64) DEFAULT ''::bpchar,
  expiretime INTEGER DEFAULT 0,
  updatetime INTEGER DEFAULT 0,
  enabled INTEGER DEFAULT 0,
  clientappport INTEGER DEFAULT 0,
  clientservpath VARCHAR(64) DEFAULT ''::bpchar,
  CONSTRAINT pk_trustipinfo_id  PRIMARY KEY (id)
)with (oids=false); 

-- otppms_portal_notice --
CREATE TABLE  otppms_portal_notice (
  id serial NOT NULL  ,
  systype INTEGER NOT NULL DEFAULT 0,
  createuser VARCHAR(64) DEFAULT ''::bpchar,
  createtime INTEGER DEFAULT 0,
  expiretime INTEGER DEFAULT 0,
  content text  DEFAULT ''::bpchar,
  title VARCHAR(128) NOT NULL,
  CONSTRAINT pk_portal_notice_id  PRIMARY KEY (id)
)with (oids=false); 
 

-- otppms_backend --
CREATE TABLE  otppms_backend (
  id serial NOT NULL  ,
  backendtype INTEGER NOT NULL DEFAULT 0,
  backendname VARCHAR(64) NOT NULL,
  host VARCHAR(64) NOT NULL DEFAULT ''::bpchar,
  sparehost VARCHAR(64) DEFAULT ''::bpchar,
  port INTEGER NOT NULL DEFAULT 389,
  priority INTEGER DEFAULT 0,
  basedn VARCHAR(255) DEFAULT ''::bpchar,
  filter VARCHAR(255) DEFAULT '(&(objectCategory=person)(objectClass=user))',
  pubkey VARCHAR(255) DEFAULT ''::bpchar, 
  timeout INTEGER DEFAULT 30,
  retrycnt INTEGER DEFAULT 0,
  usernamerule INTEGER DEFAULT 0,
  delimiter VARCHAR(32) DEFAULT '@',
  enabled INTEGER DEFAULT 0,
  policy INTEGER DEFAULT 0,
  domainid INTEGER DEFAULT 0,
  CONSTRAINT pk_backend_id  PRIMARY KEY (id),
  CONSTRAINT uk_backend_backendname UNIQUE (backendname),
  CONSTRAINT uk_backend_host_port_domainid UNIQUE (host,port,domainid)
)with (oids=false); 
 
-- otppms_monitor_admin --
CREATE TABLE   otppms_monitor_admin (
  adminid VARCHAR(64) NOT NULL,
  conftype VARCHAR(64)NOT NULL,
  CONSTRAINT pk_monitor_admin  PRIMARY KEY (adminid, conftype),
  CONSTRAINT fk_monitor_admin_admininfo_adminid FOREIGN KEY (adminid) REFERENCES otppms_admininfo(adminid) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE
)with (oids=false); 
 

-- otppms_monitorinfo --
CREATE TABLE   otppms_monitorinfo (
  id serial NOT NULL  ,
  email VARCHAR(1024),
  mobile VARCHAR(1024),
  confid INTEGER DEFAULT 0,
  conftype VARCHAR(64) NOT NULL,
  title VARCHAR(128) DEFAULT ''::bpchar,
  content text DEFAULT ''::bpchar,
  sendtime INTEGER DEFAULT 0,
  issend INTEGER DEFAULT 0,
  CONSTRAINT pk_monitorinfo_id  PRIMARY KEY (id)
)with (oids=false); 

-- otppms_admin_perm --
create table otppms_admin_perm (
	adminid VARCHAR(64) NOT NULL,
	permcode VARCHAR(64) NOT NULL,
	CONSTRAINT pk_admin_perm PRIMARY KEY(adminid,permcode),
	CONSTRAINT fk_admin_perm_admininfo_admid FOREIGN KEY (adminid) REFERENCES otppms_admininfo (adminid) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT fk_admin_perm_perminfo_percode FOREIGN KEY (permcode) REFERENCES otppms_perminfo (permcode) MATCH SIMPLE ON DELETE CASCADE ON UPDATE CASCADE
)with (oids=false);

