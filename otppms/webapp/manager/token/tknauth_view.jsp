<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
	String tokenStr = request.getParameter("tokenArr");
	int tknLen = 0;
	String[] tknArr = null;
	if(null != tokenStr && !"".equals(tokenStr.trim())){
		tknArr = tokenStr.split(",");
		tknLen = tknArr.length;
	}
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
	<link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
 	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
    <script type="text/javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
    <script src="<%=path%>/manager/common/js/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>  
	<style type="text/css">
		.ErrorMsg {
			border: 1px solid #F60;
			color: #000;
			height: 18px;
			background-color: #FFF2E9;
			background-image: url(../../../images/drop/error.gif);
			background-repeat: no-repeat;
			background-position: 3px 3px;
			padding-top: 3px;
			padding-right: 3px;
			padding-bottom: 3px;
			padding-left: 3px;
		}
	</style>
	<script language="javascript" type="text/javascript">
	<!--
	var winClose;
	 $(function() {
		 
		  // 禁用回车
		  $("td :input").keydown(function(e) {
			 if(e.keyCode == '13') {
				return false;
			 }
		  });
		 
	      $.formValidator.initConfig({
             submitButtonID:"tknAuth",
	         debug:true,
	         onSuccess:function(){
	           tknAuth('${param.source_usr}', '<%=tknLen%>');
	         },
             onError:function(){
               return false;
             }
           });
           
           $("#otp").formValidator({onFocus:'<view:LanguageTag key="tkn_vd_otp_show"/>',onCorrect:"OK"}).inputValidator({min:4, max:10, onError:'<view:LanguageTag key="tkn_vd_otp_error_len"/>'})
			.regexValidator({regExp:"num",dataType:"enum",onError:'<view:LanguageTag key="tkn_vd_otp_error"/>'});
		   $("#otp").focus();
		   //设置是否显示获取挑战值
		  
		   var tokenStr = '';
		   if('<%=tknLen%>' > 1){
		   		tokenStr = $.trim($("#tokenSN").val());
		   }else{
		   		tokenStr = $.trim($("#token").val());
		   }	
		   selectToken(tokenStr);
	});
	      
    function selectToken(tokenStr){
    	var url='<%=path%>/manager/token/token!findToken.action';
    	$.post(url, {'token':tokenStr},
			function(msg){
				if(msg.errorStr == "success"){
					var protype = msg.object.producttype;
					if(protype == 2 || protype == 102 || protype == 202){//c300 m300 s300
						$("#crTr").show();
					}else{
						$("#qsv").val('');
						$("#crTr").hide();
					}
				}else{
					$("#qsv").val('');
					$("#crTr").hide();
				}
			}, "json"
		);
    }
        
	//显示获取挑战值按钮
	function selectCR(){
        if($("#crbox").attr("checked") == true){
        	$("#getDiv").show();
        }else{
        	$("#getDiv").hide();
        	$("#qsv").val('');
        }
	}
	
	function okClick(item,win,index) {
		winClose = win;
	    $('#tknAuth').triggerHandler("click");   
    }
	
	//动态口令认证
	function tknAuth(source, len){
		var otpStr = $.trim($("#otp").val());
		var qsv = $.trim($("#qsv").val());
	    var tokenStr = '';
	    
		if(len > 1){
			tokenStr = $.trim($("#tokenSN").val());
		}else{
			tokenStr = $.trim($("#token").val());
		}	
		  
		if(qsv != '' && qsv!=null){
			if(otpStr == ''){
				toAlert('error','<view:LanguageTag key="tkn_vd_qsv_show"/>', null);
				return;
			}
			// 验证应答码
			vfyCR(tokenStr,otpStr,qsv);
			return;
		}
		
		var url='<%=path%>/manager/token/authAction!tokenAuth.action';
		$.post(url, {'messageBean.tokenSN':tokenStr, 'messageBean.otp':otpStr},
			function(msg){
				if(msg.errorStr == 'success'){
		    		parent.closeWin(msg.object);
		    	}else{
//		    		FT.toAlert(msg.errorStr,msg.object,null);
					$("#showTR").show();
					$("#checkInfo").attr('class','ErrorMsg');
					$("#checkInfo").html(msg.object);
		            return;
		    	}
			}, "json"
		);
	}
	
	//获取挑战值
	function getQS(source, len){
		var tokenStr = '';
		if(len > 1){
			tokenStr = $.trim($("#tokenSN").val());
		}else{
			tokenStr = $.trim($("#token").val());
		}
		
		var url='<%=path%>/manager/token/authAction!requestQS.action';
		$.post(url, {'messageBean.tokenSN':tokenStr},
			function(msg){
				if(msg.errorStr =='success'){
					$("#qsDiv").html(msg.object);
					$("#qsv").val(msg.object);
				}else{
					FT.toAlert(msg.errorStr,msg.object,null);
				}
			}, "json"
		);
	}
	
	//验证应答码  crStr 输入的应答码、qsv 获取的挑战码
	function vfyCR(tokenStr,crStr,qsv){
		var url='<%=path%>/manager/token/authAction!verifyCR.action';
		$.post(url, {'messageBean.tokenSN':tokenStr,'messageBean.responseCode':crStr,'messageBean.challengeCode':qsv},
			function(msg){
				if(msg.errorStr == 'success'){
		    		$.ligerDialog.success(msg.object,'<view:LanguageTag key="common_syntax_tip"/>',function(){
		    			winClose.close();
		    		});
		    	}else{
		    		//FT.toAlert(msg.errorStr,msg.object,null);
		    		$("#showTR").show();
					$("#checkInfo").attr('class','ErrorMsg');
					$("#checkInfo").html(msg.object);
		    	}
			}, "json"
		);
	}
	//-->
	</script>
  </head>
  <body>
  <form name="ImportForm" method="post" action="" enctype ="multipart/form-data">
	<table width="99%" border="0" align="right" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
        <ul id="menu">
		 <li class="tabFocus"><view:LanguageTag key="tkn_auth_title"/></li>
        </ul>
        <ul id="content">
		 <li class="conFocus">
		 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
		 	<c:if test="${param.source_usr eq 1}">
             <tr>
               <td width="30%" align="right" valign="top"><view:LanguageTag key="tkn_username"/><view:LanguageTag key="colon"/></td>
               <td width="30%">
               <input type="hidden" id="userId" name="userId" value="${param.userId}" class="formCss100" />${param.userId}</td>
               <td width="40%">&nbsp;</td>
             </tr>
            </c:if>
             <tr>
               <td align="right" valign="top" width="30%"><view:LanguageTag key="tkn_comm_tknum"/><view:LanguageTag key="colon"/></td>
               <td width="30%">
               	<%if(tknLen > 0){ %>               	 
               	  <%if(tknLen == 1){%>
               	  	<input type="hidden" id="token" name="token" value="<%=tknArr[0]%>" /><%=tknArr[0]%>
               	  <%}else{%>
               	   <select id="tokenSN" name="tokenSN" class="formCss100" onchange="selectToken(this.value);">
               	    <%for(int i=0; i<tknArr.length; i++){
               	  	  String tknSN = tknArr[i];
               	    %>
				  	  <option value="<%=tknSN%>"><%=tknSN%></option>
				    <%}%>
				   </select>
				  <%}%>
               	<%}else{ %>
                 <input type="hidden" id="token" name="token" value="${param.token}" />${param.token}
                <%} %>
               </td>
               <td width="40%">&nbsp;</td>
             </tr>
             <tr id="crTr">
               <td align="right">&nbsp;</td>
               <td colspan="2">
	               <!-- <input type="checkbox" id="crbox" name="crbox" onClick="selectCR();" value=""><view:LanguageTag key="tkn_challenge_response_tkn"/> -->
	               <div id="getDiv" style="display:">
	                 <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                     <tr>
	                     <td height="6"></td>
	                     </tr>
		                 <tr>
		                   <td width="35%"><a href="javascript:getQS('${param.source_usr}', '<%=tknLen%>');" class="button"><span><view:LanguageTag key="tkn_get_challenge_val"/></span></a></td>
		                   <td width="65%"><div id="qsDiv"></div><input type="hidden" id="qsv" name="qsv" class="formCss100" /></td>
		                 </tr>
	                 </table>
	               </div>
	           </td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="tkn_dynamic_pwd"/><view:LanguageTag key="colon"/></td>
               <td><input type="text" id="otp" name="otp" class="formCss100" /></td>
               <td class="divTipCss"><div id="otpTip"></div></td>
             </tr>
             
             <tr style="display:none" id="showTR">
		      <td height="32" align="right"><span class="isTextRed"><view:LanguageTag key="common_syntax_tip"/><view:LanguageTag key="colon"/></span></td>
		      <td colspan="2" align="left"><span id="checkInfo">&nbsp;</span></td>
		     </tr>
             
             <tr>
               <td align="right">&nbsp;</td>
               <td><span style="float:left;width:40%;"></span><a href="#" id="tknAuth" class="button" style="display:none"><span><view:LanguageTag key="tkn_auth"/></span></a></td>
               <td>&nbsp;</td>
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