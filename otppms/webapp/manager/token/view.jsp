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
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
	
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
 	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/token/js/showUser.js"></script>
	
	<script language="javascript" type="text/javascript">
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
            
            var noText = '<view:LanguageTag key="common_syntax_nothing"/>';
            if($("#adminTd").text()==""){
            	$("#adminTd").text(noText);
            }
            
            if($("#userTd").text()==""){
            	$("#userTd").text(noText);
            }
        })
	</script>
  </head>
  
  <body>
  <jsp:include page="/manager/user/userinfo/common.jsp" />
  <input type="hidden" id="contextPath" value="<%=path%>"/> 
  <input type="hidden" name="currentPage" value="${param.curPage}" id="currentPage"/>
  <form name="AddForm" method="post" action="">
    <table width="99%" border="0" align="right" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td width="100%">
			<ul id="menu">
			 <li class="tabFocus"><view:LanguageTag key='tkn_base_info'/></li>
			 <li><view:LanguageTag key='tkn_comm_state'/></li>
			 <li><view:LanguageTag key='tkn_binding_admin_user'/></li>
		    </ul>
		    <ul id="content">
		     <!-- 令牌基本信息 -->
			 <li class="conFocus">
			 	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
	    			<tr>
			            <td width="27%" align="right"><view:LanguageTag key='tkn_comm_tknum'/><view:LanguageTag key="colon"/></td>
			            <td width="23%" id="tokenvalue">${tokenInfo.token}</td>
			            <td width="28%" align="right"><view:LanguageTag key="tkn_orgunit"/><view:LanguageTag key="colon"/></td>
				        <td width="22%" >${tokenInfo.domainOrgunitName}</td>
      				</tr>
      				<tr>
				        <td align="right"><view:LanguageTag key='tkn_comm_type'/><view:LanguageTag key="colon"/></td>
			            <td > 
				           <c:if test="${tokenInfo.producttype==0}">OTP c100</c:if>
				           <c:if test="${tokenInfo.producttype==1}">OTP c200</c:if>
				           <c:if test="${tokenInfo.producttype==2}">OTP c300</c:if>
				           <c:if test="${tokenInfo.producttype==100}">OTP c100</c:if>
				           <c:if test="${tokenInfo.producttype==101}">OTP c200</c:if>
				           <c:if test="${tokenInfo.producttype==102}">OTP c300</c:if>
				           <c:if test="${tokenInfo.producttype==200}">OTP c100</c:if>
				           <c:if test="${tokenInfo.producttype==201}">OTP c200</c:if>
				           <c:if test="${tokenInfo.producttype==202}">OTP c300</c:if>
				           <c:if test="${tokenInfo.producttype==600}">OTP c100</c:if>
			        	</td>
				        <td align="right"><view:LanguageTag key='tkn_comm_physical_type'/><view:LanguageTag key="colon"/></td>
				        <td >
				           <c:if test="${tokenInfo.physicaltype==0}"><view:LanguageTag key="tkn_physical_hard"/></c:if>
				           <c:if test="${tokenInfo.physicaltype==1}"><view:LanguageTag key="tkn_physical_mobile"/></c:if>
				           <c:if test="${tokenInfo.physicaltype==2}"><view:LanguageTag key="tkn_physical_soft"/></c:if>
				           <c:if test="${tokenInfo.physicaltype==6}"><view:LanguageTag key="tkn_physical_sms"/></c:if>
				        </td>
				    </tr>
      				<tr>
				        <td  align="right"><view:LanguageTag key='tkn_comm_length'/><view:LanguageTag key="colon"/></td>
				        <td>${tokenInfo.tokenSpec.otplen}</td>
				        <td  align="right"><view:LanguageTag key='tkn_change_cycle'/><view:LanguageTag key="colon"/></td>
				        <td>${tokenInfo.tokenSpec.intervaltime}</td>
				      </tr>
				      <tr>
				        <td align="right"><view:LanguageTag key='tkn_auth_base'/><view:LanguageTag key="colon"/></td>
				        <td>${tokenInfo.authnum} / ${tokenInfo.crauthnum}</td>
				        <td align="right" ><view:LanguageTag key='tkn_drift_views'/><view:LanguageTag key="colon"/></td>
				        <td>${tokenInfo.driftcount}</td>
				      </tr>
				      <tr>
				        <td align="right"><view:LanguageTag key="tkn_auth_method"/><view:LanguageTag key="colon"/></td>
				        <td>
				        	<c:if test="${tokenInfo.authmethod eq 0}"><view:LanguageTag key="tkn_dynamic_pwd_auth"/></c:if>
				        	<c:if test="${tokenInfo.authmethod eq 1}"><view:LanguageTag key="tkn_emerg_pwd_auth"/></c:if>
				        </td>
				        <td align="right"><view:LanguageTag key="tkn_emerg_pwd_exp_time"/><view:LanguageTag key="colon"/></td>
				        <td>${tokenInfo.empindeathStr}</td>
				      </tr>
				      <tr>
				      	<td align="right"><view:LanguageTag key="tkn_tkn_exp_time"/><view:LanguageTag key="colon"/></td>
				        <td>${tokenInfo.expiretimeView}</td>
				        <td  align="right"><view:LanguageTag key="tkn_tkn_spec"/><view:LanguageTag key="colon"/></td>
				        <td>${tokenInfo.specid}</td>
				      </tr>
				       <tr>
				        <td  align="right"><view:LanguageTag key="tkn_temp_lock_err_num"/><view:LanguageTag key="colon"/></td>
				        <td>${tokenInfo.temploginerrcnt}</td>
				        <td  align="right"><view:LanguageTag key="tkn_perm_lock_err_num"/><view:LanguageTag key="colon"/></td>
				        <td>${tokenInfo.longloginerrcnt}</td>
				      </tr>
				     <!-- <tr>
				        <td  align="right"><view:LanguageTag key='tkn_sync_win'/><view:LanguageTag key="colon"/></td>
				        <td>${tokenInfo.syncwnd}</td>
				        <td align="right" ><view:LanguageTag key='tkn_alg_ident'/><view:LanguageTag key="colon"/></td>
				        <td>${tokenInfo.tokenSpec.algid}</td>
				        <td align="right" ></td>
				        <td></td>
				      </tr>  -->
				   </table>
		 	</li>
			<!-- 令牌状态 -->
			<li>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
		       <tr>
		        <td width="25%" align="right"><view:LanguageTag key='tkn_comm_enable'/><view:LanguageTag key="colon"/></td>
		        <td  width="75%" id="enableId"> 
		           <c:if test="${tokenInfo.enabled==0}"><view:LanguageTag key="common_syntax_no"/></c:if>
		           <c:if test="${tokenInfo.enabled==1}"><view:LanguageTag key="common_syntax_yes"/></c:if>
		        </td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key='tkn_comm_active'/><view:LanguageTag key="colon"/></td>
		        <td id="pubkeyState"> 
		           <c:if test="${tokenInfo.pubkeystate==-1}"><view:LanguageTag key="tkn_has_activation_notstate"/></c:if>
		           <c:if test="${tokenInfo.pubkeystate==0}"><view:LanguageTag key="tkn_not_activation"/></c:if>
		           <c:if test="${tokenInfo.pubkeystate==1}"><view:LanguageTag key="tkn_has_activation_not_auth"/></c:if>
		           <c:if test="${tokenInfo.pubkeystate==2}"><view:LanguageTag key="tkn_has_activation"/></c:if>
		        </td>
	     	  </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key='tkn_comm_lock'/><view:LanguageTag key="colon"/></td>
		        <td id="lockedId">  
		          <c:if test="${tokenInfo.locked==0}"><view:LanguageTag key="lock_state_no_locking"/></c:if>
		          <c:if test="${tokenInfo.locked==1}"><view:LanguageTag key="lock_state_temp"/>(<view:LanguageTag key='tkn_lock_time'/><view:LanguageTag key="colon"/>${tokenInfo.loginlocktimeStr})</c:if>
		          <c:if test="${tokenInfo.locked==2}"><view:LanguageTag key="lock_state_perm"/>(<view:LanguageTag key='tkn_lock_time'/><view:LanguageTag key="colon"/>${tokenInfo.loginlocktimeStr})</c:if>
		        </td>
		      </tr>
		      <tr>
		        <td align="right"><view:LanguageTag key='tkn_comm_lose'/><view:LanguageTag key="colon"/></td>
		        <td id="lostId">
		          <c:if test="${tokenInfo.lost==0}"><view:LanguageTag key="common_syntax_no"/></c:if> 
		          <c:if test="${tokenInfo.lost==1}"><view:LanguageTag key="common_syntax_yes"/></c:if> 
		        </td>
		      </tr>
		       <tr>
		        <td align="right"><view:LanguageTag key='tkn_comm_invalid'/><view:LanguageTag key="colon"/></td>
		        <td id="logoutId">
		          <c:if test="${tokenInfo.logout==0}"><view:LanguageTag key="common_syntax_no"/></c:if> 
		          <c:if test="${tokenInfo.logout==1}"><view:LanguageTag key="common_syntax_yes"/></c:if> 
		        </td>
		      </tr>   
		     </table>
	   		</li>
			 <li><table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
	             <!-- 绑定管理员  -->
	             <tr>
	               <td width="25%" align="right" valign="top"><view:LanguageTag key='admin_info'/><view:LanguageTag key="colon"/></td>
	               <td width="75%" id="adminTd">
	                <c:if test="${empty tokenInfo.userIds}"><view:LanguageTag key='common_syntax_nothing'/></c:if>
					<c:forEach items="${tokenInfo.userIds}" var="u">
	               		<c:if test="${u.domainId == null}">
		               		<a href="javascript:viewUser('${u.userId}','${u.domainId }')">${u.userId}</a><br>
					    </c:if>
	               	</c:forEach>
				   </td>
	             </tr>
	             <!-- 绑定用户  -->
	             <tr>
	               <td width="25%" align="right" valign="top"><view:LanguageTag key='tkn_dist_info_user'/><view:LanguageTag key="colon"/></td>
	               <td width="75%" id="userTd">
	                <c:if test="${empty tokenInfo.userIds}"><view:LanguageTag key='common_syntax_nothing'/></c:if>
					<c:forEach items="${tokenInfo.userIds}" var="u">
					    <c:if test="${u.domainId != null}">
		               		<a href="javascript:viewUser('${u.userId}','${u.domainId }')">${u.userId}</a><br>
					    </c:if>
	               	</c:forEach>
				   </td>
	             </tr>
	                     
	           </table>
			</li>
		  </ul>
		</td>
	  </tr>
	</table>
    </form>
  </body>
</html>