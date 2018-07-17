var contextPath;

$(function(){
	contextPath = $("#contextPath").val();
})

function selectObj(obj){
	if (obj != -1){
	 for (i=0; i<document.getElementById(obj).options.length; i++)
		document.getElementById(obj).options[i].selected = true;			 
	}
}

// 打开选择管理员对话框
function selAdmins() {
	FT.openWinMiddle(sel_admin_list_lang,contextPath + '/manager/orgunit/domain/sel_admin.jsp',false);
}
    
// 选择管理员完成后将所选管理员添加到管理员列表
function confirmSelects(jSel) {
	var selectName = "";
	// 基本配置
	selectName = "monitorConfig.adminId";
    var adminList = $("select[name='"+selectName+"']");
	var adminvals = "";
	$("select[name='"+selectName+"'] option").each(function(i,e){
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
	var selectId = "";
	// 基本配置
	selectId = "adminId";
    var selectedAdmins = $('#'+selectId+" option:selected");
    if(selectedAdmins.length<1){
       FT.toAlert('warn',check_need_del_date_lang,null);
       return;
    }
	$("#"+selectId).children().filter('[selected]').remove();
}    


//重置监控预警策略配置
function resetObj(){
	window.location.href=contextPath+"/confinfo/config/monitorconfig!find.action";
}