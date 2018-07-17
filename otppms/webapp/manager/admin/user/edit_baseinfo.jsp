<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ft.otp.common.Constant"%>
<%@ page import="com.ft.otp.util.conf.ConfDataFormat"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
    String isActivate = ConfDataFormat.getSysConfEmailEnabled()?"true":"false";// 是否需要邮件激活
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
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
   	
    <script language="javascript" type="text/javascript">
	<!--
	var winClose = null;
	$(document).ready(function(){
			window.resizeBy(0,0);
			$("#menu li").each(function(index) { //带参数遍历各个选项卡
			$(this).click(function() { //注册每个选卡的单击事件
				$("#menu li.tabFocus").removeClass("tabFocus"); //移除已选中的样式
				$(this).addClass("tabFocus"); //增加当前选中项的样式
                    //显示选项卡对应的内容并隐藏未被选中的内容
				$("#content li:eq(" + index + ")").show()
                    .siblings().hide();
                });
            });
			checkAdminForm();
	 })
	function checkAdminForm(){
	      $.formValidator.initConfig({submitButtonID:"addBtn",debug:true,
			onSuccess:function(){
				editObj();
			}, 
			onError:function(){
				return false;
			}});
	    
			$("#realname").formValidator({onFocus:'<view:LanguageTag key="admin_vd_name_tip"/>',onCorrect:"OK"}).inputValidator({min:0,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'<view:LanguageTag key="admin_vd_name_err"/>'}, onError:'<view:LanguageTag key="admin_vd_name_err_1"/>'});
			$("#localauth").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0,onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
			
			$("#tel").formValidator({empty:true,onFocus:'<view:LanguageTag key="admin_vd_tel_tip"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:64,onError:'<view:LanguageTag key="admin_vd_tel_phone_len"/>'}).regexValidator({regExp:"tel",dataType:"enum",onError:'<view:LanguageTag key="admin_vd_tel_err"/>'});
			$("#cellphone").formValidator({empty:true,onFocus:'<view:LanguageTag key="admin_vd_phone_tip"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:64,onError:'<view:LanguageTag key="admin_vd_tel_phone_len"/>'}).regexValidator({regExp:"cellphone",dataType:"enum",onError:'<view:LanguageTag key="admin_vd_phone_err"/>'});
			
			$("#address").formValidator({empty:true,onFocus:'<view:LanguageTag key="admin_vd_addr_tip"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:64,onError:'<view:LanguageTag key="admin_vd_addr_tip"/>'});
			if(<%=isActivate%>){
				$("#emailSpan").show();
				$("#email").formValidator({onFocus:'<view:LanguageTag key="admin_vd_email_tip_2"/>',onCorrect:"OK"}).inputValidator({min:4,max:255, onError:'<view:LanguageTag key="admin_vd_email_err"/>'}).regexValidator({regExp:"email",dataType:"enum",onError:'<view:LanguageTag key="admin_vd_email_err_1"/>'});
			}else{
				$("#email").formValidator({empty:true,onFocus:'<view:LanguageTag key="admin_vd_email_tip_2"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({min:0,max:255, onError:'<view:LanguageTag key="admin_vd_email_err"/>'}).regexValidator({regExp:"email",dataType:"enum",onError:'<view:LanguageTag key="admin_vd_email_err_1"/>'});
				$("#emailSpan").hide();
			}
			$("#descp").formValidator({onFocus:'<view:LanguageTag key="common_vd_descp_show"/>',onCorrect:"OK"}).inputValidator({max:255,onError:'<view:LanguageTag key="common_vd_descp_error"/>'});
      }
      
      function editObj(win){
		var url = "<%=path%>/manager/admin/user/adminUser!modifyBase.action";
		FT.Dialog.confirm('<view:LanguageTag key="admin_whether_edit"/>','<view:LanguageTag key="common_syntax_confirm"/>',function(sel) {
			if(sel){
				$("#editForm").ajaxSubmit({
					async:false,    
					dataType : "json",  
					type:"POST", 
					url : url,
					success:function(msg){
				    	if(msg.errorStr == 'success'){
					    	parent.editMoClose();
				    	}else{
				    		FT.toAlert(msg.errorStr,msg.object,null);
				    	}
					}
				});
			}
		  });
		}
		
		function okClick(btn,win,index){
			winClose = win;
		    $('#addBtn').triggerHandler("click");    	
		}
	//-->
	</script>
  </head>  
  <body style="overflow:hidden;">
  <input type="hidden" value="<%=path %>" id="contextPath" />
  <form name="editForm" id="editForm" method="post" action="">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td width="98%">  
		  <span class="topTableBgText">
	          <view:LanguageTag key="admin_info_edit"/>
  			  <input type="hidden" name="adminUser.enabled" value="${adminUser.enabled}" />
          </span>
        </td>
      </tr>
    </table> 
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td valign="top">
           <table width="98%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		      <tr>
		        <td width="22%" align="right"><view:LanguageTag key="admin_info_account"/><view:LanguageTag key="colon"/></td>
		        <td width="30%">
		        <input type="hidden"  name="adminUser.createuser" value="<%=(String)session.getAttribute(Constant.CUR_LOGINUSER)%>"/>
		        <c:if test='${not empty adminUser.adminid}'>${adminUser.adminid}<input type="hidden"  name="adminUser.adminid" value="${adminUser.adminid}" class="formCss100"/></c:if>
		        <c:if test='${empty adminUser.adminid}'>
		         <input type="text" id="adminid" name="adminUser.adminid" value="${adminUser.adminid}" class="formCss100" style="width:98%"/>
		        </c:if>
		        <c:if test='${not empty adminUser.localauth}'>
		         <input type="hidden" id="adminid" name="adminUser.localauth" value="${adminUser.localauth}" class="formCss100" style="width:98%"/>
		        </c:if>
		        </td>
		        <td width="48%"><div id="adminidTip" style="margin-left:5px;"></div></td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key="common_info_realname"/><view:LanguageTag key="colon"/></td>       
		        <td> 
		        <input type="text" id="realname" name="adminUser.realname" value="${adminUser.realname}" class="formCss100" style="width:98%"/></td>
		        <td><div id="realnameTip" style="margin-left:5px"></div></td>
		      </tr>
		      
		 <!--       <tr>
               <td align="right"><view:LanguageTag key="admin_login_mode"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
               <td>
               		<select id="localauth" name="adminUser.localauth" class="formCss100">
			        	<option value="0" <c:if test="${adminUser.localauth==0}">selected</c:if>><view:LanguageTag key="login_mode_only_vd_pwd"/></option>
			        	<option value="1" <c:if test="${adminUser.localauth==1}">selected</c:if>><view:LanguageTag key="login_mode_only_vd_pin"/></option>
			        	<option value="2" <c:if test="${adminUser.localauth==2}">selected</c:if>><view:LanguageTag key="login_mode_vd_pwd_pin"/></option>
			        </select>
			   </td>
               <td class="divTipCss"><div id ="localauthTip"></div></td>
              </tr>-->
			  <tr>
			  	<td align="right"><view:LanguageTag key="common_info_email"/><span class="text_Hong_Se" id="emailSpan">*</span><view:LanguageTag key="colon"/></td>
			  	<td><input type="text"   id="email" name="adminUser.email" class="formCss100" style="width:98%" value="${adminUser.email}"/></td>
			  	<td><div id="emailTip" style="margin-left:5px"></div></td>
			  </tr>
		 <!--     <tr>
		        <td align="right"><view:LanguageTag key="common_info_address"/><view:LanguageTag key="colon"/></td>
		        <td><input type="text"  id="address" name="adminUser.address" class="formCss100" style="width:98%" value="${adminUser.address}"/></td>
		        <td><div id="addressTip" style="margin-left:5px"></div></td>
		      </tr>  -->
		 <!--      <tr>
		        <td align="right"><view:LanguageTag key="common_info_tel"/><view:LanguageTag key="colon"/></td>
		        <td><input  type="text" id="tel" name="adminUser.tel" class="formCss100" style="width:98%" value="${adminUser.tel}"/></td>
		        <td><div id="telTip" style="margin-left:5px"></div></td>
		      </tr>  -->
		      <tr>
		        <td align="right"><view:LanguageTag key="common_info_mobile"/><view:LanguageTag key="colon"/></td>
		        <td><input type="text"  id="cellphone" name="adminUser.cellphone" class="formCss100" style="width:98%" value="${adminUser.cellphone}"/> </td>
		        <td><div id="cellphoneTip" style="margin-left:5px"></div></td>
		      </tr>		
		      <tr>
		        <td align="right"><view:LanguageTag key="admin_info_descp"/><view:LanguageTag key="colon"/></td>
		        <td><textarea id="descp" name="adminUser.descp" class="textarea100" style="width:98%">${adminUser.descp}</textarea></td>
		        <td><div id="descpTip" style="margin-left:5px"></div></td>
		      </tr>
    		  <tr>
		      </tr>
		      <tr>
		        <td align="right">&nbsp;</td>
		        <td>
		         	<a href="#" name="addBtn" id="addBtn"></a>
		        </td>
		        <td>&nbsp;</td>
		      </tr>
			</table>
			 </td>
			</tr>
   		  </table>
  		</form>
  	</body>
</html>