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
	<script type="text/javascript" src="<%=path%>/manager/confinfo/radius/js/attrlist.js"></script>		
	<script language="javascript" type="text/javascript">
		// Start,多语言提取
		var attr_id_lang = '<view:LanguageTag key="rad_attr_id"/>';
		var attr_name_lang = '<view:LanguageTag key="rad_attr_name"/>';
		var attr_valtype_lang = '<view:LanguageTag key="rad_attr_valtype"/>';
		var attr_value_lang = '<view:LanguageTag key="rad_attr_value"/>';
		var syntax_operation_lang = '<view:LanguageTag key="common_syntax_operation"/>';
		var need_del_date_lang = '<view:LanguageTag key="common_syntax_check_need_del_date"/>';
		var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
		var syntax_confirm_lang = '<view:LanguageTag key="common_syntax_confirm"/>';
		var confirm_del_sel_date_lang = '<view:LanguageTag key="common_syntax_confirm_del_sel_date"/>';
		var arrt_vendor_lang = '<view:LanguageTag key="tnk_vendor"/>';
		// End,多语言提取
	
		var permEdit;
		var permSet;
		
		$(function() {
		    var profileId = '${param.profileId}';
			
		    permEdit = "<img src='<%=path%>/images/icon/file_edit.gif' title='<view:LanguageTag key="common_syntax_edit"/>' width='16' height='16' />";
		    permSet = "<img src='<%=path%>/images/icon/radius_set.png' title='RADIUS配置' width='16' height='16' />";
		});

		function goBack(){
			location.href ='<%=path%>/manager/confinfo/radius/profilelist.jsp';
		}
	</script>
  </head>
  
  <body>
  <input type="hidden" name="contextPath" value="<%=path%>" id="contextPath"/>
  <input type="hidden" name="profileId" id="profileId" value="${param.profileId}" />
  <form name="attrForm" method="post" action="">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="operTableBg">
	      <tr>
	        <td><span style="float:left">&nbsp;&nbsp;</span>
		 		<a href="javascript:addobj()" style="display:inline-block;" class="button"><span><view:LanguageTag key="common_syntax_add"/></span></a>
		        <a href="javascript:delData()" style="display:inline-block;" class="button"><span><view:LanguageTag key="common_syntax_delete"/></span></a>
		        <a href="javascript:goBack()" style="display:inline-block;" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a>
		 	</td>
	      </tr>
	</table> 
  </form>
  
  <div id="listDataAJAX"></div>
  </body>
</html>