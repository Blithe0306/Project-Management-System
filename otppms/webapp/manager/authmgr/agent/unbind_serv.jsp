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
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
    
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
   	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
    
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>	
	<script language="javascript" src="<%=path%>/manager/common/js/checkall.js"></script>
 
	<script language="javascript" type="text/javascript">
	<!--	
	//解除绑定
	function okClick(item,win,index){
		var curpage = $('#curPage').val();
		var ipAddir = '${agentInfo.agentipaddr}';
		var form = document.forms['ListForm'];
		if (!checkAll(form)){
  			FT.toAlert('warn','<view:LanguageTag key="auth_agent_sel_unbind_ser"/>', null);
		} else {
	        $.ligerDialog.confirm('<view:LanguageTag key="auth_agent_ser_confirm_unbind"/>', function (yes){
		     if(yes){
		     $("#ListForm").ajaxSubmit({
					   async:false,
					   type:"POST",
					   dataType : "json",
					   url : "<%=path%>/manager/authmgr/server/authAgent!unBindAS.action?agentInfo.agentipaddr="+ipAddir+"&curPage="+curpage,
					   success:function(result){
							if(result.errorStr == 'success'){	            			
		            			$.ligerDialog.success(result.object, '<view:LanguageTag key="common_syntax_tip"/>', function(){
		            				parent.query(true);
		            				if(win) win.close();
		            			});
			            	}else {
			            		FT.toAlert(result.errorStr, result.object, null);
			            	}
						}
					});	
		     }
           });
		}
	}
	//-->
	</script>
  </head>
  
 <body scroll="no" style="overflow:auto; overflow-x:hidden">
 <form name="ListForm" id="ListForm" method="post" action="">
    <table width="99%" border="0" align="right" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
    	<ul id="menu">
		 <li class="tabFocus"><view:LanguageTag key="auth_ser_unbind"/></li>
	    </ul>
	    <ul id="content">
		 <li class="conFocus">
		 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
		 	<c:if test="${empty agentInfo.hostIps}">
             <tr>
               <td colspan="6" align="center" valign="top"><view:LanguageTag key="common_syntax_nothing"/></td>
              </tr>
		    </c:if>
		    <c:if test="${not empty agentInfo.hostIps}">
             <tr>
               <td width="6%"><input type="checkbox" name="check_all" onClick="selectAll(this, 'key_id')" title='<view:LanguageTag key="common_syntax_sel_or_unsel_all"/>' /></td>
        	   <td width="94%"><view:LanguageTag key="auth_ser_hostip"/></td>
             </tr>
			 <c:forEach items="${agentInfo.hostIps}" var="hostkey">
		     <tr>
		        <td><input type="checkbox" name="key_id" value="${hostkey.hostipaddr}" /></td>
		        <td>${hostkey.hostipaddr}</td>
		     </tr>
		     </c:forEach> 
             <tr>
               <td colspan="6">
               <input type="hidden" name="curPage" id = "curPage" value="${param.curPage}"/>
               <!--<a href="#" onClick="javascript:unBindServ('${agentInfo.agentipaddr}')" class="button"><span>解 绑</span></a>  --></td>
             </tr>
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