// add/edit role javascript

var contextPath;

$(function() {
	contextPath = $("#contextPath").val();

	$("#userId").keydown(function(e) {
		if(e.keyCode == '13') {
			return false;
		}
	});
})


// 回退操作
function goBack() {
	var currentPage = $("#currentPage").val();
	    location.href = contextPath + '/manager/user/userinfo/list.jsp';
}




//机构变更 执行操作
function changeOrg(){
	var oldOrgunitIds=$("#orgunitIds").val();
	var newOrgunitIds=$("#orgunitIdsTemp").val();
	if(oldOrgunitIds!=newOrgunitIds){
		var userId=$("#userId").val();
	    var unBindTag=0;
	    var isExsitUnBingTag=$("#isExsitUnBingTag").val(); //是否存在解绑项
	    if(isExsitUnBingTag==1){//存在解绑项
			if($("#unBindTag").attr("checked")==true){ //必须解绑
				unBindTag=1;
				updateObj(unBindTag);
		    }else{
		    	FT.toAlert("warn",sel_unbind_lang,null);
				return false;
		    }
	    }else{ //不存在解绑项
	    	updateObj(unBindTag);
	    }
	    
	}else{
		FT.toAlert("warn",no_same_lang,null);
		return false;
	}
}


//机构变更
function updateObj(unBindTag){
	var changeTokenOrgTag=0;
	if($("#changeTokenOrgTag").attr("checked")==true){
		changeTokenOrgTag=1;
	}
	$("#AddForm").ajaxSubmit({
			async:true,    
			dataType : "json",  
			type:"POST", 
			url : contextPath + "/manager/user/userInfo/userInfo!changeOrgunit.action?unBindTag="+unBindTag+"&changeTokenOrgTag="+changeTokenOrgTag,
			success: function(msg){
			     if(msg.errorStr == 'success'){
			         $.ligerDialog.success(msg.object, syntax_tip_lang,function(){
			         	goBack();//返回列表页
			         });
			         
			     }else{
				     FT.toAlert(msg.errorStr,msg.object,null);
			     }
			}
		});
    
}

