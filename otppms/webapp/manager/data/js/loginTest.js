
	//d登录结果
	var loginResult="";
    var alertMessage="";
    
	//测试远程连接
    function loginTest(){   
    	login(); 	
    	FT.toAlert(loginResult,alertMessage,null);
    	//if(loginResult=="success"){ //如果远程登录成功 择显示 保存按钮
    		//$("#addBtn").show();
    	//}
    }    
   
   //测试远程连接
    function login(){    	
    	$("#AddForm").ajaxSubmit({
			async:false,    
			dataType : "json",  
			type:"POST", 
			url : $("#contextPath").val() + "/manager/data/databak!loginTest.action",
			success:function(msg){
				 loginResult=msg.errorStr;
				 alertMessage=msg.object;
			     //FT.toAlert(msg.errorStr,msg.object,null);
			}
		});
    }
    
    
    //跳转页
    function linkPage(source){
        location.href=$("#contextPath").val() + "/manager/data/databak!find.action?source="+source;    	
    }
    