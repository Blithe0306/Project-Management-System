<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css">
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css">
     <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
    <script language="javascript" src="<%=path%>/manager/common/js/checkall.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
 	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js" />
 	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script language="javascript" src="<%=path%>/manager/user/userinfo/js/unbind_token.js"></script>
	<script language="javascript" type="text/javascript">
	
	// Start,多语言信息
	var langHardtkn = '<view:LanguageTag key="tkn_physical_hard"/>';
	var langMtkn = '<view:LanguageTag key="tkn_physical_mobile"/>';
	var langStkn = '<view:LanguageTag key="tkn_physical_soft"/>';
	var langSMStkn = '<view:LanguageTag key="tkn_physical_sms"/>';
	
	var tknum_lang = '<view:LanguageTag key="tkn_comm_tknum"/>';
	var type_lang = '<view:LanguageTag key="tkn_comm_type"/>';
	var physical_type_lang = '<view:LanguageTag key="tkn_comm_physical_type"/>';
	var length_lang = '<view:LanguageTag key="tkn_comm_length"/>';
	var change_cycle_lang = '<view:LanguageTag key="tkn_change_cycle"/>';
	
	var bound_tkn_lang = '<view:LanguageTag key="user_no_sel_bound_tkn"/>';
	var user_tkn_lang = '<view:LanguageTag key="user_sure_unbind_user_tkn"/>';
	var admin_tkn_lang = '<view:LanguageTag key="user_sure_unbind_admin_tkn"/>';
	var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
	// End,多语言信息
	
	</script>
  </head>
  
  <body style="overflow:hidden">
  <input id="currentPage" type="hidden" value="${param.currentPage}" />
  <input id="userId" type="hidden" value="${param.userId}" />
  <input id="domainId" type="hidden" value="${param.domainId}" />
  
  <form name="unbindForm" id="unbindForm" method="post" action="">
  </form>
  <div id="listDataAJAX"></div> 
 
  </body>
</html>