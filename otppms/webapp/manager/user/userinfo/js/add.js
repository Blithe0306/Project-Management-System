// add/edit role javascript

var contextPath;
var closeWin;

$(function() {
	contextPath = $("#contextPath").val();

	$("#userId").keydown(function(e) {
		if(e.keyCode == '13') {
			return false;
		}
	});
})


//打开选择用户组对话框
function selGroup(){
    FT.openWinMiddle(user_group_lang,contextPath + '/manager/user/userinfo/sel_usergroups.jsp',false);
}

// 选择令牌完成后将所选令牌或用户组添加到令牌列表
function confirmSelects(jSel,id) {
    var selList  =  $('#'+id); 
    var rolevals = "";
    $('#'+id+' option').each(function(i,e){
		rolevals+=e.value+",";
	 });
	while(jSel[0]) {
		var obj = jSel.shift();
		if(rolevals.indexOf(obj.id)<=-1){
		   selList.append("<option value='" + obj.id + "'>" + obj.name + "</option>");
		}
		
	}
}

// 删除令牌列表中选定的令牌 或用户组; 
function delSelect(id) {
  var selectedList = $('#'+id+' option:selected');
    if(selectedList.length<1){
       FT.toAlert('warn',need_del_date_lang,null);
       return;
    }
    $('#'+id).children().filter('[selected]').remove();
    
}

//根据配置的用户门户登录密码验证方式，校验用户信息
function checkUserInfo(flag) {
	$.ajax({ 
		async:true,
		type:"POST",
		url:contextPath + "/manager/user/userinfo/userInfo!getPortalInitPwdVType.action",
		dataType:"json",
		success:function(msg){
			var errorStr = msg.errorStr;
			var vtype = msg.object;
			var email = $("#email").val();
			var cellPhone = $("#cellPhone").val();
			if("error" != errorStr){
				var tipInfo = '';
				if (vtype == '2' && (email == '' || email == null)) {
					tipInfo = verify_login_type_check_email_lang;
				}else if(vtype == '3' && (cellPhone == '' || cellPhone == null)) {
					tipInfo = verify_login_type_check_phone_lang;
				}else {
					jumpToFun(flag);
					return;
				}
				if (tipInfo != '') {
					$.ligerDialog.confirm(tipInfo,syntax_confirm_lang,function(sel) {
						if(sel) {
							jumpToFun(flag);
						}
					});
				}
			}else{
			    jumpToFun(flag);
			}
		}
	});
}
//判断方法跳转，0：添加 1：编辑
function jumpToFun(flag) {
	if (flag == 0) {
		saveUserToDb();
	}else {
		editObj();
	}
}

 //只保存用户信息到数据库
 function saveUserToDb(){
 		//selectObj('tokens');
       $("#AddForm").ajaxSubmit({
		async:false,    
		dataType : "json",  
		type:"POST", 
		url : contextPath + "/manager/user/userinfo/userInfo!add.action",
		success:function(msg){
		     if(msg.errorStr == 'success'){
		     	 //提示提示管理员是否要去绑定令牌
		     	 FT.Dialog.confirm(bind_tkn_lang, syntax_tip_lang, function(r){
		     	 	if(r==1){//点击了 是
		     	 		var userId=msg.object.split(",")[1];
		     	 		var domainId = msg.object.split(",")[2];
		     	 		var orgunitId = msg.object.split(",")[3];
		     	 		
		     	 		// 判断组织机构ID是否为空，为空赋值为0；
		     	 		if(orgunitId == "null" || orgunitId == null){
		     	 			orgunitId = "0";
		     	 		}	
		     	 		location.href = contextPath + "/manager/user/userinfo/m_m_bind.jsp?fromTag=2&userId="+replaceUserId(userId)+"&domainId="+domainId+"&orgunitId="+orgunitId;		
		     	 	}else if(r==0){//点击了否
		     	 		location.href = contextPath + '/manager/user/userinfo/add.jsp';
		     	 	}
		     	 });
		     }else{
			     FT.toAlert(msg.errorStr,msg.object,null);
		     }
		}
	});
}
 //最后的保存,只是做修改操作
 function saveObj(){
       selectObj('tokens');
	   selectObj('userGroups');
	   $("#AddForm").ajaxSubmit({
		async:false,    
		dataType : "json",  
		type:"POST", 
		url : contextPath + "/manager/user/userinfo/userInfo!modifyUser.action",
		success:function(msg){
		     if(msg.errorStr == 'success'){
		         $.ligerDialog.success(save_succ_lang, syntax_tip_lang,function(){
		         	location.href = contextPath + '/manager/user/userinfo/list.jsp';
		         });
		     }else{
			        FT.toAlert(msg.errorStr,msg.object,null);
		     
		     }
		}
	});
}      


function selectObj(obj){
	if (obj != -1){
	 for (i=0; i<document.getElementById(obj).length; i++)
		document.getElementById(obj).options[i].selected = true;			 
	}
}
 

// 回退操作
function goBack() {
	var currentPage = $("#currentPage").val();
	location.href = contextPath + '/manager/user/userinfo/list.jsp?mode=4&currentPage='+currentPage;
}


//用户编辑保存
function editObj(){
     var cPage = $('#currentPage').val();
	    if(!bool){return;}
	    $("#AddForm").ajaxSubmit({
			async:false,    
			dataType : "json",  
			type:"POST", 
			url : contextPath + "/manager/user/userinfo/userInfo!modify.action",
			success:function(msg){
			     if(msg.errorStr == 'success'){
			         $.ligerDialog.success(save_succ_lang, syntax_tip_lang,function(){
			         	//location.href = contextPath + '/manager/user/userinfo/list.jsp?mode=4&currentPage='+cPage;
			         	goBackClose(true);
			         });
			     }else{
				        FT.toAlert(msg.errorStr,msg.object,null);
			     
			     }
			}
		});
}

// 提示组织机构为空，关闭窗口
function closeOrgWin(object) {
	$.ligerDialog.success(object,syntax_tip_lang,function(){
		winOrgClose.close();
	});
}