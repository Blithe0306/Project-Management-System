// add/edit role javascript

var contextPath;

$(function() {
	contextPath = $("#contextPath").val();

	$("#roleName").keydown(function(e) {
		if(e.keyCode == '13') {
			return false;
		}
	});
})

//添加角色
function addObj(oper,obj){
	var form = document.getElementById('roleForm');
	///document.getElementById('bottomFrame').contentWindow.getChecked();
	selectObj(obj);
	if(document.getElementById(obj).length==0){
	  FT.toAlert('warn',only_one_lang, null);
	  return;
	}
	if(oper == 'add'){
			var url = contextPath +"/manager/admin/role/adminRole!add.action";
			$("#roleForm").ajaxSubmit({
				async : false,
				type : "POST", 
				url : url,
				dataType : "json",
				success : function(data){
					if(data.errorStr == 'success'){	            			
	            			 FT.Dialog.confirm(continue_add_lang, syntax_confirm_lang, function(sel){
	            			 	if(sel) {
				    				location.href = contextPath + "/manager/admin/role/add.jsp?source=top";
				    			}else{ // 关闭tab页面
				    				window.parent.removeTabItemF('010301');
				    			}
	            			 });
	            		}else {
							FT.toAlert(data.errorStr, data.object, null);
	            		}
					}
			});	
	     
	}else {	// 编辑
		  var hidrolename = $("#hidroleName").val();
	      var url = contextPath + "/manager/admin/role/adminRole!modify.action";
		  $("#roleForm").ajaxSubmit({
				async:false,      
				type:"POST", 
				url : url,
				data: {"hidrolename" : hidrolename},
				dataType:"json",
				success:function(msg){
				    if(msg.errorStr=='error'){
				         FT.toAlert(msg.errorStr,msg.object, null);
				    }else{
				      $.ligerDialog.success(msg.object, syntax_tip_lang, function(){
	            	     location.href = contextPath + "/manager/admin/role/list.jsp?currentPage=" + $("#currentPage").val();
	            	  });
				  }
				}
			});	
	}
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
	    location.href = contextPath + '/manager/admin/role/list.jsp?currentPage=' + currentPage;
}