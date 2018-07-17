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
		//绑定点击事件
		domClickEvent();
		//点击按钮弹出窗口，设置窗口样式，取消已绑定点击事件
		var cssurl = "<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"
        btnClickEvent("#saveBt",cssurl);
        
	
		checkInput();
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
	  
	   //应急口令
       $('#token_empin2otp').formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
//     $('#empin_otp_leneq').formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
	   
	   $("#epassdefvalidtime").formValidator({onFocus:'<view:LanguageTag key="emey_vd_ency_pwd_def_vtime_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:168,type:"number",onError:'<view:LanguageTag key="emey_vd_ency_pwd_def_vtime_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'});
	   $("#epassmaxvalidtime").formValidator({onFocus:'<view:LanguageTag key="emey_vd_ency_pwd_max_vtime_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:165535,type:"number",onError:'<view:LanguageTag key="emey_vd_ency_pwd_max_vtime_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'}).compareValidator({desID:"epassdefvalidtime",operateor:">=", dataType:"number", onError:'<view:LanguageTag key="emey_vd_ency_pwd_max_vtime_err_1"/>'});
			
	}
	
	// 保存数据
	function savaData(){
    	var url = "<%=path%>/manager/confinfo/config/tokenConfAction!modify.action";
	    var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
		$("#emeyForm").ajaxSubmit({
		   async:true,
		   dataType : "json",  
		   type:"POST", 
		   url : url,
		   data:{"oper" : "emeypin"},
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
	//-->
	</script>
  </head>
  
  <body>
  <div id="background"  class="background"  style="display: none; "></div>
  <div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
  <input id="contextPath" type="hidden" value="<%=path%>" />
  <form id="emeyForm" method="post" action="" name="emeyForm">
    <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="topTableBg">
      <tr>
        <td width="98%"><span class="topTableBgText"><view:LanguageTag key="common_menu_config_emey_pin"/></span></td>
        <td width="2%" align="right">
      	 <!--	<a href="javascript:openHelpWin('<%=path%>/manager/help/confighelp.html#387','<%=path%>/manager/common/js/ligerUI/skins/Gray/css/dialog.css');">
      		<img src="<%=path%>/images/icon/help_1.png" width="16" height="16" hspace="5" title='<view:LanguageTag key="common_syntax_help"/>' align="absmiddle" />
      		</a>-->
        </td>
      </tr>
    </table>  
   <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
      <tr>
        <td valign="top">
		    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		      <tr style="display:none">
		        <td width="30%" align="right"><view:LanguageTag key="emey_empin2otp"/><view:LanguageTag key="colon"/></td>
		        <td width="30%">
					<select id="token_empin2otp" name="tokenConfInfo.tokenempin2otp" class="select100">
	               		<option value="0" <c:if test="${tokenConfInfo.tokenempin2otp=='0'}">selected</c:if> ><view:LanguageTag key="emey_empin2otp_sel2"/></option>
	               		<option value="1" <c:if test="${tokenConfInfo.tokenempin2otp=='1'}">selected</c:if> ><view:LanguageTag key="emey_empin2otp_sel"/></option>
	              	</select>		        	
		        </td>
		        <td width="40%" class="divTipCss"><div id="token_empin2otpTip"></div></td>
		      </tr>
		        
	          <tr style="display:none">
	             <td align="right" height="20"><view:LanguageTag key="emey_empin_otp_len_eq"/><view:LanguageTag key="colon"/></td>
	             <td align="left" height="20">
	             	<input type="radio" id="empinotpleneq" name="tokenConfInfo.empinotpleneq" value="0"
               			<c:if test="${tokenConfInfo.empinotpleneq eq 0 }">checked</c:if> /><view:LanguageTag key="common_syntax_no"/> &nbsp;&nbsp;
                	<input type="radio" id="empinotpleneq1" name="tokenConfInfo.empinotpleneq" value="1" 
                		<c:if test="${tokenConfInfo.empinotpleneq eq 1 }">checked</c:if> /><view:LanguageTag key="common_syntax_yes"/>
	              </td>
	              <td class="divTipCss"><div id="empin_otp_leneqTip"></div></td>
	          </tr>
	          
		      <tr>
				<td align="right" width="30%"><view:LanguageTag key="emey_pass_def_validtime" /><span class="text_Hong_Se">*</span><view:LanguageTag key="colon" /></td>
				<td width="30%">
					<input type="text" id="epassdefvalidtime" name="tokenConfInfo.epassdefvalidtime" class="formCss100"
						value="${tokenConfInfo.epassdefvalidtime}" />
				</td>
				<td class="divTipCss" width="40%"><div id="epassdefvalidtimeTip"></div></td>
			 </tr>
			 <tr>
				<td align="right"><view:LanguageTag key="emey_pass_max_validtime" /><span class="text_Hong_Se">*</span><view:LanguageTag key="colon" /></td>
				<td>
					<input type="text" id="epassmaxvalidtime" name="tokenConfInfo.epassmaxvalidtime" class="formCss100"
						value="${tokenConfInfo.epassmaxvalidtime}" />
				</td>
				<td class="divTipCss"><div id="epassmaxvalidtimeTip"></div></td>
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