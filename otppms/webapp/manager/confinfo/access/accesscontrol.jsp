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
    <title><view:LanguageTag key="common_menu_config_access"/></title>
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
	var editWin;
	$(document).ready(function(){
		$("#enabledflag").val('${trustipenable }');
	 	setIPList();
	});
	
	//设置初始化下拉列表数据
 	function setIPList() {
 		var sel = document.getElementById("accessIpList");
 		var accessIPStr = '${accessIPStr}';
 		if (accessIPStr == '' || accessIPStr == null || accessIPStr == 'undefined') {
 			return;
 		}else {
	 		var iparr = (accessIPStr.substring(0, accessIPStr.length - 1)).split(":");
			for(var j=0; j<iparr.length; j++) {
				var accIP = iparr[j];
				var accIPArr = accIP.split(",");
			    var option = new Option();
			    option.value = accIPArr[0];
			    option.text = accIPArr[1];
			    sel.options[sel.options.length]= option;
			}
 		}
 	}
 	
 	//Select列表删除
	function delSelIP(selName){
		if(!operSelObj(selName)){
		   FT.toAlert('warn','<view:LanguageTag key="trustip_remove_show"/>',null);
		   return;
		}
		var objs = document.getElementById(selName);
		for (i = objs.options.length - 1; i >= 0; i--) {
			if (objs.options[i].selected) {
				objs.options[i] = null;
			}
		}
	}
	
	//编辑访问控制IP
	function editIPWin(selName) {
		if(!operSelObj(selName)){
		   FT.toAlert('warn','<view:LanguageTag key="trustip_edit_show"/>',null);
		   return;
		}
		var num =0;
		var objs = document.getElementById(selName);
		for (i = objs.options.length - 1; i >= 0; i--) {
			if (objs.options[i].selected) {
				num ++;
			}
		}
		if(num >1) {
			FT.toAlert('warn','<view:LanguageTag key="trustip_edit_sel_one"/>',null);
		    return;
		}
		var selText  = $("#accessIpList option:selected").html();  
		editWin = FT.openWinSet('<view:LanguageTag key="trustip_edit"/>', '<%=path%>/manager/confinfo/access/edittrustip.jsp?selText='+selText, [{text:'<view:LanguageTag key="common_syntax_sure"/>',onclick:FT.buttonAction.okClick},{text:'<view:LanguageTag key="common_syntax_cancel"/>',onclick:FT.buttonAction.cancelClose}], true, 500, 250);
	}
	
	//保存启用状态
	function saveTrustFlag() {
		var selval = getCheckValue();
		var url = '<%=path%>/manager/confinfo/config/center!modify.action';
		$.post(url, {"centerInfo.trustipenabled" : selval, "oper" : "trustip"},
			function(msg){
			    if(msg.errorStr != 'success'){
			        FT.toAlert('error',msg.object, null);
			    }else {
			    	$("#enabledflag").val(selval);
			    }
			}, "json"
		);
	}
	
	//获取单选框选中值
	function getCheckValue() {
		var obj; 
		var checkValue;   
	    obj = document.getElementsByName("trustipenable");
	    if(obj != null){
		    var i;
		    for(i=0;i<obj.length;i++){
		       if(obj[i].checked){
		          checkValue = obj[i].value;
		       }
	        }
        }
        return checkValue;
	}
	
	//校验本地IP是否存在，是否添加
	function checkLocalIP(val) {
		var localIP = $("#localIP").val();
		if (localIP =='127.0.0.1' || localIP == 'localhost') {
			if(val == 1) {
				saveObj();
			}
			return;
		}
		var message = '<view:LanguageTag key="trustip_enable_show"/>';	
		var iplist = getTrustIpList();
		var selflag = getCheckValue();
		
		//当控制策略禁用并且保存的时候不做检查
		if (selflag == 0 && val == 1) {
			saveObj();
		} else {
			var reval = checkAllowIP(localIP);
			if (reval == 'success') {
			 	$.ligerDialog.confirm(message, '<view:LanguageTag key="common_syntax_confirm"/>', function(sel) {
					if(sel) {
						$("#accessIpList").append("<option value=''>" + localIP + "</option>");
						if(val == 1) saveObj();
					}else {
						var objs = document.getElementById("accessIpList");
						var len = objs.options.length;
						if(val == 1 && len > 0) {
							saveObj();
						}else{
							FT.toAlert('warn', '<view:LanguageTag key="trustip_disabled_show"/>', null);
		    				return;
						}
					}
				});
			}else {
				if(val == 1) saveObj();
			}
		}
	}

	//校验IP是否已存在
 	function checkAllowIP(val) {
 		var reval = 'success';
 		var iplist = getTrustIpList();	
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
	
	function saveObj() {		
		var iplist = getTrustIpList();
		var url = '<%=path%>/manager/confinfo/config/access!add.action';	
		$.ajax({
			type: "POST",
			url: url,
			async: false,
			data: {"trustIPList":iplist },
			dataType: "json",	    
			success: function(msg){
				if(msg.errorStr == 'success'){
					addData();
					saveTrustFlag();
					FT.toAlert('success',msg.object,null);
				} else {
					FT.toAlert('warn',msg.object,null);
				}
			}
		});
	}
	
	//添加下拉列表数据option元素
	function addData(){
		var url = '<%=path%>/manager/confinfo/config/access!getIPStr.action';	
		$.ajax({
			type: "POST",
			url: url,
			async: true,
			dataType: "json",	    
			success: function(msg){
				if(msg.errorStr == 'success'){
					var sel = document.getElementById("accessIpList");
					$("#accessIpList").empty();//清空下拉列表
					var ipStr = msg.object;
					if (ipStr == '' || ipStr == null || ipStr == 'undefined') {
			 			return;
			 		}
					var iparr = (ipStr.substring(0, ipStr.length - 1)).split(":");
					for(var j=0; j<iparr.length; j++) {
						var accIP = iparr[j];
						var accIPArr = accIP.split(",");
					    var option = new Option();
					    option.value = accIPArr[0];
					    option.text = accIPArr[1];
					    sel.options[sel.options.length]= option;
					}
					
				}
			}
		});
	}
	
	function cancelObj() {
		addData();
		var trustenable = $("#enabledflag").val();
		if (trustenable == 0) {
		 	$("input[name='trustipenable'][value=0]").attr("checked",true); 
		}else {
		 	$("input[name='trustipenable'][value=1]").attr("checked",true); 
		}
	}
	
	
	//获取信任IP列表
	function getTrustIpList() {
		var objs = document.getElementById("accessIpList");
		var selIPText='';
		for (i = objs.options.length - 1; i >= 0; i--) {
			if (objs.options[i].text != "") {
				selIPText += objs.options[i].text+",";
			}
		}
		return selIPText;
	}
	
	//打开添加信任IP窗口
	function openAddIPWin() {
		addWin = FT.openWinSet('<view:LanguageTag key="trustip_add_trustip"/>', '<%=path%>/manager/confinfo/access/addtrustip.jsp', [{text:'<view:LanguageTag key="common_syntax_add"/>',onclick:FT.buttonAction.okClick},{text:'<view:LanguageTag key="common_syntax_cancel"/>',onclick:FT.buttonAction.cancelClose}], true, 500, 250);
	}
	 
	</script>
  </head>
  <body>
  <form id="portalForm" method="post" action="" >
   <input id="localIP" type="hidden" type="text" value="${localIP }" />
   <input id="enabledflag" type="hidden" value="" />
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="topTableBg">
      <tr>
        <td width="98%"><span class="topTableBgText"><view:LanguageTag key="common_menu_config_access"/></span></td>
        <td width="2%">
	        	<a href="javascript:addAdmPermCode('050202','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a>
	        </td>
      </tr>
     </table>
     <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
      <tr>
        <td valign="top">
        	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		        <tr><td colspan="3" height="10"></td></tr>
		        <tr>
		          <td width="35%" align="right"><view:LanguageTag key="trustip_ip_access_cont_strategy"/><view:LanguageTag key="colon"/></td>
		          <td width="45%">	          	
                	<input type="radio" id="trustipenable1" name="trustipenable" value="1" onclick="checkLocalIP(0);"
                		<c:if test="${trustipenable eq 1}">checked</c:if> /><view:LanguageTag key="common_syntax_yes"/>&nbsp;&nbsp;&nbsp;&nbsp;
		          	<input type="radio" id="trustipenable" name="trustipenable" value="0"
               			<c:if test="${trustipenable eq 0}">checked</c:if> /><view:LanguageTag key="common_syntax_no"/> 
		          </td>
		          <td width="20%"></td>
		        </tr>
		        <tr><td colspan="3" height="10"></td></tr>
		    </table>
		     <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable" >
				<tr>
					<td width="35%" align="right"><view:LanguageTag key="trustip_allow_accip_list"/><view:LanguageTag key="colon"/></td>
					<td width="45%" align="left">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr valign="top">
								<td width="50%">
									<select id="accessIpList" name="accessInfo.accessIpList" size="20" multiple class="select100" style="height:280px;">
									</select>
								</td>
								<td width="1%"></td>
								<td width="49%">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td><a href="javascript:openAddIPWin();" id="saveIP" class="button"><span><view:LanguageTag key="common_syntax_add"/></span></a></td>
										</tr>
										<tr>
											<td><a href="javascript:editIPWin('accessIpList');" id="editIP" class="button"><span><view:LanguageTag key="common_syntax_modify"/></span></a></td>
										</tr>
										<tr>
											<td><a href="javascript:delSelIP('accessIpList');" id="delSelIP" class="button"><span><view:LanguageTag key="common_syntax_move"/></span></a></td>
										</tr>
									</table>
								</td>								
							</tr>
						</table>
					</td>
					<td width="20%"></td>
				</tr>
				<tr> 
					<td colspan="3" height="8"></td>
				</tr>
             	<tr>
	                <td align="right"> &nbsp;</td>
	                <td>
	                	<a href="javascript:checkLocalIP(1);" id="saveBt" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a> 
	                	<a href="javascript:cancelObj();" id="cancelBt" class="button"><span><view:LanguageTag key="common_syntax_reset"/></span></a> 
	                </td>
	                <td>&nbsp;</td>
	            </tr>
		   </table>
       </td>
      </tr>
     </table> 
   </form>
  </body>
</html>