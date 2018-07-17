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
	<script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script>
	<script language="javascript">
		function changeOrg(){
		var oldOrgunitIds=$("#orgunitIdsTemp").val();
		var neworgname = $("#orgunitNamesTemp").val();
		if(oldOrgunitIds!=''){		
			 $("#AddForm").ajaxSubmit({
			async:false,
			data: {"neworgname" : neworgname},    
			dataType : "json",  
			type:"POST", 
			url : contextPath + "/manager/orgunit/orgunit/orgunitInfo!moveOrgunit.action",
			success:function(msg){
			     if(msg.errorStr == 'success'){			     	
			     	 $.ligerDialog.success(msg.object.split(",")[0], getLangVal('common_syntax_tip',contextPath), function(){
			     		//跳转
			     		// window.location="<%=path%>/manager/orgunit/orgunit/orgunitInfo!view.action?treeOrgunitInfo.id="+(msg.object.split(",")[1])+"&treeOrgunitInfo.flag="+(msg.object.split(",")[2])+"&treeOrgunitInfo.readWriteFlag="+(msg.object.split(",")[3]);
			     		
			     		//完之后 调用initOrgunitTree.jsp左边测显示 展示效果
		       		 	var frames=window.parent.window.parent.window.document.getElementById("leftFunction");
			         	frames.contentWindow.reLoadTree();
	            	 });
			     	 
			     }else{
				     FT.toAlert(msg.errorStr,msg.object,null);
				     }
				}
			});
		}else{
			FT.toAlert("warn",'<view:LanguageTag key="org_please_sel"/>',null);
			return false;
		}
	  }

	 /**
 	  * 返回
	 */
	function goBack() {
	    var readWriteFlag = $("#readWriteFlag").val();
	    var flag = $("#flag").val();
	    var id = $("#id").val();
	    if(id == ""){
	    	id = $("#parentid").val();
	    }
  		location.href = contextPath + '/manager/orgunit/orgunit/action/orgunitInfo!view.action?treeOrgunitInfo.id='+id+"&treeOrgunitInfo.flag="+flag+"&treeOrgunitInfo.readWriteFlag="+readWriteFlag;
	}	
	</script> 
  </head>
  
  <body>
    <input id="contextPath" type="hidden" value="<%=path%>" />
    <form name="AddForm" id="AddForm" method="post" action="">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td>  
		  <span class="topTableBgText">
		  	  <view:LanguageTag key="org_move"/>
		  </span>
        </td>
      </tr>
    </table> 
     <table width="100%" border="0"  cellpadding="0" cellspacing="0">
      <tr>
        <td valign="top">
			<input type="hidden"  id="parentid" name="treeOrgunitInfo.parentId" value="${treeOrgunitInfo.parentOrgunitInfo.id}" class="formCss100"/>
	       	<input type="hidden"  name="treeOrgunitInfo.domainId" value="${treeOrgunitInfo.domainId}" class="formCss100" readonly/>
	        <input type="hidden"  id="id" name="treeOrgunitInfo.id" value="${treeOrgunitInfo.id}" class="formCss100"/>
            <input type="hidden"  name="treeOrgunitInfo.mark"  value="${treeOrgunitInfo.mark}"/>
            <input type="hidden"  name="treeOrgunitInfo.name"  value="${treeOrgunitInfo.name}"/>
        	<input type="hidden"  name="treeOrgunitInfo.descp"  value="${treeOrgunitInfo.descp}"/>
        	<input type="hidden"  id="readWriteFlag" name="treeOrgunitInfo.readWriteFlag"  value="${treeOrgunitInfo.readWriteFlag}"/>
        	<input type="hidden"  id="flag" name="treeOrgunitInfo.flag"  value="${treeOrgunitInfo.flag}"/>
		    <table width="100%" border="0" cellpadding="10" cellspacing="10" class="ulOnInsideTable">
		    <tr>
              <td align="right"><view:LanguageTag key="org_name"/><view:LanguageTag key="colon"/></td>
              <td>	<input  type=hidden id="domaind" value="${treeOrgunitInfo.domainId}"/>
              		${treeOrgunitInfo.name}	 
	       	   </td>
			   <td class="divTipCss"><div id="" style="width:100%"></div></td>
			</tr>
		    <tr>
              <td align="right"><view:LanguageTag key="org_new_parentorg"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
              <td>	
              		<input type=hidden id="orgunitIds" value=""/>
              		<input type=hidden id="orgunitIdsTemp" name="newDOrgunitId"  value=""/>
              		<input type=text id="orgunitNamesTemp" name="newDOrgunitName"  onClick="selOrgunits(4,'<%=path%>');"   class="formCss100"  readonly />		 
	       	   </td>
			   <td class="divTipCss"><div id="orgunitNamesTip" style="width:100%"></div></td>
			</tr>
			<!-- 提示 多用户绑定令牌 是否解绑 -->
			<tr>
		        <td width="25%" align="right"></td>
		        <td width="30%">
		            <input type=checkbox id=moveChildTag name=moveChildTag value=1 checked /><view:LanguageTag key="org_take_away_childorg"/>
		        	<input type=checkbox id=moveUserTokenTag name=moveUserTokenTag value=1 checked /><view:LanguageTag key="org_take_away_user_tkn"/></td>
		        <td width="45%"></td>
		    </tr>				
		    <tr>
		        <td align="right"> </td>
		        <td>
		        	<a href="#" onClick="changeOrg();" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
		        	<a href="javascript:goBack();" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a>
		        </td>
		        <td>&nbsp;</td>
	        </tr>
		    </table>
	    </td>
	</tr>
	<tr>
	 <td>
	 </td>
	</tr>
	</table>
	 
  </form>
 
  </body>
</html>