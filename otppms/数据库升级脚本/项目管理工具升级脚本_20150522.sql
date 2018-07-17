
--升级脚本内容：
--1、监视人功能，发送邮件
--2、配置管理-邮件功能
--3、项目总结功能
--4、上门记录
--5、修改Bug
-- config
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('05', '', '','', '');

-- sys common config
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('0500', '', '','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('050003', '/manager/confinfo/email/email!init.action', '','', '');


insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030007', '', '<img src="<%=path%>/images/icon/email_add.png" width="16" height="16" hspace="2" border="0">','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030008', '', '<img src="<%=path%>/images/icon/email_attach.png" width="16" height="16" hspace="2" border="0">','', '');

insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030205', '', '<img src="<%=path%>/images/icon/email_add.png" width="16" height="16" hspace="2" border="0">','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('0303', '', '','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030301', '', '','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030302', '', '','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030303', '', '<img src="<%=path%>/images/icon/file_edit.gif" width="16" height="16" hspace="2" border="0">','', '');
insert into otppms_perminfo(permcode, permlink,srcname,keymark, descp) values('030304', '', '<a href="javascript:delData();" style="display:inline-block" class="button"><span></span></a>','', '');

insert into otppms_role_perm(roleid,permcode)values(1,'030007');
insert into otppms_role_perm(roleid,permcode)values(1,'030008');
insert into otppms_role_perm(roleid,permcode)values(1,'030205');
insert into otppms_role_perm(roleid,permcode)values(1,'0303');
insert into otppms_role_perm(roleid,permcode)values(1,'030301');
insert into otppms_role_perm(roleid,permcode)values(1,'030302');
insert into otppms_role_perm(roleid,permcode)values(1,'030303');
insert into otppms_role_perm(roleid,permcode)values(1,'030304');
insert into otppms_role_perm(roleid,permcode)values(1,'05');
insert into otppms_role_perm(roleid,permcode)values(1,'0500');	
insert into otppms_role_perm(roleid,permcode)values(1,'050003');	

drop table if exists otppms_cutomer_user;
CREATE TABLE otppms_cutomer_user (
  Id int(11) NOT NULL AUTO_INCREMENT,
  userid varchar(64) DEFAULT NULL ,
  projectid varchar(255) DEFAULT NULL ,
  useremail varchar(255) DEFAULT NULL ,
  PRIMARY KEY (Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists otppms_project_result;
CREATE TABLE otppms_project_result (
  Id int(11) NOT NULL AUTO_INCREMENT,
  prjid varchar(64) DEFAULT NULL,
  results text ,
  operator varchar(255) DEFAULT NULL,
  createtime varchar(255) DEFAULT NULL,
  PRIMARY KEY (Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists otppms_record_back;
CREATE TABLE otppms_record_back (
  Id int(11) NOT NULL AUTO_INCREMENT,
  recordtime int(11) DEFAULT NULL,
  endrecordtime int(11) DEFAULT NULL,
  recordUser varchar(512) DEFAULT NULL,
  cutomer varchar(255) DEFAULT NULL,
  reason text,
  results text,
  operator varchar(255) DEFAULT NULL,
  createtime varchar(255) DEFAULT NULL,
  prjid varchar(1024) DEFAULT NULL,
  remark varchar(4096) DEFAULT NULL,
  PRIMARY KEY (Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


