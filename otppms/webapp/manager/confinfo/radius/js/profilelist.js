var contextPath;
$(function(){
	contextPath = $("#contextPath").val() + "/";
	var loadPage = 1;
	if($("#curPage").val() != '') {
		loadPage = $("#curPage").val();
	}
	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        url : 'radProfile!init.action',
        columns : [
            {display:radius_name_lang, name:'profileName',  width:'30%'},
            {display:syntax_desc_lang, name:'profileDesc', width:'40%'},
            {display:syntax_operation_lang, name:'', render:operRenderer, width:'25%'}
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

// 操作渲染器
function operRenderer(data,rIndex) {
	var opers='';
	if(permEdit !=''&&permEdit!='undefined'&&permEdit!=null){
		opers = "<a href='javascript:editObj(\"" + data.profileId + "\");'>"+permEdit+"</a>&nbsp;&nbsp;&nbsp;&nbsp;";
	}
	if(permSet != '') {
		opers += "<a href='javascript:setAttr(\"" + data.profileId + "\",\"" + data.profileName + "\");'>"+permSet+"</a>";
	}
	return opers;
}

//添加RADIUS配置
function addobj(){
    location.href = contextPath+"manager/confinfo/radius/addprofile.jsp";
}

//编辑RADIUS配置
function editObj(profileId){
	var currentPage = dataGrid.getCurrentPage();
	location.href = contextPath+"manager/confinfo/radius/radProfile!find.action?profileId="+profileId+"&currentPage="+currentPage;
}

//RADIUS配置属性设置
function setAttr(profileId,profileName){
	location.href = contextPath+"manager/confinfo/radius/attrlist.jsp?profileId="+profileId;
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

//删除RADIUS配置
function delData(){
	var selRows = dataGrid.getSelectedRows();
	if(selRows < 1) {
		FT.toAlert('warn',need_del_date_lang, syntax_tip_lang);
	} else {
		FT.Dialog.confirm(del_sel_date_lang,syntax_confirm_lang,function(sel) {            
            if(sel) {
           		var selLength = selRows.length;
	            var ids = "";
	            var names = "";
	            var delid = "";
	            for(var i=0; i<selLength; i++) {
	            	ids = selRows[i]['profileId'];
	            	names = selRows[i]['profileName'];
	            	delid = delid.concat(ids+":"+names,',');
	            }
            	$.post(contextPath + 'manager/confinfo/radius/radProfile!delete.action',{
					delIds:delid.substring(0,delid.length-1)
	            },function(data){
	            		if(data.errorStr == 'success'){	            			
	            			$.ligerDialog.success(data.object, syntax_tip_lang, function(){
	            				query(true);
	            			});
	            		}else {
	            			$.ligerDialog.success(data.object, data.errorStr, function(){
	            				query(true);
	            			});
	            		}
	            	},'json'
            	);
            }
		});
	}
}

