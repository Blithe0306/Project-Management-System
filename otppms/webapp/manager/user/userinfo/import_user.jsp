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
	<link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
	<link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
	<link href="<%=path%>/manager/common/uploadify/uploadify.css" rel="stylesheet" type="text/css"/>
	
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
    
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.6.4.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/uploadify/swfobject.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/uploadify/uploadify.v2.1.3.min.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	
 	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/validate.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>	
	
    <script language="javascript" type="text/javascript">
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
	
		$("#fileuploadbutton").attr("disabled",false);
		$.formValidator.initConfig({submitButtonID:"fileuploadbutton",debug:true,
			onSuccess:function(){
				fileUpload();
			},
			onError:function(){
				return false;
			}});
        $("#userFile").formValidator({onFocus:'<view:LanguageTag key="user_import_sel_imp_ufile" />',onCorrect:"OK"}).inputValidator({min:1,onError:'<view:LanguageTag key="user_import_sel_ufile_path" />'}).functionValidator({
		    fun:function(fileUp){
		    	var fname = $("#userFile").val();
				var s = fname.lastIndexOf('.');
				var fn = fname.substring(s);
				if(fn != '.xls'&&fn != '.XLS' && fn != '.xlsx'&&fn != '.XLSX'){
				    return '<view:LanguageTag key="user_import_only_support_execl" />';
				}else {
				    return true;
				}  
			}
		});
		    	
	});		

		function downWindow() {
			FT.openWinMiddle('<view:LanguageTag key="user_import_title_download" />','<%=path%>/manager/user/userinfo/downFile.jsp',true);			    
        }
      
		
		//全选、取消全选
		function allCheckOper(){
			if($("#allCheck").attr("checked")==true){
				$('input[name="usrAttr[]"]').each(function() {
	            	$(this).attr("checked", true);
	        	});
        	}else{            
		        $("input[name='usrAttr[]']").each(function() {
		        	if(this.value != 1){
		            	$(this).attr("checked", false);
		        	}
		        });
	        }
		}
		function fileUpload(){
			var domainId=$("#domainId").val();
			var ajaxbg = $("#background,#progressBar");//加载等待
			ajaxbg.show();
			$("#ImportForm").ajaxSubmit({
			   async:true,  
			   //dataType : "json",  
			   type:"POST", 
			   url : '<%=path%>/manager/user/userinfo/userImport!importUser.action',
			   data:{domainId:domainId},
			   //dataType:'json',
			   success:function(msg){		
				    ajaxbg.hide();
					if(msg == 'false'){
		        	    FT.toAlert('error','<view:LanguageTag key="user_import_vd_error" />', null);
		        	}else{
		        		$('#resultDiv').html(msg);
		        		FT.toAlert('success','<view:LanguageTag key="user_upload_import_succ" />', null);
		        		//下载失败记录
		        		//if(msg.indexOf("&")!=-1){
		        		    //var fileName=msg.split("&")[0];		        		   
		        		    //if(fileName!='0'){
		        				//window.location.href = "<%=path%>/manager/user/userinfo/userExport!downLoad.action?fileName="+fileName;
		        			//}
		        		//}
		        	}
				}
				
			});	
		}
	function downFaildFile(fileName){
		window.location.href = "<%=path%>/manager/user/userinfo/userExport!downLoad.action?fileName="+encodeURI(fileName);
	}
	</script>
  </head>
  
  <body onresize="initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv')">
  <div id="background"  class="background"  style="display: none; "></div>
  <div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
  <form name="ImportForm" id="ImportForm" method="post" action="" enctype ="multipart/form-data">
    <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="topTableBg">
      <tr>
        <td>
        	<span class="topTableBgText">
			  <view:LanguageTag key="user_import_title_user"/>
            </span>
        </td>
        <td width="2%" align="right">
        	<a href="javascript:addAdmPermCode('0202','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" width="16" height="16" hspace="6" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a>       	 </td>
      </tr>
    </table>
	<table width="100%" height="100" border="0"  cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
			 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
	             <tr><td colspan="3" height="10px"></td></tr>
	             <tr style="display:none">
	               <td width="30%" align="right" valign="top"><view:LanguageTag key="org_vd_company_name"/><view:LanguageTag key="colon"/></td>
	               <td width="35%">
				    	<view:ImpDomainTag dataSrc="" index1Val="1"></view:ImpDomainTag>
	               </td>
	               <td width="35%">&nbsp;</td>
	             </tr>
	             
	              <tr>
	                <td align="right" width="30%" valign="top" style="padding-top:3px"><view:LanguageTag key="user_import_file"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	            
	                
	                <td id="fileTd" width="35%">
			        	<input type="text" id="showVal" class="formCss100" onresize="initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv')" readonly/>
				        &nbsp;&nbsp;<div style="z-index:10;display: inline;"><span class="Button" id="showDiv"><a id="setShowValInp" href="javascript:"><view:LanguageTag key="common_syntax_select"/></a></span></div>
				        <div id="fileDiv" style="position:absolute;margin:0 0 0 -38px;z-index:50;filter:alpha(opacity=0);moz-opacity: 0;-khtml-opacity: 0;opacity:0;">
				        <input name="userFile" id="userFile"  type="file" size="1" style="height: 24px;" onchange="setFullPath(this,'showVal');"/></div>
			        </td>
	                
	                <td class="divTipCss" width="35%"><div id="userFileTip"></div></td>
	              </tr>
	             
	             <tr>
	               <td align="right">&nbsp;</td>
	               <td>
	               		<a href="#" onclick="downWindow()" class="button"><span><view:LanguageTag key="user_import_download_temp"/></span></a>
	               		<a href="#" class="button" id="fileuploadbutton"><span><view:LanguageTag key="user_import"/></span></a>
	               </td>
	               <td>&nbsp;</td>
	             </tr>
	             <tr>
	               <td align="right">&nbsp;</td>
	               <td><div id="resultDiv"></div></td>
	               <td>&nbsp;</td>
	             </tr>
	           </table>
		</td>
      </tr>
    </table>
  </form>
 
  </body>
</html>