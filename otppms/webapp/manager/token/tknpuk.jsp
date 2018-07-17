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
             submitButtonID:"tknPuk",
	         debug:true,
	         onSuccess:function(){
	           tknAuth('${param.codeType}','${param.token}');
	         },
             onError:function(){
               return false;
             }
           });
           
           $("#code").formValidator({onFocus:'<view:LanguageTag key="tkn_puk_incode_show"/>',onCorrect:"OK"}).inputValidator({min:4, max:20, onError:'<view:LanguageTag key="tkn_puk_incode_error_len"/>'})
			.regexValidator({regExp:"num",dataType:"enum",onError:'<view:LanguageTag key="tkn_puk_incode_error"/>'});
		   $("#code").focus();
	});
	      

        
	function okClick(item,win,index) {
		winClose = win;
	    $('#tknPuk').triggerHandler("click");   
    }
	
	// 获取解锁码
	function tknAuth(codeType, tokenStr){
		var method = "";
		var content = "";
		if(codeType == '1'){
			method = "genPUK1";
			content = '<view:LanguageTag key="tkn_puk1_code_title"/>'+'<view:LanguageTag key="colon"/>';
		}else if(codeType == '2'){
			method = "genPUK2";
			content = '<view:LanguageTag key="tkn_puk2_code_title"/>'+'<view:LanguageTag key="colon"/>';
		}
		
		var codeStr = $("#code").val();
		if(method!=""){
			var url='<%=path%>/manager/token/authAction!'+method+'.action';
			$.post(url, {'messageBean.tokenSN':tokenStr, 'messageBean.challengeCode':codeStr},
				function(msg){
					if(msg.errorStr == 'success'){
			    		$.ligerDialog.success(content+msg.object,'<view:LanguageTag key="common_syntax_tip"/>',function(){
			    			winClose.close();
			    		});
			    	}else{
			    		FT.toAlert(msg.errorStr,msg.object,null);
			    	}
				}, "json"
			);
		}
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
		 <li class="tabFocus">
		 	<c:if test="${param.codeType=='1'}"><view:LanguageTag key="tkn_puk1_title"/></c:if>
		 	<c:if test="${param.codeType=='2'}"><view:LanguageTag key="tkn_puk2_title"/></c:if>
		 </li>
        </ul>
        <ul id="content">
		 <li class="conFocus">
		 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
             <tr>
               <td align="right" valign="top" width="30%"><view:LanguageTag key="tkn_comm_tknum"/><view:LanguageTag key="colon"/></td>
               <td width="30%">
                 <input type="hidden" id="token" name="token" value="${param.token}" />${param.token}
               </td>
               <td width="40%">&nbsp;</td>
             </tr>
             <c:if test="${param.pubMode=='2'}">
             <tr>
               <td align="right"><view:LanguageTag key="tkn_puk_incode"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
               <td><input type="text" id="code" name="code" class="formCss100" style="width:98%;"/></td>
               <td><div id="codeTip"></div></td>
             </tr>
             </c:if>
             <tr>
               <td align="right">&nbsp;</td>
               <td><span style="float:left;width:40%;"></span><a href="#" id="tknPuk" class="button" style="display:none"><span><view:LanguageTag key="tkn_auth"/></span></a></td>
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