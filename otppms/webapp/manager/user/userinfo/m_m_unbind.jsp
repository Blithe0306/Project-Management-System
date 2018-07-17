<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
	<link href="<%=path%>/manager/common/uploadify/uploadify.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
    
    <script language="javascript" src="<%=path%>/manager/common/js/checkall.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.6.4.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/uploadify/swfobject.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/uploadify/uploadify.v2.1.3.min.js"></script>
	
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>	
	 
	<script language="javascript" type="text/javascript">
	
		$(function() {

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
		
            $("#menu li").each(function(index) {
            	$(this).click(function() {
                    $("#menu li.tabFocus").removeClass("tabFocus");
                    $(this).addClass("tabFocus");
                    $("#content li:eq(" + index + ")").show().siblings().hide();
                    if(index==1){
                    	initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv');
                    }
                });
            });
        })
        
        //模板文件下载
        function downLoad(){
			jQuery("#uBindForm").ajaxSubmit({
			   async:false,  
			   dataType:"json",
			   type:"POST", 
			   url : "<%=path%>/manager/user/userinfo/userUnBind!downLoadIni.action",
			   success:function(msg){
				  if(msg.object == 'false'){
				  	  FT.toAlert(msg.errorStr,msg.object,null);	
				  }else{
					  window.location.href = "<%=path%>/manager/user/userinfo/userUnBind!downLoad.action?fileName="+encodeURI(msg.object);
				  }
			   }
			});	
        }
        
        //uploadify文件上传支持
		/*
		$(document).ready(function() {
			$("#userFile").uploadify({
				//指定uploadify.swf路径
		        'uploader':'<%=path%>/manager/common/uploadify/uploadify.swf',
		        //后台处理的请求的Action 
		        'script':'<%=path%>/manager/user/userinfo/userUnBind!unBindUsrTkn.action',
		 		'cancelImg':'<%=path%>/manager/common/uploadify/cancel.png',
		 		'queueID':'fileQueue',//与下面的id对应
		 		'fileDataName':'userFile', //和以下input的name属性一致 
		 		'queueSizeLimit':5,//当允许多文件生成时，设置选择文件的个数
		        'fileDesc':'Excle、CSV、TXT文件',
		        //控制可上传文件的扩展名，启用本项时需同时声明fileDesc          
		 		'fileExt':'*.xls;*.xlsx;*.csv;*.txt',
		 		'auto':false,//是否自动上传，即选择了文件即刻上传。
		 		'multi':true, //是否允许同时上传多文件，默认false
		 		'simUploadLimit':5,//多文件上传时，同时上传文件数目限制
		 		'sizeLimit':19871202,//设置单个文件大小限制 
		 		'buttonText':'BROWSE',
		        'displayData':'percentage',
		        onComplete: function (evt, queueID, fileObj, response, data) {
		        	if(response == 'false'){
		        	     FT.toAlert('error','<view:LanguageTag key="user_unbind_validate_1"/>', null);
		        	}else{
		        		$('#resultDiv').html(response);
		        	}
		        }
	 		});
		});
		
		//批量解绑
		function uploadUpload(){
			$('#userFile').uploadifySettings('scriptData',{'sessionId':"<%=session.getId()%>"}); //给 后台action传值
			$('#userFile').uploadifyUpload();
		}
		*/
		
		 //模板文件下载
        function uploadFile(){
			var ajaxbg = $("#background,#progressBar");//加载等待
			ajaxbg.show();
			jQuery("#uBindForm").ajaxSubmit({
			   async:false,  
			   type:"POST", 
			   url : "<%=path%>/manager/user/userinfo/userUnBind!unBindUsrTkn.action",
			   success:function(msg){
				  ajaxbg.hide();
				  if(msg == 'false'){
		        	     FT.toAlert('error','<view:LanguageTag key="user_vd_temp_file_er_or_date_is_null"/>', null);
		        	}else{
		        		$('#resultDiv').html(msg);
		        		FT.toAlert('success','<view:LanguageTag key="user_upload_unbind_succ" />', null);
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
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td>  
		  <span class="topTableBgText">
			  <view:LanguageTag key="user_common_batch_unbind"/>
          </span>
        </td>
        <td width="2%" align="right">
        	<a href="javascript:addAdmPermCode('020105','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" width="16" height="16" hspace="6" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a></td>
      </tr>
    </table> 
	<table width="100%" border="0"  cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">		
		<form name="uBindForm" id="uBindForm" method="post" action="" enctype ="multipart/form-data">
		 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
		   <tr><td colspan="3" height="10px"></td></tr>
           <tr>
             <td width="30%" align="right" valign="top" style="padding-top:3px"><view:LanguageTag key="user_batch_unbind_file"/><view:LanguageTag key="colon"/></td>
              <td width="35%" id="fileTd">
	        	<input type="text" id="showVal" class="formCss100" onresize="initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv')" readonly/>
		        &nbsp;&nbsp;<div style="z-index:10;display: inline;"><span class="Button" id="showDiv"><a id="setShowValInp" href="javascript:"><view:LanguageTag key="common_syntax_select"/></a></span></div>
		        <div id="fileDiv" style="position:absolute;margin:0 0 0 -38px;z-index:50;filter:alpha(opacity=0);moz-opacity: 0;-khtml-opacity: 0;opacity:0;">
		        <input name="userFile" id="userFile"  type="file" size="1" style="height: 24px;" onchange="setFullPath(this,'showVal');" /></div>	        </td>
             <td width="35%"></td>
           </tr>
          <tr>
             <td>&nbsp;</td>
             <td><a href="javascript:uploadFile();" class="button" id="fileupload"><span><view:LanguageTag key="user_common_batch_unbind"/></span></a>
			 </td>
             <td>&nbsp;</td>
           </tr>
           <tr>
             <td>&nbsp;</td>
             <td></td>
             <td>&nbsp;</td>
           </tr>
           <tr>
             <td>&nbsp;</td>
             <td><view:LanguageTag key="user_vd_download_unbind_template"/><a href="javascript:downLoad();" id="importbutton"><span><view:LanguageTag key="common_download_temp_file"/></span></a></td>
             <td>&nbsp;</td>
           </tr>
           <tr>
             <td>&nbsp;</td>
             <td><div id="resultDiv"></div></td>
             <td>&nbsp;</td>
           </tr>
         </table>
		</form>
		</td>
      </tr>
  </table>
  </body>
</html>