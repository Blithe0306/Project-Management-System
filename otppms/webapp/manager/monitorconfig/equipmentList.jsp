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
    <title><view:LanguageTag key="monitor_device_info_list"/></title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
    <script type="text/javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
    <script src="<%=path%>/manager/common/js/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/monitorconfig/js/list.js"></script>
	<script language="javascript" type="text/javascript">
		// Start,多语言提取
		
		// 所有设备列表初始化
		var device_ip_lang = '<view:LanguageTag key="monitor_device_ip"/>';
		var device_sername_lang = '<view:LanguageTag key="monitor_device_sername"/>';
		var ser_sys_type_lang = '<view:LanguageTag key="monitor_device_ser_sys_type"/>';
		var device_ser_health_lang = '<view:LanguageTag key="monitor_device_ser_health"/>';
		// End,多语言提取
		
		$(function(){
			// 初始化列表
			var ajaxbg = $("#background,#progressBar");
		    ajaxbg.show();
			initEquipmentList();
		    ajaxbg.hide();
		});
	</script>
  </head>
  <body scroll="no" style="overflow:auto; overflow-x:hidden">
  <div id="background" class="background" style="display: none; "></div>
  <div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
  <input id="contextPath" type="hidden" value="<%=path%>" />
  <input id="curPage" type="hidden" type="text" value="${param.cPage}" />
  <form name="ListForm" id="ListForm" method="post" action="">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td>  
        	<view:LanguageTag key="monitor_all_device_list"/>
        </td>
        <td width="16px">
	    	<a href="javascript:addAdmPermCode('0900','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" width="16" height="16" hspace="6" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a></td>
      </tr>
    </table>     
  </form>
  	<div id="msgShow" class="msgDiv"><span class="msg"></span></div>     
 	<div id="listDataAJAX"></div>
  </body>
</html>
