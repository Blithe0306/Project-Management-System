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
	// Start,多语言提取
	var del_default_lang = '<view:LanguageTag key="domain_not_del_default"/>';
	var conf_del_lang = '<view:LanguageTag key="common_conf_del"/>';
	var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
	// End,多语言提取
	
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
 	
 	<c:if test='${1==treeOrgunitInfo.flag}'> <!-- 如果是域 -->
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top"><input type="hidden" value="<%=path %>" id="contextPath" />
	       <ul id="menu">
			 <li class="tabFocus"><view:LanguageTag key="domain_info"/></li>
		    </ul>
	        <ul id="content">
			 <li class="conFocus">
			 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">	             
	              <tr>
	               <td width="30%" align="right" valign="top"><view:LanguageTag key="domain_info_code"/><view:LanguageTag key="colon"/></td>
	               <td width="40%">${treeOrgunitInfo.mark}</td>
	               <td width="30%">&nbsp;</td>
	             </tr>
	             <tr>
	               <td align="right"><view:LanguageTag key="domain_info_name"/><view:LanguageTag key="colon"/></td>
	               <td>${treeOrgunitInfo.name}</td>
	               <td>&nbsp;</td>
	             </tr>
	             
	             <tr>
	               <td align="right"><view:LanguageTag key="domain_info_default"/><view:LanguageTag key="colon"/></td>
	               <td>
	               	<c:if test="${1==treeOrgunitInfo.isdefault}"><view:LanguageTag key="common_syntax_yes"/></c:if>
	               	<c:if test="${0==treeOrgunitInfo.isdefault}"><view:LanguageTag key="common_syntax_no"/></c:if>
	               
	               </td>
	               <td>&nbsp;</td>
	             </tr>
	             
	              <tr>
	               <td align="right"><view:LanguageTag key="admin_info"/><view:LanguageTag key="colon"/></td>
	               <td>
	               		<c:forEach items="${treeOrgunitInfo.admins}" var="admin">
						     ${admin}<br/>
						</c:forEach>
					</td>
	               <td>&nbsp;</td>
	             </tr>                       
	              <tr>
	               <td align="right" valign="top"><view:LanguageTag key="common_syntax_desc"/><view:LanguageTag key="colon"/></td>
	               <td><div class="autoLine">${treeOrgunitInfo.descp}</div></td>
	               <td>&nbsp;</td>
	             </tr>
	           </table>
	          </ul>
		</td>
      </tr>
    </table>
    	
    <c:if test='${2==treeOrgunitInfo.readWriteFlag}'> <!-- 如果可读可写 -->	
    	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
       <ul id="menu">
		 <li class="tabFocus"><view:LanguageTag key="common_syntax_operation"/></li>
	    </ul>
        <ul id="content">
		 <li class="conFocus">
		 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
		 	
             <tr>
               <td align="right"></td>
               <td align="center">
               		 <!-- 添加子节点是组织机构 --><a href="#" onClick="javascript:addchild()" class="isLink_HeiSe"><view:AdminPermTag key="080101" path="<%=path%>" langKey="org_add_child_org" type="1"/></a>&nbsp;&nbsp;
               		<!-- 修改域 --> <a href="#" onClick="javascript:editObj(${treeOrgunitInfo.id},${treeOrgunitInfo.flag})" class="isLink_HeiSe"><view:AdminPermTag key="080002" path="<%=path%>" langKey="common_syntax_edit" type="1"/></a> &nbsp;&nbsp;
               		<!-- 删除域 --><a href="#" onClick="javascript:delData()" class="isLink_HeiSe"><view:AdminPermTag key="080003" path="<%=path%>" langKey="common_syntax_delete" type="1" /></a>
               </td>
               <td>&nbsp;</td>
             </tr>
           </table>
          </ul>
		</td>
      </tr>
    </table>
   </c:if> 
     
     
    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
       <ul id="menu">
		 <li class="tabFocus"><view:LanguageTag key="domain_other_info"/></li>
	    </ul>
        <ul id="content">
		 <li class="conFocus">
		 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
             <tr>
               <td align="right"></td>
               <td align="center">
               		 <view:LanguageTag key="org_info_has_users"/><view:LanguageTag key="colon"/>${treeOrgunitInfo.userCount }  
               		 <c:if test='${2==treeOrgunitInfo.readWriteFlag}'> 	
               		  <a href="javascript:void(0)" onClick="f_addTab('2', this ,'03', '<view:LanguageTag key="common_menu_user_list"/>', '<%=path%>/manager/user/userinfo/list.jsp?flag=1&dOrgunitId=${treeOrgunitInfo.orgunitArr}&DOrgunitName=${treeOrgunitInfo.orgunitNameArr}');">
               		  <span><view:AdminPermTag key="02" path="<%=path%>" langKey="common_menu_user_list" type="0" /></span> </a> 
               		  </c:if>
               		  <br/>
               		 <view:LanguageTag key="org_info_has_tkns"/><view:LanguageTag key="colon"/>${treeOrgunitInfo.tokenCount } 
               		 <c:if test='${2==treeOrgunitInfo.readWriteFlag}'> 	
               		 <a href="javascript:void(0)" onClick="f_addTab('3', this ,'05', '<view:LanguageTag key="common_menu_tkn_list"/>', '<%=path%>/manager/token/list.jsp?flag=1&dOrgunitId=${treeOrgunitInfo.orgunitArr}&DOrgunitName=${treeOrgunitInfo.orgunitNameArr}');" >
               		 <span><view:AdminPermTag key="03" path="<%=path%>" langKey="common_menu_tkn_list" type="0" /> </span> </a>
               		 </c:if>
               </td>
               <td>&nbsp;</td>
             </tr>
           </table>
          </ul>
		</td>
      </tr>
    </table> 	
   </c:if> 	
 
 
 	<c:if test='${2==treeOrgunitInfo.flag}'> <!-- 如果是组织机构 -->
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top"><input type="hidden" value="<%=path %>" id="contextPath" />
	       <ul id="menu">
			 <li class="tabFocus"><view:LanguageTag key="org_info"/></li>
		    </ul>
	        <ul id="content">
			 <li class="conFocus">
			 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">	            
	              <tr>
	               <td align="right"><view:LanguageTag key="domain_info_belongs"/><view:LanguageTag key="colon"/></td>
	               <td>${treeOrgunitInfo.domainInfo.domainName}</td>
	               <td>&nbsp;</td>
	             </tr> 
	             <tr>
	               <td align="right"><view:LanguageTag key="org_parentorg"/><view:LanguageTag key="colon"/></td>
	               <td>${treeOrgunitInfo.parentOrgunitInfo.name}</td>
	               <td>&nbsp;</td>
	             </tr>
	             <tr>
	               <td width="30%" align="right" valign="top" ><view:LanguageTag key="org_code"/><view:LanguageTag key="colon"/></td>
	               <td width="40%">${treeOrgunitInfo.mark}</td>
	               <td width="30%">&nbsp;</td>
	             </tr>
	             <tr>
	               <td align="right"><view:LanguageTag key="org_name"/><view:LanguageTag key="colon"/></td>
	               <td>${treeOrgunitInfo.name}</td>
	               <td>&nbsp;</td>
	             </tr>
	              <tr>
	               <td align="right"><view:LanguageTag key="admin_info"/><view:LanguageTag key="colon"/></td>
	               <td>
	               		<c:forEach items="${treeOrgunitInfo.admins}" var="admin">
						     ${admin} <br/>
						</c:forEach>
						
					</td>
	               <td>&nbsp;</td>
	             </tr>                       
	              <tr>
	               <td align="right" valign="top"><view:LanguageTag key="common_syntax_desc"/><view:LanguageTag key="colon"/></td>
	               <td><div class="autoLine">${treeOrgunitInfo.descp}</div></td>
	               <td>&nbsp;</td>
	             </tr>
	           </table>
	          </ul>
		</td>
      </tr>
    </table>
    <c:if test='${2==treeOrgunitInfo.readWriteFlag}'> 	
    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
       <ul id="menu">
		 <li class="tabFocus"><view:LanguageTag key="common_syntax_operation"/></li>
	    </ul>
        <ul id="content">
		 <li class="conFocus">
		 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
		 	
             <tr>
               <td align="right">&nbsp;</td>
               <td align="center">
               		<view:AdminPermTag key="080102" path="<%=path%>" langKey="org_edit_org" type="2"/>
                    <view:AdminPermTag key="080101" path="<%=path%>" langKey="org_add_org" type="2"/>
                    <view:AdminPermTag key="080103" path="<%=path%>" langKey="org_delete_org" type="2"/>
                    <view:AdminPermTag key="080104" path="<%=path%>" langKey="org_move_org" type="2"/>              		
               </td>
               <td>&nbsp;</td>
             </tr>
           </table>
          </ul>
		</td>
      </tr>
    </table>
    </c:if>
     
     
    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
       <ul id="menu">
		 <li class="tabFocus"><view:LanguageTag key="domain_other_info"/></li>
	    </ul>
        <ul id="content">
		 <li class="conFocus">
		 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
             <tr>
               <td align="right"></td>
               <td align="center">
               		  <view:LanguageTag key="org_info_has_users"/><view:LanguageTag key="colon"/>${treeOrgunitInfo.userCount }  
               		  <c:if test='${2==treeOrgunitInfo.readWriteFlag}'> 	
               		  <a href="javascript:void(0)" onClick="f_addTab('2', this ,'03', '<view:LanguageTag key="common_menu_user_list"/>', '<%=path%>/manager/user/userinfo/list.jsp?flag=2&dOrgunitId=${treeOrgunitInfo.domainId}:${treeOrgunitInfo.id},&DOrgunitName=${treeOrgunitInfo.name},');">
               		  <span><view:AdminPermTag key="02" path="<%=path%>" langKey="common_menu_user_list" type="0" /></span> </a> 
               		  </c:if>
               		  <br/>
               		 <view:LanguageTag key="org_info_has_tkns"/><view:LanguageTag key="colon"/>${treeOrgunitInfo.tokenCount } 
               		 <c:if test='${2==treeOrgunitInfo.readWriteFlag}'> 	
               		 <a href="javascript:void(0)" onClick="f_addTab('3', this ,'05', '<view:LanguageTag key="common_menu_tkn_list"/>', '<%=path%>/manager/token/list.jsp?flag=2&dOrgunitId=${treeOrgunitInfo.domainId}:${treeOrgunitInfo.id},&DOrgunitName=${treeOrgunitInfo.name},');" >
               		 <span><view:AdminPermTag key="03" path="<%=path%>" langKey="common_menu_tkn_list" type="0" /> </span> </a>
               		 </c:if>
               </td>
               <td>&nbsp;</td>
             </tr>
           </table>
          </ul>
		</td>
      </tr>
    </table> 	
   </c:if> 	  
     
 </body>
</html>