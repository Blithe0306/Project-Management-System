<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ft.otp.common.Constant"%>
<%@ page import="com.ft.otp.util.conf.ConfDataFormat"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
    String isActivate = ConfDataFormat.getSysConfEmailEnabled()?"true":"false";// 是否需要邮件激活
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
	<script language="javascript" src="<%=path%>/manager/admin/user/js/add.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>  
   	<script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script>
    <script language="javascript" type="text/javascript">	
	// Start,多语言提取
	var select_role_lang = '<view:LanguageTag key="admin_select_role"/>';
	var need_del_date_lang = '<view:LanguageTag key="common_syntax_check_need_del_date"/>';
	var continue_add_lang = '<view:LanguageTag key="common_save_success_continue_add"/>';
	var whether_edit_lang = '<view:LanguageTag key="admin_whether_edit"/>';
	var syntax_confirm_lang = '<view:LanguageTag key="common_syntax_confirm"/>';
	var succ_tip_lang = '<view:LanguageTag key="common_save_succ_tip"/>';
	var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
	// End,多语言提取
	
	$(document).ready(function(){
			$("#menu li").each(function(index) { //带参数遍历各个选项卡
			$(this).click(function() { //注册每个选卡的单击事件
				$("#menu li.tabFocus").removeClass("tabFocus"); //移除已选中的样式
				$(this).addClass("tabFocus"); //增加当前选中项的样式
                    //显示选项卡对应的内容并隐藏未被选中的内容
				$("#content li:eq(" + index + ")").show()
                    .siblings().hide();
                });
            });
			checkAdminForm();

			// 针对超级管理员，编辑页面隐藏角色和机构（其它管理员登录修改不了本身）
			if('${adminUser.adminid}' == '${curLoginUser}'){
				$("#admRole").hide();
				$("#admOrgun").hide();
			}
	 }) 
	function checkAdminForm(){
	      $.formValidator.initConfig({submitButtonID:"addBtn",debug:true,
			onSuccess:function(){
			    if('${adminUser.adminid}'== null || '${adminUser.adminid}'==''){
			      addObj(0,'adminRoles');
			    }else{
			      addObj(1,'adminRoles');
			    }
			}, 
			onError:function(){
				return false;
			}});
	    if('${adminUser.adminid}'== null || '${adminUser.adminid}'==''){
			$("#adminid").formValidator({
			onFocus:'<view:LanguageTag key="admin_vd_account_tip"/>'}).inputValidator({min:1,max:64,onError:'<view:LanguageTag key="admin_vd_account_err"/>'}).regexValidator({regExp:"username",dataType:"enum",onError:'<view:LanguageTag key="admin_vd_account_err_1"/>'})
			//判断管理员帐号是否已经存在
			.ajaxValidator({
				dataType:"html",
				async:true,
				url:"<%=path%>/manager/admin/user/adminUser!findAdminisExist.action",
				success:function(data){
		            if(data =='') return true;
					return false;
				},
				buttons:$("#addBtn"),
				error:function(jqXHR, textStatus, errorThrown){
					$.messager.alert('<view:LanguageTag key="common_vd_warning"/>','<view:LanguageTag key="common_vd_server_is_busy"/>','warning');
				},
				onError:'<view:LanguageTag key="common_vd_already_exists"/>',
				onWait:'<view:LanguageTag key="common_vd_checking_exists"/>'
			});
				$("#adminid").focus();
			}
			
			$("#adminRoles").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>', onCorrect:"OK"}).functionValidator({
       		 	fun:function(val){
       		 	     var val = $("#adminRoles").text();
		             if($.trim(val)=='' || $.trim(val)==null){
	                 	return '<view:LanguageTag key="admin_vd_role"/>';
	                 }
	                 return true;
       		 	}
       		 });  

     		var realname = $("#realname").val();
			$("#realname").formValidator({onFocus:'<view:LanguageTag key="admin_vd_name_tip"/>',onCorrect:"OK"}).inputValidator({min:0,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="admin_vd_name_err"/>'}, onError:'<view:LanguageTag key="admin_vd_name_err_1"/>'}).functionValidator({
  				fun:function(realname){
  			    if(g_invalid_char_js(realname)){
  			       return "<view:LanguageTag key="admin_vd_name_err_2"/>";
  			    }
  			    return true;
  			}}); 			
			$("#password").formValidator({onFocus:'<view:LanguageTag key="common_vd_pwd_show"/>',onCorrect:"OK"}).inputValidator({min:4,max:32,onError:'<view:LanguageTag key="common_vd_pwd_show"/>'});
			$("#password2").formValidator({onFocus:'<view:LanguageTag key="common_vd_confirm_pwd_show"/>',onCorrect:"OK"}).inputValidator({min:4,max:32,onError:'<view:LanguageTag key="common_vd_confirm_pwd_show"/>'}).compareValidator({desID:"password",operateor:"=",onError:'<view:LanguageTag key="common_vd_confirm_pwd_err"/>'});
			$("#localauth").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
			
			$("#cellphone").formValidator({empty:true,onFocus:'<view:LanguageTag key="admin_vd_phone_tip"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:64,onError:'<view:LanguageTag key="admin_vd_tel_phone_len"/>'}).regexValidator({regExp:"cellphone",dataType:"enum",onError:'<view:LanguageTag key="admin_vd_phone_err"/>'});
		    if(<%=isActivate%>){
				$("#emailSpan").show();
				$("#email").formValidator({onFocus:'<view:LanguageTag key="admin_vd_email_tip_2"/>',onCorrect:"OK"}).inputValidator({min:0,max:255, onError:'<view:LanguageTag key="admin_vd_email_err_2"/>'}).regexValidator({regExp:"email",dataType:"enum",onError:'<view:LanguageTag key="admin_vd_email_err_1"/>'});
			}else{
				$("#email").formValidator({empty:true,onFocus:'<view:LanguageTag key="admin_vd_email_tip_2"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({min:0,max:255, onError:'<view:LanguageTag key="admin_vd_email_err_2"/>'}).regexValidator({regExp:"email",dataType:"enum",onError:'<view:LanguageTag key="admin_vd_email_err_1"/>'});
				$("#emailSpan").hide();
			}
			$("#descp").formValidator({onFocus:'<view:LanguageTag key="common_vd_descp_show"/>',onCorrect:"OK"}).inputValidator({max:255,onError:'<view:LanguageTag key="common_vd_descp_error"/>'});
      }
      
      //切换登录方式
      function changeLocalAuth(localAuth){
      	if(localAuth==1){//不需要输入密码
      		$("#pwdTr").hide();
      		$("#repwdTr").hide();
      		$("#password").unFormValidator(true);
      		$("#password2").unFormValidator(true);
      	}else{
      		$("#pwdTr").show();
      		$("#repwdTr").show();
      		$("#password").unFormValidator(false);
      		$("#password2").unFormValidator(false);
      	}
      }
	//-->
	</script>
  </head>  
  <body style="overflow:auto; overflow-x:hidden">
  <input type="hidden" value="<%=path %>" id="contextPath" />
  <input id="cPage" type="hidden" value="${param.currentPage}" />
  <form name="AddForm" id="AddForm" method="post" action="">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td width="98%">  
		  <span class="topTableBgText">
			  <c:if test='${empty adminUser.adminid}'><view:LanguageTag key="admin_info_add"/><div id="oper" style="display:none">add</div></c:if>
	          <c:if test='${not empty adminUser.adminid}'><view:LanguageTag key="admin_info_edit"/></c:if>
          </span>
        </td>
        <td width="2%" align="right" valign="middle">
        	<c:if test='${empty adminUser.adminid}'><a href="javascript:addAdmPermCode('010101','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" hspace="6" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a></c:if>
        </td>
      </tr>
    </table> 
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td valign="top">
           <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		      <tr>
		        <td width="25%" align="right"><view:LanguageTag key="admin_info_account"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		        <td width="30%">
		        <input type="hidden"  name="adminUser.createuser" value="<%=(String)session.getAttribute(Constant.CUR_LOGINUSER)%>"/>
		        <c:if test='${not empty adminUser.adminid}'>${adminUser.adminid}<input type="hidden"  name="adminUser.adminid" value="${adminUser.adminid}" class="formCss100"/></c:if>
		        <c:if test='${empty adminUser.adminid}'>
		         <input type="text" id="adminid" name="adminUser.adminid" value="${adminUser.adminid}" class="formCss100"/>
		        </c:if>
		        </td>
		        <td width="45%" class="divTipCss"><div id="adminidTip" style="width:100%;"></div></td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key="common_info_realname"/><view:LanguageTag key="colon"/></td>       
		        <td><input type="text" id="realname" name="adminUser.realname" value="${adminUser.realname}" class="formCss100"/></td>
		        <td class="divTipCss"><div id="realnameTip" style="width:100%;"></div></td>
		      </tr>
		      
		      <tr>
               <td align="right"><view:LanguageTag key="admin_login_mode"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
               <td>
               		<select id="localauth" name="adminUser.localauth" class="select100" onchange="changeLocalAuth(this.value)">
			        	<option value="0" <c:if test="${adminUser.localauth==0}">selected</c:if>><view:LanguageTag key="login_mode_only_vd_pwd"/></option>
			        </select>
			   </td>
               <td class="divTipCss"><div id ="localauthTip"></div></td>
              </tr>
		      
		      <c:if test='${empty adminUser.adminid}'>
			      <tr id="pwdTr">
			        <td align="right"><view:LanguageTag key="admin_info_pwd"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td><input onpaste="return false" type="password" id="password" name="adminUser.pwd" class="formCss100" value="${adminUser.pwd}"/></td>
			        <td class="divTipCss"><div id="passwordTip" style="width:100%;"></div></td>
			      </tr>
			      <tr id="repwdTr">
			        <td align="right"><view:LanguageTag key="admin_info_confirm_pwd"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td><input onpaste="return false" type="password" id="password2" name="pwd" class="formCss100" value="${adminUser.pwd}"/></td>
			        <td class="divTipCss"><div id="password2Tip" style="width:100%;"></div></td>
			      </tr>
		       </c:if>
		         <c:if test='${not empty adminUser.adminid}'>     
					 <c:if test="${adminUser.adminid==adminUser.loginUser}">
					     <tr>
					      <td align="right"><view:LanguageTag key="admin_roles_that"/><view:LanguageTag key="colon"/></td>
					      <td>
					          <c:forEach items="${adminUser.hidAdminRoles}" var="adminRole">
								<option value="${adminRole.roleId}">${adminRole.roleName}</option>
							 </c:forEach>
						  </td>
					      <td>&nbsp;</td>
					      </tr>
				      </c:if>
		       		 </c:if>
		       		 
		       		 <c:if test="${(not empty adminUser.adminid &&(adminUser.adminid!=adminUser.loginUser)) || empty adminUser.adminid}">
				      <tr id ='admRole'>
				             <td align="right"><view:LanguageTag key="admin_info_sel_role"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
				               <td>
				               <select id="adminRoles" name="adminUser.adminRoles" size="5" multiple class="select100">
							          <c:forEach items="${adminUser.adminRoles}" var="adminRole">
							            <option value="${adminRole.roleId}">${adminRole.roleName}</option>
							          </c:forEach>				        
							        </select>
								<a href="#" onClick="selRoles();" class="button"><span><view:LanguageTag key="common_syntax_select"/></span></a>
				        		<a href="#" onClick="delRoles();" class="button"><span><view:LanguageTag key="common_syntax_delete"/></span></a>
				        		</td>
							   <td class="divTipCss">
							   <div id="adminRolesTip" style="width:100%;"></div></td>
						</tr>	 
						</c:if>
						
					<%--
					<tr id='admOrgun'>
		               <td align="right"><view:LanguageTag key="user_sel_org"/><view:LanguageTag key="colon"/></td>
		               <td>	
		               		<input id="orgunitIds" name="adminUser.orgunitIds" class="textarea100" type=hidden value="${adminUser.orgunitIds}"/>
		               		<textarea id="orgunitNames" name="adminUser.orgunitNames" class="textarea100" onClick="selOrgunits(0,'<%=path%>');" readonly>${adminUser.orgunitNames}</textarea>	               
		        	   </td>
					   <td></td>
					</tr>
					--%>
					<tr>
			        <td align="right"><view:LanguageTag key="common_info_email"/><span class="text_Hong_Se" id="emailSpan">*</span><view:LanguageTag key="colon"/></td>
			        <td><input type="text" id="email" name="adminUser.email" class="formCss100" value="${adminUser.email}"/></td>
			        <td class="divTipCss"><div id="emailTip" style="width:100%;"></div></td>
			      </tr>
			      <tr>
			        <td align="right"><view:LanguageTag key="common_info_mobile"/><view:LanguageTag key="colon"/></td>
			        <td><input type="text"  id="cellphone" name="adminUser.cellphone" class="formCss100" value="${adminUser.cellphone}"/> </td>
			        <td class="divTipCss"><div id="cellphoneTip" style="width:100%;"></div></td>
			      </tr>
			      <tr>
			        <td align="right"><view:LanguageTag key="admin_info_descp"/><view:LanguageTag key="colon"/></td>
			        <td><textarea id="descp" name="adminUser.descp" class="textarea100">${adminUser.descp}</textarea></td>
			        <td class="divTipCss"><div id="descpTip" style="width:100%;"></div></td>
			      </tr>
     			  <tr>
			        <input type="hidden" id="enabled" name="adminUser.enabled" value="${adminUser.enabled}" />
			      </tr>
			      <tr>
			        <td align="right">&nbsp;</td>
			        <td>
			        	<a href="#" id="addBtn" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
			        	<c:if test='${not empty adminUser.adminid}'><a href="#" onClick=goBack() id="goback" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a> </c:if>
			        </td>
			        <td>&nbsp;</td>
			      </tr>
			    </table>
			 </td>
			</tr>
   		  </table>
  		</form>
  	</body>
</html>