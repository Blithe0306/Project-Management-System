var contextPath;
var queryData = null;//保存令牌查询条件 避免执行的数据和显示的查询条件不一致

$(function(){
	contextPath = $("#contextPath").val();
	
	var selValue=$("select option:selected").val();
	var url = "adminLog!init.action";
	if(selValue!=null){
		url = "adminLog!init.action?adminLog.queryMark=1";
	 } 
	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        url : url,
        columns : [
            {display:operator_lang,name:'operator', width:'10%'},
            {display:oper_time_lang,name:'logTimeStr', width:'10%'},
            {display:client_ip_lang,name:'clientip', width:'12%'},
            {display:actionid_lang,name:'actionDesc', width:'18%'},
            {display:log_info_lang,name:'descp', width:'24%', render:updateDescpRender},
            {display:operate_result_lang,name:'actionresult', width:'10%', render:resultRender},
            {display:detail_info_lang,name:'', width:'10%', render:logViewRenderer} 
        ],
        checkbox:false,
        rownumbers:true,
        allowUnSelectRow : true,
        rowClickSingleSelect : true,
        enabledSort:false,
        parms:{'operType':1},
        page : 1,
        pageSize:20,
        height:'100%',
        onAfterShowData:function() {
        	dataGrid.clearParams('operType');
        }
	});
	// 赋值查询条件
	setQueryObjData();
	$("td :input").keydown(function(e) {
		if(e.keyCode == '13') {
			query(false,true);
		}
	});
})

// ================================  Renderers  =======================================

//删除管理员日志中的 <br> 信息
function updateDescpRender(data,index){
	var desc ="";
	if(data != null && data.descp != ""){
		if(data.descp.indexOf('</br>') || 
			data.descp.indexOf('<br/>') || 
				data.descp.indexOf('<br>')){
			desc = data.descp.replace(/\<\/br\>/gm,"")
		}
	}
	return desc;
}

function resultRender(data,index) {
	return data.actionresult == 0 ? langSucc : '<span style="color:red">'+ langFailure +'</span>';
}

// 查看日志明细
function logViewRenderer(data,rIndex) {
	var opers;
    var permView = "<img src="+contextPath+"/images/icon/zoom.png width='16' height='16' style='padding-top:3px;' title='"+ langDetails +"'>";
	opers = "<a href='javascript:view(" + data.id + ");'>" + permView + "</a>";
	return opers;
}

// 根据日志id查看日志详细信息
function view(id){
	FT.openWinMiddle(detail_info_lang,contextPath + '/manager/logs/adminLog!view.action?id=' + id,'close');
}
// ================================ Operations =======================================
//查询
function query(stayCurrentPage,isGetQuery){
	if(isGetQuery){
		setQueryObjData(isGetQuery);
	}
	var page = 1;
	if(stayCurrentPage) {
		page = dataGrid.getCurrentPage();
	}
	var pagesize = dataGrid.getCurrentPageSize();
	var qParams = formParms(page, pagesize);

    var operator=$.trim($('#operator').val());
	$.ajax({
		async:false,    
		dataType : "json",  
		type:"POST", 
		url : contextPath + "/manager/logs/adminlog/adminLog!checkAdminId.action?adminId="+operator,
		success:function(msg){
		    if(msg.errorStr == 'error'){
			    qParams = formParms(msg.errorStr, pagesize);
		    }
		}
	});
	dataGrid.loadServerData(qParams);
}

function formParms(page, pagesize){
	if(page == "error"){
		page = 1;
		queryData.operator = "----"; // 没有权限，特殊赋值；
	}
	
    var qParams = {
    	'page':page,
		'pagesize':pagesize,
		'adminLogQueryForm.operator' : queryData.operator,
		'adminLogQueryForm.startLogTime' : queryData.startDate,
		'adminLogQueryForm.endLogTime' : queryData.endDate,
		'adminLogQueryForm.descp' : queryData.descp,
		'adminLogQueryForm.clientIp' : queryData.clientIp,
		'adminLogQueryForm.actionResult' : queryData.actionResult 
    }
    return qParams;
}

//设置查询条件的值
function setQueryObjData(){
	queryData = new Object();
	//查询值
	queryData.operator = $.trim($('#operator').val());
	queryData.startDate = $('#startDate').val();
	queryData.endDate = $('#endDate').val();
	queryData.descp = $('#descp').val();
	queryData.clientIp = $('#clientIp').val();
	queryData.actionResult = $('#actionResult').val();
}
