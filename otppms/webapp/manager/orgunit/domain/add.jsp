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
	<script language="javascript" src="<%=path%>/manager/orgunit/domain/js/add.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>  
    <script language="javascript" type="text/javascript">
    	// Start,多语言提取
		var save_succ_lang = '<view:LanguageTag key="common_save_succ_tip"/>';
		var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
		var sel_admin_lang = '<view:LanguageTag key="domain_info_sel_admin"/>';
		var need_del_date_lang = '<view:LanguageTag key="common_syntax_check_need_del_date"/>';
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
	 }); 
	//表单验证信息 
	function checkAdminForm(){
        $.formValidator.initConfig({
      		submitButtonID:"addBtn",
      		debug:true,
			onSuccess:function(){
			    if('${domainInfo.domainId}'== null || '${domainInfo.domainId}'==''){
			     addObj(0,'adminIds');
			    }else{
			      addObj(1,'adminIds');
			    }
			}, 
			onError:function(){
				return false;
			}
		});
     	// 选择管理员
		if($('#l_userid_role').val() != 'ADMIN'){
			$("#adminIds").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>', onCorrect:"OK"}).functionValidator({
	   		 	fun:function(val){
	   		 	     var val = $("#adminIds").text();
		             if($.trim(val)=='' || $.trim(val)==null){
	                 	return '<view:LanguageTag key="admin_vd_admin"/>';
	                 }
	                 return true;
	   		 	}
	   		 });
		}
		
		$("#domainSn").formValidator({onFocus:'<view:LanguageTag key="domain_vd_info_code_tip"/>', onCorrect:"OK"}).inputValidator({min:1,max:64,onError:'<view:LanguageTag key="domain_vd_info_code_err"/>'}).regexValidator({regExp:"domainSnRegex",dataType:"enum",onError:'<view:LanguageTag key="domain_vd_info_code_err_1"/>'}); //注意这里的 username 是正则表达式 英文数字 下划线

		var domainName = $.trim($("#domainName").val());
		$("#domainName").formValidator({onFocus:'<view:LanguageTag key="domain_vd_info_name_tip"/>',onCorrect:"OK"}).inputValidator({min:1,max:128,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="domain_vd_info_name_err"/>'}, onError: '<view:LanguageTag key="domain_vd_info_name_tip"/>'}).functionValidator({
			fun:function(domainName){
		    if(g_invalid_char_js(domainName)){
		       return "<view:LanguageTag key="org_vd_domainname_tip"/>";
		    }
		    return true;
		}});
		$("#descp").formValidator({onFocus:'<view:LanguageTag key="common_vd_descp_show"/>',onCorrect:"OK"}).inputValidator({max:255,onError:'<view:LanguageTag key="common_vd_descp_error"/>'});
		$("#domainSn").focus();   
    }
      
    //域标识验证
    function checkInit(){
   	  var domainSn=$("#domainSn").val();
   	  var hiddendomainSn=$("#hiddendomainSn").val();
     	  if(domainSn!=hiddendomainSn){ //如果不是原来的值 
     	  	  validateDomainSn();
     	  }else{ //如果相同
     	  	  //注意这里的 username 是正则表达式 英文数字 下划线	
     	  	  $("#domainSn").formValidator({onFocus:'<view:LanguageTag key="domain_vd_info_code_tip"/>'}).inputValidator({min:1,max:64,onError:'<view:LanguageTag key="domain_vd_info_code_err"/>'}).regexValidator({regExp:"domainSnRegex",dataType:"enum",onError:'<view:LanguageTag key="domain_vd_info_code_err_1"/>'}); 		
     	  }
 	}
	//域标识验证引用函数 
	function validateDomainSn() {
		//判断域帐号是否已经存在
	  $("#domainSn").ajaxValidator({
			dataType:"html",
			async:true,
			 url:"<%=path%>/manager/orgunit/domain/domainInfo!findDomainInfoExist.action",
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
     
    //域名称验证  
	function checkName(){
		var domainName=$("#domainName").val();
		var hiddenDomainName=$("#hiddenDomainName").val();
		if(domainName!=hiddenDomainName){ //如果不是原来的值 
			  validateDomainName();
		}else{ //如果相同
			$("#domainName").formValidator({onFocus:'<view:LanguageTag key="domain_vd_info_name_tip"/>',onCorrect:"OK"}).inputValidator({min:1,max:128,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="domain_vd_info_name_err"/>'}, onError: '<view:LanguageTag key="domain_vd_info_name_tip"/>'}).functionValidator({
				fun:function(domainName){
			    if(g_invalid_char_js(domainName)){
			       return "<view:LanguageTag key="org_vd_domainname_tip"/>";
			    }
			    return true;
			}});
		}
	} 
    
    //域名称重复校验  
	function validateDomainName() {
		$("#domainName").ajaxValidator({
			dataType:"html",
			async:true,
			 url:"<%=path%>/manager/orgunit/domain/domainInfo!findDomainInfoExist.action?source=name",
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
    </script>
  </head>
  <body scroll="no" style="overflow:hidden">
  <input type="hidden" value="<%=path %>" id="contextPath" />
  <input id="l_userid" type="hidden" value="${curLoginUser}" />
  <input id="l_userid_role" type="hidden" value="${curLoginUserRole}" />
  <input id="cPage" type="hidden" value="${param.currentPage}" />
  <form name="AddForm" id="AddForm" method="post" action="">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td>  
		  <span class="topTableBgText">
		  	  <c:if test='${empty domainInfo.domainId}'><view:LanguageTag key="domain_info_add"/></c:if>
	          <c:if test='${not empty domainInfo.domainId}'><view:LanguageTag key="domain_info_edit"/></c:if>
		  </span>
        </td>
      </tr>
    </table> 
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
				    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
				      
				      <tr>
				        <td width="30%" align="right"><view:LanguageTag key="domain_info_code"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
				        <td width="30%">
				        	
				            <input type="hidden" id="domainId" name="domainInfo.domainId" value="${domainInfo.domainId}" class="formCss100"/>
				            <input type="hidden" id="hiddendomainSn"  value="${domainInfo.domainSn}"/>
				            <input type="hidden" id="hiddenDomainName"  value="${domainInfo.domainName}"/>
					        <input type="text" id="domainSn" name="domainInfo.domainSn" value="${domainInfo.domainSn}" class="formCss100" onchange="checkInit();"/>
				        </td>
				        <td width="40%" class="divTipCss"><div id="domainSnTip" style="width:100%; margin-left:5px;"></div></td>
				      </tr>
				      
				      <tr>
				        <td align="right"><view:LanguageTag key="domain_info_name"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>       
				        <td> 
				        <input type="text" id="domainName" name="domainInfo.domainName" value="${domainInfo.domainName}" class="formCss100" onchange="checkName();"/>
				        </td>
				        <td class="divTipCss"><div id="domainNameTip" style="width:100%; margin-left:5px"></div></td>
				      </tr>
				      
				      <tr>    
					      <td align="right"><view:LanguageTag key="domain_info_sel_admin"/><c:if test='${"ADMIN"!=curLoginUserRole}'><span class="text_Hong_Se">*</span></c:if><view:LanguageTag key="colon"/></td>
					      <td>
					          <select name="domainInfo.adminIds" size="5" multiple class="select100" id="adminIds">
					             <c:if test='${not empty domainInfo.domainId}'>
						          	 <c:forEach items="${domainInfo.adminIds}" var="adminId">
						            	<option value="${adminId}" selected>${adminId}</option>
						          	 </c:forEach>	
					          	 </c:if>
					      	  </select>
				              <a href="#" onClick="selAdmins();" class="button"><span><view:LanguageTag key="common_syntax_select"/></span></a>
				              <a href="#" onClick="delAdmins();" class="button"><span><view:LanguageTag key="common_syntax_delete"/></span></a>
				          </td>
						  <td class="divTipCss"><div id="adminIdsTip" style="width:100%; margin-left:5px"></div></td>
					  </tr>	
					  	             
				      <tr>
				        <td align="right"><view:LanguageTag key="domain_info_descp"/><view:LanguageTag key="colon"/></td>
				        <td><textarea id="descp" name="domainInfo.descp" class="textarea100">${domainInfo.descp}</textarea></td>
				        <td class="divTipCss"><div id="descpTip" style="width:100%; margin-left:5px"></div></td>
				      </tr>
				      
				      <tr>
				        <td align="right">&nbsp;</td>
				        <td>
				        	<a href="#" id="addBtn" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
				        	<a href="#" onClick=goBack() id="goback" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a>
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