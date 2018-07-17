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
        
	//查看服务器詳細信息
	function viewServer(serverip){
		 window.location.href='<%=path%>/manager/authmgr/server/authServer!view.action?serverInfo.hostipaddr='+ serverip;	  
	}	 
	//-->
	</script>
  </head>
  <body style="overflow:hidden;">

    <table width="98%" border="0" align="right" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td width="100%" align="left" valign="top">
			<ul id="menu">
			 <li class="tabFocus"><view:LanguageTag key='auth_agent_detail_info'/></li>
			 <li><view:LanguageTag key='auth_agent_asso_server'/></li>
		    </ul>
		    <ul id="content">
			 <li class="conFocus">
			   <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
			    <tr>
	               <td width="30%" align="right"><view:LanguageTag key='auth_agent_agentname'/><view:LanguageTag key="colon"/></td>
	               <td width="40%">${agentInfo.agentname}</td>
	               <td width="30%">&nbsp;</td>
	             </tr>
	             <tr>
	               <td width="30%" align="right"><view:LanguageTag key='auth_agent_agentip'/><view:LanguageTag key="colon"/></td>
	               <td width="40%">${agentInfo.agentipaddr}</td>
	               <td width="30%">&nbsp;</td>
	             </tr>
	             
	             <tr>
	               <td align="right" valign="top"><view:LanguageTag key="auth_agent_agent_type"/><view:LanguageTag key="colon"/></td>
	               <td>${agentInfo.agenttypeStr}</td>
	               <td>&nbsp;</td>
	             </tr>
	             <tr>
	               <td align="right"><view:LanguageTag key="auth_agent_agentconf"/><view:LanguageTag key="colon"/></td>
	               <td>${agentInfo.agentconfStr}</td>
	               <td>&nbsp;</td>
	             </tr>
	             <c:if test="${agentInfo.flag == '1'}">
		             <c:if test="${agentInfo.graceperiod ne 0}">
			             <tr>
			               <td align="right"><view:LanguageTag key="auth_grace_period"/><view:LanguageTag key="colon"/></td>
			               <td>${agentInfo.graceperiodStr}</td>
			               <td>&nbsp;</td>
			             </tr>
		             </c:if>
		             <c:if test="${agentInfo.graceperiod=='0'}">
		             	<tr>
			             	<td align="right"><view:LanguageTag key="auth_grace_period"/><view:LanguageTag key="colon"/></td>
			             	<td><view:LanguageTag key="auth_grace_period_invalid"/></td>
			             	<td>&nbsp;</td>
		             	</tr>
		             </c:if>
	             </c:if>
	             <tr>
	               <td align="right"><view:LanguageTag key='common_syntax_desc'/><view:LanguageTag key="colon"/></td>
	               <td>${agentInfo.descp}</td>
	               <td>&nbsp;</td>
	             </tr>
	           </table>
			 </li>
			 <li>
			  <table width="98%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
	             <tr>
	               <td width="30%" align="right" valign="top"> <view:LanguageTag key='common_menu_auth_server'/><view:LanguageTag key="colon"/></td>
	               <td width="40%">   
	               	<c:if test="${empty agentInfo.hostIps}"><view:LanguageTag key="common_syntax_nothing"/></c:if>
					<c:forEach items="${agentInfo.hostIps}" var="hostkey">
	               		<a href="javascript:viewServer('${hostkey.hostipaddr}')">${hostkey.hostipaddr}</a><br/>
	               	</c:forEach>
				   </td>
				   <td width="30%">&nbsp;</td>
	             </tr>
	             <tr>
	             </tr>             
	           </table>
		      </li>
	        </ul>
		</td>
      </tr>
    </table>
  </body>
</html>