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
	<script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/view.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>
	<script language="javascript" >
	function f_addTab(number,obj,tabid, text, url){
		//window.parent.removeTabItemF(tabid);
	    //window.parent.addTabItemF(tabid,text,url);
	    
	    // 判断是否存在此tab 不存在添加，存在覆盖修改，不要先删除remove再添加 删除会触发左侧导航刷新事件
	   	if(window.parent.isTabItem(tabid)){
	   		window.parent.overrideTabItemF(tabid,text,url);
	   		window.parent.selectTabItemF(tabid);
	   	}else{
			window.parent.addTabItemF(tabid,text,url);	    		
	   	}
	} 
	</script>
	
  </head>
 <body style="overflow:hidden;"> 
 	<input type=hidden id="id" value="${treeOrgunitInfo.id}"/>
 	<input type=hidden id="isdefault" value="${treeOrgunitInfo.isdefault}"/>
 	<input type=hidden id="flag" value="${treeOrgunitInfo.flag}"/>
 	<input type=hidden id="domainId" value="${treeOrgunitInfo.domainId}"/>
	
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td>  
		  <span class="topTableBgText"><view:LanguageTag key="org_enterprise_company_info"/></span></td>
      </tr>
    </table> 
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td valign="top">
           <table width="98%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable" style="table-layout:fixed;word-wrap:break-word;word-break:break-all;">
		      <tr>
		        <td width="40%" align="right"><view:LanguageTag key="org_enterprise_company_name"/><view:LanguageTag key="colon"/></td>
		        <td width="60%">${treeOrgunitInfo.name}</td>
		      </tr>
		      <tr>
		        <td width="40%" align="right"><view:LanguageTag key="domain_info_code"/><view:LanguageTag key="colon"/></td>
		        <td width="60%">${treeOrgunitInfo.mark}</td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key="org_be_manager_admin"/><view:LanguageTag key="colon"/></td>
	            <td width="60%">
				<c:forEach items="${treeOrgunitInfo.admins}" var="admin">
						     ${admin}<br/>
				  </c:forEach>				</td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key="org_description"/><view:LanguageTag key="colon"/></td>
	            <td width="60%"><div class="autoLine">${treeOrgunitInfo.descp}</div></td>
		      </tr>
		      <tr>
		        <td align="right">&nbsp;</td>
		        <td>&nbsp;</td>
	         </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key="org_have_user_count"/><view:LanguageTag key="colon"/></td>
                <td>
					${treeOrgunitInfo.userCount} <view:LanguageTag key="report_user_caption_place"/>
               		<c:if test='${2==treeOrgunitInfo.readWriteFlag}'>
               		   <view:LanguageTag key="comma"/>
               		   <a href="javascript:void(0)" onClick="f_addTab('2', this ,'0200', '<view:LanguageTag key="common_menu_user_list"/>', '<%=path%>/manager/user/userinfo/list.jsp?flag=1&mode=2&dOrgunitId=${treeOrgunitInfo.orgunitArr}&DOrgunitName=${treeOrgunitInfo.orgunitNameArr}');">
               		   <span><view:AdminPermTag key="0200" path="<%=path%>" langKey="common_menu_user_list" type="0" /></span> </a>   
               		</c:if>				
               </td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key="org_have_token_count"/><view:LanguageTag key="colon"/></td>
	            <td>
					 ${treeOrgunitInfo.tokenCount} <view:LanguageTag key="report_token_pay"/>
               		 <c:if test='${2==treeOrgunitInfo.readWriteFlag}'> 	
               		 	<view:LanguageTag key="comma"/>
               		 	<a href="javascript:void(0)" onClick="f_addTab('3', this ,'0300', '<view:LanguageTag key="common_menu_tkn_list"/>', '<%=path%>/manager/token/list.jsp?flag=1&mode=3&dOrgunitId=${treeOrgunitInfo.orgunitArr}&DOrgunitName=${treeOrgunitInfo.orgunitNameArr}');" >
               		 	<span><view:AdminPermTag key="0300" path="<%=path%>" langKey="common_menu_tkn_list" type="0" /> </span> </a>               		 
               		 </c:if>				
               	</td>
	         </tr>
		      <tr>
		        <td align="right">&nbsp;</td>
	            <td>&nbsp;</td>
	         </tr>
		      <tr>
		        <td colspan="2" align="center"><table width="300" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                    <c:if test='${2==treeOrgunitInfo.readWriteFlag}'> 	
                     <!-- 编辑域的权限按钮 -->
                     <view:AdminPermTag key="080002" path="<%=path%>" langKey="org_edit_org" type="2" />
                     <!-- 添加子机构的权限按钮 不是添加域的权限 -->
		     		 <view:AdminPermTag key="080101" path="<%=path%>" langKey="org_add_org" type="2" />
		     		 </c:if>
				    </td>
                  </tr>
                </table></td>
             </tr>
		      <tr>
		        <td>&nbsp;</td>
	            <td>&nbsp;</td>
	         </tr>
			</table>
	    </td>
	  </tr>
    </table>
  	</body>
</html>