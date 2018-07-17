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
	$(function() {
		checkInput();
		changeVerifyType('${portalInfo.initpwdloginverifytype}');
	});
		 
	 //点击保存之前的校验
	 function checkInput(){
	  $.formValidator.initConfig({submitButtonID:"saveBt", 
			onSuccess:function(){
			   savaData();
			},
			onError:function(){
				return false;
			}
	  });
			
	  //登录默认密码方式
      $("#initpwdloginverifytype").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).functionValidator({
      	fun:function(val){
         	if(val == '' || val == null) {return '<view:LanguageTag key="common_vd_please_sel"/>';}
         	return true;
       	}
	  });
	  
	  $("#initpwdemailactexpire").formValidator({onFocus:'<view:LanguageTag key="portal_vd_init_pwd_email_expire_input"/>',onCorrect:"OK"}).inputValidator({min:1,max:24,type:"number",onError:'<view:LanguageTag key="portal_vd_init_pwd_email_expire_len"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="portal_vd_init_pwd_expire_err"/>'});
	  $("#initpwdsmsverifyexpire").formValidator({onFocus:'<view:LanguageTag key="portal_vd_init_pwd_sms_expire_input"/>',onCorrect:"OK"}).inputValidator({min:1,max:60,type:"number",onError:'<view:LanguageTag key="portal_vd_init_pwd_sms_expire_len"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="portal_vd_init_pwd_expire_err"/>'});
	  $("#adserverip").formValidator({onFocus:'<view:LanguageTag key="portal_vd_ad_ip_input" />',onCorrect:"OK"}).inputValidator({min:1,onError:'<view:LanguageTag key="portal_vd_ad_ip_null" />'}).regexValidator({regExp:"ip4",dataType:"enum",onError:'<view:LanguageTag key="portal_vd_ad_ip_err" />'});
	  $("#adserverport").formValidator({onFocus:'<view:LanguageTag key="portal_vd_ad_port_input" />',onCorrect:"OK"}).inputValidator({min:1,max:65535, type:"number", onError:'<view:LanguageTag key="portal_vd_ad_port_err" />'});
	 
	  //$("#adserverdn").formValidator({onFocus:'<view:LanguageTag key="usource_vd_ldap_root_dn"/>',onCorrect:"OK"}).functionValidator({
 	//	 fun:function(adserverdn){
	  //      if(""==$.trim(adserverdn)){
	  //      	return '<view:LanguageTag key="usource_vd_ldap_is_not_null"/>';
	  //      }
	  //      return true;
	  //    }
 	//	 });
 		 
 	  var valName = $("#adserverdn").val();
			$("#adserverdn").formValidator({onFocus:'<view:LanguageTag key="auth_bk_ad_domain_name"/>',onCorrect:"OK"}).inputValidator({min:1,max:32,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="auth_bk_ad_domain_name_err1"/>'}, onError: '<view:LanguageTag key="auth_bk_ad_domain_name_err2"/>'}).functionValidator({
	       		fun:function(valName){
			    if(!letter_u_num_english(valName)){
			       return '<view:LanguageTag key="auth_bk_ad_domain_name_err"/>';
			    }
			    return true;
		 }});
	}
	// 保存数据
	function savaData(){
    	var url = "<%=path%>/manager/confinfo/config/portal!modify.action";
		var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
		$("#confForm").ajaxSubmit({
		   async:true,
		   dataType : "json",  
		   type:"POST", 
		   url : url,
		   data:{"oper" : "initpwd"},
		   success:function(msg){
				if(msg.errorStr == 'success'){ 
				     $.ligerDialog.success(msg.object,'<view:LanguageTag key="common_syntax_tip"/>',function(){
				     	 window.location.reload();
				     });
				}else{
				     FT.toAlert(msg.errorStr,msg.object, null);
				}
				ajaxbg.hide();
		   }
	   });
	}
	
	//改变登录密码验证方式
	function changeVerifyType(type) {
		if (type == 2) {
			$("tr[id='emailActTimeTR']").show();
			$("tr[id='smsVerifyTimeTR']").hide();
			$("tr[id='adIpTR']").hide();
			$("tr[id='adPortTR']").hide();
			$("tr[id='adDnTR']").hide();
			$("tr[id='defaultPwdTR']").hide();
			$("#adserverip").unFormValidator(true);
			$("#adserverdn").val('${portalInfo.adserverdn}');
			$("#initpwdsmsverifyexpire").val('15');
			$("#adserverport").val('389');
		} else if (type == 3) {
			$("tr[id='emailActTimeTR']").hide();
			$("tr[id='smsVerifyTimeTR']").show();
			$("tr[id='adIpTR']").hide();
			$("tr[id='adPortTR']").hide();
			$("tr[id='adDnTR']").hide();
			$("tr[id='defaultPwdTR']").hide();
			$("#adserverdn").val('${portalInfo.adserverdn}');
			$("#adserverip").unFormValidator(true);
			$("#initpwdemailactexpire").val('1');
			$("#adserverport").val('389');
		} else if (type == 4) {
			$("tr[id='adIpTR']").show();
			$("tr[id='adPortTR']").show();
			$("tr[id='adDnTR']").show();
			$("#adserverip").unFormValidator(false);
			$("tr[id='emailActTimeTR']").hide();
			$("tr[id='smsVerifyTimeTR']").hide();
			$("tr[id='defaultPwdTR']").hide();
		} else {
			getDefaultPwd();
			$("tr[id='emailActTimeTR']").hide();
			$("tr[id='smsVerifyTimeTR']").hide();
			$("tr[id='adIpTR']").hide();
			$("tr[id='adPortTR']").hide();
			$("tr[id='adDnTR']").hide();
			$("#adserverip").unFormValidator(true);
			$("#initpwdemailactexpire").val('1');
			$("#initpwdsmsverifyexpire").val('15');
			$("#adserverdn").val('${portalInfo.adserverdn}');
			$("#adserverport").val('389');
			$("tr[id='defaultPwdTR']").show();
		}
	}
	
	/**
	*获取系统配置的默认密码
	*/
	function getDefaultPwd(){
		var url = '<%=path%>/manager/confinfo/config/portal!defaultPwd.action';
		$.post(url,
			function(msg){
				if(msg.errorStr == 'success'){ 
					var defaultPwd=msg.object;
					if(defaultPwd != undefined){
						document.getElementById("portalInfoDefaultPwd").innerHTML=defaultPwd;
					}
				}
			}, "json"
		);
	}
	
	</script>
  </head>
  
  <body>
  <div id="background"  class="background"  style="display: none; "></div>
  <div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
  <input id="contextPath" type="hidden" value="<%=path%>" />
  <form id="confForm" method="post" action="" name="confForm">
    <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="topTableBg">
      <tr>
        <td width="98%"><span class="topTableBgText"><view:LanguageTag key="common_menu_config_portal_login_pwd"/></span></td>
        <td width="2%" align="right"></td>
      </tr>
    </table>  
   <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
      <tr>
        <td valign="top">
		  <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="ulOnInsideTable">
			 <tr>
				<td width="30%" align="right"><view:LanguageTag key="portal_init_pwd_login_verify_type" /><view:LanguageTag key="colon" /></td>
				<td width="30%">
					<select id="initpwdloginverifytype" name="portalInfo.initpwdloginverifytype" 
						onchange="changeVerifyType(this.value)" class="select100">
						<option value="1" <c:if test="${portalInfo.initpwdloginverifytype == 1}">selected</c:if>>
							<view:LanguageTag key="portal_init_pwd_defpwd" />
						</option>
						<option value="2" <c:if test="${portalInfo.initpwdloginverifytype == 2}">selected</c:if>>
							<view:LanguageTag key="portal_init_pwd_send_email" />
						</option>
						<option value="3" <c:if test="${portalInfo.initpwdloginverifytype == 3}">selected</c:if>>
							<view:LanguageTag key="portal_init_pwd_send_sms" />
						</option>
						<option value="4" <c:if test="${portalInfo.initpwdloginverifytype == 4}">selected</c:if>>
							<view:LanguageTag key="portal_init_pwd_verify_ad_pwd" />
						</option>
					</select>
				</td>
				<td width="40%" class="divTipCss"><div id="initpwdloginverifytypeTip"></div></td>
			 </tr>
			 <tr id="defaultPwdTR">
                <td align="right"><view:LanguageTag key="userconf_def_user_pwd" /><view:LanguageTag key="colon"/></td>
                <td>
                	<span id="portalInfoDefaultPwd" ></span>
                </td>
                <td class="divTipCss"><div id ="defaultPwdTip"></div></td>
	          </tr>
			 <tr id="emailActTimeTR">
                <td align="right"><view:LanguageTag key="portal_init_pwd_email_expire" /><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
                <td>
                	<input type="text" id="initpwdemailactexpire" name="portalInfo.initpwdemailactexpire" value="${portalInfo.initpwdemailactexpire}" class="formCss100" />
                </td>
                <td class="divTipCss"><div id ="initpwdemailactexpireTip"></div></td>
	          </tr>
	          <tr id="smsVerifyTimeTR">
                <td align="right"><view:LanguageTag key="portal_init_pwd_sms_expire" /><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
                <td>
                	<input type="text" id="initpwdsmsverifyexpire" name="portalInfo.initpwdsmsverifyexpire"  value="${portalInfo.initpwdsmsverifyexpire}" class="formCss100" />
                </td>
                <td class="divTipCss"><div id ="initpwdsmsverifyexpireTip"></div></td>
	          </tr>
	          <tr id="adIpTR">
                <td align="right"><view:LanguageTag key="portal_ad_verify_pwd_ip" /><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
                <td>
                	<input type="text" id="adserverip" name="portalInfo.adserverip"  value="${portalInfo.adserverip}" class="formCss100" />
                </td>
                <td class="divTipCss"><div id ="adserveripTip"></div></td>
	          </tr>
	          <tr id="adPortTR">
                <td align="right"><view:LanguageTag key="portal_ad_verify_pwd_port" /><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
                <td>
                	<input type="text" id="adserverport" name="portalInfo.adserverport"  value="${portalInfo.adserverport}" class="formCss100" />
                </td>
                <td class="divTipCss"><div id ="adserverportTip"></div></td>
	          </tr>
	          <tr id="adDnTR">
	           <td align="right"><view:LanguageTag key="auth_bk_domain"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	           <td><input type="text" id="adserverdn" name="portalInfo.adserverdn" value="${portalInfo.adserverdn}" class="formCss100" /></td>
	           <td class="divTipCss"><div id="adserverdnTip"></div></td>
	          </tr>
		      <tr>
		        <td align="right"></td>
		        <td><a href="#" id="saveBt" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a></td>
		        <td></td>
		     </tr> 
		 </table>
        </td>
       </tr>
    </table> 
   </form>
  </body>
</html>