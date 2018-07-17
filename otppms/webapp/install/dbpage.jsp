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
<link href="<%=path%>/install/css/install.css" rel="stylesheet" type="text/css" />
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
<!--
	var mark = true;
	$(function (){
		if("" == '${dbConfInfo.dbtype}'){
			mark = false;
			dbInfoValue('mysql');
		}else {
			if("oracle" == '${dbConfInfo.dbtype}'){
				if("y" == '${dbConfInfo.dual}') {
					$("#dual").attr("checked", true);
					$("tr[id='racTR']").show();
					$("tr[id='racnameTR']").show();
					$("tr[id='ip2TR']").show();
					$("tr[id='dbnameTR']").hide();
					$("tr[id='sIdTR']").hide();
					document.getElementById("IPText").innerHTML = '<view:LanguageTag key="dbconf_ip_1"/><view:LanguageTag key="colon"/>';
				}else {
					$("tr[id='racTR']").show();
					$("tr[id='racnameTR']").hide();
					$("tr[id='ip2TR']").hide();
					$("tr[id='dbnameTR']").hide();
					$("tr[id='sIdTR']").show();
					document.getElementById("IPText").innerHTML = '<view:LanguageTag key="dbconf_ip"/><view:LanguageTag key="colon"/>';
				}
			}else {
				$("tr[id='racTR']").hide();
				$("tr[id='dbnameTR']").show();
				$("tr[id='sIdTR']").hide();
				$("tr[id='racnameTR']").hide();
				$("tr[id='ip2TR']").hide();
				document.getElementById("IPText").innerHTML = '<view:LanguageTag key="dbconf_ip"/><view:LanguageTag key="colon"/>';
			}
		}
		
		//初始化隐藏
		$("#dbconfTR").hide();
		verifyPortalDir();
		verifyAuthDir();
		
    	$.formValidator.initConfig({submitButtonID:"testDbConn", 
			onSuccess:function(){
				testConn();
			},
			onError:function(){
				return false;
			}});
			
			$("#dbtype").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0, onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
			var dbname = $("#dbname").val();
			$("#dbname").formValidator({onFocus:'<view:LanguageTag key="dbconf_vd_dbname"/>',onCorrect:"OK"}).inputValidator({min:1,max:64, onError:'<view:LanguageTag key="dbconf_vd_dbname_err"/>'}).functionValidator({
				fun:function(dbname){
			    if(g_invalid_char_js(dbname)){
			       return "<view:LanguageTag key="dbconf_vd_dbname_err_1"/>";
			    }
			    return true;
			}});
			var sId = $("#sId").val();
			$("#sId").formValidator({onFocus:'<view:LanguageTag key="dbconf_vd_sid"/>',onCorrect:"OK"}).inputValidator({min:1,max:128, onError:'<view:LanguageTag key="dbconf_vd_dbname_err"/>'}).functionValidator({
				fun:function(sId){
			    if(g_invalid_char_js(sId)){
			       return "<view:LanguageTag key="dbconf_vd_sid_err"/>";
			    }
			    return true;
			}});
			$("#racname").formValidator({onFocus:'<view:LanguageTag key="dbconf_vd_rac"/>',onCorrect:"OK"}).inputValidator({min:1,max:128, onError:'<view:LanguageTag key="dbconf_vd_dbname_err"/>'}).regexValidator({regExp:"username",dataType:"enum",onError:'<view:LanguageTag key="dbconf_vd_rac_err"/>'});
			$("#dbip").formValidator({onFocus:'<view:LanguageTag key="dbconf_vd_init_ip"/>',onCorrect:"OK"}).regexValidator({regExp:"ip4",dataType:"enum",onError:'<view:LanguageTag key="dbconf_vd_ip_err"/>'}); 
			$("#viceip").formValidator({onFocus:'<view:LanguageTag key="dbconf_vd_init_ip"/>',onCorrect:"OK"}).regexValidator({regExp:"ip4",dataType:"enum",onError:'<view:LanguageTag key="dbconf_vd_ip_err"/>'}).compareValidator({desID:"dbip",operateor:"!=",onError:'<view:LanguageTag key="dbconf_vd_ip2_err"/>'}); 
			$("#dbport").formValidator({onFocus:'<view:LanguageTag key="dbconf_vd_port"/>',onCorrect:"OK"}).inputValidator({min:1,max:65535, onError:'<view:LanguageTag key="dbconf_vd_dbport_err"/>'}).regexValidator({regExp:"num",dataType:"enum",onError:'<view:LanguageTag key="dbconf_vd_port_err"/>'});
			var username = $("#username").val();
			$("#username").formValidator({onFocus:'<view:LanguageTag key="dbconf_vd_username"/>',onCorrect:"OK"}).inputValidator({min:1,max:64,onError:'<view:LanguageTag key="dbconf_vd_username_err"/>'}).functionValidator({
				fun:function(username){
			    if(g_invalid_char_js(username)){
			       return "<view:LanguageTag key="dbconf_vd_username_err_1"/>";
			    }
			    return true;
			}});
			$("#passwd").formValidator({onFocus:'<view:LanguageTag key="dbconf_vd_pwd"/>',onCorrect:"OK"}).inputValidator({min:4,max:64, onError:'<view:LanguageTag key="dbconf_vd_pwd_err"/>'});
			//$("#dbtype").focus();
			var dbtype = $("#dbtype").find("option:selected").val();
			var dualCheck = $("#dual").attr("checked");
			if ("oracle" == dbtype) {
				if(dualCheck) {
					$("#dbname").unFormValidator(true); 
					$("#sId").unFormValidator(true); 
					$("#racname").unFormValidator(false); 
					$("#viceip").unFormValidator(false); 	
				}else {
					$('#viceip').val("127.0.0.1");
					$("#dbname").unFormValidator(true);
					$("#sId").unFormValidator(false);
					$("#racname").unFormValidator(true);
					$("#viceip").unFormValidator(true);
				}
			}else {
				$('#viceip').val("127.0.0.1");
				$("#dbname").unFormValidator(false); 
				$("#sId").unFormValidator(true); 
				$("#racname").unFormValidator(true); 
				$("#viceip").unFormValidator(true); 
			}
    });
    
    //根据验证用户门户，认证服务是否存在，设置显示隐藏
    function setCheck() {
    	var portalFlag = $("#protaldbconf").val();
		var authFlag = $("#authdbconf").val();
		if (portalFlag == "0" && authFlag == "0") {
			$("tr[id='dbconfTR']").hide();
		} else {
			if(portalFlag == "0") {
				$("#portalDiv").hide();
			}else {
				$("#dbconfTR").show();
				$("#portalDiv").show();
				$("#protaldbconf").attr("checked", true);
			}
			if(authFlag == "0") {
				$("#authDiv").hide();
			}else {
				$("#dbconfTR").show();
				$("#authDiv").show();
				$("#authdbconf").attr("checked", true);	
			}
			
		}
    }
    
    //判断用户门户是否存在
    function verifyPortalDir() {
    	var url = '<%=path%>/install/install!porAPPIsExists.action';
       	$.ajax({
			type: "POST",
		    url: url,
		    dataType: "json",	    
		    success: function(msg){
		    	$("#protaldbconf").val(msg);
			}
		});
    }
    
    //判断认证服务是否存在
    function verifyAuthDir() {
    	var url = '<%=path%>/install/install!authSerIsExists.action';
       	$.ajax({
			type: "POST",
		    url: url,
		    dataType: "json",	    
		    success: function(msg){
		    	$("#authdbconf").val(msg);
			}
		});
    }
    
    //根据判断设置属性值
    function setHidVal() {
    	var dbtype = $("#dbtype").find("option:selected").val();
		var dualCheck = $("#dual").attr("checked");
		var dbname;
		if ("oracle" == dbtype) {
			if(dualCheck) {
				dbname = $("#racname").val();
				$("#dualarc").val("y"); 
			}else {
				dbname = $("#sId").val();
				$("#dualarc").val("n"); 
			}
		}else {
			dbname = $("#dbname").val();
			$("#dualarc").val("n"); 
		}
		$("#savedbname").val(dbname);
    }

	//数据库连接测试
	function testConn(){		
		setHidVal();
		var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
		$("#DBSetForm").ajaxSubmit({
			type: "POST",
			url: "<%=path%>/install/install!testConn.action",
			async: true,
			data: {},
			dataType: "json",
			success: function(msg){
				if(msg == "0"){//连接成功，并且不需要创建数据库表
					$("#importDb").hide();
					$("#svaeDbInfo").show();
					$("#checkInfo").attr('class','OKMsg');
					$("#checkInfo").html('<view:LanguageTag key="sys_init_db_conn_succ_next"/>');	
					
					setCheck();
				}else if(msg == "1"){//数据库连接失败
					$("#importDb").hide();
					$("#svaeDbInfo").hide();
					$("#checkInfo").attr('class','WarningMsg');
					$("#checkInfo").html('<view:LanguageTag key="sys_init_db_conn_err_check"/>');
				}else if(msg == "2"){//连接成功，提示创建数据库表
					$("#importDb").show();
					$("#svaeDbInfo").hide();
					$("#checkInfo").attr('class','WarningMsg');
					$("#checkInfo").html('<view:LanguageTag key="sys_init_db_conn_succ_create_tab"/>');
					
					setCheck();
				}
				ajaxbg.hide();
			}
		});
	}

	//创建数据库表结构	
	function importDB(){
		var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
		$("#DBSetForm").ajaxSubmit({
			type: "POST",
			url: "<%=path%>/install/install!importDB.action",
			async: true,
			data: {},
			dataType: "json",
			success: function(msg){
				if(msg == "0"){//数据库表结构创建成功
					$("#importDb").hide();
					$("#svaeDbInfo").show();
					$("#checkInfo").attr('class','OKMsg');
					$("#checkInfo").html('<view:LanguageTag key="sys_init_create_tab_succ_next"/>');		
				}else if(msg == "1"){//数据库连接失败
					$("#importDb").hide();
					$("#svaeDbInfo").hide();
					$("#checkInfo").attr('class','WarningMsg');
					$("#checkInfo").html('<view:LanguageTag key="sys_init_db_conn_err_check"/>');
				}else if(msg == "2"){//未找到对应的SQL脚本文件！
					$("#importDb").show();
					$("#svaeDbInfo").hide();
					$("#checkInfo").attr('class','WarningMsg');
					$("#checkInfo").html('<view:LanguageTag key="sys_init_not_find_sql_file"/>');
				}else if(msg == "3"){//创建数据库表结构失败
					$("#importDb").show();
					$("#svaeDbInfo").hide();
					$("#checkInfo").attr('class','WarningMsg');
					$("#checkInfo").html('<view:LanguageTag key="sys_init_create_tab_err_retry"/>');
				}
				ajaxbg.hide();
			}
		});		
	}
	
	//保存数据库配置
	function saveDbConf(){
		servIfReload();	
		setHidVal();		
		var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
		var selType = '${param.selType}';
		$("#DBSetForm").ajaxSubmit({
			type: "POST",
			url: "<%=path%>/install/install!saveDbConf.action",
			async: true,
			data: {selType:selType},
			dataType: "json",
			success: function(msg){
				if(msg == "0"){//保存成功
					nextIni();
				}else if(msg == "1"){//数据库连接失败
					$("#importDb").hide();
					$("#svaeDbInfo").show();
					$("#checkInfo").attr('class','WarningMsg');
					$("#checkInfo").html('<view:LanguageTag key="sys_init_db_conn_err_check"/>');
				}else if(msg == "2"){//保存失败
					$("#importDb").hide();
					$("#svaeDbInfo").show();
					$("#checkInfo").attr('class','WarningMsg');
					$("#checkInfo").html('<view:LanguageTag key="sys_init_save_db_conf_err_retry"/>');
				}else if(msg == "3"){//初始化完成
					finish();
				}else if(msg == "4"){//IP修改选择
					ipPage();
				}else if(msg == "5"){//创建管理员
					adminPage();
				}else if(msg=="7"){//配置邮件服务器
					emailServPage();
				}else{
					FT.toAlert("warn", '<view:LanguageTag key="sys_install_db_reconf_warn"/>', null);
				}
				
				if(msg != null && msg != "" && msg != "1" && msg != "2"){
					parent.hideId('03', 'T03');
				}
				ajaxbg.hide();
			}
		});
	}
	
	//获取服务器是否重启
	function servIfReload(){
		var url= "<%=path%>/install/install!getServState.action";
		$("#DBSetForm").ajaxSubmit({
			async:false,    
			dataType : "text",  
			type:"POST", 
			url:url,
			success:function(msg){
				if(msg == ''){
					window.parent.location.href = "<%=path%>/install/index.jsp";
				}
			}
		});
	}

	//设置不同数据库的默认信息
	function dbInfoValue(obj){
		if(obj == "mysql"){
	    	$("#dbip").val("127.0.0.1");
	    	$("#dbport").val("3306");
	    	$("#dbname").val("");
	    	$("#username").val("");
	    	$("#passwd").val("");
	    	setUnSelOracle();
	    }else if(obj == "postgresql"){
	    	$("#dbip").val("127.0.0.1");
	    	$("#dbport").val("5432");
	    	$("#dbname").val("");
	    	$("#username").val("");
	    	$("#passwd").val("");
	    	setUnSelOracle();
	    }else if(obj == "oracle"){
	    	$("#dbip").val("127.0.0.1");
	    	$("#dbport").val("1521");
	    	$("#dbname").val("");
	    	$("#username").val("");
	    	$("#passwd").val("");
	    	setSelOracle();
	    }else if(obj == "sqlserver"){
	    	$("#dbip").val("127.0.0.1");
	    	$("#dbport").val("1433");
	    	$("#dbname").val("");
	    	$("#username").val("");
	    	$("#passwd").val("");
	    	setUnSelOracle();
	    }
	}

	document.onkeydown = function(evt){
		var evt = window.event?window.event:evt;
		if(evt.keyCode==13) {
			if(!testConn()){
				return false;
			}else{
				if(!saveDbConf()){
					return false;
				}
			}
		}
	}
	
	//选择Oracle时设置状态
	function setSelOracle() {
		if("y" == '${dbConfInfo.dual}') {
			$("#dual").attr("checked", true);
			$("tr[id='racTR']").show();
			$("tr[id='racnameTR']").show();
			$("tr[id='ip2TR']").show();
			$("tr[id='dbnameTR']").hide();
			$("tr[id='sIdTR']").hide();
			document.getElementById("IPText").innerHTML = '<view:LanguageTag key="dbconf_ip_1"/><view:LanguageTag key="colon"/>';
			
			$("#dbname").unFormValidator(true); 
			$("#sId").unFormValidator(true); 
			$("#racname").unFormValidator(false); 
			$("#viceip").unFormValidator(false); 
		}else {
			changeVal();
		}
	}
	
	//选择除Oracle数据库外的
	function setUnSelOracle() {
		$("tr[id='dbnameTR']").show();
		$("tr[id='racTR']").hide();
		$("tr[id='sIdTR']").hide();
		$("tr[id='racnameTR']").hide();
		$("tr[id='ip2TR']").hide();
		document.getElementById("IPText").innerHTML = '<view:LanguageTag key="dbconf_ip"/><view:LanguageTag key="colon"/>';
		if (mark) {
			$('#viceip').val("127.0.0.1");
			$("#dbname").unFormValidator(false); 
			$("#sId").unFormValidator(true); 
			$("#racname").unFormValidator(true); 
			$("#viceip").unFormValidator(true); 
		}
		mark = true;
	}
	
	//单击RAC复选框操作
	function changeVal() {
		var dualCheck = $("#dual").attr("checked");
		if (dualCheck) {
			$("tr[id='racTR']").show();
			$("tr[id='racnameTR']").show();
			$("tr[id='ip2TR']").show();
			$("tr[id='dbnameTR']").hide();
			$("tr[id='sIdTR']").hide();
			document.getElementById("IPText").innerHTML = '<view:LanguageTag key="dbconf_ip_1"/><view:LanguageTag key="colon"/>';
			
			$("#dbname").unFormValidator(true); 
			$("#sId").unFormValidator(true); 
			$("#racname").unFormValidator(false); 
			$("#viceip").unFormValidator(false); 
			$("#dualarc").val("y");
		} else {
			$("tr[id='racTR']").show();
			$("tr[id='racnameTR']").hide();
			$("tr[id='ip2TR']").hide();
			$("tr[id='dbnameTR']").hide();
			$("tr[id='sIdTR']").show();
			document.getElementById("IPText").innerHTML = '<view:LanguageTag key="dbconf_ip"/><view:LanguageTag key="colon"/>';
			$('#viceip').val("127.0.0.1");
			$("#dbname").unFormValidator(true); 
			$("#sId").unFormValidator(false); 
			$("#racname").unFormValidator(true); 
			$("#viceip").unFormValidator(true); 
			$("#dualarc").val("n");
		}
	}
	
	//下一步
	function nextIni(){
		parent.toPage("frameView05", "/install/uploadLic.jsp");
		parent.showId('05', 'T05');
	}
	
	//IP修改选择
	function ipPage(){
		parent.toPage("frameView07", "/install/local_ip.jsp");
		parent.showId('07', 'T07');
	}
	
	//配置邮件服务器页面
	function emailServPage(){
		parent.toPage("frameView09", "<%=path%>/manager/confinfo/email/email!initFind.action");
		parent.showId('09', 'T09');
	}
	
	//添加管理员页面
	function adminPage(){
		parent.toPage("frameView06", "/install/adminpage.jsp");
		parent.showId('06', 'T06');
	}
	
	//完成
	function finish(){
		parent.toPage("frameView08", "/install/install!finish.action");
		parent.showId('08', 'T08');
	}

	//返回
	function backPage(){
		parent.showId('02', 'T02');
		parent.hideId('03', 'T03');	
		parent.$('#T03').hide();
		parent.toPage("frameView02", "/install/init_select.jsp");
	}
//-->
</script>
</head>
<body >
<div id="background" class="background" style="display: none; "></div>
<div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="sys_init_perform_db_oper"/></div>
<form id="DBSetForm" name="DBSetForm" method="post" action="">
  <table width="100%" border="0" cellpadding="0" cellspacing="0" style="margin-top:5px">
    <tr>
      <td width="30%" align="right"><view:LanguageTag key="dbconf_type"/><view:LanguageTag key="colon"/></td>
      <td width="35%" align="left">
        <select id="dbtype" name="dbConfInfo.dbtype" class="select100" onChange="dbInfoValue(this.value);">
          <option value="mysql" <c:if test="${dbConfInfo.dbtype eq 'mysql'}">selected</c:if> >MySQL</option>
      	</select>
      </td>
      <td width="35%" class="divTipCss">
      <div id="dbtypeTip" class="divTipCss"></div></td>
    </tr>
    <tr id="racTR">
      <td>&nbsp;</td>
      <td align="left">
      <input type="checkbox" id="dual"  onclick="changeVal();" <c:if test="${dbConfInfo.dual eq 'y' }">checked</c:if> />&nbsp;<view:LanguageTag key="dbconf_rac_mode"/>
      <input type="hidden" id="dualarc" name="dbConfInfo.dual" value="${dbConfInfo.dual}"  />
      <td></td>
    </tr>
    <tr id="dbnameTR">
      <td align="right"><view:LanguageTag key="dbconf_dbname"/><view:LanguageTag key="colon"/></td>
      <td align="left">
      <input id="dbname" type="text" class="formCss100" value="${dbConfInfo.dbname}" /></td>
      <td class="divTipCss"><div id="dbnameTip" class="divTipCss"></div></td>
    </tr>
    <tr id="sIdTR">
      <td align="right">SID<view:LanguageTag key="colon"/></td>
      <td align="left">
      <input id="sId"  type="text" class="formCss100" value="${dbConfInfo.dbname}" /></td>
      <td class="divTipCss"><div id="sIdTip" class="divTipCss"></div></td>
    </tr>
    <tr id="racnameTR">
      <td align="right"><view:LanguageTag key="dbconf_rac_name"/><view:LanguageTag key="colon"/></td>
      <td align="left">
      <input id="racname" type="text" class="formCss100" value="${dbConfInfo.dbname}" /></td>
      <td class="divTipCss"><div id="racnameTip" class="divTipCss"></div></td>
    </tr>
    <tr>
      <td align="right"><span id="IPText">&nbsp;</span></td>
      <td align="left">
      <input id="dbip" name="dbConfInfo.ip" type="text" class="formCss100" value="${dbConfInfo.ip}" /></td>
      <td class="divTipCss"><div id="dbipTip" class="divTipCss"></div></td>
    </tr>
    <tr id="ip2TR">
      <td align="right"><view:LanguageTag key="dbconf_ip_2"/><view:LanguageTag key="colon"/></td>
      <td align="left">
      <input id="viceip" name="dbConfInfo.viceip" type="text" class="formCss100" value="${dbConfInfo.viceip}" /></td>
      <td class="divTipCss"><div id="viceipTip" class="divTipCss"></div></td>
    </tr>
    <tr>
      <td align="right"><view:LanguageTag key="dbconf_port"/><view:LanguageTag key="colon"/></td>
      <td align="left">
      <input id="dbport" name="dbConfInfo.port" type="text" class="formCss100" value="${dbConfInfo.port}" /></td>
      <td class="divTipCss"><div id="dbportTip" class="divTipCss"></div></td>
    </tr>
    <tr>
      <td align="right"><view:LanguageTag key="dbconf_username"/><view:LanguageTag key="colon"/></td>
      <td align="left">
      <input id="username" name="dbConfInfo.username" type="text" value="${dbConfInfo.username}" class="formCss100" />
      <input id="savedbname" type="hidden" name="dbConfInfo.dbname" value="" />
      </td>
      <td class="divTipCss"><div id="usernameTip" class="divTipCss"></div></td>
    </tr>
    <tr>
      <td align="right"><view:LanguageTag key="dbconf_pwd"/><view:LanguageTag key="colon"/></td>
      <td align="left">
      <input id="passwd" onpaste="return false" name="dbConfInfo.passwd" type="password" class="formCss100" /></td>
      <td class="divTipCss"><div id="passwdTip" class="divTipCss"></div></td>
    </tr>
    <tr>
      <td align="right">&nbsp;</td>
      <%--检测数据库--%>
      <td height="-1" align="left"><a href="#" id="testDbConn" class="button"><span><view:LanguageTag key="common_syntax_test_conn"/></span></a></td>
      <td height="-1" align="left">&nbsp;</td>
    </tr>
    <tr>
      <td height="32" align="right" bgcolor="#FFFFFF"><span class="isTextRed"><view:LanguageTag key="sys_init_results_back"/><view:LanguageTag key="colon"/></span></td>
      <td colspan="2" align="left" bgcolor="#FFFFFF"><span id="checkInfo">&nbsp;</span></td>
    </tr>
    <tr id="dbconfTR">
      <td align="right"></td>
      <td colspan="2">
      	<view:LanguageTag key="sys_init_update_db_conn_conf_tip"/>
      	<div id="portalDiv">
	      	<input type="checkbox" id="protaldbconf" name="dbConfInfo.protaldbconf" value="" />&nbsp;&nbsp;<view:LanguageTag key="sys_app_otpportal"/>&nbsp;&nbsp;&nbsp;&nbsp;
      	</div>
      	<div id="authDiv">
	      	<input type="checkbox" id="authdbconf" name="dbConfInfo.authdbconf" value="" />&nbsp;&nbsp;<view:LanguageTag key="sys_app_authservice"/>
      	</div>
      </td>
    </tr>
    <tr>
      <td align="right">&nbsp;</td>
      <td colspan="2" align="left">
      <a href="javascript:backPage();" id="backBt" class="button"><span><view:LanguageTag key="common_syntax_preStep"/></span></a>
      <a href="javascript:importDB();" id="importDb" class="button" style="display:none"><span><view:LanguageTag key="sys_init_create_date_tab"/></span></a>
      <a href="javascript:saveDbConf();" id="svaeDbInfo" class="button" style="display:none"><span><view:LanguageTag key="common_syntax_nextStep"/></span></a></td>
    </tr>
  </table>
</form>
</body>
</html>
