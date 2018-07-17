var contextPath;
var newTotalRow=0;
var queryData = null;//保存令牌查询条件 避免执行的数据和显示的查询条件不一致

$(function(){
    contextPath = $("#contextPath").val();
   	var userId=$("#userId").val();
    var domainId=$("#domainId").val();
    var orgunitId=$("#orgunitId").val();
    var bindState = $("#bindState").val();
    var page = 1;
	window['newDataGrid'] = $("#newTokenListAjx").ligerGrid({
		root : 'items',
        record : 'results',
        url : 'userInfo!initNewToken.action',
        columns : [
            {display:tknum_lang,name:'token', width:250},
            {display:orgunit_lang,name:'domainOrgunitName', width:250},
            {display:state_bind_lang,name:'bindTag', width:250,render:bindTagRenderer}
        ],
        checkbox:true,
        allowUnSelectRow : true,
        enabledSort:false,
        parms:{operType:'1',userId:userId,domainId:domainId,orgunitId:orgunitId,bindState:bindState},
        page : page,
        pageSize:20,
        height:'100%',
        onAfterShowData:function() {
        	newDataGrid.clearParams('operType');
        }
	});
	
	// 赋值查询条件
	setQueryObjData();
	
	// 回车完成查询
	$("td :input").keydown(function(e){
		if(e.keyCode == 13) {
			queryNewToken(true,true);
		}
	});
	 
})

function bindTagRenderer(data,rIndex){
 	return data.bindTag == 1 ? '<span style="color:red">'+ langBound +'</span>' : langUnbound;
}


/**
 * 查询,根据条件让Grid重新载入数据
 * @param {} stayCurrentPage 是否留在当前页面
 */
function queryNewToken(stayCurrentPage,isGetQuery){
    newTotalRow = newDataGrid.getCurrentTotal();
	// 赋值查询条件
	if(isGetQuery){
		setQueryObjData(isGetQuery);
	}
	var page = 1;
	if(stayCurrentPage) {
		page = newDataGrid.getCurrentPage();
	}
	var pagesize = newDataGrid.getCurrentPageSize();
    var queryParam = formParms(page, pagesize);
    
	newDataGrid.loadServerData(queryParam);
}

//设置查询条件的值
function setQueryObjData(){
	queryData = new Object();
	//查询值
	queryData.userId = $.trim($("#userId").val());   
	queryData.domainId = $.trim($("#domainId").val());
	queryData.orgunitId = $.trim($("#orgunitId").val());
	queryData.bindState = $("#bindState").val();   
	queryData.token = $.trim($("#tokenStr").val()); 
	queryData.orgunitIds = $("#orgunitIds").val();   
}

//组装表单查询条件QueryFilter
function formParms(page, pagesize){
    var queryParamTn = {
    	'page':page,
    	'pagesize':pagesize,
    	'domainId':queryData.domainId,
    	'orgunitId':queryData.orgunitId,
    	'tokenQueryForm.orgunitIds':queryData.orgunitIds,
    	'tokenQueryForm.bindState':queryData.bindState,
    	'tokenQueryForm.userId':queryData.userId,
        'tokenQueryForm.token':queryData.token
    }
    return queryParamTn;
}

 