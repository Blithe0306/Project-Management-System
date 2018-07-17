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
    <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>

  </head>
 <body>
 <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
       <ul id="menu">
		 <li class="tabFocus"><view:LanguageTag key="notice_detail_info"/></li>
	    </ul>
        <ul id="content">
		 <li class="conFocus">
		 <table width="100%" border="0" cellspacing="0" cellpadding="3px">
             <tr>
               <td width="30%" align="right" valign="top"><view:LanguageTag key="notice_title_text"/><view:LanguageTag key="colon"/></td>
               <td width="55%">${noticeInfo.title}</td>
               <td width="15%"></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="notice_created_user"/><view:LanguageTag key="colon"/></td>
               <td>${noticeInfo.createuser}</td>
               <td></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="notice_level"/><view:LanguageTag key="colon"/></td>
               <td> <c:choose> 
	        	  	<c:when test="${noticeInfo.systype == 1}">
	        	  	<span style="color:green"><view:LanguageTag key="notice_level_general"/></span>  
	        	  	</c:when>
	        	  	<c:when test="${noticeInfo.systype == 2}">
	        	  	<span style="color:red"><view:LanguageTag key="notice_level_urgent"/></span> 
	        	  	</c:when>
	        	  	<c:when test="${noticeInfo.systype == 3}">
	        	  	<span style="color:maroon"><view:LanguageTag key="notice_level_warning"/></span>  
	        	  	</c:when>
	        	  </c:choose>
	        	</td>
	        	<td></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="notice_create_time"/><view:LanguageTag key="colon"/></td>
               <td>${noticeInfo.createtimeStr}</td>
               <td></td>
             </tr>
             
             <tr>
               <td align="right"><view:LanguageTag key="notice_effec_time"/><view:LanguageTag key="colon"/></td>
               <td>${noticeInfo.expiretimeStr}</td>
               <td></td>
             </tr>
               <tr valign="top">
               <td align="right"><view:LanguageTag key="notice_content"/><view:LanguageTag key="colon"/></td>
               <td>${noticeInfo.content}</td>
               <td></td>
             </tr> 
           </table>
          </li>
          </ul>
		</td>
      </tr>
    </table>
  </body>
</html>