var contextPath;

$(function(){
	contextPath = $("#contextPath").val();
	var loadPage = 1;
	if($("#curPage").val() != '') {
		loadPage = $("#curPage").val();
	}
	
	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        url : 'authServer!init.action',
        columns : [
            {display:ser_hostip_lang,name:'hostipaddr', width:'25%'},
            {display:ser_hostname_lang,name:'hostname', width:'25%'},
            {display:ser_priority_lang,name:'priority', width:'10%',render:priorityRender},
            {display:syntax_desc_lang,name:'descp', width:'35%'}
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
        }
	});
	$(".conFocus :input").keydown(function(e) {
		if(e.keyCode == '13') {
			query(false);
		}
	});
})

// ================================  Renderers  =======================================
function priorityRender(data,index) {
	var rel = "";
	if(data.priority==0){
		rel = priority_high_lang;
	}else if(data.priority==1){
		rel = priority_ordinary_lang;
	}else if(data.priority==2){
		rel = priority_low_lang;
	}else{
		rel = syntax_nothing_lang;
	}
	return rel;
}

//认证服务器IP列
function hostIpRender(data,index) {
	return "<a href='javascript:viewServer(\""+data.hostIPAddr+"\");'>"+data.hostIPAddr+"</a>";
}

//配置服务器列
function resultRender(data,index) {
	return "<a href='javascript:viewServerConf(\""+data.confid+"\",\""+data.servConfValue+"\");'>"+data.servConfValue+"</a>";
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
	var serverIPAddr = $("#serverIPAddr").val();
	var qParams = {
		'page':page,
		'pagesize':pagesize,
		'serverQueryForm.hostipaddr' : serverIPAddr
	}
    
    return qParams;
}
