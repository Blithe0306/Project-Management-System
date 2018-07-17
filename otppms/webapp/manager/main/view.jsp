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
<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
<link href="<%=path%>/manager/common/css/home.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
<script src="<%=path%>/manager/common/js/jquery/jquery-1.6.4.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
<script language="javascript" type="text/javascript">
		$(document).ready(function() {
			var permSysInfo = '<view:AdminPermTag key="000001" path="<%=path%>" langKey="common_syntax_base_info" type="1" />';
			if(permSysInfo == ''){
					$('#sysinfo').hide();
			}
		});
		
		//通列li的索引跳转到指定标签页，执行上一步，下一步的功能
		function stepController(index){			
			$("#tabs li.tabFocus").removeClass("tabFocus current"); //移除已选中的样式
			$("#tabs li:eq(" + index + ")").addClass("tabFocus current");//增加当前选中项的样式
			$("#output li:eq(" + index + ")").show().siblings().hide();//显示选项卡对应的内容并隐藏未被选中的内容
		}
		
		
		function viewRole(roleId) {
			FT.openWinSet('<view:LanguageTag key="role_perm"/>','<%=path%>/manager/admin/role/view.jsp?roleId='+roleId,'close',true,600,420);
		}
		
		// 用户统计图初始化
		function userReportChartInit(){
			var url = "<%=path%>/manager/report/reportAction!userReportHome.action";
			//创建统计图
			createFusionCharts("<%=path%>/manager/common/charts/Column3D.swf",'chart1',url,'userChart',367,250);
			//chart.componentAttributes.exportEnabled = 0;
			//设置导出相关
			//setChartsExporter("fcExporter1","<%=path%>/manager/common/charts/FCExporter.swf","userExportDiv");
		}
		
		// 令牌统计图初始化
		function tokenReportChartInit(){
			var url = "<%=path%>/manager/report/reportAction!tokenReportHome.action";
			//创建统计图
			var chart = createFusionCharts("<%=path%>/manager/common/charts/Column3D.swf",'chart2',url,'tokenChart',367,250);
			//设置导出相关
			//setChartsExporter("fcExporter2","<%=path%>/manager/common/charts/FCExporter.swf","tokenExportDiv");
		}
		
		
		// 先判断用户列表和令牌列表的权限
		var userListPerm = '<view:AdminPermTag key="0200" path="<%=path%>" langKey="common_menu_user_list" type="0"/>';
		var tokenListPerm = '<view:AdminPermTag key="0300" path="<%=path%>" langKey="common_menu_tkn_list" type="1"/>';
		
		// 快速查询
		function quckQuery(){
			var queryText = $.trim($("#queryText").val());
			if(queryText == ''){
				FT.toAlert('warn','<view:LanguageTag key="index_query_text_empty"/>',null);
				return;
			}
			
			var queryType = $("#queryType").val();
			if(queryType==0){//用户或令牌
				// 先查询用户的数量 如果无 则查令牌 如果都无 则提示没有查到
				var url = '<%=path%>/manager/user/userInfo!countTknOrUserAtHome.action?queryText='+queryText;
				$.post(url,
					function(msg){
						if(msg!=""){
							var userOrTkn = msg.split(",");
							if(userOrTkn[0]==0&&userOrTkn[1]==0){
								FT.toAlert('warn','<view:LanguageTag key="index_query_tkn_user_empty"/>',null);
								return;
							}else if(userOrTkn[0]==0&&userOrTkn[1]!=0){// 令牌列表
								queryTab(queryText,1);
							}else if(userOrTkn[0]!=0&&userOrTkn[1]==0){// 用户列表
								queryTab(queryText,0);
							}
						}
					}, "text"
				);
				
			}else if(queryType==1){//用户列表
				queryTab(queryText,0);
			}else if(queryType==2){//令牌列表
				queryTab(queryText,1);
			}
		}
		
		//在右侧窗口打开标签
		function queryTab(queryText,listType){
			if(listType==0){//用户列表
				if(userListPerm!=''){
					var url = '<%=path%>/manager/user/userinfo/list.jsp?mode=3&queryText='+queryText;
					
					if(window.parent.isTabItem('0200')){
				   		window.parent.overrideTabItemF('0200','<view:LanguageTag key="common_menu_user_list"/>',url);
				   		window.parent.selectTabItemF('0200');
				   	}else{
						window.parent.addTabItemF('0200','<view:LanguageTag key="common_menu_user_list"/>',url);	    		
				   	}
				}else{
					FT.toAlert('warn','<view:LanguageTag key="index_query_user_no_perm"/>',null);
					return;
				}
			}else{//令牌列表
				if(tokenListPerm!=''){
					var url = '<%=path%>/manager/token/list.jsp?mode=3&queryText='+queryText;
					
					if(window.parent.isTabItem('0300')){
				   		window.parent.overrideTabItemF('0300','<view:LanguageTag key="common_menu_tkn_list"/>',url);
				   		window.parent.selectTabItemF('0300');
				   	}else{
						window.parent.addTabItemF('0300','<view:LanguageTag key="common_menu_tkn_list"/>',url);	    		
				   	}
				}else{
					FT.toAlert('warn','<view:LanguageTag key="index_query_token_no_perm"/>',null);
					return;
				}
			}
		}
	    
</script>
</head>
<body>
<input type="hidden"  name="contextPath" value="<%=path%>" id="contextPath"/>
<div id="sysinfo">

	<table width="98%" height="100%" border="0" cellspacing="0" cellpadding="0" class="tableLine">
	        <tr>
	          <td valign="top" width="100%"><ul id="tabs">
	              <li style="float:left" class="tabFocus" onclick="stepController(0)"><a href="javascript:;"><img src="<%=path%>/images/manager/programming.png" height="20" width="20" border="0"/>
	                <view:LanguageTag key="index_sys_introduction"/>
	                </a></li>
	            </ul></td>
	        </tr>
	        <tr>
	          <td>
		          <ul id="output">
		              <li>
		                <jsp:include page="../../manager/main/sys_info.jsp" />
		              </li>
		          </ul>
	          </td>
	        </tr>
	      </table>
</div>
</body>
</html>
