var contextPath ;

// 所有设备列表初始化
function initEquipmentList(){
	contextPath = $("#contextPath").val() + "/";
	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        async: false,
        url : 'monitordrecord!init.action',
        columns : [
        	{display:"服务器IP",name:'ipAddress', width:'25%'},
            {display:"服务器类型",name:'serverType', width:'25%',render:serverTypeRender},
            {display:"服务器运行状态",name:'runningState', width:'25%',render:runningStateRender},
            {display:"记录时间",name:'recordTimeStr', width:'25%'}
        ],
        parms:{'operType':1},
        checkbox:false,
        rownumbers:true,
        allowUnSelectRow : true,
        rowClickSingleSelect : true,
        enabledSort:false,
        usePager : false,
        height:'100%'
	});
}


// 服务器系统类型 渲染器
function serverTypeRender(data,i){
	var serverType = data.serverType;
	var serverTypeName = "";
	if(serverType=="1"){
		// 管理中心
		serverTypeName = "主服务器";
	}else if(serverType=="2"){
		// 用户门户
		serverTypeName = "从服务器";
	}
	return serverTypeName;
}

//服务器运行状态 渲染器
function runningStateRender(data,i){
	var runningState = data.runningState;
	var runningStateName = "";
	if(runningState=="0"){
		// 管理中心
		runningStateName = "<span style='color:red'>异常</span>";
	}else if(runningState=="1"){
		// 用户门户
		runningStateName = "<span style='color:green'>正常</span>";
	}
	return runningStateName;
}

// 定时刷新 刷新数据方法  param:arrListId 列表ID数组
function refreshData(arrListId){
return;
	for(var i=0;i<arrListId.length;i++){
		var manager = $("#"+arrListId[i]+"").ligerGetGridManager();             
		manager.loadData(true); 
	}
}