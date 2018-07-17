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
	<script language="javascript" src="<%=path%>/manager/orgunit/domain/js/sel_admin.js"></script>
	<script language="javascript" type="text/javascript">
	// Start,多语言提取
	var account_lang = '<view:LanguageTag key="admin_info_account"/>';
	var realname_lang = '<view:LanguageTag key="common_info_realname"/>';
	var please_sel_lang = '<view:LanguageTag key="admin_please_sel"/>';
	var enabled_lang = '<view:LanguageTag key="admin_orgunit_enabled"/>';
	var admin_info_lang = '<view:LanguageTag key="admin_info"/>';
	// End,多语言提取
	
	$(document).ready(function(){
		// 回车完成查询
		$("#adminId").keydown(function(e){
			if(e.keyCode == 13) {
				query(false);
			}
		});
	});
	</script>
  </head>
  
  <body scroll="no" style="overflow:auto; overflow-x:hidden">
  <form name="ListForm" method="post" action="">
  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" style="margin:8px;">
      <tr>
        <td width="20%" align="right">
        	<view:LanguageTag key="admin_info_account"/><view:LanguageTag key="colon"/>        </td>
        <td width="25%">
  		<input type="text" id="adminId" name="queryForm.adminId" value="${queryForm.adminid}" class="formCss100"/>	    </td>
        <td width="3%">        </td>
        <td width="52%"><span style="display:inline-block" class="query-button-css"><a href="javascript:query(false);" class="isLink_LanSe">
        <view:LanguageTag key="common_syntax_query"/></a></span></td>
      </tr>
    </table>
  </form>
  <div id="listDataAJAX"></div>
  </body>
</html>