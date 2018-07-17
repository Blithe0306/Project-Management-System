<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.ft.otp.util.conf.ConfDataFormat"%>
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
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
	<script type="text/javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=path%>/manager/admin/user/js/list.js"></script>
	<script language="javascript" type="text/javascript">
		
		// Start,多语言提取
		var langLocktemp = '<view:LanguageTag key="lock_state_temp"/>';
		var langLockperm = '<view:LanguageTag key="lock_state_perm"/>';
		var langNolock = '<view:LanguageTag key="lock_state_no_locking"/>';
		var langNoactive = '<view:LanguageTag key="common_syntax_activate_no"/>';
		var langNo = '<view:LanguageTag key="common_syntax_no"/>';
		var langYes = '<view:LanguageTag key="common_syntax_yes"/>';
		var langEnable = '<view:LanguageTag key="common_syntax_enable"/>';
		var langDisabled = '<view:LanguageTag key="common_syntax_disabled"/>';
		var langALock = '<view:LanguageTag key="admin_info_lock"/>';
		var langAUnlock = '<view:LanguageTag key="admin_info_unlock"/>';
		var langABindtkn = '<view:LanguageTag key="admin_bind_tkn"/>'; 
		var langAUnbindtkn = '<view:LanguageTag key="admin_unbind_tkn"/>';
		var langAReptkn = '<view:LanguageTag key="admin_replace_tkn"/>'; 
		
		// 列表
		var account_lang = '<view:LanguageTag key="admin_info_account"/>';
		var realname_lang = '<view:LanguageTag key="common_info_realname"/>';
		var domain_org_lang = '<view:LanguageTag key="admin_manag_domain_org"/>';
		var tknum_lang = '<view:LanguageTag key="tkn_comm_tknum"/>';
		var lock_state_lang = '<view:LanguageTag key="admin_info_lock_state"/>';
		var enable_lang = '<view:LanguageTag key="common_syntax_enable"/>';
		var creator_lang = '<view:LanguageTag key="common_info_creator"/>';
		var create_time_lang = '<view:LanguageTag key="common_syntax_create_time"/>';
		var operation_lang = '<view:LanguageTag key="common_syntax_operation"/>';
		
		// 操作
		var info_lang = '<view:LanguageTag key="user_info"/>';
		var group_info_lang = '<view:LanguageTag key="user_group_info"/>';
		var tkn_info_lang = '<view:LanguageTag key="admin_tkn_info"/>';
		var pass_noreset_lang = '<view:LanguageTag key="admin_noact_pass_noreset"/>';
		var noenab_pass_noreset_lang = '<view:LanguageTag key="admin_noenab_pass_noreset"/>';
		var pass_reset_lang = '<view:LanguageTag key="admin_pass_reset"/>';
		var confirm_lang = '<view:LanguageTag key="admin_confirm"/>';
		var unbind_tkn_lang = '<view:LanguageTag key="admin_sure_unbind_tkn"/>';
		var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
		var lifting_tkn_bind_lang = '<view:LanguageTag key="admin_lifting_tkn_bind"/>';
		var tkn_auth_lang = '<view:LanguageTag key="admin_tkn_auth"/>';
		var syntax_detail_lang = '<view:LanguageTag key="common_syntax_detail_info"/>';
		var change_pwd_lang = '<view:LanguageTag key="common_syntax_change_pwd"/>';
		var del_user_lang = '<view:LanguageTag key="admin_sel_del_user"/>';
		var has_child_lang = '<view:LanguageTag key="admin_sel_user_has_child"/>';
		var syntax_confirm_lang = '<view:LanguageTag key="common_syntax_confirm"/>';
		var other_create_lang = '<view:LanguageTag key="admin_not_del_other_create"/>';
		var edit_child_lang = '<view:LanguageTag key="admin_curr_only_edit_child"/>';
		var oper_myself_lang = '<view:LanguageTag key="admin_not_oper_myself"/>';
		var whether_disabled_lang = '<view:LanguageTag key="admin_whether_disabled"/>';
		var whether_enable_lang = '<view:LanguageTag key="admin_whether_enable"/>';
		var lock_myself_lang = '<view:LanguageTag key="admin_not_lock_myself"/>';
		var sure_lock_lang = '<view:LanguageTag key="admin_sure_lock"/>';
		var sure_unlock_lang = '<view:LanguageTag key="admin_sure_unlock"/>';
		var change_user_lang = '<view:LanguageTag key="admin_sel_change_user"/>';
		var myself_creater_lang = '<view:LanguageTag key="admin_not_change_myself_creater"/>';
		var myself_create_lang = '<view:LanguageTag key="admin_has_no_myself_create"/>';
		var change_creator_lang = '<view:LanguageTag key="admin_change_creator"/>';
		var syntax_modify_lang = '<view:LanguageTag key="common_syntax_modify"/>';
		var syntax_close_lang = '<view:LanguageTag key="common_syntax_close"/>';
		// End,多语言提取
	
		//权限
		var permAdd = ''; //添加
		var permEdit = ''; //编辑
		var permEnable = ''; //启用、禁用
		var permEditPwd = ''; //修改密码
		var permResetPwd = '';//重置密码  和修改 密码权限码一致
		var permLock = '';//锁定、解锁
		var permBind = '';//绑定令牌
		var permUnbind = '';//解绑令牌
		var permChange = '';//更换令牌
		var isActivate = <%=isActivate%>;// 是否激活邮件
		$(document).ready(function() {
			permAdd = '<view:AdminPermTag key="010101" path="<%=path%>" langKey="admin_info_add" type="1" />';
			permEdit = '<view:AdminPermTag key="010102" path="<%=path%>" langKey="common_syntax_edit" type="1" />';
			permEnable = '<view:AdminPermTag key="010105" path="<%=path%>" langKey="common_syntax_enable" type="1" />';
			permLock = '<view:AdminPermTag key="010106" path="<%=path%>" langKey="admin_info_lock" type="1" />';
			permEditPwd = '<view:AdminPermTag key="010107" path="<%=path%>" langKey="admin_reset_pwd" type="1" />';
			permResetPwd = '<view:AdminPermTag key="010107" path="<%=path%>" langKey="admin_reset_pwd" type="1" />';
			permBind = '<view:AdminPermTag key="010108" path="<%=path%>" langKey="admin_bind_tkn" type="1" />';
			permUnbind = '<view:AdminPermTag key="010109" path="<%=path%>" langKey="admin_unbind_tkn" type="1" />';
			permChange = '<view:AdminPermTag key="010110" path="<%=path%>" langKey="admin_replace_tkn" type="1" />';
		});
	</script>
  </head>
  <body scroll="no" style="overflow:hidden">
  <jsp:include page="/manager/user/userinfo/common.jsp" />
  <input id="contextPath" 	type="hidden"   value="<%=path%>" />
  <input id="l_userid"    	type="hidden" 	value="${curLoginUser}" />
  <input id="l_userid_role" type="hidden" 	value="${curLoginUserRole}" />
  <input id="curPage"     	type="hidden"   value="${param.cPage}" />

  <div id="msgShow" class="msgDiv"><span class="msg"></span></div>
  <form name="ListForm" id="ListForm" method="post" action="">
  	<table width="800" border="0" cellspacing="0" cellpadding="0" style="margin-top:6px">
      <tr>
        <td width="116" align="right"><view:LanguageTag key="admin_info_account"/><view:LanguageTag key="colon"/></td>
        <td width="168"><input type="text" id="adminid" name="queryForm.adminid" value="${queryForm.adminid}" class="formCss100"/></td>
        <td width="133" align="right"><view:LanguageTag key="admin_role"/><view:LanguageTag key="colon"/></td>
        <td width="168"><input type="text" id="roleName" name="queryForm.roleName" value="${queryForm.roleName}" class="formCss100"/></td>
        <td width="15">&nbsp;</td>
        <td width="200">&nbsp;</td>
      </tr>
      <tr>
        <td width="116" align="right"><view:LanguageTag key="user_info_real_name"/><view:LanguageTag key="colon"/></td>
        <td width="168"><input type="text" id="realname" name="queryForm.realname" value="${queryForm.realname}" class="formCss100" /></td>
        <td width="133" align="right"><view:LanguageTag key="common_info_creator"/><view:LanguageTag key="colon"/></td>
        <td width="168">
        	<input type="text" id="createuser" name="queryForm.createuser" value="${queryForm.createuser}" class="formCss100"/>
        </td>
        <td width="15">&nbsp;</td>
        <td width="200">&nbsp;</td>
      </tr>
      <tr>
        <td width="116" align="right"><view:LanguageTag key="tkn_comm_state_lock"/><view:LanguageTag key="colon"/></td>
        <td width="168">
        	<select id="locked" name="queryForm.locked" value="${queryForm.locked}" class="select100" style="width:168px">
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
        <td width="133" align="right"></td>
        <td width="168">
        	<span style="display:inline-block" class="query-button-css"><a href="javascript:query();" class="isLink_LanSe"><view:LanguageTag key="common_syntax_query"/></a></span>
		</td>
        <td width="15">&nbsp;</td>
        <td width="200">
        	</td>
      </tr>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="operTableBg">
      <tr>
        <td>
        	<span style="float:left">&nbsp;&nbsp;</span>
		    <view:AdminPermTag key="010103" path="<%=path%>" langKey="common_syntax_delete" type="2" />
		    <view:AdminPermTag key="010104" path="<%=path%>" langKey="admin_change_creator" type="2" />
        </td>
      </tr>
    </table>     
  </form>
 <div id="listDataAJAX"></div>
  </body>
</html>