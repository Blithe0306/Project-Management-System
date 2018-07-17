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
    <title><view:LanguageTag key="common_menu_config_core_oper"/></title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
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
	
	 $(document).ready(function(){
	 		//暂时隐藏用户转换格式需要时再做显示
	 		$("tr[id='useridformatTR']").hide();
	 		
	 	    $("#menu li").each(function(index) {
              $(this).click(function() {
                $("#menu li.tabFocus").removeClass("tabFocus");
                $(this).addClass("tabFocus");
                $("#content li:eq(" + index + ")").show().siblings().hide();
              });
            });
            
            var mtks = '${coreConfInfo.maxbindtokens}';
            var musers = '${coreConfInfo.maxbindusers}';
            if (mtks==1 && musers ==1) {
            	$("tr[id='tkbindischangeorgTR']").show();
            	$("tr[id='tkunbindischangeorgTR']").show();
            }else {
            	$("tr[id='tkbindischangeorgTR']").hide();
            	$("tr[id='tkunbindischangeorgTR']").hide();
            }

            if (musers ==1) {
            	$("tr[id='uschangeistknTR']").show();
            	$("tr[id='usunbindistknTR']").show();
            }else {
            	$("tr[id='uschangeistknTR']").hide();
            	$("tr[id='usunbindistknTR']").hide();
            }
            
 			checkBasicForm();
	 }) 

     //认证基本校验
    function checkBasicForm(){
    	 $.formValidator.initConfig({submitButtonID:'coreSave',debug:true,
			onSuccess:function(){
			  saveObj();
			},
			onError:function(){
			
				//验证校验是否通过，并定位到没有通过校验的li
				var inputArray=$("input");
            	for(var i=0;i<inputArray.length;i++){//循环整个input数组
	                var input =inputArray[i];//取到每一个input
	                var ispass = $.formValidator.isOneValid(input.id);
	                if (!ispass) {
	                	$("#content li").each(function(index){
	                		if($(this).has("input[id="+input.id+"]").text()!=""){
	                			$("#menu li.tabFocus").removeClass("tabFocus");
				                $("#menu li:eq(" + this.id + ")").addClass("tabFocus");
				                $("#content li:eq(" + this.id + ")").show().siblings().hide();
	                		}
	                	});
	                }
	            }
				
				return false;
			}});
			
       	 //认证基本配置
         $('#hotp_auth_wnd').formValidator({onFocus:'<view:LanguageTag key="core_vd_hotp_auth_wnd_show"/>',onCorrect:"OK"}).inputValidator({min:1, max:9999, type:"number",onError:'<view:LanguageTag key="core_vd_hotp_auth_wnd_err"/>'}).regexValidator({regExp:"notempty",dataType:"enum",onError:'<view:LanguageTag key="org_vd_remove_space_tip"/>'}).functionValidator({
        	 fun:function(val){
        	 if(!checkNumber(val)) {return '<view:LanguageTag key="core_vd_hotp_auth_wnd_err"/>';}
        	 return true;
	       	}
	     });
	     $('#hotpadjustwnd').formValidator({onFocus:'<view:LanguageTag key="core_vd_hotp_adjust_wnd_show"/>', onCorrect:"OK"}).inputValidator({min:1, max:9999, type:"number",onError:'<view:LanguageTag key="core_vd_hotp_adjust_wnd_err"/>'}).regexValidator({regExp:"notempty",dataType:"enum",onError:'<view:LanguageTag key="org_vd_remove_space_tip"/>'}).compareValidator({desID:"hotp_auth_wnd",operateor:">=", dataType:"number", onError:'<view:LanguageTag key="core_vd_hotp_adjust_wnd_err_1"/>'}).functionValidator({
        	 fun:function(val){
        	 if(!checkNumber(val)) {return '<view:LanguageTag key="core_vd_hotp_adjust_wnd_err"/>';}
        	 return true;
	       	}
	     });
	     $('#hotp_sync_wnd').formValidator({onFocus:'<view:LanguageTag key="core_vd_hotp_sync_wnd_show"/>', onCorrect:"OK"}).inputValidator({min:2, max:9999, type:"number",onError:'<view:LanguageTag key="core_vd_hotp_sync_wnd_err"/>'}).regexValidator({regExp:"notempty",dataType:"enum",onError:'<view:LanguageTag key="org_vd_remove_space_tip"/>'}).compareValidator({desID:"hotpadjustwnd",operateor:">=", dataType:"number", onError:'<view:LanguageTag key="core_vd_hotp_sync_wnd_err_1"/>'}).functionValidator({
        	 fun:function(val){
        	 if(!checkNumber(val)) {return '<view:LanguageTag key="core_vd_hotp_sync_wnd_err"/>';}
        	 return true;
	       	}
	     });
	     $('#totp_auth_wnd').formValidator({onFocus:'<view:LanguageTag key="core_vd_totp_auth_wnd_show"/>', onCorrect:"OK"}).inputValidator({min:1, max:999, type:"number",onError:'<view:LanguageTag key="core_vd_totp_auth_wnd_err"/>'}).regexValidator({regExp:"notempty",dataType:"enum",onError:'<view:LanguageTag key="org_vd_remove_space_tip"/>'}).functionValidator({
        	 fun:function(val){
        	 if(!checkNumber(val)) {return '<view:LanguageTag key="core_vd_totp_auth_wnd_err"/>';}
        	 return true;
	       	}
	     });
	     $('#totpadjustwnd').formValidator({onFocus:'<view:LanguageTag key="core_vd_totp_adjust_wnd_show"/>', onCorrect:"OK"}).inputValidator({min:1, max:999, type:"number",onError:'<view:LanguageTag key="core_vd_totp_adjust_wnd_err"/>'}).regexValidator({regExp:"notempty",dataType:"enum",onError:'<view:LanguageTag key="org_vd_remove_space_tip"/>'}).compareValidator({desID:"totp_auth_wnd",operateor:">=", dataType:"number", onError:'<view:LanguageTag key="core_vd_totp_adjust_wnd_err_1"/>'}).functionValidator({
        	 fun:function(val){
        	 if(!checkNumber(val)) {return '<view:LanguageTag key="core_vd_totp_adjust_wnd_err"/>';}
        	 return true;
	       	}
	     });
	     $('#totp_sync_wnd').formValidator({onFocus:'<view:LanguageTag key="core_vd_totp_sync_wnd_show"/>', onCorrect:"OK"}).inputValidator({min:2, max:999, type:"number",onError:'<view:LanguageTag key="core_vd_totp_sync_wnd_err"/>'}).regexValidator({regExp:"notempty",dataType:"enum",onError:'<view:LanguageTag key="org_vd_remove_space_tip"/>'}).compareValidator({desID:"totpadjustwnd",operateor:">=", dataType:"number", onError:'<view:LanguageTag key="core_vd_totp_sync_wnd_err_1"/>'}).functionValidator({
        	 fun:function(val){
        	 if(!checkNumber(val)) {return '<view:LanguageTag key="core_vd_totp_sync_wnd_err"/>';}
        	 return true;
	       	}
	     });
	     $('#wndadjustperiod').formValidator({onFocus:'<view:LanguageTag key="core_vd_wnd_adjust_period_show"/>',onCorrect:"OK"}).inputValidator({min:1, max:99, type:"number",onError:'<view:LanguageTag key="core_vd_wnd_adjust_period_err"/>'}).regexValidator({regExp:"notempty",dataType:"enum",onError:'<view:LanguageTag key="org_vd_remove_space_tip"/>'}).functionValidator({
        	 fun:function(val){
        	 if(!checkNumber(val)) {return '<view:LanguageTag key="core_vd_wnd_adjust_period_err"/>';}
        	 return true;
	       	}
	     }); 
	     $('#wndadjustmode').formValidator({onFocus:'<view:LanguageTag key="core_vd_wnd_adjust_mode_show"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="core_vd_wnd_adjust_mode_show"/>'});
	     $('#user_max_retry').formValidator({onFocus:'<view:LanguageTag key="core_vd_user_max_retry_show"/>',onCorrect:"OK"}).inputValidator({min:1, max:65535, type:"number",onError:'<view:LanguageTag key="core_vd_user_max_retry_err"/>'}).regexValidator({regExp:"notempty",dataType:"enum",onError:'<view:LanguageTag key="org_vd_remove_space_tip"/>'}).functionValidator({
        	 fun:function(val){
        	 if(!checkNumber(val)) {return '<view:LanguageTag key="core_vd_user_max_retry_err"/>';}
        	 return true;
	       	}
	     });  
	     $('#user_lock_expire').formValidator({onFocus:'<view:LanguageTag key="core_vd_user_lock_expire_show"/>',onCorrect:"OK"}).inputValidator({min:1, max:65535, type:"number",onError:'<view:LanguageTag key="core_vd_user_lock_expire_err"/>'}).regexValidator({regExp:"notempty",dataType:"enum",onError:'<view:LanguageTag key="org_vd_remove_space_tip"/>'}).functionValidator({
        	 fun:function(val){
        	 if(!checkNumber(val)) {return '<view:LanguageTag key="core_vd_user_lock_expire_err"/>';}
        	 return true;
	       	}
	     });   
	     $('#max_retry').formValidator({onFocus:'<view:LanguageTag key="core_vd_max_retry_show"/>',onCorrect:"OK"}).inputValidator({min:1, max:65535, type:"number",onError:'<view:LanguageTag key="core_vd_max_retry_err"/>'}).compareValidator({desID:"user_max_retry",operateor:">=", dataType:"number", onError:'<view:LanguageTag key="coreconf_lock_err"/>'}).regexValidator({regExp:"notempty",dataType:"enum",onError:'<view:LanguageTag key="org_vd_remove_space_tip"/>'}).functionValidator({
        	 fun:function(val){
        	 if(!checkNumber(val)) {return '<view:LanguageTag key="core_vd_max_retry_err"/>';}
        	 return true;
	       	}
	     });   
	     $('#retryotptimeinterval').formValidator({onFocus:'<view:LanguageTag key="core_vd_retry_interval_show"/>',onCorrect:"OK"}).functionValidator({
	     	fun:function(val){
	         	if($.trim(val)=='' || $.trim(val)==null){return '<view:LanguageTag key="core_vd_retry_interval_err"/>';}
	         	if(val>86400){return '<view:LanguageTag key="core_vd_retry_interval_err_1"/>';}
	         	if(!checkNumber(val)) {return '<view:LanguageTag key="core_vd_retry_interval_err_2"/>';}
	         	return true;
	       	}
	     });
	     
	     $("input[name='coreConfInfo.useridformattype']").formValidator({tipID:"useridformattypeTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,max:1, onError: '<view:LanguageTag key="common_vd_please_sel"/>'});
		
		 //绑定用户和令牌
	     $('#max_bind_tokens').formValidator({onFocus:'<view:LanguageTag key="core_vd_max_bind_tks_show"/>',onCorrect:"OK"}).inputValidator({min:1, max:99, type:"number",onError:'<view:LanguageTag key="core_vd_max_bind_tks_err"/>'})
	     .functionValidator({
				fun:function(val){
					var mbindu = $('#max_bind_users').val();
					if(val ==1 && mbindu == 1) {
						$("tr[id='tkbindischangeorgTR']").show();
						$("tr[id='tkunbindischangeorgTR']").show();
					}else {
						$("tr[id='tkbindischangeorgTR']").hide();
						$("tr[id='tkunbindischangeorgTR']").hide();
						$("input[name=coreConfInfo.tkbindischangeorg][value=0]").attr("checked",true);
						$("input[name=coreConfInfo.tkunbindischangeorg][value=0]").attr("checked",true);
					}
					if(mbindu == 1) {
						$("tr[id='uschangeistknTR']").show();
		            	$("tr[id='usunbindistknTR']").show();
					}else {
						$("tr[id='uschangeistknTR']").hide();
		            	$("tr[id='usunbindistknTR']").hide();
		            	$("input[name=coreConfInfo.replaceselect][value=0]").attr("checked",true);
						$("input[name=coreConfInfo.unbindselect][value=0]").attr("checked",true);
					}
					return true;
				}
			
		 });  
	     $('#max_bind_users').formValidator({onFocus:'<view:LanguageTag key="core_vd_max_bind_users_show"/>',onCorrect:"OK"}).inputValidator({min:1, max:99, type:"number",onError:'<view:LanguageTag key="core_vd_max_bind_tks_err"/>'})
	     .functionValidator({
				fun:function(val){
					var mbindtk = $('#max_bind_tokens').val();
					if(val ==1 && mbindtk == 1) {
						$("tr[id='tkbindischangeorgTR']").show();
						$("tr[id='tkunbindischangeorgTR']").show();
					}else {
						$("tr[id='tkbindischangeorgTR']").hide();
						$("tr[id='tkunbindischangeorgTR']").hide();
						$("input[name=coreConfInfo.tkbindischangeorg][value=0]").attr("checked",true);
						$("input[name=coreConfInfo.tkunbindischangeorg][value=0]").attr("checked",true);
					}
					if(val == 1) {
						$("tr[id='uschangeistknTR']").show();
		            	$("tr[id='usunbindistknTR']").show();
					}else {
						$("tr[id='uschangeistknTR']").hide();
		            	$("tr[id='usunbindistknTR']").hide();
						$("input[name=coreConfInfo.replaceselect][value=0]").attr("checked",true);
						$("input[name=coreConfInfo.unbindselect][value=0]").attr("checked",true);
					}
					return true;
				}
			
		 });  
	     
	     $('#add_user_when_bind').formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
         $("input[name='coreConfInfo.unbindselect']").formValidator({tipID:"unbindselectTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,max:1, onError: '<view:LanguageTag key="common_vd_please_sel"/>'});
         $("input[name='coreConfInfo.replaceselect']").formValidator({tipID:"replaceselectTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,max:1, onError: '<view:LanguageTag key="common_vd_please_sel"/>'});
         $("input[name='coreConfInfo.adduserwhenbind']").formValidator({tipID:"adduserwhenbindTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,max:1, onError: '<view:LanguageTag key="common_vd_please_sel"/>'});
         $("input[name='coreConfInfo.tokenoverlapbind']").formValidator({tipID:"tokenoverlapbindTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,max:1, onError: '<view:LanguageTag key="common_vd_please_sel"/>'});
	    
	   	 //令牌绑定解绑配置
	     $(":radio[name='coreConfInfo.tkbindischangeorg']").formValidator({tipID:"tkbindischangeorgTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:1,max:1,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
	     $(":radio[name='coreConfInfo.tkunbindischangeorg']").formValidator({tipID:"tkunbindischangeorgTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:1,max:1,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
  
	    
	     //应急口令
         $('#token_empin2otp').formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
	     $('#empin_otp_leneq').formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
	     
	     $("#defaultdomainid").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
	     $("#langSel").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
	     
         $('#hotp_auth_wnd').focus();
    }   

	//修改核心功能配置
	function saveObj(){
	    $('#coreForm').ajaxSubmit({
		    type:"post",
		    async:false,
		    url:"<%=path%>/manager/confinfo/config/coreAction!modify.action",
		    dataType : "json",
		    success:function(msg){
		      if(msg.errorStr=='success'){
		         $.ligerDialog.success(msg.object,'<view:LanguageTag key="common_syntax_tip"/>',null);
  			  }else{
		         FT.toAlert(msg.errorStr,msg.object,null);
		      }
		    }
		 });   
	}
	//重置核心功能配置
	function resetObj(){
		 window.location.href="<%=path%>/manager/confinfo/config/coreAction!find.action";
	}
	</script>
 </head>
  
 <body>
 <form name="coreForm" id="coreForm" method="post" action="">
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td width="100%" valign="top">
	    <a style="float:right;" href="javascript:addAdmPermCode('0500','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/keep.png" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a>&nbsp;&nbsp;&nbsp;&nbsp;
		<ul id="menu">
		  <li class="tabFocus"><view:LanguageTag key="coreconf_auth_config"/></li>
		  <li><view:LanguageTag key="coreconf_bind_user_tkn"/></li>
		  <li><view:LanguageTag key="coreconf_emergency_pwd"/></li>
		  <li><view:LanguageTag key="coreconf_def_domain_lang"/></li>
	    </ul>
	    <ul id="content">
	    	 
		<!-- core -->
 		<li  class="conFocus" id="0">
 		   <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		     <tr>
		       <td colspan="3" height="25" class="topTableBgText" background="<%=path%>/images/manager/mgr_r7_c8.png"><view:LanguageTag key="coreconf_auth_config"/></td>
		     </tr>
		   </table>  
		   <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
            <tr>
             <td width="30%" height="20" align="right"><view:LanguageTag key="coreconf_hotp_auth_wnd"/><view:LanguageTag key="colon"/></td>
             <td width="30%" height="20" align="left"><div class="tableTDivLeft"><input type="text" id="hotp_auth_wnd" name="coreConfInfo.hotpauthwnd" value="${coreConfInfo.hotpauthwnd}" class="formCss100"/></div></td>
             <td width="40%" class="divTipCss"><div id="hotp_auth_wndTip"></div></td>
            </tr>
            <tr>
             <td align="right" height="20"><view:LanguageTag key="coreconf_hotp_adjust_wnd"/><view:LanguageTag key="colon"/></td>
             <td align="left" height="20"><div class="tableTDivLeft"><input type="text" id="hotpadjustwnd" name="coreConfInfo.hotpadjustwnd" value="${coreConfInfo.hotpadjustwnd}" class="formCss100"/></div></td>
             <td class="divTipCss"><div id="hotpadjustwndTip"></div></td>
            </tr>
            <tr>
             <td align="right" height="20"><view:LanguageTag key="coreconf_hotp_sync_wnd"/><view:LanguageTag key="colon"/></td>
             <td align="left" height="20"><div class="tableTDivLeft"><input type="text" id="hotp_sync_wnd" name="coreConfInfo.hotpsyncwnd" value="${coreConfInfo.hotpsyncwnd}" class="formCss100"/></div>
             </td>
             <td class="divTipCss"><div id="hotp_sync_wndTip"></div></td>
            </tr>
            <tr>
             <td align="right" height="20"><view:LanguageTag key="coreconf_totp_auth_wnd"/><view:LanguageTag key="colon"/></td>
             <td align="left" height="20"><div class="tableTDivLeft"><input type="text" id="totp_auth_wnd" name="coreConfInfo.totpauthwnd" value="${coreConfInfo.totpauthwnd}" class="formCss100"/></div></td>
             <td class="divTipCss"><div id="totp_auth_wndTip"></div></td>
            </tr>
            <tr>
             <td align="right" height="20"><view:LanguageTag key="coreconf_totp_adjust_wnd"/><view:LanguageTag key="colon"/></td>
             <td align="left" height="20"><div class="tableTDivLeft"><input type="text" id="totpadjustwnd" name="coreConfInfo.totpadjustwnd" value="${coreConfInfo.totpadjustwnd}" class="formCss100"/></div></td>
             <td class="divTipCss"><div id="totpadjustwndTip"></div></td>
            </tr>
			<tr>
             <td align="right" height="20"><view:LanguageTag key="coreconf_totp_sync_wnd"/><view:LanguageTag key="colon"/></td>
             <td align="left" height="20"><div class="tableTDivLeft"><input type="text" id="totp_sync_wnd" name="coreConfInfo.totpsyncwnd" value="${coreConfInfo.totpsyncwnd}" class="formCss100"/></div>
             </td>
             <td class="divTipCss"><div id="totp_sync_wndTip"></div></td>
            </tr>
			<tr>
             <td align="right" height="20"><view:LanguageTag key="coreconf_wnd_adjust_period"/><view:LanguageTag key="colon"/></td>
             <td align="left" height="20">
             	<input type="text" id="wndadjustperiod" name="coreConfInfo.wndadjustperiod" value="${coreConfInfo.wndadjustperiod}" class="formCss100"/>
             </td>
             <td class="divTipCss"><div id="wndadjustperiodTip"></div></td>
            </tr>
			<tr>
             <td align="right" height="20"><view:LanguageTag key="coreconf_wnd_adjust_mode"/><view:LanguageTag key="colon"/></td>
             <td align="left" height="20">
             	<select id="wndadjustmode" name="coreConfInfo.wndadjustmode" class="select100">
		           <option value="0" <c:if test="${coreConfInfo.wndadjustmode=='0'}">selected</c:if> ><view:LanguageTag key="wnd_no_adjust"/></option>
		           <option value="1" <c:if test="${coreConfInfo.wndadjustmode=='1'}">selected</c:if> ><view:LanguageTag key="wnd_exceed_adjust_noadd"/></option>
		           <option value="2" <c:if test="${coreConfInfo.wndadjustmode=='2'}">selected</c:if> ><view:LanguageTag key="wnd_exceed_adjust_add"/></option>
		        </select>
             </td>
             <td class="divTipCss"><div id="wndadjustmodeTip"></div></td>
            </tr>
            <tr>
	          <td align="right" height="20"><view:LanguageTag key="coreconf_temp_lock_retry"/><view:LanguageTag key="colon"/></td>
	          <td align="left" height="20"><div class="tableTDivLeft"><input type="text" id="user_max_retry" name="coreConfInfo.templockretry" value="${coreConfInfo.templockretry}" class="formCss100"/></div></td>
	          <td class="divTipCss"><div id="user_max_retryTip"></div></td>
            </tr>
			<tr>
             <td align="right" height="20"><view:LanguageTag key="coreconf_temp_lock_expire"/><view:LanguageTag key="colon"/></td>
             <td align="left" height="20"><div class="tableTDivLeft"><input type="text" id="user_lock_expire" name="coreConfInfo.templockexpire" value="${coreConfInfo.templockexpire}" class="formCss100"/></div>
             </td>
             <td class="divTipCss"><div id="user_lock_expireTip"></div></td>
            </tr>
            <tr>
             <td align="right" height="20"><view:LanguageTag key="coreconf_max_retry"/><view:LanguageTag key="colon"/></td>
             <td align="left" height="20"><div class="tableTDivLeft"><input type="text" id="max_retry" name="coreConfInfo.maxretry" value="${coreConfInfo.maxretry}" class="formCss100"/></div>
             </td>
             <td class="divTipCss"><div id="max_retryTip"></div></td>
            </tr>
            
            <tr id="useridformatTR">
               <td align="right"><view:LanguageTag key="coreconf_uid_format"/><view:LanguageTag key="colon"/></td>
               <td> 
			      	<input type="radio" id="useridformattype0" name="coreConfInfo.useridformattype" value="0"
			        	<c:if test="${coreConfInfo.useridformattype eq 0 }">checked</c:if>
			        /><view:LanguageTag key="uid_format_no_convert"/> &nbsp;&nbsp;&nbsp;&nbsp;
			        <input type="radio" id="useridformattype1" name="coreConfInfo.useridformattype" value="1"  
			        <c:if test="${coreConfInfo.useridformattype eq 1 }">checked</c:if>
			        /><view:LanguageTag key="uid_fmt_cvt_lowercase"/> &nbsp;&nbsp;&nbsp;&nbsp;
			        
			   </td>
               <td class="divTipCss"><div id ="useridformattypeTip"></div></td>
             </tr>
            
            <tr>
             <td align="right" height="20"><view:LanguageTag key="coreconf_retry_interval"/><view:LanguageTag key="colon"/></td>
             <td align="left" height="20">
             <input type="text" id="retryotptimeinterval" name="coreConfInfo.retryotptimeinterval" value="${coreConfInfo.retryotptimeinterval}" class="formCss100"/>
             </td>
             <td class="divTipCss"><div id="retryotptimeintervalTip"></div></td>
            </tr>
 		 </table>
         </li>
         
          <!-- 绑定用户和令牌 -->
         <li id="1">
         <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="ulOnInsideTable">
	       <tr>
	       	  <td colspan="3" height="25" class="topTableBgText" background="<%=path%>/images/manager/mgr_r7_c8.png"><view:LanguageTag key="coreconf_bind_user_tkn"/></td>
	       </tr>
	   	 </table>  
         <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
            <tr>
              <td width="30%" align="right" height="20"><view:LanguageTag key="coreconf_max_bind_tks"/><view:LanguageTag key="colon"/></td>
              <td width="30%" align="left"  height="20"><div class="tableTDivLeft"><input type="text" id="max_bind_tokens" name="coreConfInfo.maxbindtokens" value="${coreConfInfo.maxbindtokens}" class="formCss100" /></div>
               </td>
               <td width="40%" class="divTipCss"><div id="max_bind_tokensTip"></div></td>
            </tr>
            <tr>
              <td align="right" height="20"><view:LanguageTag key="coreconf_max_bind_users"/><view:LanguageTag key="colon"/></td>
              <td align="left" height="20"><div class="tableTDivLeft"><input type="text" id="max_bind_users" name="coreConfInfo.maxbindusers" value="${coreConfInfo.maxbindusers}" class="formCss100" /></div>
             	</td>
             	<td class="divTipCss"><div id="max_bind_usersTip"></div></td>
            </tr>
            
            <tr id="tkbindischangeorgTR">
				 <td align="right"><view:LanguageTag key="coreconf_tkbind_ischange_org"/><view:LanguageTag key="colon"/></td>
				 <td>
					<input type="radio" name="coreConfInfo.tkbindischangeorg" id="tkbindischangeorg"  value="1"
		            <c:if test="${coreConfInfo.tkbindischangeorg =='1'}">checked</c:if>/><view:LanguageTag key="common_syntax_yes"/> &nbsp;&nbsp;&nbsp;&nbsp;
		        	<input type="radio" name="coreConfInfo.tkbindischangeorg" id="tkbindischangeorg1" value="0"
		            <c:if test="${coreConfInfo.tkbindischangeorg =='0'}">checked</c:if>/><view:LanguageTag key="common_syntax_no"/>  
				 </td>
				 <td class="divTipCss"><div id="tkbindischangeorgTip"></div></td>
			 </tr>
             <tr id="tkunbindischangeorgTR">
				 <td align="right"><view:LanguageTag key="coreconf_tkunbind_ischange_org"/><view:LanguageTag key="colon"/></td>
				 <td>
					<input type="radio" name="coreConfInfo.tkunbindischangeorg" id="tkunbindischangeorg"  value="1"
		            <c:if test="${coreConfInfo.tkunbindischangeorg =='1'}">checked</c:if>/><view:LanguageTag key="common_syntax_yes"/> &nbsp;&nbsp;&nbsp;&nbsp;
		        	<input type="radio" name="coreConfInfo.tkunbindischangeorg" id="tkunbindischangeorg1" value="0"
		            <c:if test="${coreConfInfo.tkunbindischangeorg =='0'}">checked</c:if>/><view:LanguageTag key="common_syntax_no"/> 
				 </td>
				 <td class="divTipCss"><div id="tkunbindischangeorgTip"></div></td>
			 </tr>
            
             <tr>
             	<td align="right" height="20"><view:LanguageTag key="coreconf_adduser_when_bind"/><view:LanguageTag key="colon"/></td>
             	<td align="left" height="20">
	                
	                <input type="radio" id="adduserwhenbind0" name="coreConfInfo.adduserwhenbind" value="y"
			        	<c:if test="${coreConfInfo.adduserwhenbind == 'y'}">checked</c:if>
			        /><view:LanguageTag key="common_syntax_yes"/> &nbsp;&nbsp;&nbsp;&nbsp;
			        <input type="radio" id="adduserwhenbind1" name="coreConfInfo.adduserwhenbind" value="n"  
			        <c:if test="${coreConfInfo.adduserwhenbind=='n'}">checked</c:if>
			        /><view:LanguageTag key="common_syntax_no"/> &nbsp;&nbsp;&nbsp;&nbsp;
	             </td>
	             <td class="divTipCss"><div id="adduserwhenbindTip"></div></td>
            </tr>
             <tr id="usunbindistknTR">
             	<td align="right" height="20"><view:LanguageTag key="coreconf_unbind_select"/><view:LanguageTag key="colon"/></td>
             	<td align="left" height="20">
		            <input type="radio" id="unbindselect1" name="coreConfInfo.unbindselect" value="1"  
			        <c:if test="${coreConfInfo.unbindselect eq 1 }">checked</c:if>
			        /><view:LanguageTag key="common_syntax_yes"/> &nbsp;&nbsp;&nbsp;&nbsp;
		            <input type="radio" id="unbindselect0" name="coreConfInfo.unbindselect" value="0"
			        	<c:if test="${coreConfInfo.unbindselect eq 0 }">checked</c:if>
			        /><view:LanguageTag key="common_syntax_no"/> &nbsp;&nbsp;&nbsp;&nbsp;
	             </td>
	             <td class="divTipCss"><div id="unbindselectTip"></div></td>
            </tr>
            <tr id="uschangeistknTR">
             	<td align="right" height="20"><view:LanguageTag key="coreconf_replace_select"/><view:LanguageTag key="colon"/></td>
             	<td align="left" height="20">
		            <input type="radio" id="replaceselect0" name="coreConfInfo.replaceselect" value="0"
			        	<c:if test="${coreConfInfo.replaceselect eq 0 }">checked</c:if>
			        /><view:LanguageTag key="core_tkn_rep_continue_use"/> &nbsp;&nbsp;&nbsp;&nbsp;
			        <input type="radio" id="replaceselect1" name="coreConfInfo.replaceselect" value="1"  
			        <c:if test="${coreConfInfo.replaceselect eq 1 }">checked</c:if>
			        /><view:LanguageTag key="tkn_comm_disable"/> &nbsp;&nbsp;&nbsp;&nbsp;
			        <input type="radio" id="replaceselect2" name="coreConfInfo.replaceselect" value="2"  
			        <c:if test="${coreConfInfo.replaceselect eq 2 }">checked</c:if>
			        /><view:LanguageTag key="tkn_comm_invalid"/>&nbsp;&nbsp;&nbsp;&nbsp;
	             </td>
	             <td class="divTipCss"><div id="replaceselectTip"></div></td>
            </tr>
          </table>            
         </li>
         
          <!-- 应急口令 -->
         <li id="2">  
         <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="ulOnInsideTable">
	       <tr>
	       	  <td colspan="3" height="25" class="topTableBgText" background="<%=path%>/images/manager/mgr_r7_c8.png"><view:LanguageTag key="coreconf_emergency_pwd"/></td>
	       </tr>
	   	 </table>
          <table width="100%"  border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
            <tr>
            	<td width="30%"></td>
            	<td width="30%"></td>
            	<td width="40%">&nbsp;</td>
            </tr>   
            <tr>
             <td align="right"><view:LanguageTag key="core_tkn_empin2otp_descp"/><view:LanguageTag key="colon"/></td>
             <td align="left">
	             <div class="tableTDivLeft">
	              <select id="token_empin2otp" name="coreConfInfo.tokenempin2otp" class="select100">
	               <option value="1" <c:if test="${coreConfInfo.tokenempin2otp=='1'}">selected</c:if> ><view:LanguageTag key="common_syntax_yes"/></option>
	               <option value="0" <c:if test="${coreConfInfo.tokenempin2otp=='0'}">selected</c:if> ><view:LanguageTag key="common_syntax_no"/></option>
	              </select>
	             </div>
             	 </td>
             	 <td width="45%" class="divTipCss"><div id="token_empin2otpTip"></div></td>
            </tr>
            <tr>
             <td align="right" height="20"><view:LanguageTag key="coreconf_empin_otp_len_eq"/><view:LanguageTag key="colon"/></td>
             <td align="left" height="20">
             	<div class="tableTDivLeft">
	              <select id="empin_otp_leneq" name="coreConfInfo.empinotpleneq" class="select100">
	               <option value="1" <c:if test="${coreConfInfo.empinotpleneq=='1'}">selected</c:if> ><view:LanguageTag key="common_syntax_yes"/></option>
	               <option value="0" <c:if test="${coreConfInfo.empinotpleneq=='0'}">selected</c:if> ><view:LanguageTag key="common_syntax_no"/></option>
	              </select>
             	</div>
              </td>
              <td class="divTipCss"><div id="empin_otp_leneqTip"></div></td>
            </tr>
          </table>
          </li>
          <li id="3">
            <!-- 域 -->
         <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="ulOnInsideTable">
	       <tr>
	       	  <td colspan="3" height="25" class="topTableBgText" background="<%=path%>/images/manager/mgr_r7_c8.png"><view:LanguageTag key="coreconf_def_domain_lang"/></td>
	       </tr>
	   	 </table>
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
            <tr>
             <td width="30%" height="20" align="right"><view:LanguageTag key="coreconf_def_domain"/><view:LanguageTag key="colon"/></td>
             <td width="30%" height="20" align="left">
             		<select id="defaultdomainid"  name="coreConfInfo.defaultdomainid" class="select100">
				      <view:DomainTag dataSrc="${coreConfInfo.defaultdomainid}" index1Lang="" index1Val=""  />
			    	</select>
              </td>
              <td width="40%" class="divTipCss"><div id="defaultdomainidTip"></div></td>
            </tr>
            <tr>
             <td align="right"><view:LanguageTag key="coreconf_def_lang"/><view:LanguageTag key="colon"/></td>
             <td align="left">
			    	<select id="langSel" name="coreConfInfo.defaultsystemlanguage" class="select100">
			          <view:LanguageSelectTag key="${coreConfInfo.defaultsystemlanguage}" />
			        </select>
              </td>
              <td class="divTipCss"><div id="langSelTip"></div></td>
            </tr>
          </table>
          </li>
        </ul>
	   </td>
      </tr>
    </table>
    
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
      <tr>
        <td align="right" width="30%"></td>
        <td align="center" width="40%">
            <a href="#" id="coreSave" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
        </td>
        <td align="right" width="30%"></td>
     </tr>
    </table>
   </form>
  </body>
</html>