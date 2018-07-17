<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld"%>
<%@page import="com.ft.otp.common.language.Language"%>
<%
	String path = request.getContextPath();

	String titleImg = Language.getCurrLang(request.getSession());
	titleImg += ".png";
%>
<html >
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>
<view:LanguageTag key="system_noun_name"/>
</title>
<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
<script src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=path%>/manager/common/js/window/popup.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/window/popupclass.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerTab.js" ></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
<script language="javascript" type="text/javascript">
<!--	        
	var tab = null;
	var accordion = null;
	var tree = null;
	var pwdWin = null;
	var editWin = null;//打开的编辑窗口
	var currPage = null;//当前操作的页面
	$(function () {
		//布局
		$("#layout1").ligerLayout({leftWidth:200, bottomHeight:30, height: '100%', allowTopResize: false, allowBottomResize: false, onHeightChanged: f_heightChanged });
		var height = $(".l-layout-center").height();
	
		//Tab
		$("#framecenter").ligerTab({ height: height });
	
		//面板
		$("#accordion1").ligerAccordion({ height: height - 32, speed: null });
		$(".l-link").hover(function ()
		{
			$(this).addClass("l-link-over");
		}, function ()
		{
			$(this).removeClass("l-link-over");
		});
	                
		//树
		$("#tree1, #tree2").ligerTree({
			checkbox: false,
			slide: false,
	        nodeWidth: 152,
	        attribute: ['nodename', 'url'],
	        onSelect: function (node)
	        {
	        	if (!node.data.url) return;
				var tabid = $(node.target).attr("tabid");
				if (!tabid)
				{
					tabid = new Date().getTime();
					$(node.target).attr("tabid", tabid)
				}
				
                /*if ($(">ul >li", tab.tab.links).length >= 4)
                    {
                    var currentTabid = $("li.l-selected", tab.tab.links).attr("tabid"); //当前选择的tabid
                    if (currentTabid == "home") return;
                    tab.overrideTabItem(currentTabid, { tabid: tabid, url: node.data.url, text: node.data.text });
                	return;
                }*/
				f_addTab(tabid, node.data.text, node.data.url); //在右侧窗口打开标签
			}
		});

		tab = $("#framecenter").ligerGetTabManager();
		accordion = $("#accordion1").ligerGetAccordionManager();
		tree = $("#tree1, #tree2").ligerGetTreeManager();
		$("#pageloading").hide();
		
		// tab 绑定选中tab触发事件 afterSelectTabItem
	    tab.bind('beforeSelectTabItem', function (tabId){
	    	// 初始化左侧页面
			initLeftPage(tabId);
	    }); 
	    
	    // tab 绑定删除tab后事件
	    tab.bind('afterRemoveTabItem', function(tabId){
	        // 删除一个tab后触发事件 获取当前选中的tabid 切换左侧导航
			initLeftPage(tab.getSelectedTabItemID());
	    });
	});

	function f_heightChanged(options){
		if (tab)
			tab.addHeight(options.diff);
		if (accordion && options.middleHeight - 32 > 0)
			accordion.setHeight(options.middleHeight - 32);
	}

	/*
		number 是模块序号 
				8=组织机构模块 
				0首页 
				1管理员
				2用户管理
				3令牌管理
				4认证管理
				5配置管理
				6日志管理
				7报表管理
				9监控预警
	*/
	function f_addTab(number,obj,tabid, text, url){	
		// 初始化左侧页面
		initLeftPage(number);
		
		addTabItemF(tabid,text,url);
	}
	
	//修改密码,由于JS引进会有冲突，所以直接调用$.ligerDialog.open方法，不引入js文件
	function modifyPwd(){
		pwdWin = FT.openWinSmall('<view:LanguageTag key="common_syntax_change_pwd"/>', "<%=path%>/manager/main/repassword.jsp",[{text:'<view:LanguageTag key="common_syntax_modify"/>',onclick:FT.buttonAction.okClick},{text:'<view:LanguageTag key="common_syntax_close"/>',onclick:FT.buttonAction.cancelClose}]);	
	}

	function winClose(obj){
		$.ligerDialog.success(obj,'<view:LanguageTag key="common_syntax_tip"/>',function(){
			pwdWin.close();
		});
	}
	
	//修改信息
	function modifyBaseInfo(){
		var adminId =$('#adminId').val();
		editWin = FT.openWinSet('<view:LanguageTag key="common_syntax_change_base"/>',"<%=path%>/manager/admin/user/adminUser!baseInfoFind.action?adminUser.adminid="+adminId,[{text:'<view:LanguageTag key="common_syntax_modify"/>',onclick:FT.buttonAction.okClick},{text:'<view:LanguageTag key="common_syntax_close"/>',onclick:FT.buttonAction.cancelClose}],true,500,340);
	}

	function editMoClose(){
		$.ligerDialog.success('<view:LanguageTag key="common_save_succ_tip"/>','<view:LanguageTag key="common_syntax_tip"/>',function(){
			editWin.close();
		});
	}
	
	//框架外层大窗口 打开关闭方法
	function openBigWin(title,url){
		var height = 600;
		var width = 1000;
		if(window.screen.width<1024){
			height = 450;
			width = 800;
		}else if(window.screen.width==1024){
			height = 500;
			width = 850;
		}else if(window.screen.width>1024&&window.screen.width<1680){
			height = 550;
			width = 900;
		}
		editWin = FT.toOpen(title,url,width,height);
	}
	function bigWinClose(){
		editWin.hidden();
	}
	
	function addTabItemF(tabid,text,url){ 
	   tab.addTabItem({ tabid : tabid, text: text, url: url });
	   
	   if(navigator.userAgent.indexOf("MSIE 6.0") > -1){//浏览器判断 如果是IE6 需要再执行一次
			tab.addTabItem({ tabid : tabid, text: text, url: url });
	   }
	}
	//移除tag
	function removeTabItemF(tabid){ 
	   tab.removeTabItem(tabid);
	}
	
	//覆盖tag
	function overrideTabItemF(tabid,text,url){
		tab.overrideTabItem(tabid,{tabid : tabid, text: text, url: url});
	}
	
	//选择tab
	function selectTabItemF(tabid){
		tab.selectTabItem(tabid);
	}

	// 判断TAG存不存在
	function isTabItem(tabid){
		return tab.isTabItemExist(tabid);
	}
	
	// 初始化左侧页面
	function initLeftPage(tabidOrNumber){
		// 获取当前左侧页面 树所属模块	
		var currMoule = $(window.frames["leftFunction"].document).find("input[id='module']").val();
		var toMoule = getLeftUrl(tabidOrNumber,0);
		
		// 如果将要加载模块 和当前模块不一样或是组织机构则重新加载树
		if(toMoule == ''||toMoule != currMoule){
			var hrefurl = getLeftUrl(tabidOrNumber,1);
			$("#leftFunction").attr("src",hrefurl);
		}else{// 没重新加载树
			// 重新加载或页签选中后 调树节点选择方法  除组织机构模块
			if(toMoule != ''){
				window.frames["leftFunction"].selectOperNode(tabidOrNumber);
			}
		}
	}
	
	// 根据tabid获取module 或者 url
	function getLeftUrl(tabid,mouleOrUrl){
		var leftUrl = "";
		var module = "";
	
		// tabid 前两位 代表所属模块
		if(tabid.length>2 && tabid != "home"){
			tabid = tabid.substring(0,2);
		}
		
		if(tabid=="08"||tabid=='8'){
			leftUrl = "<%=path%>/manager/orgunit/orgunit/initOrgunitTree.jsp";
		}else{
			if(tabid=='home'||tabid=='0'){// 首页
				module="default";
			}else if(tabid=='01'||tabid=='1'){// 管理员管理
				module="admin";
			}else if(tabid=='02'||tabid=='2'){// 客户管理
				module="customer";
			}else if(tabid=='03'||tabid=='3'){// 定制管理
				module="projectmgr";
			}else if(tabid=='04'||tabid=='4'){// 认证管理
				module="authmgr";
			}else if(tabid=='05'||tabid=='5'){// 配置管理
				module="config";
			}else if(tabid=='06'||tabid=='6'){// 日志管理
				module="logs";
			}else if(tabid=='07'||tabid=='7'){// 报表管理 				
				module="report";
			}
			
			if(module!=""){
				leftUrl = "<%=path%>/manager/main/left.jsp?module="+module;
			}
		}
		
		if(mouleOrUrl == '0'){
			return module;
		}else{
			return leftUrl;
		}
	}
	
//-->
</script>
<style type="text/css">
body,html{height:100%;}
body{ padding:0px; margin:0;   overflow:hidden;}  

#pageloading{position:absolute; left:0px; top:0px; background:white url('<%=path%>/images/icon/loading.gif') no-repeat center; width:100%; height:100%; z-index:99999;}

.l-link{display:block; line-height:22px; height:22px; padding-left:20px;border:1px solid white; margin:4px;}
.l-link-over{background:#FFEEAC; border:1px solid #DB9F00;}

.leftBottom-img-bg{
	background-image: url(<%=path%>/images/layout/mgr_r6_c1.png);
	background-repeat: repeat-x;
	height: 5px;
	background-color: #FFFFFF;
	background-position: 0px 0px;
}

.bottom-img-bg{
	background-image: url(<%=path%>/images/layout/mgr_r6_c3.png);
	background-repeat: repeat-x;
	height: 30px;
	text-align: center;
}

.top-div-set {
	position:absolute;
	left:0px;
	top:0px;
	width:100%;
	height:69px;
	z-index:100;
}

.top-bg-set{
	background-image: url(<%=path%>/images/layout/mgr_r1_c6.png);
	background-repeat: repeat-x;
	background-position: top;
}
</style>
</head>
<body style="padding:0px;">
<input id="contextPath" type="hidden" value="<%=path%>" />
<input id="adminId" type="hidden" value="${curLoginUser}" />
<div id="pageloading"></div>
<div class="top-div-set">
  <table width="100%" height="80" border="0" cellpadding="0" cellspacing="0" background="<%=path%>/images/layout/top_left_1.png">
    <tr>
      <td valign="top"><table width="100%" height="48" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td width="45%" height="43" valign="middle"><table width="590" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="260"><img src="<%=path%>/images/layout/<%=titleImg%>" width="260" height="30"></td>
                  <td width="275" align="left">
				  <span class="text_Bai_Se">
				  <view:LanguageTag key="sys_welcome" replaceVal="${curLoginUser}"/></span>
				  <!-- <view:LanguageTag key="admin_role"/>${session_mark.roleName}</span> -->
			  </td>
                </tr>
              </table></td>
            <td width="55%" align="right" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="65%" align="right"><img src="<%=path%>/images/layout/top_right_1.png" width="75" height="28"></td>
                  <td width="35%" valign="top" background="<%=path%>/images/layout/top_right_2.png"><table width="324" height="22" border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td align="left">
                        <!-- <img src="<%=path%>/images/icon/help_1.png" width="16" height="16" hspace="3" align="absmiddle"><a href="#" class="isLink_LanSeCu"><view:LanguageTag key="common_syntax_help" /></a> -->
						<img src="<%=path%>/images/icon/password.png" width="16" height="16" hspace="3" align="absmiddle"><a href="#" onClick="javaScript:modifyPwd();" class="isLink_LanSeCu"><view:LanguageTag key="common_syntax_change_pwd" /></a>
						<img src="<%=path%>/images/icon/pwd_edit.png" width="16" height="16" hspace="3" align="absmiddle"><a href="#" onClick="javaScript:modifyBaseInfo();" class="isLink_LanSeCu"><view:LanguageTag key="common_syntax_change_base" /></a>
						<img src="<%=path%>/images/icon/user_logout.png" width="16" height="16" hspace="3" align="absmiddle"><a href="<%=path%>/logout!logout.action" target="_parent" class="isLink_LanSeCu"><view:LanguageTag key="common_syntax_safety_exit" /></a>
						</td>
                      </tr>
                  </table></td>
                </tr>
              </table></td>
          </tr>
        </table>
        <table width="1024" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="1"></td>
          </tr>
        </table>
        <table width="100%" height="30" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td width="1%" height="28">&nbsp;</td>
            <td width="99%" valign="top"><jsp:include page="/manager/main/menus.jsp" /></td>
          </tr>
        </table></td>
    </tr>
  </table>
</div>
<div style="height:80px;"></div>
<div id="layout1" style="width:100%">
  <div position="left" title='<view:LanguageTag key="sys_navigation_menu"/>'>
    <div id="accordion1" name="accordion1" >
      <iframe frameborder="0" width="100%" height="100%" name="leftFunction" id="leftFunction" src="<%=path%>/manager/main/left.jsp?module=default" scrolling="no" ></iframe>
    </div>
  </div>
  <div position="center" id="framecenter" style="background-color:#FFFFFF">
    <div tabid="home" title='<view:LanguageTag key="common_title_home"/>'>
      <iframe frameborder="0" name="homeFrame" id="homeFrame" src="<%=path%>/manager/main/view.jsp"></iframe>
    </div>
  </div>
  <div position="bottom" class="bottom-img-bg"><span class="text_Lan_Se">
    <view:LanguageTag key="system_noun_copyright"/>
    &nbsp;&nbsp;
    <view:LanguageTag key="system_version_text"/>
    </span></div>
</div>
<div style="display:none;"></div>
</body>
</html>
