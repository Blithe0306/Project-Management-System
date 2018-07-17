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
		//绑定点击事件
		domClickEvent();
		//点击按钮弹出窗口，设置窗口样式，取消已绑定点击事件
		var cssurl = "<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"
        btnClickEvent("#saveBt",cssurl);
        
	
		checkInput();
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
	  
	   //短信令牌
       $('#sms_token_otp_expire').formValidator({onFocus:'<view:LanguageTag key="smsconf_vd_sms_tk_otp_expire_show"/>',onCorrect:"OK"}).inputValidator({min:60, max:86400, type:"number",onError:'<view:LanguageTag key="smsconf_vd_sms_tk_otp_expire_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'});
       $('#sms_token_gen_expire').formValidator({onFocus:'<view:LanguageTag key="smsconf_vd_sms_tk_gen_expire_show"/>',onCorrect:"OK"}).inputValidator({min:1, max:3600, type:"number",onError:'<view:LanguageTag key="smsconf_vd_sms_tk_gen_expire_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'}).compareValidator({desID:"sms_token_otp_expire",operateor:"<", dataType:"number", onError:'<view:LanguageTag key="smsconf_vd_sms_tk_gen_expire_err_1"/>'});
       $("#sms_otp_seed_message").formValidator({onFocus:'<view:LanguageTag key="sms_please_sms_otp_seed_message"/>',onCorrect:"OK"}).inputValidator({min:1,max:64,onError:'<view:LanguageTag key="sms_please_sms_otp_seed_message_error"/>'}).functionValidator({
       fun:function(val){
			var mval = $.trim(val);
	  		if(mval.indexOf('[SMSOTP]')<0) {
	  			return '<view:LanguageTag key="sms_smstkn_message_fomat_tip"/>';
	  		}else {
	  			return true;
	  		}
		}
		});
		
       	$('#sms_token_req_attr').formValidator({onFocus:'<view:LanguageTag key="smstoken_conf_req_more_attr_err"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="smstoken_conf_req_more_attr_err"/>'});
       	$('#sms_token_req_val').formValidator({onFocus:'<view:LanguageTag key="smstoken_conf_req_more_val_err"/>',onCorrect:"OK"}).inputValidator({min:1, max:64,onError:'<view:LanguageTag key="smstoken_conf_req_more_val_err1"/>'}).regexValidator({regExp:"username",dataType:"enum",onError:'<view:LanguageTag key="smstoken_conf_validate"/>'});
		$('#sms_token_req_send').formValidator({onFocus:'<view:LanguageTag key="smstoken_conf_req_send_check_err"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="smstoken_conf_req_send_check_err"/>'});
		$('#sms_token_req_return').formValidator({onFocus:'<view:LanguageTag key="smstoken_conf_req_return_code_err"/>',onCorrect:"OK"}).inputValidator({min:1, max:255, type:"number",onError:'<view:LanguageTag key="smstoken_conf_req_return_code_err1"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'});	
	}
	
	// 保存数据
	function savaData(){
    	var url = "<%=path%>/manager/confinfo/config/tokenConfAction!modify.action";
		var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
		$("#smsconfForm").ajaxSubmit({
		   async:true,
		   dataType : "json",  
		   type:"POST", 
		   url : url,
		   data:{"oper" : "smstkn"},
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
	//-->
	</script>
  </head>
  
  <body>
    <div id="background"  class="background"  style="display: none; "></div>
  <div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
  <input id="contextPath" type="hidden" value="<%=path%>" />
  <form id="smsconfForm" method="post" action="" name="smsconfForm">
    <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="topTableBg">
      <tr>
        <td width="98%"><span class="topTableBgText"><view:LanguageTag key="common_menu_config_sms_tkn"/></span></td>
        <td width="2%" align="right">
      	 <!--	<a href="javascript:openHelpWin('<%=path%>/manager/help/confighelp.html#387','<%=path%>/manager/common/js/ligerUI/skins/Gray/css/dialog.css');">
      		<img src="<%=path%>/images/icon/help_1.png" width="16" height="16" hspace="5" title='<view:LanguageTag key="common_syntax_help"/>' align="absmiddle" />
      		</a>-->
        </td>
      </tr>
    </table>  
   <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
      <tr>
        <td valign="top">
		    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		      <tr>
		       <td>&nbsp;</td>
		       <td align="center" class="topTableBgText"><view:LanguageTag key="smstoken_conf_sms"/></td>
		       <td>&nbsp;</td>
			  </tr>
		      <tr>
		         <td width="30%" align="right"><view:LanguageTag key="smstoken_conf_sms_tkn_auth_expire"/><view:LanguageTag key="colon"/></td>
		         <td width="30%">
		           <input type="text" id="sms_token_otp_expire" name="tokenConfInfo.smstokenauthexpire" value="${tokenConfInfo.smstokenauthexpire}" class="formCss100" />
		         </td>
		         <td width="40%" class="divTipCss"><div id ="sms_token_otp_expireTip"></div></td>
		      </tr>
		      <tr>
		         <td align="right"><view:LanguageTag key="smstoken_conf_sms_tkn_gen_expire"/><view:LanguageTag key="colon"/></td>
		         <td> 
		           <input type="text" id="sms_token_gen_expire" name="tokenConfInfo.smstokengenexpire" value="${tokenConfInfo.smstokengenexpire}" class="formCss100" />
		         </td>
		         <td class="divTipCss"><div id ="sms_token_gen_expireTip"></div></td>
		      </tr>

		      <tr>
		        <td align="right" valign="top"><view:LanguageTag key="smstoken_conf_sms_otp_seed_message"/><view:LanguageTag key="colon"/></td>
		        <td>
		        	<input id="sms_otp_seed_message" name="tokenConfInfo.smsotpseedmessage" type="text"  value="${tokenConfInfo.smsotpseedmessage}" class="formCss100" />
		        </td>
		        <td class="divTipCss"><div id="sms_otp_seed_messageTip"></div></td>
		      </tr>
		      
		      <tr>
		       <td>&nbsp;</td>
		       <td align="center" class="topTableBgText"><view:LanguageTag key="smstoken_conf_req"/></td>
		       <td>&nbsp;</td>
			  </tr>
		      
		      <tr>
	             <td align="right" width="30%" height="20"><view:LanguageTag key="smstoken_conf_req_more_attr"/><view:LanguageTag key="colon"/></td>
	             <td align="left" width="30%" height="20">
	             	<select id="sms_token_req_attr" name="tokenConfInfo.smstokenreqattr" class="select100">
			           <option value="State" <c:if test="${tokenConfInfo.smstokenreqattr=='State'}">selected</c:if> >State</option>
			        </select>
	             </td>
	             <td width="40%"  class="divTipCss"><div id="sms_token_req_attrTip"></div></td>
	          </tr>
		      
		      <tr>
		         <td align="right"><view:LanguageTag key="smstoken_conf_req_more_attr_val"/><view:LanguageTag key="colon"/></td>
		         <td> 
		           <input id="sms_token_req_val" name="tokenConfInfo.smstokenreqval" type="text"  value="${tokenConfInfo.smstokenreqval}" class="formCss100" />
		         </td>
		         <td class="divTipCss"><div id ="sms_token_req_valTip"></div></td>
		      </tr>
		      
		      <tr>
	             <td align="right" height="20"><view:LanguageTag key="smstoken_conf_req_send_before_check"/><view:LanguageTag key="colon"/></td>
	             <td align="left" height="20">
	             	<select id="sms_token_req_send" name="tokenConfInfo.smstokenreqsend" class="select100">
			           <option value="0" <c:if test="${tokenConfInfo.smstokenreqsend=='0'}">selected</c:if> ><view:LanguageTag key="smstoken_conf_send_type_0"/></option>
			           <option value="1" <c:if test="${tokenConfInfo.smstokenreqsend=='1'}">selected</c:if> ><view:LanguageTag key="smstoken_conf_send_type_1"/></option>
			           <option value="2" <c:if test="${tokenConfInfo.smstokenreqsend=='2'}">selected</c:if> ><view:LanguageTag key="smstoken_conf_send_type_2"/></option>
			        </select>
	             </td>
	             <td class="divTipCss"><div id="sms_token_req_sendTip"></div></td>
	          </tr>
			  
			  <tr>
		        <td align="right" valign="top"><view:LanguageTag key="smstoken_conf_req_return_code_domain"/><view:LanguageTag key="colon"/></td>
		        <td>
		        	<input id="sms_token_req_return" name="tokenConfInfo.smstokenreqreturn" type="text"  value="${tokenConfInfo.smstokenreqreturn}" class="formCss100" />
		        </td>
		        <td class="divTipCss"><div id="sms_token_req_returnTip"></div></td>
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