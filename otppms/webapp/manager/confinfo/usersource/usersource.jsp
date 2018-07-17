<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.ft.otp.common.NumConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="view" uri="/WEB-INF/tld/view.tld"%>
<%
	String path = request.getContextPath();
    String usname ="";
    String usdescp = "";
    String reload = "";
    String pageUrl = "ldap.jsp"; 
    int ustype = 1;
    
    if(request.getAttribute("usName") != null){
           usname = (String)request.getAttribute("usName");
    }if(request.getAttribute("usType") != null){
           ustype =  (Integer)request.getAttribute("usType");
    }if(request.getAttribute("usDescp") != null){
          usdescp = (String)request.getAttribute("usDescp");
    }if(request.getAttribute("reload") != null){
          reload = (String)request.getAttribute("reload");
    } 
    
    if(ustype == NumConstant.common_number_0){
        pageUrl ="database.jsp";
    }else if(ustype == NumConstant.common_number_2){
        pageUrl ="domino.jsp";
    } 
  
%>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title></title>
	<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
	<link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
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
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script> 
	<script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/confinfo/usersource/js/usersource.js" charset="UTF-8"></script> 
	<script language="javascript" type="text/javascript">
	// Start,多语言提取
	var del_relation_lang = '<view:LanguageTag key="usource_no_sel_del_relation"/>';
	var others_relation_lang = '<view:LanguageTag key="usource_del_others_relation"/>';
	var update_err_lang = '<view:LanguageTag key="usource_oper_update_err"/>';
	var succ_tip_lang = '<view:LanguageTag key="common_conn_succ_tip"/>';
	var error_tip_lang = '<view:LanguageTag key="common_conn_error_tip"/>';
	var conf_succ_lang = '<view:LanguageTag key="usource_save_conf_succ"/>';
	var timing_task_lang = '<view:LanguageTag key="usource_is_set_timing_task"/>';
	var timing_config_lang = '<view:LanguageTag key="usource_timing_config"/>';
	var oper_err_lang = '<view:LanguageTag key="usource_save_oper_err"/>';
	var save_conf_err_lang = '<view:LanguageTag key="usource_save_conf_err"/>';
	// End,多语言提取
		
	var reload =""; //是否从新加载
	var usname = "";
	var usdescp ="";
	var usType = "";//用户来源类型
    var isEdit= false; //isEdit-true编辑  isEdit-false添加
 
    if("${requestScope.isEdit}"=="isEdit"){
		isEdit = true;
	}
	//重新加载
	if("${requestScope.reload}"=="true"){
		reload = "<%=reload%>";
		usType = "<%=ustype%>";
	    usname = "<%=usname%>";
	    descp  = "<%=usdescp%>";
	}
	
  	//通列li的索引跳转到指定标签页，执行上一步，下一步的功能
    function stepController(index){
		$("#menu li.tabFocus").removeClass("tabFocus"); //移除已选中的样式
		$("#menu li:eq(" + index + ")").addClass("tabFocus");//增加当前选中项的样式
		$("#content li:eq(" + index + ")").show().siblings().hide();//显示选项卡对应的内容并隐藏未被选中的内容
    }
      
    $(document).ready(function(){  
    	if(isEdit){//编辑
			usType = "${userSourceInfo.sourcetype}";
		}else{//添加
		     if(reload == ''|| reload == null){//第一次进入添加页面，默认加载LDAP
		        usType = 1;
		        $("#sourcetype option[value='1']").attr("selected", true);  
		     }else{              //点击基本配置的“下一步”根据选择的ldap类型，从新加载页面
		     	$("#sourcetype option[value='"+usType+"']").attr("selected", true);
		     	//保存之前填写的ldap基本配置项
		     	$("#sourcename").val(usname);
		     	$("#descp").val(descp);
		        //直接转向详细配置选项
		        stepController(1);
		   	 } 
		     changeUserSourceType(usType, 0); 
	     }
		 //点击用户来源基本配置“下一步”按钮
	     $.formValidator.initConfig({submitButtonID:'toDetailUSConfig',debug:false,
			onSuccess:function(){ 
			   if(!isEdit && reload == ''){//添加
			   		var form = document.getElementById("usForm");
			   	    form.action="<%=path%>/manager/confinfo/usersource/usConf!freshPage.action";
			   	    form.submit();
			   }else{
			   		stepController(1);
			   }
			 },
			 onError:function(){
				 return false;
			 }
		  });
			
	 	 $("#sourcename").formValidator({onFocus:'<view:LanguageTag key="usource_vd_name"/>',onCorrect:"OK"}).inputValidator({min:1,max:64,onError:'<view:LanguageTag key="usource_vd_name_err"/>'});
	 	 
		 $("#sourcetype").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:1,onError: '<view:LanguageTag key="common_vd_please_sel"/>'});
		 $("#descp").formValidator({onFocus:'<view:LanguageTag key="common_vd_descp_show"/>',onCorrect:"OK"}).inputValidator({max:255,onError:'<view:LanguageTag key="common_vd_descp_error"/>'});
		 $.formValidator.reloadAutoTip();
	});
	
	//验证用户来源名称
	function checkusname() {
		$("#sourcename").ajaxValidator({
			async:true,
		 	dataType:"text",
			url:"<%=path%>/manager/confinfo/usersource/usConf!validateUSName.action",
			success:function(data){
			    if(data =='true') {
			    	return true;
			    }else{
					return false;
				}
			},
			buttons:$("#toDetailUSConfig"),
			error:function(jqXHR, textStatus, errorThrown){
				$.messager.alert('<view:LanguageTag key="common_vd_warning"/>','<view:LanguageTag key="common_vd_server_is_busy"/>','warning');
			},
			onError:'<view:LanguageTag key="common_vd_already_exists"/>',
			onWait:'<view:LanguageTag key="common_vd_checking_exists"/>'
		 });
	}

	//显示要添加的数据源模块
	function changeUserSourceType(type, flag){
		if(type!=""){
			usType = type;
	    }
	    reload = '';
	    if (flag == 1) {
	    	$("input[name='userSourceInfo.username']").val('');
	    	$("input[name='userSourceInfo.pwd']").val('');
	    	$("input[name='userSourceInfo.serveraddr']").val('');
	    	if (usType == 1) {
	    		$("input[name='userSourceInfo.port']").val('389');
	    	}else {
	    		$("input[name='userSourceInfo.port']").val('3306');
	    	}
	    }
 	}
 
	//更新用户方法
	function updateUserInfo(){
       var weekAttrStr = "";
       var taskmode1 = $('#taskmode1').val();
       if(taskmode1 == 2){
       	  $('input[name="weekAttr[]"]').each(function () {
            if ($(this).attr('checked') == true) {
                weekAttrStr += $(this).attr('value')+",";
            }
          });
          if(weekAttrStr!=""){
          	 weekAttrStr = weekAttrStr.substring(0,weekAttrStr.length-1); 
          }
          $('#taskweek').val(weekAttrStr);
       }
          
	   var checkUSNameUrl = "<%=path%>/manager/confinfo/usersource/usConf!updateUserInfo.action?isEdit="+isEdit+"&enabled="+ $('#enabled').val();
	   $("#usForm").ajaxSubmit({
		   async:false,  
		   type:"POST", 
		   dataType : "json",
		   url : checkUSNameUrl,
		   success:function(msg){
				if(msg.errorStr == 'error'){
				   FT.toAlert('error',msg.object, null);
				}else{ 
				   $.ligerDialog.success(msg.object,'<view:LanguageTag key="common_syntax_tip"/>',function(){
					   window.location.href="<%=path%>/manager/confinfo/usersource/list.jsp";
				   });
				}
			}
		});		 
	 }
   
   	//禁止回车提交表单
	document.onkeydown = function(evt){
		var evt = window.event?window.event:evt;
		if(evt.keyCode==13) {
			if(!saveConn()){
				return false;
			}
		}
	}     
 
	//返回操作
	function goBack() {
		window.location.href="<%=path%>/manager/confinfo/usersource/list.jsp";
	}
	//-->
</script>
 
</head>
<body scroll="no" style="overflow:hidden">
<div id="background"  class="background"  style="display: none; "></div>
<div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
 <input type="hidden" name="contextPath" value="<%=path%>" id="contextpath"/>
 <form name="usForm" id="usForm" method="post" action="">
	 <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="topTableBg">
		<tr>
		   <td> 
		   		<span class="topTableBgText">
 					<%if(reload =="true"){%>  
                      	<view:LanguageTag key="usource_info_add"/>
                  	<%}else{%>
				   <c:if test="${empty userSourceInfo.sourcename}"><view:LanguageTag key="usource_info_add"/></c:if>
				   <c:if test="${not empty userSourceInfo.sourcename}"><view:LanguageTag key="usource_info_edit"/></c:if>
				   <%}%> 
				</span>
		    </td>
		 </tr>
    </table>
	<table width="100%" border="0"  cellpadding="0" cellspacing="0" class="ulOnTable">
	 <tr>
		<td width="100%" colspan="2">
			<ul id="menu">
				<li class="tabFocus" style="cursor: default"><view:LanguageTag key="usource_basic_config"/></li>
				<li style="cursor: default"><view:LanguageTag key="usource_detailed_config"/></li>
				<li style="cursor: default"><view:LanguageTag key="usource_save_config"/></li>
			</ul>
			<ul id="content">
				<li class="conFocus">	
					<table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
                     <tr>
						<td width="25%" align="right"><view:LanguageTag key="usource_name"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
						<td width="30%" >
						   <%if(reload =="true"){%>
					       		<input type="text" id="sourcename" name="userSourceInfo.sourcename" onchange="checkusname();" 
					       			value="${userSourceInfo.sourcename}" class="formCss100" maxlength="255" maxlength="255" />
						   <%}else{%>
						  <c:if test="${empty userSourceInfo.sourcename}">
						    	<input type="text" id="sourcename" name="userSourceInfo.sourcename" onchange="checkusname();"
									value="${userSourceInfo.sourcename}" class="formCss100" maxlength="255" maxlength="255" />
						  </c:if>
						  <c:if test="${not empty userSourceInfo.sourcename}"> 
						   		<input type="hidden" id="uSName" name="userSourceInfo.sourcename" value="${userSourceInfo.sourcename}" />
						       	${userSourceInfo.sourcename}
						  </c:if>
						  <%}%>
						    <input type="hidden" id="oldsourcename" name="userSourceInfo.oldsourcename" value="${userSourceInfo.sourcename}" class="formCss100" />
						    <input type="hidden" id="usid" name="userSourceInfo.id" value="${userSourceInfo.id}" class="formCss100" />
						</td>
						<td width="45%" class="divTipCss"><div id="sourcenameTip"></div></td>
					</tr>
					<tr>
						<td align="right"><view:LanguageTag key="usource_type"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
						<td> 
						 <%if(reload =="true"){%>
						   <select id="sourcetype" name="userSourceInfo.sourcetype" class="select100"
										onchange="changeUserSourceType(this.value, 1)">
									<option value=""><view:LanguageTag key="usource_sel_usersource"/></option>
									<option value="0"><view:LanguageTag key="usource_remote_db"/></option>
									<option value="1">LDAP</option>
									<!--  
									<option value="2">Domino</option>
									-->
								</select>
						   <%}else{%>
						       <c:if test="${empty userSourceInfo.sourcetype}">
									<select id="sourcetype" name="userSourceInfo.sourcetype" class="select100"
										onchange="changeUserSourceType(this.value, 1)">
										<option value="" >
											<view:LanguageTag key="usource_sel_usersource"/>
										</option>
										<option value="0" <c:if test="${userSourceInfo.sourcetype eq 0}">selected</c:if>>
											<view:LanguageTag key="usource_remote_db"/>
										</option>
										<option value="1" <c:if test="${userSourceInfo.sourcetype eq 1}">selected</c:if>>
											LDAP
										</option>
										<!--  
										<option value="2" <c:if test="${userSourceInfo.sourcetype eq 2}">selected</c:if>>
											Domino
										</option>
										-->
									</select>
								</c:if>
								 <c:if test="${not empty userSourceInfo.sourcetype}">
		                           <c:if test="${userSourceInfo.sourcetype eq 0}"><view:LanguageTag key="usource_remote_db"/></c:if>
		                           <c:if test="${userSourceInfo.sourcetype eq 1}">LDAP</c:if>
		                           <!--  
		                           <c:if test="${userSourceInfo.sourcetype eq 2}">Domino</c:if>
		                           -->
		                         <input type="hidden" id="sourcetype" name="userSourceInfo.sourcetype" value="${userSourceInfo.sourcetype}" />
								 </c:if>
						   <%}%>
						  </td>
						<td class="divTipCss"><div id="sourcetypeTip" style="width:100%"></div></td>
					</tr>
					<tr>
						<td align="right"><view:LanguageTag key="usource_descp"/><view:LanguageTag key="colon"/></td>
						<td>
							<input type="text" id="descp" name="userSourceInfo.descp" value="${userSourceInfo.descp}" class="formCss100" maxlength="255" />
						</td>
						<td class="divTipCss"><div id="descpTip" style="width:100%"></div></td>
					</tr>
						<tr>
						<td>&nbsp;</td>
						<td>
							<a href="#" id="toDetailUSConfig" class="button"><span><view:LanguageTag key="common_syntax_nextStep"/></span></a>
							<a href="javascript:goBack();" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a>
						</td>
					</tr>
			</table>
		</li>
		<!--详细配置和保存配置项-->
		<jsp:include  page= "<%=pageUrl%>"   flush="true"/> 
		
	 </ul>
	</td>
  </tr>
</table>	 
</form>
</body>
</html>