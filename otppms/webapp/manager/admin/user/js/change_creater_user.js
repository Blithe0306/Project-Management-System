var contextPath;

$(function(){
	contextPath = $("#contextPath").val();
	contextPath += "/";
	var loadPage = 1;
	if($("#currentPage").val() != '') {
		loadPage = $("#currentPage").val();
	}
	var adminid = $('#adminid').val();
	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        url : 'adminUser!designInit.action',
        columns : [ 
        	{display:role_name_lang,name:'roleName', width:'28%'},
            {display:info_creator_lang,name:'createuser', width:'28%'},
            {display:info_descp_lang,name:'descp', width:'38%'}
        ],
        parms:{'operType':1,'adminid':adminid},
        usePager:false,
        checkbox:true,
        allowUnSelectRow : true,
        enabledSort:false,
        page : loadPage,
        pageSize:20,
        height:'99%'
	});
	
	$(".conFocus :input").keydown(function(e) {
		if(e.keyCode == '13') {
			query(false);
		}
	});
})


//查询
function query(currentPage){
	var qParams = formParms();
	dataGrid.loadServerData(qParams);
}

//组装表单查询条件QueryFilter
function formParms(){
	var adminid = $("#adminid").val();
	var qParams = {
		'adminid' : adminid
	}
    return qParams;
}
