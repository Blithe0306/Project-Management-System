<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
<script src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
<script language="javascript" type="text/javascript">
	//设置本机真实IP
	function setLocalIp(){
		var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
		var ipSel = "";
		if($("#inDiv").css("display")!="none"&&$("#selectIpSel").val()=='0'){
			ipSel = $("#inputIpSel").val();
			
			//ip校验
			if($.trim(ipSel)==""){
				FT.toAlert('error', '<view:LanguageTag key="sys_init_please_in_real_ip"/>', null);
				return;
			}else{
				var isok = checkIpAddr(ipSel);
				if(!isok){
					FT.toAlert('error', '<view:LanguageTag key="auth_ser_vd_hostip_err_1"/>', null);
					return;
				}
			}
		}else{
			ipSel = $("#selectIpSel").val();
		}
		
		$("#IpSetForm").ajaxSubmit({
			type: "POST",
			url: "<%=path%>/manager/authmgr/server/authServer!modifyLocalIp.action",
			async: false,
			data: {ipSel:ipSel},
			dataType: "text",
			success: function(msg){
				if(msg == "success"){//设置成功					
					nextPage();
					reloadConf();
				}else{//设置失败
					FT.toAlert('error', '<view:LanguageTag key="sys_init_set_rela_ip_err"/>', null);
				}
				ajaxbg.hide();
			}
		});
	}

	//下一页
	function nextPage(){
		parent.toPage("frameView08", "/install/install!finish.action");
		parent.hideId('07', 'T07');
		parent.showId('08', 'T08');
	}
	
	//切换ip
	function changeIp(val){
		if(val==0){
			$("#inDiv").show();
			$("#selDiv").hide();
		}
	}
	
	//重新加载配置请求，分开处理
	function reloadConf(){		
		var url = "<%=path%>/manager/confinfo/config/center!reloadConf.action";
		$.ajax({
			type: "POST",
			url: url,
			data: {},
			dataType: "json",	    
			success: function(msg){				
			}
		});
	}
</script>
</head>
<body>
<form id="IpSetForm" name="IpSetForm" method="post" action="">
  <table width="100%" border="0" cellpadding="4" cellspacing="0" style="margin-top:5px">
    <tr>
      <td width="10%" align="right">&nbsp;</td>
      <td colspan="2" align="left"><strong><view:LanguageTag key="sys_init_local_real_ip_sel"/><view:LanguageTag key="colon"/></strong><br/>
        <view:LanguageTag key="sys_init_sel_real_ip_tip"/></td>
    </tr>
    <tr>
      <td width="10%" align="right">&nbsp;</td>
      <td width="45%" align="left">
      	<div id="inDiv" style="display:none"><input type="text" id="inputIpSel" class="formCss100"/></div>
        <div id="selDiv"><select id="selectIpSel" name="selectIpSel" class="select100" onchange="changeIp(this.value)">
          <view:LocalIpTag dataSrc = "" />
          <option value="0"><view:LanguageTag key="sys_init_in_real_ip"/></option>
        </select></div>
      </td>
      <td width="45%" align="left">&nbsp;</td>
    </tr>
    <tr>
      <td align="right">&nbsp;</td>
      <td colspan="2" align="left"><a href="javascript:setLocalIp();" id="nextBt" class="button"><span><view:LanguageTag key="common_syntax_nextStep"/></span></a> </td>
    </tr>
  </table>
</form>
</body>
</html>
