var contextPath;
var changeWin;
$(function(){
	contextPath = $("#contextPath").val();
	contextPath += "/";
	
	/**
	 * 初始化列表时加载指定页的数据，此处主要应用为从列表页面的非第1页跳到编辑页面，然后返回时需要记住跳转前的页码，
	 * 返回后仍然显示该页数据，故在此需取出页码，加载数据时传入.
	 */
	var loadPage = 1;
	if($("#currentPage").val() != '') {
		loadPage = $("#currentPage").val();
	}
	
	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        url : 'adminRole!init.action',
        columns : [
            {display:role_name_lang,name:'roleName', width:'16%',render:roleIdRender},
            {display:creator_lang,name:'createuser', width:'16%'},
            {display:create_time_lang,name:'createtimeStr', width:'16%'},
            {display:update_time_lang,name:'modifytimeStr', width:'16%'},
            {display:desc_lang,name:'descp', width:'16%'},
            {display:operation_lang,name:'',minWidth:120,render:operationRenderer, width:'16%'}
        ],
        parms:[{name:'operType',value:'1'}],
        checkbox:true,
        allowUnSelectRow : true,
        enabledSort:false,
        page : loadPage,
        pageSize:20,
        height:'100%',
        onAfterShowData:function() {
        	dataGrid.clearParams('operType');
        }
	});
	
	$("td :input").keydown(function(e) {
		if(e.keyCode == '13') {
			query(false);
		}
	});
})

// ====================  Grid 所用Renderer  ==========================

// 操作列渲染器
function operationRenderer(data,rIndex) {
    if(permEdit!=''){
        return "<a href='javascript:findobj(\"" + data.roleId + "\",\"" + data.createuser + "\",\"" + data.rolemark + "\");'>"+permEdit+"</a>";
    }else{          
        return "";
    }
}

//角色名称渲染器
function roleIdRender(data,rIndex) {
	return "<a href='javascript:view(\"" + data.roleId + "\");'>"+ data.roleName + "</a>";
}

function view(roleId) {
	FT.openWinSet(role_perm_lang,contextPath + '/manager/admin/role/view.jsp?roleId='+roleId,'close',true,600,380);
}
 
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
	var startDate = $('#startDate').val();
	var endDate = $('#endDate').val();
	var createUser = $("#createUser").val();
	
    var queryParam = {
    	'page':page,
    	'pagesize':pagesize,
        'queryForm.roleName':roleName,
        'queryForm.startTime':startDate,
        'queryForm.endTime':endDate,
        'queryForm.createUser':createUser
    }
    
    return queryParam;
}

// 删除操作
function delData() {
	var selRows = dataGrid.getSelectedRows();
	if(selRows < 1) {
		FT.toAlert('warn',del_role_lang, syntax_tip_lang);
	} else {
		FT.Dialog.confirm(sure_del_lang,syntax_confirm_lang,function(sel) {
            if(sel) {
            	var selLength = selRows.length;
            	var ids = "";
            	for(var i=0;i<selLength;i++) {
            		ids = ids.concat(selRows[i]['roleId'],',');
            	}
            	$.post(contextPath + '/manager/admin/role/adminRole!delete.action',{
					delRoleIds:ids.substring(0,ids.length-1)
	            	},function(data){
	            		if(data.errorStr == 'success'){	            			
		            		FT.toAlert(data.errorStr, data.object, null);
		            	}else {
		            		FT.toAlert(data.errorStr, data.object, null);
		            	}
		            	query(true);
	            	},'json'
            	);
            }
		});
	}
}

function changeClose(){
	$.ligerDialog.success(creator_succ_lang,syntax_tip_lang,function(){
		changeWin.close();
		query(true);
	});
}

//初始化变更创建人
function changeCreater(){
	var curPage = dataGrid.getCurrentPage();
	var curLoginUser = $('#l_userid').val();
	var curloginuserRole = $('#l_userid_role').val();
	var selRows = dataGrid.getSelectedRows(); 
	if(selRows < 1) {
		$.ligerDialog.alert(change_role_lang, syntax_tip_lang, 'warn');
	}else{
	    var selLength = selRows.length;
        var ids = "";
    	$.ligerDialog.confirm(change_creator_lang,syntax_confirm_lang,function(sel) {
            if(sel) {
               for(var i=0;i<selLength;i++) {
		            if(curloginuserRole!='' && curloginuserRole=='ADMIN'){
			            if(selRows[i]['rolemark']=='ADMIN'){ //超级管理员
			               $.ligerDialog.alert(myself_role_lang, syntax_tip_lang, 'warn');
			               return;
			            }
		            }else if(curloginuserRole==''&& selRows[i]['createuser']!= curLoginUser){
		                $.ligerDialog.alert(myself_role_lang, syntax_tip_lang, 'warn');
			               return;
		            }
		            ids = ids.concat(selRows[i]['roleName'],',');
		        }   
            }else{
                    return;
            }
            changeWin = FT.openWinMiddle(change_creator_lang,contextPath + '/manager/admin/role/adminRole!queryDesignateRole.action?curPage='+curPage+'&&roleNames=' + encodeURI(encodeURI(ids)),true);  
		});    
	}
}

//添加角色
function addRoleInfo(){
    location.href=contextPath +'/manager/admin/role/add.jsp';
}

//编辑
function findobj(roleid,createuser,rolemark){ 
    var curloginuser = $.trim($("#l_userid").val()); 
        createuser = $.trim(createuser);
    if(rolemark != null && rolemark != ''){
	    FT.toAlert('warn',init_role_lang,syntax_tip_lang);
	    return;
    } if(curloginuser =='' || createuser!= curloginuser){
        FT.toAlert('warn',create_myself_lang,syntax_tip_lang);
        return;
    } else{
    var curPage = dataGrid.getCurrentPage();
	    FT.Dialog.confirm(sure_edit_lang,syntax_confirm_lang,function(sel) {
	            if(sel) {
					location.href = contextPath + '/manager/admin/role/adminRole!find.action?roleInfo.roleId='+roleid+'&cPage='+curPage+"&oper=${param.oper}";
	            }
			});
    }
}

/**
 * 处理返回信息的显示与动态隐藏
 * @param {} data 信息包装JSON
 */
function showAndHiddenMsg(data) {
	var jMSG = $("#msgShow").find('span').empty().append(data.object).end().fadeIn('slow');
    setTimeout(function(){jMSG.fadeOut('normal')},4500);
}
