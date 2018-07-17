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
        url : 'agentConf!init.action',
        columns : [
            {display:conf_name_lang,name:'confname', width:'15%'},
            {display:conf_agent_type_lang,name:'type',render:typeRender, width:'14%'},
            {display:conf_uname_format_lang,name:'userformat',render:userformatRender, width:'13%'},
            {display:local_protect_lang,name:'localprotect',render:localRender, width:'9%'},
            {display:remote_protect_lang,name:'remoteprotect',render:remoteRender, width:'10%'},
            {display:uac_protect_lang,name:'uacprotect',render:uacRender, width:'7%'},
            {display:unbound_login_lang,name:'unboundlogin',render:unboundRender, width:'20%'},
            {display:syntax_operation_lang, name:'', render:operRenderer, width:'8%'}
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

//登录保护代理配置类型
function typeRender(data,rIndex) {
	return data.type == 1 ? linux_login_lang : win_login_lang;
}
//用户名格式
function userformatRender(data,rIndex) {
	var userformatStr='';
	if(data.userformat == 0){
		userformatStr = 'user@ip';
	}
	if(data.userformat == 1){
		userformatStr = 'user';
	}
	if(data.userformat == 2){
		userformatStr = 'user@domain';
	}
	if(data.userformat == 3){
		userformatStr = 'domain'+'\\user';
	}
	return userformatStr;
}
//本地登录保护渲染器
function localRender(data,rIndex) {
	return getShowInfo(data.localprotect,data.type,1);
}
//远程登录保护渲染器
function remoteRender(data,rIndex) {
	return getShowInfo(data.remoteprotect,data.type,2);
}
//UAC登录保护渲染器
function uacRender(data,rIndex) {
	return getShowInfo(data.uacprotect,data.type,3);
}
//登录保护公共判断
function getShowInfo(dataVal,type,flag) {
	var showInfo='';
	if(type == 0){
		if(dataVal == 0){
			showInfo = langNoprotect;
		}
		if(dataVal == 1){
			showInfo = langProLocal;
		}
		if(dataVal == 2){
			showInfo = langProDomain;
		}
		if(dataVal == 3){
			showInfo = langProLocDomain;
		}
	}else{
		if(flag == 3){
			showInfo = langVdnosup;
		}else{
			showInfo = langHandPro;
		}
	}
	return showInfo;
}

//是否允许未绑定令牌的用户登录
function unboundRender(data,rIndex) {
	return data.unboundlogin == 1 ? langYes : langNo;
}

// 操作渲染器
function operRenderer(data,rIndex) {
	var opers = '';
	if(permEdit != ''){
		opers +="<a href='javascript:findObj(\"" + data.id + "\");'>"+permEdit+"</a>&nbsp;";
	} 
	return opers;
}

//添加认证代理配置
function addAgentConf(){
	location.href = contextPath+"manager/authmgr/agentconf/add.jsp";
}

//查找认证代理配置信息
function findObj(id){
	location.href = contextPath+"manager/authmgr/agentconf/agentConf!find.action?confid="+id;
}

//认证代理配置删除操作
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
	            	$.post(contextPath+"manager/authmgr/agentconf/agentConf!delete.action",{
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
