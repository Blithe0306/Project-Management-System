<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%@page import="com.ft.otp.common.ConfConstant"%>
<%@page import="com.ft.otp.util.conf.ConfDataFormat"%>
<%
	String path = request.getContextPath();
	//分发站点是否启用
	String site_enabled = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_TOKEN,ConfConstant.SITE_ENABLED);
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css" />
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/> 
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>    
  	<script src="<%=path%>/manager/common/js/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/token/distmanager/js/formatudid.js"></script>	
	
	<script language="javascript" type="text/javascript">
	<!--
	$(function() {
		$("#menu li").each(function(index) { //带参数遍历各个选项卡
            document.getElementById("apnewm").options[0].selected = true;  
            //手动输入密码项隐藏 
	        $("#textDiv").hide();
		    $("#valueDiv").hide(); 
		    $('#tipDiv').hide();
			$(this).click(function() { //注册每个选卡的单击事件
				$("#menu li.tabFocus").removeClass("tabFocus"); //移除已选中的样式
				$(this).addClass("tabFocus"); //增加当前选中项的样式
                    //显示选项卡对应的内容并隐藏未被选中的内容
				$("#content li:eq(" + index + ")").show()
				.siblings().hide();
				changeMenu(index); 
        	});
		});
		
		var siteEnabled = '<%=site_enabled %>';
		if (siteEnabled == 'y') {
			$("input[name=isonlineact][value=0]").attr("checked",true); 
		}else {
			$("input[name=isonlineact][value=1]").attr("checked",true); 
		}
		
		checkonlineInput();
		checkofflineInput();
		
		//标识码udid输入格式化
		$('#phoneudid').inputFormat('formatUdid');
		$('#phoneudid2').inputFormat('formatUdid');
		//格式化标识码udid并显示
		var udid = '${distManagerInfo.phoneudid}';
		$("#phoneudid").val(udid.replace(/\s(?=\d)/g,'').replace(/(\d{5})(?=\d)/g,"$1 "));
		$("#phoneudid2").val(udid.replace(/\s(?=\d)/g,'').replace(/(\d{5})(?=\d)/g,"$1 "));
	})

	//初始化选项卡时，分发的状态
	function changeMenu(obj){
		if(obj == 0){ //离线
            document.getElementById("apnewm").options[0].selected = true;   
	        $("#textDiv").hide();
		    $("#valueDiv").hide(); 
		    $('#tipDiv').hide();
         }else if(obj == 1){//在线
            document.getElementById("apnewm2").options[0].selected = true ; 
	        $("#textDiv2").hide();
		    $("#valueDiv2").hide(); 
		    $("#tipDiv2").hide(); 
		}
	}
        
	//选择激活密码生成方式
	function judgePass1(va,obj){
		if(obj=='online'){
			//选择手动输入
			if(va.value == 8){
				$("#textDiv").show();
				$("#valueDiv").show(); 
				$('#tipDiv').show();
		     }else{
				$("#textDiv").hide();
				$("#valueDiv").hide();
				$('#tipDiv').hide();
		     }
		}else if(obj=='offline'){
			if(va.value == 8){
				$("#textDiv2").show();
				$("#valueDiv2").show(); 
				$("#tipDiv2").show(); 
			}else{
				$("#textDiv2").hide();
				$("#valueDiv2").hide();
				$("#tipDiv2").hide(); 
			} 
		}
	}
 
	//在线分发表单提交之前的校验
	function checkonlineInput(){
	     $.formValidator.initConfig({submitButtonID:"ondistBt",debug:true,
		        onSuccess:function(){
		            //submitDist('online');
		            getDefaultPwd("ondistBt");
		        },
	           onError:function(){
	              return false;
	           }
	     });
	     
//	     $("#phoneudid").formValidator({empty:true,onFocus:"<view:LanguageTag key='tkn_vd_phoneudid_show'/>",onCorrect:"OK",onEmpty:"OK"}).inputValidator({min:20,max:20,onError:"<view:LanguageTag key='tkn_vd_phoneudid_error'/>"}).regexValidator({regExp:"num",dataType:"enum",onError:"<view:LanguageTag key='tkn_vd_phoneudid_error_1'/>"});
		 $("#phoneudid").formValidator({empty:true,onFocus:"<view:LanguageTag key='tkn_vd_phoneudid_show'/>",onCorrect:"OK",onEmpty:"OK"}).functionValidator({
		 	fun:function(pudid){
		 		var udid = Trim($.trim(pudid),'g');
		 		if (udid.length != 20) {
		 			return "<view:LanguageTag key='tkn_vd_phoneudid_error'/>";
		 		}
		 		if(!checkTextDataForNUMBER(udid)){  
			     	return '<view:LanguageTag key="tkn_vd_phoneudid_error_1"/>';
		   	  	}
		   	  	return true;
		 	}
		 });    
		 $("#activepass").formValidator({tipID:"passTip",onFocus:"<view:LanguageTag key='tkn_vd_activepass_show'/>",onCorrect:"OK"}).functionValidator({
	      fun:function(activeP){
	        
	        var apnewm = document.getElementById("apnewm");
	        var distBt = document.getElementById("ondistBt");
	        if(apnewm.value==8){
	           var  activepass = $('#activepass').val();
	           if(activepass == "" || activepass == null){ 
	              distBt.disabled = false; 
	             return '<view:LanguageTag key="tkn_vd_activepass_error"/>';
	      	 }
			 if(!checkTextDataForNUMBER(activepass)){  
			      distBt.disabled = false; 
			     return '<view:LanguageTag key="tkn_vd_activepass_error_1"/>';
		   	  }
		   	 if(activepass.length < 4 || activepass.length > 16){
		   	      distBt.disabled = false; 
			     return '<view:LanguageTag key="tkn_vd_activepass_error_2"/>';
		 	   }
	        }
	        return true; 
	      }
	    });
	    
		$("#phoneudid").focus();
	}
	
    //离线分发表单提交之前的校验
    function checkofflineInput(){
      $.formValidator.initConfig({validatorGroup:"2",submitButtonID:"ondistBt2",debug:true,
		        onSuccess:function(){
		        	getDefaultPwd("ondistBt2");
		          //  submitDist('offline');
		        },
	           onError:function(){
	              return false;
	              }
	            });     
//	     $("#phoneudid2").formValidator({validatorGroup:"2",empty:true,onFocus:"<view:LanguageTag key='tkn_vd_phoneudid_show'/>",onCorrect:"OK",onEmpty:"OK"}).inputValidator({min:20,max:20,onError:"<view:LanguageTag key='tkn_vd_phoneudid_error'/>"}).regexValidator({regExp:"num",dataType:"enum",onError:"<view:LanguageTag key='tkn_vd_phoneudid_error_1'/>"});       
	     $("#phoneudid2").formValidator({validatorGroup:"2",empty:true,onFocus:"<view:LanguageTag key='tkn_vd_phoneudid_show'/>",onCorrect:"OK",onEmpty:"OK"}).functionValidator({
		 	fun:function(pudid){
		 		var udid = Trim($.trim(pudid),'g');
		 		if (udid.length != 20) {
		 			return "<view:LanguageTag key='tkn_vd_phoneudid_error'/>";
		 		}
		 		if(!checkTextDataForNUMBER(udid)){  
			     	return '<view:LanguageTag key="tkn_vd_phoneudid_error_1"/>';
		   	  	}
		   	  	return true;
		 	}
		 });    
	     
	     $("#activepass2").formValidator({validatorGroup:"2",tipID:"pass2Tip",onFocus:"<view:LanguageTag key='tkn_vd_activepass_show'/>",onCorrect:"OK"}).functionValidator({
	      fun:function(activeP2){
	        var distBt2 = document.getElementById("ondistBt2");
		    var apnewm2 = document.getElementById("apnewm2");
		     
	        if(apnewm2.value==8){
	         var  activepass2 = $('#activepass2').val();
	           if(activepass2 == "" || activepass2 == null){ 
	              distBt2.disabled = false; 
	             return '<view:LanguageTag key="tkn_vd_activepass_error"/>';
	      	 }
			 if(!checkTextDataForNUMBER(activepass2)){  
			      distBt2.disabled = false; 
			     return '<view:LanguageTag key="tkn_vd_activepass_error_1"/>';
		   	  }
		   	 if(activepass2.length < 4 || activepass2.length > 16){
		   	      distBt2.disabled = false; 
			     return '<view:LanguageTag key="tkn_vd_activepass_error_2"/>';
		 	   }
	        }
	        return true; 
	        }
	      });
	     $("#phoneudid2").focus(); 
    }
	
	//点击分发提交按钮
	function submitDist(obj){
		var sendEmail = false;
	    var formId = "onlineForm";//在线分发
	    var url = '<%=path%>/manager/token/distmanager/distManager!onLineDistribute.action';
	    if(obj == 'online'){
			var paramVal = 0;
			$('input:checkbox[name = distParam][checked]').each(function(){
            	paramVal += parseInt($(this).val());
            });
            
            if(paramVal <= 0){
            	FT.toAlert('warn', '<view:LanguageTag key="tkn_dist_site_access_param_sel"/>', null);
            	return;
            }
            
            url += "?distParam=" + paramVal;
            
		    if($("#sendMail").attr("checked") == true){
	   			url += "&sendMail=on";
	   		};
	    }
	    
		if(obj == 'offline'){//离线分发
		 	url ='<%=path%>/manager/token/distmanager/distManager!offLineActivate.action';
		 	if($("#sendMail2").attr("checked") == true){
	   			url += "?sendMail=on";
	   		};
		 	
			formId = "offlineForm";
		}
		
		$("#"+formId).ajaxSubmit({
			async:false,    
			dataType : "json",  
			type:"POST", 
			url : url,
			success:function(msg){
				var errorStr = msg.errorStr;
				if(errorStr == 'error'){
			    	FT.toAlert('warn', msg.object, null);
				}else{
					$('#distTab').hide();
				    $('#distResultTb').show();
				    
				    var resultTip = '<view:LanguageTag key="tkn_dist_mobile_on_succ"/>';
				    var result = msg.object;
				    var activePwd = "";
				    var activeInfo = "";
				    if(obj == 'offline'){
				    	resultTip = '<view:LanguageTag key="tkn_dist_mobile_off_succ"/>';
				    	activePwd = result.substring(0, result.length - 25);
				    	var tempStr = result.substring(result.length - 25, result.length);
				    	
				    	if(tempStr != '' && tempStr.length == 25){
				    		activeInfo += tempStr.substring(0, 5);
				    		activeInfo += " " + tempStr.substring(5, 10);
				    		activeInfo += " " + tempStr.substring(10, 15);
				    		activeInfo += " " + tempStr.substring(15, 20);
				    		activeInfo += " " + tempStr.substring(20, 25);
				    	}else{
				    		activeInfo = tempStr;
				    	}
				    }else{
				    	activePwd = result.substring(0, result.indexOf("http"));
				    	activeInfo = result.substring(result.indexOf("http"), result.length);
				    }


				    $('#resultDiv').html(resultTip);
				    $('#activePwdDiv').html(activePwd);
				    $('#activeInfoDiv').html(activeInfo);
				}
			}
		});
	}
	
	// 是否需要激活密码
	function isNeedAcPwd(val){
		if(val){
			$("#selTr").show();
			$("#valTr").show();
			$("#valueText").hide();
			$("#apnewm").show();
		}else{
			$("#selTr").hide();
			$("#valTr").hide();
		}
	}
	
	// 显示二维码
	function twoCodeActive(){
		//在线选中，在线激活
		var disttype  = $('input[name="isonlineact"]:checked').val(); 
		if(disttype == 0){
			onlineTwoCode();
		} else {
			offlineTwoCode();
		}
	}
	
	//离线生成二维码
	function offlineTwoCode() {
		var isneedPwd = 0;
		if($("#isneedpwd").attr("checked")){
			isneedPwd = 1;
		}
		$("#onlineForm").ajaxSubmit({
			async : true,
			type : "POST",
			url : "<%=path%>/manager/token/distmanager/distManager!twoCodeActive.action?isneedPwd="+isneedPwd,
			dataType : "json",
			success : function(msg){
				if(msg.errorStr=='error'){
					FT.toAlert('warn', msg.object, null);
	            	return;
					//$("#twoCodeImage").attr("alt",msg.object);
				}else{
					var filename = msg.object.split(',')[0];
					$("#twoCodeImage").attr("src","<%=path%>"+filename);
					
					var appnew = $("#apnewm").val();
					if(isneedPwd ==1&& (appnew==2||appnew==4)){// 如果是需要激活密码 且 是随机或默认
						$("#valueText").show().text(msg.object.split(',')[1]);
						$("#apnewm").hide();
					}
				}
				$("#twoCodeImage").show();
			}
		});
	}
	
	//生成在线激活二维码图片
	function onlineTwoCode() {
		var url = '<%=path%>/manager/token/distmanager/distManager!onLineDist.action';
        //是否邮件发送   
	    if($("#sendMail").attr("checked") == true){
   			url += "?sendMail=on";
   		};
   		//是否需要激活密码
   		var isneedPwd = 0;
		if($("#isneedpwd").attr("checked")){
			isneedPwd = 1;
		}
		if (url.indexOf("?") != -1) {
			url += "&isneedPwd="+isneedPwd;
		}else {
			url += "?isneedPwd="+isneedPwd;
		}
		$("#onlineForm").ajaxSubmit({
			async:false,    
			dataType : "json",  
			type:"POST", 
			url : url,
			success:function(msg){
				if(msg.errorStr=='error'){
					FT.toAlert('warn', msg.object, null);
	            	return;
				}else{
					var filename = msg.object.split(',')[0];
					$("#twoCodeImage").attr("src","<%=path%>"+filename);
					
					var appnew = $("#apnewm").val();
					if(isneedPwd ==1&& (appnew==2||appnew==4)){// 如果是需要激活密码 且 是随机或默认
						$("#valueText").show().text(msg.object.split(',')[1]);
						$("#apnewm").hide();
					}
				}
				$("#twoCodeImage").show();
			}
		});
	}
	
	//格式化手机标识码
	function formatUdid(val) {
		$("#phoneudid").val(val.replace(/\s(?=\d)/g,'').replace(/(\d{5})(?=\d)/g,"$1 "));
	}
	function formatUdid2(val) {
		$("#phoneudid2").val(val.replace(/\s(?=\d)/g,'').replace(/(\d{5})(?=\d)/g,"$1 "));
	}
	//去除空格
	function Trim(str,is_global) {
	     var result;
	     result = str.replace(/(^\s+)|(\s+$)/g,"");
	     if(is_global.toLowerCase()=="g"){
	         result = result.replace(/\s/g,"");
	     }
	     return result;
	}
	
	/**
	*获取系统是否配置分发时的邮箱服务器、短信网关、用户的邮箱和手机信息
	*/
	function getDefaultPwd(buttonId){
		var userid=$("#userName").val();
		if(userid == undefined){
			userid = "";
		}
		var activationType="sweep";
		if(buttonId == "ondistBt2"){
			activationType="offline";
		}
		var url = '<%=path%>/manager/token/distmanager/distManager!checkMailSMSConfig.action?userid='+userid+'&activationType='+activationType;
		$.post(url,
			function(msg){
				if(msg.errorStr == 'error'){
					var info=msg.object;
					if(info != undefined){
						$.ligerDialog.confirm(info,'<view:LanguageTag key="common_syntax_confirm"/>',function(sel) {
						if(sel) {
							if(buttonId=="ondistBt"){//扫一扫按钮触发
								twoCodeActive();
							}else if(buttonId == "ondistBt2"){//离线分发触发
								submitDist('offline');
							}
						}
					});
					}
				}else{
					if(buttonId=="ondistBt"){//扫一扫按钮触发
						twoCodeActive();
					}else if(buttonId == "ondistBt2"){//离线分发触发
						submitDist('offline');
					}
				}
			}, "json"
		);
	}
	
	
	
	//-->
	</script>
  </head>
  <body>
     <input type="hidden" name="currentPage" id="currentPage" value="${param.curPage}"/> 
     <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td>
        	<span class="topTableBgText">
		  		<view:LanguageTag key='common_menu_tkn_distribute'/>
		  	</span>
        </td>
      </tr>
    </table> 
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnTable" id="distTab">
      <tr>
        <td width="100%">
		<ul id="menu">
		 <li class="tabFocus"><view:LanguageTag key="tkn_dist_two_dimensional"/></li>
		 <li><view:LanguageTag key="tkn_dist_offline"/></li>
	    </ul>
	    <ul id="content">
	     <li class="conFocus">
		  	<form name="onlineForm" id="onlineForm" method="post" action="">
		   <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
		   	 <c:if test="${not empty distManagerInfo.userName}">
		     <tr>
               <td width="30%" align="right"><view:LanguageTag key="user_username"/><view:LanguageTag key="colon"/></td>
               <td width="35%">${distManagerInfo.userName}
		       <input type="hidden" id="userName" name="distManagerInfo.userName" value="${distManagerInfo.userName}" class="formCss100" readonly />
		       </td>
              <td  width="35%">&nbsp;</td>
             </tr>
             </c:if>
             <tr>
               <td width="30%" align="right"><view:LanguageTag key="tkn_comm_tknum"/><view:LanguageTag key="colon"/></td>
               <td width="35%">${distManagerInfo.token}
		       <input type="hidden" id="token" name="distManagerInfo.token" value="${distManagerInfo.token}" class="formCss100" readonly />
		       </td>
              <td  width="35%">&nbsp;</td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="tkn_mobile_tkn_code"/><view:LanguageTag key="colon"/></td>
               <td><input type="text" id="phoneudid" name="distManagerInfo.phoneudid" value="${distManagerInfo.phoneudid}" onchange="formatUdid(this.value);" class="formCss100" /></td>
               <td class="divTipCss"><div id="phoneudidTip"></div></td>
             </tr>
             
              <tr>
		       <td align="right"><view:LanguageTag key="tkn_dist_type"/><view:LanguageTag key="colon" /></td>
				<td>
					<input type="radio" id="isonlineact0" name="isonlineact" value="0" /><view:LanguageTag key="log_action_id_1029"/>&nbsp;&nbsp;&nbsp;
			        <input type="radio" id="isonlineact1" name="isonlineact" value="1" /><view:LanguageTag key="tkn_dist_offline"/>
				</td>
				<td>&nbsp;</td>
             </tr>
             
             <tr>
               <td align="right"><view:LanguageTag key="tkn_dist_site_access_param"/><view:LanguageTag key="colon"/></td>
               <td>
		 		   <input type="checkbox" id="isneedpwd" name="isneedpwd" value="2" checked="checked" onclick="isNeedAcPwd(this.checked)"/>&nbsp;&nbsp;<view:LanguageTag key="common_syntax_need"/>
		       </td>
		       <td>&nbsp;</td>
             </tr>
             <tr id="selTr">
               <td align="right"><view:LanguageTag key="tkn_activation_pwd"/><view:LanguageTag key="colon"/></td>
               <td>
               <div id="valueText" style="display:none"></div>
               <select name="apnewm" id="apnewm" onchange="judgePass1(this,'online');" class="select100"> 
		   		 <option value="2"><view:LanguageTag key="tkn_random"/></option>    
		   		 <option value="4"><view:LanguageTag key="tkn_default"/></option>   
		  		 <option value="8"><view:LanguageTag key="tkn_manual_input"/></option>
		       </select>
		       </td> 
               <td>&nbsp;</td>
             </tr>
             
            <tr id="valTr">
             <td align="right">
                <div id="textDiv" style="display:none"><view:LanguageTag key="tkn_set_activation_pwd"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></div>
             </td>
             <td>
              <div id="valueDiv" style="display:none">
                 <input type="text" id="activepass" name="distManagerInfo.activepass" class="formCss100" />
              </div>
             </td>
             <td id="tipDiv" style="display:none" class="divTipCss"><div id="passTip"></div></td>
            </tr>
            
            <tr>
            	<td></td>
            	<td><img id="twoCodeImage" style="display:none" src="" width="150" height="150" /></td>
            	<td></td>
            </tr>
             
             <c:if test="${distManagerInfo.emFlag == 1}">
              <tr style="display:none">
               <td align="right">&nbsp;&nbsp;&nbsp;</td>
               <td><input type="checkbox" id="sendMail" name="sendMail" checked /><view:LanguageTag key='tkn_whether_send_email'/></td>   
		       <td>&nbsp;</td>
             </tr>
             </c:if>
  
            <tr>
		      <td align="right">&nbsp;</td>
		      <td>
		         <span style="float:left;"></span>
		         <a href="#" id="ondistBt" class="button"><span><view:LanguageTag key="tkn_dist"/></span></a>
		         <a href="<%=path%>/manager/token/distmanager/list.jsp" class="button"><span><view:LanguageTag key='common_syntax_return'/></span></a>
		      </td>
		      <td></td>
			</tr> 
           </table>
           </form>
	      </li>
		 <li>
		 	<form name="offlineForm"  id="offlineForm" method="post" action="">
		  <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
            <c:if test="${not empty distManagerInfo.userName}">
		     <tr>
               <td width="30%" align="right"><view:LanguageTag key="user_username"/><view:LanguageTag key="colon"/></td>
               <td width="35%">${distManagerInfo.userName}
		       <input type="hidden" id="userName" name="distManagerInfo.userName" value="${distManagerInfo.userName}" class="formCss100" readonly />
		       </td>
              <td  width="35%">&nbsp;</td>
             </tr>
             </c:if>
            <tr>
               <td width="30%" align="right"><view:LanguageTag key="tkn_comm_tknum"/><view:LanguageTag key="colon"/></td>
               <td width="35%">${distManagerInfo.token}
		       <input type="hidden" id="token2" name="distManagerInfo.token" value="${distManagerInfo.token}" class="formCss100" readonly /></td>
             <td  width="35%">&nbsp;</td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="tkn_mobile_tkn_code"/><view:LanguageTag key="colon"/></td>
               <td><input type="text" id="phoneudid2" name="distManagerInfo.phoneudid" value="${distManagerInfo.phoneudid}" onchange="formatUdid2(this.value);" class="formCss100" /></td>
               <td class="divTipCss"><div id="phoneudid2Tip"></div></td>
             </tr>
              <tr>
               <td align="right"><view:LanguageTag key="tkn_activation_pwd"/><view:LanguageTag key="colon"/></td>
               <td>
               <select name="apnewm" id="apnewm2" onchange="judgePass1(this,'offline');" class="select100"> 
		   		 <option value="2"><view:LanguageTag key="tkn_random"/></option>    
		   		 <option value="4"><view:LanguageTag key="tkn_default"/></option>   
		  		 <option value="8"><view:LanguageTag key="tkn_manual_input"/></option>
		       </select>
		       </td>
               <td >&nbsp;</td> 
             </tr>
             
            <tr>
             <td align="right">
                <div id="textDiv2" style="display:none"><view:LanguageTag key="tkn_set_activation_pwd"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></div>
             </td>
             <td>
              <div id="valueDiv2" style="display:none">
              <input type="text" id="activepass2" name="distManagerInfo.activepass" class="formCss100" />
              </div>
             </td>
             <td id="tipDiv2" style="display:none" class="divTipCss"><div id="pass2Tip"></div></td>
            </tr>  
             
             <c:if test="${distManagerInfo.emFlag == 1}">
             <tr style="display:none">
               <td align="right">&nbsp;&nbsp;&nbsp;</td>
               <td><input type="checkbox" id="sendMail2" name="sendMail2" checked /><view:LanguageTag key='tkn_whether_send_email'/></td>   
		       <td>&nbsp;</td>
             </tr>
             </c:if>
       
             <tr>
		       <td align="right">&nbsp;</td>
		       <td>
		         <span style="float:left;"></span>
		         <a href="#" class="button" id="ondistBt2"><span><view:LanguageTag key="tkn_dist"/></span></a>
		         <a href="<%=path%>/manager/token/distmanager/list.jsp" class="button"><span><view:LanguageTag key='common_syntax_return'/></span></a>
		       </td>
		       <td></td>
			 </tr> 
           </table>
           </form> 
		 </li>
        </ul>
		</td>
      </tr>
    </table>
    <!-- start 令牌分发结果 -->
    <table id="distResultTb" width="100%" border="0" cellspacing="0" cellpadding="0" style="display:none">
	  <c:if test="${not empty distManagerInfo.userName}">
      <tr>
        <td width="41%" align="right"><view:LanguageTag key="user_username"/><view:LanguageTag key="colon"/></td>
        <td width="59%">${distManagerInfo.userName}</td>
      </tr>
	  </c:if>
      <tr>
        <td align="right"><view:LanguageTag key="tkn_comm_tknum"/><view:LanguageTag key="colon"/></td>
        <td width="59%">${distManagerInfo.token}</td>
      </tr>
      <tr>
        <td align="right"><view:LanguageTag key="tkn_activation_pwd"/><view:LanguageTag key="colon"/></td>
        <td width="59%"><div id="activePwdDiv"></div></td>
      </tr>
      <tr>
        <td align="right"><view:LanguageTag key="tkn_activation_code"/><view:LanguageTag key="colon"/></td>
        <td><div id="activeInfoDiv"></div></td>
      </tr>
      <tr>
        <td align="right"><view:LanguageTag key="tkn_result_warn"/><view:LanguageTag key="colon"/></td>
        <td><div id="resultDiv" style="color:#006600; font-weight:bold"></div></td>
      </tr>
	  <tr>
        <td align="right">&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
	  <tr>
        <td align="right">&nbsp;</td>
        <td><a href="<%=path%>/manager/token/distmanager/list.jsp" class="button"><span><view:LanguageTag key='common_syntax_return'/></span></a></td>
      </tr>
    </table>
    <!-- end 令牌分发结果 -->
	<script type="text/javascript">
		judgePass1(document.getElementById("apnewm"), 'online');
	</script>

  </body>
</html>