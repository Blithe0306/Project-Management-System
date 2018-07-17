-- alter table otppms_attrinfo
ALTER TABLE otppms_attrinfo ADD COLUMN vendorid integer NOT NULL DEFAULT -1;

ALTER TABLE otppms_attrinfo DROP CONSTRAINT pk_attrinfo_attrid_profileid;
ALTER TABLE otppms_attrinfo ADD CONSTRAINT pk_attrinfo_attrid_profileid PRIMARY KEY(attrid, profileid,vendorid);

-- alter table otppms_usersource
ALTER TABLE otppms_usersource ADD COLUMN rootdn character varying(255) DEFAULT '';
ALTER TABLE otppms_usersource ADD COLUMN localusermark integer DEFAULT 0;
ALTER TABLE otppms_usersource ADD COLUMN issyncuserinfo integer DEFAULT 0;

-- alter table otppms_userinfo
ALTER TABLE otppms_userinfo ADD COLUMN initactpwd character varying(64) DEFAULT '';
ALTER TABLE otppms_userinfo ADD COLUMN initactpwddeath integer DEFAULT 0;

ALTER TABLE otppms_userinfo ALTER COLUMN tel type varchar(64);
ALTER TABLE otppms_userinfo ALTER COLUMN cellphone type varchar(64);
ALTER TABLE otppms_userinfo ALTER COLUMN email type varchar(255);

-- alter table otppms_loginfo
ALTER TABLE otppms_loginfo ADD COLUMN serverip character varying(64) DEFAULT '';

-- alter table otppms_agentinfo
ALTER TABLE otppms_agentinfo ADD COLUMN agentname character varying(64) DEFAULT '';

-- alter table otppms_admininfo
ALTER TABLE otppms_admininfo ALTER COLUMN tel type varchar(64);
ALTER TABLE otppms_admininfo ALTER COLUMN cellphone type varchar(64);
ALTER TABLE otppms_admininfo ALTER COLUMN email type varchar(255);

-- alter table otppms_portal_notice
alter table otppms_portal_notice drop constraint uk_portal_notice_title;