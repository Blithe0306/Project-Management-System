<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
%>
<html>
<head>
<title><view:LanguageTag key='tkn_import'/></title>
<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
<link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.6.4.min.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/validate.js"></script>
<script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script>
<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>

<script language="javascript" type="text/javascript">
<!--
	var waitW = null;
	//等待窗口
    function waitWin(){
    	waitW = $.ligerDialog.open({title:'<view:LanguageTag key="common_vd_plase_wait"/>', width:320, height:200, url:'<%=path%>/manager/token/import/wait.jsp', allowClose:false });
    }
    
    function closeWaitWin(){
    	waitW.close();
    }
	$(document).ready(function(){
		// 禁用回退
		$("td :input").keydown(function(e) {
			if(e.keyCode == '8') {
				return false;
			}
		});
		//窗体大小改变时要重新设置透明文件框的位置
		$(window).resize(initFileInputDivNoParame);
		//初始化透明文件框的位置
		initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv');
		initFileInputDiv('setShowValInp1','showVal1','fileTd1','fileDiv1');
	
		$("#importbutton").attr("disabled",false);
		$.formValidator.initConfig({submitButtonID:"importbutton",debug:true,
			onSuccess:function(){
				importToken();
			},
			onError:function(){
				return false;
			}});
			
		$("#showVal1").formValidator({onFocus:"<view:LanguageTag key='tkn_vd_bin'/>",onCorrect:"Ok"}).inputValidator({min:0,onError:"<view:LanguageTag key='tkn_vd_bin'/>"}).functionValidator({
	    	fun:function(fileUp){
	    		var fileUp = $.trim($("#keyFile").val());
				var fname = fileUp.substring(fileUp.lastIndexOf(".") + 1);
				fname = fname.toLowerCase();
				
				if(fname != "" && fname != "bin") {
					return "<view:LanguageTag key='tkn_vd_bin_error'/>";
				}
				return true;
			}
		});
		
		var venId = $("#vendorId").find("option:selected").val();
		selectVendor(venId);
			
		$("#orgunitNames").formValidator({onFocus:"<view:LanguageTag key='tkn_vd_orgunit_show'/>",onCorrect:"OK"}).inputValidator({min:1,onError:"<view:LanguageTag key='tkn_vd_orgunit_error'/>"});
		$("#enabled").formValidator({onFocus:"<view:LanguageTag key='tkn_vd_sel_enable_show'/>",onCorrect:"OK"}).inputValidator({min:0,max:1,onError:"<view:LanguageTag key='tkn_vd_sel_enable_show'/>"});	  
		$("#orgunitNames").focus(); 
		// 此处为解决google浏览器 点击选择不给提示问题
		$("#showVal").focus();
		$("#orgunitNames").focus(); 
	});	
	
	//令牌导入格式改变事件
	function selectVendor(val) {
		if (val == '1001') {
			 $("#showVal").formValidator({onFocus:"<view:LanguageTag key='tkn_vd_sel_file_tnk'/>",onCorrect:"OK"}).inputValidator({min:1,onError:"<view:LanguageTag key='tkn_vd_sel_file_tnk'/>"}).functionValidator({
			    fun:function(fileUp){
					var fname = getFileType();
					if(fname != "tnk"){
						return "<view:LanguageTag key='tkn_vd_must_tnk_file'/>";
					}
					return true;
				}
			});
			$("tr[id='keyfileTR']").hide();
		}else if (val == '1002') {
			$("#showVal").formValidator({onFocus:"<view:LanguageTag key='tkn_vd_sel_file_xml'/>",onCorrect:"OK"}).inputValidator({min:1,onError:"<view:LanguageTag key='tkn_vd_sel_file_xml'/>"}).functionValidator({
			    fun:function(fileUp){
					var fname = getFileType();
					if(fname != "xml"){
						return "<view:LanguageTag key='tkn_vd_must_xml_file'/>";
					}
					return true;
				}
			});
			$("tr[id='keyfileTR']").show();
		}
		//清空
		if($.browser.msie){ //IE浏览器判断
			$("#keyFile").after($("#keyFile").clone().val("")); 
			$("#keyFile").remove();

			$("#seedFile").after($("#seedFile").clone().val(""));  
			$("#seedFile").remove(); 
		}else{
			$("#keyFile").val(''); 
			$("#seedFile").val('');
		}
		$("#showVal1").val(""); // 密钥文件
		$("#showVal").val('');  // 令牌文件
		$("#showVal").focus(); 
	}
	
	//获取令牌文件后缀类型
	function getFileType() {
		var fileUp = $.trim($("#seedFile").val());
		var fname = fileUp.substring(fileUp.lastIndexOf(".") + 1);
		fname = fname.toLowerCase();
		return fname;
	}

	//令牌导入
	function importToken(){
		$.ligerDialog.confirm('<view:LanguageTag key="tkn_sure_tkn_file" />', function (yes){
		if(yes){
			waitWin();
			fullScreenMask();//开启全屏遮罩
		    $("#importForm").ajaxSubmit({
				type: "POST",
				url: "<%=path%>/manager/token/importToken!importToken.action",
				async: true,
				data: {},
				dataType: "json",
				success: function(msg){
			        var errorStr = msg.errorStr;
			        if(errorStr=='success'){
			        	unFullScreenMask();//关闭全屏遮罩
			        	// 提示语加上是否继续导入
				        FT.Dialog.confirm(msg.object+'<view:LanguageTag key="tkn_import_succ_continue_import"/>', '<view:LanguageTag key="common_syntax_confirm"/>', function(sel){
				        	if(sel) {
				    			location.href = contextPath + "/manager/token/import/import.jsp";
				    		}else{ // 关闭tab页面
				    			window.parent.removeTabItemF('0302');
				    		}
	            		});				
			        }else {
			        	unFullScreenMask();//关闭全屏遮罩
		                FT.toAlert(errorStr, msg.object, null);						 
			        }
			        closeWaitWin();
				}
			});
		}
		});
	}
	
	document.onkeydown = function(evt){
		var evt = window.event?window.event:evt;
		var fname = $("#seedFile").val();
		var s = fname.lastIndexOf('.');
		var fn = fname.substring(s);
		if(evt.keyCode==13 && ("" != $.trim(fname) && ".xml"== fn || ".zip"== fn || ".tnk"== fn)) {
			importGo();  
		}
	}
	
	//重置
	function resetFileInput(){ 
		if($.browser.msie){ //IE浏览器判断
			$("#keyFile").after($("#keyFile").clone().val("")); 
			$("#keyFile").remove();

			$("#seedFile").after($("#seedFile").clone().val(""));  
			$("#seedFile").remove(); 
		}else{
			$("#keyFile").val(''); 
			$("#seedFile").val('');
		}
			$("#showVal1").val(""); // 密钥文件
		$("#showVal").val('');  // 令牌文件
		//$("#keyPass").val('');  //密钥文件密码
		$("#orgunitNames").val('');  // 组织机构
		$("#orgunitIds").val(''); 
		$("#enabled").val(1);   // 启用令牌
	}
	
	// 提示组织机构为空，关闭窗口
	function closeOrgWin(object) {
		$.ligerDialog.success(object,'<view:LanguageTag key="common_syntax_tip"/>',function(){
			winOrgClose.close();
		});
	}
//-->
</script>
</head>
<body onresize="initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv');">
<div id="background"  class="background"  style="display: none; "></div>
<div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key='tkn_import_execution'/></div>
<input type="hidden" value="<%=path %>" id="contextPath" />
<form id="importForm" name="importForm" method="post" action="" enctype ="multipart/form-data">
  <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
    <tr>
      <td><span class="topTableBgText"><view:LanguageTag key='common_menu_tkn_import'/></span></td>
      <td width="2%" align="right">
      	<a href="javascript:addAdmPermCode('0302','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" width="16" height="16" hspace="6" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a>      </td>
    </tr>
  </table>
  <table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td valign="top">
            <table  width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnInsideTable"  >
              <tr>
                <td width="25%" align="right" ><view:LanguageTag key="tkn_import_orgunit"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
                <td width="30%" align="left">
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="70%">
                        <input id="orgunitIds" name="orgunitIds" type="hidden" value="" />
                        <input id="orgunitNames" name="orgunitNames" onClick="selOrgunits(1,'<%=path%>');" readonly value="" style="width:99%" class="formCss100"/>
                      </td>
                    </tr>
                  </table>
                </td>
                <td class="divTipCss"><div id="orgunitNamesTip"></div></td>
              </tr>
              <tr>
                <td align="right" ><view:LanguageTag key="tkn_import_formats"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
                <td align="left">
	               <select id="vendorId" name="vendorId" onchange="selectVendor(this.value);" class="select100" >
				      <view:VendorInfoTag dataSrc=""  />
			       </select>
                </td>
                <td align="left">&nbsp;</td>
              </tr>
              <tr>
              <tr>
                <td align="right" valign="top" style="padding-top:3px"><view:LanguageTag key="tkn_file"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
                <td id="fileTd">
		        	<input type="text" id="showVal" class="formCss100" onresize="initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv')" readonly/>
			        &nbsp;&nbsp;<div style="z-index:10;display: inline;"><span class="Button" id="showDiv"><a id="setShowValInp" href="javascript:"><view:LanguageTag key="common_syntax_select"/></a></span></div>
			        <div id="fileDiv" style="position:absolute;margin:0 0 0 -38px;z-index:50;filter:alpha(opacity=0);moz-opacity: 0;-khtml-opacity: 0;opacity:0;">
			        <input name="seedFile" id="seedFile" type="file" size="1" style="height: 24px;" onchange="setFullPath(this,'showVal');"/></div>
		        </td>
                <td align="left" class="divTipCss"><div id="showValTip"></div></td>
              </tr>
              
       		  <tr id="keyfileTR">
                <td align="right" valign="top" style="padding-top:3px"><view:LanguageTag key="tkn_bin_file"/><view:LanguageTag key="colon"/></td>
                <td id="fileTd1">
		        	<input type="text" id="showVal1" class="formCss100" onresize="initFileInputDiv('setShowValInp1','showVal1','fileTd1','fileDiv1')"/>
			        &nbsp;&nbsp;<div style="z-index:10;display: inline;"><span class="Button" id="showDiv1"><a id="setShowValInp1" href="javascript:"><view:LanguageTag key="common_syntax_select"/></a></span></div>
			        <div id="fileDiv1" style="position:absolute;margin:0 0 0 -38px;z-index:50;filter:alpha(opacity=0);moz-opacity: 0;-khtml-opacity: 0;opacity:0;">
			        <input name="keyFile" id="keyFile" type="file" size="1" style="height: 24px;" onchange="setFullPath(this,'showVal1');"/></div>
		        </td>
                <td class="divTipCss"><div id="showVal1Tip"></div></td>
              </tr>
              
              <!--<tr>
                <td align="right" ><view:LanguageTag key="tkn_bin_pwd"/><view:LanguageTag key="colon"/></td>
                <td align="left">
                  <input type="text" id="keyPass" name="keyPass" class="formCss100" />
                </td>
                <td align="left">&nbsp;</td>
              </tr> -->
              <tr>
                <td align="right" ><view:LanguageTag key="tkn_whether_enable"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
                <td align="left">
                  <select id="enabled" name="enabled" class="select100">
                    <option value="1"><view:LanguageTag key="common_syntax_yes"/></option>
                    <option value="0"><view:LanguageTag key="common_syntax_no"/></option>
                  </select>
                </td>
                <td align="left" class="divTipCss"><div id="enabledTip"></div></td>
              </tr>
              <tr>
              <tr>
                <td  align="right" ></td>
                <td> 
                	<a href="#" class="button" id="importbutton"><span><view:LanguageTag key="tkn_import"/></span></a> 
                 	<a href="javascript:resetFileInput()" class="button"><span><view:LanguageTag key="common_syntax_reset"/></span></a> 
                 	<!--  
                 	<a onclick="seedFile.select();document.execCommand('Delete');" class="button"><span><view:LanguageTag key='common_syntax_reset'/></span></a> 
                 	-->
                 </td>
                <td></td>
              </tr>
            </table>
      </td>
    </tr>
  </table>
</form>
</body>
</html>