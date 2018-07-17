var contextPath;

$(function(){
	contextPath = $("#contextPath").val();
	var url = "userLog!init.action";
	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        url : url,
        columns : [
            {display:query_user_lang,name:'userid', width:'10%'},
            {display:comm_tknum_lang,name:'token', width:'10%'},
            {display:oper_time_lang,name:'logTimeStr', width:'10%'},
            {display:client_ip_lang,name:'clientip', width:'10%'},
            {display:server_ip_lang,name:'serverip', width:'10%'},
            {display:actionid_lang,name:'actionid', width:'11%',render:actionIdRender},
            {display:failed_info_lang,name:'logcontent', width:'13%',render:actionIdContent},
            {display:operate_result_lang,name:'actionresult', width:'10%',render:operResultRender},
            {display:detail_info_lang,name:'', width:'10%', render:viewRenderer}
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
	
	$("td :input").keydown(function(e) {
		if(e.keyCode == '13') {
			query(false);
		}
	});
})

// ================================  Renderers  =======================================
// 操作名称
function actionIdRender(data,rIndex) {
	return getLangVal('packet_'+data.actionid,contextPath);
}

// 日志描述
function actionIdContent(data,rIndex) {
	return getLangVal(data.logcontent,contextPath);
}
// 操作结果
function operResultRender(data,index) {
	return data.actionresult == 1 ? langSucc : '<span style="color:red">'+ langFailure +'</span>';
}

// 查看日志明细
function viewRenderer(data,rIndex) {
	var opers;
    var permView = "<img src="+contextPath+"/images/icon/zoom.png width='16' height='16' style='padding-top:3px;' title='"+ langDetails +"'>";
	opers = "<a href='javascript:view(" + data.id + ");'>" + permView + "</a>";
	return opers;
}

// 查看日志详细信息
function view(id){
	FT.openWinMiddle(syntax_detail_lang,contextPath + '/manager/logs/userLog!view.action?id=' + id,'close');
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
	var userid = $.trim($("#userid").val());
	var tokenStr = $.trim($("#tokenStr").val());
	var startDate = $('#startDate').val();
	var endDate = $('#endDate').val();
	var clientIp = $('#clientIp').val();
	var actionResult = $('#actionResult').val();
		
	var qParams = {
		'page':page,
		'pagesize':pagesize,
		'userLogQueryForm.userid' : userid,
		'userLogQueryForm.token' : tokenStr,
		'userLogQueryForm.startDate' : startDate,
		'userLogQueryForm.endDate' : endDate,
		'userLogQueryForm.clientIp' : clientIp,
		'userLogQueryForm.actionResult' : actionResult 
	}
    
    return qParams;
}
