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
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css">
    <link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css">
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
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
      	$.formValidator.initConfig({submitButtonID:"saveBtn",
			onSuccess:function(){
			    saveRad();
			},
			onError:function(){
				return false;
			}});
			
      	$("#backendAuth").formValidator({onFocus:'<view:LanguageTag key="user_vd_sel_backend" />',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="user_vd_sel_backend" />'});
      })
      
      function saveRad() {
      	 var backendid = $("#backendAuth").val();
      	 var url = parent.$("#seturl").val();
	     var userIds = parent.$("#userIds").val();
      	 parent.$("#backendAuth").val(backendid);
      	 parent.batchExecOper(url,userIds);
      }
      
      
      function okClick(btn,win,index){
		    $('#saveBtn').triggerHandler("click");    	
	  }
	</script>
  </head>
  
  <body>
  <input id="contextPath" type="hidden" value="<%=path%>" />
  <form  id="AddForm" name="AddForm" method="post" action="">
    <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		      <tr>
		        <td width="30%"  align="right"><view:LanguageTag key="user_whether_backend_auth"/><view:LanguageTag key="colon"/></td>
		        <td width="30%">
		        	<select id="backendAuth" class="select100" >
						<option value="0"><view:LanguageTag key="backend_auth_default"/></option>
						<option value="1"><view:LanguageTag key="backend_auth_need"/></option>
						<option value="2"><view:LanguageTag key="backend_auth_no_need"/></option>
			        </select>
		        </td>
		        <td width="40%" class="divTipCss"><div id="backendAuthTip" ></div></td>
		      </tr>
		      <tr>
                <td><a href="#" name="saveBtn" id="saveBtn"></a></td>
              </tr>
		    </table>
		</td>
      </tr>
    </table>
  </form>
 
  </body>
</html>