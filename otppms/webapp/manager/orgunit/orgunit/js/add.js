var contextPath;

$(function(){
	contextPath = $("#contextPath").val();
})

//oper 0表示添加 1表示修改
function addObj(oper,obj){
	var ajaxbg = $("#background,#progressBar");//加载等待
	ajaxbg.show();
	selectObj(obj);//选中复选框中的每一项 否则后台获取是空
	var url = contextPath + "/manager/orgunit/orgunit/orgunitInfo!add.action";
	if(oper == 1){
	    url = contextPath + "/manager/orgunit/orgunit/orgunitInfo!modify.action";
	}
	var hidname = $("#hidname").val();
		$("#AddForm").ajaxSubmit({
			async:true,
			data: {"oldname" : hidname},    
			dataType : "json",  
			type:"POST", 
			url : url,
			success:function(msg){
				 ajaxbg.hide();
			     if(msg.errorStr == 'success'){
			     	 //添加、编辑完 之后 对左边数据进行修改或添加
			         var frames = window.top.parent.document.getElementById("leftFunction");
			     	 frames.contentWindow.addOrg(msg.object.split(",")[1],msg.object.split(",")[2],msg.object.split(",")[3]);
			     
			         if(oper==1){ //如果是修改完毕
			         	$.ligerDialog.success(msg.object.split(",")[0], syntax_tip_lang, function(){
				         	//跳转到展示本次添加的机构信息
				         	 var id=msg.object.split(",")[1];
				         	 var flag=msg.object.split(",")[2];
				         	 //var readWriteFlag=msg.object.split(",")[4];
					         //window.location=contextPath +"/manager/orgunit/orgunit/orgunitInfo!view.action?treeOrgunitInfo.id="+id+"&treeOrgunitInfo.flag="+flag+"&treeOrgunitInfo.readWriteFlag="+readWriteFlag;
					         //调用一下选中事件，右侧刷新
					         frames.contentWindow.selectOperNode(id,flag);
					    });   
			         }else{
				         FT.Dialog.confirm(whether_continue_lang, '',function(r){ 
				         	if(r==1){
				         		$("#name").val("");
					         	$("#mark").val("");
					         	$("#descp").val("");
					         	$("#admins").children().filter('[selected]').remove();
				         	}else{
				         		 //跳转到展示本次添加的机构信息
				         		 var viewFlag = true;
				         		 var readWriteFlag;
				         		 var id=msg.object.split(",")[1];
					         	 var flag=msg.object.split(",")[2];
					         	 /**if($('#l_userid_role').val() != 'ADMIN'){
					         	 	 var adminvals = "";
						         	 $("select[name='treeOrgunitInfo.admins'] option").each(function(i,e){
										adminvals+=e.value+",";
									 });
									 adminvals = adminvals.substring(0,adminvals.length-1);
									 for(var i=0; i<adminvals.split(",").length; i++){
										adminid = adminvals.split(",")[i];
										if(adminid == $('#l_userid').val()){
											viewFlag = false;
											break;
										}
									 }
									 if(viewFlag){
										readWriteFlag=1; // 查看
									 }else{
										readWriteFlag=2; // 可操作按钮
									 }
					         	 }else{
					         	 	readWriteFlag=2; // 可操作按钮
					         	 }
				         		 window.location=contextPath +"/manager/orgunit/orgunit/orgunitInfo!view.action?treeOrgunitInfo.id="+id+"&treeOrgunitInfo.flag="+flag+"&treeOrgunitInfo.readWriteFlag="+readWriteFlag;
				         		 if(readWriteFlag == 1){
			         				frames.contentWindow.reLoadTree();
			        	 		 }**/
			        	 		 
			        	 		 //调用一下选中事件，右侧刷新
					         	frames.contentWindow.selectOperNode(id,flag);
				         	 }
				         });
			         }
			         
			        // 完之后 调用initOrgunitTree.jsp左边测显示
			        // var frames=window.parent.window.parent.window.document.getElementById("leftFunction");
			        // frames.contentWindow.addOrg(msg.object.split(",")[1],msg.object.split(",")[2],msg.object.split(",")[3]);
			     }else{
				        FT.toAlert(msg.errorStr,msg.object,null);
			     }
			}
		});
}

//选中对象
function selectObj(obj){
	if (obj != -1){
	  for (i=0; i<document.getElementById(obj).options.length; i++){
		document.getElementById(obj).options[i].selected = true;	
	  }		 
	}
}
  
// 打开选择管理员对话框
function selAdmins() {
	FT.openWinMiddle(sel_admin_lang,contextPath + '/manager/orgunit/orgunit/sel_admin.jsp',true);
}
    
// 选择管理员完成后将所选管理员添加到管理员列表
function confirmSelects(jSel) {
    var adminList = $("select[name='treeOrgunitInfo.admins']");
	var adminvals = "";
	$("select[name='treeOrgunitInfo.admins'] option").each(function(i,e){
		adminvals+=e.value+",";
	});
	while(jSel[0]) {
		var adminUser = jSel.shift();
		
		// 此处要加结尾判断 要不然名字相识则不能选择了
		if(adminvals.indexOf(adminUser.adminId+",")<=-1){
		   adminList.append("<option value='" + adminUser.adminId + "'>" + adminUser.adminId + "</option>");
		}
		
	}
}


// 删除管理员列表中选定的管理员
function delAdmins() {
    var selectedAdmins = $('#admins option:selected');
    if(selectedAdmins.length<1){
       FT.toAlert('warn',need_del_date_lang,null);
       return;
    }
	$("#admins").children().filter('[selected]').remove();
} 