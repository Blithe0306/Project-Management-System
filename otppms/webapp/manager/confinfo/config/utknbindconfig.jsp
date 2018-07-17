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
        
        var mtks = '${userConfInfo.maxbindtokens}';
        var musers = '${userConfInfo.maxbindusers}';
        if (mtks==1 && musers ==1) {
        	$("tr[id='tkbindischangeorgTR']").show();
        	$("tr[id='tkunbindischangeorgTR']").show();
        }else {
        	$("tr[id='tkbindischangeorgTR']").hide();
        	$("tr[id='tkunbindischangeorgTR']").hide();
        }

        if (musers ==1) {
        	$("tr[id='uschangeistknTR']").show();
        	$("tr[id='usunbindistknTR']").show();
        }else {
        	$("tr[id='uschangeistknTR']").hide();
        	$("tr[id='usunbindistknTR']").hide();
        }
        
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
			
 	  //绑定用户和令牌
	  $('#max_bind_tokens').formValidator({onFocus:'<view:LanguageTag key="userconf_vd_max_bind_tks_show"/>',onCorrect:"OK"}).inputValidator({min:1, max:99, type:"number",onError:'<view:LanguageTag key="userconf_vd_max_bind_tks_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'})
	     .functionValidator({
				fun:function(val){
					var mbindu = $('#max_bind_users').val();
					if(val ==1 && mbindu == 1) {
						$("tr[id='tkbindischangeorgTR']").show();
						$("tr[id='tkunbindischangeorgTR']").show();
					}else {
						$("tr[id='tkbindischangeorgTR']").hide();
						$("tr[id='tkunbindischangeorgTR']").hide();
						$("input[name=userConfInfo.tkbindischangeorg][value=0]").attr("checked",true);
						$("input[name=userConfInfo.tkunbindischangeorg][value=0]").attr("checked",true);
					}
					if(mbindu == 1) {
						$("tr[id='uschangeistknTR']").show();
		            	$("tr[id='usunbindistknTR']").show();
					}else {
						$("tr[id='uschangeistknTR']").hide();
		            	$("tr[id='usunbindistknTR']").hide();
		            	$("input[name=userConfInfo.replaceselect][value=0]").attr("checked",true);
						$("input[name=userConfInfo.unbindselect][value=0]").attr("checked",true);
					}
					return true;
				}
			
		 });  
	  $('#max_bind_users').formValidator({onFocus:'<view:LanguageTag key="userconf_vd_max_bind_users_show"/>',onCorrect:"OK"}).inputValidator({min:1, max:99, type:"number",onError:'<view:LanguageTag key="userconf_vd_max_bind_tks_err"/>'}).regexValidator({regExp:"intege1",dataType:"enum",onError:'<view:LanguageTag key="common_number_validate"/>'})
	     .functionValidator({
				fun:function(val){
					var mbindtk = $('#max_bind_tokens').val();
					if(val ==1 && mbindtk == 1) {
						$("tr[id='tkbindischangeorgTR']").show();
						$("tr[id='tkunbindischangeorgTR']").show();
					}else {
						$("tr[id='tkbindischangeorgTR']").hide();
						$("tr[id='tkunbindischangeorgTR']").hide();
						$("input[name=userConfInfo.tkbindischangeorg][value=0]").attr("checked",true);
						$("input[name=userConfInfo.tkunbindischangeorg][value=0]").attr("checked",true);
					}
					if(val == 1) {
						$("tr[id='uschangeistknTR']").show();
		            	$("tr[id='usunbindistknTR']").show();
					}else {
						$("tr[id='uschangeistknTR']").hide();
		            	$("tr[id='usunbindistknTR']").hide();
						$("input[name=userConfInfo.replaceselect][value=0]").attr("checked",true);
						$("input[name=userConfInfo.unbindselect][value=0]").attr("checked",true);
					}
					return true;
				}
			
		 });  
	     
	     $('#add_user_when_bind').formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
         $("input[name='userConfInfo.unbindselect']").formValidator({tipID:"unbindselectTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,max:1, onError: '<view:LanguageTag key="common_vd_please_sel"/>'});
         $("input[name='userConfInfo.replaceselect']").formValidator({tipID:"replaceselectTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,max:1, onError: '<view:LanguageTag key="common_vd_please_sel"/>'});

		 //令牌绑定解绑配置
	     $(":radio[name='userConfInfo.tkbindischangeorg']").formValidator({tipID:"tkbindischangeorgTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:1,max:1,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
	     $(":radio[name='userConfInfo.tkunbindischangeorg']").formValidator({tipID:"tkunbindischangeorgTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:1,max:1,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
 		 
	}
	
	// 保存数据
	function savaData(){
    	var url = "<%=path%>/manager/confinfo/config/userConfAction!modify.action";
		var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
		$("#userconfForm").ajaxSubmit({
		   async:true,
		   dataType : "json",  
		   type:"POST", 
		   url : url,
		   data:{"oper" : "utknconf"},
		   success:function(msg){
				if(msg.errorStr == 'success'){ 
				     $.ligerDialog.success(msg.object,'<view:LanguageTag key="common_syntax_tip"/>',function(){
				     	 window.location.reload();
				     });
				}else{
				     FT.toAlert(msg.errorStr,msg.object, null);
				}
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
  <form id="userconfForm" method="post" action="" name="userconfForm">
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="topTableBg">
      <tr>
        <td width="98%"><span class="topTableBgText"><view:LanguageTag key="common_menu_config_usertkn_bind"/></span></td>
        <td width="2%" align="right">
      	 <!--	<a href="javascript:openHelpWin('<%=path%>/manager/help/confighelp.html#387','<%=path%>/manager/common/js/ligerUI/skins/Gray/css/dialog.css');">
      		<img src="<%=path%>/images/icon/help_1.png" width="16" height="16" hspace="5" title='<view:LanguageTag key="common_syntax_help"/>' align="absmiddle" />
      		</a> -->
        </td>
      </tr>
    </table>  
   <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
      <tr>
        <td valign="top">
		    <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="ulOnInsideTable">
				<tr>
	              <td width="30%" align="right" height="20"><view:LanguageTag key="userconf_max_bind_tks"/><view:LanguageTag key="colon"/></td>
	              <td width="30%" align="left"  height="20"><div class="tableTDivLeft"><input type="text" id="max_bind_tokens" name="userConfInfo.maxbindtokens" value="${userConfInfo.maxbindtokens}" class="formCss100" /></div>
	               </td>
	               <td width="40%" class="divTipCss"><div id="max_bind_tokensTip"></div></td>
	            </tr>
	            
	             <tr>
	             	<td align="right" height="20"><view:LanguageTag key="userconf_adduser_when_bind"/><view:LanguageTag key="colon"/></td>
	             	<td align="left" height="20">
		                
		                <input type="radio" id="adduserwhenbind0" name="userConfInfo.adduserwhenbind" value="y"
				        	<c:if test="${userConfInfo.adduserwhenbind == 'y'}">checked</c:if>
				        /><view:LanguageTag key="common_syntax_yes"/> &nbsp;&nbsp;&nbsp;&nbsp;
				        <input type="radio" id="adduserwhenbind1" name="userConfInfo.adduserwhenbind" value="n"  
				        <c:if test="${userConfInfo.adduserwhenbind=='n'}">checked</c:if>
				        /><view:LanguageTag key="common_syntax_no"/> &nbsp;&nbsp;&nbsp;&nbsp;
		             </td>
		             <td class="divTipCss"><div id="adduserwhenbindTip"></div></td>
	            </tr>
	           
	            <tr>
	             	<td align="right" height="20"><view:LanguageTag key="userconf_authotp_when_bind"/><view:LanguageTag key="colon"/></td>
	             	<td align="left" height="20">
		                <input type="radio" id="authotpwhenbind0" name="userConfInfo.authotpwhenbind" value="1"
				        	<c:if test="${userConfInfo.authotpwhenbind == '1'}">checked</c:if>
				        /><view:LanguageTag key="common_syntax_need"/> &nbsp;&nbsp;&nbsp;&nbsp;
				        <input type="radio" id="authotpwhenbind1" name="userConfInfo.authotpwhenbind" value="2"
				        	<c:if test="${userConfInfo.authotpwhenbind == '2'}">checked</c:if>
				        /><view:LanguageTag key="common_syntax_optional"/> &nbsp;&nbsp;&nbsp;&nbsp;
				        <input type="radio" id="authotpwhenbind2" name="userConfInfo.authotpwhenbind" value="0"  
				        <c:if test="${userConfInfo.authotpwhenbind=='0'}">checked</c:if>
				        /><view:LanguageTag key="common_syntax_no_need"/> &nbsp;&nbsp;&nbsp;&nbsp;
		             </td>
		             <td class="divTipCss"><div id="authotpwhenbindTip"></div></td>
	            </tr>
	             
	            <tr>
	              <td align="right" height="20"><view:LanguageTag key="userconf_max_bind_users"/><view:LanguageTag key="colon"/></td>
	              <td align="left" height="20"><div class="tableTDivLeft"><input type="text" id="max_bind_users" name="userConfInfo.maxbindusers" value="${userConfInfo.maxbindusers}" class="formCss100" /></div></td>
	              <td class="divTipCss"><div id="max_bind_usersTip"></div></td>
	            </tr>
	            
	            <tr id="tkbindischangeorgTR">
				 <td align="right"><view:LanguageTag key="userconf_tkbind_ischange_org"/><view:LanguageTag key="colon"/></td>
				 <td>
					<input type="radio" name="userConfInfo.tkbindischangeorg" id="tkbindischangeorg"  value="1"
		            <c:if test="${userConfInfo.tkbindischangeorg =='1'}">checked</c:if>/><view:LanguageTag key="common_syntax_yes"/> &nbsp;&nbsp;&nbsp;&nbsp;
		        	<input type="radio" name="userConfInfo.tkbindischangeorg" id="tkbindischangeorg1" value="0"
		            <c:if test="${userConfInfo.tkbindischangeorg =='0'}">checked</c:if>/><view:LanguageTag key="common_syntax_no"/>  
				 </td>
				 <td class="divTipCss"><div id="tkbindischangeorgTip"></div></td>
			 	</tr>
             	<tr id="tkunbindischangeorgTR">
				 <td align="right"><view:LanguageTag key="userconf_tkunbind_ischange_org"/><view:LanguageTag key="colon"/></td>
				 <td>
					<input type="radio" name="userConfInfo.tkunbindischangeorg" id="tkunbindischangeorg"  value="1"
		            <c:if test="${userConfInfo.tkunbindischangeorg =='1'}">checked</c:if>/><view:LanguageTag key="common_syntax_yes"/> &nbsp;&nbsp;&nbsp;&nbsp;
		        	<input type="radio" name="userConfInfo.tkunbindischangeorg" id="tkunbindischangeorg1" value="0"
		            <c:if test="${userConfInfo.tkunbindischangeorg =='0'}">checked</c:if>/><view:LanguageTag key="common_syntax_no"/> 
				 </td>
				 <td class="divTipCss"><div id="tkunbindischangeorgTip"></div></td>
			 	</tr>
		      
		      	<tr id="usunbindistknTR">
	             	<td align="right" height="20"><view:LanguageTag key="userconf_unbind_select"/><view:LanguageTag key="colon"/></td>
	             	<td align="left" height="20">
			            <input type="radio" id="unbindselect1" name="userConfInfo.unbindselect" value="1"  
				        <c:if test="${userConfInfo.unbindselect eq 1 }">checked</c:if>
				        /><view:LanguageTag key="common_syntax_yes"/> &nbsp;&nbsp;&nbsp;&nbsp;
			            <input type="radio" id="unbindselect0" name="userConfInfo.unbindselect" value="0"
				        	<c:if test="${userConfInfo.unbindselect eq 0 }">checked</c:if>
				        /><view:LanguageTag key="common_syntax_no"/> &nbsp;&nbsp;&nbsp;&nbsp;
		             </td>
		             <td class="divTipCss"><div id="unbindselectTip"></div></td>
	            </tr>
	            <tr id="uschangeistknTR">
	             	<td align="right" height="20"><view:LanguageTag key="userconf_replace_select"/><view:LanguageTag key="colon"/></td>
	             	<td align="left" height="20">
			            <input type="radio" id="replaceselect0" name="userConfInfo.replaceselect" value="0"
				        	<c:if test="${userConfInfo.replaceselect eq 0 }">checked</c:if>
				        /><view:LanguageTag key="user_tkn_rep_continue_use"/> &nbsp;&nbsp;&nbsp;&nbsp;
				        <input type="radio" id="replaceselect1" name="userConfInfo.replaceselect" value="1"  
				        <c:if test="${userConfInfo.replaceselect eq 1 }">checked</c:if>
				        /><view:LanguageTag key="tkn_comm_disable"/> &nbsp;&nbsp;&nbsp;&nbsp;
				        <input type="radio" id="replaceselect2" name="userConfInfo.replaceselect" value="2"  
				        <c:if test="${userConfInfo.replaceselect eq 2 }">checked</c:if>
				        /><view:LanguageTag key="tkn_comm_invalid"/>&nbsp;&nbsp;&nbsp;&nbsp;
		             </td>
		             <td class="divTipCss"><div id="replaceselectTip"></div></td>
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