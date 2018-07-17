<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.ft.otp.common.Constant" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
	String loginUser = (String)request.getSession(true).getAttribute(Constant.CUR_LOGINUSER);
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
	<link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/ligerUI/skins/Aqua/css/ligerui-alert.css" rel="stylesheet" type="text/css" />
    <style>
			form {
				margin: 0;
			}
			textarea {
				display: block;
			}
		</style>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>  
   	<script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script>
   	<script charset="utf-8" src="<%=path %>/kindeditor/kindeditor-min.js"></script>
	<script charset="utf-8" src="<%=path %>/kindeditor/lang/zh_CN.js"></script>
    <script language="javascript" type="text/javascript">
		var results;
		var results2;
		var contextPath ;
		$(function() {
        	contextPath ='<%=path%>';
        });
        
        function addObj(oper){
			var url = contextPath+"/manager/project/projectAction!addProjectResult.action";
		    var sumry = results.html();
			$("#AddForm").ajaxSubmit({
				async:false,    
				dataType : "json",  
				type:"POST", 
				url : url,
				data:{"projectResult.results": sumry},
				success:function(msg){
					var myDate = new Date();
					var curDate = myDate.getFullYear()+"-"+(myDate.getMonth()+1)+"-"+myDate.getDate()
							+" "+myDate.getHours()+":"+myDate.getMinutes()+":"+myDate.getSeconds();
					//提示"是否继续添加管理员"，选是，添加成功，停留在添加界面允许继续添加，选否的话则关掉界面
			    	if(msg.errorStr == 'success'){
			    		//将增加table显示项目总结
			    		var resultForm ="<form name='editForm"+msg.object+"' id='editForm"+msg.object+"' method='post' action=''>";
			    		var resultTab1 = "<table align='center' id='resultTab"+msg.object+"' border='0' style='overflow:scroll;' width='40%' class='ulOnInsideTable'>";
						var resultTabTitle1 = "<tr><td width='20%' align='right'>操作人：</td><td width='30%'><%=loginUser%></td>";
						var resultTabTitle2 = "<td width='20%' align='right'>创建时间：</td><td width='30%'>"+curDate+"</td></tr>";
						var results = "<tr><td width='20%' align='right' valign='top'>项目总结：</td>";			    		
		               	var resultTab2 = "<td width='80%' colspan='3'><span id='resultSpan"+msg.object+"'>"+ sumry + "</span><br/>";
		               	var resultTab2_1 = "<input type='hidden' name='projectResult.id' value='"+msg.object+"'/><input type='hidden' name='projectResult.prjid' value='${project.id}'/>";
		               	//var resultTab2_2 = "<textarea style='display:none; width: 475px;' id='resultTextarea"+msg.object+"' name='projectResult.results' rows='10'>"+$('#projresult').val()+"</textarea>"
		               	//var resultTab2_2 = "<textarea style=' width:700px;height:200px; visibility:hidden;' id='resultTextarea"+msg.object+"' name='results3'>"+$('#projresult').val()+"</textarea>"
		               	var resultTab2_3 = "<br/><a href='javascript:void(0);' style='display:none' id='editBtn"+msg.object+"' onclick='editObj("+msg.object+",1)'>保存</a>";
		               	var resultTab2_4 = "<a href='javascript:void(0);' id='showBtn"+msg.object+"' onclick='editObj("+msg.object+",0)'>编辑</a>&nbsp;&nbsp;";
		               	var resultTab3 = "<a href='#' id='delBtn' onclick='returnObj("+msg.object+");'>关闭</a>";
		                var resultTab4 = "</td></tr></table></form>";
		                var resultContent = resultForm+resultTab1 +resultTabTitle1+resultTabTitle2+ results+
		                					resultTab2+resultTab2_1+resultTab2_3+resultTab2_4+resultTab3+resultTab4;
			    		$('#resultsDIV').append(resultContent);
			    		window.location.reload();
			    	}else{
			    		//FT.toAlert(msg.errorStr,msg.object,null);
			    		alert('添加失败！');
			    	}
				}
			});
		}
		//删除项目总结
		function deleteObj(id){
			var r=confirm("确定删除项目总结？");
			 if (r==true){
			 	var url = contextPath+"/manager/project/projectAction!deleteProjectResult.action?projectResult.id="+id;
				$("#AddForm").ajaxSubmit({
					async:false,    
					dataType : "json",  
					type:"POST", 
					url : url,
					success:function(msg){
						//提示"是否继续添加管理员"，选是，添加成功，停留在添加界面允许继续添加，选否的话则关掉界面
				    	if(msg.errorStr == 'success'){
				    		$('#resultTab'+id).remove();
				    		$('#resultHr'+id).remove();
				    	}else{
				    		alert('删除失败！');
				    	}
					}
				});
			 }
		}
		//关闭
		function returnObj(id){
			window.location.reload();
		}
		//编辑
		function editObj(id,flag,resultsId){
			if(flag==0){	//弹出textarea
				$('#resultTextarea'+id).show();
				$('#editBtn'+id).show();
				$('#showBtn'+id).hide();
				$('#AddForm').hide();
				$('#resultsdiv'+id).show();
				$('#returndiv'+id).show();
				$('#deldiv'+id).hide();
			}else{
				var sumry2 = $('#'+resultsId).val();
				var url = contextPath+"/manager/project/projectAction!modifyProjectResult.action";
				$("#editForm"+id).ajaxSubmit({
					async:false,
					dataType : "json",  
					type:"POST", 
					url : url,
					data:{"projectResult.results": sumry2},
					success:function(msg){
						//提示"是否继续添加管理员"，选是，添加成功，停留在添加界面允许继续添加，选否的话则关掉界面
				    	if(msg.errorStr == 'success'){
				    		$('#resultTextarea'+id).hide();
				    		$('#editBtn'+id).hide();
				    		$('#showBtn'+id).show();
				    		$('#resultSpan'+id).html($('#resultTextarea'+id).val());
				    		$('#resultsdiv'+id).hide();
				    		$('#AddForm').show();
				    		window.location.reload();
				    	}else{
				    		alert('编辑失败！');
				    	}
					}
				});
			}
		}
		function goBack(){
			location.href = contextPath + '/manager/project/list.jsp';
		}
	</script>
 
  </head>
 <body style="overflow:scroll; overflow-y:hidden">
	<table width="100%" height="700px" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
        <ul >
		 <li>
		 <div style="height: 800px;overflow:scroll;" >
		 <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
	      <tr>
	        <td width="98%">
	          <a name="project"></a>
			  <span class="topTableBgText">定制项目</span>
	        </td>
	        <td width="2%" align="right" valign="middle">
	        </td>
	      </tr>
	    </table> 
		 <table width="40%" align="center" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
             <tr>
               <td width="20%" align="right" valign="top">项目名称<view:LanguageTag key="colon"/></td>
               <td width="30%">${project.prjname}</td>
               <td width="20%" align="right" valign="top">项目编号<view:LanguageTag key="colon"/></td>
               <td width="30%">${project.prjid}</td>
             </tr>
              <tr>
               <td align="right" valign="top">客户名称<view:LanguageTag key="colon"/></td>
               <td>${project.custname}</td>
                <td align="right" valign="top">定制类型<view:LanguageTag key="colon"/></td>
               <td>${project.typeStr}</td>
             </tr>
             <tr>
               <td align="right" valign="top">基础版本<view:LanguageTag key="colon"/></td>
               <td>${project.typeversion}</td>
                <td align="right" valign="top">是否收费<view:LanguageTag key="colon"/></td>
               <td>${project.ifpayStr}</td>
             </tr>
             <tr>
               <td align="right">项目状态<view:LanguageTag key="colon"/></td>
               <td>
               	<c:if test="${project.prjstate != ''}">
               		<c:choose>
						<c:when test="${project.prjstate == 0}">新立项</c:when>
						<c:when test="${project.prjstate == 1}">需求</c:when>
						<c:when test="${project.prjstate == 2}">设计	</c:when>
						<c:when test="${project.prjstate == 3}">开发</c:when>
						<c:when test="${project.prjstate == 4}">测试	</c:when>
						<c:when test="${project.prjstate == 5}">完成</c:when>
						<c:when test="${project.prjstate == 6}">反馈</c:when>
					</c:choose>
               	</c:if>
               </td>
                <td align="right" valign="top">BUG号<view:LanguageTag key="colon"/></td>
               <td colspan="3">${project.bug}</td>
             </tr>
             <tr>
               <td align="right" valign="top">需求部门<view:LanguageTag key="colon"/></td>
               <td colspan="3">${project.needdept}</td>
             </tr>
             <tr>
               <td align="right" valign="top">SVN路径<view:LanguageTag key="colon"/></td>
               <td colspan="3">${project.svn}</td>
             </tr>
             <tr>
               <td align="right" valign="top">销售人员及联系方式<view:LanguageTag key="colon"/></td>
               <td colspan="3">${project.sales}</td>
             </tr>
             <tr>
               <td align="right" valign="top">技术支持及联系方式<view:LanguageTag key="colon"/></td>
               <td colspan="3">${project.techsupport}</td>
             </tr>
             <tr>
               <td align="right" valign="top">详细描述<view:LanguageTag key="colon"/></td>
               <td colspan="3">${project.prjdesc}</td>
             </tr>
           </table>
           <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
		      <tr>
		        <td width="98%">
		          <a name="prjinfo"></a>
				  <span class="topTableBgText">定制信息</span>
		        </td>
		        <td width="2%" align="right" valign="middle">
		        </td>
		      </tr>
		    </table>
           <c:forEach var="prjinfo" items="${prjs}">
           	<table align="center" width="40%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
             <tr>
               <td width="20%" align="right" valign="top">定制信息摘要<view:LanguageTag key="colon"/></td>
               <td width="30%">${prjinfo.prjdesc}</td>
             </tr>
             <tr>
               <td width="20%" align="right" valign="top">定制信息类型<view:LanguageTag key="colon"/></td>
               <td width="30%">${prjinfo.typeStr}</td>
             </tr>
              <tr>
               <td align="right" valign="top">定制项目名称<view:LanguageTag key="colon"/></td>
               <td>${prjinfo.prjname}</td>
               <td>&nbsp;</td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td align="right" valign="top">信息BUG<view:LanguageTag key="colon"/></td>
               <td colspan="3">${prjinfo.bug}</td>
             </tr>
              <tr>
               <td align="right" valign="top">测试结果<view:LanguageTag key="colon"/></td>
               <td colspan="3">${prjinfo.results}</td>
             </tr>
              <tr>
               <td align="right" valign="top">信息SVN<view:LanguageTag key="colon"/></td>
               <td colspan="3">${prjinfo.svn}</td>
             </tr>
             <tr>
               <td align="right" valign="top">信息位置<view:LanguageTag key="colon"/></td>
               <td colspan="3">${prjinfo.path}</td>
             </tr>
             <tr>
               <td align="right" valign="top">开发人员<view:LanguageTag key="colon"/></td>
               <td colspan="3">${prjinfo.developer}</td>
             </tr>
              <tr>
               <td align="right" valign="top">测试人员<view:LanguageTag key="colon"/></td>
               <td colspan="3">${prjinfo.tester}</td>
             </tr>
			<tr>
               <td align="right" valign="top">信息内容<view:LanguageTag key="colon"/></td>
               <td colspan="3">${prjinfo.content}</td>
             </tr>
           </table>
<!--           <hr />-->
           </c:forEach>
           <!-- ------项目总结------ -->
            <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
		      <tr>
		        <td width="98%">
		          <a name="results"></a>
				  <span class="topTableBgText">项目总结</span>
		        </td>
		        <td width="2%" align="right" valign="middle">
		        </td>
		      </tr>
		    </table>
		    <div id="resultsDIV">
		    	<c:forEach var="result" items="${results}">
		    		<form name='editForm${result.id}' id='editForm${result.id}' method='post' action=''>
					    <table align="center" id='resultTab${result.id}' style='overflow:scroll;' width='40%' border="0" class='ulOnInsideTable'>
			           	<tr>
			           		<td width='20%' align='right'>操作人：</td>
			           		<td width='30%'>${result.operator}</td>
			           		<td width='20%' align='right'>创建时间：</td>
			           		<td width='30%'>${result.createtime}</td>
			           	</tr>
			           	<tr>
				           	<td width="20%" align="right" valign="top">项目总结：</td>
				            <td width="80%" colspan="3">
				            	<input type='hidden' id="resultid" name='projectResult.id' value='${result.id}'/>
				            	<input type='hidden' name='projectResult.prjid' value='${result.prjid}'/>
				               	<span id='resultSpan${result.id}'>${result.results}</span><br/>
				               	<div id="resultsdiv${result.id}" style="display: none; height: 210px;">
				               		<textarea style="width:500px;height:200px; visibility:hidden;" id='resultTextarea${result.id}' 
				               			name='results2${result.id}'  rows='10'>${result.results}</textarea>
				               	</div>
				               	<a href='javascript:void(0);' id='showBtn${result.id}' onclick='editObj(${result.id},0)'>编辑</a>
				               	<dir id="returndiv${result.id}" style="display: none;">
        					  	<a href='javascript:void(0);' style='display:none' id='editBtn${result.id}' onclick='editObj(${result.id},1,"resultTextarea${result.id}")'>保存</a>
				               	<a href='javascript:void(0);' id='returnBtn' onclick='returnObj(${result.id})'>关闭</a>
				               	</dir>
				               	<a id="deldiv${result.id}" href='javascript:void(0);' id='delBtn' onclick='deleteObj(${result.id})'>删除</a>
				            </td>
			            </tr>
			            </table>
<!--			            <hr id="resultHr${result.id}"/>-->
							<script type="text/javascript">
							            KindEditor.ready(function(K) {
										K.create('textarea[name="results2"+"${result.id}"]', {
										resizeType : 1,
										allowPreviewEmoticons : false,
										allowImageUpload : false,
										width:"100%",
										afterBlur: function(){this.sync();},
										items : [
											'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
											'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
											'insertunorderedlist', '|', 'emoticons', 'image', 'link']
									});
								});
		            		</script>
		            </form>
			    </c:forEach>
		    </div>
		    <form name="AddForm" id="AddForm" method="post" action="">
	           <table align="center" width="40%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
	           	<tr>
	           		<td width="10%" align="right" valign="top">项目总结：</td>
	               <td width="90%">
	               	<input type="hidden" id="prjid" name="projectResult.prjid" value="${project.id}">
	               	<textarea id="kresults" name="results" style="width:700px;height:200px;visibility:hidden;" class="textarea1">${projectResult.results}</textarea>
	               </td>
	             </tr>
	             <tr>
	             	<td align="center" colspan="2">
	             		<a href="#" id="addBtn" onclick="addObj(0);">添加</a>&nbsp;
	             		<a href="#" onClick=goBack(); id="goback" ><span><view:LanguageTag key="common_syntax_return"/></span></a>
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
 <script type="text/javascript">
			KindEditor.ready(function(K) {
				results = K.create('textarea[class="textarea1"]', {
					resizeType : 1,
					allowPreviewEmoticons : false,
					allowImageUpload : false,
					width:"100%",
					items : [
						'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
						'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
						'insertunorderedlist', '|', 'emoticons', 'image', 'link']
				});
			});
			
		</script>
</html>