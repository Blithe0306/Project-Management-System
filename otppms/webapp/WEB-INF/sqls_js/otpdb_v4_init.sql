-- Set Default Domain
insert into otppms_domaininfo (domainsn, domainname,descp) 
values('default', 'default domain', 'Initialize the default domain');

-- config ---------------------------------------------------
-- insert sql version
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('sqlsversion', 'common', '4.2.0', 0, 'The sql version infomation');
-- insert common config
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('session_effectively_time', 'common', '120', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('log_level', 'common', '0', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('log_timing_enabled', 'common', '0', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('log_timing_delete_validity', 'common', '30', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('log_is_bak', 'common', '0', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('default_system_language', 'common', ' ', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) select 'default_domain_id', 'common', domainid, 0, '' from otppms_domaininfo where domainsn='default';
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('seed_private_key_random', 'common', ' ', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('userid_format_type', 'common', '1', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('db_data_create_type', 'common', '0', 0, '');

-- insert auth config
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('hotp_auth_wnd', 'auth', '40', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('hotp_adjust_wnd', 'auth', '20', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('hotp_sync_wnd', 'auth', '200', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('totp_auth_wnd', 'auth', '3', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('totp_adjust_wnd', 'auth', '1', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('totp_sync_wnd', 'auth', '20', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('wnd_adjust_mode', 'auth', '2', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('wnd_adjust_period', 'auth', '1209600', 0, '');

insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('retry_otp_timeinterval', 'auth', '0', 0, '');

insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('temp_lock_retry', 'auth', '5', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('max_retry', 'auth', '15', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('temp_lock_expire', 'auth', '600', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('enabled_peap', 'auth', 'n', 0, 'y/n');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('locked_peap', 'auth', 'y', 0, 'y/n');

-- insert center config
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('login_error_retry_temp', 'center', '5', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('login_error_retry_long', 'center', '15', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('login_lock_expire', 'center', '600', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('passwd_update_period', 'center', '30', 0, '');

insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('prohibit_admin', 'center', 'n', 0, 'y/n');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('email_activate_enabled', 'center', '0', 0, '');

insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('main_hostipaddr', 'center', '127.0.0.1', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('spare_hostipaddr', 'center', '127.0.0.1', 0, '');

insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('enabled_trust_ip_check', 'center', '0', 0, '');

-- insert user config
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('max_bind_tokens', 'user', '1', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('add_user_when_bind', 'user', 'n', 0, 'y/n');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('auth_otp_when_bind', 'user', '1', 0, '0-not need,1-require,2-optional');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('max_bind_users', 'user', '1', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('token_bind_is_change_org', 'user', '0', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('token_unbind_is_change_org', 'user', '0', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('unbind_state_select', 'user', '0', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('replace_state_select', 'user', '0', 0, '');

insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('default_user_pwd', 'user', '123456', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('default_localauth', 'user', '0', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('default_backendauth', 'user', '0', 0, '');

-- insert AD config
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('local_attr_userid', 'adattr', 'sAMAccountName', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('local_attr_realname', 'adattr', 'displayname', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('local_attr_email', 'adattr', 'mail', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('local_attr_address', 'adattr', ' ', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('local_attr_tel', 'adattr', 'telephoneNumber', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('local_attr_cellphone', 'adattr', 'mobile', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('local_attr_enabled', 'adattr', 'userAccountControl', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('local_attr_ou', 'adattr', 'distinguishedName', 0, '');

-- insert token config
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('softtoken_distribute_pwd', 'token', '12345678', 0, '');

insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('ap_period', 'token', '1', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('ap_retry', 'token', '5', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('defult_ap', 'token', '123456', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('ap_sms_send', 'token', '0', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('mobile_activate_code_message', 'token', 'Token number:[SN],Active password:[AP],Active code:[AC]', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('mobile_online_dist_message', 'token', 'URL:[URL],Active password:[AP]', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('dist_email_send', 'token', '0', 0, '');

insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('site_enabled', 'token', 'n', 0, 'y/n');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('site_type', 'token', '1', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('site_url', 'token', 'http://', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('ap_gen_method', 'token', '2', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('url_params', 'token', '2', 0, '');

insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('sms_token_auth_expire', 'token', '600', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('sms_token_gen_expire', 'token', '60', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('sms_otp_seed_message', 'token', 'Your SMS OTP is [SMSOTP]', 0, '');

-- insert radius request send smsotp password
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('sms_token_req_more_attr','token', 'State', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('sms_token_req_more_attr_val', 'token','get', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('sms_token_req_send_before_check', 'token','1', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('sms_token_req_return_code_domain', 'token','11', 0, '');

insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('token_empin2otp', 'token', '1', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('empin_otp_leneq', 'token', '1', 0, '1/0');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('emergency_pass_def_validtime', 'token', '2', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('emergency_pass_max_validtime', 'token', '168', 0, '');

-- insert portal config
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('self_service_enable', 'portal', '1', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('pwd_email_active_expire', 'portal', '24', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('open_function_config', 'portal', '1001,1003,1004,1006,1007,1008,1017', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('init_pwd_login_verify_type', 'portal', '1', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('init_pwd_email_active_expire', 'portal', '1', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('init_pwd_sms_verify_expire', 'portal', '15', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('ad_verify_pwd_ip', 'portal', '127.0.0.1', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('ad_verify_pwd_port', 'portal', '389', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('ad_verify_pwd_dn', 'portal', 'www.sample.com', 0, '');

-- insert monitor config
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('enabled', 'warn_base', '0', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('send_type', 'warn_base', '0', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('remain_days', 'warn_base', '30', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('unbind_lower', 'warn_base', '20', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('syn_cupper', 'warn_base', '20', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('time_interval', 'warn_base', '30', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('time_interval_common', 'warn_base', '2880', 0, '');

-- db bak config
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('is_time_auto', 'bak', '0', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('is_bak_Log', 'bak', '0', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('is_remote', 'bak', '0', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('dir', 'bak', '0', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('temp_dir', 'bak', '0', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('server_ip', 'bak', '0', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('port', 'bak', '0', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('user', 'bak', '0', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('password', 'bak', '0', 0, '');

-- heartbeat warn
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('enabled', 'warn_heart_beat', '0', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('main_ip', 'warn_heart_beat', ' ', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('spare_ip', 'warn_heart_beat', ' ', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('adminid', 'warn_heart_beat', ' ', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('time_interval', 'warn_heart_beat', '60', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('send_type', 'warn_heart_beat', ' ', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('port', 'warn_heart_beat', ' ', 0, '');

-- insert Default emailInfo
insert into otppms_emailinfo (servname, account, pwd, sender, host, port, validated, isdefault, descp) values ('default', 'default', '', 'default', 'default', 25, 0, 1, '');

-- insert adminperm --------------------------------------------------------
-- home
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('00', '', '','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('0000', '', '','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('000001', '/manager/lic/license!find.action', '<a href="javascript:setLicView();"><img src="<%=path%>/images/manager/services.png" height="20" width="20" border="0"> <span></span></a>','', '');

-- admin
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('01', '', '','', '');

-- admin user
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('0100', '/manager/admin/user/adminUser!init.action', '','', ''); 
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('0101','', '','', '');  
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('010101', '/manager/admin/user/adminUser!add.action', '<img src="<%=path%>/images/icon/file_add.gif" width="16" height="16" hspace="5" align="absmiddle"><a href="#" onClick="javascript:addAdmInfo()" class="isLink_HeiSe"><span></span></a>','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('010102', '/manager/admin/user/adminUser!modify.action', '<img src="<%=path%>/images/icon/file_edit.gif" width="16" height="16" hspace="2"  border="0"> ','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('010103', '/manager/admin/user/adminUser!delete.action', '<a href="javascript:delData();" style="display:inline-block" class="button"><span></span></a>','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('010104', '/manager/admin/user/adminUser!queryDesignate.action', '<a href="javascript:changeCreater();" style="display:inline-block" class="button"><span></span></a>','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('010105', '/manager/admin/user/adminUser!enabledAdmin.action', '<img src="<%=path%>/images/icon/error_go.png" width="16" height="16" hspace="2"  border="0">','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('010106', '/manager/admin/user/adminUser!lockedAdmin.action', '<img src="<%=path%>/images/icon/lock.png" width="16" height="16" hspace="2"   border="0">','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('010107', '/manager/admin/user/adminUser!modifyPassword.action', '<img src="<%=path%>/images/icon/key_go.png" width="16" height="16" hspace="2"  border="0">','', '');

-- admin role
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('0102', '/manager/admin/role/adminRole!init.action', '','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('0103', '', '','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('010301', '/manager/admin/role/adminRole!add.action', '<img src="<%=path%>/images/icon/file_add.gif" width="16" height="16" hspace="5" align="absmiddle"><a href="#" onClick="javascript:addRoleInfo()" class="isLink_HeiSe"><span></span></a>','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('010302', '/manager/admin/role/adminRole!modify.action', '<img src="<%=path%>/images/icon/file_edit.gif" width="16" height="16" hspace="2" border="0">','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('010303', '/manager/admin/role/adminRole!delete.action', '<a href="javascript:delData()" style="display:inline-block" class="button"><span></span></a>','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('010304', '/manager/admin/role/adminRole!queryDesignateRole.action', '<a href="javascript:changeCreater()" style="display:inline-block" class="button"><span></span></a>','', '');

-- User
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('02', '', '','', '');
-- user info


insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('0200', '/manager/customer/manager/customer/custInfo!init.action', '','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('020001', '/manager/customer/manager/customer/custInfo!add.action', '','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('020002', '/manager/user/userinfo/userInfo!init.action', '','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('020003', '/manager/user/userinfo/userInfo!init.action', '<img src="<%=path%>/images/icon/file_edit.gif" width="16" height="16" hspace="2"   border="0">','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('020004', '/manager/customer/manager/customer/custInfo!delete.action', '<a href="javascript:delData();" style="display:inline-block" class="button"><span></span></a>','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('020005', '', '<img src="<%=path%>/images/icon/link_go.png" width="16" height="16" hspace="2" border="0">','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('020006', '', '<img src="<%=path%>/images/icon/radius_set.png" width="16" height="16" hspace="2" border="0">','', '');


insert into otppms_perminfo(permcode, permlink, srcname,keymark,descp) values('03', '', '<a href="javascript:enableToken(''${tokenInfo.token}'',1)" id="enableStart" class="button"><span></span></a>','', '');
-- token info
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('0300', '/manager/token/token!init.action', '<img src="<%=path%>/images/icon/file_edit.gif" width="16" height="16" hspace="2"   border="0">','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030001', '/manager/project/list.jsp', '','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030002', '/manager/project/projectAction!toAddProject.action', '','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030003', '', '<img src="<%=path%>/images/icon/file_edit.gif" width="16" height="16" hspace="2" border="0">','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030004', '', '<a href="javascript:delData();" style="display:inline-block" class="button"><span></span></a>','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030005', '', '<img src="<%=path%>/images/icon/link_go.png" width="16" height="16" hspace="2" border="0">','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030006', '', '<img src="<%=path%>/images/icon/radius_set.png" width="16" height="16" hspace="2" border="0">','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030007', '', '<img src="<%=path%>/images/icon/email_add.png" width="16" height="16" hspace="2" border="0">','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030008', '', '<img src="<%=path%>/images/icon/email_attach.png" width="16" height="16" hspace="2" border="0">','', '');

insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('0302', '/manager/token/importToken!importToken.action', '','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030201', '/manager/prjinfo/list.jsp', '','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030202', '/manager/prjinfo/prjinfoAction!toAddPrjinfo.action', '','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030203', '', '<img src="<%=path%>/images/icon/file_edit.gif" width="16" height="16" hspace="2" border="0">','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030204', '', '<a href="javascript:delData();" style="display:inline-block" class="button"><span></span></a>','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030205', '', '<img src="<%=path%>/images/icon/email_add.png" width="16" height="16" hspace="2" border="0">','', '');

insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('0303', '', '','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030301', '', '','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030302', '', '','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030303', '', '<img src="<%=path%>/images/icon/file_edit.gif" width="16" height="16" hspace="2" border="0">','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030304', '', '<a href="javascript:delData();" style="display:inline-block" class="button"><span></span></a>','', '');

-- config
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('05', '', '','', '');

-- sys common config
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('0500', '', '','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('050003', '/manager/confinfo/email/email!init.action', '','', '');

-- Log
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('06', '', '','', '');
-- log admin
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('0600', '', '','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('060001', '/manager/logs/adminLog!init.action', '<a href="javascript:queryObj();" class="button"><span></span></a>','', '');
insert into otppms_perminfo(permcode, permlink, srcname,keymark,descp) values('060002', '/manager/logs/adminLog!init.action', '','', '');
insert into otppms_perminfo(permcode, permlink, srcname,keymark,descp) values('060003', '/manager/logs/adminLog!init.action', '','', '');

-- insert admin roleinfo
insert into otppms_roleinfo(rolename, rolemark, createuser, createtime, modifytime, descp)
values('poweradmin', 'ADMIN', 'admin', 0, 0, 'Super administrator can operate the entire system!');


insert into otppms_role_perm(roleid,permcode)values(1,'00');
insert into otppms_role_perm(roleid,permcode)values(1,'0000');
insert into otppms_role_perm(roleid,permcode)values(1,'000001');

insert into otppms_role_perm(roleid,permcode)values(1,'01');
insert into otppms_role_perm(roleid,permcode)values(1,'0100');
insert into otppms_role_perm(roleid,permcode)values(1,'0101');
insert into otppms_role_perm(roleid,permcode)values(1,'010101');
insert into otppms_role_perm(roleid,permcode)values(1,'010102');
insert into otppms_role_perm(roleid,permcode)values(1,'010103');
insert into otppms_role_perm(roleid,permcode)values(1,'010104');
insert into otppms_role_perm(roleid,permcode)values(1,'010105');
insert into otppms_role_perm(roleid,permcode)values(1,'010106');
insert into otppms_role_perm(roleid,permcode)values(1,'010107');

insert into otppms_role_perm(roleid,permcode)values(1,'0102');
insert into otppms_role_perm(roleid,permcode)values(1,'0103');
insert into otppms_role_perm(roleid,permcode)values(1,'010301');
insert into otppms_role_perm(roleid,permcode)values(1,'010302');
insert into otppms_role_perm(roleid,permcode)values(1,'010303');
insert into otppms_role_perm(roleid,permcode)values(1,'010304');

insert into otppms_role_perm(roleid,permcode)values(1,'02');
insert into otppms_role_perm(roleid,permcode)values(1,'0200');
insert into otppms_role_perm(roleid,permcode)values(1,'020001');
insert into otppms_role_perm(roleid,permcode)values(1,'020002');
insert into otppms_role_perm(roleid,permcode)values(1,'020003');
insert into otppms_role_perm(roleid,permcode)values(1,'020004');
insert into otppms_role_perm(roleid,permcode)values(1,'020005');
insert into otppms_role_perm(roleid,permcode)values(1,'020006');


insert into otppms_role_perm(roleid,permcode)values(1,'03');
insert into otppms_role_perm(roleid,permcode)values(1,'0300');
insert into otppms_role_perm(roleid,permcode)values(1,'030001');
insert into otppms_role_perm(roleid,permcode)values(1,'030002');
insert into otppms_role_perm(roleid,permcode)values(1,'030003');
insert into otppms_role_perm(roleid,permcode)values(1,'030004');
insert into otppms_role_perm(roleid,permcode)values(1,'030005');
insert into otppms_role_perm(roleid,permcode)values(1,'030006');
insert into otppms_role_perm(roleid,permcode)values(1,'030007');
insert into otppms_role_perm(roleid,permcode)values(1,'030008');
insert into otppms_role_perm(roleid,permcode)values(1,'0302');
insert into otppms_role_perm(roleid,permcode)values(1,'030201');
insert into otppms_role_perm(roleid,permcode)values(1,'030202');
insert into otppms_role_perm(roleid,permcode)values(1,'030203');
insert into otppms_role_perm(roleid,permcode)values(1,'030204');
insert into otppms_role_perm(roleid,permcode)values(1,'030205');
insert into otppms_role_perm(roleid,permcode)values(1,'0303');
insert into otppms_role_perm(roleid,permcode)values(1,'030301');
insert into otppms_role_perm(roleid,permcode)values(1,'030302');
insert into otppms_role_perm(roleid,permcode)values(1,'030303');
insert into otppms_role_perm(roleid,permcode)values(1,'030304');

insert into otppms_role_perm(roleid,permcode)values(1,'05');
insert into otppms_role_perm(roleid,permcode)values(1,'0500');	
insert into otppms_role_perm(roleid,permcode)values(1,'050003');	

insert into otppms_role_perm(roleid,permcode)values(1,'06');
insert into otppms_role_perm(roleid,permcode)values(1,'0600');
insert into otppms_role_perm(roleid,permcode)values(1,'060001');
insert into otppms_role_perm(roleid,permcode)values(1,'060002');
insert into otppms_role_perm(roleid,permcode)values(1,'060003');
	
																																
update otppms_role_perm set roleid=(select roleid from otppms_roleinfo where rolemark='ADMIN') where roleid = 1;

-- insert for windows agentconf
insert into otppms_agentconf (confname, type, userformat, localprotect, remoteprotect, uacprotect, unboundlogin) values ('windows-logon-config', 0, 1, 0, 3, 0, 0);
-- insert for linux agentconf
insert into otppms_agentconf (confname, type, userformat, localprotect, remoteprotect, uacprotect, unboundlogin) values ('linux-pam-config', 1, 1, 0, 0, 0, 0);

-- insert demo license
insert into otppms_licinfo (licid, licinfo, lictype, issuer, customerid, licstate, licupdatetime) values ('oBHr5L5gjJumsHGWIc2lPA==', 'gs426CJhP9HrTZfbf/Ki6FY6l8KrV2esiGWXGc5tI4U6VKY+bMVZkxFblzphsh8D2jNXzA0WbnaXVJ5KlrmYj1CLIFEhs/cde07gZ/qPeWFdFmyNgchuKhmNX90CveG8doiaJXs1fvhPHFunegaaiNiCk3fbPnFKoFTQKWvZ8KTOlKOC9COMvsZAShyw3JZ24QCDgkI/Or/zOTQ4/wHYSm5nOgMSoH/y9a6OUYKxmNc3p31SinAlhp6KKKOgy97U7XDnKqb/GxXizdARxZ4Siu0e7bUlkaUeo5rq0ppAyngxD85OMAUaTio+p1Lo1KCrNr7yZyGZUJTonXrHazOicL8C33Woc0o6Yi2u0PJSnbJiNcz4HBeIPXscXSizzq522DaB/Llm9Z5aIu3D9diuubkGNEHe32IqFmIOAti24NMZFosE6tIjUblcVMxM3d491Dso6SUQ39ZoOl0VYun5G/ljernMpcTew4KG2E9BSMmEAoBZ3lr08IPKWlgbLZFeJMexUT+Ck76OHSO/tJgKBdibfP1w6OOpxwUIXGPZi/3uXrbMYlLb7w==', 0, 'FeiTian Beijing CHINA', 'EITD001', 1, 1380000000);
-- insert host of 127.0.0.1
insert into otppms_hostinfo (hostipaddr, hostname, confid, priority, licid, ftradiusenabled, protocoltype, authport, syncport, radiusenabled, radauthport, soapenabled, soapport, webservicename) values ('127.0.0.1', 'localhost', 0, 1, 'oBHr5L5gjJumsHGWIc2lPA==', 1, 'udp', 1915, 1916, 1, 1812, 1, 18081, 'otpwebservice');


