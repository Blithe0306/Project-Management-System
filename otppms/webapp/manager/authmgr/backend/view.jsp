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
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css">
	<link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css">
    <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
	<script language="javascript" type="text/javascript">
		$(document).ready(function(){
			$("tr[id='hidpolicy']").hide();
			var checkvalue = '${backendInfo.policy}';
		 	if (checkvalue != 0) {
		 		var showVal = '';
		 		$('input[name="backendpolicy"]').each(function () {
				   var result = checkvalue & this.value;
				   if (result != 0) {
						if (this.value == 1) showVal += '<view:LanguageTag key="auth_bk_local_user_not_exist_unbound_view"/><br/>';
					//	if (this.value == 2) showVal += '<view:LanguageTag key="auth_bk_local_user_exist_unbound_tk"/>；\n';
					//	if (this.value == 4) showVal += '<view:LanguageTag key="auth_bk_local_auth_err"/>；\n';
						if (this.value == 8) showVal += '<view:LanguageTag key="auth_bk_local_auth_succ"/>';
				   }
				});
				$("#policy").html(showVal);
		 	}
	});
	</script>
  </head>
 <body>
 <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
        <ul id="menu">
		 <li class="tabFocus"><view:LanguageTag key="auth_bk_detail_info"/></li>
	    </ul>
        <ul id="content">
		 <li class="conFocus">
		 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
             <tr>
               <td width="30%" align="right"><view:LanguageTag key="auth_bk_name"/><view:LanguageTag key="colon"/></td>
               <td width="40%">${backendInfo.backendname}</td>
               <td width="30%"></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="auth_bk_host"/><view:LanguageTag key="colon"/></td>
               <td>${backendInfo.host}</td>
               <td></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="auth_bk_sparehost"/><view:LanguageTag key="colon"/></td>
               <td>${backendInfo.sparehost}</td>
               <td></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="auth_bk_type"/><view:LanguageTag key="colon"/></td>
               <td>${backendInfo.backendtype == 0 ? "Radius" : "AD"}</td>
               <td></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="auth_bk_port"/><view:LanguageTag key="colon"/></td>
               <td>${backendInfo.port}</td>
               <td></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="auth_bk_enabled"/><view:LanguageTag key="colon"/></td>
               <td><c:if test="${backendInfo.enabled == 0}"><view:LanguageTag key="common_syntax_no"/></c:if><c:if test="${backendInfo.enabled == 1}"><view:LanguageTag key="common_syntax_yes"/></c:if></td>
               <td></td>
             </tr>
             <tr>
               <td align="right" valign="top"><view:LanguageTag key="auth_bk_policy"/><view:LanguageTag key="colon"/></td>
               <td id="policy" colspan="2"></td>
             </tr>
             <tr style="display:none">
               <td align="right"><view:LanguageTag key="auth_bk_domain_name"/><view:LanguageTag key="colon"/></td>
               <td>${backendInfo.domainStr}</td>
               <td></td>
             </tr>
             <!--  
             <tr>
               <td align="right"><view:LanguageTag key="auth_bk_priority"/><view:LanguageTag key="colon"/></td>
               <td>${backendInfo.priority == 0 ? '' : backendInfo.priority}</td>
               <td></td>
             </tr>
              -->
             <!--  
             <c:if test="${backendInfo.backendtype == 1}">
             <tr>
               <td align="right"><view:LanguageTag key="auth_bk_ldap_catalog"/><view:LanguageTag key="colon"/></td>
               <td>${backendInfo.basedn}</td>
               <td></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="auth_bk_filter"/><view:LanguageTag key="colon"/></td>
               <td>${backendInfo.filter}</td>
               <td></td>
             </tr>
             </c:if>
             -->
             <c:if test="${backendInfo.backendtype == 1}">
             <tr style="display:none">
               <td align="right"><view:LanguageTag key="auth_bk_specify_AD_domain"/><view:LanguageTag key="colon"/></td>
               <td><input type="checkbox" id="usernamerule" name="backendInfo.usernamerule" <c:if test="${backendInfo.usernamerule == 1}">checked</c:if> disabled/></td>
               <td></td>
             </tr>

             <c:if test="${backendInfo.usernamerule == 1}">
             <tr>
               <td align="right"><view:LanguageTag key="auth_bk_domain"/><view:LanguageTag key="colon"/></td>
               <td>${backendInfo.delimiter}</td>
               <td></td>
             </tr>
             </c:if>
             </c:if>
             
             <tr>
               <td align="right"><view:LanguageTag key="auth_bk_timeout"/><view:LanguageTag key="colon"/></td>
               <td>${backendInfo.timeout == 0 ? '' : backendInfo.timeout}</td>
               <td></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="auth_bk_retrycnt"/><view:LanguageTag key="colon"/></td>
               <td>${backendInfo.retrycnt == 0 ? '' : backendInfo.retrycnt}</td>
               <td></td>
             </tr>
             <tr id="hidpolicy">
               <td></td>
               <td>
               	 <input type="checkbox" id="policy1" name="backendpolicy" value="1" />&nbsp;<view:LanguageTag key="auth_bk_local_user_not_exist_unbound"/>
		     <!--<input type="checkbox" id="policy2" name="backendpolicy" value="2" />&nbsp;<view:LanguageTag key="auth_bk_local_user_exist_unbound_tk"/>-->
		     <!--<input type="checkbox" id="policy3" name="backendpolicy" value="4" />&nbsp;<view:LanguageTag key="auth_bk_local_auth_err"/>-->
		         <input type="checkbox" id="policy4" name="backendpolicy" value="8" />&nbsp;<view:LanguageTag key="auth_bk_local_auth_succ"/>
               </td>
               <td></td>
             </tr>
           </table>
          </li>
         </ul>
		</td>
      </tr>
    </table>
  </body>
</html>