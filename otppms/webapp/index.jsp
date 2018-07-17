<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.ft.otp.manager.login.entity.LinkUser"%>
<%@page import="com.ft.otp.manager.login.service.OnlineUsers"%>
<%@page import="com.ft.otp.util.tool.StrTool"%>
<%@page import="com.ft.otp.util.conf.ConfDataFormat"%>
<%
	String path = request.getContextPath();

	boolean dbConn = false;
	boolean licCheck = false;
	boolean isAdmin = false;
	boolean isLocal = false;
	boolean isConfEmailServ = false;
	if(null != application.getAttribute("isCanConn")){
	    dbConn = (Boolean) application.getAttribute("isCanConn");
	}
	if(null != application.getAttribute("licIsNull")){
	    licCheck = (Boolean)application.getAttribute("licIsNull");
	}
	if(null != application.getAttribute("isSuperMan")){
	    isAdmin = (Boolean)application.getAttribute("isSuperMan");
	}
	if(null != application.getAttribute("isLocalIp")){
	    isLocal = (Boolean)application.getAttribute("isLocalIp");
	}
	if(null != application.getAttribute("isConfEmailServer")){
	    isConfEmailServ = (Boolean)application.getAttribute("isConfEmailServer");
	}
	
	boolean dbSrcType = ConfDataFormat.getDbDataSrcType();
	
	Boolean invalidation = (Boolean) request.getAttribute("invalidation");
	boolean outof = false;
	if(null != invalidation){
	    outof = invalidation;
	}
	
	String remoteAddr = request.getRemoteAddr();
    String sessionId = session.getId();
    LinkUser linkUser = OnlineUsers.getUser(sessionId);
    String userAddr = null;
    if(null != linkUser){
		userAddr = linkUser.getRemoteAddr();
    }
    	
    String redirect_1 = "/login/login.jsp?outof="+outof;
    String redirect_2 = "/install/index.jsp";
    String redirect_3 = "/manager/main/layout.jsp";    
    String redirect = "";
    if(!dbConn || !licCheck || !isAdmin || isLocal || !isConfEmailServ || (dbConn && dbSrcType)){
        redirect = path + redirect_2;
    }else if(!StrTool.strEquals(remoteAddr, userAddr)) {                
    	redirect = path + redirect_1;
    }else if (StrTool.strEquals(remoteAddr, userAddr)) {
		redirect = path + redirect_3;
    }
%>

<script language="javascript" type="text/javascript">
<!--
	window.parent.parent.location.href ="<%=redirect%>";
//-->
</script>
