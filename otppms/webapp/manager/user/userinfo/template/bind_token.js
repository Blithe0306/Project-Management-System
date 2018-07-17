var contextPath;
var tknTotalRow=0;
var queryData = null;//保存令牌查询条件 避免执行的数据和显示的查询条件不一致

$(function(){
    contextPath = $("#contextPath").val();
    var page = 1;
    var dOrgunitId = $.trim($("#orgunitIds_").val());
  	var urlStr = "userBind!tknQuery.action"; // 用户绑定令牌列表 管理员所管理域或机构下的 令牌
  	source = $("#bindFrom").val();
  	if(source=='admin'){// 管理员绑定令牌
  		// 管理员只能绑定属于域下边的令牌'
  		urlStr = "adminBind!tknQuery.action";
  	}
	window['dataGrid'] = $("#tokenListAjx").ligerGrid({
		root : 'items',
        record : 'results',
        url : urlStr,
        columns : [
            {display:tknum_lang, name:'token',width:'16%',render:viewRenderer},            
            {display:title_orgunit_lang,name:'domainOrgunitName',width:'16%'},
            {display:physical_type_lang,name:'physicaltype', render:physicaltypeRenderer,width:'15%'},
            {display:enable_lang,name:'enable', render:enableRenderer,width:'10%'},
            {display:lock_lang,name:'locked',render:lockedRenderer,width:'10%'},
            {display:lose_lang,name:'lost',render:lostRenderer,width:'10%'},
            {display:comm_bind_lang,name:'bindTag', render:bindTagRenderer,width:'10%'} 
        ],
        checkbox:true,
        allowUnSelectRow : true,
        enabledSort:false,
        parms:{operType:'1',dOrgunitId:dOrgunitId},
        page : page,
        pageSize:20,
        height:'100%',
        onAfterShowData:function() {
        	dataGrid.clearParams('operType');
        }
	});
	
	// 赋值查询条件
	setQueryObjData();
	
	// 回车完成查询
	$("td :input").keydown(function(e){
		if(e.keyCode === 13) {
			query(true,true);
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
function query(stayCurrentPage,isGetQuery){
    tknTotalRow = dataGrid.getCurrentTotal();
	// 赋值查询条件
	if(isGetQuery){
		setQueryObjData(isGetQuery);
	}
	var page = 1;
	if(stayCurrentPage) {
		page = dataGrid.getCurrentPage();
	}
	var pagesize = dataGrid.getCurrentPageSize();
    var queryParam = formParms(page, pagesize);
    
	dataGrid.loadServerData(queryParam);
}

//设置查询条件的值
function setQueryObjData(){
	queryData = new Object();
	//查询值
	queryData.dOrgunitId = $("#orgunitIds_").val();   
	queryData.tokenStart = $.trim($("#tokenStart").val());
	queryData.tokenEnd = $.trim($("#tokenEnd").val());
	queryData.token = $.trim($("#token").val());   
	queryData.producttype = $("#producttype").val();
	queryData.physicaltype = $("#pType").val();
	queryData.bindState = $("#bindState").val();
	queryData.domainId = $("#domainId").val(); 
}

//组装表单查询条件QueryFilter
function formParms(page, pagesize){
    var queryParamTn = {
    	'page':page,
    	'pagesize':pagesize,
    	'queryForm.dOrgunitId':queryData.dOrgunitId,
        'queryForm.physicaltype':queryData.physicaltype,
        'queryForm.token':queryData.token,
        'queryForm.tokenStart':queryData.tokenStart,
        'queryForm.tokenEnd':queryData.tokenEnd,
        'queryForm.producttype':queryData.producttype,
        'queryForm.bindState':queryData.bindState,
        'queryForm.domainId':queryData.domainId,
        'queryForm.userId':queryData.userId,
        'queryForm.realName':queryData.realName,
        'queryForm.usbindState':queryData.usbindState
    }
    return queryParamTn;
}

//跳转到令牌绑定选项
function stepBindTnk(orgunitId,CORE_MAX_BIND_USERS,CORE_MAX_BIND_TOKENS,flag){
    var tknlen = 0;
    var tokenArr='';
    var tknSel =  $("#tknOperSel").find("option:selected").val();
	if(tknSel == 0){		
		var sRows = dataGrid.getSelectedRows();
		for(var i=0,sl=sRows.length;i<sl;) {
	       sRow = sRows[i++];
	       // Start 2013-5-4
	       if(sRow['orgunitid'] != 0 && orgunitId!=null){
		       if(sRow['orgunitid'] != orgunitId){
		    	   FT.toAlert('warn',orgunit_err_lang, null);
		    	   return ;
		       }
	       }
	       // End 2013-5-4
           tokenArr  = tokenArr + sRow['token'] +",";
		}
		tknlen = sRows.length; 
	}else if(tknSel == 1){
		tknTotalRow = dataGrid.getCurrentTotal();
		tknlen = tknTotalRow;
	}
	$('#tokenTotal').val(tknlen);
	$('#tokenArr').val(tokenArr);
	
	var userTotal = $("#userTotal").val(); // 选择的用户数   
	// CORE_MAX_BIND_USERS 每令牌最大可绑定用户数量
	// 多个用户与同一令牌绑定
	if(tknlen == "1" && userTotal > CORE_MAX_BIND_USERS){
		FT.toAlert('warn',count_err_lang, null);
		return ;
	}
	// 一个用户与多个令牌
	// CORE_MAX_BIND_TOKENS每用户最大可绑定令牌数量
	if(userTotal == "1" && tknlen > CORE_MAX_BIND_TOKENS){
		FT.toAlert('warn',bind_count_lang, null);
		return ;
	}
	
	if(tknlen==0){
	   FT.toAlert('warn',select_err_lang, null);
	   return ;
	}else{
	   bindIni();
	   if(flag == 1){
	   	 stepController(2);
	   }else{
	     bindUTTips();
	   }
	}
} 
 
 