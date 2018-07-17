var contextPath;

$(function(){
    contextPath = $("#contextPath").val() + "/";
    
	var batchTknSn = $("#batchTknSn").val();
    var page = 1;
    if($('#currentPage').val()!=''){
       page = $('#currentPage').val();
    }
 
	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        url : 'token!init.action?physicaltype=2',
        columns : [
            {display:tknum_lang,name:'token', width:'12%'},
            {display:orgunit_lang,name:'domainOrgunitName', width:'16%'},
            {display:type_lang,name:'tokenType', width:'10%',render:tokenTypeRenderer},
            {display:enable_lang,name:'enable', width:'5%',render:enableRenderer},
            {display:lock_lang,name:'locked', width:'5%',render:lockedRenderer},
            {display:lose_lang,name:'lost', width:'5%',render:lostRenderer},
            {display:invalid_lang,name:'logout', width:'5%',render:logoutRenderer},
            {display:valid_lang,name:'expiretimeStr', width:'12%'},
            {display:operate_lang,name:'', width:'10%',render:operationRenderer} 
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
	$("td :input").keydown(function(e) {
		if(e.keyCode == '13') {
			query(false);
		}
	});
})

function physicaltypeRenderer(data,rIndex){
	if(data.physicaltype==0){
	   return langHardtkn;
	}else if(data.physicaltype==1){
	  return langMtkn;
	}else if(data.physicaltype==2){
	  return langStkn;
	}else if(data.physicaltype==6){
	  return langSMStkn;
	}else {
	  return "";
	}
}
 
//操作
function operationRenderer(data,rIndex){
  var opers = "";
  	 if (data.enabled != 0 && data.locked == 0 && data.lost != 1 && data.logout != 1) {
	      opers+="<a href='javascript:softTknDist(\"" + data.token + "\",\"" + data.distributetime + "\");'>"+tkn_dist_lang+"</a>&nbsp;&nbsp;";
  	 }
     return opers;
}     

//令牌类型
function tokenTypeRenderer(data,rIndex){
	if(data.producttype==0){
   		return "OTP c100";
 	}else if(data.producttype==1){
  		return "OTP c200";
 	}else if(data.producttype==2){
  		return "OTP c300";
 	}else if(data.producttype==3){
  		return "OTP c400";
 	}else if(data.producttype==100){
  		return "OTP c100";
 	}else if(data.producttype==101){
  		return "OTP c200";
 	}else if(data.producttype==102){
  		return "OTP c300";
 	}else if(data.producttype==200){
  		return "OTP c100";
 	}else if(data.producttype==201){
  		return "OTP c200";
 	}else if(data.producttype==202){
  		return "OTP c300";
 	}else if(data.producttype==600){
  		return "OTP c100";
 	}else {
	  	return "";
	}
}
 
// 多语言
var langYes = syntax_yes_lang;
var langNo = syntax_no_lang;

function enableRenderer(data,rIndex){
 return data.enabled == 1 ? langYes : '<span style="color:red">'+langNo+'</span>';
}

function lockedRenderer(data,rIndex){
 if(data.locked == 1 || data.locked == 2){
    return '<span style="color:red">'+langYes+'</span>';
 }else {
    return langNo;
 }
}

function lostRenderer(data,rIndex){
 return data.lost == 1 ? '<span style="color:red">'+langYes+'</span>' : langNo;
}

function logoutRenderer(data,rIndex){
 return data.logout == 1 ? '<span style="color:red">'+langYes+'</span>' : langNo;
}

/**
 * 查询,根据条件让Grid重新载入数据
 * @param {} stayCurrentPage 是否留在当前页面
 */
function query(stayCurrentPage){
	var page = 1;
	if(stayCurrentPage) {
		page = dataGrid.getCurrentPage();
	}
	var pagesize = dataGrid.getCurrentPageSize();
    var queryParam = formParms(page, pagesize);
    
    dataGrid.loadServerData(queryParam);
}

//组装表单查询条件QueryFilter
function formParms(page, pagesize){
    var tokenStr = $.trim($("#tokenStr").val());   
	var tokenStartStr = $.trim($("#tokenStart").val());//令牌起始序列号
	var tokenEndStr = $.trim($("#tokenEnd").val());//令牌结束序列号
    var queryParam = {
    	'page':page,
    	'pagesize':pagesize,
    	'tokenQueryForm.tokenStr':tokenStr,
    	'tokenQueryForm.tokenStart':tokenStartStr,
    	'tokenQueryForm.tokenEnd':tokenEndStr
    }
    
    return queryParam;
}
 

//软件令牌分发
function softTknDist(token,distributetime){

 var curPage = dataGrid.getCurrentPage();
     if(distributetime!=0){
		  $.ligerDialog.confirm(confirm_info_1_lang, function (yes){
     	    if(yes){
     	   	   FT.openWinMiddle(confirm_info_1,contextPath + '/manager/token/token!prevSoftTknDist.action?tokenInfo.token=' + token + '&curPage=' + curPage,true); 
	 		}
 		});
	 }else{
		FT.openWinMiddle(confirm_info_1,contextPath + '/manager/token/token!prevSoftTknDist.action?tokenInfo.token=' + token + '&curPage=' + curPage,true); 
  	 }
	
}
 