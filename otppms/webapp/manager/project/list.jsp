<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	request.setCharacterEncoding("UTF-8");
	String path = request.getContextPath();
	String custid = request.getParameter("custid");
	String returnBack = request.getParameter("returnBack");
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
 	<script type="text/javascript" src="<%=path%>/manager/project/js/list.js"></script>	
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
		
		var custid =<%=custid%>;
		var returnBack = <%=returnBack%>;
		var operType=null;
		//权限
		var permEdit = '';  //编辑
		var permGoPrjinfo = '';  //跳转定制信息
		var permGoPrjinfoAdd = '';  //添加定制信息
		var customUserAdd = '';	//添加定制项目监视人
		var projSummay = '';	//定制项目总结
		
		$(document).ready(function() {
			permEdit = '<view:AdminPermTag key="030003" path="<%=path%>" langKey="common_syntax_edit" type="1" />';
			permGoPrjinfo = '<view:AdminPermTag key="030005" path="<%=path%>" langKey="adm_perm_030005" type="1" />';
			permGoPrjinfoAdd = '<view:AdminPermTag key="030006" path="<%=path%>" langKey="adm_perm_030006" type="1" />';
			customUserAdd = '<view:AdminPermTag key="030007" path="<%=path%>" langKey="adm_perm_030007" type="1" />';
			projSummay = '<view:AdminPermTag key="030008" path="<%=path%>" langKey="adm_perm_030008" type="1" />';
		});
		
		function goback_(){
			location.href='<%=path%>/manager/customer/list.jsp';
		}
		
		function goPrjinfo(id){
			if(returnBack == 1){
				location.href = contextPath + '/manager/prjinfo/list.jsp?prjid='+ id+"&flag=2";
			}else{
				location.href = contextPath + '/manager/prjinfo/list.jsp?prjid='+ id;
			}
		}
		//添加定制信息
		function goPrjinfoAdd(id){
			window.parent.removeTabItemF('030202');
			window.parent.f_addTab('0302', this ,'030202', '添加定制信息',contextPath + '/manager/project/projectAction!goPrjinfoAdd.action?projId=' + id);
		}
		//添加监视人
		function addCustomUser(projId){
			projId = decodeURI(projId);
			window.location.href = contextPath + '/manager/project/addCustomUser.jsp?projId=' + encodeURI(encodeURI(projId))+"&ptype=0";
		}
		//项目总结
		function addProjSummary(projId){
			//FT.openWinSet('定制项目总结',contextPath + '/manager/project/projectAction!addProjSummary.action?project.id=' + projId,'close','',800,600);
			window.location.href = contextPath + '/manager/project/projectAction!addProjSummary.action?project.id=' + projId;
		}
	</script>
  </head>
  <body scroll="no" style="overflow:hidden">
  <input type="hidden" name="contextPath" value="<%=path%>" id="contextPath"/>
 	<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="61%">
	<table width="800" border="0" cellspacing="0" cellpadding="0" style="margin-top:6px; margin-bottom:6px">
      <tr>
        <td width="116" align="right">项目名称<view:LanguageTag key="colon"/></td>
        <td width="168"><input type="text" id="prjname" name="projectQueryForm.prjname" value="${projectQueryForm.prjname}" class="formCss100"/></td>
        <td width="116" align="right">项目编号<view:LanguageTag key="colon"/></td>
        <td width="168"><input type="text" id="prjid" name="projectQueryForm.prjid" value="${projectQueryForm.prjid}" class="formCss100"/></td>
        <td width="15"></td>
        <td width="217"></td>
      </tr>
      <tr>
        <td align="right"><view:LanguageTag key="common_syntax_start_time"/><view:LanguageTag key="colon"/></td>
        <td>
		  <input id="startTime" type="text" name="projectQueryForm.startTime" value="${projectQueryForm.startTime}" onclick ="WdatePicker({lang:'${language_session_key}', dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endTime\')}', position:{left:2}});" readOnly="readOnly" class="formCss100" style="width:168px" />
        </td>
        <td align="right"><view:LanguageTag key="common_syntax_end_time"/><view:LanguageTag key="colon"/></td>
        <td>
		  <input id="endTime" type="text" name="projectQueryForm.endTime" value="${projectQueryForm.endTime}" onclick="WdatePicker({lang:'${language_session_key}', dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}', position:{left:2}});" readOnly="readOnly" class="formCss100" style="width:168px" /> 
        </td>
        <td></td>
        <td><span style="display:inline-block" class="query-button-css"><a href="javascript:query(false,true);" class="isLink_LanSe"><view:LanguageTag key="common_syntax_query"/></a></span></td>
      </tr>
    </table>
	</td>
    <td width="39%" align="right" valign="top"><a href="javascript:addAdmPermCode('030001','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" hspace="6" vspace="6" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="operTableBg">
      <tr>
      	<td width="5">&nbsp;&nbsp;</td>
        <td>
		    <view:AdminPermTag key="030004" path="<%=path%>" langKey="common_syntax_delete" type="2" />
		    <c:if test='${not empty paramValues.custid || not empty paramValues.returnBack}'><a href="#" onclick=goback_() id="goback" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a> </c:if>
        </td>
      </tr>
    </table> 
 	<div id="listDataAJAX"></div>
  </body>
</html>