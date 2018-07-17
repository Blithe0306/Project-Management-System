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
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/easyui/easyui.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	
	<script language="javascript" type="text/javascript">
	<!--
	$(function() {
    	var userIdStr = '${html_page_obj}';   
        var userIdStrs = userIdStr.split(','); 
        var userIdHtml = "";
        for(var i=0;i<userIdStrs.length;i++){
        	userIdHtml = userIdHtml+userIdStrs[i]+"</br>";
        }
        $('#roleDiv').html(userIdHtml);    
	})
      
	//变更创建人
	function designate(win){
       var roleNameStr = encodeURI(encodeURI('${html_page_obj}'));
       var roleIdSrt = '${html_obj}';
       var user = $('#adminid').val();
       var cpage = ${param.curPage};
       if(cpage==''||cpage==null){
          cpage=1;
       }
       if(user==''||user==null){
         FT.toAlert('warn','<view:LanguageTag key="admin_vd_sel_designee"/>',null);
          return false;
       }
         $("#createrForm").ajaxSubmit({
			async:false,    
			dataType : "json",  
			type:"POST", 
			url : 'manager/admin/role/adminRole!designate.action?roleNameStr='+roleNameStr+'&&adminId='+user+'&&cPage='+cpage+'&&roleIdSrt='+roleIdSrt,
			success:function(msg){
			     if(msg.errorStr == 'success'){
			    	 parent.changeClose();
			     }else if(msg.errorStr == 'error'){
			     	 FT.toAlert('warn',msg.object,null);
				 }else{
			        FT.toAlert('warn','<view:LanguageTag key="admin_replace_creator_err"/>',null);
			     }
			   
			}
		});
      }
      
      function okClick(btn,win,index){
         designate(win);
       
      }
	//-->
	</script>
  </head>
 <body>
<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnTable">
  <tr>
    <td valign="top">
      <ul id="menu">
        <li class="tabFocus">
          <view:LanguageTag key="admin_change_creator"/>
        </li>
      </ul>
      <ul id="content">
        <li  class="conFocus">
         <form id="createrForm" name="createrForm" action="">
          <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
            <tr>
              <td width="100%" colspan="3" height="15"></td>
              </tr>
            <tr>
              <td width="20%" align="right" valign="top">
                <view:LanguageTag key="role_change"/><view:LanguageTag key="colon"/>
              </td>
              <td width="20%"  align="left" valign="top">
               <div id="roleDiv"></div>
              </td>
              <td width="20%"  align="right" valign="top"><view:LanguageTag key="admin_designee"/><view:LanguageTag key="colon"/></td>
              <td width="40%"  align="left" valign="top">
                  <select id="adminid" name="adminUser.adminid" style="width:120px;">
                       <option value=""><view:LanguageTag key="admin_sel_designee"/></option>
                    <c:forEach items="${html_page_list}" var="admuser">
                       <option value="${admuser.adminid}">${admuser.adminid}</option>
                    </c:forEach>
                  </select>
              </td>
              </tr>
          </table>
            </form> 
        </li>
      </ul>
    </td>
  </tr>
</table>

  </body>
</html>
