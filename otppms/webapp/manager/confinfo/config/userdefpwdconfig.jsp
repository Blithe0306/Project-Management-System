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
	  
	  //用户配置       
      $("#defaultuserpwd").formValidator({onFocus:'<view:LanguageTag key="userconf_vd_def_user_pwd_show"/>',onCorrect:"OK"}).inputValidator({min:4,max:32,onError:'<view:LanguageTag key="userconf_vd_def_user_pwd_err"/>'});
 
			
	}
	
	// 保存数据
	function savaData(){
    	var url = "<%=path%>/manager/confinfo/config/userConfAction!modify.action";
		$("#upwdForm").ajaxSubmit({
		   async:false,
		   dataType : "json",  
		   type:"POST", 
		   url : url,
		   data:{"oper" : "upwdconf"},
		   success:function(msg){
				if(msg.errorStr == 'success'){ 
				     $.ligerDialog.success(msg.object,'<view:LanguageTag key="common_syntax_tip"/>',function(){
				     	 window.location.reload();
				     });
				}else{
				     FT.toAlert(msg.errorStr,msg.object, null);
				}
		   }
	   });
	}
	//-->
	</script>
  </head>
  
  <body>
  <input id="contextPath" type="hidden" value="<%=path%>" />
  <form id="upwdForm" method="post" action="" name="upwdForm">
    <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="topTableBg">
      <tr>
        <td width="98%"><span class="topTableBgText"><view:LanguageTag key="common_menu_config_user_dfpwd"/></span></td>
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
		        <td width="30%" align="right"><view:LanguageTag key="userconf_def_user_pwd"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		        <td width="30%">
		        	<input type="text" id="defaultuserpwd" name="userConfInfo.defaultuserpwd"  value="${userConfInfo.defaultuserpwd}" class="formCss100" />
		        </td>
		        <td width="40%" class="divTipCss"><div id="defaultuserpwdTip"></div></td>
		      </tr>
		      
		            <tr>
        <td align="right"><view:LanguageTag key="userconf_def_localauth"/><view:LanguageTag key="colon"/></td>
        <td>
        <select id="localAuth" name="userConfInfo.defaultlocalauth" class="select100">
	        	<c:if test="${userConfInfo.defaultlocalauth==0}">
					<option value="0" selected><view:LanguageTag key="local_auth_only_vd_tkn"/></option>
					<option value="1"><view:LanguageTag key="local_auth_vd_pwd_tkn"/></option>
					<option value="2"><view:LanguageTag key="local_auth_only_vd_pwd"/></option>
				</c:if>
				<c:if test="${userConfInfo.defaultlocalauth==1}">
					<option value="0"><view:LanguageTag key="local_auth_only_vd_tkn"/></option>
					<option value="1" selected><view:LanguageTag key="local_auth_vd_pwd_tkn"/></option>
					<option value="2"><view:LanguageTag key="local_auth_only_vd_pwd"/></option>
				</c:if>
				<c:if test="${userConfInfo.defaultlocalauth==2}">
					<option value="0"><view:LanguageTag key="local_auth_only_vd_tkn"/></option>
					<option value="1"><view:LanguageTag key="local_auth_vd_pwd_tkn"/></option>
					<option value="2" selected><view:LanguageTag key="local_auth_only_vd_pwd"/></option>
				</c:if>
        </select> 
         <input type="hidden" id="localAuthVal" name="localAuthVal" value="0" />
        </td>
        <td class="divTipCss"><div id="localAuthTip" style="width:100%"></div></td>
      </tr>
      
            <tr>
        <td align="right"><view:LanguageTag key="userconf_def_backendauth"/><view:LanguageTag key="colon"/></td>
        <td>
        <select id="backendAuth" name="userConfInfo.defaultbackendauth" class="select100" >
			<c:if test="${userConfInfo.defaultbackendauth==0}">
						<option value="0" id="a" selected><view:LanguageTag key="backend_auth_default"/></option>
						<option value="1" id="b"><view:LanguageTag key="backend_auth_need"/></option>
						<option value="2" id="c"><view:LanguageTag key="backend_auth_no_need"/></option>
					</c:if>
					<c:if test="${userConfInfo.defaultbackendauth==1}">
						<option value="0" id="a"><view:LanguageTag key="backend_auth_default"/></option>
						<option value="1" id="b" selected><view:LanguageTag key="backend_auth_need"/></option>
						<option value="2" id="c"><view:LanguageTag key="backend_auth_no_need"/></option>
					</c:if>
					<c:if test="${userConfInfo.defaultbackendauth==2}">
						<option value="0" id="a"><view:LanguageTag key="backend_auth_default"/></option>
						<option value="1" id="b"><view:LanguageTag key="backend_auth_need"/></option>
						<option value="2" id="c" selected><view:LanguageTag key="backend_auth_no_need"/></option>
					</c:if>
        </select>  
        </td>
        <td class="divTipCss"><div id="backendAuthTip" style="width:100%"></div></td>
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