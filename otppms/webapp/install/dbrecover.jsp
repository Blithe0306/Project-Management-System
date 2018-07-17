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
<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
<link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
<link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>

<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>

<script type="text/javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/validate.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>
<script language="javascript" type="text/javascript">

	$(document).ready(function() {
		$("#filePath").keydown(function(e){
			if(e.keyCode == 13) {
				return false;
			}
		});
	});
	
	//恢复数据
	function revertFile(){
		//setHidVal();
	    var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
		
		$("#DBSetForm").ajaxSubmit({
			async:true,
		   	dataType : "json",
		   	type:"POST",
		   	url : "<%=path%>/manager/data/databak!revert.action",
		  	success:function(msg){
		  		var result = msg.errorStr;
		  		if(result == 'success'){
		  			nextPage();
		  			//FT.toAlert("warn", msg.object, null);
		  		}else{
					FT.toAlert(result, msg.object, null);
				}
				ajaxbg.hide();
		   	}
		});
	}
	
	//下一页
	function nextPage(){
		parent.hideId('04', 'T04');
		parent.showId('07', 'T07');
	}
	
	//返回
	function backPage(){
		$("#DBSetForm").ajaxSubmit({
			async:true,
		   	dataType : "json",
		   	type:"POST",
		   	url : "<%=path%>/install/install!dbBackInitSel.action",
		  	success:function(msg){
		  		parent.showId('01', 'T01');
		  		
		  		parent.hideId('02', 'T02');
				parent.$('#T02').hide();
				parent.hideId('03', 'T03');
				parent.$('#T03').hide();
				parent.hideId('04', 'T04');
				parent.$('#T04').hide();
				parent.toPage("frameView01", "/install/languagepage.jsp");
		   	}
		});
	}
//-->
</script>
</head>
<body >
<div id="background" class="background" style="display: none; "></div>
<div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
<form id="DBSetForm" name="DBSetForm" method="post" action="">
  <table width="100%" border="0" cellpadding="5" cellspacing="1" style="margin-top:5px">
    <tr>
      <td width="25%" height="12" align="right" valign="top"><view:LanguageTag key="sys_remind"/><view:LanguageTag key="colon"/></td>
      <td width="65%" align="left">
      		<view:LanguageTag key="data_backup_recovery_tip"/><br/>
     	 	<view:LanguageTag key="data_backup_recovery_tip2"/>
      </td>
      <td width="10%" align="left">&nbsp;</td>
    </tr>
    <tr>
      <td width="25%" height="12" align="right"><view:LanguageTag key="data_backup_file_path"/><view:LanguageTag key="colon"/></td>
      <td align="left">
        <input id="filePath" name="filePath" type="text" value='<view:LanguageTag key="data_path_for_example"/>' class="formCss100" />
      </td>
      <td align="left">&nbsp;</td>
    </tr>
    <tr id="racTR">
      <td>&nbsp;</td>
      <td colspan="2" align="left">
      <a href="javascript:revertFile();" class="button" id="fileupload"><span><view:LanguageTag key="data_recovery"/></span></a>
      <a href="javascript:backPage();" id="backBt" class="button"><span><view:LanguageTag key="data_recovery_release"/></span></a>
    </tr>  
  </table>
</form>
</body>
</html>
