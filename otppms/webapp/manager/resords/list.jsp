<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
	String prjid = request.getParameter("prjid");
	String flag = request.getParameter("flag");        

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
 	<script type="text/javascript" src="<%=path%>/manager/resords/js/list.js"></script>	
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
		var has_child_lang = '<view:LanguageTag key="admin_sel_user_has_child"/>';
		var syntax_confirm_lang = '<view:LanguageTag key="common_syntax_confirm"/>';
		// End,多语言提取
		var prjid = <%=prjid%>;
		
		var permEdit='';
		
		$(document).ready(function() {
			permEdit = '<view:AdminPermTag key="030303" path="<%=path%>" langKey="common_syntax_edit" type="1" />';
		});
		function goback_(){
			location.href='<%=path%>/manager/resords/list.jsp';
		}
		
		
	</script>
  </head>
  <body scroll="no" style="overflow:hidden">
  <input type="hidden" name="contextPath" value="<%=path%>" id="contextPath"/>
  	<input id="currentPage" type="hidden" value="${param.cPage}" />
 	<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="61%">
	<table width="800" border="0" cellspacing="0" cellpadding="0" style="margin-top:6px; margin-bottom:6px">
	 <tr>
        <td width="116" align="right">项目名称<view:LanguageTag key="colon"/></td>
        <td width="168"><input type="text" id="prjid" name="resords.prjid" value="${resords.prjid}" class="formCss100"/></td>
        <td width="116" align="right">上门人员<view:LanguageTag key="colon"/></td>
        <td width="168"><input type="text" id="recordUser" name="resords.recordUser" value="${resords.recordUser}" class="formCss100"/></td>
       
       <td width="15"></td>
        <td><span style="display:inline-block" class="query-button-css"><a href="javascript:query();" class="isLink_LanSe"><view:LanguageTag key="common_syntax_query"/></a></span></td> 
      </tr>
    </table>
	</td>
    <td width="39%" align="right" valign="top"><a href="javascript:addAdmPermCode('030301','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" hspace="6" vspace="6" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a></td>
  </tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="operTableBg">
      <tr>
      	<td width="5">&nbsp;&nbsp;</td>
        <td>
		   <view:AdminPermTag key="030304" path="<%=path%>" langKey="common_syntax_delete" type="2" />
		   <c:if test='${not empty paramValues.prjid }'><a href="#" onClick=goback_() id="goback" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a> </c:if>
        </td>
      </tr>
    </table> 
 	<div id="listDataAJAX"></div>
  </body>
</html>