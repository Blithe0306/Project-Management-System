var contextPath;

$(function(){
    contextPath = $("#contextPath").val() + "/";
    
    var page = 1;
    if($('#currentPage').val()!=''){
       page = $('#currentPage').val();
    }
	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        url : 'distManager!init.action',
        columns : [
            {display:tknum_lang,name:'token', width:'15%'},
            {display:username_lang,name:'userName', width:'10%'},
            {display:tkn_code_lang,name:'phoneudid', width:'15%',render:pudidRenderer},
            {display:activation_lang,name:'actived', width:'8%',render:activedRenderer},
            {display:activation_time_lang,name:'activetimeStr', width:'15%'},
            //{display:pwd_out_time_lang,name:'apdeathStr', width:'13%'},
            //{display:dist_type_lang,name:'provtype', width:'12%',render:provRenderer},
            {display:operate_lang,name:'', width:'15%',render:operationRenderer} 
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

function activedRenderer(data,rIndex){
  return data.actived == 1 ? langYes : langNo;
}
 
function provRenderer(data,rIndex){
 if(data.provtype == -1){
    return langUndist;
 }else if(data.provtype == 1){
    return langOndist;
 }else if(data.provtype == 0){
    return langOffdist;
 }else {
    return '';
 }
} 

function pudidRenderer(data,rIndex) {
	var udid = data.phoneudid;
	return udid.replace(/\s(?=\d)/g,'').replace(/(\d{5})(?=\d)/g,"$1 ");
}


function operationRenderer(data,rIndex){
  var opers = "";
  if(permSetCode != ''){
  	opers += "<a href='javascript:findObj(\"" + data.token + "\",0);'>"+ permSetCode +"</a>&nbsp;&nbsp;";
  }
  
  if(permDist != ''){
  	if(data.actived=="1"){
  		opers += "<a href='javascript:ifBeDist(\"" + data.token + "\",\"" + data.provtype + "\",\"" + data.userName + "\",\""+1+"\");'>"+ permDist +"</a>&nbsp;&nbsp;";
  	}else{
  		opers += "<a href='javascript:ifBeDist(\"" + data.token + "\",\"" + data.provtype + "\",\"" + data.userName + "\",\""+0+"\");'>"+ permDist +"</a>&nbsp;&nbsp;";
  	}
  }
  
  if(permReset != ''){
  	opers += "<a href='javascript:findObj(\"" + data.token + "\",1);'>"+ permReset +"</a>&nbsp;&nbsp;";
  }
  
  return opers;
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
	var userName = $("#userName").val();   
	var token =  $("#token").val();   
	var actived =  $("#actived").val();  
	var tknprovtype =  $("#tknprovtype").val();
	var phoneudid = $("#phoneudid").val();   
	var startDate = $('#startDate').val();
	var endDate = $('#endDate').val();
	
    var queryParam = {
    	'page':page,
    	'pagesize':pagesize,
        'distManagerQueryForm.userName':userName,
        'distManagerQueryForm.tokenStr':token,
        'distManagerQueryForm.actived':actived,
        'distManagerQueryForm.provtype':tknprovtype,
 		'distManagerQueryForm.phoneudid':phoneudid,
 		'distManagerQueryForm.startTime':startDate,
 		'distManagerQueryForm.endTime':endDate
    }
    
    return queryParam;
}
 
//操作 0设定标识码 1 重置
function findObj(token,oper){
  var curPage = dataGrid.getCurrentPage();
  //设定标识码
  if(oper==0){
	      location.href = contextPath + "/manager/token/distmanager/distManager!find.action?distManagerInfo.token=" + token + "&curPage=" + curPage;
	  }else if(oper==1){  
	   $.ligerDialog.confirm(mobile_tkn_lang, function (yes){
         if(yes){
             $("#ListForm").ajaxSubmit({
					   async:false,  
					   dataType:"json",
					   type:"POST", 
					   url : contextPath + "/manager/token/distmanager/distManager!modify.action?distManagerInfo.token=" + token + "&oper="+oper,
					   success:function(msg){
							if(msg.errorStr == 'success'){ 
							    $.ligerDialog.success(msg.object,syntax_tip_lang,function(){
							    	query(false);	
							    });
							 }else{
							     FT.toAlert(msg.errorStr,msg.object,null);
							} 
						}
			        });
              }
          });     
	  }
}

//手机令牌分发
function beDist(token, disted, userName){
	$.ajax({ 
		async:true,
		type:"POST",
		url:contextPath + "/manager/token/distmanager/distManager!checkTknExpTime.action",
		data:{'token':token},
		dataType:"json",
		success:function(msg){
            if(msg.errorStr == 'success') {
				var curPage = dataGrid.getCurrentPage();
				location.href = contextPath + '/manager/token/distmanager/distManager!find.action?distManagerInfo.userName='+userName+'&distManagerInfo.token='+token+'&dist=dist';
			}else {
				if(msg.object == 'error') {
					FT.toAlert('warn',tkn_is_expired,null);
				} else {
					FT.toAlert(msg.errorStr,msg.object,null);
				}
			}
		}
	});


}