<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="view" uri="/WEB-INF/tld/view.tld"%>
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
	    <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	    <script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
		<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
		<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>
		<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
	    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
	    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>    
	    <script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>        
	    <script language="javascript" src="<%=path%>/manager/confinfo/usersource/js/usersource.js"></script> 
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
	   
	$(document).ready(function(){ 
		testLdapUSConnForm();
		testLdapUSNextForm();
		saveLdapUSForm();
		loadFieldMapping();
	 	if(!isEdit && usType == '1'){//添加
	 		var ldap_u = $('#ldap_user').val();
	 		if (ldap_u == '' || ldap_u == null) {
		    	$('#ldap_user').val('Administrator@www.example.com');
	 		}
	 		var ldap_bdn = $('#ldap_base_dn').val();
	 		if (ldap_bdn == '' || ldap_bdn == null) {
		    	$('#ldap_base_dn').val('CN=Users,DC=www,DC=example,DC=com');
	 		}
	 		var ldap_rdn = $('#ldap_root_dn').val();
	 		if (ldap_rdn == '' || ldap_rdn == null) {
		    	$('#ldap_root_dn').val('DC=www,DC=example,DC=com');
	 		}
	 		var ldap_f = $('#ldap_filter').val();
	 		if (ldap_f == '' || ldap_f == null) {
		    	$('#ldap_filter').val('(&(objectCategory=person)(objectClass=user))');
	 		}
		    $('#ldap_timeout').val('30');
		    $('#ldap_port').val('389');
		}
	})
	
	//校验公用方法提取,传入分组号
	function checkForm(groupId){
		//用户来源为ldap	
	    $("#ldap_domain").formValidator({validatorGroup:groupId,onFocus:'<view:LanguageTag key="usource_vd_db_ip"/>',onCorrect:"OK"}).functionValidator({
	    fun:function(ldap_domain){
	        var usType = $('#sourcetype').val(); 
	        if(usType=='1'){if(!checkIpAddr($.trim(ldap_domain))){return '<view:LanguageTag key="usource_vd_db_ip_err"/>';}}
	        return true;
	        }
	    });
		$("#ldap_port").formValidator({validatorGroup:groupId,onFocus:'<view:LanguageTag key="usource_vd_ldap_port"/>',onCorrect:"OK"}).functionValidator({
		fun:function(ldap_port){
	        var usType = $('#sourcetype').val(); 
	        if(usType=='1'){if(!checkPort($.trim(ldap_port))){return '<view:LanguageTag key="usource_vd_ldap_port_err"/>';}}
	         return true;
	      }
		 });
		$("#ldap_user").formValidator({validatorGroup:groupId,onFocus:'<view:LanguageTag key="usource_vd_ldap_account"/>',onCorrect:"OK"}).functionValidator({
		 fun:function(ldap_user){
	        var usType = $('#sourcetype').val(); 
	        if(usType=='1'){if(""==$.trim(ldap_user)){return '<view:LanguageTag key="usource_vd_ldap_is_not_null"/>';}}
	         return true;
	       }
		 });
 		$("#ldap_password").formValidator({validatorGroup:groupId,onFocus:'<view:LanguageTag key="usource_vd_ldap_pwd"/>',onCorrect:"OK"}).functionValidator({
 		 fun:function(ldap_password){
	        var usType = $('#sourcetype').val(); 
	        if(usType=='1'){ if(""==$.trim(ldap_password)){return '<view:LanguageTag key="usource_vd_ldap_is_not_null"/>';}}
	         return true;
	       }
 		 });
 		$("#ldap_base_dn").formValidator({validatorGroup:groupId,onFocus:'<view:LanguageTag key="usource_vd_ldap_find_dn"/>',onCorrect:"OK"}).functionValidator({
 		 fun:function(ldap_base_dn){
	        var usType = $('#sourcetype').val(); 
	        if(usType=='1'){if(""==$.trim(ldap_base_dn)){return '<view:LanguageTag key="usource_vd_ldap_is_not_null"/>';}} 
	        return true;
	      }
 		 });
 		 $("#ldap_root_dn").formValidator({validatorGroup:groupId,onFocus:'<view:LanguageTag key="usource_vd_ldap_root_dn"/>',onCorrect:"OK"}).functionValidator({
 		 fun:function(ldap_root_dn){
	        var usType = $('#sourcetype').val(); 
	        if(usType=='1'){if(""==$.trim(ldap_root_dn)){return '<view:LanguageTag key="usource_vd_ldap_is_not_null"/>';}} 
	        return true;
	      }
 		 });

		$("#ldap_filter").formValidator({validatorGroup:groupId,onFocus:'<view:LanguageTag key="usource_vd_ldap_find_filter"/>',onCorrect:"OK"}).functionValidator({
		 fun:function(ldap_filter){
	        var usType = $('#sourcetype').val(); 
	        if(usType=='1'){if(""==$.trim(ldap_filter)){return '<view:LanguageTag key="usource_vd_ldap_is_not_null"/>';}}
	         return true;
	      }
		 });
		$("#ldap_timeout").formValidator({validatorGroup:groupId,onFocus:'<view:LanguageTag key="usource_vd_conn_timeout"/>',onCorrect:"OK"}).functionValidator({
		 fun:function(ldap_timeout){
	        var usType = $('#sourcetype').val(); 
	        if(usType=='1'){if(!checkTextDataForNUMBER($.trim(ldap_timeout))){return '<view:LanguageTag key="usource_vd_conn_timeout_err"/>';}}
	         return true;
	      }
		 });
		$("#ldap_domain").focus();  
     } 
    
	//用户来源详细配置选项,点击"测试连接"按钮
	function testLdapUSConnForm(){
	      $.formValidator.initConfig({validatorGroup:"2",submitButtonID:'testLdapUSConn',debug:true,
			onSuccess:function(){
			    testUSConn();
			},
			onError:function(){
				return false;
			}});
		 checkForm('2');
	}	   
	
	//用户来源详细配置选项，点击"下一步"按钮 
	function testLdapUSNextForm(){
   		$.formValidator.initConfig({validatorGroup:"3",submitButtonID:'toUpdateLdapUsers',debug:true,
			onSuccess:function(){
				var  ajaxbg = $("#background,#progressBar");//加载等待
				  $('#progressBar').html('<view:LanguageTag key="usource_checking_ldap_info"/>');
                  ajaxbg.show();
                  setTimeout(function(){ 
	                 if(testUSConnPreNext()){
	                   ajaxbg.hide();
			     	   stepController(2);
				     }else{
				        ajaxbg.hide();
				     	FT.toAlert('error','<view:LanguageTag key="usource_vd_conn_error"/>', null);
				     }
              }, 1);	  
			},
			onError:function(){
				return false;
			}});
		checkForm('3');
	}
	
	//点击”保存配置“按钮
	function saveLdapUSForm(){
        $.formValidator.initConfig({validatorGroup:"4",submitButtonID:'saveLdapBt',debug:true,
			onSuccess:function(){
			    saveUS();
			},
			onError:function(){
				return false;
			}});
	    
	    $("#isupdateou").formValidator({validatorGroup:"4", onFocus:'<view:LanguageTag key="usource_sel_ort_set_type"/>',onCorrect:"OK"}).inputValidator({min:0,onError: '<view:LanguageTag key="usource_sel_ort_set_type"/>'});	
		 
		 var str = '<view:LanguageTag key="usource_invalid_usertab_aduser"/>';
		 var warningStr = '<view:LanguageTag key="common_vd_please_sel"/>';
		 $("input[name='userSourceInfo.upinvaliduser']").formValidator({validatorGroup:"4",tipID:"upinvaliduserTip",onFocus:warningStr,onCorrect:"OK"}).inputValidator({min:0,max:1, onError: '<view:LanguageTag key="common_vd_please_sel"/>'});	
		 $("#orgunitNames").formValidator({validatorGroup:"4",onFocus:'<view:LanguageTag key="usource_vd_sel_belongs_org"/>',onCorrect:"OK"}).functionValidator({
				 fun:function(orgunitNames){
			        var usType = $('#sourcetype').val(); 
			        if(usType=='1'){if(""==$.trim(orgunitNames)){return '<view:LanguageTag key="usource_vd_sel_belongs_org"/>';}}
			         return true;
			       }
				 }); 
		
		 $("input[name='userSourceInfo.localusermark']").formValidator({validatorGroup:"4",tipID:"localusermarkTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,max:1, onError: '<view:LanguageTag key="common_vd_please_sel"/>'});	
		 $("input[name='userSourceInfo.issyncuserinfo']").formValidator({validatorGroup:"4",tipID:"issyncuserinfoTip",onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,max:1, onError: '<view:LanguageTag key="common_vd_please_sel"/>'});	
		 $("#localuserattr").focus();
	}
	 
	
	//添加对应关系,yyf:可以删除
	function addLdapFieldMapping(){
		var local_UIField = $.trim($("#localuserattr").val());
		if($.trim($("#ldap_account_attr").val())==''||$.trim($("#ldap_account_attr").val())==null){
		   FT.toAlert('warn','<view:LanguageTag key="usource_ldap_attr_is_null"/>', null);
		   return; 
		}
		var remote_UIField =  $.trim($("#ldap_account_attr").val())+":0";
		var arr_local_UIField = local_UIField.split(":");
		var arr_remote_UIField = remote_UIField.split(":");
		//检查是否已存在要添加字段的对应关系
		//必须先添加用户名,只有用户名对应关系存在才充许添加其他对应关系	
		if(arr_local_UIField[0]!="userid"){
			if(!checkUsdrIdExist("userid")){
				FT.toAlert('warn','<view:LanguageTag key="usource_first_add_uname_relation"/>', null);
				return false;
			}
		}
		if(checkUsdrIdExist(arr_local_UIField[0])){
			FT.toAlert('warn','<view:LanguageTag key="usource_relation_is_exist"/>', null);
			return false;
		}
		var option_mapping = "<option value='"+arr_local_UIField[0]+":"+arr_remote_UIField[0]+"'>"+getSelectTextByValue(arr_local_UIField[0])+"----&gt; "+arr_remote_UIField[0]+"</option>";
			$(option_mapping).appendTo($("#fieldMapping"));
			setJsonFieldMapping();
	}
	
	//根据查找DN得到根DN
	function getRootDn(bdn) {
		if(bdn != '' && bdn != null && bdn.indexOf('DC') >= 0) {
			var rdn = bdn.substring(bdn.indexOf('DC'));
			$('#ldap_root_dn').val(rdn);
		} else {
		    $('#ldap_root_dn').val('');
		}
	} 
	
 
	//-->
</script>
</head>
 <body>
 <input type="hidden" name="path" id="contextpath"  value="<%=path%>" />
     <li>                        
     <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		<tr>
			<td width="25%" align="right">
				<view:LanguageTag key="usource_ldap_server_ip"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
			</td>
			<td width="30%" >
				<input id="ldap_domain" name="userSourceInfo.serveraddr" type="text" class="formCss100" maxlength="255" value="${userSourceInfo.serveraddr}" />
			</td>
			<td width="45%" class="divTipCss">
				<div id="ldap_domainTip" style="width:100%"></div> 
			</td>
		</tr>
		<tr>
			<td align="right" >
				<view:LanguageTag key="usource_ldap_server_port"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
			</td>
			<td>
				<input id="ldap_port" name="userSourceInfo.port" type="text" class="formCss100" maxlength="255" value="${userSourceInfo.port}" />
			</td>
			<td class="divTipCss">
				<div id="ldap_portTip" style="width:100%"></div> 
			</td>
		</tr>
		<tr>
			<td align="right" >
				<view:LanguageTag key="usource_ldap_account"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
			</td>
			<td>
				<input id="ldap_user" name="userSourceInfo.username" type="text" class="formCss100" maxlength="255" value="${userSourceInfo.username}" />
			</td>
			<td class="divTipCss">
				<div id="ldap_userTip" style="width:100%"></div> 
			</td>
		</tr>
		<tr>
			<td align="right" >
				<view:LanguageTag key="usource_ldap_pwd"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
			</td>
			<td>
				<input onpaste="return false" id="ldap_password" name="userSourceInfo.pwd" type="password" class="formCss100" maxlength="255" value="${userSourceInfo.pwd}" />
			</td>
			<td class="divTipCss">
				<div id="ldap_passwordTip" style="width:100%"></div>
			</td>
		</tr>
		
		<tr>
			<td align="right" >
				<view:LanguageTag key="usource_ldap_find_dn"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
			</td>
			<td>
				<input id="ldap_base_dn" name="userSourceInfo.basedn" type="text" class="formCss100" maxlength="255" value="${userSourceInfo.basedn}" onblur="getRootDn(this.value);" />
			</td>
			<td class="divTipCss">
				<div id="ldap_base_dnTip" style="width:100%"></div>
			</td>
		</tr>
		
		<tr>
			<td align="right" >
				<view:LanguageTag key="usource_vd_root_dn"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
			</td>
			<td>
				<input id="ldap_root_dn" name="userSourceInfo.rootdn" type="text" class="formCss100" maxlength="255" value="${userSourceInfo.rootdn}" />
			</td>
			<td class="divTipCss">
				<div id="ldap_root_dnTip" style="width:100%"></div>
			</td>
		</tr>
		<tr>
			<td align="right">
				<view:LanguageTag key="usource_ldap_find_condition"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
			</td>
			<td>
				<input id="ldap_filter" name="userSourceInfo.filter" type="text" class="formCss100" maxlength="255"  value="${userSourceInfo.filter}" />
			</td>
			<td class="divTipCss">
				<div id="ldap_filterTip" style="width:100%"></div>
			</td>
		</tr>
		<tr>
			<td align="right" >
				<view:LanguageTag key="usource_ldap_conn_timeout_seconds"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
			</td>
			<td>
				<input id="ldap_timeout" name="userSourceInfo.timeout" type="text" class="formCss100" maxlength="255" value="${userSourceInfo.timeout}" />
			</td>
			<td class="divTipCss">
				<div id="ldap_timeoutTip" style="width:100%"></div>
			</td>
		</tr>
		<tr>
			<td align="right"></td>
			<td>
				<a href="#" class="button" onclick="stepController(0)"><span><view:LanguageTag key="common_syntax_preStep"/></span></a>
				<a href="#" class="button" id="testLdapUSConn"><span><view:LanguageTag key="common_syntax_test_conn"/></span></a>
				<a href="#" class="button" id="toUpdateLdapUsers"><span><view:LanguageTag key="common_syntax_nextStep"/></span></a>
			</td>
		</tr>
	</table>
	</li>			 
			
						 
	<li>
	<table width="100%" border="0" align="left" cellspacing="0" cellpadding="0"    
		class="ulOnInsideTable">
		 <%-- 组织结构设置方式 --%>
				<tr>
					<td align="right" width="25%">
						<view:LanguageTag key="usource_ort_set_type" /><view:LanguageTag key="colon" />
					</td>
					<td align="left" width="30%">
						<select id="isupdateou" name="userSourceInfo.isupdateou" class="select100">
							<option value="0"
								<c:if test="${userSourceInfo.isupdateou eq 0}">selected="selected"</c:if>>
								<view:LanguageTag key="usource_set_local_org" />
							</option>
							<option value="1"
								<c:if test="${userSourceInfo.isupdateou eq 1}">selected="selected"</c:if>>
								<view:LanguageTag key="usource_update_ad_org" />
							</option>
						</select>
					</td>
					<td width="10%"><input id="mapingAttr" name="userSourceInfo.mapingAttr" type="hidden" value="${userSourceInfo.mapingAttr}" /></td>
					<td class="divTipCss">
						<div id="isupdateouTip" style="width: 100%"></div>
					</td>
				</tr>
				
				<tr>
			      <td align="right"><view:LanguageTag key="usource_belongs_org"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
	               	<!-- 域：组织机构组合ID字符串 -->
				     <input id="orgunitIds"   name="userSourceInfo.orgunitIds" type=hidden value="${userSourceInfo.orgunitIds}" />
		             
	              </td>
	              <td>
	               <!-- 域 组织机构名称字符串 -->
	               <c:if test="${userSourceInfo.id != 0}">
	               	${userSourceInfo.orgunitNames}
				    <input type="hidden" id="orgunitNames" name="userSourceInfo.orgunitNames"  value="${userSourceInfo.orgunitNames}"  />
	               </c:if>
	               <c:if test="${userSourceInfo.id == 0}">
				    <input id="orgunitNames" name="userSourceInfo.orgunitNames" readonly value="${userSourceInfo.orgunitNames}" onClick="selOrgunits(1,'<%=path%>')"  class="formCss100" />
	               </c:if>
	              
	              </td>
	                <td valign="top"></td>
	              <td class="divTipCss"><div id="orgunitNamesTip" style="width:100%"></div></td>
			  </tr>
				
			<%-- 是否更新禁用或已过期用户 --%>	
			<tr>
               <td align="right"><view:LanguageTag key="usource_is_update_invalid_user" /><view:LanguageTag key="colon"/></td>
               <td> 
			      	<input type="radio" id="upinvaliduser0" name="userSourceInfo.upinvaliduser" value="0"
			        	<c:if test="${userSourceInfo.upinvaliduser eq 0 }">checked</c:if>
			        /> <view:LanguageTag key="usource_no_update" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			        <input type="radio" id="upinvaliduser1" name="userSourceInfo.upinvaliduser" value="1"  
			        <c:if test="${userSourceInfo.upinvaliduser eq 1 }">checked</c:if>
			        /> <view:LanguageTag key="usource_update" /> &nbsp;&nbsp;&nbsp;&nbsp;
			   </td>
			   <td></td>
               <td class="divTipCss"><div id ="upinvaliduserTip" style="width: 100%"></div></td>
             </tr>
		
		    <tr>
               <td align="right"><view:LanguageTag key="usource_ldap_user_is_del"/><view:LanguageTag key="colon"/></td>
               <td> 
               		<input type="radio" id="notreatmentU" name="userSourceInfo.localusermark" value="0"
			        	<c:if test="${userSourceInfo.localusermark eq 0 }">checked</c:if>
			        /> <view:LanguageTag key="usource_local_no_deal"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			      	<input type="radio" id="disableLocalU" name="userSourceInfo.localusermark" value="1"
			        	<c:if test="${userSourceInfo.localusermark eq 1 }">checked</c:if>
			        /> <view:LanguageTag key="usource_disable_local_user"/><view:LanguageTag key="usource_ldap_recommend"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			        <input type="radio" id="delLocalU" name="userSourceInfo.localusermark" value="2"  
			        <c:if test="${userSourceInfo.localusermark eq 2 }">checked</c:if>
			        /> <view:LanguageTag key="usource_del_local_user"/> &nbsp;&nbsp;&nbsp;&nbsp;
			   </td>
			   <td></td>
               <td class="divTipCss"><div id ="localusermarkTip" style="width:100%"></div></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="usource_is_sync_user_info"/><view:LanguageTag key="colon"/></td>
               <td> 
			      	<input type="radio" id="nosyncuinfo" name="userSourceInfo.issyncuserinfo" value="0"
			        	<c:if test="${userSourceInfo.issyncuserinfo eq 0 }">checked</c:if>
			        /> <view:LanguageTag key="common_syntax_no"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			        <input type="radio" id="syncuinfo" name="userSourceInfo.issyncuserinfo" value="1"  
			        <c:if test="${userSourceInfo.issyncuserinfo eq 1 }">checked</c:if>
			        /> <view:LanguageTag key="common_syntax_yes"/>&nbsp;&nbsp;&nbsp;&nbsp;
			   </td>
			   <td></td>
               <td class="divTipCss"><div id ="issyncuserinfoTip" style="width:100%"></div></td>
             </tr>
		    
		    
			<tr>
				<td align="right" ></td>
				<td>
					 <a href="#" class="button" onclick="stepController(1)"><span><view:LanguageTag key="common_syntax_preStep"/></span></a>
					 <a href="#" class="button" style="align: right" id="saveLdapBt"><span><view:LanguageTag key="usource_save_config"/></span></a>
				</td>
			</tr>
		</table>
	</li>
 </body>
</html>