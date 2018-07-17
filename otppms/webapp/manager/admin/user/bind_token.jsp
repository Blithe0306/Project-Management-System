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
  	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
	
    <script type="text/javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
    <script src="<%=path%>/manager/common/js/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/admin/user/js/sel_tokens.js"></script>
    
    <script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script>  
    
	<script language="javascript" type="text/javascript">
	
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
	var state_bind_lang = '<view:LanguageTag key="tkn_comm_state_bind"/>';
	// End,多语言提取
	
	// 更换令牌主方法
	function exchangess(){	
		var rightsRows = rightListData.getSelectedRows();
		if(rightsRows.length==0){
			FT.toAlert('warn','<view:LanguageTag key="admin_sel_replace_tkn"/>',null);
			return false;
		}
		if(rightsRows.length!=1){
			FT.toAlert('warn','<view:LanguageTag key="admin_replace_tkn_must_align"/>',null);
			return false;
		}else{
			//原来的令牌
			var oldtokens=document.getElementById("hiddenTkns");
			
			//选中的新令牌
			var newtokens= '';
			for(var i=0,sl=rightsRows.length;i<sl;) {
			   newtokens = rightsRows[i++]['token'];
			}
			
			//赋值
			$("#tokens").val(newtokens);
			//更换或绑定令牌
			exchangTokenObj();
			
		} //end else
	}	
	
	//更换令牌
	function exchangTokenObj(oldToken,newToken){			
		$("#AddForm").ajaxSubmit({
			async:false,    
			dataType : "json",  
			type:"POST", 
			url : "adminBind!bindChangeTkn.action",
			success:function(msg){
			     if(msg.errorStr == 'success'){
			         $.ligerDialog.success(msg.object, '<view:LanguageTag key="common_syntax_tip"/>',function(){
			         	goBack();
			         });
			     }else{
				        FT.toAlert(msg.errorStr,msg.object,null);
			     
			     }
			}
		});
	}
	
	// 返回
	function goBack(){
		var cpage = $("#currentPage").val();
		window.location.href = "<%=path%>/manager/admin/user/list.jsp?cPage="+cpage;
	} 
	</script>
  </head>

  <body style="overflow:hidden">
  <input id="contextPath" type="hidden" value="<%=path%>" />
  <input id="currentPage" type="hidden" value="${param.currentPage}" />
    
  <form name="AddForm" id="AddForm" method="post" action="">
  <input type="hidden" id="hiddenTkns" name="adminUser.hiddenTkns" value="${param.tokenStr}"/>
  <input type="hidden" id="userId" name="adminUser.adminid" value="${param.adminid}"/>
  <input type="hidden" id="tokens" name="adminUser.tokens" value=""/>
  
  <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
    <tr>
      <td>  
        <span class="topTableBgText"><view:LanguageTag key="admin_replace_tkn"/></span>
      </td>
    </tr>
  </table> 
  <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td>&nbsp;&nbsp;&nbsp;<view:LanguageTag key="admin_info_account"/><view:LanguageTag key="colon"/>&nbsp;&nbsp;${param.adminid }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<view:LanguageTag key="admin_curr_bind_tkn"/><view:LanguageTag key="colon"/>&nbsp;&nbsp;${param.tokenStr}</td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td>
        	<table width="800" border="0" cellspacing="0" cellpadding="0">
       			<tr>   
       			   <input type="hidden" id="orgunitIds" name="queryForm.dOrgunitId"  value=""/>
       			   <input type="hidden" id="orgunitNames" name="orguintNames" onClick="selOrgunits(5,'<%=path%>');" value="" class="formCss" readonly/>
       			   <td width="64" align="right"><view:LanguageTag key="tkn_comm_tknum"/><view:LanguageTag key="colon"/></td>
       			   <td width="150" align="left">
       			   		<input type="text" id="tokenStr" value="${queryForm.token}" name="queryForm.token" style="width:150px" class="formCss"/>
       			   </td>
       			   <td width="64" align="right"><view:LanguageTag key='tkn_comm_state_bind'/><view:LanguageTag key="colon"/></td>
       			   <td width="150" align="left">
             			<select id="bindState" name="queryForm.bindState" value="${queryForm.bindState}" class="select100" style="width:150px" >
			              <option value="-1" <c:if test='${queryForm.bindState == -1}'> selected</c:if>><view:LanguageTag key="common_syntax_whole"/></option>
						  <option value="1"  <c:if test='${queryForm.bindState == 1}'> selected</c:if>><view:LanguageTag key="tkn_state_bound"/></option>
						  <option value="0"  <c:if test='${queryForm.bindState == 0}'> selected</c:if>selected><view:LanguageTag key="tkn_state_unbound"/></option>
					    </select>
       			   </td>
       			   <td width="64" align="right"><view:LanguageTag key="tkn_comm_physical_type"/><view:LanguageTag key="colon"/></td>
	               <td width="150"><select id="pType" name="queryForm.physicaltype" value="${queryForm.physicaltype}" class="select100" style="width:150px">
					  <option value="-1" <c:if test='${queryForm.physicaltype==-1}'> selected</c:if>><view:LanguageTag key="common_syntax_whole"/></option>
					  <option value="0"  <c:if test='${queryForm.physicaltype == 0}'> selected</c:if>><view:LanguageTag key="tkn_physical_hard"/></option>
					  <option value="1"  <c:if test='${queryForm.physicaltype == 1}'> selected</c:if>><view:LanguageTag key="tkn_physical_mobile"/></option>
					  <option value="2"  <c:if test='${queryForm.physicaltype == 2}'> selected</c:if>><view:LanguageTag key="tkn_physical_soft"/></option>
					  <option value="6"  <c:if test='${queryForm.physicaltype == 6}'> selected</c:if>><view:LanguageTag key="tkn_physical_sms"/></option>
				   </select></td>
       			   <td width="100">
       			   		<span style="float:left">
       			   			<a href="#" onClick="query()" class="button"><span><view:LanguageTag key="common_syntax_query"/></span></a>
       			   		</span>
       			   </td>
       		</table>	   
       		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="operTableBg">
	         	<tr>
	   				<td>
	  			   		<span style="float:right">		
			       			<span><a href="#" onClick="goBack();" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a></span>
			       			<span><a href="#" onClick="exchangess();" class="button"><span><view:LanguageTag key="tkn_sel_oper_update_tkn"/></span></a></span>
			       		</span>
			       	</td>
   				</tr>
      	 	</table>	   
        </td>
      </tr>
      <tr>
        <td id="listDataAJAX" width="70%"></td>
      </tr>
    </table>
  </form>
  </body>
</html>