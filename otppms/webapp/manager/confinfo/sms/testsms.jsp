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
    <title><view:LanguageTag key="sms_test_conn"/></title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
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
	<!--
	 $(document).ready(function(){
	 	window.resizeBy(0,0);
	 	checkForm();
	 }) 

    //跳转到认证服务器选项
    function checkForm(){
       $.formValidator.initConfig({submitButtonID:"okBtn",
			onSuccess:function(){
			    testSendSms();
			},
			onError:function(){
				return false;
			}});
		$("#phone").formValidator({onFocus:'<view:LanguageTag key="sms_vd_mobile_attr_show"/>' ,onCorrect:"OK"}).inputValidator({max:20,onError:'<view:LanguageTag key="sms_vd_mobile_attr_err"/>'}).regexValidator({regExp:"^[A-Za-z0-9\-]+$",dataType:"String",onError:'<view:LanguageTag key="sms_vd_mobile_attr_err_1"/>'});
		$("#message").formValidator({onFocus:'<view:LanguageTag key="sms_vd_test_mess_content_show"/>' ,onCorrect:"OK"}).inputValidator({min:1,max:140,onError:'<view:LanguageTag key="sms_vd_test_mess_content_err"/>'});
    }
    
    //测试短信网关
    function testSendSms() {
    	var phone = $("#phone").val();
    	var message = $("#message").val();
		parent.$("#callPhone").val(phone);
		parent.$("#message").val(message);
	    parent.testSend();
    }
	
	function okClick(btn,win,index){
	    $('#okBtn').triggerHandler("click");    	
	}
	//-->
	</script>
  </head>
  
  <body>
   <form id="testsmsForm" method="post" action="">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	    <tr>
	      <td colspan="3" height="12"></td>
	    </tr>
	    <tr>
	      <td width="25%" align="right" valign="middle"><view:LanguageTag key="sms_test_phone"/><view:LanguageTag key="colon"/></td>
	      <td width="30%">
	         <input type="text" id="phone" class="formCss100" value="" />
	      </td>
	      <td width="45%" class="divTipCss"><div id="phoneTip"></div></td>
	    </tr>
	    <tr>
	      <td align="right" valign="middle"><view:LanguageTag key="sms_test_message_content"/><view:LanguageTag key="colon"/></td>
	      <td>
	         <textarea id="message" style="height:50px; overflow-y:visible;" class="textarea100"></textarea>
	      </td>
	      <td class="divTipCss"><div id="messageTip"></div></td>
	    </tr>
	    <tr>
	       <td><a href="#" name="okBtn" id="okBtn"></a></td>
	    </tr>
	 </table>
   </form>
  </body>
</html>