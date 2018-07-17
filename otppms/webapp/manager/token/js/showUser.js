var contextPath = $("#contextPath").val();
var sessionId = $("#sessionId").val();
	//查看用户
	function viewUser(id,domainId){
		//  管理员
		if(domainId == ""){
			window.location.href=contextPath+"/manager/admin/user/adminUser!view.action?adminUser.adminid=" + replaceUserId(id);
		}else{ // 用户
			window.location.href=contextPath+"/manager/user/userinfo/userInfo!view.action?userInfo.userId=" + replaceUserId(id)+"&userInfo.domainId="+domainId;
		}
	}