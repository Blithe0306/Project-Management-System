// add.jsp

var contextPath;

$(function(){
	contextPath = $("#contextPath").val();
});

// 打开选择角色对话框
function selCustomers() {
	FT.openWinMiddle('选择客户',contextPath + '/manager/project/sel_customer.jsp',false);
}

// 选择角色完成后将所选角色添加到角色列表
function confirmSelects(jSel) {
//	var rolevals = "";
//    var roleList = $("#custids");
//	
//	$("#custids option").each(function(i,e){
//		rolevals+=e.value+",";
//	});
//	
//	while(jSel[0]) {
//		var role = jSel.shift();
//		if(rolevals.indexOf(role.rid+",")<=-1){
//		   roleList.append("<option value='" + role.rid + "'>" + role.rname + "</option>");
//		}
//	}
	var role = jSel.shift();
	$('#custid').val(role.rid);
	$('#custname').val(role.rname);
}

// 删除角色列表中选定的角色
function delCustomers() {
    var selectedRole = $('#custids option:selected');
    if(selectedRole.length<1){
       FT.toAlert('warn',need_del_date_lang,null);
       return;
    }
	$("#custids").children().filter('[selected]').remove();
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
  location.href = contextPath + '/manager/project/list.jsp?cPage='+$("#cPage").val();
}

function addObj(oper,obj,win){
	var cpage = 1;
	var url = contextPath+"/manager/project/projectAction!add.action";
	if(oper == 1){
	    cpage = $("#cPage").val();
	    url = contextPath+"/manager/project/projectAction!modify.action";
	}
	var message;
	if(oper==0){
		message = continue_add_lang;
	}else{
		message = '是否修改定制项目';
	}
	//判断添加与修改操作oper：1修改；0添加；
	if(oper==1){
		$("#AddForm").ajaxSubmit({
			async:false,    
			dataType : "json",  
			type:"POST", 
			url : url,
			data:{"project.prjdesc": prjdesc.html(),"project.svn":svn.html()},
			success:function(msg){
		    	if(msg.errorStr == 'success'){
		    		$.ligerDialog.success(succ_tip_lang, syntax_tip_lang,function(){
		    			location.href = contextPath + '/manager/project/list.jsp?cPage='+cpage; 
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
			data:{"project.prjdesc": prjdesc.html(),"project.svn":svn.html()},
			success:function(msg){
				//提示"是否继续添加管理员"，选是，添加成功，停留在添加界面允许继续添加，选否的话则关掉界面
		    	if(msg.errorStr == 'success'){
		    		FT.Dialog.confirm(message,syntax_confirm_lang,function(sel) {
	    				if(sel) {
		    				location.href = contextPath+'/manager/project/projectAction!toAddProject.action';
		    			}else{ // 关闭tab页面
		    				window.parent.removeTabItemF('030002');
		    			}
	    			});
		    	}else{
		    		FT.toAlert(msg.errorStr,msg.object,null);
		    	}
			}
		});
	}
}