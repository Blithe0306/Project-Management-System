 <%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
%>

<html>
  <head>    
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>系统提示</title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css">
    <style type="text/css">
	<!--
	.STYLE1 {
		font-size: 14px;
		color: #333333;
		font-weight: bold;
	}
	-->
	</style>
  </head>  
  <body>
  <table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td align="center" valign="top">&nbsp;</td>
    </tr>
    <tr>
      <td align="center"><span class="STYLE1">
      	<c:if test="${param.errcode eq '303'}"><view:LanguageTag key="common_exception_errcode1"/></c:if>
        <c:if test="${param.errcode eq '707'}"><view:LanguageTag key="common_exception_errcode2"/></c:if>
        <c:if test="${param.errcode eq '404'}"><view:LanguageTag key="common_exception_errcode3"/></c:if>
        <c:if test="${param.errcode eq '500' || param.errcode eq '606' || empty param.errcode}"><view:LanguageTag key="common_exception_errcode4"/></c:if>
      </span></td>
    </tr>
  </table> 
  </body>
</html>