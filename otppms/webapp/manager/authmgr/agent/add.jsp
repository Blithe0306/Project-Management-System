<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%@page import="com.ft.otp.util.tool.DateTool"%>
<%
	String path = request.getContextPath();
	String time3Str = DateTool.getCurTimeLastThreeDay();
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
    <link rel="stylesheet" id="openwincss" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
    
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/datepicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>    
    <script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>
	    
    <script language="javascript" type="text/javascript">
		var confIdIsOK = true;
		$(function() {
		
		  $("tr[id='confListTR']").hide();
		  $("tr[id='graceperiodTR']").hide();
		  $("tr[id='expiredTR']").hide();
		
	      $.formValidator.initConfig({submitButtonID:"addobj",debug:true,
			onSuccess:function(){
			    addObj('${agentInfo.agentipaddr}','hostKey');
			},
			onError:function(){
				return false;
			}});
			$("#agentIpAddr").formValidator({onFocus:'<view:LanguageTag key="agent_vd_agentip_show"/>',onCorrect:"OK"}).inputValidator({min:1,onError:'<view:LanguageTag key="agent_vd_agentip_err"/>'}).functionValidator({fun:checkIpAddr,onError:'<view:LanguageTag key="agent_vd_agentip_err_1"/>'})
			 .ajaxValidator({
				dataType:"html",
				async:true,
				url:"<%=path%>/manager/authmgr/agent/authAgent!findIpIfExist.action",
				success:function(data){
		            if(data == 'success'){
		            	return true;
		            }else{
						return false;
					}
				},
				buttons:$("#addobj"),
				error:function(jqXHR, textStatus, errorThrown){
						$.messager.alert('<view:LanguageTag key="common_vd_warning"/>','<view:LanguageTag key="common_vd_server_is_busy"/>','warning');
				},
				onError:'<view:LanguageTag key="common_vd_already_exists"/>',
				onWait:'<view:LanguageTag key="common_vd_checking_exists"/>'
			});
			
			
			//$("#agentname").formValidator({onFocus:'<view:LanguageTag key="agent_vd_agentname_show"/>',onCorrect:"OK"}).inputValidator({min:4, max:32,onError:'<view:LanguageTag key="agent_vd_agentname_err"/>'});
			
			var agentname = $("#agentname").val();
			$("#agentname").formValidator({onFocus:'<view:LanguageTag key="agent_vd_agentname_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="agent_vd_agentname_err"/>'},onError:'<view:LanguageTag key="agent_vd_agentname_err_1"/>'}).functionValidator({
				fun:function(agentname){
			    if(g_invalid_char_js(agentname)){
			       return "<view:LanguageTag key="agent_vd_agentname_err_2"/>";
			    }
			    return true;
			}});
			
			
			$("#pubkey").formValidator({onFocus:'<view:LanguageTag key="agent_vd_shared_key_show"/>',onCorrect:"OK"}).inputValidator({min:4, max:32,onError:'<view:LanguageTag key="agent_vd_shared_key_err"/>'});
			$("#confpubkey").formValidator({onFocus:'<view:LanguageTag key="agent_vd_confshared_key_show"/>',onCorrect:"OK"}).inputValidator({min:4,max:32, onError:'<view:LanguageTag key="agent_vd_shared_key_err"/>'}).compareValidator({desID:"pubkey",operateor:"=",onError:'<view:LanguageTag key="agent_vd_shared_key_not_same"/>'}); 
			
			$("#hostKey").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({max:100,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
			$("input[name='agenttype']").formValidator({tipID:"agenttypeTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:1,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
			$("#graceperiod").formValidator({onFocus:'<view:LanguageTag key="agent_vd_grace_period"/>',onCorrect:"OK"}).inputValidator({min:4,max:20, onError: '<view:LanguageTag key="agent_vd_grace_period_err"/>'}).functionValidator({
				fun:function(val){
		  			var now = getSysDate();
		  			if (val < now) {
		  			 	return '<view:LanguageTag key="agent_vd_period_date_err"/>';
		  			}
		  			return true;
		  			
				}
			});
			$("#descp").formValidator({onFocus:'<view:LanguageTag key="common_vd_descp_show"/>',onCorrect:"OK"}).inputValidator({max:255,onError:'<view:LanguageTag key="common_vd_descp_error"/>'});
			$("#agentname").focus();
			
			$("#graceperiod").unFormValidator(true);
			
			var checkvalue = '${agentInfo.agenttype}';
			//初始化已选择的选项
			  if (checkvalue != 0) {
			 	$('input[name="agenttype"]').each(function () {
					   var result = checkvalue & this.value;
					   if (result != 0) {
							$(this).attr("checked", true);
					   }
				});
				
				$('input[name="agenttype"]').each(function () {
			       if ($(this).attr('checked') == true) {
			       	  var val = parseInt($(this).attr('value'),10);
			          if (val == 4 || val == 8) {
			          	  changeType(val,0);
			          }
			       }
			    });
			  }	
		 })
	//校验agentname名称
	 function checkAgentname(){
			var agentname = $("#agentname").val();	
			var agentnameHid = $("#agentnameHid").val();
			if(agentname != agentnameHid) {
				validateAgentname();
			} else {
				$("#agentname").formValidator({onShow:'<view:LanguageTag key="agent_vd_agentname_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="agent_vd_agentname_err"/>'},onError:'<view:LanguageTag key="agent_vd_agentname_err_1"/>'});
			}
	 }
	 
	 //校验agentname是否存在
	 function validateAgentname() {
	 	$("#agentname").ajaxValidator({
			dataType:"html",
			async:true,
			url:"<%=path%>/manager/authmgr/agent/authAgent!findHostnameisExist.action",
			success:function(data){
			   if(data =='false') {return false;}
				return true;
			},
			buttons:$("#addBtn"),
			error:function(jqXHR, textStatus, errorThrown){
				$.messager.alert('<view:LanguageTag key="common_vd_warning"/>','<view:LanguageTag key="common_vd_server_is_busy"/>','warning');
			},
			onError:'<view:LanguageTag key="common_vd_already_exists"/>',
			onWait:'<view:LanguageTag key="common_vd_checking_exists"/>'
		});
	 }	 
        
	//添加 
	function addObj(agentipStr,obj){
		var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
        var length = $("#hostKey")[0].options.length;
        var cpage = 1;
		if(length > 0){
		 	//选中认证服务器Select列表
			selectObj(obj);
		}
		selectObj("hidHostIps");
		
		getCheckedVal();
		
		var isAdd = true;
		//save
		var url = "<%=path%>/manager/authmgr/agent/authAgent!add.action";
		if(agentipStr != '' && agentipStr != null){
			cpage = $("#cPage").val();
			url = "<%=path%>/manager/authmgr/agent/authAgent!modify.action?length="+length;
			isAdd = false;
		}
		
		var agenttype3 = $("#agenttype3").attr("checked");
		if(!agenttype3) {
			$('#graceperiod').val("1970-01-01 08:00:00");
		}
		$("#AgentForm").ajaxSubmit({
			async:true,    
			dataType : "json",  
			type:"POST", 
			url : url,
			success:function(msg){
				 ajaxbg.hide();
			     if(msg.errorStr == 'success'){
			     	if(isAdd){//添加
			     		FT.Dialog.confirm('<view:LanguageTag key="common_save_success_continue_add"/>','<view:LanguageTag key="common_syntax_confirm"/>', function(sel){
	           			 	if(sel) {
			    				location.href = "<%=path%>/manager/authmgr/agent/add.jsp";
			    			}else{ // 关闭tab页面
			    				window.parent.removeTabItemF('040102');
			    			}
						});
			     	}else{// 编辑
			     		$.ligerDialog.success('<view:LanguageTag key="common_save_succ_tip"/>', '<view:LanguageTag key="common_syntax_tip"/>',function(){
			        		location.href = '<%=path%>/manager/authmgr/agent/list.jsp?cPage='+cpage;
			     		});
			     	}
			     }else{
				 	FT.toAlert(msg.errorStr,msg.object,null);
			     }
			}
		});
	}
	
	//获取认证代理类型值
	function getCheckedVal() {
		var checkedVal=0;
		$('input[name="agenttype"]').each(function () {
	       if ($(this).attr('checked') == true) {
	       	  var val = parseInt($(this).attr('value'),10);
	          checkedVal += val;
	       }
	    });
	    $("#agenttype").val(checkedVal);
	}
	
	// 选择认证服务器后将所选认证服务器添加到服务器列表
	function confirmSelects(jSel) {
	    var hostIpList = $("select[name='agentInfo.hostIps']");
		var hostIpvals = "";
		$("select[name='agentInfo.hostIps'] option").each(function(i,e){
			hostIpvals += e.value+",";
		});
		while(jSel[0]) {
			var hostIp = jSel.shift();
			if(hostIpvals.indexOf(hostIp.rid+",")<0){
			   hostIpList.append("<option value='" + hostIp.rid + "'>" + hostIp.rname + "</option>");
			}
			
		}
	}

	// 删除认证服务器列表中选定的服务器
	function delSelect() {
	    var selectedIps = $('#hostKey option:selected');
	    if(selectedIps.length<1){
	       FT.toAlert('warn','<view:LanguageTag key="common_syntax_check_need_del_date"/>',null);
	       return;
	    }
		$("#hostKey").children().filter('[selected]').remove();
	}
	
	function selectObj(obj){
		if (obj != -1){
			for (i=0; i<document.getElementById(obj).length; i++)
			document.getElementById(obj).options[i].selected = true;			 
		}
	}	
	
	//系统当前日期加3天后日期
	function dateaddday() {
		var url = '<%=path%>/manager/authmgr/agent/authAgent!getCurTimeLastThreeDay.action';
		$.post(url,
			function(msg){
				if(msg != ""){
					$("#graceperiod").val(msg);
				}else{
					$("#graceperiod").val('<%=time3Str%>');
				}
			}, "text"
		);
     }
     
     //获取系统当前时间
     function getSysDate() {
		var dateval = "";	
	    var url = '<%=path%>/manager/authmgr/agent/authAgent!getCurTimeLastThreeDay.action';
	       $.ajax({
			type: "POST",
		    url: url,
		    async: false,
		    data: {"flag" : "1"},
		    dataType: "text",
		    success: function(result){
				dateval = result;
			}
		});
		return dateval;
	}
	
	//设置checkbox状态
	function changeType(val,flag) {
		var agenttype3 = $("#agenttype3").attr("checked");
		var agenttype4 = $("#agenttype4").attr("checked");
		var typeVa = '${agentInfo.agenttype}';
		var type;
		if (val == 4) {
			if (agenttype3) {
				$('#agenttype4').attr("disabled", true);
				if('${agentInfo.agentipaddr}' != "" && typeVa != 1073741824){ // 1073741824代表普通认证代理的值，编辑时判断是否只有普通认证代理选中；
					$('#agenttype3').attr("disabled", true);
				}
				$("tr[id='confListTR']").show();
				var gratime = '${agentInfo.graceperiodStr}';
				if ('${agentInfo.graceperiod}' == 0) {
					$("tr[id='graceperiodTR']").show();
					dateaddday(); 
					$("#graceperiod").unFormValidator(false); 
				}else {
					var now = getSysDate();
					if(gratime < now) {
						$("tr[id='graceperiodTR']").hide();
			  			$("tr[id='expiredTR']").show();
			  			$("#graceperiod").unFormValidator(true);
					}else {
						$("tr[id='expiredTR']").hide();
						$("tr[id='graceperiodTR']").show();
						$("#graceperiod").unFormValidator(false); 
					}
				}
				type = 0;
			} else {
				$('#agenttype4').attr("disabled", false);
				$("tr[id='confListTR']").hide();
				$("tr[id='graceperiodTR']").hide();
				$("tr[id='expiredTR']").hide();
				$("#graceperiod").unFormValidator(true); 
			}
		}else if (val == 8) {
			if (agenttype4) {
				$('#agenttype3').attr("disabled", true);
				if('${agentInfo.agentipaddr}' != "" && typeVa != 1073741824){ // 1073741824代表普通认证代理的值，编辑时判断是否只有普通认证代理选中；
					$('#agenttype4').attr("disabled", true);
				}
				$("tr[id='confListTR']").show();
				$("tr[id='graceperiodTR']").hide();
				$("#graceperiod").unFormValidator(true); 
			  	$("tr[id='expiredTR']").hide();
				type = 1;
			} else {
				$('#agenttype3').attr("disabled", false);
				$("tr[id='confListTR']").hide();
				$("tr[id='graceperiodTR']").hide();
			  	$("tr[id='expiredTR']").hide();
			  	$("#graceperiod").unFormValidator(true); 
				
			}
		}
		 
		var confid = $("#confid").val();
		var agentId = $("#agentId").val();
		var url_ = "<%=path%>/manager/authmgr/agent/authAgent!queryConfList.action";
		$.ajax({
			type: "POST",
			url: url_,
			async: false,
			data: {'type':type, 'confid':confid, 'agentId':agentId},
			dataType: "json",	    
			success: function(msg){
				if(msg.errorStr == 'success'){
					 document.getElementById('confListDiv').innerHTML = msg.object;
					 $("#agentconfid").formValidator({onFocus:'<view:LanguageTag key="agent_vd_agentconf"/>',onCorrect:"OK"}).functionValidator({
					 fun:function(val,elem){
						var confVal = $("#agentconfid").val();
					    if (agenttype3 || agenttype4) {
					    	if(confVal==0){
					    	   return '<view:LanguageTag key="agent_vd_agentconf"/>';
					    	}else{
					    	   return true;
					    	}
					    }else if(!agenttype3&&!agenttype4){
					        return true;
					    }
					}
					});
				}else {
					FT.toAlert(msg.errorStr,msg.object,null);
				}
			}
		});
	}
	//编辑时校验认证代理配置ID是否选择
	function setSelect() {
		$('input[name="agenttype"]').each(function () {
		       if ($(this).attr('checked') == true) {
		       	  var val = parseInt($(this).attr('value'),10);
		          if (val == 4 || val == 8) {
					$("#agentconfid").formValidator({onFocus:'<view:LanguageTag key="agent_vd_agentconf"/>',onCorrect:"OK"}).inputValidator({min:1,onError:'<view:LanguageTag key="agent_vd_agentconf"/>'});
		          }
		       }
		});
	}
 
	 //认证服务器选择
	function queryObj(){
		FT.openWinMiddle('<view:LanguageTag key="common_menu_auth_server"/>',"<%=path%>/manager/authmgr/agent/sel_servers.jsp",false);  
	}
	
	//返回操作
	function goBack() {
		var currentPage = $("#cPage").val();
		window.location.href = '<%=path%>/manager/authmgr/agent/list.jsp?currentPage=' + currentPage;
	}	
	
	</script>
  </head>
  
  <body style="overflow:auto; overflow-x:hidden">
    <div id="background"  class="background"  style="display: none; "></div>
  <div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
	  <form id="AgentForm" method="post" action="">
	  	<input type="hidden" value="<%=path %>" id="contextPath" />
		<input id="cPage" type="hidden" value="${param.currentPage}" />
		<input type="hidden" id="agenttype" name="agentInfo.agenttype" value="" />
		<input type="hidden" id="confid" value="${agentInfo.agentconfid}" />
		<input type="hidden" id="agentId" value="${agentInfo.agentipaddr}" />
		<input type="hidden" id="enabled" name="agentInfo.enabled"  value="${agentInfo.enabled}" />
	    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="topTableBg">
	      <tr>
	        <td width="98%">
	        	<span class="topTableBgText">
	            <c:if test="${empty agentInfo.agentipaddr}"><view:LanguageTag key="auth_agent_add"/></c:if>
	            <c:if test="${not empty agentInfo.agentipaddr}"><view:LanguageTag key="auth_agent_edit"/></c:if>
	            </span>	        </td>
	        <td width="2%">
	        	<a href="javascript:addAdmPermCode('040102','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" width="16" height="16" hspace="6" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a>	        
	      	 	<!--<a href="javascript:openHelpWin('<%=path%>/manager/help/confighelp.html#371','<%=path%>/manager/common/js/ligerUI/skins/Gray/css/dialog.css');">
	      		<img src="<%=path%>/images/icon/help_1.png" width="16" height="16" hspace="5" title='<view:LanguageTag key="common_syntax_help"/>'/></a>--></td>
          </tr>
	    </table>   
	   <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="ulOnTable">
	      <tr>
	        <td valign="top">
			    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
			      <tr>
			        <td align="right"><view:LanguageTag key="auth_agent_agentname"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td>
			        <input type="text" name="agentInfo.agentname" class="formCss100" id="agentname" value="${agentInfo.agentname}" onchange="javascript:checkAgentname();"/>
			        <input type="hidden" id="agentnameHid" value="${agentInfo.agentname}"  />
			        </td>
			        <td class="divTipCss"><div id="agentnameTip"></div></td>
			      </tr>
			      <tr>
			        <td width="30%" align="right"><view:LanguageTag key="auth_agent_agentip"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td width="30%">
				        <c:if test="${empty agentInfo.agentipaddr}">
				        <input type="text" name="agentInfo.agentipaddr" class="formCss100" id="agentIpAddr" value="${agentInfo.agentipaddr}" />
				        </c:if>
				        <c:if test="${not empty agentInfo.agentipaddr}">
				        	${agentInfo.agentipaddr}
				        	<input type="hidden" name="agentInfo.agentipaddr" class="formCss100" id="hidagentIpAddr" value="${agentInfo.agentipaddr}" />
				        </c:if>
			        </td>
			        <td width="40%" class="divTipCss"><div id="agentIpAddrTip"></div></td>
			      </tr>
			       
			      <tr>
			        <td align="right"><view:LanguageTag key="auth_agent_shared_key"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td><input onpaste="return false" type="password" name="agentInfo.pubkey" class="formCss100" id="pubkey" value="${agentInfo.pubkey}"/></td>
			        <td class="divTipCss"><div id="pubkeyTip"></div></td>
			      </tr>
			      <tr>
			        <td align="right"><view:LanguageTag key="auth_agent_conf_shared_key"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td><input onpaste="return false" type="password"  class="formCss100" id="confpubkey" value="${agentInfo.pubkey}"/></td>
			        <td class="divTipCss"><div id="confpubkeyTip"></div></td>
			      </tr>
			     <tr>
			        <td align="right"><view:LanguageTag key="auth_agent_server_list"/><view:LanguageTag key="colon"/></td>
			        <td>        
				        <select id="hostKey" name="agentInfo.hostIps" multiple class="select100" style="height:50px;">
				          <c:forEach items="${agentInfo.hostIps}" var="hostkey">
				              <option value="${hostkey.hostipaddr}">${hostkey.hostipaddr}</option>
				          </c:forEach> 
				        </select>
				        <div style="display:none">
				        <select id="hidHostIps" name="agentInfo.hidHostIps" multiple class="select100" style="height:50px;">
				          <c:forEach items="${agentInfo.hidHostIps}" var="servInfo">
				              <option value="${servInfo.hostipaddr}">${servInfo.hostipaddr}</option>
				          </c:forEach> 
				        </select>
				        </div>
			        </td>
			        <td class="divTipCss"><div id="hostKeyTip"></div></td>
			      </tr>     
			      <tr>
			        <td align="right">&nbsp;</td>
			        <td>
				        <a href="javascript:queryObj();" id="selServer" class="button"><span><view:LanguageTag key="common_syntax_select"/></span></a>
						<a href="javascript:delSelect();" id="delSelServ" class="button"><span><view:LanguageTag key="common_syntax_delete"/></span></a>
					</td>
				    <td>&nbsp;</td>
			      </tr>
			      
			      <tr>
		            <td align="right" valign="top"><view:LanguageTag key="auth_agent_agent_type"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		            <td>
				        <input type="checkbox" id="agenttype3" name="agenttype" value="4" onclick="changeType(this.value);" />&nbsp;<view:LanguageTag key="auth_agent_wins_login_protect"/>&nbsp;&nbsp;&nbsp;&nbsp;<br/>
				        <input type="checkbox" id="agenttype4" name="agenttype" value="8" onclick="changeType(this.value);" />&nbsp;<view:LanguageTag key="auth_agent_linux_login_protect"/><br/>
				       <!-- <input type="checkbox" id="agenttype1" name="agenttype" value="1" />&nbsp;<view:LanguageTag key="auth_agent_ext_rad_client"/><br />
				        <input type="checkbox" id="agenttype2" name="agenttype" value="2" />&nbsp;<view:LanguageTag key="auth_agent_standard_rad_client"/><br />
				        <input type="checkbox" id="agenttype5" name="agenttype" value="16" />&nbsp;<view:LanguageTag key="auth_agent_iis_agent"/><br />
				        <input type="checkbox" id="agenttype6" name="agenttype" value="32" />&nbsp;<view:LanguageTag key="auth_agent_apache_agent"/><br />
				        <input type="checkbox" id="agenttype7" name="agenttype" value="64" />&nbsp;<view:LanguageTag key="auth_agent_soap_client"/><br />
				        <input type="checkbox" id="agenttype8" name="agenttype" value="128" />&nbsp;<view:LanguageTag key="auth_agent_http_client"/><br />
				        <input type="checkbox" id="agenttype9" name="agenttype" value="256" />&nbsp;<view:LanguageTag key="auth_agent_https_client"/> --> 
				        <input type="checkbox" id="agenttype10" name="agenttype" value="1073741824" />&nbsp;<view:LanguageTag key="auth_agent_other_proxy"/>
		            </td>
		            <td class="divTipCss"><div id="agenttypeTip"></div></td>
		          </tr>
		          
		          <tr id="confListTR">
	                <td align="right"><view:LanguageTag key="auth_agent_agentconf"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	                <td>
	                	<div id="confListDiv"></div>
			    	</td>
	                <td class="divTipCss"><div id="agentconfidTip"></div></td>
	              </tr>
	              
		          <tr id="graceperiodTR">
	                <td align="right"><view:LanguageTag key="auth_grace_period"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	                <td>
	                	<input type="text" id="graceperiod" name="agentInfo.graceperiodStr" value="<c:if test="${empty agentInfo.graceperiodStr}"><%=time3Str%></c:if><c:if test="${not empty agentInfo.graceperiodStr}">${agentInfo.graceperiodStr}</c:if>" onclick="WdatePicker({lang:'${language_session_key}', isShowClear:false})" readOnly="readOnly" class="formCss100" />
			    	</td>
	                <td class="divTipCss"><div id="graceperiodTip"></div></td>
	              </tr>
	              
		          <tr id="expiredTR">
	                <td align="right"><view:LanguageTag key="auth_grace_period"/><view:LanguageTag key="colon"/></td>
	                <td>
				        <view:LanguageTag key="auth_grace_period_invalid"/>
			    	</td>
	                <td></td>
	              </tr>
			      
			      <tr>
			        <td align="right"><view:LanguageTag key="common_syntax_desc"/><view:LanguageTag key="colon"/></td>
			        <td><textarea id="descp" name="agentInfo.descp" class="textarea100">${agentInfo.descp}</textarea></td>
			        <td class="divTipCss"><div id="descpTip"></div></td>
			      </tr>
			      <tr>
			        <td align="right"> </td>
			        <td> 
				        <a href="#" id="addobj" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
				        <c:if test="${not empty agentInfo.agentipaddr}"><a href="javascript:goBack();" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a></c:if>
			        </td>
			        <td></td>
			      </tr> 
			    </table>
	    	</td>
		  </tr>
	   </table>
	</form>
  </body>
</html>