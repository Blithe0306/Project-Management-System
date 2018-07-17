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
    <link rel="stylesheet" id="openwincss" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"/>
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
	$(function() {
		//绑定点击事件
		domClickEvent();
		//点击按钮弹出窗口，设置窗口样式，取消已绑定点击事件
		var cssurl = "<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"
        btnClickEvent("#saveBt",cssurl);
        
        // 日志定时删除是否启用单击按钮，隐藏显示操作
		$(":radio[name^='commInfo.logtimingenabled']").click(function(e){
			if($(this).val() === '1') { 
				$("tr[id='logdelTR']").show();
				$("#logtimingdelete").unFormValidator(false); 
			}else {
			 	$("tr[id='logdelTR']").hide();
			 	//$("#logtimingdelete").val("90");
			 	$("#logtimingdelete").unFormValidator(true);
			 	$("#isbaklog").attr("checked",false);
			 	$("#bakTip").hide();
			}
		});	
		
		// 是否显示备份路径提示
		$("#isbaklog").click(function(e){
			if(this.checked){
				$("#bakTip").show();
			}else{
				$("#bakTip").hide();
			}
		});
	
		checkInput();
	});
		 
	 //点击保存之前的校验
	 function checkInput(){
	  $.formValidator.initConfig({submitButtonID:"saveBt", 
			onSuccess:function(){
			   savaData();
			},
			onError:function(){
				return false;
			}
	  });
			
	   //系统配置
	   $("#sessioneffectivelytime").formValidator({onFocus:'<view:LanguageTag key="comm_vd_session_eff_time_show"/>',onCorrect:"OK"}).inputValidator({min:1,max:480,type:"number",onError:'<view:LanguageTag key="comm_vd_session_eff_time_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'});
	   
       $("#loglevel").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
       $("#logtimingdelete").formValidator({onFocus:'<view:LanguageTag key="comm_log_delete_days"/>',onCorrect:"OK"}).inputValidator({min:30,max:365,type:"number",onError:'<view:LanguageTag key="comm_log_delete_days_error"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'});	
       $("input[name='centerInfo.logtimingenabled']").formValidator({tipID:"logtimingenabledTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,max:1, onError: '<view:LanguageTag key="common_vd_please_sel"/>'});
       // 定时删除日志的显示判断
       if($("#logtimingenabled").attr("checked")){
			$("tr[id='logdelTR']").hide();
			//$("#logtimingdelete").val("90");
			$("#logtimingdelete").unFormValidator(true); 
			$("#bakTip").hide();
	   }
	   
	   if(!$("#isbaklog").attr("checked")){
	   		$("#bakTip").hide();
	   }else{
	   		$("#bakTip").show();
	   }
	   
       $("#langSel").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
       $("#sessioneffectivelytime").focus();
	}
	
	// 保存数据
	function savaData(){
		var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
    	var url = "<%=path%>/manager/confinfo/config/commonAction!modify.action";
		$("#commForm").ajaxSubmit({
		   async:true,
		   dataType : "json",  
		   type:"POST", 
		   url : url,
		   success:function(msg){
				FT.toAlert(msg.errorStr,msg.object, null);
				ajaxbg.hide();
		   }
	   });
	}
	//-->
	</script>
  </head>
  
  <body>
  <div id="background"  class="background"  style="display: none; "></div>
  <div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
  <input id="contextPath" type="hidden" value="<%=path%>" />
  <form id="commForm" method="post" action="" name="commForm">
    <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="topTableBg">
      <tr>
        <td width="98%"><span class="topTableBgText"><view:LanguageTag key="common_menu_config_other_comm"/></span></td>
        <td width="2%" align="right">
      	 <!-- <a href="javascript:openHelpWin('<%=path%>/manager/help/confighelp.html#387','<%=path%>/manager/common/js/ligerUI/skins/Gray/css/dialog.css');">
      		<img src="<%=path%>/images/icon/help_1.png" width="16" height="16" hspace="5" title='<view:LanguageTag key="common_syntax_help"/>' align="absmiddle" />
      		</a> -->
        </td>
      </tr>
    </table>  
   <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="ulOnInsideTable">
      <tr>
        <td valign="top">
		    <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		      <tr>
		        <td width="30%" align="right"><view:LanguageTag key="comm_session_eff_time"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		        <td width="30%">
		        	<input type="text" id="sessioneffectivelytime" name="commInfo.sessioneffectivelytime"  value="${commInfo.sessioneffectivelytime}" class="formCss100" />
		        </td>
		        <td width="40%" class="divTipCss"><div id="sessioneffectivelytimeTip"></div></td>
		      </tr>
		      <tr style="display:none">
		        <td align="right"><view:LanguageTag key="comm_log_level"/><view:LanguageTag key="colon"/></td>
		        <td>
					<select id="loglevel" name="commInfo.loglevel" class="select100">
						<option value="-1"
							<c:if test="${commInfo.loglevel==-1}">selected</c:if>><view:LanguageTag key="comm_no_record_log" />
						</option>
						<option value="0"
							<c:if test="${commInfo.loglevel==0}">selected</c:if>><view:LanguageTag key="comm_key_log_mode" />
						</option>
						<option value="1"
							<c:if test="${commInfo.loglevel==1}">selected</c:if>><view:LanguageTag key="comm_full_log_mode" />
						</option>
					</select>
				</td>
		        <td class="divTipCss"><div id="loglevelTip"></div></td>
		      </tr>
		      <tr>
	             <td align="right"><view:LanguageTag key="comm_def_lang"/><view:LanguageTag key="colon"/></td>
	             <td align="left">
				    	<select id="langSel" name="commInfo.defaultsystemlanguage" class="select100">
				          <view:LanguageSelectTag key="${commInfo.defaultsystemlanguage}" />
				        </select>
	              </td>
	              <td class="divTipCss"><div id="langSelTip"></div></td>
	          </tr>
	  		  <tr>
                 <td align="right"><view:LanguageTag key="comm_log_delete_enable_span"/><view:LanguageTag key="colon"/></td>
                 <td>
               		<input type="radio" id="logtimingenabled1" name="commInfo.logtimingenabled" value="1" 
                		<c:if test="${commInfo.logtimingenabled eq 1 }">checked</c:if> /><view:LanguageTag key="common_syntax_yes"/> &nbsp;&nbsp;
               		<input type="radio" id="logtimingenabled" name="commInfo.logtimingenabled" value="0"
               			<c:if test="${commInfo.logtimingenabled eq 0 }">checked</c:if> /><view:LanguageTag key="common_syntax_no"/>
			     </td>
                 <td class="divTipCss"><div id ="logtimingenabledTip"></div></td>
	          </tr>
	          <tr id="logdelTR">
	               <td align="right"><view:LanguageTag key="comm_log_delete_days_span" /><view:LanguageTag key="colon"/></td>
	               <td>
	               		<input type="text" id="logtimingdelete" name="commInfo.logtimingdelete" class="formCss100" style="width:80px" value="${commInfo.logtimingdelete}"/>
	               		&nbsp;&nbsp;<div style="display:inline"><input type="checkbox" id="isbaklog" name="commInfo.logisbak" style="vertical-align:middle;margin-right:3px" value="1" <c:if test="${commInfo.logisbak eq 1 }">checked</c:if> /><label style="vertical-align:middle;"><view:LanguageTag key="comm_log_delete_is_bak"/></label></div>
	               </td>
	               <td class="divTipCss"><div id ="logtimingdeleteTip"></div></td>
	          </tr>
	          <tr id="bakTip" style="display:none">
	          	<td>&nbsp;</td>
	          	<td colspan="2"><span class="text_Hong_Se"><view:LanguageTag key="common_syntax_tip"/><view:LanguageTag key="colon"/><view:LanguageTag key="common_bak_deletelog_tip"/></span></td>
	          </tr>
		      
		      <tr>
		        <td align="right"></td>
		        <td><a href="#" id="saveBt" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a></td>
		        <td></td>
		      </tr> 
		    </table>
        </td>
       </tr>
      </table> 
   </form>
  </body>
</html>