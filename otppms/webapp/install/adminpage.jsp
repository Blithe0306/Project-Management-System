<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.ft.otp.util.conf.ConfDataFormat"%>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
	int isLocal = 0;
	if(null != application.getAttribute("isLocalIp")){
	    isLocal = (Boolean)application.getAttribute("isLocalIp") ? 1 : 0;
	}
	String isActivate = ConfDataFormat.getSysConfEmailEnabled()?"true":"false";// 是否需要邮件激活
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
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
    <script language="javascript" type="text/javascript">
	$(document).ready(function(){
		checkAdminForm();
	})
	
	function checkAdminForm(){
	    $.formValidator.initConfig({submitButtonID:"addBtn",debug:true,
		onSuccess:function(){
			addObj();
		}, 
		onError:function(){
			return false;
		}});
		
		$("#adminid").formValidator({onFocus:'<view:LanguageTag key="admin_vd_account_tip"/>'}).inputValidator({min:1,max:64,onError:'<view:LanguageTag key="admin_vd_account_err"/>'}).regexValidator({regExp:"username",dataType:"enum",onError:'<view:LanguageTag key="admin_vd_account_err_1"/>'});
		$("#realname").formValidator({onFocus:'<view:LanguageTag key="admin_vd_name_tip"/>',onCorrect:"OK"}).inputValidator({min:0,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="admin_vd_name_err"/>'}, onError:'<view:LanguageTag key="admin_vd_name_err_1"/>'});
		$("#password").formValidator({onFocus:'<view:LanguageTag key="common_vd_pwd_show"/>',onCorrect:"OK"}).inputValidator({min:4,max:32,onError:'<view:LanguageTag key="common_vd_pwd_show"/>'});
		$("#password2").formValidator({onFocus:'<view:LanguageTag key="common_vd_confirm_pwd_show"/>',onCorrect:"OK"}).inputValidator({min:4,max:32,onError:'<view:LanguageTag key="common_vd_confirm_pwd_show"/>'}).compareValidator({desID:"password",operateor:"=",onError:'<view:LanguageTag key="common_vd_confirm_pwd_err"/>'});
		$("#tel").formValidator({empty:true,onFocus:'<view:LanguageTag key="admin_vd_tel_tip"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:64,onError:'<view:LanguageTag key="admin_vd_tel_phone_len"/>'}).regexValidator({regExp:"tel",dataType:"enum",onError:'<view:LanguageTag key="admin_vd_tel_err"/>'});
		$("#cellphone").formValidator({empty:true,onFocus:'<view:LanguageTag key="admin_vd_phone_tip"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:64,onError:'<view:LanguageTag key="admin_vd_tel_phone_len"/>'}).regexValidator({regExp:"cellphone",dataType:"enum",onError:'<view:LanguageTag key="admin_vd_phone_err"/>'});
		$("#address").formValidator({empty:true,onFocus:'<view:LanguageTag key="admin_vd_addr_tip"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:128,onError:'<view:LanguageTag key="admin_vd_addr_tip"/>'});
		
		if('<%=isActivate%>'=='true'){
			$("#emailSpan").show();
			$("#email").formValidator({onFocus:'<view:LanguageTag key="admin_vd_email_tip_2"/>',onCorrect:"OK"}).inputValidator({min:4,max:255, onError:'<view:LanguageTag key="admin_vd_email_err"/>'}).regexValidator({regExp:"email",dataType:"enum",onError:'<view:LanguageTag key="admin_vd_email_err_1"/>'});
		}else{
			$("#email").formValidator({empty:true,onFocus:'<view:LanguageTag key="admin_vd_email_tip_2"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({min:0,max:255, onError:'<view:LanguageTag key="admin_vd_email_err"/>'}).regexValidator({regExp:"email",dataType:"enum",onError:'<view:LanguageTag key="admin_vd_email_err_1"/>'});
			$("#emailSpan").hide();
		}
		
		$("#descp").formValidator({onFocus:'<view:LanguageTag key="common_vd_descp_show"/>',onCorrect:"OK"}).inputValidator({max:255,onError:'<view:LanguageTag key="common_vd_descp_error"/>'});		
		$("#adminid").focus();
	}
    
    //创建管理员
	function addObj(){
		var ajaxbg = $("#background,#progressBar");//加载等待
		var url = "<%=path%>/manager/admin/user/adminUser!addUnionAdmin.action";
		ajaxbg.show();
		$("#AddForm").ajaxSubmit({
			async:false,    
			dataType:"text",  
			type:"POST", 
			url : url,
			success:function(msg){
				if(msg == 'success'){
					nextIni();
				}else{
					FT.toAlert("error", '<view:LanguageTag key="sys_first_admin_create_err"/>', null);
				}
				ajaxbg.hide();
			}
		});
	}
	
	//下一步
	function nextIni(){
		parent.hideId('06', 'T06');
		if('<%=isLocal%>' == '1'){
			parent.showId('07', 'T07');
		}else{
			parent.toPage("frameView08", "/install/install!finish.action");
			parent.showId('08', 'T08');
		}
	}
	</script>
  </head>
<body style="overflow:auto; overflow-x:hidden">
<div id="background" class="background" style="display: none;"></div>
<div id="progressBar" class="progressBar" style="display: none;"><view:LanguageTag key="sys_being_saved_please_wait"/></div>
  <form name="AddForm" id="AddForm" method="post" action="">
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable" >
		<tr>
		   <td width="23%" align="right"><view:LanguageTag key="admin_info_account"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		   <td width="31%">
		       <input type="text" id="adminid" name="adminUser.adminid" value="${adminUser.adminid}" class="formCss100" style="width: 97%"/>
	      </td>
		   <td width="46%" class="divTipCss">
	      	<div id="adminidTip" style="word-wrap:break-word; "></div>
	      </td>
		</tr>
		<tr>
		   <td align="right"><view:LanguageTag key="common_info_realname"/><view:LanguageTag key="colon"/></td>       
		   <td> 
		        <input type="text" id="realname" name="adminUser.realname" class="formCss100"  style="width: 97%"/></td>
		   <td class="divTipCss"><div id="realnameTip" style="word-wrap:break-word; "></div></td>
		</tr>
		<tr>
		  <td align="right"><view:LanguageTag key="admin_info_pwd"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	      <td>
	        <input onpaste="return false" type="password" id="password" name="adminUser.pwd" class="formCss100"  style="width: 97%"/>
	      </td>
	      <td class="divTipCss"><div id="passwordTip" style="word-wrap:break-word;"></div></td>
	  </tr>
		<tr>
		  <td align="right"><view:LanguageTag key="admin_info_confirm_pwd"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	      <td>
	        <input type="password" id="password2" name="password2" class="formCss100"  style="width: 97%"/>
	      </td>
	      <td class="divTipCss"><div id="password2Tip" style="word-wrap:break-word;"></div></td>
	  </tr>
		<tr>
			<td align="right"><view:LanguageTag key="common_info_email"/><span class="text_Hong_Se" id="emailSpan">*</span><view:LanguageTag key="colon"/></td>
			<td><input type="text" id="email" name="adminUser.email" class="formCss100"  style="width: 97%"/></td>
			<td class="divTipCss"><div id="emailTip" style="word-wrap:break-word;"></div></td>
		</tr>
		<tr>
			<td align="right"><view:LanguageTag key="common_info_mobile"/><view:LanguageTag key="colon"/></td>
			<td><input type="text" id="cellphone" name="adminUser.cellphone" class="formCss100"  style="width: 97%"/> </td>
			<td class="divTipCss"><div id="cellphoneTip" style="word-wrap:break-word;"></div></td>
		</tr>
	<!--<tr>
			<td align="right"><view:LanguageTag key="common_info_tel"/><view:LanguageTag key="colon"/></td>
			<td><input  type="text" id="tel" name="adminUser.tel" class="formCss100" /></td>
			<td class="divTipCss"><div id="telTip" style="width:100%; margin-left:5px"></div></td>
		</tr> -->
	<!--<tr>
			<td align="right"><view:LanguageTag key="common_info_address"/><view:LanguageTag key="colon"/></td>
			<td><input type="text" id="address" name="adminUser.address" class="formCss100" /></td>
			<td class="divTipCss"><div id="addressTip" style="width:100%; margin-left:5px"></div></td>
		</tr>-->
		<tr>
			<td align="right"><view:LanguageTag key="admin_info_descp"/><view:LanguageTag key="colon"/></td>
			<td><textarea id="descp" name="adminUser.descp" class="textarea100"  style="width: 97%"></textarea></td>
			<td class="divTipCss"><div id="descpTip" style="word-wrap:break-word;"></div></td>
		</tr>
		<tr>
			<td align="right">&nbsp;</td>
			<td><a href="#" id="addBtn" class="button"><span id="saveId"><view:LanguageTag key="common_syntax_save"/></span></a></td>
			<td>&nbsp;</td>
		</tr>
	  </table>
</form>
</body>
</html>