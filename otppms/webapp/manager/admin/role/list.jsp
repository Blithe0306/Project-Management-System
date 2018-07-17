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
	<script type="text/javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/datepicker/WdatePicker.js"></script>	
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/admin/role/js/list.js"></script>
	
	<script language="javascript" type="text/javascript">
		
		// Start,多语言提取
		var role_name_lang = '<view:LanguageTag key="admin_role_name"/>';
		var creator_lang = '<view:LanguageTag key="common_info_creator"/>';
		var create_time_lang = '<view:LanguageTag key="common_syntax_create_time"/>';
		var update_time_lang = '<view:LanguageTag key="common_syntax_update_time"/>';
		var desc_lang = '<view:LanguageTag key="common_syntax_desc"/>';
		var operation_lang = '<view:LanguageTag key="common_syntax_operation"/>';
		var role_perm_lang = '<view:LanguageTag key="role_perm"/>';
		var del_role_lang = '<view:LanguageTag key="role_sel_del_role"/>';
		var sure_del_lang = '<view:LanguageTag key="role_sure_del_role"/>';
		var syntax_confirm_lang = '<view:LanguageTag key="common_syntax_confirm"/>';
		var creator_succ_lang = '<view:LanguageTag key="admin_replace_creator_succ"/>';
		var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
		var change_role_lang = '<view:LanguageTag key="role_sel_change_role"/>';
		var change_creator_lang = '<view:LanguageTag key="role_sure_change_creator"/>';
		var init_role_lang = '<view:LanguageTag key="role_not_edit_sys_init_role"/>';
		var create_myself_lang = '<view:LanguageTag key="role_not_edit_not_create_myself"/>';
		var sure_edit_lang = '<view:LanguageTag key="role_maybe_dist_sure_edit"/>';
		var myself_role_lang = '<view:LanguageTag key="role_admin_not_change_myself_role"/>';
		var change_creator_lang = '<view:LanguageTag key="admin_change_creator"/>';
		// End,多语言提取
		
		var permEdit = '';
		$(document).ready(function() {
		  permEdit = '<view:AdminPermTag key="010302" path="<%=path%>" langKey="common_syntax_edit" type="1"/>';
		});
	</script>
  </head>
  
  <body scroll="no" style="overflow:hidden">
  	<input id="contextPath"   type="hidden" value="<%=path%>"/>
	<input id="l_userid"      type="hidden" value="${curLoginUser}"/>
	<input id="l_userid_role" type="hidden" value="${curLoginUserRole}"/>
	<input id="currentPage"   type="hidden" value="${param.currentPage}"/>
		
    <div id="msgShow" class="msgDiv"><span class="msg"></span></div>
	<form name="ListForm" method="post" action="" id="ListForm">
      <table width="800" border="0" cellspacing="0" cellpadding="0" style="margin-top:6px">
        <tr>
          <td width="116" align="right"><view:LanguageTag key="admin_role_name"/><view:LanguageTag key="colon"/></td>
          <td width="168"><input type="text" id="roleName" name="queryForm.roleName" value="${queryForm.roleName}" class="formCss100"/></td>
          <td width="116" align="right"><view:LanguageTag key="common_info_creator"/><view:LanguageTag key="colon"/></td>
          <td width="168">
		    <input type="text" id="createUser" name="queryForm.createUser" value="${queryForm.createUser}" class="formCss100"/>
		  </td>
		  <td width="15">&nbsp;</td>
          <td width="217"></td>
        </tr>
          <td width="116" align="right"><view:LanguageTag key="common_syntax_create_start_time"/><view:LanguageTag key="colon"/></td>
          <td width="168"><input id="startDate" type="text" name="queryForm.startTime" value="${queryForm.startTime}" onclick ="WdatePicker({lang:'${language_session_key}', dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endDate\')}', position:{left:2}});" readOnly="readOnly" class="form-text" style="width:168px" /></td>
          <td width="116" align="right"><view:LanguageTag key="common_syntax_create_end_time"/><view:LanguageTag key="colon"/></td>
          <td width="168"><input id="endDate" type="text" name="queryForm.endTime" value="${queryForm.endTime}" onclick="WdatePicker({lang:'${language_session_key}', dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startDate\')}', position:{left:2}});" readOnly="readOnly" class="form-text" style="width:168px" /></td>
          <td width="15">&nbsp;</td>
          <td width="217"><span style="display:inline-block" class="query-button-css"><a href="javascript:query();" id="queryButton" class="isLink_LanSe"><view:LanguageTag key="common_syntax_query"/></a></span></td>
      </table>
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="operTableBg">
      <tr>
        <td> 
        	<!--  
		     <view:AdminPermTag key="010102" path="<%=path%>" langKey="common_syntax_add" type="3"  />
		      -->
		     <span style="float:left">&nbsp;&nbsp;</span>
		     <view:AdminPermTag key="010303" path="<%=path%>" langKey="common_syntax_delete" type="2" />
		     <view:AdminPermTag key="010304" path="<%=path%>" langKey="admin_change_creator" type="2" />
		    
        </td>
      </tr>
    </table>   
 </form>
  <div id="listDataAJAX"></div>
  </body>
</html>