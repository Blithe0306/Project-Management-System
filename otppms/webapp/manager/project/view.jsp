<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" escape="false"/>
    <title></title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
	<link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/ligerUI/skins/Aqua/css/ligerui-alert.css" rel="stylesheet" type="text/css" />
 
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
    <script language="javascript" type="text/javascript">
	<!--
		$(function() {
			$("#menu li").each(function(index) { //带参数遍历各个选项卡
				$(this).click(function() { //注册每个选卡的单击事件
					$("#menu li.tabFocus").removeClass("tabFocus"); //移除已选中的样式
					$(this).addClass("tabFocus"); //增加当前选中项的样式
						//显示选项卡对应的内容并隐藏未被选中的内容
					$("#content li:eq(" + index + ")").show()
	                    .siblings().hide();
	                });
           	});
           	
           	//为了解决chrome 浏览器权限页签显示不出来问题 焦点必须先选中权限页面 然后再调整到基本信息页面
           	window.setTimeout(function(){
           	    //等权限树初始化了再跳到第一个页签页面
           		$("#menu li.tabFocus").removeClass("tabFocus"); //移除已选中的样式
           		$("#menu li:eq(0)").addClass("tabFocus"); //增加当前选中项的样式
           		$("#content li:eq(0)").show()
	                    .siblings().hide();
           	},10);
        
        });
	//-->
	</script>
 
  </head>
 <body style="overflow:auto; overflow-y:hidden">
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
        <ul id="content">
		 <li>
		 <div style="height: 560px;overflow:scroll;" >
		 <table style="overflow:scroll;" width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
             <tr>
               <td width="30%" align="right" valign="top">项目名称<view:LanguageTag key="colon"/></td>
               <td width="40%">${project.prjname}</td>
               <td width="30%">&nbsp;</td>
             </tr>
             <tr>
               <td align="right" valign="top">项目编号<view:LanguageTag key="colon"/></td>
               <td>${project.prjid}</td>
               <td>&nbsp;</td>
             </tr>
              <tr>
               <td align="right" valign="top">客户名称<view:LanguageTag key="colon"/></td>
               <td>${project.custname}</td>
               <td>&nbsp;</td>
             </tr>
              <tr>
               <td align="right" valign="top">定制类型<view:LanguageTag key="colon"/></td>
               <td>${project.typeStr}</td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td align="right" valign="top">基础版本<view:LanguageTag key="colon"/></td>
               <td>${project.typeversion}</td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td align="right" valign="top">是否收费<view:LanguageTag key="colon"/></td>
               <td>${project.ifpayStr}</td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td align="right">项目状态<view:LanguageTag key="colon"/></td>
               <td>
               	<c:if test="${project.prjstate != ''}">
               		<c:choose>
						<c:when test="${project.prjstate == 0}">
						新立项
						</c:when>
						<c:when test="${project.prjstate == 1}">
						 需求
						</c:when>
						<c:when test="${project.prjstate == 2}">
						 设计
						</c:when>
						<c:when test="${project.prjstate == 3}">
						 开发
						</c:when>
						<c:when test="${project.prjstate == 4}">
						 测试
						</c:when>
						<c:when test="${project.prjstate == 5}">
						 完成
						</c:when>
						<c:when test="${project.prjstate == 6}">
						 反馈
						</c:when>
					</c:choose>
               	</c:if>
               </td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td align="right" valign="top">需求部门<view:LanguageTag key="colon"/></td>
               <td>${project.needdept}</td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td align="right" valign="top">SVN路径<view:LanguageTag key="colon"/></td>
               <td>${project.svn}</td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td align="right" valign="top">BUG号<view:LanguageTag key="colon"/></td>
               <td>${project.bug}</td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td align="right" valign="top">销售人员及联系方式<view:LanguageTag key="colon"/></td>
               <td>${project.sales}</td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td align="right" valign="top">技术支持及联系方式<view:LanguageTag key="colon"/></td>
               <td>${project.techsupport}</td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td align="right" valign="top">详细描述<view:LanguageTag key="colon"/></td>
               <td>${project.prjdesc}</td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td align="right" valign="top">添加人<view:LanguageTag key="colon"/></td>
               <td>${project.operator}</td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td align="right" valign="top">添加时间<view:LanguageTag key="colon"/></td>
               <td>${project.createtimeStr}</td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td align="right" valign="top">更新时间<view:LanguageTag key="colon"/></td>
               <td>${project.updatetimeStr}</td>
               <td>&nbsp;</td>
             </tr>
             
           </table>
           </div>
           </li> 
       </ul>
		</td>
      </tr>
    </table>
 </body>
</html>