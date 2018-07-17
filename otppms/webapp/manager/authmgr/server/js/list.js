var contextPath;

//退出递归的条件变量     
var currentIndex = 0;
var manager = null;
$(function(){
	contextPath = $("#contextPath").val();
	var loadPage = 1;
	if($("#curPage").val() != '') {
		loadPage = $("#curPage").val();
	}
	manager = window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        url : 'authServer!init.action?isInit=1',
        columns : [
            {display:ser_hostip_lang,name:'hostipaddr', width:'20%',render:hostIpRender},
            {display:ser_hostname_lang,name:'hostname', width:'20%'},
            {display:ser_priority_lang,name:'priority', width:'10%',render:priorityRender},
            {display:syntax_desc_lang,name:'descp', width:'20%'},
            {display:ser_state_lang,name:'servstate', width:'15%'},
            {display:syntax_operation_lang,name:'', width:'10%',render:operationRenderer}
        ],
        checkbox:true,
        rownumbers:false,
        allowUnSelectRow : true,
        rowClickSingleSelect : true,
        enabledSort:false,
        parms:{'operType':1},
        page : loadPage,
        pageSize:20,
        height:'100%',
        onAfterShowData:function() {
        	dataGrid.clearParams('operType');
        	//初始化加载图片
        	initRowLoadImg();
        	
        	//重置标识为0开始
			currentIndex = 0;
        	//获取认证服务器状态
        	loadingServState();
        }
	});
	
	$("td :input").keydown(function(e) {
		if(e.keyCode == '13') {
			query(false);
		}
	});
})

// ================================  Renderers  =======================================
function priorityRender(data,index) {
	var rel = "";
	if(data.priority==0){
		rel = langSerHigh;
	}else if(data.priority==1){
		rel = langSerOrdinary;
	}else if(data.priority==2){
		rel = langSerLow;
	}else{
		rel = langNothing;
	}
	return rel;
}

//认证服务器IP列
function hostIpRender(data,index) {
	return "<a href='javascript:viewServer(\""+data.hostipaddr+"\");'>"+data.hostipaddr+"</a>";
}

//配置服务器列
function resultRender(data,index) {
	return "<a href='javascript:viewServerConf(\""+data.confid+"\",\""+data.servConfValue+"\");'>"+data.servConfValue+"</a>";
}

//初始化服务器状态加载图片
function initRowLoadImg() {
	var rowlen = dataGrid.getData().length;
	for(var i=0; i<rowlen; i++) {
	 	dataGrid.select(i);
	 	var selected = manager.getSelected();
	 	dataGrid.unselect(i);
	 	manager.updateRow(selected,{
	          servstate: '<img src='+contextPath+'/images/icon/loading.gif width="16" height="16" hspace="2" border="0" title="'+ ser_state_getting_lang +'" />'
	    });
	}
}

//加载服务器状态
function loadingServState(){
	newRequest(getServArr());
}

//获取认证服务器IP
function getServArr() {
	var rowlen = dataGrid.getData().length;
	if (rowlen > 0) {
		var serArry = new Array(rowlen);
		for(var i=0; i<rowlen; i++) {
			var hostip = dataGrid.getData()[i]['hostipaddr'];
			serArry[i] = hostip;
		}
		return serArry;
	} else {
		return '';
	}
}

//获取认证服务器状态，并修改显示状态
function newRequest(serArry){ 
	if (serArry == '') {
		return;
	}
	//serArry是存放服务器IP的数组           
	if(currentIndex >= serArry.length){ 
		return;         
	}         
	var hostip = serArry[currentIndex];
	//行标识  
	var index = 0; 
	$.ajax({ 
		async:true,
		type:"POST",
		url:contextPath + "/manager/authmgr/server/authServer!loadServerState.action",
		data:{hostip:hostip},
		dataType:"json",
		success:function(msg){
			currentIndex++; 
			var rlen = dataGrid.getData().length;
			for(var i=0; i<rlen; i++) {
				var hip = dataGrid.getData()[i]['hostipaddr'];
				if (hip == hostip) {
					index = i;
					break;
				}
			}
			//将要修改的行选中，再进行修改行内容
			dataGrid.select(index);
	 		var selected = manager.getSelected();
	 		dataGrid.unselect(index);
			if(msg.errorStr == "success") {
			//	manager.updateRow(selected,{
			//      servstate: '<span style="color:green">'+ ser_conn_succ_lang +'</span>'
			//  });
			  	var tableObj = document.getElementById("body2").getElementsByTagName("table")[0];
			    tableObj.rows[index].cells[4].innerHTML='<span style="color:green">'+ ser_conn_succ_lang +'</span>';
			}else {
			//	manager.updateRow(selected,{
			//       servstate: '<span style="color:red">'+ ser_conn_err_lang +'</span>'
			//  });
			  	var tableObj = document.getElementById("body2").getElementsByTagName("table")[0];
			    tableObj.rows[index].cells[4].innerHTML='<span style="color:red">'+ ser_conn_err_lang +'</span>';
			}
			newRequest(getServArr());
		}
	});
}     


// 操作列渲染器
function operationRenderer(data,rIndex) {
	var opers = '';
	if(permEdit!=''){
    	opers += "<a href='javascript:findObj(\""+data.hostipaddr+"\");'>"+permEdit+"</a>";
    }
    return opers;
}

// ================================ Operations =======================================
//查询
function query(currentPage){
	var page = 1;
	if(currentPage) {
		page = dataGrid.getCurrentPage();
	} 
    var pagesize = dataGrid.getCurrentPageSize();
    
	var qParams = formParms(page, pagesize);
	dataGrid.loadServerData(qParams);
}

//组装表单查询条件QueryFilter
function formParms(page, pagesize){
	var hostipaddr = $("#hostipaddr").val();
	var hostname = $("#hostname").val();
	var qParams = {
		'page':page,
		'pagesize':pagesize,
		'serverQueryForm.hostipaddr' : hostipaddr,
		'serverQueryForm.hostname' : hostname
	}
    
    return qParams;
}

//添加认证服务器
function addAuthServInfo(){
	window.location.href= contextPath+"/manager/authmgr/server/add.jsp";
	//window.location.href= contextPath+"/manager/authmgr/server/authServer!findLic.action";
} 

//编辑
function findObj(hostip){
	window.location.href = contextPath+"/manager/authmgr/server/authServer!find.action?hostipaddr=" + hostip + "&curPage=${html_page_info.curPage}&talRow=${html_page_info.totalRow}";
}
	
//查看詳細信息
function viewServer(serverip){
	FT.openWinMiddle(base_info_lang,contextPath+'/manager/authmgr/server/authServer!view.action?serverInfo.hostipaddr=' + serverip,'close');
}

function viewServerConf(confid,confvalue){
	FT.openWinMiddle(conf_info_lang,contextPath+"/manager/config/server/serverConf!find.action?confInfo.confid="+confid+"&confInfo.confvalue="+confvalue+"&source=edit&fromSource=sel_servConf",'close');
} 

//删除
function delData(){
	var selRows = dataGrid.getSelectedRows();
	if(selRows < 1){
		FT.toAlert('warn',need_del_date_lang, syntax_tip_lang);
	} else {
		$.ligerDialog.confirm(del_sel_date_lang,syntax_confirm_lang,function(yes){
			if(yes){
				var selLength = selRows.length;
	            var ids = "";
	            for(var i=0;i<selLength;i++) {
	            	ids = ids.concat(selRows[i]['hostipaddr'],',');
	            }
	            var ajaxbg = $("#background,#progressBar");//加载等待
				ajaxbg.show();
	            $.post(contextPath+"/manager/authmgr/server/authServer!delete.action",{
					delHostIps:ids.substring(0,ids.length-1)
		            },function(result){
			            if(result.errorStr == 'success'){
			            	$.ligerDialog.success(result.object, syntax_tip_lang, function(){
			            	});
			            }else if(result.errorStr == 'question'){
			            	FT.Dialog.confirm(result.object, syntax_tip_lang, function(r){
					     	 	if(r==1){//点击了 是
					     	 		// 跳转到设置主备服务
					     	 		location.href = contextPath + "/manager/confinfo/config/center!find.action?oper=authsel";
					     	 	}
					     	});
			            }else{
			            	FT.toAlert(result.errorStr, result.object, null);
			            }
			            ajaxbg.hide();
			            query(true);
		            },'json'
	            );
			}
		});			
	}
}


