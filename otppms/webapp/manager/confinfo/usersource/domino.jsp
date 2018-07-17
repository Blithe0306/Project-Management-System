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
		<link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
		<link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
		<link href="<%=path%>/manager/common/ligerUI/skins/Aqua/css/ligerui-alert.css" rel="stylesheet" type="text/css" />
		<script language="javascript" src="<%=path%>/manager/common/js/checkall.js"></script>
		<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
		<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
		<script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
		<script language="javascript" src="<%=path%>/manager/common/js/json2.js"></script>
	    <script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
	    <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
		<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
	    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
	    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
		<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
		<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
		<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script> 
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
        
    var jsonFieldInfos = "";//保存字段信息,包括字段名和字段长度
    $(document).ready(function(){ 
        testDominoUSConnForm();
        testDominoUSNextForm();
        saveDominoUSForm();
        loadDominoFieldMapping();
	})
	
     //校验公用方法提取,传入分组号
      function checkForm(groupId){
          //用户来源为domino
			     $("#domino_driver").formValidator({validatorGroup:groupId,onFocus:"请选择domin驱动器",onCorrect:"OK"}).functionValidator({
			    fun:function(domino_driver){
			        var usType = $('#sourcetype').val(); 
			        if(usType=='2'){if(domino_driver==''){return '请选择domin驱动器';}}
			        return true;
			        }
			    });
			   $("#domino_server").formValidator({validatorGroup:groupId,onFocus:"请填写domino服务器地址",onCorrect:"OK"}).functionValidator({
			    fun:function(domino_server){
			        var usType = $('#sourcetype').val(); 
			        if(usType=='2'){if(!checkIpAddr($.trim(domino_server))){return 'domino服务器地址不能为空，必须符合IP格式!';}}
			        return true;
			        }
			    });
				$("#domino_java_port").formValidator({validatorGroup:groupId,onFocus:"请填写domino JAVA端口",onCorrect:"OK"}).functionValidator({
				fun:function(domino_java_port){
			        var usType = $('#sourcetype').val(); 
			        if(usType=='2'){if(!checkPort($.trim(domino_java_port))){return 'domino JAVA端口不能为空，必须符合端口格式!';}}
			         return true;
			      }
				 });
				$("#domino_db_file").formValidator({validatorGroup:groupId,onFocus:"请填写domino数据库文件名",onCorrect:"OK"}).functionValidator({
				 fun:function(domino_db_file){
			        var usType = $('#sourcetype').val(); 
			        if(usType=='2'){if(""==$.trim(domino_db_file)){return 'domino数据库文件不能为空!';}}
			         return true;
			       }
				 });
		 		$("#domino_user").formValidator({validatorGroup:groupId,onFocus:"请输入domino用户名",onCorrect:"OK"}).functionValidator({
		 		 fun:function(domino_user){
			        var usType = $('#sourcetype').val(); 
			        if(usType=='2'){ if(""==$.trim(domino_user)){return 'domino用户名不能为空!';}}
			         return true;
			       }
		 		 });
		 		$("#domino_password").formValidator({validatorGroup:groupId,onFocus:"请输入domino密码",onCorrect:"OK"}).functionValidator({
		 		 fun:function(domino_password){
			        var usType = $('#sourcetype').val(); 
			        if(usType=='2'){if(""==$.trim(domino_password)){return 'domino密码不能为空!';}} 
			        return true;
			      }
		 		 });
		 		 $("#domino_driver").focus();        
     } 
    
	//用户来源详细配置选项,点击"测试连接"按钮
	function testDominoUSConnForm(){
	      $.formValidator.initConfig({validatorGroup:"2",submitButtonID:'testDominoUSConn',debug:true,
			onSuccess:function(){
			    testUSConn();
			},
			onError:function(){
				return false;
			}});
		 checkForm('2');
	}	   
	
   //用户来源详细配置选项，点击"下一步"按钮 
   function testDominoUSNextForm(){
   	      $.formValidator.initConfig({validatorGroup:"3",submitButtonID:'toUpdateDominoUsers',debug:true,
			onSuccess:function(){
			     if(testUSConnPreNext()){
			          stepController(2);
			     }else{
			         FT.toAlert('error','连接失败，无法进行下一步', null);
			     }
			     
			},
			onError:function(){
				return false;
			}});
		 checkForm('3');
   }
      //点击”保存配置“按钮
     function saveDominoUSForm(){
        $.formValidator.initConfig({validatorGroup:"4",submitButtonID:'saveDominoBt',debug:true,
			onSuccess:function(){
			    saveUS();
			},
			onError:function(){
				return false;
			}});
	    
		 $("#localuserattr").formValidator({validatorGroup:"4", onFocus:"请选择本地系统字段属性",onCorrect:"OK"}).inputValidator({min:0,onError: "请选择本地系统字段属性"});	
		 $("#mappingAddr").formValidator({validatorGroup:"4",tipID:"sourceTip",onFocus:"请设置LDAP属性字段对应关系",onCorrect:"OK"}).functionValidator({
		 		 fun:function(mappingAddr){
			        if(""==$.trim(mappingAddr)){
			           return "请设置LDAP属性字段对应关系";
			        } 
			        return true;
			      }
		 });
		 $("#sourceuserattr").formValidator({empty:true,validatorGroup:"4", onFocus:"请填写其它来源字段属性",onCorrect:"OK"}).inputValidator({min:1,onError: "请填写其它来源字段属性"});	
		 $("#localuserattr").focus();
	}
	
        
  
	//初始化对应关系列表
	function loadDominoFieldMapping(){
		if(isEdit){ //编辑
		    var columnname = "${userSourceInfo.mapingAttr}"; 
			if(columnname!=="")
			{
				var arr_columnname = columnname.split(",");
				for(var i=0;i<arr_columnname.length;i++)
				{
					var option_mappings=arr_columnname[i].split(":");
					var option_mapping = "<option value='"+option_mappings[0]+":"+option_mappings[1]+"'>"+getSelectTextByValue(option_mappings[0])+"----&gt; "+option_mappings[1]+"</option>";
					$(option_mapping).appendTo($("#fieldMapping"));
				}
			}
		 
		}
	}
	
	 //添加对应关系
	function addFieldMapping(){
		var local_UIField = $.trim($("#localuserattr").val());
		var remote_UIField =  $.trim($("#sourceuserattr").val())+":0";
		var arr_local_UIField = local_UIField.split(":");
		var arr_remote_UIField = remote_UIField.split(":");
		//检查是否已存在要添加字段的对应关系
		//必须先添加用户名,只有用户名对应关系存在才充许添加其他对应关系	
		if(arr_local_UIField[0]!="userid"){
			if(!checkUsdrIdExist("userid")){
				FT.toAlert('warn','请先添加用户名字段对应关系!', null);
				return false;
			}
		}
		if(checkUsdrIdExist(arr_local_UIField[0])){
			FT.toAlert('warn','此字段对应关系已存在!', null);
			return false;
		}
		var option_mapping = "<option value='"+arr_local_UIField[0]+":"+arr_remote_UIField[0]+"'>"+getSelectTextByValue(arr_local_UIField[0])+"----&gt; "+arr_remote_UIField[0]+"</option>";
			$(option_mapping).appendTo($("#fieldMapping"));
			setJsonFieldMapping();
	}
	
	//检查字段对应关系是否存在
	function checkUsdrIdExist(fieldName)
	{
			var exist=false;
			$("#fieldMapping option").each(function(){
			var optvalue=$(this).val().split(":");
		 	if(optvalue[0]==fieldName){
		 		exist=true;
		 	}
		  });
		  return exist;
	}
	//移除字段对应关系
	function removefieldMapping(){
		var len = $("#fieldMapping option").length;
		var otpselected = $('#fieldMapping option:selected').val();
		if(otpselected==undefined){
		    FT.toAlert('warn','没有选择要删除的对应关系', null);
		    return false;
		}
		var optvalue=otpselected.split(":");
		var canRemove=true;
	 	if(optvalue[0]=="userid" && len>1){
	 		canRemove=false;
	 	}
	 	if(canRemove){
	 		$('#fieldMapping option:selected').remove();
			setJsonFieldMapping();
	 	}else{
	 	   FT.toAlert('warn','删除用户名字段对应关系前，必须先删除其它字段对应关系!', null);
	 		 
	 	}
	}
	
	//构造对应关系
	function setJsonFieldMapping(){
		 var num = 0;
		 var fmString="";
		 $("#fieldMapping option").each(function(){
		 	if(num>0){
		 		fmString=fmString+",";
		 	}
		 	fmString=fmString+$(this).val();
		 	num++;
		    });
			$("#mapingAttr").val(fmString);
			 
	}
	
	//由下拉框的字际值得到显示值
	function getSelectTextByValue(select_value){
		  var select_text="";
		  $("#localuserattr option").each(function(){
		  var this_val=$(this).val().split(":");
		  var select_val = select_value.split(":");
			  if(this_val[0]==select_val[0]){select_text=  $(this).text();}
		  });
		  return select_text;
	}

 
	//-->
</script>
</head>

 <body>
<li>                        
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		<tr>
			<td width="25%" align="right" >
				<view:LanguageTag key="domino_driver_name"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
			</td>
			<td width="30%" >
				<select id="domino_driver" name="userSourceInfo.dominodriver" class="formCss100">
					<option value="driver_name" selected="selected">
						driver_name
					</option>
				</select>
			</td>
			<td width="45%"><div id="domino_driverTip" style="width:100%"></div></td>
		</tr>

		<tr>
			<td align="right"><view:LanguageTag key="domino_serv_addr"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			<td>
				<input id="domino_server" name="userSourceInfo.serveraddr" type="text" class="formCss100" maxlength="255"
					value="${userSourceInfo.serveraddr}" />
				</td>
				<td>
					<div id="domino_serverTip" style="width:100%"></div>
				</td>
			</tr>

			<tr>
				<td align="right">
					<view:LanguageTag key="domino_java_port"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
				</td>
				<td>
					<input id="domino_java_port" name="userSourceInfo.port" type="text" class="formCss100" maxlength="255"
						value="${userSourceInfo.port}" />
				</td>
				<td>
					<div id="domino_java_portTip" style="width:100%"></div>
				</td>
			</tr>

			<tr>
				<td align="right"><view:LanguageTag key="domino_db_file_name"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
				</td>
				<td>
					<input id="domino_db_file" name="userSourceInfo.namesfile" type="text" class="formCss100" maxlength="255"
						value="${userSourceInfo.namesfile}" />
				</td>
				<td>
					<div id="domino_db_fileTip" style="width:100%"></div>
				</td>
			</tr>

			<tr>
				<td align="right"><view:LanguageTag key="domino_username"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
				<td>
					<input id="domino_user" name="userSourceInfo.username" type="text" class="formCss100" maxlength="255"
						value="${userSourceInfo.username}" />
				</td>
				<td>
					<div id="domino_userTip" style="width:100%"></div>
				</td>
			</tr>

			<tr>
				<td align="right"><view:LanguageTag key="domino_pwd"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
				</td>
				<td>
					<input onpaste="return false" id="domino_password" name="userSourceInfo.passwd" type="password" class="formCss100" maxlength="255"
						value="${userSourceInfo.passwd}" />
				</td>
				<td>
					<div id="domino_passwordTip" style="width:100%"></div>
				</td>
			</tr>
			<tr>
				<td align="right"></td>
				<td></td>
				 <td>
				     <a href="#" class="button" onclick="stepController(0)"><span><view:LanguageTag key="common_syntax_preStep"/></span></a>
					 <a href="#" class="button" id="testDominoUSConn"><span><view:LanguageTag key="common_syntax_test_conn"/></span></a>
					 <a href="#" class="button" id="toUpdateDominoUsers"><span><view:LanguageTag key="common_syntax_nextStep"/></span></a>
					
				 </td>
			</tr>
		</table>
	</li>			 

   <li>
	<table width="100%" border="0" align="left" cellspacing="0" cellpadding="0"    
		class="ulOnInsideTable">
		  <tr>
			<td align="right" width="25%"><view:LanguageTag key="domino_the_sys_field"/><view:LanguageTag key="colon"/></td>
			<td align="left" width="36%" >
				<select id="localuserattr" name="localuserattr" class="formCss100">
					<option value="userid:64" selected="selected">
						<view:LanguageTag key="common_info_username"/>
					</option>
					<option value="realname:64">
						<view:LanguageTag key="common_info_realname"/>
					</option>
					<option value="pin:32">
						<view:LanguageTag key="common_info_pwd"/>
					</option>
					<option value="email:64">
						<view:LanguageTag key="common_info_email"/>
					</option>
					<option value="address:64">
						<view:LanguageTag key="common_info_address"/>
					</option>
					<option value="tel:64">
						<view:LanguageTag key="common_info_tel"/>
					</option>
					<option value="cellphone:64">
						<view:LanguageTag key="common_info_mobile"/>
					</option>
				</select>
			</td>
			<td width="9%" rowspan="2" align="right" >
			   <a href="#" class="button" onclick="addFieldMapping()"><span><view:LanguageTag key="usource_relation"/></span></a>
			</td>
			<td  width="30%" align="left"><div id="localuserattrTip" style="width:100%"></div></td>
		 </tr>
		 <tr>
			<td align="right" ><view:LanguageTag key="domino_other_source_attr"/><view:LanguageTag key="colon"/></td>
			 <td align="left" >
			 <input id="mappingAddr" name="userSourceInfo.mapingAttr" type="hidden"
					value="${userSourceInfo.mapingAttr}" />

				<input id="sourceuserattr" name="userSourceInfo.sourceuserattr" type="hidden"
					value="${userSourceInfo.sourceuserattr}" />
			  </td>
			  <td><div id="sourceuserattrTip"  style="width:100%"></div></td>
			<td align="left"></td>
		</tr>
		<tr>
			<td align="right"><view:LanguageTag key="usource_corres_relation"/><span class="text_Hong_Se">*&nbsp;</span><view:LanguageTag key="colon"/>
			</td>
			<td colspan="2">
				<select name="fieldMapping" id="fieldMapping" size="8"  style="width:100%;background-color: #FFFFFF">
				</select>	 
			</td>
			<td width="30%">
				 <div id="sourceTip" style="width:100%"></div>
			</td>
		</tr>
		<tr>
			<td align="right" ></td>
			 <td colspan="2">
			     <a href="#" class="button" onclick="removefieldMapping()"><span><view:LanguageTag key="usource_del_relation"/></span></a>
			</td>
			<td>
				 <a href="#" class="button" onclick="stepController(1)"><span><view:LanguageTag key="common_syntax_preStep"/></span></a>
				 <a href="#" class="button" style="align: right" id="saveDominoBt"><span><view:LanguageTag key="usource_save_config"/></span></a>
			</td>
		</tr>
	</table>
	</li>
 </body>
</html>