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
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
    <link rel="stylesheet" id="openwincss" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
    
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
    <script type="text/javascript" src="<%=path%>/manager/confinfo/radius/js/addattr.js"></script>	
    
	<script language="javascript" type="text/javascript">
	// Start,多语言提取
	var name_lang = '<view:LanguageTag key="radattr_vd_name"/>';
	var custom_name_lang = '<view:LanguageTag key="radattr_vd_custom_name"/>';
	var custom_id_lang = '<view:LanguageTag key="radattr_vd_custom_id"/>';
	var custom_id_err_lang = '<view:LanguageTag key="radattr_vd_custom_id_err"/>';
	var valtype_lang = '<view:LanguageTag key="radattr_vd_valtype"/>';
	var valtype_err_lang = '<view:LanguageTag key="radattr_vd_valtype_err"/>';
	var valtype_err_1_lang = '<view:LanguageTag key="radattr_vd_valtype_err_1"/>';
	var valtype_err_2_lang = '<view:LanguageTag key="radattr_vd_valtype_err_2"/>';
	// Start,多语言提取
	
	$(function() {
		//绑定点击事件
		domClickEvent();
		//点击按钮弹出窗口，设置窗口样式，取消已绑定点击事件
		var cssurl = "<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"
        btnClickEvent("#addBtn",cssurl);
	
		 var attrHtml = $("#attrHtml").val();
		 var attr = attrHtml.split("^^"); 
		 document.getElementById('attrValueTypeDiv').innerHTML = attr[1];
	     document.getElementById('attrValueDiv').innerHTML = attr[0];
	     jQuery("#addBtn").click(function(){
		   if(!checkAttr()){
		     return;
		   } 
		   var urlPath = "<%=path%>/manager/confinfo/radius/radAttr!modify.action";
		   var profileId = $("#profileId").val();
		   jQuery("#attrForm").ajaxSubmit({
				async:false, 
				dataType : "json", 
				type:"POST",
				url : urlPath,
				success : function(msg){
					if(msg.errorStr == 'success'){
				     	$.ligerDialog.success('<view:LanguageTag key="common_save_succ_tip"/>', '<view:LanguageTag key="common_syntax_tip"/>',function(){
				        	window.location.href = "<%=path%>/manager/confinfo/radius/attrlist.jsp?profileId="+profileId;
				     	});
					    }else{
						 	FT.toAlert(msg.errorStr,msg.object,null);
					    }
				 	}
				}); 
			}); 
	 })
	 
	
	</script>
  </head>
  
  <body scroll="no" style="overflow:auto; overflow-x:hidden">
	  <input type="hidden" value="<%=path %>" id="contextPath" />
	  <input id="curPage" type="hidden" value="${param.currentPage}" />
	  <form id="attrForm" method="post" action="">
	    <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="topTableBg">
	      <tr>
	        <td width="98%"><span class="topTableBgText"><view:LanguageTag key="radattr_edit"/></span></td>
	        <td width="2%" align="right">
	      <!--	 	<a href="javascript:openHelpWin('<%=path%>/manager/help/confighelp.html#387','<%=path%>/manager/common/js/ligerUI/skins/Gray/css/dialog.css');">
	      		<img src="<%=path%>/images/icon/help_1.png" width="16" height="16" hspace="5" title='<view:LanguageTag key="common_syntax_help"/>' align="absmiddle" />
	      		</a>-->
	        </td>
	      </tr>
	    </table>  
	    <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="ulOnInsideTable">
	      <tr>
	        <td valign="top">
			    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
			      <tr>
			        <td width="30%" align="right"><view:LanguageTag key="tnk_vendor"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td width="30%">
			        	<input type="hidden" id="vendorid" name="radAttrInfo.vendorid" value="${radAttrInfo.vendorid}"  />
				        ${radAttrInfo.vendorname}
			        </td>
			        <td width="40%"><div id="attrNameTip" style="width:100%"></div></td>
			      </tr>
			      <tr>
			        <td width="30%" align="right"><view:LanguageTag key="radattr_name"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td width="30%">
			        	<input type="hidden" id="attrSel"  value="radAttrInfo.attrName"  />
				        ${radAttrInfo.attrName}
			        </td>
			        <td width="40%"><div id="attrNameTip" style="width:100%"></div></td>
			      </tr>
			      
			      <tr>
			        <td align="right"><view:LanguageTag key="radattr_valtype"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td><div id="attrValueTypeDiv"></td>
			        <td><div id="attrValueTypeTip" style="width:100%"></div></td>
			      </tr>
			      
			      <tr>
			        <td align="right"><view:LanguageTag key="radattr_value"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td><div id="attrValueDiv"></td>
			        <td><div id="attrValueTip" style="width:100%"></div></td>
			      </tr>
			      <tr> 
					    <td colspan="3" height="10"></td>
				  </tr>
				</table>
				<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="bottomTableCss">
			      <tr>
			        <td width="30%">&nbsp;</td>
			        <td width="40%" align="center"> 
			        	<input type="hidden" id="profileId" name="radAttrInfo.profileId" value="${param.profileId}" />
			        	<input type="hidden" id="attrHtml"  value="${attrHtml}" />
			        	<a href="#" id="addBtn" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
			            <a href="javascript:history.back(-1)" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a>
			        </td>
			        <td width="30%"></td>
			      </tr> 
			    </table>
	   		</td>
	   	  </tr>
		</table>    
	</form>
</body>
</html>