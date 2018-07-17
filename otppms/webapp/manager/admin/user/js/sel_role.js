var contextPath;

$(function(){

	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        url : 'adminRole!init.action?sel_role=sel_role',
        columns : [
            {display:role_name_lang,name:'roleName', width:'40%'},
            {display:creator_lang,name:'createuser', width:'30%'}
        ],
        checkbox:true,
        allowUnSelectRow : true,
        enabledSort:false,
        parms:{operType:'1'},
        page : 1,
        pageSize:20,
        height:'100%',
        onAfterShowData:function() {
        	dataGrid.clearParams('operType');
        }
	});
	
	// 回车完成查询
	$("#roleName").keydown(function(e){
		if(e.keyCode === 13) {
			query(false);
		}
	});
})

/**
 * 查询,根据条件让Grid重新载入数据
 * @param {} stayCurrentPage 是否留在当前页面
 */
function query(stayCurrentPage){
	var page = 1;
	if(stayCurrentPage) {
		page = dataGrid.getCurrentPage();
	}
	var pagesize = dataGrid.getCurrentPageSize();
    var queryParam = formParms(page, pagesize);
    
    dataGrid.loadServerData(queryParam);
}

//组装表单查询条件QueryFilter
function formParms(page, pagesize){
	var roleName = $("#roleName").val();   
    var queryParam = {
    	'page':page,
    	'pagesize':pagesize,
        'queryForm.roleName':roleName
    }
    
    return queryParam;
}

function okClick(item,win,index) {
	var sRows = dataGrid.getSelectedRows(),
		sInfo = [];
	if(sRows.length<1){
	   FT.toAlert('warn',sel_role_lang,null);
	   return;
	}
	for(var i=0,sl=sRows.length;i<sl;) {
		var role = {},sRow = sRows[i++];
		role.rid = sRow['roleId'];
		role.rname = sRow['roleName'];
		sInfo.push(role);
	}
	parent.confirmSelects(sInfo);
	if(win) win.close();
}

