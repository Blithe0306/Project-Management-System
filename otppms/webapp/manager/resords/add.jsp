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
    <script language="javascript" src="<%=path%>/manager/common/datepicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>  
   	<script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/resords/js/add.js"></script>
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
			checkAdminForm();
	 });
	function checkAdminForm(){
	      $.formValidator.initConfig({submitButtonID:"addBtn",debug:true,
				onSuccess:function(){
				    if('${resords.id}' <= 0){	// add
					   addObj(0, '');
					}else{			// update
					   addObj(1, '');
					}				   
				}, 
				onError:function(){
					return false;
				}});
	      	 $("#prjid").formValidator({onFocus:'请选择项目名称！',onCorrect:"OK"}).functionValidator({
	     		 	fun:function(val){
			 	     var val = $("#prjid").val();
		             if($.trim(val)=='' || $.trim(val)==null){
	              	return '请选择项目名称！';
	              }
	              return true;
			 	}
			 });
	  	$("#recordtimeStr").formValidator({onFocus:'请输入上门开始时间！',onCorrect:"OK"})
		.inputValidator({min:4,max:256,empty:{leftEmpty:false,rightEmpty:false,emptyError:'记录时间不能有空符合！'}, onError:'请输入上门开始时间！'});
		
	  	$("#endrecordtimeStr").formValidator({onFocus:'请输入上门结束时间！',onCorrect:"OK"})
		.inputValidator({min:4,max:256,empty:{leftEmpty:false,rightEmpty:false,emptyError:'记录时间不能有空符合！'}, onError:'请输入上门结束时间！'});
		
	  	$("#cutomer").formValidator({onFocus:'请输入客户名称！',onCorrect:"OK"}).inputValidator({min:0,max:1000, onError:'长度为0-4000个字符！'});
      	$("#recordUser").formValidator({onFocus:'请输入记录人员！',onCorrect:"OK"}).inputValidator({min:0,max:1000, onError:'长度为0-1000个字符！'});
      	$("#reason").formValidator({onFocus:'请输入理由！',onCorrect:"OK"}).inputValidator({min:0,max:4000,onError:'长度为0-4000个字符！'});
      	$("#results").formValidator({onFocus:'请输入记录结果！',onCorrect:"OK"}).inputValidator({min:0,max:1000, onError:'长度为0-4000个字符！'});
      	$("#remark").formValidator({onFocus:'请输入描述！',onCorrect:"OK"}).inputValidator({min:0,max:1000, onError:'长度为0-4000个字符！'});
     }
	//-->
	</script>
  </head>  
  <body style="overflow:auto; overflow-x:hidden">
  <input type="hidden" value="<%=path %>" id="contextPath" />
  <input id="cPage" type="hidden" value="${param.currentPage}" />
  <form name="AddForm" id="AddForm" method="post" action="">
  	<input type="hidden" id="infoid" name="resords.id" value="${resords.id}" class="formCss100"/>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
         <td width="98%">  
		  <span class="topTableBgText">
			  <c:if test='${empty resords.prjid}'>添加上门记录<div id="oper" style="display:none">add</div></c:if>
	          <c:if test='${not empty resords.prjid}'>编辑上门记录</c:if>
          </span>
        </td>
        <td width="2%" align="right" valign="middle">
        	<a href="javascript:addAdmPermCode('030302','<%=path%>');" class="isLink_HeiSe">
        	<img src="<%=path%>/images/icon/rightarrow.png" hspace="6" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a>
        </td>
    </tr>
   </table>
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td valign="top">
           <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
	          <tr >
		            <td width="25%"  align="right">项目名称<span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	        		<td width="30%" >
					 	<input type="text" id="prjid" name="resords.prjid" class="formCss100" value="${resords.prjid}" readonly="readonly"/>
					</td>
					<td width="40%" class="divTipCss"><div id="prjidTip" style="width:100%;"></div></td>
				</tr>
				 <tr >
		            <td align="right">&nbsp;</td>
	        		<td>
						<a href="#" onclick="selResords();" class="button"><span><view:LanguageTag key="common_syntax_select"/></span></a>
					</td>
					<td class="divTipCss">&nbsp;</td>
				</tr>
		         <tr >
		            <td align="right">上门开始时间<span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	        		<td>
					 	 <input type="text" id="recordtimeStr" name="resords.recordtimeStr" value="${resords.recordtimeStr}"
					 	 onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss' ,maxDate:'#F{$dp.$(\'endrecordtimeStr\').value}'})"  readonly="readonly" class="formCss100" />
					</td>
					<td class="divTipCss"><div id="recordtimeStrTip" style="width:100%;"></div></td>
				</tr>
				<tr >
		            <td align="right">上门结束时间<span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	        		<td>
					 	 <input type="text" id="endrecordtimeStr" name="resords.endrecordtimeStr" value="${resords.endrecordtimeStr}"
					 	 onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss' ,minDate:'#F{$dp.$(\'recordtimeStr\').value}'})"  readonly="readonly" class="formCss100" />
					</td>
					<td class="divTipCss"><div id="endrecordtimeStrTip" style="width:100%;"></div></td>
				</tr>
			      <tr>
			        <td align="right">上门人员<view:LanguageTag key="colon"/></td>
			        <td><textarea id="recordUser" name="resords.recordUser" style="resize:both;width: 560px;" class="textarea100">${resords.recordUser}</textarea></td>
			        <td class="divTipCss"><div id="recordUserTip" style="width:100%;"></div></td>
			      </tr>
			       <tr>
			        <td align="right">上门原因<view:LanguageTag key="colon"/></td>
			        <td><textarea id="reason" name="resords.reason" rows="4" cols="86" style="resize:both;width: 560px;" class="textarea100">${resords.reason}</textarea></td>
			        <td class="divTipCss"><div id="reasonTip" style="width:100%;"></div></td>
			      </tr>
			       <tr>
			        <td align="right">上门成果<view:LanguageTag key="colon"/></td>
			        <td><textarea id="results" name="resords.results" rows="4" cols="86" style="resize:both;width: 560px;" class="textarea100">${resords.results}</textarea></td>
			        <td class="divTipCss"><div id="resultsTip" style="width:100%;"></div></td>
			      </tr>
			       <tr>
			        <td align="right">描述<view:LanguageTag key="colon"/></td>
			        <td><textarea id="remark" name="resords.remark" rows="4" cols="86" style="resize:both;width: 560px;" class="textarea100">${resords.remark}</textarea></td>
			        <td class="divTipCss"><div id="remarkTip" style="width:100%;"></div></td>
			      </tr>

		      <tr>
		        <td align="right">&nbsp;</td>
		        <td>
		        	<a href="#" id="addBtn" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
		   			 <c:if test='${ resords.id > 0}'><a href="#" onClick=goBack() id="goback" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a> </c:if>
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
      	