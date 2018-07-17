<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld"%>
<%
    String path = request.getContextPath();
%>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title></title>
	<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css" />
	<script language="javascript" type="text/javascript">
		$(function() {
			// 禁用回退
			$("#showVal").keydown(function(e) {
				if(e.keyCode == '8') {
					return false;
				}
			});
			//窗体大小改变时要重新设置透明文件框的位置
			$(window).resize(initFileInputDivNoParame);
			//初始化透明文件框的位置
			initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv');
		
		
			$("#tab1").show();
			$("#tab2").hide();
			
			setLicInfo();
		})
		
		function setLicInfo() {			
			var lictype = ['<view:LanguageTag key="lic_type_eval"/>','<view:LanguageTag key="lic_type_busi"/>','<view:LanguageTag key="lic_type_advanced"/>'];
			var url = '<%=path%>/manager/lic/license!find.action';
	       	$.ajax({
				type: "POST",
			    url: url,
			    async: true,
			    dataType: "json",	    
			    success: function(msg){
			    	var result = '';
			    	if(msg != null){
			    		result = msg.errorStr;
			    	}
					if(result != '' && result == 'success'){
			    		var lic = msg.object;
			    		$("#customerid").html(lic.customerid);
			    		$("#lictype").html(lictype[lic.lictype]);
			    		$("#issuer").html(lic.issuer);
			    		$("#licupdatetime").html(lic.licupdateTimeStr);
			    		$("#startTime").html(lic.startTime);
			    		$("#expireTime").html(lic.expireTime);
			    		var licStateStr = "";
			    		if(lic.licstate==3){// 正常
			    			licStateStr = '<view:LanguageTag key="lic_state_normal"/>';
			    		}else if(lic.licstate==1){// 失效
			    			licStateStr = '<view:LanguageTag key="lic_state_fail"/>';
			    		}else{// 不正常
			    			licStateStr = '<view:LanguageTag key="lic_state_no_normal"/>';
			    		}
			    		$("#licstate").html(licStateStr);
			    		$("#tokenCount").html(lic.tokenCount == -1 ? '<view:LanguageTag key="index_unlimited"/>':lic.tokenCount);
			    		$("#serverNodes").html(lic.serverNodes == -1 ? '<view:LanguageTag key="index_unlimited"/>':lic.serverNodes);
			    		$("#mobileTokenNum").html(lic.mobileTokenNum == -1 ? '<view:LanguageTag key="index_unlimited"/>':lic.mobileTokenNum);
			    		$("#softTokenNum").html(lic.softTokenNum == -1 ? '<view:LanguageTag key="index_unlimited"/>':lic.softTokenNum);
			    		$("#smsTokenNum").html(lic.smsTokenNum == -1 ? '<view:LanguageTag key="index_unlimited"/>':lic.smsTokenNum);
			    	}
				}
			});
		}
		
		//更新授权文件
		function updateLicFile(flag) {
			$("#flag").val(flag);
			
			$("#tab1").hide();
			$("#tab2").show();
			
			//窗体大小改变时要重新设置透明文件框的位置
			$(window).resize(initFileInputDivNoParame);
			//初始化透明文件框的位置
			initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv');
		}
		//取消上传
		function cancelUp() {
			var flag = $("#flag").val();
			if(flag == 1) {
	        	stepController(1);
			}else {
				$("#tab1").show();
				$("#tab2").hide();
			}
		}

		//重置
		function resetFileInput(){ 
			$("#licFile").after($("#licFile").clone().val("")); 
			$("#licFile").remove(); 
			$("#showVal").val('');
		}
		
		//点击设置授权显示
	    function setLicView() {
	    	$("#tab1").show();
			$("#tab2").hide();
	    }
		
		function upLoadLic() {
			var fileUp = $.trim($("#licFile").val());
			if(fileUp == '' || fileUp == null) {
				FT.toAlert('warn','<view:LanguageTag key="sys_lic_vd_file_err"/>', null);
				return;
			}
			var fname = fileUp.substring(fileUp.lastIndexOf(".") + 1);
				fname = fname.toLowerCase();
				if(fname != "lic"){
					FT.toAlert('warn','<view:LanguageTag key="sys_lic_vd_file_err_1"/>', null);
					return;
				}
		
			$.ligerDialog.confirm('<view:LanguageTag key="sys_lic_vd_confirm_file"/>', function (yes){
			if(yes){
			    var ajaxbg = $("#background,#progressBar");//加载等待
	         	ajaxbg.show();
			    $("#UpLicForm").ajaxSubmit({
					type: "POST",
					url: "<%=path%>/manager/lic/license!upLic.action",
					async: true,
					data: {},
					dataType: "json",
					success: function(msg){			
						ajaxbg.hide();
				        var errorStr = msg.errorStr;
				        if(errorStr != '' && errorStr == 'success'){
				        	setLicInfo();
				        	$("#tab1").show();
							$("#tab2").hide();
		                }else{
		                	FT.toAlert(errorStr, msg.object, null);
		                }
					}
				});
			}
			});
		}
	</script>
	</head>
	<body onresize="initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv')">
	<div id="background" class="background" style="display: none; "></div>
	<div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
	<input type="hidden" id="flag" value="" />
		<table id="tab1" width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px">
		  <tr>
		      <td align="right" width="22%"><strong>
	        <view:LanguageTag key="lic_type"/><view:LanguageTag key="colon"/></strong></td>
		      <td width="27%"><div id="lictype"></div></td>
		    <td align="right" width="24%"><view:LanguageTag key="lic_cust_ident"/><view:LanguageTag key="colon"/></td>
	        <td width="27%"><div id="customerid"></div></td>
		  </tr>
		  <tr>
			  <td align="right"><view:LanguageTag key="lic_auth_serv_nodes"/><view:LanguageTag key="colon"/></td>
		      <td><div id="serverNodes"></div></td>
		      <td align="right"><view:LanguageTag key="lic_tkn_total"/><view:LanguageTag key="colon"/></td>
		      <td><div id="tokenCount"></div></td>
		  </tr>
		  <tr>
			  <td align="right"><view:LanguageTag key="lic_start_time"/><view:LanguageTag key="colon"/></td>
		      <td><div id="startTime"></div></td>
		      <td align="right"><view:LanguageTag key="lic_end_time"/><view:LanguageTag key="colon"/></td>
		      <td><div id="expireTime"></div></td>
		  </tr>
		  <tr>
			  <td align="right"><view:LanguageTag key="lic_update_time"/><view:LanguageTag key="colon"/></td>
		      <td><div id="licupdatetime"></div></td>
		      <td align="right"><view:LanguageTag key="lic_state"/><view:LanguageTag key="colon"/></td>
		      <td><div id="licstate"></div></td>
		  </tr>
		  <tr>
			  <td height="40" colspan="4" align="center"><table width="150" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><view:AdminPermTag key="000002" path="<%=path%>" langKey="lic_update_file" type="2"/></td>
                </tr>
              </table></td>
          </tr>
		</table>
	    <form action="" method="post" enctype="multipart/form-data" id="UpLicForm" name="UpLicForm">
	    <table id="tab2" width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:30px">
          <tr>
            <td width="29%" align="right"><view:LanguageTag key="sys_lic_file"/><view:LanguageTag key="colon"/></td>            
            <td width="58%" id="fileTd">
	        	<input type="text" id="showVal" onresize="initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv')" readonly/>
		        &nbsp;&nbsp;<div style="z-index:10;display: inline;"><span class="Button" id="showDiv" style="margin-top:-2px"><a id="setShowValInp" href="javascript:"><view:LanguageTag key="common_syntax_select"/></a></span></div>
		        <div id="fileDiv" style="position:absolute;margin:0 0 0 -38px;z-index:50;filter:alpha(opacity=0);moz-opacity: 0;-khtml-opacity: 0;opacity:0;">
	        <input name="licFile" id="licFile"  type="file" size="1" style="height: 24px;" onchange="setFullPath(this,'showVal');"/></div>	        </td>
            
            <td width="13%"></td>
          </tr>
          <tr>
            <td height="40"></td>
            <td>
            	<a href="javascript:upLoadLic()" id="upLicBtn" class="button"><span><view:LanguageTag key="common_syntax_upload"/></span></a>
            	<!-- <a href="javascript:resetFileInput()" class="button"><span><view:LanguageTag key="common_syntax_reset"/></span></a> --> 
            	<a href="javascript:cancelUp()" id="cancelBtn" class="button"><span><view:LanguageTag key="common_syntax_cancel"/></span></a>            </td>
            <td></td>
          </tr>
        </table>
        </form>
	</body>
</html>
