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
		// 激活密码是否通过短信分发
		$(":radio[name^='tokenConfInfo.apsmssend']").click(function(e){
			if($(this).val() === '1') { 
				$("tr[id='mactmessageTR']").show();
				//$("tr[id='onlineDistMTR']").hide();
				$("#mobileactivatecodemessage").unFormValidator(false); 
				$("#mobileonlinedistmessage").unFormValidator(false); 
			}else {
			 	$("tr[id='mactmessageTR']").hide();
			 	//$("tr[id='onlineDistMTR']").hide();
			 	$("#mobileactivatecodemessage").unFormValidator(true); 
			 	$("#mobileonlinedistmessage").unFormValidator(true); 
			}
		});	
		
		var smssend = '${tokenConfInfo.apsmssend}';
		if(smssend =='0') {
			$("tr[id='mactmessageTR']").hide();
			//$("tr[id='onlineDistMTR']").hide();
			$("#mobileactivatecodemessage").unFormValidator(true); 
			$("#mobileonlinedistmessage").unFormValidator(true); 
		}else {
			$("tr[id='mactmessageTR']").show();
			//$("tr[id='onlineDistMTR']").show();
			$("#mobileactivatecodemessage").unFormValidator(false); 
			$("#mobileonlinedistmessage").unFormValidator(false); 
		}
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
	  
	  	$("#apperiod").formValidator({onFocus:'<view:LanguageTag key="distconf_vd_ap_period_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:7,type:"number",onError: '<view:LanguageTag key="distconf_vd_ap_period_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'});
		$("#apretry").formValidator({onFocus:'<view:LanguageTag key="distconf_vd_ap_retry_show"/>',onCorrect:"OK"}).inputValidator({min:2,max:20,type:"number",onError:'<view:LanguageTag key="distconf_vd_ap_retry_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'});
		$("#siteenabled").formValidator({tipID:"sexTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:1,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
		$("#defultap").formValidator({onFocus:'<view:LanguageTag key="distconf_vd_def_ap_err"/>',onCorrect:"OK"}).regexValidator({regExp:"^[0-9]{4,16}$",onError:'<view:LanguageTag key="distconf_vd_def_ap_err"/>'});
		$("#provsitetype").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
		$("#protocol").formValidator({tipID:"urlTip",onFocus:'<view:LanguageTag key="distconf_vd_site_url_show"/>',onCorrect:"OK"}).functionValidator({
		  fun:function(val){
		  	if($.trim(val)=='' || $.trim(val)==null) {
		  		return '<view:LanguageTag key="distconf_vd_site_url_not_empty"/>';
		  	}else {
		        if(val=="http" || val=="https"){
				    return true;
			    }else{
				    return '<view:LanguageTag key="distconf_vd_url_format"/>';
			    }
		  	}
		  }
		  });
		$("#ip").formValidator({tipID:"urlTip",onFocus:'<view:LanguageTag key="distconf_vd_url_ip"/>',onCorrect:"OK"}).regexValidator({regExp:"ip4",dataType:"enum",onError:'<view:LanguageTag key="distconf_vd_url_ip_format"/>'}).functionValidator({
		 fun:function(val){
		     if($.trim(val)=='' || $.trim(val)==null) {
		  		return '<view:LanguageTag key="distconf_vd_url_ip_not_empty"/>';
		  	 }else if($.trim(val)=='127.0.0.1' || $.trim(val)=='localhost') {
		  		return '<view:LanguageTag key="distconf_vd_input_other_ip"/>';
		  	 }else {
			     return true;
		  	 }
		    }
		 });
		$("#port").formValidator({tipID:"urlTip",onFocus:'<view:LanguageTag key="distconf_vd_url_port_show"/>',onCorrect:"OK"}).functionValidator({fun:checkPort,onError:'<view:LanguageTag key="distconf_vd_url_port_err"/>'});
		$("#path").formValidator({tipID:"urlTip",onFocus:'<view:LanguageTag key="distconf_vd_url_path_show"/>',onCorrect:"OK"}).functionValidator({
		   fun:function(val){
		     var re1 = new RegExp(":", "i");
		     var yesorno1 = re1.test(val);
		     if($.trim(val)=='' || $.trim(val)==null) {
		  		return '<view:LanguageTag key="distconf_vd_url_path_not_empty"/>';
		  	 }else {
		       if(yesorno1){
		          return '<view:LanguageTag key="distconf_vd_url_path_format"/>';
		       }else{
		          return true;
		       }
		  	 }
		   }
		}); 
		$("#mobileactivatecodemessage").formValidator({onFocus:'<view:LanguageTag key="sms_please_mobile_activate_code_message"/>',onCorrect:"OK"}).inputValidator({min:1,max:128,onError:'<view:LanguageTag key="sms_please_mobile_activate_code_message_error"/>'}).functionValidator({
			fun:function(val){
				var mval = $.trim(val);
		  		if(mval.indexOf('[SN]')<0 || mval.indexOf('[AP]')<0 || mval.indexOf('[AC]')<0) {
		  			return '<view:LanguageTag key="sms_mobile_message_fomat_tip"/>';
		  		}else {
		  			return true;
		  		}
			}
		});
		$("#mobileonlinedistmessage").formValidator({onFocus:'<view:LanguageTag key="sms_mobile_online_dist_message"/>',onCorrect:"OK"}).inputValidator({min:1,max:128,onError:'<view:LanguageTag key="sms_please_mobile_activate_code_message_error"/>'}).functionValidator({
			fun:function(val){
				var mval = $.trim(val);
		  		if(mval.indexOf('[URL]')<0 || mval.indexOf('[AP]')<0) {
		  			return '<view:LanguageTag key="sms_online_dist_message_fomat_tip"/>';
		  		}else {
		  			return true;
		  		}
			}
		});
		
		
	    $("#apperiod").focus(); 
			
	}
	//选择分发协议时调用
    function orgProtocol(pvalue){
      var p = document.getElementById("protocol");
       if(pvalue == 1){
          p.value = "http";
       }else if(pvalue == 2){
          p.value = "https";
       }else if(pvalue == 3){
          p.value = "http";
       }
	}
	
	// 保存数据
	function savaData(){
    	var url = "<%=path%>/manager/confinfo/config/tokenConfAction!modify.action";
		var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
		$("#mconfForm").ajaxSubmit({
		   async:true,
		   dataType : "json",  
		   type:"POST", 
		   url : url,
		   data:{"oper" : "mobiletkn"},
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
  <form id="mconfForm" method="post" action="" name="mconfForm">
    <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="topTableBg">
      <tr>
        <td width="98%"><span class="topTableBgText"><view:LanguageTag key="common_menu_config_mobile_tkn"/></span></td>
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
			       <td align="center" class="topTableBgText"><view:LanguageTag key="distconf_act_pwd_policy"/></td>
			       <td>&nbsp;</td>
			  </tr>
		      
		      <tr>
		        <td width="30%" align="right"><view:LanguageTag key="distconf_ap_period"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		        <td width="30%">
		        	<input type="text" name="tokenConfInfo.apperiod" id="apperiod" value="${tokenConfInfo.apperiod}" class="formCss100"/>
		        </td>
		        <td width="40%" class="divTipCss"><div id="apperiodTip"></div></td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key="distconf_ap_retry"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		        <td><input type="text" id="apretry" name="tokenConfInfo.apretry" value="${tokenConfInfo.apretry}" class="formCss100"/></td>
		        <td class="divTipCss"><div id="apretryTip"></div></td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key="distconf_defult_ap"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		        <td><input type="text" id="defultap" name="tokenConfInfo.defultap" value="${tokenConfInfo.defultap}" class="formCss100"/></td>
		        <td class="divTipCss"><div id="defultapTip"></div></td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key="distconf_ap_sms_send"/><view:LanguageTag key="colon"/></td>
		        <td>
		        	<input type="radio" id="apsmssend1" name="tokenConfInfo.apsmssend" value="1" 
                		<c:if test="${tokenConfInfo.apsmssend eq 1 }">checked</c:if> /><view:LanguageTag key="common_syntax_yes"/>&nbsp;&nbsp;
		        	<input type="radio" id="apsmssend" name="tokenConfInfo.apsmssend" value="0"
               			<c:if test="${tokenConfInfo.apsmssend eq 0 }">checked</c:if> /><view:LanguageTag key="common_syntax_no"/> 
		        </td>
		        <td class="divTipCss"><div id="apsmssendTip"></div></td>
		      </tr>
		      <tr id="mactmessageTR">
		        <td align="right"><view:LanguageTag key="distconf_ap_sms_send_tip"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		        <td><input type="text" id="mobileactivatecodemessage" name="tokenConfInfo.mobileactivatecodemessage" value="${tokenConfInfo.mobileactivatecodemessage}" class="formCss100"/></td>
		        <td class="divTipCss"><div id="mobileactivatecodemessageTip"></div></td>
		      </tr><!--
		      <tr id="onlineDistMTR" style="display:none">
		        <td align="right"><view:LanguageTag key="distconf_online_dist_send_tip"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		        <td><input type="text" id="mobileonlinedistmessage" name="tokenConfInfo.mobileonlinedistmessage" value="${tokenConfInfo.mobileonlinedistmessage}" class="formCss100"/></td>
		        <td class="divTipCss"><div id="mobileonlinedistmessageTip"></div></td>
		      </tr>
		      
		      --><tr>
		        <td align="right"><view:LanguageTag key="distconf_email_send"/><view:LanguageTag key="colon"/></td>
		        <td>
		        	<input type="radio" id="distemailsend1" name="tokenConfInfo.distemailsend" value="1" 
                		<c:if test="${tokenConfInfo.distemailsend eq 1 }">checked</c:if> /><view:LanguageTag key="common_syntax_yes"/>&nbsp;&nbsp;
		        	<input type="radio" id="distemailsend" name="tokenConfInfo.distemailsend" value="0"
               			<c:if test="${tokenConfInfo.distemailsend eq 0 }">checked</c:if> /><view:LanguageTag key="common_syntax_no"/> 
		                &nbsp;&nbsp;<font color="red"><view:LanguageTag key="distconf_email_send_info"/></font>
		        </td>
		        <td class="divTipCss"><div id="distemailsendTip"></div></td>
		      </tr>
		      
		      <tr>
		       <td>&nbsp;</td>
		       <td align="center" class="topTableBgText"><view:LanguageTag key="distconf_dist_site_policy"/></td>
		       <td>&nbsp;</td>
			  </tr>
		      
		      <tr>
		        <td align="right"><view:LanguageTag key="distconf_site_type"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		        <td><select onChange="orgProtocol(this.value);" name="tokenConfInfo.sitetype" id="provsitetype" class="select100">
		            <option value="1" 
		              <c:if test='${tokenConfInfo.sitetype == 1}'>selected</c:if>
		            >  http</option>
		            <option value="2" 
		              <c:if test='${tokenConfInfo.sitetype == 2}'>selected</c:if>
		             > https</option>
		            <option value="3"
		              <c:if test='${tokenConfInfo.sitetype == 3}'>selected</c:if>
		              > http;https</option>
		          </select>
		        </td>
		        <td class="divTipCss"><div id="provsitetypeTip"></div></td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key="distconf_site_url"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		        <td><input type="text" value="${tokenConfInfo.protocol}" size="3" name="tokenConfInfo.protocol" id="protocol" class="form-text" />
		          ://
		          <input type="text" value="${tokenConfInfo.ip}" size="15" name="tokenConfInfo.ip" id="ip" class="form-text" />
		          :
		          <input type="text" value="${tokenConfInfo.port}" size="5" name="tokenConfInfo.port" id="port" class="form-text" />
		          /
		          <input type="text" value="${tokenConfInfo.path}" size="33" name="tokenConfInfo.path" id="path" class="form-text" />
		            <input type="hidden"   size="25" name="tokenConfInfo.siteurl" id="siteurl"/>
		        </td>
		        <td class="divTipCss"><div id="urlTip"></div></td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key="distconf_site_enabled"/><view:LanguageTag key="colon"/></td>
		        <td>
			        <input type="radio" name="tokenConfInfo.siteenabled" id="provsiteenabled" 
					   value="y"
			            <c:if test="${tokenConfInfo.siteenabled eq 'y'}">checked</c:if>
			          /> <view:LanguageTag key="common_syntax_enable"/>&nbsp;&nbsp;&nbsp;
			            <input type="radio" name="tokenConfInfo.siteenabled" id="provsiteenabled1"
					   value="n"
			          <c:if test="${tokenConfInfo.siteenabled eq 'n'}">checked</c:if>
			          /> <view:LanguageTag key="common_syntax_close"/>
			    </td>
		        <td class="divTipCss"><div id="siteenabledTip"></div></td>
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