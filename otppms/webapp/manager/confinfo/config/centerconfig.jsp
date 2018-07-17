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
	  
	  //管理员配置	
	  $("#loginerrorretrytemp").formValidator({onFocus:'<view:LanguageTag key="center_vd_login_err_retry_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:65535,type:"number",onError:'<view:LanguageTag key="center_vd_login_err_retry_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'});
	  $("#loginerrorretrylong").formValidator({onFocus:'<view:LanguageTag key="center_vd_login_err_retry_long_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:65535,type:"number",onError:'<view:LanguageTag key="center_vd_login_err_retry_long_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'}).compareValidator({desID:"loginerrorretrytemp",operateor:">=", dataType:"number", onError:'<view:LanguageTag key="center_vd_login_err"/>'});
	  $("#loginlockexpire").formValidator({onFocus:'<view:LanguageTag key="center_vd_login_lock_exp_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:1440,type:"number",onError:'<view:LanguageTag key="center_vd_login_lock_exp_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'});
      $(":radio[name='centerInfo.prohibitadmin']").formValidator({tipID:"prohibitadminTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:1,max:1,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
	  $("#passwdupdateperiod").formValidator({onFocus:'<view:LanguageTag key="center_vd_upd_pwd_period_show"/>',onCorrect:"OK"}).inputValidator({min:0,max:65535,type:"number",onError:'<view:LanguageTag key="center_vd_upd_pwd_period_err"/>'}).regexValidator({regExp:"intege3",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'});
       
	}
	
	// 保存数据
	function savaData(){
		var ifDisAdmin = $("input[name='centerInfo.prohibitadmin']:checked").val();
		var roleMark = '${curLoginUserRole}';
		if(ifDisAdmin == 'y' && roleMark != '' && roleMark == 'ADMIN'){
			FT.toAlert('warn', '<view:LanguageTag key="center_admin_no_diseanble"/>', null);
			return;
		}
		
		var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
		
    	var url = "<%=path%>/manager/confinfo/config/center!modify.action";
		$("#centerForm").ajaxSubmit({
			async:true,
			dataType : "json",
		   	type:"POST",
		   	url : url,
		   	data:{"oper" : "adminconf"},
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
  <form id="centerForm" method="post" action="" name="centerForm">
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="topTableBg">
      <tr>
        <td width="98%"><span class="topTableBgText"><view:LanguageTag key="common_menu_config_admin"/></span></td>
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
		    <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="ulOnInsideTable">
				<tr>
			       <td>&nbsp;</td>
			       <td align="center" class="topTableBgText"><view:LanguageTag key="center_conf_admin_login_policy"/></td>
			       <td>&nbsp;</td>
				</tr>
				<tr>
					<td width="30%" align="right"><view:LanguageTag key="center_login_error_retry" /><view:LanguageTag key="colon" /></td>
					<td width="40%">
						<input type="text" id="loginerrorretrytemp" name="centerInfo.loginerrorretrytemp" class="formCss100"
							value="${centerInfo.loginerrorretrytemp}" />
					</td>
					<td width="30%" class="divTipCss"><div id="loginerrorretrytempTip"></div></td>
				</tr>
				<tr>
					<td align="right"><view:LanguageTag key="center_login_err_retry_long" /><view:LanguageTag key="colon" /></td>
					<td>
						<input type="text" id="loginerrorretrylong" name="centerInfo.loginerrorretrylong" class="formCss100"
							value="${centerInfo.loginerrorretrylong}" />
					</td>
					<td class="divTipCss"><div id="loginerrorretrylongTip"></div></td>
				</tr>
				<tr>
					<td align="right"><view:LanguageTag key="center_login_lock_expire" /><view:LanguageTag key="colon" /></td>
					<td>
						<input type="text" id="loginlockexpire" name="centerInfo.loginlockexpire" class="formCss100"
							value="${centerInfo.loginlockexpire}" />
					</td>
					<td class="divTipCss"><div id="loginlockexpireTip"></div></td>
				</tr>

				<tr>
					<td align="right"><view:LanguageTag key="center_update_period" /><view:LanguageTag key="colon" /></td>
					<td>
						<input type="text" id="passwdupdateperiod" name="centerInfo.passwdupdateperiod" class="formCss100"
							value="${centerInfo.passwdupdateperiod}" />
					</td>
					<td class="divTipCss"><div id="passwdupdateperiodTip"></div></td>
				</tr>
				
				<tr>
			       <td>&nbsp;</td>
			       <td align="center" class="topTableBgText"><view:LanguageTag key="center_conf_admin_perm_policy"/></td>
			       <td>&nbsp;</td>
				</tr>
				
				<tr>
					<td align="right">
						<view:LanguageTag key="center_prohibit_admin" /><view:LanguageTag key="colon" />
					</td>
					<td>
						<input type="radio" name="centerInfo.prohibitadmin" id="prohibitadmin" value="y"
							<c:if test="${centerInfo.prohibitadmin =='y'}">checked</c:if> /><view:LanguageTag key="common_syntax_yes" />&nbsp;&nbsp;&nbsp;
						<input type="radio" name="centerInfo.prohibitadmin" id="prohibitadmin1" value="n"
							<c:if test="${centerInfo.prohibitadmin =='n'}">checked</c:if> /><view:LanguageTag key="common_syntax_no" />
					</td>
					<td class="divTipCss"><div id="prohibitadminTip"></div></td>
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