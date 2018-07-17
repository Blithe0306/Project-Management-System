<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>

<%
	String path = request.getContextPath();
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css">    
	<script language="javascript" type="text/javascript">
	<!--
		function showSysVersion(){
			FT.toAlert('none', '<view:LanguageTag key="sys_version_descp"/>', null);
		}
	//-->
	</script>
  </head>
  
  <body>
  <table width="96%" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-top:12px">
    <tr>
      <td align="left">
      	用于记录定制项目的基本信息，便于跟进。
      </td>
    </tr>
  </table>
  </body>
</html>
