<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.ft.otp.common.Constant"%>
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
    <link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/easyui/easyui.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>  
    <script language="javascript" src="<%=path%>/manager/admin/role/js/add.js"></script>
    
	<script language="javascript" type="text/javascript">
		
		// Start,多语言提取
		var only_one_lang = '<view:LanguageTag key="role_sel_only_one_perm"/>';
		var continue_add_lang = '<view:LanguageTag key="common_save_success_continue_add"/>';
		var syntax_confirm_lang = '<view:LanguageTag key="common_syntax_confirm"/>';
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
		
		var roleid = $.trim($("#roleId").val());
		$.formValidator.initConfig({submitButtonID:"addBtn", 
			onSuccess:function(){
				if(roleid=='0' || roleid==''){
			    	addObj('add','adminPerms');
			    }else{
			    	addObj('edit','adminPerms');
				}
			}, 
			onError:function(){
				return false;
			}});
			
			var roleName = $.trim($("#roleName").val());
			$("#roleName").formValidator({onFocus:'<view:LanguageTag key="role_vd_name_tip"/>'}).inputValidator({min:4,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="role_vd_name_err"/>'},onError:'<view:LanguageTag key="role_vd_name_err_1"/>'});
 
			$("#descp").formValidator({empty:true,onFocus:'<view:LanguageTag key="common_vd_descp_show"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:255,onError:'<view:LanguageTag key="common_vd_descp_error"/>'});
			$("#roleName").focus();
	});

	function checkRoleName(){
		var roleid = $.trim($("#roleId").val());
		var name = $.trim($("#name").val());
		var roleName = $.trim($("#roleName").val());
		if(roleName != name) {
			validateRoleName();
		}else{
			 if(roleid == '' || roleid == '0'){
				 validateRoleName();
			 }else{
				 $("#roleName").formValidator({onFocus:'<view:LanguageTag key="role_vd_name_tip"/>'}).inputValidator({min:4,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="role_vd_name_err"/>'},onError:'<view:LanguageTag key="role_vd_name_err_1"/>'});
			 }
		}
	}
 
	
	//验证角色名称
	function validateRoleName() {
		var oldRoleName = $.trim($("#oldRoleName").val());
		$("#roleName").ajaxValidator({
			dataType:"html",
			async:true,
			url:"<%=path%>/manager/admin/role/adminRole!findRoleisExist.action?roleInfo.oldRoleName="+encodeURI(oldRoleName),
			success:function(data){
			    if(null != data && data =='true') {
			    	return true;
			    }else{
					return false;
				}
			},
			buttons:$("#addBtn"),
			error:function(jqXHR, textStatus, errorThrown){
				$.messager.alert('<view:LanguageTag key="common_vd_warning"/>','<view:LanguageTag key="common_vd_server_is_busy"/>','warning');
			},
			onError:'<view:LanguageTag key="common_vd_already_exists"/>',
			onWait:'<view:LanguageTag key="common_vd_checking_exists"/>'
		});
	}
 
	//-->
	</script>
  </head>
 <body>
 <input name="contextPath" id="contextPath" type="hidden" value="<%=path %>" />
 <input name="currentPage" id="currentPage" type="hidden" value="${param.cPage}" />
 <input type="hidden" name="oper" value="${empty roleInfo.roleId ? '' : 'edit'}" id="oper"/>
 <form id="roleForm" method="post" action="">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td width="98%">  
		  <span class="topTableBgText"><c:if test='${empty roleInfo.roleId}'><view:LanguageTag key="role_info_add"/></c:if>
          <c:if test='${not empty roleInfo.roleId}'><view:LanguageTag key="role_info_edit"/></c:if></span>
        </td>
        <td width="2%" align="right">
        	<a href="javascript:addAdmPermCode('010301','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" hspace="6" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a></td>
      </tr>
    </table> 
	  <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td valign="top">
	    <!-- 设置基本信息 -->
	        <table width="98%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
             <tr>
               <td width="25%" align="right"><view:LanguageTag key="admin_role_name"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
               <td width="45%">
              		<input type="text" id="roleName" name="roleInfo.roleName" class="formCss100" value="${roleInfo.roleName}" onchange="checkRoleName();"/>
              		<input type="hidden" id="name" value="${roleInfo.roleName}" />
              		<input type="hidden" name="roleInfo.createtime" value="${roleInfo.createtime}" />
               </td>
               <td width="30%" class="divTipCss">
               		<c:if test="${empty adminRole.roleId}">
               			<div id="roleNameTip"></div>
               		</c:if>
               </td>
             </tr>
             
             <tr>
                <td align="right" width="25%"><view:LanguageTag key="role_sel_perm"/><view:LanguageTag key="colon"/></td>
              	<td height="355" width="45%">
				    <iframe  id="bottomFrame"
					   src="<%=path%>/manager/admin/role/perm_view.jsp?oper=${empty roleInfo.roleId ? '' : 'edit'}&roleid=${roleInfo.roleId}"
					    scrolling="no" name="bottomFrame" width="100%" height="100%"
					    frameborder="0" style="color: red">
				    </iframe>
				    <div style="display:none"> 
						<select id="adminPerms" name="roleInfo.adminPerms" size="5" multiple class="select100" style="height:100px;">
						     <c:forEach items="${roleInfo.adminPerms}" var="roleperm">
					             <option value="${roleperm.permcode}">${roleperm.permcode}</option>
					         </c:forEach> 
					    </select> 
				 	</div>
              	</td>
                <td width="30%">&nbsp;</td>
             </tr>
             
             
             <tr>
               <td align="right"><view:LanguageTag key="common_syntax_desc"/><view:LanguageTag key="colon"/></td>
               <td><div class="autoLine"><textarea id="descp" name="roleInfo.descp" class="textarea100">${roleInfo.descp}</textarea></div></td>
               <td class="divTipCss"><div id="descpTip"></div></td>
             </tr>
		      
	         <tr>
		        <td>
		        	<input type="hidden"  name="roleInfo.createuser" value="<%=(String)session.getAttribute(Constant.CUR_LOGINUSER)%>"/>
		            <input type="hidden" id="roleId" name="roleInfo.roleId" class="formCss100" value="${roleInfo.roleId}"/> 
		            <input type="hidden" id="oldRoleName" name="roleInfo.oldRoleName" value="${otpRole.roleName}"/>
		            <input type="hidden" id="hidroleName" value="${roleInfo.roleName}"/>
		        </td>
		        <td align="right">
		        	<a href="#" id="addBtn" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
				    <c:if test='${not empty roleInfo.roleId}'><a href="<%=path%>/manager/admin/role/list.jsp"  class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a> </c:if> 
		        </td>
		    </tr>
       </table>
		</td>
      </tr>
    </table>	 
  </form>
  </body>
</html>