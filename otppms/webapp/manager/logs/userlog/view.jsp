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
              <td width="30%" align="right" valign="top"><view:LanguageTag key="log_query_user"/><view:LanguageTag key="colon"/>
              </td>
              <td width="55%">${userLog.userid}</td>
              <td width="15%">&nbsp;</td>
            </tr>
            <tr>
              <td align="right"><view:LanguageTag key="log_info_oper_time"/><view:LanguageTag key="colon"/>
              </td>
              <td>${userLog.logTimeStr}</td>
              <td>&nbsp;</td>
            </tr>
            
            <tr>
              <td align="right"><view:LanguageTag key="tkn_comm_tknum"/><view:LanguageTag key="colon"/>
              </td>
              <td>${userLog.token}</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td align="right"><view:LanguageTag key="log_actionid"/><view:LanguageTag key="colon"/>
              </td>
              <td>${userLog.actionidOper}</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td align="right"><view:LanguageTag key="log_info_operate_result"/><view:LanguageTag key="colon"/>
              </td>
              <td>
                <c:choose>
                  <c:when test="${userLog.actionresult == 0}"><span style="color:red"><view:LanguageTag key="common_syntax_failure"/></span></c:when>
                  <c:otherwise><view:LanguageTag key="common_syntax_success"/></c:otherwise>
                </c:choose>
              </td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td align="right"><view:LanguageTag key="log_info_client_ip"/><view:LanguageTag key="colon"/>
              </td>
              <td>${userLog.clientip}</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td align="right"><view:LanguageTag key="log_info_server_ip"/><view:LanguageTag key="colon"/>
              </td>
              <td>${userLog.serverip}</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td align="right"><view:LanguageTag key="domain_info_name"/><view:LanguageTag key="colon"/>
              </td>
              <td>${userLog.domainname}</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td align="right"><view:LanguageTag key="domain_orgunit"/><view:LanguageTag key="colon"/>
              </td>
              <td>${userLog.orgunitname}</td>
              <td>&nbsp;</td>
            </tr>
     		<tr>
              <td align="right"><view:LanguageTag key="log_otp_failed_info"/><view:LanguageTag key="colon"/>
              </td>
              <td>${userLog.logcontent}</td>
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
