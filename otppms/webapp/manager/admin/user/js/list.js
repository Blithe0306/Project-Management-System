var cPath;
var changeWin;

$(function() {
	cPath = $("#contextPath").val();
	cPath += "/";
	var loadPage = 1;
	if($("#curPage").val() != '') {
		loadPage = +$("#curPage").val();
	}

    window['dataGrid'] = $("#listDataAJAX").ligerGrid({
        root : 'items',
        record : 'results',
        url : 'adminUser!init.action?rflag=0',
        columns : [
            {display:account_lang,name:'adminid', width:'16%',render:adminRender},
            {display:realname_lang,name:'realname', width:'16%'},
            {display:lock_state_lang,name:'locked', width:'8%',render:lockStatusRender},
            {display:enable_lang,name:'enabled',render:UseStatusRender, width:'8%'},
            {display:creator_lang,name:'createuser', width:'12%'},
            {display:create_time_lang,  name:'createtimeStr', width:'14%'},
            {display:operation_lang,name:'',minWidth:120,render:operationRenderer, width:'24%'}
        ],
        parms : [
        	{name:'operType',value:'1'}
        ],         
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
    $("td :input").keydown(function(e) {
		if(e.keyCode == '13') {
			query(false);
		}
	});
})
 
// ====================  Grid 所用Renderer  ===========================
// 锁定状态渲染器
function lockStatusRender(data,rIndex) {
	if(data.locked == 1){
	   return '<span style="color:red">'+ langLocktemp +'</span>';
	}else if(data.locked == 2){
	   return '<span style="color:red">'+ langLockperm +'</span>';
	}else{
	   return langNolock;
	}
}

/**
 * 用户账户渲染函数
 * @param {} data
 */
function adminRender(data,i) {
	return "<a href='javascript:view(\"" + data.adminid + "\",\"" + data.createuser + "\");'>" + data.adminid + "</a>";
}


// 启用状态渲染器
function UseStatusRender(data,rIndex) {
	if(data.enabled==0||data.enabled==2){//否
		if(data.logincnt==0&&isActivate){//未激活
			return '<span style="color:red">'+ langNoactive +'</span>';
		}else{
			return '<span style="color:red">'+ langNo +'</span>';
		}
	}else if(data.enabled==1){//是
		return langYes;
	}else{
		return '';
	}
}

//查看管理员绑定的令牌的相关信息
function viewTokenRenderer(data,rIndex){
	var opers='';
    var obj = data.tokens;
    if(obj.length<1){
       return opers;
    }
    for(var i=0;i<obj.length;i++){
        opers = opers+ "<a href='javascript:viewobj(\"" + obj[i].token+"\",\""+0+"\",\"token\");'>" + obj[i].token + "</a>&nbsp;&nbsp;&nbsp;";  
    }
   return opers;
}

//用户详细
function viewobj(objId,domainId,obj){
     if(obj=='user'){
     	 FT.openWinMiddle(info_lang,cPath + "/manager/user/userinfo/userInfo!view.action?userInfo.userId=" + replaceUserId(objId)+'&userInfo.domainId='+domainId,'close' );
     }if(obj=='group'){
         FT.openWinMiddle(group_info_lang,cPath + "/manager/user/group/userGroup!view.action?userGroup.groupId=" + objId,'close' );
     }if(obj=='token'){
         FT.openWinMiddle(tkn_info_lang,cPath + "/manager/token/token!view.action?tokenInfo.token=" + objId,'close' );
     }
}
 

// 操作列渲染器
function operationRenderer(data,rIndex) { 
    var curLoginRole = $('#l_userid_role').val();
    var loginUser = $('#l_userid').val();
    var opers;
    
    //不能对自己进行如下操作
    if(loginUser != data.adminid){ // 登录的用户与列表上用户ID不同
	    if(permEdit != ''){
	       opers += "<a href='javascript:findObj(\"" + data.adminid + "\",\"" + data.createuser + "\");'>"+permEdit+"</a>&nbsp;&nbsp;";
	    }
	    
	    if(permEnable!=''){
	    	if(data.enabled == 1){
				permEnable = permEnable.replace('error_go.png','error_delete.png'),
				permEnable = permEnable.replace(langEnable, langDisabled);
			}else{
				//permEnable 图片默认就是启用的
				permEnable = permEnable.replace('error_delete.png','error_go.png'),
				permEnable = permEnable.replace(langDisabled, langEnable);
			}
			opers += "<a href='javascript:editObj(\"" + data.adminid + "\",\"" + data.enabled + "\",0)'>" + permEnable + "</a>&nbsp;&nbsp;";
	    }
	    
	    if(permLock!=''){
	    	if(data.locked == 1||data.locked == 2){
				permLock = permLock.replace('lock.png','unlock.gif'),
				permLock = permLock.replace(langALock, langAUnlock);
			}else if(data.locked == 0){
				//permEnable 图片默认就是锁定
				permLock = permLock.replace('unlock.gif','lock.png'),
				permLock = permLock.replace(langAUnlock, langALock);
			}
			opers += "<a href='javascript:editObj(\"" + data.adminid + "\",\"" + data.locked + "\",1)'>" + permLock + "</a>&nbsp;&nbsp;";
	    }
	    
	    //密码操作
	    if(permEditPwd != ''){
	    	opers += getPwdOpers(data);
	    }
	}else{  // 登录的用户与列表上用户ID相同
		if(curLoginRole =='ADMIN'){  // 超级管理员
	    	if(permEdit != ''){
		       opers += "<a href='javascript:findObj(\"" + data.adminid + "\",\"" + data.createuser + "\");'>"+permEdit+"</a>&nbsp;&nbsp;";
		    }
		    
		    //密码操作
		    if(permEditPwd != ''){
				opers += getPwdOpers(data);
			}
	    }
	}
    
    if(opers=='' || opers==null){
        return '';
    }
    return opers.replace("undefined", "");
}

//获取密码相关的操作
function getPwdOpers(data){
	var opers = '';
	//修改密码 或者是密码重置 说明：如果系统配置为邮件激活方式 则此处是密码重置，否则是修改密码
	if(permEditPwd!=''||permResetPwd!=''){
   	 	if(isActivate){
   	 		opers += "<a href='javaScript:resetAdmPwd(\"" + data.adminid + "\",\""+data.enabled+"\",\""+data.logincnt+"\")'>" + permResetPwd + "</a>&nbsp;&nbsp;";
   	 	}else{
			opers += "<a href='javaScript:modifyPwd(\"" + data.adminid + "\")'>" + permEditPwd + "</a>&nbsp;&nbsp;";
		}
	}
	
	return opers;
}

//获取管理员对令牌相关的操作权限 根据需求中 有创建管理员的权限就有 令牌操作的权限
function getTokenOpers(data){
	var opers = '';
	if(data.tokens==null||data.tokens==''){
		if(permAdd!=''){
			var permBindToken = '<img src="'+cPath+'images/icon/tag_blue_add.png" width="16" height="16" hspace="2"  border="0" title="'+ langABindtkn +'">';
        	opers += "<a href='javascript:bindToken(\"" + data.adminid + "\",\""+data.orgunitIds+"\",1);'>" + permBindToken+ "</a>";//1表示绑定令牌
		}
	}else{
		if(permAdd!=''){
	        var tokens = data.tokens;
	        var tokenStr='';
	        if(tokens.length==1){
	        	tokenStr=tokens[0].token;
	        }
	        //解绑
	        var permUnBindToken = '<img src="'+cPath+'/images/icon/tag_blue_delete.png" width="16" height="16" hspace="2"   border="0" title="'+ langAUnbindtkn +'">';
	        opers+="<a href='javascript:unbindToken(\"" + data.adminid + "\",\"\",\"" + tokens.length + "\",\"" + tokenStr + "\");'>" + permUnBindToken+ "</a>";
	        
	        //更换
			var permChangeToken = '<img src="'+cPath+'/images/icon/tag_blue_edit.png" width="16" height="16" hspace="2"  border="0" title="'+ langAReptkn +'">';
        	opers+="<a href='javascript:bindToken(\"" + data.adminid +"\",\""+data.orgunitIds+"\",2,\""+tokenStr+"\");'>" + permChangeToken+ "</a>";
		}
   	}
   
   return opers;
}

//管理员密码重置
function resetAdmPwd(adminId,enabled,logincnt){
	if(enabled == 0||enabled==2){
		if(isActivate&&logincnt==0){
			//未激活状态 的不能重置密码
			FT.toAlert('warn', pass_noreset_lang, null); 
		}else{
			//未启用状态 的不能重置密码
			FT.toAlert('warn', noenab_pass_noreset_lang, null);
		}
	}else{
		$.ligerDialog.confirm(pass_reset_lang,confirm_lang,function(sel) {
			if(sel) {
	        	var url = cPath+'/manager/admin/user/adminUser!resetPwd.action';		
				$.post(url, {adminid:adminId},
					function(msg){
						var errorStr = msg.errorStr;				
						FT.toAlert(errorStr, msg.object, null);
						if(errorStr=="success"){
							query(true);
						}
				}, "json");	
	        }
		});
	}
	
}

 //tag 1绑定令牌（tag 2更换令牌）
function bindToken(userId,orgunitIds,tag,tokenStr){
	var curPage = dataGrid.getCurrentPage();
	if(tag==2){//更换令牌
		location.href = cPath + "/manager/admin/user/bind_token.jsp?tokenStr="+tokenStr+"&adminid=" + replaceUserId(userId) +"&currentPage="+curPage;	 	
	}else{ //绑定令牌
		location.href = cPath + "/manager/admin/user/m_m_bind.jsp?currentPage="+curPage+"&userId="+replaceUserId(userId)+"&orgunitIds="+orgunitIds;
	}
}
		
//解除绑定 可以直接使用用户的解绑方法
function unbindToken(userId,domainId, count, token){
 var curPage =  dataGrid.getCurrentPage();
	if(count==1){ //单只令牌
		$.ligerDialog.confirm(unbind_tkn_lang, function (yes){
		 if(yes){
		   $("#ListForm").ajaxSubmit({
			async:false,    
			dataType : "json",  
			type:"POST", 
			url : cPath +  "/manager/admin/user/adminBind!unBindUT.action?adminUser.adminid=" + replaceUserId(userId) +"&adminUser.tokens="+token+"&unbindNum="+count,
			success:function(msg){
			     if(msg.errorStr == 'success'){
			         	 $.ligerDialog.success(msg.object,syntax_tip_lang,function(){
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
	 	 FT.openWinMiddle(lifting_tkn_bind_lang,cPath + "/manager/user/userinfo/unbind_token.jsp?userId="+replaceUserId(userId)+"&domainId="+domainId+"&currentPage="+curPage,false);
	}
}
 
//令牌认证
function tknAuth(userId, tokenArr){
    var curPage = dataGrid.getCurrentPage();
    if(tokenArr!=''){
       tokenArr = tokenArr.substr(0,tokenArr.length-1);
    }
	FT.openWinMiddle(tkn_auth_lang, cPath +  "/manager/token/tknauth_view.jsp?userId="+replaceUserId(userId)+"&tokenArr="+tokenArr+"&source_usr=1&&currentPage="+curPage,  true);
}


// 根据userid查看用户详细信息
function view(adminid,createuser){
	FT.openWinMiddle(syntax_detail_lang,cPath + 'manager/admin/user/adminUser!view.action?adminUser.adminid=' + adminid,'close');
}


//修改密码
function modifyPwd(userName){
    changeWin = FT.openWinSmall(change_pwd_lang, cPath + "/manager/main/repassword.jsp?userName="+userName+"&flag="+1,[{text:syntax_modify_lang,onclick:FT.buttonAction.okClick},{text:syntax_close_lang,onclick:FT.buttonAction.cancelClose}]);
}

//添加管理员
function addAdmInfo(){
	 location.href = cPath + "/manager/admin/user/add.jsp";
}

//查询,根据条件让Grid重新载入数据
function query(currentPage){
	var page = 1;
	if(currentPage) {
		page = dataGrid.getCurrentPage();
	} 

	var pagesize = dataGrid.getCurrentPageSize();
    var queryParam = formParms(page, pagesize);
    
    dataGrid.loadServerData(queryParam);
}

//组装表单查询条件QueryFilter
function formParms(page, pagesize){
	var adminId = $("#adminid").val();
    var adminName = $("#realname").val();
    var createuser = $("#createuser").val();
    var locked = $("#locked").val();
    var roleName = $("#roleName").val();
    var orgunitIds = $("#orgunitIds").val();
 
    var queryParam = {
    	'page':page,
    	'pagesize':pagesize,
        'queryForm.adminid':adminId,
        'queryForm.createuser':createuser,
        'queryForm.locked':locked,
        'queryForm.realname':adminName,
        'queryForm.roleName':roleName,
        'queryForm.orgunitIds':orgunitIds
        
    }
    
    return queryParam;
}

// =========================  Grid支持操作  =============================
// 删除操作
function delData() {
	var selRows = dataGrid.getSelectedRows();
	var loginUser = $('#l_userid').val();
	var loginUserRole = $('#l_userid_role').val();
	var flag = false;
	if(selRows < 1) {
		FT.toAlert('warn', '请选择要删除的管理员！',null);
	} else {
		$.ligerDialog.confirm(has_child_lang,syntax_confirm_lang,function(sel) {
            if(sel) {
            	var selLength = selRows.length;
            	var ids = "";
            	for(var i=0;i<selLength;i++) {
            		ids = ids.concat(selRows[i]['adminid'],',');
            		
            		if(selRows[i]['createuser']!=loginUser && selRows[i]['adminid']!=loginUser){
            		   flag = true;
            		   $.ligerDialog.alert(other_create_lang, syntax_tip_lang, 'warn');
            		   return;
            		} 
            	}
            	$.post(cPath + 'manager/admin/user/adminUser!delete.action',{
					delUserIds:ids.substring(0,ids.length-1)
	            	},function(result){
	            		if(result.errorStr == 'success'){	            			
		            		FT.toAlert(result.errorStr, result.object, null);
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
 
 
//编辑
function findObj(adminid,createuser){
	var user = $('#l_userid').val();
	var loginUserRole = $('#l_userid_role').val();
	var curPage = dataGrid.getCurrentPage();
	if(adminid == user){
		location.href= cPath+"/manager/admin/user/adminUser!find.action?adminUser.adminid="+adminid+"&currentPage="+curPage;
	}else if(adminid!=user && createuser!=user){
		FT.toAlert('warn',edit_child_lang, null);
		return;
	}else{
		location.href= cPath+"/manager/admin/user/adminUser!find.action?adminUser.adminid="+adminid+"&currentPage="+curPage;
	}
}

//启用、禁用某个管理员 ,flag 为0是启用禁用；为1锁定解锁
function editObj(adminid,data,flag){
	var user = $('#l_userid').val();
	var curPage = dataGrid.getCurrentPage();

	if(flag == 0){
		if(adminid == user){
			 FT.toAlert('warn',oper_myself_lang, null);
		     return;
		}else{
			var message;
				if(data == 1){
					message = whether_disabled_lang;
				}else if(data == 0||data == 2){// 刚重置完密码的是2
					message = whether_enable_lang;
				}
				FT.Dialog.confirm(message,syntax_confirm_lang,function(sel) {
					if(sel) {
				       $("#ListForm").ajaxSubmit({
							async:false,    
							dataType : "json",  
							type:"POST", 
							url : cPath+"/manager/admin/user/adminUser!enabledAdmin.action?adminUser.adminid="+adminid+"&adminUser.enabled="+data+"&currentPage="+curPage,
							success:function(msg){
							     if(msg.errorStr == 'success'){
							        $.ligerDialog.success(msg.object, syntax_tip_lang, function(){
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
		   
	}else {
		if(adminid == user){
			FT.toAlert('warn',lock_myself_lang, null);
		    return;
        }else {
        	var message = sure_lock_lang;
         	if(data == 1 || data == 2) {
         		message = sure_unlock_lang;
         	}
         	
         	$.ligerDialog.confirm(message,syntax_confirm_lang,function(sel) {
	            if(sel) {
		         	$.post(cPath + '/manager/admin/user/adminUser!lockedAdmin.action',{
							adminid:adminid,
							locked:data
		            	},function(result){
		            		if(result.errorStr == 'success'){	            			
			            		$.ligerDialog.success(result.object, syntax_tip_lang, function(){
			            			query(true);
			            		});
			            	}else {
			            		FT.toAlert(result.errorStr, result.object, null);
			            	}
		            	},'json'
		        	);
        		}
			});
        }
	}
}
/**
*初始化变更创建人
*/
function changeCreater(){
var curPage = dataGrid.getCurrentPage();
var curLoginUser = $('#l_userid').val();
var selRows = dataGrid.getSelectedRows();
var flag = false;
if(selRows < 1) {
		var warn = $.ligerDialog.alert(change_user_lang, syntax_tip_lang, 'warn');
	}else{
	    var selLength = selRows.length;
        var ids = "";
        for(var i=0;i<selLength;i++) {
            if(selRows[i]['adminid'] == curLoginUser){
               flag = true;
               $.ligerDialog.alert(myself_creater_lang, syntax_tip_lang, 'warn');
               return;
            }
            ids = ids.concat(selRows[i]['adminid'],',');
        }
    	$.ligerDialog.confirm(myself_create_lang,syntax_confirm_lang,function(sel) {
            if(sel) {
            	changeWin = FT.openWinMiddle(change_creator_lang,cPath+'/manager/admin/user/adminUser!queryDesignate.action?curPage='+curPage+'&&adminids=' + ids ,true);  
            }else{
            	return;
            }
		});    
      
	}
}

function winClose(object){
	$.ligerDialog.success(object,syntax_tip_lang,function(){
		changeWin.close();
		query(true);
	});
}

/**
 * 处理返回信息的显示与动态隐藏
 * @param {} data 信息包装JSON
 */
function showAndHiddenMsg(data) {
	var jMSG = $("#msgShow").find('span').empty().append(data).end().fadeIn('slow');
    setTimeout(function(){jMSG.fadeOut('normal')},4500);
}

// 提示组织机构为空，关闭窗口
function closeOrgWin(object) {
	$.ligerDialog.success(object,syntax_tip_lang,function(){
		winOrgClose.close();
	});
}
