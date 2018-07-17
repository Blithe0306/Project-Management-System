-- alter table otppms_attrinfo
ALTER TABLE otppms_attrinfo ADD  vendorid NUMBER(10)  DEFAULT -1 NOT NULL;

ALTER TABLE otppms_attrinfo DROP CONSTRAINT pk_attrinfo_attrid_profileid;
ALTER TABLE otppms_attrinfo ADD CONSTRAINT pk_attrinfo_attrid_profileid PRIMARY KEY(attrid, profileid,vendorid) USING INDEX;

-- alter table otppms_usersource
ALTER TABLE otppms_usersource ADD  rootdn VARCHAR2(255) DEFAULT '';
ALTER TABLE otppms_usersource ADD  localusermark NUMBER(10) DEFAULT 0;
ALTER TABLE otppms_usersource ADD  issyncuserinfo NUMBER(10) DEFAULT 0;

-- alter table otppms_userinfo
ALTER TABLE otppms_userinfo ADD initactpwd VARCHAR2(64) DEFAULT '';
ALTER TABLE otppms_userinfo ADD initactpwddeath NUMBER(10) DEFAULT 0;

ALTER TABLE otppms_userinfo MODIFY email VARCHAR2(255);
ALTER TABLE otppms_userinfo MODIFY cellphone VARCHAR2(64);
ALTER TABLE otppms_userinfo MODIFY tel VARCHAR2(64);

-- alter table otppms_loginfo
ALTER TABLE otppms_loginfo ADD serverip VARCHAR2(64) DEFAULT '';

-- alter table otppms_agentinfo
ALTER TABLE otppms_agentinfo ADD agentname VARCHAR2(64) DEFAULT '';

-- alter table otppms_admininfo
ALTER TABLE otppms_admininfo MODIFY email VARCHAR2(255);
ALTER TABLE otppms_admininfo MODIFY cellphone VARCHAR2(64);
ALTER TABLE otppms_admininfo MODIFY tel VARCHAR2(64);

-- alter table otppms_configinfo
ALTER TABLE otppms_configinfo MODIFY confvalue VARCHAR2(128);

-- alter table otppms_portal_notice
alter table otppms_portal_notice drop constraint uk_portal_notice_title;
