<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="view" uri="/WEB-INF/tld/view.tld"%>
<%
    String path = request.getContextPath();
%>
<html>
	<head>
	<title></title>
	<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css" />
	<link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css" />
	<link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" id="openwincss" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"/>	
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

	<script language="javascript" type="text/javascript">
	 $(function() {
	 
	 	//绑定点击事件
		domClickEvent();
		//点击按钮弹出窗口，设置窗口样式，取消已绑定点击事件
		var cssurl = "<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"
        btnClickEvent("#save",cssurl);
	 
	 	if("oracle" == '${dbConfInfo.dbtype}'){
			if("y" == '${dbConfInfo.dual}') {
				$("tr[id='racTR']").show();
				$("tr[id='racnameTR']").show();
				$("tr[id='ip2TR']").show();
				$("tr[id='dbnameTR']").hide();
				$("tr[id='sIdTR']").hide();
				document.getElementById("IPText").innerHTML = '<view:LanguageTag key="dbconf_ip_1"/>';
			}else {
				$("tr[id='racTR']").hide();
				$("tr[id='racnameTR']").hide();
				$("tr[id='ip2TR']").hide();
				$("tr[id='dbnameTR']").hide();
				$("tr[id='sIdTR']").show();
				document.getElementById("IPText").innerHTML = '<view:LanguageTag key="dbconf_ip"/>';
			}
		}else {
			$("tr[id='racTR']").hide();
			$("tr[id='dbnameTR']").show();
			$("tr[id='sIdTR']").hide();
			$("tr[id='racnameTR']").hide();
			$("tr[id='ip2TR']").hide();
			document.getElementById("IPText").innerHTML = '<view:LanguageTag key="dbconf_ip"/>';
		}
			
	});
	 
	//重新初始化数据库配置 
	function initDBConf(){
		$.ligerDialog.confirm('<view:LanguageTag key="dbconf_conf_replace_db_tip"/>','<view:LanguageTag key="common_syntax_confirm"/>',function(yes){
			if(yes){
				window.parent.location.href = "<%=path%>/logout!logout.action?topage=init";
			}
		});		
	}
	
</script>
</head>
<body>
 <form id="DBForm" method="post" action="">
	<table width="100%" border="0"  cellpadding="0" cellspacing="0" class="topTableBg">
      <tr>
        <td width="98%"><span class="topTableBgText"><view:LanguageTag key="common_menu_config_db"/></span></td>
        <td width="2%" align="right">
      	 <!--	<a href="javascript:openHelpWin('<%=path%>/manager/help/confighelp.html#385','<%=path%>/manager/common/js/ligerUI/skins/Gray/css/dialog.css');">
      		<img src="<%=path%>/images/icon/help_1.png" width="16" height="16" hspace="5" title='<view:LanguageTag key="common_syntax_help"/>' align="absmiddle" />
      		</a>-->
        </td>
      </tr>
    </table>  
 	<table width="100%" border="0"  cellpadding="0" cellspacing="0" class="ulOnInsideTable">
      <tr>
        <td valign="top">
          <table width="100%" border="0" cellpadding="5" cellspacing="0" class="ulOnInsideTable">
			<tr>
				<td width="30%" align="right"><view:LanguageTag key="dbconf_type"/><view:LanguageTag key="colon"/></td>
				<td>  
					  <c:choose> 
		        	  	<c:when test="${dbConfInfo.dbtype eq 'mysql'}">MySQL</c:when>
		        	  	<c:when test="${dbConfInfo.dbtype eq 'oracle'}">Oracle</c:when>
		        	  	<c:when test="${dbConfInfo.dbtype eq 'sqlserver'}">SQLServer</c:when>
		        	  	<c:when test="${dbConfInfo.dbtype eq 'db2'}">DB2</c:when>
		        	  	<c:when test="${dbConfInfo.dbtype eq 'postgresql'}">PostgreSQL</c:when>
		        	  	<c:when test="${dbConfInfo.dbtype eq 'sybase'}">SyBase</c:when>
		        	  </c:choose>
				</td>
			  </tr>
			<tr id="racTR">
			   	<td>&nbsp;</td>
			    <td align="left"><view:LanguageTag key="dbconf_rac_enabled_mode"/></td>
		      </tr>
			<tr id="dbnameTR">
				<td align="right"><view:LanguageTag key="dbconf_dbname"/><view:LanguageTag key="colon"/></td>
				<td>${dbConfInfo.dbname}</td>
			  </tr>
			<tr id="sIdTR">
			    <td align="right">SID：</td>
			    <td align="left">${dbConfInfo.dbname}</td>
		      </tr>
			<tr id="racnameTR">
			    <td align="right"><view:LanguageTag key="dbconf_rac_name"/><view:LanguageTag key="colon"/></td>
			    <td align="left">${dbConfInfo.dbname}</td>
		      </tr>
			<tr>
				<td align="right" ><span id="IPText">&nbsp;</span><view:LanguageTag key="colon"/></td>
				<td>${dbConfInfo.ip}</td>
			  </tr>
			<tr id="ip2TR">
			    <td align="right"><view:LanguageTag key="dbconf_ip_2"/><view:LanguageTag key="colon"/></td>
			    <td align="left">${dbConfInfo.viceip}</td>
		      </tr>
			<tr>
				<td align="right"><view:LanguageTag key="dbconf_port"/><view:LanguageTag key="colon"/></td>
				<td>${dbConfInfo.port}</td>
			  </tr>
			<tr>
				<td align="right"><view:LanguageTag key="dbconf_username"/><view:LanguageTag key="colon"/></td>
				<td>${dbConfInfo.username}</td>
			  </tr>
			<tr>
				<td height="-2" align="right" bgcolor="#FFFFFF"><view:LanguageTag key="dbconf_dbconn_result"/><view:LanguageTag key="colon"/></td>
      			<td align="left" bgcolor="#FFFFFF"><span style="color:green"><view:LanguageTag key="dbconf_dbconn_succ"/></span>  </td>
			</tr>
			<tr>
				<td rowspan="2" align="right" ></td>
				<td><span class="text_Hong_Se"><br/><view:LanguageTag key="dbconf_replace_db_tip"/></span></td> 
			  </tr>
			<tr>
			  <td><a href="javascript:initDBConf();" id="save" class="button"><span><view:LanguageTag key="dbconf_again_conf_dbconn"/></span></a></td>
			  </tr>
		 </table>
	  </td>
	 </tr>
   </table>
  </form>
</body>
</html>