var contextPath;

$(function() {
	contextPath = $("#contextPath").val() + "/";
	var loadPage = 1;
	if($("#curPage").val() != '') {
		loadPage = +$("#curPage").val();
	}
    window['dataGrid'] = $("#listDataAJAX").ligerGrid({
        root : 'items',
        record : 'results',
        url : 'backendAuth!init.action',
        columns : [
            {display:bk_name_lang,name:'backendname', render:backendNRender, width:'20%'},
            {display:bk_host_lang,name:'host', width:'16%'},
            {display:bk_sparehost_lang,name:'sparehost', width:'18%'},
            {display:bk_type_lang,name:'backendtype', render:backendTypeRender, width:'15%'},
            {display:bk_port_lang,name:'port', width:'8%'},
            {display:langEnable,name:'enabled', render:enabledRender, width:'8%'},
            {display:syntax_operation_lang, name:'',render:operRenderer, width:'10%'}
        ],
        parms:{operType:'1'},
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
})

// ====================  Grid 所用Renderer  ===========================

// 操作渲染器
function operRenderer(data,rIndex) {
	var opers = '';
	
	// 编辑
	if(permEdit != ''){
	     opers += "<a href='javascript:findObj(\"" + data.id + "\");'>"+permEdit+"</a>&nbsp;";
	}
	
	// 启用/禁用
	if(permEnable!=''){
    	if(data.enabled == 1){
			permEnable = permEnable.replace('error_go.png','error_delete.png'),
			permEnable = permEnable.replace(langEnable, langDisabled);
		}else{
			//permEnable 图片默认就是启用的
			permEnable = permEnable.replace('error_delete.png','error_go.png'),
			permEnable = permEnable.replace(langDisabled, langEnable);
		}
		opers += "<a href='javascript:editObj(\"" + data.id + "\",\"" + data.enabled + "\",\"" + data.backendname + "\")'>" + permEnable + "</a>&nbsp;&nbsp;";
    }
	return opers;
}


//后端认证类型
function backendTypeRender(data,rIndex) {
	return data.backendtype == 1 ? 'AD' : 'Radius';
}
//优先级
function priorityRender(data,rIndex) {
	return data.priority == 0? '' : ''+data.priority+'';
}
//是否启用后端认证
function enabledRender(data,rIndex) {
	return data.enabled == 1 ? langYes : langNo;
}

//查看后端认证信息
function backendNRender(data,index) {
	return "<a href='javascript:view(\""+data.id+"\");'>"+data.backendname+"</a>";
}

//编辑后端认证
function findObj(id){
	location.href = contextPath+"manager/authmgr/backend/backendAuth!find.action?backendid="+id;
}

//添加后端认证配置
function addBackendAuth(){
	location.href = contextPath+"manager/authmgr/backend/add.jsp";
}

//查看后端认证详细信息
function view(id){
	FT.openWinMiddle(bk_info_lang,contextPath + 'manager/authmgr/backend/backendAuth!view.action?backendInfo.id=' + id,'close');
}

//后端认证删除操作
function delData(){
	var ajaxbg = $("#background,#progressBar");//加载等待
	var selRows = dataGrid.getSelectedRows();
		if(selRows < 1){
			FT.toAlert('warn',need_del_date_lang, null);
		} else {
			$.ligerDialog.confirm(del_sel_date_lang,syntax_confirm_lang,function(yes){
				if(yes){
					ajaxbg.show();
					var selLength = selRows.length;
	            	var ids = "";
	            	for(var i=0;i<selLength;i++) {
	            		ids = ids.concat(selRows[i]['id'],',');
	            	}
	            	$.post(contextPath+"manager/authmgr/backend/backendAuth!delete.action",{
						delIds:ids.substring(0,ids.length-1)
		            	},function(result){
		            		ajaxbg.hide();
		            		if(result.errorStr == 'success'){	            			
			            		$.ligerDialog.success(result.object, syntax_tip_lang, null);
			            	}else {
			            		FT.toAlert(result.errorStr, result.object, null);
			            	}
			            	query(true);
		            	},'json'
	            	);
				}
			});			
		}
}

//查询,根据条件让Grid重新载入数据
function query(currentPage){
    var page = 1;
	if(currentPage) {
		page = dataGrid.getCurrentPage();
	} 
    var pagesize = dataGrid.getCurrentPageSize();
    
	var qParams = formParms(page, pagesize);
	dataGrid.loadServerData(qParams);
}
	

//组装表单查询条件QueryFilter
function formParms(page, pagesize){
	var queryParam = {
		'page':page,
		'pagesize':pagesize
    }
    
    return queryParam;
}

/**
 * 处理返回信息的显示与动态隐藏
 * @param {} data 信息包装JSON
 */
function showAndHiddenMsg(data) {
	var jMSG = $("#msgShow").find('span').empty().append(data).end().fadeIn('slow');
    setTimeout(function(){jMSG.fadeOut('normal')},4500);
}

//启用、禁用
function editObj(id,data,backendname){
	var ajaxbg = $("#background,#progressBar");//加载等待
	var message;
	var curPage = dataGrid.getCurrentPage();
	if(data == 1){
		message = whether_enable_lang;
	}else if(data == 0){
		message = whether_disabled_lang;
	}
	FT.Dialog.confirm(message,syntax_confirm_lang,function(sel){
		if(sel) {
		   ajaxbg.show();
	       $("#ListForm").ajaxSubmit({
				async:true,    
				dataType : "json",  
				type:"POST", 
				url : contextPath+"/manager/authmgr/backend/backendAuth!enableAuth.action?backendInfo.id="+id+"&backendInfo.enabled="+data+"&backendInfo.backendname="+backendname+"&currentPage="+curPage,
				success:function(msg){
				   ajaxbg.hide();
			       if(msg.errorStr == 'success'){
			          $.ligerDialog.success(msg.object, syntax_tip_lang, function(){
            			 query(true);
            		  });
			       }else{
				      FT.toAlert(msg.errorStr,msg.object,null);
			       }
				}
			});
		}
	});	
}
