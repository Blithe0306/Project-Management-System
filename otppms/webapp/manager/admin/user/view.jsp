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
    <link href="<%=path%>/manager/common/ligerUI/skins/Aqua/css/ligerui-alert.css" rel="stylesheet" type="text/css" />
 
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
    <script language="javascript" type="text/javascript">
	<!--
		$(function() {
			$("#menu li").each(function(index) { //带参数遍历各个选项卡
				$(this).click(function() { //注册每个选卡的单击事件
					$("#menu li.tabFocus").removeClass("tabFocus"); //移除已选中的样式
					$(this).addClass("tabFocus"); //增加当前选中项的样式
						//显示选项卡对应的内容并隐藏未被选中的内容
					$("#content li:eq(" + index + ")").show()
	                    .siblings().hide();
	                });
           	});
           	
           	//为了解决chrome 浏览器权限页签显示不出来问题 焦点必须先选中权限页面 然后再调整到基本信息页面
           	window.setTimeout(function(){
           	    //等权限树初始化了再跳到第一个页签页面
           		$("#menu li.tabFocus").removeClass("tabFocus"); //移除已选中的样式
           		$("#menu li:eq(0)").addClass("tabFocus"); //增加当前选中项的样式
           		$("#content li:eq(0)").show()
	                    .siblings().hide();
           	},10);
        
        });
	//-->
	</script>
 
  </head>
 <body style="overflow:auto; overflow-y:hidden">
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
       <ul id="menu">
		 <li class="tabFocus"><view:LanguageTag key="common_syntax_base_info"/></li>
		 <li><view:LanguageTag key="admin_role"/></li>
		 <li><view:LanguageTag key="admin_perm"/></li>
	    </ul>
        <ul id="content">
		 <li>
		 <div style="height: 400px;overflow:scroll;" >
		 <table style="overflow:scroll;" width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
             <tr>
               <td width="30%" align="right" valign="top"><view:LanguageTag key="admin_info_account"/><view:LanguageTag key="colon"/></td>
               <td width="40%">${adminUser.adminid}</td>
               <td width="30%">&nbsp;</td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="common_info_realname"/><view:LanguageTag key="colon"/></td>
               <td>${adminUser.realname}</td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="admin_login_mode"/><view:LanguageTag key="colon"/></td>
               <td>
	        	<c:if test="${adminUser.localauth==0}"><view:LanguageTag key="login_mode_only_vd_pwd"/></c:if>
	        	<c:if test="${adminUser.localauth==1}"><view:LanguageTag key="login_mode_only_vd_pin"/></c:if>
	        	<c:if test="${adminUser.localauth==2}"><view:LanguageTag key="login_mode_vd_pwd_pin"/></c:if>
			   </td>
              </tr>
              <tr>
               <td align="right"><view:LanguageTag key="admin_pwd_set_time"/><view:LanguageTag key="colon"/></td>
               <td>${adminUser.pwdsettimeStr}</td>
               <td>&nbsp;</td>
             </tr>
              <tr>
               <td align="right"><view:LanguageTag key="admin_whether_lock"/><view:LanguageTag key="colon"/></td>
               <td>
               	<c:if test="${adminUser.locked == 0}"><view:LanguageTag key="lock_state_no_locking"/></c:if>
               	<c:if test="${adminUser.locked == 1}"><view:LanguageTag key="lock_state_temp"/></c:if>
               	<c:if test="${adminUser.locked == 2}"><view:LanguageTag key="lock_state_perm"/></c:if>
               </td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="admin_locked_time"/><view:LanguageTag key="colon"/></td>
               <td>${adminUser.loginlocktimeStr}</td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="common_syntax_whether_enable"/><view:LanguageTag key="colon"/></td>
               <td><c:if test="${adminUser.enabled == 0}"><view:LanguageTag key="common_syntax_no"/></c:if><c:if test="${adminUser.enabled == 1}"><view:LanguageTag key="common_syntax_yes"/></c:if></td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="common_info_creator"/><view:LanguageTag key="colon"/></td>
               <td>${adminUser.createuser}</td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="admin_login_count"/><view:LanguageTag key="colon"/></td>
               <td>${adminUser.logincnt}</td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="common_info_email"/><view:LanguageTag key="colon"/></td>
               <td>${adminUser.email}</td>
               <td>&nbsp;</td>
             </tr>
       <!--  <tr>
               <td align="right"><view:LanguageTag key="common_info_address"/><view:LanguageTag key="colon"/></td>
               <td>${adminUser.address}</td>
               <td>&nbsp;</td>
             </tr> -->
       <!--        <tr>
               <td align="right"><view:LanguageTag key="common_info_tel"/><view:LanguageTag key="colon"/></td>
               <td>${adminUser.tel}</td>
               <td>&nbsp;</td>
             </tr>-->
             <tr>
               <td align="right"><view:LanguageTag key="common_info_mobile"/><view:LanguageTag key="colon"/></td>
               <td>${adminUser.cellphone}</td>
               <td>&nbsp;</td>
             </tr>
              <tr>
               <td align="right"><view:LanguageTag key="common_syntax_create_time"/><view:LanguageTag key="colon"/></td>
               <td>${adminUser.createtimeStr}</td>
               <td>&nbsp;</td>
             </tr>
              <tr>
               <td align="right"><view:LanguageTag key="common_syntax_desc"/><view:LanguageTag key="colon"/></td>
               <td>${adminUser.descp}</td>
               <td>&nbsp;</td>
             </tr>
           </table>
           </div>
           </li>
             <li>
              <div style="height: 400px;overflow:scroll;" >
             <table style="overflow:scroll;" width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
             <tr>
               <td width="25%" align="right" valign="top"><view:LanguageTag key="common_info_role"/><view:LanguageTag key="colon"/></td>
               <td width="75%">
                <c:if test="${empty adminUser.hidAdminRoles}"><view:LanguageTag key="common_syntax_nothing"/></c:if>
				<c:forEach items="${adminUser.hidAdminRoles}" var="role">
               		${role.roleName}<br/>
               	</c:forEach>
			   </td>
             </tr>
           </table>
           </div>
		</li>
		<li>
           <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
             <tr><td>
                 <iframe id="bottomFrame"
						src="<%=path%>/manager/admin/role/perm_view.jsp?oper=view&adminId=${adminUser.adminid}"
						scrolling="no" name="bottomFrame" width="100%" height=400
						frameborder="0" style="color: red">
				 </iframe>
			 </td></tr>
           </table>
		 </li> 
       </ul>
		</td>
      </tr>
    </table>
 </body>
</html>