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
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
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
	$(function() {
		setRadioState();
		//点击按钮弹出窗口，设置窗口样式，取消已绑定点击事件
		var cssurl = "<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"
        btnClickEvent("#saveBt",cssurl);
		checkInput();
		$(":radio[name^='authConfInfo.propeapadbled']").click(function(e){
			if($(this).val() == 'n') {
				$("tr[id='lockedpeapTR']").hide();
			}else {
			 	$("tr[id='lockedpeapTR']").show();
			}
		});
	});
		 
	 //点击保存之前的校验
	function checkInput(){
	  $.formValidator.initConfig({submitButtonID:"saveBt", 
			onSuccess:function(){
			   savaData();
			},
			onError:function(){
				return false;
			}
	 });
	  
	  $(":radio[name='authConfInfo.propeapadbled']").formValidator({tipID:"propeapadbledTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:1,max:1,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});    
	  $(":radio[name='authConfInfo.propeaplocked']").formValidator({tipID:"propeaplockedTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:1,max:1,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});            
    }
	
	function setRadioState(){
    	var propeapadble = '${authConfInfo.propeapadbled}';
    	if(propeapadble == 'n'){
    		$("tr[id='lockedpeapTR']").hide();	
    	}else{
    		$("tr[id='lockedpeapTR']").show();
    	}
    }

	// 保存数据
	function savaData(){
	    // 如果提交PEAP开启选择的是否，将是否锁定用户默认为开启
		if($('input[name="authConfInfo.propeapadbled"]:checked').val() == "n"){
			$("input[name='authConfInfo.propeaplocked'][value='y']").attr("checked",true);
		}
    	var url = "<%=path%>/manager/confinfo/config/authConfAction!modify.action";
		var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
		$("#authpeapForm").ajaxSubmit({
		   async:true,
		   dataType : "json",  
		   type:"POST", 
		   url : url,
		   data:{"oper" : "initpeap"},
		   success:function(msg){
				if(msg.errorStr == 'success'){ 
				     $.ligerDialog.success(msg.object,'<view:LanguageTag key="common_syntax_tip"/>',function(){
				     	 window.location.reload();
				     });
				}else{
				     FT.toAlert(msg.errorStr,msg.object, null);
				}
				ajaxbg.hide();
		   }
	   });
	}
	</script>
  </head>
  
  <body>
  <body>
  <div id="background"  class="background"  style="display: none; "></div>
  <div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
  <input id="contextPath" type="hidden" value="<%=path%>" />
  <form id="authpeapForm" method="post" action="" name="authpeapForm">
    <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="topTableBg">
      <tr>
        <td width="98%"><span class="topTableBgText"><view:LanguageTag key="common_menu_config_peap"/></span></td>
        <td width="2%" align="right">
      	 	<!--<a href="javascript:openHelpWin('<%=path%>/manager/help/confighelp.html#387','<%=path%>/manager/common/js/ligerUI/skins/Gray/css/dialog.css');">
      		<img src="<%=path%>/images/icon/help_1.png" width="16" height="16" hspace="5" title='<view:LanguageTag key="common_syntax_help"/>' align="absmiddle" />
      		</a>-->
        </td>
      </tr>
    </table>  
   <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="ulOnInsideTable">
      <tr>
        <td valign="top">
		    <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="ulOnInsideTable">
				<tr>
					<td width="40%" height="20" align="right"><view:LanguageTag key="authconf_adbled_peap"/><view:LanguageTag key="colon" /></td>
					<td width="20%" height="20">
						<input type="radio" name="authConfInfo.propeapadbled" id="propeapadbled" value="y"
							<c:if test="${authConfInfo.propeapadbled =='y'}">checked</c:if> /><view:LanguageTag key="common_syntax_yes" />&nbsp;&nbsp;&nbsp;
						<input type="radio" name="authConfInfo.propeapadbled" id="propeapadbled1" value="n"
							<c:if test="${authConfInfo.propeapadbled =='n'}">checked</c:if> /><view:LanguageTag key="common_syntax_no" />
					</td>
					<td width="40%" class="divTipCss"><div id="propeapadbledTip"></div></td>
				</tr>
				
				<tr id="lockedpeapTR">
					<td align="right"><view:LanguageTag key="authconf_locked_peap"/><view:LanguageTag key="colon" /></td>
					<td>
						<input type="radio" name="authConfInfo.propeaplocked" id="propeaplocked" value="y"
							<c:if test="${authConfInfo.propeaplocked =='y'}">checked</c:if> /><view:LanguageTag key="common_syntax_yes" />&nbsp;&nbsp;&nbsp;
						<input type="radio" name="authConfInfo.propeaplocked" id="propeaplocked1" value="n"
							<c:if test="${authConfInfo.propeaplocked =='n'}">checked</c:if> /><view:LanguageTag key="common_syntax_no" />
					</td>
					<td class="divTipCss"><div id="propeaplockedTip"></div></td>
				</tr>
			    <tr>
			        <td align="right"></td>
			        <td><a href="#" id="saveBt" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a></td>
			        <td></td>
			    </tr> 
		    </table>
        </td>
       </tr>
      </table> 
   </form>
  </body>
</html>