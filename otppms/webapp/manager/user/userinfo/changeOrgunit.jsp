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
	<script language="javascript" src="<%=path%>/manager/user/userinfo/js/changeOrgunit.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script>
	<script language="javascript" type="text/javascript">
		// Start,多语言信息
		var sel_unbind_lang = '<view:LanguageTag key="user_must_sel_unbind"/>';
		var no_same_lang = '<view:LanguageTag key="user_sel_no_same_org"/>';
		var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
		// End,多语言信息
	</script>	 
	 
  </head>
  
  <body>
    <input id="contextPath" type="hidden" value="<%=path%>" />
    <form name="AddForm" id="AddForm" method="post" action="">
	<table width="100%" border="0"  cellpadding="0" cellspacing="0" class="topTableBg">
      <tr>
        <td >
        	<span class="topTableBgText">
        		<view:LanguageTag key="user_change_orgunit"/>
        	</span>
        </td>
      </tr>
    </table>
     <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td width="100%" valign="top">
		    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		    
		     <tr>
		        <td width="30%" align="right"><view:LanguageTag key="user_username"/><view:LanguageTag key="colon"/></td>
		        <td width="35%">${userInfo.userId}
		        				<input type="hidden" id="userId" name="userInfo.userId" value="${userInfo.userId}" />
		        				<input type="hidden" id="domainId" name="userInfo.domainId" value="${userInfo.domainId}" />
		        									
		        </td>
		        <td width="35%"></td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key="user_info_real_name"/><view:LanguageTag key="colon"/></td>
		        <td>${userInfo.realName}</td>
		        <td class="divTipCss"><div id="realNameTip" style="width:100%"></div></td>
		      </tr>
		    
		    <tr>
              <td align="right"><view:LanguageTag key="user_current_orgunit"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
              <td>	<input  type=hidden id="domaind" value="${userInfo.domainId}"/>
              		<input  type=hidden   value="${userInfo.DOrgunitName}"  name="userInfo.currentOrgunitName"/>
              		${userInfo.DOrgunitName}	 
	       	   </td>
			   <td class="divTipCss"><div id="" style="width:100%"></div></td>
			</tr>
		    
		    <tr>
              <td align="right"><view:LanguageTag key="user_target_org"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
              <td>	
              		<input type=hidden id="orgunitIds"   value="${userInfo.DOrgunitId}"/>
              		<input type=hidden id="orgunitIdsTemp" name="userInfo.dOrgunitId"  value="${userInfo.DOrgunitId}"/>
              		<input type=text id="orgunitNamesTemp"  onClick="selOrgunits(4,'<%=path%>');"  value="${userInfo.DOrgunitName}"  name="userInfo.targetOrgunitName" class="formCss100"  readonly />		 
	 
	       	   </td>
			   <td><div id="orgunitNamesTip" style="width:100%"></div></td>
			</tr>
			
			<!-- 提示 多用户绑定令牌 是否解绑 -->
			<c:if test="${not empty userInfo.reTokens}">
		     <tr>
		        <td width="25%" align="right"><view:LanguageTag key="user_unbundling"/><view:LanguageTag key="colon"/></td>
		        <td width="30%"><input type=checkbox id=unBindTag checked /></td>
		        <td width="45%"><input type=hidden id=isExsitUnBingTag value=1 /></td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key="user_tkn_bind_other_users_same_time"/><view:LanguageTag key="colon"/></td>
		        <td>
		        	<c:forEach items="${userInfo.reTokens}" var="tokens">
               			${tokens.token}<br/>
               		</c:forEach>
		        </td>
		        <td></td>
		      </tr>
		     </c:if> 
		      
		    <!-- 提示 无信息 多用户绑定令牌 是否解绑 -->
			<c:if test="${empty userInfo.reTokens}">
		     <tr>
		        <td width="25%" align="right"><view:LanguageTag key="user_unbundling"/><view:LanguageTag key="colon"/></td>
		        <td width="30%"><input type=checkbox id=unBindTag /></td>
		        <td width="45%"><input type=hidden id=isExsitUnBingTag value=0 /></td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key="user_tkn_bind_other_users_same_time"/><view:LanguageTag key="colon"/></td>
		        <td> <view:LanguageTag key="common_syntax_nothing"/>
		        </td>
		        <td></td>
		      </tr>
		     </c:if> 
		     
		      
		      
		      
		     <!-- 提示 是否变更令牌的组织机构 -->
			<c:if test="${not empty userInfo.leftTokens}">
		     <tr>
		        <td width="25%" align="right"><view:LanguageTag key="user_change_tkn_org"/><view:LanguageTag key="colon"/></td>
		        <td width="30%"><input type=checkbox id=changeTokenOrgTag checked /></td>
		        <td width="45%"></td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key="user_to_change_org_tkn"/><view:LanguageTag key="colon"/></td>
		        <td>
		        	<c:forEach items="${userInfo.leftTokens}" var="tokens">
               			${tokens.token}<br/>
               		</c:forEach>
		        </td>
		        <td></td>
		      </tr>
		     </c:if>    
		 
		 	 <!-- 提示 无信息 是否变更令牌的组织机构 -->
			<c:if test="${empty userInfo.leftTokens}">
		     <tr>
		        <td width="25%" align="right"><view:LanguageTag key="user_change_tkn_org"/><view:LanguageTag key="colon"/></td>
		        <td width="30%"><input type=checkbox id=changeTokenOrgTag /></td>
		        <td width="45%"></td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key="user_to_change_org_tkn"/><view:LanguageTag key="colon"/></td>
		        <td><view:LanguageTag key="common_syntax_nothing"/>
		        </td>
		        <td></td>
		      </tr>
		     </c:if>   
		 
			   <tr>
		        <td align="right"> </td>
		        <td>
		        	<a href="#" onClick="changeOrg();" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
		        	<a href="#" onClick="goBack();" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a>
		        
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