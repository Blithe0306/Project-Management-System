
-- upgrade sql version
update otppms_configinfo set confvalue='4.0.1' where confname='sqlsversion' and conftype='common';

-- upgrade img
update otppms_perminfo set srcname ='<img src="<%=path%>/images/icon/error_go.png" width="16" height="16" hspace="2"  border="0">' where permcode='010105';
update otppms_perminfo set srcname ='<img src="<%=path%>/images/icon/key_go.png" width="16" height="16" hspace="2"  border="0">' where permcode='010107';

update otppms_perminfo set srcname ='<img src="<%=path%>/images/icon/error_go.png" width="16" height="16" hspace="2"  border="0" >' where permcode='020109';
update otppms_perminfo set srcname ='<img src="<%=path%>/images/icon/folder_user.png" width="16" height="16" hspace="2"  border="0" >' where permcode='020111';

update otppms_perminfo set srcname ='<img src="<%=path%>/images/icon/error_go.png" width="16" height="16" hspace="2"   border="0">' where permcode='030101';
update otppms_perminfo set srcname ='<img src="<%=path%>/images/icon/label_edit.gif" width="16" height="16" hspace="2"  border="0">' where permcode='030110';

update otppms_perminfo set srcname ='<img src="<%=path%>/images/icon/link_go.png" width="16" height="16" hspace="2"  border="0">' where permcode='030112';
update otppms_perminfo set srcname ='<img src="<%=path%>/images/icon/link_break.png" width="16" height="16" hspace="2"  border="0">' where permcode='030113';

update otppms_perminfo set srcname ='<img src="<%=path%>/images/icon/drive_delete.png" width="16" height="16" hspace="2"   border="0">' where permcode='040106';
update otppms_perminfo set srcname ='<img src="<%=path%>/images/icon/drive_add.png" width="16" height="16" hspace="2"   border="0">' where permcode='040108';

update otppms_perminfo set srcname ='<img src="<%=path%>/images/icon/error_go.png" width="16" height="16" hspace="2"  border="0">' where permcode='040205';


-- insert radius request send smsotp password
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('sms_token_req_more_attr','token', 'State', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('sms_token_req_more_attr_val', 'token','get', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('sms_token_req_send_before_check', 'token','1', 0, '');
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('sms_token_req_return_code_domain', 'token','11', 0, '');


-- data bak
insert into otppms_configinfo (confname, conftype, confvalue, parentid, descp) values ('log_is_bak', 'common', '0', 0, '');
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


-- upgrade adminperm --------------------------------------------------------
-- home
update otppms_perminfo set permlink ='/manager/lic/license!find.action' where permcode = '000001';

-- admin

-- admin user

-- admin role

-- User

-- user info
update otppms_perminfo set permlink ='/manager/user/userinfo/userInfo!init.action' where permcode ='0200';
update otppms_perminfo set permlink ='/manager/user/userinfo/userInfo!batchOper.action?oper=0' where permcode='020103';
update otppms_perminfo set permlink ='/manager/user/userinfo/userInfo!unBindUT.action,/manager/user/userinfo/userInfo!batchOper.action?oper=1' where permcode='020105';
update otppms_perminfo set permlink ='/manager/user/userinfo/userInfo!bindChangeTkn.action' where permcode ='020106';

update otppms_perminfo set permlink ='/manager/user/userinfo/userInfo!editUserLost.action,/manager/user/userinfo/userInfo!batchOper.action?oper=3,/manager/user/userinfo/userInfo!batchOper.action?oper=4' where permcode ='020108';
update otppms_perminfo set permlink ='/manager/user/userinfo/userInfo!editUserEnabled.action,/manager/user/userinfo/userInfo!batchOper.action?oper=5,/manager/user/userinfo/userInfo!batchOper.action?oper=6' where permcode ='020109';
update otppms_perminfo set permlink ='/manager/user/userinfo/userInfo!batchOper.action?oper=2' where permcode ='020110';
update otppms_perminfo set permlink ='/manager/user/userinfo/userChange!changeUser.action' where permcode ='020111';
update otppms_perminfo set permlink ='/manager/user/userinfo/userInfo!batchOper.action?oper=7' where permcode ='020112';
update otppms_perminfo set permlink ='/manager/user/userinfo/userInfo!batchOper.action?oper=8' where permcode ='020113';

-- token info
update otppms_perminfo set permlink = '/manager/token/token!init.action' where permcode ='0300';
update otppms_perminfo set permlink = '/manager/token/token!modifyBatch.action?oper=0,/manager/token/token!modifyBatch.action?oper=1,/manager/token/token!modify.action?operType=1' where permcode ='030101';
update otppms_perminfo set permlink = '/manager/token/token!modifyBatch.action?oper=2,/manager/token/token!modifyBatch.action?oper=3,/manager/token/token!modify.action?operType=3' where permcode ='030102';
update otppms_perminfo set permlink = '/manager/token/token!modifyBatch.action?oper=4,/manager/token/token!modifyBatch.action?oper=5,/manager/token/token!modify.action?operType=2' where permcode ='030103';
update otppms_perminfo set permlink = '/manager/token/token!modifyBatch.action?oper=6,/manager/token/token!modify.action?operType=4&sign=0' where permcode ='030104';
update otppms_perminfo set permlink = '/manager/token/token!modifyBatch.action?oper=8,/manager/token/token!modify.action?operType=4&sign=1' where permcode ='030105';
update otppms_perminfo set permlink = '/manager/token/authAction!tokenAuth.action' where permcode ='030106';
update otppms_perminfo set permlink = '/manager/token/authAction!tokenSync.action' where permcode ='030107';
update otppms_perminfo set permlink = '/manager/token/token!modifyBatch.action?oper=9' where permcode ='030109';

-- token dist
update otppms_perminfo set permlink = '/manager/token/distmanager/distManager!init.action' where permcode ='0304';
update otppms_perminfo set permlink = '/manager/token/distmanager/distManager!onLineDistribute.action,/manager/token/distmanager/distManager!offLineActivate.action' where permcode ='030402';
update otppms_perminfo set permlink = '/manager/token/distmanager/distManager!modify.action?oper=1' where permcode ='030403';

-- Auth Manager

-- server

-- agent
update otppms_perminfo set permlink = '/manager/authmgr/server/authAgent!selServer.action' where permcode ='040108';

-- backend

-- agentConf

-- config

-- sys common config
update otppms_perminfo set permlink = '/logout!logout.action' where permcode ='050001';

-- auth config

-- user config
update otppms_perminfo set permlink = '/manager/confinfo/config/userConfAction!modify.action?oper=utknconf' where permcode ='050201';
update otppms_perminfo set permlink = '/manager/confinfo/config/userConfAction!modify.action?oper=upwdconf' where permcode ='050202';

-- token config
update otppms_perminfo set permlink = '/manager/confinfo/config/tokenConfAction!modify.action?oper=softtkn' where permcode ='050301';
update otppms_perminfo set permlink = '/manager/confinfo/config/tokenConfAction!modify.action?oper=mobiletkn' where permcode ='050302';
update otppms_perminfo set permlink = '/manager/confinfo/config/tokenConfAction!modify.action?oper=smstkn' where permcode ='050303';
update otppms_perminfo set permlink = '/manager/confinfo/config/tokenConfAction!modify.action?oper=emeypin' where permcode ='050304';

-- center config
update otppms_perminfo set permlink = '/manager/confinfo/config/center!modify.action?oper=adminconf' where permcode ='050401';
update otppms_perminfo set permlink = '/manager/confinfo/config/center!modify.action?oper=trustip,/manager/confinfo/config/access!add.action' where permcode ='050402';
update otppms_perminfo set permlink = '/manager/confinfo/config/center!modify.action?oper=authser' where permcode ='050403';

-- protal config

-- Log

-- log admin

-- log user

-- Report

-- report operation

-- report user

-- report token

-- orgunit domain

-- orgunit unit
