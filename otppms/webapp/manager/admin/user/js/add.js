// add.jsp

var contextPath;

$(function(){
	contextPath = $("#contextPath").val();
})

// 打开选择角色对话框
function selRoles() {
	FT.openWinMiddle(select_role_lang,contextPath + '/manager/admin/user/sel_role.jsp',false);
}
    
// 选择角色完成后将所选角色添加到角色列表
function confirmSelects(jSel) {
	var rolevals = "";
    var roleList = $("#adminRoles");
	
	$("#adminRoles option").each(function(i,e){
		rolevals+=e.value+",";
	});
	 
	while(jSel[0]) {
	
		var role = jSel.shift();
		if(rolevals.indexOf(role.rid+",")<=-1){
		   roleList.append("<option value='" + role.rid + "'>" + role.rname + "</option>");
		}
		
	}
}

// 删除角色列表中选定的角色
function delRoles() {
    var selectedRole = $('#adminRoles option:selected');
    if(selectedRole.length<1){
       FT.toAlert('warn',need_del_date_lang,null);
       return;
    }
	$("#adminRoles").children().filter('[selected]').remove();
}


function addObj(oper,obj,win){
	selectObj(obj);
	var cpage = 1;
	var url = contextPath + "/manager/admin/user/adminUser!add.action";
	if(oper == 1){
	    cpage = $("#cPage").val();
	    url = contextPath + "/manager/admin/user/adminUser!modify.action";
	} 
	var message;
	if(oper==0){
		message = continue_add_lang;
	}else{
		message = whether_edit_lang;
	}
	FT.Dialog.confirm(message,syntax_confirm_lang,function(sel) {
		//判断添加与修改操作oper：1修改；0添加；
		if(oper==1){
			if(sel){
				$("#AddForm").ajaxSubmit({
					async:false,    
					dataType : "json",  
					type:"POST", 
					url : url,
					success:function(msg){
				    	if(msg.errorStr == 'success'){
				    		$.ligerDialog.success(succ_tip_lang, syntax_tip_lang,function(){
				    			location.href = contextPath + '/manager/admin/user/list.jsp?cPage='+cpage;
				    		});
				    	}else{
				    		FT.toAlert(msg.errorStr,msg.object,null);
				    	}
					}
				});
			}
		}else{
			$("#AddForm").ajaxSubmit({
				async:false,    
				dataType : "json",  
				type:"POST", 
				url : url,
				success:function(msg){
					//提示"是否继续添加管理员"，选是，添加成功，停留在添加界面允许继续添加，选否的话则关掉界面
			    	if(msg.errorStr == 'success'){
	    				if(sel) {
		    				location.href = contextPath + '/manager/admin/user/add.jsp?cPage='+cpage;
		    			}else{ // 关闭tab页面
		    				window.parent.removeTabItemF('010101');
		    			}
			    	}else{
			    		FT.toAlert(msg.errorStr,msg.object,null);
			    	}
				}
			});
		}
	  });
}

function selectObj(obj){
	if (obj != -1){
		for (i=0; i<document.getElementById(obj).length; i++){
			document.getElementById(obj).options[i].selected = true;
		}			 
	}
}
/**
 * 返回
 */
function goBack() {
  location.href = contextPath + '/manager/admin/user/list.jsp?cPage='+$("#cPage").val();
}