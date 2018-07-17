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
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=path%>/manager/customer/js/list.js"></script>
	<script language="javascript" type="text/javascript">
		
		//权限
		var permAdd = '';  //添加
		var permEdit = ''; //编辑
		var permGoPrjinfo = ''; //编辑
		var permGoPrjAdd = ''; //编辑
		var syntax_detail_lang = '<view:LanguageTag key="common_syntax_detail_info"/>';
		var syntax_confirm_lang = '<view:LanguageTag key="common_syntax_confirm"/>';
		
		$(document).ready(function() {
			permAdd = '<view:AdminPermTag key="020001" path="<%=path%>" langKey="admin_info_add" type="1" />';
			permEdit = '<view:AdminPermTag key="020003" path="<%=path%>" langKey="common_syntax_edit" type="1" />';
			permGoPrjinfo = '<view:AdminPermTag key="020005" path="<%=path%>" langKey="adm_perm_020005" type="1" />';
			permGoPrjAdd = '<view:AdminPermTag key="020006" path="<%=path%>" langKey="adm_perm_020006" type="1" />';
			
			var currDept = $('#dept').val();
			$.ajax({
				async:true,
				type:"POST",
				url:contextPath + "/manager/customer/custInfo!getDepts.action",
				dataType:"String",
				success:function(msg){
					var selAttr = "";
					if(msg.length != 0){
						var baseVer = msg.split(",");
						selAttr = "<select id='dept' name='custInfo.dept' class='select100' onchange='javascript:inputTypeVer(this)' value='${custInfo.dept}' >";
						selAttr = selAttr+"<option value=''>全部</option>"
						for (var i=0;i<baseVer.length;i++)
						{
							selAttr = selAttr + "<option value='" + baseVer[i].split(";")[0] + "'"; 
							if(currDept == baseVer[i].split(";")[0]){
								selAttr = selAttr + " selected";
								$('#dept').val(baseVer[i].split(";")[0]);
							}
							selAttr = selAttr + ">" + baseVer[i].split(";")[1] + "</option>";
						}
						selAttr = selAttr + "</select>";
					}
					document.getElementById("selDeptsDIV").innerHTML=selAttr;
				}
			});
		});
		function inputTypeVer(obj){
			$('#dept').val(obj.value);
		}
	</script>
  </head>
  <body scroll="no" style="overflow:hidden">
  <input id="contextPath" 	type="hidden"   value="<%=path%>" />
  <input id="l_userid"    	type="hidden" 	value="${curLoginUser}" />
  <input id="l_userid_role" type="hidden" 	value="${curLoginUserRole}" />
  <input id="curPage"     	type="hidden"   value="${param.cPage}" />

  <div id="msgShow" class="msgDiv"><span class="msg"></span></div>
  <form name="ListForm" id="ListForm" method="post" action="">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="61%">
		<table width="800" border="0" cellspacing="0" cellpadding="0" style="margin-top:6px; margin-bottom:6px">
	      <tr>
	      	<td width="116" align="right">客户名称<view:LanguageTag key="colon"/></td>
	        <td width="168"><input type="text" id="custname" name="custInfo.custname" value="${custInfo.custname}" class="formCss100"/></td>
	        <td width="116" align="right">客户编号<view:LanguageTag key="colon"/></td>
	        <td width="168"><input type="text" id="custid" name="custInfo.custid" value="${custInfo.custid}" class="formCss100"/></td>
	        <td width="116" align="right">所属部门<view:LanguageTag key="colon"/></td>
	        <td width="168">
	        	<input type="hidden" id="dept" name="custInfo.dept" class="formCss100"  value="${custInfo.dept}"/>
	        	<div id="selDeptsDIV"></div>
	        </td>
	        <td width="116" align="right">&nbsp;</td>
	        <td><span style="display:inline-block" class="query-button-css"><a href="javascript:query(false,true);" class="isLink_LanSe"><view:LanguageTag key="common_syntax_query"/></a></span></td>
	      </tr>
	    </table>
		</td>
	    <td width="39%" align="right" valign="top"><a href="javascript:addAdmPermCode('020001','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" hspace="6" vspace="6" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a></td>
	  </tr>
	</table>
    
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="operTableBg">
      <tr>
      	<td width="5">&nbsp;&nbsp;</td>
        <td>
		    <view:AdminPermTag key="020004" path="<%=path%>" langKey="common_syntax_delete" type="2" />
        </td>
      </tr>
    </table>     
  </form>
 <div id="listDataAJAX"></div>
  </body>
</html>