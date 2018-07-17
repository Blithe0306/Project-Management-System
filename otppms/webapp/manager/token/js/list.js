var contextPath;
var queryData = null;//保存令牌查询条件 避免执行的数据和显示的查询条件不一致
var authWin;
var softWin;
var pinWin;
var syncWin;

$(function(){
    contextPath = $("#contextPath").val() + "/";
	
	/**批量查询 令牌号**/
	var batchTknSn = $("#batchTknSn").val();
    var page = 1;
    if($('#currentPage').val()!=''){
       page = $('#currentPage').val();
    }
    
    // 默认初始化清空查询条件
    var operTypeStr = 1;
    // 首页快速查询可能带过来的条件
    var queryText = $("#queryText").val();
    if(queryText!=""){
    	//不清空查询条件
    	operTypeStr = "";
    	$("#tokenStr").val(queryText);
    }
    // 组织机构传过来的查询条件
    var dOrgunitId = $("#orgunitIds").val();
    if(dOrgunitId != ""){
    	//不清空查询条件
    	operTypeStr = "";
    }
	var orgFlag = $("#orgFlag").val();
	if(orgFlag == "null"){
    	$("#orgFlag").val(1);
    	orgFlag = 1;  
    }
	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        async: true,
        url : 'token!init.action?batchTknSn=' + batchTknSn,
        columns : [
            {display:tknum_lang,name:'token', width:'13%',render:viewRenderer},
            {display:orgunit_lang,name:'domainOrgunitName', width:'14%'},
            {display:physical_type_lang,name:'physicaltype', width:'10%',render:physicaltypeRenderer},
            {display:langEnable,name:'enable', width:'6%',render:enableRenderer},
            {display:update_key_lang,name:'pubkeystate', width:'12%',render:pubkeyStateRenderer},
            {display:bind_lang,name:'bindTag', width:'5%',render:bindRenderer},
            {display:langLock,name:'locked', width:'6%',render:lockedRenderer},
            {display:langLose,name:'lost', width:'5%',render:lostRenderer},
            {display:commUnvalid,name:'logout', width:'5%',render:logoutRenderer},
            {display:operate_lang,name:'', width:'22%',render:operationRenderer}
        ],
        checkbox:true,
        allowUnSelectRow:true,
        enabledSort:false,
        parms:{
        	operType:operTypeStr,
        	'tokenQueryForm.tokenStr':queryText,
        	"tokenQueryForm.orgunitIds":dOrgunitId,
        	"tokenQueryForm.orgFlag":orgFlag
        },
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
			query(false,true);
		}
	});
})

//物理类型
function physicaltypeRenderer(data){
	switch(data.physicaltype){
		case 0:
			return physicalHard;
		case 1:
			return physicalMobile;
		case 2:
			return physicalSoft;
		case 6:
			return physicalSms;
		default:
			return "";
	}
}

//查看详细信息
function viewRenderer(data){
	return "<a href='javascript:view(\"" + data.token + "\");'>"+data.token+"</a>";
} 

//绑定状态
function bindRenderer(data){
	if(data.bindTag == 0){
		return langNo;
	}else {
	    return '<span style="color:red">'+ langYes +'</span>';
	}
}

//激活状态
function pubkeyStateRenderer(data){
	switch(data.pubkeystate){
		case -1:
			return notstate;
		case 0:
			return '<span style="color:red">'+ noactivate +'</span>';
		case 1:
			return notauth;
		case 2:
			return hasActive;
		default:
			return "";
	}
}
 
//操作
function operationRenderer(data){
	var opers = "";
	//不等于作废
	if(data.logout != 1){
	    if(permEnabledY != ''){
	    	//启用或者是停用
	    	if(data.enabled == 1){
	    		permEnabledY = permEnabledY.replace('error_go.png', 'error_delete.png'),
	    		permEnabledY = permEnabledY.replace(langEnable, langDisable);
			}else if(data.enabled == 0){
				//permEnable 图片默认就是启用的
				permEnabledY = permEnabledY.replace('error_delete.png', 'error_go.png'),
				permEnabledY = permEnabledY.replace(langDisable, langEnable);
			}
	    	opers+="<a href='javascript:tknEnabled(\"" + data.token + "\",\"" + data.enabled + "\");'>"+permEnabledY+"</a>&nbsp;&nbsp;";
	    }
		
		if(permLostY != ''){
			//挂失或者解挂
			if(data.lost == 1){
	    		permLostY = permLostY.replace('tag_blue_delete.png','tag_blue_edit.png'),
	    		permLostY = permLostY.replace(langLose,langRelieveLose);
			}else{
				//permEnable 图片默认就是启用的
				permLostY = permLostY.replace('tag_blue_edit.png','tag_blue_delete.png'),
				permLostY = permLostY.replace(langRelieveLose,langLose);
			}
			opers+="<a href='javascript:tknLost(\"" + data.token + "\",\"" + data.lost + "\");'>"+permLostY+"</a>&nbsp;&nbsp;";
		}
		
		if(permLockedY != ''){
			//锁定或者解锁
			if(data.locked == 1||data.locked == 2){
				permLockedY = permLockedY.replace('lock.png','unlock.gif'),
				permLockedY = permLockedY.replace(langLock,langUnlock);
			}else if(data.locked == 0){
				//permEnable 图片默认就是锁定
				permLockedY = permLockedY.replace('unlock.gif','lock.png'),
				permLockedY = permLockedY.replace(langUnlock,langLock);
			}
			opers+="<a href='javascript:tknLocked(\"" + data.token + "\",\"" + data.locked + "\");'>"+permLockedY+"</a>&nbsp;&nbsp;";
		}
		
		// 令牌分配
    	if(permTknAllot != ''){
    		if(data.bindTag == 0){
    			opers+="<a href='javascript:tknAllot(\"" + data.token+ "\",\"" +data.domainid + "\",\"" +data.orgunitid + "\",\"" +data.domainOrgunitName + "\");'>" + permTknAllot+ "</a>&nbsp;&nbsp;";
    		}
    	}
		
		if(permLogout!='' || permDelete!=''){
			//作废或者删除
			opers+="<a href='javascript:tknLogout(\"" + data.token + "\",\"" + data.logout + "\");'>"+operText(4,data.logout,1)+"</a>&nbsp;&nbsp;";
		}
		//状态为启用的且不是短信令牌才有 认证、同步
		if(data.enabled==1 && data.physicaltype != 6 && data.pubkeystate !=0){
			if(permAuth!=''){
				if(data.lost == 0) {
					opers+= "<a href='javascript:tknAuth(\"" + data.token + "\");'>"+permAuth+"</a>&nbsp;&nbsp;";
				}
			}
			if(permSync!=''){
				if(data.lost == 0) {
					opers+= "<a href='javascript:tknSync(\"" + data.token + "\");'>"+permSync+"</a>&nbsp;&nbsp;";
				}
			}
		}
		//未停用并且未作废的令牌都可以设置应急口令
		if(data.enabled!=0 && data.logout!=1){
			if(permPin!=''){
				opers+= "<a href='javascript:setTokenPin(\"" + data.token + "\",\"" + data.authmethod + "\",\"" + data.expiretime + "\",\"" + data.empindeath + "\");'>"+permPin+"</a>&nbsp;&nbsp;";
			}
		}
		
		//获取激活码 一级解锁码 二级解锁码
		if(data.producttype == 2 || data.producttype == 102 || data.producttype == 202){
		    
		    // 获取激活码
		    if(permAC!='' && (data.pubkeystate=='0' || data.pubkeystate=='1')){// 未激活状态或者激活未认证
				opers+= "<a href='javascript:getTokenCode(\"" + data.token + "\",0);'>"+permAC+"</a>&nbsp;&nbsp;";
			}
			
			// 获取一级解锁码
			if(permPUK1!=''&& data.tokenSpec.puk1mode!='0' && data.pubkeystate!='0'){  // 解锁模式为时间或挑战(非0)，激活状态除"未激活"(非0)
				opers+= "<a href='javascript:getTokenCode(\"" + data.token + "\",1,\"" + data.tokenSpec.puk1mode + "\");'>"+permPUK1+"</a>&nbsp;&nbsp;";
			}
			
			// 获取二级解锁码
			if(permPUK2!=''&&data.tokenSpec.puk2mode!='0' && data.pubkeystate!='0'){ // 解锁模式为时间或挑战(非0)，激活状态除"未激活"(非0)
				opers+= "<a href='javascript:getTokenCode(\"" + data.token + "\",2,\"" + data.tokenSpec.puk2mode + "\");'>"+permPUK2+"</a>&nbsp;&nbsp;";
			}
		}
		
		//软件令牌分发
		if(data.physicaltype == 2){
			if(permSDist!=''){
				if(data.lost == 0) {
					opers+= "<a href='javascript:softTknDist(\"" + data.token + "\",\"" + data.distributetime + "\");'>"+ permSDist +"</a>&nbsp;&nbsp;";
				}
			}
		}
		
	}else{
		if(permLogout!=''||permDelete!=''){
			opers+="<a href='javascript:tknLogout(\"" + data.token + "\",\"" + data.logout + "\");'>"+operText(4,data.logout,1)+"</a>&nbsp;&nbsp;";
		}
	}
   
  	return opers;
}     

//获取激活码codeType=0 一级解锁码codeType=1 二级解锁码codeType=2 
function getTokenCode(tokenStr,codeType,pubMode){
	if(codeType == 0){
		var url= contextPath+'/manager/token/authAction!genAC.action';
		$.post(url, {'messageBean.tokenSN':tokenStr},
			function(msg){
				var content = msg.object;
				if(msg.errorStr == 'success'){
					content = code_title_lang+colon_lang+msg.object;
					$.ligerDialog.success(content,syntax_tip_lang,function(){
		    			query(true,false);
		    		});
				}else{
					FT.toAlert(msg.errorStr,content, null);
				}
			}, "json"
		);
	}else if(codeType == 1 || codeType == 2){
		var curPage = dataGrid.getCurrentPage();
		var title = '';
		if(codeType == 1){
			title = puk1_title_lang;
		}else{
			title = puk2_title_lang;
		}
		
		if(pubMode == 1){ // 时间型
			var method = "";
			var content = "";
			var result = ""
			
			if(codeType == '1'){
				method = "genPUK1";
				content = puk1_code_title_lang + colon_lang;
			}else if(codeType == '2'){
				method = "genPUK2";
				content = puk2_code_title_lang + colon_lang;
			}
			var url=contextPath+'/manager/token/authAction!'+method+'.action';
			$.post(url, {'messageBean.tokenSN':tokenStr},
				function(msg){
					result = msg.object;
					if(msg.errorStr == 'success'){
			    		result = content + msg.object;
			    	}
			    	FT.toAlert(msg.errorStr,result, null);
				}, "json"
			);
		}else{ // 挑战型，输入挑战码
			FT.openWinMiddle(title, contextPath + '/manager/token/tknpuk.jsp?token='+tokenStr + '&codeType='+codeType + '&pubMode='+pubMode+'&curPage='+ curPage,true);
		}
	}
}

// 令牌分配
function tknAllot(token,domainId,orgunitId,domainOrgunitName){
	var url=contextPath+'/manager/token/token!checkBindTag.action';
	$.ajax({ 
		async:true,
		type : "POST",
		url:url,
		data:{'tokenSN':token},
		dataType:"json",
		success:function(msg){
			if(msg.errorStr == 'success'){
				openAssign(token,domainId,orgunitId,domainOrgunitName);
	    	}else{
	    		$.ligerDialog.success(tkn_allot_org_bind_lang, syntax_tip_lang, function(){
	            	query(true,false);
	            });
	    	}
		}
	});
}

function openAssign(token,domainId,orgunitId,domainOrgunitName){
	var url = contextPath + "/manager/token/tknassign.jsp?fromTag=1&token="+token+"&domainId="+domainId+"&orgunitId="+orgunitId+"&domainOrgunitName="+domainOrgunitName;
	var topWin = FT.openWinBig(tkn_allot_lang,url);
 	topWin.currPage = this;
	
}

//根据不同的状态值显示不同的操作
//flag 判断直接返回中文还是返回图片
function operText(operType,sign,flag){
	if(operType==1){//启用、停用
		if(sign==1){
	    	return flag==0?langDisable:permEnabledY;
	    }else{
	    	return flag==0?langEnable:permEnabledY;
	    }
	}
	if(operType==2){//挂失、解挂
	    if(sign==1){
	       return flag==0?langRelieveLose:permLostY;
	    }else{
	       return flag==0?langLose:permLostY;
	    }
	}
	if(operType==3){//锁定、解锁
	    if(sign==1){
	    	return flag==0?langUnlock:permLockedY;
	    }{
	       return flag==0?langLock:permLockedY;
	    }
	}
	if(operType==4){//作废、删除
	    if(sign==0){
	       return flag==0 ? commUnvalid : permLogout;
	    }else{
	       return flag==0 ? commDelete : permDelete;
	    }
	}
}

//令牌类型
function tokenTypeRenderer(data){
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

function enableRenderer(data){
	return data.enabled == 1 ? langYes : '<span style="color:red">'+langNo+'</span>';
}

function lockedRenderer(data) {
	if(data.locked == 1){
	   return '<span style="color:red">'+ tempLock +'</span>';
	}else if(data.locked == 2){
	   return '<span style="color:red">'+ longLock +'</span>';
	}else{
	   return noLock;
	}
}


function lostRenderer(data){
 return data.lost == 1 ? '<span style="color:red">'+langYes+'</span>' : langNo;
}

function logoutRenderer(data){
 return data.logout == 1 ? '<span style="color:red">'+langYes+'</span>' : langNo;
}

//设置查询条件的值
function setQueryObjData(){
	queryData = new Object();
	//查询值
	queryData.tokenStr = $.trim($("#tokenStr").val());
	queryData.physicaltype = $("#physicaltype").val();
	queryData.enableStr = $("#enableState").val();
	queryData.bindState =  $("#bindState").val();   
	queryData.lockedStr = $("#lockedState").val();
	queryData.lostStr = $("#lostState").val();
	queryData.logoutStr = $("#logoutState").val();
	queryData.pubkeyState = $("#pubkeyState").val(); 
	queryData.tokenStartStr = $.trim($("#tokenStart").val());//令牌起始序列号
	queryData.tokenEndStr = $.trim($("#tokenEnd").val());//令牌结束序列号
	queryData.orgunitIds = $("#orgunitIds").val();
//	queryData.vendorId = $("#vendorId").val();//厂商名称
	queryData.orgFlag = $("#orgFlag").val();
	queryData.expiretimeType = $("#expiretimeType").val();//过期类型
}

/**
 * 查询,根据条件让Grid重新载入数据 
 * isGetQuery参数表示在显示列表前是否获取查询条件 true 获取，false 不获取
 * @param {} stayCurrentPage 是否留在当前页面
 */
function query(stayCurrentPage,isGetQuery){
	var orgunitIds = $("#orgunitIds").val();
	var orgunitid = "";
	var flag = true;
	if(orgunitIds != ""){
		var orgunit = orgunitIds.substring(0,orgunitIds.length-1);
		for(var i=0; i<orgunit.split(",").length; i++){
			orgunitid = orgunit.split(",")[i].split(":")[1];
			if(orgunitid == 0){
				flag = false;
				break;
			}
		}
	}
	if(!flag){
		$("#orgFlag").val(1);
	}else{
		$("#orgFlag").val(2);
	}
	
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
    
    pageFocus(1);
    dataGrid.loadServerData(queryParam);
}

//组装表单查询条件QueryFilter
function formParms(page, pagesize){
    var queryParam = {
    	'page':page,
    	'pagesize':pagesize,
    	'tokenQueryForm.tokenStr':queryData.tokenStr,
    	'tokenQueryForm.physicaltype':queryData.physicaltype,
        'tokenQueryForm.bindState':queryData.bindState,
        'tokenQueryForm.enableState':queryData.enableStr,
        'tokenQueryForm.lockedState':queryData.lockedStr,
        'tokenQueryForm.lostState':queryData.lostStr,
        'tokenQueryForm.logoutState':queryData.logoutStr,
        'tokenQueryForm.tokenStart':queryData.tokenStartStr,
        'tokenQueryForm.tokenEnd':queryData.tokenEndStr,
        'tokenQueryForm.orgunitIds':queryData.orgunitIds,
        'tokenQueryForm.pubkeyState':queryData.pubkeyState,
        'tokenQueryForm.orgFlag':queryData.orgFlag,
        'tokenQueryForm.expiretimeType':queryData.expiretimeType
    }
    
    return queryParam;
}

 

//根据token查看令牌详细信息
function view(token){
	FT.openWinMiddle(syntax_detail_info_lang,contextPath + '/manager/token/token!view.action?tokenInfo.token=' + token,"close");
}

//设置应急口令
function setTokenPin(token,authmethod,expiretime,empindeath){
     var curPage = dataGrid.getCurrentPage();
     //当前时间
     var curtime = new Number($('#curtime').val());
     var maxtime = new Number(7*24*60*60);
     expiretime = new Number(expiretime);
     empindeath = new Number(empindeath);
     var oper = "";
     //令牌已经过期
     if(expiretime < curtime){
        if(empindeath > (curtime+maxtime)){ //应急口令过期时间超过一周
        	FT.toAlert('warn',warn_info_1_lang, null);
            return;
        }else{ //应急口令过期时间未超过一周时，可以设置应急口令，但是设置的应急口令过期时间不能超过一周
            oper = curtime+maxtime;
        }
     }
    //未过期时应急口令过期时间不能超过配置中配置的最大有效时间
 	pinWin = FT.openWinMiddle(emerg_pwd_lang,contextPath + '/manager/token/token!find.action?tokenInfo.token='+token +'&authmethod='+ authmethod + '&curPage='+ curPage + '&maxtime='+oper,true);
 	 
}

function pinClose(object){
	$.ligerDialog.success(object,syntax_tip_lang,function(){
		pinWin.close();
		query(true,false);
	});
}

function closeWin(object) {
	$.ligerDialog.success(object,syntax_tip_lang,function(){
		authWin.close();
		query(true,false);
	});
}

function closeSyncWin(object) {
	$.ligerDialog.success(object,syntax_tip_lang,function(){
		syncWin.close();
		query(true,false);
	});
}

//令牌认证
function tknAuth(token, tkntype){
     var curPage = dataGrid.getCurrentPage();
	 authWin = FT.openWinMiddle(auth_title_lang, contextPath + '/manager/token/tknauth_view.jsp?token='+token + '&curPage='+ curPage,true);
}
	
//令牌同步
function tknSync(token, tkntype){
     var curPage = dataGrid.getCurrentPage();
	 syncWin = FT.openWinMiddle(synch_title_lang,contextPath + '/manager/token/tknsync_view.jsp?token='+token + '&curPage='+ curPage,true);
}

//软件令牌分发
function softTknDist(token,distributetime){

 var curPage = dataGrid.getCurrentPage();
     if(distributetime!=0){
		  $.ligerDialog.confirm(confirm_info_1_lang, function (yes){
     	    if(yes){
     	   	   softWin = FT.openWinMiddle(dist_soft_lang,contextPath + '/manager/token/token!prevSoftTknDist.action?tokenInfo.token=' + token + '&curPage=' + curPage,true); 
	 		}
 		});
	 }else{
		softWin = FT.openWinMiddle(dist_soft_lang,contextPath + '/manager/token/token!prevSoftTknDist.action?tokenInfo.token=' + token + '&curPage=' + curPage,true); 
  	 }
	
} 

function softClose(msg){
	$.ligerDialog.success(soft_success_lang,syntax_tip_lang,function(){
        parent.window.location.href= contextPath + "/manager/token/token!download.action?fileName="+msg.object;
        softWin.close();
    });
}

//批量操作
function batchOper(){
    var curPage = dataGrid.getCurrentPage();
    var ajaxbg = $("#background,#progressBar");//加载等待
	//操作对象 本页 或 本次查询
	var operObj = $("#operObj").val();
	//操作
	var oper = $("#oper").val();
	if(oper==-1){
		FT.toAlert('warn',operation_state_lang, null);
	    return;
	}
	
	//查询条件
	var tokenStr = queryData.tokenStr;
	var physicaltype = queryData.physicaltype;
	var enableStr = queryData.enableStr;
	var bindStr = queryData.bindState;
	var lockedStr = queryData.lockedStr;
	var lostStr = queryData.lostStr;
	var logoutStr = queryData.logoutStr;
	var pubkeyState = queryData.pubkeyState; 
	var tokenStartStr = queryData.tokenStartStr;//令牌起始序列号
   	var tokenEndStr = queryData.tokenEndStr;//令牌结束序列号
	var orgunitIds = queryData.orgunitIds;
	var orgFlag = queryData.orgFlag;

	//本页选中的记录
	var tokenNum = 0; //选中的个数
	var tokenIds=''; //选中的令牌号
	//执行之后的状态
	if(operObj == 0){
	    //本页选中的记录
	    var sRows = dataGrid.getSelectedRows();
		if(sRows.length<1){
		    FT.toAlert('warn',sel_tkn_lang, null);
		    return;
		}
		tokenNum = sRows.length;
		var info = confirmInfo(tokenNum, oper);
		$.ligerDialog.confirm(info, function (yes){
		     if(yes){
		     	   for(var i=0,sl=sRows.length;i<sl;) {
					   tokenNum = tokenNum + 1;
					   var token = {},sRow = sRows[i++];
				           tokenIds  = tokenIds + sRow['token'] +",";
				           if(oper==9){
				           	 // 如果是取消分配
				             if(sRow['bindTag']==1){
				             	// 如果已绑定则不能取消分配
				             	FT.toAlert('error',warn_info_2_lang, null);
				    			return;
				             }
				           }
				           if(oper == 8){
				           	 // 如果是删除令牌
				           	 if(sRow['logout']!=1){
				             	// 只能删除作废的令牌
				             	FT.toAlert('error',warn_info_3_lang, null);
				    			return;
				             }
				           }else{
				        	   if(sRow['logout']==1){
				        		   FT.toAlert('error',warn_info_8_lang, null);
				        		   return;
				        	   }
				           }
				           
					}
				   tokenIds = tokenIds.substr(0,tokenIds.length-1);
		           var url = contextPath + "/manager/token/token!modifyBatch.action?oper="+oper+"&tokenStr=" + tokenStr + 
		            "&physicaltype=" +physicaltype+
					"&enableStr=" +enableStr+
					"&bindStr=" +  bindStr + 
					"&lockedStr=" +lockedStr +
					"&lostStr=" +  lostStr+
					"&logoutStr=" +logoutStr +
					"&ptotal=" +ptotal +
					"&tokenStart="+tokenStartStr +
					"&tokenEnd="+tokenEndStr +
					"&currentPage="+curPage +
					"&orgunitIds="+orgunitIds +
					"&orgFlag="+orgFlag +
					"&pubkeyState="+pubkeyState;
					ajaxbg.show();
		            $("#ListForm").ajaxSubmit({
						async:true,    
						dataType : "json",  
						type:"POST", 
						url : url,
						data:{tokenIds:tokenIds},
						success:function(msg){
							 ajaxbg.hide();
						     if(msg.errorStr == 'success'){
						     		FT.toAlert(msg.errorStr,msg.object,null);
						         	query(true,false);
						     }else{
							        FT.toAlert(msg.errorStr,msg.object,null);
						     }
						}
		       	 	}); 
			 	} 
		  });
	 }else{
	    // 本次查询的所有记录
		var ptotal= dataGrid.getCurrentTotal();
		if(ptotal< 1){
			 FT.toAlert('warn',warn_info_4_lang, null);
			 return;
		}
		tokenNum = ptotal;
		var info = confirmInfo(tokenNum, oper);
		$.ligerDialog.confirm(info, function (yes){
		     if(yes){
		     	   ajaxbg.show();
		           var url = contextPath + "/manager/token/token!modifyBatch.action?oper="+oper+"&tokenStr=" + tokenStr + 
		            "&physicaltype=" +physicaltype+
					"&enableStr=" +enableStr+
					"&bindStr=" +  bindStr + 
					"&lockedStr=" +lockedStr +
					"&lostStr=" +  lostStr+
					"&logoutStr=" +logoutStr +
					"&ptotal=" +ptotal +
					"&tokenStart="+tokenStartStr +
					"&tokenEnd="+tokenEndStr +
					"&currentPage="+curPage +
					"&orgunitIds="+orgunitIds +
					"&orgFlag="+orgFlag +
					"&pubkeyState="+pubkeyState;
		            $("#ListForm").ajaxSubmit({
						async:true,    
						dataType : "json",  
						type:"POST", 
						url : url,
						data:{tokenIds:tokenIds},
						success:function(msg){
						     ajaxbg.hide();
						     if(msg.errorStr == 'success'){
						     		FT.toAlert(msg.errorStr,msg.object,null);
						         	query(true,false);
						     }else{
							        FT.toAlert(msg.errorStr,msg.object,null);
						     }
						}
		          }); 
			   } 
		  });
	   }
   }
		
function confirmInfo(tokenNum, oper){
		var msg = "";
		var str1 = tkn_sure_lang;
		var str2 = tkn_this_lang;
		var str3 = tkn_a_tkn_lang;
		if(oper == 0){
			msg = str1 + langEnable + str2 + tokenNum + str3;
		}else if(oper == 1){
			msg = str1 + langDisable + str2 + tokenNum + str3;
		}else if(oper == 2){
			msg = str1 + langLock + str2 + tokenNum + str3;
		}else if(oper == 3){
			msg = str1 + langUnlock + str2 + tokenNum + str3;
		}else if(oper == 4){
			msg = str1 + langLose + str2 + tokenNum + str3;
		}else if(oper == 5){
			msg = str1 + langRelieveLose + str2 + tokenNum +  str3;
		}else if(oper == 6){
			msg = str1 + commUnvalid + str2 + tokenNum + warn_info_6_lang;
		}else if(oper == 8){
			msg = str1 + commDelete + str2 + tokenNum + str3;
		}else if(oper == 9){
			msg = str1 + cancel_dist_lang + str2 + tokenNum + str3;
		}
		return msg;
}	 
	
//启用、停用
function tknEnabled(token, sign){
	   	updateTkn(1,token,sign);
}

//挂失、解挂
function tknLost(token, sign){
	   updateTkn(2,token,sign);
}

		 
//锁定、解锁
function tknLocked(token, sign){
	updateTkn(3, token, sign);
}
		 
		 
//作废、删除
function tknLogout(token, sign){
	 updateTkn(4,token,sign);
} 

//令牌操作公用方法
function  updateTkn(operType,token,sign){
	var text = tkn_sure_lang + operText(operType,sign,0) + ttkn_this_tkn_lang;
	var result = lock_succ_lang;
	//Start,修改作废提示
	if(operType == 4){
		if(sign==0){
			text = warn_info_5_lang;
		}
	}
	
	if(operType == 3){
		if(sign == 2 || sign == 1){
			text = unlock_tkn_lang;
			result = unlock_succ_lang;
		}
	}
	
	//End,修改作废提示
	$.ligerDialog.confirm(text, function (yes){
		if(yes){
	    	$.ajax({   
            	url : contextPath +"/manager/token/token!modify.action",   
                data : {token:token,sign:sign,operType:operType},   
                type : "post",
                dataType:"text",   
                success:function(data){},
                complete: function(XMLHttpRequest, textStatus){
                	var mes = XMLHttpRequest.responseText;
                	if(mes=='true'){
                		$.ligerDialog.success(result, syntax_tip_lang, function(){
	            			query(true,false);
	            		});
                	}
           		}
			});  
		}
	});
}

//标签切换 令牌查询与令牌操作切换
function pageFocus(index){
	$("#menu li.tabFocus").removeClass("tabFocus"); //移除已选中的样式
	$("#menu li:eq(" + index + ")").addClass("tabFocus");//增加当前选中项的样式
	$("#content li:eq(" + index + ")").show().siblings().hide();//显示选项卡对应的内容并隐藏未被选中的内容
	if(index == 1){
		$("#listDataAJAX").show();
	}else{
		$("#listDataAJAX").hide();
	}
 }

// 提示组织机构为空，关闭窗口
function closeOrgWin(object) {
	$.ligerDialog.success(object,syntax_tip_lang,function(){
		winOrgClose.close();
	});
}