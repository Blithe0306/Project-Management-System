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
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css">
    <link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css">
         <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/> 
     <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
     <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
     <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	 <script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>    
  	<script src="<%=path%>/manager/common/js/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>

	
	<script language="javascript" type="text/javascript">
    <!--
	//-->
	</script>
  </head>
  
  <body> 
 
   <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td align="center">  
		  <view:LanguageTag key="tkn_dist_info"/>
        </td>
      </tr>
    </table> 

       <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
      <tr>
        <td width="25%" align="right"><view:LanguageTag key="tkn_dist_info_user"/><view:LanguageTag key="colon"/></td>
        <td width="50%">
         ${distManagerInfo.userName}
        </td>
      </tr>
      <tr>
        <td align="right"><view:LanguageTag key="tkn_dist_info_tkn"/><view:LanguageTag key="colon"/></td>
        <td>${distManagerInfo.token}</td>
      </tr>
      <tr>
        <td align="right"><view:LanguageTag key="tkn_dist_type"/><view:LanguageTag key="colon"/></td>
        <td>
         <c:if test="${distManagerInfo.provtype==1}"><view:LanguageTag key="tkn_dist_online"/></c:if>
         <c:if test="${distManagerInfo.provtype==0}"><view:LanguageTag key="tkn_dist_offline"/></c:if>
        </td>
      </tr>
      <tr>
        <td align="right"><view:LanguageTag key="tkn_activation_pwd"/><view:LanguageTag key="colon"/></td>
        <td> 
        ${distManagerInfo.activepass}
		</td>
      </tr> 
      <c:if test="${distManagerInfo.provtype==0}">
      <tr>
        <td align="right"><view:LanguageTag key="tkn_dist_url"/><view:LanguageTag key="colon"/></td>
        <td>${distManagerInfo.distUrl}</td>
      </tr>
      </c:if>
       <c:if test="${distManagerInfo.provtype==1}">
       <tr>
        <td align="right"><view:LanguageTag key="tkn_activation_code"/><view:LanguageTag key="colon"/></td>
        <td>${distManagerInfo.activeCode}</td>
      </tr>
     </c:if>
    </table>
  </body>
</html>