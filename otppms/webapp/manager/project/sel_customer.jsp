<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
     <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
  	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
	 <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script language="javascript" src="<%=path%>/manager/project/js/sel_customer.js"></script>
	<script language="javascript" type="text/javascript">
		
		// Start,多语言提取
		var role_name_lang = '<view:LanguageTag key="admin_role_name"/>';
		var creator_lang = '<view:LanguageTag key="common_info_creator"/>';
		var sel_role_lang = '<view:LanguageTag key="admin_sel_role"/>';
		// End,多语言提取
	</script>	
  </head>
  
  <body scroll="no" style="overflow:auto; overflow-x:hidden">
	  <form name="ListForm" method="post" action="">
	   <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" style="margin:8px;">
		<tr>
		 <td>
		    <span class="query-font-css">客户名称<view:LanguageTag key="colon"/>
		    	<input type="text" id="custname" name="custInfo.custname" value="${custInfo.custname}" style="width:140px"/>&nbsp;&nbsp;
		    	<div style="display:none"><input type="text" id="roleT" name="custInfo.custname" class="formCss"/></div>  
		    <span style="display:inline-block" class="query-button-css"><a href="javascript:query();" class="isLink_LanSe"><view:LanguageTag key="common_syntax_query"/></a></span>
		 </td>
		</tr>
	   </table>
	  </form>
	  
 	<div id="listDataAJAX"></div>
  </body>
</html>