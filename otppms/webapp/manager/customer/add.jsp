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
	<script language="javascript" src="<%=path%>/manager/customer/js/add.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>  
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
			    if('${custInfo.custname}'== null || '${custInfo.custname}'==''){
			      addObj(0,'adminRoles');
			    }else{
			      addObj(1,'adminRoles');
			    }
			}, 
			onError:function(){
				return false;
			}});
			//
			$("#custname").formValidator({onFocus:'请输入客户名称,长度为4-64个字符!',onCorrect:"OK"})
			.inputValidator({min:4,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'客户名称两边不能有空符合!'}, onError:'客户名称长度为4-64个字符!'})
			.ajaxValidator({
					dataType:"html",
					async:true,
					data:{'custInfo.custname': $('#custname').val() },
					url:"<%=path%>/manager/customer/custInfo!queryConditionName.action",
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
			
			$("#custid").formValidator({onFocus:'请输入客户编号,长度为4-64个字符!',onCorrect:"OK"})
			.inputValidator({min:4,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'客户编号两边不能有空符合!'},onError:'客户编号长度为4-64个字符!'});
				
      		$("#contacts").formValidator({onFocus:'请输入客户联系方式！',onCorrect:"OK"}).inputValidator({min:0,max:256, onError:'长度为0-256个字符！'});
      		
      }
	//-->
	</script>
  </head>  
  <body style="overflow:auto; overflow-x:hidden">
  <input type="hidden" value="<%=path %>" id="contextPath" />
  <input id="cPage" type="hidden" value="${param.currentPage}" />
  <form name="AddForm" id="AddForm" method="post" action="">
  	<input type="hidden" id="infoid" name="custInfo.id" value="${custInfo.id}" class="formCss100"/>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td width="98%">  
		  <span class="topTableBgText">
			  <c:if test='${empty custInfo.custid}'>添加客户<div id="oper" style="display:none">add</div></c:if>
	          <c:if test='${not empty custInfo.custid}'>编辑客户</c:if>
          </span>
        </td>
        <td width="2%" align="right" valign="middle">
        	<c:if test='${empty custInfo.custid}'><a href="javascript:addAdmPermCode('020002','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" hspace="6" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a></c:if>
        </td>
      </tr>
    </table>
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td valign="top">
           <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		      <tr>
		        <td width="25%" align="right">客户名称<span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		        <td width="30%">
		         <input type="hidden" id="custnameHid" value="${custInfo.custname}" />
		         <input type="text" id="custname" name="custInfo.custname" value="${custInfo.custname}" class="formCss100"/>
		        </td>
		        <td width="45%" class="divTipCss"><div id="custnameTip" style="width:100%;"></div></td>
		      </tr>
		      <tr>
		        <td align="right">客户编号<span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>       
		        <td>
		        <input type="hidden" id='custidHid' value='${custInfo.custid}'/>
		        <input type="text" id="custid" name="custInfo.custid" value="${custInfo.custid}" class="formCss100"/></td>
		        <td class="divTipCss"><div id="custidTip" style="width:100%;"></div></td>
		      </tr>
		      
		      <tr>
               <td align="right">所属部门<span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
               <td>
               		<select id="dept" name="custInfo.dept" class="select100">
               			<c:forEach var="dept" items="${deptList}">
               				<option value="${dept.deptid }" <c:if test="${custInfo.dept == dept.deptid}">selected</c:if>>${dept.deptName }</option>
               				
               			</c:forEach>
			        </select>
			   </td>
               <td class="divTipCss"><div id ="deptTip"></div></td>
              </tr>
		      <tr>
		        <td align="right">客户联系方式<view:LanguageTag key="colon"/></td>
		        <td><textarea id="contacts" name="custInfo.contacts" class="textarea100">${custInfo.contacts}</textarea></td>
		        <td class="divTipCss"><div id="contactsTip" style="width:100%;"></div></td>
		      </tr>
		      <tr>
		        <td align="right">&nbsp;</td>
		        <td>
		        	<a href="#" id="addBtn" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
		        	<c:if test='${not empty custInfo.custid}'><a href="#" onClick=goBack() id="goback" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a> </c:if>
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
