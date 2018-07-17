 
var contextPath;
$(function(){
	contextPath = $("#contextpath").val() + "/";
	
})
 
//初始化对应关系列表
 function loadFieldMapping(){
   if(isEdit){ //编辑
		    var columnname = $("#mapingAttr").val(); 
			if(columnname!==""){
				var arr_columnname = columnname.split(",");
				for(var i=0;i<arr_columnname.length;i++){
					var option_mappings=arr_columnname[i].split(":");
					var option_mapping = "<option value='"+option_mappings[0]+":"+option_mappings[1]+"'>"+getSelectTextByValue(option_mappings[0])+"----&gt; "+option_mappings[1]+"</option>";
					$(option_mapping).appendTo($("#fieldMapping"));
				}
			}
		}else{
		  if(usType=='1'){  //LDAP
		     $("#ldap_account_attr").val("sAMAccountName");
		     //addLdapFieldMapping();
		  }
		  
		}
	}
	
//检查字段对应关系是否存在
function checkUsdrIdExist(fieldName){
	var exist=false;
	$("#fieldMapping option").each(function(){
		var optvalue=$(this).val().split(":");
		if(optvalue[0]==fieldName){
			exist=true;
		}
	});
	
	return exist;
}
 
 //移除字段对应关系
function removefieldMapping(){
	var len = $("#fieldMapping option").length;
	var otpselected = $('#fieldMapping option:selected').val();
	if(otpselected == undefined || otpselected==''){
	    FT.toAlert('warn',del_relation_lang, null);
	    return false;
	}
	var optvalue=otpselected.split(":");
	var canRemove=true;
 	if(optvalue[0]=="userid" && len>1){
 		canRemove=false;
 	}
 	if(canRemove){
 		$('#fieldMapping option:selected').remove();
 		$("#mapingAttr").val("");
		setJsonFieldMapping();
 	}else{
 	   FT.toAlert('warn',others_relation_lang, null);
 	}
}
	
	 
//构造对应关系
function setJsonFieldMapping(){
	var num = 0;
	var fmString="";
	$("#fieldMapping option").each(function(){
		 	if(num>0){
		 		fmString = fmString+",";
		 	}
		 	fmString = fmString+$(this).val();    
		 	num++;
	});
	$("#mapingAttr").val(fmString);
}
	
//由下拉框的字际值得到显示值
function getSelectTextByValue(select_value){
	  var select_text="";
	  $("#localuserattr option").each(function(){
	  var this_val=$(this).val().split(":");
	  var select_val = select_value.split(":");
		  if(this_val[0]==select_val[0]){select_text=  $(this).text();}
	  });
	  return select_text;
}		
		
 //手动更新
 function manuallyUpdate(id,usName,usType){
     var ajaxbg = $("#background,#progressBar");//加载等待
     ajaxbg.show();
     setTimeout(function(){
     $.ajax({
	   type: "POST",
	   dataType : "json",
	   url: contextPath + "manager/confinfo/usersource/usConf!manuallyUpdate.action",
	   data: "usId="+id+"&usName="+usName+"&usType="+usType,
	   success: function(msg){
	   			ajaxbg.hide();	
	     	    if(msg.errorStr == 'success'){
				    FT.toAlert(msg.errorStr,msg.object, null);
				}else{
				    FT.toAlert(msg.errorStr,msg.object, null);
				} 
			
	       },
		error:function(msg){
			  ajaxbg.hide();	  
			  FT.toAlert('error',update_err_lang, null);
		  }
	  });
   }, 1);
 }	
 
 
 
//测试数据源连接
function testUSConn(){
      var checkUSNameUrl = contextPath + "manager/confinfo/usersource/usConf!testConnection.action";
	  var ajaxbg = $("#background,#progressBar");//加载等待
        ajaxbg.show();
        setTimeout(function(){
      	   $("#usForm").ajaxSubmit({
		   async:false,  
		   type:"POST",
		   dataType : "json", 
		   url : checkUSNameUrl,
		   success:function(msg){
		  
		   ajaxbg.hide();	
				if('success'== msg.errorStr){
					FT.toAlert('success',succ_tip_lang, null);
				}else{
				    FT.toAlert('error',error_tip_lang, null);
				}
			}
		});	
   }, 1);
} 
 
		
 //测试
function testUSConnPreNext(){
     var flag = true;
     var checkUSNameUrl = contextPath + "manager/confinfo/usersource/usConf!testConnection.action";
     $("#usForm").ajaxSubmit({
	   async:false,  
	   type:"POST",
	   dataType : "json", 
	   url : checkUSNameUrl,
	   success:function(msg){
			if('success'==msg.errorStr){
				  flag = true;
			}else{
			      flag = false;
			}
		}
	});	
	return flag;	
} 
 
 

 //保存用户来源配置
 function saveUS(){
    var url ="";
    var id= $('#id').val();
    var usName = $('#sourcename').val();
    var usType = $('#sourcetype').val();
    
    var usid;
    
     if(isEdit){
	     url = contextPath + "manager/confinfo/usersource/usConf!modify.action";
	 }else{
		 url = contextPath + "manager/confinfo/usersource/usConf!add.action";
	 }
        var ajaxbg = $("#background,#progressBar");//加载等待
        ajaxbg.show();
        setTimeout(function(){
           $("#usForm").ajaxSubmit({
		   async:false,  
		   type:"POST",
		   dataType : "json", 
		   url : url,
		   success:function(msg){
				 if(msg.errorStr == 'success'){
				   if(!isEdit) {
					   $('#usid').val(msg.object);
				   }
				   usid = $('#usid').val();
				  //更新用户来源
				     $.ajax({
						   type: "POST",
						   dataType : "json",
						   url: contextPath + "manager/confinfo/usersource/usConf!manuallyUpdate.action",
						   data: "usId="+usid+"&usName="+usName+"&usType="+usType,
						   success: function(mesg){
							    if(mesg.errorStr == 'success'){
							          ajaxbg.hide();	
									  $.ligerDialog.confirm(conf_succ_lang+mesg.object+timing_task_lang, function (yes){
										     if(yes){
											 	//  stepController(3);
											   FT.openWinMiddle(timing_config_lang,contextPath + "manager/confinfo/usersource/usConf!view.action?userSourceInfo.id="+usid+"&userSourceInfo.sourcetype="+usType+"&flag=1&isEdit="+isEdit, false);   	
										     }else{
										       location.href = contextPath + "manager/confinfo/usersource/list.jsp";
										     }
									   });   
								}else{
								    ajaxbg.hide();	  
								    FT.toAlert(mesg.errorStr,mesg.object, null);
								} 
								  
						     },
						     error:function(mesg){
						            ajaxbg.hide();	  
						            FT.toAlert('error',oper_err_lang, null);
						     }
						  });
					
				}else{
				       FT.toAlert('error',save_conf_err_lang, null);
				       ajaxbg.hide();
				}
			}
		});				
       }, 1);	
	}