<%@ page language="java" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title></title>
	<script language="javascript" type="text/javascript">
	<!--
	//-->
	</script>
</head>
<frameset rows="*, 36" cols="*" framespacing="0" frameborder="NO" border="0"    > 
	<frame src="<%=path%>/manager/main/welcome.jsp" name="mainFrame" scrolling="NO" noresize>
	<frame src="<%=path%>/manager/main/bottom.jsp" name="bottomFrame" scrolling="NO" noresize>
</frameset>
<noframes></noframes>
<body>
</body>
</html>
