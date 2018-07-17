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
	if(currTabIndex==1){
		// 设备监控预警
		selectName = "monitorConfInfo.sbrecvusers";
	}else if(currTabIndex==2){
		// 应用系统监控预警
		selectName = "monitorConfInfo.apprecvusers";
	}else{
		// 基本配置
		selectName = "monitorConfInfo.baserecvusers";
	}
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
	if(currTabIndex==1){
		// 设备监控预警
		selectId = "sbrecvusers";
	}else if(currTabIndex==2){
		// 应用系统监控预警
		selectId = "apprecvusers";
	}else{
		// 基本配置
		selectId = "baserecvusers";
	}
	
    var selectedAdmins = $('#'+selectId+" option:selected");
    if(selectedAdmins.length<1){
       FT.toAlert('warn',check_need_del_date_lang,null);
       return;
    }
	$("#"+selectId).children().filter('[selected]').remove();
}    

//认证基本校验
function checkBasicForm(){
	$.formValidator.initConfig({submitButtonID:'monitorSave',debug:true,
		onSuccess:function(){
		  saveObj();
		},
		onError:function(){
		    //验证校验是否通过，并定位到没有通过校验的li
			var inputArray = $("input:text");
           	for(var i=0;i<inputArray.length;i++){//循环整个input数组
                var input =inputArray[i];//取到每一个input
                var ispass = $.formValidator.isOneValid(input.id);
                if (!ispass) {
                	$("#content li").each(function(index){
                		if($(this).has("input[id="+input.id+"]").text()!=""){
                			$("#menu li.tabFocus").removeClass("tabFocus");
			                $("#menu li:eq(" + this.id + ")").addClass("tabFocus");
			                $("#content li:eq(" + this.id + ")").show().siblings().hide();
                		}
                	});
                }
            }
           	
			//FT.toAlert('warn',getLangVal('monitor_conf_failed_1',contextPath),null);
			return false;
	    }
	});
      
	$('#baserecvuser').formValidator({onFocus:please_sel_lang,onCorrect:"OK"}).inputValidator({min:1,onError:please_sel_lang});
	$('#baseremaindays').formValidator({onFocus:tkn_exp_lang,onCorrect:"OK"}).inputValidator({min:7,max:180,type:"number",onError:tkn_exp_err_lang}).regexValidator({regExp:"notempty",dataType:"enum",onError:remove_space_tip_lang}).functionValidator({
	 fun:function(val){
       	 if(!checkNumber(val)) {return tkn_exp_err_lang;}
       	 return true;
       	}
     });
	
	$('#baseunbindlower').formValidator({onFocus:unbound_tkn_lang,onCorrect:"OK"}).inputValidator({min:1,max:100,type:"number",onError:unbound_tkn_err_lang}).regexValidator({regExp:"notempty",dataType:"enum",onError:remove_space_tip_lang}).functionValidator({
	  fun:function(val){
       	 if(!checkNumber(val)) {return unbound_tkn_err_lang;}
       	 return true;
       	}
      });
	 
	$('#basesyncupper').formValidator({onFocus:one_hour_sync_lang,onCorrect:"OK"}).inputValidator({min:1,max:100,type:"number",onError:one_hour_sync_err_lang}).regexValidator({regExp:"notempty",dataType:"enum",onError:remove_space_tip_lang}).functionValidator({
	  fun:function(val){
       	 if(!checkNumber(val)) {return one_hour_sync_err_lang;}
       	 return true;
       	}
      });
	 
	$('#basetimeinterval').formValidator({onFocus:timer_exec_lang,onCorrect:"OK"}).inputValidator({min:30,max:10080,type:"number",onError:timer_exec_err_lang}).regexValidator({regExp:"notempty",dataType:"enum",onError:remove_space_tip_lang}).functionValidator({
	  fun:function(val){
       	 if(!checkNumber(val)) {return timer_exec_err_lang;}
       	 return true;
       	}
      });
	
	
	$('#sbrecvuser').formValidator({onFocus:please_sel_lang,onCorrect:"OK"}).inputValidator({min:1,onError:please_sel_lang}); 
	$('#sbcpuupper').formValidator({onFocus:cpu_upper_lang,onCorrect:"OK"}).inputValidator({min:30,max:100,type:"number",onError:cpu_upper_err_lang}).regexValidator({regExp:"notempty",dataType:"enum",onError:remove_space_tip_lang}).functionValidator({
	  fun:function(val){
       	 if(!checkNumber(val)) {return cpu_upper_err_lang;}
       	 return true;
       	}
      });
	 
	$('#sbdiskupper').formValidator({onFocus:disk_use_upper_lang,onCorrect:"OK"}).inputValidator({min:50,max:100,type:"number",onError:disk_use_upper_err_lang}).regexValidator({regExp:"notempty",dataType:"enum",onError:remove_space_tip_lang}).functionValidator({
	  fun:function(val){
       	 if(!checkNumber(val)) {return disk_use_upper_err_lang;}
       	 return true;
       	}
      });
	 
	$('#sbmemupper').formValidator({onFocus:memory_upper_lang,onCorrect:"OK"}).inputValidator({min:50,max:100,type:"number",onError:memory_upper_err_lang}).regexValidator({regExp:"notempty",dataType:"enum",onError:remove_space_tip_lang}).functionValidator({
	  fun:function(val){
       	 if(!checkNumber(val)) {return memory_upper_err_lang;}
       	 return true;
       	}
      });
	
	$('#sbtimeinterval').formValidator({onFocus:timer_interval_lang,onCorrect:"OK"}).inputValidator({min:1,max:1440,type:"number",onError:timer_interval_err_lang}).regexValidator({regExp:"notempty",dataType:"enum",onError:remove_space_tip_lang}).functionValidator({
	  fun:function(val){
       	 if(!checkNumber(val)) {return timer_interval_err_lang;}
       	 return true;
       	}
      });
        
	$('#apprecvuse').formValidator({onFocus:please_sel_lang,onCorrect:"OK"}).inputValidator({min:1,onError:please_sel_lang});
	$('#apptimeinterval').formValidator({onFocus:timer_interval_lang,onCorrect:"OK"}).inputValidator({min:1,max:1440,type:"number",onError:timer_interval_err_lang}).regexValidator({regExp:"notempty",dataType:"enum",onError:remove_space_tip_lang}).functionValidator({
	  fun:function(val){
       	 if(!checkNumber(val)) {return timer_interval_err_lang;}
       	 return true;
       	}
      });

	$('#baserecvuser').focus();
}   

//修改核心功能配置
function saveObj(){
	selectObj('baserecvusers');
	selectObj('sbrecvusers');
	selectObj('apprecvusers');
    $('#coreForm').ajaxSubmit({
	    type:"post",
	    async:false,
	    url:contextPath+"/manager/monitor/monitor!modify.action",
	    dataType : "json",
	    success:function(msg){
	      if(msg.errorStr=='success'){
	      	   FT.toAlert(msg.errorStr,msg.object,null);
	      	   // 页面刷新
	           $.ligerDialog.success(msg.object,syntax_tip_lang,resetObj);
 		  }else{
	          FT.toAlert(msg.errorStr,msg.object,null);
	      }
	    }
	 });   
}
//重置监控预警策略配置
function resetObj(){
	window.location.href=contextPath+"/manager/monitor/monitor!find.action";
}
    
  