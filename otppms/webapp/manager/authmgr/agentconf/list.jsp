<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>

<%
	String path = request.getContextPath();
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
	<script type="text/javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/authmgr/agentconf/js/list.js"></script>
 	<script language="javascript" type="text/javascript">
 		// Start,多语言提取
		var langNoprotect = '<view:LanguageTag key="auth_conf_no_protect"/>';
		var langProLocal = '<view:LanguageTag key="auth_conf_protect_local_account"/>';
		var langProDomain = '<view:LanguageTag key="auth_conf_protect_domain_account"/>';
		var langProLocDomain = '<view:LanguageTag key="auth_conf_pro_local_domain_account"/>';
		var langVdnosup = '<view:LanguageTag key="auth_conf_vd_not_support"/>';
		var langHandPro = '<view:LanguageTag key="auth_conf_hand_protection"/>';
		var langYes = '<view:LanguageTag key="common_syntax_yes"/>';
		var langNo = '<view:LanguageTag key="common_syntax_no"/>';
		
		// 列表
		var conf_name_lang = '<view:LanguageTag key="auth_conf_name"/>';
		var conf_agent_type_lang = '<view:LanguageTag key="auth_conf_agent_type"/>';
		var conf_uname_format_lang = '<view:LanguageTag key="auth_conf_uname_format"/>';
		var local_protect_lang = '<view:LanguageTag key="auth_conf_local_protect"/>';
		var remote_protect_lang = '<view:LanguageTag key="auth_conf_remote_protect"/>';
		var uac_protect_lang = '<view:LanguageTag key="auth_conf_uac_protect"/>';
		var unbound_login_lang = '<view:LanguageTag key="auth_conf_unbound_login"/>';
		var syntax_operation_lang = '<view:LanguageTag key="common_syntax_operation"/>';
		
		// 列表
		var linux_login_lang = '<view:LanguageTag key="auth_conf_linux_login_pro"/>';
		var win_login_lang = '<view:LanguageTag key="auth_conf_win_login_pro"/>';
		var need_del_date_lang = '<view:LanguageTag key="common_syntax_check_need_del_date"/>';
		var del_sel_date_lang = '<view:LanguageTag key="common_syntax_confirm_del_sel_date"/>';
		var syntax_confirm_lang = '<view:LanguageTag key="common_syntax_confirm"/>';
		var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
		// End,多语言提取
 		
		var permEdit = '';//编辑权限
	    $(function(){
	      	permEdit = '<view:AdminPermTag key="040303" path="<%=path%>" langKey="common_syntax_edit" type="1" />';
	    });
	
	</script>
  </head>
  <body scroll="no" style="overflow:hidden">
  <div id="background"  class="background"  style="display: none; "></div>
  <div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
  <input type="hidden" name="contextPath" value="<%=path%>" id="contextPath"/>
  <input id="curPage" type="hidden" type="text" value="${param.cPage}" />
	<form name="ListForm" method="post" action="">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="operTableBg">
		  <tr>
		    <td width="99%">
		<!--  <a href="javascript:addAgentConf()" class="isLink_HeiSe"><view:AdminPermTag key="040302" path="<%=path%>" langKey="common_syntax_add" type="1" /></a> -->
		      <span style="float:left">&nbsp;&nbsp;</span>		
			  <view:AdminPermTag key="040304" path="<%=path%>" langKey="common_syntax_delete" type="2" />
		    </td>
		  </tr>
		</table>
	</form>
    <div id="listDataAJAX"></div>
  </body>
</html>