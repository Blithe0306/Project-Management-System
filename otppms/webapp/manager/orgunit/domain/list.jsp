<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%@page import="com.ft.otp.common.ConfConstant"%>
<%@page import="com.ft.otp.util.conf.ConfDataFormat"%>
<%
	String path = request.getContextPath();
	String defaultDomainId = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_COMMON,ConfConstant.DEFAULT_DOMAIN_ID);
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
    <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
    <script src="<%=path%>/manager/common/js/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/orgunit/domain/js/list.js"></script>
	<script language="javascript" type="text/javascript">
	// Start,多语言提取
	var code_lang = '<view:LanguageTag key="domain_info_code"/>';
	var name_lang = '<view:LanguageTag key="domain_info_name"/>';
	var whether_def_lang = '<view:LanguageTag key="domain_info_whether_def"/>';
	var desc_lang = '<view:LanguageTag key="common_syntax_desc"/>';
	var operation_lang = '<view:LanguageTag key="common_syntax_operation"/>';
	var syntax_yes_lang = '<view:LanguageTag key="common_syntax_yes"/>';
	var syntax_no_lang = '<view:LanguageTag key="common_syntax_no"/>';
	var del_date_lang = '<view:LanguageTag key="domain_sel_del_date"/>';
	var del_default_lang = '<view:LanguageTag key="domain_not_del_default"/>';
	var del_sel_date_lang = '<view:LanguageTag key="common_syntax_confirm_del_sel_date"/>';
	var confirm_del_sel_lang = '<view:LanguageTag key="common_syntax_confirm_del_sel_date"/>';
	var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
	var confirm_del_sel_lang = '<view:LanguageTag key="common_syntax_confirm_del_sel_date"/>';
	// End,多语言提取
	
	var permEdit; //编辑权限
	$(document).ready(function(){
	    permEdit = '<view:AdminPermTag key="080002" path="<%=path%>" langKey="common_syntax_edit" type="1"/>';
	    $("td :input").keydown(function(e) {
		if(e.keyCode == '13') {
			query(false);
		}
	});
	})
	</script>
  </head>
  <body scroll="no" style="overflow:auto; overflow-x:hidden">
  <input id="contextPath" type="hidden" value="<%=path%>" />
  <input id="defaultDomainId" type="hidden" value="<%=defaultDomainId%>" />
  <input id="curPage" type="hidden" value="${param.cPage}" />
  <div id="msgShow" class="msgDiv"><span class="msg"></span></div>
  <form name="ListForm" id="ListForm" method="post" action="">
    <table width="800" border="0" cellspacing="0" cellpadding="0" style="margin-top:6px">
      <tr>
        <td width="116" align="right"><view:LanguageTag key="domain_info_code"/><view:LanguageTag key="colon"/></td>
        <td width="168"><input type="text" id="domainSn" name="queryForm.domainSn" value="${queryForm.domainSn}" class="formCss100"/></td>
        <td width="116" align="right"><view:LanguageTag key="domain_info_name"/><view:LanguageTag key="colon"/></td>
        <td width="168"><input type="text" id="domainName" name="queryForm.domainName" value="${queryForm.domainName}" class="formCss100" /></td>
        <td width="15">&nbsp;</td>
        <td width="217"><span style="display:inline-block" class="query-button-css"><a href="javascript:query();" class="isLink_LanSe">
        <view:LanguageTag key="common_syntax_query"/></a></span></td>
      </tr>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="operTableBg">
      <tr>
        <td>  
		     <a href="#" onClick="javascript:addDomainInfo()" class="isLink_HeiSe"><view:AdminPermTag key="080001" path="<%=path%>" langKey="common_syntax_add" type="3"  /></a>
		     <a href="#" onClick="javascript:delData()" class="isLink_HeiSe"><view:AdminPermTag key="080003" path="<%=path%>" langKey="common_syntax_delete" type="3" /></a>
        </td>
      </tr>
    </table>     
  </form>
 <div id="listDataAJAX"></div>
  </body>
</html>