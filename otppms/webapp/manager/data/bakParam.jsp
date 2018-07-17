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
	<script type="text/javascript" src="<%=path%>/manager/common/datepicker/WdatePicker.js"></script>
	
	<script language="javascript" src="<%=path%>/manager/data/js/loginTest.js" charset="UTF-8"></script>
	
    <script language="javascript" type="text/javascript">  
     //初始表单信息 或 组件函数 
	$(document).ready(function(){
		/*
		$("#loginTestBtn").click(function(){
				loginTest();
		});
		*/
		$("#gobackBtn").click(function(){
				linkPage("find");
		});
		
        $.formValidator.initConfig({
      		submitButtonID:"addBtn",
      		debug:true,
			onSuccess:function(){
				submitForm();
			}, 
			onError:function(){
				return false;
			}
		});
		
		//初始显示的备份时间bakTimeTr
		showBakTimeTrByAuto();	
		
		//初始显示的选择时间项
		showTimeCompentByMode();	
		
		//初始显示远程连接参数行
		showRemoteTrsByIsRomote();
		
		//触发事件 点击启动定时备份
		$("#isTimeAutoBox").click(function(){ 
			showBakTimeTrByAuto();		
		});
		
		//触发事件 备份模式时间下拉框的change 事件
		$("select[name=dbBakConfInfo.taskInfo.taskmode1]").bind("change",function(){
				showTimeCompentByMode();
		});
		
		//触发事件 本地 远程 单选按钮单击触发事件 
		$("input[name=dbBakConfInfo.isremote]").click(function(){
				showRemoteTrsByIsRomote();
		});
		
		// 如果远程FTP为空时，初始化信息
		if($("#serverIp").val() == 0){
			$("#serverIp").val("");
			$("#user").val("");
			$("#password").val("");
		}
		if($("#address").val() == 0){
			$("#address").val("");
		}
		
    });


	/*
	  根据 是否启用定时备份选项
	       显示 时间组件的行
	*/
	function showBakTimeTrByAuto(){
		if($("#isTimeAutoBox").attr("checked")==true){ //启动定时备份				
			$("#bakTimeTr").show();
		}else{
			$("#bakTimeTr").hide();			
		}
	}

	/*
	  根据 选择的时间模式显示 时间组件
	  选择按月1 显示 周值的下拉框、时分框
	  选择按周2 显示 日值的下拉框、时分框
	  选择按日3 显示 时分框
	*/
	function showTimeCompentByMode(){
		var mode=$("select[name=dbBakConfInfo.taskInfo.taskmode1] option:selected").val();
		if(mode=="2"){ //周 时分 				
			$("#taskweekSelect").show();
			$("#taskdaySelect").hide();
		}else if(mode=="1"){ //月时分			
			$("#taskdaySelect").show();
			$("#taskweekSelect").hide();			
		}else if(mode=="3"){
			$("#taskdaySelect").hide();
			$("#taskweekSelect").hide();
		}
		$("#timeStr").show();
	}

	/*
	  根据是否远程 显示远程参数
	*/
	function showRemoteTrsByIsRomote(){
		var isRemote=$("input[name=dbBakConfInfo.isremote]:radio:checked").val();
		if(isRemote=="1"){ //远程 				
			$("#remoteIpTr").show();
			$("#remotePortTr").show();
			$("#remoteUserTr").show();
			$("#remotePassTr").show();
			// 修改路径后的描述内容
			$("#descri").html('<view:LanguageTag key="databal_vd_addr_descri2"/>');
			
			$("#showAddr").show();

			var ip = $('#serverIp').val(); // 远程机器IP
	    	var http = "ftp://";
	    	var port = $('#port').val(); // 端口
	    	var address = $('#address').val();
	    	var complete = http+ip+":"+port+address;
			if(port!= "" && ip!=""){
	    		$('#completeAdd').html(complete);
	    		$("#showAddr").show();
        	}
			//$("#loginTestBtn").show(); //测试远程连接按钮
			
			//$("#addBtn").hide(); //保存按钮隐藏 待远程测试成功再显示
		}else if(isRemote=="0"){ //本地			
			$("#remoteIpTr").hide();
			$("#remotePortTr").hide();
			$("#remoteUserTr").hide();
			$("#remotePassTr").hide();	
			// 修改路径后的描述内容
			$("#descri").html('<view:LanguageTag key="databal_vd_addr_descri1"/>');
			$("#showAddr").hide();
			var port = $('#port').val();
			var partn = new RegExp("^[1-9]\\d*$");
			if(!partn.exec(port)){
				$('#port').val('21');
			}
			//$("#loginTestBtn").hide();	//测试远程连接按钮	
			//$("#addBtn").show(); //本地备份 保存按钮始终显示
		}
	}
    
    
    function submitForm(){
    
    	var address = $('#address').val();
        if($.trim(address)==''){
        	FT.toAlert("warn",'<view:LanguageTag key="databak_vd_addres_err"/>',null); 
        	return ;
        }
        
        if(address.indexOf("\\") > -1){
        	FT.toAlert("warn",'<view:LanguageTag key="databak_vd_err_10"/>',null); 
        	return ;
        }
    
    	//整理参数信息
    	if($("#isBakLogBox").attr("checked")==true){ //如果是否备份日志选中
    		$("#isBakLog").val("1");
    	}else{
    		$("#isBakLog").val("0");
    	}
    	
    	if($("#isTimeAutoBox").attr("checked")==true){ //如果启动定时备份 
    		$("#isTimeAuto").val("1");
    	}else{
    		$("#isTimeAuto").val("0");
    	}
    	
    	
    	if($("#isTimeAutoBox").attr("checked")==true){ //如果启动定时备份 重设时间设置 
    		var taskweek="*";
	    	var taskmonth="*";
	    	var taskday="*";
	    	var taskhour="0";
	    	var taskminute="0";
    	
	    	var mode=$("select[name=dbBakConfInfo.taskInfo.taskmode1] option:selected").val(); //选择属性name为dbBakConfInfo.mode的选中的项的value
	    	if(mode=="2"){ //周时分
	    		taskweek=$("select[id=taskweekSelect] option:selected").val();	    		   		
	    	}else if(mode=="1"){ //月日时分   		
	    		taskday=$("select[id=taskdaySelect] option:selected").val();	
	    	}else if(mode=="3"){ //日时分
	    		
	    	}
	    	
	    	var timeStr=$("#timeStr").val();  
	    	if(timeStr==null || timeStr.indexOf(":")==-1){
	    		FT.toAlert("warn",'<view:LanguageTag key="databak_vd_addres_err"/>',null);
	    		return ;
	    	}
	    	
	    	taskhour=timeStr.split(":")[0];
	    	taskminute=timeStr.split(":")[1];
	    	
	    	if(taskhour==""||taskminute==""){
	    		FT.toAlert("warn",'<view:LanguageTag key="databak_vd_time_err"/>',null);
	    		return;
	    	}
	    	
	    	$("input[name=dbBakConfInfo.taskInfo.taskweek]:hidden").val(taskweek); //周
	    	$("input[name=dbBakConfInfo.taskInfo.taskmonth]:hidden").val(taskmonth);//月
	   		$("input[name=dbBakConfInfo.taskInfo.taskday]:hidden").val(taskday); //日
	    	$("input[name=dbBakConfInfo.taskInfo.taskhour]:hidden").val(taskhour);//时
	    	$("input[name=dbBakConfInfo.taskInfo.taskminute]:hidden").val(taskminute); //分
    	}
    	
		//远程备份前的测试
		var isRemote=$("input[name=dbBakConfInfo.isremote]:radio:checked").val(); //选择属性name为dbBakConfInfo.isRemote的选中的单选框
		if(isRemote==1 || isRemote=="1"){//如果是远程
			var port = $('#port').val();
			var partn = new RegExp("^[1-9]\\d*$");
			if($('#serverIp').val() == ""){
				FT.toAlert("warn",'<view:LanguageTag key="databak_vd_serip_err"/>',null); 
				return ;
			}
			if($('#port').val() == ""){
				FT.toAlert("warn",'<view:LanguageTag key="databak_vd_port_err"/>',null);
				return ;
			} 
			if(!partn.exec(port)){
				FT.toAlert("error",'<view:LanguageTag key="databak_vd_port_err_1"/>',null);
				return ;
			}
			if($('#user').val() == ""){
				FT.toAlert("warn",'<view:LanguageTag key="databak_vd_user_err"/>',null);
				return ;
			}
	
			login();
			if(loginResult!="success"){//如果没有登录远程成功
				FT.toAlert(loginResult,alertMessage,null);
				return ;
			}
		}
    	//提交订单
    	$("#AddForm").ajaxSubmit({
			async:false,    
			dataType : "json",  
			type:"POST", 
			url : $("#contextPath").val() + "/manager/data/databak!bakConfig.action",
			success:function(msg){
				if(msg.errorStr=="success"){
					linkPage("find");//跳转到备份页
				}else{
					FT.toAlert(msg.errorStr,msg.object,null);
				}
			}
		});
    }

    // onkeyup事件
    function completeaddr(){
    	var ip = $('#serverIp').val(); // 远程机器IP
    	var http = "ftp://";
    	var port = $('#port').val(); // 端口
    	var address = $('#address').val();
    	var complete = http+ip+":"+port+address;
    	var isRemote=$("input[name=dbBakConfInfo.isremote]:radio:checked").val();
    	if(isRemote == 1){
        	if(port!= "" && ip!=""){
	    		$('#completeAdd').html(complete);
	    		$("#showAddr").show();
        	}else{
        		$("#showAddr").hide();
        	}
    	}
    }
    
    </script>
  </head>  
  
  <body scroll="no" style="overflow:hidden">
  <input type="hidden" value="<%=path %>" id="contextPath" />
  <form name="AddForm" id="AddForm" method="post" action="">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td>  
		  <span class="topTableBgText">
		  	  <view:LanguageTag key="databak_vd_setbak"/>
		  </span>
        </td>
      </tr>
    </table> 
    
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td valign="top">
        
        	  <input type="hidden" name="dbBakConfInfo.taskInfo.taskweek" value="${dbBakConfInfo.taskInfo.taskweek}" /><!-- 真正的周 -->
  			  <input type="hidden" name="dbBakConfInfo.taskInfo.taskmonth" value="${dbBakConfInfo.taskInfo.taskmonth}" /><!-- 真正的月 -->
  			  <input type="hidden" name="dbBakConfInfo.taskInfo.taskday" value="${dbBakConfInfo.taskInfo.taskday}" /><!-- 真正的日 -->
      		  <input type="hidden" name="dbBakConfInfo.taskInfo.taskhour" value="${dbBakConfInfo.taskInfo.taskhour}" /><!-- 真正的时点 -->
 			  <input type="hidden" name="dbBakConfInfo.taskInfo.taskminute" value="${dbBakConfInfo.taskInfo.taskminute}" />   <!-- 真正的分点 -->
        
		    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		     <tr>
		        <td width="30%" rowspan="2" align="right">&nbsp;</td>
		        <td width="18%">		        	
		        	<input type="hidden" id="isBakLog"  name="dbBakConfInfo.isbaklog" value="${dbBakConfInfo.isbaklog}" />
		        	<c:if test="${dbBakConfInfo.isbaklog==1}">
		            	 <input type="checkbox"  id="isBakLogBox" checked />
		        	</c:if>
		            <c:if test="${dbBakConfInfo.isbaklog==0}">
		        		<input type="checkbox"  id="isBakLogBox" />
		        	</c:if>
		        	<view:LanguageTag key="databak_vd_log_isbak"/></td>
		        <td width="52%" rowspan="2"></td>	
		      </tr>
		     <tr>
		       <td>
			   <input type="hidden" id="isTimeAuto"  name="dbBakConfInfo.istimeauto" value="${dbBakConfInfo.istimeauto}" />
			        	<c:if test="${dbBakConfInfo.istimeauto==1}"><!-- 0否 手动  1 是 定时 -->
			            	 <input type="checkbox"  id="isTimeAutoBox"  checked />
			        	</c:if>
			            <c:if test="${dbBakConfInfo.istimeauto==0}">
			        		<input type="checkbox"  id="isTimeAutoBox"  />
			        	</c:if>
			        <view:LanguageTag key="databak_vd_fixed_isbak"/>
			   </td>
	          </tr>
		    
		      <tr id="bakTimeTr">
			        <td width="30%" align="right"><view:LanguageTag key="databak_vd_time"/><view:LanguageTag key="colon"/>&nbsp;</td>
			        <td width="25%">
			        	<select name="dbBakConfInfo.taskInfo.taskmode1" class="select100" style="width:60px">
		        		   <option value="3" <c:if test="${dbBakConfInfo.taskInfo.taskmode1==3}"> selected </c:if> ><view:LanguageTag key="databak_everyday"/></option>
		        		   <option value="2" <c:if test="${dbBakConfInfo.taskInfo.taskmode1==2}"> selected </c:if> ><view:LanguageTag key="databak_weekly"/></option>
		        		   <option value="1" <c:if test="${dbBakConfInfo.taskInfo.taskmode1==1}"> selected </c:if> ><view:LanguageTag key="databak_monthly"/></option>
			            </select>
			        	&nbsp; 
			           <select id="taskweekSelect" class="select100" style="width:70px">		        		   
		        		   <option value="1" <c:if test="${dbBakConfInfo.taskInfo.taskweek=='1'}"> selected </c:if> ><view:LanguageTag key="common_date_monday"/></option>
		        		   <option value="2" <c:if test="${dbBakConfInfo.taskInfo.taskweek=='2'}"> selected </c:if> ><view:LanguageTag key="common_date_tuesday"/></option>
		        		   <option value="3" <c:if test="${dbBakConfInfo.taskInfo.taskweek=='3'}"> selected </c:if> ><view:LanguageTag key="common_date_wednesday"/></option>
		        		   <option value="4" <c:if test="${dbBakConfInfo.taskInfo.taskweek=='4'}"> selected </c:if> ><view:LanguageTag key="common_date_thursday"/></option>
		        		   <option value="5" <c:if test="${dbBakConfInfo.taskInfo.taskweek=='5'}"> selected </c:if> ><view:LanguageTag key="common_date_friday"/></option>
		        		   <option value="6" <c:if test="${dbBakConfInfo.taskInfo.taskweek=='6'}"> selected </c:if> ><view:LanguageTag key="common_date_saturday"/></option>
		        		   <option value="0" <c:if test="${dbBakConfInfo.taskInfo.taskweek=='0'}"> selected </c:if> ><view:LanguageTag key="common_date_sunday"/></option>
	        		   </select>
	           	      <select id="taskdaySelect" class="select100" style="width:62px">		        		   
		        		   <option value="1" <c:if test="${dbBakConfInfo.taskInfo.taskday=='1'}"> selected </c:if> ><view:LanguageTag key="databak_oneday"/></option>
		        		   <option value="2" <c:if test="${dbBakConfInfo.taskInfo.taskday=='2'}"> selected </c:if> ><view:LanguageTag key="databak_twoday"/></option>
		        		   <option value="3" <c:if test="${dbBakConfInfo.taskInfo.taskday=='3'}"> selected </c:if> ><view:LanguageTag key="databak_threeday"/></option>
		        		   <option value="4" <c:if test="${dbBakConfInfo.taskInfo.taskday=='4'}"> selected </c:if> ><view:LanguageTag key="databak_fourday"/></option>
		        		   <option value="5" <c:if test="${dbBakConfInfo.taskInfo.taskday=='5'}"> selected </c:if> ><view:LanguageTag key="databak_fiveday"/></option>
		        		   <option value="6" <c:if test="${dbBakConfInfo.taskInfo.taskday=='6'}"> selected </c:if> ><view:LanguageTag key="databak_sixday"/></option>
		        		   <option value="7" <c:if test="${dbBakConfInfo.taskInfo.taskday=='7'}"> selected </c:if> ><view:LanguageTag key="databak_sevenday"/></option>
		        		   <option value="8" <c:if test="${dbBakConfInfo.taskInfo.taskday=='8'}"> selected </c:if> ><view:LanguageTag key="databak_eightday"/></option>
		        		   <option value="9" <c:if test="${dbBakConfInfo.taskInfo.taskday=='9'}"> selected </c:if> ><view:LanguageTag key="databak_nineday"/></option>
		        		   <option value="10" <c:if test="${dbBakConfInfo.taskInfo.taskday=='10'}"> selected </c:if> ><view:LanguageTag key="databak_tenday"/></option>
		        		   <option value="11" <c:if test="${dbBakConfInfo.taskInfo.taskday=='11'}"> selected </c:if> ><view:LanguageTag key="databak_elevenday"/></option>
		        		   <option value="12" <c:if test="${dbBakConfInfo.taskInfo.taskday=='12'}"> selected </c:if> ><view:LanguageTag key="databak_twelveday"/></option>
		        		   <option value="13" <c:if test="${dbBakConfInfo.taskInfo.taskday=='13'}"> selected </c:if> ><view:LanguageTag key="databak_thirteenday"/></option>
		        		   <option value="14" <c:if test="${dbBakConfInfo.taskInfo.taskday=='14'}"> selected </c:if> ><view:LanguageTag key="databak_fourteenday"/></option>
		        		   <option value="15" <c:if test="${dbBakConfInfo.taskInfo.taskday=='15'}"> selected </c:if> ><view:LanguageTag key="databak_fifteenday"/></option>
		        		   <option value="16" <c:if test="${dbBakConfInfo.taskInfo.taskday=='16'}"> selected </c:if> ><view:LanguageTag key="databak_sixteenday"/></option>
		        		   <option value="17" <c:if test="${dbBakConfInfo.taskInfo.taskday=='17'}"> selected </c:if> ><view:LanguageTag key="databak_seventeenday"/></option>
		        		   <option value="18" <c:if test="${dbBakConfInfo.taskInfo.taskday=='18'}"> selected </c:if> ><view:LanguageTag key="databak_eighteenday"/></option>
		        		   <option value="19" <c:if test="${dbBakConfInfo.taskInfo.taskday=='19'}"> selected </c:if> ><view:LanguageTag key="databak_nineteenday"/></option>
		        		   <option value="20" <c:if test="${dbBakConfInfo.taskInfo.taskday=='20'}"> selected </c:if> ><view:LanguageTag key="databak_twentyday"/></option>
		        		   <option value="21" <c:if test="${dbBakConfInfo.taskInfo.taskday=='21'}"> selected </c:if> ><view:LanguageTag key="databak_twenoneday"/></option>
		        		   <option value="22" <c:if test="${dbBakConfInfo.taskInfo.taskday=='22'}"> selected </c:if> ><view:LanguageTag key="databak_twentwoday"/></option>
		        		   <option value="23" <c:if test="${dbBakConfInfo.taskInfo.taskday=='23'}"> selected </c:if> ><view:LanguageTag key="databak_twenthreeday"/></option>
		        		   <option value="24" <c:if test="${dbBakConfInfo.taskInfo.taskday=='24'}"> selected </c:if> ><view:LanguageTag key="databak_twenfourday"/></option>
		        		   <option value="25" <c:if test="${dbBakConfInfo.taskInfo.taskday=='25'}"> selected </c:if> ><view:LanguageTag key="databak_twenfiveday"/></option>
		        		   <option value="26" <c:if test="${dbBakConfInfo.taskInfo.taskday=='26'}"> selected </c:if> ><view:LanguageTag key="databak_twensixday"/></option>
		        		   <option value="27" <c:if test="${dbBakConfInfo.taskInfo.taskday=='27'}"> selected </c:if> ><view:LanguageTag key="databak_twensevenday"/></option>
		        		   <option value="28" <c:if test="${dbBakConfInfo.taskInfo.taskday=='28'}"> selected </c:if> ><view:LanguageTag key="databak_tweneightday"/></option>
	        		   </select>
	       		       <input type="text" id="timeStr" value="${dbBakConfInfo.taskInfo.taskhour}:${dbBakConfInfo.taskInfo.taskminute}" class="formCss100" style="width:55px" onFocus="WdatePicker({lang:'${language_session_key}', dateFmt:'HH:mm'})"/><!-- 显示的时间 -->			        </td>
			        <td width="45%"></td>	
			   </tr>
		      
		       <tr>
		        <td width="30%" align="right"><view:LanguageTag key="databak_vd_reomote_bak"/><view:LanguageTag key="colon"/>&nbsp;</td>
		        <td width="25%">
		        	<c:if test="${dbBakConfInfo.isremote==0}">
		            	<input type="radio"  name="dbBakConfInfo.isremote" value="0" checked/><view:LanguageTag key="databak_local"/>&nbsp;
		        		<input type="radio"  name="dbBakConfInfo.isremote" value="1" /><view:LanguageTag key="databak_vd_protocol"/>
		        	</c:if>
		        	<c:if test="${dbBakConfInfo.isremote==1}">
		            	<input type="radio"  name="dbBakConfInfo.isremote" value="0" /><view:LanguageTag key="databak_local"/>&nbsp;
		        		<input type="radio"  name="dbBakConfInfo.isremote" value="1" checked/><view:LanguageTag key="databak_vd_protocol"/>
		        	</c:if>		        </td>
		        <td width="45%"></td>	
		      </tr>
		      
		     
		      <tr id="remoteIpTr">
		        <td width="30%" align="right"><view:LanguageTag key="databak_vd_server"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>&nbsp;</td>
		        <td width="25%">
		            <input type="text"  id="serverIp" name="dbBakConfInfo.serverip" value="${dbBakConfInfo.serverip}" onkeyup="completeaddr();" class="formCss100"/>		        </td>
		        <td width="45%"></td>	
		      </tr>
		      <tr id="remotePortTr">
		        <td width="30%" align="right"><view:LanguageTag key="databak_vd_port"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>&nbsp;</td>
		        <td width="25%">
		            <input type="text"  id="port" name="dbBakConfInfo.port" value="${dbBakConfInfo.port}" onkeyup="completeaddr();" class="formCss100"/>		        </td>
		        <td width="45%"></td>	
		      </tr>
		      <tr id="remoteUserTr">
		        <td width="30%" align="right"><view:LanguageTag key="databak_vd_username"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>&nbsp;</td>
		        <td width="25%">
		            <input type="text" id="user"  name="dbBakConfInfo.user" value="${dbBakConfInfo.user}" class="formCss100"/>		        </td>
		        <td width="45%"></td>	
		      </tr>
		      <tr id="remotePassTr">
		        <td width="30%" align="right"><view:LanguageTag key="databak_vd_pwd"/><view:LanguageTag key="colon"/>&nbsp;</td>
		        <td width="25%">
		            <input onpaste="return false" type="password" id="password" name="dbBakConfInfo.password" value="${dbBakConfInfo.password}" class="formCss100"/>		        </td>
		        <td width="45%"></td>	
		      </tr>
		      
		      <tr>
		        <td width="30%" align="right"><view:LanguageTag key="databak_vd_bakaddress"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>&nbsp;</td>
		        <td width="25%" align="left">
		        	<input type="text" id="address" size="50" name="dbBakConfInfo.dir" value="${dbBakConfInfo.dir}" onkeyup="completeaddr();" class="formCss100"/>		        </td>
		        <td width="45%">&nbsp;</td>	
		      </tr>
		      <tr>
		      	 <td width="30%">&nbsp;</td>
		      	 <td width="70%" colspan="2" id="descri" align="left">
		      	 	<view:LanguageTag key="databal_vd_addr_descri"/>		      	 </td>
		      </tr>
		      
		      <tr id="showAddr" style="display:none;">
		        <td width="30%" align="right">&nbsp;</td>
		        <td width="25%">
		        	<div id="completeAdd" ></div>		        </td>
		        <td width="45%"></td>	
		      </tr>
		      
		       <tr>
		        <td align="right">&nbsp;</td>
		        <td>		            
		            <!-- <a href="#" id="loginTestBtn" class="button"><span>远程登录测试</span></a> -->		        </td>
		      </tr>
		      
		      <tr>
		        <td align="right">&nbsp;</td>
		        <td>
		            <a href="#" id="addBtn" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>	
		            <c:if test="${goBackTag=='1'}">
		            	<a href="#" id="gobackBtn" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a>		            </c:if>		        </td>
		      </tr>
		    </table>
		        
    	</td>
    </tr>
    </table>
    
  </form>
  </body>
</html>