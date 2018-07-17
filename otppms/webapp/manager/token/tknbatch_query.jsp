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
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.6.4.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script language="javascript" src="<%=path%>/manager/orgunit/domain/js/add.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>  
    <script language="javascript" type="text/javascript">
	// Start,多语言提取
	var save_succ_lang = '<view:LanguageTag key="common_save_succ_tip"/>';
	var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
	var sel_admin_lang = '<view:LanguageTag key="domain_info_sel_admin"/>';
	var need_del_date_lang = '<view:LanguageTag key="common_syntax_check_need_del_date"/>';
	// End,多语言提取
	
	$(document).ready(function(){
			//窗体大小改变时要重新设置透明文件框的位置
			$(window).resize(initFileInputDivNoParame);
			//初始化透明文件框的位置
			initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv');
			
			$("#menu li").each(function(index) { //带参数遍历各个选项卡
			$(this).click(function() { //注册每个选卡的单击事件
				$("#menu li.tabFocus").removeClass("tabFocus"); //移除已选中的样式
				$(this).addClass("tabFocus"); //增加当前选中项的样式
                    //显示选项卡对应的内容并隐藏未被选中的内容
				$("#content li:eq(" + index + ")").show()
                    .siblings().hide();
                });
            });
            $("#showVal").focus(); 
	 }) 
	 
	 
	 /**
     * 上传令牌号文件操作
     */
    function upTokenFile(){
		var fname = $("#tknFile").val();
		if($.trim(fname)==""){
			FT.toAlert('warn','<view:LanguageTag key="tkn_vd_tkn_select"/>',null);
			return;
		}
		
		var s = fname.lastIndexOf('.');
		var fn = fname.substring(s);
		if(fn.toLowerCase() != '.txt'){  // 转换大小写
			FT.toAlert('error','<view:LanguageTag key="tkn_query_file_type"/>',null);
			return;
		}     	

		$("#queryForm").ajaxSubmit({
			async : true,
			type : "POST",
			url : "<%=path%>/manager/token/token!upBatchFile.action",
			data: {},
			dataType : "json",
			success : function(msg){
				var errorStr = msg.errorStr;
				if(errorStr == "success"){
					 window.location.href = "<%=path%>/manager/token/list.jsp?batchTknSn=" + msg.object+"&mode=1"; //mod 1为了返回此页标记
				} else{
					FT.toAlert(msg.errorStr, msg.object, null);
				}
			}
		});
	} 
	//-->
	</script>
  </head>  
  
  <body scroll="no" style="overflow:hidden" onresize="initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv')">
  <input type="hidden" value="<%=path %>" id="contextPath" />
  <input id="cPage" type="hidden" value="${param.currentPage}" />
  <form name="queryForm" id="queryForm" method="post" action="">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td>  
		  <span class="topTableBgText"><view:LanguageTag key="tkn_batch_query_tkn"/></span>
        </td>
        <td width="2%" align="right">
        	<a href="javascript:addAdmPermCode('0303','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" width="16" height="16" hspace="6" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a>       	 </td>
      </tr>
    </table> 
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td align="center">
		    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		      <tr><td colspan="3" height="10px"></td></tr>
		      <tr>
		      	<td width="30%" align="right" valign="top" style="padding-top:3px"><view:LanguageTag key="tkn_key_file"/><font color="red">*</font><view:LanguageTag key="colon"/></td>				        
		      	<td width="35%" align="left" id="fileTd">
	        		<input type="text" id="showVal" class="formCss100" onresize="initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv')"/>
		        	&nbsp;&nbsp;<div style="z-index:10;display: inline;"><span class="Button" id="showDiv"><a id="setShowValInp" href="javascript:"><view:LanguageTag key="common_syntax_select"/></a></span></div>
		        	<div id="fileDiv" style="position:absolute;margin:0 0 0 -38px;z-index:50;filter:alpha(opacity=0);moz-opacity: 0;-khtml-opacity: 0;opacity:0;">
		        	<input name="upFile" id="tknFile"  type="file" size="1" style="height: 24px;" onchange="setFullPath(this,'showVal');"/></div>
	          	</td>
		      	<td width="35%" align="left"><view:LanguageTag key="tkn_query_file_type_err"/></td>
		      </tr>    
		      <tr>
		        <td>&nbsp;</td>
		        <td><a href="#" id="queryBtn" class="button" onclick="upTokenFile();"><span><view:LanguageTag key="tkn_batch_query"/></span></a></td>
		        <td>&nbsp;</td>
		      </tr>
		    </table>
    	</td>
    </tr>
    </table>
  </form>
  </body>
</html>