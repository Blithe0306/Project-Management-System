<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.ft.otp.common.page.PageTool"%>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
%>
<html>
	<head>
    <title></title>
    <link href="<%=path%>/manager/common/ligerUI/skins/Aqua/css/ligerui-alert.css" rel="stylesheet" type="text/css" />
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css">
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/window/popup.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/window/popupclass.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/window/dialog.js"></script>
	<script src="<%=path%>/manager/common/ligerUI/js/core/base.js" type="text/javascript"></script>
	<script src="<%=path%>/manager/common/ligerUI/js/plugins/ligerDialog.js" type="text/javascript"></script>
	
	<script language="javascript" type="text/javascript">
	<!--
		function TurnPageOnEnter(curPage){
			if(curPage == '' || curPage == 0){
			     toAlert('warn','<view:LanguageTag key="common_syntax_jump_page"/>', null);
				 return false;
			}
			if(curPage > ${html_page_info.totalPage}){
				toAlert('warn','<view:LanguageTag key="common_syntax_jump_page_no_exist"/>', null);
				document.getElementById('curPage').focus();
				return false;
			}
			TurnPage(curPage, ${html_page_info.totalRow}, <%=PageTool.PG_SKIP%>);
		}
	//-->
	</script>
	</head>
	<body>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td align="right"><view:LanguageTag key="common_syntax_all_records" replaceVal="${html_page_info.totalRow}"/>
				<c:if test="${html_page_info.totalRow !=0}">${html_page_info.curPage}/<view:LanguageTag key="common_syntax_page" replaceVal="${html_page_info.totalPage}"/>&nbsp;&nbsp;
					${firstPage}
					${prePage}
					${nextPage}
					${lastPage}
					<view:LanguageTag key='common_syntax_jump_to'/>
				</c:if>
			    <c:if test="${html_page_info.totalRow !=0}">
					<input name="curPage" type="text" style="width:60px" class="formCss">
					<input name="goto" type="button" onClick="javascript:TurnPageOnEnter(curPage.value)" class="buttonSkipCss" value="GO">
				</c:if>
				</td>
			</tr>
		</table>
	</body>
</html>