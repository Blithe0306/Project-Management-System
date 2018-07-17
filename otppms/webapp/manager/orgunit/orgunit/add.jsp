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
	<script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/add.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>  
   	<script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script>
    <script language="javascript" type="text/javascript">
    
    	// Start,多语言提取
		var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
		var whether_continue_lang = '<view:LanguageTag key="org_save_succ_whether_continue"/>';
		var sel_admin_lang = '<view:LanguageTag key="domain_info_sel_admin"/>';
		var need_del_date_lang = '<view:LanguageTag key="common_syntax_check_need_del_date"/>';
		// End,多语言提取
		
    $(document).ready(function(){
		$("#menu li").each(function(index) { //带参数遍历各个选项卡
			$(this).click(function() { //注册每个选卡的单击事件
				$("#menu li.tabFocus").removeClass("tabFocus"); //移除已选中的样式
				$(this).addClass("tabFocus"); //增加当前选中项的样式
				$("#content li:eq(" + index + ")").show().siblings().hide();//显示选项卡对应的内容并隐藏未被选中的内容
			});
		});
		checkAdminForm();
	});
	//验证表单信息 
	function checkAdminForm(){
		$.formValidator.initConfig({
			submitButtonID:"addBtn",
			debug:true,
			onSuccess:function(){
				if('${treeOrgunitInfo.id}'== 0 || '${treeOrgunitInfo.id}'=='0'){
					addObj(0,"admins"); //添加
				}else{
					addObj(1,"admins"); //修改
				}
			}, 
			onError:function(){
				return false;
			}
		});

		// 选择管理员
		if($('#l_userid_role').val() != 'ADMIN'){
			$("#admins").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>', onCorrect:"OK"}).functionValidator({
	   		 	fun:function(val){
	   		 	     var val = $("#admins").text();
		             if($.trim(val)=='' || $.trim(val)==null){
	                 	return '<view:LanguageTag key="admin_vd_admin"/>';
	                 }
	                 return true;
	   		 	}
	   		 });
		}
		var name = $("#name").val();
		if($("#flag").val()==1 && $("#id").val()!=''){ //域 并且是编辑时  不存在添加域
			$("#mark").formValidator({empty:true,onFocus:'<view:LanguageTag key="org_vd_mark_tip"/>',onCorrect:"OK",onEmpty:"OK"})
					  .inputValidator({min:0,max:32,onError:'<view:LanguageTag key="org_vd_mark_input_tip"/>'})
					  .regexValidator({regExp:"domainSnRegex",dataType:"enum",onError:'<view:LanguageTag key="org_vd_mark_err_1"/>'}); //注意这里的 username 是正则表达式 英文数字 下划线
			$("#mark").focus();  
			$("#name").formValidator({onFocus:'<view:LanguageTag key="org_vd_name_tip"/>',onCorrect:"OK"}).inputValidator({min:1,max:128,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="org_vd_name_err"/>'}, onError: '<view:LanguageTag key="org_vd_name_err_1"/>'}).functionValidator({
				fun:function(name){
			    if(g_invalid_char_js(name)){
			       return '<view:LanguageTag key="org_vd_domainname_tip"/>';
			    }
			    return true;
			}});
			$("#descp").formValidator({onFocus:'<view:LanguageTag key="common_vd_descp_show"/>',onCorrect:"OK"}).inputValidator({max:255,onError:'<view:LanguageTag key="common_vd_descp_error"/>'});
		}else{ //其他情况均针对组织机构验证
			$("#mark").formValidator({empty:true,onFocus:'<view:LanguageTag key="org_vd_info_code_tip"/>',onCorrect:"OK",onEmpty:"OK"})
					  .inputValidator({min:0,max:32,onError:'<view:LanguageTag key="org_vd_mark_input_tip"/>'})
					   .regexValidator({regExp:"notempty",dataType:"enum",onError:'<view:LanguageTag key="org_vd_remove_space_tip"/>'})
					   .regexValidator({regExp:"username",dataType:"enum",onError:'<view:LanguageTag key="org_vd_mark_err_2"/>'}); 
		    $("#name").formValidator({onFocus:'<view:LanguageTag key="org_vd_orgname_tip"/>',onCorrect:"OK"})
		    .inputValidator({min:1,max:128,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="org_vd_name_notempty"/>'}, onError: '<view:LanguageTag key="org_vd_orgname_tip"/>'}).functionValidator({
				fun:function(name){
			    if(g_invalid_char_js(name)){
			       return '<view:LanguageTag key="org_vd_orgname_tip_err"/>';
			    }
			    return true;
			}}); 
		    $("#descp").formValidator({onFocus:'<view:LanguageTag key="common_vd_descp_show"/>',onCorrect:"OK"}).inputValidator({max:255,onError:'<view:LanguageTag key="common_vd_descp_error"/>'});
		}
	}
      
    //编辑域时用到的验证
    function checkDomainInit(){
      	  var domainId = $("#id").val();
    	  var domainSn=$("#mark").val();
    	  var hiddendomainSn=$("#hiddenmark").val();
      	  if(domainSn!=hiddendomainSn){ //如果不是原来的值 
      	  	  validateDomainSn();
      	  }else{ //如果相同
    	  	  $("#mark").formValidator({empty:true,onFocus:'<view:LanguageTag key="org_vd_mark_tip"/>',onCorrect:"OK",onEmpty:"OK"})
			  .inputValidator({min:0,max:32,onError:'<view:LanguageTag key="org_vd_mark_input_tip"/>'})
			  .regexValidator({regExp:"domainSnRegex",dataType:"enum",onError:'<view:LanguageTag key="org_vd_mark_err_2"/>'}); //注意这里的 username 是正则表达式 英文数字 下划线
      	  }
		   
	}
	
	//判断域标识是否已经存在
	function validateDomainSn() {
		  $("#mark").ajaxValidator({
				dataType:"html",
				async:true,
				 url:"<%=path%>/manager/orgunit/orgunit/orgunitInfo!findIsExist.action",
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
	 }
      
     //编辑机构 或 添加机构时用到的验证
      function checkOrgInit(){
      	  var id = $("#id").val();
    	  var mark=$("#mark").val();
    	  var hiddenmark=$("#hiddenmark").val();
      	  if(mark!=hiddenmark){ //如果不是原来的值 
      	  	  validateOrgnumber();
      	  }else{ //如果相同
    	  	  $("#mark").formValidator({empty:true,onFocus:'<view:LanguageTag key="org_vd_mark_input_tip"/>',onCorrect:"OK",onEmpty:"OK"})
			  .inputValidator({min:0,max:32,onError:'<view:LanguageTag key="org_vd_mark_input_tip"/>'})
      	  }
		   
	 }
	 //判断机构帐号是否已经存在
	 function validateOrgnumber() {
		  $("#mark").ajaxValidator({
				dataType:"html",
				async:true,
				 url:"<%=path%>/manager/orgunit/orgunit/orgunitInfo!findOrgunitNumberIsExist.action?treeOrgunitInfo.domainId="+$("#domainId").val()+"&treeOrgunitInfo.parentId="+$("#parentid").val(),
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
	 } 
	 
     //判断机构名称是否已经存在
     function checkOrgName() {
     	  var oldorgname = $("#hidname").val();
     	  var orgname = $("#name").val();
     	  if(oldorgname!=orgname){
     	  	$("#name").ajaxValidator({
				dataType:"html",
				async:true,
				 url:"<%=path%>/manager/orgunit/orgunit/orgunitInfo!findOrgnameIsExist.action?treeOrgunitInfo.domainId="+$("#domainId").val()+"&treeOrgunitInfo.parentId="+$("#parentid").val(),
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
     	  }else{
     	  	$("#name").formValidator({onFocus:'<view:LanguageTag key="org_vd_orgname_tip"/>',onCorrect:"OK"})
		    .inputValidator({min:1,max:128,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="org_vd_name_notempty"/>'}, onError: '<view:LanguageTag key="org_vd_orgname_tip"/>'}).functionValidator({
				fun:function(name){
			    if(g_invalid_char_js(name)){
			       return '<view:LanguageTag key="org_vd_orgname_tip_err"/>';
			    }
			    return true;
			}});
     	  }
	 } 
      
     //域名称重复验证 
     function checkName(){
    	  var domainName=$("#name").val();
    	  var hiddenDomainName=$("#hiddenName").val();    	  
      	  if(domainName!=hiddenDomainName){ //如果不是原来的值 
      	  	  validateDomainName();
      	  }else{ //如果相同
      	  	  $("#name").formValidator({onFocus:'<view:LanguageTag key="org_vd_name_tip"/>',onCorrect:"OK"}).inputValidator({min:1,max:128,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="org_vd_name_err"/>'}, onError: '<view:LanguageTag key="org_vd_name_err_1"/>'}).functionValidator({
  				fun:function(domainName){
	  			    if(g_invalid_char_js(domainName)){
	  			       return "<view:LanguageTag key="org_vd_domainname_tip"/>";
	  			    }
	  			    return true;
	  			}}); 			
      	  }
	 } 
      //判断域名称是否已经存在
      function validateDomainName() {
		  $("#name").ajaxValidator({
				dataType:"html",
				async:true,
				 url:"<%=path%>/manager/orgunit/orgunit/orgunitInfo!findIsExist.action?source=name",
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
	 } 
	 
	 /**
 	  * 返回
	 */
	function goBack() {
	    var readWriteFlag = $("#readWriteFlag").val();
	    var flag = $("#flag").val();
	    var id = $("#id").val();
	    if(id == ""){
	    	id = $("#parentid").val();
	    }
  		location.href = contextPath + '/manager/orgunit/orgunit/action/orgunitInfo!view.action?treeOrgunitInfo.id='+id+"&treeOrgunitInfo.flag="+flag+"&treeOrgunitInfo.readWriteFlag="+readWriteFlag;
	}	
    </script>
  </head>
  
  <body style="overflow:auto; overflow-x:hidden">
  <div id="background"  class="background"  style="display: none; "></div>
  <div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
  <input type="hidden" value="<%=path %>" id="contextPath" />
  <input id="l_userid" type="hidden" value="${curLoginUser}" />
  <input id="l_userid_role" type="hidden" value="${curLoginUserRole}" />
  <input id="cPage" type="hidden" value="${param.currentPage}" />
  <form name="AddForm" id="AddForm" method="post" action="">
  <input id="readWriteFlag" type="hidden" value="${treeOrgunitInfo.readWriteFlag}" />
  <input type="hidden" id="hidname" value="${treeOrgunitInfo.name}" />
  <c:if test='${1==treeOrgunitInfo.flag && 0!=treeOrgunitInfo.id}'><!-- 如果是域 并且是编辑域-->
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td>  
		  <span class="topTableBgText">
	          <c:if test='${0!=treeOrgunitInfo.id}'><view:LanguageTag key="domain_info_edit"/></c:if>
		  </span>
        </td>
      </tr>
    </table> 
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
				    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
				      
				      <tr>
				        <td width="25%" align="right"><view:LanguageTag key="domain_info_code"/><view:LanguageTag key="colon"/></td>
				        <td width="30%">  
					        <input type="hidden" id="isDefault" name="treeOrgunitInfo.isdefault"  value="${treeOrgunitInfo.isdefault}"/>
					        <input type="hidden" id="flag" name="treeOrgunitInfo.flag"  value="${treeOrgunitInfo.flag}"/>
							<input type="hidden" id="parentid" name="treeOrgunitInfo.parentId" value="${treeOrgunitInfo.parentOrgunitInfo.id}" class="formCss100"/>
					       	<input type="hidden" id="domainId" name="treeOrgunitInfo.domainId" value="${treeOrgunitInfo.domainId}" class="formCss100" readonly/>
					        <input type="hidden" id="id" name="treeOrgunitInfo.id" value="${treeOrgunitInfo.id}" class="formCss100"/>
				            <input type="hidden" id="hiddenmark"  value="${treeOrgunitInfo.mark}"/>
				            <input type="hidden" id="hiddenName"  value="${treeOrgunitInfo.name}"/>
					        <input type="text" id="mark" name="treeOrgunitInfo.mark" value="${treeOrgunitInfo.mark}" class="formCss100" onchange="checkDomainInit();"/>
				        </td>
				        <td width="45%" class="divTipCss"><div id="markTip" style="width:100%; margin-left:5px;"></div></td>
				      </tr>
				      <tr>
				        <td align="right"><view:LanguageTag key="domain_info_name"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>       
				        <td> 
				        	<input type="text" id="name" name="treeOrgunitInfo.name" value="${treeOrgunitInfo.name}" class="formCss100" onchange="checkName();"/>
				        </td> 
				        <td class="divTipCss"><div id="nameTip" style="width:100%; margin-left:5px"></div></td>
				      </tr>  
				      <tr>    
					      <td align="right"><view:LanguageTag key="org_sel_admin"/><c:if test='${"ADMIN"!=curLoginUserRole}'><span class="text_Hong_Se">*</span></c:if><view:LanguageTag key="colon"/></td>
					      <td>
					          <select name="treeOrgunitInfo.admins" size="5" multiple class="select100" id="admins">
					             <c:if test='${0!=treeOrgunitInfo.id}'>
						          	 <c:forEach items="${treeOrgunitInfo.admins}" var="admins">
						            	<option value="${admins}" selected>${admins}</option>
						          	 </c:forEach>	
					          	 </c:if>
					      	  </select>
				              <a href="#" onClick="selAdmins();" class="button"><span><view:LanguageTag key="common_syntax_select"/></span></a>
				              <a href="#" onClick="delAdmins();" class="button"><span><view:LanguageTag key="common_syntax_delete"/></span></a>
				          </td>
						  <td class="divTipCss"><div id="adminsTip" style="width:100%; margin-left:5px"></div></td>
					  </tr>	         
				      <tr>
				        <td align="right"><view:LanguageTag key="org_description"/><view:LanguageTag key="colon"/></td>
				        <td>
				        	<textarea id="descp" name="treeOrgunitInfo.descp" class="textarea100">${treeOrgunitInfo.descp}</textarea>
				        </td>
				        <td class="divTipCss"><div id="descpTip" style="width:100%; margin-left:5px"></div></td>
				      </tr>
				      <tr>
				        <td align="right">&nbsp;</td>
				        <td>
				        	<a href="#" id="addBtn" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
				        	<a href="javascript:goBack();" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a>
				        </td>
				        <td>&nbsp;</td>
				      </tr>
				    </table>
    	</td>
    </tr>
    </table>
    </c:if>
    <c:if test='${1!=treeOrgunitInfo.flag || 0==treeOrgunitInfo.id}'><!-- 其他均针对组织机构 -->    
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td>  
		  <span class="topTableBgText">
		  	  <c:if test='${0==treeOrgunitInfo.id}'><view:LanguageTag key="org_add_child_org"/><div id="oper" style="display:none">add</div></c:if>
	          <c:if test='${0!=treeOrgunitInfo.id}'><view:LanguageTag key="org_info_eidt"/></c:if>
		  </span>
        </td>
      </tr>
    </table> 
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td valign="top">
				    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
				      <tr>
				        <td align="right"><view:LanguageTag key="domain_info_belongs"/><span class="text_Hong_Se"></span><view:LanguageTag key="colon"/></td>       
				        <td>
				        <input type="hidden" id="domainId" name="treeOrgunitInfo.domainId" value="${treeOrgunitInfo.domainId}" class="formCss100" readonly/> 
				       ${treeOrgunitInfo.domainInfo.domainName}
				        </td>
				        <td class="divTipCss"><div id="parentIdTip" style="width:100%; margin-left:5px"></div></td>
				      </tr>
				      
				      <tr>
				        <td align="right"><view:LanguageTag key="org_parentorg"/><span class="text_Hong_Se"></span><view:LanguageTag key="colon"/></td>       
				        <td> 
				       		 <input type="hidden" id="parentid" name="treeOrgunitInfo.parentId" value="${treeOrgunitInfo.parentOrgunitInfo.id}" class="formCss100"/>
				       		 <input type="hidden" id="parentName" name="treeOrgunitInfo.orgParentName" value="${treeOrgunitInfo.parentOrgunitInfo.name}" />
				       		 ${treeOrgunitInfo.parentOrgunitInfo.name}
				        </td>
				        <td class="divTipCss"><div id="parentIdTip" style="width:100%; margin-left:5px"></div></td>
				      </tr>
				      
				      <tr>
				        <td width="25%" align="right"><view:LanguageTag key="org_code"/><view:LanguageTag key="colon"/></td>
				        <td width="30%">  
				        <input type="hidden" id="isDefault" name="treeOrgunitInfo.isdefault"  value="${treeOrgunitInfo.isdefault}"/>
				        <input type="hidden" id="flag" name="treeOrgunitInfo.flag"  value="${treeOrgunitInfo.flag}"/>
				        <c:if test='${0!=treeOrgunitInfo.id}'>
				            <input type="hidden" id=id name="treeOrgunitInfo.id" value="${treeOrgunitInfo.id}" class="formCss100"/>
				            <input type="hidden" id="hiddenmark"  value="${treeOrgunitInfo.mark}"/>
					        <input type="text" id="mark" name="treeOrgunitInfo.mark" value="${treeOrgunitInfo.mark}" class="formCss100" onchange="checkOrgInit();"/>
					    </c:if>
					    <c:if test='${0==treeOrgunitInfo.id}'>
				            <input type="hidden" id=id name="treeOrgunitInfo.id" value="" class="formCss100"/>
				            <input type="hidden" id="hiddenmark"  value=""/>
					        <input type="text" id="mark" name="treeOrgunitInfo.mark" value="" class="formCss100" onchange="checkOrgInit();"/>
					    </c:if>
				        </td>
				        <td width="45%" class="divTipCss"><div id="markTip" style="width:100%; margin-left:5px;"></div></td>
				      </tr>
				      <tr>
				        <td align="right"><view:LanguageTag key="org_name"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>       
				        <td> 
				         <c:if test='${0!=treeOrgunitInfo.id}'>
				        <input type="text" id="name" name="treeOrgunitInfo.name" value="${treeOrgunitInfo.name}" class="formCss100" onchange="checkOrgName()"/>
				        </c:if>
				        <c:if test='${0==treeOrgunitInfo.id}'>
				        <input type="text" id="name" name="treeOrgunitInfo.name" value="" class="formCss100" onchange="checkOrgName()"/>
				        </c:if>
				       </td> 
				        <td class="divTipCss"><div id="nameTip" style="width:100%; margin-left:5px"></div></td>
				      </tr>
				      <tr>    
					      <td align="right"><view:LanguageTag key="domain_info_sel_admin"/><c:if test='${"ADMIN"!=curLoginUserRole}'><span class="text_Hong_Se">*</span></c:if><view:LanguageTag key="colon"/></td>
					      <td>
					          <select name="treeOrgunitInfo.admins" size="5" multiple class="select100" id="admins">
					             <c:if test='${0!=treeOrgunitInfo.id}'>
						          	 <c:forEach items="${treeOrgunitInfo.admins}" var="admins">
						            	<option value="${admins}" selected>${admins}</option>
						          	 </c:forEach>	
					          	 </c:if>
					      	  </select>
				              <a href="#" onClick="selAdmins();" class="button"><span><view:LanguageTag key="common_syntax_select"/></span></a>
				              <a href="#" onClick="delAdmins();" class="button"><span><view:LanguageTag key="common_syntax_delete"/></span></a>
				          </td>
						  <td class="divTipCss"><div id="adminsTip" style="width:100%; margin-left:5px"></div></td>
					  </tr>           
				      <tr>
				        <td align="right"><view:LanguageTag key="org_description"/><view:LanguageTag key="colon"/></td>
				        <td>
				        <c:if test='${0!=treeOrgunitInfo.id}'>
				        <textarea id="descp" name="treeOrgunitInfo.descp" class="textarea100">${treeOrgunitInfo.descp}</textarea>
				        </c:if>
				        <c:if test='${0==treeOrgunitInfo.id}'>
				        <textarea id="descp" name="treeOrgunitInfo.descp" class="textarea100"></textarea>
				        </c:if>
				        </td>
				        <td class="divTipCss"><div id="descpTip" style="width:100%; margin-left:5px"></div></td>
				      </tr>
				      <tr>
				        <td align="right">&nbsp;</td>
				        <td>
				        	<a href="#" id="addBtn" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
				        	<a href="javascript:goBack();" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a>
				        </td>
				        <td>&nbsp;</td>
				      </tr>
				    </table>
    	</td>
    </tr>
    </table>
    </c:if>
  </form>
  </body>
</html>