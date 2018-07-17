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
    
	<script language="javascript" type="text/javascript">
	
	$(function() {
	
		//回车事件
	   $("td :input").keydown(function(e) {
			if(e.keyCode == '13') {
				return false;
			}
		});
	
		//绑定点击事件
		domClickEvent();
		//点击按钮弹出窗口，设置窗口样式，取消已绑定点击事件
		var cssurl = "<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"
        btnClickEvent("#addBtn",cssurl);
	
	      $.formValidator.initConfig({submitButtonID:"addBtn",debug:true,
			onSuccess:function(){
	    	  if($("#profileId").val()== null || $("#profileId").val()==''){
			      addObj(0); // 添加
			    }else{
			      addObj(1); // 编辑
			    }
			},
			onError:function(){
				return false;
			}});
			
			$("#profileName").formValidator({onFocus:'<view:LanguageTag key="radius_vd_name_show"/>'}).inputValidator({min:1,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="radius_vd_name_err"/>'},onError:'<view:LanguageTag key="radius_vd_name_err_1"/>'});
			$("#profileDesc").formValidator({onFocus:'<view:LanguageTag key="common_vd_descp_show"/>',onCorrect:"OK"}).inputValidator({min:0, max:255,onError:'<view:LanguageTag key="common_vd_descp_error"/>'});
			$("#profileName").focus();
	 })
	 
	 //校验RADIUS配置名称
	 function check(){
	        var profileId = $("#profileId").val();	
			var profileNameHid = $("#profileNameHid").val();	
			var profileName = $("#profileName").val();
			
			if(profileName != profileNameHid) {
				validateProfileName();
			}else {
				if (profileId == null || "" == profileId) {
					validateProfileName();
				} else {
					$("#profileName").formValidator({onFocus:'<view:LanguageTag key="radius_vd_name_show"/>'}).inputValidator({min:1,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="radius_vd_name_err"/>'},onError:'<view:LanguageTag key="radius_vd_name_err_1"/>'});
				}
			}
	 }
	 
	 function validateProfileName() {
	 	$("#profileName").ajaxValidator({
			dataType:"html",
			async:true,
			url:"<%=path%>/manager/confinfo/radius/radProfile!findRPFNameisExist.action",
			success:function(data){
	            if(data =='false') {return false;}
				return true;
			},
			buttons:$("#addBtn"),
			error:function(jqXHR, textStatus, errorThrown){
				$.messager.alert('<view:LanguageTag key="common_vd_warning"/>','<view:LanguageTag key="common_vd_server_is_busy"/>','warning');
			},
			onError:'<view:LanguageTag key="common_vd_already_exists"/>',
			onWait:'<view:LanguageTag key="common_vd_checking_exists"/>'
		});
	 }
     
     //添加RADIUS配置
	 function addObj(oper){
		var url = "<%=path%>/manager/confinfo/radius/radProfile!add.action";
		var cpage = 1;
		if(oper == 1){
			cpage = $("#curPage").val();
			url = "<%=path%>/manager/confinfo/radius/radProfile!modify.action";
		}
		if(oper == 0){ // 添加
			$("#addForm").ajaxSubmit({
				async:false,    
				dataType : "json",  
				type:"POST", 
				url : url,
				success:function(msg){
					if(msg.errorStr == 'success'){
				     	FT.Dialog.confirm(getLangVal('radius_vd_name_succ',contextPath), getLangVal('common_syntax_tip',contextPath), function(r){
				     	 	if(r==1){//点击了 是
				     	 		var profileId = msg.object.split(",")[1];
				     	 		location.href = '<%=path%>/manager/confinfo/radius/addattr.jsp?profileId='+profileId;		
				     	 	}else if(r==0){//点击了否
				     	 		location.href = '<%=path%>/manager/confinfo/radius/profilelist.jsp?curPage='+cpage;
				     	 	}
				     	});
				    }else{
						FT.toAlert(msg.errorStr,msg.object,null);
				    }
				}
			});
		}else{
			$("#addForm").ajaxSubmit({
				async:false,    
				dataType : "json",  
				type:"POST", 
				url : url,
				success:function(msg){
					if(msg.errorStr == 'success'){
				    	$.ligerDialog.success('<view:LanguageTag key="common_save_succ_tip"/>', '<view:LanguageTag key="common_syntax_tip"/>',function(){
				        	location.href = '<%=path%>/manager/confinfo/radius/profilelist.jsp?curPage='+cpage;
				     	});
				    }else{
					 	FT.toAlert(msg.errorStr,msg.object,null);
				    }
				}
			});
		}
	}
	
	//返回操作
	function goBack() {
		window.location.href="<%=path%>/manager/confinfo/radius/profilelist.jsp";
	}	

	</script>
  </head>
  
  <body scroll="no" style="overflow:auto; overflow-x:hidden">
	  <input type="hidden" value="<%=path %>" id="contextPath" />
	  <input id="curPage" type="hidden" value="${param.currentPage}" />
	  <form id="addForm" method="post" action="">
	    <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="topTableBg">
	      <tr>
	        <td width="98%">
	        	<span class="topTableBgText">
	        	<c:if test="${empty radProfileInfo.profileId}"><view:LanguageTag key="radius_add"/></c:if>
	        	<c:if test="${not empty radProfileInfo.profileId}"><view:LanguageTag key="radius_edit"/></c:if>
	        	</span>
	        </td>
	        <td width="2%" align="right">
	      	<!-- 	<a href="javascript:openHelpWin('<%=path%>/manager/help/confighelp.html#387','<%=path%>/manager/common/js/ligerUI/skins/Gray/css/dialog.css');">
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
			        <td width="30%" align="right"><view:LanguageTag key="radius_name"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td width="30%">
				        <input type="text" id="profileName" name="radProfileInfo.profileName"  value="${radProfileInfo.profileName}" onchange="check();" class="formCss100" />
				        <input type="hidden" id="profileNameHid" value="${radProfileInfo.profileName}"  />
			        </td>
			        <td width="40%" class="divTipCss"><div id="profileNameTip"></div></td>
			      </tr>
			      <tr>
			        <td align="right"><view:LanguageTag key="radius_descp"/><view:LanguageTag key="colon"/></td>
			        <td><textarea id="profileDesc" name="radProfileInfo.profileDesc" class="textarea100">${radProfileInfo.profileDesc}</textarea></td>
			        <td class="divTipCss"><div id="profileDescTip"></div></td>
			      </tr>
			      <tr> 
					    <td colspan="3" height="10"></td>
				  </tr>
				</table>
				<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="bottomTableCss">
			      <tr>
			        <td width="30%">&nbsp;</td>
			        <td> 
			        	<input type="hidden" id="profileId" name="radProfileInfo.profileId" value="${radProfileInfo.profileId}" />
			        	<a href="#" id="addBtn" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
			            <a href="javascript:goBack();" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a>			        
			        </td>
		          </tr> 
			    </table>
	   		</td>
	   	  </tr>
		</table>    
	</form>
</body>
</html>