var contextPath;
var queryData = null;//保存令牌查询条件 避免执行的数据和显示的查询条件不一致

$(function(){
	
	contextPath = $("#contextPath").val();
	var selValue=$("select option:selected").val();
	var url = "projectAction!init.action";
	if(custid != null){
		url = url +"?projectQueryForm.custid="+custid+"&operType="+operType;
	}
	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        url : url,
        columns : [
            {display:'项目名称',name:'prjname', width:'14%', render:projectRender},
            {display:'项目编号',name:'prjid', width:'10%'},
            {display:'客户名称',name:'custname', width:'10%'},
            {display:'定制类型',name:'typeStr', width:'10%'},
            {display:'基础版本',name:'typeversion', width:'10%'},
            {display:'项目状态',name:'prjstate', width:'10%', render:stateRenderer},
            {display:'需求部门',name:'needdept', width:'10%'},
            {display:'创建时间',name:'createtimeStr', width:'12%'},
            {display:'操作',name:'', width:'10%', render:operateRenderer} 
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
	setQueryObjData();
	$("td :input").keydown(function(e) {
		if(e.keyCode == '13') {
			query(false,true);
		}
	});
})

// ================================  Renderers  =======================================

//删除 <br> 标签信息
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

function stateRenderer(data,rIndex){
	if(data.prjstate == 0){
		return '新立项';
	} else if(data.prjstate == 1){
		return '需求';
	} else if(data.prjstate == 2){
		return '设计';
	} else if(data.prjstate == 3){
		return '开发';
	} else if(data.prjstate == 4){
		return '测试';
	} else if(data.prjstate == 5){
		return '完成';
	} else if(data.prjstate == 6){
		return '反馈';
	} 
}

/**
 * 客户渲染函数
 */
function projectRender(data,i) {
	return "<a href='javascript:view(\"" + data.id + "\");'>" + data.prjname + "</a>";
}

//根据客户唯一id查看用户详细信息
function view(id){
	FT.openWinMiddle(detail_info_lang,contextPath + '/manager/project/projectAction!view.action?project.id=' + id,'close');
}
//编辑
function findobj(id){
   var curPage = dataGrid.getCurrentPage();
   location.href = contextPath + '/manager/project/projectAction!find.action?project.id='+ id +'&cPage='+curPage;
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

	dataGrid.loadServerData(qParams);
}

// 查看日志明细
function operateRenderer(data,rIndex) {
	var opers='';
	if(permEdit != ''){
		opers = opers + "<a href='javascript:findobj(" + data.id + ");'>" + permEdit + "</a>";
		
	}
	if(customUserAdd != ''){
		//添加监视人
		opers+="&nbsp;&nbsp;&nbsp;";
		opers = opers + '<a href=javascript:addCustomUser("'+encodeURI(encodeURI(data.prjname))+'");>' + customUserAdd + '</a>';
	}
	if(projSummay != ''){
		//项目总结
		opers+="&nbsp;&nbsp;&nbsp;";
		opers = opers + "<a href=javascript:addProjSummary('" + data.id + "');>" + projSummay + "</a>";
	}
	if(permGoPrjinfo != ''){
		// go定制项目信息
		opers+="&nbsp;&nbsp;&nbsp;";
		opers = opers + "<a href='javascript:goPrjinfo(" + data.id + ");'>" + permGoPrjinfo + "</a>";
	}
	if(permGoPrjinfoAdd != ''){
		// 添加定制项目信息
		opers+="&nbsp;&nbsp;&nbsp;";
		opers = opers + "<a href='javascript:goPrjinfoAdd(" + data.id + ");'>" + permGoPrjinfoAdd + "</a>";
	}
	
	
	return opers;
}

function formParms(page, pagesize){
	
    var qParams = {
    	'page':page,
		'pagesize':pagesize,
		'projectQueryForm.prjid' : queryData.prjid,
		'projectQueryForm.prjname' : queryData.prjname,
		'projectQueryForm.startTime' : queryData.startTime,
		'projectQueryForm.endTime' : queryData.endTime
    }
    return qParams;
}

//设置查询条件的值
function setQueryObjData(){
	queryData = new Object();
	//查询值
	queryData.prjid = $.trim($('#prjid').val());
	queryData.prjname = $('#prjname').val();
	queryData.endTime = $('#endTime').val();
	queryData.startTime = $('#startTime').val();
}


//删除操作
function delData() {
	var selRows = dataGrid.getSelectedRows();
	if(selRows < 1) {
		FT.toAlert('warn', '请选择要删除的定制项目！',null);
	} else {
		$.ligerDialog.confirm('所选定制项目可能存在定制信息,确定要删除?',syntax_confirm_lang,function(sel) {
            if(sel) {
            	var selLength = selRows.length;
            	var ids='';
            	for(var i=0;i<selLength;i++) {
            		ids = ids.concat(selRows[i]['id'],',');
            	}
            	$.post(cPath + 'manager/project/projectAction!delete.action',{
            		delPrjIds: ids.substring(0,ids.length-1)
	            	},function(result){
	            		if(result.errorStr == 'success'){	            			
		            		FT.toAlert(result.errorStr, result.object, null);
		            	}else {
		            		FT.toAlert(result.errorStr, result.object, null);
		            	}
		            	query(false, true);
	            	},'json'
            	);
            }
		});
	}
}
