var contextPath;
var leftListData;
var rightListData;

$(function(){

	contextPath = $("#contextPath").val();
	contextPath += "/";
    var userId=$("#userId").val();
    var domainId=$("#domainId").val();
    var orgunitId=$("#orgunitId").val();
    var orgunitIds=$("#orgunitIds").val(); //domainid:orgunitid,
    
	var urlStr = "adminBind!tknQuery.action?userId="+userId;
	rightListData = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        url : urlStr,
        columns : [
            {display:tknum_lang,name:'token', width:'18%'},
            {display:orgunit_lang,name:'domainOrgunitName', width:'50%'},
            {display:state_bind_lang,name:'bindTag', width:'12%',render:bindTagRenderer}
        ],
        checkbox:true,
        allowUnSelectRow : true,
        enabledSort:false,
        parms:{operType:'1'},
        page : 1,
        pageSize:20,
        height:'100%',
        onAfterShowData:function() {
        	rightListData.clearParams('operType');
        }
	});
	
	// 回车完成查询
	$("#token").keydown(function(e){
		if(e.keyCode === 13) {
			query(false);
		}
	});
})

function physicalTypeRenderer(data,rIndex){
 if(data.producttype==0){
   return langHardtkn;
 }else if(data.producttype==1){
  return langMtkn;
 }else if(data.producttype==2){
   return langStkn;
 } else if(data.producttype==5){
  return langSMStkn;
 }else {
  return "";
 }
}

function bindTagRenderer(data,rIndex){
 return data.bindTag == 1 ? '<span style="color:red">'+ langBound +'</span>' : langUnbound;
}

function enableRenderer(data,rIndex){
 return data.enable == 1 ? '<span style="color:red">'+ langYes +'</span>' : langNo;
}

function lockedRenderer(data,rIndex){
 return data.locked == 1 ? '<span style="color:red">'+ langYes +'</span>' : langNo;
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
	var page = 1;
	if(stayCurrentPage) {
		page = rightListData.getCurrentPage();
	}
	var pagesize = rightListData.getCurrentPageSize();
    var queryParam = formParms(page, pagesize);
    
    rightListData.loadServerData(queryParam);
}

//组装表单查询条件QueryFilter
function formParms(page, pagesize){
	var userId=$("#userId").val();
    var domainId=$("#domainId").val();
    var orgunitId=$("#orgunitId").val();
	var orgunitIds =  $("#orgunitIds").val();
	var token = $("#tokenStr").val();
	var phyType = $.trim($("#pType").val());
	var bindS = $.trim($("#bindState").val());
	  
    var queryParam = {    	
    	'page':page,
    	'pagesize':pagesize,
    	'domainId':domainId,
    	'orgunitId':orgunitId,
    	'queryForm.dOrgunitId':orgunitIds,
        'queryForm.token':token,
        'queryForm.physicaltype':phyType,
        'queryForm.bindState':bindS
    }
    
    return queryParam;
}

function selectObj(obj){
	if (obj != -1){
	 for (i=0; i<document.getElementById(obj).length; i++)
		document.getElementById(obj).options[i].selected = true;			 
	}
}