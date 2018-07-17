<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld"%>
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
	$(document).ready(function(){
		//绑定点击事件
		domClickEvent();
		//点击按钮弹出窗口，设置窗口样式，取消已绑定点击事件
		var cssurl = "<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"
        btnClickEvent("#addBtn",cssurl);
		
		var typeVal = '${agentConfInfo.type}';
		setUserformat(typeVal);
	
		$.formValidator.initConfig({submitButtonID:"addBtn", 
			onSuccess:function(){
				addObj();
			},
			onError:function(){
				return false;
			}});
			$("#confname").formValidator({onFocus:'<view:LanguageTag key="auth_conf_vd_name"/>',onCorrect:"OK"}).inputValidator({min:1,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="auth_conf_vd_name_err"/>'},onError:'<view:LanguageTag key="auth_conf_vd_name_err_1"/>'});
			$("#type").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).functionValidator({	
			fun:function(val,elem){
				 return true;
			}
			});

			$("#localprotect").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});	
			$("#remoteprotect").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});	
			$("#uacprotect").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});	
			$(":radio[name='agentConfInfo.unboundlogin']").formValidator({tipID:"unboundloginTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:1,max:1,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
			$("#descp").formValidator({onFocus:'<view:LanguageTag key="common_vd_descp_show"/>',onCorrect:"OK"}).inputValidator({max:255,onError:'<view:LanguageTag key="common_vd_descp_error"/>'});
	});
	
	//验证配置名称
	function checkName() {
		var id = $("#id").val();	
		var confnameHid = $("#confnameHid").val();	
		var confname = $("#confname").val();
			
		if(confname != confnameHid) {
			validateConfName();
		}else {
			if (id == null || "" == id) {
				validateConfName();
			} else {
				$("#confname").formValidator({onFocus:'<view:LanguageTag key="auth_conf_vd_name"/>',onCorrect:"OK"}).inputValidator({min:1,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="auth_conf_vd_name_err"/>'},onError:'<view:LanguageTag key="auth_conf_vd_name_err_1"/>'});
			}
		}
	}
	
	//认证代理配置名称是否存在
	function validateConfName() {
	 	$("#confname").ajaxValidator({
			dataType:"html",
			async:true,
			url:"<%=path%>/manager/authmgr/agentconf/agentConf!checkConfName.action",
			success:function(data){
			   if(data =='false') {return false;}
			   return true;
			},
			buttons:$("#addBtn"),
			error:function(jqXHR, textStatus, errorThrown){
				$.messager.alert('<view:LanguageTag key="common_vd_warning"/>','<view:LanguageTag key="common_vd_server_is_busy"/>','warning');
			},
			onError:'<view:LanguageTag key="common_vd_already_exists"/>',
			onWait:'<view:LanguageTag key="common_vd_checking_exists"/>'
		});
	 }
	
	//添加，编辑认证代理配置操作
	function addObj() {
		var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
	 	var id = $("#id").val();
	 	var isAdd = true;
		var urlTo = '<%=path%>/manager/authmgr/agentconf/agentConf!add.action';
		if("" != id){
			urlTo = '<%=path%>/manager/authmgr/agentconf/agentConf!modify.action';
			isAdd = false;
		}
	
		$("#addForm").ajaxSubmit({
			async : true,  
			type : "POST", 
			url : urlTo,
			dataType : "json",
			success : function(msg){
				if(msg.errorStr == 'success'){
					ajaxbg.hide();
					if(isAdd){// 添加
						FT.Dialog.confirm('<view:LanguageTag key="common_save_success_continue_add"/>','<view:LanguageTag key="common_syntax_confirm"/>', function(sel){
	           			 	if(sel) {
			    				location.href = "<%=path%>/manager/authmgr/agentconf/add.jsp";
			    			}else{ // 关闭tab页面
			    				window.parent.removeTabItemF('040302');
			    			}
						});
					}else{// 编辑
						$.ligerDialog.success('<view:LanguageTag key="common_save_succ_tip"/>', '<view:LanguageTag key="common_syntax_tip"/>',function(){
				        	window.location.href="<%=path%>/manager/authmgr/agentconf/list.jsp";
				     	});
					}
			    }else{
				 	FT.toAlert(msg.errorStr,msg.object,null);
			    }
			}
		});		  
	 }	
	 
	 // 设置下拉列表
	 function setInnerHTML() {
	 	document.getElementById('userformatDiv').innerHTML = 
			"<select id='userformat' name='agentConfInfo.userformat' class='select100'>"
				+"<option value='0' <c:if test='${agentConfInfo.userformat==0}'>selected</c:if>>user@ip</option>"
				+"<option value='1' <c:if test='${agentConfInfo.userformat==1}'>selected</c:if>>user</option>"
				+"<option value='2' <c:if test='${agentConfInfo.userformat==2}'>selected</c:if>>user@domain</option>"
			//	+"<option value='3' <c:if test='${agentConfInfo.userformat==3}'>selected</c:if>>domain"+"\\user</option>"
			+"</select>";
	 }
	
	//根据登录保护代理配置类型，设置用户名格式
	 function setUserformat(val){
	 	if(val == '' || val == null) {
	 		setInnerHTML();
	 	}else {
	 		if(val == 0) {
	 			setInnerHTML();
	 			$("#localDiv").show();
	 			$("#remoteDiv").show();
	 			$("#uacDiv").show();
	 		}
	 		if(val == 1) {
	 			$("#localDiv").hide();
	 			$("#remoteDiv").hide();
	 			$("#uacDiv").hide();
	 			$("select[name='agentConfInfo.localprotect'] option[value='0']").attr("selected","selected");
	 			$("select[name='agentConfInfo.remoteprotect'] option[value='0']").attr("selected","selected");
	 			$("select[name='agentConfInfo.uacprotect'] option[value='0']").attr("selected","selected");
	 			
	 			document.getElementById('userformatDiv').innerHTML = 
				"<select id='userformat' name='agentConfInfo.userformat' class='select100'>"
					+"<option value='0' <c:if test='${agentConfInfo.userformat==0}'>selected</c:if>>user@ip</option>"
					+"<option value='1' <c:if test='${agentConfInfo.userformat==1}'>selected</c:if>>user</option>"
				+"</select>";
	 		}
	 	}
	 	
	 }

	 //返回操作
	 function goBack() {
		var currentPage = $("#currentPage").val();
		window.location.href = '<%=path%>/manager/authmgr/agentconf/list.jsp?currentPage=' + currentPage;
	 }	
	
	</script>
</head>
<body style="overflow:auto; overflow-x:hidden">
<div id="background"  class="background"  style="display: none; "></div>
<div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
<input name="currentPage" id="currentPage" type="hidden" value="${param.cPage }" />
<form id="addForm" method="post" action="">
  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="topTableBg">
    <tr>
      <td width="98%"> 
        <span class="topTableBgText"> 
	        <c:if test="${not empty agentConfInfo.id}"><view:LanguageTag key="auth_conf_edit"/></c:if>
	        <c:if test="${empty agentConfInfo.id}"><view:LanguageTag key="auth_conf_add"/></c:if>
        </span>
      </td>
      <td width="2%" align="right">
      	 	<!--<a href="javascript:openHelpWin('<%=path%>/manager/help/confighelp.html#373','<%=path%>/manager/common/js/ligerUI/skins/Gray/css/dialog.css');">
      		<img src="<%=path%>/images/icon/help_1.png" width="16" height="16" hspace="5" title='<view:LanguageTag key="common_syntax_help"/>' align="absmiddle" />
      		</a>-->
      </td>
    </tr>
  </table>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnTable">
    <tr>
     <td valign="top">
	    <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="ulOnInsideTable">
	    <tr>
	      <td width="30%" align="right" ><view:LanguageTag key="auth_conf_name"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	      <td width="30%">
	          <input type="text" id="confname" name="agentConfInfo.confname" value="${agentConfInfo.confname}" onchange="checkName();" class="formCss100" />
	          <input type="hidden" id="confnameHid" value="${agentConfInfo.confname}" />
	      </td>
	      <td width="40%" class="divTipCss"><div id="confnameTip"></div></td>
	    </tr>
        <tr>
	      <td align="right"><view:LanguageTag key="auth_conf_agent_type"/><view:LanguageTag key="colon"/></td>
	      <td> 
	        <select id="type" name="agentConfInfo.type" onchange="setUserformat(this.value);" class="select100" >
			   <option value="0" <c:if test="${agentConfInfo.type==0}">selected</c:if>><view:LanguageTag key="auth_conf_win_login_pro"/></option>
			   <option value="1" <c:if test="${agentConfInfo.type==1}">selected</c:if>><view:LanguageTag key="auth_conf_linux_login_pro"/></option>
			</select>
	      </td>
	      <td class="divTipCss"><div id="typeTip"></div></td>
	    </tr>
        <tr>
	      <td align="right"><view:LanguageTag key="auth_conf_uname_format"/><view:LanguageTag key="colon"/></td>
	      <td> 
	      	<div id="userformatDiv"></div>
	      </td>
	      <td class="divTipCss"><div id="userformatTip"></div></td>
	    </tr>
        <tr id="localDiv">
	      <td align="right" valign="top"><view:LanguageTag key="auth_conf_local_protect"/><view:LanguageTag key="colon"/></td>
	      <td> 
	        <select id="localprotect" name="agentConfInfo.localprotect" class="select100" >
			   <option value="0" <c:if test="${agentConfInfo.localprotect==0}">selected</c:if>><view:LanguageTag key="auth_conf_no_protect"/></option>
			   <option value="1" <c:if test="${agentConfInfo.localprotect==1}">selected</c:if>><view:LanguageTag key="auth_conf_protect_local_account"/></option>
			   <option value="2" <c:if test="${agentConfInfo.localprotect==2}">selected</c:if>><view:LanguageTag key="auth_conf_protect_domain_account"/></option>
			   <option value="3" <c:if test="${agentConfInfo.localprotect==3}">selected</c:if>><view:LanguageTag key="auth_conf_pro_local_domain_account"/></option>
			</select>
	      </td>
	      <td class="divTipCss"><div id="localprotectTip"></div></td>
	    </tr>
        <tr id="remoteDiv">
	      <td align="right" valign="top"><view:LanguageTag key="auth_conf_remote_protect"/><view:LanguageTag key="colon"/></td>
	      <td> 
	        <select id="remoteprotect" name="agentConfInfo.remoteprotect" class="select100" >
			   <option value="0" <c:if test="${agentConfInfo.remoteprotect==0}">selected</c:if>><view:LanguageTag key="auth_conf_no_protect"/></option>
			   <option value="1" <c:if test="${agentConfInfo.remoteprotect==1}">selected</c:if>><view:LanguageTag key="auth_conf_protect_local_account"/></option>
			   <option value="2" <c:if test="${agentConfInfo.remoteprotect==2}">selected</c:if>><view:LanguageTag key="auth_conf_protect_domain_account"/></option>
			   <option value="3" <c:if test="${agentConfInfo.remoteprotect==3}">selected</c:if>><view:LanguageTag key="auth_conf_pro_local_domain_account"/></option>
			</select>
	      </td>
	      <td class="divTipCss"><div id="remoteprotectTip"></div></td>
	    </tr>
        <tr id="uacDiv">
	      <td align="right" valign="top"><view:LanguageTag key="auth_conf_uac_protect"/><view:LanguageTag key="colon"/></td>
	      <td> 
	        <select id="uacprotect" name="agentConfInfo.uacprotect" class="select100" >
			   <option value="0" <c:if test="${agentConfInfo.uacprotect==0}">selected</c:if>><view:LanguageTag key="auth_conf_no_protect"/></option>
			   <option value="1" <c:if test="${agentConfInfo.uacprotect==1}">selected</c:if>><view:LanguageTag key="auth_conf_protect_local_account"/></option>
			   <option value="2" <c:if test="${agentConfInfo.uacprotect==2}">selected</c:if>><view:LanguageTag key="auth_conf_protect_domain_account"/></option>
			   <option value="3" <c:if test="${agentConfInfo.uacprotect==3}">selected</c:if>><view:LanguageTag key="auth_conf_pro_local_domain_account"/></option>
			</select>
	      </td>
	      <td class="divTipCss"><div id="uacprotectTip"></div></td>
	    </tr>
		<tr>
	      <td align="right"><view:LanguageTag key="auth_conf_unbound_login"/><view:LanguageTag key="colon"/></td>
	      <td> 
	      	
	        <input type="radio" id="unboundlogin1" name="agentConfInfo.unboundlogin" value="1" <c:if test="${agentConfInfo.unboundlogin == 1 }">checked</c:if>/><view:LanguageTag key="common_syntax_yes"/> &nbsp;&nbsp;&nbsp;&nbsp;
	        <input type="radio" id="unboundlogin0" name="agentConfInfo.unboundlogin" value="0" <c:if test="${agentConfInfo.unboundlogin != 1 }">checked</c:if>/><view:LanguageTag key="common_syntax_no"/>
	      </td>
	      <td class="divTipCss"><div id="unboundloginTip"></div></td>
	    </tr>
	    <tr>
		   <td align="right"><view:LanguageTag key="common_syntax_desc"/><view:LanguageTag key="colon"/></td>
		   <td><textarea id="descp" name="agentConfInfo.descp" class="textarea100">${agentConfInfo.descp}</textarea></td>
		   <td class="divTipCss"><div id="descpTip"></div></td>
		</tr>
	    <tr>
	      <td>&nbsp;</td>
	      <td>
	      	 <input type="hidden" id="id" name="agentConfInfo.id" value="${agentConfInfo.id}" />
	      	 <a href="#" id="addBtn" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
	      	 <c:if test="${not empty agentConfInfo.id}"><a href="javascript:goBack();" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a></c:if>
	      </td>
	      <td></td>
	    </tr>
 	   </table>
    </td>
   </tr>
 </table>
</form>
</body>
</html>
