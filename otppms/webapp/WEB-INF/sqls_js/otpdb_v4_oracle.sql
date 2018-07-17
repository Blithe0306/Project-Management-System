 /*
  索引：idx_表名（或表名缩写）_字段名（或字段名缩写）
  外键：fk_本表名（或表名缩写）_关联表名（或表名缩写）_字段名（或字段名缩写）
  唯一索引：uk_表名（或表名缩写）_唯一健名（可以是字段名缩写）
*/

-- otppms_domaininfo --
CREATE TABLE otppms_domaininfo (
  domainid NUMBER(10) NOT NULL ,
  domainsn VARCHAR2(64) DEFAULT '',
  domainname VARCHAR2(128) DEFAULT '' NOT NULL ,  
  descp VARCHAR2(255) DEFAULT '',
  createtime NUMBER(10)  DEFAULT 0 NOT NULL
) ;
ALTER  TABLE otppms_domaininfo ADD CONSTRAINT  pk_domaininfo_domainid  PRIMARY KEY(domainid) USING INDEX;
ALTER  TABLE otppms_domaininfo ADD CONSTRAINT  uk_domaininfo_domainname UNIQUE(domainname);

CREATE  sequence ftp_domaininfo_id_seq minvalue 1 START WITH 1 increment BY 1 cache 20;
CREATE OR REPLACE TRIGGER trg_ftp_domaininfo_add BEFORE INSERT ON otppms_domaininfo FOR EACH ROW BEGIN  SELECT ftp_domaininfo_id_seq.nextval INTO :NEW.domainid FROM dual; END trg_ftp_domaininfo_add;;

commit;


-- otppms_orgunitinfo --
CREATE TABLE otppms_orgunitinfo (
  orgunitid NUMBER(10) NOT NULL ,
  orgunitnumber VARCHAR2(32) DEFAULT '',
  orgunitname VARCHAR2(128)  DEFAULT '' NOT NULL,
  parentid NUMBER(10) DEFAULT 0 ,
  domainid NUMBER(10)  DEFAULT 0 NOT NULL,
  descp VARCHAR2(255) DEFAULT '',
  createtime NUMBER(10) DEFAULT 0  NOT NULL
) ;
ALTER  TABLE otppms_orgunitinfo ADD CONSTRAINT   pk_orgunitinfo_orgunitid  PRIMARY KEY(orgunitid) USING INDEX;
ALTER  TABLE otppms_orgunitinfo ADD CONSTRAINT   uk_orginfo_orgname_parentid UNIQUE(orgunitname, parentid,domainid);
ALTER  TABLE  otppms_orgunitinfo ADD CONSTRAINT  fk_orginfo_dominfo_domainid FOREIGN KEY (domainid) REFERENCES otppms_domaininfo (domainid) ON DELETE Cascade;
CREATE  sequence ftp_orgunitinfo_orgunitid_seq minvalue 1 START WITH 1 increment BY 1 cache 20;
CREATE OR REPLACE TRIGGER trg_ftp_orgunitinfo_add BEFORE INSERT ON otppms_orgunitinfo FOR EACH ROW BEGIN  SELECT ftp_orgunitinfo_orgunitid_seq.nextval INTO :NEW.orgunitid FROM dual; END trg_ftp_orgunitinfo_add;;

commit;


-- otppms_rad_profile --
CREATE TABLE otppms_rad_profile (
  id NUMBER(10) NOT NULL ,
  profilename VARCHAR2(64)  DEFAULT '' NOT NULL,
  descp VARCHAR2(255) DEFAULT ''
) ;
ALTER  TABLE otppms_rad_profile ADD CONSTRAINT  pk_rad_profile_id  PRIMARY KEY(id) USING INDEX;
ALTER  TABLE otppms_rad_profile ADD CONSTRAINT  uk_rad_profile_profilename UNIQUE(profilename);
CREATE  sequence ftp_rad_profile_id_seq minvalue 1 START WITH 1 increment BY 1 cache 20;
CREATE OR REPLACE TRIGGER trg_ftp_rad_profile_add BEFORE INSERT ON otppms_rad_profile FOR EACH ROW BEGIN  SELECT ftp_rad_profile_id_seq.nextval INTO :NEW.id FROM dual; END trg_ftp_rad_profile_add;;

commit;


-- otppms_userinfo -- 
CREATE TABLE  otppms_userinfo (
  userid VARCHAR2(64)  DEFAULT '' NOT NULL,   
  realname VARCHAR2(64)  DEFAULT '', 
  localauth NUMBER(10)  DEFAULT 0  NOT NULL,      
  pwd VARCHAR2(64)    DEFAULT ''  ,  
  pwddeath NUMBER(10)  DEFAULT 0 ,
  getpwddeath NUMBER(10)  DEFAULT 0  , 
  initactpwd VARCHAR2(64)  DEFAULT '',
  initactpwddeath NUMBER(10)  DEFAULT 0 , 
  paperstype NUMBER(10)  DEFAULT 0 , 
  papersnumber VARCHAR2(64)  DEFAULT '',
  email VARCHAR2(255)  DEFAULT '',    
  address VARCHAR2(128)  DEFAULT '',   
  tel VARCHAR2(64)  DEFAULT '',   
  cellphone VARCHAR2(64)  DEFAULT '', 
  locked NUMBER(10)  DEFAULT 0 ,    
  temploginerrcnt NUMBER(10) DEFAULT 0,
  longloginerrcnt NUMBER(10) DEFAULT 0,
  loginlocktime NUMBER(10) DEFAULT 0,
  logincnt NUMBER(10) DEFAULT 0,
  lastlogintime NUMBER(10) DEFAULT 0,
  backendauth NUMBER(10) DEFAULT 0,
  enabled NUMBER(10) DEFAULT 1,
  domainid NUMBER(10)  NOT NULL,
  orgunitid NUMBER(10),
  radprofileid NUMBER(10),
  createtime NUMBER(10) DEFAULT 0
) ;
ALTER  TABLE otppms_userinfo ADD CONSTRAINT  pk_userinfo_userid PRIMARY KEY(userid,domainid) USING INDEX;
CREATE INDEX idx_userinfo_locked ON otppms_userinfo(locked);
CREATE INDEX idx_userinfo_backendauth ON otppms_userinfo(backendauth);
CREATE INDEX idx_userinfo_enabled ON otppms_userinfo(enabled);
CREATE INDEX idx_userinfo_localauth ON otppms_userinfo(localauth);
ALTER  TABLE  otppms_userinfo ADD CONSTRAINT  fk_user_domaininfo_domainid FOREIGN KEY (domainid) REFERENCES otppms_domaininfo (domainid) ON DELETE Cascade;
ALTER  TABLE  otppms_userinfo ADD CONSTRAINT  fk_user_orgunitinfo_orgunitid FOREIGN KEY (orgunitid) REFERENCES otppms_orgunitinfo (orgunitid) ON DELETE Cascade;
ALTER  TABLE  otppms_userinfo ADD CONSTRAINT  fk_user_rp_radprofileid FOREIGN KEY (radprofileid) REFERENCES otppms_rad_profile (id) ON DELETE Cascade;

-- otppms_tokenspec --
CREATE TABLE otppms_tokenspec (
  specid VARCHAR2(32) NOT NULL,
  tokentype NUMBER(10) DEFAULT 0,
  algid NUMBER(10) DEFAULT 0,
  signsuite VARCHAR2(64) DEFAULT '',
  cvssuite VARCHAR2(64) DEFAULT '',
  crsuite  VARCHAR2(64) DEFAULT '',
  atid     VARCHAR2(16),     
  otplen NUMBER(10) DEFAULT 0,
  intervaltime NUMBER(10) DEFAULT 0,
  begintime NUMBER(10) DEFAULT 0,
  maxauthcnt NUMBER(10) DEFAULT 0,
  initauthnum NUMBER(10) DEFAULT 0,
  haformat NUMBER(10) DEFAULT 0,
  halen NUMBER(10) DEFAULT 0,
  cardrow NUMBER(10) DEFAULT 0,
  cardcol NUMBER(10) DEFAULT 0,
  rowstart VARCHAR2(2) DEFAULT 'a',
  colstart VARCHAR2(2) DEFAULT '1',
  updatemode NUMBER(10) DEFAULT 0,
  updatelimit NUMBER(10) DEFAULT 0,
  updateresplen NUMBER(10) DEFAULT 0,
  puk1mode NUMBER(10) DEFAULT 0,
  puk1len NUMBER(10) DEFAULT 0,
  puk1itv NUMBER(10) DEFAULT 0,
  puk2mode NUMBER(10) DEFAULT 0,
  puk2len NUMBER(10) DEFAULT 0,
  puk2itv NUMBER(10) DEFAULT 0,
  maxcounter NUMBER(10) DEFAULT 0,
  syncmode NUMBER(10) DEFAULT 0,
  descp VARCHAR2(255)
);
ALTER TABLE otppms_tokenspec ADD CONSTRAINT  pk_tokenspec_specid PRIMARY KEY(specid) USING INDEX;

-- otppms_tokeninfo --
CREATE TABLE  otppms_tokeninfo (
  token VARCHAR2(32) NOT NULL , 
  enabled NUMBER(10)   DEFAULT  0 ,   
  locked NUMBER(10)   DEFAULT  0 , 
  lost NUMBER(10)   DEFAULT  0 , 
  logout NUMBER(10)   DEFAULT  0 , 
  pubkey VARCHAR2(1024)   DEFAULT '' NOT NULL ,
  authnum VARCHAR2(32)  DEFAULT '0', 
  authmethod NUMBER(10)     DEFAULT 0, 
  empin VARCHAR2(32)   DEFAULT '',  
  empindeath NUMBER(10)   DEFAULT  0 , 
  expiretime NUMBER(10)   DEFAULT  0 ,
  syncwnd NUMBER(10)   DEFAULT 0,  
  temploginerrcnt NUMBER(10)   DEFAULT 0, 
  longloginerrcnt NUMBER(10)   DEFAULT 0, 
  loginlocktime NUMBER(10)   DEFAULT 0, 
  driftcount NUMBER(10)   DEFAULT 0, 
  physicaltype NUMBER(10)   DEFAULT 0,
  producttype NUMBER(10)   DEFAULT 0,
  specid VARCHAR2(32) NOT NULL,
  domainid NUMBER(10)   NOT NULL,
  orgunitid NUMBER(10),
  gensmstime NUMBER(10) DEFAULT 0,
  importtime NUMBER(10) DEFAULT 0,
  distributetime NUMBER(10) DEFAULT 0,
  crauthnum VARCHAR2(32) DEFAULT '0',
  authtime NUMBER(10) DEFAULT 0,
  crauthtime NUMBER(10) DEFAULT 0,
  authotp VARCHAR2(32) DEFAULT '',
  crauthotp VARCHAR2(32) DEFAULT '',
  crauthdata VARCHAR2(32) DEFAULT '',
  cractivecode VARCHAR2(32) DEFAULT '',
  pubkeystate NUMBER(10) DEFAULT 0,
  newpubkey VARCHAR2(1024) DEFAULT 0,
  cractivetime NUMBER(10) DEFAULT 0,
  cractivecount NUMBER(10) DEFAULT 0,
  vendorid VARCHAR2(32) DEFAULT '' NOT NULL 
) ;
ALTER TABLE otppms_tokeninfo ADD CONSTRAINT  pk_tokeninfo_token PRIMARY KEY(token) USING INDEX;
CREATE INDEX idx_tokeninfo_enabled ON otppms_tokeninfo (enabled);
CREATE INDEX idx_tokeninfo_locked ON otppms_tokeninfo (locked);
CREATE INDEX idx_tokeninfo_lost ON otppms_tokeninfo (lost);
CREATE INDEX idx_tokeninfo_logout ON otppms_tokeninfo (logout);
CREATE INDEX idx_tokeninfo_authmethod ON otppms_tokeninfo (authmethod);
CREATE INDEX idx_tokeninfo_physicaltype ON otppms_tokeninfo (physicaltype);
CREATE INDEX idx_tokeninfo_producttype ON otppms_tokeninfo (producttype);
ALTER  TABLE  otppms_tokeninfo ADD CONSTRAINT  fk_token_domaininfo_domainid FOREIGN KEY (domainid) REFERENCES otppms_domaininfo (domainid) ON DELETE Cascade;
ALTER  TABLE  otppms_tokeninfo ADD CONSTRAINT  fk_token_orgunitinfo_orgunitid FOREIGN KEY (orgunitid) REFERENCES otppms_orgunitinfo (orgunitid) ON DELETE Cascade;
ALTER  TABLE  otppms_tokeninfo ADD CONSTRAINT  fk_token_tokenspec_specid FOREIGN KEY (specid) REFERENCES otppms_tokenspec (specid) ON DELETE Cascade;

-- otppms_tokenext --
CREATE TABLE  otppms_tokenext (
  token VARCHAR2(32) NOT NULL  ,
  pubkeyfactor VARCHAR2(128)  DEFAULT '',
  phoneudid VARCHAR2(32)  DEFAULT '',
  activepass VARCHAR2(32)  DEFAULT '',
  apdeath NUMBER(10)   DEFAULT 0 ,
  apretry NUMBER(10)   DEFAULT 0,
  actived NUMBER(10)   DEFAULT 0 ,
  activetime NUMBER(10)  DEFAULT 0 ,
  provtype NUMBER(10)  DEFAULT 0 ,
  exttype VARCHAR2(16)  DEFAULT ''
) ;
ALTER TABLE otppms_tokenext ADD CONSTRAINT  pk_tokenext_token  PRIMARY KEY(token) USING INDEX;
ALTER TABLE otppms_tokenext ADD CONSTRAINT  fk_tokenext_tokeninfo_token FOREIGN KEY (token) REFERENCES otppms_tokeninfo (token) ON DELETE Cascade ;
CREATE INDEX idx_tokenext_actived ON otppms_tokenext(actived);
CREATE INDEX idx_tokenext_provtype ON otppms_tokenext(provtype);
 
-- otppms_user_token --
CREATE TABLE  otppms_user_token (
  bindid  NUMBER(10) NOT NULL,
  userid VARCHAR2(64) NOT NULL,
  token VARCHAR2(32) NOT NULL,
  domainid NUMBER(10),
  bindtime NUMBER(10) DEFAULT 0 NOT NULL 
) ;
ALTER TABLE  otppms_user_token ADD  CONSTRAINT pk_user_token_bindid PRIMARY KEY(bindid) USING INDEX;
ALTER TABLE  otppms_user_token ADD  CONSTRAINT uk_user_token UNIQUE (userid,token);
ALTER TABLE  otppms_user_token ADD  CONSTRAINT fk_user_token_tokeninfo_token  FOREIGN KEY (token)  REFERENCES otppms_tokeninfo (token) ON DELETE Cascade;
ALTER TABLE  otppms_user_token ADD  CONSTRAINT fk_user_token_dminfo_domainid  FOREIGN KEY (domainid)  REFERENCES otppms_domaininfo (domainid) ON DELETE Cascade;
CREATE  sequence ftp_user_token_bindid_seq minvalue 1 START WITH 1 increment BY 1 cache 20;
CREATE OR REPLACE TRIGGER trg_ftp_user_token_add BEFORE INSERT ON otppms_user_token FOR EACH ROW BEGIN  SELECT ftp_user_token_bindid_seq.nextval INTO :NEW.bindid FROM dual; END trg_ftp_user_token_add;;

commit;
  
-- otppms_hostinfo --
CREATE TABLE  otppms_hostinfo (
  hostipaddr VARCHAR2(64) NOT NULL,
  hostname VARCHAR2(64),
  confid NUMBER(10) DEFAULT 0,
  priority NUMBER(10) DEFAULT 0,
  licid VARCHAR2(32) DEFAULT '',
  descp VARCHAR2(255) DEFAULT '',
  ftradiusenabled NUMBER(10) DEFAULT 1,
  protocoltype VARCHAR2(32) DEFAULT 'udp' NOT NULL ,
  authport NUMBER(10)  DEFAULT 1915 NOT NULL,
  syncport NUMBER(10)  DEFAULT 1916 NOT NULL,
  radiusenabled NUMBER(10)  DEFAULT 1 NOT NULL,
  radauthport NUMBER(10)  DEFAULT 1812 NOT NULL,
  httpenabled NUMBER(10) DEFAULT 0,
  httpport NUMBER(10) DEFAULT 18080,
  httpsenabled NUMBER(10) DEFAULT 0,
  httpsport NUMBER(10) DEFAULT 18443,
  keystorepwd VARCHAR2(64) DEFAULT '',
  certificatepwd VARCHAR2(64) DEFAULT '',
  keystoreinstance VARCHAR2(32) DEFAULT '',
  keystorerootpath VARCHAR2(4000) DEFAULT '',
  soapenabled NUMBER(10)  DEFAULT 1 NOT NULL,
  soapport NUMBER(10)  DEFAULT 18081 NOT NULL,
  webservicename VARCHAR2(32)  DEFAULT 'otpwebservice' NOT NULL
) ;
 ALTER TABLE otppms_hostinfo ADD  CONSTRAINT pk_hostinfo_hostipaddr PRIMARY KEY(hostipaddr) USING INDEX;
 
 -- otppms_agentconf --
CREATE TABLE otppms_agentconf (
  id NUMBER(10) NOT NULL ,
  confname VARCHAR2(64) NOT NULL ,
  type NUMBER(10)   DEFAULT 0 NOT NULL,
  userformat NUMBER(10) DEFAULT 0,
  localprotect NUMBER(10) DEFAULT 0,
  remoteprotect NUMBER(10) DEFAULT 0,
  uacprotect NUMBER(10) DEFAULT 0,
  unboundlogin NUMBER(10) DEFAULT 0,
  descp VARCHAR2(255) DEFAULT ''
) ;
ALTER TABLE otppms_agentconf ADD CONSTRAINT pk_agentconf_id PRIMARY KEY(id) USING INDEX;
ALTER  TABLE otppms_agentconf ADD CONSTRAINT  uk_agentconf_confname UNIQUE(confname);
CREATE  sequence ftp_agentconf_id_seq minvalue 1 START WITH 1 increment BY 1 cache 20;
CREATE OR REPLACE TRIGGER trg_ftp_agentconf_add BEFORE INSERT ON otppms_agentconf FOR EACH ROW BEGIN  SELECT ftp_agentconf_id_seq.nextval INTO :NEW.id FROM dual; END trg_ftp_agentconf_add;;

commit;


-- otppms_agentinfo --
CREATE TABLE otppms_agentinfo  (
  agentipaddr VARCHAR2(64) NOT NULL,
  agentname VARCHAR2(64) DEFAULT '',
  pubkey VARCHAR2(64)  DEFAULT '' NOT NULL,
  enabled NUMBER(10)  DEFAULT 1 NOT NULL,
  descp VARCHAR2(255) DEFAULT '',
  agenttype NUMBER(10)  DEFAULT 0 NOT NULL,
  agentconfid NUMBER(10) DEFAULT 0,
  graceperiod NUMBER(10) DEFAULT 0
) ;
ALTER TABLE otppms_agentinfo ADD CONSTRAINT pk_agentinfo_agentipaddr PRIMARY KEY(agentipaddr) USING INDEX;
 
-- otppms_agent_host --
CREATE TABLE   otppms_agent_host (
  agentipaddr VARCHAR2(64) NOT NULL,
  hostipaddr  VARCHAR2(64) NOT NULL 
  ) ;
 ALTER TABLE otppms_agent_host ADD CONSTRAINT  pk_agent_host PRIMARY KEY(agentipaddr,hostipaddr) USING INDEX;
 ALTER TABLE otppms_agent_host ADD CONSTRAINT  fk_ahost_agentinfo_agentipaddr FOREIGN KEY (agentipaddr) REFERENCES otppms_agentinfo (agentipaddr) ON DELETE Cascade;
 ALTER TABLE otppms_agent_host ADD CONSTRAINT  fk_ahost_hostinfo_hostipaddr FOREIGN KEY (hostipaddr) REFERENCES otppms_hostinfo (hostipaddr) ON DELETE Cascade ; 
 
-- otppms_licinfo --
CREATE TABLE  otppms_licinfo(
	licid VARCHAR2 (32) NOT NULL,
	licinfo VARCHAR2 (2048) NOT NULL,
	lictype NUMBER(10)  NOT NULL,
	issuer VARCHAR2 (64) NOT NULL,
	customerid VARCHAR2 (64) NOT NULL,
	licstate NUMBER(10)  DEFAULT 1 NOT NULL,
    licupdatetime NUMBER(10)  DEFAULT 0 NOT NULL
);
 
 -- otppms_loginfo --
 CREATE TABLE  otppms_loginfo (
  id NUMBER(10) NOT NULL ,
  userid VARCHAR2(64) DEFAULT '',
  token VARCHAR2(32) DEFAULT '',
  serverip VARCHAR2(64) DEFAULT '',
  clientip VARCHAR2(64) DEFAULT '',
  actionid NUMBER(10) NOT NULL,
  actionresult NUMBER(10) NOT NULL,
  logtime NUMBER(10) NOT NULL,
  logcontent VARCHAR2(255) DEFAULT '',
  hashcode VARCHAR2(255) DEFAULT 0,
  domainid NUMBER(10)  DEFAULT 0,
  domainname VARCHAR2(64)  DEFAULT '',
  orgunitid NUMBER(10) DEFAULT 0,
  orgunitname VARCHAR2(64) DEFAULT '',
  moduletype NUMBER(10)  DEFAULT 0 NOT NULL,
  vendorid VARCHAR2(32) DEFAULT ''
) ;
ALTER TABLE otppms_loginfo ADD CONSTRAINT  pk_loginfo_id PRIMARY KEY(id) USING INDEX;
CREATE sequence ftp_loginfo_id_seq minvalue 1 START WITH 1 increment BY 1 cache 20;
CREATE OR REPLACE TRIGGER trg_ftp_loginfo_add BEFORE INSERT ON otppms_loginfo FOR EACH ROW BEGIN SELECT ftp_loginfo_id_seq.nextval INTO :NEW.id FROM dual; END trg_ftp_loginfo_add;;

commit;
 
-- otppms_admininfo --
CREATE TABLE  otppms_admininfo (
  adminid VARCHAR2(64) NOT NULL,
  realname VARCHAR2(64) DEFAULT '',
  localauth NUMBER(10)  DEFAULT 0  NOT NULL,
  pwd VARCHAR2(64) NOT NULL,
  pwdsettime NUMBER(10) DEFAULT 0 NOT NULL ,
  getpwddeath NUMBER(10) DEFAULT 0 ,
  locked NUMBER(10) DEFAULT 0,
  temploginerrcnt NUMBER(10) DEFAULT 0,
  longloginerrcnt NUMBER(10) DEFAULT 0,
  loginlocktime NUMBER(10) DEFAULT 0,
  logintime NUMBER(10) DEFAULT 0,
  logincnt NUMBER(10) DEFAULT 0,
  createuser VARCHAR2 (64) NOT NULL,
  enabled NUMBER(10) DEFAULT 1,
  email VARCHAR2(255) DEFAULT '',
  address VARCHAR2(128) DEFAULT '',
  tel VARCHAR2(64) DEFAULT '',
  cellphone VARCHAR2(64) DEFAULT '',
  createtime NUMBER(10) NOT NULL,
  descp VARCHAR2(255) DEFAULT ''
) ;
ALTER  TABLE otppms_admininfo ADD CONSTRAINT pk_admininfo_adminid PRIMARY KEY (adminid) USING INDEX;

-- otppms_admin_orgunit --
CREATE TABLE otppms_admin_orgunit (
  adminid VARCHAR2(64) NOT NULL,
  domainid NUMBER(10)  NOT NULL,
  orgunitid NUMBER(10)
) ;
ALTER TABLE  otppms_admin_orgunit ADD CONSTRAINT  fk_adm_org_adminfo_adminid FOREIGN KEY (adminid) REFERENCES otppms_admininfo (adminid) ON DELETE Cascade;
ALTER TABLE  otppms_admin_orgunit ADD CONSTRAINT  fk_adm_org_dominfo_domainid FOREIGN KEY (domainid) REFERENCES otppms_domaininfo (domainid) ON DELETE Cascade ; 
ALTER TABLE  otppms_admin_orgunit ADD CONSTRAINT  fk_adm_org_orginfo_orgunitid FOREIGN KEY (orgunitid) REFERENCES otppms_orgunitinfo (orgunitid) ON DELETE Cascade ;   

-- otppms_perminfo --
CREATE TABLE  otppms_perminfo (
  permcode VARCHAR2(64) NOT NULL,
  permlink VARCHAR2(255) DEFAULT '',
  srcname VARCHAR2(255) DEFAULT '',
  keymark VARCHAR2(64) DEFAULT '',
  descp VARCHAR2(255) DEFAULT ''
);
ALTER TABLE otppms_perminfo ADD CONSTRAINT  pk_perminfo_permcode PRIMARY KEY (permcode) USING INDEX;

-- otppms_roleinfo --
CREATE TABLE  otppms_roleinfo (
    roleid   NUMBER(10) NOT NULL,
    rolename  VARCHAR2(64) NOT NULL,
    rolemark  VARCHAR2(64)  DEFAULT '',
    createuser VARCHAR2 (64) DEFAULT '' NOT NULL ,
    createtime  NUMBER(10)  DEFAULT 0 NOT NULL,
    modifytime NUMBER(10) DEFAULT 0  NOT NULL,
    descp VARCHAR2(255)  DEFAULT ''
) ;
ALTER TABLE  otppms_roleinfo ADD CONSTRAINT pk_roleinfo_roleid PRIMARY KEY (roleid) USING INDEX;
CREATE  sequence ftp_roleinfo_roleid_seq minvalue 1 START WITH 1 increment BY 1 cache 20;
CREATE OR REPLACE TRIGGER trg_ftp_roleinfo_add BEFORE INSERT ON otppms_roleinfo FOR EACH ROW BEGIN SELECT ftp_roleinfo_roleid_seq.nextval INTO :NEW.roleid FROM dual; END trg_ftp_roleinfo_add;;

 
-- otppms_role_perm --
CREATE TABLE  otppms_role_perm (
	roleid NUMBER(10)  NOT NULL,
	permcode VARCHAR2(64) NOT NULL
) ;
ALTER TABLE  otppms_role_perm ADD CONSTRAINT pk_role_perm PRIMARY KEY (roleid, permcode) USING INDEX;
ALTER TABLE  otppms_role_perm ADD CONSTRAINT fk_role_perm_roleinfo_roleid FOREIGN   KEY (roleid)   REFERENCES  otppms_roleinfo(roleid) ON DELETE Cascade ;
ALTER TABLE  otppms_role_perm ADD CONSTRAINT fk_role_perm_perminfo_permcode FOREIGN KEY (permcode) REFERENCES  otppms_perminfo (permcode) ON DELETE Cascade ;

commit;

-- otppms_admin_role --
CREATE TABLE  otppms_admin_role (
    adminid VARCHAR2(64) NOT NULL,
    roleid NUMBER(10)  NOT NULL
) ;
ALTER TABLE otppms_admin_role ADD CONSTRAINT pk__admin_role PRIMARY KEY (adminid, roleid) using index;
ALTER TABLE otppms_admin_role ADD CONSTRAINT fk_admin_role_adminfo_adminid FOREIGN   KEY (adminid)  REFERENCES  otppms_admininfo(adminid) ON DELETE Cascade ;
ALTER TABLE otppms_admin_role ADD CONSTRAINT fk_admin_role_roleinfo_roleid  FOREIGN   KEY (roleid)   REFERENCES otppms_roleinfo (roleid) ON DELETE Cascade ;

-- otppms_admin_log --
CREATE TABLE   otppms_admin_log (
  id NUMBER(10) NOT NULL ,
  operator VARCHAR2(64) default '',
  logtime NUMBER(10)  NOT NULL,
  actionid NUMBER(10)  NOT NULL,
  actionresult NUMBER(10)  NOT NULL,
  actionobject NUMBER(10)  NOT NULL,
  clientip VARCHAR2(64) DEFAULT '',
  descp VARCHAR2(4000)  DEFAULT '',
  hashcode VARCHAR2(255)  DEFAULT ''
) ;
ALTER TABLE otppms_admin_log ADD CONSTRAINT pk_admin_log_id PRIMARY KEY (id) USING INDEX;
CREATE sequence ftp_admin_log_id_seq minvalue 1 START WITH 1 increment BY 1 cache 20;
CREATE OR REPLACE TRIGGER trg_ftp_admin_log_add BEFORE INSERT ON otppms_admin_log FOR EACH ROW BEGIN SELECT ftp_admin_log_id_seq.nextval INTO :NEW.id FROM dual; END trg_ftp_admin_log_add;;

commit;

-- otppms_taskinfo --
CREATE TABLE otppms_taskinfo (
  taskname VARCHAR2(128)   DEFAULT '' NOT NULL,
  sourceid NUMBER(10) NOT NULL ,
  sourcetype NUMBER(10)  DEFAULT 0 NOT NULL ,
  taskmode1 NUMBER(10) NOT NULL ,
  taskmode2 NUMBER(10) NOT NULL ,
  taskminute VARCHAR2(64) DEFAULT '',
  taskhour VARCHAR2(64) DEFAULT '',
  taskday VARCHAR2(64) DEFAULT '',
  taskmonth VARCHAR2(64) DEFAULT '',
  taskweek VARCHAR2(64) DEFAULT '',
  enabled NUMBER(10) DEFAULT 1,
  taskid VARCHAR2(128)  DEFAULT '' NOT NULL ,
  descp VARCHAR2(255) DEFAULT ''
) ;
ALTER TABLE otppms_taskinfo ADD CONSTRAINT pk_taskinfo_taskname PRIMARY KEY (taskname) USING INDEX;
ALTER  TABLE otppms_taskinfo ADD CONSTRAINT  uk_taskinfo UNIQUE(taskid, sourcetype);
 
-- otppms_configinfo --
CREATE TABLE  otppms_configinfo (
    id NUMBER(10) NOT NULL ,
    confname VARCHAR2(64) NOT NULL,
    conftype VARCHAR2(64) NOT NULL,
    confvalue VARCHAR2(128)  NOT NULL,
    parentid NUMBER(10)    DEFAULT -1 ,
    descp VARCHAR2(255)   DEFAULT ''
) ;
ALTER TABLE otppms_configinfo ADD CONSTRAINT pk_configinfo_id PRIMARY KEY(id) USING INDEX;
ALTER  TABLE otppms_configinfo ADD CONSTRAINT  uk_conf_cfname_cftype_cfvalue UNIQUE(confname,conftype,confvalue); 
CREATE sequence ftp_configinfo_id_seq minvalue 1 START WITH 1 increment BY 1 cache 20;
CREATE OR REPLACE TRIGGER trg_ftp_configinfo_add BEFORE INSERT ON otppms_configinfo FOR EACH ROW BEGIN SELECT ftp_configinfo_id_seq.nextval INTO :NEW.id FROM dual; END trg_ftp_configinfo_add;;

commit;

 
-- otppms_usersource --
CREATE TABLE otppms_usersource (
  id NUMBER(10) NOT NULL,
  sourcename VARCHAR2(64) DEFAULT ''  NOT NULL,
  sourcetype NUMBER(10) DEFAULT 0 NOT NULL ,
  dbtype NUMBER(10) DEFAULT 0,
  serveraddr VARCHAR2(64) DEFAULT '',
  port NUMBER(10) DEFAULT 0,
  username VARCHAR2(64)  DEFAULT '' NOT NULL,
  pwd VARCHAR2(64) DEFAULT '',
  dbname VARCHAR2(64) DEFAULT '',
  dbtablename varchar(64) DEFAULT '',
  filter VARCHAR2(255) DEFAULT '',
  basedn VARCHAR2(255) DEFAULT '',
  rootdn VARCHAR2(255) DEFAULT '',
  dominodriver VARCHAR2(64) DEFAULT '',
  namesfile VARCHAR2(64) DEFAULT '',
  timeout NUMBER(10) DEFAULT 30,
  isupdateou NUMBER(10) DEFAULT 0,
  upinvaliduser NUMBER(10) DEFAULT 0,
  domainid NUMBER(10) NOT NULL,
  orgunitid NUMBER(10),
  localusermark NUMBER(10) DEFAULT 0,
  issyncuserinfo NUMBER(10) DEFAULT 0,
  descp VARCHAR2(255) DEFAULT ''
) ;
ALTER TABLE otppms_usersource ADD CONSTRAINT pk_usersource_id PRIMARY KEY(id) USING INDEX;
ALTER  TABLE otppms_usersource ADD CONSTRAINT  uk_usersource_sourcename UNIQUE(sourcename); 
CREATE sequence ftp_usersource_id_seq minvalue 1 START WITH 1 increment BY 1 cache 20;
CREATE OR REPLACE TRIGGER trg_ftp_usersource_add BEFORE INSERT ON otppms_usersource FOR EACH ROW BEGIN SELECT ftp_usersource_id_seq.nextval INTO :NEW.id FROM dual; END trg_ftp_usersource_add;;

commit;

 
-- otppms_usersource_attr -- 
CREATE TABLE otppms_usersource_attr (
  sourceid NUMBER(10) NOT NULL,
  localuserattr VARCHAR2(64) DEFAULT '',
  sourceuserattr VARCHAR2(64) DEFAULT ''
) ;
ALTER TABLE otppms_usersource_attr ADD CONSTRAINT pk_usersource_attr PRIMARY KEY(sourceid, localuserattr, sourceuserattr) USING INDEX;
ALTER TABLE otppms_usersource_attr ADD CONSTRAINT fk_usersource_attr_sourceid  FOREIGN   KEY (sourceid)   REFERENCES otppms_usersource (id) ON DELETE Cascade ;

-- otppms_emailinfo --
CREATE TABLE otppms_emailinfo (
  id NUMBER(10) NOT NULL ,
  servname VARCHAR2(128) DEFAULT '' NOT NULL,
  account VARCHAR2(64)  DEFAULT '' NOT NULL,
  pwd VARCHAR2(64) DEFAULT '',
  sender VARCHAR2(64)  DEFAULT '' NOT NULL,
  host VARCHAR2(64)  DEFAULT '' NOT NULL,
  port NUMBER(10)  DEFAULT 25 NOT NULL,
  validated NUMBER(10) DEFAULT 0,
  isdefault NUMBER(10) DEFAULT 0,
  descp VARCHAR2(255) DEFAULT ''
) ;
ALTER TABLE otppms_emailinfo ADD CONSTRAINT pk_emailinfo_id PRIMARY KEY(id) USING INDEX;
ALTER  TABLE otppms_emailinfo ADD CONSTRAINT  uk_emailinfo_servname UNIQUE(servname); 
CREATE sequence ftp_emailinfo_id_seq minvalue 1 START WITH 1 increment BY 1 cache 20;
CREATE OR REPLACE TRIGGER trg_ftp_emailinfo_add BEFORE INSERT ON otppms_emailinfo FOR EACH ROW BEGIN SELECT ftp_emailinfo_id_seq.nextval INTO :NEW.id FROM dual; END trg_ftp_emailinfo_add;;

commit;


-- otppms_smsinfo --
CREATE TABLE otppms_smsinfo (
  id NUMBER(10) NOT NULL ,
  smsname VARCHAR2(64) DEFAULT '' NOT NULL,
  sendtype NUMBER(10) DEFAULT 0,
  host VARCHAR2(128) DEFAULT '' NOT NULL ,
  username VARCHAR2(64)  DEFAULT '' NOT NULL,
  pwd VARCHAR2(64)  DEFAULT '' NOT NULL,
  accountattr VARCHAR2(32) DEFAULT '' NOT NULL,
  passwdattr VARCHAR2(32) DEFAULT '' NOT NULL,
  phoneattr VARCHAR2(32) DEFAULT '' NOT NULL,
  messageattr VARCHAR2(32) DEFAULT '' NOT NULL,
  paramannex VARCHAR2(128) DEFAULT '',
  sendresult VARCHAR2(255) DEFAULT '',
  enabled NUMBER(10) DEFAULT 0,
  priority NUMBER(10) DEFAULT 0,
  retrysend NUMBER(10) DEFAULT 0,
  descp VARCHAR2(255) DEFAULT ''
) ;
ALTER TABLE otppms_smsinfo ADD CONSTRAINT pk_smsinfo_id PRIMARY KEY(id) USING INDEX;
CREATE sequence ftp_smsinfo_id_seq minvalue 1 START WITH 1 increment BY 1 cache 20;
ALTER  TABLE otppms_smsinfo ADD CONSTRAINT  uk_smsinfo_smsname UNIQUE(smsname); 
CREATE OR REPLACE TRIGGER trg_ftp_smsinfo_add BEFORE INSERT ON otppms_smsinfo FOR EACH ROW BEGIN SELECT ftp_smsinfo_id_seq.nextval INTO :NEW.id FROM dual; END trg_ftp_smsinfo_add;;

commit;

 

-- otppms_attrinfo --
CREATE TABLE otppms_attrinfo (
  attrtype NUMBER(10) DEFAULT 0  NOT NULL ,
  attrid   VARCHAR2(32)  DEFAULT '' NOT NULL,
  attrname VARCHAR2(64)  DEFAULT '' NOT NULL,
  attrvalue VARCHAR2(128)  DEFAULT '' NOT NULL,
  valuetype VARCHAR2(32) DEFAULT '' NOT NULL ,
  vendorid NUMBER(10) DEFAULT -1  NOT NULL ,
  profileid NUMBER(10) NOT NULL
) ;
ALTER TABLE otppms_attrinfo ADD CONSTRAINT pk_attrinfo_attrid_profileid PRIMARY KEY(attrid, profileid,vendorid) USING INDEX;
ALTER TABLE otppms_attrinfo ADD CONSTRAINT fk_attrinfo_rpile_profileid  FOREIGN   KEY (profileid)   REFERENCES otppms_rad_profile (id) ON DELETE Cascade ;

-- otppms_trustipinfo --
CREATE TABLE otppms_trustipinfo (
  id NUMBER(10)  NOT NULL   ,
  systype NUMBER(10)  DEFAULT 0  NOT NULL ,
  clientapptype NUMBER(10)   DEFAULT 0  NOT NULL,
  startip VARCHAR2(64)   DEFAULT '' NOT NULL ,
  endip VARCHAR2(64) DEFAULT '',
  expiretime NUMBER(10) DEFAULT 0,
  updatetime NUMBER(10) DEFAULT 0,
  enabled NUMBER(10) DEFAULT 0,
  clientappport NUMBER(10) DEFAULT 0,
  clientservpath VARCHAR2(64) DEFAULT ''
) ;
ALTER TABLE otppms_trustipinfo ADD CONSTRAINT pk_trustipinfo_id PRIMARY KEY(id) USING INDEX;
CREATE sequence ftp_trustipinfo_id_seq minvalue 1 START WITH 1 increment BY 1 cache 20;
CREATE OR REPLACE TRIGGER trg_ftp_trustipinfo_add BEFORE INSERT ON otppms_trustipinfo FOR EACH ROW BEGIN SELECT ftp_trustipinfo_id_seq.nextval INTO :NEW.id FROM dual; END trg_ftp_trustipinfo_add;;

commit;

-- otppms_portal_notice --
CREATE TABLE otppms_portal_notice (
  id NUMBER(10) NOT NULL ,
  systype NUMBER(10)  DEFAULT 0 NOT NULL,
  createuser VARCHAR2(64) DEFAULT '',
  createtime NUMBER(10) DEFAULT 0,
  expiretime NUMBER(10) DEFAULT 0,
  content CLOB  DEFAULT '',
  title VARCHAR2(128) NOT NULL
) ;
ALTER TABLE otppms_portal_notice ADD CONSTRAINT pk_portal_notice_id PRIMARY KEY(id) USING INDEX;
CREATE sequence ftp_portal_notice_id_seq minvalue 1 START WITH 1 increment BY 1 cache 20;
CREATE OR REPLACE TRIGGER trg_ftp_portal_notice_add BEFORE INSERT ON otppms_portal_notice FOR EACH ROW BEGIN SELECT ftp_portal_notice_id_seq.nextval INTO :NEW.id FROM dual; END trg_ftp_portal_notice_add;;

commit;

 
-- otppms_backend --
CREATE TABLE otppms_backend (
  id NUMBER(10) NOT NULL ,
  backendtype NUMBER(10)  DEFAULT 0 NOT NULL,
  backendname VARCHAR2(64) NOT NULL,
  host VARCHAR2(64)  DEFAULT '' NOT NULL,
  sparehost VARCHAR2(64) DEFAULT '',
  port NUMBER(10)  DEFAULT 389 NOT NULL,
  priority NUMBER(10) DEFAULT 0,
  basedn VARCHAR2(255) DEFAULT '',
  filter VARCHAR2(255) DEFAULT '(&(objectCategory=person)(objectClass=user))',
  pubkey VARCHAR2(255) DEFAULT '',  
  timeout NUMBER(10) DEFAULT 30,
  retrycnt NUMBER(10) DEFAULT 0,
  usernamerule NUMBER(10) DEFAULT 0,
  delimiter VARCHAR2(32) DEFAULT '@',
  enabled NUMBER(10) DEFAULT 0,
  policy NUMBER(10) DEFAULT 0,
  domainid NUMBER(10) DEFAULT 0
) ;
ALTER TABLE otppms_backend ADD CONSTRAINT pk_backend_id PRIMARY KEY(id) USING INDEX;
ALTER  TABLE otppms_backend ADD CONSTRAINT  uk_backend_backendname UNIQUE(backendname); 
ALTER  TABLE otppms_backend ADD CONSTRAINT  uk_backend_host_port_domainid UNIQUE(host,port,domainid); 
CREATE sequence ftp_backend_id_seq minvalue 1 START WITH 1 increment BY 1 cache 20;
CREATE OR REPLACE TRIGGER trg_ftp_backend_add BEFORE INSERT ON otppms_backend FOR EACH ROW BEGIN SELECT ftp_backend_id_seq.nextval INTO :NEW.id FROM dual; END trg_ftp_backend_add;;

commit;

-- otppms_monitor_admin --
CREATE TABLE otppms_monitor_admin (
  adminid VARCHAR2(64) NOT NULL,
  conftype VARCHAR2(64) NOT NULL
) ;
ALTER TABLE otppms_monitor_admin ADD CONSTRAINT pk_monitor_admin PRIMARY KEY(adminid,conftype) USING INDEX; 
ALTER TABLE otppms_monitor_admin ADD CONSTRAINT fk_monitor_admin_adm_adminid  FOREIGN   KEY (adminid)   REFERENCES otppms_admininfo (adminid) ON DELETE Cascade;
 
 
-- otppms_monitorinfo --
CREATE TABLE  otppms_monitorinfo (
  id NUMBER(10) NOT NULL ,
  email VARCHAR2(1024),
  mobile VARCHAR2(1024),
  confid NUMBER(10) DEFAULT 0,
  conftype VARCHAR2(64) NOT NULL,
  title VARCHAR2(128) DEFAULT '',
  content CLOB DEFAULT '',
  sendtime NUMBER(10) DEFAULT 0,
  issend NUMBER(10) DEFAULT 0
) ;
ALTER TABLE otppms_monitorinfo ADD CONSTRAINT pk_monitorinfo_id PRIMARY KEY(id) USING INDEX; 
CREATE sequence ftp_monitorinfo_id_seq minvalue 1 START WITH 1 increment BY 1 cache 20;
CREATE OR REPLACE TRIGGER trg_ftp_monitorinfo_add BEFORE INSERT ON otppms_monitorinfo FOR EACH ROW BEGIN SELECT ftp_monitorinfo_id_seq.nextval INTO :NEW.id FROM dual; END trg_ftp_monitorinfo_add;;

commit;

-- otppms_admin_perm --
create table otppms_admin_perm (
	adminid VARCHAR2(64) NOT NULL,
	permcode VARCHAR2(64) NOT NULL
);
ALTER TABLE otppms_admin_perm ADD CONSTRAINT pk_admin_perm PRIMARY KEY(adminid,permcode) USING INDEX; 
ALTER TABLE otppms_admin_perm ADD CONSTRAINT fk_admin_perm_admininfo_admid FOREIGN KEY (adminid) REFERENCES otppms_admininfo (adminid) ON DELETE Cascade;
ALTER TABLE otppms_admin_perm ADD CONSTRAINT fk_admin_perm_perminfo_percode FOREIGN KEY (permcode) REFERENCES otppms_perminfo (permcode) ON DELETE Cascade;

commit;
