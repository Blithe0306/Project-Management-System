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
   	<link rel="stylesheet" type="text/css" href="<%=path%>/kindeditor/themes/simple/simple.css"/>
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
	
	<style type="text/css">
		.textarea100_add {
			border: 1px solid #005495;
			background-color: #FFFFFF;
			width:520px;
			margin-top: 4px;
			margin-bottom: 4px;
			display: block;
			resize: none;
		}
	</style>
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
	<script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script language="javascript" src="<%=path%>/manager/prjinfo/js/add.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>  
   	<script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script>
   	<script charset="utf-8" src="<%=path %>/kindeditor/kindeditor-min.js"></script>
	<script charset="utf-8" src="<%=path %>/kindeditor/lang/zh_CN.js"></script>
	<script>
			var svn;
			KindEditor.ready(function(K) {
				svn = K.create('textarea[name="svn"]', {
					resizeType : 2,
					allowPreviewEmoticons : false,
					allowImageUpload : false,
					width:"100%",
					height:"8px",
					themeType : 'simple',
					items : [
						'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
						'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
						'insertunorderedlist', '|', 'emoticons', 'image', 'link']
				});
			});
			var path;
			KindEditor.ready(function(K) {
				path = K.create('textarea[name="path"]', {
					resizeType : 2,
					allowPreviewEmoticons : false,
					allowImageUpload : false,
					width:"100%",
					height:"8px",
					themeType : 'simple',
					items : [
						'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
						'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
						'insertunorderedlist', '|', 'emoticons', 'image', 'link']
				});
			});
			var content;
			KindEditor.ready(function(K) {
				content = K.create('textarea[name="content"]', {
					resizeType : 2,
					allowPreviewEmoticons : false,
					allowImageUpload : false,
					width:"100%",
					themeType : 'simple',
					items : [
						'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
						'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
						'insertunorderedlist', '|', 'emoticons', 'image', 'link']
				});
			});
		</script>
    <script language="javascript" type="text/javascript">	
	// Start,多语言提取
	var select_role_lang = '<view:LanguageTag key="admin_select_role"/>';
	var need_del_date_lang = '<view:LanguageTag key="common_syntax_check_need_del_date"/>';
	var continue_add_lang = '<view:LanguageTag key="common_save_success_continue_add"/>';
	var syntax_confirm_lang = '<view:LanguageTag key="common_syntax_confirm"/>';
	var succ_tip_lang = '<view:LanguageTag key="common_save_succ_tip"/>';
	var syntax_tip_lang = '<view:LanguageTag key="common_syntax_tip"/>';
	// End,多语言提取
	
	$(document).ready(function(){
			$("#menu li").each(function(index) { //带参数遍历各个选项卡
			$(this).click(function() { //注册每个选卡的单击事件
				$("#menu li.tabFocus").removeClass("tabFocus"); //移除已选中的样式
				$(this).addClass("tabFocus"); //增加当前选中项的样式
                    //显示选项卡对应的内容并隐藏未被选中的内容
				$("#content li:eq(" + index + ")").show()
                    .siblings().hide();
                });
            });
			checkAdminForm();
	 }); 
	function checkAdminForm(){
	      $.formValidator.initConfig({submitButtonID:"addBtn",debug:true,
			onSuccess:function(){
			    if('${prjinfo.id}'== null || '${prjinfo.id}'==''){
			      addObj(0,'adminRoles','');
			    }else{
			      addObj(1,'adminRoles','');
			    }
			}, 
			onError:function(){
				return false;
			}});
			if('${prjinfo.prjdesc}'== null || '${prjinfo.prjdesc}'==''){
				$("#prjdesc").formValidator({onFocus:'请输入项目名称,长度为4-64个字符！',onCorrect:"OK"}).inputValidator({min:4,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'项目名称两边不能有空符合！'}, onError:'项目名称长度为4-64个字符！'})
				.ajaxValidator({
					dataType:"html",
					async:true,
					data:{'prjinfo.prjdesc': $('#prjdesc').val() },
					url:"<%=path%>/manager/prjinfo/prjinfoAction!findPrjectIsExist.action",
					success:function(data){
			            if(data =='') return true;
						return false;
					},
					buttons:$("#addBtn"),
					error:function(jqXHR, textStatus, errorThrown){
						$.messager.alert('<view:LanguageTag key="common_vd_warning"/>','<view:LanguageTag key="common_vd_server_is_busy"/>','warning');
					},
					onError:'<view:LanguageTag key="common_vd_already_exists"/>',
					onWait:'<view:LanguageTag key="common_vd_checking_exists"/>'
				});
	        } else {
	        	$("#prjdesc").formValidator({onFocus:'请输入摘要信息名称,长度为4-64个字符！',onCorrect:"OK"}).inputValidator({min:4,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'定制信息摘要两边不能有空符合！'}, onError:'定制信息摘要名称长度为4-64个字符！'});
	        }
			 //$("#prjdesc").formValidator({onFocus:'请输入摘要信息名称,长度为4-256个字符！',onCorrect:"OK"}).inputValidator({min:4,max:256,empty:{leftEmpty:false,rightEmpty:false,emptyError:'定制信息摘要两边不能有空符合！'}, onError:'定制信息摘要名称长度为4-256个字符！'});
	      	 $("#prjname").formValidator({onFocus:'请选择定制项目名称！',onCorrect:"OK"}).functionValidator({
	     		 	fun:function(val){
			 	     var val = $("#prjname").val();
		             if($.trim(val)=='' || $.trim(val)==null){
	              	return '请选择定制项目名称！';
	              }
	              return true;
			 	}
			 });
      	$("#svn").formValidator({onFocus:'请输入信息svn！',onCorrect:"OK"}).inputValidator({min:0,max:2000, onError:'长度为0-2000个字符！'});
      	$("#bug").formValidator({onFocus:'请输入信息bug！',onCorrect:"OK"}).inputValidator({min:0,max:500, onError:'长度为0-500个字符！'});
		$("#path").formValidator({onFocus:'请输入信息位置！',onCorrect:"OK"}).inputValidator({min:0,max:2000,onError:'长度为0-2000个字符！'});
      	$("#developer").formValidator({onFocus:'请输入开发人员！',onCorrect:"OK"}).inputValidator({min:0,max:1000, onError:'长度为0-1000个字符！'});
      	$("#tester").formValidator({onFocus:'请输入测试人员！',onCorrect:"OK"}).inputValidator({min:0,max:1000,onError:'长度为0-1000个字符！'});
      	$("#results").formValidator({onFocus:'请输入测试结果！',onCorrect:"OK"}).inputValidator({min:0,max:1000, onError:'长度为0-1000个字符！'});
      	$("#content").formValidator({onFocus:'请输入信息内容！',onCorrect:"OK"}).inputValidator({min:0,max:4000, onError:'长度为0-4000个字符！'});
      }
	</script>
  </head>  
  <body style="overflow:auto; overflow-x:hidden">
  <input type="hidden" value="<%=path %>" id="contextPath" />
  <input id="cPage" type="hidden" value="${param.currentPage}" />
  <form name="AddForm" id="AddForm" method="post" action="">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td width="98%">  
		  <span class="topTableBgText">
			  <c:if test='${empty prjinfo.id}'>添加定制信息<div id="oper" style="display:none">add</div></c:if>
	          <c:if test='${not empty prjinfo.id}'>编辑定制信息</c:if>
          </span>
        </td>
        <td width="2%" align="right" valign="middle">
        	<c:if test='${empty prjinfo.id}'><a href="javascript:addAdmPermCode('030202','<%=path%>');" class="isLink_HeiSe">
        	<img src="<%=path%>/images/icon/rightarrow.png" hspace="6" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a></c:if>
        </td>
      </tr>
    </table> 
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td valign="top">
           <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		      <tr>
		        <td width="25%" align="right">定制信息摘要<span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		        <td width="30%">
		        <input type="hidden"  name="prjinfo.id" value="${prjinfo.id}"/>
		         <input type="text" id="prjdesc" name="prjinfo.prjdesc" value="${prjinfo.prjdesc}" class="formCss100"/>
		        </td>
		        <td width="45%" class="divTipCss"><div id="prjdescTip" style="width:100%;"></div></td>
		      </tr>
			  <tr >
			  <tr>
		        <td align="right">定制信息归类<span class="text_Hong_Se" id="type">*</span><view:LanguageTag key="colon"/></td>
		        <td>
		        	<select id="type" name="prjinfo.type" class="select100">
               			<c:forEach var="type" items="${typeList}">
               				<option value="${type.typeid }" <c:if test="${prjinfo.type == type.typeid}">selected</c:if>>${type.typeName }</option>
               			</c:forEach>
               			<c:forEach var="type" items="${prjInfotypeList}">
               				<option value="${type.typeid }" <c:if test="${prjinfo.type == type.typeid}">selected</c:if>>${type.typeName }</option>
               			</c:forEach>
		        	</select>
			     </td>
			  </tr>
	          <tr >
		            <td align="right">项目名称<span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	        		<td>
					 	<input type="hidden" id="prjid" name="prjinfo.prjid" class="formCss100" value="${prjinfo.prjid}" />
						<input type="text" id="prjname" name="prjinfo.prjname" class="formCss100" value="${prjinfo.prjname}" readonly="readonly" />
					</td>
					<td class="divTipCss"><div id="prjnameTip" style="width:100%;"></div></td>
				</tr>
	            <tr >
		            <td align="right">&nbsp;</td>
	        		<td>
						<a href="#" onClick="selProjects();" class="button"><span><view:LanguageTag key="common_syntax_select"/></span></a>
					</td>
					<td class="divTipCss">&nbsp;</td>
				</tr>
				<tr>
			        <td align="right">信息SVN<view:LanguageTag key="colon"/></td>
			        <td><textarea id="svn" name="svn" style="width:700px;height:200px;visibility:hidden;">${prjinfo.svn}</textarea></td>
			        <td class="divTipCss"><div id="prjdescTip" style="width:100%;"></div></td>
			      </tr>
			      <tr>
			        <td align="right">信息BUG<view:LanguageTag key="colon"/></td>
			        <td><textarea id="bug" name="prjinfo.bug" rows="4" cols="86"  class="textarea100_add">${prjinfo.bug}</textarea></td>
			        <td class="divTipCss"><div id="bugTip" style="width:100%;"></div></td>
			      </tr>
			      <tr>
			        <td align="right">信息位置<view:LanguageTag key="colon"/></td>
			        <td><textarea id="path" name="path" style="width:700px;height:200px;visibility:hidden;">${prjinfo.path}</textarea></td>
			        <td class="divTipCss"><div id="prjdescTip" style="width:100%;"></div></td>
			      </tr>
			      <tr>
			        <td align="right">开发人员<view:LanguageTag key="colon"/></td>
			        <td><textarea id="developer" name="prjinfo.developer" rows="4" cols="86"  class="textarea100_add">${prjinfo.developer}</textarea></td>
			        <td class="divTipCss"><div id="developerTip" style="width:100%;"></div></td>
			      </tr>
			      <tr>
			        <td align="right">测试人员<view:LanguageTag key="colon"/></td>
			        <td><textarea id="tester" name="prjinfo.tester" rows="4" cols="86"  class="textarea100_add">${prjinfo.tester}</textarea></td>
			        <td class="divTipCss"><div id="testerTip" style="width:100%;"></div></td>
			      </tr>
			      <tr>
			        <td align="right">测试结果<view:LanguageTag key="colon"/></td>
			        <td><textarea id="results" name="prjinfo.results" rows="4" cols="86"  class="textarea100_add">${prjinfo.results}</textarea></td>
			        <td class="divTipCss"><div id="resultsTip" style="width:100%;"></div></td>
			      </tr>
			      <tr>
			        <td align="right">信息内容<view:LanguageTag key="colon"/></td>
			        <td><textarea id="content" name="content" style="width:700px;height:200px;visibility:hidden;">${prjinfo.content}</textarea></td>
			        <td class="divTipCss"><div id="prjdescTip" style="width:100%;"></div></td>
			      </tr>
			      <tr>
			        <td align="right">&nbsp;</td>
			        <td>
			        	<a href="#" id="addBtn" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
			        	<c:if test='${not empty prjinfo.id}'><a href="#" onClick=goBack() id="goback" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a> </c:if>
			        </td>
			        <td>&nbsp;</td>
			      </tr>
			    </table>
			 </td>
			</tr>
   		  </table>
  		</form>
  	</body>
</html>