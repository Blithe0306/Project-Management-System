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
	<script type="text/javascript" src="<%=path%>/manager/admin/user/js/change_creater_user.js"></script>
	<script language="javascript" type="text/javascript">
		// Start,多语言提取
		var role_name_lang = '<view:LanguageTag key="admin_role_name"/>';
		var info_creator_lang = '<view:LanguageTag key="common_info_creator"/>';
		var info_descp_lang = '<view:LanguageTag key="admin_info_descp"/>';
		// End,多语言提取
		
		$(document).ready(function() {          
        	var userIdStr = '${html_page_obj}';   
         	var userIdStrs = userIdStr.split(','); 
         	var userIdHtml = "";
           	for(var i=0;i<userIdStrs.length;i++){
            	userIdHtml = userIdHtml+userIdStrs[i]+"</br>";
         	}
         	$('#userDiv').html(userIdHtml);    
		});
      

	    //变更创建人
      	function designateUser(win){
      		var user = $('#adminid').val();
      		if(user=='---'){
         		FT.toAlert('warn','<view:LanguageTag key="admin_vd_sel_designee"/>',null);
          		return false;
       		}
      		var sRows = dataGrid.getSelectedRows(),
      		sInfo = [];
			if(sRows.length<1){
			   FT.toAlert('warn','<view:LanguageTag key="admin_sel_role"/>',null);
			   return;
			}
			var roleArr='';
			for(var i=0,sl=sRows.length;i<sl;) {
				sRow = sRows[i++];
				roleArr  = roleArr + sRow['roleId'] +",";
			}
       		var cpage = ${param.curPage};
       		if(cpage==''||cpage==null){
          		cpage=1;
       		}
       		FT.Dialog.confirm('<view:LanguageTag key="role_sure_change_creator"/>', '<view:LanguageTag key="common_syntax_tip"/>', function(r){
				if(r == 1){
					$("#ListForm").ajaxSubmit({
	    				async:false,    
	    				dataType : "json",  
	    				type:"POST", 
	    				url : 'manager/admin/user/adminUser!designate.action?userIdStr=${html_page_obj}&&cPage='+cpage+'&roleArr='+roleArr,
	    				success:function(msg){
	    			    	if(msg.errorStr == 'success'){
	    			        	parent.winClose('<view:LanguageTag key="admin_replace_creator_succ"/>');
	    			     	}else if(msg.errorStr == 'warn'){
	    			    	  	FT.toAlert(msg.errorStr,msg.object,null);
	    			     	}else{
	    			           	FT.toAlert('warn','<view:LanguageTag key="admin_replace_creator_err"/>',null);
	    			    	}
	    				}
	    			});
				}
   			});
      }
      
      function okClick(btn,win,index){
         designateUser(win);
      }
	</script>
</head>
<body style="overflow:auto;overflow-x:hidden">
<input id="currentPage" type="hidden" value="${param.curPage}"/>
<form name="ListForm" method="post" action="" id="ListForm">
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" style="margin:8px;">
	 <tr>
 	   <td width="10%"  align="right" valign="top"><view:LanguageTag key="admin_designee"/><view:LanguageTag key="colon"/></td>
        <td width="26%"  align="left" valign="top">
        	<select id="adminid" name="adminUser.adminid" onchange="query(false);" class="select100_2">
            	<option value="---"><view:LanguageTag key="admin_sel_designee"/></option>
              	<c:forEach items="${html_page_list}" var="admuser">
                 	<option value="${admuser.adminid}">${admuser.adminid}</option>
              	</c:forEach>
            </select></td>
       <td width="64%"></td>
     </tr>          
</table>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="98%"><span class="topTableBgText"><view:LanguageTag key="admin_info_sel_role"/></span></td>
  </tr>
</table>
</form>	
<div id="listDataAJAX"></div>
</body>
</html>
