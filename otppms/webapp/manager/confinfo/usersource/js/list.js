var contextPath;

$(function(){
	contextPath = $("#contextPath").val() + "/";
	var loadPage = 1;
	if($("#curPage").val() != '') {
		loadPage = +$("#curPage").val();
	}
	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        url : 'usConf!init.action',
        columns : [
            {display:name_lang, name:'sourcename',  width:'18%'},
            {display:type_lang, name:'sourcetype',  render:sourcetypeRender, width:'18%'},
            {display:syntax_desc_lang, name:'descp', width:'20%'},
            {display:timing_update_lang, name:'timingState', render:enabledRender, width:'20%'},
            {display:syntax_operation_lang, name:'', render:operRenderer, width:'18%'}
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

// 是否启用定时更新状态渲染器
function enabledRender(data,rIndex) {
	return data.timingState == 1 ? syntax_enable_lang : syntax_disabled_lang;
}
function sourcetypeRender(data,rIndex) {
	if(data.sourcetype == 0){
	  return remote_db_lang;
	}else if(data.sourcetype == 1){
	  return "LDAP";
	}else{
	  return "Domino";
	}
}



// 操作渲染器
function operRenderer(data,rIndex) {
	var manually = "<img src="+contextPath+"images/icon/us_automatic.png width='16' height='16' style='padding-top:3px;' title='"+ langmanually +"'>";
	var automatic = "<img src="+contextPath+"images/icon/us_manually.png width='16' height='16' style='padding-top:3px;' title='"+ langautomatic +"'>";
	var permSet = "<img src="+contextPath+"images/icon/config_1.gif width='16' height='16' style='padding-top:3px;' title='"+ langconfig +"'>";
	if(permSet !='' && permSet!='undefined' && permSet!=null){
		opers = "<a href='javascript:showUSconf(\"" + data.id + "\",\"" + data.sourcetype + "\");'>"+permSet+"</a>&nbsp;&nbsp;&nbsp;&nbsp;";
	}
	if(manually !='' && manually!='undefined' && manually!=null){
		opers += "<a href='javascript:manuallyUpdate(\"" + data.id + "\",\"" + data.sourcename + "\",\"" + data.sourcetype + "\");'>"+ manually +"</a>&nbsp;&nbsp;&nbsp;&nbsp;";
	}
	if(automatic !='' && automatic!='undefined' && automatic!=null){
		opers += "<a href='javascript:autoTimeUpdate(\"" + data.id + "\",\"" + data.sourcename + "\",\"" + data.sourcetype + "\");'>"+ automatic +"</a>";
	}
	return opers;
}

//编辑,配置
function showUSconf(id,sourcetype){
	location.href = contextPath+"manager/confinfo/usersource/usConf!view.action?userSourceInfo.id="+id+"&userSourceInfo.sourcetype="+sourcetype;
}

//手动更新
function manuallyUpdate(usId,usName,usType){
      var ajaxbg = $("#background,#progressBar");//加载等待
        ajaxbg.show();
        setTimeout(function(){
       $.ajax({
		  type: "POST",
		  dataType : "json",
		  url: contextPath+"manager/confinfo/usersource/usConf!manuallyUpdate.action",
		  data: "usId="+usId+"&usType="+usType+"&usName="+usName,
		  success: function(msg){
		        ajaxbg.hide();
		     	if(msg.errorStr == 'success'){
					$.ligerDialog.success(msg.object,syntax_tip_lang, function(){});
				}else{
					FT.toAlert(msg.errorStr,msg.object, null);
				} 
				
		  }
        });
   }, 1);
}
//定时更新配置
function autoTimeUpdate(usId,usName,usType){
    var url = contextPath+"manager/confinfo/usersource/usConf!view.action?userSourceInfo.id="+usId+"&userSourceInfo.sourcetype="+usType+"&flag=1&isEdit=true";
	FT.openWinMiddle(timing_config_lang, url, false);  
}


//查询,根据条件让Grid重新载入数据
function query(){
	var page = 1;
    var pagesize = dataGrid.getCurrentPageSize();
    var queryParam = formParms(page, '');
    
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

function delData(){
	var selRows = dataGrid.getSelectedRows();
	if(selRows < 1){
		FT.toAlert('warn',need_del_date_lang, null);
	} else {
		$.ligerDialog.confirm(confirm_del_sel_date_lang,syntax_confirm_lang,function(yes){
			if(yes){
				var selLength = selRows.length;
            	var ids = "";
            	for(var i=0;i<selLength;i++) {
            		ids = ids.concat(selRows[i]['id'],',');
            	}
            	$.post(contextPath + "manager/confinfo/usersource/usConf!delete.action",{
					delIds:ids.substring(0,ids.length-1)
	            	},function(result){
	            		if(result.errorStr == 'success'){	            			
		            		$.ligerDialog.success(result.object, syntax_tip_lang, function(){
		            		});
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

