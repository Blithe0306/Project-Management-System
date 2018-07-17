<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.ft.otp.common.page.PageTool"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
	String path = request.getContextPath();	
%>

<html>
	<head>
		<title></title>
	<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css">
	<c:if test="${empty html_page_info.curPage}">
	<script language="javascript" type="text/javascript">
	<!--
		function deleteObj(){
			//后期可在此进行删除前验证
			  delData(0, <%=PageTool.PG_SKIP%>);
		}
	//-->
	</script>
	</c:if>
	<c:if test="${not empty html_page_info.curPage}">
	<script language="javascript" type="text/javascript">
	<!--
		function deleteObj(){
			//后期可在此进行删除前验证
			  delData(${html_page_info.curPage}, <%=PageTool.PG_SKIP%>);
		}
	//-->
	</script>
	</c:if>
	</head>
	<body>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" >
			<tr>
				<td>
				 <!--<input type="checkbox" name="check_all" onClick="selectAll(this, 'key_id')" title="全选/取消全选">&nbsp;&nbsp;</span>-->
				<a href="#" onClick="javascript:deleteObj()" class="button"><span>删 除</span></a>
				</td>
			</tr>
		</table>
	</body>
</html>