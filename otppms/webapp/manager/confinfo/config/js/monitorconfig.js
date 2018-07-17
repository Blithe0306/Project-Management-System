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
	// 基本配置
	var selectName = "monitorConfInfo.baserecvusers";
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
	var	selectId = "baserecvusers";
	
    var selectedAdmins = $('#'+selectId+" option:selected");
    if(selectedAdmins.length<1){
       FT.toAlert('warn',need_del_date_lang,null);
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
			return false;
	    }
	});
      
	$('#baserecvusers').formValidator({onFocus:please_sel_lang,onCorrect:"OK"}).functionValidator({
		fun:function() {
		if($('#baserecvusers option').length < 1) {
			return warning_receiver_lang;
		}
		return true;
	}});
	
	
	$('#baseremaindays').formValidator({onFocus:tkn_exp_lang,onCorrect:"OK"}).inputValidator({min:7,max:180,type:"number",onError:tkn_exp_err_lang}).regexValidator({regExp:"notempty",dataType:"enum",onError:remove_space_lang}).functionValidator({
	 fun:function(val){
       	 if(!checkNumber(val)) {return tkn_exp_err_lang;}
       	 return true;
       	}
     });
	
	$('#baseunbindlower').formValidator({onFocus:unbound_tkn_lang,onCorrect:"OK"}).inputValidator({min:1,max:100,type:"number",onError:unbound_tkn_err_lang}).regexValidator({regExp:"notempty",dataType:"enum",onError:remove_space_lang}).functionValidator({
	  fun:function(val){
       	 if(!checkNumber(val)) {return unbound_tkn_err_lang;}
       	 return true;
       	}
      });
	 
	$('#basesyncupper').formValidator({onFocus:tkn_one_hour_lang,onCorrect:"OK"}).inputValidator({min:1,max:100,type:"number",onError:one_hour_sync_err_lang}).regexValidator({regExp:"notempty",dataType:"enum",onError:remove_space_lang}).functionValidator({
	  fun:function(val){
       	 if(!checkNumber(val)) {return one_hour_sync_err_lang;}
       	 return true;
       	}
      });
	
	$('#baserecvuser').focus();
}   

//修改核心功能配置
function saveObj(){
	selectObj('baserecvusers');
	var ajaxbg = $("#background,#progressBar");//加载等待
	ajaxbg.show();
    $('#monitorForm').ajaxSubmit({
	    type:"post",
	    async:true,
	    url:contextPath+"/confinfo/config/monitorconf!modify.action",
	    dataType : "json",
	    success:function(msg){
	      if(msg.errorStr=='success'){
	      	   FT.toAlert(msg.errorStr,msg.object,null);
	      	   // 页面刷新
	           $.ligerDialog.success(msg.object,syntax_tip_lang,resetObj);
 		  }else{
	          FT.toAlert(msg.errorStr,msg.object,null);
	      }
	      ajaxbg.hide();
	    }
	 });   
}
//重置监控预警策略配置
function resetObj(){
	window.location.href=contextPath+"/confinfo/config/monitorconf!find.action";
}
    
  