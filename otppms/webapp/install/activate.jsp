<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.ft.otp.common.language.Language"%>
<%
	String path = request.getContextPath();
	
	String titleImg = Language.getCurrLang(request.getSession());
	titleImg += ".png";
	
	// 防止跨站脚本编制
	String source;
	if("0".equals(request.getParameter("source"))){
		source = "0";
	}else if("1".equals(request.getParameter("source"))){
		source = "1";
	}else{
		source = "";
	}
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title><view:LanguageTag key="system_noun_name"/></title>
	<link href="<%=path%>/install/css/install.css" rel="stylesheet" type="text/css" />  
	<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
	<link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>  
    <link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/install/css/ligerui-tab.css" />
	<script type="text/javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" ></script>
	<style type="text/css">
	<!--
	body {
		background-color: #FFFFFF;
		margin-top: 50px;
	}
	
	.iframecss{
		margin: 0px;
		padding: 0px;
	}
	
	.textCss{
		font-family: Arial, Helvetica, sans-serif;
		font-size: 14px;
		font-weight: bold;
		color: #002559;
	}
	-->
	</style>
    <script type="text/javascript">        
        $(function (){
        	$.ajax({ 
				async:true,
				type:"POST",
				url:"<%=path%>/manager/admin/user/adminUser!getPwdDeath.action?adminId=${adminUser.reAdminId}&passwd=${adminUser.rePwd}",
				dataType:"json",
				success:function(msg){
					if(msg.errorStr == 'success'){
						$("#tab1").ligerTab({dblClickToClose:false});
			            $.formValidator.initConfig({submitButtonID:"setPwdBtn",
						onSuccess:function(){
							setAdminPwd();
						},
						onError:function(){
							return false;
						}});
					   $("#password").formValidator({onFocus:'<view:LanguageTag key="admin_vd_new_pwd"/>',onCorrect:"OK"}).inputValidator({min:4,max:32,onError:'<view:LanguageTag key="admin_vd_new_pwd"/>'});
					   $("#passwordConf").formValidator({onFocus:'<view:LanguageTag key="admin_vd_new_confpwd"/>',onCorrect:"OK"}).inputValidator({min:4,max:32,onError:'<view:LanguageTag key="admin_vd_new_confpwd"/>'}).compareValidator({desID:"password",operateor:"=",onError:'<view:LanguageTag key="common_vd_confirm_pwd_err"/>'}); 
				    }else{
				    	$.ligerDialog.warn(msg.object, '<view:LanguageTag key="common_syntax_tip"/>',function(){
				    		window.parent.location.href = "<%=path%>/login/login.jsp";
				     	});
				    }
				}
			});
        });
        
        //设置管理员密码
        function setAdminPwd(){
			var url = "<%=path%>/manager/admin/user/adminUser!setInitAdminPwd.action?source="+'<%=source%>';
			$("#PwdSetForm").ajaxSubmit({
				async : true,
				type : "POST", 
				url : url,
				dataType : "json",
				success : function(msg){
					var errorStr = msg.errorStr;
					if("success" == errorStr){
						$.ligerDialog.confirm('<view:LanguageTag key="admin_update_pwd_succ"/>', '<view:LanguageTag key="common_syntax_confirm"/>', function(sel) {
				            if(sel) {
				            	window.parent.location.href = "<%=path%>/login/login.jsp";
				            }
						});
					}else {
						FT.toAlert(errorStr, msg.object, null);
					}
				}
			});	
		}
    </script>
  </head>  
<body>
  <table width="808" border="0" align="center" cellpadding="0" cellspacing="0">
   <tr>
    <td>
      <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
        <tr>
          <td width="1%"><img src="<%=path%>/install/images/init_r2_c2.png" width="10" height="50"/></td>
          <td width="97%" align="left" valign="top" background="<%=path%>/install/images/init_r2_c11.png"><table width="100%" height="50" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="21%"><img src="<%=path%>/images/layout/<%=titleImg%>" width="260" height="30"></td>
                <td width="79%">&nbsp;</td>
              </tr>
       	</table></td>
          <td width="2%" align="right"><img src="<%=path%>/install/images/init_r2_c15.png" width="15" height="50"/></td>
        </tr>
        <tr>
          <td><img src="<%=path%>/install/images/init_r3_c2.png" width="10" height="30"/></td>
          <td align="center" background="<%=path%>/install/images/init_r3_c5.png"><span class="textCss"><c:if test="${param.source=='0'}"><view:LanguageTag key="admin_sys_activation"/></c:if><c:if test="${param.source=='1'}"><view:LanguageTag key="admin_modify_pwd"/></c:if></span></td>
          <td align="right"><img src="<%=path%>/install/images/init_r3_c15.png" width="15" height="30"/></td>
        </tr>
        <tr>
          <td height="190" colspan="3" valign="top" background="<%=path%>/install/images/init_r5_c2.png"><table width="100%" border="0" align="left" cellpadding="0" cellspacing="0">
              <tr>
                <td colspan="3" height="8"></td>
              </tr>
              <tr>
                <td width="1%">&nbsp;</td>
                <td width="97%">
				<form id="PwdSetForm" name="PwdSetForm" method="post" action="">
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="28%" align="right"><view:LanguageTag key="admin_info_account"/><view:LanguageTag key="colon"/></td>
                      <td width="30%">${adminUser.reAdminId}</td>
                      <td width="42%">&nbsp;</td>
                    </tr>
                    <tr>
                      <td align="right"><view:LanguageTag key="common_info_new_pwd"/><view:LanguageTag key="colon"/></td>
                      <td>
                      	  <input type="hidden" name="adminUser.reAdminId" id="adminId" value="${adminUser.reAdminId}" />
                      	  <input onpaste="return false" type="password" id="password" name="adminUser.pwd" value="" class="formCss100"/>
					  </td>
                      <td class="divTipCss"><div id="passwordTip"></div></td>
                    </tr>
                    <tr>
                      <td align="right"><view:LanguageTag key="common_info_confirm_new_pwd"/><view:LanguageTag key="colon"/></td>
                      <td>
                      		<input type="password" id="passwordConf" name="passwordConf" value="" class="formCss100"/>
					  </td>
                      <td class="divTipCss"><div id="passwordConfTip"></div></td>
                    </tr>
                    <tr>
				      <td>&nbsp;</td>
				      <td colspan="2">
				      	<input type="hidden" id="passwordOld" name="adminUser.rePwd" value="${adminUser.rePwd}" />
						<a href="#" id="setPwdBtn" class="button"><span><c:if test="${param.source=='0'}"><view:LanguageTag key="common_syntax_activate"/></c:if><c:if test="${param.source=='1'}"><view:LanguageTag key="common_syntax_reset"/></c:if></span></a></td>
				    </tr>
                  </table>
				</form>
                </td>
                <td width="2%">&nbsp;</td>
              </tr>
              <tr>
                <td colspan="3" height="8"></td>
              </tr>
          </table>
          </td>
        </tr>
        <tr>
          <td><img src="<%=path%>/install/images/init_r10_c2.png" width="10" height="38"/></td>
          <td align="center" background="<%=path%>/install/images/init_r10_c4.png"><view:LanguageTag key='system_noun_copyright'/></td>
          <td align="right"><img src="<%=path%>/install/images/init_r10_c15.png" width="15" height="38"/></td>
        </tr>
      </table></td>
    </tr>
  </table>
  </body>
</html>