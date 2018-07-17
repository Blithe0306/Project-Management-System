<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld"%>
<%
    String path = request.getContextPath();

	int isAdmin = 0;
	int isLocal = 0;
	if(null != application.getAttribute("isSuperMan")){
	    isAdmin = (Boolean)application.getAttribute("isSuperMan") ? 1 : 0;
	}
	if(null != application.getAttribute("isLocalIp")){
	    isLocal = (Boolean)application.getAttribute("isLocalIp") ? 1 : 0;
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
<link rel="stylesheet" id="openwincss" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>
<script language="javascript" type="text/javascript">
	$(document).ready(function(){
		$.formValidator.initConfig({submitButtonID:"initBtn", 
			onSuccess:function(){
				testConn();
			},
			onError:function(){
				return false;
			}});
			
			$("#servname").formValidator({onFocus:'<view:LanguageTag key="email_vd_servname_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:64, onError:'<view:LanguageTag key="email_vd_servname_err"/>'}).regexValidator({regExp:"^[a-zA-Z0-9\u4e00-\u9fa5]+$",dataType:"string",onError:'<view:LanguageTag key="email_vd_servname_err_1"/>'});
			$("#host").formValidator({onFocus:'<view:LanguageTag key="email_vd_host_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:64, onError:'<view:LanguageTag key="email_vd_host_err"/>'}).regexValidator({regExp:"^[A-Za-z0-9]+(\.[A-Za-z0-9]+)+$",dataType:"string",onError:'<view:LanguageTag key="email_vd_host_err_1"/>'});
			$("#account").formValidator({onFocus:'<view:LanguageTag key="email_vd_account_show"/>',onCorrect:"OK"}).inputValidator({min:4,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="email_vd_account_err"/>'}, onError:'<view:LanguageTag key="email_vd_account_err_1"/>'}).regexValidator({regExp:"email",dataType:"enum",onError:'<view:LanguageTag key="email_vd_account_err_2"/>'});
			$("#password").formValidator({onFocus:'<view:LanguageTag key="common_vd_pwd_show"/>',onCorrect:"OK"}).inputValidator({min:4,max:32, onError:'<view:LanguageTag key="common_vd_pwd_show"/>'});
			$("#sender").formValidator({onFocus:'<view:LanguageTag key="email_vd_sender_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="email_vd_sender_err"/>'}, onError:'<view:LanguageTag key="email_vd_sender_err_1"/>'});
			$("#port").formValidator({onFocus:'<view:LanguageTag key="email_vd_port_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:65535, type:"number", onError:'<view:LanguageTag key="email_vd_port_err"/>'});
			$("input[name='emailConf.validate']").formValidator({tipID:"validateTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,max:1, onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
			$("#descp").formValidator({onFocus:'<view:LanguageTag key="common_vd_descp_show"/>',onCorrect:"OK"}).inputValidator({max:255,onError:'<view:LanguageTag key="common_vd_descp_error"/>'});
			$("#servname").focus();
		});
		
		function checkInit(obj){
			var servname = $("#servname").val();			
			var hidservname = $("#emailservname").val();
			var host = $("#host").val();
			var hidhost = $("#emailhost").val();
			if(obj == 0){
				if(servname != hidservname) {
					validateSNameUtil();
				}else{
					$("#servname").formValidator({onFocus:'<view:LanguageTag key="email_vd_servname_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:32,onError:'<view:LanguageTag key="email_vd_servname_err"/>'}).regexValidator({regExp:"^[a-zA-Z0-9\u4e00-\u9fa5]+$",dataType:"string",onError:'<view:LanguageTag key="email_vd_servname_err_1"/>'});
				}
			}else{
				if(host != hidhost) {
					validateHostUtil();
				}else{
					$("#host").formValidator({onFocus:'<view:LanguageTag key="email_vd_host_show"/>',onCorrect:"OK"}).inputValidator({min:4,max:20, onError:'<view:LanguageTag key="email_vd_host_err"/>'}).regexValidator({regExp:"^[A-Za-z0-9]+(\.[A-Za-z]+)+$",dataType:"string",onError:'<view:LanguageTag key="email_vd_host_err_1"/>'});
				}
			}
		}
		
		//服务器端进行邮箱服务器名称校验方法
		function validateSNameUtil() {
			$("#servname").ajaxValidator({
				dataType:"html",
				async:true,
				url:"<%=path%>/manager/confinfo/email/email!findOENameisExist.action",
				success:function(data){
			    	if(data =='false') {return false;}
					return true;
				},
				buttons:$("#initBtn"),
				error:function(jqXHR, textStatus, errorThrown){
						$.messager.alert('<view:LanguageTag key="common_vd_warning"/>','<view:LanguageTag key="common_vd_server_is_busy"/>','warning');
				},
				onError:'<view:LanguageTag key="common_vd_already_exists"/>',
				onWait:'<view:LanguageTag key="common_vd_checking_exists"/>'
			});
		}
		
		//服务器端进行邮箱服务器校验方法
		function validateHostUtil() {
		 	$("#host").ajaxValidator({
				dataType:"html",
				async:true,
				url:"<%=path%>/manager/confinfo/email/email!findOEisExist.action",
				success:function(data){
			        if(data =='false') {return false;}
					return true;
				},
				buttons:$("#initBtn"),
				error:function(jqXHR, textStatus, errorThrown){
						$.messager.alert('<view:LanguageTag key="common_vd_warning"/>','<view:LanguageTag key="common_vd_server_is_busy"/>','warning');
				},
				onError:'<view:LanguageTag key="common_vd_already_exists"/>',
				onWait:'<view:LanguageTag key="common_vd_checking_exists"/>'
			});
		}
		
		var ajaxbg = null;		
		//初始化添加一个默认邮件服务器
		function addObj(){
		 	var url = '<%=path%>/manager/confinfo/email/email!initAdd.action';
			$("#initEmailForm").ajaxSubmit({
				async : true,  
				type : "POST", 
				url : url,
				dataType : "json",
				success : function(msg){
					if(msg == '0'){
						ajaxbg.hide();
						parent.hideId('09', 'T09');
				     	nextPage();				   		
				   	}else{
				   		FT.toAlert('error', '<view:LanguageTag key="email_save_conf_err"/>', null);
						ajaxbg.hide();
				   	}
				}
			});		  
	 	}
	 	
	 	//邮件服务器连接测试
	 	function testConn(){
	 		ajaxbg = $("#background,#progressBar");//加载等待
	 		ajaxbg.show();
	 		var url = '<%=path%>/manager/confinfo/email/email!emailConnTest.action';
			$("#initEmailForm").ajaxSubmit({
				async : true,  
				type : "POST", 
				url : url,
				dataType : "json",
				success : function(msg){
					if(msg == '0'){
				   		addObj();
				   	}else{
				     	$.ligerDialog.confirm('<view:LanguageTag key="sys_init_save_conf_email_err_is_next"/>','<view:LanguageTag key="common_syntax_confirm"/>',function(sel) {
				     		if(sel){
				     			addObj();
				     		}
				     	});
				     	ajaxbg.hide();
				   	}
				}
			});
	 	}
		
	 	//保存并下一步
	 	function nextPage(){
			if('<%=isAdmin%>' == '1'){ //管理员已配置
				if('<%=isLocal%>' == '1'){//IP修改选择
					parent.showId('07', 'T07');
				}else{//完成
					parent.toPage("frameView08", "/install/install!finish.action");
					parent.showId('08', 'T08');
				}
			}else{
				parent.toPage("frameView06", "/install/adminpage.jsp");
				parent.showId('06', 'T06');
			}
		}
	</script>
</head>
<body style="overflow:auto; overflow-x:hidden">
<div id="background" class="background" style="display: none; "></div>
<div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="sys_being_saved_please_wait"/></div>
<form id="initEmailForm" method="post" action="">
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
    <tr>
      <td width="30%" align="right" ><view:LanguageTag key="email_servname"/>(<span class="text_Hong_Se">*</span>)<view:LanguageTag key="colon"/></td>
      <td width="30%"><input type="text" id="servname" name="emailInfo.servname" value="${emailInfo.servname}" onchange="checkInit(0);" class="formCss100" />
      <input type="hidden" id="emailservname" value="${emailInfo.servname}" />
      </td>
      <td width="40%" class="divTipCss"><div id="servnameTip"></div></td>
    </tr>
    <tr>
      <td align="right"><view:LanguageTag key="email_init_host"/>(<span class="text_Hong_Se">*</span>)<view:LanguageTag key="colon"/></td>
      <td><input type="text" id="host" name="emailInfo.host" value="${emailInfo.host}" onchange="checkInit(1);" class="formCss100"/>
      </td>
      <td class="divTipCss"><div id="hostTip"></div></td>
    </tr>
    <tr>
      <td align="right"><view:LanguageTag key="email_port"/>(<span class="text_Hong_Se">*</span>)<view:LanguageTag key="colon"/></td>
      <td><input type="text" id="port" name="emailInfo.port" value='${empty emailInfo.port ? "25" : emailInfo.port}' class="formCss100"/>
      </td>
      <td class="divTipCss"><div id="portTip"></div></td>
    </tr>
    <tr>
      <td align="right"><view:LanguageTag key="email_init_account"/>(<span class="text_Hong_Se">*</span>)<view:LanguageTag key="colon"/></td>
      <td><input type="text" id="account" name="emailInfo.account" value="${emailInfo.account}" class="formCss100"/>
      <input type="hidden" id="emailhost" value="${emailInfo.host}" />
      </td>
      <td class="divTipCss"><div id="accountTip"></div></td>
    </tr>
    <tr>
      <td align="right"><view:LanguageTag key="email_pwd"/>(<span class="text_Hong_Se">*</span>)<view:LanguageTag key="colon"/></td>
      <td><input onpaste="return false" type="password" id="password" name="emailInfo.pwd" class="formCss100"/>
      </td>
      <td class="divTipCss"><div id="passwordTip"></div></td>
    </tr>
    <tr>
      <td align="right" ><view:LanguageTag key="email_sender"/>(<span class="text_Hong_Se">*</span>)<view:LanguageTag key="colon"/></td>
      <td><input type="text" id="sender" name="emailInfo.sender" value="${emailInfo.sender}" class="formCss100"/>
      </td>
      <td class="divTipCss"><div id="senderTip"></div></td>
    </tr>
    <tr>
	     <td align="right"><view:LanguageTag key="email_is_validate_acc"/><view:LanguageTag key="colon"/></td>
	     <td> 
	      	<input type="radio" id="validate0" name="emailInfo.validated" value="0" checked
	        	<c:if test="${emailInfo.validated eq 0 }">checked</c:if>
	        />
	        <view:LanguageTag key="common_syntax_no"/>
	        <input type="radio" id="validate1" name="emailInfo.validated" value="1"  
	        <c:if test="${emailInfo.validated eq 1 }">checked</c:if>
	        />
	        <view:LanguageTag key="common_syntax_yes"/>
	      </td>
	     <td class="divTipCss"><div id="validateTip"></div></td>
	</tr>
    <tr>
      <td align="right"><view:LanguageTag key="common_syntax_desc"/><view:LanguageTag key="colon"/></td>
      <td><textarea id="descp" name="emailInfo.descp" class="textarea100">${emailInfo.descp}</textarea></td>
      <td class="divTipCss"><div id="descpTip"></div></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td colspan="2">
      	<input type="hidden" id="id" name="emailInfo.id" value="${emailInfo.id}"/>
        <input type="hidden" id="isdefault" name="emailInfo.isdefault" value="${emailInfo.isdefault}" />
		<a href="#" id="initBtn" class="button"><span><view:LanguageTag key="sys_init_save_nad_next"/></span></a></td>
    </tr>
  </table>
</form>
</body>
</html>
