-- upgrade sql version
update otppms_configinfo set confvalue='4.2.0'  where confname='sqlsversion' and conftype='common';

-- user
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('auth_otp_when_bind', 'user', '1', 0, '0-not need,1-require,2-optional');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('default_localauth', 'user', '0', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('default_backendauth', 'user', '0', 0, '');

-- peap
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('enabled_peap', 'auth', 'n', 0, 'y/n');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('locked_peap', 'auth', 'y', 0, 'y/n');

-- portal
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('init_pwd_login_verify_type', 'portal', '1', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('init_pwd_email_active_expire', 'portal', '1', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('init_pwd_sms_verify_expire', 'portal', '15', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('ad_verify_pwd_ip', 'portal', '127.0.0.1', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('ad_verify_pwd_port', 'portal', '389', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('ad_verify_pwd_dn', 'portal', 'www.sample.com', 0, '');

-- auth config
update otppms_perminfo set permlink='/manager/confinfo/config/authConfAction!modify.action?oper=initconf' where permcode='050101';
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('050103', '/manager/confinfo/config/authConfAction!modify.action?oper=initpeap', '','', '');
insert into otppms_role_perm(roleid,permcode)values(1,'050103');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('050503', '/manager/confinfo/config/portal!find.action', '','', '');
insert into otppms_role_perm(roleid,permcode)values(1,'050503');

-- otppms_agentinfo
update otppms_agentinfo set agentname=agentipaddr;

-- heartbeat warn
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('050602', '/manager/confinfo/config/monitorconfig!find.action', '','', '');
insert into otppms_role_perm(roleid,permcode)values(1,'050602');

insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('enabled', 'warn_heart_beat', '0', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('main_ip', 'warn_heart_beat', ' ', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('spare_ip', 'warn_heart_beat', ' ', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('adminid', 'warn_heart_beat', ' ', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('time_interval', 'warn_heart_beat', '60', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('send_type', 'warn_heart_beat', ' ', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('port', 'warn_heart_beat', ' ', 0, '');