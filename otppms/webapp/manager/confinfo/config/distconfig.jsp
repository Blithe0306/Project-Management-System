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
    <title><view:LanguageTag key="common_menu_config_dist"/></title>
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
		 
	//选择分发协议时调用
     function orgProtocol(pvalue){
      var p = document.getElementById("protocol");
       if(pvalue == 1){
          p.value = "http";
       }else if(pvalue == 2){
          p.value = "https";
       }else if(pvalue == 3){
          p.value = "http";
       }
	 }
	 
	 //点击保存之前的校验
	 function checkInput(){
	  $.formValidator.initConfig({submitButtonID:"saveBt", 
			onSuccess:function(){
			   savaData();
			},
			onError:function(){
				return false;
			}});
			
 			$("#apperiod").formValidator({onFocus:'<view:LanguageTag key="distconf_vd_ap_period_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:7,type:"number",onError: '<view:LanguageTag key="distconf_vd_ap_period_err"/>'});
			$("#apretry").formValidator({onFocus:'<view:LanguageTag key="distconf_vd_ap_retry_show"/>',onCorrect:"OK"}).inputValidator({min:2,max:20,type:"number",onError:'<view:LanguageTag key="distconf_vd_ap_retry_err"/>'});
			$("#siteenabled").formValidator({tipID:"sexTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:1,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
			$("#defultap").formValidator({onFocus:'<view:LanguageTag key="distconf_vd_def_ap_err"/>',onCorrect:"OK"}).regexValidator({regExp:"^[0-9]{4,16}$",onError:'<view:LanguageTag key="distconf_vd_def_ap_err"/>'});
			$("#provsitetype").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
			$("#protocol").formValidator({tipID:"urlTip",onFocus:'<view:LanguageTag key="distconf_vd_site_url_show"/>',onCorrect:"OK"}).functionValidator({
			  fun:function(val){
			  	if($.trim(val)=='' || $.trim(val)==null) {
			  		return '<view:LanguageTag key="distconf_vd_site_url_not_empty"/>';
			  	}else {
			        if(val=="http" || val=="https"){
					    return true;
				    }else{
					    return '<view:LanguageTag key="distconf_vd_url_format"/>';
				    }
			  	}
			  }
			  });
			$("#ip").formValidator({tipID:"urlTip",onFocus:'<view:LanguageTag key="distconf_vd_url_ip"/>',onCorrect:"OK"}).functionValidator({
			 fun:function(val){
			     var re = new RegExp(":|/|//|://|:/", "i");
			     var yesorno = re.test(val);
			     if($.trim(val)=='' || $.trim(val)==null) {
			  		return '<view:LanguageTag key="distconf_vd_url_ip_not_empty"/>';
			  	 }else {
				     if(!yesorno){
				         return true;
				     }else{
				         return '<view:LanguageTag key="distconf_vd_url_ip_format"/>';
				     }
			  	 }
			    }
			 });
			$("#port").formValidator({tipID:"urlTip",onFocus:'<view:LanguageTag key="distconf_vd_url_port_show"/>',onCorrect:"OK"}).functionValidator({fun:checkPort,onError:'<view:LanguageTag key="distconf_vd_url_port_err"/>'});
			$("#path").formValidator({tipID:"urlTip",onFocus:'<view:LanguageTag key="distconf_vd_url_path_show"/>',onCorrect:"OK"}).functionValidator({
			   fun:function(val){
			     var re1 = new RegExp(":", "i");
			     var yesorno1 = re1.test(val);
			     if($.trim(val)=='' || $.trim(val)==null) {
			  		return '<view:LanguageTag key="distconf_vd_url_path_not_empty"/>';
			  	 }else {
			       if(yesorno1){
			          return '<view:LanguageTag key="distconf_vd_url_path_format"/>';
			       }else{
			          return true;
			       }
			  	 }
			   }
			}); 
            $("#apperiod").focus();
	}
	
	function savaData(){
    	document.getElementById('siteurl').value=$.trim($("#protocol").val())+'://'+$.trim($("#ip").val())+':'+$.trim($("#port").val())+'/'+$.trim($("#path").val());
    	var urlH = "<%=path%>/manager/confinfo/config/distribute!modify.action";
		var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
		$("#distForm").ajaxSubmit({
		   async:true,
		   dataType : "json",  
		   type:"POST", 
		   url : urlH,
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
  <form id="distForm" method="post" action="" name="distForm">
    <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="topTableBg">
      <tr>
        <td width="98%"><span class="topTableBgText"><view:LanguageTag key="common_menu_config_dist"/></span></td>
        <td width="2%" align="right">
      	 <!--	<a href="javascript:openHelpWin('<%=path%>/manager/help/confighelp.html#387','<%=path%>/manager/common/js/ligerUI/skins/Gray/css/dialog.css');">
      		<img src="<%=path%>/images/icon/help_1.png" width="16" height="16" hspace="5" title='<view:LanguageTag key="common_syntax_help"/>' align="absmiddle" />
      		</a>-->
        </td>
      </tr>
    </table>  
   <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="ulOnInsideTable">
      <tr>
        <td valign="top">
         <ul id="content">
	      <li class="conFocus">
		    <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		      <tr>
		        <td width="25%" align="right"><view:LanguageTag key="distconf_ap_period"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		        <td width="30%">
		        	<input type="text" name="distConfInfo.apperiod" id="apperiod" value="${distConfInfo.apperiod}" class="formCss100"/>
		        </td>
		        <td width="45%" class="divTipCss"><div id="apperiodTip"></div></td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key="distconf_ap_retry"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		        <td><input type="text" id="apretry" name="distConfInfo.apretry" value="${distConfInfo.apretry}" class="formCss100"/></td>
		        <td class="divTipCss"><div id="apretryTip"></div></td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key="distconf_defult_ap"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		        <td><input type="text" id="defultap" name="distConfInfo.defultap" value="${distConfInfo.defultap}" class="formCss100"/></td>
		        <td class="divTipCss"><div id="defultapTip"></div></td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key="distconf_site_type"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		        <td><select onChange="orgProtocol(this.value);" name="distConfInfo.sitetype" id="provsitetype" class="select100">
		            <option value="1" 
		              <c:if test='${distConfInfo.sitetype == 1}'>selected</c:if>
		            >  http</option>
		            <option value="2" 
		              <c:if test='${distConfInfo.sitetype == 2}'>selected</c:if>
		             > https</option>
		            <option value="3"
		              <c:if test='${distConfInfo.sitetype == 3}'>selected</c:if>
		              > http;https</option>
		          </select>
		        </td>
		        <td class="divTipCss"><div id="provsitetypeTip"></div></td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key="distconf_site_url"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		        <td><input type="text" value="${distConfInfo.protocol}"  size="3" name="distConfInfo.protocol" id="protocol"/>
		          ://
		          <input type="text" value="${distConfInfo.ip}"     size="15" name="distConfInfo.ip" id="ip"/>
		          :
		          <input type="text" value="${distConfInfo.port}"  size="5" name="distConfInfo.port" id="port"/>
		          /
		          <input type="text" value="${distConfInfo.path}"  size="33" name="distConfInfo.path" id="path"/>
		            <input type="hidden"   size="25" name="distConfInfo.siteurl" id="siteurl"/>
		        </td>
		        <td class="divTipCss"><div id="urlTip"></div></td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key="distconf_site_enabled"/><view:LanguageTag key="colon"/></td>
		        <td>
			        <input type="radio" name="distConfInfo.siteenabled" id="provsiteenabled" 
					   value="y"
			            <c:if test="${distConfInfo.siteenabled eq 'y'}">checked</c:if>
			          /> <view:LanguageTag key="common_syntax_enable"/>&nbsp;&nbsp;&nbsp;
			            <input type="radio" name="distConfInfo.siteenabled" id="provsiteenabled1"
					   value="n"
			          <c:if test="${distConfInfo.siteenabled eq 'n'}">checked</c:if>
			          /> <view:LanguageTag key="common_syntax_close"/>
			    </td>
		        <td class="divTipCss"><div id="siteenabledTip"></div></td>
		      </tr>
		      <tr>
		        <td align="right"></td>
		        <td><a href="#" id="saveBt" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a></td>
		        <td></td>
		      </tr> 
		    </table>
    	  </li>
         </ul>
        </td>
       </tr>
      </table> 
   </form>
  </body>
</html>