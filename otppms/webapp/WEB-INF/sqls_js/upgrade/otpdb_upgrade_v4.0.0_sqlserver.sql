-- alter table otppms_attrinfo
ALTER TABLE otppms_attrinfo ADD  vendorid int NOT NULL DEFAULT -1;
-- ALTER TABLE otppms_attrinfo DROP CONSTRAINT (select name from sysobjects where parent_obj in (select id from sysobjects where name='otppms_attrinfo') and xtype='pk');
-- ALTER TABLE otppms_attrinfo ADD CONSTRAINT  pk_attrinfo_attrid_profileid PRIMARY KEY(attrid, profileid,vendorid);

-- alter table otppms_usersource
ALTER TABLE otppms_usersource ADD rootdn varchar(255) DEFAULT '';
ALTER TABLE otppms_usersource ADD localusermark int DEFAULT 0;
ALTER TABLE otppms_usersource ADD issyncuserinfo int DEFAULT 0;

-- alter table otppms_userinfo
ALTER TABLE otppms_userinfo ADD initactpwd varchar(64) DEFAULT '';
ALTER TABLE otppms_userinfo ADD initactpwddeath int DEFAULT 0;

ALTER TABLE otppms_userinfo ALTER COLUMN tel varchar(64);
ALTER TABLE otppms_userinfo ALTER COLUMN email varchar(255);
ALTER TABLE otppms_userinfo ALTER COLUMN cellphone varchar(64);

-- alter table otppms_loginfo
ALTER TABLE otppms_loginfo ADD serverip varchar(64) DEFAULT '';

-- alter table otppms_agentinfo
ALTER TABLE otppms_agentinfo ADD agentname varchar(64) DEFAULT '';

-- alter table otppms_admininfo
ALTER TABLE otppms_admininfo ALTER COLUMN tel varchar(64);
ALTER TABLE otppms_admininfo ALTER COLUMN email varchar(255);
ALTER TABLE otppms_admininfo ALTER COLUMN cellphone varchar(64);

-- alter table otppms_configinfo
-- ALTER TABLE otppms_configinfo ALTER COLUMN confvalue varchar(128);

-- alter table otppms_portal_notice
alter table otppms_portal_notice drop constraint uk_portal_notice_title;