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
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css">
    <link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css">
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css">
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
      $(document).ready(function(){
    	checkUserForm();
    	changeSel($('#localAuth').val());
      });
      
      function checkUserForm(){
    	  $.formValidator.initConfig({submitButtonID:"saveBtn",
  			onSuccess:function(){
  			    saveRad();
  			},
  			onError:function(){
  				return false;
  			}});
  			
        	$("#localAuth").formValidator({onFocus:'<view:LanguageTag key="user_vd_sel_localauth" />',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="user_vd_sel_localauth" />'});
        	$("#pwd").formValidator({onFocus:'<view:LanguageTag key="user_vd_pwd_tip"/>',onCorrect:"OK"}).functionValidator({
              fun:function(val){
                 var localAuth = $('#localAuthVal').val();
                 if(localAuth==1 || localAuth==2){
                   if(val=='' || val==null){return '<view:LanguageTag key="user_vd_pwd_err"/>';}
                   if(val.length<4||val.length>32){return '<view:LanguageTag key="user_vd_pwd_tip"/>';}
                 }
                 return true;
              }
             });
  		   $("#confrmpwd").formValidator({onFocus:'<view:LanguageTag key="user_vd_confpwd_tip"/>',onCorrect:"OK"}).functionValidator({
              fun:function(val){
                 var localAuthVal = $('#localAuthVal').val();
                 var pwd = $('#pwd').val();
                 if(localAuthVal==1 || localAuthVal==2){
                   if(val=='' || val==null){return '<view:LanguageTag key="user_vd_confpwd_err"/>';}
                   if(val.length<4||val.length>32){return '<view:LanguageTag key="user_vd_confpwd_tip"/>';}
                   if(val!=pwd){return '<view:LanguageTag key="common_vd_confirm_pwd_err"/>';}
                 }
                 return true;
              }
            });
      }
      
      
      
      function saveRad() {
      	 var localAuthid = $("#localAuth").val();
      	 var pwd = $("#pwd").val();
      	 var url = parent.$("#seturl").val();
	     var userIds = parent.$("#userIds").val();
      	 parent.$("#localAuth").val(localAuthid);
      	 parent.$("#pwd").val(pwd);
      	 parent.batchExecOper(url,userIds);
      }
      
      
      function okClick(btn,win,index){
		    $('#saveBtn').triggerHandler("click");    	
	  }

      //展示静态密码设置框
      function changeSel(obj){
 		 //只验证OTP
 		$("#localAuthVal").val(obj);
 		if (obj==0 ||  obj==3){
  		   $('#pwdTR').hide();
  		   $('#confrmpwdTR').hide();
  		   $("#pwd").val("");
  		   $("#confrmpwd").val("");
  		   $("#pwd").unFormValidator(true); 
  		   $("#confrmpwd").unFormValidator(true); 
 		 } else {
  		   $('#pwdTR').show();
  		   $('#confrmpwdTR').show();
  		   $("#pwd").unFormValidator(false); 
  		   $("#confrmpwd").unFormValidator(false); 
 		 }
 	}
	</script>
  </head>
  
  <body style="overflow:auto; overflow-x:hidden">
  <input id="contextPath" type="hidden" value="<%=path%>" />
  <form  id="AddForm" name="AddForm" method="post" action="">
    <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		      <tr>
		        <td width="30%"  align="right"><view:LanguageTag key="user_local_auth_mode"/><view:LanguageTag key="colon"/></td>
		        <td width="30%">
		        	<select id="localAuth" class="select100" onchange="changeSel(this.value)">
						<option value="0"><view:LanguageTag key="local_auth_only_vd_tkn"/></option>
						<option value="2"><view:LanguageTag key="local_auth_only_vd_pwd"/></option>
						<option value="1"><view:LanguageTag key="local_auth_vd_pwd_tkn"/></option>
			        </select> 
			        <input type="hidden" id="localAuthVal" name="localAuthVal" value="0" />
		        </td>
		        <td width="40%" class="divTipCss"><div id="localAuthTip" ></div></td>
		      </tr>
		      <tr id="pwdTR">
		        <td align="right"><view:LanguageTag key="user_static_pwd"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		        <td><input onpaste="return false" type="password" id="pwd" class="formCss100" /></td>
		        <td class="divTipCss"><div id="pwdTip" style="width:100%"></div></td>
		      </tr>
		      <tr id="confrmpwdTR">
		        <td align="right"><view:LanguageTag key="user_conf_static_pwd"/><span class="text_Hong_Se" >*</span><view:LanguageTag key="colon"/></td>
		        <td><input onpaste="return false" type="password" id="confrmpwd" name="confrmpwd" class="formCss100" /></td>
		        <td class="divTipCss"><div id="confrmpwdTip" style="width:100%"></div></td>
		      </tr>
		      <tr>
                <td><a href="#" name="saveBtn" id="saveBtn"></a></td>
              </tr>
		    </table>
		</td>
      </tr>
    </table>
  </form>
 
  </body>
</html>