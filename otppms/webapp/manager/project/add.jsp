<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
    <link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/kindeditor/themes/simple/simple.css"/>
	
	<style type="text/css">
		.textarea100_add {
			border: 1px solid #005495;
			background-color: #FFFFFF;
			width:100%;
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
	<script language="javascript" src="<%=path%>/manager/project/js/add.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>  
   	<script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script>
   	<script charset="utf-8" src="<%=path %>/kindeditor/kindeditor-min.js"></script>
	<script charset="utf-8" src="<%=path %>/kindeditor/lang/zh_CN.js"></script>
	<script>
			var prjdesc;
			KindEditor.ready(function(K) {
				prjdesc = K.create('textarea[name="prjdesc"]', {
					resizeType : 2,
					allowPreviewEmoticons : false,
					allowImageUpload : false,
					themeType : 'simple',
					items : [
						'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
						'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
						'insertunorderedlist', '|', 'emoticons', 'image', 'link']
				});
			});
			var svn;
			KindEditor.ready(function(K) {
				svn = K.create('textarea[name="svn"]', {
					resizeType : 2,
					allowPreviewEmoticons : false,
					allowImageUpload : false,
					height:"8px",
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
			    if('${project.id}'== null || '${project.id}'==''){
			      addObj(0,'adminRoles','');
			    }else{
			      addObj(1,'adminRoles','');
			    }
			}, 
			onError:function(){
				return false;
			}});

	      if('${project.prjname}'== null || '${project.prjname}'==''){
				$("#prjname").formValidator({onFocus:'请输入项目名称,长度为4-64个字符！',onCorrect:"OK"}).inputValidator({min:4,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'项目名称两边不能有空符合！'}, onError:'项目名称长度为4-64个字符！'})
				.ajaxValidator({
					dataType:"html",
					async:true,
					data:{'project.id': $('#prjid').val() },
					url:"<%=path%>/manager/project/projectAction!findPrjectIsExist.action",
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
	        	$("#prjname").formValidator({onFocus:'请输入项目名称,长度为4-64个字符！',onCorrect:"OK"}).inputValidator({min:4,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'项目名称两边不能有空符合！'}, onError:'项目名称长度为4-64个字符！'});
	        }
	      if('${project.prjid}'== null || '${project.prjid}'==''){
				$("#prjid").formValidator({onFocus:'请输入项目名称,长度为4-64个字符！',onCorrect:"OK"}).inputValidator({min:4,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'项目名称两边不能有空符合！'}, onError:'项目名称长度为4-64个字符！'})
				.ajaxValidator({
					dataType:"html",
					async:true,
					data:{'project.id': $('#prjid').val() },
					url:"<%=path%>/manager/project/projectAction!findPrjectIsExist.action",
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
	        	$("#prjname").formValidator({onFocus:'请输入项目编号,长度为4-64个字符！',onCorrect:"OK"}).inputValidator({min:4,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'项目编号两边不能有空符合！'}, onError:'项目编号长度为4-64个字符！'});
	        }
	      
	      //$("#prjname").formValidator({onFocus:'请输入项目名称,长度为4-64个字符！',onCorrect:"OK"}).inputValidator({min:4,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'项目名称两边不能有空符合！'}, onError:'项目名称长度为4-64个字符！'});
	      //$("#prjid").formValidator({onFocus:'请输入项目编号,长度为4-64个字符！',onCorrect:"OK"}).inputValidator({min:4,max:64,empty:{leftEmpty:false,rightEmpty:false,emptyError:'项目编号两边不能有空符合！'}, onError:'项目编号长度为4-64个字符！'});
	      $("#custname").formValidator({onFocus:'请选择客户名称！',onCorrect:"OK"}).functionValidator({
     		 	fun:function(val){
		 	     var val = $("#custname").val();
	             if($.trim(val)=='' || $.trim(val)==null){
              	return '请选择客户名称！';
              }
              return true;
		 	}
		 });
	     $("#typeversion").formValidator({onFocus:'请输入基础版本！',onCorrect:"OK"}).inputValidator({min:1,max:32,empty:{leftEmpty:false,rightEmpty:false,emptyError:'基础版本两边不能有空符合！'}, onError:'基础版本长度为1-32个字符！'});
      	
      	$("#needdept").formValidator({onFocus:'请输入需求部门！',onCorrect:"OK"}).inputValidator({min:0,max:1000,onError:'长度为0-1000个字符！'});
      	$("#svn").formValidator({onFocus:'请输入svn路径！',onCorrect:"OK"}).inputValidator({min:0,max:2000, onError:'长度为0-2000个字符！'});
      	$("#bug").formValidator({onFocus:'请输入bug号！',onCorrect:"OK"}).inputValidator({min:0,max:256, onError:'长度为0-256个字符！'});
      	$("#sales").formValidator({onFocus:'请输入销售人员及联系方式！',onCorrect:"OK"}).inputValidator({min:0,max:256, onError:'长度为0-256个字符！'});
      	$("#techsupport").formValidator({onFocus:'请输入技术支持及联系方式！',onCorrect:"OK"}).inputValidator({min:0,max:256,onError:'长度为0-256个字符！'});
      	$("#prjdesc").formValidator({onFocus:'请输入详细描述！',onCorrect:"OK"}).inputValidator({min:0,max:4000, onError:'长度为0-4000个字符！'});
      }

	//基础版本
	function baseVersion(){
		var prjtype = $('#prjtype').val();
		var currTypeVer = $('#typeversion').val();
		if(prjtype != null){
			$.ajax({
				async:true,
				type:"POST",
				url:contextPath + "/manager/project/projectAction!getBaseVer.action",
				data:{prjtype: prjtype},
				dataType:"String",
				success:function(msg){
					var selAttr = "";
					if(msg.length == 0){
						selAttr = "<input type='text' id='versionAttr' name='versionAttr' onchange='javascript:inputTypeVer(this)' class='formCss100' value='${project.typeversion}'/>";
					} else {
						var baseVer = msg.split(",");
						selAttr = "<select id='versionAttr' name='versionAttr' onactivate='javascript:inputTypeVer(this)' onchange='javascript:inputTypeVer(this)' class='select100'>";

						var seleted = false; //是否有匹配的值
						for (var i=0;i<baseVer.length;i++)
						{
							selAttr = selAttr + "<option value='" + baseVer[i] + "'"; 
							if(currTypeVer == baseVer[i]){
								selAttr = selAttr + " selected";
								$('#typeversion').val(baseVer[i]);
								seleted = true;
							}
							selAttr = selAttr + ">" + baseVer[i] + "</option>";
						}
						selAttr = selAttr + "</select>";
						if(!seleted){
							//没有匹配的值的时候，选择第一个。
							$('#typeversion').val(baseVer[0]);
						}
					}
					document.getElementById("selBaseVerion").innerHTML=selAttr;
				}
			});
		}
	}

	function inputTypeVer(obj){
		$('#typeversion').val(obj.value);
	}
    
	</script>
  </head>  
  <body style="overflow:auto; overflow-x:hidden" onload="baseVersion()">
  <input type="hidden" value="<%=path %>" id="contextPath" />
  <input id="cPage" type="hidden" value="${param.currentPage}" />
  <form name="AddForm" id="AddForm" method="post" action="">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td width="98%">  
		  <span class="topTableBgText">
			  <c:if test='${empty project.id}'>添加定制项目<div id="oper" style="display:none">add</div></c:if>
	          <c:if test='${not empty project.id}'>编辑定制项目</c:if>
          </span>
        </td>
        <td width="2%" align="right" valign="middle">
        	<c:if test='${empty project.id}'><a href="javascript:addAdmPermCode('030002','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" hspace="6" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a></c:if>
        </td>
      </tr>
    </table> 
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td valign="top">
           <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		      <tr>
		        <td width="25%" align="right">项目名称<span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
		        <td width="30%">
		        	<input type="hidden"  name="project.id" value="${project.id}"/>
		         	<input type="text" id="prjname" name="project.prjname" value="${project.prjname}" class="formCss100"/>
		        </td>
		        <td width="45%" class="divTipCss"><div id="prjnameTip" style="width:100%;"></div></td>
		      </tr>
		      <tr>
		        <td align="right">项目编号<span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>       
		        <td><input type="text" id="prjid" name="project.prjid" value="${project.prjid}" class="formCss100"/></td>
		        <td class="divTipCss"><div id="prjidTip" style="width:100%;"></div></td>
		      </tr>
		      
			  <tr>
		        <td align="right">客户名称<span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
				<td>
				 	<input type="hidden" id="custid" name="project.custid" class="formCss100" value="${project.custid}" />
					<input type="text" id="custname" name="project.custname" class="formCss100" value="${project.custname}" readonly="readonly" />
				</td>
				<td class="divTipCss" style="vertical-align:top;"><div id="custnameTip" style="width:100%;"></div></td>
			  </tr>
			  <tr>
		        <td align="right" style="vertical-align:top;" >&nbsp;</td>
				<td>
					<a href="#" onClick="selCustomers();" class="button"><span><view:LanguageTag key="common_syntax_select"/></span></a>
				</td>
				<td class="divTipCss" style="vertical-align:top;">&nbsp;</td>
			  </tr>
			  <tr>
			        <td align="right">定制类型<span class="text_Hong_Se" id="type">*</span><view:LanguageTag key="colon"/></td>
			        <td>
			        	<select id="prjtype" name="project.type" class="select100" onchange="baseVersion()">
	               			<c:forEach var="type" items="${typeList}">
	               				<option value="${type.typeid }" <c:if test="${project.type == type.typeid}">selected</c:if>>${type.typeName }</option>
	               			</c:forEach>
			        	</select>
			        </td>
			        <td class="divTipCss" ><div id="prjtypeTip" style="width:100%;"></div></td>
		      </tr>
			  <tr>		
		        <td align="right">基础版本<span class="text_Hong_Se" id="emailSpan">*</span><view:LanguageTag key="colon"/></td>
		        <td>
		        	<input type="hidden" id="typeversion" name="project.typeversion" class="formCss100"  value="${project.typeversion}"/>
		        	<!-- 选择基础版本 -->
		        	<div id="selBaseVerion"></div>
		        </td>
		        <td class="divTipCss"><div id="typeversionTip" style="width:100%;"></div></td>
		      </tr>
			      <tr>
			        <td align="right">是否收费<span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			        <td>
			        	<input type="radio" name="project.ifpay" value="0" <c:if test="${project.ifpay=='0' }">checked</c:if>/>是&nbsp;&nbsp;
       　					<input type="radio" name="project.ifpay" value="1" <c:if test="${project.ifpay=='1'}">checked</c:if>/>否     
			        </td>
			      </tr>
			      <tr>
			        <td align="right">项目状态<view:LanguageTag key="colon"/></td>
			        <td>
			        	<select id="prjstate" name="project.prjstate" class="select100" onchange="baseVersion()">
			        	
	               			<option value="0" <c:if test="${0 == project.prjstate}">selected</c:if>>新立项</option>
	               			<option value="1" <c:if test="${1 == project.prjstate}">selected</c:if>>需求</option>
	               			<option value="2" <c:if test="${2 == project.prjstate}">selected</c:if>>设计</option>
	               			<option value="3" <c:if test="${3 == project.prjstate}">selected</c:if>>开发</option>
	               			<option value="4" <c:if test="${4 == project.prjstate}">selected</c:if>>测试</option>
	               			<option value="5" <c:if test="${5 == project.prjstate}">selected</c:if>>完成</option>
	               			<option value="6" <c:if test="${6 == project.prjstate}">selected</c:if>>反馈</option>
			        	</select>
			        </td>
			        <td class="divTipCss"><div id="prjstateTip" style="width:100%;"></div></td>
			      </tr>
			      <tr>
			        <td align="right">需求部门<view:LanguageTag key="colon"/></td>
			        <td><input type="text"  id="needdept" name="project.needdept" class="formCss100" value="${project.needdept}"/> </td>
			        <td class="divTipCss"><div id="needdeptTip" style="width:100%;"></div></td>
			      </tr>
			      <tr>
			        <td align="right">SVN路径<view:LanguageTag key="colon"/></td>
			        <td><textarea id="svn" name="svn" style="width:700px;height:200px;visibility:hidden;">${project.svn}</textarea></td>
			        <td class="divTipCss"><div id="prjdescTip" style="width:100%;"></div></td>
			      </tr>
			      <tr>
			        <td align="right">BUG号<view:LanguageTag key="colon"/></td>
			        <td><input type="text" id="bug" name="project.bug" value="${project.bug}" class="formCss100"/></td>
			        <td class="divTipCss"><div id="bugTip" style="width:100%;"></div></td>
			      </tr>
			      <tr>
			        <td align="right">销售人员及联系方式<view:LanguageTag key="colon"/></td>
			        <td><textarea id="sales" name="project.sales" class="textarea100">${project.sales}</textarea></td>
			        <td class="divTipCss"><div id="salesTip" style="width:100%;"></div></td>
			      </tr>
			      <tr>
			        <td align="right">技术支持及联系方式<view:LanguageTag key="colon"/></td>
			        <td><textarea id="techsupport" name="project.techsupport" class="textarea100">${project.techsupport}</textarea></td>
			        <td class="divTipCss"><div id="techsupportTip" style="width:100%;"></div></td>
			      </tr>
			      <tr>
			        <td align="right">详细描述<view:LanguageTag key="colon"/></td>
			        <td><textarea id="prjdesc" name="prjdesc" style="width:700px;height:200px;visibility:hidden;">${project.prjdesc}</textarea></td>
			        <td class="divTipCss"><div id="prjdescTip" style="width:100%;"></div></td>
			      </tr>
			      <tr>
			        <td align="right">&nbsp;</td>
			        <td>
			        	<a href="#" id="addBtn" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
			        	<c:if test='${not empty project.id}'><a href="#" onClick=goback(); id="goback" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a> </c:if>
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