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
	<script type="text/javascript" src="<%=path%>/manager/confinfo/usersource/js/list.js"></script>		
	<script language="javascript" type="text/javascript">
		// Start,多语言提取
		var langmanually = '<view:LanguageTag key="usource_manual_update"/>';
		var langautomatic = '<view:LanguageTag key="usource_timing_update_title"/>';
		var langconfig = '<view:LanguageTag key="common_syntax_config"/>';
		
		// 列表
		var name_lang = '<view:LanguageTag key="usource_name"/>';
		var type_lang = '<view:LanguageTag key="usource_type"/>';
		var syntax_desc_lang = '<view:LanguageTag key="common_syntax_desc"/>';
		var timing_update_lang = '<view:LanguageTag key="usource_is_timing_update"/>';
		var syntax_operation_lang = '<view:LanguageTag key="common_syntax_operation"/>';
		
		// 操作
		var syntax_enable_lang = '<view:LanguageTag key="common_syntax_enable"/>';
		var syntax_disabled_lang = '<view:LanguageTag key="common_syntax_disabled"/>';
		var remote_db_lang = '<view:LanguageTag key="usource_remote_db"/>';
		var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
		var timing_config_lang = '<view:LanguageTag key="usource_timing_config"/>';
		var need_del_date_lang = '<view:LanguageTag key="common_syntax_check_need_del_date"/>';
		var confirm_del_sel_date_lang = '<view:LanguageTag key="common_syntax_confirm_del_sel_date"/>';
		var syntax_confirm_lang = '<view:LanguageTag key="common_syntax_confirm"/>';
		// Start,多语言提取
	</script>	
  </head>
  
  <body style="overflow:auto; overflow-x:hidden">
  <input type="hidden" name="contextPath" value="<%=path%>" id="contextPath"/>
  <input id="curPage" type="hidden" type="text" value="${param.cPage}" />
  <div id="background" class="background" style="display: none; "></div>
  <div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
  <form name="ListForm" method="post" action="">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="operTableBg">
      <tr>
        <td>
            <span style="float:left">&nbsp;&nbsp;</span>
		    <a href="<%=path%>/manager/confinfo/usersource/usersource.jsp" style="display:inline-block;" class="button"><span><view:LanguageTag key="common_syntax_add"/></span></a> 
			<a href="javascript:delData()" style="display:inline-block;"  class="button" ><span><view:LanguageTag key="common_syntax_delete"/></span></a>
	 	</td>
      </tr>
    </table>    
  </form>
  <div id="listDataAJAX"></div>
  </body>
</html>