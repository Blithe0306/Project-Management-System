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
	
	var cPath;
	$(function() {
		cPath = $("#contextPath").val() + "/";
	
		//绑定点击事件
		domClickEvent();
		//点击按钮弹出窗口，设置窗口样式，取消已绑定点击事件
		var cssurl = "<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"
        btnClickEvent("#addBtn",cssurl);
        
		  $("tr[id='customAttrNameTR']").hide();
		  $("tr[id='customAttrIdTR']").hide();
		  $("tr[id='attrNameTR']").hide();
		  
		 jQuery("#addBtn").click(function(){
			if(!checkAttr()){
		     	return;
			}
			var type = $.trim($("#type").val());
			var profileId = $("#profileId").val();
		    var urlPath = "<%=path%>/manager/confinfo/radius/radAttr!add.action?type="+type;
			jQuery("#attrForm").ajaxSubmit({
				async:false,
				dataType : "json",   
				type:"POST",
				url : urlPath,
				success : function(msg){
					if(msg.errorStr == 'success'){
				     	FT.Dialog.confirm('<view:LanguageTag key="radattr_add_succ_whether_cont"/>', '<view:LanguageTag key="common_syntax_confirm"/>', function(sel) {
							if(sel) {
								$("#attrSel").val("-2");
								setInnerHTML();
						    }else{
						    	window.location.href = "<%=path%>/manager/confinfo/radius/attrlist.jsp?profileId="+profileId;
							}
						});
				    }else{
					 	FT.toAlert(msg.errorStr,msg.object,null);
				    }
				}
					
				}); 
			
		    }); 
	 })
	 
	function setInnerHTML() {
			document.getElementById('attrNameDiv').innerHTML = "";
		  	document.getElementById('attrValueDiv').innerHTML = "";
		  	document.getElementById('attrIdDiv').innerHTML = "";
		  	document.getElementById('attrValueTypeDiv').innerHTML = "";
			$("tr[id='customAttrNameTR']").hide();
			$("tr[id='customAttrIdTR']").hide();
	}
	 
	 
	 function selVendor(key){
	 	$("#attrSel").val(-2);
	 	changeAttr(-2);
	 	if(key == "-2"){
	 		$("tr[id='attrNameTR']").hide();
			$("tr[id='attrNameTRB']").show();
		  	setInnerHTML();
			return;
		}
		$("tr[id='attrNameTR']").show();
		$("tr[id='attrNameTRB']").hide();
		
		// 级联赋值
		var url = "<%=path%>/manager/confinfo/radius/radAttr!selAttrName.action?vendorid="+key;
		$.ajax({
			type: "POST",
		    url: url,
		    async: false,
		    data: {},
		    dataType: "json",	    
		    success: function(msg){
		    	var errorStr = msg.errorStr;
				if("" == errorStr && "" != msg.items){
					seedfilealgTemplate(msg);
				}
			}
		});
	 }
	 
	//显示RADIUS配置属性
    function seedfilealgTemplate(data) {
    	var lis = "";
    	lis += "<select id='attrSel' name='attrSel' class='select100' onchange='changeAttr(this.options[this.options.selectedIndex].value);'>";
    	lis += "<option value ='-2'>---<view:LanguageTag key="common_syntax_please_sel"/>---</option>";
	    $(data.items).each(function(i, ite){
	        //遍历JSON数据得到所需形式
		    lis += "<option value = '" + ite.attrId + "'";
		    lis += ">";
		    lis += ite.attrName;
		    lis += "<\/option>";
        })
        lis += "<\/select>";
        if("" != lis){
        	$("#radAttrDIV").html(lis);
        }
    }
	 
	 function changeAttr(key) {
	 	if(key == "-2"){
		  	setInnerHTML();
			return;
		}
		
		//为自定义设置属性时
		if(key == "-1"){
			document.getElementById('attrNameDiv').innerHTML = 
							"<input type='text' id='attrName' name='radAttrInfo.attrName' class='formCss100' />";
			document.getElementById('attrValueDiv').innerHTML = 
							"<input type='text' id='attrValue' name='radAttrInfo.attrValue' class='formCss100' />";
			document.getElementById('attrIdDiv').innerHTML = 
							"<input type='text' id='attrId' name='radAttrInfo.attrId' class='formCss100' />"
							+"<input type='hidden' id='type' name='type' value='custom'>";
			document.getElementById('attrValueTypeDiv').innerHTML = 
			"<select  id='attrValueType' class='select100' name='radAttrInfo.attrValueType'>"
				+"<option value='string'>string</option>"
				+"<option value='integer'>integer</option>"
				+"<option value='ipaddr'>ipaddr</option>"
			+"</select>";
			
			$("tr[id='customAttrNameTR']").show();
			$("tr[id='customAttrIdTR']").show();
			return;
		}else {
			setInnerHTML();
		}
		
		var vendorid = $("#vendorid").val();
		//获取Radius配置属性
		var urlTo = "<%=path%>/manager/confinfo/radius/radAttr!getRadProfileAttr.action?attrkey="+key+"&vendorid="+vendorid;
			jQuery("#attrForm").ajaxSubmit({
			async:false,
			dataType : "json",   
			type:"POST",
			url : urlTo,
			success : function(msg){
				if(msg.errorStr == 'success'){
					var strAttr = msg.object;
					var attr = strAttr.split("^^"); 
					document.getElementById('attrValueTypeDiv').innerHTML = attr[1];
					document.getElementById('attrValueDiv').innerHTML = attr[0];
				}
			}
		}); 
		
	 }
	 
	 //返回操作
	function goBack() {
		var profileId = $("#profileId").val();
		window.location.href = "<%=path%>/manager/confinfo/radius/attrlist.jsp?profileId="+profileId;
	}	
	 
    
	</script>
  </head>
  
  <body scroll="no" style="overflow:auto; overflow-x:hidden">
	  <input type="hidden" value="<%=path %>" id="contextPath" />
	  <input id="curPage" type="hidden" value="${param.currentPage}" />
	  <form id="attrForm" method="post" action="">
	   <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="topTableBg">
	      <tr>
	        <td width="98%"><span class="topTableBgText"><view:LanguageTag key="radattr_add"/></span></td>
	        <td width="2%" align="right">
	      	 <!--	<a href="javascript:openHelpWin('<%=path%>/manager/help/confighelp.html#387','<%=path%>/manager/common/js/ligerUI/skins/Gray/css/dialog.css');">
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
				        <select id="vendorid" name="radAttrInfo.vendorid" class="select100" onChange="selVendor(this.options[this.options.selectedIndex].value);">
					      <view:RadiusVendorTag dataSrc=""/>
				    	</select>
			        </td>
			        <td width="40%"><div id="vendorNameTip" style="width:100%"></div></td>
			      </tr>
			      <tr id="attrNameTR">
			        <td align="right"><view:LanguageTag key="radattr_name"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td>
			        	<div id="radAttrDIV">
					        <select id="attrSel"  name="attrSel" class="select100" onChange="changeAttr(this.options[this.options.selectedIndex].value);">
						      <view:RadiusAttrTag dataSrc="" index1Val=""/>
					    	</select>
					    </div>	
			        </td>
			        <td><div id="attrNameTip" style="width:100%"></div></td>
			      </tr>
			      
			      <tr id="attrNameTRB">
			        <td align="right"><view:LanguageTag key="radattr_name"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td></td>
			        <td></td>
			      </tr>
			      
			      <tr id="customAttrNameTR">
			        <td align="right"><view:LanguageTag key="radattr_custom_name"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td><div id="attrNameDiv"></div></td>
			        <td></td>
			      </tr>
			      
			      <tr id="customAttrIdTR">
			        <td align="right"><view:LanguageTag key="radattr_custom_id"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td><div id="attrIdDiv"></div></td>
			        <td></td>
			      </tr>
			      
			      <tr>
			        <td align="right"><view:LanguageTag key="radattr_valtype"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td><div id="attrValueTypeDiv"></div></td>
			        <td><div id="attrValueTypeTip" style="width:100%"></div></td>
			      </tr>
			      
			      <tr>
			        <td align="right"><view:LanguageTag key="radattr_value"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td><div id="attrValueDiv"></div></td>
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
			        	<input type="hidden" id="showProfileName"  value="${param.profileName}" />
			        	<a href="#" id="addBtn" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
			            <a href="javascript:goBack();" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a>
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