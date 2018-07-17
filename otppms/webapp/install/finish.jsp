<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=utf-8"%>
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
<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
<script language="javascript" type="text/javascript">
<!--
	function login(){
		window.parent.location.href = "<%=path%>/login/login.jsp";
	}
//-->
</script>
</head>
<body>
<table width="100%" border="0" cellpadding="5" cellspacing="0" style="margin-top:5px">
    <tr>
      <td width="26%" height="12" align="right"></td>
      <td width="74%" align="left">&nbsp;</td>
    </tr>
    <tr>
      <td width="26%" align="right"><img src="<%=path%>/install/images/p_success.gif" width="36" height="33" /></td>
      <td align="left"><view:LanguageTag key="sys_init_succ_complete"/><br/><view:LanguageTag key="sys_init_click_access_login_page"/><a href="javascript:login();"><view:LanguageTag key="common_syntax_login"/></a><view:LanguageTag key="sys_init_exit_close_win"/></td>
    </tr>
</table>
</body>
</html>