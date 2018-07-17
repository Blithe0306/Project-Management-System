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
    
	<script language="javascript" type="text/javascript">
	<!--
	$(function() {
		$("#menu li").each(function(index) { //带参数遍历各个选项卡
			$(this).click(function() { //注册每个选卡的单击事件
				$("#menu li.tabFocus").removeClass("tabFocus"); //移除已选中的样式
				$(this).addClass("tabFocus"); //增加当前选中项的样式
					//显示选项卡对应的内容并隐藏未被选中的内容
				$("#content li:eq(" + index + ")").show()
                    .siblings().hide();
        	});
    	});
    })
        
	//查看代理详细信息
	function viewAgent(agentip){
	   window.location.href='<%=path%>/manager/authmgr/agent/authAgent!view.action?agentInfo.agentipaddr='+ agentip; 
	}

	//-->
	</script>
  </head>
  
  <body style="overflow-x:hidden;overflow-y:auto;">
  <form name="AddForm" method="post" action="">
    <table width="98%" border="0" align="right" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td width="100%" valign="top">
		<ul id="menu">
		 <li class="tabFocus"><view:LanguageTag key='auth_ser_base_info'/></li>
		 <li><view:LanguageTag key="auth_ser_asso_agent"/></li>
	    </ul>
	    <ul id="content">
		 <li class="conFocus">
		   <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
		   	 <tr>
               <td align="right" valign="top"><view:LanguageTag key="auth_ser_hostname"/><view:LanguageTag key="colon"/></td>
               <td>${serverInfo.hostname}</td>
             </tr>
             <tr>
               <td width="35%" align="right"><view:LanguageTag key='auth_ser_hostip'/><view:LanguageTag key="colon"/></td>
               <td width="45%">${serverInfo.hostipaddr}</td>
               <td width="20%"></td>
             </tr>
             <tr>
             	<td align="right"><view:LanguageTag key='auth_ser_priority'/><view:LanguageTag key="colon"/></td>
             	<td>
               		<c:if test="${serverInfo.priority==0}"><view:LanguageTag key='auth_ser_priority_high'/></c:if>
			        <c:if test="${serverInfo.priority==1}"><view:LanguageTag key='auth_ser_priority_ordinary'/></c:if>
			        <c:if test="${serverInfo.priority==2}"><view:LanguageTag key='auth_ser_priority_low'/></c:if>
         		</td>
         		<td></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="auth_ser_ftradius_enabled"/><view:LanguageTag key="colon"/></td>
               <td>
               		<c:if test="${serverInfo.ftradiusenabled==0}"><view:LanguageTag key="common_syntax_no"/></c:if>
               		<c:if test="${serverInfo.ftradiusenabled==1}"><view:LanguageTag key="common_syntax_yes"/></c:if>
               </td> 
               <td></td>
             </tr>
             <c:if test="${serverInfo.ftradiusenabled==1}">
             <tr>
               <td align="right"><view:LanguageTag key="auth_ser_authport"/><view:LanguageTag key="colon"/></td>
               <td>${serverInfo.authport}</td> 
               <td></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="auth_ser_syncport"/><view:LanguageTag key="colon"/></td>
               <td>${serverInfo.syncport}</td> 
               <td></td>
             </tr>
             </c:if>
             <tr>
               <td align="right"><view:LanguageTag key="auth_ser_radius_enabled"/><view:LanguageTag key="colon"/></td>
               <td>
               		<c:if test="${serverInfo.radiusenabled==0}"><view:LanguageTag key="common_syntax_no"/></c:if>
               		<c:if test="${serverInfo.radiusenabled==1}"><view:LanguageTag key="common_syntax_yes"/></c:if>
               </td>
               <td></td> 
             </tr>
             <c:if test="${serverInfo.radiusenabled==1}">
             <tr>
               <td align="right"><view:LanguageTag key="auth_ser_radius_authport"/><view:LanguageTag key="colon"/></td>
               <td>${serverInfo.radauthport}</td> 
               <td></td>
             </tr>
             </c:if>
             <!-- <tr>
               <td align="right"><view:LanguageTag key="auth_ser_http_enabled"/><view:LanguageTag key="colon"/></td>
               <td>
               		<c:if test="${serverInfo.httpenabled==0}"><view:LanguageTag key="common_syntax_no"/></c:if>
               		<c:if test="${serverInfo.httpenabled==1}"><view:LanguageTag key="common_syntax_yes"/></c:if>
               </td> 
               <td></td>
             </tr>
             <c:if test="${serverInfo.httpenabled==1}">
             <tr>
             <td align="right"><view:LanguageTag key="auth_ser_http_port"/><view:LanguageTag key="colon"/></td>
               <td>${serverInfo.httpport}</td> 
               <td></td>
             </tr>
             </c:if>
             <tr>
               <td align="right"><view:LanguageTag key="auth_ser_https_enabled"/><view:LanguageTag key="colon"/></td>
               <td>
               		<c:if test="${serverInfo.httpsenabled==0}"><view:LanguageTag key="common_syntax_no"/></c:if>
               		<c:if test="${serverInfo.httpsenabled==1}"><view:LanguageTag key="common_syntax_yes"/></c:if>
               </td> 
               <td></td>
             </tr>
             <c:if test="${serverInfo.httpsenabled==1}">
              <tr>
               <td align="right"><view:LanguageTag key="auth_ser_https_port"/><view:LanguageTag key="colon"/></td>
               <td>${serverInfo.httpsport}</td> 
               <td></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="auth_ser_serv_cer_type"/><view:LanguageTag key="colon"/></td>
               <td>${serverInfo.keystoreinstance}</td> 
               <td></td>
             </tr>
             </c:if>
              -->
             <tr style="display:none">
               <td align="right"><view:LanguageTag key="auth_ser_soap_enabled"/><view:LanguageTag key="colon"/></td>
               <td>
               		<c:if test="${serverInfo.soapenabled==0}"><view:LanguageTag key="common_syntax_no"/></c:if>
               		<c:if test="${serverInfo.soapenabled==1}"><view:LanguageTag key="common_syntax_yes"/></c:if>
               </td> 
               <td></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="auth_ser_soap_port"/><view:LanguageTag key="colon"/></td>
               <td>${serverInfo.soapport}</td>
               <td></td> 
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="auth_ser_webservicename"/><view:LanguageTag key="colon"/></td>
               <td>${serverInfo.webservicename}</td> 
               <td></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key='common_syntax_desc'/><view:LanguageTag key="colon"/></td>
               <td>${serverInfo.descp}</td>
               <td></td>
             </tr>
           </table>
		 </li>
		 <li>
		  <table width="98%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
             <tr>
               <td width="25%" align="right" valign="top"><view:LanguageTag key='common_menu_auth_proxy'/><view:LanguageTag key="colon"/></td>
               <td width="75%"> 
                <c:if test="${empty serverInfo.agentAddrips}"><view:LanguageTag key='common_syntax_nothing'/></c:if>
				<c:forEach items="${serverInfo.agentAddrips}" var="agentkey">
               		<a href="javascript:viewAgent('${agentkey.agentipaddr}')">${agentkey.agentipaddr}</a><br/>
               	</c:forEach>  
			   </td>
             </tr>
             <tr>
             </tr>             
           </table>
	      </li>
        </ul>
		</td>
      </tr>
    </table>
  </form>
  </body>
</html>