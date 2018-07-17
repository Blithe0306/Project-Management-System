<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title></title>
	<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
	<link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
	<link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
		
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script> 
	<script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/confinfo/usersource/js/usersource.js" charset="UTF-8"></script> 	
	<script language="javascript" type="text/javascript">
	// Start,多语言提取
	var del_relation_lang = '<view:LanguageTag key="usource_no_sel_del_relation"/>';
	var others_relation_lang = '<view:LanguageTag key="usource_del_others_relation"/>';
	var update_err_lang = '<view:LanguageTag key="usource_oper_update_err"/>';
	var succ_tip_lang = '<view:LanguageTag key="common_conn_succ_tip"/>';
	var error_tip_lang = '<view:LanguageTag key="common_conn_error_tip"/>';
	var conf_succ_lang = '<view:LanguageTag key="usource_save_conf_succ"/>';
	var timing_task_lang = '<view:LanguageTag key="usource_is_set_timing_task"/>';
	var timing_config_lang = '<view:LanguageTag key="usource_timing_config"/>';
	var oper_err_lang = '<view:LanguageTag key="usource_save_oper_err"/>';
	var save_conf_err_lang = '<view:LanguageTag key="usource_save_conf_err"/>';
	// End,多语言提取
	
	var isEdit= false; //isEdit-true编辑  isEdit-false添加
	var setwin;
	$(document).ready(function(){
		//更新用户选项，点击"更新用户"按钮
	   isEdit = '${param.isEdit}';
	   
       $.formValidator.initConfig({submitButtonID:'okBtn',
			onSuccess:function(){
			    updateUserInfo();
			},
			onError:function(){
				return false;
			}
		}); 
			 
		//按天设置
	    $("#taskhour").formValidator({tipID:"hourTip",onShow:'<view:LanguageTag key="usource_vd_task_hour"/>',onFocus:'<view:LanguageTag key="usource_vd_task_hour_err"/>',onCorrect:"OK"}).functionValidator({
	    fun:function(taskhour){
		     var taskmode1 =  $("#taskmode1").find("option:selected").val();
		     var enabled = document.getElementById('enabled');	
		     if(enabled.checked){//启用定时更新
		        if(taskmode1 == 2){ //按周
		           return true;
		        }
                var taskmode2 =  $("input[name='userSourceInfo.taskInfo.taskmode2']:checked").val();
		        if(taskmode2=='1'){
		           if($('#taskhour').val()==''||$('#taskhour').val()==null){
		               return '<view:LanguageTag key="usource_vd_task_hour"/>';
		           }
		        }
		     }
	   		 return true;
	    }});
	   
		//按周设置
		$('input[name="weekAttr[]"]').formValidator({tipID:"taskweekTip",onShow:'<view:LanguageTag key="usource_vd_task_hour_err"/>',onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).functionValidator({
	   		fun:function(week){
		        var enabled = document.getElementById('enabled');	
		        var settype = $("#taskmode1").find("option:selected").val();
		        if(enabled.checked){
		           if(settype==1){
		              return true;
		           }else{
		           	   var checkval = getCheckedVal();
		               if(checkval == null || checkval==''){
		                  return '<view:LanguageTag key="usource_vd_task_hour_err"/>';
		               } 
		           }
		        }
	       		return true;      
	   }}); 
	   
	   if('${userSourceInfo.taskInfo.enabled}'!=null&&'${userSourceInfo.taskInfo.enabled}'!=''&&'${userSourceInfo.taskInfo.enabled}'==0){
		   // 编辑 且未启用定时更新
		   checkState(false);
	   }else{
		   checkState(true);
	   }
	   
	   if(isEdit){//编辑
			//设置方式
	    	setTypeChange('${userSourceInfo.taskInfo.taskmode1}');
	    	//按天设置的类型（按每n小时/按每n分钟）
	     	changeTaskMode2('${userSourceInfo.taskInfo.taskmode2}');
	    	//编辑状态下按周,周被选中状态
	    	setWeekSelected('${userSourceInfo.taskInfo.taskweek}');
	   }
	});
	//获取选择周
	function getCheckedVal() {
		var checkedVal = "";
		$('input[name="weekAttr[]"]').each(function () {
	       if ($(this).attr('checked') == true) {
	          checkedVal += $(this).attr('value')+",";
	       }
	    });
	    return checkedVal;
	}
	
 	//选择设置类型
 	function setTypeChange(obj){
 	  if(obj=='1'){ //按天设置置
 	  	 var taskmode2 =  $("input[name='userSourceInfo.taskInfo.taskmode2']:checked").val();
	     $("#dayTab").show();
	     $("#weekTab").hide();
	     if (taskmode2 == 1) {
	     	$("#taskhour").unFormValidator(false); 
	     }else {
	     	$("#taskhour").unFormValidator(true); 
	     }
	  }
 	  if(obj=='2'){   //按周设置
	     $("#weekTab").show();
	     $("#dayTab").hide();
	     $("#taskhour").unFormValidator(true); 
	  }
 	}
 	
   //单选按时间点还是按没n小时执行
   function changeTaskMode2(obj){
        var taskmode2 =  $("input[name='userSourceInfo.taskInfo.taskmode2']:checked").val();
		if(obj == '1' && obj==taskmode2){
			$('#selAccHour').hide(); //选择时间点
			$('#taskhour').show();   //选择按每n小时执行
			$("#taskhour").unFormValidator(false); 
			$('#taskhourBut').show();
			$("#timingTd").html('<view:LanguageTag key="usource_timing_oper_point"/><view:LanguageTag key="colon"/>');
		}else {
			$('#selAccHour').show(); //选择时间点
			$('#taskhour').hide();   //选择按每n小时执行
			$("#taskhour").unFormValidator(true); 
			$('#taskhourBut').hide();
			$("#timingTd").html('<view:LanguageTag key="usource_timing_oper_interval"/><view:LanguageTag key="colon"/>');
		}
	}	
	
	//更新用户方法
	function updateUserInfo(){
       var weekAttrStr = "";
       var taskmode1 = $('#taskmode1').val();
       if(taskmode1 == 2){
       	  $('input[name="weekAttr[]"]').each(function () {
            if ($(this).attr('checked') == true) {
                weekAttrStr += $(this).attr('value')+",";
            }
          });
          if(weekAttrStr!=""){
          	 weekAttrStr = weekAttrStr.substring(0,weekAttrStr.length-1); 
          }
          $('#taskweek').val(weekAttrStr);
       }
          
	   var checkUSNameUrl = "<%=path%>/manager/confinfo/usersource/usConf!updateUserInfo.action?isEdit="+isEdit+"&enabled="+ $('#enabled').val();
	   $("#timingForm").ajaxSubmit({
		   async:false,  
		   type:"POST", 
		   dataType : "json",
		   url : checkUSNameUrl,
		   success:function(msg){
				if(msg.errorStr == 'error'){
				   FT.toAlert('error',msg.object, null);
				}else{ 
				   $.ligerDialog.success(msg.object,'<view:LanguageTag key="common_syntax_tip"/>',function(){
					   window.parent.location.href="<%=path%>/manager/confinfo/usersource/list.jsp";
					   setwin.close();
				   });
				}
			}
		});		 
	 }
 
	function setWeekSelected(wkstr){
	  var wkarray = new Array();
	      wkarray = wkstr.split(",");
	      
	      if(wkarray.length>0 && wkarray.length<7){
	        for(var i=0;i<wkarray.length;i++){
	           if(wkarray[i]=='0'){
	             $('#weekAttr0').attr("checked","true");
	           }if(wkarray[i]=='1'){
	             $('#weekAttr1').attr("checked","true");
	           }if(wkarray[i]=='2'){
	             $('#weekAttr2').attr("checked","true");
	           }if(wkarray[i]=='3'){
	             $('#weekAttr3').attr("checked","true");
	           }if(wkarray[i]=='4'){
	             $('#weekAttr4').attr("checked","true");
	           }if(wkarray[i]=='5'){
	             $('#weekAttr5').attr("checked","true");
	           }if(wkarray[i]=='6'){
	             $('#weekAttr6').attr("checked","true");
	           }
	        }
	      }if(wkarray.length==7){
	            $("#allCheck").attr("checked",true);
				$('input[name="weekAttr[]"]').each(function() {
	            	$(this).attr("checked", true);
	        	});
	      }
	}
	
   //是否启用定时更新
   function checkState(checked){
		if(checked){
			$('#enabled').val('1');
			$('#enabled').attr("checked","checked");
			$('#timTab').show();
			var taskmode1= $("#taskmode1").find("option:selected").val();
			var taskmode2 =  $("input[name='userSourceInfo.taskInfo.taskmode2']:checked").val();
			if(taskmode1=='1'){
			  setTypeChange(taskmode1);
			  changeTaskMode2(taskmode2)
			}
		 }else{
			$('#enabled').val('0');
			$('#enabled').attr("checked","");
			$('#timTab').hide();
		}
	}   
 
	//全选、取消全选
	function selectWeekAll(){
		if($("#allCheck").attr("checked") == true){
			$('input[name="weekAttr[]"]').each(function() {
            	$(this).attr("checked", true);
        	});
       	}else{            
	        $("input[name='weekAttr[]']").each(function() {
	            $(this).attr("checked", false);
	        });
        }
	}
	
	//选择时间点
	function selTaskTime(){
	   FT.openWinSet('<view:LanguageTag key="usource_timing_oper_point"/>','<%=path%>/manager/confinfo/usersource/sel_tasktime.jsp',[{text:'<view:LanguageTag key="common_syntax_sure"/>',onclick:FT.buttonAction.okClick},{text:'<view:LanguageTag key="common_syntax_close"/>',onclick:FT.buttonAction.cancelClose}],true, 550, 250);
	}
	
	//绑定点击事件
	function okClick(btn,win,index){
		setwin = win;
    	$('#okBtn').triggerHandler("click");
	}
	
	
	</script>
  </head>
  <body style="overflow:auto; overflow-x:hidden">
  <form id="timingForm" method="post" action="" >
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="topTableBg">
      <tr>
        <td>&nbsp;</td>
       <td align="center" class="topTableBgText"><view:LanguageTag key="usource_timing_task_config"/></td>
       <td>&nbsp;</td>
      </tr>
    </table>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable" >
		<tr> 
		 <td width="25%" align="right"></td>
		 <td width="28%" align="left">
	       <input type="checkbox"  id="enabled" name="userSourceInfo.taskInfo.enabled" <c:if test="${userSourceInfo.taskInfo.enabled==1}">checked</c:if> value="" onClick="checkState(this.checked)"/>
		    <input type="hidden" id="taskid" name="userSourceInfo.taskInfo.taskid" value="${userSourceInfo.taskInfo.taskid}"/><view:LanguageTag key="usource_is_timing_update"/>
		   
			<input type="hidden" id="sourcename" name="userSourceInfo.sourcename" value="${userSourceInfo.sourcename}"/>
			<input type="hidden" id="usid" name="userSourceInfo.id" value="${userSourceInfo.id}"/>
		 </td>
		 <td width="47%" align="left"></td>
		</tr>
		<tr> 
			<td align="right" colspan="3" height="5"></td>
		</tr>
	</table> 
		 
		
	<!-- 定时任务时间设置 -->
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="display:none" id="timTab" class="ulOnInsideTable">
		<tr> 
		 <td>
			 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
			  <tr>
			   <td width="25%" align="right"><view:LanguageTag key="usource_timing_oper_type"/><view:LanguageTag key="colon"/></td>
			   <td width="28%" align="left">
				<select name="userSourceInfo.taskInfo.taskmode1" id="taskmode1" onchange="setTypeChange(this.value)" style="width:100%"> 
			   		 <option value="1" <c:if test="${userSourceInfo.taskInfo.taskmode1==1}">selected</c:if>><view:LanguageTag key="usource_set_of_days"/></option>  
			   		 <option value="2" <c:if test="${userSourceInfo.taskInfo.taskmode1==2}">selected</c:if>><view:LanguageTag key="usource_set_weekly"/></option>   
			    </select>
			  </td>
			  <td width="47%" align="left"><div id="taskmode1Tip" style="width:100%"></td>
			 </tr>
			</table>
		</td>
		</tr>
		<tr><td colspan="2" height="5"></td></tr>
		<tr>
	     <td>
		  <!-- 按天设置 -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0" id="dayTab"  style="display:none" class="ulOnInsideTable">
				<tr>
				    <td width="25%"  align="right" ></td>
					<td width="28%"  align="left" >
					    <input type="radio" name="userSourceInfo.taskInfo.taskmode2" id="taskmode1" value="1" checked <c:if test="${userSourceInfo.taskInfo.taskmode2=='1'}">checked</c:if>  onClick="changeTaskMode2(this.value)" /><view:LanguageTag key="usource_sel_oper_point_in_time"/><br/>
	          			<input type="radio" name="userSourceInfo.taskInfo.taskmode2" id="taskmode2" value="2" <c:if test="${userSourceInfo.taskInfo.taskmode2 =='2'}">checked</c:if> onClick="changeTaskMode2(this.value)"/><view:LanguageTag key="usource_sel_every_n_hours"/>
					</td>
					<td width="14%"  align="left"></td>
					<td width="33%"  align="left" ></td>
				</tr>
				  <tr><td colspan="4" height="3"></td></tr>
				<tr> 
					<td align="right" id="timingTd"><view:LanguageTag key="usource_timing_oper_point"/><view:LanguageTag key="colon"/></td>
					<td align="left">
					   
					    <!-- 每天n点或n,n+1点开始执行 -->
					    <input type="text" name="userSourceInfo.taskInfo.taskhour" id="taskhour" class="formCss100" value="${userSourceInfo.taskInfo.taskhour }"  readonly/>
					   
					    <!-- 按每隔n小时执行一次,选择执行的时间 -->
					    <select name="userSourceInfo.taskInfo.selAccHour" id="selAccHour"   style="width:100%;display:none"> 
				   		 <option value="1" <c:if test="${userSourceInfo.taskInfo.selAccHour=='1'}">selected</c:if>>1</option>   
				  		 <option value="2" <c:if test="${userSourceInfo.taskInfo.selAccHour=='2'}">selected</c:if>>2</option>
				  		 <option value="3" <c:if test="${userSourceInfo.taskInfo.selAccHour=='3'}">selected</c:if>>3</option>    
				   		 <option value="4" <c:if test="${userSourceInfo.taskInfo.selAccHour=='4'}">selected</c:if>>4</option>   
				  		 <option value="6" <c:if test="${userSourceInfo.taskInfo.selAccHour=='6'}">selected</c:if>>6</option>
				   		 <option value="8" <c:if test="${userSourceInfo.taskInfo.selAccHour=='8'}">selected</c:if>>8</option>    
				   		 <option value="12" <c:if test="${userSourceInfo.taskInfo.selAccHour=='12'}">selected</c:if>>12</option>   
				  		 <option value="24" <c:if test="${userSourceInfo.taskInfo.selAccHour=='24'}">selected</c:if>>24</option>
				        </select>
					</td>
					<td align="left"><div style="padding-left:10px"><a href="javascript:selTaskTime()" class="button"  id="taskhourBut" style="width:55px"><span><view:LanguageTag key="common_syntax_select"/></span></a></div></td>
					<td align="left" ><div id="hourTip"></div></td>
				 </tr>	
			</table>
			
	  		 <!-- 按周设置 -->									    
			<table width="100%" border="0" cellspacing="0" cellpadding="0" id="weekTab" style="display:none" class="ulOnInsideTable">
			    <tr colspan='4' height="3"></tr>
			    <tr>
				<td width="25%" align="right" >
					&nbsp;&nbsp;&nbsp;<view:LanguageTag key="usource_timing_oper_date"/><view:LanguageTag key="colon"/>
				</td>
				<td width="28%" align="left" >
				   <input type="checkbox" id="weekAttr1" name="weekAttr[]" value="1"/>&nbsp;<view:LanguageTag key="common_date_monday"/>
	               <input type="checkbox" id="weekAttr2" name="weekAttr[]" value="2"/>&nbsp;<view:LanguageTag key="common_date_tuesday"/>
	               <br/>
	               <input type="checkbox" id="weekAttr3" name="weekAttr[]" value="3"/>&nbsp;<view:LanguageTag key="common_date_wednesday"/>
			 	   <input type="checkbox" id="weekAttr4" name="weekAttr[]" value="4"/>&nbsp;<view:LanguageTag key="common_date_thursday"/>
			 	   <br/>
			 	   <input type="checkbox" id="weekAttr5" name="weekAttr[]" value="5"/>&nbsp;<view:LanguageTag key="common_date_friday"/>
				   <input type="checkbox" id="weekAttr6" name="weekAttr[]" value="6"/>&nbsp;<view:LanguageTag key="common_date_saturday"/>
				   <br/>
				   <input type="checkbox" id="weekAttr0" name="weekAttr[]" value="0"/>&nbsp;<view:LanguageTag key="common_date_sunday"/>
				   <input type="hidden"   id="taskweek"  name="userSourceInfo.taskInfo.taskweek" value="${userSourceInfo.taskInfo.taskweek}"/>
			    </td>
			    <td width="3%"  align="left"></td>
			   	<td width="44%" align="left" ><div id="taskweekTip" style="width:100%"></div></td>      
			    </tr>
			    <tr>
			      <td></td>
			      <td><input type="checkbox" name="allCheck" id="allCheck" onClick="selectWeekAll()" title='<view:LanguageTag key="common_syntax_sel_or_unsel_all"/>' align="baseline"/>&nbsp;<view:LanguageTag key="common_syntax_sel_or_unsel_all"/></td>
			      <td></td>
			      <td></td>
			    </tr>  		
		  	</table>
			<br/>
		 </td>
		</tr>
		<tr>
	       <td><a href="#" name="okBtn" id="okBtn"></a></td>
	    </tr>
	</table> 
  </form>
  </body>
</html>