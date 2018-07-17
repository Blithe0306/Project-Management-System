var contextPath;
var tknTotalRow=0;

$(function(){
    contextPath = $("#contextPath").val();
    var page = 1;
    var userId = $("#userId").val();
  	var urlStr = "adminBind!tknQuery.action?userId="+userId;
  	
	window['dataGrid'] = $("#tokenListAjx").ligerGrid({
		root : 'items',
        record : 'results',
        url : urlStr,
        columns : [
            {display:tknum_lang, name:'token',width:'16%',render:viewRenderer},        
            {display:orgunit_lang,name:'domainOrgunitName',width:'15%'},
            {display:physical_type_lang,name:'physicaltype', render:physicaltypeRenderer,width:'10%'},
            {display:enable_lang,name:'enable', render:enableRenderer,width:'8%'},
            {display:lock_lang,name:'locked',render:lockedRenderer,width:'8%'},
            {display:lose_lang,name:'lost',render:lostRenderer,width:'8%'},
            {display:binding_lang,name:'bindTag', render:bindTagRenderer,width:'10%'} 
            
        ],
        checkbox:true,
        allowUnSelectRow : true,
        enabledSort:false,
        parms:{operType:'1'},
        page : page,
        pageSize:20,
        height:'100%',
        onAfterShowData:function() {
        	dataGrid.clearParams('operType');
        }
	});
	
	// 回车完成查询
	$("#token").keydown(function(e){
		if(e.keyCode === 13) {
			 query(false);
		}
	});
	 
})

//查看详细信息
function viewRenderer(data,rIndex){
	return "<a href='javascript:view(\"" + data.token + "\");'>"+data.token+"</a>";
} 

//根据token查看令牌详细信息
function view(token){
	FT.openWinMiddle(detail_info_lang,contextPath + '/manager/token/token!view.action?tokenInfo.token=' + token,"close");
}

function physicaltypeRenderer(data,rIndex){
 if(data.physicaltype==0){
   return langHardtkn;
 }else if(data.physicaltype==1){
  return langMtkn;
 }else if(data.physicaltype==2){
  return langStkn;
 }else if(data.physicaltype==6){
  return langSMStkn;
 }  
}
 
 
function bindTagRenderer(data,rIndex){
 return data.bindTag == 1 ? '<span style="color:red">'+ langBound +'</span>' : langUnbound;
}

function productRenderer(data,rIndex){
 var productType = ['c100','c200','c300','c400'];
	 return productType[data.producttype] || '';
}

function enableRenderer(data,rIndex){
 return data.enabled == 1 ? langYes : '<span style="color:red">'+ langNo +'</span>';
}

function lockedRenderer(data,rIndex){
 return data.locked != 0 ? '<span style="color:red">'+ langYes +'</span>' : langNo;
}

function lostRenderer(data,rIndex){
 return data.lost == 1 ? '<span style="color:red">'+ langYes +'</span>' : langNo;
}

function logoutRenderer(data,rIndex){
 return data.logout == 1 ? '<span style="color:red">'+ langYes +'</span>' : langNo;
}
/**
 * 查询,根据条件让Grid重新载入数据
 * @param {} stayCurrentPage 是否留在当前页面
 */
function query(stayCurrentPage){
    tknTotalRow = dataGrid.getCurrentTotal();
    
	var page = 1;
	if(stayCurrentPage) {
		page = dataGrid.getCurrentPage();
	}
	var pagesize = dataGrid.getCurrentPageSize();
    var queryParamTnk = formParms(page, pagesize);
    
	dataGrid.loadServerData(queryParamTnk);
}

//组装表单查询条件QueryFilter
function formParms(page, pagesize){
	var dOrgunitId = $.trim($("#orgunitIds").val());
	var tokenStr = $.trim($("#token").val());
	var pType = $.trim($("#producttype").val());
	var phyType = $.trim($("#pType").val());
	var bindS = $.trim($("#bindState").val());
	var tokenStart = $.trim($("#tokenStart").val());
	var tokenEnd = $.trim($("#tokenEnd").val());
	
    var queryParamTn = {
    	'page':page,
    	'pagesize':pagesize,
    	'queryForm.dOrgunitId':dOrgunitId,
        'queryForm.physicaltype':phyType,
        'queryForm.token':tokenStr,
        'queryForm.producttype':pType,
        'queryForm.bindState':bindS,
        'queryForm.tokenStart':tokenStart,
        'queryForm.tokenEnd':tokenEnd
    }
    
    return queryParamTn;
}

//获取选择的令牌
function getSelectTnk(){
    var tknlen = 0;
    var tokenArr='';
    
	var sRows = dataGrid.getSelectedRows();
	if(sRows.length>1){
		FT.toAlert('warn',tkn_bind_lang, null);
	   	return false;
	}
	
	for(var i=0,sl=sRows.length;i<sl;) {
		sRow = sRows[i++];
	    tokenArr  = tokenArr + sRow['token'] +",";
	}
	
    tknlen = sRows.length;
	$('#tokenArr').val(tokenArr);
	
	if(tknlen==0){
	   FT.toAlert('warn',bind_tkn_lang, null);
	   return false;
	}
	
	return true;
} 
 
 