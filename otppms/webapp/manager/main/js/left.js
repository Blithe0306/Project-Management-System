/**
 * 用户管理Tree,使用LigerUI库中的ligerTree组件
 * 该树节点数据可以通过三种方式加载：
 * 1、HTML标签转换成树(通过ul li嵌套)
 * 2、JSON方式,配置data属性,将data对应的JSON串写进Javascript中.
 * 3、远程JSON,配置url属性,该URL对应的服务器资源返回指定格式的JSON字符串即可.
 * 此处采用临时第三种方式,数据配置在XML文件中,服务器端读取并转换成JSON返回.
 */
var contextPath;
$(function(){
   contextPath = $("#contextPath").val();
   initMessage();
})

function initMessage(){
	var module = $("#module").val(); // 模块名,指示菜单树加载合适数据
	async : true,
	$.post('treeAction!loadTreeData.action?module=' + module+"&randomNum="+Math.random(),null, getTree, 'json');
}

var manager;
function getTree(data){
	var module = $("#module").val(); // 模块名,指示菜单树加载合适数据
 	var nodeWidth = 1000;
 	// 如果是首页
 	if(module=="default"){
 		nodeWidth = 300;
 	}
 	
	$("#uTree").ligerTree({
		//url: 'treeAction!loadTreeData.action?module=' + module+"&&randomNum="+Math.random() ,	// 数据获取路径
		slide:false,							//不以动画方式展示树
		checkbox: false,
		nodeWidth: nodeWidth,				// 指定节点宽度,此数据会应用到ul的宽度,会影响到节点文本过长不显示
		btnClickToToggleOnly: false,		// 是否只在点击+/—图标时展开/收缩节点,设置为false后点击节点任意位置均可触发
		onClick: function(node) {
			if(node.data==null){
				// 首页点击节点 触发删除事件
				if(module=="default"){
			  		var imgId = node.target.id;//删除img的id格式为 delImg_ + 权限码permcode
			  		if(imgId!=""||imgId!=undefined){
			  			var permcode = imgId.split("_")[1];
			  			delAdmPermCode(permcode,contextPath);
			  		}
			  	}
			}else{
				if(node.data.url!=''){
					var nodeUrl = node.data.url;
					var url=$("#contextPath").val()+ nodeUrl;
					var tabTitle = "";
						if(module=="default"){
							tabTitle = $("#delImg_"+node.data.permcode).parent().text();
						}else{
							tabTitle = node.data.text;
						}
						
						var pcode = node.data.permcode;
						
			        	if(window.parent.isTabItem(pcode)){
				    		window.parent.overrideTabItemF(pcode,tabTitle,url);
				    		window.parent.selectTabItemF(pcode);
				    	}else{
							window.parent.addTabItemF(pcode,tabTitle, url);	    		
				    	}
			        }else{
			        	return false;
			        }
			}			
		},
		data: data
	});
	
	// 如果是首页
	if(module=="default"){
	 		$("li[outlinelevel=2]").bind("mouseover",function(){
			$(this).find("img").show();
		});
		$("li[outlinelevel=2]").bind("mouseout",function(){
			$(this).find("img").hide();
		});
	}
	
	manager = $("#uTree").ligerGetTreeManager();
	
	//选择当前打开的tab树节点
	if(window.parent.tab != null&&window.parent.tab != undefined){
		selectOperNode(window.parent.tab.getSelectedTabItemID());
	}
}

// 根据相关数据tabid选中节点
function selectOperNode(id){
	if(manager==null||manager==undefined){
		return;
	}

	var nodes = manager.nodes;
	if(nodes==null||nodes==undefined||nodes.length<=0){
		return;
	}
	
	for(var i = 0;i < nodes.length; i++){
 		if(id == nodes[i].permcode){
			var currNode = manager.getSelected();
			if(currNode != null&&currNode != undefined){// 如果已有选中节点
				// 判断本次选中节点值和 旧的选中节点值是否一样
				if(id!=currNode.data.permcode){
					// 已选中的节点样式去掉
					var C = $(currNode.target),A = $(">div:first", C);
					A.removeClass("l-selected");
					// 选中节点
					manager.selectNode(manager.getNodeDom(nodes[i]));
					break; 	
				}
			}else{
				// 旧的树没选中任何节点 则只添加选中样式
				// var CU = $(nodes[i].target),B = $(">div:first", CU);
				// B.addClass("l-selected");
				manager.selectNode(manager.getNodeDom(nodes[i]));
				break;
			}
		}
	}
}
  
//删除首页管理员常用功能树 的权限节点
function delAdmPermCode(permCode, cPath) {
    //var url = cPath + 'manager/common/language/langAction!getLangTagVal.action';
	$.ajax({
		type: "POST",
	    url: "adminUser!delPermCode.action",
	    async: false,
	    data: {"permcode":permCode},
	    dataType: "json",
	    success: function(msg){
	   		//只删除失败提示，成功不提示刷新树
			if(msg.errorStr == 'error'){
				parent.FT.toAlert(msg.errorStr,msg.object,null);
			}else{
			    location.href = location.href;
			    
				//清空树
	  			//$("#uTree").empty();
	  			// 初始化树 只能重新初始化 remove不可以
	  			//initMessage();
			}
		}
	});
}
