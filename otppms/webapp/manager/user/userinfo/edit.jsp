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
    <link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
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
	<script language="javascript" src="<%=path%>/manager/user/userinfo/js/add.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script> 
	<script language="javascript" type="text/javascript">
		
	   // Start,多语言信息
	   var sel_tkn_lang = '<view:LanguageTag key="user_sel_tkn"/>';
	   var user_group_lang = '<view:LanguageTag key="user_sel_user_group"/>';
	   var need_del_date_lang = '<view:LanguageTag key="common_syntax_check_need_del_date"/>';
	   var bind_tkn_lang = '<view:LanguageTag key="user_seve_succ_is_bind_tkn"/>';
	   var save_succ_lang = '<view:LanguageTag key="common_save_succ_tip"/>';
	   var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
	   var syntax_confirm_lang = '<view:LanguageTag key="common_syntax_confirm"/>';
	   var verify_login_type_check_email_lang = '<view:LanguageTag key="user_vd_init_pwd_login_verify_uemail"/>';
	   var verify_login_type_check_phone_lang = '<view:LanguageTag key="user_vd_init_pwd_login_verify_uphone"/>';
	   // End,多语言信息
	
	    var bool = false;
		$(function() {
            checkUserForm();
            window.resizeBy(0,0);
            if($('#localAuth').val() == 3){
				$("#c").remove();
			 }
        })
       
       function checkUserForm(){
          //校验用户基本信息选项
           $.formValidator.initConfig({submitButtonID:"editBtUser",debug:true,
		     onSuccess:function(){
		          //editObj();
		          checkUserInfo(1);
	         },
             onError:function(){return false;}
           });       
            
             $("#realName").formValidator({empty:true,onFocus:'<view:LanguageTag key="user_vd_realname_tip"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:64,onError:'<view:LanguageTag key="user_vd_realname_err"/>'});
	//		 $("#papersNumber").formValidator({empty:true,onFocus:'<view:LanguageTag key="user_vd_document_num_tip"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:64,onError:'<view:LanguageTag key="user_vd_papersnumber_err"/>'}).regexValidator({regExp:"pnumber",dataType:"enum",onError:'<view:LanguageTag key="user_vd_document_num_err"/>'});
			 $("#email").formValidator({empty:true,onFocus:'<view:LanguageTag key="user_vd_email_tip"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:255,onError:'<view:LanguageTag key="user_vd_email_num_err"/>'}).regexValidator({regExp:"email",dataType:"enum",onError:'<view:LanguageTag key="user_vd_email_err"/>'});
	//		 $("#tel").formValidator({empty:true,onFocus:'<view:LanguageTag key="user_vd_phone_tip"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:20,onError:'<view:LanguageTag key="user_vd_tel_num_err"/>'}).regexValidator({regExp:"tel",dataType:"enum",onError:'<view:LanguageTag key="user_vd_phone_err"/>'}); 
			 $("#cellPhone").formValidator({empty:true,onFocus:'<view:LanguageTag key="user_vd_tel_tip"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:64,onError:'<view:LanguageTag key="user_vd_cell_num_err"/>'}).regexValidator({regExp:"cellphone",dataType:"enum",onError:'<view:LanguageTag key="user_vd_tel_err"/>'}); 
	//		 $("#address").formValidator({empty:true,onFocus:'<view:LanguageTag key="user_vd_addr_tip"/>',onCorrect:"OK",onEmpty:"OK"}).inputValidator({max:128, onError: '<view:LanguageTag key="user_vd_addr_err"/>'});
               
             $("#realName").focus();
             
             if($.formValidator.pageIsValid){ 
				bool = true;
			}
 	   }
	 //展示静态密码设置框
     function changeSel(obj){
		 // 本地认证模式选择不进行本地认证时，移除后端认证中的不需要选项
		 if(obj == 3){
			$("#c").remove();
		 }else{
			if($('#backendAuth option:last').val() != 2){
				$("#b").after("<option value='2' id='c'><view:LanguageTag key='backend_auth_no_need'/></option>");
			}
		 }
	 }
		 
	/**
	 * 打开大窗口编辑 返回/关闭
	 * isRequery true:重查、false:只关闭窗口
	 */
	function goBackClose(isRequery) {
	  var topWin = FT.closeWinBig();
	  if(isRequery){
	  	topWin.currPage.query(true,true);
	  }
	  topWin.currPage = null;
	}
	 	   
	</script>
  </head>
  
  <body style="overflow:auto; overflow-x:hidden">
    <input id="contextPath" type="hidden" value="<%=path%>" />
    <input id="currentPage" type="hidden" value="${param.cPage}" />
   <form name="AddForm" id="AddForm" method="post" action="">
	<table width="100%" border="0"  cellpadding="0" cellspacing="0" class="topTableBg">
      <tr>
        <td >
        	<span class="topTableBgText">
			  	<view:LanguageTag key="user_info_edit"/>
            </span>
        </td>
      </tr>
    </table>
     <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td width="100%" valign="top">
		    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		    
		    <tr>
              <td align="right"><view:LanguageTag key="domain_orgunit"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
              <td>	
              		${userInfo.DOrgunitName}	 
	       	   </td>
	       	   <input id="dOrgunitId" type="hidden" name="userInfo.dOrgunitId" value="${userInfo.DOrgunitId}" />
			   <td class="divTipCss"><div id="orgunitNamesTip" style="width:100%"></div></td>
			</tr>
		    
		      <tr>
		        <td width="25%" align="right"><view:LanguageTag key="user_info_account"/><view:LanguageTag key="colon"/></td>
		        <td width="30%">${userInfo.userId}
		        			<input type="hidden" id="userId" name="userInfo.userId" value="${userInfo.userId}" />
		        			<input type="hidden" id="domainId" name="userInfo.domainId" value="${userInfo.domainId}" />
		        			<input id="orgunitId" type="hidden" name="userInfo.orgunitId" value="${userInfo.orgunitId}"  />
		        </td>
		        <td width="45%"></td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key="user_info_real_name"/><view:LanguageTag key="colon"/></td>
		        <td><input type="text" id="realName" name="userInfo.realName" value="${userInfo.realName}" class="formCss100"/></td>
		        <td class="divTipCss"><div id="realNameTip" style="width:100%"></div></td>
		      </tr>
      		<tr>
	      <td align="right"><view:LanguageTag key="user_local_auth_mode"/><view:LanguageTag key="colon"/></td>
	        <td>
	        <select id="localAuth" name="userInfo.localAuth" class="select100" onchange="changeSel(this.value)">
	        	<c:if test="${userInfo.localAuth==0}">
					<option value="0" selected><view:LanguageTag key="local_auth_only_vd_tkn"/></option>
					<option value="1"><view:LanguageTag key="local_auth_vd_pwd_tkn"/></option>
					<option value="2"><view:LanguageTag key="local_auth_only_vd_pwd"/></option>
				</c:if>
				<c:if test="${userInfo.localAuth==1}">
					<option value="0"><view:LanguageTag key="local_auth_only_vd_tkn"/></option>
					<option value="1" selected><view:LanguageTag key="local_auth_vd_pwd_tkn"/></option>
					<option value="2"><view:LanguageTag key="local_auth_only_vd_pwd"/></option>
				</c:if>
				<c:if test="${userInfo.localAuth==2}">
					<option value="0"><view:LanguageTag key="local_auth_only_vd_tkn"/></option>
					<option value="1"><view:LanguageTag key="local_auth_vd_pwd_tkn"/></option>
					<option value="2" selected><view:LanguageTag key="local_auth_only_vd_pwd"/></option>
				</c:if>
	        </select> 
	        </td>
        	<td class="divTipCss"><div id="localAuthTip" style="width:100%"></div></td>
          </tr>
      	  <tr>
		     <td align="right"><view:LanguageTag key="user_whether_backend_auth"/><view:LanguageTag key="colon"/></td>
		        <td >
		        <select id="backendAuth" name="userInfo.backEndAuth" class="select100" >
		        	<c:if test="${userInfo.backEndAuth==0}">
						<option value="0" id="a" selected><view:LanguageTag key="backend_auth_default"/></option>
						<option value="1" id="b"><view:LanguageTag key="backend_auth_need"/></option>
						<option value="2" id="c"><view:LanguageTag key="backend_auth_no_need"/></option>
					</c:if>
					<c:if test="${userInfo.backEndAuth==1}">
						<option value="0" id="a"><view:LanguageTag key="backend_auth_default"/></option>
						<option value="1" id="b" selected><view:LanguageTag key="backend_auth_need"/></option>
						<option value="2" id="c"><view:LanguageTag key="backend_auth_no_need"/></option>
					</c:if>
					<c:if test="${userInfo.backEndAuth==2}">
						<option value="0" id="a"><view:LanguageTag key="backend_auth_default"/></option>
						<option value="1" id="b"><view:LanguageTag key="backend_auth_need"/></option>
						<option value="2" id="c" selected><view:LanguageTag key="backend_auth_no_need"/></option>
					</c:if>
		        </select>  
		        </td>
		        <td class="divTipCss"><div id="backendAuthTip" style="width:100%"></div></td>
		      </tr>
		 	<tr>
		      <td align="right"><view:LanguageTag key="user_return_client_Rad_conf"/><view:LanguageTag key="colon"/></td>
		        <td>
		        	<select id="radProfileId" name="userInfo.radProfileId" class="select100" >
		        		<view:RadProfileTag dataSrc="${userInfo.radProfileId}" />
		        	</select> 
		        </td>
		        <td class="divTipCss"><div id="radProfileIdTip" style="width:100%"></div></td>
		    </tr>
		 	<!--   <tr>
		        <td align="right"><view:LanguageTag key="user_info_document_type"/><view:LanguageTag key="colon"/></td>
		        <td>
		            <select id="papersType" name="userInfo.papersType" class="select100" >
						<option value="0"><view:LanguageTag key="user_info_user_num"/></option>
		        	</select>
		        </td>
		        <td class="divTipCss"><div id="papersTypeTip" style="width:100%"></div>
		        </td>
		      </tr> --> 
      
		    <!--   <tr>
		        <td align="right" ><view:LanguageTag key="user_info_document_num"/><view:LanguageTag key="colon"/></td>
		        <td> 
		            <input type="text" id="papersNumber" name="userInfo.papersNumber" value="${userInfo.papersNumber}" class="formCss100"/>
		         </td>
		          <td class="divTipCss"><div id="papersNumberTip" style="width:100%"></div>
		              
		        </td> 
		      </tr>-->
		      <tr>
		        <td align="right"><view:LanguageTag key="common_info_email"/><view:LanguageTag key="colon"/></td>
		        <td><input type="text" id="email" name="userInfo.email" value="${userInfo.email}" class="formCss100"/></td>
		        <td class="divTipCss"><div id="emailTip" style="width:100%"></div></td>
		      </tr>
	<!--  	  <tr>
		        <td align="right"><view:LanguageTag key="common_info_tel"/><view:LanguageTag key="colon"/></td>
		        <td><input type="text" id="tel" name="userInfo.tel" value="${userInfo.tel}" class="formCss100"/></td>
		        <td class="divTipCss"><div id="telTip" style="width:100%"></div></td>
		      </tr>--> 
		      <tr>
		        <td align="right"><view:LanguageTag key="common_info_mobile"/><view:LanguageTag key="colon"/></td>
		        <td><input type="text" id="cellPhone" name="userInfo.cellPhone" value="${userInfo.cellPhone}" class="formCss100"/></td>
		        <td class="divTipCss"><div id="cellPhoneTip" style="width:100%"></div></td>
		      </tr>
	<!--  	  <tr>
		        <td align="right"><view:LanguageTag key="common_info_address"/><view:LanguageTag key="colon"/></td>
		        <td><textarea id="address" name="userInfo.address" class="textarea100">${userInfo.address}</textarea></td>
		        <td class="divTipCss"><div id="addressTip" style="width:100%"></div></td>
		      </tr>--> 
		      
			   <tr>
		        <td align="right"> </td>
		        <td><a href="#" id="editBtUser" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
		        	<a href="#" onclick="goBackClose(false);" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a>
		        </td>
		        <td>&nbsp;</td>
		      </tr>
		    </table>
	    </td>
	</tr>
	<tr>
	 <td>
	 </td>
	</tr>
	</table>
	 
  </form>
 
  </body>
</html>