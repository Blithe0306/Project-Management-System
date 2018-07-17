var contextPath;

$(function(){
	contextPath = $("#contextPath").val() + "/";
	var loadPage = 1;
	if($("#curPage").val() != '') {
		loadPage = +$("#curPage").val();
	}
	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        url : 'smsInfo!init.action',
        columns : [
            {display:sms_name_lang, name:'smsname',  width:'25%'},
            {display:syntax_desc_lang, name:'descp', width:'25%'},
            {display:enable_mode_lang, name:'enabled', render:enabledRender, width:'25%'},
            {display:syntax_operation_lang, name:'', render:operRenderer, width:'20%'}
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

// ==================== Grid 所用Renderer ==========================
// 是否启用短信网关渲染器
function enabledRender(data,rIndex) {
	return data.enabled == 1 ? langEnable : langClose;
}

// 操作渲染器
function operRenderer(data,rIndex) {
	var opers;
    var permEdit = "<img src="+contextPath+"images/icon/file_edit.gif width='16' height='16' style='padding-top:3px;' title='"+ langEdit +"'>";
    var permEnable = "<img src="+contextPath+"images/icon/error_go.png width='16' height='16' style='padding-top:3px;' title='"+ langEnable +"'>";
    
    if(data.enabled == 1) 
		permEnable = permEnable.replace('error_go.png','error_delete.png'),
		permEnable = permEnable.replace(langEnable, langDisabled);
	if(data.enabled == 0) 
		permEnable = permEnable.replace('error_delete.png','error_go.png'),
		permEnable = permEnable.replace(langDisabled, langEnable);
    
    opers = "<a href='javascript:findObj(\"" + data.id + "\");'>"+permEdit+"</a>&nbsp;";
    opers += "&nbsp;&nbsp;<a href='javascript:setObj(" + data.id + "\,\"" + (data.enabled == 0 ? 1 : 0) + "\",\"" + data.smsname + "\");'>" + permEnable + "</a>";
    
    return opers;	
}

//编辑
function findObj(id){
	location.href = contextPath+"manager/confinfo/sms/smsInfo!find.action?smsid="+id;
}
//添加短信网关配置
function addobj(){
	location.href = contextPath+"manager/confinfo/sms/add.jsp";
}	
//删除操作
function delData(){
	var ajaxbg = $("#background,#progressBar");//加载等待
	var selRows = dataGrid.getSelectedRows();
		if(selRows < 1){
			FT.toAlert('warn',check_need_del_date_lang, null);
		} else {
			$.ligerDialog.confirm(confirm_del_sel_date_lang,syntax_confirm_lang,function(yes){
				if(yes){
					ajaxbg.show();
					var selLength = selRows.length;
	            	var ids = "";
	            	for(var i=0;i<selLength;i++) {
	            		ids = ids.concat(selRows[i]['id'],',');
	            	}
	            	$.post(contextPath+"manager/confinfo/sms/smsInfo!delete.action",{
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

function setObj(smsid, enabled, smsname){
	var ajaxbg = $("#background,#progressBar");//加载等待
	var message = disabled_smsconf_lang;
   	if(enabled == 1) {
   		message = enable_smsconf_lang;
   	}
   	$.ligerDialog.confirm(message,syntax_confirm_lang,function(sel) {
       if(sel) {
       		ajaxbg.show();
	     	$.post(contextPath+'manager/confinfo/sms/smsInfo!isEnableSms.action',{
				smsid:smsid,
				enabled:enabled,
				smsname:smsname
	       		},function(result){
	       			ajaxbg.hide();
		      		if(result.errorStr == 'success'){	            			
			       		$.ligerDialog.success(result.object, syntax_tip_lang, function(){
			       			query(true);
		       			});
			        }else {
			         	FT.toAlert(result.errorStr, result.object, null);
			        }
	       		},'json'
	   		);
	 	}
	});
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

