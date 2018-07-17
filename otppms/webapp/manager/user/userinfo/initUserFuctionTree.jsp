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
<script src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=path%>/manager/common/js/window/popup.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/window/popupclass.js"></script>
	  <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerTab.js" ></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
<script type="text/javascript">
	$(initMessage);
	function initMessage(){
	    
        $("#wholeOrgunitTree").ligerTree({ 
        	textFieldName:'name',
            checkbox:false,
            //nodeWidth:1000, 
            onClick:doMethod,
          	data:[
				    { name: '<view:LanguageTag key="user"/>', 
				        tab:'03',
				        url:'<%=path%>/manager/user/userinfo/list.jsp',
				        children: [
				        { name: '<view:LanguageTag key="user_info_add"/>', tab:'0300',url:'<%=path%>/manager/user/userinfo/add.jsp?source=top'},
				        { name: '<view:LanguageTag key="user_common_batch_bind"/>', tab:'0301',url:'<%=path%>/manager/user/userinfo/m_m_bind.jsp'},
				        { name: '<view:LanguageTag key="user_common_batch_unbind"/>', tab:'0302',url:'<%=path%>/manager/user/userinfo/m_m_unbind.jsp'},
				        { name: '<view:LanguageTag key="user_import_title_user"/>', tab:'0303',url:'<%=path%>/manager/user/userinfo/import_user.jsp'},
				        { name: '<view:LanguageTag key="user_export_title_user"/>', tab:'0304',url:'<%=path%>/manager/user/userinfo/export_user.jsp'}
				        ]
				    }
				]

        });
        manager = $("#wholeOrgunitTree").ligerGetTreeManager();
        manager.loadData(null,'',null);
	}
	
	//选中事件触发方法  将来搜索后也触发该方法
	function doMethod(node){
		//window.parent.removeTabItemF(node.data.tab);
	    //window.parent.addTabItemF(node.data.tab,node.data.name,node.data.url);
	      
	    // 判断是否存在此tab 不存在添加，存在覆盖修改，不要先删除remove再添加 删除会触发左侧导航刷新事件
	   	if(window.parent.isTabItem(node.data.tab)){
	   		window.parent.overrideTabItemF(node.data.tab,node.data.name,node.data.url);
	   	}else{
			window.parent.addTabItemF(node.data.tab,node.data.name, node.data.url);	    		
	   	}
	}
</script>
   
   
   
   
  <div id="orgTreeList0" title='<view:LanguageTag key="org_tree"/>' class="l-scroll">
 		<ul  id="wholeOrgunitTree">
 		</ul>
	</div>
