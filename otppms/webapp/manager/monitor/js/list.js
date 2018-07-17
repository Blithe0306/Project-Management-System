var contextPath ;
$(function(){
	contextPath = $("#contextPath").val()
});

// 所有设备列表初始化
function initEquipmentList(){
	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        async: false,
        url : 'monitor!getEquipmentList.action',
        columns : [
        	{display:device_ip_lang,name:'ipAddr', width:'20%',render:ipAddrRender},
            {display:device_sername_lang,name:'serverName', width:'25%'},
            {display:ser_sys_type_lang,name:'serverType', width:'25%',render:serverTypeRender},
            {display:device_ser_health_lang,name:'runState', width:'20%'}
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

// ip 渲染器
function ipAddrRender(data,i){
	return "<a href='javascript:viewEquipmentInfo(\"" + data.ipAddr + "\",\"" + data.port + "\",\""+data.serverType+"\",\""+data.clientServPath+"\");' title='"+device_title_tip_lang+"'>" + data.ipAddr + "</a>";
}

// 服务器系统类型 渲染器
function serverTypeRender(data,i){
	var serverType = data.serverType;
	var serverTypeName = "";
	
	if(serverType==0){
		// 管理中心
		serverTypeName = monitor_center_lang;
	}else if(serverType==1){
		// 用户门户
		serverTypeName = monitor_portal_lang;
	}else{
		// 认证服务器
		serverTypeName = monitor_auth_ser_lang;
	}
	
	return serverTypeName;
}

function viewEquipmentInfo(ipAddr,port,serverType,clientServPath){
	$.ligerDialog.open({
		title:detail_info_lang,
		url:contextPath + '/manager/monitor/equipmentInfo.jsp?ipaddr=' + ipAddr +'&port='+port+'&clientservpath='+clientServPath+'&servertype='+serverType,
		width:700,
		height:380,
		name:'winFrame',
		isResize:false,
		isDrag:false,
		buttons: FT.bs['close']
	});
}

// 内存信息列表
function initMemInfoList(ipAddr,port,serverType,clientServPath){
	var urlStr = 'manager/monitor/monitor!getMemInfo.action?ipaddr='+ipAddr+'&port='+port+'&clientservpath='+clientServPath+'&servertype='+serverType;
	window['dataGrid'] = $("#memListDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        async: false,
        url : urlStr,
        columns : [
        	{display:'',name:'memTypeName', width:'30%'},
            {display:memory_total_lang,name:'memTotal', width:'15%'},
            {display:memory_used_lang,name:'memUsed', width:'16%'},
            {display:memory_surplus_lang,name:'memFree', width:'15%'},
            {display:memory_occupancy_lang,name:'memUse', width:'15%'}
        ],
        parms:{'operType':1},
        checkbox:false,
        rownumbers:true,
        allowUnSelectRow : true,
        rowClickSingleSelect : true,
        enabledSort:false,
        usePager : false,
        height:'45%'
	});
}

// cpu信息列表
function initCpuInfoList(ipAddr,port,serverType,clientServPath){
	var urlStr = 'manager/monitor/monitor!getCpuInfo.action?ipaddr='+ipAddr+'&port='+port+'&clientservpath='+clientServPath+'&servertype='+serverType;
	window['dataGrid'] = $("#cpuListDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        async: false,
        url : urlStr,
        columns : [
        	{display:all_use_rate_lang,name:'cpuCombined', width:'13%'},
            {display:cpu_curr_free_rate_lang,name:'cpuIdleTime', width:'13%'},
            {display:cpu_user_use_rate_lang,name:'cpuUserTime', width:'13%'},
            {display:cpu_sys_use_rate_lang,name:'cpuSysTime', width:'13%'},
            {display:cpu_curr_wait_rate_lang,name:'cpuWaitTime', width:'13%'},
            {display:cpu_interrupt_req_lang,name:'cpuIrqTime', width:'13%'},
            {display:cpu_nice_use_rate_lang,name:'cpuNiceTime',width:'13%'}
        ],
        parms:{'operType':1},
        checkbox:false,
        rownumbers:true,
        allowUnSelectRow : true,
        rowClickSingleSelect : true,
        enabledSort:false,
        usePager : false,
        height:'33%'
	});
}
// 磁盘信息列表
function initDrInfoList(ipAddr,port,serverType,clientServPath){
	var urlStr = 'manager/monitor/monitor!getDfInfo.action?ipaddr='+ipAddr+'&port='+port+'&clientservpath='+clientServPath+'&servertype='+serverType;
	window['dataGrid'] = $("#drListDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        async: false,
        url : urlStr,
        columns : [
        	{display:file_system_lang,name:'dfDevName', width:'13%'},
            {display:file_letter_lang,name:'dfDirName', width:'13%'},
            {display:file_letter_total_size_lang,name:'dfTotal', width:'12%'},
            {display:file_letter_use_size_lang,name:'dfUsed', width:'13%'},
            {display:file_letter_avai_size_lang,name:'dfFree', width:'13%'},
            {display:file_letter_rate_lang,name:'dfUsePercent', width:'12%'},
            {display:file_type_lang,name:'dfSysTypeName',width:'15%'}
        ],
        parms:{'operType':1},
        checkbox:false,
        rownumbers:true,
        allowUnSelectRow : true,
        rowClickSingleSelect : true,
        enabledSort:false,
        usePager : false,
        height:'70%'
	});
}

// 初始化应用系统在线用户数列表
function initAppStateList(){
	var urlStr = 'manager/monitor/monitor!getAppStateInfo.action';
	window['dataGrid'] = $("#appListDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        async: false,
        url : urlStr,
        columns : [
        	{display:app_sys_name_lang,name:'serverName', width:'30%'},
            {display:device_ip_lang,name:'ipAddr', width:'30%'},
            {display:online_users_lang,name:'onlineUsers', width:'30%'}
          
        ],
        parms:{'operType':1},
        checkbox:false,
        rownumbers:true,
        allowUnSelectRow : true,
        rowClickSingleSelect : true,
        enabledSort:false,
        usePager : false
//        height:'35%'
	});
}

// 初始化认证服务器状态列表
function initServerStateList(){
	var urlStr = 'manager/monitor/monitor!getServerStateInfo.action';
	window['dataGrid'] = $("#serverListDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        async: false,
        url : urlStr,
        columns : [
        	{display:auth_ser_name_lang,name:'serverName', width:'30%'},
            {display:device_ip_lang,name:'ipAddr', width:'30%'},
            {display:syntax_status_lang,name:'runState', width:'30%'}
        ],
        parms:{'operType':1},
        checkbox:false,
        rownumbers:true,
        allowUnSelectRow : true,
        rowClickSingleSelect : true,
        enabledSort:false,
        usePager : false
	});
}

// 定时刷新 刷新数据方法  param:arrListId 列表ID数组
function refreshData(arrListId){
return;
	for(var i=0;i<arrListId.length;i++){
		var manager = $("#"+arrListId[i]+"").ligerGetGridManager();             
		manager.loadData(true); 
	}
}