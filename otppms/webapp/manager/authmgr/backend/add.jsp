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
		var backendname = '${backendInfo.backendname}';
		//验证授权服务器允许添加节点数
		if (backendname != '' && backendname != undefined) {
			addInit();
		} else {
			$.ajax({ 
				async:true,
				type:"POST",
				url:"<%=path%>/manager/authmgr/backend/backendAuth!checkBackNodes.action", 
				dataType:"json",
				success:function(msg){
					if(msg.errorStr == 'success'){
				     	addInit();
				    }else{
				     	$.ligerDialog.warn(msg.object, '<view:LanguageTag key="common_syntax_tip"/>',function(){
				        	window.parent.removeTabItemF('040202');
				     	});
				    }
				}
			});
		}
	});

	function addInit(){
		//绑定点击事件
		domClickEvent();
		//点击按钮弹出窗口，设置窗口样式，取消已绑定点击事件
		var cssurl = "<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"
        btnClickEvent("#addBtn",cssurl);
		var btype = $('#backendtype').val();
		if(btype == 1){
			$("tr[id='addomainTR']").hide(); 
			$("input[name='backendInfo.usernamerule']").attr("checked",true);
	        $("tr[id='authdomainTR']").show(); 
		}else{
			$("tr[id='addomainTR']").hide(); 
			$("tr[id='authdomainTR']").hide(); 
		}	
		
		var checkvalue = '${backendInfo.policy}';
	 	//初始化已选择的选项
	 	if (checkvalue != 0) {
	 		$('input[name="backendpolicy"]').each(function () {
			   var result = checkvalue & this.value;
			   if (result != 0) {
					$(this).attr("checked", true);
			   }
			});
	 	}

	 	if($("#sparehost").val() == 0){
	 		$("#sparehost").val('');
	 	}

	 	//校验信息
	 	checkInfo();
	 	
	 	//设置属性
	 	setValFun();

	 	if(btype == 0){
	 		$("#delimiter").unFormValidator(true); 
	 	}
	}

	//单击后端认证类型，设置端口
	function backTypeSel(obj){
		// 初始化
		$("#host").val('');  // 后端服务器IP地址
		$("#sparehost").val(''); // 备后端服务器IP地址
		$("input[name='backendpolicy']").each(function() {    // 转发策略
            $(this).attr("checked", false);
        });
		$("#priority").val('');  //优先级
		$("#retrycnt").val(''); // 连接超时的重试次数
		$("#timeout").val(30); // 连接超时时间
		if(obj == '0') {
			$("#port").val('1812');
			$("#delimiter").val('');
        	$("tr[id='pubkeyTR']").show(); 
        	$("tr[id='confpubkeyTR']").show(); 
        	$("tr[id='addomainTR']").hide(); 
        	$("tr[id='authdomainTR']").hide(); 
        	$("#pubkey").unFormValidator(false); 
        	$("#confpubkey").unFormValidator(false); 
        	$("#delimiter").unFormValidator(true); 
		}else {
		 	$("#port").val('389');
		 	$("#pubkey").val(''); // 共享密钥
        	$("tr[id='pubkeyTR']").hide(); 
        	$("tr[id='confpubkeyTR']").hide(); 
        	$("tr[id='addomainTR']").hide(); 
        	$("tr[id='authdomainTR']").show(); 
        	$("input[name='backendInfo.usernamerule']").attr("checked",true);
        	$("#pubkey").unFormValidator(true); 
        	$("#confpubkey").unFormValidator(true); 
        	$("#delimiter").unFormValidator(false); 
		}
	}
	
	function checkInfo() {
		$.formValidator.initConfig({submitButtonID:"addBtn", 
			onSuccess:function(){
				addObj();
			},
			onError:function(){
				return false;
			}});
			$("#backendname").formValidator({onFocus:'<view:LanguageTag key="auth_bk_vd_name"/>',onCorrect:"OK"}).inputValidator({min:1,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="auth_bk_vd_name_err"/>'},onError:'<view:LanguageTag key="auth_bk_vd_name"/>'});
			$("#host").formValidator({onFocus:'<view:LanguageTag key="auth_bk_vd_host"/>',onCorrect:"OK"}).inputValidator({min:1,onError:'<view:LanguageTag key="auth_bk_vd_host_null"/>'}).regexValidator({regExp:"ip4",dataType:"enum",onError:'<view:LanguageTag key="auth_bk_vd_host_err"/>'}).
			functionValidator({
				fun:function(val){ 
					 var showtip = checkAttr();
					 if (showtip != 'success') {
					 	return showtip;
					 }else {
					 	return true;
					 }
	              }
	        });
			$("#sparehost").formValidator({empty:true,onFocus:'<view:LanguageTag key="auth_bk_vd_sparehost"/>',onCorrect:"OK",onEmpty:"OK"}).regexValidator({regExp:"ip4",dataType:"enum",onError:'<view:LanguageTag key="auth_bk_vd_sparehost_err"/>'}).compareValidator({desID:"host",operateor:"!=",onError:'<view:LanguageTag key="auth_bk_vd_same"/>'});
			$("#port").formValidator({onFocus:'<view:LanguageTag key="auth_bk_vd_port"/>',onCorrect:"OK"}).regexValidator({regExp:"num",dataType:"enum",onError:'<view:LanguageTag key="auth_bk_vd_port_err"/>'}).
			functionValidator({
				fun:function(val){ 
					 var showtip = checkAttr();
					 if (showtip != 'success') {
					 	return showtip;
					 }else {
					 	return true;
					 }
	              }
	        });
			$("#priority").formValidator({empty:true,onFocus:'<view:LanguageTag key="auth_bk_vd_priority"/>',onCorrect:"OK",onEmpty:"OK"}).
			functionValidator({
				fun:function(val){ 
					 if(val>1024 || val<1) return '<view:LanguageTag key="auth_bk_vd_priority"/>';
					 if(!checkNumber(val)) return '<view:LanguageTag key="auth_bk_vd_priority_err"/>';
	                 return true;
	              }
	        });
	        $("input[name='backendpolicy']").formValidator({tipID:"policyTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
			
			$("#domainid").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'}).
			functionValidator({
				fun:function(val){ 
					 var showtip = checkAttr();
					 if (showtip != 'success') {
					 	return showtip;
					 }else {
					 	return true;
					 }
	              }
	        });

	        var valName = $("#delimiter").val();
			$("#delimiter").formValidator({onFocus:'<view:LanguageTag key="auth_bk_ad_domain_name"/>',onCorrect:"OK"}).inputValidator({min:1,max:32,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="auth_bk_ad_domain_name_err1"/>'}, onError: '<view:LanguageTag key="auth_bk_ad_domain_name_err2"/>'}).functionValidator({
	       		fun:function(valName){
			    if(!letter_u_num_english(valName)){
			       return '<view:LanguageTag key="auth_bk_ad_domain_name_err"/>';
			    }
			    return true;
		 }});
//			$("#basedn").formValidator({empty:true,onFocus:'<view:LanguageTag key="auth_bk_vd_ldap_catalog"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="auth_bk_vd_ldap_catalog_err"/>'}, onError: '<view:LanguageTag key="auth_bk_vd_ldap_catalog_err_1"/>'});
//			$("#filter").formValidator({empty:true,onFocus:'<view:LanguageTag key="auth_bk_vd_filter"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:64, onError: '<view:LanguageTag key="auth_bk_vd_filter_err"/>'});
		 
		 $("#pubkey").formValidator({onFocus:'<view:LanguageTag key="auth_bk_vd_pubkey"/>',onCorrect:"OK"}).inputValidator({min:4,max:32,onError:'<view:LanguageTag key="auth_bk_vd_pubkey"/>'});
		 $("#confpubkey").formValidator({onFocus:'<view:LanguageTag key="agent_vd_confshared_key_show"/>',onCorrect:"OK"}).inputValidator({min:4,max:32, onError:'<view:LanguageTag key="agent_vd_shared_key_err"/>'}).compareValidator({desID:"pubkey",operateor:"=",onError:'<view:LanguageTag key="agent_vd_shared_key_not_same"/>'}); 
		
		 $("input[name='backendpolicy']").formValidator({tipID:"policyTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:1,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
	     $('#timeout').formValidator({empty:true,onFocus:'<view:LanguageTag key="auth_bk_vd_timeout"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({min:1, max:180, type:"number",onError:'<view:LanguageTag key="auth_bk_vd_timeout_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'});
	     $('#retrycnt').formValidator({empty:true,onFocus:'<view:LanguageTag key="auth_bk_vd_retrycnt"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({min:1, max:5, type:"number",onError:'<view:LanguageTag key="auth_bk_vd_retrycnt_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'});
	}
	
	function checkAttr(){
		var reval = 'success';	
		var host = $("#host").val();
		var hostHid = $("#hostHid").val();
		var port = $("#port").val();
		var portHid = $("#portHid").val();
		var domainid = $("#domainid").val();
		var domainidHid = $("#domainidHid").val();
		var url_ = "<%=path%>/manager/authmgr/backend/backendAuth!checkUKIsExist.action";		
		if (host != hostHid || port != portHid || domainid != domainidHid) {
			$.ajax({
				type: "POST",
				url: url_,
				async: false,
				data: {"host" : host, "port" : port, "domainid" : domainid},
				dataType: "json",	    
				success: function(msg){
				   var errorStr = msg.errorStr;
	               if(errorStr == 'error'){
		              reval = '<view:LanguageTag key="auth_bk_ip_port_domainid_exist"/>';
				   }else {
				   	  reval = 'success';
				   }
				}
			});	
		}
		return reval;
	 }
	
	//验证配置名称
	function checkName() {
		var id = $("#id").val();	
		var backendnameHid = $("#backendnameHid").val();	
		var backendname = $("#backendname").val();
			
		if(backendname != backendnameHid) {
			validateBackendname();
		}else {
			if (id == null || "" == id) {
				validateBackendname();
			} else {
				$("#backendname").formValidator({onFocus:'<view:LanguageTag key="auth_bk_vd_name"/>',onCorrect:"OK"}).inputValidator({min:1,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="auth_bk_vd_name_err"/>'},onError:'<view:LanguageTag key="auth_bk_vd_name"/>'});
			}
		}
	}
	//后端认证名称是否存在
	function validateBackendname() {
	 	$("#backendname")
		   .ajaxValidator({
				dataType:"html",
				async:true,
				url:"<%=path%>/manager/authmgr/backend/backendAuth!checkBackendName.action",
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
	 
	//获取选择转发策略值
	function getCheckedVal() {
		var checkedVal=0;
		$('input[name="backendpolicy"]').each(function () {
	       if ($(this).attr('checked') == true) {
	       	  var val = parseInt($(this).attr('value'),10);
	          checkedVal += val;
	       }
	    });
	    $("#policy").val(checkedVal);
	}
	
	//添加、编辑后端认证
	function addObj(){
		getCheckedVal();
		
		var host = $("#host").val();
		var hostHid = $("#hostHid").val();
		var port = $("#port").val();
		var portHid = $("#portHid").val();
		var domainid = $("#domainid").val();
		var domainidHid = $("#domainidHid").val();
		var url_ = "<%=path%>/manager/authmgr/backend/backendAuth!checkUKIsExist.action";		
		if (host != hostHid || port != portHid || domainid != domainidHid) {
			$.ajax({
				type: "POST",
				url: url_,
				async: false,
				data: {"host" : host, "port" : port, "domainid" : domainid},
				dataType: "json",	    
				success: function(msg){
				   var errorStr = msg.errorStr;
	               if(errorStr == 'error'){
					  FT.toAlert('warn','<view:LanguageTag key="auth_bk_ip_port_domainid_exist"/>',null);
		              return false;
				   }else {
					  saveForm();
				   }
				}
			});	
		}else {
			saveForm();
		}
	 }
	 //保存操作
	 function saveForm() {
	 	var id = $("#id").val();
	 	var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
	 	var isAdd = true;
	 	if($("#usernamerule").attr("checked") == false){
			$('#usernamerule').val(0);
	 	}else{
	 		$('#usernamerule').val(1);
	 	}
		var urlTo = '<%=path%>/manager/authmgr/backend/backendAuth!add.action';
		if("" != id){
			urlTo = '<%=path%>/manager/authmgr/backend/backendAuth!modify.action';
			isAdd = false;
		}
		$("#addForm").ajaxSubmit({
			async : true,  
			type : "POST", 
			url : urlTo,
			dataType : "json",
			success : function(msg){
				ajaxbg.hide();
				if(msg.errorStr=='error' || msg.errorStr=='warn'){
					FT.toAlert(msg.errorStr, msg.object, null);
				}else{
					$.ligerDialog.success(msg.object, '<view:LanguageTag key="common_syntax_tip"/>',function(){
						if(isAdd){
							window.parent.removeTabItemF('040202');
						}else{
							var currentPage = $("#currentPage").val();
							window.location.href = '<%=path%>/manager/authmgr/backend/list.jsp?currentPage=' + currentPage;
						}
					});
				}
			}
		});		  
	 }	
	 
	 function setValFun() {
	 	var btype = $('#backendtype').val();
 		if (btype == 0) {
        	$("tr[id='pubkeyTR']").show(); 
        	$("tr[id='confpubkeyTR']").show(); 
        	$("#pubkey").unFormValidator(false); 
        	$("#confpubkey").unFormValidator(false); 
 		}
 		if (btype == 1) {
        	$("tr[id='pubkeyTR']").hide(); 
        	$("tr[id='confpubkeyTR']").hide(); 
        	$("#pubkey").unFormValidator(true); 
        	$("#confpubkey").unFormValidator(true); 
 		}
	 }

	function selectBkDomain(){
		if($("#usernamerule").attr("checked") == true){
			$('#delimiter').val('');
			$("tr[id='authdomainTR']").show(); 
		}else{
			$('#delimiter').val('');
			$("tr[id='authdomainTR']").hide(); 
		}
	}
	 
	 //返回操作
	 function goBack() {
		var currentPage = $("#currentPage").val();
		window.location.href = '<%=path%>/manager/authmgr/backend/list.jsp?currentPage=' + currentPage;
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
	        <c:if test="${not empty backendInfo.id}"><view:LanguageTag key="auth_bk_edit"/></c:if>
	        <c:if test="${empty backendInfo.id}"><view:LanguageTag key="auth_bk_add"/></c:if>
        </span>
      </td>
      <td width="2%" align="right">
      	 	<!--<a href="javascript:openHelpWin('<%=path%>/manager/help/confighelp.html#374','<%=path%>/manager/common/js/ligerUI/skins/Gray/css/dialog.css');">
      		<img src="<%=path%>/images/icon/help_1.png" width="16" height="16" hspace="5" title='<view:LanguageTag key="common_syntax_help"/>' align="absmiddle" />
      		</a>-->
      </td>
    </tr>
  </table>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnTable">
    <tr>
     <td valign="top">
	    <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="ulOnInsideTable">
	    <input type="hidden" id="enabled" name="backendInfo.enabled" value="${backendInfo.enabled}"  />
	    <tr>
	      <td width="30%" align="right" ><view:LanguageTag key="auth_bk_name"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	      <td width="30%">
	          <input type="text" id="backendname" name="backendInfo.backendname" value="${backendInfo.backendname}" onchange="checkName();" class="formCss100" />
	          <input type="hidden" id="backendnameHid" value="${backendInfo.backendname}" />
	      </td>
	      <td width="40%" class="divTipCss"><div id="backendnameTip"></div></td>
	    </tr>
	    <tr>
         <td align="right"><view:LanguageTag key="auth_bk_type"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
         <td> 
		  <select id="backendtype" name="backendInfo.backendtype" class="select100" onchange="backTypeSel(this.value)">
		    <view:BackendTypeTag dataSrc="${backendInfo.backendtype}"></view:BackendTypeTag>
		  </select>
		 </td>
         <td class="divTipCss"><div id="backendtypeTip"></div></td>
        </tr>
	    <tr>
           <td align="right"><view:LanguageTag key="auth_bk_host"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
           <td>
           		<input type="text" id="host" name="backendInfo.host" value="${backendInfo.host}" class="formCss100" />
           		<input type="hidden" id="hostHid" value="${backendInfo.host}"  />
           </td>
           <td class="divTipCss"><div id="hostTip"></div></td>
        </tr>
        <tr>
           <td align="right"><view:LanguageTag key="auth_bk_sparehost"/><view:LanguageTag key="colon"/></td>
           <td>
           		<input type="text" id="sparehost" name="backendInfo.sparehost" value="${backendInfo.sparehost}" class="formCss100" />
           		<input type="hidden" id="sparehostHid" value="${backendInfo.sparehost}"  />
           </td>
           <td class="divTipCss"><div id="sparehostTip"></div></td>
        </tr>
	    <tr>
           <td align="right"><view:LanguageTag key="auth_bk_port"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
           <td>
           		<input type="text" id="port" name="backendInfo.port" value="${empty backendInfo.port ? "1812" : backendInfo.port }" class="formCss100" />
           		<input type="hidden" id="portHid" value="${backendInfo.port}" />
           </td>
           <td class="divTipCss"><div id="portTip"></div></td>
        </tr>
	    <tr>
           <td align="right"><view:LanguageTag key="auth_bk_policy"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
           <td>
		        <input type="checkbox" id="policy1" name="backendpolicy" value="1" />&nbsp;<view:LanguageTag key="auth_bk_local_user_not_exist_unbound"/><br/>
		  <!--  <input type="checkbox" id="policy2" name="backendpolicy" value="2" />&nbsp;<view:LanguageTag key="auth_bk_local_user_exist_unbound_tk"/><br/> -->
		  <!--  <input type="checkbox" id="policy3" name="backendpolicy" value="4" />&nbsp;<view:LanguageTag key="auth_bk_local_auth_err"/><br/> -->
		        <input type="checkbox" id="policy4" name="backendpolicy" value="8" />&nbsp;<view:LanguageTag key="auth_bk_local_auth_succ"/>
           </td>
           <td class="divTipCss"><div id="policyTip"></div></td>
        </tr>
	    <tr style="display:none">
           <td align="right"><view:LanguageTag key="auth_bk_domain_name"/><view:LanguageTag key="colon"/></td>
           <td>
           		<input type="hidden" id="domainidHid" value="${backendInfo.domainid}" />
           		<select id="domainid"  name="backendInfo.domainid" class="select100">
			      <view:DomainTag dataSrc="${backendInfo.domainid}" index1Lang="" index1Val="1"  />
		    	</select>	
           </td>
           <td class="divTipCss"><div id="domainidTip"></div></td>
        </tr>
        <!-- 
	    <tr>
           <td align="right"><view:LanguageTag key="auth_bk_priority"/><view:LanguageTag key="colon"/></td>
           <td><input type="text" id="priority" name="backendInfo.priority" value="${backendInfo.priority == 0 ? '' : backendInfo.priority}" class="formCss100" /></td>
           <td class="divTipCss"><div id="priorityTip"></div></td>
        </tr>
         -->
        <!-- 
	    <tr id="basednTR">
           <td align="right"><view:LanguageTag key="auth_bk_ldap_catalog"/><view:LanguageTag key="colon"/></td>
           <td><input type="text" id="basedn" name="backendInfo.basedn" value="${backendInfo.basedn}" class="formCss100" /></td>
           <td class="divTipCss"><div id="basednTip"></div></td>
        </tr>
	    <tr id="filterTR">
           <td align="right"><view:LanguageTag key="auth_bk_filter"/><view:LanguageTag key="colon"/></td>
           <td><input type="text" id="filter" name="backendInfo.filter" value="${empty backendInfo.filter ? "(&(objectCategory=person)(objectClass=user))" : backendInfo.filter}" class="formCss100" /></td>
           <td class="divTipCss"><div id="filterTip"></div></td>
        </tr>
         -->
	    <tr id="pubkeyTR">
           <td align="right"><view:LanguageTag key="auth_bk_pubkey"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
           <td><input onpaste="return false" type="password" id="pubkey" name="backendInfo.pubkey" value="${backendInfo.pubkey}" class="formCss100" /></td>
           <td class="divTipCss"><div id="pubkeyTip"></div></td>
        </tr>
	    <tr id="confpubkeyTR">
           <td align="right"><view:LanguageTag key="auth_agent_conf_shared_key"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
           <td><input onpaste="return false" type="password" id="confpubkey" value="${backendInfo.pubkey}" class="formCss100" /></td>
           <td class="divTipCss"><div id="confpubkeyTip"></div></td>
        </tr>
        
        
        <tr id="addomainTR">
	      <td align="right"><view:LanguageTag key="auth_bk_specify_AD_domain"/><view:LanguageTag key="colon"/></td>
	      <td><input type="checkbox" id="usernamerule" name="backendInfo.usernamerule" value="1" onClick="selectBkDomain();"/></td>
	    </tr>
        <tr id="authdomainTR">
           <td align="right"><view:LanguageTag key="auth_bk_domain"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
           <td><input type="text" id="delimiter" name="backendInfo.delimiter" value="${backendInfo.delimiter}" class="formCss100" /></td>
           <td class="divTipCss"><div id="delimiterTip"></div></td>
        </tr>
        <tr>
           <td align="right"><view:LanguageTag key="auth_bk_timeout"/><view:LanguageTag key="colon"/></td>
           <td>
           	<c:if test="${empty backendInfo.id}">
           		<input type="text" id="timeout" name="backendInfo.timeout" value="${empty backendInfo.timeout ? '30' : backendInfo.timeout}" class="formCss100" />
           	</c:if>
           	<c:if test="${not empty backendInfo.id}">
           		<input type="text" id="timeout" name="backendInfo.timeout" value="${backendInfo.timeout==0 ? '' : backendInfo.timeout}" class="formCss100" />
           	</c:if>
           	</td>
           <td class="divTipCss"><div id="timeoutTip"></div></td>
        </tr>
        <tr>
           <td align="right"><view:LanguageTag key="auth_bk_retrycnt"/><view:LanguageTag key="colon"/></td>
           <td><input type="text" id="retrycnt" name="backendInfo.retrycnt" value="${backendInfo.retrycnt==0 ? '' : backendInfo.retrycnt}" class="formCss100" /></td>
           <td class="divTipCss"><div id="retrycntTip"></div></td>
        </tr>
	    <tr>
	      <td>&nbsp;</td>
	      <td>
	      	 <input type="hidden" id="id" name="backendInfo.id" value="${backendInfo.id}" />
	      	 <input type="hidden" id="policy" name="backendInfo.policy" value="" />
	      	 <a href="#" id="addBtn" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
	      	 <c:if test="${not empty backendInfo.id }">
	      		 <a href="javascript:goBack();" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a>
	      	 </c:if>	 
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
