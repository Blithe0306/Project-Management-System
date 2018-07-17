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

function addObj(oper,obj,win){
	//selectObj(obj);
	var cpage = 1;
	var url = contextPath + "/manager/customer/custInfo!add.action";
	if(oper == 1){
	    cpage = $("#cPage").val();
	    url = contextPath + "/manager/customer/custInfo!modify.action";
	} 
	var message;
	if(oper==0){
		message = continue_add_lang;
	}else{
		message = whether_edit_lang;
	}
	//判断添加与修改操作oper：1修改；0添加；
	if(oper==1){
		$("#AddForm").ajaxSubmit({
			async:false,    
			dataType : "json",  
			type:"POST", 
			url : url,
			success:function(msg){
		    	if(msg.errorStr == 'success'){
		    		$.ligerDialog.success(succ_tip_lang, syntax_tip_lang,function(){
		    			location.href = contextPath + '/manager/customer/list.jsp?cPage='+cpage;
		    		});
		    	}else{
		    		FT.toAlert(msg.errorStr,msg.object,null);
		    	}
			}
		});
	}else{
		$("#AddForm").ajaxSubmit({
			async:false,    
			dataType : "json",  
			type:"POST", 
			url : url,
			success:function(msg){
		    	if(msg.errorStr == 'success'){
		    		FT.Dialog.confirm(message,syntax_confirm_lang,function(sel) {
	    				if(sel) {
		    				location.href = contextPath+'/manager/customer/custInfo!toAddCustomer.action';
		    			}else{ // 关闭tab页面
		    				window.parent.removeTabItemF('020002');
		    			}
	    			});
		    	}else{
		    		FT.toAlert(msg.errorStr,msg.object,null);
		    	}
			}
		});
  };
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
	location.href = contextPath + '/manager/customer/list.jsp?mode=4&currentPage='+currentPage;
}

// 提示组织机构为空，关闭窗口
function closeOrgWin(object) {
	$.ligerDialog.success(object,syntax_tip_lang,function(){
		winOrgClose.close();
	});
}