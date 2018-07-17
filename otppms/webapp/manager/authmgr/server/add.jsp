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
	<script type="text/javascript" src="<%=path%>/manager/authmgr/server/js/add.js"></script>
	<script language="javascript" type="text/javascript">

	$(function() {
		var hostip = '${serverInfo.hostipaddr}';
		//验证授权服务器允许添加节点数
		if (hostip != '' && hostip != undefined) {
			addInit();
		} else {
			$.ajax({ 
				async:true,
				type:"POST",
				url:"<%=path%>/manager/authmgr/server/authServer!checkLicSerNodes.action",
				dataType:"json",
				success:function(msg){
					if(msg.errorStr == 'success'){
				     	addInit();
				    }else{
				     	$.ligerDialog.warn(msg.object, '<view:LanguageTag key="common_syntax_tip"/>',function(){
				     		window.parent.removeTabItemF('040002');
				     	});
				    }
				}
			});
		}
	 })
		
	 function addInit() {
	 	//窗体大小改变时要重新设置透明文件框的位置
	//	$(window).resize(initFileInputDivNoParame);
		//初始化透明文件框的位置
		//initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv');
	
	
		//绑定点击事件
		domClickEvent();
		//点击按钮弹出窗口，设置窗口样式，取消已绑定点击事件
		var cssurl = "<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"
        btnClickEvent("#addBtn",cssurl);
        setRadioState();
		initSelPort();
		clickFun();
	      $.formValidator.initConfig({submitButtonID:"addBtn",debug:true,
			onSuccess:function(){
			    addObj('${serverInfo.hostipaddr}');
			},
			onError:function(){
				return false;
			}});
	      var hostname = $("#hostname").val();
			$("#hostname").formValidator({onFocus:'<view:LanguageTag key="auth_ser_vd_hostname_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="auth_ser_vd_hostname_err"/>'},onError:'<view:LanguageTag key="auth_ser_vd_hostname_err_1"/>'}).functionValidator({
				fun:function(hostname){
			    if(g_invalid_char_js(hostname)){
			       return "<view:LanguageTag key="auth_ser_vd_hostname_err_2"/>";
			    }
			    return true;
			}}); 
  				
			$("#ipAddr").formValidator({onFocus:'<view:LanguageTag key="auth_ser_vd_hostip_show"/>',onCorrect:"OK"}).inputValidator({min:1,onError:'<view:LanguageTag key="auth_ser_vd_hostip_err"/>'}).functionValidator({fun:checkIpAddr,onError:'<view:LanguageTag key="auth_ser_vd_hostip_err_1"/>'})
			.ajaxValidator({
				dataType:"html",
				async:true,
				url:"<%=path%>/manager/authmgr/server/authServer!findbyIPStr.action",
				success:function(data){
		            if(data=='') return true;
					return false;
				},
				buttons:$("#addBtn"),
				error:function(jqXHR, textStatus, errorThrown){
					$.messager.alert('<view:LanguageTag key="common_vd_warning"/>','<view:LanguageTag key="common_vd_server_is_busy"/>','warning');
				},
				onError:'<view:LanguageTag key="common_vd_already_exists"/>',
				onWait:'<view:LanguageTag key="common_vd_checking_exists"/>'
			});
			$("#selectServId").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError: '<view:LanguageTag key="common_vd_please_sel"/>'});
 			$("#selectId").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,max:2,onError: '<view:LanguageTag key="common_vd_please_sel"/>'});
 			$("#licid").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError: '<view:LanguageTag key="common_vd_please_sel"/>'});
	//		$("#keystoreinstance").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});	
			$("#authport").formValidator({onFocus:'<view:LanguageTag key="auth_ser_vd_authport"/>',onCorrect:"OK"}).inputValidator({min:1025, max:65535, type:"number",onError:'<view:LanguageTag key="auth_ser_vd_port_err"/>'});
			$("#syncport").formValidator({onFocus:'<view:LanguageTag key="auth_ser_vd_syncport"/>',onCorrect:"OK"}).inputValidator({min:1025, max:65535, type:"number",onError:'<view:LanguageTag key="auth_ser_vd_port_err"/>'});
			$("#radauthport").formValidator({onFocus:'<view:LanguageTag key="auth_ser_vd_radius_authport"/>',onCorrect:"OK"}).inputValidator({min:1025, max:65535, type:"number",onError:'<view:LanguageTag key="auth_ser_vd_port_err"/>'});
	//		$("#httpport").formValidator({onFocus:'<view:LanguageTag key="auth_ser_vd_http_port"/>',onCorrect:"OK"}).inputValidator({min:1025, max:65535, type:"number",onError:'<view:LanguageTag key="auth_ser_vd_port_err"/>'});
	//		$("#httpsport").formValidator({onFocus:'<view:LanguageTag key="auth_ser_vd_https_port"/>',onCorrect:"OK"}).inputValidator({min:1025, max:65535, type:"number",onError:'<view:LanguageTag key="auth_ser_vd_port_err"/>'});
			$("#soapport").formValidator({onFocus:'<view:LanguageTag key="auth_ser_vd_soap_port"/>',onCorrect:"OK"}).inputValidator({min:1025, max:65535, type:"number",onError:'<view:LanguageTag key="auth_ser_vd_port_err"/>'});
			$("#webservicename").formValidator({onFocus:'<view:LanguageTag key="auth_ser_vd_webser_show"/>',onCorrect:"OK"}).inputValidator({min:1, max:32, onError:'<view:LanguageTag key="auth_ser_vd_webser"/>'}).regexValidator({regExp:"username",dataType:"enum",onError:'<view:LanguageTag key="auth_ser_vd_webser_err"/>'});
			
			$("input[name='serverInfo.ftradiusenabled']").formValidator({tipID:"ftradiusenabledTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,max:1, onError: '<view:LanguageTag key="common_vd_please_sel"/>'});
			$("input[name='serverInfo.radiusenabled']").formValidator({tipID:"enableradiusTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,max:1, onError: '<view:LanguageTag key="common_vd_please_sel"/>'});
	//		$("input[name='serverInfo.httpenabled']").formValidator({tipID:"enablehttpTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,max:1, onError: '<view:LanguageTag key="common_vd_please_sel"/>'});
	//		$("input[name='serverInfo.httpsenabled']").formValidator({tipID:"enablehttpsTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,max:1, onError: '<view:LanguageTag key="common_vd_please_sel"/>'});

	//		var httpsstate = '${serverInfo.httpsenabled}';
	//		if(httpsstate == 1){
	//			$("#keystorepwd").formValidator({onFocus:'<view:LanguageTag key="auth_ser_vd_serv_key_pwd"/>',onCorrect:"OK"}).inputValidator({min:4,max:16,onError:'<view:LanguageTag key="auth_ser_vd_pwd_err"/>'});
	//	    	$("#certificatepwd").formValidator({onFocus:'<view:LanguageTag key="auth_ser_vd_pri_key_cert_pwd"/>',onCorrect:"OK"}).inputValidator({min:4,max:16,onError:'<view:LanguageTag key="auth_ser_vd_pwd_err"/>'});
	//	    	$("#keystoreinstance").formValidator({onFocus:'<view:LanguageTag key="agent_vd_instance"/>',onCorrect:"OK"}).inputValidator({min:1,onError:'<view:LanguageTag key="agent_vd_instance"/>'});
	//	    	$("#keystorerootpath").formValidator({onFocus:"<view:LanguageTag key='agent_vd_rootpath'/>",onCorrect:"OK"}).inputValidator({min:1,onError:"<view:LanguageTag key='agent_vd_rootpath' />"});
	//		}
			
			$("#descp").formValidator({onFocus:'<view:LanguageTag key="common_vd_descp_show"/>',onCorrect:"OK"}).inputValidator({max:255,onError:'<view:LanguageTag key="common_vd_descp_error"/>'});
			$("#hostname").focus();
			
			// 禁用只读文本框获得焦点时的退格键 
			$(document).keydown(function (e) {
   				var doPrevent=true;
   				if (e.keyCode == 8){
	       			var d = e.srcElement || e.target;
	      			if (d.tagName.toUpperCase() == 'INPUT' || d.tagName.toUpperCase() == 'TEXTAREA') {
	          			doPrevent = d.readOnly || d.disabled;
	
	       			}else{
	       				doPrevent = false;
	       			}
	   			}else{
	       			doPrevent = false;
	   			}
				if (doPrevent){
					e.preventDefault();
	   			}
			});
	 }

	
	 function addObj(ipStr){
	  	var isAdd = true;
		var url = "<%=path%>/manager/authmgr/server/authServer!add.action";
		var cpage = 1;
		if(ipStr != '' && ipStr != null){
			cpage = $("#cPage").val();
			url = "<%=path%>/manager/authmgr/server/authServer!modify.action";
			isAdd = false;
		}
		var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
		$("#addForm").ajaxSubmit({
			async:true,    
			dataType : "json",  
			type:"POST", 
			url : url,
			success:function(msg){
			     if(msg.errorStr == 'success'){
			     	if(isAdd){
						FT.Dialog.confirm('<view:LanguageTag key="common_save_success_continue_add"/>','<view:LanguageTag key="common_syntax_confirm"/>', function(sel){
	           			 	if(sel) {
			    				location.href = "<%=path%>/manager/authmgr/server/add.jsp";
			    			}else{ // 关闭tab页面
			    				window.parent.removeTabItemF('040002');
			    			}
						});
					}else{
						$.ligerDialog.success(msg.object, '<view:LanguageTag key="common_syntax_tip"/>',function(){
				        	location.href = '<%=path%>/manager/authmgr/server/list.jsp?cPage='+cpage;
				     	});
					}
			     }else{
				 	FT.toAlert(msg.errorStr,msg.object,null);
			     }
			     ajaxbg.hide();
			}
		});
	}	
	
	//校验hostname名称
	 function checkHostname(){
			var hostname = $("#hostname").val();	
			var hostnameHid = $("#hostnameHid").val();
			if(hostname != hostnameHid) {
				validateHostname();
			} else {
				$("#hostname").formValidator({onShow:'<view:LanguageTag key="auth_ser_vd_hostname_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="auth_ser_vd_hostname_err"/>'},onError:'<view:LanguageTag key="auth_ser_vd_hostname_err_1"/>'});
			}
	 }
	 
	 //校验hostname是否存在
	 function validateHostname() {
	 	$("#hostname").ajaxValidator({
			dataType:"html",
			async:true,
			url:"<%=path%>/manager/authmgr/server/authServer!findHostnameisExist.action",
			success:function(data){
			   if(data =='false') {return false;}
				return true;
			},
			buttons:$("#addBtn"),
			error:function(jqXHR, textStatus, errorThrown){
				$.messager.alert('<view:LanguageTag key="common_vd_warning"/>','<view:LanguageTag key="common_vd_server_is_busy"/>','warning');
			},
			onError:'<view:LanguageTag key="common_vd_already_exists"/>',
			onWait:'<view:LanguageTag key="common_vd_checking_exists"/>'
		});
	 }
	 
	 //初始化单选按钮是否选中，来显示隐藏
	 function initSelPort() {
	 	var ftradstate = '${serverInfo.ftradiusenabled}';
	 	var radstate = '${serverInfo.radiusenabled}';
	// 	var httpstate = '${serverInfo.httpenabled}';
	// 	var httpsstate = '${serverInfo.httpsenabled}';
		if (ftradstate != '' && ftradstate != undefined) {
		 	if (ftradstate == 0) {
		 		$("tr[id='authportTR']").hide();
	        	$("tr[id='syncportTR']").hide();
		 	}
		 	if (ftradstate == 1) {
		 		$("tr[id='authportTR']").show();
	        	$("tr[id='syncportTR']").show();
		 	}
		 } else {
		 	$("tr[id='authportTR']").show();
	        $("tr[id='syncportTR']").show();
		 }
		 	
		 if ('' != radstate && undefined != radstate) {
		 	if (radstate == 0) $("tr[id='radauthportTR']").hide();
		 	if (radstate == 1) $("tr[id='radauthportTR']").show();
		 }else { $("tr[id='radauthportTR']").show(); }
		 	
	//	 if ('' != httpstate && undefined != httpstate) {
	//	 	if (httpstate == 0) $("tr[id='httpportTR']").hide();
	//	 	if (httpstate == 1) $("tr[id='httpportTR']").show();
	//	 }else { $("tr[id='httpportTR']").hide(); }
		 
	//	 if ('' != httpsstate && undefined != httpsstate) {
	//	 	if (httpsstate == 0) {
	//	 		$("tr[id='httpsportTR']").hide();
	//	        $("tr[id='keypwdTR']").hide();
	//	        $("tr[id='cerpwdTR']").hide();
	//	        $("tr[id='keystanceTR']").hide();
	//	        $("tr[id='keypathTR']").hide();
	//	 	}
	//	 	if (httpsstate == 1) {
	//	 		$("tr[id='httpsportTR']").show();
	//	     	$("tr[id='keypwdTR']").show();
	//	     	$("tr[id='cerpwdTR']").show();
	//	     	$("tr[id='keystanceTR']").show();
	//	     	$("tr[id='keypathTR']").show();
	//	 	}
	//	 }else { 
	//	 	$("tr[id='httpsportTR']").hide();
	//	    $("tr[id='keypwdTR']").hide();
	//	    $("tr[id='cerpwdTR']").hide();
	//	    $("tr[id='keystanceTR']").hide();
	//	    $("tr[id='keypathTR']").hide();
	//	 }
	 }
	 
	 //点击单选按钮显示隐藏
	 function clickFun() {
	 	$(":radio[name^='serverInfo.ftradiusenabled']").click(function(e){
	 		if($(this).val() === '1') { 
	 			$("tr[id='authportTR']").show();
	        	$("tr[id='syncportTR']").show();
	 		} else {
	 			$("tr[id='authportTR']").hide();
	        	$("tr[id='syncportTR']").hide();
	        	document.getElementById("authport").value = '1915';
	        	document.getElementById("syncport").value = '1916';
	 		}
	 	});
	 	$(":radio[name^='serverInfo.radiusenabled']").click(function(e){
	 		if($(this).val() === '1') $("tr[id='radauthportTR']").show();
		    else {
		    	$("tr[id='radauthportTR']").hide();
		    	document.getElementById("radauthport").value = '1812';
		    }
	 	});
	// 	$(":radio[name^='serverInfo.httpenabled']").click(function(e){
	// 		if($(this).val() === '1') $("tr[id='httpportTR']").show();
	// 		else {
	// 			$("tr[id='httpportTR']").hide();
	// 			document.getElementById("httpport").value = '18080';
	// 		}
	// 	});
		$(":radio[name^='serverInfo.httpsenabled']").click(function(e){
			if($(this).val() === '1') { 
	//			$("tr[id='httpsportTR']").show();
	//	     	$("tr[id='keypwdTR']").show();
	//	     	$("tr[id='cerpwdTR']").show();
	//	     	$("tr[id='keystanceTR']").show();
	//	     	$("tr[id='keypathTR']").show();

	//	     	$("#keystorepwd").formValidator({onFocus:'<view:LanguageTag key="auth_ser_vd_serv_key_pwd"/>',onCorrect:"OK"}).inputValidator({min:4,max:16,onError:'<view:LanguageTag key="auth_ser_vd_pwd_err"/>'});
	//	    	$("#certificatepwd").formValidator({onFocus:'<view:LanguageTag key="auth_ser_vd_pri_key_cert_pwd"/>',onCorrect:"OK"}).inputValidator({min:4,max:16,onError:'<view:LanguageTag key="auth_ser_vd_pwd_err"/>'});
	//	    	$("#keystoreinstance").formValidator({onFocus:'<view:LanguageTag key="agent_vd_instance"/>',onCorrect:"OK"}).inputValidator({min:1,onError:'<view:LanguageTag key="agent_vd_instance"/>'});
	//	    	$("#keystorerootpath").formValidator({onFocus:"<view:LanguageTag key='agent_vd_rootpath'/>",onCorrect:"OK"}).inputValidator({min:1,onError:"<view:LanguageTag key='agent_vd_rootpath' />"});
			}else {
	//			$("tr[id='httpsportTR']").hide();
	//	        $("tr[id='keypwdTR']").hide();
	//	        $("tr[id='cerpwdTR']").hide();
	//	        $("tr[id='keystanceTR']").hide();
	//	        $("tr[id='keypathTR']").hide();

	//	        document.getElementById("httpsport").value = '18443';
	//	        document.getElementById("keystorepwd").value = '';
	//	        document.getElementById("certificatepwd").value = '';
	//	        document.getElementById("keystoreinstance").value = '';

	//	        $("#keystorepwd").formValidator({onFocus:'<view:LanguageTag key="auth_ser_vd_serv_key_pwd"/>',onCorrect:"OK"});
	//	    	$("#certificatepwd").formValidator({onFocus:'<view:LanguageTag key="auth_ser_vd_pri_key_cert_pwd"/>',onCorrect:"OK"});
	//	    	$("#keystoreinstance").formValidator({onFocus:'<view:LanguageTag key="agent_vd_instance"/>',onCorrect:"OK"});
	//	    	$("#keystorerootpath").formValidator({onFocus:"<view:LanguageTag key='agent_vd_rootpath'/>",onCorrect:"OK"});
			}
		});
	 }
	 //根据单选按钮状态隐藏显示属性
	 function setRadioState() {
		var ftradstate = '${serverInfo.ftradiusenabled}';
		var radstate = '${serverInfo.radiusenabled}';
	//	var httpstate = '${serverInfo.httpenabled}';
	//	var httpsstate = '${serverInfo.httpsenabled}';
		if (ftradstate != '' && ftradstate != undefined) {
		 	if (ftradstate == 0) $("input[name='serverInfo.ftradiusenabled'][value=0]").attr("checked",true); 
		 	if (ftradstate == 1) $("input[name=serverInfo.ftradiusenabled][value=1]").attr("checked",true); 
		} else { $("input[name=serverInfo.ftradiusenabled][value=1]").attr("checked",true); }
		 	
		if ('' != radstate && undefined != radstate) {
		 	if (radstate == 0) $("input[name=serverInfo.radiusenabled][value=0]").attr("checked",true); 
		 	if (radstate == 1) $("input[name=serverInfo.radiusenabled][value=1]").attr("checked",true); 
		}else { $("input[name=serverInfo.radiusenabled][value=1]").attr("checked",true); }
		 	
	//	if ('' != httpstate && undefined != httpstate) {
	//	 	if (httpstate == 0) $("input[name=serverInfo.httpenabled][value=0]").attr("checked",true); 
	//	 	if (httpstate == 1) $("input[name=serverInfo.httpenabled][value=1]").attr("checked",true); 
	//	}else { $("input[name=serverInfo.httpenabled][value=0]").attr("checked",true); }
		 
	//	if ('' != httpsstate && undefined != httpsstate) {
	//	 	if (httpsstate == 0) $("input[name=serverInfo.httpsenabled][value=0]").attr("checked",true); 
	//	 	if (httpsstate == 1) $("input[name=serverInfo.httpsenabled][value=1]").attr("checked",true); 
	//	}else { $("input[name=serverInfo.httpsenabled][value=0]").attr("checked",true); }
	}
	 
	 
	 //返回操作
	 function goBack() {
		var currentPage = $("#cPage").val();
		window.location.href = '<%=path%>/manager/authmgr/server/list.jsp?currentPage=' + currentPage;
	 }	

	</script>
  </head>
  
  <body style="overflow:auto; overflow-x:hidden" >
  <div id="background"  class="background"  style="display: none; "></div>
  <div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
	  <input type="hidden" value="<%=path %>" id="contextPath" />
	  <input id="cPage" type="hidden" value="${param.currentPage}" />
	  <form id="addForm" method="post" action="">
	    <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="topTableBg">
	      <tr>
	        <td width="98%">  
			  <span class="topTableBgText"><c:if test='${empty serverInfo.hostipaddr}'><view:LanguageTag key="auth_ser_add"/></c:if>
	          <c:if test='${not empty serverInfo.hostipaddr}'><view:LanguageTag key="auth_ser_edit"/></c:if></span>
	        </td>
	        <td width="2%" align="right">
	      	 	<!--<a href="javascript:openHelpWin('<%=path%>/manager/help/confighelp.html#371','<%=path%>/manager/common/js/ligerUI/skins/Gray/css/dialog.css');">
	      		<img src="<%=path%>/images/icon/help_1.png" width="16" height="16" hspace="5" title='<view:LanguageTag key="common_syntax_help"/>' align="absmiddle" />
	      		</a>-->
	      	</td>
	      </tr>
	    </table>  
	    <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="ulOnTable">
	      <tr>
	        <td valign="top">
			    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
			      <tr>
			        <td width="30%" align="right"><view:LanguageTag key="auth_ser_hostname"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td width="30%">
			        	<input type="text" id="hostname" name="serverInfo.hostname" value="${serverInfo.hostname}" onchange="checkHostname();" class="formCss100" />
			        	<input type="hidden" id="hostnameHid" value="${serverInfo.hostname}"  />
					</td>
			        <td width="40%" class="divTipCss"><div id="hostnameTip"></div></td>
			      </tr>
			      <tr>
			        <td align="right"><view:LanguageTag key="auth_ser_hostip"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td>
				        <c:if test="${empty serverInfo.hostipaddr}">
				        <input type="text" name="serverInfo.hostipaddr" class="formCss100" id="ipAddr" value="${serverInfo.hostipaddr}" />
				        </c:if>
				        <c:if test="${not empty serverInfo.hostipaddr}">
				       		${serverInfo.hostipaddr}
				       		<input type="hidden" name="serverInfo.hostipaddr" class="formCss100" id="hostipAddr" value="${serverInfo.hostipaddr}" />
				        </c:if>
			        </td>
			        <td class="divTipCss"><div id="ipAddrTip"></div></td>
			      </tr>
			      <tr>
			        <td align="right"><view:LanguageTag key="auth_ser_priority"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td> 
					  <select id="selectId" name="serverInfo.priority" class="select100">
					    <view:ServerAuthPriorityTag dataSrc="${serverInfo.priority}"></view:ServerAuthPriorityTag>
					  </select>
					</td>
			        <td class="divTipCss"><div id="selectIdTip"></div></td>
			      </tr> 
			      <tr>
			        <td align="right"><view:LanguageTag key="auth_ser_lic_file"/><view:LanguageTag key="colon"/></td>
			        <td> 
					    <view:LicNameTag dataSrc="${serverInfo.licid}"></view:LicNameTag>
					</td>
			      </tr> 
			       <tr>
			        <td align="right"><view:LanguageTag key="auth_ser_ftradius_enabled"/><view:LanguageTag key="colon"/></td>
			        <td>
				        <input type="radio" id="ftradiusenabled1" name="serverInfo.ftradiusenabled" value="1" 
				        	<c:if test="${serverInfo.ftradiusenabled eq 1 }">checked</c:if>
				        /><view:LanguageTag key="common_syntax_yes"/>
			        	<input type="radio" id="ftradiusenabled0" name="serverInfo.ftradiusenabled" value="0" 
				        	<c:if test="${serverInfo.ftradiusenabled eq 0 }">checked</c:if>
				        /><view:LanguageTag key="common_syntax_no"/>
					</td>
			        <td class="divTipCss"><div id="ftradiusenabledTip"></div></td>
			      </tr>
			      <tr id="authportTR">
			        <td align="right"><view:LanguageTag key="auth_ser_authport"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td>
			        	<input type="text" id="authport" name="serverInfo.authport" value="${empty serverInfo.authport ? "1915" : serverInfo.authport}" class="formCss100" />
					</td>
			        <td class="divTipCss"><div id="authportTip"></div></td>
			      </tr>
			      <tr id="syncportTR">
			        <td align="right"><view:LanguageTag key="auth_ser_syncport"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td>
			        	<input type="text" id="syncport" name="serverInfo.syncport" value="${empty serverInfo.syncport ? "1916" : serverInfo.syncport}" class="formCss100" />
					</td>
			        <td class="divTipCss"><div id="syncportTip"></div></td>
			      </tr>
			     
			      <tr>
			        <td align="right"><view:LanguageTag key="auth_ser_radius_enabled"/><view:LanguageTag key="colon"/></td>
			        <td>
				        <input type="radio" id="enableradius1" name="serverInfo.radiusenabled" value="1"
				        	<c:if test="${serverInfo.radiusenabled eq 1 }">checked</c:if>
				        /><view:LanguageTag key="common_syntax_yes"/>
			        	<input type="radio" id="enableradius0" name="serverInfo.radiusenabled" value="0" 
				        	<c:if test="${serverInfo.radiusenabled eq 0 }">checked</c:if>
				        /><view:LanguageTag key="common_syntax_no"/>
					</td>
			        <td class="divTipCss"><div id="enableradiusTip"></div></td>
			      </tr>
			      <tr id="radauthportTR">
			        <td align="right"><view:LanguageTag key="auth_ser_radius_authport"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td>
			        	<input type="text"  id="radauthport" name="serverInfo.radauthport" value="${empty serverInfo.radauthport ? "1812" : serverInfo.radauthport}" class="formCss100" />
					</td>
			        <td class="divTipCss"><div id="radauthportTip"></div></td>
			      </tr>
			   <!--    <tr>
			        <td align="right"><view:LanguageTag key="auth_ser_http_enabled"/><view:LanguageTag key="colon"/></td>
			        <td>
				        <input type="radio" id="enablehttp1" name="serverInfo.httpenabled" value="1"  
				        	<c:if test="${serverInfo.httpenabled eq 1 }">checked</c:if>
				        /><view:LanguageTag key="common_syntax_yes"/>
			        	<input type="radio" id="enablehttp0" name="serverInfo.httpenabled" value="0"
				        	<c:if test="${serverInfo.httpenabled eq 0 }">checked</c:if>
				        /><view:LanguageTag key="common_syntax_no"/>
					</td>
			        <td class="divTipCss"><div id="enablehttpTip"></div></td>
			      </tr>
			      <tr id="httpportTR">
			        <td align="right"><view:LanguageTag key="auth_ser_http_port"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td>
			        	<input type="text"  id="httpport" name="serverInfo.httpport" value="${empty serverInfo.httpport ? "18080" : serverInfo.httpport}" class="formCss100" />
					</td>
			        <td class="divTipCss"><div id="httpportTip"></div></td>
			      </tr>-->
			    <!--   <tr>
			        <td align="right"><view:LanguageTag key="auth_ser_https_enabled"/><view:LanguageTag key="colon"/></td>
			        <td>
				        <input type="radio" id="enablehttps1" name="serverInfo.httpsenabled" value="1"  
				        	<c:if test="${serverInfo.httpsenabled eq 1 }">checked</c:if>
				        /><view:LanguageTag key="common_syntax_yes"/>
			        	<input type="radio" id="enablehttps0" name="serverInfo.httpsenabled" value="0"
				        	<c:if test="${serverInfo.httpsenabled eq 0 }">checked</c:if>
				        /><view:LanguageTag key="common_syntax_no"/>
					</td>
			        <td class="divTipCss"><div id="enablehttpsTip"></div></td>
			      </tr>-->
			    <!--   <tr id="httpsportTR">
			        <td align="right"><view:LanguageTag key="auth_ser_https_port"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td>
			        	<input type="text"  id="httpsport" name="serverInfo.httpsport" value="${empty serverInfo.httpsport ? "18443" : serverInfo.httpsport}" class="formCss100" />
					</td>
			        <td class="divTipCss"><div id="httpsportTip"></div></td>
			      </tr>-->
			    <!--  <tr id="keypwdTR">
			        <td align="right"><view:LanguageTag key="auth_ser_serv_key_pwd"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td>
			        	<input type="password"  id="keystorepwd" name="serverInfo.keystorepwd" value="${serverInfo.keystorepwd}" class="formCss100" />
					</td>
			        <td class="divTipCss"><div id="keystorepwdTip"></div></td>
			      </tr>-->
			    <!--   <tr id="cerpwdTR">
			        <td align="right"><view:LanguageTag key="auth_ser_private_key_cert_pwd"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td>
			        	<input type="password"  id="certificatepwd" name="serverInfo.certificatepwd" value="${serverInfo.certificatepwd}" class="formCss100" />
					</td>
			        <td class="divTipCss"><div id="certificatepwdTip"></div></td>
			      </tr>-->
			   <!--   <tr id="keystanceTR">
	                <td align="right"><view:LanguageTag key="auth_ser_serv_cer_type"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	                <td>
	                	<select id="keystoreinstance" name="serverInfo.keystoreinstance" class="select100">
					  		<option value="" selected><view:LanguageTag key="common_syntax_please_sel"/></option>
					  		<option value="PKCS12" <c:if test="${serverInfo.keystoreinstance eq 'PKCS12'}">selected="selected"</c:if>>PKCS12</option>
					  		<option value="JKS" <c:if test="${serverInfo.keystoreinstance eq 'JKS'}">selected="selected"</c:if>>JKS</option>
			    	  	</select>
			    	</td>
	                <td class="divTipCss"><div id="keystoreinstanceTip"></div></td>
	              </tr>-->
	           <!--   <tr id="keypathTR">
			        <td align="right"><view:LanguageTag key="auth_ser_serv_certificate"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
					<td id="fileTd">
			        	<input type="text" id="showVal" class="formCss100" onresize="initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv')"/>
				        &nbsp;&nbsp;<div style="z-index:10;display: inline;"><span class="Button" id="showDiv"><a id="setShowValInp" href="javascript:"><view:LanguageTag key="common_syntax_select"/></a></span></div>
				        <div id="fileDiv" style="position:absolute;margin:0 0 0 -38px;z-index:50;filter:alpha(opacity=0);moz-opacity: 0;-khtml-opacity: 0;opacity:0;">
				        <input name="serverInfo.keystorerootpath" id="keystorerootpath"  type="file" size="1" style="height: 24px;" onchange="setFullPath(this,'showVal');"/></div>
			        </td>
					
			        <td class="divTipCss"><div id="keystorerootpathTip"></div></td>
			      </tr>-->
	              <tr style="display:none">
			        <td align="right"><view:LanguageTag key="auth_ser_soap_enabled"/><view:LanguageTag key="colon"/></td>
			        <td>
				        <input type="radio" id="enablesoap1" name="serverInfo.soapenabled" value="1" checked /><view:LanguageTag key="common_syntax_yes"/>
					</td>
			        <td class="divTipCss"><div id="enablesoapTip"></div></td>
			      </tr>
			      <tr>
			        <td align="right"><view:LanguageTag key="auth_ser_soap_port"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td>
			        	<input type="text"  id="soapport" name="serverInfo.soapport" value="${empty serverInfo.soapport ? "18081" : serverInfo.soapport}" class="formCss100" />
					</td>
			        <td class="divTipCss"><div id="soapportTip"></div></td>
			      </tr>
			      <tr>
			        <td align="right"><view:LanguageTag key="auth_ser_webservicename"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td>
			        	<input type="text"  id="webservicename" name="serverInfo.webservicename" value="${empty serverInfo.webservicename ? "otpwebservice" : serverInfo.webservicename}" class="formCss100" />
					</td>
			        <td class="divTipCss"><div id="webservicenameTip"></div></td>
			      </tr>
			      <tr>
			        <td align="right"><view:LanguageTag key="common_syntax_desc"/><view:LanguageTag key="colon"/></td>
			        <td><textarea id="descp" name="serverInfo.descp" class="textarea100">${serverInfo.descp}</textarea></td>
			        <td class="divTipCss"><div id="descpTip"></div></td>
			      </tr>
			      <tr>
			        <td align="right"></td>
			        <td> 
			        	<a href="#" id="addBtn" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
			            <c:if test='${not empty serverInfo.hostipaddr}'><a href="javascript:goBack();"   class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a></c:if>
			        </td>
			        <td></td>
			      </tr> 
			    </table>
	   		</td>
	   	  </tr>
		</table>    
	</form>
</body>
</html>