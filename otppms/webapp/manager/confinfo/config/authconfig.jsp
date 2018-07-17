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
		var wnadjustmode = '${authConfInfo.wndadjustmode}';
		if(wnadjustmode =='0') {
			$("tr[id='wndadjustperiodTR']").hide();
			$("#wndadjustperiod").unFormValidator(true); 
		}else {
			$("tr[id='wndadjustperiodTR']").show();
			$("#wndadjustperiod").unFormValidator(false);
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
			
     //认证基本配置
     $('#hotp_auth_wnd').formValidator({onFocus:'<view:LanguageTag key="auth_vd_hotp_auth_wnd_show"/>',onCorrect:"OK"}).inputValidator({min:1, max:9999, type:"number",onError:'<view:LanguageTag key="auth_vd_hotp_auth_wnd_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'});
     $('#hotpadjustwnd').formValidator({onFocus:'<view:LanguageTag key="auth_vd_hotp_adjust_wnd_show"/>', onCorrect:"OK"}).inputValidator({min:1, max:9999, type:"number",onError:'<view:LanguageTag key="auth_vd_hotp_adjust_wnd_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'});
     $('#hotp_sync_wnd').formValidator({onFocus:'<view:LanguageTag key="auth_vd_hotp_sync_wnd_show"/>', onCorrect:"OK"}).inputValidator({min:2, max:9999, type:"number",onError:'<view:LanguageTag key="auth_vd_hotp_sync_wnd_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'}).compareValidator({desID:"hotp_auth_wnd",operateor:">", dataType:"number", onError:'<view:LanguageTag key="auth_vd_hotp_adjust_wnd_err_1"/>'}).functionValidator({
    	fun:function(val){
         	if($.formValidator.isOneValid("hotp_auth_wnd") && $.formValidator.isOneValid("hotpadjustwnd")) {
         		var hauthval = parseInt($("#hotp_auth_wnd").val(),10);
         		var hadjustval = parseInt($("#hotpadjustwnd").val(),10);
         		var hsyncval = parseInt(val,10);
         		if ((hauthval + hadjustval)>hsyncval) {
         			return "<view:LanguageTag key="auth_vd_hotp_sync_wnd_err_2"/>";
         		}
         		return true;
         	}
       	}
	 });

     $('#totp_auth_wnd').formValidator({onFocus:'<view:LanguageTag key="auth_vd_totp_auth_wnd_show"/>', onCorrect:"OK"}).inputValidator({min:1, max:999, type:"number",onError:'<view:LanguageTag key="auth_vd_totp_auth_wnd_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'});
     $('#totpadjustwnd').formValidator({onFocus:'<view:LanguageTag key="auth_vd_totp_adjust_wnd_show"/>', onCorrect:"OK"}).inputValidator({min:1, max:999, type:"number",onError:'<view:LanguageTag key="auth_vd_totp_adjust_wnd_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'});
     $('#totp_sync_wnd').formValidator({onFocus:'<view:LanguageTag key="auth_vd_totp_sync_wnd_show"/>', onCorrect:"OK"}).inputValidator({min:2, max:999, type:"number",onError:'<view:LanguageTag key="auth_vd_totp_sync_wnd_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'}).compareValidator({desID:"totp_auth_wnd",operateor:">", dataType:"number", onError:'<view:LanguageTag key="auth_vd_totp_adjust_wnd_err_1"/>'}).functionValidator({
		fun:function(val){
         	if($.formValidator.isOneValid("totp_auth_wnd") && $.formValidator.isOneValid("totpadjustwnd")) {
         		var tauthval = parseInt($("#totp_auth_wnd").val(),10);
         		var tadjustval = parseInt($("#totpadjustwnd").val(),10);
         		var tsyncval = parseInt(val,10);
         		if ((tauthval + tadjustval)>tsyncval) {
         			return "<view:LanguageTag key="auth_vd_totp_sync_wnd_err_2"/>";
         		}
         		return true;
         	}
       	}
	 });
	 
     $('#wndadjustperiod').formValidator({onFocus:'<view:LanguageTag key="auth_vd_wnd_adjust_period_show"/>',onCorrect:"OK"}).inputValidator({min:1, max:99, type:"number",onError:'<view:LanguageTag key="auth_vd_wnd_adjust_period_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'});

     $('#wndadjustmode').formValidator({onFocus:'<view:LanguageTag key="auth_vd_wnd_adjust_mode_show"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="auth_vd_wnd_adjust_mode_show"/>'});
	 $('#retryotptimeinterval').formValidator({onFocus:'<view:LanguageTag key="auth_vd_retry_interval_show"/>',onCorrect:"OK"}).regexValidator({regExp:"num1",dataType:"enum",onError:'<view:LanguageTag key="common_num1_validate"/>'}).functionValidator({
	     	fun:function(val){
	         	if($.trim(val)=='' || $.trim(val)==null){return '<view:LanguageTag key="auth_vd_retry_interval_err"/>';}
	         	if(val>86400){return '<view:LanguageTag key="auth_vd_retry_interval_err_1"/>';}
	         	if(!checkNumber(val)) {return '<view:LanguageTag key="auth_vd_retry_interval_err_2"/>';}
	         	return true;
	       	}
	 });
	 
	 $('#user_max_retry').formValidator({onFocus:'<view:LanguageTag key="auth_vd_user_max_retry_show"/>',onCorrect:"OK"}).inputValidator({min:1, max:65535, type:"number",onError:'<view:LanguageTag key="auth_vd_user_max_retry_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'});
     $('#user_lock_expire').formValidator({onFocus:'<view:LanguageTag key="auth_vd_user_lock_expire_show"/>',onCorrect:"OK"}).inputValidator({min:1, max:65535, type:"number",onError:'<view:LanguageTag key="auth_vd_user_lock_expire_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'});
     $('#max_retry').formValidator({onFocus:'<view:LanguageTag key="auth_vd_max_retry_show"/>',onCorrect:"OK"}).inputValidator({min:1, max:65535, type:"number",onError:'<view:LanguageTag key="auth_vd_max_retry_err"/>'}).compareValidator({desID:"user_max_retry",operateor:">=", dataType:"number", onError:'<view:LanguageTag key="auth_vd_lock_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'});   
    }
    
    //窗口调整模式
    function selectMode(val) {
    	if(val == '0') {
    		$("tr[id='wndadjustperiodTR']").hide();
			$("#wndadjustperiod").unFormValidator(true); 
    	}else {
    		$("tr[id='wndadjustperiodTR']").show();
			$("#wndadjustperiod").unFormValidator(false);
    	}
    }
	
	// 保存数据
	function savaData(){
    	var url = "<%=path%>/manager/confinfo/config/authConfAction!modify.action";
		var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
		$("#authconfForm").ajaxSubmit({
		   async:true,
		   dataType : "json",  
		   type:"POST", 
		   url : url,
		   data:{"oper" : "initconf"},
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
  <form id="authconfForm" method="post" action="" name="authconfForm">
    <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="topTableBg">
      <tr>
        <td width="98%"><span class="topTableBgText"><view:LanguageTag key="authconf_auth_config"/></span></td>
        <td width="2%" align="right">
      	 	<!--<a href="javascript:openHelpWin('<%=path%>/manager/help/confighelp.html#387','<%=path%>/manager/common/js/ligerUI/skins/Gray/css/dialog.css');">
      		<img src="<%=path%>/images/icon/help_1.png" width="16" height="16" hspace="5" title='<view:LanguageTag key="common_syntax_help"/>' align="absmiddle" />
      		</a>-->
        </td>
      </tr>
    </table>  
   <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="ulOnInsideTable">
      <tr>
        <td valign="top">
		    <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="ulOnInsideTable">
			    <tr>
			       <td>&nbsp;</td>
			       <td align="center" class="topTableBgText"><view:LanguageTag key="authconf_event_tkn_win_policy"/></td>
			       <td>&nbsp;</td>
			    </tr>
			    <tr>
	             <td width="30%" height="20" align="right"><view:LanguageTag key="authconf_hotp_auth_wnd"/><view:LanguageTag key="colon"/></td>
	             <td width="30%" height="20" align="left"><div class="tableTDivLeft"><input type="text" id="hotp_auth_wnd" name="authConfInfo.hotpauthwnd" value="${authConfInfo.hotpauthwnd}" class="formCss100"/></div></td>
	             <td width="40%" class="divTipCss"><div id="hotp_auth_wndTip"></div></td>
	            </tr>
	            <tr>
	             <td align="right" height="20"><view:LanguageTag key="authconf_hotp_adjust_wnd"/><view:LanguageTag key="colon"/></td>
	             <td align="left" height="20"><div class="tableTDivLeft"><input type="text" id="hotpadjustwnd" name="authConfInfo.hotpadjustwnd" value="${authConfInfo.hotpadjustwnd}" class="formCss100"/></div></td>
	             <td class="divTipCss"><div id="hotpadjustwndTip"></div></td>
	            </tr>
	            <tr>
	             <td align="right" height="20"><view:LanguageTag key="authconf_hotp_sync_wnd"/><view:LanguageTag key="colon"/></td>
	             <td align="left" height="20"><div class="tableTDivLeft"><input type="text" id="hotp_sync_wnd" name="authConfInfo.hotpsyncwnd" value="${authConfInfo.hotpsyncwnd}" class="formCss100"/></div>
	             </td>
	             <td class="divTipCss"><div id="hotp_sync_wndTip"></div></td>
	            </tr>
	            <tr>
			       <td>&nbsp;</td>
			       <td align="center" class="topTableBgText"><view:LanguageTag key="authconf_time_tkn_win_policy"/></td>
			       <td>&nbsp;</td>
			    </tr>
	            <tr>
	             <td align="right" height="20"><view:LanguageTag key="authconf_totp_auth_wnd"/><view:LanguageTag key="colon"/></td>
	             <td align="left" height="20"><div class="tableTDivLeft"><input type="text" id="totp_auth_wnd" name="authConfInfo.totpauthwnd" value="${authConfInfo.totpauthwnd}" class="formCss100"/></div></td>
	             <td class="divTipCss"><div id="totp_auth_wndTip"></div></td>
	            </tr>
	            <tr>
	             <td align="right" height="20"><view:LanguageTag key="authconf_totp_adjust_wnd"/><view:LanguageTag key="colon"/></td>
	             <td align="left" height="20"><div class="tableTDivLeft"><input type="text" id="totpadjustwnd" name="authConfInfo.totpadjustwnd" value="${authConfInfo.totpadjustwnd}" class="formCss100"/></div></td>
	             <td class="divTipCss"><div id="totpadjustwndTip"></div></td>
	            </tr>
				<tr>
	             <td align="right" height="20"><view:LanguageTag key="authconf_totp_sync_wnd"/><view:LanguageTag key="colon"/></td>
	             <td align="left" height="20"><div class="tableTDivLeft"><input type="text" id="totp_sync_wnd" name="authConfInfo.totpsyncwnd" value="${authConfInfo.totpsyncwnd}" class="formCss100"/></div>
	             </td>
	             <td class="divTipCss"><div id="totp_sync_wndTip"></div></td>
	            </tr>
	            <tr>
			       <td>&nbsp;</td>
			       <td align="center" class="topTableBgText"><view:LanguageTag key="authconf_win_adjust_mode"/></td>
			       <td>&nbsp;</td>
			    </tr>
			    <tr>
	             <td align="right" height="20"><view:LanguageTag key="authconf_wnd_adjust_mode"/><view:LanguageTag key="colon"/></td>
	             <td align="left" height="20">
	             	<select id="wndadjustmode" name="authConfInfo.wndadjustmode" onchange="selectMode(this.value);" class="select100">
			           <option value="0" <c:if test="${authConfInfo.wndadjustmode=='0'}">selected</c:if> ><view:LanguageTag key="wnd_no_adjust"/></option>
			           <option value="1" <c:if test="${authConfInfo.wndadjustmode=='1'}">selected</c:if> ><view:LanguageTag key="wnd_exceed_adjust_noadd"/></option>
			           <option value="2" <c:if test="${authConfInfo.wndadjustmode=='2'}">selected</c:if> ><view:LanguageTag key="wnd_exceed_adjust_add"/></option>
			        </select>
	             </td>
	             <td class="divTipCss"><div id="wndadjustmodeTip"></div></td>
	            </tr>
				<tr id="wndadjustperiodTR">
	             <td align="right" height="20"><view:LanguageTag key="authconf_wnd_adjust_period"/><view:LanguageTag key="colon"/></td>
	             <td align="left" height="20">
	             	<input type="text" id="wndadjustperiod" name="authConfInfo.wndadjustperiod" value="${authConfInfo.wndadjustperiod}" class="formCss100"/>
	             </td>
	             <td class="divTipCss"><div id="wndadjustperiodTip"></div></td>
	            </tr>
				<tr>
			       <td>&nbsp;</td>
			       <td align="center" class="topTableBgText"><view:LanguageTag key="authconf_replay_strategy"/></td>
			       <td>&nbsp;</td>
			    </tr>
			    <tr>
	             <td align="right" height="20"><view:LanguageTag key="authconf_retry_interval"/><view:LanguageTag key="colon"/></td>
	             <td align="left" height="20">
	             <input type="text" id="retryotptimeinterval" name="authConfInfo.retryotptimeinterval" value="${authConfInfo.retryotptimeinterval}" class="formCss100"/>
	             </td>
	             <td class="divTipCss"><div id="retryotptimeintervalTip"></div></td>
	            </tr>
	            <tr>
			       <td>&nbsp;</td>
			       <td align="center" class="topTableBgText"><view:LanguageTag key="authconf_auth_err_lock_policy"/></td>
			       <td>&nbsp;</td>
			    </tr>
	            <tr>
		          <td align="right" height="20"><view:LanguageTag key="authconf_temp_lock_retry"/><view:LanguageTag key="colon"/></td>
		          <td align="left" height="20"><div class="tableTDivLeft"><input type="text" id="user_max_retry" name="authConfInfo.templockretry" value="${authConfInfo.templockretry}" class="formCss100"/></div></td>
		          <td class="divTipCss"><div id="user_max_retryTip"></div></td>
	            </tr>
				<tr>
	             <td align="right" height="20"><view:LanguageTag key="authconf_temp_lock_expire"/><view:LanguageTag key="colon"/></td>
	             <td align="left" height="20"><div class="tableTDivLeft"><input type="text" id="user_lock_expire" name="authConfInfo.templockexpire" value="${authConfInfo.templockexpire}" class="formCss100"/></div>
	             </td>
	             <td class="divTipCss"><div id="user_lock_expireTip"></div></td>
	            </tr>
	            <tr>
	             <td align="right" height="20"><view:LanguageTag key="authconf_max_retry"/><view:LanguageTag key="colon"/></td>
	             <td align="left" height="20"><div class="tableTDivLeft"><input type="text" id="max_retry" name="authConfInfo.maxretry" value="${authConfInfo.maxretry}" class="formCss100"/></div>
	             </td>
	             <td class="divTipCss"><div id="max_retryTip"></div></td>
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