var contextPath;
var tokenTotalRow = 0; 
var queryData = null;//保存令牌查询条件 避免执行的数据和显示的查询条件不一致

$(function(){
    contextPath = $("#contextPath").val() + "/";
    
    var page = 1;

    if($('#currentPage').val()!=''){
       page = $('#currentPage').val();
    }
 
	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        url : 'token!init.action?assignInit=1&tknSource=1',
        columns : [
            {display:tknum_lang,name:'token', width:'10%'},
            {display:orgunit_lang,name:'domainOrgunitName', width:'12%'},
            {display:physical_type_lang,name:'physicaltype', width:'12%',render:physicaltypeRenderer},
            {display:langEnable,name:'enabled', width:'8%',render:enableRenderer},
            {display:langLock,name:'locked', width:'8%',render:lockedRenderer},
            {display:langLose,name:'lost', width:'8%',render:lostRenderer},
            {display:invalid_lang,name:'logout', width:'8%',render:logoutRenderer},
            {display:valid_lang,name:'expiretimeStr', width:'10%'}
        ],
        parms:{operType:'1'},
        checkbox:true,
        allowUnSelectRow : true,
        enabledSort:false,
        page : 1,
        pageSize:20,
        height:'100%',
        onAfterShowData:function() {
        	dataGrid.clearParams('operType');
        }
	});
	
	queryObjData();
	
	// 回车完成查询
	$("td :input").keydown(function(e){
		if(e.keyCode === 13) {
			query(false,true);
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
 
//根据不同的状态值显示不同的操作
function operText(operType,sign){
 if(operType==1){//启用、停用
    if(sign==1){
       return langDisable;
    }else{
       return langEnable;
    }
 }
 if(operType==2){//挂失、解挂
  if(sign==1){
       return langRelieveLose;
    }else{
       return langLose;
    }
 }
if(operType==3){//锁定、解锁
  if(sign==1){
       return langUnlock;
    }{
       return langLock;
    }
 }
if(operType==4){//作废
  if(sign==0){
       return langInvalid;
    }else{
       return langDel;
    }
 }
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


//设置查询条件的值
function queryObjData(){
	queryData = new Object();
	//查询值
	queryData.token = $.trim($("#tokenStr").val());  
    queryData.domaind = $.trim($("#domaind").val());   
	queryData.tokenStart = $.trim($("#tokenStart").val());//令牌起始序列号
    queryData.tokenEnd = $.trim($("#tokenEnd").val());//令牌结束序列号
    queryData.orgunit = $.trim($("#orgunit").val());   
}

//组装表单查询条件QueryFilter
function formParms(page, pagesize){
    var assignInit = "";
	var operType = "";
	// 刷新时特殊处理，判断机构是否为空
	if(queryData.domaind == "" && queryData.orgunit == ""){
		assignInit = "1";
		operType = "1";
	}
    var queryParam = {
    	'page':page,
    	'pagesize':pagesize,
    	'tokenQueryForm.tokenStr':queryData.token,
        'tokenQueryForm.tokenStart':queryData.tokenStart,
        'tokenQueryForm.tokenEnd':queryData.tokenEnd,
        'tokenQueryForm.domaind':queryData.domaind,
        'tokenQueryForm.orgunitid':queryData.orgunit,
        'tokenQueryForm.logoutState':0,
        'assignInit':assignInit,
        'operType':operType
    }
    return queryParam;
}

function query(stayCurrentPage,isGetQuery){
	tokenTotalRow = dataGrid.getCurrentTotal();
	// 赋值查询条件
	if(isGetQuery){
		queryObjData(isGetQuery);
	}
	var page = 1;
	if(stayCurrentPage) {
		page = dataGrid.getCurrentPage();
	}
	var pagesize = dataGrid.getCurrentPageSize();
    var queryParam = formParms(page, pagesize);
    dataGrid.loadServerData(queryParam);
}

 //转入选择组织机构
 function nextSelOrgunit(){
 	var tokenSel =  $("#tokenOperSel").find("option:selected").val();
 	if(tokenSel == 0){
 		var sRows = dataGrid.getSelectedRows(),
	 	sInfo = [];
		var tokenBatchSn = "";
		if(sRows.length<1){
	   		FT.toAlert('warn',sel_tkn_lang,null);
	   		return;
		}
		for(var i=0,sl=sRows.length;i<sl;) {
			var role = {},sRow = sRows[i++];
			role.token = sRow['token'];
			tokenBatchSn = tokenBatchSn + sRow['token'] + ",";
		}
	 	//选择的令牌的个数
	 	$('#assignTknCount').html(sRows.length);
	 	//选择的令牌字符串
	 	$('#assignTkn').html(tokenBatchSn);
	 
	 	var domaind_tkn = $('#domaind').val();
	 	var orgunit_org = $('#orgunitIdsTemp').val();
	 	var domain_org = orgunit_org.split(":")[0];
	 	if(domaind_tkn!=domain_org){
	 		$('#orgunitNamesTemp').val('');
			$('#orgunitIdsTemp').val('');
	 	}	
 	}else{
 		tokenTotalRow = dataGrid.getCurrentTotal();
 		if(tokenTotalRow<1){
	   		FT.toAlert('warn',sel_tkn_lang,null);
	   		return;
		}
		//选择的令牌的个数
	 	$('#assignTknCount').html(tokenTotalRow);
 	}
    stepController(1);
 }
   