 <%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
%>

<html>
  <head>    
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>错误提示</title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" >	
		window.parent.location.href = "<%=path%>/errorview.jsp?errcode=${param.errcode}&title=${param.title}";
	</script> 
  </head>  
  <body>
  </body>
</html>
