var cPath;

$(function() {
	cPath = $("#contextPath").val() + "/";
	
	var loadPage = 1;
	if($("#currentPage").val() != '') {
		loadPage = +$("#currentPage").val();
	}
	
    window['dataGrid'] = $("#listDataAJAX").ligerGrid({
        root : 'items',
        record : 'results',
        url : 'pronotice!init.action',
        columns : [
            {display:title_text_lang,name:'title', width:'14%', render:noticeIdRenderer},
            {display:level_lang,name:'systype', width:'8%', render:systypeRenderer},
            {display:create_time_lang,name:'createtimeStr', width:'13%'},
            {display:effec_time_lang,name:'expiretimeStr', width:'13%'},
            {display:content_lang,name:'content', width:'30%'},
            {display:created_user_lang,name:'createuser', width:'8%'},
            {display:syntax_operation_lang,name:'', width:'8%', render:operRenderer}
        ],
        checkbox:true,
        rownumbers:false,
        allowUnSelectRow : true,
        rowClickSingleSelect : true,
        enabledSort:false,
        parms:{'operType':1},
        page : loadPage,
        pageSize:20,
        height:'100%',
        onAfterShowData:function() {
        	dataGrid.clearParams('operType');
        }
    });

})

// ====================  Grid 所用Renderer  ===========================
// 通知等级渲染器
function systypeRenderer(data,rIndex) {
	var systypeName;
	if(data.systype == 1){
		systypeName = '<span style="color:green">'+ langGeneral +'</span>';
	}
	if(data.systype == 2){
		systypeName = '<span style="color:red">'+ langUrgent +'</span>';
	}
	if(data.systype == 3){
		systypeName = '<span style="color:maroon">'+ langWarning +'</span>';
	}
	return systypeName;
}

// 查看通知
function noticeIdRenderer(data,rIndex) {
	return "<a href='javascript:view(\"" + data.id + "\");'>" + data.title + "</a>&nbsp;";
}

// 操作列渲染器
function operRenderer(data, rIndex) { 
    var opers;
    var permEdit = "<img src="+cPath+"/images/icon/file_edit.gif width='16' height='16' style='padding-top:3px;' title='"+ langEdit +"'>";
	
	opers = "<a href='javascript:editObj(" + data.id + ");'>" + permEdit + "</a>";
    return opers;
}

//查询,根据条件让Grid重新载入数据
function query(currentPage){
	var page = 1;
	if(currentPage) {
		page = dataGrid.getCurrentPage();
	}
    
	var pagesize = dataGrid.getCurrentPageSize();
    var queryParam = formParms(page, pagesize);
    dataGrid.loadServerData(queryParam);
}

//组装表单查询条件QueryFilter
function formParms(page, pagesize){
	var systype = $("#systype").val();
    var title = $("#title").val();
    var startDate = $("#startNoticeTime").val();
    var endDate = $("#endNoticeTime").val();

    
	var queryParam = {
    	'page':page,
    	'pagesize':pagesize,
    	'noticeQueryForm.title':title,
    	'noticeQueryForm.systype':systype,
    	'noticeQueryForm.startNoticeTime':startDate,
    	'noticeQueryForm.endNoticeTime':endDate
    }
    
    return queryParam;
}

// =========================  Grid支持操作  =============================
// 添加系统通知
function addMethod() {
	window.location.href = cPath + "manager/confinfo/portal/addnotice.jsp";
}

/**
 * 编辑系统通知，查找信息
 */
function editObj(id){
	var currentPage = dataGrid.getCurrentPage();
	location.href = cPath + "manager/confinfo/portal/pronotice!find.action?noticeId="+id;
}

/**
 * 查看通知详细信息
 * @param noticeId 通知ID
 */
function view(noticeId){
    FT.openWinMiddle(detail_info_lang,cPath + 'manager/confinfo/portal/pronotice!view.action?noticeId=' + noticeId,'close');
}

// 删除系统通知
function delMethod() {
	var selRows = dataGrid.getSelectedRows();
	if(selRows < 1) {
		var warn = $.ligerDialog.alert(need_del_date_lang, syntax_tip_lang, 'warn');
	} else {
		$.ligerDialog.confirm(del_sel_date_lang,syntax_confirm_lang,function(sel) {
            if(sel) {
            	var idArray = [],
            		ids = "";
            	while(selRows[0]) {
            		idArray.push(selRows.shift()['id']);
            	}
            	ids = idArray.join(",");
            	$.post(cPath + 'manager/confinfo/portal/pronotice!delete.action',{
					delIds:ids
	            	},function(data){
	            		if(data.errorStr == 'success'){	            			
	            			$.ligerDialog.success(data.object, syntax_tip_lang, function(){
	            				query(true);
	            			});
	            		}else {
	            			FT.toAlert(data.errorStr, data.object, null);
	            		}
	            	},'json'
            	);
            }
		});
	}
}


/**
 * 处理返回信息的显示与动态隐藏
 * @param {} data 信息包装JSON
 */
function showAndHiddenMsg(data) {
	var jMSG = $("#msgShow").find('span').empty().append(data.success).end().fadeIn('slow');
    setTimeout(function(){jMSG.fadeOut('normal')},4500);
}
