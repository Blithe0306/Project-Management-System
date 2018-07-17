<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>

<%
	String path = request.getContextPath();
	// 点击域信息中用户数传过来的组织机构ID，格式：domainid:orgunitid,
	String dOrgunitId = request.getParameter("dOrgunitId");
	if(dOrgunitId == null){
		dOrgunitId = "";
	}
	
	// 点击域信息中令牌数传过来的组织机构名称
	String dOrgunitName = request.getParameter("DOrgunitName");
	if(dOrgunitName == null){
		dOrgunitName = "";
	}
	// 点击域信息中令牌数传过来的参数，决定拼接哪些SQL
	String orgFlag = request.getParameter("flag");
%>
<html>

  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
 	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>	
    <script type="text/javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
    <script src="<%=path%>/manager/common/js/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script language="javascript"  src="<%=path%>/manager/user/userinfo/js/list.js"></script>	
	<script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script>  
	
	<script language="javascript" type="text/javascript">
	
	// Start,多语言信息
	var tempLock = '<view:LanguageTag key="lock_state_temp"/>';
	var	longLock = '<view:LanguageTag key="lock_state_perm"/>'; 
	var	noLock = '<view:LanguageTag key="lock_state_no_locking"/>';
	var	commonYes = '<view:LanguageTag key="common_syntax_no"/>';
	var	commonNo = '<view:LanguageTag key="common_syntax_yes"/>';
	var lock = '<view:LanguageTag key="user_account_lock"/>'; 
	var unlock = '<view:LanguageTag key="user_account_unlock"/>';
	var enable = '<view:LanguageTag key="common_syntax_enable"/>'; 
	var disabled = '<view:LanguageTag key="common_syntax_disabled"/>';
	
	// 列表
	var account_lang = '<view:LanguageTag key="user_info_account"/>';
	var orgunitName_lang = '<view:LanguageTag key="domain_orgunit"/>'; 
	var realname_lang = '<view:LanguageTag key="user_info_real_name"/>'; 
	var tokens_lang = '<view:LanguageTag key="tkn_comm_tknum"/>'; 
	var locked_lang = '<view:LanguageTag key="user_account_lock_state"/>';
	var enable_lang = '<view:LanguageTag key="tkn_comm_enable"/>';
	var operation_lang = '<view:LanguageTag key="common_syntax_operation"/>'; 	
	
	// 操作
	var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
	var change_orgunit_lang = '<view:LanguageTag key="user_change_orgunit"/>';
	var edit_lang = '<view:LanguageTag key="user_info_edit"/>';
	var user_lang = '<view:LanguageTag key="user_info"/>';
	var group_lang = '<view:LanguageTag key="user_group_info"/>';
	var tkn_lang = '<view:LanguageTag key="user_tkn_info"/>';
	var static_pw_lang = '<view:LanguageTag key="user_set_static_pwd"/>';
	var syntax_lang = '<view:LanguageTag key="common_syntax_tip"/>';
	var lock_account_lang = '<view:LanguageTag key="user_sure_lock_account"/>';
	var unlock_account_lang = '<view:LanguageTag key="user_sure_unlock_account"/>';
	var syntax_confirm_lang = '<view:LanguageTag key="common_syntax_confirm"/>';
	var whether_disabled_lang = '<view:LanguageTag key="user_whether_disabled"/>';
	var whether_enable_lang = '<view:LanguageTag key="user_whether_enable"/>';
	var replace_tkn_lang = '<view:LanguageTag key="user_replace_tkn"/>';
	var bind_tkn_lang = '<view:LanguageTag key="user_bind_tkn"/>';
	var unbind_user_lang = '<view:LanguageTag key="user_sure_unbind_user_tkn"/>';
	var unbind_tkn_lang = '<view:LanguageTag key="user_unbind_tkn"/>';
	var tkn_auth_lang = '<view:LanguageTag key="user_tkn_auth"/>';
	var tkn_no_sync_lang = '<view:LanguageTag key="user_bind_tkn_no_sync"/>';
	var synch_title_lang = '<view:LanguageTag key="tkn_synch_title"/>';
	var oper_state_lang = '<view:LanguageTag key="user_sel_oper_state"/>';
	var sel_user_lang = '<view:LanguageTag key="user_sel_user"/>';
	var oper_user_lang = '<view:LanguageTag key="user_no_oper_user"/>';
	var rad_conf_lang = '<view:LanguageTag key="user_sel_rad_conf"/>';
	var back_endauth_lang = '<view:LanguageTag key="user_sel_back_endauth"/>';
	var sel_localauth_lang = '<view:LanguageTag key="user_sel_localauth"/>';
	var a_user_lang = '<view:LanguageTag key="user_a_user"/>';
	var batch_del_lang = '<view:LanguageTag key="user_sure_batch_del"/>';
	var batch_unbind_lang = '<view:LanguageTag key="user_sure_batch_unbind"/>';
	var batch_add_lang = '<view:LanguageTag key="user_sure_batch_add_rad"/>';
	var batch_lock_lang = '<view:LanguageTag key="user_sure_batch_lock"/>';
	var batch_unlock_lang = '<view:LanguageTag key="user_sure_batch_unlock"/>';
	var batch_disabled_lang = '<view:LanguageTag key="user_sure_batch_disabled"/>';
	var batch_enable_lang = '<view:LanguageTag key="user_sure_batch_enable"/>';
	var batch_backend_lang = '<view:LanguageTag key="user_sure_batch_backend"/>';
	var batch_localauth_lang = '<view:LanguageTag key="user_sure_batch_localauth"/>';
	// End,多语言信息

	var permEdit;
	var permStaticPass;//设置静态密码
	var permLocked; //锁定
	var permBindToken;//绑定
	var permUnBindToken;//解绑
	var permChangeToken;//更换
	var permAuth;//认证
	var permSync;//同步
	var permEnabled; //启用
	var permChangeOrgunit; //变更组织机构
	
	$(document).ready(function() {
	   	$("#oper option:empty").remove();
	   	permEdit = '<view:AdminPermTag key="020102" path="<%=path%>" langKey="common_syntax_edit" type="1" />';
	   	permStaticPass = '<view:AdminPermTag key="020107" path="<%=path%>" langKey="user_set_static_pwd" type="1" />';
	   	permBindToken = '<view:AdminPermTag key="020104" path="<%=path%>" langKey="user_bind_tkn" type="1" />';
	   	permChangeToken = '<view:AdminPermTag key="020106" path="<%=path%>" langKey="user_replace_tkn" type="1"/>';
	   	permUnBindToken = '<view:AdminPermTag key="020105" path="<%=path%>" langKey="user_unbind_tkn" type="1" />'; 
	   	permAuth = '<view:AdminPermTag key="030106" path="<%=path%>" langKey="user_tkn_auth" type="1" />';
	   	permSync = '<view:AdminPermTag key="030107" path="<%=path%>" langKey="tkn_synch_title" type="1" />';
		permChangeOrgunit='<view:AdminPermTag key="020111" path="<%=path%>" langKey="user_change_orgunit" type="1" />';
		permEnabled = '';
	   	permLocked = '<view:AdminPermTag key="020108" path="<%=path%>" langKey="user_account_lock" type="1" />';

	 	// 组织查看过来的mode=2, 快速查询过来mode=3, 编辑完成或其它跳转到列表mode=4;
		if($("#mode").val()=='2' || $("#mode").val()=='3' || $("#mode").val()=='4'){
			pageFocus(1);
		}else{// 如果是点击令牌列表过来的
		   	pageFocus(0);
		}
	});

	// 重查操作
	function reQuery(){
	   	// 如果是点击令牌列表过来的
	   	pageFocus(0);
	}
	</script>
  </head>
  
  <body>
  <jsp:include page="/manager/user/userinfo/common.jsp" />
  <div id="background"  class="background"  style="display: none; "></div>
  <div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
  <input id="contextPath" type="hidden" value="<%=path%>" />
  <input id="currentPage" type="hidden" type="text" value="${param.currentPage}" />
  <!-- 首页快速查询 的查询条件 -->
  <input name="queryText" type="hidden" id="queryText"  value="${param.queryText}"/>
  <input type="hidden" id="mode" name="mode" value="${param.mode}"/>
  <form name="ListForm" id="ListForm" method="post" action="">
  	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:6px">
   		<tr>
    		<td>
     		<ul id="menu">
      			<li class="tabFocus"><img src="<%=path%>/images/icon/zoom.png" width="16" height="16" hspace="4" align="absmiddle"/><view:LanguageTag key="user_userquery"/></li>
      			<li><img src="<%=path%>/images/icon/user.png" width="16" height="16" hspace="4" align="absmiddle"/><view:LanguageTag key="common_menu_user_list"/></li>
	  			<li style="float:right"><a href="javascript:addAdmPermCode('0200','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a></li>
     		</ul>
     		<ul id="content">
     	<li class="conFocus">
	     <!-- 用户查询条件页 begin -->
			 <table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:6px">
			 	<tr>
			   		<td width="5%">&nbsp;</td>
			   	 	<td width="23%" align="right"><view:LanguageTag key="domain_orgunit"/><view:LanguageTag key="colon"/></td>
		         	<td width="20%">
		            	<input type="hidden" id="orgunitIds" name="queryForm.dOrgunitId" value="<%=dOrgunitId%>" />
          				<input type="hidden" id="orgFlag" name="queryForm.orgFlag" value="<%=orgFlag%>" />
          				<input id="orgunitNames" name="orgunitNames" value="<%=dOrgunitName%>" onClick="selOrgunits(7,'<%=path%>');" readonly class="formCss100" style="width:220px" />
		         	</td>
		         	<td width="23%" align="right"><view:LanguageTag key="user_info_account"/><view:LanguageTag key="colon"/></td>
		         	<td width="20%">
		         		<input type="text" id="userId" name="queryForm.userId"  value="${queryForm.userId}" class="formCss100" style="width:220px"/>
		        	</td>
		        	<td width="9%">&nbsp;</td>
			   </tr>
			   
		       <tr>
		       	   <td>&nbsp;</td>
		           <td align="right"><view:LanguageTag key="tkn_comm_tknum"/><view:LanguageTag key="colon"/></td>
		           <td><input type="text" id="token" name="queryForm.token" value="${queryForm.token}" class="formCss100" style="width:220px"/></td>
		           <td align="right"><view:LanguageTag key="user_info_real_name"/><view:LanguageTag key="colon"/></td>
		           <td>
			       	  <input type="text" id="realName" name="queryForm.realName" value="${queryForm.realName}" class="formCss100" style="width:220px"/>
		           </td>
		           <td>
		           	  <input type="hidden" id="seturl"  value="" />
				      <input type="hidden" id="userIds"  value="" />
				      <input type="hidden" id="radprofileid"  value="" />
				      <input type="hidden" id="backendAuth"  value="" />
				      <input type="hidden" id="localAuth"  value="" />
				      <input type="hidden" id="pwd"  value="" />
				      <input type="hidden" id="bindToneks"  value="" />
		  		   </td>
		       </tr>
		       <tr>
		        <td>&nbsp;</td>
		        <td align="right"><view:LanguageTag key="common_syntax_enable_mode"/><view:LanguageTag key="colon"/></td>
		        <td>
		           <select id="enabled" name="queryForm.enabled" value="${queryForm.enabled}" class="select100" style="width:220px">
			        	<option value="-1" <c:if test='${queryForm.enabled == -1}'> selected</c:if>>
			        		<view:LanguageTag key="common_syntax_whole"/>
			          	</option>
			          	<option value="1" <c:if test='${queryForm.enabled == 1}'> selected</c:if>>
			          		<view:LanguageTag key="tkn_state_enabled"/>
			          	</option>
			          	<option value="0" <c:if test='${queryForm.enabled == 0}'> selected</c:if>>
			          		<view:LanguageTag key="tkn_state_unenabled"/>
			          	</option>
		        	</select>
		        </td>
		        <td align="right"><view:LanguageTag key="tkn_comm_state_lock"/><view:LanguageTag key="colon"/></td>
		        <td>
		           <select id="locked" name="queryForm.locked" value="${queryForm.locked}" class="select100" style="width:220px">
			            <option value="-1" <c:if test='${queryForm.locked == -1}'> selected</c:if>>
			            	<view:LanguageTag key="common_syntax_whole"/>
			            </option>
			            <option value="2" <c:if test='${queryForm.locked == 2}'> selected</c:if>>
			            	<view:LanguageTag key="tkn_state_locked"/>
			            </option>
			            <option value="0" <c:if test='${queryForm.locked == 0}'> selected</c:if>>
			            	<view:LanguageTag key="tkn_state_unlocked"/>
			            </option>
		            </select>        
		        </td>
		        <td>&nbsp;</td>
		       </tr>
		       <tr>
		        <td>&nbsp;</td>
		        <td align="right"><view:LanguageTag key="tkn_comm_state_bind"/><view:LanguageTag key="colon"/></td>
		          <td>
		              <select id="bindState" name="queryForm.bindState" value="${queryForm.bindState}" class="select100" style="width:220px">
				      	  <option value="0" <c:if test='${queryForm.bindState == 0}'> selected</c:if>>
				              <view:LanguageTag key="common_syntax_whole"/>
				          </option>
				          <option value="1" <c:if test='${queryForm.bindState == 1}'> selected</c:if>>
				              <view:LanguageTag key="tkn_state_unbound"/>
				          </option>
				          <option value="2" <c:if test='${queryForm.bindState == 2}'> selected</c:if>>
				              <view:LanguageTag key="tkn_state_bound"/>
				          </option>
			          </select>
		        </td>
		        <td align="right"><view:LanguageTag key="user_local_auth_mode"/><view:LanguageTag key="colon"/></td>
		        <td>
		           <select id="localAuthStr" name="queryForm." value="${queryForm.localAuth}" class="select100" style="width:220px">
			        	<option value="-1" <c:if test='${queryForm.localAuth == -1}'> selected</c:if>>
			        		<view:LanguageTag key="common_syntax_whole"/>
			          	</option>
			          	<option value="0" <c:if test='${queryForm.localAuth == 0}'> selected</c:if>>
			          		<view:LanguageTag key="local_auth_only_vd_tkn"/>
			          	</option>
			          	<option value="2" <c:if test='${queryForm.localAuth == 2}'> selected</c:if>>
			          		<view:LanguageTag key="local_auth_only_vd_pwd"/>
			          	</option>
			          	<option value="1" <c:if test='${queryForm.localAuth == 1}'> selected</c:if>>
			          		<view:LanguageTag key="local_auth_vd_pwd_tkn"/>
			          	</option>
		        	</select>
				</td>
				<td>&nbsp;</td>
		       </tr>
		       <tr>
		          <td>&nbsp;</td>
		          <td align="right"><view:LanguageTag key="user_whether_backend_auth"/><view:LanguageTag key="colon"/></td>
		       	  <td>
		          <select id="backEndAuthStr" name="queryForm.backEndAuth" value="${queryForm.backEndAuth}" class="select100" style="width:220px">
						<option value="-1" <c:if test='${queryForm.backEndAuth == -1}'> selected</c:if>>
			        		<view:LanguageTag key="common_syntax_whole"/>
			          	</option>
						<option value="0" <c:if test='${queryForm.backEndAuth == 0}'> selected</c:if>>
			        		<view:LanguageTag key="backend_auth_default"/>
			          	</option>
						<option value="1" <c:if test='${queryForm.backEndAuth == 1}'> selected</c:if>>
			        		<view:LanguageTag key="backend_auth_need"/>
			          	</option>
			          	<option value="2" <c:if test='${queryForm.backEndAuth == 2}'> selected</c:if>>
			        		<view:LanguageTag key="backend_auth_no_need"/>
			          	</option>
			        </select>
		          </td>
		          <td>&nbsp;</td>
		          <td>&nbsp;</td>
		          <td>&nbsp;</td>
		       </tr>
		       <tr>
			   	  <td>&nbsp;</td>
			      <td>&nbsp;</td>
			      <td>&nbsp;</td>
			      <td>&nbsp;</td>
			      <td>&nbsp;</td>
			      <td>&nbsp;</td>
			  </tr>
			   <tr>
			   	  <td></td>
			      <td></td>
			      <td></td>
			      <td height="40"><span style="display:inline-block;" class="query-button-css"><a href="javascript:query(false,true);" id="query" class="isLink_LanSe"><view:LanguageTag key="common_syntax_query"/></a></span></td>
			      <td></td>
			      <td></td>
			  </tr>
		   </table>
     <!-- 用户查询条件页 end -->
     </li>
     
     <li>
     	<!-- 用户列表页 begin -->
		 <table width="100%" border="0" cellspacing="0" cellpadding="0">
	     	<tr>
	        	<td>
	        		<table width="800" border="0" cellspacing="0" cellpadding="0">
	             		<tr>
	               			<td width="101" align="right"><view:LanguageTag key="common_syntax_operate"/><view:LanguageTag key="colon"/></td>
	              	 		<td width="155">
	               				<select id="operObj" name="operObj" class="select100_2">
	                				<option value="0"><view:LanguageTag key="common_syntax_this_page_date"/></option>
	                				<option value="1"><view:LanguageTag key="common_syntax_this_query_date"/></option>
	              				</select>
	               			</td>
	               			<td width="102" align="right"><view:LanguageTag key="common_syntax_operation"/><view:LanguageTag key="colon"/></td>
	               			<td width="155">
	            				<select id="oper" name="oper" class="select100_2">
		                			<option value="-1">--<view:LanguageTag key="common_syntax_please_sel"/>--</option>
								    <option value="3"><view:AdminPermTag key="020108" path="<%=path%>" langKey="tkn_comm_lock"  type="0" /></option>
									<option value="4"><view:AdminPermTag key="020108" path="<%=path%>" langKey="tkn_comm_unlock" type="0" /></option>
									<option value="5"><view:AdminPermTag key="020109" path="<%=path%>" langKey="common_syntax_disabled"  type="0" /></option>
									<option value="6"><view:AdminPermTag key="020109" path="<%=path%>" langKey="common_syntax_enable" type="0" /></option>
									<option value="0"><view:AdminPermTag key="020103" path="<%=path%>" langKey="user_common_delete" type="0" /></option>
									<option value="1"><view:AdminPermTag key="020105" path="<%=path%>" langKey="user_unbundling" type="0" /></option>
									<option value="2"><view:AdminPermTag key="020110" path="<%=path%>" langKey="user_set_radius" type="0" /></option>
									<option value="7"><view:AdminPermTag key="020112" path="<%=path%>" langKey="adm_perm_020112" type="0" /></option>
									<option value="8"><view:AdminPermTag key="020113" path="<%=path%>" langKey="adm_perm_020113" type="0" /></option>
				              	</select>
	               			</td>
	               			<td width="20">&nbsp;</td>
	               			<td width="200">
	                  			<a href="#" style="display:inline-block;float:left;" onClick="batchOper();" class="button"><span><view:LanguageTag key="common_syntax_execute"/></span></a>
	                  			<a href="#" style="display:inline-block;float:left;" onClick="javascript:reQuery();" class="button"><span><view:LanguageTag key="tkn_query_4"/></span></a>
	               			</td>
	               			<td width="67" align="right"></td>
	            		</tr>
	          		</table>
	         	</td>
	      	</tr>
	    </table>
    	<!-- 用户列表页 end -->
    </li>
	</td>
  </tr>
</table>
</form>
<div id="listDataAJAX"></div>
</body>
</html>