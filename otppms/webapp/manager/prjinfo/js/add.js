// add.jsp

var contextPath;

$(function(){
	contextPath = $("#contextPath").val();
})

// 打开选择定制项目对话框
function selProjects() {
	FT.openWinMiddle('选择定制项目',contextPath + '/manager/prjinfo/sel_project.jsp',false);
}
    
// 选择角色完成后将所选角色添加到角色列表
function confirmSelects(jSel) {
//	var rolevals = "";
//    var roleList = $("#prjids");
//	
//	$("#prjids option").each(function(i,e){
//		rolevals+=e.value+",";
//	});
//	 
//	while(jSel[0]) {
//	
//		var role = jSel.shift();
//		if(rolevals.indexOf(role.rid+",")<=-1){
//		   roleList.append("<option value='" + role.rid + "'>" + role.rname + "</option>");
//		}
//	}
	var role = jSel.shift();
	$('#prjid').val(role.rid);
	$('#prjname').val(role.rname);
}

// 删除角色列表中选定的角色
function delProjects() {
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
  location.href = contextPath + '/manager/prjinfo/list.jsp?cPage='+$("#cPage").val();
}

function addObj(oper,obj,win){
	var cpage = 1;
	var url = contextPath+"/manager/prjinfo/prjinfoAction!add.action";
	if(oper == 1){
	    cpage = $("#cPage").val();
	    url = contextPath+"/manager/prjinfo/prjinfoAction!modify.action";
	}
	var message;
	if(oper==0){
		message = continue_add_lang;
	}else{
		message = '是否修改定制信息';
	}
	//判断添加与修改操作oper：1修改；0添加；
	if(oper==1){
		$("#AddForm").ajaxSubmit({
			async:false,    
			dataType : "json",  
			type:"POST", 
			url : url,
			data:{"prjinfo.content": content.html(),"prjinfo.path": path.html(),"prjinfo.svn": svn.html()},
			success:function(msg){
		    	if(msg.errorStr == 'success'){
		    		$.ligerDialog.success(succ_tip_lang, syntax_tip_lang,function(){
		    			location.href = contextPath + '/manager/prjinfo/list.jsp?cPage='+cpage; 
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
			data:{"prjinfo.content": content.html(),"prjinfo.path": path.html(),"prjinfo.svn": svn.html()},
			success:function(msg){
				//提示"是否继续添加管理员"，选是，添加成功，停留在添加界面允许继续添加，选否的话则关掉界面
		    	if(msg.errorStr == 'success'){
		    		FT.Dialog.confirm(message,syntax_confirm_lang,function(sel) {
	    				if(sel) {
		    				location.href = contextPath+'/manager/prjinfo/prjinfoAction!toAddPrjinfo.action';
		    			}else{ // 关闭tab页面
		    				window.parent.removeTabItemF('030202');
		    			}
	    			});
		    	}else{
		    		FT.toAlert(msg.errorStr,msg.object,null);
		    	}
			}
		});
	}
}