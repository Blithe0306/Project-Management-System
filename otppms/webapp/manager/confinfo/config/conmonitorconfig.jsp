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
    <link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
    <link rel="stylesheet" id="openwincss" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"/>
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
	<script language="javascript" src="<%=path%>/manager/confinfo/config/js/conmonitorconf.js"></script>
	<script language="javascript" type="text/javascript">
	// Start,多语言提取
	var colon = '<view:LanguageTag key="colon"/>';
	var please_sel_lang = '<view:LanguageTag key="common_vd_please_sel"/>';
	var warning_receiver_lang = '<view:LanguageTag key="monitor_warning_receiver_tip"/>';
	var remove_space_lang = '<view:LanguageTag key="org_vd_remove_space_tip"/>';
	var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
    var check_need_del_date_lang = '<view:LanguageTag key="common_syntax_check_need_del_date"/>';
	var tkn_exp_lang = '<view:LanguageTag key="monitor_vd_tkn_exp"/>';
	var tkn_exp_err_lang = '<view:LanguageTag key="monitor_vd_tkn_exp_err"/>';
	var remove_space_tip_lang = '<view:LanguageTag key="org_vd_remove_space_tip"/>';
	var unbound_tkn_lang = '<view:LanguageTag key="monitor_vd_unbound_tkn"/>';
	var unbound_tkn_err_lang = '<view:LanguageTag key="monitor_vd_unbound_tkn_err"/>';
	var one_hour_sync_lang = '<view:LanguageTag key="monitor_vd_tkn_one_hour_sync"/>';
	var one_hour_sync_err_lang = '<view:LanguageTag key="monitor_vd_tkn_one_hour_sync_err"/>';
	var timer_exec_lang = '<view:LanguageTag key="monitor_vd_timer_exec"/>';
	var timer_exec_err_lang = '<view:LanguageTag key="monitor_vd_timer_exec_err"/>';
	var cpu_upper_lang = '<view:LanguageTag key="monitor_vd_cpu_upper"/>';
	var cpu_upper_err_lang = '<view:LanguageTag key="monitor_vd_cpu_upper_err"/>';
	var disk_use_upper_lang = '<view:LanguageTag key="monitor_vd_disk_use_upper"/>';
	var disk_use_upper_err_lang = '<view:LanguageTag key="monitor_vd_disk_use_upper_err"/>';
	var memory_upper_lang = '<view:LanguageTag key="monitor_vd_memory_upper"/>';
	var memory_upper_err_lang = '<view:LanguageTag key="monitor_vd_memory_upper_err"/>';
	var timer_interval_lang = '<view:LanguageTag key="monitor_vd_timer_interval"/>';
	var timer_interval_err_lang = '<view:LanguageTag key="monitor_vd_timer_interval_err"/>';
	var sel_admin_list_lang = '<view:LanguageTag key="monitor_sel_admin_list"/>';
	var compareWarn = '<view:LanguageTag key="auth_ser_vd_compareHostip_err"/>';
	var warnings = '<view:LanguageTag key="common_vd_warning"/>';
	var conn_error = '<view:LanguageTag key="auth_ser_conn_error"/>';
	// End,多语言提取
	$(function() {
			addInit();
			initDisabled();
	 })
	 
	 //校验主服务器是否有效
	 function vaMainIp(){
 		var mainIpAddress = $("#mainIpAddress").val();	
		var mainIpAddressHid = $("#mainIpAddressHid").val();
		
		if(mainIpAddress != mainIpAddressHid) {
			validateHost();
		}else{
			$("#mainIpAddress").inputValidator({min:1,onError:'<view:LanguageTag key="heart_beat_warn_main_ip_err"/>'})
			.functionValidator({fun:checkIpAddr,onError:'<view:LanguageTag key="auth_ser_vd_hostip_err_1"/>'});
		}
	 }
	 
	 //校验主服务器是否有效
	 function validateHost() {
	 	$("#mainIpAddress").ajaxValidator({
			dataType:"html",
			async:true,
			url:"<%=path%>/service/heartbeat!testServerState.action?type=main",
			success:function(msg){
		            if(msg=='success') return true;
					return false;
				},
			buttons:$("#monitorSave"),
			error:function(jqXHR, textStatus, errorThrown){
				$.messager.alert('<view:LanguageTag key="common_vd_warning"/>','<view:LanguageTag key="common_vd_server_is_busy"/>','warning');
			},
			onError:'<view:LanguageTag key="heart_beat_warn_server_no_response"/>',
			onWait:'<view:LanguageTag key="heart_beat_warn_server_if_exists"/>'
		});
	 }
	 
	 //校验从服务器是否有效
	 function vaSpareIp(){
 		var spareIpAddress = $("#spareIpAddress").val();	
		var spareIpAddressHid = $("#spareIpAddressHid").val();
		
		if(spareIpAddress != spareIpAddressHid) {
			validateSpareHost();
		}else{
			$("#spareIpAddress").inputValidator({min:1,onError:'<view:LanguageTag key="heart_beat_warn_spare_ip_err"/>'})
			.functionValidator({fun:checkIpAddr,onError:'<view:LanguageTag key="auth_ser_vd_hostip_err_1"/>'});
		}
	 }
	 
	 //校验从服务器是否有效
	 function validateSpareHost() {
	 	$("#spareIpAddress").ajaxValidator({
			dataType:"html",
			async:true,
			url:"<%=path%>/service/heartbeat!testServerState.action?type=spare",
			success:function(msg){
		            if(msg=='success') return true;
					return false;
				},
			buttons:$("#monitorSave"),
			error:function(jqXHR, textStatus, errorThrown){
				$.messager.alert('<view:LanguageTag key="common_vd_warning"/>','<view:LanguageTag key="common_vd_server_is_busy"/>','warning');
			},
			onError:'<view:LanguageTag key="heart_beat_warn_server_no_response"/>',
			onWait:'<view:LanguageTag key="heart_beat_warn_server_if_exists"/>'
		});
	 }
	 
	 function addInit() {
			$.formValidator.initConfig({submitButtonID:'monitorSave',debug:true,
			onSuccess:function(){
			  saveObj();
			},
			onError:function(){
				return false;
		    }
			});
			
			$("#mainIpAddress").formValidator({onFocus:'<view:LanguageTag key="heart_beat_warn_main_ip_show"/>',onCorrect:"OK"}).inputValidator({min:1,onError:'<view:LanguageTag key="heart_beat_warn_main_ip_err"/>'}).functionValidator({fun:checkIpAddr,onError:'<view:LanguageTag key="auth_ser_vd_hostip_err_1"/>'}); 
			$("#spareIpAddress").formValidator({onFocus:'<view:LanguageTag key="heart_beat_warn_spare_ip_show"/>',onCorrect:"OK"}).inputValidator({min:1,onError:'<view:LanguageTag key="heart_beat_warn_spare_ip_err"/>'}).functionValidator({fun:checkIpAddr,onError:'<view:LanguageTag key="auth_ser_vd_hostip_err_1"/>'});
			$("input[name='monitorConfig.sendtype']").formValidator({tipID:"appsendtypeTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,max:1, onError: '<view:LanguageTag key="common_vd_please_sel"/>'});
		 	$("#adminId").formValidator({onFocus:please_sel_lang,onCorrect:"OK"}).functionValidator({
				fun:function() {
				if($('#adminId option').length < 1) {
					return warning_receiver_lang;
				}
				return true;
			}});
		 	
		 	$('#apptimeinterval').formValidator({onFocus:"<view:LanguageTag key="heart_beat_monitor_err"/>",onCorrect:"OK"}).inputValidator({min:1,max:1440,type:"number",onError:"<view:LanguageTag key="heart_beat_monitor_err_1"/>"}).regexValidator({regExp:"notempty",dataType:"enum",onError:"<view:LanguageTag key="heart_beat_monitor_err_2"/>"}).functionValidator({
		    fun:function(val){
	       	 if(!checkNumber(val)) {return timer_interval_err_lang;}
	       	 return true;
	       	}
	      });
	 }
	 
	 // 初始化是否可以输入方法
	 function initDisabled(){
		 var appEnabled = ${monitorConfig.enabled};
		 if($("#enabled").attr("checked")){
		 	appEnabled = 1;
		 }
		 selEnabled();
	 }
	 
	 
	 function selEnabled() {
	 	if($("#selenabled").attr("checked") == true){
	 		$("#confTable").show();  
			$("#appenabled").val("1");
			$("#mainIpAddress").unFormValidator(false); 
			$("#spareIpAddress").unFormValidator(false); 
			$("input[name='monitorConfig.sendtype']").unFormValidator(false);
			$("#adminId").unFormValidator(false); 
			$("#apptimeinterval").unFormValidator(false); 
	    }else{ 
	    	$("#confTable").hide();  
			$("#appenabled").val("0");
			$("#mainIpAddress").unFormValidator(true); 
			$("#spareIpAddress").unFormValidator(true); 
			$("input[name='monitorConfig.sendtype']").unFormValidator(true); 
			$("#adminId").unFormValidator(true); 
			$("#apptimeinterval").unFormValidator(true); 
		}
	 }
	 
	 //修改核心功能配置
	function saveObj(){
		
		if($("#selenabled").attr('checked')){
			//当主从服务器IP地址相同时提示不允许相同
			var mainIpAddress = $("#mainIpAddress").val();	
			var spareIpAddress = $("#spareIpAddress").val();
		 	if(mainIpAddress == spareIpAddress){
		 		FT.toAlert('warn',compareWarn, warnings);
		 		return false;
		 	}
			
			//校验主从服务器是否可以连接
			//禁用按钮
			document.getElementById("monitorSave").disabled=true;
			$.ajax({
		        type: "post",
		        url: "<%=path%>/service/heartbeat!testServerState.action?type=main&val="+mainIpAddress,
		        datatype: "json",
		        contentType: "application/json; charset=utf-8",
		        success: function(data) {
		            if('success' == data){
						$.ajax({
					        type: "post",
					        url: "<%=path%>/service/heartbeat!testServerState.action?type=spare&val="+spareIpAddress,
					        datatype: "json",
					        contentType: "application/json; charset=utf-8",
					        success: function(data) {
					            if('success' == data){
					            	submitForm();
					            }else if('error' == data){
					            	FT.toAlert('warn',spareIpAddress+colon+conn_error, warnings);
					            	//启用按钮
		    						document.getElementById("monitorSave").removeAttribute("disabled");
					            }
					        }
					    });
		            }else if ('error' == data){
		            	FT.toAlert('warn',mainIpAddress+colon+conn_error, warnings);
		            	//启用按钮
		    			document.getElementById("monitorSave").removeAttribute("disabled");
		            }
		        }
		    });
		}else{
			submitForm();
		}
	}
	//提交表单
	function submitForm(){
		selectObj('adminId');
		var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
	    $('#coreForm').ajaxSubmit({
		    type:"post",
		    async:false,
		    url:contextPath+"/confinfo/config/monitorconfig!modify.action",
		    dataType : "json",
		    success:function(msg){
		      if(msg.errorStr=='success'){
		      	   FT.toAlert(msg.errorStr,msg.object,null);
		      	   // 页面刷新
		           $.ligerDialog.success(msg.object,syntax_tip_lang,resetObj);
	 		  }else{
		          FT.toAlert(msg.errorStr,msg.object,null);
		      }
		      ajaxbg.hide();
		    }
		 });
	}
	</script>
  </head>
  
  <body style="overflow:auto; overflow-x:hidden" >
  <div id="background"  class="background"  style="display: none; "></div>
  <div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
	  <input type="hidden" value="<%=path %>" id="contextPath" />
	  <input id="cPage" type="hidden" value="${param.currentPage}" />
	  <form id="coreForm" method="post" action="">
	    <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="ulOnInsideTable">
	       <tr>
	       	  <td colspan="3" height="25" class="topTableBgText" background="<%=path%>/images/manager/mgr_r7_c8.png"><view:LanguageTag key="common_menu_config_heart_beat_monitor"/></td>
	       </tr>
	   	 </table> 
	   	 
	    <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="ulOnTable">
	      <tr>
	        <td valign="top">
	        	<table id="appTable" width="100%"  border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		             <tr >
		             <td width="30%" align="right"><view:LanguageTag key="monitor_whether_enable"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		             <td align="left" height="20">
		             	<div class="tableTDivLeft">
		             		<input type="checkbox" id="selenabled" name="selenabled" <c:if test="${monitorConfig.enabled eq '1'}">checked="checked"</c:if> onClick="selEnabled();" />
			             	<input type="hidden" name="monitorConfig.enabled" id="appenabled" value="${monitorConfig.enabled}"/>
						</div>
		              </td>
		              <td class="divTipCss"><div id="appenabledTip"></div></td>
		            </tr> 
	            </table>
			    <table id="confTable" width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
			    
			   	 <tr>
	            	<td width="30%" height="20" align="right">
	            	<view:LanguageTag key="heart_beat_warn_main_ip"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
	            	</td>
	            	<td width="30%" height="20" align="left">
	            	<input type="text" id="mainIpAddress" name="monitorConfig.mainIpAddress" value="${monitorConfig.mainIpAddress}" class="formCss100" onblur="vaMainIp();"/>
	            	<input type="hidden" id="mainIpAddressHid"  value="${monitorConfig.mainIpAddress}" />
	            	</td>
	            	<td width="40%" class="divTipCss"><div id="mainIpAddressTip"></div></td>
           		</tr>
           		
           		<tr>
	            <td width="30%" height="20" align="right">
	            	<view:LanguageTag key="heart_beat_warn_spare_ip"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
	            </td>
	            <td width="30%" height="20" align="left">
		            <input type="text" id="spareIpAddress" name="monitorConfig.spareIpAddress" value="${monitorConfig.spareIpAddress}" class="formCss100" onblur="vaSpareIp();"/>
		            <input type="hidden" id="spareIpAddressHid"  value="${monitorConfig.spareIpAddress}" />
	            </td>
	            <td width="40%" class="divTipCss"><div id="spareIpAddressTip"></div></td>
            </tr>
            
            <tr>
	             <td width="30%" height="20" align="right"><view:LanguageTag key="monitor_method"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	             <td width="30%" height="20" align="left">
		             <div class="tableTDivLeft">
						<c:choose>
							<c:when test="${monitorConfig.sendtype eq '0'}">
							<input type="radio" name="monitorConfig.sendtype" id="appsendtype" value="0"	checked/>
							<view:LanguageTag key="monitor_email"/>&nbsp;&nbsp;
							<input type="radio" name="monitorConfig.sendtype" id="appsendtype1" value="1" />
							<view:LanguageTag key="monitor_sms"/>
							</c:when>
							<c:when test="${monitorConfig.sendtype eq '1'}">
								<input type="radio" name="monitorConfig.sendtype" id="appsendtype" value="0"	/>
								<view:LanguageTag key="monitor_email"/>&nbsp;&nbsp;
								<input type="radio" name="monitorConfig.sendtype" id="appsendtype1" value="1" checked/>
								<view:LanguageTag key="monitor_sms"/>
							</c:when>
							<c:otherwise>
								<input type="radio" name="monitorConfig.sendtype" id="appsendtype" value="0"	checked/>
								<view:LanguageTag key="monitor_email"/>&nbsp;&nbsp;
								<input type="radio" name="monitorConfig.sendtype" id="appsendtype1" value="1" />
								<view:LanguageTag key="monitor_sms"/>
							</c:otherwise>
						</c:choose>
	             	</div>
	             </td>
	             <td width="40%" class="divTipCss"><div id="appsendtypeTip"></div></td>
            </tr>
            
            
            <tr>
	             <td align="right" height="20"><view:LanguageTag key="monitor_warning_receiver"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	             <td align="left" height="20">
	             	<div class="tableTDivLeft">
	             		<select name="monitorConfig.adminId" size="5" multiple class="select100" id="adminId">
			             <c:if test='${not empty monitorConfig.adminId}'>
				          	 <c:forEach items="${monitorConfig.adminId}" var="adminUserId">
				            	<option value="${adminUserId}" selected>${adminUserId}</option>
				          	 </c:forEach>
			          	 </c:if>
			      		</select>
		            	<a href="#" onClick="selAdmins();" class="button"><span><view:LanguageTag key="common_syntax_select"/></span></a>
		                <a href="#" onClick="delAdmins();" class="button"><span><view:LanguageTag key="common_syntax_delete"/></span></a>
	             	</div>
	              </td>
	              <td class="divTipCss"><div id="adminIdTip"></div></td>
            </tr>
            
            
             <tr>
	             <td align="right" height="20"><view:LanguageTag key="monitor_timer_interval"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	             <td align="left" height="20">
	             	<div class="tableTDivLeft">
		              <input type="text" id="apptimeinterval" name="monitorConfig.timeInterval" value="${monitorConfig.timeInterval}" class="formCss100"/>
	             	</div>
	              </td>
	              <td class="divTipCss"><div id="apptimeintervalTip"></div></td>
            </tr>
			    </table>
	   		</td>
	   	  </tr>
		</table>  
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
      <tr>
      	<td align="right" width="30%"></td>
		<td align="right">
            <a href="#" id="monitorSave" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
        </td>
        <td align="right"></td>
     </tr>
    </table>  
	</form>
</body>
</html>