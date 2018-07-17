<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%@page import="com.ft.otp.manager.login.entity.LinkUser"%>
<%@page import="com.ft.otp.manager.login.service.OnlineUsers"%>
<%@page import="com.ft.otp.util.tool.StrTool"%>
<%@page import="com.ft.otp.common.language.Language"%>
<%
	String path = request.getContextPath();
	
	String remoteAddr = request.getRemoteAddr();
	String sessionId = session.getId();
	LinkUser linkUser = OnlineUsers.getUser(sessionId);
	String userAddr = null;
	if(null != linkUser){
		userAddr = linkUser.getRemoteAddr();
	}
	
	String result = "0";
	String redirect = "/manager/main/layout.jsp";	
	if (StrTool.strEquals(remoteAddr, userAddr)) {
	    redirect = path + redirect;
	    result = "1";
	}
	
	String titleImg = Language.getCurrLang(request.getSession());
	titleImg += ".png";
	
	// 防止跨站脚本编制
	String outof;
	if("true".equals(request.getParameter("outof"))){
		outof = "true";
	}else if("false".equals(request.getParameter("outof"))){
		outof = "false";
	}else{
		outof = "";
	}
%>
<html>
<head>
<script language="javascript" type="text/javascript">
<!--
	if('<%=result%>' == '1'){
		window.parent.location.href ="<%=redirect%>";
	}
//-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><view:LanguageTag key="system_noun_name"/></title>
<link href="<%=path%>/login/css/login.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"/>
<style type="text/css">
<!--
body {
	background-color: #005495;
}
-->
</style>
<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>
<script language="javascript" src="<%=path%>/manager/common/js/md5.js"></script> 
<script language="javascript" type="text/javascript">
	<!--
		$(function(){		
			if("true" == '<%=outof%>'){
				var tip = $.ligerDialog.tip({title:'<view:LanguageTag key="login_log_out_tips"/>', content:'<view:LanguageTag key="login_repeat_login_tips"/>'});
				setTimeout(function(){tip.close()}, 8000);
			}
			
			//初始化登录页面 静态密码登录
			setLoginTypeHTML(0);
			$("#userId").focus();
		})
		
		//语言选择
		function languageSel(language){
			window.location.href = "<%=path%>/login!language.action?language=" + language;	 
		}
		
		//登录方式包含三种 0只验证静态密码、1只验证动态口令、2验证静态密码和动态口令
		var loginType = 0;// 默认为0
		//根据输入的用户名来判断 是属于哪种认证方式
		function setLoginType(userName){
			if($.trim(userName) == ""){//默认静态密码登录
				loginType = 0;
			}else{
				var urlStr = "<%=path%>/login!getAdmUserLocalAuth.action";
				$.post(urlStr,{
					userId:userName
			    	},function(result){
			        	if(result.errorStr == 'success'){//查到输入的用户
			            	if(result.object == null){
			            		loginType = 0;
			            	}else{
			            		loginType = result.object.localauth;
			            		loginType = loginType==''?0:loginType;
			            	}
			        	}else if(result.errorStr == 'error'){//获取登录方式异常
			            	loginType = 0;
			            	FT.toAlert(result.errorStr, result.object, null);
			            }else{
			            	loginType = 0;
			            }
			            //设置登录页面
			            setLoginTypeHTML(loginType);
			       	},'json'
		     	);
			}
		}
		
		//根据登录方式设置登录页面
		function setLoginTypeHTML(loginType){
			if(loginType==0){//验证静态密码登录
				$("#pinTr").hide();
				$("#nullTr").hide();
				$("#pwdTr").show();
				$("#checkCodeTr").show();
				$("#password").focus();
			}else if(loginType==1){//验证动态口令登录
				$("#pinTr").show();
				$("#nullTr").show();
				$("#pwdTr").hide();
				$("#checkCodeTr").hide();
				$("#pin").focus();
			}else if(loginType==2){//验证静态密码和动态口令
				$("#checkCodeTr").hide();
				$("#nullTr").hide();
				$("#pinTr").show();
				$("#pwdTr").show();
				$("#password").focus();
			}
			
			$("#localAuth").val(loginType);
		}
		
		//回车提交事件
		document.onkeydown = function(evt){
			var evt = window.event?window.event:evt;
			if(evt.keyCode==13) {
				if(!admLogin()){
					return false;
				}
			}
		}
		
		//更换验证码
		function refreshCode(){
			var obj = document.getElementById('validateCode');
            obj.src="<%=path%>/validationCode?"+ Math.random();
		}
		
		//管理员登录
		function admLogin(){
			if(!checkForm()){
				return;
			}
			
			$("#LoginForm").ajaxSubmit({
				async : true,
				type : "POST",
				url : "<%=path%>/login!login.action?language=" + $("#langSel").val(),
				dataType : "json",
				success : function(msg){
					var errorStr = msg.errorStr;
					if("success" == errorStr){
						window.location.href = "<%=path%>/manager/main/layout.jsp";
					}else if("resetpwd" == errorStr){
						//为刚重置后的密码
						var adminId = $("#userId").val();
						window.location.href = "<%=path%>/install/activate.jsp?account="+adminId+"&passwd=" + msg.object+"&source=1";
					}else if("1" == errorStr){// 需要同步
						var otpTdText = '<view:LanguageTag key="tkn_next_dynamic_pwd"/><view:LanguageTag key="colon"/>';
						$("#otpTD").html(otpTdText);
						$("#oldPin").val($("#pin").val());
						$("#pin").val("");
						FT.toAlert('error', msg.object, null);
					}else{
						var loginType = $("#localAuth").val();
						if(loginType==0||loginType==2){
							$("#password").val("");
						}
						
						if(loginType==1||loginType==2){
							$("#pin").val("");
							$("#oldPin").val("");
							var otpTdText = '<view:LanguageTag key="login_otp_pin"/><view:LanguageTag key="colon"/>';
							$("#otpTD").html(otpTdText);
						}
						
						if(loginType==0){
							$("#checkCode").val("");
							refreshCode();
						}
						FT.toAlert(errorStr, msg.object, null);
					}
				}
			});
	 	}
	 	
	 	//表单检查
		function checkForm(){
			if("" == $.trim($("#userId").val())){
				FT.toAlert('warn', '<view:LanguageTag key="login_vd_userid_tip"/>', null);
				$("#userId").focus();
				return false;
			}
			var loginType = $("#localAuth").val();
			
			if(loginType==0||loginType==2){
				if($("#password").val().length==0){
					FT.toAlert('warn', '<view:LanguageTag key="login_vd_pwd_tip"/>', null);
					$("#password").focus();
					return false;
				}
			}
			
			if(loginType==1||loginType==2){
				if("" == $.trim($("#pin").val())){
					FT.toAlert('warn', '<view:LanguageTag key="login_vd_otp_pin_tip"/>', null);
					$("#pin").focus();
					return false;
				}
			}
			
			if(loginType==0){
				if("" == $.trim($("#checkCode").val())){
					FT.toAlert('warn', '<view:LanguageTag key="login_vd_code_tip"/>', null);
					$("#checkCode").focus();
					return false;
				}
			}
			//进行密码加密和替换
			var password = $("#password").val();
			if(password!=undefined && password!=null && password!=""){
				var newhex = hex_md5(password);
				$("#password").val(newhex);
			}
			
			return true;
		}
		
		//找回密码
		function retrievePwd(){
			window.location.href = "<%=path%>/install/retrieve.jsp?source=0";
		}
		
		//获取应急口令
		function getPin(){
			window.location.href = "<%=path%>/install/retrieve.jsp?source=1";
		}
		
		// 设置下一个focus的input
		function setNextFocus(){
		    if(loginType=="0"){
		    	$("#checkCode").focus();
		    }else{
		    	$("#pin").focus();
		    }
		}
	//-->
	</script>
</head>
<body style="overflow:auto; overflow-x:hidden">
<table width="100%" height="179" border="0" cellpadding="0" cellspacing="0" background="<%=path%>/images/login/admin_r1_c2.png">
  <tr>
    <td height="172" valign="top"><table width="99%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="right">
		<select id="langSel" class="select100" style="width:180px" onchange="languageSel(this.value)">
			<view:LanguageSelectTag key="${sessionScope.language_session_key}" />
		</select>
		</td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td align="center" valign="top"><table width="593" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td align="right">&nbsp;</td>
        <td height="55" align="center" valign="bottom" background="<%=path%>/images/login/admin_r2_c5.png">
          <img src="<%=path%>/images/login/<%=titleImg%>" width="215" height="30" /></td>
        <td align="left">&nbsp;</td>
      </tr>
      <tr>
        <td align="right">&nbsp;</td>
        <td><img src="<%=path%>/images/login/admin_r3_c5.png" width="420" height="34" /></td>
        <td align="left">&nbsp;</td>
      </tr>
      <tr>
        <td align="right" valign="bottom"><img src="<%=path%>/images/login/admin_r5_c4.png" width="95" height="34" /></td>
        <td valign="top"><table width="100%" height="152" border="0" cellpadding="0" cellspacing="0" background="<%=path%>/images/login/admin_r4_c9.png">
            <tr>
              <td valign="top">
			    <form name="LoginForm" id="LoginForm" method="post" action="" style="margin:0">
			      <table width="100%" border="0" cellspacing="0" cellpadding="3">
          <tr>
            <td width="48%" align="right" valign="top"><view:LanguageTag key="login_username"/><view:LanguageTag key="colon"/></td>
            <td valign="top" width="31%">
              <input type="text" id="userId" name="queryForm.userId" onblur="setLoginType(this.value);" class="login_form_text" />
              <input type="hidden" id="localAuth" name="queryForm.localAuth" />            </td>
            <td valign="top" width="21%">&nbsp;</td>
          </tr>
          <tr id="pwdTr">
            <td align="right" valign="top"><view:LanguageTag key="common_info_pwd"/><view:LanguageTag key="colon"/></td>
            <td valign="top">
              <input onpaste="return false" type="password" id="password" name="queryForm.password" class="login_form_text" />
            </td>
            <td align="left">&nbsp;&nbsp;
            <%--
            	<a href="#" onfocus="setNextFocus();" id="forgetPwd" onClick="retrievePwd();" class="isLink_HeiSe"><view:LanguageTag key="login_forgot_pwd"/></a>
             --%>
            </td>
          </tr>
          <tr id="pinTr">
            <td align="right" valign="top" id="otpTD"><view:LanguageTag key="login_otp_pin"/><view:LanguageTag key="colon"/></td>
            <td valign="top">
              <input type="hidden" id="oldPin" name="queryForm.oldPin"/>
              <input type="password" id="pin" name="queryForm.pin" class="login_form_text" />
            </td>
            <td align="left">&nbsp;&nbsp;<a href="#" id="getPin" onClick="getPin();" class="isLink_HeiSe"><view:LanguageTag key="login_emergency_pin"/></a></td>
          </tr>
          <tr id="nullTr">
          	<td colspan="3" height="20px"></td>
          </tr>
          <tr id="checkCodeTr">
            <td align="right" width="43%"><view:LanguageTag key="login_validate_code"/><view:LanguageTag key="colon"/></td>
            <td colspan="2" align="left" valign="top">
            	<table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td width="30%">
                      <input type="text" id="checkCode" name="queryForm.checkCode" class="login_form_text" style="width:70px" />
                    </td>
                    <td width="70%"><img id="validateCode" style="margin-top:5;" src="<%=path%>/validationCode" alt='<view:LanguageTag key="login_not_see_change_a"/>' onclick="refreshCode();" /></td>
                  </tr>
                </table>
           	</td>
          </tr>
        </table>
			      <table width="100%" border="0" cellspacing="0" cellpadding="3">
                    <tr>
                      <td colspan="2" height="0"></td>
                    </tr>
                    <tr>
                      <td width="47%">&nbsp;</td>
                      <td width="27%"><a href="#" onClick="admLogin();" class="isLink_BaiSe"><span class="login_button"><view:LanguageTag key="common_syntax_login"/></span></a></td>
                      <td width="26%">&nbsp;</td>
                    </tr>
                  </table>
			    </form></td>
              </tr>
        </table></td>
        <td align="left" valign="bottom"><img src="<%=path%>/images/login/admin_r5_c10.png" width="78" height="34" /></td>
      </tr>
      <tr>
        <td height="48" colspan="3" valign="top" background="<%=path%>/images/login/admin_r6_c4.png"><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td colspan="3" height="12"></td>
        </tr>
        <tr>
          <td width="16%">&nbsp;</td>
          <td width="70%" align="center"><span class="text_Bai_Se"><view:LanguageTag key='system_noun_copyright'/></span></td>
          <td width="14%">&nbsp;</td>
        </tr>
      </table></td>
      </tr>
      <tr>
        <td colspan="3"><img src="<%=path%>/images/login/admin_r7_c4.png" width="593" height="46" /></td>
      </tr>
      <tr>
        <td colspan="3"><img src="<%=path%>/images/login/admin_r8_c4.png" width="593" height="47" /></td>
      </tr>
      <tr>
        <td colspan="3"><img src="<%=path%>/images/login/admin_r9_c4.png" width="593" height="46" /></td>
      </tr>
      <tr>
        <td colspan="3"><img src="<%=path%>/images/login/admin_r10_c4.png" width="593" height="54" /></td>
      </tr>
      <tr>
        <td colspan="3"><img src="<%=path%>/images/login/admin_r11_c4.png" width="593" height="41" /></td>
      </tr>
      <tr>
        <td colspan="3"><img src="<%=path%>/images/login/admin_r12_c4.png" width="593" height="39" /></td>
      </tr>
      <tr>
        <td colspan="3"><img src="<%=path%>/images/login/admin_r13_c4.png" width="593" height="34" /></td>
      </tr>
    </table></td>
  </tr>
</table>
</body>
</html>
