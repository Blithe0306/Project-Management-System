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
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css">
	<link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css">
	<link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css">
     <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>    
     <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
     <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>	
		
	<script language="javascript" type="text/javascript">
	<!--
	var winClose;
	 $(function() {
	     $.formValidator.initConfig({submitButtonID:"tknSync",debug:true,
	       onSuccess:function(){
	           tknSync('<%=tknLen%>');
	        },
           onError:function(){
               return false;
           }
         });
  		 $("#otp").formValidator({onFocus:'<view:LanguageTag key="tkn_vd_otp_show"/>',onCorrect:"OK"}).inputValidator({min:4, max:10, onError:'<view:LanguageTag key="tkn_vd_otp_error_len"/>'})
			.regexValidator({regExp:"num",dataType:"enum",onError:'<view:LanguageTag key="tkn_vd_otp_error"/>'});         
		 
		 $("#nextotp").formValidator({onFocus:'<view:LanguageTag key="tkn_vd_otp_show"/>',onCorrect:"OK"}).inputValidator({min:4, max:10, onError:'<view:LanguageTag key="tkn_vd_otp_error_len"/>'})
			.regexValidator({regExp:"num",dataType:"enum",onError:'<view:LanguageTag key="tkn_vd_otp_error"/>'});
	     
	     $("#challengeotp").formValidator({onFocus:'<view:LanguageTag key="tkn_vd_challengeotp_tip"/>',onCorrect:"OK"}).inputValidator({min:4, max:10, onError:'<view:LanguageTag key="tkn_vd_challengeotp_len"/>'})
			.regexValidator({regExp:"num",dataType:"enum",onError:'<view:LanguageTag key="tkn_vd_challengeotp_err"/>'});
		 $("#ordinaryotp").formValidator({onFocus:'<view:LanguageTag key="tkn_vd_otp_tip"/>',onCorrect:"OK"}).inputValidator({min:4, max:10, onError:'<view:LanguageTag key="tkn_vd_otp_len"/>'})
			.regexValidator({regExp:"num",dataType:"enum",onError:'<view:LanguageTag key="tkn_vd_otp_err"/>'});
		
		 $("#challrespotp").formValidator({onFocus:'<view:LanguageTag key="tkn_vd_challrespotp_tip"/>',onCorrect:"OK"}).inputValidator({min:4, max:10, onError:'<view:LanguageTag key="tkn_vd_challrespotp_len"/>'})
			.regexValidator({regExp:"num",dataType:"enum",onError:'<view:LanguageTag key="tkn_vd_challrespotp_err"/>'});
		 $("#challrespnextotp").formValidator({onFocus:'<view:LanguageTag key="tkn_vd_challrespotp_tip"/>',onCorrect:"OK"}).inputValidator({min:4, max:10, onError:'<view:LanguageTag key="tkn_vd_challrespotp_len"/>'})
			.regexValidator({regExp:"num",dataType:"enum",onError:'<view:LanguageTag key="tkn_vd_challrespotp_err"/>'});
	     
    	 checkTknSync();
    })
        
    function checkTknSync() {
        <%if(tknLen > 0){ %>               	 
       	  <%if(tknLen == 1){%>
       	  	  selectToken(<%=tknArr[0]%>);
       	  <%}else{%>
       	  	 selectToken($.trim($("#tokenSN").val()));
		  <%}%>
		<%}else{%>
			 selectToken('${param.token}');
        <%}%>
    }
	
	function okClick(item,win,index) {
		winClose = win;
	    $('#tknSync').triggerHandler("click");    
    }
    
    //查询令牌同步模式        
    function selectToken(tokenStr){
    	if (tokenStr == '' || tokenStr == null) {
    		return;
    	}
    	var url='<%=path%>/manager/token/token!findToken.action?flag=tknspec';
    	$.post(url, {'token':tokenStr},
			function(msg){
				if(msg.errorStr == "success"){
					var syncmode = msg.object.syncmode;
					$("#syncmode").val(syncmode);
					if (syncmode == 0) {
						cancelVal();
						$("#syncTab0").show();
						$("#syncTab1").hide();
						$("#syncTab2").hide();
						$("#otp").unFormValidator(false); 
						$("#nextotp").unFormValidator(false); 
						$("#challengeotp").unFormValidator(true); 
						$("#ordinaryotp").unFormValidator(true); 
						$("#challrespotp").unFormValidator(true); 
						$("#challrespnextotp").unFormValidator(true);
						$("#otp").focus(); 
					} else if (syncmode == 1)  {
					    cancelVal();
					 	$("#syncTab1").show();
					 	$("#syncTab0").hide();
						$("#syncTab2").hide();
						$("#otp").unFormValidator(true); 
						$("#nextotp").unFormValidator(true); 
						$("#challengeotp").unFormValidator(false); 
						$("#ordinaryotp").unFormValidator(false); 
						$("#challrespotp").unFormValidator(true); 
						$("#challrespnextotp").unFormValidator(true); 
						$("#challengeotp").focus();
					} else if (syncmode == 2) {
						getQS(tokenStr);
						cancelVal();
						$("#syncTab2").show();
						$("#syncTab1").hide();
						$("#syncTab0").hide();
						$("#otp").unFormValidator(true); 
						$("#nextotp").unFormValidator(true); 
						$("#challengeotp").unFormValidator(true); 
						$("#ordinaryotp").unFormValidator(true); 
						$("#challrespotp").unFormValidator(false); 
						$("#challrespnextotp").unFormValidator(false); 
						$("#challrespotp").focus();
					}else {
						cancelVal();
						$("#syncTab0").show();
						$("#syncTab1").hide();
						$("#syncTab2").hide();
						$("#otp").unFormValidator(false); 
						$("#nextotp").unFormValidator(false); 
						$("#challengeotp").unFormValidator(true); 
						$("#ordinaryotp").unFormValidator(true); 
						$("#challrespotp").unFormValidator(true); 
						$("#challrespnextotp").unFormValidator(true); 
						$("#otp").focus(); 
					}
				}else{
					FT.toAlert('error','<view:LanguageTag key="tkn_get_tkn_syncmode_err"/>',null);
				}
			}, "json"
		);
    }
    
    //获取挑战值
	function getQS(tokenStr){
		var url='<%=path%>/manager/token/authAction!requestQS.action';
		$.post(url, {'messageBean.tokenSN':tokenStr},
			function(msg){
				if(msg.errorStr =='success'){
					$("#changecode").html(msg.object);
				}
			}, "json"
		);
	}
    
    
	//令牌同步
	function tknSync(len){
		var tokenStr = '';
		if(len > 1){
			tokenStr = $.trim($("#tokenSN").val());
		}else{
			tokenStr = $.trim($("#token").val());
		}
		
		var otpStr;
		var nextOtpStr;
		var question='';
		var syncmode = $("#syncmode").val();
		if (syncmode == 0) {
			otpStr = $.trim($("#otp").val());
			nextOtpStr = $.trim($("#nextotp").val());
		}else if(syncmode == 1) {
			otpStr = $.trim($("#challengeotp").val());
			nextOtpStr = $.trim($("#ordinaryotp").val());
		}else if(syncmode == 2) {
			question = $("#changecode").text();
			otpStr = $.trim($("#challrespotp").val());
			nextOtpStr = $.trim($("#challrespnextotp").val());
		}else {
			otpStr = $.trim($("#otp").val());
			nextOtpStr = $.trim($("#nextotp").val());
		}	

	    var url = '<%=path%>/manager/token/authAction!tokenSync.action';
		$.post(url, {'messageBean.tokenSN':tokenStr, 'messageBean.otp':otpStr, 'messageBean.nextOtp':nextOtpStr, "messageBean.challengeCode" : question},
			function(msg){
				if(msg.errorStr == 'success'){
		    		parent.closeSyncWin(msg.object);
		    	}else{
		    		FT.toAlert(msg.errorStr,msg.object,null);
		    	}
			}, "json"
		);
	}
	
	//清空列表
	function cancelVal(){
		$("#otp").val("");
		$("#nextotp").val("");
		$("#challengeotp").val("");
		$("#ordinaryotp").val("");
		$("#challrespotp").val("");
		$("#challrespnextotp").val("");
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
		 <li class="tabFocus"><view:LanguageTag key="tkn_synch_title"/></li>
        </ul>
        <ul id="content">
		 <li class="conFocus">
		 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
             <tr>
               <td width="32%" align="right" valign="top"><view:LanguageTag key="tkn_comm_tknum"/><view:LanguageTag key="colon"/></td>
               <td width="28%">
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
                <input type="hidden" id="syncmode" value="" />
               </td>
               <td width="40%">&nbsp;</td>
             </tr>
             
             <tr>
             	<td colspan="3">
             	
             	 <table width="100%" border="0" cellspacing="0" cellpadding="0" id="syncTab0" style="display:none">
		          	<tr>
		               <td align="right" width="32%"><view:LanguageTag key="tkn_dynamic_pwd"/><view:LanguageTag key="colon"/></td>
		               <td width="28%"><input type="text" id="otp" name="otp" class="formCss100" /></td>
		               <td class="divTipCss" width="40%"><div id="otpTip"></div></td>
		             </tr>
		             <tr>
		               <td align="right"><view:LanguageTag key="tkn_next_dynamic_pwd"/><view:LanguageTag key="colon"/></td>
		               <td><input type="text" id="nextotp" name="nextotp" class="formCss100" /></td>
		               <td class="divTipCss"><div id="nextotpTip"></div></td>
		             </tr>
		         </table>
		         
		 		<table width="100%" border="0" cellspacing="0" cellpadding="0" id="syncTab1" style="display:none"> 
		          <tr>
		            <td align="right" width="32%"><view:LanguageTag key="tkn_challenge_otp"/><view:LanguageTag key="colon"/></td>
		            <td width="28%">
		              <input type="text" id="challengeotp" class="formCss100" />
		            </td>
		            <td width="40%" class="divTipCss"><div id="challengeotpTip"></div></td>
		          </tr>
		          <tr>
		            <td align="right"><view:LanguageTag key="tkn_dynamic_pwd"/><view:LanguageTag key="colon"/></td>
		            <td>
		              <input type="text" id="ordinaryotp" class="formCss100" />
		            </td>
		            <td class="divTipCss"><div id="ordinaryotpTip"></div></td>
		          </tr>
		         </table>
		         
		 		<table width="100%" border="0" cellspacing="0" cellpadding="0" id="syncTab2" style="display:none">
		          <tr>
		            <td align="right" width="32%"><view:LanguageTag key="tkn_challenge_val"/><view:LanguageTag key="colon"/></td>
		            <td width="28%">
		            	<div id="changecode"></div>
		            </td>
		            <td width="40%"></td>
		          </tr>
		          <tr>
		            <td align="right" width="32%"><view:LanguageTag key="tkn_challenge_resp_otp"/><view:LanguageTag key="colon"/></td>
		            <td width="28%">
		              <input type="text" id="challrespotp" class="formCss100" />
		            </td>
		            <td width="40%" class="divTipCss"><div id="challrespotpTip"></div></td>
		          </tr>
		          <tr>
		            <td align="right"><view:LanguageTag key="tkn_challenge_resp_nextotp"/><view:LanguageTag key="colon"/></td>
		            <td>
		              <input type="text" id="challrespnextotp" class="formCss100" />
		            </td>
		            <td class="divTipCss"><div id="challrespnextotpTip"></div></td>
		          </tr>
		         </table>
             	</td>
             </tr>
             
             <tr>
               <td align="right">&nbsp;</td>
               <td> <span style="float:left;width:40%;"></span><a href="#" id="tknSync" style="display:none" class="button"><span><view:LanguageTag key="tkn_synch"/></span></a></td>
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