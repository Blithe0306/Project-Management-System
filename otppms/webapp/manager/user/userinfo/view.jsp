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
        })
        //查看用户关联的令牌
        function viewToken(token){
          window.location.href="<%=path%>/manager/token/token!view.action?tokenInfo.token=" + token;
        }
	//-->
	</script>
  </head>
  
  <body>
  <form name="AddForm" method="post" action="">
    <table width="99%" border="0" align="right" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
		<ul id="menu">
		 <li class="tabFocus"><view:LanguageTag key="user_info"/></li>
		 <li><view:LanguageTag key="user_already_bind_tkn"/></li>
	    </ul>
	    <ul id="content">
		 <li class="conFocus">
		   <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable" >
		   	 <tr>
               <td width="35%" align="right"><view:LanguageTag key="domain_orgunit"/><view:LanguageTag key="colon"/></td>
               <td width="65%">${userInfo.DOrgunitName}	</td>
             </tr>
             <tr>
               <td width="35%" align="right"><view:LanguageTag key="user_info_account"/><view:LanguageTag key="colon"/></td>
               <td width="65%">${userInfo.userId}</td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="user_info_real_name"/><view:LanguageTag key="colon"/></td>
               <td>${userInfo.realName}</td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="user_local_auth_mode"/><view:LanguageTag key="colon"/></td>
               <td>
               	  <c:if test="${userInfo.localAuth==0}">
					<view:LanguageTag key="local_auth_only_vd_tkn"/>
			  	  </c:if>
				  <c:if test="${userInfo.localAuth==1}">
					<view:LanguageTag key="local_auth_vd_pwd_tkn"/>
				  </c:if>
				  <c:if test="${userInfo.localAuth==2}">
					<view:LanguageTag key="local_auth_only_vd_pwd"/>
				  </c:if>
               </td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="user_whether_backend_auth"/><view:LanguageTag key="colon"/></td>
               <td>
	              <c:if test="${userInfo.backEndAuth==0}">
					<view:LanguageTag key="backend_auth_default"/>
			  	  </c:if>
				  <c:if test="${userInfo.backEndAuth==1}">
					<view:LanguageTag key="backend_auth_need"/>
				  </c:if>
				  <c:if test="${userInfo.backEndAuth==2}">
					<view:LanguageTag key="backend_auth_no_need"/>
				  </c:if>
               </td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="user_return_client_Rad_conf"/><view:LanguageTag key="colon"/></td>
               <td>
               		<c:if test="${userInfo.radProfileId==0}"><view:LanguageTag key="common_syntax_not_return"/></c:if>
               		<c:if test="${userInfo.radProfileId!=0}">${userInfo.radProfileName}</c:if>
               	</td>
             </tr>
         <!--   <tr>
               <td align="right">
               <c:if test="${userInfo.papersType == 0}"><view:LanguageTag key="user_info_user_num"/><view:LanguageTag key="colon"/></c:if>
               </td>
               <td>
               ${userInfo.papersNumber}
               </td>
             </tr> -->   
             <tr>
               <td align="right"><view:LanguageTag key="common_info_email"/><view:LanguageTag key="colon"/></td>
               <td>${userInfo.email}</td>
             </tr>
      <!--   <tr>
               <td align="right"><view:LanguageTag key="common_info_tel"/><view:LanguageTag key="colon"/></td>
               <td>${userInfo.tel}</td>
             </tr> -->
             <tr>
               <td align="right"><view:LanguageTag key="common_info_mobile"/><view:LanguageTag key="colon"/></td>
               <td>${userInfo.cellPhone}</td>
             </tr>
        <!-- <tr>
               <td align="right"><view:LanguageTag key="common_info_address"/><view:LanguageTag key="colon"/></td>
               <td>${userInfo.address}</td>
             </tr>-->
             <tr>
		        <td align="right"><view:LanguageTag key='user_account_lock_state'/><view:LanguageTag key="colon"/></td>
		        <td>  
		          <c:if test="${userInfo.locked==0}"><view:LanguageTag key="lock_state_no_locking"/></c:if>
		          <c:if test="${userInfo.locked==1}"><view:LanguageTag key="lock_state_temp"/></c:if>
		          <c:if test="${userInfo.locked==2}"><view:LanguageTag key="lock_state_perm"/></c:if>
		        </td>
		      </tr>
             <tr>
		        <td align="right"><view:LanguageTag key='user_temp_lock_err_num'/><view:LanguageTag key="colon"/></td>
		        <td>${userInfo.tempLoginErrCnt}</td>
		      </tr>
             <tr>
		        <td align="right"><view:LanguageTag key='user_perm_lock_err_num'/><view:LanguageTag key="colon"/></td>
		        <td>${userInfo.longLoginErrCnt}</td>
		      </tr>
           </table>
		 </li>
		 <li>
		 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
		 	<c:if test="${empty userInfo.tokens}">
             <tr>
               <td colspan="5" align="center" valign="top"><view:LanguageTag key="common_syntax_nothing"/></td>
               </tr>
			 </c:if>
			 <c:if test="${not empty userInfo.tokens}">
             <tr>
               <td width="26%"><view:LanguageTag key="tkn_comm_tknum"/></td>
               <td width="20%"><view:LanguageTag key="tkn_comm_type"/></td>
               <td width="20%"><view:LanguageTag key="tkn_comm_physical_type"/></td>
               <td width="17%"><view:LanguageTag key="user_pwd_length"/></td>
               <td width="17%"><view:LanguageTag key="user_pwd_indep_cycle"/></td>
             </tr>
			 <c:forEach items="${userInfo.tokens}" var="userToken">
             <tr>
               <td><a href="javascript:viewToken('${userToken.token}')">${userToken.token}</a></td>
               <td>
	           <c:if test="${userToken.producttype==0}">c100</c:if>
	           <c:if test="${userToken.producttype==1}">c200</c:if>
	           <c:if test="${userToken.producttype==2}">c300</c:if>
	           <c:if test="${userToken.producttype==100}">c100</c:if>
	           <c:if test="${userToken.producttype==101}">c200</c:if>
	           <c:if test="${userToken.producttype==102}">c300</c:if>
	           <c:if test="${userToken.producttype==200}">c100</c:if>
	           <c:if test="${userToken.producttype==201}">c200</c:if>
	           <c:if test="${userToken.producttype==202}">c300</c:if>
	           <c:if test="${userToken.producttype==600}">c100</c:if>
	           </td>
	           <td>
               <c:if test="${userToken.physicaltype==0}"><view:LanguageTag key="tkn_physical_hard"/></c:if>
	           <c:if test="${userToken.physicaltype==1}"><view:LanguageTag key="tkn_physical_mobile"/></c:if>
	           <c:if test="${userToken.physicaltype==2}"><view:LanguageTag key="tkn_physical_soft"/></c:if>
	           <c:if test="${userToken.physicaltype==6}"><view:LanguageTag key="tkn_physical_sms"/></c:if>
               </td>
               <td>${userToken.otplen}</td>
               <td>${userToken.intervaltime}</td>
             </tr>
			 </c:forEach>
			 </c:if>      
           </table>
	      </li>
		
        </ul>
		</td>
      </tr>
    </table>
  </form>
  </body>
</html>