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
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>	
	<script type="text/javascript" src="<%=path%>/manager/confinfo/radius/js/profilelist.js"></script>		
	<script language="javascript" type="text/javascript">
		// Start,多语言提取
		var radius_name_lang = '<view:LanguageTag key="radius_name"/>';
		var syntax_desc_lang = '<view:LanguageTag key="common_syntax_desc"/>';
		var syntax_operation_lang = '<view:LanguageTag key="common_syntax_operation"/>';
		var need_del_date_lang = '<view:LanguageTag key="common_syntax_check_need_del_date"/>';
		var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
		var del_sel_date_lang = '<view:LanguageTag key="common_syntax_confirm_del_sel_date"/>';
		var syntax_confirm_lang = '<view:LanguageTag key="common_syntax_confirm"/>';
		// End,多语言提取
		
		var permEdit;
		var permSet;
		$(function() {
		    permEdit = "<img src='<%=path%>/images/icon/file_edit.gif' title='<view:LanguageTag key="common_syntax_edit"/>' width='16' height='16' />";
		    permSet = "<img src='<%=path%>/images/icon/radius_set.png' title='<view:LanguageTag key="radius_attr_conf"/>' width='16' height='16' />";
		});
	</script>
  </head>
  
  <body>
  <input type="hidden" name="contextPath" value="<%=path%>" id="contextPath"/>
  <input id="curPage" type="hidden" type="text" value="${param.cPage}" />

  <form name="ListForm" method="post" action="">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="operTableBg">
	      <tr>
	        <td>
	            <span style="float:left">&nbsp;&nbsp;</span>
	        	<a href="javascript:addobj()" style="display:inline-block;" class="button"><span><view:LanguageTag key="common_syntax_add"/></span></a>
		        <a href="javascript:delData()" style="display:inline-block;" class="button"><span><view:LanguageTag key="common_syntax_delete"/></span></a>
		 	</td>
	      </tr>
	</table> 
  </form>
  
  <div id="listDataAJAX"></div>
  </body>
</html>