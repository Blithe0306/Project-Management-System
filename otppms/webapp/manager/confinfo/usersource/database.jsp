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
	    <script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script>
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
	        testDbUSConnForm();
	        testDbUSNextForm();
	        saveDbUSForm();
	        loadFieldMapping();
	       if(!isEdit && usType == '0'){//添加  usType-->远程数据库
		        changePort($('#db_port').val());
			}
		})
	
	  //选择不同的数据库设置不同的端口
	  function changePort(value){
	        if(value=='0'){
	           $('#db_port').val('3306');
	        }else if(value=='1'){
	           $('#db_port').val('1521');
	        }else if(value=='2'){
	           $('#db_port').val('5432');
	        }else if(value=='3'){
	           $('#db_port').val('1433');
	        }
	  }
	  
     //校验公用方法提取,传入分组号
      function checkForm(groupId){
        //用户来源为database
        $("#db_type").formValidator({validatorGroup:groupId,onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).functionValidator({
	    fun:function(db_type){
	        var usType = $('#sourcetype').val(); 
	        if(usType=='0'){if(db_type==''){return '<view:LanguageTag key="common_vd_please_sel"/>';}}
	        return true;
	        }
	    });
	    
	   $("#db_ip").formValidator({validatorGroup:groupId,onFocus:'<view:LanguageTag key="dbconf_vd_ip"/>',onCorrect:"OK"}).functionValidator({
	    fun:function(db_ip){
	        var usType = $('#sourcetype').val(); 
	        if(usType=='0'){if(!checkIpAddr($.trim(db_ip))){return '<view:LanguageTag key="dbconf_vd_ip_err"/>';}}
	        return true;
	        }
	    });
		$("#db_port").formValidator({validatorGroup:groupId,onFocus:'<view:LanguageTag key="dbconf_vd_port"/>',onCorrect:"OK"}).functionValidator({
		fun:function(db_port){
	        var usType = $('#sourcetype').val(); 
	        if(usType=='0'){if(!checkPort($.trim(db_port))){return '<view:LanguageTag key="dbconf_vd_port_err"/>';}}
	         return true;
	      }
		 });
		$("#db_dbname").formValidator({validatorGroup:groupId,onFocus:'<view:LanguageTag key="dbconf_vd_dbname"/>',onCorrect:"OK"}).functionValidator({
		 fun:function(db_dbname){
	        var usType = $('#sourcetype').val(); 
	        if(usType=='0'){if(""==$.trim(db_dbname)){return '<view:LanguageTag key="usource_vd_ldap_is_not_null"/>';}}
	         return true;
	       }
		 });
 		$("#db_user").formValidator({validatorGroup:groupId,onFocus:'<view:LanguageTag key="dbconf_vd_username"/>',onCorrect:"OK"}).functionValidator({
 		 fun:function(db_user){
	        var usType = $('#sourcetype').val(); 
	        if(usType=='0'){ if(""==$.trim(db_user)){return '<view:LanguageTag key="usource_vd_ldap_is_not_null"/>';}}
	         return true;
	       }
 		 });
 		$("#db_password").formValidator({validatorGroup:groupId,onFocus:'<view:LanguageTag key="dbconf_vd_pwd"/>',onCorrect:"OK"}).functionValidator({
 		 fun:function(db_password){
	        var usType = $('#sourcetype').val(); 
	        if(usType=='0'){if(""==$.trim(db_password)){return '<view:LanguageTag key="usource_vd_ldap_is_not_null"/>';}} 
	        return true;
	      }
 		 });
	    $("#db_type").focus();
     } 
     
	 
	//详细配置选项,点击"测试连接"按钮
	function testDbUSConnForm(){
	      $.formValidator.initConfig({validatorGroup:"2",submitButtonID:'testdbUSConn',debug:true,
			onSuccess:function(){
			    testUSConn();
			},
			onError:function(){
				return false;
			}});
		 checkForm('2');
	}	   
	
   //详细配置选项，“测试连接”成功后，点击"下一步"按钮 
   function testDbUSNextForm(){
   	      $.formValidator.initConfig({validatorGroup:"3",submitButtonID:'toUpdatedbUsers',debug:true,
			onSuccess:function(){
				 var  ajaxbg = $("#background,#progressBar");//加载等待
                  ajaxbg.show();
                  setTimeout(function(){ 
	                   if(testUSConnPreNext()){
					        if(getTables()){
					           loadFieldMapping();
					           ajaxbg.hide(); 
					           stepController(2);
					        }			        
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
   
      //保存用户来源配置
     function saveDbUSForm(){
        $.formValidator.initConfig({validatorGroup:"4",submitButtonID:'savedbBt',debug:true,
			onSuccess:function(){
			    saveUS();
			},
			onError:function(){
				return false;
			}});
			
		$("#db_tablename").formValidator({validatorGroup:"4", onFocus:'<view:LanguageTag key="usource_vd_sel_remote_db_tab"/>',onCorrect:"OK"}).inputValidator({min:0,onError: '<view:LanguageTag key="usource_vd_sel_remote_db_tab"/>'});	
		$("#mapingAttr").formValidator({validatorGroup:"4",tipID:"sourceTip",onFocus:'<view:LanguageTag key="usource_vd_set_relation"/>',onCorrect:"OK"}).functionValidator({
		 		 fun:function(mapingAttr,elem){
			        var usType = $('#sourcetype').val(); 
			        if(usType=='0'){if(""==$.trim(mapingAttr)){
			         return '<view:LanguageTag key="usource_vd_set_relation"/>';
			        }} 
			        return true;
			      }
		 });
		 $("#localuserattr").formValidator({validatorGroup:"4", onFocus:'<view:LanguageTag key="usource_vd_sel_local_field_attr"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="usource_vd_sel_local_field_attr"/>'});	
		 $("#sourceuserattr").formValidator({validatorGroup:"4", onFocus:'<view:LanguageTag key="usource_vd_sel_remote_dbtab_attr"/>',onCorrect:"OK"}).inputValidator({min:0,onError: '<view:LanguageTag key="usource_vd_sel_remote_dbtab_attr"/>'});	
		 
		 $("#orgunitNames").formValidator({validatorGroup:"4",onFocus:'<view:LanguageTag key="usource_vd_sel_belongs_org"/>',onCorrect:"OK"}).functionValidator({
				 fun:function(orgunitNames){
			        var usType = $('#sourcetype').val(); 
			        if(usType=='0'){if(""==$.trim(orgunitNames)){return '<view:LanguageTag key="usource_vd_sel_belongs_org"/>';}}
			         return true;
			       }
				 });
		 $("#localuserattr").focus(); 
	}
 
   
	//获取所有表名
	function getTables(){ 
			var connectionfail = true;
			//列表数据
			var lis = "";
			 $("#usForm").ajaxSubmit({
				url:"<%=path%>/manager/confinfo/usersource/usConf!queryAllTableName.action",
				async:false,
				dataType : "json",
				success:function(msg){
					if('connfailed'==msg.object){
						connectionfail = false;
					}else if(msg.object!="failed"){
					 var jsonMsg = eval(msg.object);
				      	lis += "<select id='db_tablename' name='userSourceInfo.dbtablename' class='select100' onchange='fillFields(this.value,true);'>"
						$.each(jsonMsg ,function(commentIndex, comment){
						if("${userSourceInfo.dbtablename}" == comment['dbtablename']){
						  lis += "<option value = '" + comment['dbtablename'] + "' selected>";
						}else{
						  lis += "<option value = '" + comment['dbtablename'] + "'>";
						}
							    	lis += comment['dbtablename'];
							    	lis += "<\/option>";
						}); 
					    lis += "<\/select>";	   
					  $('#dnameDiv').html(lis); 	
					  if(isEdit){//编辑
						       fillFields("${userSourceInfo.dbtablename}",false);
							}else{
							   fillFields($("#db_tablename").find("option:selected").val(),false);
							}
					} 
				}
			});
		return connectionfail;
	}
	
	//根据表名查得字段信息
 
	function fillFields(tableName,flag){	
	 //clear
	 $("#fieldMapping").empty();//清除已经设置的对应关系
	 if(flag){
	    $("#sattrDiv").empty();
	 }
	 
	 var option_fn = '';
	 var connectionfail = true;
     var checkUSNameUrl = "<%=path%>/manager/confinfo/usersource/usConf!queryFieldsByTabName.action?dbtablename=" + tableName;
     var ajaxbg = $("#background,#progressBar");//加载等待
        ajaxbg.show();
     setTimeout(function(){   
	 $("#usForm").ajaxSubmit({
		   async:false,  
		   type:"POST",
		   dataType : "json", 
		   url : checkUSNameUrl,
		   success:function(msg){
		       ajaxbg.hide();
		       if('connfailed'==msg.object){
				  connectionfail = false;
				}else if(msg.object!="failed"){
					var jsonFields = eval(msg.object);
						option_fn = "<select id='sourceuserattr' name='userSourceInfo.sourceuserattr' class='select100'>";
						$.each( jsonFields ,function(commentIndex, comment){
							 option_fn += "<option value='"+comment['columnName']+":"+comment['columnDisplaySize']+"'>"+comment['columnName']+"</option>";
						});
						 option_fn += "<\/select>";
					}else{
					    FT.toAlert('warn','<view:LanguageTag key="usource_vd_not_get_column"/>', null);
					}
				$('#sattrDiv').html(option_fn);
			}
		});				
	
	  	if(!connectionfail){
		     FT.toAlert('error', '<view:LanguageTag key="common_conn_error_tip"/>', null);
		} 
 
       }, 1);	
	}
	 
	//添加对应关系
	function addDbFieldMapping()
	{
		var local_UIField = $.trim($("#localuserattr").val());  //userid:64
		var remote_UIField = $.trim($("#sourceuserattr").val());//groupid:64
		var arr_local_UIField = local_UIField.split(":"); //本数据库表属性
		var arr_remote_UIField = remote_UIField.split(":");//远程数据库表属性
		
	 
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
		 if(parseInt(arr_remote_UIField[1]) > parseInt(arr_local_UIField[1])){ //字段类型长度判断
			    FT.toAlert('warn','<view:LanguageTag key="usource_vd_unable_set_relation"/>', null);
				return false;
			}

		var option_mapping = "<option value='"+arr_local_UIField[0]+":"+arr_remote_UIField[0]+"'>"+getSelectTextByValue(arr_local_UIField[0])+"----&gt; "+arr_remote_UIField[0]+"</option>";
			$(option_mapping).appendTo($("#fieldMapping"));
			setJsonFieldMapping(); 
	}
	
	//-->
</script>
 
</head>
 
<body>                                        
 <input type="hidden" name="path" id="contextpath"  value="<%=path%>"/>
  <li>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		<tr>
			<td width="25%" align="right" >
				<view:LanguageTag key="dbconf_type"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
			</td>
			<td width="35%" >
			<select id="db_type" class="select100" name="userSourceInfo.dbtype" onChange="changePort(this.value)">
				<option value="0" <c:if test="${userSourceInfo.dbtype eq 0}">selected</c:if>>MySQL</option>
				<option value="1" <c:if test="${userSourceInfo.dbtype eq 1}">selected</c:if>>Oracle</option>
			 	<option value="3" <c:if test="${userSourceInfo.dbtype eq 3}">selected</c:if>>SQL Server</option>
				<option value="2" <c:if test="${userSourceInfo.dbtype eq 2}">selected</c:if>>PostgreSQL</option>
			</select>
		</td>
		<td width="45%" class="divTipCss">
			 <div id="db_typeTip" style="width:100%"></div> 
		</td>
	</tr>
	<tr>
		<td align="right">
			<view:LanguageTag key="dbconf_ip"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
		</td>
		<td>
			<input id="db_ip" name="userSourceInfo.serveraddr" type="text" class="formCss100" maxlength="255"  value="${userSourceInfo.serveraddr}" />
		</td>
		<td class="divTipCss">
			 <div id="db_ipTip" style="width:100%"></div> 
		</td>
		 
	</tr>
	<tr>
		<td  align="right"  >
			<view:LanguageTag key="dbconf_port"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
		</td>
		<td>
			<input id="db_port" name="userSourceInfo.port" type="text" class="formCss100" maxlength="255" value="${userSourceInfo.port}" />
		</td>
		<td class="divTipCss">
			<div id="db_portTip" style="width:100%"></div> 
		</td>
	 
	</tr>
	<tr>
		<td align="right">
			<view:LanguageTag key="dbconf_dbname"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
		</td>
		<td>
			<input id="db_dbname" name="userSourceInfo.dbname" type="text" class="formCss100" maxlength="255" value="${userSourceInfo.dbname}" />
		</td>
		<td class="divTipCss">
			<div id="db_dbnameTip" style="width:100%"></div> 
		</td>
		 
	</tr>
	<tr>
		<td align="right">
			<view:LanguageTag key="dbconf_username"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
		</td>
		<td>
			<input id="db_user" name="userSourceInfo.username" type="text"  class="formCss100" maxlength="255"  value="${userSourceInfo.username}" />
		</td>
		<td class="divTipCss">
			<div id="db_userTip" style="width:100%"></div> 
		</td>
	</tr>
	<tr>
		<td align="right" >
			<view:LanguageTag key="dbconf_pwd"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
		</td>
		<td>
			<input onpaste="return false" id="db_password" name="userSourceInfo.pwd" type="password" class="formCss100" maxlength="255" value="${userSourceInfo.pwd}" />
			</td>
			<td class="divTipCss">
				<div id="db_passwordTip" style="width:100%"></div> 
			</td>
		</tr>
			<tr>
			    <td align="right"></td>
				 <td>
				     <a href="#" class="button" onclick="stepController(0)"><span><view:LanguageTag key="common_syntax_preStep"/></span></a>
					 <a href="#" class="button" id="testdbUSConn"><span><view:LanguageTag key="common_syntax_test_conn"/></span></a>
					 <a href="#" class="button" id="toUpdatedbUsers"><span><view:LanguageTag key="common_syntax_nextStep"/></span></a>
				 </td>
				<td></td>
			</tr>
	</table>
	</li>			 

    <li>
	<table width="100%" border="0" align="left" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
		<tr>
			<td width="25%" align="right" >
				<view:LanguageTag key="usource_remote_db_name"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
			</td>
			<td width="30%" align="left">
				 <div id="dnameDiv"></div>
			</td>
			<td width="15%" align="left" class="divTipCss"><div id="db_tablenameTip" style="width:100%"></div></td>
			<td width="30%"></td>
		</tr>
		
		<tr>
			<td align="right" >
			 <view:LanguageTag key="usource_this_db_attr"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
			</td>
			<td align="left" >
				<select id="localuserattr" name="userSourceInfo.localuserattr"  class="select100">  
					<option value="userid:64" selected="selected"><view:LanguageTag key="common_info_username"/></option>
					<option value="realname:64"><view:LanguageTag key="common_info_realname"/></option>
					<option value="pwd:64"><view:LanguageTag key="common_info_pwd"/></option>
					<option value="email:64"><view:LanguageTag key="common_info_email"/></option>
					<option value="address:64"><view:LanguageTag key="common_info_address"/></option>
					<option value="tel:20"><view:LanguageTag key="common_info_tel"/></option>
					<option value="cellPhone:20"><view:LanguageTag key="common_info_mobile"/></option>
				</select>
			</td>
			<td align="left" class="divTipCss"><div id="localuserattrTip"  style="width:100%"></div></td>
			<td> </td>
		</tr>
	
		
		 <tr>
			<td align="right"><view:LanguageTag key="usource_remote_db_table_attr"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			<td align="left">	 
		       <div id="sattrDiv"></div>
			</td>
			<td class="divTipCss"><div id="sourceuserattrTip"  style="width:100%"></div></td>
			<td align="left" ></td>
		</tr>
		<tr valign="top">
			<td colspan="4">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr valign="top">
						<td align="right" width="25%" valign="middle"><view:LanguageTag key="usource_corres_relation"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/> 
						</td>
						<td width="30%">
							 <select name="fieldMapping" id="fieldMapping" size="8"  style="width:100%;background-color: #FFFFFF"></select>
							 <input id="mapingAttr"  name="userSourceInfo.mapingAttr" type="hidden" value="${userSourceInfo.mapingAttr}"/>	 
						</td>
						<td width="14%">
							<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td><div style="padding-bottom:20px;margin-left:5px;"><a href="#" class="button" onclick="addDbFieldMapping()"><span><view:LanguageTag key="usource_relation"/></span></a></div></td>
								</tr>
								<tr>
									<td><div style="padding-bottom:20px;margin-left:5px;"><a href="#" class="button" onclick="removefieldMapping()"><span><view:LanguageTag key="usource_del_relation"/></span></a></div></td>
								</tr>
							</table>
						</td>
						<td width="30%" valign="middle" class="divTipCss"><div id="sourceTip" style="width:100%"></div></td>
					</tr>
				</table>
			</td>
			
			
		</tr>
		
		<tr>
	      <td align="right"><view:LanguageTag key="usource_belongs_org"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
              	<!-- 域：组织机构组合ID字符串 -->
		     <input id="orgunitIds" name="userSourceInfo.orgunitIds" type=hidden value="${userSourceInfo.orgunitIds}" />
             </td>
             <td>
              <!-- 域 组织机构名称字符串 -->
		      <c:if test="${userSourceInfo.id != 0}">
               	${userSourceInfo.orgunitNames}
			    <input type="hidden" id="orgunitNames" name="userSourceInfo.orgunitNames"  value="${userSourceInfo.orgunitNames}"  />
              </c:if>
              <c:if test="${userSourceInfo.id == 0}">
              	<input id="orgunitNames" name="userSourceInfo.orgunitNames" onClick="selOrgunits(1,'<%=path%>');" readonly value="${userSourceInfo.orgunitNames}" class="formCss100" />
              </c:if>
		      
             </td>
             <td class="divTipCss"><div id="orgunitNamesTip" style="width:100%"></div></td>
             <td valign="top"></td>
	    </tr>
		    
	    <tr>
			<td align="right"></td>
			<td>
				 <a href="#" class="button" onclick="stepController(1)"><span><view:LanguageTag key="common_syntax_preStep"/></span></a>
				 <a href="#" class="button" style="align: right" id="savedbBt"><span><view:LanguageTag key="usource_save_config"/></span></a>
			</td>
			 <td colspan="2"></td>
		</tr>
	</table>
  </li>
</body>
</html>