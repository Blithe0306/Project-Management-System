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
	<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css" />
	<link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css" />
	<link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" id="openwincss" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"/>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>    
	<script language="javascript" type="text/javascript">
		var addWin;
		$(document).ready(function(){
			$("tr[id='accessIPDuan']").hide();
			
			$.formValidator.initConfig({submitButtonID:"okBtn", 
			onSuccess:function(){
				addObj();
			},
			onError:function(){
				return false;
			}});
			
			$("#allowIP").formValidator({onFocus:'<view:LanguageTag key="trustip_vd_accip_addr_show"/>',onCorrect:"OK"}).regexValidator({regExp:"ip4",dataType:"enum",onError:'<view:LanguageTag key="trustip_vd_accip_addr_err"/>'})
			.functionValidator({fun:function(val){
				 var showtip = checkAllowIP(val);
				 if (showtip != 'success') {
				 	return showtip;
				 }else {
				 	return true;
				 }
			}});
			
			$("#startip").formValidator({tipID:"IPduanTip",onFocus:'<view:LanguageTag key="trustip_vd_startip_show"/>',onCorrect:"OK"}).regexValidator({regExp:"ip4",dataType:"enum",onError:'<view:LanguageTag key="trustip_vd_startip_err"/>'})
			.functionValidator({fun:function(val){
				 var endip = $("#endip").val();
				 if (endip != '') {
				 	 var returnval = vdstartendip(val, endip);
				 	 if (returnval != 'success') {
					 	return returnval;
					 }else {
						 var showtip = checkStartIP(val);
						 if (showtip != 'success') {
						 	return showtip;
						 }else {
						 	return true;
						 }
					 }
				 } else {
				 	 var showtip = checkStartIP(val);
					 if (showtip != 'success') {
					 	return showtip;
					 }else {
					 	return true;
					 }
				 }
			}});
			$("#endip").formValidator({tipID:"IPduanTip",onFocus:'<view:LanguageTag key="trustip_vd_endip_show"/>',onCorrect:"OK"}).regexValidator({regExp:"ip4",dataType:"enum",onError:'<view:LanguageTag key="trustip_vd_endip_err"/>'})
			.functionValidator({fun:function(val){
				 var startip = $("#startip").val();
				 if(!checkIpAddr(startip)) {
				 	return '<view:LanguageTag key="trustip_vd_startip_err"/>';
				 }
				 if (startip != '') {
				 	 var returnval = vdstartendip(startip, val);
				 	 if (returnval != 'success') {
					 	return returnval;
					 }else {
						 var showtip = checkEndIP(val);
						 if (showtip != 'success') {
						 	return showtip;
						 }else {
						 	return true;
						 }
					 }
				 } else {
				 	 var showtip = checkEndIP(val);
					 if (showtip != 'success') {
					 	return showtip;
					 }else {
					 	return true;
					 }
				 }
			}});
			$("#allowIP").focus();
			$("#startip").unFormValidator(true); 
			$("#endip").unFormValidator(true); 
		});
		
		function vdstartendip(startip, endip) {
			var reval = 'success';
			var sattr = startip.split(".");
			var eattr = endip.split(".");
			if (startip == endip) {
		   		reval = '<view:LanguageTag key="trustip_vd_start_end_ip_err"/>';
			}
			if (sattr[0] != eattr[0] || sattr[1] != eattr[1]) {
		   		reval = '<view:LanguageTag key="trustip_vd_start_end_ip_err_1"/>';
			}
			if (parseInt(sattr[2],10) > parseInt(eattr[2],10)) {
		   		reval = '<view:LanguageTag key="trustip_vd_start_end_ip_err_2"/>';
			}
			if ((parseInt(sattr[2],10) == parseInt(eattr[2],10)) && (parseInt(sattr[3],10) > parseInt(eattr[3],10))) {
		   		reval = '<view:LanguageTag key="trustip_vd_start_end_ip_err_2"/>';
			}
			return reval;
	 	}
	 	
	 	//校验IP是否已存在
	 	function checkAllowIP(val) {
	 		var reval = 'success';
	 		var iplist = getSelIpText();	
			$.ajax({ 
				async:false,
				type:"POST",
				url:"<%=path%>/manager/confinfo/config/access!findIPIsExist.action",
				data:{"allowIP" : val, "allowIPList" : iplist},
				dataType:"json",
				success:function(msg){
	                if(msg.errorStr == 'error') {
						reval = '<view:LanguageTag key="trustip_checkip_err"/>';
					}else {
						if(msg.object != "SUCCESS") {
							reval = msg.object;
						}else {
							reval = 'success';
						}
				    }
				}
			});
			return reval;
		}
		//校验起始IP是否已经存在
	 	function checkStartIP(val) {
	 		var reval = 'success';
	 		var iplist = getSelIpText();	
			$.ajax({ 
				async:false,
				type:"POST",
				url:"<%=path%>/manager/confinfo/config/access!findSIPIsExist.action",
				data:{"startIP" : val, "allowIPList" : iplist},
				dataType:"json",
				success:function(msg){
					if(msg.errorStr == 'error') {
						reval = '<view:LanguageTag key="trustip_checkip_err"/>';
					}else {
						if(msg.object != "SUCCESS") {
							reval = msg.object;
						}else {
							reval = 'success';
						}
				    }
				}
			});
			return reval;
		}
		//校验结束IP是否存在
	 	function checkEndIP(val) {
	 		var iplist = getSelIpText();
	 		var reval = 'success';
			$.ajax({ 
				async:false,
				type:"POST",
				url:"<%=path%>/manager/confinfo/config/access!findEIPIsExist.action",
				data:{"endIP" : val, "allowIPList" : iplist},
				dataType:"json",
				success:function(msg){
					if(msg.errorStr == 'error') {
						reval = '<view:LanguageTag key="trustip_checkip_err"/>';
					}else {
						if(msg.object != "SUCCESS") {
							reval = msg.object;
						}else {
							reval = 'success';
						}
				    }
				}
			});
			return reval;
		}
		
		//校验开始结束IP是否存在
	 	function checkStartEndIP() {
	 		var startip = $("#startip").val();
			var endip = $("#endip").val();
			var iplist = getSelIpText();
	 		var reval = 'success';
			$.ajax({ 
				async:false,
				type:"POST",
				url:"<%=path%>/manager/confinfo/config/access!checkIPDExist.action",
				data:{"startip":startip, "endip":endip, "allowIPList" : iplist},
				dataType:"json",
				success:function(msg){
					if(msg.errorStr == 'error') {
						reval = '<view:LanguageTag key="trustip_checkip_err"/>';
					}else {
						if(msg.object != "SUCCESS") {
							reval = msg.object;
						}else {
							reval = 'success';
						}
				    }
				}
			});
			return reval;
		}
		
		function addObj() {
			var returnIp;
			if($("#selIP").attr("checked") == true){
				var reval = checkStartEndIP();
				if (reval != 'success') {
					$.formValidator.setFailState("IPduanTip","onError",reval);
					return;
				}else {
					$.formValidator.setFailState("IPduanTip","onCorrect","OK");
				}
				returnIp = $("#startip").val() + "-" + $("#endip").val()
			} else {
				returnIp = $("#allowIP").val();
			}
			parent.$("#accessIpList").append("<option value=''>" + returnIp + "</option>");
			$("#endip").val("");
			$("#startip").val("");
			$("#allowIP").val("");
			if($("#selIP").attr("checked") == true){
				$("#startip").focus();
			}else {
				$("#allowIP").focus();
			}
			
		}
		
		//获取父窗口IP列表
		function getSelIpText() {
			var objs = parent.document.getElementById("accessIpList");
			var selIPText='';
			for (i = objs.options.length - 1; i >= 0; i--) {
				if (objs.options[i].text != "") {
					selIPText += objs.options[i].text+",";
				}
			}
			return selIPText;
		}
		
		//选择允许访问IP号段
		function selectIP(){
			if($("#selIP").attr("checked") == true){
				$("tr[id='accessIP']").hide();
				$("tr[id='accessIPDuan']").show();
				$("tr[id='ipduanTipTR']").show();
				$("#allowIP").unFormValidator(true); 
				$("#startip").unFormValidator(false); 
				$("#endip").unFormValidator(false); 
				$("#startip").focus();
		    }else{    
				$("tr[id='accessIP']").show();
				$("tr[id='accessIPDuan']").hide();
				$("tr[id='ipduanTipTR']").hide();
				$("#allowIP").unFormValidator(false); 
				$("#startip").unFormValidator(true); 
				$("#endip").unFormValidator(true); 
				$("#allowIP").focus();
			}
		}
		
		//绑定点击事件
		function okClick(btn,win,index){
			addWin = win;
	    	$('#okBtn').triggerHandler("click");
		}
	</script>
  </head>
  <body>
  <form id="trustipForm" method="post" action="" >
     <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
      <tr>
        <td valign="top">
		     <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable" id="trustIpListTab">
		      	<tr><td colspan="3" height="10"></td></tr>
		      	<tr>
					<td width="25%" align="right"><view:LanguageTag key="trustip_allow_accip_section_no_sel"/><view:LanguageTag key="colon"/></td>
					<td width="30%">
						<input type="checkbox" id="selIP" name="selIP" onClick="selectIP();" />
					</td>
					<td width="45%" align="right">&nbsp;</td>
				</tr>
				<tr><td colspan="3" height="8"></td></tr>
				<tr id="accessIP">
					<td align="right"><view:LanguageTag key="trustip_allow_accip_addr"/><view:LanguageTag key="colon"/></td>
					<td>
						<input type="text" id="allowIP" name="accessInfo.allowIP" />
					</td>
					<td></td>
				</tr>
				<tr id="allowIPTipTR">
					<td></td>
					<td colspan="2"><div id="allowIPTip"></div></td>
				</tr>
				<tr id="accessIPDuan">
					<td align="right"><view:LanguageTag key="trustip_allow_accip_section_no"/><view:LanguageTag key="colon"/></td>
					<td colspan="2">
						<input type="text" id="startip" name="accessInfo.startip" />
					    —
						<input type="text" id="endip" name="accessInfo.endip"  />
					</td>
				</tr>
				<tr id="ipduanTipTR">
					<td></td>
					<td colspan="2"><div id="IPduanTip"></div></td>
				</tr>
				
				<tr>
			       <td><a href="#" name="okBtn" id="okBtn"></a></td>
			    </tr>
		   </table>
       </td>
      </tr>
     </table> 
   </form>
  </body>
</html>