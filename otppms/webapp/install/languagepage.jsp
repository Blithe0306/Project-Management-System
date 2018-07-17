<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css" />
<script src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js" type="text/javascript"></script>
<script language="javascript" type="text/javascript">	
	//语言选择
	function languageSel(flag){
		var language = $.trim($("#langSel").val());
		$.post("<%=path%>/install/install!initLanguage.action",
			{currLang:language},
			function(result){
				if(flag==0){
					parent.location.href = parent.location.href;
				}else{
					nextIni();
				}
	    	},'json'
		);
	}
	
	//下一步
	function nextIni(){		
		parent.hideId('01', 'T01');
		parent.showId('02', 'T02');
		parent.toPage("frameView02", "/install/init_select.jsp");
	}
</script>
</head>
<body>
<form name="LanguageForm" method="post" action="">
  <table width="100%" border="0" cellpadding="4" cellspacing="0" style="margin-top:5px">
    <tr>
      <td width="25%" height="12" align="right" bgcolor="#FFFFFF">
        <view:LanguageTag key="sys_init_sel_use_lang"/><view:LanguageTag key="colon"/>      </td>
      <td width="35%" align="left" bgcolor="#FFFFFF">
        <select id="langSel" name="langSel" class="select100" onchange="languageSel(0);">
          <view:LanguageSelectTag key="${sessionScope.language_session_key}" />
        </select>
      </td>
      <td width="40%" align="left" bgcolor="#FFFFFF">&nbsp;</td>
    </tr>
    <tr>
      <td width="25%" height="12" align="right" bgcolor="#FFFFFF">&nbsp;</td>
      <td colspan="2" align="left" bgcolor="#FFFFFF"><a href="javascript:languageSel(1);" id="backBt" class="button"><span><view:LanguageTag key="common_syntax_nextStep"/></span></a></td>
    </tr>
  </table>
</form>
</body>
</html>
