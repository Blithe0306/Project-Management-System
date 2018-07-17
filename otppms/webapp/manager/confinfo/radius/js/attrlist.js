var contextPath;

$(function(){
	
	contextPath = $("#contextPath").val() + "/";
	var loadPage = 1;
	if($("#curPage").val() != '') {
		loadPage = $("#curPage").val();
	}
	var profileId = $("#profileId").val();
	var url_ = "radAttr!queryAttr.action?profileId="+profileId;
	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        url : url_,
        columns : [
        	{display:arrt_vendor_lang, name:'vendorname',  width:'15%'},
            {display:attr_id_lang, name:'attrId',  width:'12%'},
            {display:attr_name_lang, name:'attrName', width:'23%'},
            {display:attr_valtype_lang, name:'attrValueType', width:'16%'},
            {display:attr_value_lang, name:'attrValueToName', width:'16%'},
            {display:syntax_operation_lang, name:'', render:operRenderer, width:'15%'}
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
		opers = "<a href='javascript:editObj(\"" + data.attrId + "\",\"" + data.profileId + "\",\"" + data.vendorid + "\");'>"+permEdit+"</a>";
	}
	return opers;
}

//添加RADIUS配置属性
function addobj(){
	var profileId = $("#profileId").val();
    location.href = contextPath+"manager/confinfo/radius/addattr.jsp?profileId="+profileId;
}

//编辑RADIUS配置属性
function editObj(attrId,profileId,vendorid){
	var currentPage = dataGrid.getCurrentPage();
	location.href = contextPath+"manager/confinfo/radius/radAttr!find.action?profileId="+profileId+"&attrId="+attrId+"&vendorid="+vendorid;
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
		FT.Dialog.confirm(confirm_del_sel_date_lang, syntax_confirm_lang,function(sel) {            
            if(sel) {
           		var selLength = selRows.length;
	            var ids = "";
	            for(var i=0; i<selLength; i++) {
	            	ids = ids.concat(selRows[i]['attrId'],':',selRows[i]['profileId'],':',selRows[i]['vendorid'],',');
	            }
            	$.post(contextPath + 'manager/confinfo/radius/radAttr!delete.action',{
					delIds:ids.substring(0,ids.length-1)
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

