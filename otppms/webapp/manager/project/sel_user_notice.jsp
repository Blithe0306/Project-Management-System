<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
%>
<html>
  <head>
    <title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
	<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
	<link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
	    
    <script type="text/javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/datepicker/WdatePicker.js"></script>	
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>	
 	<script type="text/javascript" src="<%=path%>/manager/project/js/sel_user_notice.js"></script>	
	<script language="javascript" type="text/javascript">
		// Start,多语言提取
		var operator_lang = '<view:LanguageTag key="log_info_operator"/>';
		var oper_time_lang = '<view:LanguageTag key="log_info_oper_time"/>';
		var client_ip_lang = '<view:LanguageTag key="log_info_client_ip"/>';
		var actionid_lang = '<view:LanguageTag key="log_actionid"/>';
		var log_info_lang = '<view:LanguageTag key="log_info"/>';
		var operate_result_lang = '<view:LanguageTag key="log_info_operate_result"/>';
		var detail_info_lang = '<view:LanguageTag key="common_syntax_detail_info"/>';
		var langSucc = '<view:LanguageTag key="common_syntax_success"/>';
		var langFailure = '<view:LanguageTag key="common_syntax_failure"/>';
		var langDetails = '<view:LanguageTag key="common_syntax_details"/>';
		var detail_info_lang = '<view:LanguageTag key="common_syntax_detail_info"/>';
		var has_child_lang = '<view:LanguageTag key="admin_sel_user_has_child"/>';
		var syntax_confirm_lang = '<view:LanguageTag key="common_syntax_confirm"/>';
		// End,多语言提取
		
	//-->
	</script>
  </head> 
  <body scroll="no">
	<input id="contextPath" type="hidden" value="<%=path%>" />
	<input id="l_userid" type="hidden" value="${curLoginUser}" />
	<input id="curPage" type="hidden" value="${param.cPage}" />
	<input id="orderid" type="hidden" value="${param.orderid}" />
	<div id="listDataAJAX"></div>
  </body>
</html>