<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
%>
	<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
	<script type="text/javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js" ></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerTab.js" ></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script src="<%=path%>/manager/main/js/left.js" type="text/javascript"></script>
	<script language="javascript" type="text/javascript">
	</script>
    
  	<input type="hidden" name="contextPath" id="contextPath" value="<%=path %>" />
  	<input type="hidden" name="module" id="module" value="${param.module}" />
  	
	<div class="l-scroll" style="width:95%; height:98%; margin:10px; float:left; overflow:auto; ">
		<ul id="uTree" ></ul>
    </div>
