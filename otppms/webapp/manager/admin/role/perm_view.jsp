<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.Date"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld"%>
<%
	String path = request.getContextPath();
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title></title>
		<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css" />
		<link href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
		<link href="<%=path%>/install/css/ligerui-tab.css" rel="stylesheet" type="text/css" />
		<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
		<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
		<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
		<script src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerTree.js" type="text/javascript"></script>
		<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerTab.js" ></script>
		<script src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js" type="text/javascript"></script>
		<script src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerMenu.js" type="text/javascript"></script>
		<script src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDrag.js" type="text/javascript"></script>
		<script type="text/javascript">
		var navtab = null;   
		var selectdTab = null;
		var treeIdStr ='';
		$(function (){
			var context = true;
			if('${param.oper}'=='view' || '${param.oper}'=='viewrole'){
				context = false;   // 查看时禁止，TAB右键功能
	       	}
	       	
	        $("#tab1").ligerTab({contextmenu: context });
	        navtab = $("#tab1").ligerGetTabManager();
	        
	        // 获取所有的角色权限
	        $.getJSON("<%=path%>/manager/admin/role/adminRole!viewPerm.action?oper=${param.oper}&roleid=${param.roleid}&t="+'<%=new Date()%>'+'&adminId=${param.adminId}',   
	        	function(json){
	            	// 循环每一个模块的权限
	            	for(var i=0;i<(json.items).length;i++){
	                	var moduel = (json.items)[i].split("#"); 
		            	var treeid = "tree"+moduel[0] ;
		                	if(i==0){
								selectdTab = treeid;
		                	}
		                	
		                 	// 每一个模块权限添加为一个tab   此处多传入了一个是否跑到最后一个tab的参数
		                 	navtab.addTabItem({height:320, tabid :treeid, text: moduel[1] ,isToLastTabItem:'view',url:'<%=path%>/manager/admin/role/permtree.jsp?permid='+moduel[0]+'&treeId='+treeid+'&expandId='+i+'&oper=${param.oper}',showClose:false});		                 
		                 	treeIdStr = treeIdStr+treeid+","; 
	             	} 
	             	
	               	$('#treeIdStr').val(treeIdStr);
	               	// 哪个模块的权限打开
			        navtab.selectTabItem(selectdTab);
	           	   	$('#pageloading').hide();          
	        	});
		});
 </script>
 <style type="text/css">
	#pageloading{position:absolute; left:0px; top:0px; background:white url('<%=path%>/images/icon/loading.gif') no-repeat center; width:100%; height:100%; z-index:99999;}

	.l-link{display:block; line-height:22px; height:22px; padding-left:20px;border:1px solid white; margin:4px;}
	.l-link-over{background:#FFEEAC; border:1px solid #DB9F00;}
}
</style>
</head>
	<div id="pageloading"></div>
	<body scrolling="no" style="overflow-y:hidden;">
	<input id="contextPath" type="hidden" value="<%=path%>" />
	   <input type="hidden" id="treeIdStr" value="" />
	   <c:if test="${param.oper=='view'}">
	        <div id="tab1" style="height:350px;overflow:hidden;width:100%;border:1px solid #A3C0E8;"></div>
	   </c:if>
	   <c:if test="${param.oper!='view'}">
	   		 <div id="tab1" style="width:99%;border:1px solid #A3C0E8;"></div>
	   </c:if>
	   <div style="display:none"></div>
	</body>
</html>