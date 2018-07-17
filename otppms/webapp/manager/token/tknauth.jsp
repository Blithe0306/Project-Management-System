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
	$(function() {
	      $.formValidator.initConfig({
           submitButtonID:"tknAuth",
	        debug:false,
	        onSuccess:function(){
	           tknAuth();
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
					return true;
				}
			}});
		  
		  
		  $("#otp").formValidator({onFocus:'<view:LanguageTag key="tkn_vd_otp_show"/>',onCorrect:"OK"}).inputValidator({min:4, max:10, onError:'<view:LanguageTag key="tkn_vd_otp_error_len"/>'})
			.regexValidator({regExp:"num",dataType:"enum",onError:'<view:LanguageTag key="tkn_vd_otp_error"/>'});
		  
		  $("#token").focus();
    })
    
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
	
	//显示获取挑战值按钮
	function selectCR(){
        if($("#crbox").attr("checked") == true){
        	$("#getDiv").show();
        }else{
        	$("#getDiv").hide();
        	$("#qsv").val('');
        }
	}
	
	//动态口令认证
	function tknAuth(){
	    var otpStr = $.trim($("#otp").val());
		var qsv = $.trim($("#qsv").val());
		var tokenStr = $.trim($("#token").val());
		
		if(qsv != ''&& qsv!=null){
			if(otpStr == ''){
				FT.toAlert('error','<view:LanguageTag key="tkn_vd_qsv_show"/>', null);
				return;
			}
			// 验证应答码
			vfyCR(tokenStr,otpStr,qsv);
			return;
		}	
		
		var url='<%=path%>/manager/token/authAction!tokenAuth.action';
		$.post(url, {'messageBean.tokenSN':tokenStr, 'messageBean.otp':otpStr},
			function(msg){
			    FT.toAlert(msg.errorStr,msg.object , null);
			}, "json"
		);
	}
	
	//获取挑战值
	function getQS(){
    	var tokenStr = $.trim($("#token").val());
    	var url='<%=path%>/manager/token/authAction!requestQS.action';
		$.post(url, {'messageBean.tokenSN':tokenStr},
			function(msg){
				if(msg.errorStr =='success'){
					$("#qsDiv").html(msg.object);
					$("#qsv").val(msg.object);
				}else{
					FT.toAlert(msg.errorStr,msg.object , null);
				}
			}, "json"
		);   
	}
	
	//验证应答码  crStr 输入的应答码、qsv 获取的挑战码
	function vfyCR(tokenStr,crStr,qsv){
		var url='<%=path%>/manager/token/authAction!verifyCR.action';
		$.post(url, {'messageBean.tokenSN':tokenStr,'messageBean.responseCode':crStr,'messageBean.challengeCode':qsv},
			function(msg){
				FT.toAlert(msg.errorStr,msg.object , null);
			}, "json"
		);
	}
	//-->
	</script>
  </head>
 <body>
 <form name="ImportForm" method="post" action="" enctype ="multipart/form-data">
    <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="topTableBg">
      <tr>
        <td width="98%"><span class="topTableBgText"><view:LanguageTag key="tkn_auth_title"/></span></td>
        <td width="2%" align="right">
      		<a href="javascript:addAdmPermCode('030106','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" hspace="6" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a></td>
      </tr>
    </table>
	<table width="100%" border="0"  cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
		 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
             <tr>
               <td width="25%" align="right" valign="top"><view:LanguageTag key="tkn_comm_tknum"/><view:LanguageTag key="colon"/></td>
               <td width="30%"><input type="text" id="token" name="token" class="formCss100" /></td>
               <td width="45%" class="divTipCss"><div id="tokenTip"></div></td>
             </tr>
             <tr>
               <td align="right">&nbsp;</td>
               <td>
               <input type="checkbox" id="crbox" name="crbox" onClick="selectCR();" value="" /><view:LanguageTag key="tkn_challenge_response_tkn"/>
               <div id="getDiv" style="display:none">
                 <table width="100%" border="0" cellspacing="0" cellpadding="0">
                   <tr>
                     <td height="6"></td>
                   </tr>
                 </table>
                 <table width="100%" border="0" cellspacing="0" cellpadding="0">
                 <tr>
                   <td width="35%"><a href="#" id="getQS" onclick="getQS();" class="button"><span><view:LanguageTag key="tkn_get_challenge_val"/></span></a></td>
                   <td width="65%"><div id="qsDiv"></div><input type="hidden" id="qsv" name="qsv" class="formCss100" /></td>
                 </tr>
               </table></div></td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="tkn_dynamic_pwd"/><view:LanguageTag key="colon"/></td>
               <td><input type="text" id="otp" name="otp" class="formCss100" /></td>
               <td class="divTipCss"><div id="otpTip"></div></td>
             </tr>
             <tr>
               <td align="right">&nbsp;</td>
               <td><a href="#" id="tknAuth" class="button"><span><view:LanguageTag key="tkn_auth"/></span></a></td>
               <td></td>
             </tr>
           </table>
		</td>
      </tr>
    </table>
  </form>
 
  </body>
</html>