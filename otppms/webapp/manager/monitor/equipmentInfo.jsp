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
	<script type="text/javascript" src="<%=path%>/manager/monitor/js/list.js"></script>
	<script language="javascript" type="text/javascript">
		// Start,多语言提取
		
		// 所有设备列表初始化
		var device_ip_lang = '<view:LanguageTag key="monitor_device_ip"/>';
		var device_sername_lang = '<view:LanguageTag key="monitor_device_sername"/>';
		var ser_sys_type_lang = '<view:LanguageTag key="monitor_device_ser_sys_type"/>';
		var device_ser_health_lang = '<view:LanguageTag key="monitor_device_ser_health"/>';
		
		var device_title_tip_lang = '<view:LanguageTag key="monitor_device_title_tip"/>';
		var monitor_center_lang = '<view:LanguageTag key="monitor_center"/>';
		var monitor_portal_lang = '<view:LanguageTag key="monitor_portal"/>';
		var monitor_auth_ser_lang = '<view:LanguageTag key="monitor_auth_ser"/>';
		var detail_info_lang = '<view:LanguageTag key="common_syntax_detail_info"/>';
		
		// 内存信息列表
		var memory_total_lang = '<view:LanguageTag key="monitor_memory_total"/>';
		var memory_used_lang = '<view:LanguageTag key="monitor_memory_used"/>';
		var memory_surplus_lang = '<view:LanguageTag key="monitor_memory_surplus"/>';
		var memory_occupancy_lang = '<view:LanguageTag key="monitor_memory_occupancy"/>';
		
		// cpu信息列表
		var all_use_rate_lang = '<view:LanguageTag key="monitor_cpu_all_use_rate"/>';
		var cpu_curr_free_rate_lang = '<view:LanguageTag key="monitor_cpu_curr_free_rate"/>';
		var cpu_user_use_rate_lang = '<view:LanguageTag key="monitor_cpu_user_use_rate"/>';
		var cpu_sys_use_rate_lang = '<view:LanguageTag key="monitor_cpu_sys_use_rate"/>';
		var cpu_curr_wait_rate_lang = '<view:LanguageTag key="monitor_cpu_curr_wait_rate"/>';
		var cpu_interrupt_req_lang = '<view:LanguageTag key="monitor_cpu_interrupt_req"/>';
		var cpu_nice_use_rate_lang = '<view:LanguageTag key="monitor_cpu_nice_use_rate"/>';
		
		// 磁盘信息列表
		var file_system_lang = '<view:LanguageTag key="monitor_file_system"/>';
		var file_letter_lang = '<view:LanguageTag key="monitor_file_letter"/>';
		var file_letter_total_size_lang = '<view:LanguageTag key="monitor_file_letter_total_size"/>';
		var file_letter_use_size_lang = '<view:LanguageTag key="monitor_file_letter_use_size"/>';
		var file_letter_avai_size_lang = '<view:LanguageTag key="monitor_file_letter_avai_size"/>';
		var file_letter_rate_lang = '<view:LanguageTag key="monitor_file_letter_rate"/>';
		var file_type_lang = '<view:LanguageTag key="monitor_file_type"/>';
		
		// 初始化应用系统在线用户数列表
		var app_sys_name_lang = '<view:LanguageTag key="monitor_app_sys_name"/>';
		var online_users_lang = '<view:LanguageTag key="monitor_app_online_users"/>';
		
		// 初始化认证服务器状态列表
		var auth_ser_name_lang = '<view:LanguageTag key="monitor_app_auth_ser_name"/>';
		var syntax_status_lang = '<view:LanguageTag key="common_syntax_status"/>';
		// End,多语言提取
		
		$(function(){
			var ipAddr = $("#ipAddr").val();
			var serverType = $("#serverType").val();
			var port = $("#port").val();
			var clientServPath = $("#clientServPath").val();
			//var ajaxbg = $("#background,#progressBar");
		    //ajaxbg.show();
			// 初始化内存列表
			initMemInfoList(ipAddr,port,serverType,clientServPath);
			// 初始化cpu使用率列表
			initCpuInfoList(ipAddr,port,serverType,clientServPath);
			// 初始化磁盘信息列表
			initDrInfoList(ipAddr,port,serverType,clientServPath);
			// ajaxbg.hide();
			// 定时执行方法
			window.setInterval(function(){
				refreshData(new Array('memListDataAJAX','cpuListDataAJAX','drListDataAJAX'));
			},"5000");
		});
	</script>
  </head>
  <body style="overflow:auto;">
  	<div id="background" class="background" style="display: none; "></div>
	<div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
	<input id="contextPath" type="hidden" value="<%=path%>" />
	<input id="curPage" type="hidden" type="text" value="${param.cPage}" />
	<input id="ipAddr" type="hidden" value="${param.ipaddr }" />
	<input id="port" type="hidden" value="${param.port }" />
	<input id="clientServPath" type="hidden" value="${param.clientservpath }" />
	<input id="serverType" type="hidden" value="${param.servertype }" />
    <!--  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" style="margin:10px;">
      <tr>
        <td>
        	<span class="query-font-css">管理员帐号：</span><input type="text" id="adminid" name="queryForm.adminid" value="${queryForm.adminid}" class="form-text"/>
			<span class="query-font-css">真实姓名：</span><input type="text" id="realname" name="queryForm.realname" value="${queryForm.realname}" class="form-text" />
       		<span style="display:inline-block" class="query-button-css"><a href="javascript:query();" class="isLink_LanSe">查 询</a></span>
       	</td>
      </tr>
    </table>-->
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td>  
        	 <view:LanguageTag key="monitor_memory_basic_info"/>
        </td>
      </tr>
    </table>     
 	<div id="memListDataAJAX"></div>
 	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td>  
        	 <view:LanguageTag key="monitor_cpu_usage_info"/>
        </td>
      </tr>
    </table>
    <div id="cpuListDataAJAX"></div>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td>  
        	 <view:LanguageTag key="monitor_file_sys_basic_info"/>
        </td>
      </tr>
    </table>  
    <div id="drListDataAJAX"></div> 
  </body>
</html>
