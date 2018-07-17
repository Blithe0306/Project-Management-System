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
    <title><view:LanguageTag key="common_menu_config_portal"/></title>
	<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css" />
	<link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css" />
	<link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" id="openwincss" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"/>
 
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
	<!--
	 $(document).ready(function(){
	 
	 	//绑定点击事件
		domClickEvent();
		//点击按钮弹出窗口，设置窗口样式，取消已绑定点击事件
		var cssurl = "<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"
        btnClickEvent("#savePortal",cssurl);
	 	
	 	var checkedStr = '${portalInfo.openfunctionconfig}';
	 	//默认选中已选择的选项
	 	if (checkedStr != null || "" != checkedStr) {
	 		var checkedAttr = checkedStr.split(",");
	 		for (var i=0; i<checkedAttr.length; i++) {
	 			var checkedValue = checkedAttr[i];
	 			$('input[name="openfunction"]').each(function () {
			       if (this.value == checkedValue) {
			          $(this).attr("checked", true);
			       }
			    });
	 		}
	 		$("#checkedStr").val(checkedStr);
	 	}
	 	checkForm();
	 	
	 	var isenabled = '${portalInfo.selfservice}';
		if(isenabled =='0') {
			$("tr[id='openfunTR']").hide();
		}else {
			$("tr[id='openfunTR']").show();
		}
	 	
	 	 // 是否启用用户门户
		$(":radio[name^='portalInfo.selfservice']").click(function(e){
			if($(this).val() === '1') { 
				$("tr[id='openfunTR']").show();
			}else {
			 	$("tr[id='openfunTR']").hide();
			}
		});	
	 }) 

    //跳转到认证服务器选项
    function checkForm(){
       $.formValidator.initConfig({submitButtonID:"savePortal",
			onSuccess:function(){
			   savePortal();
			},
			onError:function(){
				return false;
			}});
       $(":radio[name='portalInfo.selfservice']").formValidator({tipID:"selfserviceTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:1,max:1,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
       $("#pwdemailactiveexpire").formValidator({onFocus:'<view:LanguageTag key="portal_vd_pwd_email_exp_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:168, type:"number", onError: '<view:LanguageTag key="portal_vd_pwd_email_exp_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'});
    }
	
	//保存用户门户配置
	function savePortal(){
		getCheckedStr();
		
		var isself = $("input[name='portalInfo.selfservice']:checked").val(); 
		if (isself == 1) {
			var openfun = $("#openfunctionconfig").val();
			if (openfun.indexOf('1019') >= 0) {
				$.ligerDialog.confirm('<view:LanguageTag key="portal_save_open_fun_tip"/>','<view:LanguageTag key="common_syntax_confirm"/>',function(sel) {
					if(sel) {
						saveDate();
					}
				});
			}else {
				saveDate()
			}
		}else {
			saveDate()
		}
	 }
	 
	 //保存开启功能
	 function saveDate() {
	 	$('#portalForm').ajaxSubmit({
			type:"post",
			async:false,
			url:"<%=path%>/manager/confinfo/config/portal!modify.action",
			dataType : "json",
			data:{"oper" : "conf"},
			success:function(msg){
			    if(msg.errorStr == 'success'){	            			
			       $.ligerDialog.success(msg.object, '<view:LanguageTag key="common_syntax_tip"/>', function(){
			            window.location.reload();
			       });
			    }else {
			       FT.toAlert(msg.errorStr, result.object, null);
			    }
			}
		});   
	 }
	
	//获取选择的功能字符串
	function getCheckedStr() {
		var checkedStr = "";
		$('input[name="openfunction"]').each(function () {
	       if ($(this).attr('checked') == true) {
	          checkedStr += $(this).attr('value')+",";
	       }
	    });
	    
	    if (checkedStr != null || "" != checkedStr) {
	    	checkedStr = checkedStr.substring(0, checkedStr.length - 1);
	    }
	    $("#openfunctionconfig").val(checkedStr);
	}
	 
	 //全选、取消全选
	function allCheckOper(val){
		if($("#allCheck").attr("checked") == true){
			$('input[name="openfunction"]').each(function() {
	            $(this).attr("checked", true);
	        });
        }else{            
		    $("input[name='openfunction']").each(function() {
		        $(this).attr("checked", false);
		    });
	    }
	    
	}
	//重置
	function resetCheckOper(){
		
		if($("#allCheck").attr("checked") == true){
			$("#allCheck").attr("checked", false);
		}
		allCheckOper(1);
		var checkedStr = $("#checkedStr").val();
		var checkedAttr = checkedStr.split(",");
	 	for (var i=0; i<checkedAttr.length; i++) {
	 		var checkedValue = checkedAttr[i];
	 		$('input[name="openfunction"]').each(function () {
			   if (this.value == checkedValue) {
			      $(this).attr("checked", true);
			   }
			});
	 	}
	}

	//-->
	</script>
  </head>
  
  <body>
  <form id="portalForm" method="post" action="" >
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="topTableBg">
      <tr>
        <td width="98%"><span class="topTableBgText"><view:LanguageTag key="common_menu_config_portal"/></span></td>
        <td width="2%" align="right">
      	 <!--	<a href="javascript:openHelpWin('<%=path%>/manager/help/confighelp.html#387','<%=path%>/manager/common/js/ligerUI/skins/Gray/css/dialog.css');">
      		<img src="<%=path%>/images/icon/help_1.png" width="16" height="16" hspace="5" title='<view:LanguageTag key="common_syntax_help"/>' align="absmiddle" />
      		</a>-->
        </td>
      </tr>
     </table>
     <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td valign="top">
		     <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		      <tr>
		         <td width="30%" align="right"><view:LanguageTag key="portal_self_service_enable"/><view:LanguageTag key="colon"/></td>
		         <td width="40%">
		           <input type="radio" name="portalInfo.selfservice" id="selfservice"  value="1"
				      <c:if test="${portalInfo.selfservice =='1'}">checked</c:if>/> <view:LanguageTag key="common_syntax_yes"/>&nbsp;&nbsp;&nbsp;
				   <input type="radio" name="portalInfo.selfservice" id="selfservice1" value="0"
				      <c:if test="${portalInfo.selfservice =='0'}">checked</c:if>/> <view:LanguageTag key="common_syntax_no"/> 
		         </td>
		         <td width="30%" class="divTipCss"><div id ="selfserviceTip"></div></td>
		      </tr>

		      <tr id="openfunTR">
		        <td align="right" valign="top"><view:LanguageTag key="portal_open_fun_config"/><view:LanguageTag key="colon"/></td>
		        <td>
		        	<table width="95%" height="100%">
		        		<tr>
		        			<td width="50%"><input type="checkbox" id="allCheck" name="allCheck" onClick="allCheckOper(0);" value="checkbox" />&nbsp;<view:LanguageTag key="portal_select_all"/></td>
		        			<td width="50%"></td>
		        		</tr>
		        		<tr>
		        			<td><input type="checkbox" id="function01" name="openfunction" value="1001" />&nbsp;<view:LanguageTag key="portal_bind_user_tkn"/></td>
		        			<td><input type="checkbox" id="function02" name="openfunction" value="1002" />&nbsp;<view:LanguageTag key="portal_modify_pin"/></td>
		        		</tr>
		        		<tr>
		        			<td><input type="checkbox" id="function03" name="openfunction" value="1003" />&nbsp;<view:LanguageTag key="portal_replace_tkn"/></td>
		        			<td><input type="checkbox" id="function04" name="openfunction" value="1004" />&nbsp;<view:LanguageTag key="portal_unlock_tkn"/></td>
		        		</tr>
		        		<tr>
		        			<td><input type="checkbox" id="function05" name="openfunction" value="1005" />&nbsp;<view:LanguageTag key="portal_tkn_activation"/></td>
		        			<td><input type="checkbox" id="function06" name="openfunction" value="1006" />&nbsp;<view:LanguageTag key="portal_tkn_synch"/></td>
		        		</tr>
		        		<tr>
		        			<td><input type="checkbox" id="function07" name="openfunction" value="1007" />&nbsp;<view:LanguageTag key="portal_tkn_losed"/></td>
		        			<td><input type="checkbox" id="function08" name="openfunction" value="1008" />&nbsp;<view:LanguageTag key="portal_tkn_unlosed"/></td>
		        		</tr>
		        		<tr>
		        			<td><input type="checkbox" id="function09" name="openfunction" value="1009" />&nbsp;<view:LanguageTag key="portal_get_an_unlock_code"/></td>
		        			<td><input type="checkbox" id="function10" name="openfunction" value="1010" />&nbsp;<view:LanguageTag key="portal_get_two_unlockcode"/></td>
		        		</tr>
		        		<tr>
		        			<td><input type="checkbox" id="function11" name="openfunction" value="1011" />&nbsp;<view:LanguageTag key="common_menu_tkn_mobile_dist"/></td>
		        			<td><input type="checkbox" id="function12" name="openfunction" value="1012" />&nbsp;<view:LanguageTag key="portal_self_binding_tel_tk"/></td>
		        		</tr>
		        		
		        		<tr>
		        			<td><input type="checkbox" id="function13" name="openfunction" value="1013" />&nbsp;<view:LanguageTag key="portal_sms_tkn_dist"/></td>
		        			<td><input type="checkbox" id="function14" name="openfunction" value="1014" />&nbsp;<view:LanguageTag key="portal_self_binding_sms_tk"/></td>
		        		</tr>
		        		
		        		<tr>
		        			<td><input type="checkbox" id="function15" name="openfunction" value="1015" />&nbsp;<view:LanguageTag key="portal_software_tkn_dist"/></td>
		        			<td><input type="checkbox" id="function16" name="openfunction" value="1016" />&nbsp;<view:LanguageTag key="portal_self_binding_soft_tk"/></td>
		        		</tr>
		        		<tr>
		        			<td><input type="checkbox" id="function17" name="openfunction" value="1017" />&nbsp;<view:LanguageTag key="portal_tkn_auth_test"/></td>
		        			<td><input type="checkbox" id="function18" name="openfunction" value="1018" />&nbsp;<view:LanguageTag key="portal_trans_sign_auth_test"/></td>
		        		</tr>
		        		<tr>
		        			<td><input type="checkbox" id="function19" name="openfunction" value="1019" />&nbsp;<view:LanguageTag key="portal_edit_uinfo"/></td>
		        			<td></td>
		        		</tr>

		        		
		        	</table>
		        </td>
		        <td><div id="openfunctionconfigTip" style="width:100%"></div></td>
		      </tr>
		      
		      
		      <tr>
		         <td align="right"><view:LanguageTag key="portal_pwd_email_expire"/><view:LanguageTag key="colon"/></td>
		         <td> 
		           <input id="pwdemailactiveexpire" name="portalInfo.pwdemailactiveexpire" type="text"  value="${portalInfo.pwdemailactiveexpire}" class="form-text" />    
		         </td>
		         <td class="divTipCss"><div id ="pwdemailactiveexpireTip"></div></td>
		      </tr>
		      
		      <tr>
				<td align="right">&nbsp;</td>
				<td>
				   <input type="hidden" id="checkedStr" value="" />
				   <input type="hidden" name="portalInfo.openfunctionconfig" id="openfunctionconfig" />
				   <a href="#" id="savePortal" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
				   <a href="javascript:resetCheckOper();" id="resetChecked" class="button"><span><view:LanguageTag key="portal_reselect"/></span></a>
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