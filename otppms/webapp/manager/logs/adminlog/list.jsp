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
	    
    <script type="text/javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/datepicker/WdatePicker.js"></script>	
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>	
 	<script type="text/javascript" src="<%=path%>/manager/logs/adminlog/js/list.js"></script>	
	<script language="javascript" type="text/javascript">
		// Start,多语言提取
		var operator_lang = '<view:LanguageTag key="log_info_operator"/>';
		var oper_time_lang = '<view:LanguageTag key="log_info_oper_time"/>';
		var client_ip_lang = '<view:LanguageTag key="log_info_client_ip"/>';
		var actionid_lang = '<view:LanguageTag key="log_actionid"/>';
		var log_info_lang = '<view:LanguageTag key="log_info"/>';
		var operate_result_lang = '<view:LanguageTag key="log_info_operate_result"/>';
		var detail_info_lang = '<view:LanguageTag key="common_syntax_detail_info"/>';
		var langSucc = '<view:LanguageTag key="common_syntax_success"/>';
		var langFailure = '<view:LanguageTag key="common_syntax_failure"/>';
		var langDetails = '<view:LanguageTag key="common_syntax_details"/>';
		var detail_info_lang = '<view:LanguageTag key="common_syntax_detail_info"/>';
		// End,多语言提取
	</script>
  </head>
  <body scroll="no" style="overflow:hidden">
  <input type="hidden" name="contextPath" value="<%=path%>" id="contextPath"/>
 	<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="61%">
	<table width="800" border="0" cellspacing="0" cellpadding="0" style="margin-top:6px; margin-bottom:6px">
      <tr>
        <td width="116" align="right"><view:LanguageTag key="log_info_operator"/><view:LanguageTag key="colon"/></td>
        <td width="168"><input type="text" id="operator" name="adminLogQueryForm.operator" value="${adminLogQueryForm.operator}" class="formCss100"/></td>
        <td width="116" align="right"><view:LanguageTag key="log_otp_failed_info"/><view:LanguageTag key="colon"/></td>
        <td width="168"><input type="text" id="descp" name="adminLogQueryForm.descp" value="${adminLogQueryForm.descp}" class="formCss100"/></td>
        <td width="15"></td>
        <td width="217"></td>
      </tr>
      <tr>
        <td align="right"><view:LanguageTag key="log_info_client_ip"/><view:LanguageTag key="colon"/></td>
        <td><input type="text" id="clientIp" name="adminLogQueryForm.clientIp" value="${adminLogQueryForm.clientIp}" class="formCss100" style="width:168px"/></td>
        <td align="right"><view:LanguageTag key="log_info_operate_result"/><view:LanguageTag key="colon"/></td>
        <td>
        	<select id="actionResult" name="adminLogQueryForm.actionResult" value="${adminLogQueryForm.actionResult}" class="select100" style="width:168px">
	            <option value="-1" <c:if test='${adminLogQueryForm.actionResult == -1}'> selected</c:if>>
	            	<view:LanguageTag key="common_syntax_please_sel"/>
	            </option>
	            <option value="0" <c:if test='${adminLogQueryForm.actionResult == 0}'> selected</c:if>>
	            	<view:LanguageTag key="common_syntax_success"/>
	            </option>
	            <option value="1" <c:if test='${adminLogQueryForm.actionResult == 1}'> selected</c:if>>
	            	<view:LanguageTag key="common_syntax_failure"/>
	            </option>
            </select>
        </td>
        <td></td>
        <td></td>
      </tr>
      <tr>
        <td align="right"><view:LanguageTag key="common_syntax_start_time"/><view:LanguageTag key="colon"/></td>
        <td>
		  <input id="startDate" type="text" name="adminLogQueryForm.startLogTime" value="${adminLogQueryForm.startLogTime}" onclick ="WdatePicker({lang:'${language_session_key}', dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endDate\')}', position:{left:2}});" readOnly="readOnly" class="formCss100" style="width:168px" />
        </td>
        <td align="right"><view:LanguageTag key="common_syntax_end_time"/><view:LanguageTag key="colon"/></td>
        <td>
		  <input id="endDate" type="text" name="adminLogQueryForm.endLogTime" value="${adminLogQueryForm.endLogTime}" onclick="WdatePicker({lang:'${language_session_key}', dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startDate\')}', position:{left:2}});" readOnly="readOnly" class="formCss100" style="width:168px" /> 
        </td>
        <td></td>
        <td><span style="display:inline-block" class="query-button-css"><a href="javascript:query(false,true);" class="isLink_LanSe"><view:LanguageTag key="common_syntax_query"/></a></span></td>
      </tr>
    </table>
	</td>
    <td width="39%" align="right" valign="top"><a href="javascript:addAdmPermCode('0600','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" hspace="6" vspace="6" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a></td>
  </tr>
</table>
 	<div id="listDataAJAX"></div>
  </body>
</html>