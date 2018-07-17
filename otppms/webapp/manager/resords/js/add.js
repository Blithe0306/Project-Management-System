// add.jsp

var contextPath;

$(function(){
	contextPath = $("#contextPath").val();
});

// 打开选择角色对话框
function selResords() {
	FT.openWinMiddle('选择项目名称',contextPath + '/manager/resords/sel_resords.jsp',false);
}
    
// 选择角色完成后将所选角色添加到角色列表
function confirmSelects(jSel) {
//	var rolevals = "";
//    var roleList = $("#prjid");
//	
//	$("#prjids option").each(function(i,e){
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
	$('#prjid').val(role.rname);
//	$('#prjname').val(role.rname);
}

// 删除角色列表中选定的角色
function delResords() {
    var selectedRole = $('#prjids option:selected');
    if(selectedRole.length<1){
       FT.toAlert('warn',need_del_date_lang,null);
       return;
    }
	$("#prjids").children().filter('[selected]').remove();
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
  location.href = contextPath + '/manager/resords/list.jsp?cPage='+$("#cPage").val();
}

function addObj(oper,win){
	var cpage = 1;
	var url = contextPath+"/manager/resords/resordsAction!add.action";
	if(oper == 1){
	    cpage = $("#cPage").val();
	    url = contextPath+"/manager/resords/resordsAction!modify.action";
	}
	var message;
	if(oper==0){
		message = continue_add_lang;
	}else{
		message = '保存成功！';
	}
	
	
	if($('#recordtimeStr').val() > $('#endrecordtimeStr').val()){
		FT.toAlert('warn','上门开始时间应小于上门结束时间！');
		return;
	}
	
	//判断添加与修改操作oper：1修改；0添加；
	$("#AddForm").ajaxSubmit({
		async:false,    
		dataType : "json",  
		type:"POST", 
		url : url,
		success:function(msg){
	    	if(msg.errorStr == 'success'){
	    		if(oper == 1){		// 更新
					$.ligerDialog.success(succ_tip_lang, syntax_tip_lang,function(){
		    			location.href = contextPath + '/manager/resords/list.jsp?cPage='+$("#cPage").val();
		    		});
				}else{				// 新增
					FT.Dialog.confirm(message,syntax_confirm_lang,function(sel) {
	    				if(sel) {
		    				location.href = contextPath + '/manager/resords/resordsAction!toAddResords.action'; 
		    			}else{ // 关闭tab页面
		    				window.parent.removeTabItemF('030302');
		    			}
    				});
				}
	    	}else{
	    		FT.toAlert(msg.errorStr,msg.object,null);
	    	}
		}
	});
	
}