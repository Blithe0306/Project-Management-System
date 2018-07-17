var cPath;
$(function() {
	cPath = $("#contextPath").val();
	cPath += "/";
});

//添加
function addchild(){
	var crrentOrgId=$("#id").val();
   	var flag=$("#flag").val();
   	location.href=cPath + 'manager/orgunit/orgunit/orgunitInfo!find.action?treeOrgunitInfo.id='+crrentOrgId+'&treeOrgunitInfo.flag='+flag+'&source=add';
}
 //编辑
function editObj(){
	var crrentOrgId=$("#id").val();
	var flag=$("#flag").val();
	location.href=cPath + 'manager/orgunit/orgunit/orgunitInfo!find.action?treeOrgunitInfo.id='+crrentOrgId+'&treeOrgunitInfo.flag='+flag+'&source=edit';
}

 //迁移机构
function moveObj(){
	var crrentOrgId=$("#id").val();
	var flag=$("#flag").val();
	location.href=cPath + 'manager/orgunit/orgunit/orgunitInfo!find.action?treeOrgunitInfo.id='+crrentOrgId+'&treeOrgunitInfo.flag='+flag+'&source=move';
}

// 删除
function delData() {
	var ajaxbg = $("#background,#progressBar");//加载等待
	var isdefault=$("#isdefault").val();
	if(isdefault==1){ //默认域不能删除
		FT.toAlert('warn', del_default_lang,null);
	}else{
		FT.Dialog.confirm(conf_del_lang, syntax_tip_lang, function(r){
			if(r==1){ //
				ajaxbg.show();
				var crrentOrgId=$("#id").val();
				var flag=$("#flag").val();
				var domainId=$("#domainId").val();
				
				//删除 所选 
				$.post(
				 	cPath + 'manager/orgunit/orgunit/orgunitInfo!delete.action', //第一个参数 url
				    {crrentOrgId:crrentOrgId,flag:flag,domainId:domainId},  //第二个参数 传的参数对 key/value 传的参数名和参数值
				    function(result){
			           if(result.errorStr == 'success'){
			           		ajaxbg.hide();	            			
				       		$.ligerDialog.success(result.object, syntax_tip_lang, function(){
				       			
				       			var frames = window.top.parent.document.getElementById("leftFunction");
				       		
					       		var treeManager = frames.contentWindow.manager;
					       		// 获取将要删除节点的父节点
					       		var currNodeObj = treeManager.getSelected();
					       		var parentNote = treeManager.getParentTreeItem(currNodeObj.target);
					       		
								frames.contentWindow.deleteNode(crrentOrgId,2);
								
								treeManager.selectNode(parentNote);
								var parentData = treeManager.getSelected();
							
					        });
				       }else {
				       		ajaxbg.hide();
				       		FT.toAlert(result.errorStr, result.object, null);
				       }
				      // query(true);
			        }, //第三个参数 成功调用的函数
			        'json' //第四个参数 是返回值类型
			    );
		    }
		});     
	}	
}
