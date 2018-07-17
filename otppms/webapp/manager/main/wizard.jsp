<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld"%>
<%
    String path = request.getContextPath();
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title></title>
		<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css" />
		<link type="text/css" rel="stylesheet" href="css/wizard.css" />
		<script type="text/javascript" src="js/wizard.js"></script>
		<script language="javascript" type="text/javascript">
			//添加Tab页面
			function addTab(key) {
				var pwin = window.parent;
				if (key == '010301') {
					pwin.f_addTab('1', this ,'010301', '<view:LanguageTag key="common_menu_admin_add_admrole"/>', '<%=path%>/manager/admin/role/add.jsp?source=top');
				} else if (key == '010101') {
					pwin.f_addTab('1', this ,'010101', '<view:LanguageTag key="common_menu_admin_add"/>', '<%=path%>/manager/admin/user/add.jsp?source=top');
				} 
				else if (key == '020101') {
					pwin.f_addTab('2', this ,'020101', '<view:LanguageTag key="common_menu_user_add"/>', '<%=path%>/manager/user/userinfo/add.jsp?source=top');
				} else if (key == '0202') {
					pwin.f_addTab('2', this ,'0202', '<view:LanguageTag key="common_menu_user_import"/>', '<%=path%>/manager/user/userinfo/import_user.jsp');
				} else if (key == '020104') {
					pwin.f_addTab('2', this ,'020104', '<view:LanguageTag key="common_menu_user_batch_bind"/>', '<%=path%>/manager/user/userinfo/m_m_bind.jsp');
				}
				else if (key == '0302') {
					pwin.f_addTab('3', this ,'0302', '<view:LanguageTag key="common_menu_tkn_import"/>', '<%=path%>/manager/token/import/import.jsp');
				}
				else if (key == '040002') {
					pwin.f_addTab('4', this ,'040002', '<view:LanguageTag key="common_menu_auth_add_server"/>', '<%=path%>/manager/authmgr/server/add.jsp');
				}else if (key == '040102') {
					pwin.f_addTab('4', this ,'040102', '<view:LanguageTag key="common_menu_auth_add_proxy"/>', '<%=path%>/manager/authmgr/agent/add.jsp');
				}else if (key == '040101') {
					pwin.f_addTab('4', this ,'040101', '<view:LanguageTag key="common_menu_auth_proxy_list"/>', '<%=path%>/manager/authmgr/agent/list.jsp');
				}else if (key == '040202') {
					pwin.f_addTab('4', this ,'040202', '<view:LanguageTag key="common_menu_auth_add_backend"/>', '<%=path%>/manager/authmgr/backend/add.jsp');
				}
				else if (key == '050203') {
					pwin.f_addTab('5', this ,'050203', '<view:LanguageTag key="common_menu_config_usersource"/>', '<%=path%>/manager/confinfo/usersource/list.jsp');
				}
				else if (key == '0601') {
					pwin.f_addTab('6', this ,'0601', '<view:LanguageTag key="common_menu_log_user"/>', '<%=path%>/manager/logs/userlog/list.jsp');
				}
				else if (key == '08') {
					pwin.f_addTab('8', this ,'08', '<view:LanguageTag key="common_title_orgunit"/>', '<%=path%>/manager/main/orgunitInfo!view.action?treeOrgunitInfo.id=&treeOrgunitInfo.flag=1&treeOrgunitInfo.readWriteFlag=2');
				}
			}
			
		</script>
	</head>

	<body>
	<div id="index-splash-block" class="index-splash-block" style="width:95%">
		<div id="feature-slide-block" class="feature-slide-block">
			<div class="feature-slide-preview" style="display: none; ">
				<h3><view:LanguageTag key="using_wizard_title"/></h3>
				<h3><view:LanguageTag key="using_wizard_tip"/></h3>
			</div>
			<div class="feature-slide-preview" style="display: none; ">
				<h3><view:LanguageTag key="lic_update_file"/></h3>
				<p><view:LanguageTag key="using_wizard_update_lic_tip"/><a href="javascript:upLicFile();"><view:AdminPermTag key="000002" path="<%=path%>" langKey="lic_update_file" type="0"/></a></p>
			</div>
			<div class="feature-slide-preview" style="display: none; ">
				<h3><view:LanguageTag key="org_add_child_org"/></h3>
				<p><view:LanguageTag key="using_wizard_add_childorg_tip"/><view:LanguageTag key="colon"/></p>
				<p>— <view:LanguageTag key="common_menu_admin_add_admrole"/> <a href="javascript:addTab('010301');"><view:AdminPermTag key="010301" path="<%=path%>" langKey="role_info_add" type="0"/></a></p>
				<p>— <view:LanguageTag key="common_menu_admin_add"/> <a href="javascript:addTab('010101');"><view:AdminPermTag key="010101" path="<%=path%>" langKey="admin_info_add" type="0"/></a></p>
				<p>— <view:LanguageTag key="using_wizard_add_childorg_tip2"/> <a href="javascript:addTab('08');"><view:AdminPermTag key="08" path="<%=path%>" langKey="org_add_child_org" type="0"/></a></p>
				<p>— <view:LanguageTag key="using_wizard_add_childorg_tip3"/></p>
			</div>
			<div class="feature-slide-preview" style="display: none; ">
				<h3><view:LanguageTag key="tkn_import_token"/></h3>
				<p><view:LanguageTag key="using_wizard_import_tkn_tip"/><a href="javascript:addTab('030009');"><view:AdminPermTag key="030009" path="<%=path%>" langKey="common_menu_tkn_import" type="0"/></a></p>
			</div>
			<div class="feature-slide-preview" style="display: none; ">
				<h3><view:LanguageTag key="using_wizard_add_bind_tip"/></h3>
				<p>— <view:LanguageTag key="using_wizard_add_user_tip"/> <a href="javascript:addTab('020001');"><view:AdminPermTag key="020001" path="<%=path%>" langKey="user_info_add" type="0"/></a></p>
				<p>— <view:LanguageTag key="using_wizard_batch_import_utkn_tip"/><a href="javascript:addTab('020004');"><view:AdminPermTag key="020004" path="<%=path%>" langKey="user_import_title_user" type="0"/></a></p>
				<p>— <view:LanguageTag key="using_wizard_import_ad_user_tip"/><a href="javascript:addTab('050203');"><view:AdminPermTag key="050203" path="<%=path%>" langKey="common_menu_config_usersource" type="0"/></a><view:LanguageTag key="using_wizard_batch_bind_tkn"/><a href="javascript:addTab('020006');"><view:AdminPermTag key="020006" path="<%=path%>" langKey="common_menu_user_batch_bind" type="0"/></a><view:LanguageTag key="using_wizard_portal_bind_tkn"/></p>
			</div>
			<div class="feature-slide-preview" style="display: none; ">
				<h3><view:LanguageTag key="using_wizard_auth_title"/></h3>
				<p><view:LanguageTag key="using_wizard_auth_tip"/></p>
				<p>— <view:LanguageTag key="using_wizard_auth_tip2"/><a href="javascript:addTab('040002');"><view:AdminPermTag key="040002" path="<%=path%>" langKey="common_menu_auth_add_server" type="0"/></a></p>
				<p>— <view:LanguageTag key="using_wizard_auth_tip3"/> <a href="javascript:addTab('040202');"><view:AdminPermTag key="040202" path="<%=path%>" langKey="common_menu_auth_add_backend" type="0"/></a></p>
			</div>
			<div class="feature-slide-preview" style="display: none; ">
				<h3><view:LanguageTag key="using_wizard_agent_title"/></h3>
				<p><view:LanguageTag key="using_wizard_agent_tip"/><a href="javascript:addTab('040102');"><view:AdminPermTag key="040102" path="<%=path%>" langKey="common_menu_auth_add_proxy" type="0"/></a></p>
				<p><view:LanguageTag key="using_wizard_agent_tip2"/><a href="javascript:addTab('040101');"><view:AdminPermTag key="040101" path="<%=path%>" langKey="using_wizard_export_conf_file" type="0"/></a></p>
			</div>
			<div class="feature-slide-preview" style="display: none; ">
				<h3><view:LanguageTag key="using_wizard_auth_test"/></h3>
				<p><view:LanguageTag key="using_wizard_auth_test_tip"/></p>
				<p><view:LanguageTag key="using_wizard_auth_test_tip2"/><a href="javascript:addTab('0601');"><view:AdminPermTag key="0601" path="<%=path%>" langKey="common_menu_log_user" type="0"/></a></p>
			</div>
			
			<div id="feature-slide-list" class="feature-slide-list">
				<a href="#" id="feature-slide-list-previous" class="feature-slide-list-previous"></a>
				<div id="feature-slide-list-items" class="feature-slide-list-items">
				</div>
				<a href="#" id="feature-slide-list-next" class="feature-slide-list-next"></a>
			</div>
		</div>
		<script type="text/javascript">
			initFeatureSlide();
		</script>
	</div>
	</body>
</html>
