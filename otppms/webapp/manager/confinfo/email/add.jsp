<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld"%>
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
<script language="javascript" type="text/javascript">
	var isconn = false;
	$(document).ready(function(){
		//绑定点击事件
		domClickEvent();
		//点击按钮弹出窗口，设置窗口样式，取消已绑定点击事件
		var cssurl = "<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"
        btnClickEvent("#addBtn",cssurl);
		
		$.formValidator.initConfig({submitButtonID:"addBtn", 
			onSuccess:function(){
				if(isconn) {
					isconn = false;
					testconn();
				}else {
					addObj();
				}
			},
			onError:function(){
				return false;
			}});
			var servname = $("#servname").val();
			$("#servname").formValidator({onFocus:'<view:LanguageTag key="email_vd_servname_show"/>',onCorrect:"OK"})
			.inputValidator({min:1,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="email_vd_servername_empty_err"/>'},onError:'<view:LanguageTag key="email_vd_servname_err_2"/>'})
			.functionValidator({
 				fun:function(servname){
			    	if(g_invalid_char_js(servname)){
			       		return '<view:LanguageTag key="email_vd_servname_err_1"/>';
			    	}
			    	return true;
				}});
			$("#host").formValidator({onFocus:'<view:LanguageTag key="email_vd_host_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:64, onError:'<view:LanguageTag key="email_vd_host_err"/>'}).regexValidator({regExp:"^[A-Za-z0-9]+(\.[A-Za-z0-9]+)+$",dataType:"string",onError:'<view:LanguageTag key="email_vd_host_err_1"/>'});
			$("#account").formValidator({onFocus:'<view:LanguageTag key="email_vd_account_show"/>',onCorrect:"OK"}).inputValidator({min:4,max:255,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="email_vd_account_err"/>'}, onError:'<view:LanguageTag key="email_vd_account_err_1"/>'}).regexValidator({regExp:"email",dataType:"enum",onError:'<view:LanguageTag key="email_vd_account_err_2"/>'});
			$("#password").formValidator({onFocus:'<view:LanguageTag key="common_vd_pwd_show1"/>',onCorrect:"OK"}).inputValidator({min:4,max:64, onError:'<view:LanguageTag key="common_vd_pwd_show1"/>'});
			$("#pwdconfirm").formValidator({onFocus:'<view:LanguageTag key="common_vd_confirm_pwd_show1"/>',onCorrect:"OK"}).inputValidator({min:4,max:64, onError:'<view:LanguageTag key="common_vd_pwd_show1"/>'}).compareValidator({desID:"password",operateor:"=",onError:'<view:LanguageTag key="common_vd_confirm_pwd_err"/>'}); 
			$("#sender").formValidator({onFocus:'<view:LanguageTag key="email_vd_sender_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="email_vd_sender_err"/>'}, onError:'<view:LanguageTag key="email_vd_sender_err_1"/>'});
				
			$("#port").formValidator({onFocus:'<view:LanguageTag key="email_vd_port_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:65535, type:"number", onError:'<view:LanguageTag key="email_vd_port_err"/>'});
			$("input[name='emailConf.validate']").formValidator({tipID:"validateTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,max:1, onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
			$("#descp").formValidator({onFocus:'<view:LanguageTag key="common_vd_descp_show"/>',onCorrect:"OK"}).inputValidator({max:255,onError:'<view:LanguageTag key="common_vd_descp_error"/>'});
			$("#servname").focus();
		});
		
		function checkInit(obj){
			var id = $("#id").val();
			var host = $("#host").val();
			var hidhost = $("#emailhost").val();			
			var servname = $("#servname").val();			
			var hidservname = $("#emailservname").val();			
			if(obj == 0){
			    if(servname != hidservname) {
					validateOTNameUtil();
				}else {
					if (id == null || "" == id) {
						validateOTNameUtil();
					} else {
						$("#servname").formValidator({onFocus:'<view:LanguageTag key="email_vd_servname_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:32,onError:'<view:LanguageTag key="email_vd_servname_err"/>'}).regexValidator({regExp:"^[a-zA-Z0-9\u4e00-\u9fa5]+$",dataType:"string",onError:'<view:LanguageTag key="email_vd_servname_err_1"/>'});
					}
				}
			}else{
				if(host != hidhost) {
					validateOTHostUtil();
				}else {
					if (id == null || "" == id) {
						validateOTHostUtil();
					} else {
						$("#host").formValidator({onFocus:'<view:LanguageTag key="email_vd_host_show"/>',onCorrect:"OK"}).inputValidator({min:4,max:20, onError:'<view:LanguageTag key="email_vd_host_err"/>'}).functionValidator({
			 				fun:function(realName){
					    	if(checkIp(realName)){
					       		return '<view:LanguageTag key="email_vd_host_err_1"/>';
					    	}
					    	return true;
						}});
					}
				}
			}
		}
		
		//服务器端进行邮箱服务器名称校验方法
		function validateOTNameUtil() {
			$("#servname").ajaxValidator({
				dataType:"html",
				async:true,
				url:"<%=path%>/manager/confinfo/email/email!findOENameisExist.action",
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
		
		//服务器端进行邮箱服务器校验方法
		function validateOTHostUtil() {
		 	$("#host").ajaxValidator({
				dataType:"html",
				async:true,
				url:"<%=path%>/manager/confinfo/email/email!findOEisExist.action",
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
		//新增一个邮箱服务器
		function addObj(){
			var id = $("#id").val();
			var isdefault = $("#isdefault").val();
		 	var url = '<%=path%>/manager/confinfo/email/email!add.action';
		 	if("" != id){
				url = "<%=path%>/manager/confinfo/email/email!modify.action?isdefault="+isdefault;
		 	}
		 	
			$("#addEmailForm").ajaxSubmit({
				async : false,  
				type : "POST", 
				url : url,
				dataType : "json",
				success : function(msg){
					if(msg.errorStr=='error' || msg.errorStr=='warn'){
				   		FT.toAlert(msg.errorStr, msg.object, null);
				   	}else{
				     	$.ligerDialog.success(msg.object, '<view:LanguageTag key="common_syntax_tip"/>',function(){
				             window.location.href="<%=path%>/manager/confinfo/email/list.jsp";
				  		});
				   	}
				}
			});		  
	 	}
	 	
	 	function connemail() {
	 		isconn = true;
	 		$('#addBtn').triggerHandler("click");
	 	}
	 	
	 	//测试连接邮件
	 	function testconn() {
	 		var url = '<%=path%>/manager/confinfo/email/email!emailConnTest.action';
	 		var ajaxbg = $("#background,#progressBar");//加载等待
			ajaxbg.show();
	 		$("#addEmailForm").ajaxSubmit({
				async : true,  
				type : "POST", 
				url : url,
				dataType : "html",
				success : function(msg){
					if (msg == 1) {
						FT.toAlert('error', '<view:LanguageTag key="common_conn_error_tip"/>', null);
					}else if (msg == 0){
						FT.toAlert('success', '<view:LanguageTag key="common_conn_succ_tip"/>', null);
					}
					ajaxbg.hide();
				}
			});		  
	 	}
	 	
	 	//返回操作
		function goBack() {
			window.location.href="<%=path%>/manager/confinfo/email/list.jsp";
		}
	 	
	</script>
	
</head>
<body style="overflow:auto; overflow-x:hidden">
<div id="background"  class="background"  style="display: none; "></div>
<div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
<input name="currentPage" id="currentPage" type="hidden" value="${param.cPage }" />
<input id="contextPath" type="hidden" value="<%=path%>" />
<form id="addEmailForm" method="post" action="">
  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="topTableBg">
    <tr>
      <td width="98%"> 
        <span class="topTableBgText"> 
	        <c:if test="${not empty emailInfo.id}"><view:LanguageTag key="email_serv_edit"/></c:if>
	        <c:if test="${empty emailInfo.id}"><view:LanguageTag key="email_serv_add"/></c:if>
        </span>
      </td>
      <td width="2%" align="right">
      <!--	<a href="javascript:openHelpWin('<%=path%>/manager/help/confighelp.html#385','<%=path%>/manager/common/js/ligerUI/skins/Gray/css/dialog.css');">
      		<img src="<%=path%>/images/icon/help_1.png" width="16" height="16" hspace="5" title='<view:LanguageTag key="common_syntax_help"/>' align="absmiddle" />
      	</a>-->
      </td>
    </tr>
  </table>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnTable">
    <tr>
     <td valign="top">
	   <table width="100%" border="0"  cellpadding="0" cellspacing="0">
	      <tr>
	      <td width="25%" align="right"><view:LanguageTag key="email_servname"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	      <td width="35%">
	          <input type="text" id="servname" name="emailInfo.servname" value="${emailInfo.servname}" onchange="checkInit(0);" class="formCss100" />
	     	  <input type="hidden" id="emailservname" value="${emailInfo.servname}" />
	      </td>
	      <td width="40%" class="divTipCss"><div id="servnameTip"></div></td>
	    </tr>
	    <tr>
	      <td align="right"><view:LanguageTag key="email_host"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	      <td>
	          <input type="text" id="host" name="emailInfo.host" value="${emailInfo.host}" onchange="checkInit(1);" class="formCss100"/>
	          <input type="hidden" id="emailhost" value="${emailInfo.host}" />
	      </td>
	      <td class="divTipCss"><div id="hostTip"></div></td>
	    </tr>
	    <tr>
	      <td align="right"><view:LanguageTag key="email_port"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	      <td>
	        <input type="text" id="port" name="emailInfo.port" value='${empty emailInfo.port ? "25" : emailInfo.port}' class="formCss100"/>
	      </td>
	      <td class="divTipCss"><div id="portTip"></div></td>
	    </tr>
	    <tr>
	      <td align="right"><view:LanguageTag key="email_account"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	      <td>
	        <input type="text" id="account" name="emailInfo.account" value="${emailInfo.account}" class="formCss100"/>
	      </td>
	      <td class="divTipCss"><div id="accountTip"></div></td>
	    </tr>
	    <tr>
	      <td align="right"><view:LanguageTag key="email_pwd"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td> 
	      <td>
	        <input type="password" id="password" name="emailInfo.pwd" value="${emailInfo.pwd}" class="formCss100"/>
	      </td>
	      <td class="divTipCss"><div id="passwordTip"></div></td>
	    </tr>
	    <tr>
	      <td align="right"><view:LanguageTag key="email_confm_pwd"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td> 
	      <td>
	        <input type="password" id="pwdconfirm"  value="${emailInfo.pwd}" class="formCss100"/>
	      </td>
	      <td class="divTipCss"><div id="pwdconfirmTip"></div></td>
	    </tr>
	    <tr>
	      <td align="right"><view:LanguageTag key="email_sender"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	      <td>
	        <input type="text" id="sender" name="emailInfo.sender" value="${emailInfo.sender}" class="formCss100"/>
	      </td>
	      <td class="divTipCss"><div id="senderTip"></div>
	      </td>
	    </tr>
	    <tr style="display:none">
	      <td align="right"><view:LanguageTag key="email_is_validate_acc"/><view:LanguageTag key="colon"/></td>
	      <td> 
	      	<input type="radio" id="validate1" name="emailInfo.validated" value="1" checked
	        
	        />
	        <view:LanguageTag key="common_syntax_yes"/>
	      	<input type="radio" id="validate0" name="emailInfo.validated" value="0" 
	        	
	        />
	        <view:LanguageTag key="common_syntax_no"/>
	      </td>
	      <td class="divTipCss"><div id="validateTip"></div></td>
	    </tr>
	    <tr>
	    	<td align="right"><view:LanguageTag key="common_syntax_desc"/><view:LanguageTag key="colon"/></td>
			<td><textarea id="descp" name="emailInfo.descp" class="textarea100">${emailInfo.descp}</textarea></td>
			<td class="divTipCss"><div id="descpTip"></div></td>
	    </tr>
	    <tr>
	      <td>&nbsp;</td>
	      <td>
	      	<input type="hidden" id="id" name="emailInfo.id" value="${emailInfo.id}"/>
	      	<input type="hidden" id="isdefault" name="emailInfo.isdefault" value="${emailInfo.isdefault}" />
	      	 <a href="#" id="addBtn" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
	      	 <a href="javascript:connemail();" class="button"><span><view:LanguageTag key="common_syntax_test_conn"/></span></a>
	      	 <a href="javascript:goBack();" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a>
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
