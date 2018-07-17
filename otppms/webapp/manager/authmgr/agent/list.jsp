<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
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
    
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
   	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
    
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>	
	<script type="text/javascript" src="<%=path%>/manager/authmgr/agent/js/list.js"></script>
	
	<script language="javascript" type="text/javascript">
	// Start,多语言提取
	var langYes = '<view:LanguageTag key="common_syntax_yes"/>';
	var langNo = '<view:LanguageTag key="common_syntax_no"/>';
	var langDisabled = '<view:LanguageTag key="common_syntax_disabled"/>';
	var langEnable = '<view:LanguageTag key="common_syntax_enable"/>';
	
	// 列表
	var auth_agent_agentname_lang='<view:LanguageTag key="auth_agent_agentname"/>';
	var agent_agentip_lang = '<view:LanguageTag key="auth_agent_agentip"/>';
	var ser_hostip_lang = '<view:LanguageTag key="auth_ser_hostip"/>';
	var agent_type_lang = '<view:LanguageTag key="auth_agent_agent_type"/>';
	var agent_agentconf_lang = '<view:LanguageTag key="auth_agent_agentconf"/>';
	var syntax_operation_lang = '<view:LanguageTag key="common_syntax_operation"/>';
	
	// 操作
	var confirm_enable_lang = '<view:LanguageTag key="auth_agent_confirm_enable"/>';
	var confirm_disabled_lang = '<view:LanguageTag key="auth_agent_confirm_disabled"/>';
	var syntax_confirm_lang = '<view:LanguageTag key="common_syntax_confirm"/>';
	var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
	var auth_server_lang = '<view:LanguageTag key="common_menu_auth_server"/>';
	var succ_tip_lang = '<view:LanguageTag key="common_save_succ_tip"/>';
	// End,多语言提取
	
	 var permEdit;//编辑
	 var permDown;//下载
	 var permFree;//解除
	 var permAdd;//添加
	 var permEnabled;//是否启用
	 $(function() {
	 	  permDown = '<view:AdminPermTag key="040105" path="<%=path%>" langKey="auth_agent_download_proxy_file" type="1" />';
	 	  permFree = '<view:AdminPermTag key="040106" path="<%=path%>" langKey="auth_agent_lift_proxy_server" type="1" />';
	 	  permEdit = '<view:AdminPermTag key="040103" path="<%=path%>" langKey="common_syntax_edit" type="1" />';
	 	  permEnabled = '';
	 	  permAdd = '<view:AdminPermTag key="040108" path="<%=path%>" langKey="auth_agent_add_proxy_server" type="1" />';
	 	  
	 	  initAgentConf();//初始化认证代理配置 查询项
     })
		
	//删除
	function delData(){
		var ajaxbg = $("#background,#progressBar");//加载等待
		var selRows = dataGrid.getSelectedRows();
		if(selRows < 1){
			FT.toAlert('warn','<view:LanguageTag key="common_syntax_check_need_del_date" />', null);
		} else {
			$.ligerDialog.confirm('<view:LanguageTag key="common_syntax_confirm_del_sel_date" />','<view:LanguageTag key="common_syntax_confirm" />',function(yes){
				if(yes){
					ajaxbg.show();
					var selLength = selRows.length;
	            	var ids = "";
	            	for(var i=0;i<selLength;i++) {
	            		ids = ids.concat(selRows[i]['agentipaddr'],',');
	            	}
	            	$.post("<%=path%>/manager/authmgr/agent/authAgent!delete.action",{
						delAgentIps:ids.substring(0,ids.length-1)
		            	},function(result){
		            		ajaxbg.hide();
		            		if(result.errorStr == 'success'){	            			
			            		$.ligerDialog.success(result.object, '<view:LanguageTag key="common_syntax_tip" />', function(){
			            		});
			            	}else {
			            		FT.toAlert(result.errorStr, result.object, null);
			            	}
			            	query(true);
		            	},'json'
	            	);
				}
			});			
		}
	 }
		
	//编辑
	function findObj(hostip){
		window.location.href = "<%=path%>/manager/authmgr/agent/authAgent!find.action?agentInfo.agentipaddr=" + hostip +"&curPage=${html_page_info.curPage}&talRow=${html_page_info.totalRow}";
	}
		
	//查看详细信息
	function viewAgent(agentip,groupid){
		var  url="<%=path%>/manager/authmgr/agent/authAgent!view.action?agentInfo.agentipaddr=" + agentip +"&agentInfo.groupId=" + groupid;
    	FT.openWinMiddle('<view:LanguageTag key="auth_agent_detail_info"/>',url,'close');
	}
        
	//查看代理关联的服务器的信息
	function viewServer(serverip){
		FT.openWinMiddle('<view:LanguageTag key="auth_ser_base_info" />', '<%=path%>/manager/authmgr/server/authServer!view.action?serverInfo.hostipaddr=' + serverip ,'close');
	}
	
	//查看代理关联的用户组的信息
	function viewGroup(groupid){
		FT.openWinMiddle('<view:LanguageTag key="common_syntax_detail_info" />',"<%=path%>/manager/user/group/userGroup!view.action?userGroup.groupId=" + groupid ,'close');
	}
	
	//代理配置文件下载
	function downLoad(agentIp){
			$.ajax({
			   type: "POST",
			   url: "<%=path%>/manager/authmgr/agent/authAgent!createACF.action",
			   data: "agentIp="+agentIp,
			   success: function(msg){
			     	   if(msg == 'noServ'){
						   FT.toAlert('warn','<view:LanguageTag key="auth_agent_download_proxy_no_sel" />', null);
						}else if(msg == 'failed'){
						    FT.toAlert('error','<view:LanguageTag key="auth_agent_download_proxy_err" />', null);
						}else{
						    window.location.href="<%=path%>/manager/authmgr/agent/authAgent!downLoadACF.action?acfName="+msg;
						}
			       }
			});
	 }

	//解除认证代理与认证服务器的绑定关系
	function unbindAS(ipAddr,count){
		if(count==1){
		    var hostipaddr = $(document.getElementById(ipAddr+"hidehostIp")).val();
			$.ligerDialog.confirm('<view:LanguageTag key="auth_agent_ser_confirm_unbind" />', function (yes){
		     if(yes){
		     	var unbindUrl = "<%=path%>/manager/authmgr/server/authAgent!unBindAS.action?agentInfo.agentipaddr="+ipAddr+"&hostIP="+hostipaddr+"&unbindNum="+count+"&curPage="+curPage;
		     	$.post(unbindUrl,function(result){
	            		if(result.errorStr == 'success'){	            			
		            		$.ligerDialog.success(result.object, '<view:LanguageTag key="common_syntax_tip" />', function(){
		            		});
		            	}else {
		            		FT.toAlert(result.errorStr, result.object, null);
		            	}
		            	query(true);
	            	},'json'
            	);
		     }
		 	});
		}else{
			FT.openWinMiddle('<view:LanguageTag key="auth_ser_unbind" />',"<%=path%>/manager/authmgr/server/authAgent!unBindASInit.action?agentInfo.agentipaddr="+ipAddr+"&curPage="+curPage,[{text:'<view:LanguageTag key="auth_agent_ser_unbind" />',onclick:FT.buttonAction.okClick},{text:'<view:LanguageTag key="common_syntax_close" />',onclick:FT.buttonAction.cancelClose}]);
		}
	}
	
	//添加认证代理
	function addAuthAgentInfo(){
	    window.location.href="<%=path%>/manager/authmgr/agent/add.jsp";
	}
	
	//初始化认证代理配置
	function initAgentConf(){
		var confid = "";
		var agentId = "";
		var url_ = "<%=path%>/manager/authmgr/agent/authAgent!queryConfList.action";
		$.ajax({
			type: "POST",
			url: url_,
			async: false,
			data: {'type':'-1', 'confid':confid, 'agentId':agentId},
			dataType: "json",	    
			success: function(msg){
				if(msg.errorStr == 'success'){
					 document.getElementById('confListDiv').innerHTML = msg.object;
				}else {
					FT.toAlert(msg.errorStr,msg.object,null);
				}
			}
		});
	}

	//-->
	</script>
  </head>  
  <body scroll="no" style="overflow:auto; overflow-x:hidden">
  	<div id="background"  class="background"  style="display: none; "></div>
    <div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
  	<input id="contextPath" type="hidden" value="<%=path%>" />
  	<input id="ipaddr" type="hidden" value="" />
  	<input id="hostIps" type="hidden" value="" />
	<input id="curPage" type="hidden" type="text" value="${param.cPage}" />
	<table width="800" border="0" cellspacing="0" cellpadding="0" style="margin-top:6px">
      <tr>
        <td width="116" align="right"><view:LanguageTag key='auth_agent_agentip'/><view:LanguageTag key="colon"/></td>
        <td width="168"><input type="text" id="agentIPAddr" name="agentQueryForm.agentIPaddr" value="${agentQueryForm.agentIPaddr}" class="formCss100"/></td>
        <td width="116" align="right"><view:LanguageTag key='auth_ser_hostip'/><view:LanguageTag key="colon"/></td>
        <td width="168"><input type="text" id="hostIPAddr" name="agentQueryForm.hostIPaddr" value="${agentQueryForm.hostIPaddr}" class="formCss100"/></td>
        <td width="15">&nbsp;</td>
        <td width="217">&nbsp;</td>
   	  </tr>
   	  <tr>
   	  	<td align="right"><view:LanguageTag key="auth_agent_agent_type"/><view:LanguageTag key="colon"/></td>
        <td>
			<select id="agentType" name="queryForm.agentType" value="${queryForm.agentType}" class="select100" style="width:168px">
	            <option value="-1" <c:if test='${queryForm.agentType == -1}'> selected</c:if>>
	            	<view:LanguageTag key="common_syntax_please_sel"/>
	            </option>
	            <option value="4" <c:if test='${queryForm.agentType == 4}'> selected</c:if>>
	            	<view:LanguageTag key="auth_agent_wins_login_protect"/>
	            </option>
	            <option value="8" <c:if test='${queryForm.agentType == 8}'> selected</c:if>>
	            	<view:LanguageTag key="auth_agent_linux_login_protect"/>
	            </option>
	            <option value="1073741824" <c:if test='${queryForm.agentType == 1073741824}'> selected</c:if>>
	            	<view:LanguageTag key="auth_agent_other_proxy"/>
	            </option>
            </select>        
		</td>
        <td align="right"><view:LanguageTag key="auth_agent_agentconf"/><view:LanguageTag key="colon"/></td>
        <td>
			<div id="confListDiv"></div>
		</td>
        <td>&nbsp;</td>
        <td><span style="display:inline-block" class="query-button-css"><a href="javascript:query();" class="isLink_LanSe"><view:LanguageTag key="common_syntax_query"/></a></span></td>
   	  </tr>
    </table>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="operTableBg">
	  <tr>
	  	<td>
	  		<span style="float:left">&nbsp;&nbsp;</span>
			<view:AdminPermTag key="040104" path="<%=path%>" langKey="common_syntax_delete" type="2" />
		</td>
	  </tr>
	</table>
 	<div id="listDataAJAX"></div>     
 </body>
</html>