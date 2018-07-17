var contextPath;

$(function(){
	contextPath = $("#contextPath").val();

	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        url : 'adminUser!init.action',
        columns : [
            {display:account_lang,name:'adminid', width:'40%'},
            {display:realname_lang,name:'realname', width:'50%'}
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
	
	$("#adminId").keydown(function(e){
		if(e.keyCode === 13) {
			return false;
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
	var adminId = $("#adminId").val();   
    var queryParam = {
    	'page':page,
    	'pagesize':pagesize,
        'queryForm.adminid':adminId
    }
    
    return queryParam;
}

//点击确定按钮触发的事件
function okClick(item,win,index) {
	var sRows = dataGrid.getSelectedRows(),
		sInfo = [];
	if(sRows.length<1){
	   FT.toAlert('warn',please_sel_lang,null);
	   return;
	}
	for(var i=0,sl=sRows.length;i<sl;) {
		var adminUser = {},sRow = sRows[i++];
		if(sRow['enabled'] == 0){
			FT.toAlert('warn',admin_info_lang+sRow['adminid']+enabled_lang,null);
	   		return;	
		}
		adminUser.adminId = sRow['adminid'];
		sInfo.push(adminUser);
	}
	parent.confirmSelects(sInfo);
	if(win) win.close();
}

