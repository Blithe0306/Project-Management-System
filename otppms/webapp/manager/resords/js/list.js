var contextPath;


$(function(){

	var url = "resordsAction!init.action";
//$(function(){
//	contextPath = $("#contextPath").val();
//	
//	var loadPage = 1;
//	if($("#currentPage").val() != '') {
//		loadPage = +$("#currentPage").val();
//	}
//	var url = "resordsAction!init.action";
	
	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        url : url,
        columns : [
            {display:'项目名称',name:'prjid', width:'10%',render:resordsRender},
            {display:'上门人员',name:'recordUser', width:'10%'},
            {display:'上门原因',name:'reason', width:'10%'},
            {display:'上门成果',name:'results', width:'10%'},
            {display:'上门开始时间',name:'recordtimeshowStr', width:'10%'},
            {display:'上门结束时间',name:'endrecordtimeStr', width:'10%'},
            {display:'描述',name:'remark', width:'20%'},
            {display:'操作',name:'', width:'10%', render:logViewRenderer} 
        ],
        checkbox:true,
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

	$("td :input").keydown(function(e) {
		if(e.keyCode == '13') {
			query(false);
		}
	});
});

// ================================  Renderers  =======================================


function resultRender(data,index) {
	return data.actionresult == 0 ? langSucc : '<span style="color:red">'+ langFailure +'</span>';
}




// ================================ Operations =======================================
//查询
function query(stayCurrentPage){

	var page = 1;
	if(stayCurrentPage) {
		page = dataGrid.getCurrentPage();
	}
	var pagesize = dataGrid.getCurrentPageSize();
	var qParams = formParms(page, pagesize);

	dataGrid.loadServerData(qParams);
}
function formParms(page, pagesize){
	var prjid = $("#prjid").val();
    var recordUser = $("#recordUser").val();
    var qParams = {
    	'page':page,
		'pagesize':pagesize,
		'projectQueryForm.prjid':prjid,
		'projectQueryForm.recordUser':recordUser
		
    }
    return qParams;
}

/**
 * 客户渲染函数
 */
function resordsRender(data,i) {
	
	return "<a href='javascript:view(\"" + data.id + "\");'>" + data.prjid + "</a>";
}

//根据客户唯一id查看用户详细信息
function view(id){
	FT.openWinMiddle(detail_info_lang,contextPath + '/manager/resords/resordsAction!view.action?resords.id=' + id,'close');
}


//删除操作
function delData() {
	var selRows = dataGrid.getSelectedRows();

	if(selRows < 1) {
		FT.toAlert('warn', '请选择要删除的上门记录！',null);
	} else {
		$.ligerDialog.confirm('是否确定删除?',syntax_confirm_lang,function(sel) {
          if(sel) {
          	var selLength = selRows.length;
          	var ids='';
          	for(var i=0;i<selLength;i++) {
          		ids = ids.concat(selRows[i]['id'],',');
          	}
          	$.post(cPath + 'manager/resords/resordsAction!delete.action',
          		{delPrjIds: ids.substring(0,ids.length-1)}
          	,function(result){
	            		if(result.errorStr == 'success'){	            			
		            		$.ligerDialog.success(result.object, '提示', function(){
	            				query(true);
	            			});
		            	}else {
		            		FT.toAlert(result.errorStr, result.object, null);
		            	}
	            	},'json'
          	);
          }
		});
	}
}
function logViewRenderer(data,rIndex) {
	var opers="";
	if(permEdit != ''){
		opers = opers + "<a href='javascript:findobj(" + data.id + ");'>" + permEdit + "</a>";
	}

	return opers;
}
//编辑
function findobj(id){
   var curPage = dataGrid.getCurrentPage();
   location.href = contextPath + '/manager/resords/resordsAction!find.action?resords.id='+ id +'&cPage='+curPage;
}
