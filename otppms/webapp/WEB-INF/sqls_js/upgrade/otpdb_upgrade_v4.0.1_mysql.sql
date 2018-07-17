-- alter table otppms_attrinfo
ALTER TABLE otppms_attrinfo ADD COLUMN vendorid int(10) NOT NULL DEFAULT -1;

ALTER TABLE otppms_attrinfo DROP PRIMARY KEY,
ADD PRIMARY KEY (attrid, profileid, vendorid);

-- alter table otppms_usersource
ALTER TABLE otppms_usersource ADD COLUMN rootdn varchar(255) DEFAULT '';
ALTER TABLE otppms_usersource ADD COLUMN localusermark int(10) DEFAULT 0;
ALTER TABLE otppms_usersource ADD COLUMN issyncuserinfo int(10) DEFAULT 0;

-- alter table otppms_userinfo
ALTER TABLE otppms_userinfo ADD COLUMN initactpwd varchar(64) DEFAULT '';
ALTER TABLE otppms_userinfo ADD COLUMN initactpwddeath int(10) DEFAULT 0;

ALTER TABLE otppms_userinfo MODIFY email varchar(255);
ALTER TABLE otppms_userinfo MODIFY cellphone varchar(64);
ALTER TABLE otppms_userinfo MODIFY tel varchar(64);

-- alter table otppms_loginfo
ALTER TABLE otppms_loginfo ADD COLUMN serverip varchar(64) DEFAULT '';

-- alter table otppms_agentinfo
ALTER TABLE otppms_agentinfo ADD COLUMN agentname varchar(64) DEFAULT '';

-- alter table otppms_admininfo
ALTER TABLE otppms_admininfo MODIFY email varchar(255);
ALTER TABLE otppms_admininfo MODIFY cellphone varchar(64);
ALTER TABLE otppms_admininfo MODIFY tel varchar(64);

-- alter table otppms_portal_notice
alter table otppms_portal_notice DROP INDEX uk_portal_notice_title;