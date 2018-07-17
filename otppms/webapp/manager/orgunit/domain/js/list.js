var cPath;
$(function() {
	cPath = $("#contextPath").val();
	cPath += "/";
	var loadPage = 1;
	if($("#curPage").val() != '') {
		loadPage = +$("#curPage").val();
	}

    window['dataGrid'] = $("#listDataAJAX").ligerGrid({
        root : 'items',
        record : 'results',
        url : 'domainInfo!init.action',
        columns : [
            {display:code_lang,name:'domainSn', width:'15%'},
            {display:name_lang,name:'domainName', width:'15%'},
            {display:whether_def_lang,name:'isDefault',width:'10%',render:defaultStatusRender},
            {display:desc_lang,name:'descp', width:'13%'},
            {display:operation_lang,name:'', width:'13%',render:operationRenderer}
            
        ],
        parms : [
        	{name:'operType',value:'1'}
        ],         
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
 
 
 
// 操作列渲染器
function operationRenderer(data,rIndex) {
	var operationListstr='';
    if(permEdit!=''){ //如果有编辑的权限
    	operationListstr=operationListstr+"<a href='javascript:editObj(\"" + data.domainId + "\");'>"+permEdit+"</a>";
    }
    //if(permSet!=''){ //如果有设置默认域的权限
    	//if(data.isDefault==0){ //如果当前域不是默认域
    		//operationListstr=operationListstr+"<a href='javascript:setDefaultDomain(\"" + data.domainId + "\");'>"+permSet+"</a>";
    	//}
    //}
    return operationListstr;
} 
 

 //编辑域
function editObj(domainId){
	location.href=cPath + '/manager/orgunit/domain/domainInfo!find.action?domainInfo.domainId='+domainId;
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
	var domainSn = $("#domainSn").val();
	var domainName = $("#domainName").val();
    var queryParam = {
    	'page':page,
    	'pagesize':pagesize,
        'queryForm.domainSn':domainSn,
         'queryForm.domainName':domainName
    }
    
    return queryParam;
}
  
 // 默认状态 渲染器
function defaultStatusRender(data,rIndex) {
	var defaultDomainId= $("#defaultDomainId").val();	
	return data.domainId == defaultDomainId ? '<span style="color:blue">'+syntax_yes_lang+'</span>' : syntax_no_lang;
}

//添加域
function addDomainInfo(){
    location.href=cPath+'manager/orgunit/domain/add.jsp';
}


// 删除操作
function delData() {
	
	var selRows = dataGrid.getSelectedRows();
	if(selRows < 1) {
		FT.toAlert('warn', del_date_lang,null);
	} else {
		//组装所选的行值 并记录默认域
		var isDefaultTag=0;//不存在默认域
		var selLength = selRows.length;
      	var ids = "";
      	var defaultDomainId= $("#defaultDomainId").val();	
      	for(var i=0;i<selLength;i++) {
      		ids = ids.concat(selRows[i]['domainId'],',');
      		if(selRows[i]['domainId']== defaultDomainId){
      			isDefaultTag=1;
      		}
      	}
      	if(isDefaultTag==1){
      		FT.toAlert('warn', del_default_lang,null);
      	}else{
			FT.Dialog.confirm(del_sel_date_lang, confirm_del_sel_lang, function(r){
				if(r==1){ //
					//删除 所选 //删除前会检查所选行是否有下级组织机构
					$.post(
					 	cPath + 'manager/orgunit/domain/domainInfo!delete.action', //第一个参数 url
					    {delDomainIds:ids.substring(0,ids.length-1)},  //第二个参数 传的参数对 key/value 传的参数名和参数值
					    function(result){
				           if(result.errorStr == 'success'){	            			
					       		$.ligerDialog.success(result.object, syntax_tip_lang, function(){});
					       		query(true);
					       		 //完之后 调用initOrgunitTree.jsp左边测显示
						         var frames=window.parent.window.document.getElementById("leftFunction");
						         frames.contentWindow.deleteNodes(1);
					       }else {
					       		FT.toAlert(result.errorStr, result.object, null);
					       }
					       //query(true);
				        }, //第三个参数 成功调用的函数
				        'json' //第四个参数 是返回值类型
			         );
	      		}
			});   
        }
	}
	


}

//设置当前域为默认域
/*
function setDefaultDomain(domainIdv){
	$.post(cPath + '/manager/orgunit/domain/domainInfo!modifyDefaultDomain.action',
				{"domainInfo.domainId":domainIdv,"domainInfo.isDefault":1},
				function(result){
            	if(result.errorStr != 'success') 
            		FT.toAlert(result.errorStr, result.object, '提示');
            	else {
            		//showAndHiddenMsg(result.object)
            		query();
            	}
            },'json');
}
*/

/**
 * 处理返回信息的显示与动态隐藏
 * @param {} data 信息包装JSON
 */
function showAndHiddenMsg(data) {
 var  jMSG = $("#msgShow").find('span').empty().append(data).end().fadeIn('slow');
      setTimeout(function(){jMSG.fadeOut('normal')},4500);
}
