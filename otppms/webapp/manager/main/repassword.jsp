<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
%>
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf-8" />
<title></title>
<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
<link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
<link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" ></script>
<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
<script language="JavaScript" type="text/javascript">
		var winClose = null;
		$(document).ready(function(){
			checkForm();
			window.resizeBy(0,0);
		});
		
		function modifyPwd(){
			var url = "<%=path%>/manager/admin/user/adminUser!editPassword.action";
			$("#AddForm").ajaxSubmit({
				async : false,
				type : "POST", 
				url : url,
				dataType : "json",
				success : function(msg){
					 parent.winClose(msg.object);
//					FT.toAlert(msg.errorStr, msg.object, null);
//			    	toClear();				   
				}
			});	
		}
		
		function toClear(){ 
            $('#passwordOld').val("");  
            $('#password').val("");  
            $('#passwordConf').val("");  
        }		
		
		function checkForm(){
		var  adminId =$('#adminId').val();
			$.formValidator.initConfig({submitButtonID:"modifyBtn",
			onSuccess:function(){
				modifyPwd();
			},
			onError:function(){
				return false;
			}});
			
	        $("#passwordOld").formValidator({onFocus:'<view:LanguageTag key="admin_vd_old_pwd_tip"/>'}).inputValidator({min:4,max:32,empty:{leftEmpty:true,rightEmpty:true,emptyError:'<view:LanguageTag key="admin_vd_old_pwd_err"/>'},onError:'<view:LanguageTag key="admin_vd_old_pwd_tip"/>'})
			//判断原密码是否正确
			.ajaxValidator({
				dataType:"html",
				async:true,
				data:"adminId="+adminId,
				url:"<%=path%>/manager/admin/adminUser!pwdIsCorrect.action",
				success:function(data){
		            if(data=='true'){ 
		            	return true;
		            }else{
		                $("#passwordOld").focus();
		                return false;
		            }
					
				},
				buttons:$("#modifyBtn"),
				error:function(jqXHR, textStatus, errorThrown){
					$.messager.alert('<view:LanguageTag key="common_vd_warning"/>','<view:LanguageTag key="common_vd_server_is_busy"/>','warning');
				},
				onError:'<view:LanguageTag key="admin_vd_modify_pwd_err"/>',
				onWait:'<view:LanguageTag key="admin_vd_modify_pwd_checking"/>'
			});
			
		   $("#password").formValidator({onFocus:'<view:LanguageTag key="admin_vd_new_pwd_tip"/>',onCorrect:"OK"}).inputValidator({min:4,max:32, empty:{leftEmpty:true,rightEmpty:true,emptyError:'<view:LanguageTag key="admin_vd_new_pwd_err"/>'},onError:'<view:LanguageTag key="admin_vd_new_pwd_tip"/>'}).compareValidator({desID:"passwordOld",operateor:"!=",onError:'<view:LanguageTag key="admin_pwd_new_old_dif"/>'});
		   $("#passwordConf").formValidator({onFocus:'<view:LanguageTag key="admin_vd_new_confpwd"/>',onCorrect:"OK"}).inputValidator({min:4,max:32,empty:{leftEmpty:true,rightEmpty:true,emptyError:'<view:LanguageTag key="admin_vd_conf_newpwd_err"/>'},onError:'<view:LanguageTag key="admin_vd_conf_newpwd_tip"/>'}).compareValidator({desID:"password",operateor:"=",onError:'<view:LanguageTag key="admin_vd_new_confpwd_error"/>'}).compareValidator({desID:"passwordOld",operateor:"!=",onError:'<view:LanguageTag key="admin_pwd_new_old_dif"/>'}); 
		   $("#passwordOld").focus();}
		
		function okClick(btn,win,index){
			winClose = win;
		    $('#modifyBtn').triggerHandler("click");    	
		}
		
	</script>
</head>
<body style="overflow:hidden;">
<form id="AddForm" name="AddForm" method="post" action="">
  <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnTable">
    <tr>
      <td valign="top">
        <ul id="menu">
          <li class="tabFocus"> <strong>
            <view:LanguageTag key="admin_modify_pwd"/>
            </strong> </li>
        </ul>
        <ul id="content">
          <li class="conFocus">
            <table width="100%" align="center" cellspacing="0" cellpadding="5px" class="tableTdText">
              <tr>
                <td align="right" width="30%">
                  <view:LanguageTag key="admin_info_account"/><view:LanguageTag key="colon"/>
                </td>
                <td width="30%">
			     <c:if test="${empty param.userName}">
				          ${curLoginUser}
				         <input type="hidden" name="adminUser.adminid" id="adminId" value="${curLoginUser}"/>
				   </c:if> 
				   <c:if test="${not empty param.userName}">
				         ${param.userName}
				         <input type="hidden" name="adminUser.adminid" id="adminId" value="<c:out value="${param.userName}"/>"/>
				   </c:if> 

                </td>
                <td width="40%">
                </td>
              </tr>
              
              <c:if test="${param.flag != '1'}">
              <tr>
              	
                <td align="right">
                  <view:LanguageTag key="common_info_old_pwd"/><view:LanguageTag key="colon"/>
                </td>
                <td>
                  <input onpaste="return false" type="password" id="passwordOld" name="adminUser.pwdOld" value="${adminUser.pwdOld}" class="formCss100"/>
                </td>
                <td class="divTipCss">
                  <div id="passwordOldTip"></div>
                </td>
              </tr>
              </c:if>
              <tr>
                <td align="right">
                   <view:LanguageTag key="common_info_new_pwd"/><view:LanguageTag key="colon"/>
                </td>
                <td>
                  <input onpaste="return false" type="password" id="password" name="adminUser.pwd" value="${adminUser.pwd}" class="formCss100"/>
                </td>
                <td class="divTipCss">
                  <div id="passwordTip"></div>
                </td>
              </tr>
              <tr>
                <td align="right">
                  <view:LanguageTag key="common_info_confirm_new_pwd"/><view:LanguageTag key="colon"/>
                </td>
                <td>
                  <input onpaste="return false" type="password" id="passwordConf" name="passwordConf" value="${adminUser.pwd}" class="formCss100"/>
                </td>
                <td class="divTipCss">
                  <div id="passwordConfTip"></div>
                </td>
              </tr>
              <tr>
                <td><a href="#" name="modifyBtn" id="modifyBtn"></a></td>
              </tr>
            </table>
          </li>
        </ul>
      </td>
    </tr>
  </table>
</form>
</body>
</html>
