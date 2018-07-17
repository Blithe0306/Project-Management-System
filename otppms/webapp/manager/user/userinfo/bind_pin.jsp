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
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css">
    <link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css">
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css">
 
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
	 	var winObj;
	
        $(function() {
         checkForm();   
        })

		 
   function checkForm(){
		 //执行"下一步"校验静态密码选项 
           $.formValidator.initConfig({
	        submitButtonID:"editObj",
	        debug:false,
	        onSuccess:function(){
	             var checkedVal = $("input[name='validate']:checked").val();
	              
	             $("#AddForm").ajaxSubmit({
					   async:false,  
					   type:"POST", 
					   dataType:"json",
					   url : "<%=path%>/manager/user/userinfo/userInfo!staticUserPass.action?usreInfo.userId="+$('#userId').val()+"&checkedVal="+checkedVal,
					   success:function(msg){
							if(msg.errorStr == 'success'){ 
							     parent.staticClose(msg.object);
							 }else{
							    FT.toAlert(msg.errorStr,msg.object,null);
							} 
						}
			 }); 
	        },
           onError:function(){
               
              return false;
              }
            });    
             $("#localAuth").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"});
             $("#pwd").formValidator({onFocus:'<view:LanguageTag key="user_vd_pwd_tip"/>',onCorrect:"OK"}).functionValidator({
               fun:function(val){
                  if(val=='' || val==null){return '<view:LanguageTag key="user_vd_pwd_err"/>';}
                  if(val.length<4||val.length>32){return '<view:LanguageTag key="user_vd_pwd_tip"/>';}
                  return true;
               }
             });
  			$("#confrmpwd").formValidator({onFocus:'<view:LanguageTag key="user_vd_confpwd_tip"/>',onCorrect:"OK"}).functionValidator({
              fun:function(val){
                 var pwd = $('#pwd').val();
                 if(val=='' || val==null){return '<view:LanguageTag key="user_vd_confpwd_err"/>';}
                 if(val.length<4||val.length>32){return '<view:LanguageTag key="user_vd_confpwd_tip"/>';}
                 if(val!=pwd){return '<view:LanguageTag key="common_vd_confirm_pwd_err"/>';}
                 return true;
              }
             });
		 $("#localAuth").focus();
		 
	 }
	 
	 function okClick(item,win,index) {
		winObj = win; 
	    $('#editObj').triggerHandler("click");   
	    //winObj = pwd;
	     
   }
	  
	//-->
	</script>
  </head>
  <body style="overflow:hidden;">
  	<input id="contextPath" type="hidden" value="<%=path%>" />
  	<input id="currentPage" type="hidden" value="${param.curPage}" />
 	<form  id="AddForm" name="AddForm" method="post" action="">
    <table width="98%" border="0" align="center"  cellpadding="0" cellspacing="0" class="ulOnTable">
    	<tr>
      		<td valign="top">
        		<ul id="menu">
          			<li class="tabFocus"> 
          				<strong>
            				<view:LanguageTag key="user_set_static_pwd"/>
            			</strong> 
            		</li>
        		</ul>
        		<ul id="content">
          			<li class="conFocus">
            			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
              				<input id="domainId" type="hidden" name="userInfo.domainId" value="${userInfo.domainId}"/>
	        				<input id="localAuth" type="hidden" name="userInfo.localAuth" value="${userInfo.localAuth}"/>
	        				<input id="userId" type="hidden" name="userInfo.userId" value="${userInfo.userId}"/>
              				<tr>
								<td align="right" width="30%"><view:LanguageTag key="user_static_pwd"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			     				<td width="30%"><input onpaste="return false" type="password" id="pwd" name="userInfo.pwd" class="formCss100" value="" /></td>
			     				<td width="40%" class="divTipCss"><div id="pwdTip" ></div></td>               
              				</tr>
              				<tr>
	        					<td align="right"><view:LanguageTag key="user_conf_static_pwd"/><span class="text_Hong_Se" >*</span><view:LanguageTag key="colon"/></td>
	        					<td><input onpaste="return false" type="password" id="confrmpwd" name="pwd" class="formCss100" value="" /></td>
	        					<td class="divTipCss"><div id="confrmpwdTip"></div></td>
	      	  				</tr>
              				<tr>
			        			<td>&nbsp;</td>
			        			<td><a href="#" id="editObj"><span></span></a></td>
			        			<td>&nbsp;</td>
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