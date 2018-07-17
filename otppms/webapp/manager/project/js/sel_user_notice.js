var cPath;
$(function() {
	cPath = $("#contextPath").val();
	cPath += "/";
	
	var loadPage = 1;
	if($("#curPage").val() != '') {
		loadPage = $("#curPage").val();
	}
	//var orderid = $("#orderid").val();
	//var createuser = $("#createuser").val();

    window['dataGrid'] = $("#listDataAJAX").ligerGrid({
        root : 'items',
        record : 'results',
        url : 'adminUser!init.action?rflag=1',
        columns : [
            {display:'用户帐号', name:'adminid', width:'30%'},
            {display:'用户名称', name:'realname', width:'30%'},
            {display:'邮箱帐号', name:'email', width:'32%',render:operateRenderer}
        ],
        parms : [
        	{name:'operType',value:'1'}
        ],
        checkbox:true,
        allowUnSelectRow:true,
        enabledSort:false,
        page:loadPage,
        pageSize:20,
        height:'100%',
        onAfterShowData:function() {
        	dataGrid.clearParams('operType');
        }
    });
    $(".formCss").keydown(function(e) {
		if(e.keyCode == '13') {
			query();
		}
	});
})

function operateRenderer(data,rIndex) {
	if(data.email ==''){
		data.email = ' ';
	}
	return data.email;
}

//点击保存
function okClick(item, win, index) {
	var sRows = dataGrid.getSelectedRows();
    var sInfoStr = '';
	if(sRows.length < 1){
		FT.toAlert('warn','请选择用户数据！',null);
	   	return;
	}

	var selLength = sRows.length;
    var email = "";
    var idStrs ="";
    for(var i=0; i < selLength; i++) {
    	idStrs = idStrs.concat(sRows[i]['adminid'],',');
    	email = email.concat(sRows[i]['email'],',');
    }
   	email = email.substring(0, email.length - 1);
    idStrs = idStrs.substring(0, idStrs.length - 1);
	parent.addObj(idStrs,email);
	if(win) win.close();
}
//组装表单查询条件QueryFilter
function formParms(page, pagesize){
	var orderid = $("#orderid").val();
	var createuser = $("#createuser").val();
    var userid = $("#userid").val();
    var username = $("#username").val();
   
    var queryParam = {
    	'page':page,
    	'pagesize':pagesize,
        'queryForm.userid':userid,
        'queryForm.username':username,
        'queryForm.orderid':orderid,
        'queryForm.ordCreateUsr':createuser
    }
    
    return queryParam;
}
