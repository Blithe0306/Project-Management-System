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
	<script type="text/javascript" src="<%=path%>/manager/confinfo/email/js/list.js"></script>
	
	<script language="javascript" type="text/javascript">
		// Start,多语言提取
		var langYes = '<view:LanguageTag key="common_syntax_yes"/>';
		var langNo = '<view:LanguageTag key="common_syntax_no"/>';
		var langSetdef = '<view:LanguageTag key="email_set_def_server"/>';
		var langEdit = '<view:LanguageTag key="common_syntax_edit"/>';
		
		// 列表
		var servname_lang = '<view:LanguageTag key="email_servname"/>';
		var host_lang = '<view:LanguageTag key="email_host"/>';
		var port_lang = '<view:LanguageTag key="email_port"/>';
		var account_lang = '<view:LanguageTag key="email_account"/>';
		var sender_lang = '<view:LanguageTag key="email_sender"/>';
		var def_server_lang = '<view:LanguageTag key="email_def_server"/>';
		var syntax_operation_lang = '<view:LanguageTag key="common_syntax_operation"/>';
		
		// 操作
		var need_del_date_lang = '<view:LanguageTag key="common_syntax_check_need_del_date"/>';
		var not_allow_del_lang = '<view:LanguageTag key="email_def_serv_not_allow_del"/>';
		var del_sel_date_lang = '<view:LanguageTag key="common_syntax_confirm_del_sel_date"/>';
		var syntax_confirm_lang = '<view:LanguageTag key="common_syntax_confirm"/>';
		var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
		var one_def_serv_lang = '<view:LanguageTag key="email_must_one_def_serv"/>';
		var set_def_serv_lang = '<view:LanguageTag key="email_confirm_set_def_serv"/>';
		// End,多语言提取
	</script>
  </head>
  
  <body scroll="no" style="overflow:hidden">
  	<input id="contextPath" type="hidden" value="<%=path%>" />
	<form name="ListForm" method="post" action="">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="operTableBg">
		  <tr>
		    <td>
		    	<span style="float:left">&nbsp;&nbsp;</span>
		        <a href="javascript:addMethod()" style="display:inline-block;" class="button"><span><view:LanguageTag key="common_syntax_add"/></span></a>
		        <a href="javascript:delMethod()" style="display:inline-block;" class="button"><span><view:LanguageTag key="common_syntax_delete"/></span></a>
		    </td>
		  </tr>
		</table>
	</form>
    <div id="listDataAJAX"></div>
  </body>
</html>