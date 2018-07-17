<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
		
	// 点击域信息中令牌数传过来的组织机构ID，格式：domainid:orgunitid,
	String dOrgunitId = request.getParameter("dOrgunitId");
	if(dOrgunitId == null){
		dOrgunitId = "";
	}
	
	// 点击域信息中令牌数传过来的组织机构名称
	String dOrgunitName = request.getParameter("DOrgunitName");
	if(dOrgunitName == null){
		dOrgunitName = "";
	}
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
<script language="javascript" src="<%=path%>/manager/common/js/checkall.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
<script type="text/javascript" src="<%=path%>/manager/token/js/list.js"></script>
<script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script>
<script language="javascript" type="text/javascript">

	// Start,多语言提取
	var langEnable = '<view:LanguageTag key="tkn_comm_enable"/>';
	var langDisable = '<view:LanguageTag key="tkn_comm_disable"/>';
	var langLose = '<view:LanguageTag key="tkn_comm_lose"/>';
	var langRelieveLose = '<view:LanguageTag key="tkn_comm_relieve_lose"/>';
	var tempLock = '<view:LanguageTag key="lock_state_temp"/>';
	var	longLock = '<view:LanguageTag key="lock_state_perm"/>';
	var	noLock = '<view:LanguageTag key="lock_state_no_locking"/>';
	var langLock = '<view:LanguageTag key="tkn_comm_lock"/>';
	var langUnlock = '<view:LanguageTag key="tkn_comm_unlock"/>';
	var notstate = '<view:LanguageTag key="tkn_has_activation_notstate"/>';
	var noactivate = '<view:LanguageTag key="tkn_not_activation"/>';
	var notauth = '<view:LanguageTag key="tkn_has_activation_not_auth"/>';
	var hasActive = '<view:LanguageTag key="tkn_has_activation"/>';
	var langYes = '<view:LanguageTag key="common_syntax_yes"/>';
	var langNo = '<view:LanguageTag key="common_syntax_no"/>';
	var physicalHard = '<view:LanguageTag key="tkn_physical_hard"/>';
	var physicalSoft = '<view:LanguageTag key="tkn_physical_soft"/>';
	var physicalMobile = '<view:LanguageTag key="tkn_physical_mobile"/>';
	var physicalSms = '<view:LanguageTag key="tkn_physical_sms"/>';
	var commUnvalid = '<view:LanguageTag key="tkn_comm_invalid"/>';
	var commDelete = '<view:LanguageTag key="tkn_comm_delete"/>';
	
	//列表
	var tknum_lang = '<view:LanguageTag key="tkn_comm_tknum"/>';
	var orgunit_lang = '<view:LanguageTag key="tkn_orgunit"/>';
	var physical_type_lang = '<view:LanguageTag key="tkn_comm_physical_type"/>';
	var update_key_lang = '<view:LanguageTag key="tkn_comm_active_update_key"/>';
	var bind_lang = '<view:LanguageTag key="tkn_comm_bind"/>';
	var operate_lang = '<view:LanguageTag key="tkn_operate"/>';
	
	// 操作
	var code_title_lang = '<view:LanguageTag key="tkn_ac_code_title"/>';
	var colon_lang = '<view:LanguageTag key="colon"/>';
	var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
	var puk1_title_lang = '<view:LanguageTag key="tkn_puk1_title"/>';
	var puk2_title_lang = '<view:LanguageTag key="tkn_puk2_title"/>';
	var puk1_code_title_lang = '<view:LanguageTag key="tkn_puk1_code_title"/>';
	var puk2_code_title_lang = '<view:LanguageTag key="tkn_puk2_code_title"/>';
	var tkn_allot_lang = '<view:LanguageTag key="tkn_allot"/>';
	var syntax_detail_info_lang = '<view:LanguageTag key="common_syntax_detail_info"/>';
	var emerg_pwd_lang = '<view:LanguageTag key="tkn_set_emerg_pwd"/>';
	var auth_title_lang = '<view:LanguageTag key="tkn_auth_title"/>';
	var synch_title_lang = '<view:LanguageTag key="tkn_synch_title"/>';
	var confirm_info_1_lang = '<view:LanguageTag key="tkn_confirm_info_1"/>';
	var dist_soft_lang = '<view:LanguageTag key="tkn_dist_soft"/>';
	var operation_state_lang = '<view:LanguageTag key="tkn_vd_operation_state"/>';
	var sel_tkn_lang = '<view:LanguageTag key="tkn_vd_sel_tkn"/>';
	var warn_info_1_lang = '<view:LanguageTag key="tkn_warn_info_1"/>';
	var warn_info_2_lang = '<view:LanguageTag key="tkn_warn_info_2"/>';
	var warn_info_3_lang = '<view:LanguageTag key="tkn_warn_info_3"/>';
	var warn_info_4_lang = '<view:LanguageTag key="tkn_warn_info_4"/>';
	var warn_info_5_lang = '<view:LanguageTag key="tkn_warn_info_5"/>';
	var warn_info_6_lang = '<view:LanguageTag key="tkn_warn_info_6"/>';
	var warn_info_8_lang = '<view:LanguageTag key="tkn_warn_info_8"/>';
	var tkn_sure_lang = '<view:LanguageTag key="tkn_sure"/>';
	var tkn_this_lang = '<view:LanguageTag key="tkn_this"/>';
	var ttkn_this_tkn_lang = '<view:LanguageTag key="tkn_this_tkn"/>';
	var tkn_a_tkn_lang = '<view:LanguageTag key="tkn_a_tkn"/>';
	var cancel_dist_lang = '<view:LanguageTag key="tkn_comm_cancel_dist"/>';
	var lock_succ_lang = '<view:LanguageTag key="common_lock_succ_tip"/>';
	var unlock_tkn_lang = '<view:LanguageTag key="tkn_sure_unlock_tkn"/>';
	var unlock_succ_lang = '<view:LanguageTag key="common_unlock_succ_tip"/>';
	var soft_success_lang = '<view:LanguageTag key="tkn_dist_soft_success"/>';
	var tkn_allot_org_bind_lang = '<view:LanguageTag key="tkn_allot_org_bind"/>'; 
	
	
	// End,多语言提取
	
	//权限
	var permEnabledY = ''; //启用
	var permEnabledN = ''; //停用
	var permLostY = ''; //挂失
	var permLostN = ''; //解挂
	var permLockedY = '';//锁定
	var permLockedN = '';//解锁
	var permLogout = '';//作废
	var permDelete = '';//删除
	var permAuth = '';//认证
	var permSync = '';//同步
	var permPin = '';//设置应急口令
	var permUnAssign = '';//取消分配
	var permTknAllot = '';//令牌分配
	var permAC = '';//获取激活码
	var permPUK1 = '';//获取一级解锁码
	var permPUK2 = '';//获取二级解锁码
	var permSDist = '';//软件令牌分发
	
	$(document).ready(function() {
	   //删除空白的（无权限的option）
	   $("#oper option:empty").remove();
	   permAuth = '<view:AdminPermTag key="030106" path="<%=path%>" langKey="tkn_auth" type="1" />';
	   permSync = '<view:AdminPermTag key="030107" path="<%=path%>" langKey="tkn_synch" type="1" />';
	   permPin = '<view:AdminPermTag key="030108" path="<%=path%>" langKey="tkn_set_emerg_pwd" type="1" />';
	   permUnAssign = '<view:AdminPermTag key="030109" path="<%=path%>" langKey="tkn_comm_cancel_dist" type="1" />';
	   permTknAllot = '<view:AdminPermTag key="030110" path="<%=path%>" langKey="tkn_allot" type="1" />';
	   permAC = '<view:AdminPermTag key="030111" path="<%=path%>" langKey="tkn_ac_title" type="1" />';
	   permPUK1 = '<view:AdminPermTag key="030112" path="<%=path%>" langKey="tkn_puk1_title" type="1" />';
	   permPUK2 = '<view:AdminPermTag key="030113" path="<%=path%>" langKey="tkn_puk2_title" type="1" />';
	   permSDist = '<view:AdminPermTag key="030114" path="<%=path%>" langKey="tkn_dist_soft" type="1" />';
	   permLockedY = '<view:AdminPermTag key="030102" path="<%=path%>" langKey="tkn_comm_lock" type="1" />';
	   
	   // 如果是批量查询过来的mode=1 或 如果是首页快速查询过来的mode=2或如果是组织查看过来的mode=3
	   if($("#mode").val()=='1'||$("#mode").val()=='2' ||$("#mode").val()=='3'){
	   	 pageFocus(1);
	   	 // 不显示收藏常用按钮
	   	 $("#useTd").hide();
	   }else{// 如果是点击令牌列表过来的
		 $('#logoutState').val(0);
	   	 pageFocus(0);
	   }
	   
	});
	
	// 重查操作
	function reQuery(){
	   var flag = $("#mode").val();
	   if(flag=='1'){
	   	 window.location.href = "<%=path%>/manager/token/tknbatch_query.jsp";
	   }else if(flag=='2'){
	     parent.tab.selectTabItem('home');
	     parent.removeTabItemF('0300');
	   }else{// 如果是点击令牌列表过来的
	   	 pageFocus(0);
	   }
	}
	
	</script>
</head>
<body scroll="no" style="overflow:hidden">
<div id="background"  class="background"  style="display: none; "></div>
<div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
<input type="hidden" id="contextPath" name="contextPath" value="<%=path%>" />
<input type="hidden" id="currentPage" name="currentPage" value="${param.currentPage}" />
<input type="hidden"  id="curtime" value="<%=System.currentTimeMillis()/1000%>"/>
<input type=hidden id="orgFlag" name="tokenQueryForm.orgFlag" value="<%=orgFlag%>" />
<!-- 首页快速查询 的查询条件 -->
<input type="hidden" id="queryText" value="${param.queryText}"/>
<!-- 令牌批量查询令牌号 -->
<input type="hidden" id="batchTknSn" name="batchTknSn" value="${param.batchTknSn}"/>
<input type="hidden" id="mode" name="mode" value="${param.mode}"/>
<form name="ListForm" id="ListForm" method="post" action="">
  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:6px">
   <tr>
    <td>
     <ul id="menu">
      <li class="tabFocus"><img src="<%=path%>/images/icon/zoom.png" width="16" height="16" hspace="4" align="absmiddle"/><c:if test="${param.mode eq 1}"><view:LanguageTag key="tkn_batch_query_tkn"/></c:if><c:if test="${param.mode != 1}"><view:LanguageTag key="tkn_query"/></c:if></li>
      <li><img src="<%=path%>/images/icon/tag_blue.png" width="16" height="16" hspace="4" align="absmiddle"/><view:LanguageTag key="tkn_tkn_list"/></li>
	  <li style="float:right"><a href="javascript:addAdmPermCode('0300','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a></li>
     </ul>
     <ul id="content">
     
     <li class="conFocus">
     <!-- 令牌查询条件页 begin -->
	 <table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:6px">
	   <tr>
	   	 <td width="10%">&nbsp;</td>
	   	 <td width="20%" align="right"><view:LanguageTag key="common_title_orgunit"/><view:LanguageTag key="colon"/></td>
         <td width="20%">
            <!-- 域：组织机构组合ID字符串 -->
            <input id="orgunitIds"  name="tokenQueryForm.orgunitIds" type="hidden" value="<%=dOrgunitId%>" />
            <!-- 域 组织机构名称字符串 -->
            <input id="orgunitNames" name="orgunitNames" onClick="selOrgunits(7,'<%=path%>');" readonly value="<%=dOrgunitName%>" class="formCss100" style="width:220px"/>
         </td>
         <td width="15%" align="right"><view:LanguageTag key="tkn_comm_tknum"/><view:LanguageTag key="colon"/></td>
         <td width="20%"><input type="text" id="tokenStr" name="tokenQueryForm.tokenStr" value="${tokenQueryForm.tokenStr}" class="formCss100" style="width:220px"/></td>
        <td width="15%">&nbsp;</td>
	   </tr>
	   <tr>
        <td>&nbsp;</td>
        <td align="right"><view:LanguageTag key="tkn_start_num"/><view:LanguageTag key="colon"/></td>
        <td>
           <input type="text" id="tokenStart" name="tokenQueryForm.tokenStart" value="${tokenQueryForm.tokenStart}" class="form-text" style="width:220px"/>
        </td>
        <td align="right"><view:LanguageTag key="tkn_stop_num"/><view:LanguageTag key="colon"/></td>
        <td>
           <input type="text" id="tokenEnd" name="tokenQueryForm.tokenEnd" value="${tokenQueryForm.tokenEnd}" class="form-text" style="width:220px"/>
        </td>
        <td>&nbsp;</td>
       </tr>
       <tr>
        <td width="10%">&nbsp;</td>
        <td width="15%" align="right"><view:LanguageTag key="tkn_comm_physical_type"/><view:LanguageTag key="colon"/></td>
         <td width="20%">
          <select id="physicaltype" name="tokenQueryForm.physicaltype" value="${tokenQueryForm.physicaltype}" class="select100" style="width:220px">
	          <option value="-1" <c:if test='${tokenQueryForm.physicaltype == -1}'> selected</c:if>>
	          <view:LanguageTag key="common_syntax_whole"/>
	          </option>
	          <option value="0" <c:if test='${tokenQueryForm.physicaltype == 0}'> selected</c:if>>
	          <view:LanguageTag key="tkn_physical_hard"/>
	          </option>
	          <option value="1" <c:if test='${tokenQueryForm.physicaltype == 1}'> selected</c:if>>
	          <view:LanguageTag key="tkn_physical_mobile"/>
	          </option>
	          <option value="2" <c:if test='${tokenQueryForm.physicaltype == 2}'> selected</c:if>>
	          <view:LanguageTag key="tkn_physical_soft"/>
	          </option>
	          <option value="6" <c:if test='${tokenQueryForm.physicaltype == 6}'> selected</c:if>>
	          <view:LanguageTag key="tkn_physical_sms"/>
	          </option>
         </select>
        </td>
        <td align="right"><view:LanguageTag key="tkn_valid"/><view:LanguageTag key="colon"/></td>
        <td>
	       <select id="expiretimeType" name="tokenQueryForm.expiretimeType" value="${tokenQueryForm.expiretimeType}" class="select100" style="width:220px">
              <option value="-1"><view:LanguageTag key='common_syntax_whole'/></option>
              <option value="0"><view:LanguageTag key='report_token_expired'/></option>
              <option value="1"><view:LanguageTag key='report_token_week_expire'/></option>
              <option value="2"><view:LanguageTag key='report_token_month_expire'/></option>
              <option value="3"><view:LanguageTag key='report_token_quarter_expire'/></option>
              <option value="4"><view:LanguageTag key='report_token_more_expire'/></option>
           </select>
        </td>
        <td>&nbsp;</td>
       </tr>

       <tr>
        <td>&nbsp;</td>
        <td align="right"><view:LanguageTag key="tkn_comm_state_enable"/><view:LanguageTag key="colon"/></td>
       	<td>
          <select id="enableState" name="tokenQueryForm.enableState" value="${tokenQueryForm.enableState}" class="select100" style="width:220px">
	          <option value="-1" <c:if test='${tokenQueryForm.enableState == -1}'> selected</c:if>>
	          <view:LanguageTag key="common_syntax_whole"/>
	          </option>
	          <option value="1" <c:if test='${tokenQueryForm.enableState == 1}'> selected</c:if>>
	          <view:LanguageTag key="tkn_state_enabled"/>
	          </option>
	          <option value="0" <c:if test='${tokenQueryForm.enableState == 0}'> selected</c:if>>
	          <view:LanguageTag key="tkn_state_unenabled"/>
	          </option>
          </select>
        </td>
        <td align="right"><view:LanguageTag key="tkn_comm_state_bind"/><view:LanguageTag key="colon"/></td>
        <td>
           <select id="bindState" name="tokenQueryForm.bindState" value="${tokenQueryForm.bindState}" class="select100" style="width:220px">
	           <option value="-1" <c:if test='${tokenQueryForm.bindState == -1}'> selected</c:if>>
	           <view:LanguageTag key="common_syntax_whole"/>
	           </option>
	           <option value="1" <c:if test='${tokenQueryForm.bindState == 1}'> selected</c:if>>
	           <view:LanguageTag key="tkn_state_bound"/>
	           </option>
	           <option value="0" <c:if test='${tokenQueryForm.bindState == 0}'> selected</c:if>>
	           <view:LanguageTag key="tkn_state_unbound"/>
	           </option>
           </select>
		</td>
		<td>&nbsp;</td>
       </tr>
       <tr>
         <td>&nbsp;</td>
         <td align="right"><view:LanguageTag key="tkn_comm_state_activation"/><view:LanguageTag key="colon"/></td>
       	 <td>
            <select id="pubkeyState" name="tokenQueryForm.pubkeyState" value="${tokenQueryForm.pubkeyState}" class="select100" style="width:220px">
	           <option value="-2" <c:if test='${tokenQueryForm.pubkeyState == -2}'> selected</c:if>>
	           <view:LanguageTag key="common_syntax_whole"/>
	           </option>
	           <option value="-1" <c:if test='${tokenQueryForm.pubkeyState == -1}'> selected</c:if>>
	           <view:LanguageTag key="tkn_has_activation_notstate"/>
	           </option>
	           <option value="0" <c:if test='${tokenQueryForm.pubkeyState == 0}'> selected</c:if>>
	           <view:LanguageTag key="tkn_not_activation"/>
	           </option>
	           <option value="1" <c:if test='${tokenQueryForm.pubkeyState == 1}'> selected</c:if>>
	           <view:LanguageTag key="tkn_has_activation_not_auth"/>
	           </option>
	           <option value="2" <c:if test='${tokenQueryForm.pubkeyState == 2}'> selected</c:if>>
	           <view:LanguageTag key="tkn_has_activation"/>
	           </option>
           </select>
         </td>
         <td align="right"><view:LanguageTag key="tkn_comm_state_lock"/><view:LanguageTag key="colon"/></td>
         <td>
          	<select id="lockedState" name="tokenQueryForm.lockedState" value="${tokenQueryForm.lockedState}" class="select100" style="width:220px">
	            <option value="-1" <c:if test='${tokenQueryForm.lockedState == -1}'> selected</c:if>>
	            <view:LanguageTag key="common_syntax_whole"/>
	            </option>
	            <option value="2" <c:if test='${tokenQueryForm.lockedState == 2}'> selected</c:if>>
	            <view:LanguageTag key="tkn_state_locked"/>
	            </option>
	            <option value="0" <c:if test='${tokenQueryForm.lockedState == 0}'> selected</c:if>>
	            <view:LanguageTag key="tkn_state_unlocked"/>
	            </option>
            </select>
         </td>
         <td>&nbsp;</td>
       </tr>
	   <tr>
	    <td>&nbsp;</td>
	   	<td align="right"><view:LanguageTag key='tkn_comm_state_lose'/><view:LanguageTag key="colon"/></td>
	   	<td>
           <select id="lostState" name="tokenQueryForm.lostState" value="${tokenQueryForm.lostState}" class="select100" style="width:220px">
	           <option value="-1" <c:if test='${tokenQueryForm.lostState == -1}'> selected</c:if>>
	           <view:LanguageTag key="common_syntax_whole"/>
	           </option>
	           <option value="1" <c:if test='${tokenQueryForm.lostState == 1}'> selected</c:if>>
	           <view:LanguageTag key="tkn_state_losed"/>
	           </option>
	           <option value="0" <c:if test='${tokenQueryForm.lostState == 0}'> selected</c:if>>
	           <view:LanguageTag key="tkn_state_unlosed"/>
	           </option>
           </select>
        </td>
	   	<td align="right"><view:LanguageTag key='tkn_comm_state_invalid'/><view:LanguageTag key="colon"/></td>
	   	<td>
          <select id="logoutState" name="tokenQueryForm.logoutState" value="${tokenQueryForm.logoutState}" class="select100" style="width:220px">
             <option value="-1" <c:if test='${tokenQueryForm.logoutState == -1}'> selected</c:if>>
             <view:LanguageTag key="common_syntax_whole"/>
             </option>
	         <option value="1" <c:if test='${tokenQueryForm.logoutState == 1}'> selected</c:if>>
	         <view:LanguageTag key="tkn_state_obsolete"/>
	         </option>
	         <option value="0" <c:if test='${tokenQueryForm.logoutState == 0}'> selected</c:if>>
	         <view:LanguageTag key="tkn_state_not_void"/>
	         </option>
          </select>
         </td>
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
	    <td height="40"><span style="display:inline-block;" class="query-button-css"><a href="javascript:query(false,true);" class="isLink_LanSe"><view:LanguageTag key="tkn_query_2"/></a></span></td>
	    <td></td>
	    <td></td>
	   </tr>
     </table>
     <!-- 令牌查询条件页 end -->
     </li>
     <li>
     <!-- 令牌列表页 begin -->
	 <table width="100%" border="0" cellspacing="0" cellpadding="0">
       <tr>
         <td>
           <table width="800" border="0" cellspacing="0" cellpadding="0">
             <tr>
               <td width="101" align="right"><view:LanguageTag key="common_syntax_operate"/><view:LanguageTag key="colon"/></td>
               <td width="155">
            	<select id="operObj" name="tokenQueryForm.operObj" class="select100_2">
                	<option value="0"><view:LanguageTag key="common_syntax_this_page_date"/></option>
                	<option value="1"><view:LanguageTag key="common_syntax_this_query_date"/></option>
              	</select>
               </td>
               <td width="102" align="right"><view:LanguageTag key="common_syntax_operation"/><view:LanguageTag key="colon"/></td>
               <td width="155">
            	<select id="oper" name="tokenQueryForm.operBatch" class="select100_2">
	                <option value="-1">--<view:LanguageTag key="common_syntax_please_sel"/>--</option>
	                <option value="0"><view:AdminPermTag key="030101" path="<%=path%>" langKey="tkn_comm_enable"  type="0" /></option>
	                <option value="1"><view:AdminPermTag key="030101" path="<%=path%>" langKey="tkn_comm_disable" type="0" /></option>
	                <option value="2"><view:AdminPermTag key="030102" path="<%=path%>" langKey="tkn_comm_lock"  type="0" /></option>
	                <option value="3"><view:AdminPermTag key="030102" path="<%=path%>" langKey="tkn_comm_unlock" type="0" /></option>
	                <option value="4"><view:AdminPermTag key="030103" path="<%=path%>" langKey="tkn_comm_lose"  type="0" /></option>
	                <option value="5"><view:AdminPermTag key="030103" path="<%=path%>" langKey="tkn_comm_relieve_lose" type="0" /></option>
	                <option value="6"><view:AdminPermTag key="030104" path="<%=path%>" langKey="tkn_comm_invalid"  type="0" /></option>
	                <option value="8"><view:AdminPermTag key="030105" path="<%=path%>" langKey="tkn_comm_delete" type="0" /></option>
	                <option value="9"><view:AdminPermTag key="030109" path="<%=path%>" langKey="tkn_comm_cancel_dist" type="0" /></option>
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
     <!-- 令牌列表页 end -->
     </li>
	</td>
  </tr>
</table>
</form>
<div id="listDataAJAX"></div>
</body>
</html>
