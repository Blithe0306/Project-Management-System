<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	request.setCharacterEncoding("UTF-8");
	String path = request.getContextPath(); 
	String projId = request.getParameter("projId");
	projId = java.net.URLDecoder.decode(projId, "UTF-8");
	String ptype = request.getParameter("ptype");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
<script language="javascript" src="<%=path%>/manager/common/js/checkall.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
<script language="javascript" type="text/javascript">
		
		
	<!--
	$(function() {
		getExistUsers();
	});
	
	//取得已经存在的派单监视用户
	function getExistUsers(){
		var projId = $("#projId").val();
		var url = '<%=path%>/manager/notice/customer/customerUserAction!init.action';
		$.ajax({
			type: "POST",
		    url: url,
		    async: true,	    
		    data: {"customerUser.projectId":projId},
		    dataType: "json",	    
		    success: function(msg){
				viewUsers(msg);
		    }
		});
	}
	
	//处理已经存在的派单用户数据
	function viewUsers(data) {
    	//列表数据
    	var lis = "";
    	if(data.items.length > 0){
			$(data.items).each(function(i, ite){
				ite.projectId = encodeURI(encodeURI(ite.projectId));
		    	//遍历JSON数据得到所需形式
		        lis += ite.realname;
		        lis += "&nbsp;&nbsp;[<a href=javascript:delObj('" + ite.userid + "',\'"+ite.projectId+"\')>";
		        lis += "删 除";
		        if((i+1) % 4 == 0){
		        	lis += "</a>]&nbsp;&nbsp;&nbsp;&nbsp;<br/>";
			    }else{
			    	lis += "</a>]&nbsp;&nbsp;&nbsp;&nbsp;";
			    }
			})
        }
        $("#userNameDiv").html(lis);
    }
	
	function selUsers(){
		FT.openWinMiddle('选择监视人', '<%=path%>/manager/project/sel_user_notice.jsp', true);
	}
	
	//添加监视用户操作
	function addObj(usersIds,email){
		var projId = $('#projId').val();
		var ptype = $('#ptype').val();
		
		var url = "<%=path%>/manager/notice/customer/customerUserAction!add.action";
		$.ajax({
			type: "POST",
		    url: url,
		    async: true,
		    data: {"customerUser.userid":usersIds, "customerUser.userEmail":email,"customerUser.projectId":projId,"ptype":ptype},
		    dataType: "json",	    
		    success: function(msg){
		    	var errorStr = msg.errorStr;
		    	if('success' == errorStr){
					getExistUsers();
				}else{
					FT.toAlert(errorStr, msg.object, null);
				}
			}
		});
	}
	
	//删除监视用户
	function delObj(userid, projId){
	 	projId = decodeURI(decodeURI(projId));
		var url = "<%=path%>/manager/notice/customer/customerUserAction!delete.action";
		$.ajax({
			type: "POST",
		    url: url,
		    async: true,
		    data: {"customerUser.userid":userid,"customerUser.projectId":projId},
		    dataType: "json",	    
		    success: function(msg){
				var errorStr = msg.errorStr;
		    	if('success' == errorStr){
					getExistUsers();
				}else{
					FT.toAlert(errorStr, msg.object, null);
				}
			}
		});
	}
	//-->
</script>
</head>
<body style="overflow:auto; overflow-x:hidden">
<input type="hidden" id='projId' name='projId' value="<%=projId%>"/>
<input type="hidden" id='ptype' name='ptype' value="<%=ptype%>"/>

  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="titleBorder">
    <tr>
      <td><span class="text_Lan_Se">
        <view:LanguageTag key='common_syntax_0'/>
        </span> <span class="text_Hui_Se">
        	&nbsp;&nbsp;定制管理
        </span><img src="<%=path%>/images/manager/jt01.gif" width="11" height="7" hspace="5"/> <span class="text_Hui_Se">监视人列表</span></td>
    </tr>
  </table>
  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tableBorder">
    <tr>
      <td colspan="3" align="center" class="titleBorderTrBg">定制管理-监视人管理</td>
    </tr>
    <tr>
      <td align="right" width="38%"></td>
      <td width="62%" colspan="2"></td>
    </tr>
    <tr>
      <td align="right">&nbsp;</td>
      <td  colspan="2">&nbsp;</td>       
    </tr>
    <tr>
      <td align="right">定制监视人：</td>
      <td  colspan="2"><div id="userNameDiv"></div></td>      
    </tr>
    <tr>
      <td align="right">&nbsp;</td>
      <td  colspan="2">&nbsp;</td>      
    </tr>
     <tr>
      <td  colspan="3" height="20">&nbsp;</td>      
    </tr>
  </table>
  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="bottomTableCss">
	<tr>
	  <td align="center"><span class="Button"><a href="javascript:selUsers();">添 加</a></span> &nbsp;&nbsp;
	  <span class="Button"><a href="javascript:history.go(-1)"><span>返 回</span></a></span>			</td>
	</tr>
  </table>
</body>
</html>
