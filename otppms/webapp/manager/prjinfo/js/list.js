var contextPath;
var queryData = null;//保存令牌查询条件 避免执行的数据和显示的查询条件不一致

$(function(){
	contextPath = $("#contextPath").val();
	var selValue=$("select option:selected").val();
	var url = "prjinfoAction!init.action";
	if(prjid != null){
		url = url+"?prjinfo.prjid="+prjid;
	}
	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        url : url,
        columns : [
            {display:'定制信息摘要',name:'prjdesc', width:'20%',render:projectRender},
            {display:'定制信息归类',name:'typeStr', width:'10%'},
            {display:'项目名称',name:'prjname', width:'16%'},
            //{display:'信息SVN',name:'svn', width:'10%'},
            {display:'信息BUG',name:'bug', width:'10%'},
            {display:'创建人',name:'operator', width:'12%'},
            {display:'创建时间',name:'createtimeStr', width:'12%'},
            {display:'操作',name:'', width:'15%', render:logViewRenderer} 
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
/**
 * view
 */
function projectRender(data,i) {
	return "<a href='javascript:view(\"" + data.id + "\");'>" + data.prjdesc + "</a>";
}
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

// 查看日志明细
function logViewRenderer(data,rIndex) {
	var opers="";
	if(permEdit != ''){
		opers = opers + "<a href='javascript:findobj(" + data.id + ");'>" + permEdit + "</a>";
	}
	if(customUserAdd != ''){
		//添加监视人
		opers+="&nbsp;&nbsp;&nbsp;";
		opers = opers + "<a href=javascript:addCustomUser('" + encodeURI(encodeURI(data.prjdesc)) + "');>" + customUserAdd + "</a>";
	}
	return opers;
}

// 根据日志id查看日志详细信息
function view(id){
	FT.openWinMiddle(detail_info_lang,contextPath + '/manager/prjinfo/prjinfoAction!view.action?prjinfo.id=' + id,'close');
}
//编辑
function findobj(id){
   var curPage = dataGrid.getCurrentPage();
   location.href = contextPath + '/manager/prjinfo/prjinfoAction!find.action?prjinfo.id='+ id +'&cPage='+curPage;
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

function formParms(page, pagesize){
	
    var qParams = {
    	'page':page,
		'pagesize':pagesize,
		'prjinfo.prjdesc' : queryData.prjdesc,
		'prjinfo.prjname' : queryData.prjname
    }
    return qParams;
}

//设置查询条件的值
function setQueryObjData(){
	queryData = new Object();
	//查询值
	queryData.prjdesc = $.trim($('#prjdesc').val());
	queryData.prjname = $('#prjname').val();
}


//删除操作
function delData() {
	var selRows = dataGrid.getSelectedRows();
	if(selRows < 1) {
		FT.toAlert('warn', '请选择要删除的定制信息！',null);
	} else {
		$.ligerDialog.confirm('确定要删除?',syntax_confirm_lang,function(sel) {
            if(sel) {
            	var selLength = selRows.length;
            	var ids='';
            	for(var i=0;i<selLength;i++) {
            		ids = ids.concat(selRows[i]['id'],',');
            	}
            	$.post(cPath + 'manager/prjinfo/prjinfoAction!delete.action',{
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
