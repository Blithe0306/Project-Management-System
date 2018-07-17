var contextPath;

$(function(){
    contextPath = $('#contextPath').val();
	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        url : 'userInfo!initOldToken.action?userInfo.userId='+$('#userId').val()+'&userInfo.domainId='+$('#domainId').val(),
        columns : [
            {display:tknum_lang,name:'token', width:'22%'},
            {display:type_lang,name:'producttype', width:'18%',render:productRenderer},
            {display:physical_type_lang,name:'physicaltype', width:'18%',render:physicaltypeRenderer},
            {display:length_lang,name:'otplen', width:'18%'},
            {display:change_cycle_lang,name:'intervaltime', width:'18%'}
        ],
        checkbox:true,
        allowUnSelectRow : true,
        enabledSort:false,
        parms:{operType:'1'},
        usePager:false,
        page : 1,
        pageSize:20,
        height:'100%',
        onAfterShowData:function() {
        	//dataGrid.clearParams('operType');
        }
	});
})
/**
 * 查询,根据条件让Grid重新载入数据
 * @param {} stayCurrentPage 是否留在当前页面
 */
function query(stayCurrentPage){
	var page = 1;
	//if(stayCurrentPage) {
	//	page = dataGrid.getCurrentPage();
	//}

	 //var pagesize = dataGrid.getCurrentPageSize();
     //var queryParam = formParms(page, 20);
    
        dataGrid.loadData(true);
}

//组装表单查询条件QueryFilter
function formParms(page, pagesize){
	
    var queryParam = {
    	'page':page,
    	'pagesize':pagesize
    }
    
    return queryParam;
}
 
function productRenderer(data,rIndex){
	if(data.producttype==0){
   		return "c100";
 	}else if(data.producttype==1){
  		return "c200";
 	}else if(data.producttype==2){
  		return "c300";
 	}else if(data.producttype==3){
  		return "c400";
 	}else if(data.producttype==100){
  		return "c100";
 	}else if(data.producttype==101){
  		return "c200";
 	}else if(data.producttype==102){
  		return "c300";
 	}else if(data.producttype==200){
  		return "c100";
 	}else if(data.producttype==201){
  		return "c200";
 	}else if(data.producttype==202){
  		return "c300";
 	}else if(data.producttype==600){
  		return "c100";
 	}
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
 
function okClick(item,win,index){
	var userid = $('#userId').val();
	var domainId = $('#domainId').val();
	var tokenIds = '';
	var sRows = dataGrid.getSelectedRows(),
	sInfo = [];
	if(sRows.length<1){
	   FT.toAlert('warn',bound_tkn_lang,null);
	   return;
	}
	
	var confirmStr = user_tkn_lang;
	if(domainId==null||domainId==''||domainId==undefined){
		//管理员令牌无域信息
		confirmStr = admin_tkn_lang;
	}
 
	$.ligerDialog.confirm(confirmStr, function (yes){
		 if(yes){
		     for(var i=0,sl=sRows.length;i<sl;) {
				var token = {},sRow = sRows[i++];
				tokenIds = tokenIds + sRow['token'] +",";	 
			}
			 tokenIds = tokenIds.substring(0,tokenIds.length-1);
			 $("#unbindForm").ajaxSubmit({
				 async:false,  
				 type:"POST", 
				 dataType:"json",
				 data:{tokenIds:tokenIds},
				 url : contextPath + "/manager/user/userinfo/userInfo!unBindUT.action?userInfo.userId="+userid+'&userInfo.domainId='+domainId,
				 success:function(msg){
							if(msg.errorStr == 'success'){ 
							     $.ligerDialog.success(msg.object,syntax_tip_lang,function(){
							       query(true);
							       parent.query(true);
							       if(win) win.close();
							     });
							 }else{
							     FT.toAlert(msg.errorStr,msg.object,null);
							} 
						}
				 });	
		     }
	});	
	  
} 
 

