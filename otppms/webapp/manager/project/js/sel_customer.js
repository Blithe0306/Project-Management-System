var contextPath;

$(function(){

	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        url : 'custInfo!init.action',
        columns : [
            {display:'客户名称',name:'custname', width:'40%'},
            {display:'客户编号',name:'custid', width:'30%'},
            {display:creator_lang,name:'operator', width:'30%'}
        ],
        checkbox:true,
        frozenCheckbox: true,
        allowUnSelectRow : false,
        enabledSort:false,
        parms:{operType:'1'},
        page : 1,
        pageSize:20,
        height:'100%',
        onAfterShowData:function() {
        	dataGrid.clearParams('operType');
        }, 
        onCheckRow: function(checked, rowdata, rowindex) {
            for (var rowid in this.records)
                this.unselect(rowid); 
            this.select(rowindex);
        }
	});
	
	// 回车完成查询
	$("#custname").keydown(function(e){
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
	var custname = $("#custname").val();   
    var queryParam = {
    	'page':page,
    	'pagesize':pagesize,
        'custInfo.custname':custname
    }
    
    return queryParam;
}

function okClick(item,win,index) {
	var sRows = dataGrid.getSelectedRows(),
		sInfo = [];
	if(sRows.length < 1){
	   FT.toAlert('warn','请选择一个客户！',null);
	   return;
	}
	if(sRows.length > 1){
		FT.toAlert('warn','只能选择一个客户！',null);
		return;
	}
	for(var i=0,sl=sRows.length;i<sl;) {
		var role = {},sRow = sRows[i++];
		role.rid = sRow['id'];
		role.rname = sRow['custname'];
		sInfo.push(role);
	}
	parent.confirmSelects(sInfo);
	if(win) win.close();
}

