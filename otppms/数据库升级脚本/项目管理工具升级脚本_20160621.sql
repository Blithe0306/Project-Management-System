
--升级脚本内容：
--1、客户列表的操作列中添加定制项目的操作
--2、定制项目列表的操作列中添加定制信息的操作
-- config
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030006', '', '<img src="<%=path%>/images/icon/radius_set.png" width="16" height="16" hspace="2" border="0">','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('020006', '', '<img src="<%=path%>/images/icon/radius_set.png" width="16" height="16" hspace="2" border="0">','', '');

insert into otppms_role_perm(roleid,permcode)values(1,'030006');
insert into otppms_role_perm(roleid,permcode)values(1,'020006');



