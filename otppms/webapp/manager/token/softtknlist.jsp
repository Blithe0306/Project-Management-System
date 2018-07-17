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
	
    <script language="javascript" src="<%=path%>/manager/common/js/checkall.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>	
	<script type="text/javascript" src="<%=path%>/manager/token/js/softtknlist.js"></script>	
	<script language="javascript" type="text/javascript">
		
		// Start,多语言提取
		var langHardtkn = '<view:LanguageTag key="tkn_physical_hard"/>';
		var langMtkn = '<view:LanguageTag key="tkn_physical_mobile"/>';
		var langStkn = '<view:LanguageTag key="tkn_physical_soft"/>';
		var langSMStkn = '<view:LanguageTag key="tkn_physical_sms"/>';
		
		// 列表
		var tknum_lang = '<view:LanguageTag key="tkn_comm_tknum"/>';
		var orgunit_lang = '<view:LanguageTag key="tkn_orgunit"/>';
		var type_lang = '<view:LanguageTag key="tkn_comm_type"/>';
		var enable_lang = '<view:LanguageTag key="tkn_comm_enable"/>';
		var lock_lang = '<view:LanguageTag key="tkn_comm_lock"/>';
		var lose_lang = '<view:LanguageTag key="tkn_comm_lose"/>';
		var invalid_lang = '<view:LanguageTag key="tkn_comm_invalid"/>';
		var valid_lang = '<view:LanguageTag key="tkn_valid"/>';
		var operate_lang = '<view:LanguageTag key="tkn_operate"/>';
		var tkn_dist_lang = '<view:LanguageTag key="tkn_dist"/>';
		var syntax_yes_lang = '<view:LanguageTag key="common_syntax_yes"/>';
		var syntax_no_lang = '<view:LanguageTag key="common_syntax_no"/>';
		var confirm_info_1_lang = '<view:LanguageTag key="tkn_confirm_info_1"/>';
		var dist_soft_lang = '<view:LanguageTag key="tkn_dist_soft"/>';
		// End,多语言提取
		
		$(function() {
			//删除空白的（无权限的option）
              $('#oper option').each(function(index){
                 $("#oper option[text='']").remove();   
              });
 
		})
	</script>
 
  </head>
  
  <body scroll="no" style="overflow:hidden;">
   <input type="hidden"  name="contextPath" value="<%=path%>" id="contextPath"/> 
   <input type="hidden"  name="currentPage" value="${param.currentPage}" id="currentPage"/> 
   <input id="curtime"   type="hidden"   value="<%=System.currentTimeMillis()/1000%>"/>
  
   <form name="ListForm" id="ListForm" method="post" action="">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td bgcolor="#FFFFFF">
          <table width="800" border="0" cellspacing="0" cellpadding="0" style="margin-top:6px; margin-bottom:6px;">
            <tr>
	          <td width="110" align="right"><view:LanguageTag key="tkn_comm_tknum"/><view:LanguageTag key="colon"/></td>
		        <td width="148">
		          <input type="text" id="tokenStr" name="tokenQueryForm.tokenStr" value="${tokenQueryForm.tokenStr}" class="formCss100" /></td>
		        <td width="110" align="right"><view:LanguageTag key="tkn_start_num"/><view:LanguageTag key="colon"/></td>
		        <td width="148"><input type="text" id="tokenStart" name="tokenQueryForm.tokenStart" value="${tokenQueryForm.tokenStart}" class="formCss100" /></td>
		        <td width="110" align="right"><view:LanguageTag key="tkn_stop_num"/><view:LanguageTag key="colon"/></td>
		        <td width="148"><input type="text" id="tokenEnd" name="tokenQueryForm.tokenEnd" value="${tokenQueryForm.tokenEnd}" class="formCss100" /></td>
		        <td width="26">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="display:inline-block" class="query-button-css"><a href="javascript:query();" class="isLink_LanSe"><span>
	          <view:LanguageTag key="tkn_query_2"/></span></a></span></td>
            </tr>
          </table>
        </td>
      </tr>
    </table>    
  </form>
   <div id="listDataAJAX"></div>
  </body>
</html>