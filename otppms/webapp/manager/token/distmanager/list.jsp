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
	<script type="text/javascript" src="<%=path%>/manager/common/datepicker/WdatePicker.js"></script>	
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
 	<script src="<%=path%>/manager/common/js/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/token/distmanager/js/list.js"></script>	
		
	<script language="javascript" type="text/javascript">
		// Start,多语言提取
		var langYes = '<view:LanguageTag key="common_syntax_yes"/>';
		var langNo = '<view:LanguageTag key="common_syntax_no"/>';
		var langUndist = '<view:LanguageTag key="tkn_dist_undist"/>';
		var langOndist = '<view:LanguageTag key="tkn_dist_online"/>';
		var langOffdist = '<view:LanguageTag key="tkn_dist_offline"/>';
		
		// 列表
		var tknum_lang = '<view:LanguageTag key="tkn_comm_tknum"/>';
		var username_lang = '<view:LanguageTag key="tkn_username"/>';
		var tkn_code_lang = '<view:LanguageTag key="tkn_mobile_tkn_code"/>';
		var activation_lang = '<view:LanguageTag key="tkn_whether_activation"/>';
		var activation_time_lang = '<view:LanguageTag key="tkn_activation_time"/>';
		var pwd_out_time_lang = '<view:LanguageTag key="tkn_activation_pwd_out_time"/>';
		var dist_type_lang = '<view:LanguageTag key="tkn_dist_type"/>';
		var operate_lang = '<view:LanguageTag key="tkn_operate"/>';
		
		// 操作
		var mobile_tkn_lang = '<view:LanguageTag key="tkn_sure_reset_mobile_tkn"/>';
		var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
		
		var tkn_is_expired = '<view:LanguageTag key="tkn_is_expired"/>';
		var tkn_sure_again_dist= '<view:LanguageTag key="tkn_sure_again_dist"/>';
		var common_syntax_confirm='<view:LanguageTag key="common_syntax_confirm"/>';
		// End,多语言提取
	
		var permSetCode = '';//设定标识码
		var permDist = '';//分发
		var permReset = ''; //重置
		$(document).ready(function() {
			permSetCode = '<view:AdminPermTag key="030401" path="<%=path%>" langKey="tkn_set_code" type="1" />';
			permDist = '<view:AdminPermTag key="030402" path="<%=path%>" langKey="tkn_dist" type="1" />';
			permReset = '<view:AdminPermTag key="030403" path="<%=path%>" langKey="common_syntax_reset" type="1" />';
		});
		

//手机令牌分发
function ifBeDist(token, disted, userName,flag){
	if(flag==1){
		$.ligerDialog.confirm(tkn_sure_again_dist,common_syntax_confirm,function(sel) {
			if(sel) {
				beDist(token, disted, userName);
			}
		});
	}else{
		beDist(token, disted, userName);
	}
}
		

	</script>
  </head>
  
  <body scroll="no" style="overflow:hidden">
   <input type="hidden" name="contextPath" value="<%=path%>" id="contextPath"/> 
   <input type="hidden" name="currentPage" value="${param.currentPage}" id="currentPage"/> 
     <form name="ListForm" id="ListForm" method="post" action="">
    <table width="800" border="0" cellspacing="0" cellpadding="0" style="margin-top:6px; margin-bottom:6px;">
      <tr>
        <td width="133" align="right"><view:LanguageTag key="tkn_username"/><view:LanguageTag key="colon"/></td>
        <td width="168"><input type="text" id="userName" name="distManagerQueryForm.userName" value="${distManagerQueryForm.userName}" class="formCss100"/></td>
        <td width="116" align="right"><view:LanguageTag key="tkn_comm_tknum"/><view:LanguageTag key="colon"/></td>
        <td width="168"><input type="text" id="token" name="distManagerQueryForm.tokenStr" value="${distManagerQueryForm.tokenStr}" class="formCss100"/></td>
        <td width="15">&nbsp;</td>
        <td width="200">&nbsp;</td>
      </tr>
      <tr>
        <td width="133" align="right"><view:LanguageTag key="tkn_activation_begin_time"/><view:LanguageTag key="colon"/></td>
        <td width="168">
        	<input id="startDate" type="text" name="distManagerQueryForm.startTime" value="${distManagerQueryForm.startTime}" onclick ="WdatePicker({lang:'${language_session_key}', dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endDate\')}', position:{left:2}});" readOnly="readOnly" class="formCss100" style="width:168px" />
        </td>
        <td width="116" align="right"><view:LanguageTag key="tkn_activation_end_time"/><view:LanguageTag key="colon"/></td>
        <td width="168">
         <input id="endDate" type="text" name="distManagerQueryForm.endTime" value="${distManagerQueryForm.endTime}" onclick="WdatePicker({lang:'${language_session_key}', dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startDate\')}', position:{left:2}});" readOnly="readOnly" class="formCss100" style="width:168px" />
        </td>
        <td width="15">&nbsp;</td>
        <td width="200"></td>
      </tr>
      <tr>
        <!--  <td width="133" align="right"><view:LanguageTag key="tkn_dist_type"/><view:LanguageTag key="colon"/></td>
        <td width="168">
        	<select name="distManagerQueryForm.provtype" id="tknprovtype"  value="${distManagerQueryForm.provtype}" class="select100" style="width:168px">
		    <option value="-2"><view:LanguageTag key="common_syntax_please_sel"/></option> 
		    <option value="-1" <c:if test="${distManagerQueryForm.provtype == -1}">selected</c:if>><view:LanguageTag key="tkn_dist_undist"/></option>
            <option value="1" <c:if test="${distManagerQueryForm.provtype == 1}">selected</c:if>><view:LanguageTag key="tkn_dist_online"/></option>
            <option value="0" <c:if test="${distManagerQueryForm.provtype == 0}">selected</c:if>><view:LanguageTag key="tkn_dist_offline"/></option>
		  </select>
        </td>-->
        <td width="116" align="right"><view:LanguageTag key="tkn_comm_state_activation"/><view:LanguageTag key="colon"/></td>
        <td width="168">
         <select name="distManagerQueryForm.actived" id="actived"  value="${distManagerQueryForm.actived}" class="select100" style="width:168px">
		    <option value="-1"><view:LanguageTag key="common_syntax_please_sel"/></option>  
            <option value="0" <c:if test="${distManagerQueryForm.actived == 0}">selected</c:if>><view:LanguageTag key="tkn_not_activation"/></option>
            <option value="1" <c:if test="${distManagerQueryForm.actived == 1}">selected</c:if>><view:LanguageTag key="tkn_has_activation"/></option>
		  </select>
        </td>
        <td></td>
        <td></td>
        <td width="15">&nbsp;</td>
        <td width="200"><span style="display:inline-block" class="query-button-css"><a href="javascript:query();" class="isLink_LanSe"><view:LanguageTag key="tkn_query_2"/></a></span></td>
      </tr>
    </table>
     </form>
   <div id="listDataAJAX"></div> 
  </body>
</html>