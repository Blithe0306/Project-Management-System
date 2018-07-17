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
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css" />
	<link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css" />
     <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
     <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
     <script type="text/javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>    
 
    <script src="<%=path%>/manager/common/js/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	
	<script language="javascript" type="text/javascript">
	<!--
	 $(function() {
	     $.formValidator.initConfig({submitButtonID:"tknSync", debug:true,
	        onSuccess:function(){
	           tknSync();
	        },
           onError:function(){
               return false;
              }
         });
         $("#token").formValidator({onShow:'<view:LanguageTag key="tkn_vd_token_show"/>',onFocus:'<view:LanguageTag key="tkn_vd_token_show"/>',onCorrect:"OK"}).inputValidator({min:4,max:32,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="tkn_vd_token_error"/>'},onError:'<view:LanguageTag key="tkn_vd_token_error_1"/>'}) 
	 	 .functionValidator({
			fun:function(val,elem){
				var showtext = checkTokenInfo(val);
				if (showtext != 'success') {
					return showtext;
				}else {
					selectToken(val);
					return true;
				}
			}});
			
	 	 $("#otp").formValidator({onFocus:'<view:LanguageTag key="tkn_vd_otp_show"/>',onCorrect:"OK"}).inputValidator({min:4, max:10, onError:'<view:LanguageTag key="tkn_vd_otp_error_len"/>'})
			.regexValidator({regExp:"num",dataType:"enum",onError:'<view:LanguageTag key="tkn_vd_otp_error"/>'});
		 
		 $("#nextotp").formValidator({onFocus:'<view:LanguageTag key="tkn_vd_otp_show"/>',onCorrect:"OK"}).inputValidator({min:4, max:10, onError:'<view:LanguageTag key="tkn_vd_otp_error_len"/>'})
			.regexValidator({regExp:"num",dataType:"enum",onError:'<view:LanguageTag key="tkn_vd_otp_error"/>'});
	     
     	 $("#challengeotp").formValidator({onFocus:'<view:LanguageTag key="tkn_vd_challengeotp_tip"/>',onCorrect:"OK"}).inputValidator({min:4, max:10, onError:'<view:LanguageTag key="tkn_vd_challengeotp_len"/>'})
			.regexValidator({regExp:"num",dataType:"enum",onError:'<view:LanguageTag key="tkn_vd_challengeotp_err"/>'});
		 $("#ordinaryotp").formValidator({onFocus:'<view:LanguageTag key="tkn_vd_otp_tip"/>',onCorrect:"OK"}).inputValidator({min:4, max:10, onError:'<view:LanguageTag key="tkn_vd_otp_len"/>'})
			.regexValidator({regExp:"num",dataType:"enum",onError:'<view:LanguageTag key="tkn_vd_otp_err"/>'});
		
		 $("#challrespotp").formValidator({onFocus:'<view:LanguageTag key="tkn_vd_challrespotp_tip"/>',onCorrect:"OK"}).inputValidator({min:4, max:10, onError:'<view:LanguageTag key="tkn_vd_challrespotp_len"/>'})
			.regexValidator({regExp:"num",dataType:"enum",onError:'<view:LanguageTag key="tkn_vd_challrespotp_err"/>'});
		 $("#challrespnextotp").formValidator({onFocus:'<view:LanguageTag key="tkn_vd_challrespotp_tip"/>',onCorrect:"OK"}).inputValidator({min:4, max:10, onError:'<view:LanguageTag key="tkn_vd_challrespotp_len"/>'})
			.regexValidator({regExp:"num",dataType:"enum",onError:'<view:LanguageTag key="tkn_vd_challrespotp_err"/>'});
	     
	});
	
	//校验令牌信息
	function checkTokenInfo(val) {
   		var reval;
		$.ajax({ 
			async:false,
			type:"POST",
			url:"<%=path%>/manager/token/token!findToken.action",
			data:{"token" : val},
			dataType:"json",
			success:function(msg){
				if(msg.errorStr == 'error' && msg.object != '') {
					reval = '<view:LanguageTag key="tkn_info_check_is_exist"/>';
				}else {
					if(msg.object.physicaltype==6) {
						reval = '<view:LanguageTag key="tkn_sms_tkn_no_auth_sync"/>';
					}else {
						reval = "success";
					}
				}
			}
		});
		return reval;
	}
	
    //查询令牌同步模式        
    function selectToken(tokenStr){
    	if (tokenStr == '' || tokenStr == null) {
    		return;
    	}
    	var url='<%=path%>/manager/token/token!findToken.action?flag=tknspec';
    	$.post(url, {'token':tokenStr},
			function(msg){
				if(msg.errorStr == "success"){
					var syncmode = msg.object.syncmode;
					$("#syncmode").val(syncmode);
					if (syncmode == 0) {
						cancelVal();
						$("#syncTab0").show();
						$("#syncTab1").hide();
						$("#syncTab2").hide();
						$("#otp").unFormValidator(false); 
						$("#nextotp").unFormValidator(false); 
						$("#challengeotp").unFormValidator(true); 
						$("#ordinaryotp").unFormValidator(true); 
						$("#challrespotp").unFormValidator(true); 
						$("#challrespnextotp").unFormValidator(true); 
						$("#otp").focus(); 
					} else if (syncmode == 1)  {
						cancelVal();
					 	$("#syncTab1").show();
					 	$("#syncTab0").hide();
						$("#syncTab2").hide();
						$("#otp").unFormValidator(true); 
						$("#nextotp").unFormValidator(true); 
						$("#challengeotp").unFormValidator(false); 
						$("#ordinaryotp").unFormValidator(false); 
						$("#challrespotp").unFormValidator(true); 
						$("#challrespnextotp").unFormValidator(true); 
						$("#challengeotp").focus();
					} else if (syncmode == 2) {
						getQS(tokenStr);
						cancelVal();
						$("#syncTab2").show();
						$("#syncTab1").hide();
						$("#syncTab0").hide();
						$("#otp").unFormValidator(true); 
						$("#nextotp").unFormValidator(true); 
						$("#challengeotp").unFormValidator(true); 
						$("#ordinaryotp").unFormValidator(true); 
						$("#challrespotp").unFormValidator(false); 
						$("#challrespnextotp").unFormValidator(false); 
						$("#challrespotp").focus();
					}else {
						cancelVal();
						$("#syncTab0").show();
						$("#syncTab1").hide();
						$("#syncTab2").hide();
						$("#otp").unFormValidator(false); 
						$("#nextotp").unFormValidator(false); 
						$("#challengeotp").unFormValidator(true); 
						$("#ordinaryotp").unFormValidator(true); 
						$("#challrespotp").unFormValidator(true); 
						$("#challrespnextotp").unFormValidator(true); 
						$("#otp").focus(); 
					}
				}else{
					FT.toAlert('error','<view:LanguageTag key="tkn_get_tkn_syncmode_err"/>',null);
				}
			}, "json"
		);
    }
    
    //获取挑战值
	function getQS(tokenStr){
		var url='<%=path%>/manager/token/authAction!requestQS.action';
		$.post(url, {'messageBean.tokenSN':tokenStr},
			function(msg){
				if(msg.errorStr =='success'){
					$("#changecode").html(msg.object);
				}
			}, "json"
		);
	}
    
    //清空列表
	function cancelVal(){
		$("#otp").val("");
		$("#nextotp").val("");
		$("#challengeotp").val("");
		$("#ordinaryotp").val("");
		$("#challrespotp").val("");
		$("#challrespnextotp").val("");
	}
	
	//令牌同步
	function tknSync(){
		var tokenStr = $.trim($("#token").val());
		var otpStr;
		var nextOtpStr;
		var question='';
		var syncmode = $("#syncmode").val();
		if (syncmode == 0) {
			otpStr = $.trim($("#otp").val());
			nextOtpStr = $.trim($("#nextotp").val());
		}else if(syncmode == 1) {
			otpStr = $.trim($("#challengeotp").val());
			nextOtpStr = $.trim($("#ordinaryotp").val());
		}else if(syncmode == 2) {
			question = $("#changecode").text();
			otpStr = $.trim($("#challrespotp").val());
			nextOtpStr = $.trim($("#challrespnextotp").val());
		}else {
			otpStr = $.trim($("#otp").val());
			nextOtpStr = $.trim($("#nextotp").val());
		}
		
		var url = '<%=path%>/manager/token/authAction!tokenSync.action';
		var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
		$.post(url, {'messageBean.tokenSN':tokenStr, 'messageBean.otp':otpStr, 'messageBean.nextOtp':nextOtpStr, "messageBean.challengeCode" : question},
			function(msg){
				FT.toAlert(msg.errorStr,msg.object, null);
				ajaxbg.hide();
			}, "json"
		);
	}
	//-->
	</script>
  </head>
 <body>
  <div id="background"  class="background"  style="display: none; "></div>
  <div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
 <form name="ImportForm" method="post" action="" enctype ="multipart/form-data">
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="topTableBg">
      <tr>
        <td width="98%"><span class="topTableBgText"><view:LanguageTag key="tkn_synch_title"/></span></td>
		<td width="2%" align="right">
      		<a href="javascript:addAdmPermCode('030107','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" hspace="6" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a></td>
      </tr>
    </table>
	<table width="100%" border="0"  cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
		 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
             <tr>
               <td width="25%" align="right" valign="top"><view:LanguageTag key="tkn_comm_tknum"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
               <td width="30%">
               		<input type="text" id="token" name="token" class="formCss100" />
               		<input type="hidden" id="syncmode" value="" />
               		</td>
               <td width="45%" class="divTipCss"><div id="tokenTip"></div></td>
             </tr>
             
             <tr>
             	<td colspan="3">
             		
             	<table width="100%" border="0" cellspacing="0" cellpadding="0" id="syncTab0">
		          	<tr>
		               <td align="right" width="25%"><view:LanguageTag key="tkn_dynamic_pwd"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		               <td width="30%"><input type="text" id="otp" name="otp" class="formCss100" /></td>
		               <td class="divTipCss" width="45%"><div id="otpTip"></div></td>
		             </tr>
		             <tr>
		               <td align="right"><view:LanguageTag key="tkn_next_dynamic_pwd"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		               <td><input type="text" id="nextotp" name="nextOtp" class="formCss100" /></td>
		               <td class="divTipCss"><div id="nextotpTip"></div></td>
		             </tr>
		         </table>
		         
		 		<table width="100%" border="0" cellspacing="0" cellpadding="0" id="syncTab1" style="display:none"> 
		          <tr>
		            <td align="right" width="25%"><view:LanguageTag key="tkn_challenge_otp"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		            <td width="30%">
		              <input type="text" id="challengeotp" class="formCss100" />
		            </td>
		            <td width="45%" class="divTipCss"><div id="challengeotpTip"></div></td>
		          </tr>
		          <tr>
		            <td align="right"><view:LanguageTag key="tkn_dynamic_pwd"/><view:LanguageTag key="colon"/></td>
		            <td>
		              <input type="text" id="ordinaryotp" class="formCss100" />
		            </td>
		            <td class="divTipCss"><div id="ordinaryotpTip"></div></td>
		          </tr>
		         </table>
		         
		 		<table width="100%" border="0" cellspacing="0" cellpadding="0" id="syncTab2" style="display:none">
		          <tr>
		            <td align="right" width="25%"><view:LanguageTag key="tkn_challenge_val"/><view:LanguageTag key="colon"/></td>
		            <td width="30%">
		            	<div id="changecode"></div>
		            </td>
		            <td width="45%"></td>
		          </tr>
		          <tr>
		            <td align="right"><view:LanguageTag key="tkn_challenge_resp_otp"/><view:LanguageTag key="colon"/></td>
		            <td>
		              <input type="text" id="challrespotp" class="formCss100" />
		            </td>
		            <td class="divTipCss"><div id="challrespotpTip"></div></td>
		          </tr>
		          <tr>
		            <td align="right"><view:LanguageTag key="tkn_challenge_resp_nextotp"/><view:LanguageTag key="colon"/></td>
		            <td>
		              <input type="text" id="challrespnextotp" class="formCss100" />
		            </td>
		            <td class="divTipCss"><div id="challrespnextotpTip"></div></td>
		          </tr>
		         </table>
             		
             	</td>
             </tr>
             <tr>
               <td align="right">&nbsp;</td>
               <td><a href="#" id="tknSync" class="button"><span><view:LanguageTag key="tkn_synch"/></span></a></td>
               <td></td>
             </tr>
           </table>
		</td>
      </tr>
    </table>
  </form>
 
  </body>
</html>