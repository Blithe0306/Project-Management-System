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
    <link href="<%=path%>/manager/common/ligerUI/skins/Aqua/css/ligerui-alert.css" rel="stylesheet" type="text/css" />
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
  </head>
  
 <body scrolling="no" style="overflow-y:hidden;">
 <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnTable">
	<tr>
    <td valign="top" height="380px">
	 <iframe  id="bottomFrame"
		src="<%=path%>/manager/admin/role/perm_view.jsp?oper=viewrole&roleid=${param.roleId}"
		scrolling="no" name="bottomFrame" width="100%" height="380px"
		frameborder="0" style="color: red">
	 </iframe>
    </td>
  </tr>
 </table>
 </body>
</html>