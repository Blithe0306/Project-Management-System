<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld"%>
<%
	String path = request.getContextPath();
%>
<html>
<head>
	<title></title>
	<meta http-equiv="content-type" content="text/html;charset=utf-8" />
	<link type="text/css" href="<%=path%>/manager/common/css/menus.css" rel="stylesheet" />
	<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=path%>/manager/common/js/menu/menu.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script type="text/javascript">
   	$(document).ready(function(){	     
	    var admintext='<view:AdminPermTag  key="01" path="<%=path%>" langKey="common_title_admin"   type="0" />';
	    var usertext='<view:AdminPermTag   key="02" path="<%=path%>" langKey="common_title_user"    type="0" />';
	    var tokentext='<view:AdminPermTag  key="03" path="<%=path%>" langKey="common_title_token"   type="0" />';
	    var config='<view:AdminPermTag  key="05" path="<%=path%>" langKey="common_title_config"   type="0" />';
	    var logstext='<view:AdminPermTag  key="06" path="<%=path%>" langKey="common_title_logs"   type="0" />';
	    
	    if(admintext==''){
	       $('#admin').hide();
	    }if(usertext==''){
	       $('#customer').hide();
	    }if(tokentext==''){
	       $('#projectmgr').hide();
	    }if(logstext==''){
	       $('#log').hide();
	    }if(config==''){
	       $('#config').hide();
	    }
	    
	  });

   </script>
   <style type="text/css">
		div#menu {
			margin: 1px 0 0 0px;
			position: absolute;
		}
		.noHand {
			cursor: default;
		}
   </style>
</head>
<body>
	<div id="menu">
		<ul class="menu">
			<li id="home">
				<a class="parent" href="javascript:f_addTab('0', this,'home', '<view:LanguageTag key="common_title_home"/>', '<%=path%>/manager/main/view.jsp');">
						<span><view:LanguageTag key="common_title_home"/></span>
				</a>
			</li>
				
			<%--
				<li id="orgunit">
					<a href="javascript:void(0)" onClick="f_addTab('8', this ,'08', '<view:LanguageTag key="common_title_orgunit"/>', '<%=path%>/manager/main/orgunitInfo!view.action?treeOrgunitInfo.id=&treeOrgunitInfo.flag=1&treeOrgunitInfo.readWriteFlag=2');" class="parent">
						<span><view:AdminPermTag key="08" path="<%=path%>" langKey="common_title_orgunit" type="0" /></span>
					</a>				
				</li>
			--%>
			<li id="admin">
				<a href="javascript:void(0)" class="parent">
					<span class="noHand"><view:AdminPermTag key="01" path="<%=path%>" langKey="common_title_admin" type="0" /></span>
				</a>
				<div>
				<ul>
					<li>
						<a href="javascript:void(0)" class="jiantou2">
							<span class="noHand"><view:AdminPermTag key="0100" path="<%=path%>" langKey="common_title_admin" type="0" /></span>
						</a>
						<div>
							<ul>
							<li>
								<a href="javascript:void(0)" onClick="f_addTab('1', this ,'0100', '<view:LanguageTag key="common_menu_admin_list"/>', '<%=path%>/manager/admin/user/list.jsp');" class="jiantou2"> 
									<span><view:AdminPermTag key="0100" path="<%=path%>" langKey="common_menu_admin_list" type="0" /></span> 
								</a>
							</li>
							<li>
								<a href="javascript:void(0)" onClick="f_addTab('1', this ,'010101', '<view:LanguageTag key="common_menu_admin_add"/>', '<%=path%>/manager/admin/user/add.jsp?source=top');" class="jiantou2"> 
									<span><view:AdminPermTag key="010101" path="<%=path%>" langKey="common_menu_admin_add" type="0" /></span> 
								</a>
							</li>
							</ul>
						</div>
					</li>
					
					<li>
						<a href="javascript:void(0)" class="jiantou2"> 
							<span class="noHand"><view:AdminPermTag key="0102" path="<%=path%>" langKey="common_menu_admin_role" type="0" /></span> 
						</a>
						<div>
						<ul>
							<li>
								<a href="javascript:void(0)" onClick="f_addTab('1', this ,'0102', '<view:LanguageTag key="common_menu_admin_role_list"/>', '<%=path%>/manager/admin/role/list.jsp');" class="jiantou2"> 
									<span><view:AdminPermTag key="0102" path="<%=path%>" langKey="common_menu_admin_role_list" type="0" /></span> 
								</a>
							</li>
							<li>
								<a href="javascript:void(0)" onClick="f_addTab('1', this ,'010301', '<view:LanguageTag key="common_menu_admin_add_admrole"/>', '<%=path%>/manager/admin/role/add.jsp?source=top');" class="jiantou2"> 
									<span><view:AdminPermTag key="010301" path="<%=path%>" langKey="common_menu_admin_add_role" type="0" /></span> 
								</a>
							</li>
						</ul>
						</div>
					</li>
							
				</ul>
				</div>
			</li>
			
	<li id="customer">
		<a class="parent" href="javascript:void(0)" >
			<span class="noHand"><view:AdminPermTag key="02" path="<%=path%>" langKey="common_menu_customer" type="0" /></span> 
		</a>
		<div>
			<ul>
				<li>
					<a href="javascript:void(0)" onClick="f_addTab('2', this ,'020001', '客户列表', '<%=path%>/manager/customer/list.jsp');" class="jiantou2"> 
						<span><view:AdminPermTag key="020001" path="<%=path%>" langKey="common_menu_customer_list" type="0" /></span> 
					</a>
					<a href="javascript:void(0)" onClick="f_addTab('2', this ,'020002', '添加客户', '<%=path%>/manager/customer/custInfo!toAddCustomer.action');" class="jiantou2"> 
						<span><view:AdminPermTag key="020002" path="<%=path%>" langKey="common_menu_customer_add" type="0" /></span> 
					</a>
				</li>
			</ul>
		</div>
	</li>
			
	<li id="projectmgr">
		<a href="javascript:void(0)" class="parent">
			<span class="noHand"><view:AdminPermTag key="03" path="<%=path%>" langKey="common_menu_projectmgr" type="0" /></span>
		</a>
		<div>
		<ul>
			<li>
				<a href="javascript:void(0)" class="jiantou2">
					<span class="noHand"><view:AdminPermTag key="0300" path="<%=path%>" langKey="common_menu_projectmgr_prj" type="0" /></span>
				</a>
				<div>
					<ul>
					<li>
						<a href="javascript:void(0)" onClick="f_addTab('3', this ,'030001', '定制项目列表', '<%=path%>/manager/project/list.jsp');" class="jiantou2"> 
							<span><view:AdminPermTag key="030001" path="<%=path%>" langKey="common_menu_projectmgr_prj_list" type="0" /></span> 
						</a>
					</li>
					<li>
						<a href="javascript:void(0)" onClick="f_addTab('3', this ,'030002', '添加定制项目', '<%=path%>/manager/project/projectAction!toAddProject.action');" class="jiantou2"> 
							<span><view:AdminPermTag key="030002" path="<%=path%>" langKey="common_menu_projectmgr_prj_add" type="0" /></span> 
						</a>
					</li>
					</ul>
				</div>
			</li>
			
			<li>
				<a href="javascript:void(0)" class="jiantou2"> 
					<span class="noHand"><view:AdminPermTag key="0302" path="<%=path%>" langKey="common_menu_projectmgr_info" type="0" /></span> 
				</a>
				<div>
				<ul>
					<li>
						<a href="javascript:void(0)" onClick="f_addTab('3', this ,'030201', '定制信息列表', '<%=path%>/manager/prjinfo/list.jsp');" class="jiantou2"> 
							<span><view:AdminPermTag key="030201" path="<%=path%>" langKey="common_menu_projectmgr_info_list" type="0" /></span> 
						</a>
					</li>
					<li>
						<a href="javascript:void(0)" onClick="f_addTab('3', this ,'030202', '添加定制信息', '<%=path%>/manager/prjinfo/prjinfoAction!toAddPrjinfo.action');" class="jiantou2"> 
							<span><view:AdminPermTag key="030202" path="<%=path%>" langKey="common_menu_projectmgr_info_add" type="0" /></span> 
						</a>
					</li>
				</ul>
				</div>
			</li>
			<li>
				<a href="javascript:void(0)" class="jiantou2"> 
					<span class="noHand"><view:AdminPermTag key="0303" path="<%=path%>" langKey="common_menu_projectmgr_rds" type="0" /></span> 
				</a>
				<div>
					<ul>
						<li>
							<a href="javascript:void(0)" onClick="f_addTab('3', this ,'030301', '上门记录列表', '<%=path%>/manager/resords/list.jsp');" class="jiantou2"> 
								<span><view:AdminPermTag key="030301" path="<%=path%>" langKey="common_menu_projectmgr_rds_list" type="0" /></span> 
							</a>
						</li>
						<li>
							<a href="javascript:void(0)" onClick="f_addTab('3', this ,'030302', '添加上门记录', '<%=path%>/manager/resords/resordsAction!toAddResords.action');" class="jiantou2"> 
								<span><view:AdminPermTag key="030302" path="<%=path%>" langKey="common_menu_projectmgr_rds_add" type="0" /></span> 
							</a>
						</li>
					</ul>
				</div>
			</li>
		</ul>
		</div>
	</li>
	
	<li id="config">
				<a class="parent">
					<span class="noHand"><view:AdminPermTag key="05" path="<%=path%>" langKey="common_title_config" type="0" /></span>
				</a>
				<div>
				<ul>
				<li>
				<a href="javascript:void(0)" class="jiantou2"> 
					<span class="noHand"><view:AdminPermTag key="0500" path="<%=path%>" langKey="common_menu_config_sys_comm" type="0" /></span> 
				</a>
				<div>
				<ul>
					<li>
						<a href="javascript:void(0)" onClick="f_addTab('5', this ,'050003', '<view:LanguageTag key="common_menu_config_email"/>', '<%=path%>/manager/confinfo/email/list.jsp');" class="jiantou2"> 
							<span><view:AdminPermTag key="050003" path="<%=path%>" langKey="common_menu_config_email" type="0" /></span>
						</a>
					</li>
				</ul>
				</div>
				</li>
			</ul>
		</div>
	</li>
				

	<li class="last" id="log">
		<a class="parent" href="javascript:void(0)" >
			<span class="noHand"><view:AdminPermTag key="06" path="<%=path%>" langKey="common_title_logs" type="0" /></span> 
		</a>
		<div>
			<ul>
				<li>
					<a href="javascript:void(0)" onClick="f_addTab('6', this ,'0600', '<view:LanguageTag key="common_menu_log_admin"/>', '<%=path%>/manager/logs/adminlog/list.jsp');" class="jiantou2"> 
						<span><view:AdminPermTag key="0600" path="<%=path%>" langKey="common_menu_log_admin" type="0" /></span> 
					</a>
				</li>
			</ul>
		</div>
	</li>
	</ul>
	</div>
	<div id="copyright"><a href="http://apycom.com/"></a></div>
</body>
</html>