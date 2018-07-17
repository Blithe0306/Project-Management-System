// add.jsp

var contextPath;

$(function(){
	contextPath = $("#contextPath").val();
})

//oper 0表示添加 1表示修改
function addObj(oper,obj){
	selectObj(obj);
	var cpage = 1;
	var url = contextPath + "/manager/orgunit/domain/domainInfo!add.action";
	if(oper == 1){
	    cpage = $("#cPage").val();
	    url = contextPath + "/manager/orgunit/domain/domainInfo!modify.action";
	} 
	var hidname = $("#hiddenDomainName").val();
		$("#AddForm").ajaxSubmit({
			async:false, 
			data: {"oldDname" : hidname},    
			dataType : "json",  
			type:"POST", 
			url : url,
			success:function(msg){
			     if(msg.errorStr == 'success'){
			         $.ligerDialog.success(save_succ_lang, syntax_tip_lang,function(){
			         	location.href = contextPath + '/manager/orgunit/domain/list.jsp?cPage='+cpage;
			         });
			         //完之后 调用initOrgunitTree.jsp左边测显示
			         var frames=window.parent.window.document.getElementById("leftFunction");
			         frames.contentWindow.addOrg(msg.object.split(",")[1],msg.object.split(",")[2],msg.object.split(",")[3]); 
			     }else{
				        FT.toAlert(msg.errorStr,msg.object,null);
			     
			     }
			}
		});
}

function selectObj(obj){
	if (obj != -1){
	 for (i=0; i<document.getElementById(obj).options.length; i++)
		document.getElementById(obj).options[i].selected = true;			 
	}
}

// 打开选择管理员对话框
function selAdmins() {
	FT.openWinMiddle(sel_admin_lang,contextPath + '/manager/orgunit/domain/sel_admin.jsp',true);
}
    
// 选择管理员完成后将所选管理员添加到管理员列表
function confirmSelects(jSel) {
    var adminList = $("select[name='domainInfo.adminIds']");
	var adminvals = "";
	$("select[name='domainInfo.adminIds'] option").each(function(i,e){
		adminvals+=e.value+",";
	});
	while(jSel[0]) {
		var adminUser = jSel.shift();
		if(adminvals.indexOf(adminUser.adminId)<=-1){
		   adminList.append("<option value='" + adminUser.adminId + "'>" + adminUser.adminId + "</option>");
		}
		
	}
}


// 删除管理员列表中选定的管理员
function delAdmins() {
    var selectedAdmins = $('#adminIds option:selected');
    if(selectedAdmins.length<1){
       FT.toAlert('warn',need_del_date_lang,null);
       return;
    }
	$("#adminIds").children().filter('[selected]').remove();
}    
    
/**
 * 返回
 */
function goBack() {
  location.href = contextPath + '/manager/orgunit/domain/list.jsp?cPage='+$("#cPage").val();
}  