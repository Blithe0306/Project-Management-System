<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%@page import="com.ft.otp.manager.login.entity.LinkUser"%>
<%@page import="com.ft.otp.manager.login.service.OnlineUsers"%>
 
<%
	String path = request.getContextPath();
 	LinkUser linkUser = OnlineUsers.getUser(session.getId());
 	linkUser.setPercent(0);
 %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css">
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css">
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.progressbar.js"></script>
	<script language="javascript" type="text/javascript">
	<!--
	$(function() {
		getPercent();
	});
	
	//进度条
    function freshBar(percent){    
    	$("#progressBarId").progressBar(percent, {barImage:"<%=path%>/images/icon/barImg/progressbg_black.gif"});
    	if(percent != 100){
    		var time = 1000;
    		setTimeout(function(){getPercent();}, time);
    	}
    }
    
    //取得进度条百分比
    function getPercent(){
    	var url = '<%=path%>/manager/main!getPercentVal.action';
		$.ajax({
			type: "POST",
		    url: url,
		    async: true,
		    dataType: "text",
		    success: function(msg){
		    	freshBar(msg);
		    }
		});
    }
	//-->
	</script>
</head>

<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="center">&nbsp;</td>
  </tr>
  <tr>
    <td align="center"><span id="waitID" style="font-size:14px; color:#666666;"><view:LanguageTag key='common_vd_is_execution'/></span></td>
  </tr>
  <tr>
    <td align="center">&nbsp;</td>
  </tr>
  <tr>
    <td align="center"><div id="progressBarId"></div></td>
  </tr>
</table>
</body>
</html>