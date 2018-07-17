var contextPath;
var waitW = null;
var queryData = null;//保存令牌查询条件 避免执行的数据和显示的查询条件不一致
var staticPassWin;

var syncWin;
$(function(){
	contextPath = $("#contextPath").val();
	
	/**
	 * 初始化列表时加载指定页的数据，此处主要应用为从列表页面的非第1页跳到编辑页面，然后返回时需要记住跳转前的页码，
	 * 返回后仍然显示该页数据，故在此需取出页码，加载数据时传入.
	 */
	var loadPage = 1;
	if($("#currentPage").val() != '') {
		loadPage = +$("#currentPage").val();
	}
	
	// 默认初始化清空查询条件
    var operTypeStr = 1;
    // 首页快速查询可能带过来的条件
    var queryText = $("#queryText").val();
    if(queryText!=""){
    	//不清空查询条件
    	operTypeStr = "";
    	$("#userId").val(queryText);
    }
    
    // 是否拼接相关SQL
    var orgFlag = $("#orgFlag").val();
    if(orgFlag == "null"){
    	$("#orgFlag").val(1);
    	orgFlag = 1;  
    }
    // 组织机构传过来的查询条件，格式：domainid:orgunitid,
    var dOrgunitId = $("#orgunitIds").val();
    if(dOrgunitId != "null"){
    	//不清空查询条件
    	operTypeStr = "";
    }

    // 取配置中用户绑定令牌最大数
    checkConfi("user","max_bind_tokens");
	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        async : true,
        url : 'userInfo!init.action',
        columns : [
            {display:account_lang,name:'userId', width:'12%',render:viewUserRenderer},
            {display:orgunitName_lang,name:'DOrgunitName', width:'14%'},
            {display:realname_lang,name:'realName', width:'8%'},
            {display:tokens_lang,name:'tokens', width:'24%',render:viewTokenRenderer},
            {display:locked_lang,name:'locked', width:'8%',render:lockedUserRenderer},
            {display:enable_lang,name:'enabled', width:'8%',render:enabledUserRenderer},
            {display:operation_lang,name:'', minWidth:120, render:operationRenderer, width:'22%'}
        ],
        parms:{
        	operType:operTypeStr,
        	'queryForm.userId':queryText,
        	"queryForm.dOrgunitId":dOrgunitId,
        	"queryForm.orgFlag":orgFlag
		},
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
	
	// 赋值查询条件
	setQueryObjData();
	$("td :input").keydown(function(e) {
		if(e.keyCode == '13') {
			query(false,true);
		}
	});
})

// ====================  Grid 所用Renderer  ==========================

// 操作列渲染器
function operationRenderer(data) {
    var opers = '';
    if(permEdit != ''){
		opers += "<a href='javascript:findobj(\"" + data.userId+ "\",\""+data.domainId + "\");'>" + permEdit + "</a>";
    }
	if(permStaticPass != ''){
    	opers +="<a href='javascript:setStaticPass(\"" + data.userId + "\",\""+data.domainId+"\");'>" + permStaticPass+ "</a>";
    }
    
    //解锁、锁定
    if(permLocked != ''){
    	if(data.locked == 2 || data.locked == 1){
    		permLocked = permLocked.replace('lock.png','unlock.gif'),
    		permLocked = permLocked.replace(lock, unlock);
		}else if(data.locked == 0){
			//permEnable 图片默认就是锁定
			permLocked = permLocked.replace('unlock.gif','lock.png'),
			permLocked = permLocked.replace(unlock, lock);
		}
    	opers+="<a href='javascript:editLost(\"" + data.userId + "\",\"" + data.locked +"\",\""+data.domainId+ "\");'>" + permLocked+ "</a>";
    }
    
    //变更机构渲染
    if(permChangeOrgunit != ''){
    	var obj = data.tokens;
    	var tokenSn = "";
		var tknOrgunitSn = "";
        for(var i=0; i<obj.length; i++){
        	tokenSn = tokenSn + obj[i].token +",";
        	tknOrgunitSn = tknOrgunitSn + obj[i].orgunitId +",";
        }
    	opers+="<a href='javascript:changeOrgunit(\"" + data.userId+ "\",\"" +data.domainId + "\",\"" +data.orgunitId + "\",\"" +tokenSn + "\",\"" +tknOrgunitSn + "\");'>" + permChangeOrgunit+ "</a>";
    }
    
    //禁用启用 用户渲染 
    if(permEnabled != ''){
    	if(data.enabled == 1){
    		permEnabled = permEnabled.replace('error_go.png','error_delete.png'),
			permEnabled = permEnabled.replace(enable, disabled);
		}else if(data.enabled == 0){
			//permEnable 图片默认就是启用的
			permEnabled = permEnabled.replace('error_delete.png','error_go.png'),
			permEnabled = permEnabled.replace(disabled, enable);
		}
    	opers+="<a href='javascript:editEnabled(\"" + data.userId + "\",\"" + data.enabled +"\",\"" +data.domainId+ "\");'>" + permEnabled+ "</a>";
    }
     
   if(data.tokens==null || data.tokens==''){
		if(permBindToken != ''){
			opers+="<a href='javascript:bindToken(\"" + data.userId +"\",\"" +data.domainId+ "\",\"" +data.orgunitId+ "\",\"" +data.DOrgunitName+ "\",1);'>" + permBindToken+ "</a>";//1表示绑定令牌
      	}   
   }else{
		// 绑定令牌未达上限, 显示绑定按钮
	    if(data.tokens.length < $("#bindToneks").val()){
	    	if(permBindToken != ''){
		   		opers+="<a href='javascript:bindToken(\"" + data.userId +"\",\"" +data.domainId+ "\",\"" +data.orgunitId+ "\",\"" +data.DOrgunitName+ "\",1);'>" + permBindToken+ "</a>";//1表示绑定令牌
	    	}
		}  
		if(permUnBindToken != ''){
	    	var tokens = data.tokens;
	        var tokenStr='';
	        if(tokens.length == 1){
	        	tokenStr=tokens[0].token;
	        }
	        opers+="<a href='javascript:unbindToken(\"" + data.userId + "\",\""+data.domainId+ "\",\"" + tokens.length + "\",\"" + tokenStr + "\");'>" + permUnBindToken+ "</a>";
		}  
		if(permChangeToken!=''){
	    	opers+="<a href='javascript:bindToken(\"" + data.userId +"\",\"" +data.domainId+ "\",\"" +data.orgunitId+ "\",\"" +data.DOrgunitName+ "\",2);'>" + permChangeToken+ "</a>";
		} 
	 
		//同步
		if(permSync != ''){
			var tokens = getTokensParam(data);
			opers+="<a href='javascript:tknSync(\"" + data.userId + "\",\"" + tokens + "\");'>" + permSync+ "</a>";
		}
	}

	return opers;
}

// 根据data获取令牌号参数
function getTokensParam(data){
	var tokens = '';
	var obj = data.tokens;
	
    for(var i=0;i<obj.length;i++){
    	tokens = tokens + obj[i].token +",";
    }
    
    if(tokens!=''){
       return tokens.substr(0,tokens.length-1);
    }
    
    return tokens;
}
 
//详细信息
function viewUserRenderer(data) {
     return "<a href='javascript:viewobj(\"" + data.userId +"\",\"" +data.domainId+ "\",\"user\");'>"+data.userId+"</a>";
}

//查看用户绑定的令牌的详细信息
function viewTokenRenderer(data){
	var opers = '';
    var obj = data.tokens;
	if(obj.length < 1){
    	return opers;
    }
    for(var i=0; i<obj.length; i++){
        opers = opers+ "<a href='javascript:viewobj(\"" + obj[i].token+"\",\"" +data.domainId + "\",\"token\");'>" + obj[i].token + "</a>&nbsp;&nbsp;&nbsp;";  
    }
    
	return opers;
}

// 锁定状态渲染器
function lockedUserRenderer(data) {
	if(data.locked == 1){
	   return '<span style="color:red">'+ tempLock +'</span>';
	}else if(data.locked == 2){
	   return '<span style="color:red">'+ longLock +'</span>';
	}else{
	   return noLock;
	}
}

//启用状态
function enabledUserRenderer(data) {
	return data.enabled == 0 ? '<span style="color:red">'+ commonYes +'</span>' : '<span style="color:blue">'+ commonNo +'</span>';
}

/**
 * 查询,根据条件让Grid重新载入数据
 * @param {} stayCurrentPage 是否留在当前页面
 */
function query(stayCurrentPage){
	$("#orgFlag").val(1);
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
	checkConfi("user","max_bind_tokens");
	var orgFlag = $("#orgFlag").val();
    var queryParam = {
    	'page':page,
    	'pagesize':pagesize,
    	'queryForm.dOrgunitId':queryData.dOrgunitId,
    	'queryForm.userId':queryData.userId,
        'queryForm.realName':queryData.realName,
        'queryForm.token':queryData.token,
        'queryForm.locked':queryData.locked,
        'queryForm.enabled':queryData.enabled,
        'queryForm.backEndAuth':queryData.backEndAuthStr,
        'queryForm.localAuth':queryData.localAuthStr,
        'queryForm.bindState':queryData.bindState,
        'queryForm.orgFlag':queryData.orgFlag
    }
    return queryParam;
}
 

//设置查询条件的值
function setQueryObjData(){
	queryData = new Object();
	//查询值
	queryData.dOrgunitId = $("#orgunitIds").val();
	queryData.userId = $("#userId").val();
	queryData.realName = $("#realName").val();
	queryData.token = $("#token").val();
	queryData.orgFlag = $("#orgFlag").val();
	queryData.locked = $("#locked").val();
	queryData.enabled = $("#enabled").val();
	queryData.localAuthStr = $("#localAuthStr").val();
	queryData.bindState = $("#bindState").val();
	queryData.backEndAuthStr= $("#backEndAuthStr").val();
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


//添加用户
 function addUserInfo(){
     location.href= contextPath + "/manager/user/userinfo/add.jsp";
 }

//变更机构
function changeOrgunit(userId,domainId,orgunitId,tokenSn,tknOrgunitSn){
 	//location.href = contextPath + "/manager/user/userinfo/m_m_changeOrg.jsp?fromTag=1&userId="+replaceUserId(userId)+"&domainId="+domainId+"&orgunitId="+orgunitId+"&tokenSn="+tokenSn+"&tknOrgunitSn="+tknOrgunitSn
 	var url = contextPath + "/manager/user/userinfo/m_m_changeOrg.jsp?fromTag=1&userId="+replaceUserId(userId)+"&domainId="+domainId+"&orgunitId="+orgunitId+"&tokenSn="+tokenSn+"&tknOrgunitSn="+tknOrgunitSn
 	var topWin = FT.openWinBig(change_orgunit_lang,url);
	    topWin.currPage = this;
}

//编辑用户
function findobj(userId,domainId){
   var curPage = dataGrid.getCurrentPage();
   //  location.href = contextPath + '/manager/user/userinfo/userInfo!find.action?userInfo.userId='+ replaceUserId(userId) +'&userInfo.domainId='+domainId+'&cPage='+curPage;
   var url = contextPath + '/manager/user/userinfo/userInfo!find.action?userInfo.userId='+ replaceUserId(userId) +'&userInfo.domainId='+domainId+'&cPage='+curPage;
   var topWin = FT.openWinBig(edit_lang,url);
	   topWin.currPage = this;
}

//用户详细
function viewobj(objId,domainId,obj){
	if(obj == 'user'){
		FT.openWinMiddle(user_lang, contextPath + "/manager/user/userinfo/userInfo!view.action?userInfo.userId=" + replaceUserId(objId)+'&userInfo.domainId='+domainId, 'close');
	}if(obj == 'group'){
    	FT.openWinMiddle(group_lang, contextPath + "/manager/user/group/userGroup!view.action?userGroup.groupId=" + objId, 'close');
	}if(obj == 'token'){
    	FT.openWinMiddle(tkn_lang, contextPath + "/manager/token/token!view.action?tokenInfo.token=" + objId, 'close');
	}
}

//设置静态密码
function setStaticPass(userId,domainId){
	var curPage = dataGrid.getCurrentPage();
    staticPassWin = FT.openWinSmall(static_pw_lang, contextPath + "/manager/user/userinfo/userInfo!find.action?userInfo.userId="+replaceUserId(userId)+"&userInfo.domainId="+domainId+"&curPage="+curPage+"&source=setpass",true);
} 

function staticClose(object){
	$.ligerDialog.success(object,syntax_lang,function(){
        staticPassWin.close();
        query(true,false);
    });
}
 
//锁定解锁状态
function editLost(userId, locked, domainId){
	var curPage =  dataGrid.getCurrentPage();
 	var message = lock_account_lang;
 	if(locked == 1 || locked == 2) {
		message = unlock_account_lang;
 	}
 	$.ligerDialog.confirm(message,syntax_confirm_lang,function(sel) {
     if(sel) {
		 $("#ListForm").ajaxSubmit({
			async:false,    
			dataType : "json",  
			type:"POST", 
			url : contextPath + "/manager/user/userinfo/userInfo!editUserLost.action?userInfo.userId="+replaceUserId(userId)+"&userInfo.domainId="+domainId+"&userInfo.locked="+locked,
			success:function(msg){
			    if(msg.errorStr == 'success'){
		    		$.ligerDialog.success(msg.object, syntax_lang, function(){
            			query(true);
            		});
		    	}else{
			        FT.toAlert(msg.errorStr,msg.object,null);
		    	}
			}
		});
		}
	});
}

//启用禁用用户 方法
function editEnabled(userId,enabled,domainId){
	var curPage =  dataGrid.getCurrentPage();
	var message;
	if(enabled == 1){
		message = whether_disabled_lang;
	}else if(enabled == 0){
		message = whether_enable_lang;
	}
	FT.Dialog.confirm(message,syntax_confirm_lang,function(sel) {
		if(sel) {
			$("#ListForm").ajaxSubmit({
				async:false,    
				dataType : "json",  
				type:"POST", 
				url : contextPath + "/manager/user/userinfo/userInfo!editUserEnabled.action?userInfo.userId="+replaceUserId(userId)+"&userInfo.domainId="+domainId+"&userInfo.enabled="+enabled,
				success:function(msg){
			    	if(msg.errorStr == 'success'){
			    		$.ligerDialog.success(msg.object, syntax_lang, function(){
	            			query(true);
	            		});
			    	}else{
				        FT.toAlert(msg.errorStr,msg.object,null);
			    	}
				}
			});
		}
	 });
}


 //tag 1绑定令牌（tag 2更换令牌）
function bindToken(userId,domainId,orgunitId,DOrgunitName,tag){
	 var url = "";
	 var title = "";
	 if(tag==2){//更换令牌
	 	title = replace_tkn_lang;
 	 	var curPage =  dataGrid.getCurrentPage();
	 	url = contextPath + "/manager/user/userinfo/userInfo!bindChangeInit.action?userInfo.userId=" + replaceUserId(userId) +"&userInfo.domainId="+domainId+"&currentPage="+curPage;
	 }else{ //绑定令牌
	 	title = bind_tkn_lang;
	    url = contextPath + "/manager/user/userinfo/m_m_bind.jsp?fromTag=1&userId="+replaceUserId(userId)+"&domainId="+domainId+"&orgunitId="+orgunitId+"&DOrgunitName="+DOrgunitName;
	 }
	 if(url!=""){
	 	//打开大窗口进行操作
	 	var topWin = FT.openWinBig(title,url);
	    topWin.currPage = this;
	 }
}
		
//解除绑定
function unbindToken(userId,domainId, count, token){
 var curPage =  dataGrid.getCurrentPage();
	if(count==1){ //单只令牌
		$.ligerDialog.confirm(unbind_user_lang, function (yes){
		 if(yes){
		   $("#ListForm").ajaxSubmit({
			async:false,    
			dataType : "json",  
			type:"POST", 
			url : contextPath +  "/manager/user/userinfo/userInfo!unBindUT.action?userInfo.userId=" + replaceUserId(userId) +"&userInfo.domainId="+domainId+"&userInfo.tokens="+token+"&unbindNum="+count,
			success:function(msg){
			     if(msg.errorStr == 'success'){
			         	 $.ligerDialog.success(msg.object,syntax_lang,function(){
						   query(true);
						 });
			     }else{
				        FT.toAlert(msg.errorStr,msg.object,null);
			     }
			}
		  });
		 }
	  });	
    }else{//多只令牌
		FT.openWinMiddle(unbind_tkn_lang,contextPath + "/manager/user/userinfo/unbind_token.jsp?userId="+replaceUserId(userId)+"&domainId="+domainId+"&currentPage="+curPage,true);
	}
}
 
//令牌认证
function tknAuth(userId, tokenArr){
    var curPage = dataGrid.getCurrentPage();
	FT.openWinMiddle(tkn_auth_lang, contextPath +  "/manager/token/tknauth_view.jsp?userId="+replaceUserId(userId)+"&tokenArr="+tokenArr+"&source_usr=1&&currentPage="+curPage,  true);
}

//令牌同步
function tknSync(userId, tokenArr){
    var curPage = dataGrid.getCurrentPage();
    var tokens = '';
    var tknarr = tokenArr.split(",");
    var url= contextPath+'/manager/token/token!findToken.action';
    if(tknarr.length == 1) {
	   	$.post(url, {'token' : tokenArr},
			function(msg){
				if(msg.errorStr == "success"){
					var physicaltype = msg.object.physicaltype;
					if (physicaltype == 6) {
						FT.toAlert('warn',tkn_no_sync_lang, null);
						return;
					}else {
						syncWin = FT.openWinMiddle(synch_title_lang, contextPath +  "/manager/user/userinfo/bindtknsync.jsp?userId="+replaceUserId(userId)+"&tokenArr="+tokenArr+"&source_usr=1&&currentPage="+curPage,  true);
					}
				}
			}, "json"
		);
		
    }else {
    	var urlto = contextPath+'/manager/token/token!getSyncTkns.action';
    	$.post(urlto, {'syncTkns' : tokenArr},
			function(msg){
				if(msg.errorStr == "success"){
					var synctkns = msg.object;
					syncWin = FT.openWinMiddle(synch_title_lang, contextPath +  "/manager/user/userinfo/bindtknsync.jsp?userId="+replaceUserId(userId)+"&tokenArr="+synctkns+"&source_usr=1&&currentPage="+curPage,  true);
				}else {
					FT.toAlert('warn',tkn_no_sync_lang, null);
					return;
				}
			}, "json"
		);
    }

}

function closeSyncWin(object) {
	$.ligerDialog.success(object,syntax_tip_lang,function(){
		syncWin.close();
		query(true);
	});
}


//批量删除/批量解绑
function batchOper(){
	var operObj = $("#operObj").val();
	var oper = $("#oper").val();
	
	//查询条件
	var userIdStr = queryData.userId;
	var realNameStr = queryData.realName;
	var dOrgunitId = queryData.dOrgunitId;
	var tokenStr = queryData.token;
	var lockedStr = queryData.locked;
	var enabledStr = queryData.enabled;
	var localAuthStr = queryData.localAuthStr;
	var backEndAuthStr = queryData.backEndAuthStr;
	var bindStateStr = queryData.bindState;
	
	if(oper==-1){
	    FT.toAlert('warn',oper_state_lang, null);
		return;
	}
	
	//本页选中的记录
	var userNum = 0;
	var userIds = '';
	var url = contextPath + "/manager/user/userinfo/userInfo!batchOper.action?operObj=" + operObj + "&oper=" + oper;
	
	//执行之后的状态
	if(operObj == 0){ //本页
	    var sRows = dataGrid.getSelectedRows();
		if(sRows.length<1){
		    FT.toAlert('warn',sel_user_lang, null);
		    return;
		}
		for(var i=0,sl=sRows.length;i<sl;) {
		   userNum = userNum + 1;
		   var token = {},sRow = sRows[i++];
	           userIds  = userIds + sRow['userId']+":"+sRow['domainId'] +",";//将域id拼接上
		}
		userIds = userIds.substr(0,userIds.length-1);
		userIds=replaceUserId(userIds);
		userNum = sRows.length;
	 } else {//本次查询
	     var ptotal= dataGrid.getCurrentTotal();
		 if(ptotal< 1){
			 FT.toAlert('warn',oper_user_lang, null);
			 return;
		 }
		 userNum = ptotal;
		 url = contextPath + "/manager/user/userinfo/userInfo!batchOper.action?operObj=" + operObj + 
		    "&oper=" + oper +
			"&userId=" + replaceUserId(userIdStr) + 
			"&realName=" +realNameStr+
			"&dOrgunitId="+dOrgunitId+
			"&locked="+lockedStr+
			"&enabled="+enabledStr+
			"&localAuthStr="+localAuthStr+
			"&backEndAuthStr="+backEndAuthStr+
			"&bindState="+bindStateStr+
			"&token=" +tokenStr;
	}

	var info = orgInfo(userNum, oper);
	$.ligerDialog.confirm(info, function (yes){
     if(yes){
	    if (oper == 2) {
	    	$("#seturl").val(url);
	    	$("#userIds").val(userIds);
			waitW = FT.openWinSmall(rad_conf_lang,contextPath+"/manager/user/userinfo/selradius.jsp",true);
		}else if (oper == 7) {
	    	$("#seturl").val(url);
	    	$("#userIds").val(userIds);
			waitW = FT.openWinSmall(back_endauth_lang,contextPath+"/manager/user/userinfo/selbackendauth.jsp",true);
		}else if (oper == 8) {
	    	$("#seturl").val(url);
	    	$("#userIds").val(userIds);
			waitW = FT.openWinSmall(sel_localauth_lang,contextPath+"/manager/user/userinfo/sellocalauth.jsp",true);
		} else {
	     	batchExecOper(url,userIds);
		}
      
     }
	});
}

function batchExecOper(url,userIds) {
	var ajaxbg = $("#background,#progressBar");//加载等待
	ajaxbg.show();
	var radprofileid = $("#radprofileid").val();
	var backEndAuth = $("#backendAuth").val();
	var localAuth = $("#localAuth").val();
	var pwd = $("#pwd").val();
	
	$("#ListForm").ajaxSubmit({
		async:true,    
		dataType : "json",  
		type:"POST", 
		url : url,
		data:{userIds:userIds,radprofileid:radprofileid,backEndAuth:backEndAuth,localAuth:localAuth,pwd:pwd},
		success:function(msg){
		     ajaxbg.hide();
		     if(msg.errorStr == 'success'){
		     	$.ligerDialog.success(msg.object,syntax_lang,function(){
		     		if (waitW != null) {
					    waitW.close();
		     		}
		     		query(true);
				});
		     }else{
			    FT.toAlert(msg.errorStr,msg.object,null);
		     }
		}
    }); 
}

function orgInfo(n, t){
	var r;
	if(t == 0){
		r = batch_del_lang + n + a_user_lang;
	}else if(t == 1){
		r = batch_unbind_lang + n +  a_user_lang;
	}else if(t == 2) {
		r = batch_add_lang +  n + a_user_lang;
	}else if(t == 3) {
		r = batch_lock_lang +  n + a_user_lang;
	}else if(t == 4) {
		r = batch_unlock_lang +  n + a_user_lang;
	}else if(t == 5) {
		r = batch_disabled_lang +  n + a_user_lang;
	}else if(t == 6) {
		r = batch_enable_lang +  n + a_user_lang;
	}else if(t == 7) {
		r = batch_backend_lang +  n + a_user_lang;
	}else if(t == 8) {
		r = batch_localauth_lang +  n + a_user_lang;
	}
	
	return r;
}

function checkConfi(confType, confMark){
	var url = contextPath + "/manager/user/userinfo/userInfo!checkConfi.action?confType="+confType+ "&confMark=" + confMark;;
	var hidValue;
	$.post(url,
		function(msg){
			$('#bindToneks').val(msg);
		}, "text"
	);
}

//标签切换 用户查询与用户操作切换
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
