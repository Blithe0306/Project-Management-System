<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.ft.otp.util.tool.DateTool"%>
 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
 
<%
	String path = request.getContextPath();
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css">
    <link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css">
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css">    
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/> 
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	 <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/datepicker/WdatePicker.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>

	<script src="<%=path%>/manager/common/js/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	
     <script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>   	
    <script language="javascript" type="text/javascript">
    var winClose;
	$(document).ready(function(){
		var abc = '${tokenInfo.tokenSpec.otplen}';
		
		
	    changeSel(${tokenInfo.authmethod});
	    checkInput(); 
	 
	});
	
	//设置令牌应急口令
   function okClick(item,win,index){
         winClose = win;
         $('#addObj').triggerHandler("click");    	
	 }
	 
	function checkInput(){
		$.formValidator.initConfig({submitButtonID:"addObj", debug:true,
	        onSuccess:function(){
	            $("#addForm").ajaxSubmit({
				   async:false,  
				   dataType:"json",
				   type:"POST", 
				   url : "<%=path%>/manager/token/token!setTokenPin.action",
				   success:function(msg){
						if(msg.errorStr == 'success'){ 
							parent.pinClose(msg.object);
						 }else{
						     FT.toAlert(msg.errorStr,msg.object,null);
						} 
					}
		        });
	        },
           onError:function(){
              return false;
              }
            });    
			 var partn = new RegExp("^\\S+$");  // 空格校验的正则表达式
			 var type = new RegExp("^([+-]?)\\d*\\.?\\d+$");
			 var otpLength = '${tokenInfo.tokenSpec.otplen}'; 
			 $("#empin1").formValidator({onFocus:'<view:LanguageTag key="tkn_vd_pin_show_1"/>'+otpLength+'<view:LanguageTag key="report_user_caption_place"/>',onCorrect:"OK"}).functionValidator({
              fun:function(val){
                 var authmethod = $("#authmethod").find("option:selected").val();
                 if(authmethod!=0){
                   if($.trim(val)=='' || $.trim(val)==null){return '<view:LanguageTag key="tkn_vd_empin_error"/>';}
                   if(!partn.exec(val)){return '<view:LanguageTag key="org_vd_remove_space_tip"/>';}
                   if(val.length != otpLength){return '<view:LanguageTag key="tkn_vd_pin_error_3"/>'+otpLength+'<view:LanguageTag key="report_user_caption_place"/>';}
                   if(!type.exec(val)){return '<view:LanguageTag key="tkn_vd_empin_error_3"/>';}
                 }
                 return true;
              }
             });
  			$("#empin2").formValidator({onFocus:'<view:LanguageTag key="tkn_vd_pin_show_2"/>'+otpLength+'<view:LanguageTag key="report_user_caption_place"/>',onCorrect:"OK"}).functionValidator({
              fun:function(val){
                 var authmethod = $("#authmethod").find("option:selected").val();
                 var pin = $('#empin1').val();
                 if(authmethod!=0){
                   if($.trim(val)=='' || $.trim(val)==null){return '<view:LanguageTag key="tkn_vd_empin_two_error"/>';}
                   if(!partn.exec(val)){return '<view:LanguageTag key="org_vd_remove_space_tip"/>';}
                   if(val.length != otpLength){return '<view:LanguageTag key="tkn_vd_pin_error_4"/>'+otpLength+'<view:LanguageTag key="report_user_caption_place"/>';}
                   if($.trim(val)!=$('#empin1').val()){return '<view:LanguageTag key="tkn_vd_empin_error_2"/>';}
                   if(!type.exec(val)){return '<view:LanguageTag key="tkn_vd_empin_error_4"/>';}
                 }
                 return true;
              }
             });
			 $("#empindeath").formValidator({ onFocus:'<view:LanguageTag key="tkn_vd_date_show"/>',onCorrect:"OK"}).functionValidator({
         	 fun:function(valtime){
				  var now = new Date();
				  var authmethod = $("#authmethod").find("option:selected").val();
				  var empindeathMax = '${tokenInfo.empindeathMaxStr}';
				  
				  if(authmethod==0){
				     return true;
				  }
				  
				 if(valtime==''||valtime==null){
				   return '<view:LanguageTag key="tkn_vd_date_error_1"/>';
				 }
			     if(valtime < dateToStr(now)){
	  				return '<view:LanguageTag key="tkn_vd_date_error_2"/>';
				 }
				 //设置的时间不能大于管理中心配置的最大有效值
				 if(valtime > empindeathMax){
	  				return '<view:LanguageTag key="tkn_vd_date_error_3"/>';
				 }
		        return true; 
	         }});

			 $("#empin1").focus();
	}

	function changeSel(obj){
	     authmethod = obj;
		 if(obj==0){
             $('#trEmpin').hide();
             $('#trEmpin2').hide();  
             $('#trEmpdeath').hide();       
		 }else{
             $('#trEmpin').show();
             $('#trEmpin2').show();   
             $('#trEmpdeath').show(); 
		 }
	}
</script>
</head>

<body style="overflow:hidden">
 <input type="hidden" name="currentPage" value="${param.curPage}" id="currentPage"/> 
 <form name="addForm" method="post" action="" id="addForm">
    <table width="98%" border="0" align="center"  cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
        <ul id="menu">
		 <li class="tabFocus"><view:LanguageTag key="tkn_set_emerg_pwd"/></li>
        </ul>
	    <ul id="content">
		 <li class="conFocus">
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
  		<tr>
        <td width="25%" align="right"><view:LanguageTag key="tkn_dist_info_tkn"/><view:LanguageTag key="colon"/></td>
        <td width="30%">${tokenInfo.token}
         <input type="hidden"   name="tokenInfo.token" value="${tokenInfo.token}" class="formCss100" readonly > 
           </td>
        <td width="45%">&nbsp;</td>
      </tr>
       <tr>
        <td align="right"><view:LanguageTag key="tkn_auth_method"/><view:LanguageTag key="colon"/></td>
        <td>
        
            <select id="authmethod" name="tokenInfo.authmethod" value="${tokenInfo.authmethod}" onChange="changeSel(this.value)"  style="width:128px">
				  <option value="1"  <c:if test='${tokenInfo.authmethod == 1}'> selected</c:if>><view:LanguageTag key="tkn_emerg_pwd_auth"/></option>
				  <option value="0"  <c:if test='${tokenInfo.authmethod == 0}'> selected</c:if>><view:LanguageTag key="tkn_dynamic_pwd_auth"/></option>
		  </select>
        </td>
        <td class="divTipCss"><div id="authmethodTip" style="width:100%"></div></td>
      </tr>
    
      <tr id="trEmpin">
        <td align="right"><view:LanguageTag key="tkn_emerg_pwd"/><span class="text_Hong_Se" id="span1">*</span><view:LanguageTag key="colon"/></td>
        <td >
         <input onpaste="return false" type="password" id="empin1" name="tokenInfo.empin" class="formCss100"  value="${tokenInfo.empin}"> 
           </td>
        <td class="divTipCss"><div id="empin1Tip" style="width:100%"></div></td>
      </tr>
        <tr id="trEmpin2">
            <td align="right"><view:LanguageTag key="tkn_confirm_emerg_pwd"/><span class="text_Hong_Se" id="span2">*</span><view:LanguageTag key="colon"/></td>
            <td><input onpaste="return false" type="password" id="empin2" name="empin" class="formCss100" value="${tokenInfo.empin}"></td>
            <td class="divTipCss"><div id="empin2Tip" style="width:100%"></div></td>
          </tr>
          
          <tr  id="trEmpdeath">
            <td align="right"><view:LanguageTag key="tkn_emerg_pwd_exp_time"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
            <td>
		      <input type="text" id="empindeath" name="tokenInfo.empindeathStr" onClick="WdatePicker({lang:'${language_session_key}', isShowClear:false})" readOnly="readOnly" class="formCss100"  value="${tokenInfo.empindeathStr}"/>         
            </td>
            <td class="divTipCss"><div id="empindeathTip" style="width:100%"></div></td>
          </tr>
           <tr>
           <td>&nbsp;</td>
           <td><span style="float:left;width:40%;"></span>
           	   <input type="hidden"  name="tokenInfo.specid" value="${tokenInfo.specid}">
               <input type="hidden"  name="tokenInfo.tokenSpec.otplen" value="${tokenInfo.tokenSpec.otplen}">
	           <a href="#" id="addObj" style="display:none" class="button"><span><view:LanguageTag key="common_syntax_setup"/></span></a>
		   </td>
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