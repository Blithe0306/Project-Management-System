<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
 
<%
	String path = request.getContextPath();
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);

	String userId = request.getParameter("userId");
	// 管理员管理的域机构
	String orgunitIds = request.getParameter("orgunitIds");
	String domainId = "";
	userId=userId+":"+domainId;
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
    <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
    <script src="<%=path%>/manager/common/js/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script> 
	<script type="text/javascript" src="<%=path%>/manager/admin/user/js/bind_token.js"></script>
	<script type="text/javascript">
		
		// Start,多语言提取
		var langHardtkn = '<view:LanguageTag key="tkn_physical_hard"/>';
		var langMtkn = '<view:LanguageTag key="tkn_physical_mobile"/>';
		var langStkn = '<view:LanguageTag key="tkn_physical_soft"/>';
		var langSMStkn = '<view:LanguageTag key="tkn_physical_sms"/>';
		var langBound = '<view:LanguageTag key="tkn_state_bound"/>';
		var langUnbound = '<view:LanguageTag key="tkn_state_unbound"/>';
		var langYes = '<view:LanguageTag key="common_syntax_yes"/>';
		var langNo = '<view:LanguageTag key="common_syntax_no"/>';
		
		// 列表
		var tknum_lang = '<view:LanguageTag key="tkn_comm_tknum"/>';
		var orgunit_lang = '<view:LanguageTag key="domain_orgunit"/>';
		var physical_type_lang = '<view:LanguageTag key="tkn_comm_physical_type"/>';
		var enable_lang = '<view:LanguageTag key="common_syntax_enable"/>';
		var lock_lang = '<view:LanguageTag key="admin_info_lock"/>';
		var lose_lang = '<view:LanguageTag key="tkn_comm_lose"/>';
		var binding_lang = '<view:LanguageTag key="admin_binding"/>';
		
		// 操作
		var detail_info_lang = '<view:LanguageTag key="common_syntax_detail_info"/>';
		var tkn_bind_lang = '<view:LanguageTag key="admin_only_sel_one_tkn_bind"/>';
		var bind_tkn_lang = '<view:LanguageTag key="admin_no_sel_can_bind_tkn"/>';
		// End,多语言提取
	
     	$(function(){
		   $('#userArr').val('<%=userId+","%>');
     		
     	   //回车查询事件
		   $('#ListForm').bind('keyup', function(event){
		   		if (event.keyCode=="13"){
		    		$('#query').click();
		   		}
		   });
		});
         
        //绑定操作
		function bindUT(){
		    //获取选择的令牌
			var isOK = getSelectTnk();
			if(!isOK){
				return ;
			}
			
         	//用户条件 管理只有一个
			var userIdStr = $.trim($("#userId").val());
			//令牌条件 管理员只支持一个
			var tokenStr = $.trim($("#token").val());
			
			//用户集合 userId:domainId,userId:domainId,形式
			var userArr = $('#userArr').val();
			//令牌集合
			var tokenArr = $('#tokenArr').val();
					
			var url='<%=path%>/manager/admin/user/adminBind!batchBindUT.action';
			$.post(url, {
			    userId:userIdStr,
				token:tokenStr,
				userArr:userArr,
				tokenArr:tokenArr},
				function(msg){
				   if(msg.errorStr=='success'){
				   		$.ligerDialog.success('<view:LanguageTag key="admin_bind_succ"/>', '<view:LanguageTag key="common_syntax_tip"/>',function(){
				    			goBack();
				   		});
				   }else{
				   	 FT.toAlert(msg.errorStr,msg.object, null);
				   }
				 },"json");
		}
		
		//绑定确认
		function bindUTTips(){			
		    $.ligerDialog.confirm('<view:LanguageTag key="user_validate_confirm_bind"/>', function (yes){
		     if(yes){
			    bindUT();
		     }
		 });
		}
		
		// 返回
		function goBack(){
			var cpage = $("#cPage").val();
			window.location.href = "<%=path%>/manager/admin/user/list.jsp?cPage="+cpage;
		} 
	//-->
	</script>
  </head>

  <body style="overflow-x:hidden">
  <input  type="hidden" value="${param.userId }"  id="userId"/>
  <input id="cPage" type="hidden" value="${param.currentPage}" />
  <input  type="hidden"   value=""  id="userArr"/>
  <input  type="hidden"   value=""  id="tokenArr" />
  <input  type="hidden" name="contextPath" value="<%=path%>" id="contextPath" />
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td>  
		  <span class="topTableBgText"><view:LanguageTag key="user_tkn_query_sel"/></span>
        </td>
      </tr>
    </table> 
	<table width="100%" border="0"   cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">		
		<form name="BindForm" id="BindForm" method="post" action="">
        <input type="hidden" id="orgunitIds" name="queryForm.dOrgunitId"  value="${queryForm.dOrgunitId}"/>
		 <!-- 令牌列表 -->
		 <table width="800" border="0" cellspacing="0" cellpadding="0">
           <tr>
             <input type="hidden" id="orgunitNames_"  onClick="selOrgunits(5,'<%=path%>');"  class="formCss100" readonly/>
			 <input type="hidden" id="orgunitIds_"   value="${queryForm.dOrgunitId}"/>
             <td width="120" align="right"><view:LanguageTag key="tkn_comm_tknum"/><view:LanguageTag key="colon"/></td>
             <td width="155"><input type="text" id="token" name="queryForm.token" value="${queryForm.token}" class="formCss100" style="width:140px"/></td>
             <td width="120" align="right"><view:LanguageTag key="tkn_start_stop_num"/><view:LanguageTag key="colon"/></td>
             <td colspan="2"><input type="text" id="tokenStart" name="queryForm.tokenStart" value="${queryForm.tokenStart}" class="form-text" style="width:140px" />
            	--
            	<input type="text" id="tokenEnd" name="queryForm.tokenEnd" value="${queryForm.tokenEnd}"  class="form-text" style="width:140px" /></td>
           </tr>
           <tr>
             <td align="right"><view:LanguageTag key="tkn_comm_physical_type"/><view:LanguageTag key="colon"/></td>
             <td><select id="pType" name="queryForm.physicaltype" value="${queryForm.physicaltype}" class="select100" style="width:141px">
				  <option value="-1" <c:if test='${queryForm.physicaltype==-1}'> selected</c:if>><view:LanguageTag key="common_syntax_whole"/></option>
				  <option value="0"  <c:if test='${queryForm.physicaltype == 0}'> selected</c:if>><view:LanguageTag key="tkn_physical_hard"/></option>
				  <option value="1"  <c:if test='${queryForm.physicaltype == 1}'> selected</c:if>><view:LanguageTag key="tkn_physical_mobile"/></option>
				  <option value="2"  <c:if test='${queryForm.physicaltype == 2}'> selected</c:if>><view:LanguageTag key="tkn_physical_soft"/></option>
				  <option value="6"  <c:if test='${queryForm.physicaltype == 6}'> selected</c:if>><view:LanguageTag key="tkn_physical_sms"/></option>
			   </select></td>
             <td align="right"><view:LanguageTag key='tkn_comm_state_bind'/><view:LanguageTag key="colon"/></td>
             <td width="227"><select id="bindState" name="queryForm.bindState" value="${queryForm.bindState}" class="select100" style="width:140px" >
	              <option value="-1" <c:if test='${queryForm.bindState == -1}'> selected</c:if>><view:LanguageTag key="common_syntax_whole"/></option>
				  <option value="1"  <c:if test='${queryForm.bindState == 1}'> selected</c:if>><view:LanguageTag key="tkn_state_bound"/></option>
				  <option value="0"  <c:if test='${queryForm.bindState == 0}'> selected</c:if>selected><view:LanguageTag key="tkn_state_unbound"/></option>
			    </select></td>
             <td width="178"><table width="100%" border="0" cellspacing="0" cellpadding="0">
               <tr>
                 <td width="33%"><span style="display:inline-block" class="query-button-css"><a href="#" onClick="query()" class="isLink_LanSe"><span>
                  <view:LanguageTag key="common_syntax_query"/></span></a></span></td>
               </tr>
             </table></td>
           </tr>
         </table>
		 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="operTableBg">
	      <tr>
	        <td>
	        	<span style="float:right">	
					<span><a href="#" onClick="goBack();" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a></span>
	        		<span><a href="javascript:bindUT();"  class="button"><span><view:LanguageTag key="admin_binding"/></span></a></span>
	        	</span>
	        </td>
	      </tr>
	    </table> 
		</form>
		</td>
      </tr>
  </table>
  <div id="tokenListAjx" /> 
  </body>
</html>