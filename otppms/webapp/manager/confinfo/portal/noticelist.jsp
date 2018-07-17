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
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
   	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/datepicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>	
	<script type="text/javascript" src="<%=path%>/manager/confinfo/portal/js/list.js"></script>
	<script type="text/javascript" >
		// Start,多语言提取
		var langGeneral = '<view:LanguageTag key="notice_general"/>'; 
		var langUrgent = '<view:LanguageTag key="notice_urgent"/>';
		var langWarning = '<view:LanguageTag key="notice_warning"/>';
		var langEdit = '<view:LanguageTag key="common_syntax_edit"/>';
		
		// 列表
		var title_text_lang = '<view:LanguageTag key="notice_title_text"/>';
		var level_lang = '<view:LanguageTag key="notice_level"/>';
		var create_time_lang = '<view:LanguageTag key="notice_create_time"/>';
		var effec_time_lang = '<view:LanguageTag key="notice_effec_time"/>';
		var content_lang = '<view:LanguageTag key="notice_content"/>';
		var created_user_lang = '<view:LanguageTag key="notice_created_user"/>';
		var syntax_operation_lang = '<view:LanguageTag key="common_syntax_operation"/>';
		
		// 操作
		var detail_info_lang = '<view:LanguageTag key="notice_detail_info"/>';
		var need_del_date_lang = '<view:LanguageTag key="common_syntax_check_need_del_date"/>';
		var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
		var del_sel_date_lang = '<view:LanguageTag key="common_syntax_confirm_del_sel_date"/>';
		var syntax_confirm_lang = '<view:LanguageTag key="common_syntax_confirm"/>';
		// End,多语言提取
	</script>
  </head>
  <body scroll="no" style="overflow:auto; overflow-x:hidden">
  	<input id="contextPath" type="hidden" value="<%=path%>" />
	<input id="currentPage" type="hidden" value="${param.cPage}" />
	<form name="ListForm" method="post" action="">
	   <td bgcolor="#FFFFFF"><table width="800" border="0" cellspacing="0" cellpadding="0" style="margin-top:6px">
            <tr>
	          <td width="120" align="right"><view:LanguageTag key="notice_title_text"/><view:LanguageTag key="colon"/></td>
		        <td width="160">
		          <input type="text" id="title" name="noticeQueryForm.title" class="formCss100" />		
	          </td>
		        <td width="120" align="right"><view:LanguageTag key="notice_level"/><view:LanguageTag key="colon"/></td>
		        <td width="160">
		        	<select id="systype" name="noticeQueryForm.systype" class="select100" >
						 <option value="-1"><view:LanguageTag key="common_syntax_please_sel"/></option>
						 <option value="1"><view:LanguageTag key="notice_level_general"/></option>
						 <option value="2"><view:LanguageTag key="notice_level_urgent"/></option>
						 <option value="3"><view:LanguageTag key="notice_level_warning"/></option>
					</select>
	          </td>
		        <td width="40" align="right"></td>
		        <td width="200"></td>
		    </tr>
            <tr>
		        <td align="right"><view:LanguageTag key="notice_create_time"/><view:LanguageTag key="colon"/></td>
		        <td>
		          <input id="startNoticeTime" type="text" name="noticeQueryForm.startNoticeTime" onClick="WdatePicker({lang:'${language_session_key}', dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endNoticeTime\')}', position:{left:2}});" readOnly="readOnly" class="formCss100" />
		        </td>
		        <td align="right"><view:LanguageTag key="notice_effec_time"/><view:LanguageTag key="colon"/></td>
		        <td>
				  <input id="endNoticeTime" type="text" name="noticeQueryForm.endNoticeTime" onClick="WdatePicker({lang:'${language_session_key}', dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startNoticeTime\')}', position:{left:2}});" readOnly="readOnly" class="formCss100" />
		        </td>
		        <td align="right"></td>
		        <td><span style="display:inline-block" class="query-button-css"><a href="javascript:query();" class="isLink_LanSe"><span><view:LanguageTag key="tkn_query_2"/></span></a></span></td>
		    </tr>
       </table>
	   <table width="100%" border="0" cellspacing="0" cellpadding="0" class="operTableBg">
		  <tr>
		    <td width="99%">
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