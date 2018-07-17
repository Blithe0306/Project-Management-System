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
    <title><view:LanguageTag key="common_menu_config_otp_server"/></title>
	<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
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
        btnClickEvent("#saveObj",cssurl);
        
 		checkForm();
	 }) 

    //
    function checkForm(){
       $.formValidator.initConfig({submitButtonID:"saveObj",
			onSuccess:function(){
			   save();
			},
			onError:function(){
				return false;
			}});
       $("#clearpolicy").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
	   $("#tunersleeptime").formValidator({onFocus:'<view:LanguageTag key="otpser_vd_cache_clean_show"/>',onCorrect:"OK"}).inputValidator({min:1,type:"number",onError:'<view:LanguageTag key="otpser_vd_cache_clean_err"/>'});	
	   $("#maxsize").formValidator({onFocus:'<view:LanguageTag key="otpser_vd_cache_maxsize_show"/>',onCorrect:"OK"}).inputValidator({min:1,type:"number",onError:'<view:LanguageTag key="otpser_vd_cache_clean_err"/>'});	
	   $("#expiretime").formValidator({onFocus:'<view:LanguageTag key="otpser_vd_cache_exp_time_show"/>',onCorrect:"OK"}).inputValidator({min:1,type:"number",onError:'<view:LanguageTag key="otpser_vd_cache_clean_err"/>'});	
       
       $("#clearpolicy").focus();
    }
	
	//保存OTP Server配置
	function save(){
	     $('#serverForm').ajaxSubmit({
			type:"post",
			async:false,
			url:"<%=path%>/manager/confinfo/config/otpServerAction!modify.action",
			dataType : "json",
			success:function(msg){
				var errorStr = msg.errorStr;
				$.ligerDialog.success(msg.object, '<view:LanguageTag key="common_syntax_tip"/>', function(){
			        window.location.reload();
			    });
			}
		 });   
	 }

	//-->
	</script>
  </head>
  
  <body>
  <form id="serverForm" method="post" action="">
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="topTableBg">
      <tr>
        <td width="98%">
        	<span class="topTableBgText"><view:LanguageTag key="common_menu_config_otp_server"/></span>
        </td>
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
             <tr>
               <td width="30%" align="right"><view:LanguageTag key="otpserconf_cache_clean_policy"/><view:LanguageTag key="colon"/></td>
               <td width="30%"><select id="clearpolicy" name="serverInfo.cacheclearpolicy" class="select100" >
			        <option value="0" <c:if test="${serverInfo.cacheclearpolicy == 0}">selected</c:if>>FIFO</option>
			        <option value="1" <c:if test="${serverInfo.cacheclearpolicy == 1}">selected</c:if>>LRU</option>
			        <option value="2" <c:if test="${serverInfo.cacheclearpolicy == 2}">selected</c:if>>LFU</option>
			       </select>
			   </td>
               <td width="40%" class="divTipCss"><div id ="clearpolicyTip"></div></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="otpserconf_cache_maxsize"/><view:LanguageTag key="colon"/></td>
               <td><input type="text" id="maxsize" name="serverInfo.cachemaxsize" class="formCss100" value="${serverInfo.cachemaxsize}"/></td>
               <td class="divTipCss"><div id ="maxsizeTip"></div></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="otpserconf_cache_tuner_sleeptime"/><view:LanguageTag key="colon"/></td>
               <td><input type="text" id="tunersleeptime" name="serverInfo.cachetunersleeptime" class="formCss100" value="${serverInfo.cachetunersleeptime}"/></td>
               <td class="divTipCss"><div id ="tunersleeptimeTip"></div></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="otpserconf_cache_expire_time"/><view:LanguageTag key="colon"/></td>
               <td><input type="text" id="expiretime" name="serverInfo.cacheexpiretime" class="formCss100" value="${serverInfo.cacheexpiretime}"/></td>
               <td class="divTipCss"><div id ="expiretimeTip"></div></td>
             </tr>
             
             <tr>
		       <td align="right">&nbsp;</td>
		       <td>
		           <a href="#" id="saveObj" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
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