<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
%>
<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
<script type="text/javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.5.2.min.js" ></script>
<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/window/popup.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/window/popupclass.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerTreeCommonOrgunit.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerTab.js" ></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
<script type="text/javascript" src="<%=path%>/manager/orgunit/orgunit/js/initOrgunitTree.js"></script>
<script language="javascript" type="text/javascript">
	// Start,多语言提取
	var orgunit_lang = '<view:LanguageTag key="domain_orgunit"/>';
	var search_data_lang = '<view:LanguageTag key="org_no_search_data"/>';
	var key_word_lang = '<view:LanguageTag key="org_search_key_word"/>';
	// End,多语言提取
</script>

<input type="hidden" value="<%=path %>" id="contextPath" />
<div style="width:100%"><input type=text id="searchText" value='<view:LanguageTag key="org_search_key_word"/>' onkeyup="loadNodes(this.id,event);" style="width:100%"></div>
<div id="orgTreeList0" class="l-scroll" style="width:95%; height:95%; margin:5px; float:left; overflow:auto; ">
	<ul id="wholeOrgunitTree"></ul>
</div>