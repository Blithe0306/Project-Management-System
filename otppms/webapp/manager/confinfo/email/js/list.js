var cPath;
var isDefault = false;

$(function() {
	cPath = $("#contextPath").val();
	cPath += "/";
    window['dataGrid'] = $("#listDataAJAX").ligerGrid({
        root : 'items',
        record : 'results',
        url : 'email!init.action',
        columns : [
            {display:servname_lang,name:'servname', width:'18%'},
            {display:host_lang,name:'host', width:'16%'},
            {display:port_lang,name:'port', width:'12%'},
            {display:account_lang,name:'account', width:'14%'},
            {display:sender_lang,name:'sender',width:'13%'},
            {display:def_server_lang,name:'isdefault',render:isDefaultRender, width:'14%'},
            {display:syntax_operation_lang,name:'',render:operationRenderer, width:'9%'}
        ],
        parms : [
        	{name:'operType',value:'1'}
        ],
        checkbox:true,
        allowUnSelectRow : true,
        enabledSort:false,
        usePager : false,
        height:'100%',
        onAfterShowData:function() {
        	dataGrid.clearParams('operType');
        }
    });
});



// ====================  Grid 所用Renderer  ===========================
// 默认邮箱状态渲染器
function isDefaultRender(data,rIndex) {
	return data.isdefault == 1 ? '<span style="color:green">'+ langYes +'</span>' : langNo;
}

// 操作列渲染器
function operationRenderer(data, rIndex) { 
    var opers;
    var setStr;
    var permRep = "<img src="+cPath+"/images/icon/email_attach.png width='16' height='16' style='padding-top:3px;' title='"+ langSetdef +"'>";
    var permEdit = "<img src="+cPath+"/images/icon/file_edit.gif width='16' height='16' style='padding-top:3px;' title='"+ langEdit +"'>";
    setStr = permRep;
	if(data.isdefault == 1){
		setStr = '';
		isDefault = true;
	}

	opers = "<a href='javascript:editObj(" + data.id + ");'>" + permEdit + "</a>";
    opers += "&nbsp;&nbsp;<a href='javascript:setObj(" + data.id + "\,\"" + data.isdefault + "\");'>" + setStr + "</a>";	

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
	var queryParam = {
    	'page':page,
    	'pagesize':pagesize
    }
    
    return queryParam;
}

// =========================  Grid支持操作  =============================

// 添加邮箱设置操作
function addMethod() {
	window.location.href = cPath + "manager/confinfo/email/add.jsp";
}

// 删除操作
function delMethod() {
	var selRows = dataGrid.getSelectedRows();
	if(selRows < 1) {
		var warn = FT.toAlert('warn', need_del_date_lang, null);
	} else {
		var selLength = selRows.length;
        for(var i=0; i<selLength; i++) {
           if(selRows[i]['isdefault'] == 1) {
           	  var warn = FT.toAlert('warn', not_allow_del_lang, null);
           	  return;
           }
        }
		$.ligerDialog.confirm(del_sel_date_lang,syntax_confirm_lang,function(sel) {
            if(sel) {
            	var idArray = [],
            		ids = "";
            	while(selRows[0]) {
            		idArray.push(selRows.shift()['id']);
            	}
            	ids = idArray.join(",");
            	
            	$.post(cPath + 'manager/confinfo/email/email!delete.action',{
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
 * 编辑修改邮件服务器,查找邮件服务器信息
 */
function editObj(id){
	var currentPage = dataGrid.getCurrentPage();
	location.href = cPath + "manager/confinfo/email/email!find.action?emailId="+id;
}

/**
 * 设置、取消邮箱默认
 */
function setObj(id, isdefault){
	if(isdefault == 1) {
		var warn = FT.toAlert('warn', one_def_serv_lang, null);
		setTimeout(function(){warn.close()},1500);
	}
	if(isdefault == 0) {
		$.ligerDialog.confirm(set_def_serv_lang,syntax_confirm_lang,function(sel) {
			if(sel) {
				$.post(cPath + 'manager/confinfo/email/email!setDefEmail.action',{
					emailId:id
		            },function(result){
		            	if(result.errorStr != 'success') FT.toAlert(result.errorStr, result.object, syntax_tip_lang);
		            	else {
		            		showAndHiddenMsg(result.object)
		            		query();
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
	var jMSG = $("#msgShow").find('span').empty().append(data).end().fadeIn('slow');
    setTimeout(function(){jMSG.fadeOut('normal')},4500);
}
