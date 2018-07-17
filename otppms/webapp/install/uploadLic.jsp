<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%@ page import="com.ft.otp.util.conf.ConfDataFormat"%>
<%
	String path = request.getContextPath();
	int isAdmin = 0;
	int isLocal = 0;
	int isConfEmailServ = 0;
	if(null != application.getAttribute("isSuperMan")){
	    isAdmin = (Boolean)application.getAttribute("isSuperMan") ? 1 : 0;
	}
	if(null != application.getAttribute("isLocalIp")){
	    isLocal = (Boolean)application.getAttribute("isLocalIp") ? 1 : 0;
	}
	if(null != application.getAttribute("isConfEmailServer")){
	    isConfEmailServ = (Boolean)application.getAttribute("isConfEmailServer") ? 1 : 0;
	}
	
	String isActivate = ConfDataFormat.getSysConfEmailEnabled()?"true":"false";// 是否需要邮件激活
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>
<view:LanguageTag key="system_noun_name"/>
</title>
<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
<link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
<link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
<link href="<%=path%>/install/css/install.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
<script type="text/javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.6.4.min.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" ></script>
<script type="text/javascript">
	$(document).ready(function(){
		//窗体大小改变时要重新设置透明文件框的位置
		$(window).resize(initFileInputDivNoParame);
		//初始化透明文件框的位置
		initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv');
	
		$.formValidator.initConfig({submitButtonID:"upLicBtn",debug:true,
			onSuccess:function(){
				upLicFile();
			},
			onError:function(){
				return false;
		}});
		$("#licFile").formValidator({onFocus:'<view:LanguageTag key="sys_lic_vd_file"/>',onCorrect:"OK"}).inputValidator({min:1,onError:'<view:LanguageTag key="sys_lic_vd_file_err"/>'}).functionValidator({
			fun:function(fileUp){
				var fileUp = $.trim($("#licFile").val());
				var fname = fileUp.substring(fileUp.lastIndexOf(".") + 1);
				fname = fname.toLowerCase();
				if(fname != "lic"){
					return '<view:LanguageTag key="sys_lic_vd_file_err_1"/>';
				}
				return true;
			}
		});
	});
	
	//上传授权
	function upLicFile(ajaxbg, val){
		$.ligerDialog.confirm('<view:LanguageTag key="sys_lic_vd_confirm_file"/>', function (yes){
			if(yes){
			    var ajaxbg = $("#background,#progressBar");//加载等待
	         	ajaxbg.show();
			    $("#UpLicForm").ajaxSubmit({
					type: "POST",
					url: "<%=path%>/manager/lic/license!upLic.action",
					async: false,
					data: {},
					dataType: "json",
					success: function(msg){			
						ajaxbg.hide();
				        var errorStr = msg.errorStr;
				        if(errorStr == 'success'){
				        	parent.hideId('05', 'T05');
		                	nextPage();
		                }else{
		                	FT.toAlert(errorStr, msg.object, null);
		                }
					}
				});
			}
		});
	}

	//下一步
	function nextPage(){
		if('<%=isActivate%>'=='true'){//需要邮件激活
			if('<%=isConfEmailServ%>'=='1'){//邮件服务器已配
				nextAdmAndIp();
			}else{
				parent.toPage("frameView09", "<%=path%>/manager/confinfo/email/email!initFind.action");
				parent.showId('09', 'T09');
			}
		}else{
			nextAdmAndIp();
		}
		
		/** old nextpage method
		if('<%=isAdmin%>' == '1' && '<%=isLocal%>' == '0'){//完成初始化
			parent.toPage("frameView08", "/install/install!finish.action");
			parent.showId('08', 'T08');
		}else if('<%=isAdmin%>' == '1' && '<%=isLocal%>' == '1'){//IP修改选择
			parent.showId('07', 'T07');
		}else if('<%=isAdmin%>' == '0'){//创建管理员
			parent.toPage("frameView06", "/install/adminpage.jsp");
			parent.showId('06', 'T06');
		}**/
	}
	
	//根据 管理员 ip判断下一步页面
	function nextAdmAndIp(){
		if('<%=isAdmin%>' == '1'){ //管理员已配置
			if('<%=isLocal%>' == '1'){//IP修改选择
				parent.showId('07', 'T07');
			}else{//完成
				parent.toPage("frameView08", "/install/install!finish.action");
				parent.showId('08', 'T08');
			}
		}else{
			parent.toPage("frameView06", "/install/adminpage.jsp");
			parent.showId('06', 'T06');
		}
	}

	function backPage(){
		parent.showId('02', 'T02');
		parent.hideId('05', 'T05');
		parent.$('#T03').hide();
		parent.hideId('05', 'T05');
		parent.$('#T05').hide();
	}

	
</script>
</head>
<body onresize="initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv')">
<div id="background" class="background" style="display: none; "></div>
<div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
<form action="" method="post" enctype="multipart/form-data" id="UpLicForm" name="UpLicForm">
  <table width="100%" border="0" cellspacing="1" cellpadding="5" style="margin-top:5px" class="tableCss">
    <tr>
      <td width="25%" align="right">&nbsp;</td>
      <td width="40%"><strong><view:LanguageTag key="sys_lic_sel"/><view:LanguageTag key="colon"/></strong></td>
      <td width="35%"></td>
    </tr>
    <tr>
      <td align="right"><view:LanguageTag key="sys_lic_file"/><view:LanguageTag key="colon"/></td>
      
      <td id="fileTd">
       	<input type="text" id="showVal" onresize="initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv')" readonly/>
        &nbsp;&nbsp;<div style="z-index:10;display: inline;"><span class="Button" id="showDiv"><a id="setShowValInp" href="javascript:"><view:LanguageTag key="common_syntax_select"/></a></span></div>
        <div id="fileDiv" style="position:absolute;margin:0 0 0 -38px;z-index:50;filter:alpha(opacity=0);moz-opacity: 0;-khtml-opacity: 0;opacity:0;">
        <input name="licFile" id="licFile"  type="file" size="1" style="height: 24px;" onchange="setFullPath(this,'showVal');"/></div>
      </td>
      
      
      <td>
      <div id="licFileTip" class="divTipCss"></div></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td colspan="2">
      	<a href="javascript:backPage();" id="backBt" class="button"><span><view:LanguageTag key="common_syntax_preStep"/></span></a>
      	<a href="#" id="upLicBtn" class="button"><span><view:LanguageTag key="sys_lic_upload"/></span></a>
      </td>
    </tr>
  </table>
</form>
</body>
</html>