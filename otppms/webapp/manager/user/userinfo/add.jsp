<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%@ page import="com.ft.otp.util.conf.ConfDataFormat"%>
<%@ page import="com.ft.otp.common.ConfConstant" %>
<%
	String path = request.getContextPath(); 

	String localAuth=ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER, ConfConstant.DEFAULT_LOCALAUTH);
	String backendAuth=ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER, ConfConstant.DEFAULT_BACKENDAUTH);
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
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script> 	
 	
 	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
 	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script language="javascript" src="<%=path%>/manager/user/userinfo/js/add.js"></script>

	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>  
	<script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script>       
	 
	    
	<script language="javascript" type="text/javascript">
	
	   // Start,多语言信息
	   var sel_tkn_lang = '<view:LanguageTag key="user_sel_tkn"/>';
	   var user_group_lang = '<view:LanguageTag key="user_sel_user_group"/>';
	   var need_del_date_lang = '<view:LanguageTag key="common_syntax_check_need_del_date"/>';
	   var bind_tkn_lang = '<view:LanguageTag key="user_seve_succ_is_bind_tkn"/>';
	   var save_succ_lang = '<view:LanguageTag key="common_save_succ_tip"/>';
	   var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
	   var syntax_confirm_lang = '<view:LanguageTag key="common_syntax_confirm"/>';
	   var verify_login_type_check_email_lang = '<view:LanguageTag key="user_vd_init_pwd_login_verify_uemail"/>';
	   var verify_login_type_check_phone_lang = '<view:LanguageTag key="user_vd_init_pwd_login_verify_uphone"/>';
	   // End,多语言信息
	   
	   $(document).ready(function(){
	     checkUserForm();
	     //根据 用户默认属性配置 是默认的本地认证模式和默认的后端认证模式默认选中
	     <%if(null !=localAuth){%>
	     	$("#localAuth").val("<%=localAuth%>");
	     <%}%>
	     <%if(null !=backendAuth){%>
	     	$("#backendAuth").val("<%=backendAuth%>");
	     <%}%>
	   	 changeSel($('#localAuth').val());//是否显示静态密码
	   });
	   
       //用户基本信息在保存和保存下一步，调用的通用方法
       function checkUserForm(){
       		
       	   $.formValidator.initConfig({submitButtonID:"addBtUser",debug:true,
	         	onSuccess:function(){
//	            	saveUserToDb();
					checkUserInfo(0);
         		},
           		onError:function(){
           			return false;
           		}
          });       
       		 
       		var userId = $("#userId").val();
       		 $("#userId").formValidator({onFocus:'<view:LanguageTag key="user_vd_add_userid_err"/>'}).inputValidator({min:0,max:64,onError:'<view:LanguageTag key="user_vd_account_err"/>'}).functionValidator({
       		 fun:function(userId){
	       			if($.trim(userId)=='' || $.trim(userId)==null){
				    	return '<view:LanguageTag key="user_userid_notnull"/>';
				    }
				    if(!letter_u_num_english(userId)){
				       return '<view:LanguageTag key="user_vd_account_err_1"/>';
				    }
				    return true;
			 }});
       		 
       		 //判断用户帐号是否已经存在
       		 /*
			.ajaxValidator({
				dataType:"html",
				async:true,
				url:"<%=path%>/manager/user/userinfo/userInfo!findUserIsExist.action?userInfo.dOrgunitId="+$("#orgunitIds").val(),
				success:function(data){
		            if(data==''){return true;}else{return false;}
				},
				buttons:$("#addBtUser"),
				error:function(jqXHR, textStatus, errorThrown){
					$.messager.alert('<view:LanguageTag key="common_vd_warning"/>','<view:LanguageTag key="common_vd_server_is_busy"/>','warning');
				},
				onError:'<view:LanguageTag key="user_vd_already_exists"/>',
				onWait:'<view:LanguageTag key="user_vd_checking_exists"/>'
			});
			*/
       		 $("#orgunitNames").formValidator({onFocus:'<view:LanguageTag key="user_vd_org_tip"/>', onCorrect:"OK"}).functionValidator({
       		 	fun:function(val){
	                 if($.trim(val)=='' || $.trim(val)==null){
	                 	return '<view:LanguageTag key="user_vd_org_err"/>';
	                 }
	                 return true;
       		 	}
       		 });  

     		 var realName = $("#realName").val();
       		 $("#realName").formValidator({empty:true,onFocus:'<view:LanguageTag key="user_vd_realname_tip"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:64,onError:'<view:LanguageTag key="user_vd_realname_err"/>'}).functionValidator({
 				fun:function(realName){
 			    	if(g_invalid_char_js(realName)){
 			       		return '<view:LanguageTag key="user_vd_realname_err_2"/>';
 			    	}
 			    	return true;
 				}});
		//	 $("#papersNumber").formValidator({empty:true,onFocus:'<view:LanguageTag key="user_vd_document_num_tip"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:64,onError:'<view:LanguageTag key="user_vd_document_num_len"/>'}).regexValidator({regExp:"pnumber",dataType:"enum",onError:'<view:LanguageTag key="user_vd_document_num_err"/>'});
			 $("#email").formValidator({empty:true,onFocus:'<view:LanguageTag key="user_vd_email_tip"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:255,onError:'<view:LanguageTag key="user_vd_email_num_err"/>'}).regexValidator({regExp:"email",dataType:"enum",onError:'<view:LanguageTag key="user_vd_email_err"/>'});
		//	 $("#tel").formValidator({empty:true,onFocus:'<view:LanguageTag key="user_vd_phone_tip"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:20,onError:'<view:LanguageTag key="user_vd_tel_num_err"/>'}).regexValidator({regExp:"telphone",dataType:"enum",onError:'<view:LanguageTag key="user_vd_phone_err"/>'}); 
			 $("#cellPhone").formValidator({empty:true,onFocus:'<view:LanguageTag key="user_vd_tel_tip"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:64,onError:'<view:LanguageTag key="user_vd_cell_num_err"/>'}).regexValidator({regExp:"cellphone",dataType:"enum",onError:'<view:LanguageTag key="user_vd_tel_err"/>'}); 
		//	 $("#address").formValidator({empty:true,onFocus:'<view:LanguageTag key="user_vd_addr_tip"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:128, onError: '<view:LanguageTag key="user_vd_addr_err"/>'});
             $("#userId").focus();
             $("#localAuth").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK",defaultValue:"0"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'}); 
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
       
       /*
       function checkUserInfo(){
       //判断用户帐号是否已经存在
          $("#userId").formValidator({onFocus:'<view:LanguageTag key="user_vd_orgunit_err"/>'}).inputValidator({min:1,max:64,onError:'<view:LanguageTag key="user_vd_userid_err"/>'}).regexValidator({regExp:"username",dataType:"enum",onError:'<view:LanguageTag key="user_vd_userid_err_1"/>'})
       		 //判断用户帐号是否已经存在
			.ajaxValidator({
				dataType:"html",
				async:true,
				url:"<%=path%>/manager/user/userinfo/userInfo!findUserIsExist.action?userInfo.dOrgunitId="+$("#orgunitIds").val(),
				success:function(data){
		            if(data==''){return true;}else{return false;}
				},
				buttons:$("#addBtUser"),
				error:function(jqXHR, textStatus, errorThrown){
					$.messager.alert('<view:LanguageTag key="common_vd_warning"/>','<view:LanguageTag key="common_vd_server_is_busy"/>','warning');
				},
				onError:'<view:LanguageTag key="user_vd_already_exists"/>',
				onWait:'<view:LanguageTag key="user_vd_checking_exists"/>'
			});
		
       }*/
	  //展示静态密码设置框
     function changeSel(obj){
		 //只验证OTP
		 $("#localAuthVal").val(obj);
		 if (obj == 0){
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

		 // 本地认证模式选择不进行本地认证时，移除后端认证中的不需要选项
		 if(obj == 3){
			$("#c").remove();
		 }else{
			if($('#backendAuth option:last').val() != 2){
				$("#b").after("<option value='2' id='c'><view:LanguageTag key='backend_auth_no_need'/></option>");
			}
		 }
	 }
	</script>
	
	
  </head>
  
  <body style="overflow:auto; overflow-x:hidden">
  <jsp:include page="/manager/user/userinfo/common.jsp" />
  <input id="contextPath" type="hidden" value="<%=path%>" />
  <form name="AddForm" method="post" action="" id="AddForm">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td>  
		  <span class="topTableBgText">
			  <view:LanguageTag key="user_info_add"/>
          </span>
        </td>
        <td width="2%" align="right">
        	<a href="javascript:addAdmPermCode('020101','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" hspace="6" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a></td>
      </tr>
    </table> 
	  <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td valign="top">
		  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		  <tr>
              <td align="right"><view:LanguageTag key="user_sel_org"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
              <td>	
              		<input type=hidden id="orgunitIds" name="userInfo.dOrgunitId"  />
              		<input type=text id="orgunitNames" name="adminUser.orgunitName" class="formCss100" onClick="selOrgunits(1,'<%=path%>');" readonly />	 
       	   </td>
		   <td class="divTipCss"><div id="orgunitNamesTip" style="width:100%"></div></td>
		</tr>

       <tr>
        <td width="25%" align="right"><view:LanguageTag key="user_info_account"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
        <td width="30%"><input type="text" id="userId" name="userInfo.userId" class="formCss100"  /></td>
        <td width="45%" class="divTipCss"><div id="userIdTip" style="width:100%"></div></td>
      </tr>
      
      <tr>
        <td align="right"><view:LanguageTag key="user_info_real_name"/><view:LanguageTag key="colon"/></td>
        <td><input type="text" id="realName" name="userInfo.realName" class="formCss100"/></td>
        <td class="divTipCss"><div id="realNameTip" style="width:100%"></div></td>
      </tr>
      <tr>
        <td align="right"><view:LanguageTag key="user_local_auth_mode"/><view:LanguageTag key="colon"/></td>
        <td>
        <select id="localAuth" name="userInfo.localAuth" class="select100"  onchange="changeSel(this.value)">
			<option value="0"><view:LanguageTag key="local_auth_only_vd_tkn"/></option>
			<option value="2"><view:LanguageTag key="local_auth_only_vd_pwd"/></option>
			<option value="1"><view:LanguageTag key="local_auth_vd_pwd_tkn"/></option>
        </select> 
         <input type="hidden" id="localAuthVal" name="localAuthVal" value="0" />
        </td>
        <td class="divTipCss"><div id="localAuthTip" style="width:100%"></div></td>
      </tr>
      <tr id="pwdTR">
        <td align="right"><view:LanguageTag key="user_static_pwd"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
        <td><input onpaste="return false" type="password" id="pwd" name="userInfo.pwd" class="formCss100" /></td>
        <td class="divTipCss"><div id="pwdTip" style="width:100%"></div></td>
      </tr>
      <tr id="confrmpwdTR">
        <td align="right"><view:LanguageTag key="user_conf_static_pwd"/><span class="text_Hong_Se" >*</span><view:LanguageTag key="colon"/></td>
        <td><input onpaste="return false" type="password" id="confrmpwd" name="confrmpwd" class="formCss100" /></td>
        <td class="divTipCss"><div id="confrmpwdTip" style="width:100%"></div></td>
      </tr>
      <tr>
        <td align="right"><view:LanguageTag key="user_whether_backend_auth"/><view:LanguageTag key="colon"/></td>
        <td>
        <select id="backendAuth" name="userInfo.backEndAuth" class="select100" >
			<option value="0" id="a"><view:LanguageTag key="backend_auth_default"/></option>
			<option value="1" id="b"><view:LanguageTag key="backend_auth_need"/></option>
			<option value="2" id="c"><view:LanguageTag key="backend_auth_no_need"/></option>
        </select>  
        </td>
        <td class="divTipCss"><div id="backendAuthTip" style="width:100%"></div></td>
      </tr>
	  <tr>
        <td align="right"><view:LanguageTag key="user_return_client_Rad_conf"/><view:LanguageTag key="colon"/></td>
        <td>
        	<select id="radProfileId" name="userInfo.radProfileId" class="select100" >
        		<view:RadProfileTag dataSrc="${userInfo.radProfileId}" />
        	</select>  
        </td>
        <td class="divTipCss"><div id="radProfileIdTip" style="width:100%"></div></td>
      </tr>     
    <!-- <tr>
        <td align="right"><view:LanguageTag key="user_info_document_type"/><view:LanguageTag key="colon"/></td>
        <td>
             <select id="papersType" name="userInfo.papersType" class="select100" >
			<option value="0"><view:LanguageTag key="user_info_user_num"/></option>
        	</select>
        </td>
        <td class="divTipCss"><div id="papersTypeTip" style="width:100%"></div>
        </td>
      </tr>  --> 
       <!--
      <tr>
        <td align="right"><view:LanguageTag key="user_info_document_num"/><view:LanguageTag key="colon"/></td>
        <td><input type="text" id="papersNumber" name="userInfo.papersNumber" class="formCss100"/>
        </td>
        <td class="divTipCss"><div id="papersNumberTip" style="width:100%"></div>
        </td>
      </tr>--> 
      <tr>
        <td align="right"><view:LanguageTag key="common_info_email"/><view:LanguageTag key="colon"/></td>
        <td><input type="text" id="email" name="userInfo.email" class="formCss100"/></td>
        <td class="divTipCss"><div id="emailTip" style="width:100%"></div></td>
      </tr>
    <!--  <tr>
        <td align="right"><view:LanguageTag key="common_info_tel"/><view:LanguageTag key="colon"/></td>
        <td><input type="text" id="tel" name="userInfo.tel" class="formCss100"/></td>
        <td class="divTipCss"><div id="telTip" style="width:100%"></div></td>
      </tr>--> 
      <tr>
        <td align="right"><view:LanguageTag key="common_info_mobile"/><view:LanguageTag key="colon"/></td>
        <td><input type="text" id="cellPhone" name="userInfo.cellPhone" class="formCss100"/></td>
        <td class="divTipCss"><div id="cellPhoneTip" style="width:100%"></div></td>
      </tr>
     <!-- <tr>
        <td align="right"><view:LanguageTag key="common_info_address"/><view:LanguageTag key="colon"/></td>
        <td><textarea id="address" name="userInfo.address" class="textarea100"></textarea></td>
        <td class="divTipCss"><div id="addressTip" style="width:100%"></div></td>
      </tr> --> 
	  <tr>
     	<td align="right">&nbsp;</td>
      	<td><a href="#" id="addBtUser" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
        </td>
        <td></td>
	  </tr> 
	</table>
	</td>
   </tr>
   <tr>
   <td>
  </td>
 </tr>
 </table>	 
</form>
</body>
</html>