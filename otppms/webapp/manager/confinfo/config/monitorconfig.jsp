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
    <link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>  
   	<script language="javascript" src="<%=path%>/manager/confinfo/config/js/monitorconfig.js"></script>
   	
	<script language="javascript" type="text/javascript">
	// Start,多语言提取
	var sel_admin_list_lang = '<view:LanguageTag key="monitor_sel_admin_list"/>';
	var need_del_date_lang = '<view:LanguageTag key="common_syntax_check_need_del_date"/>';
	var please_sel_lang = '<view:LanguageTag key="common_vd_please_sel"/>';
	var warning_receiver_lang = '<view:LanguageTag key="monitor_warning_receiver_tip"/>';
	var tkn_exp_lang = '<view:LanguageTag key="monitor_vd_tkn_exp"/>';
	var tkn_exp_err_lang = '<view:LanguageTag key="monitor_vd_tkn_exp_err"/>';
	var unbound_tkn_lang = '<view:LanguageTag key="monitor_vd_unbound_tkn"/>';
	var unbound_tkn_err_lang = '<view:LanguageTag key="monitor_vd_unbound_tkn_err"/>';
	var tkn_one_hour_lang = '<view:LanguageTag key="monitor_vd_tkn_one_hour_sync"/>';
	var one_hour_sync_err_lang = '<view:LanguageTag key="monitor_vd_tkn_one_hour_sync_err"/>';
	var remove_space_lang = '<view:LanguageTag key="org_vd_remove_space_tip"/>';
	var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
	// End,多语言提取
	
	 $(document).ready(function(){
            // 添加校验
 			checkBasicForm();
 			
 			if ('${monitorConfInfo.baseenabled}' == 0) {
 				$("#confTable").hide();
 				cancelVd();
 				
 			}else {
 				$('#selenabled').attr("checked","true");
 				$("#confTable").show();
 				addVd();
 			}
	 }) 
	 
	 function selEnabled() {
	 	if($("#selenabled").attr("checked") == true){
	 		$("#confTable").show();  
			$("#enabled").val("1");
			addVd();
	    }else{ 
	    	$("#confTable").hide();  
			$("#enabled").val("0");
			cancelVd();
		}
	 }
	 
	 function cancelVd() {
	 	$("#baserecvusers").unFormValidator(true); 
	 	$("#baseremaindays").unFormValidator(true); 
	 	$("#baseunbindlower").unFormValidator(true); 
	 	$("#basesyncupper").unFormValidator(true); 
	 	if($.trim($("#baseremaindays").val()) == '') {
		 	$("#baseremaindays").val('30'); 
	 	}
	 	if($.trim($("#baseunbindlower").val()) == '') {
		 	$("#baseunbindlower").val('30'); 
	 	}
	 	if($.trim($("#basesyncupper").val()) == '') {
		 	$("#basesyncupper").val('30'); 
	 	}
	 }
	 function addVd() {
	 	$("#baserecvusers").unFormValidator(false); 
	 	$("#baseremaindays").unFormValidator(false); 
	 	$("#baseunbindlower").unFormValidator(false); 
	 	$("#basesyncupper").unFormValidator(false); 
	 }
	 
	 
	</script>
 </head>
  
 <body>
 <div id="background"  class="background"  style="display: none; "></div>
 <div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
 <input type="hidden" value="<%=path %>" id="contextPath" />
 <form name="monitorForm" id="monitorForm" method="post" action="">
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="topTableBg">
      <tr>
        <td width="98%"><span class="topTableBgText"><view:LanguageTag key="common_menu_config_monitor_conf"/></span></td>
        <td width="2%" align="right">
        </td>
      </tr>
    </table>  
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td width="100%" valign="top">
		   <table id="baseTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
             <tr>
             <td width="30%" align="right" height="20"><view:LanguageTag key="monitor_whether_enable"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
             <td width="30%" align="left" height="20">
				<input type="checkbox" id="selenabled" name="selenabled" onClick="selEnabled();" />
				<input type="hidden" id="enabled" name="monitorConfInfo.baseenabled" value="${monitorConfInfo.baseenabled}" />
             </td>
             <td width="40%" class="divTipCss"><div id="baseenabledTip"></div></td>
            </tr>
            </table>
            <table id="confTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
             <tr>
             <td width="30%" height="20" align="right"><view:LanguageTag key="monitor_method"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
             <td width="30%" height="20" align="left">
             	<div class="tableTDivLeft">
             		<input type="radio" name="monitorConfInfo.basesendtype" id="basesendtype" value="0"
						<c:if test="${monitorConfInfo.basesendtype eq 0}">checked</c:if>/><view:LanguageTag key="monitor_email"/>&nbsp;&nbsp;
					<input type="radio" name="monitorConfInfo.basesendtype" id="basesendtype1" value="1"
					<c:if test="${monitorConfInfo.basesendtype eq 1}">checked</c:if>/><view:LanguageTag key="monitor_sms"/>
             	</div>
           	</td>
             <td width="40%" class="divTipCss"><div id="basesendtypeTip"></div></td>
            </tr>
 			<tr>
             <td align="right" height="20"><view:LanguageTag key="monitor_warning_receiver"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
             <td align="left" height="20">
             	<div class="tableTDivLeft">
             		<select name="monitorConfInfo.baserecvusers" size="5" multiple class="select100" id="baserecvusers">
		             <c:if test='${not empty monitorConfInfo.baserecvusers}'>
			          	 <c:forEach items="${monitorConfInfo.baserecvusers}" var="adminUserId">
			            	<option value="${adminUserId}">${adminUserId}</option>
			          	 </c:forEach>	
		          	 </c:if>
		      		</select>
	            	<a href="#" onClick="selAdmins();" class="button"><span><view:LanguageTag key="common_syntax_select"/></span></a>
	                <a href="#" onClick="delAdmins();" class="button"><span><view:LanguageTag key="common_syntax_delete"/></span></a>
             	</div>
             </td>
             <td class="divTipCss"><div id="baserecvusersTip"></div></td>
            </tr>
			<tr>
             <td align="right" height="20"><view:LanguageTag key="monitor_tkn_expire"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
             <td align="left" height="20">
             	<div class="tableTDivLeft">
             		<input type="text" id="baseremaindays" name="monitorConfInfo.baseremaindays" value="${monitorConfInfo.baseremaindays}" class="formCss100"/>
             	</div>
             </td>
             <td class="divTipCss"><div id="baseremaindaysTip"></div></td>
            </tr>
            <tr>
             	<td align="right" height="20"><view:LanguageTag key="monitor_unbound_tkn_rate"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
             	<td align="left" height="20">
		             <div class="tableTDivLeft">
		              	<input type="text" id="baseunbindlower" name="monitorConfInfo.baseunbindlower" value="${monitorConfInfo.baseunbindlower}" class="formCss100"/>
		            </div>
	            </td>
	            <td class="divTipCss"><div id="baseunbindlowerTip"></div></td>
            </tr>
            <tr>
	             <td align="right" height="20"><view:LanguageTag key="monitor_tkn_within_one_hour_sync_rate"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	             <td align="left" height="20">
	             	<div class="tableTDivLeft">
	             		<input type="text" id="basesyncupper" name="monitorConfInfo.basesyncupper" value="${monitorConfInfo.basesyncupper}" class="formCss100"/>
	             	</div>
	             </td>
	             <td class="divTipCss"><div id="basesyncupperTip"></div></td>
            </tr>
            <tr style="display:none">
             <td align="right" height="20"><view:LanguageTag key="monitor_timer_exec_time_interval"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
             <td align="left" height="20">
             	<div class="tableTDivLeft">
             		<input type="text" id="basetimeinterval" name="monitorConfInfo.basetimeinterval" value="${monitorConfInfo.basetimeinterval}" class="formCss100"/>
             	</div>
             </td>
            </tr>
            </table>
	        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnTable">
	            <tr>
			      	<td width="30%" align="right"></td>
					<td width="70%" colspan="2">
			            <a href="#" id="monitorSave" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
			        </td>
			   </tr>
	 		</table>
	   </td>
      </tr>
    </table>
   </form>
  </body>
</html>