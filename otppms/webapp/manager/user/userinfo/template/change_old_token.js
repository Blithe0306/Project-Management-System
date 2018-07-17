var contextPath;
var oldTotalRow = 0; 
var queryData = null;//保存令牌查询条件 避免执行的数据和显示的查询条件不一致

$(function(){
	contextPath = $("#contextPath").val();
	var userId=$("#userId").val();
    var domainId=$("#domainId").val();
    var orgunitId=$("#orgunitId").val();
    var orgunitIds=$("#orgunitIds").val(); 
    var bindState = $("#bindState").val();
    
	window['oldDataGrid'] = $("#oldTokenListAjx").ligerGrid({
		root : 'items',
        record : 'results',
        url : 'userInfo!initOldToken.action',
        columns : [
            {display:tknum_lang,name:'token', width:'40%'}
        ],
        parms:{operType:'1','userInfo.userId':userId,'userInfo.domainId':domainId},
        checkbox:true,
        allowUnSelectRow : true,
        enabledSort:false,
        page : 1,
        pageSize:20,
        height:'100%',
        onAfterShowData:function() {
        }
	});
	
	// 赋值查询条件
	queryObjData();
	
	// 回车完成查询
	$("td :input").keydown(function(e){
		if(e.keyCode == 13) {
			return false;
		}
	});
})


/**
 * 查询,根据条件让Grid重新载入数据
 * @param {} stayCurrentPage 是否留在当前页面
 */
function queryOldToken(stayCurrentPage,isGetQuery){
	oldTotalRow = oldDataGrid.getCurrentTotal();
	// 赋值查询条件
	if(isGetQuery){
		queryObjData(isGetQuery);
	}
	var page = 1;
	if(stayCurrentPage) {
		page = oldDataGrid.getCurrentPage();
	}
	var pagesize = oldDataGrid.getCurrentPageSize();
    var queryParam = formParmsOldToken(page, pagesize);
    oldDataGrid.loadServerData(queryParam);
}

//设置查询条件的值
function queryObjData(){
	queryData = new Object();
	//查询值
	queryData.userId = $.trim($("#userId").val());   
	queryData.domainId = $("#domainId").val();
}


//组装表单查询条件QueryFilter
function formParmsOldToken(page, pagesize){
    var queryParamOld = {
		'page':page,
    	'pagesize':pagesize,
    	'userInfo.userId':queryData.userId,
        'userInfo.domainId':queryData.domainId
    }
    return queryParamOld;
}

// 下一步，到选择新令牌处
function nextChangeTkn(){
	var sRows = oldDataGrid.getSelectedRows(),
	sInfo = [];
	var tknBatchSn = "";
	if(sRows.length<1){
	   FT.toAlert('warn',replaced_tkn_lang,null);
	   return;
	}
	
	for(var i=0,sl=sRows.length;i<sl;) {
		var role = {},sRow = sRows[i++];
		role.token = sRow['token'];
		tknBatchSn = tknBatchSn + sRow['token'] + ",";
	}
	//选择的用户的个数
	$('#changeTknCount').html(sRows.length);
	
	$('#tokenCount').val(sRows.length);
	
	//选择的令牌字符串
	$('#changeTkn').html(tknBatchSn);
	queryNewToken(false,true);
    stepController(1);
}
 
/**
 * 处理返回信息的显示与动态隐藏
 * @param {} data 信息包装JSON
 */
function showAndHiddenMsg(data) {
	var jMSG = $("#msgShow").find('span').empty().append(data.object).end().fadeIn('slow');
    setTimeout(function(){jMSG.fadeOut('normal')},4500);
}
