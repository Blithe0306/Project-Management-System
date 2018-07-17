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
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
    
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
   	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
    
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>	
	<script type="text/javascript" src="<%=path%>/manager/authmgr/server/js/list.js"></script>
		
	<script language="javascript" type="text/javascript">
		// Start,多语言提取
		var langYes = '<view:LanguageTag key="common_syntax_yes"/>';
		var langSerHigh = '<view:LanguageTag key="auth_ser_priority_high"/>'; 
		var langSerOrdinary = '<view:LanguageTag key="auth_ser_priority_ordinary"/>';
		var langSerLow = '<view:LanguageTag key="auth_ser_priority_low"/>';
		var langNothing = '<view:LanguageTag key="common_syntax_nothing"/>';
		
		// 列表
		var ser_hostip_lang = '<view:LanguageTag key="auth_ser_hostip"/>';
		var ser_hostname_lang = '<view:LanguageTag key="auth_ser_hostname"/>';
		var ser_priority_lang = '<view:LanguageTag key="auth_ser_priority"/>';
		var syntax_desc_lang = '<view:LanguageTag key="common_syntax_desc"/>';
		var syntax_operation_lang = '<view:LanguageTag key="common_syntax_operation"/>';
		var ser_state_lang = '<view:LanguageTag key="auth_ser_state"/>';
		var ser_state_getting_lang = '<view:LanguageTag key="auth_ser_state_getting"/>';
		var ser_conn_succ_lang = '<view:LanguageTag key="auth_ser_conn_succ"/>';
		var ser_conn_err_lang = '<view:LanguageTag key="auth_ser_conn_error"/>';
		
		
		// 操作
		var base_info_lang = '<view:LanguageTag key="auth_ser_base_info"/>';
		var conf_info_lang = '<view:LanguageTag key="auth_ser_conf_info"/>';
		var need_del_date_lang = '<view:LanguageTag key="common_syntax_check_need_del_date"/>';
		var del_sel_date_lang = '<view:LanguageTag key="common_syntax_confirm_del_sel_date"/>';
		var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
		var syntax_confirm_lang = '<view:LanguageTag key="common_syntax_confirm"/>';
		// End,多语言提取
	
		var permEdit = '';//编辑权限
	    $(function(){
	      permEdit = '<view:AdminPermTag key="040003" path="<%=path%>" langKey="common_syntax_edit" type="1" />';
	    });
	    
	</script>
  </head>
  
  <body scroll="no" style="overflow:auto; overflow-x:hidden">
  	<div id="background" class="background" style="display: none; "></div>
  	<div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
  	<input id="contextPath" type="hidden" value="<%=path%>" />
	<input id="curPage" type="hidden" type="text" value="${param.cPage}" />
	<table width="800" border="0" cellspacing="0" cellpadding="0" style="margin-top:6px">
      <tr>
        <td width="116" align="right"><view:LanguageTag key="auth_ser_hostip"/><view:LanguageTag key="colon"/></td>
        <td width="168"><input type="text" id="hostipaddr" name="serverQueryForm.hostipaddr" value="${serverQueryForm.hostipaddr}" class="formCss100"/></td>
        <td width="116" align="right"><view:LanguageTag key="auth_ser_hostname"/><view:LanguageTag key="colon"/></td>
        <td width="168"><input type="text" id="hostname" name="serverQueryForm.hostname" value="${serverQueryForm.hostname}" class="formCss100"/></td>
        <td width="15">&nbsp;</td>
        <td width="217"><span style="display:inline-block" class="query-button-css"><a href="javascript:query();" class="isLink_LanSe"><view:LanguageTag key="common_syntax_query"/></a></span></td>
      </tr>
    </table>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="operTableBg">
	  <tr>
	  	<td>
	  		<span style="float:left">&nbsp;&nbsp;</span>
		    <view:AdminPermTag key="040004" path="<%=path%>" langKey="common_syntax_delete" type="2" />
		</td>
	  </tr>
	</table>
  	<div id="listDataAJAX"></div>
  </body>
</html>