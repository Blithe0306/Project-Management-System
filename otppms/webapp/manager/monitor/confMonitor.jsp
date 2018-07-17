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
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>  
   	<script language="javascript" src="<%=path%>/manager/monitor/js/confMonitor.js"></script>
   	
	<script language="javascript" type="text/javascript">
	// Start,多语言提取
	var check_need_del_date_lang = '<view:LanguageTag key="common_syntax_check_need_del_date"/>';
	var please_sel_lang = '<view:LanguageTag key="common_vd_please_sel"/>';
	var tkn_exp_lang = '<view:LanguageTag key="monitor_vd_tkn_exp"/>';
	var tkn_exp_err_lang = '<view:LanguageTag key="monitor_vd_tkn_exp_err"/>';
	var remove_space_tip_lang = '<view:LanguageTag key="org_vd_remove_space_tip"/>';
	var unbound_tkn_lang = '<view:LanguageTag key="monitor_vd_unbound_tkn"/>';
	var unbound_tkn_err_lang = '<view:LanguageTag key="monitor_vd_unbound_tkn_err"/>';
	var one_hour_sync_lang = '<view:LanguageTag key="monitor_vd_tkn_one_hour_sync"/>';
	var one_hour_sync_err_lang = '<view:LanguageTag key="monitor_vd_tkn_one_hour_sync_err"/>';
	var timer_exec_lang = '<view:LanguageTag key="monitor_vd_timer_exec"/>';
	var timer_exec_err_lang = '<view:LanguageTag key="monitor_vd_timer_exec_err"/>';
	var cpu_upper_lang = '<view:LanguageTag key="monitor_vd_cpu_upper"/>';
	var cpu_upper_err_lang = '<view:LanguageTag key="monitor_vd_cpu_upper_err"/>';
	var disk_use_upper_lang = '<view:LanguageTag key="monitor_vd_disk_use_upper"/>';
	var disk_use_upper_err_lang = '<view:LanguageTag key="monitor_vd_disk_use_upper_err"/>';
	var memory_upper_lang = '<view:LanguageTag key="monitor_vd_memory_upper"/>';
	var memory_upper_err_lang = '<view:LanguageTag key="monitor_vd_memory_upper_err"/>';
	var timer_interval_lang = '<view:LanguageTag key="monitor_vd_timer_interval"/>';
	var timer_interval_err_lang = '<view:LanguageTag key="monitor_vd_timer_interval_err"/>';
	var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
	var sel_admin_list_lang = '<view:LanguageTag key="monitor_sel_admin_list"/>';
	// End,多语言提取
	
	 var currTabIndex = 0;
	 $(document).ready(function(){
	 	    $("#menu li").each(function(index) {
            $(this).click(function() {
                $("#menu li.tabFocus").removeClass("tabFocus");
                $(this).addClass("tabFocus");
                $("#content li:eq(" + index + ")").show().siblings().hide();
                currTabIndex = index;
              });
            });
            // 添加校验
 			checkBasicForm();
 			// 初始化是否可以输入方法
 			initDisabled();
	 }) 
	 
	 // 根据是否启用监控 判断控件的显示与否
	 function setDisabled(isEnabled,tableName,unCheckName){
	 	var eles = $("#"+tableName).find("input[name!='monitorConfInfo."+unCheckName+"'],a[id!='monitorSave']");
	 	if(isEnabled==0){
	 		eles.attr("disabled", true);
	 	}else{
	 		eles.attr("disabled", false);
	 	}
	 }
	 
	 // 初始化是否可以输入方法
	 function initDisabled(){
		 var baseEnabled = 0;
		 if($("#baseenabled").attr("checked")){
		 	baseEnabled = 1;
		 }
		 setDisabled(baseEnabled,'baseTable','baseenabled');
		 var sbEnabled = 0;
		 if($("#sbenabled").attr("checked")){
		 	sbEnabled = 1;
		 }
		 setDisabled(sbEnabled,'sbTable','sbenabled');
		 var appEnabled = 0;
		 if($("#appenabled").attr("checked")){
		 	appEnabled = 1;
		 }
		 setDisabled(appEnabled,'appTable','appenabled');
	 }
	</script>
 </head>
  
 <body>
 <input type="hidden" value="<%=path %>" id="contextPath" />
 <form name="coreForm" id="coreForm" method="post" action="">
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td width="100%" valign="top">
		<ul id="menu">
		  <li class="tabFocus"><view:LanguageTag key="monitor_base_info"/></li>
		  <li><view:LanguageTag key="monitor_equip_monitoring"/></li>
		  <li><view:LanguageTag key="monitor_app_monitoring"/></li>
	    </ul>
	    <ul id="content">	 
		<!-- 预警基本配置 -->
 		<li  class="conFocus" id="0">
 		 <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="ulOnInsideTable">
	       <tr>
	       	  <td colspan="3" height="25" class="topTableBgText" background="<%=path%>/images/manager/mgr_r7_c8.png"><view:LanguageTag key="monitor_base_info"/></td>
	       </tr>
	   	 </table>
		   <table id="baseTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
             <tr>
             <td align="right" height="20"><view:LanguageTag key="monitor_whether_enable"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
             <td align="left" height="20">
             	<div class="tableTDivLeft">
	             	<input type="radio" name="monitorConfInfo.baseenabled" id="baseenabled" value="1" onclick="setDisabled(1,'baseTable','baseenabled')"
						<c:if test="${monitorConfInfo.baseenabled eq 1}">checked</c:if>/><view:LanguageTag key="common_syntax_yes"/>&nbsp;&nbsp;
					<input type="radio" name="monitorConfInfo.baseenabled" id="baseenabled1" value="0" onclick="setDisabled(0,'baseTable','baseenabled')"
					<c:if test="${monitorConfInfo.baseenabled eq 0}">checked</c:if>/><view:LanguageTag key="common_syntax_no"/>
				</div>
             </td>
             <td class="divTipCss"><div id="baseenabledTip"></div></td>
            </tr>
             <tr>
             <td width="25%" height="20" align="right"><view:LanguageTag key="monitor_method"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
             <td width="30%" height="20" align="left">
             	<div class="tableTDivLeft">
             		<input type="radio" name="monitorConfInfo.basesendtype" id="basesendtype" value="0"
						<c:if test="${monitorConfInfo.basesendtype eq 0}">checked</c:if>/><view:LanguageTag key="monitor_email"/>&nbsp;&nbsp;
					<input type="radio" name="monitorConfInfo.basesendtype" id="basesendtype1" value="1"
					<c:if test="${monitorConfInfo.basesendtype eq 1}">checked</c:if>/><view:LanguageTag key="monitor_sms"/>
             	</div>
           	</td>
             <td width="45%" class="divTipCss"><div id="basesendtypeTip"></div></td>
            </tr>
 			<tr>
             <td align="right" height="20"><view:LanguageTag key="monitor_warning_receiver"/><view:LanguageTag key="colon"/></td>
             <td align="left" height="20">
             	<div class="tableTDivLeft">
             		<select name="monitorConfInfo.baserecvusers" size="5" multiple class="select100" id="baserecvusers">
		             <c:if test='${not empty monitorConfInfo.baserecvusers}'>
			          	 <c:forEach items="${monitorConfInfo.baserecvusers}" var="adminUserId">
			            	<option value="${adminUserId}" selected>${adminUserId}</option>
			          	 </c:forEach>	
		          	 </c:if>
		      		</select>
	            	<a href="#" onClick="selAdmins();" class="button"><span><view:LanguageTag key="common_syntax_select"/></span></a>
	                <a href="#" onClick="delAdmins();" class="button"><span><view:LanguageTag key="common_syntax_delete"/></span></a>
             	</div>
             </td>
             <td class="divTipCss"><div id="baserecvusersTip"></div></td>
            </tr>
			<tr>
             <td align="right" height="20"><view:LanguageTag key="monitor_tkn_expire"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
             <td align="left" height="20">
             	<div class="tableTDivLeft">
             		<input type="text" id="baseremaindays" name="monitorConfInfo.baseremaindays" value="${monitorConfInfo.baseremaindays}" class="formCss100"/>
             	</div>
             </td>
             <td class="divTipCss"><div id="baseremaindaysTip"></div></td>
            </tr>
            <tr>
             	<td align="right" height="20"><view:LanguageTag key="monitor_unbound_tkn_rate"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
             	<td align="left" height="20">
		             <div class="tableTDivLeft">
		              	<input type="text" id="baseunbindlower" name="monitorConfInfo.baseunbindlower" value="${monitorConfInfo.baseunbindlower}" class="formCss100"/>
		            </div>
	            </td>
	            <td class="divTipCss"><div id="baseunbindlowerTip"></div></td>
            </tr>
            <tr>
	             <td align="right" height="20"><view:LanguageTag key="monitor_tkn_within_one_hour_sync_rate"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	             <td align="left" height="20">
	             	<div class="tableTDivLeft">
	             		<input type="text" id="basesyncupper" name="monitorConfInfo.basesyncupper" value="${monitorConfInfo.basesyncupper}" class="formCss100"/>
	             	</div>
	             </td>
	             <td class="divTipCss"><div id="basesyncupperTip"></div></td>
            </tr>
             <tr>
             <td align="right" height="20"><view:LanguageTag key="monitor_timer_exec_time_interval"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
             <td align="left" height="20">
             	<div class="tableTDivLeft">
             		<input type="text" id="basetimeinterval" name="monitorConfInfo.basetimeinterval" value="${monitorConfInfo.basetimeinterval}" class="formCss100"/>
             	</div>
             </td>
             <td class="divTipCss"><div id="basetimeintervalTip"></div></td>
            </tr>
 		</table>
         </li>
          <!-- 设备监控预警 -->
         <li id="1">
         <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="ulOnInsideTable">
	       <tr>
	       	  <td colspan="3" height="25" class="topTableBgText" background="<%=path%>/images/manager/mgr_r7_c8.png"><view:LanguageTag key="monitor_equip_monitoring"/></td>
	       </tr>
	   	 </table>
         <table id="sbTable"  width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
            <tr>
              <td align="right" height="20"><view:LanguageTag key="monitor_whether_enable"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
              <td align="left"  height="20">
              	<div class="tableTDivLeft">
	             	<input type="radio" name="monitorConfInfo.sbenabled" id="sbenabled" value="1" onclick="setDisabled(1,'sbTable','sbenabled')"
						<c:if test="${monitorConfInfo.sbenabled eq 1}">checked</c:if>/><view:LanguageTag key="common_syntax_yes"/>&nbsp;&nbsp;
					<input type="radio" name="monitorConfInfo.sbenabled" id="sbenabled1" value="0" onclick="setDisabled(0,'sbTable','sbenabled')"
					<c:if test="${monitorConfInfo.sbenabled eq 0}">checked</c:if>/><view:LanguageTag key="common_syntax_no"/>
				</div>
               </td>
               <td class="divTipCss"><div id="sbenabledTip"></div></td>
            </tr>
		   	<tr>
             <td width="25%" height="20" align="right" ><view:LanguageTag key="monitor_method"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
             <td width="30%" height="20" align="left">
            	 <div class="tableTDivLeft">
             		<input type="radio" name="monitorConfInfo.sbsendtype" id="sbsendtype" value="0"
						<c:if test="${monitorConfInfo.sbsendtype eq 0}">checked</c:if>/><view:LanguageTag key="monitor_email"/>&nbsp;&nbsp;
					<input type="radio" name="monitorConfInfo.sbsendtype" id="sbsendtype1" value="1"
					<c:if test="${monitorConfInfo.sbsendtype eq 1}">checked</c:if>/><view:LanguageTag key="monitor_sms"/>
             	</div>
             </td>
             <td width="45%" class="divTipCss"><div id="sbsendtypeTip"></div></td>
            </tr>
            <tr>
              <td align="right" height="20"><view:LanguageTag key="monitor_warning_receiver"/><view:LanguageTag key="colon"/></td>
              <td align="left" height="20">
              	<div class="tableTDivLeft">
             		<select name="monitorConfInfo.sbrecvusers" size="5" multiple class="select100" id="sbrecvusers">
		             <c:if test='${not empty monitorConfInfo.sbrecvusers}'>
			          	 <c:forEach items="${monitorConfInfo.sbrecvusers}" var="adminUserId">
			            	<option value="${adminUserId}" selected>${adminUserId}</option>
			          	 </c:forEach>
		          	 </c:if>
		      		</select>
	            	<a href="#" onClick="selAdmins();" class="button"><span><view:LanguageTag key="common_syntax_select"/></span></a>
	                <a href="#" onClick="delAdmins();" class="button"><span><view:LanguageTag key="common_syntax_delete"/></span></a>
             	</div>
              </td>
              <td class="divTipCss"><div id="sbrecvusersTip"></div></td>
            </tr>
            <tr>
	            <td align="right" height="20"><view:LanguageTag key="monitor_cpu_upper"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	            <td align="left" height="20">
	            	<div class="tableTDivLeft">
		              <input type="text" id="sbcpuupper" name="monitorConfInfo.sbcpuupper" value="${monitorConfInfo.sbcpuupper}" class="formCss100"/>
	              	</div>
	             </td>
	             <td class="divTipCss"><div id="sbcpuupperTip"></div></td>
            </tr>
             <tr>
             	<td align="right" height="20"><view:LanguageTag key="monitor_disk_use_upper"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
             	<td align="left" height="20">
		            <div class="tableTDivLeft">
		              <input type="text" id="sbdiskupper" name="monitorConfInfo.sbdiskupper" value="${monitorConfInfo.sbdiskupper}" class="formCss100"/>
	                </div>
	             </td>
	             <td class="divTipCss"><div id="sbdiskupperTip"></div></td>
            </tr>
            <tr>
             	<td align="right" height="20"><view:LanguageTag key="monitor_memory_occu_upper"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
             	<td align="left" height="20">
		            <div class="tableTDivLeft">
		              <input type="text" id="sbmemupper" name="monitorConfInfo.sbmemupper" value="${monitorConfInfo.sbmemupper}" class="formCss100"/>
	                </div>
	             </td>
	             <td class="divTipCss"><div id="sbmemupperTip"></div></td>
            </tr>
            <tr>
             <td align="right" height="20"><view:LanguageTag key="monitor_timer_interval"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
             <td align="left" height="20">
             	<div class="tableTDivLeft">
             		<input type="text" id=sbtimeinterval name="monitorConfInfo.sbtimeinterval" value="${monitorConfInfo.sbtimeinterval}" class="formCss100"/>
             	</div>
             </td>
             <td class="divTipCss"><div id="sbtimeintervalTip"></div></td>
            </tr>
          </table>            
         </li>
          <!-- 应用监控预警 -->
         <li id="2">
         <table width="100%" border="0"   cellpadding="0" cellspacing="0" class="ulOnInsideTable">
	       <tr>
	       	  <td colspan="3" height="25" class="topTableBgText" background="<%=path%>/images/manager/mgr_r7_c8.png"><view:LanguageTag key="monitor_app_monitoring"/></td>
	       </tr>
	   	 </table>
          <table id="appTable" width="100%"  border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
            <tr>
             <td align="right" height="20"><view:LanguageTag key="monitor_whether_enable"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
             <td align="left" height="20">
             	<div class="tableTDivLeft">
	             	<input type="radio" name="monitorConfInfo.appenabled" id="appenabled" value="1" onclick="setDisabled(1,'appTable','appenabled')"
						<c:if test="${monitorConfInfo.appenabled eq 1}">checked</c:if>/><view:LanguageTag key="common_syntax_yes"/>&nbsp;&nbsp;
					<input type="radio" name="monitorConfInfo.appenabled" id="appenabled1" value="0" onclick="setDisabled(0,'appTable','appenabled')"
					<c:if test="${monitorConfInfo.appenabled eq 0}">checked</c:if>/><view:LanguageTag key="common_syntax_no"/>
				</div>
              </td>
              <td class="divTipCss"><div id="appenabledTip"></div></td>
            </tr>
            <tr>
             <td width="25%" height="20" align="right"><view:LanguageTag key="monitor_method"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
             <td width="30%" height="20" align="left">
	             <div class="tableTDivLeft">
             		<input type="radio" name="monitorConfInfo.appsendtype" id="appsendtype" value="0"
						<c:if test="${monitorConfInfo.appsendtype eq 0}">checked</c:if>/><view:LanguageTag key="monitor_email"/>&nbsp;&nbsp;
					<input type="radio" name="monitorConfInfo.appsendtype" id="appsendtype1" value="1"
					<c:if test="${monitorConfInfo.appsendtype eq 1}">checked</c:if>/><view:LanguageTag key="monitor_sms"/>
             	</div>
             	 </td>
             	 <td width="45%" class="divTipCss"><div id="appsendtypeTip"></div></td>
            </tr>   
            <tr>
             <td align="right" height="20"><view:LanguageTag key="monitor_warning_receiver"/><view:LanguageTag key="colon"/></td>
             <td align="left" height="20">
             	<div class="tableTDivLeft">
             		<select name="monitorConfInfo.apprecvusers" size="5" multiple class="select100" id="apprecvusers">
		             <c:if test='${not empty monitorConfInfo.apprecvusers}'>
			          	 <c:forEach items="${monitorConfInfo.apprecvusers}" var="adminUserId">
			            	<option value="${adminUserId}" selected>${adminUserId}</option>
			          	 </c:forEach>
		          	 </c:if>
		      		</select>
	            	<a href="#" onClick="selAdmins();" class="button"><span><view:LanguageTag key="common_syntax_select"/></span></a>
	                <a href="#" onClick="delAdmins();" class="button"><span><view:LanguageTag key="common_syntax_delete"/></span></a>
             	</div>
              </td>
              <td class="divTipCss"><div id="apprecvuserTip"></div></td>
            </tr>
            <tr>
             <td align="right" height="20"><view:LanguageTag key="monitor_timer_interval"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
             <td align="left" height="20">
             	<div class="tableTDivLeft">
	              <input type="text" id=apptimeinterval name="monitorConfInfo.apptimeinterval" value="${monitorConfInfo.apptimeinterval}" class="formCss100"/>
             	</div>
              </td>
              <td class="divTipCss"><div id="apptimeintervalTip"></div></td>
            </tr>
          </table>
          </li>
          
        </ul>
	   </td>
      </tr>
    </table>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
      <tr>
      	<td align="right" width="25%"></td>
		<td align="right">
            <a href="#" id="monitorSave" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
        </td>
        <td align="right"></td>
     </tr>
    </table>
   </form>
  </body>
</html>