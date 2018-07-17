<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
	String flag = request.getParameter("flag");
	
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
    
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>    
    <script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/authmgr/agent/js/sel_list.js"></script>
	
	<script language="javascript" type="text/javascript">
		// Start,多语言提取
		var ser_hostip_lang = '<view:LanguageTag key="auth_ser_hostip"/>';
		var ser_hostname_lang = '<view:LanguageTag key="auth_ser_hostname"/>';
		var ser_priority_lang = '<view:LanguageTag key="auth_ser_priority"/>';
		var syntax_desc_lang = '<view:LanguageTag key="common_syntax_desc"/>';
		var priority_high_lang = '<view:LanguageTag key="auth_ser_priority_high"/>';
		var priority_ordinary_lang = '<view:LanguageTag key="auth_ser_priority_ordinary"/>';
		var priority_low_lang = '<view:LanguageTag key="auth_ser_priority_low"/>';
		var syntax_nothing_lang = '<view:LanguageTag key="common_syntax_nothing"/>';
		// End,多语言提取
		
		$(document).ready(function() {
		});
	
		//确定按钮
		function okClick(item,win,index) {
			var flag = $("#flag").val();
			var hostipArr='';
			var sRows = dataGrid.getSelectedRows(),
			sInfo = [];
			if(sRows.length<1){
			   FT.toAlert('warn','<view:LanguageTag key="auth_agent_sel_server"/>',null);
			   return;
			}
			if(flag != 1){
				for(var i=0,sl=sRows.length;i<sl;) {
					var ips = {},sRow = sRows[i++];
					ips.rid = sRow['hostipaddr'];
					ips.rname = sRow['hostipaddr'];
					sInfo.push(ips);
				}
				parent.confirmSelects(sInfo);
				if(win) win.close();
			}else{
				for(var i=0,sl=sRows.length;i<sl;) {
					var ips = {},sRow = sRows[i++];
					hostipArr  = hostipArr + sRow['hostipaddr'] +",";
				}
				parent.okServer(hostipArr);
			}
		}
	</script>
	
</head>
<body scroll="no" style="overflow:auto; overflow-x:hidden">
	<input id="contextPath" type="hidden" value="<%=path%>" />
	<input id="flag" type="hidden" value="<%=flag%>" />
	<input id="curPage" type="hidden" type="text" value="${param.cPage}" />
	<form name="SelForm" method="post" action="" id="SelForm">
		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" style="margin:8px;">
	      <tr>
	        <td>
	        <span class="query-font-css"><view:LanguageTag key='auth_ser_hostip'/><view:LanguageTag key="colon"/><input type="text" id="serverIPAddr" name="serverQueryForm.hostipaddr" value="${serverQueryForm.hostipaddr}" class="form-text"/>   
	        <span style="display:inline-block" class="query-button-css"><a href="javascript:query();" class="isLink_LanSe"><view:LanguageTag key="common_syntax_query"/></a></span>
	        </td>
	      </tr>
	    </table>
	</form>
	<div id="listDataAJAX"></div>
</body>
</html>