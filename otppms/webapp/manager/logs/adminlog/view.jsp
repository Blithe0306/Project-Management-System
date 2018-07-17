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
<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>

</head>
<body style="overflow:auto; overflow-x:hidden">
<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnTable">
  <tr>
    <td valign="top"> 
      <ul id="menu">
        <li class="tabFocus">
          <view:LanguageTag key="common_syntax_base_info"/>
        </li>
      </ul>
      <ul id="content">
        <li class="conFocus">
          <table width="100%" border="0" cellspacing="0" cellpadding="3">
            <tr>
              <td width="30%" align="right" valign="top"><view:LanguageTag key="log_info_operator"/><view:LanguageTag key="colon"/>
              </td>
              <td width="55%">${adminLog.operator}</td>
              <td width="15%">&nbsp;</td>
            </tr>
            <tr>
              <td align="right"><view:LanguageTag key="log_info_oper_time"/><view:LanguageTag key="colon"/>
              </td>
              <td>${adminLog.logTimeStr}</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td align="right"><view:LanguageTag key="log_info_operate_result"/><view:LanguageTag key="colon"/>
              </td>
              <td>
                <c:choose>
                  <c:when test="${adminLog.actionresult == 0}"><view:LanguageTag key="common_syntax_success"/></c:when>
                  <c:otherwise><span style="color:red"><view:LanguageTag key="common_syntax_failure"/></span></c:otherwise>
                </c:choose>
              </td>
              <td>&nbsp;</td>
            </tr>
             <tr>
              <td align="right"><view:LanguageTag key="log_info_client_ip"/><view:LanguageTag key="colon"/>
              </td>
              <td>${adminLog.clientip}</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td align="right" valign="top"><view:LanguageTag key="log_actionid"/><view:LanguageTag key="colon"/></td>
              <td> ${adminLog.actionDesc} </td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td align="right" valign="top"><view:LanguageTag key="log_info"/><view:LanguageTag key="colon"/></td>
              <td> ${adminLog.descp} </td>
              <td>&nbsp;</td>
            </tr>
          </table>
        </li>
      </ul>
    </td>
  </tr>
</table>
</body>
</html>
