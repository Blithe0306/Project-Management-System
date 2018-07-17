<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%@page import="com.ft.otp.util.conf.ConfDataFormat"%>
<%
	String path = request.getContextPath();
	int dbConn = 0;
	int licValid = 0;
	int isAdmin = 0;
	int isLocal = 0;
	int isConfEmailServ = 0;
	if(null != application.getAttribute("isCanConn")){
		dbConn = (Boolean) application.getAttribute("isCanConn") ? 1 : 0;
	}
	if(null != application.getAttribute("licIsNull")){
	    licValid = (Boolean)application.getAttribute("licIsNull") ? 1 : 0;
	}
	if(null != application.getAttribute("isSuperMan")){
	    isAdmin = (Boolean)application.getAttribute("isSuperMan") ? 1 : 0;
	}
	if(null != application.getAttribute("isLocalIp")){
	    isLocal = (Boolean)application.getAttribute("isLocalIp") ? 1 : 0;
	}
	if(null != application.getAttribute("isConfEmailServer")){
	    isConfEmailServ = (Boolean)application.getAttribute("isConfEmailServer") ? 1 : 0;
	}
	int srcType = ConfDataFormat.getDbDataSrcType() == true ? 1 : 0;
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css" />
<script src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js" type="text/javascript"></script>
<script language="javascript" type="text/javascript">
	//下一页
	function nextPage(){
		var selType = $("input[name=selType]:checked").val();
		parent.hideId('02', 'T02');
		parent.toPage("frameView03", "/install/install!find.action?selType="+selType);

		if('<%=dbConn%>'=='0'){
			parent.showId('03', 'T03');
		}else if('<%=dbConn%>'=='1' && selType=='1'){
			parent.showId('04', 'T04');
		}else if('<%=dbConn%>'=='1' && '<%=licValid%>'=='0'){
			parent.showId('05', 'T05');
		}else if('<%=dbConn%>'=='1' && '<%=licValid%>'=='1' && '<%=isConfEmailServ%>'=='0'){
			parent.toPage("frameView09", "/manager/confinfo/email/email!initFind.action");
			parent.showId('09', 'T09');
		}else if('<%=dbConn%>'=='1' && '<%=licValid%>'=='1' && '<%=isConfEmailServ%>'=='1' && '<%=isAdmin%>'=='0'){
			parent.showId('06', 'T06');
		}else if('<%=dbConn%>'=='1' && '<%=licValid%>'=='1' && '<%=isConfEmailServ%>'=='1' && '<%=isAdmin%>'=='1' && '<%=isLocal%>'=='1'){
			parent.showId('07', 'T07');
		}else if('<%=dbConn%>'=='1' && '<%=licValid%>'=='1' && '<%=isConfEmailServ%>'=='1' && '<%=isAdmin%>'=='1' && '<%=isLocal%>'=='0'){
			parent.toPage("frameView08", "/install/install!finish.action");
			parent.showId('08', 'T08');
		}
	}

	//返回
	function backPage(){
		parent.showId('01', 'T01');
		parent.hideId('02', 'T02');
		parent.$('#T02').hide();
	}
</script>
</head>
<body>
<form id="initSelForm" name="initSelForm" method="post" action="">
  <table width="100%" border="0" cellpadding="5" cellspacing="1" style="margin-top:5px">
    <tr>
      <td width="15%" align="right">&nbsp;</td>
      <td width="85%" align="left"><strong><view:LanguageTag key="sys_init_db_init_mode_sel"/><view:LanguageTag key="colon"/></strong></td>
    </tr>
    <tr>
      <td width="15%" align="right">&nbsp;</td>
      <td align="left">
        <input id="selType0" name="selType" type="radio" value="0" checked />
      	<view:LanguageTag key="sys_init_create_dbconn_conduct_sys_init"/></td>
    </tr>
    <%--<tr>
      <td width="15%" align="right">&nbsp;</td>
      <td align="left">
        <input id="selType1" name="selType" type="radio" value="1" />
      	<view:LanguageTag key="sys_init_restore_backup_date"/></td>
    </tr>
    --%>
    <tr>
      <td align="right">&nbsp;</td>
      <td align="left">
      <a href="javascript:backPage();" id="backBt" class="button"><span><view:LanguageTag key="common_syntax_preStep"/></span></a>
      <a href="javascript:nextPage();" id="nextBt" class="button"><span><view:LanguageTag key="common_syntax_nextStep"/></span></a>
      </td>
    </tr>
  </table>
</form>
</body>
</html>
