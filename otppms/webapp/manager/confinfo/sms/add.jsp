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
	<script language="javascript" type="text/javascript">
	//测试短信网关窗口
 	var waitW;
    $(document).ready(function(){
    	//窗体大小改变时要重新设置透明文件框的位置
		$(window).resize(initFileInputDivNoParame);
		//初始化透明文件框的位置
		initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv');
    
    	//隐藏https证书
    	$("tr[id='httpsTR']").hide();
    
		//对短信网关配置校验
       $.formValidator.initConfig({submitButtonID:'nextstep',debug:true,
			onSuccess:function(){
				stepController(1);
			},
			onError:function(){
				return false;
			}
		});

		var smsname = $("#smsname").val();
		$("#smsname").formValidator({onFocus:'<view:LanguageTag key="sms_vd_name_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="sms_vd_name_err"/>'},onError:'<view:LanguageTag key="sms_vd_name_err_1"/>'}).functionValidator({
			fun:function(smsname){
	    	if(g_invalid_char_js(smsname)){
	       		return "<view:LanguageTag key="sms_vd_smsname_err"/>";
	    	}
	    	return true;
		}});
		$("#host").formValidator({onFocus:'<view:LanguageTag key="sms_vd_host_show"/>',onCorrect:"OK"})
		.inputValidator({min:10,max:255,onError:'<view:LanguageTag key="sms_vd_host_err"/>'})
		.functionValidator({
			fun:function(val){
				 var hostval = $.trim(val);
				 var hname = hostval.substring(0,hostval.indexOf(":"));
				 if(hname != "http" && hname != "https"){
				 	return '<view:LanguageTag key="sms_vd_host_err_1"/>';
				 }
				 if(hname == "https") {
				 	$("tr[id='httpsTR']").show();
				 }else {
				 	$("tr[id='httpsTR']").hide();
				 }
                 return true;
              }
	    });	
		$("#sendtype").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});	
		$("#cerhttps").formValidator({empty:true,onFocus:'<view:LanguageTag key="sms_vd_cer_https_show"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="sms_vd_cer_https_show"/>'});
		$("#username").formValidator({onFocus:'<view:LanguageTag key="sms_vd_uname_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="sms_vd_uname_err"/>'},onError:'<view:LanguageTag key="sms_vd_uname_err_2"/>'}).functionValidator({
			fun:function(smsname){
	    	if(g_invalid_char_js(smsname)){
	       		return "<view:LanguageTag key="sms_vd_uname_err_3"/>";
	    	}
	    	return true;
		}});
		$("#password").formValidator({onFocus:'<view:LanguageTag key="common_vd_pwd_show1"/>',onCorrect:"OK"}).inputValidator({min:4,max:64,onError:'<view:LanguageTag key="common_vd_pwd_show1"/>'});
		$("#pwdconf").formValidator({onFocus:'<view:LanguageTag key="common_vd_confirm_pwd_show1"/>',onCorrect:"OK"}).inputValidator({min:4,max:64, onError:'<view:LanguageTag key="common_vd_confirm_pwd_show1"/>'}).compareValidator({desID:"password",operateor:"=",onError:'<view:LanguageTag key="common_vd_confirm_pwd_err"/>'}); 
		$("#priority").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
		$("#retrysend").formValidator({onFocus:'<view:LanguageTag key="sms_vd_retry_send_show"/>',onCorrect:"OK"}).regexValidator({regExp:"num1",dataType:"enum",onError:'<view:LanguageTag key="sms_vd_retry_send_err"/>'});
		$("#descp").formValidator({empty:true,onFocus:'<view:LanguageTag key="common_vd_descp_show"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:255,onError:'<view:LanguageTag key="common_vd_descp_error"/>'});
		
		$("#smsname").focus();	
       		  
	    //点击测试，对参数校验
		$.formValidator.initConfig({validatorGroup:"2",submitButtonID:'testBnt',debug:true,
		  onSuccess:function(){
		    testSMS();
		  },
		  onError:function(){
			return false;
		  }}
		);

	    var accountattr = $("#accountattr").val();
	    $("#accountattr").formValidator({validatorGroup:"2",onFocus:'<view:LanguageTag key="sms_vd_acc_attr_show"/>'}).inputValidator({min:1,max:64,onError:'<view:LanguageTag key="sms_vd_acc_attr_err"/>'}).functionValidator({
			fun:function(accountattr){
		    if(g_invalid_char_js(accountattr)){
		       return '<view:LanguageTag key="sms_vd_acc_attr_err_1"/>';
		    }
		    return true;
		}});
		$("#passwdattr").formValidator({validatorGroup:"2",onFocus:'<view:LanguageTag key="sms_vd_pwd_attr"/>',onCorrect:"OK"}).inputValidator({min:1,max:64,onError:'<view:LanguageTag key="sms_vd_pwd_attr"/>'});
		$("#phoneattr").formValidator({validatorGroup:"2",onFocus:'<view:LanguageTag key="sms_vd_mobile_attr_show"/>' ,onCorrect:"OK"}).inputValidator({max:20,onError:'<view:LanguageTag key="sms_vd_mobile_attr_err"/>'}).regexValidator({regExp:"^[A-Za-z0-9\-]+$",dataType:"String",onError:'<view:LanguageTag key="sms_vd_mobile_attr_err_1"/>'});
		$("#messageattr").formValidator({validatorGroup:"2",onFocus:'<view:LanguageTag key="sms_vd_message_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:255,onError:'<view:LanguageTag key="sms_vd_message_err"/>'});
		$("#addparam").formValidator({validatorGroup:"2",onFocus:'<view:LanguageTag key="sms_vd_add_param_show"/>',onCorrect:"OK"}).inputValidator({max:128,onError:'<view:LanguageTag key="sms_vd_add_param_err"/>'});
		$("#sendresult").formValidator({validatorGroup:"2",empty:true,onFocus:'<view:LanguageTag key="sms_vd_send_result_show"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:255,onError:'<view:LanguageTag key="sms_vd_send_result_err"/>'});
	    $("#accountattr").focus();
	}) 
	
	 //通列li的索引跳转到指定标签页，执行上一步，下一步的功能
    function stepController(index){
     	$("#menu li.tabFocus").removeClass("tabFocus"); //移除已选中的样式
		$("#menu li:eq(" + index + ")").addClass("tabFocus");//增加当前选中项的样式
		$("#content li:eq(" + index + ")").show().siblings().hide();//显示选项卡对应的内容并隐藏未被选中的内容
    }
	
	//测试短信网关
	function testSMS(){
		waitW = FT.openWinSet('<view:LanguageTag key="sms_test_param"/>', '<%=path%>/manager/confinfo/sms/testsms.jsp', [{text:'<view:LanguageTag key="common_syntax_sure"/>',onclick:FT.buttonAction.okClick},{text:'<view:LanguageTag key="common_syntax_close"/>',onclick:FT.buttonAction.cancelClose}], true, 500, 200);
	}
	
	//校验短信网关配置名称
	 function check(){
        var smsid = $("#smsid").val();	
		var smsname = $("#smsname").val();	
		var hidsmsname = $("#hidsmsname").val();
		
		if(hidsmsname != smsname) {
			validateSmsName();
		}else {
			if (smsid == null || "" == smsid) {
				validateSmsName();
			} else {
				$("#smsname").formValidator({onFocus:'<view:LanguageTag key="sms_vd_name_show"/>',onCorrect:"OK"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="sms_vd_name_err"/>'},onError:'<view:LanguageTag key="sms_vd_name_err_1"/>'});
			}
		}
	 }
	
	//校验网关配置名称
	function validateSmsName() {
		jQuery("#smsname").ajaxValidator({
			async:true,
			type:"POST",
			url:"<%=path%>/manager/confinfo/sms/smsInfo!isExist.action",
			dataType:"html",
			success:function(data){
		       if(data =='false') {return false;}
			   return true;
			},
			buttons:$("#saveBt"),
			error:function(jqXHR, textStatus, errorThrown){
				$.messager.alert('<view:LanguageTag key="common_vd_warning"/>','<view:LanguageTag key="common_vd_server_is_busy"/>','warning');
			},
			onError:'<view:LanguageTag key="common_vd_already_exists"/>',
			onWait:'<view:LanguageTag key="common_vd_checking_exists"/>'
		});
	}
	
	
   //测试短信网关通道是否通畅
	function testSend(){
		$('#addSMSForm').ajaxSubmit({
		    type:"post",
		    async:false,
		    url:"<%=path%>/manager/confinfo/sms/smsInfo!testSendSms.action",
		    dataType : "json",
		    success:function(msg){
		      if(msg.errorStr=='success'){
				 $.ligerDialog.confirm('<view:LanguageTag key="sms_vd_whether_test_succ"/>','<view:LanguageTag key="common_syntax_confirm"/>',function(sel){
				 	if(sel) {
				 	    $("#sendresult").val(msg.object);
				 	    // 如果发送结果大于255个长度 就提示失败
				 		if(msg.object.length > 255){
				 			FT.toAlert('error','<view:LanguageTag key="sms_vd_not_save_tip"/>',null);
				 		}else{
				 		    waitW.close();
				 		    $("#saveBnt").show();
						}
					}
				 });
  			  }else{
		         FT.toAlert(msg.errorStr,msg.object,null);
		      }
		    }
		 });   
	}
	
	//修改保存短信网关配置
	function save(smsid){
		var urlStr;
		if(smsid ==''|| smsid==null){
			urlStr = "<%=path%>/manager/confinfo/sms/smsInfo!add.action";
		}else{
			urlStr = "<%=path%>/manager/confinfo/sms/smsInfo!modify.action";
		}
		
		var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
		$("#addSMSForm").ajaxSubmit({
		   async:true,
		   dataType : "json",  
		   type:"POST", 
		   url : urlStr,
		   success:function(msg){
				if(msg.errorStr == 'success'){ 
				     $.ligerDialog.success(msg.object,'<view:LanguageTag key="common_syntax_tip"/>',function(){
				     	window.location.href="<%=path%>/manager/confinfo/sms/list.jsp";
				     });
				}else{
				     FT.toAlert(msg.errorStr,msg.object, null);
				}
				ajaxbg.hide();
		   }
	   }); 
	}
	
	//返回操作
	function goBack() {
		window.location.href="<%=path%>/manager/confinfo/sms/list.jsp";
	}
</script>
</head>

 <body onresize="initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv')">
 <div id="background"  class="background"  style="display: none; "></div>
 <div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
 
  <form id="addSMSForm" method="post" action="">
  	<input type="hidden" id="smsid" name="smsInfo.id" value="${smsInfo.id}"/>
   	<input type="hidden" id="hidsmsname" value="${smsInfo.smsname}" />
   	<input type="hidden" id="callPhone" name="smsInfo.callPhone" />
   	<input type="hidden" id="enabled" name="smsInfo.enabled" value="${smsInfo.enabled}" />
   	<input type="hidden" id="message" name="smsInfo.message" />
     <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td width="100%" valign="top">
		<ul id="menu">
		  <li class="tabFocus" style="cursor: default"><view:LanguageTag key="sms_base_info_conf"/></li>
		  <li style="cursor: default"><view:LanguageTag key="sms_send_param_conf"/></li>
	    </ul>
	    <ul id="content"> 
	      <li class="conFocus" id="0">
	      	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="topTableBg">
			    <tr>
			      <td width="98%" background="<%=path%>/images/manager/mgr_r7_c8.png"> 
			        <span class="topTableBgText"><view:LanguageTag key="sms_base_info_conf"/></span>
			      </td>
			      <td width="2%" align="right">
			      </td>
			    </tr>
			  </table>
	      	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable" >
             <tr>
               <td width="25%" align="right"><view:LanguageTag key="sms_name"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
               <td width="35%">
                   <input type="text" id="smsname" name="smsInfo.smsname" value="${smsInfo.smsname}" onchange="check();"  class="formCss100"/>
               </td>
               <td width="40%" class="divTipCss"><div id="smsnameTip"></div></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="sms_sendtype"/><view:LanguageTag key="colon"/></td>
               <td><select id="sendtype" name="smsInfo.sendtype" class="select100">
				  <option value="0" <c:if test="${smsInfo.sendtype eq 0}">selected="selected"</c:if>>GET</option>
				  <option value="1" <c:if test="${smsInfo.sendtype eq 1}">selected="selected"</c:if>>POST</option>
		    	</select></td>
               <td class="divTipCss"><div id="sendtypeTip"></div></td>
             </tr>
             
             <tr>
               <td align="right"><view:LanguageTag key="sms_host"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
               <td><input type="text" id="host" name="smsInfo.host" class="formCss100" value="${smsInfo.host}" /></td>
               <td class="divTipCss"><div id="hostTip"></div></td>
             </tr>
             <tr id="httpsTR">
               <td align="right"><view:LanguageTag key="sms_cer_https"/><view:LanguageTag key="colon"/></td>
               
                <td id="fileTd">
		        	<input type="text" id="showVal" class="formCss100" onresize="initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv')"/>
			        &nbsp;&nbsp;<div style="z-index:10;display: inline;"><span class="Button" id="showDiv"><a id="setShowValInp" href="javascript:"><view:LanguageTag key="common_syntax_select"/></a></span></div>
			        <div id="fileDiv" style="position:absolute;margin:0 0 0 -38px;z-index:50;filter:alpha(opacity=0);moz-opacity: 0;-khtml-opacity: 0;opacity:0;">
			        <input name="cerhttps" id="cerhttps"  type="file" size="1" style="height: 24px;" onchange="setFullPath(this,'showVal');"/></div>
		        </td>
               
               <td class="divTipCss"><div id="cerhttpsTip"></div></td>
             </tr>
              <tr>
               <td align="right"><view:LanguageTag key="sms_username"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
               <td><input type="text" id="username" name="smsInfo.username" value="${smsInfo.username}" class="formCss100" /></td>
                <td class="divTipCss"><div id="usernameTip"></div></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="sms_pwd"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
               <td><input onpaste="return false" type="password" id="password" name="smsInfo.pwd" value="${smsInfo.pwd}" class="formCss100" /></td>
               <td class="divTipCss"><div id="passwordTip"></div></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="sms_conf_pwd"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
               <td><input onpaste="return false" type="password" id="pwdconf"  value="${smsInfo.pwd}" class="formCss100" /></td>
               <td class="divTipCss"><div id="pwdconfTip"></div></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="sms_retry_send"/><view:LanguageTag key="colon"/></td>
               <td><input type="text" id="retrysend" name="smsInfo.retrysend" value="${empty smsInfo.retrysend ? 0 : smsInfo.retrysend}" class="formCss100" /></td>
                <td class="divTipCss"><div id="retrysendTip"></div></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="sms_priority"/><view:LanguageTag key="colon"/></td>
               <td><select id="priority" name="smsInfo.priority" class="select100">
				  	<option value="0" <c:if test="${smsInfo.priority == 0}">selected</c:if>><view:LanguageTag key="sms_low_grade"/></option>
				  	<option value="1" <c:if test="${smsInfo.priority == 1}">selected</c:if>><view:LanguageTag key="sms_middle_grade"/></option>
				  	<option value="2" <c:if test="${smsInfo.priority == 2}">selected</c:if>><view:LanguageTag key="sms_top_grade"/></option>
		   		 </select>
		   	 	</td>
               <td class="divTipCss"><div id="priorityTip"></div></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="sms_descp"/><view:LanguageTag key="colon"/></td>
               <td>
               	   <textarea id="descp" name="smsInfo.descp" class="textarea100">${smsInfo.descp}</textarea>
               </td>
               <td class="divTipCss"><div id="descpTip"></div></td>
             </tr> 
             <tr>
				<td>&nbsp;</td>
				<td>
					<a href="#" id="nextstep" class="button"><span><view:LanguageTag key="common_syntax_nextStep"/></span></a>
					<a href="javascript:goBack();" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a>
				</td>
			 </tr>
            </table>
	      </li>
	      
	      <li id="1">
	      	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="topTableBg">
			    <tr>
			      <td width="98%" background="<%=path%>/images/manager/mgr_r7_c8.png"> 
			        <span class="topTableBgText"><view:LanguageTag key="sms_send_param_conf"/></span>
			      </td>
			      <td width="2%" align="right">
			      </td>
			    </tr>
			  </table>
	      	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
             <tr>
               <td width="25%" align="right"><view:LanguageTag key="sms_account_attr"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
               <td width="35%"><input type="text" id="accountattr" name="smsInfo.accountattr" class="formCss100" value="${empty smsInfo.accountattr ? "username" : smsInfo.accountattr}"/></td>
               <td width="40%" class="divTipCss"><div id ="accountattrTip"></div></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="sms_pwd_attr"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
               <td><input type="text" id="passwdattr" name="smsInfo.passwdattr" class="formCss100" value="${empty smsInfo.passwdattr ? "password" : smsInfo.passwdattr}"/></td>
               <td class="divTipCss"><div id ="passwdattrTip"></div></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="sms_phone_attr"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
               <td><input type="text" id="phoneattr" name="smsInfo.phoneattr" class="formCss100" value="${empty smsInfo.phoneattr ? "mobile" : smsInfo.phoneattr}"/></td>
               <td class="divTipCss"><div id ="phoneattrTip"></div></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="sms_message_attr"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
               <td><input type="text" id="messageattr" name="smsInfo.messageattr" class="formCss100" value="${empty smsInfo.messageattr ? "content" : smsInfo.messageattr}"/></td>
               <td class="divTipCss"><div id ="messageattrTip"></div></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="sms_add_param"/><view:LanguageTag key="colon"/></td>
               <td><input type="text" id="addparam" name="smsInfo.paramannex" class="formCss100" value="${empty smsInfo.paramannex ? "" : smsInfo.paramannex}"/></td>
               <td class="divTipCss"><div id ="addparamTip"></div></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="sms_send_result"/><view:LanguageTag key="colon"/></td>
               <td><input type="text" id="sendresult" name="smsInfo.sendresult" class="formCss100" value="${smsInfo.sendresult}"/></td>
               <td class="divTipCss"><div id ="sendresultTip"></div></td>
             </tr>
             <tr>
             	<td align="right"></td>
		        <td align="right">
		        	<a href="javascript:stepController(0)"   class="button"><span><view:LanguageTag key="common_syntax_preStep"/></span></a>
		        	<a href="#" id="testBnt" class="button"><span><view:LanguageTag key="common_syntax_test_conn"/></span></a>
		        	<a href="#" id="saveBnt" style="display:none" class="button" onclick="save('${smsInfo.id}');"><span><view:LanguageTag key="common_syntax_save"/></span></a>
		        </td>
		        <td align="right"></td>
		     </tr>
            </table>
	      </li>
	      
	    </ul>
	   </td>
      </tr>
    </table>
   </form>
</body>
</html>