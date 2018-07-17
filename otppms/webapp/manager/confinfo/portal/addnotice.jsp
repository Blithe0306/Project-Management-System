<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page language="java" pageEncoding="UTF-8"%>
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
    <link rel="stylesheet" id="openwincss" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-dialog.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/datepicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>
	
    <script language="javascript" type="text/javascript">
	$(document).ready(function(){
		$("select[name='noticeInfo.systype'] option[value='${noticeInfo.systype}']").attr("selected","selected");
	
		$.formValidator.initConfig({submitButtonID:"addBtn", 
			onSuccess:function(){
				addObj();
			},
			onError:function(){
				return false;
			}});

			$("#title").formValidator({onFocus:'<view:LanguageTag key="notice_vd_title_show"/>',onCorrect:"OK"}).inputValidator({min:4,max:50,onError:'<view:LanguageTag key="notice_vd_title_err"/>'});
			$("#tempDeathTime").formValidator({onFocus:'<view:LanguageTag key="notice_vd_death_time_show"/>',onCorrect:"OK"}).inputValidator({min:4,max:20, onError:'<view:LanguageTag key="notice_vd_death_time_err"/>'}).functionValidator({
				fun:function(val){
		  			var now = new Date();
		  			if (val < dateToStr(now)) {
		  			 	return '<view:LanguageTag key="notice_vd_death_time_err1"/>';
		  			}
		  			return true;
		  			
				}
			});
			$("#contentText").formValidator({onFocus:'<view:LanguageTag key="notice_vd_content_show"/>',onCorrect:"OK"}).inputValidator({min:10, max:3000,onError:'<view:LanguageTag key="notice_vd_content_err"/>'});
			$("#systype").formValidator({onFocus:'<view:LanguageTag key="common_vd_please_sel"/>',onCorrect:"OK"}).inputValidator({min:0, onError:'<view:LanguageTag key="common_vd_please_sel"/>'});
			$("#title").focus();
	});
		
	function addObj(){
		var curpage = $("#currentPage").val();
		var noticeId = $("#noticeId").val();
		
		var url = "<%=path%>/manager/confinfo/portal/pronotice!add.action";		
		if("" != noticeId) {
			url = "<%=path%>/manager/confinfo/portal/pronotice!modify.action?currentPage=" + curpage;
		}
		
		$("#addNoticeForm").ajaxSubmit({
			type : "POST", 
			url : url,
			dataType : "json",
			success : function(msg){
			   if(msg.errorStr == 'success'){
			        $.ligerDialog.success(msg.object, '<view:LanguageTag key="common_syntax_tip"/>',function(){
				        if(noticeId == null || noticeId ==''){
				            window.location.href="<%=path%>/manager/confinfo/portal/noticelist.jsp";
				        }else{
				        	window.location.href="<%=path%>/manager/confinfo/portal/noticelist.jsp?cPage="+ curpage+"&&source=${param.source}" ;
				        }
			   		});
			    }else{
				 	FT.toAlert(msg.errorStr, msg.object, null);
				}
			}
		});	
		
	}
	
	function goBack() {
		var currentPage = $("#currentPage").val();
		window.location.href = '<%=path%>/manager/confinfo/portal/noticelist.jsp?cPage=' + currentPage+"&source=${param.source}";
	}	
	</script>
  </head>
  
  <body style="overflow:auto; overflow-x:hidden">
  <input name="currentPage" id="currentPage" type="hidden" value="${param.cPage}" />
  <form name="addNoticeForm" id="addNoticeForm" method="post" action="" >
    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="topTableBg">
    <tr>
      <td width="98%"> 
        <span class="topTableBgText"> 
	        <c:if test="${not empty noticeInfo.id}"><view:LanguageTag key="notice_eidt"/></c:if>
	        <c:if test="${empty noticeInfo.id}"><view:LanguageTag key="notice_add"/></c:if>
        </span>
      </td>
      <td width="2%" align="right"></td>
    </tr>
  	</table>
   <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
    <tr>
     <td valign="top">
	   <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="ulOnInsideTable" style="table-layout:fixed;word-wrap:break-word;word-break:break-all">
	    <tr>
	      <td width="30%" align="right"><view:LanguageTag key="notice_title"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	      <td width="40%">
	          <input id="title" name="noticeInfo.title"  value="${noticeInfo.title}" class="formCss100"/>
	          <input type="hidden" id="noticeId" name="noticeInfo.id" value="${noticeInfo.id}"/>
	      </td>
	      <td width="30%" class="divTipCss"><div id="titleTip"></div></td>
	    </tr>
	    <tr>
			<td align="right"><view:LanguageTag key="notice_level"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
			<td>
				<select id="systype" name="noticeInfo.systype" class="select100">
					<option value="1"><view:LanguageTag key="notice_level_general"/></option>
		            <option value="2"><view:LanguageTag key="notice_level_urgent"/></option>
		            <option value="3"><view:LanguageTag key="notice_level_warning"/></option>
		    	</select>	
			</td>
		  <td class="divTipCss"><div id="systypeTip"></div></td>
		</tr>
	    <tr>
			<td align="right">
				<view:LanguageTag key="notice_death_time"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/>
			</td>
			<td>
			  <input type="text" id="tempDeathTime" name="noticeInfo.tempDeathTime" value="${noticeInfo.tempDeathTime}" onclick="WdatePicker({lang:'${language_session_key}', dateFmt:'yyyy-MM-dd HH:mm:ss'})"  readOnly="readOnly" class="formCss100" />
			</td>
			<td class="divTipCss"><div id="tempDeathTimeTip"></div></td>
		</tr>
		<tr>
	        <td align="right"><view:LanguageTag key="notice_content"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	        <td>
	        	<textarea rows="8" id="contentText" name="noticeInfo.content" class="textarea100">${noticeInfo.content}</textarea>
	        </td>
	        <td class="divTipCss"><div id="contentTextTip"></div></td>
	    </tr>
	    <tr>
	        <td align="right"> </td>
	        <td> 
		        <a href="#" id="addBtn" class="button"><span><view:LanguageTag key="common_syntax_save"/></span></a>
		        <a href="javascript:goBack();" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a>
	        </td>
	        <td></td>
	    </tr> 
 	  </table>
    </td>
   </tr>
 </table>
</form>
</body>
</html>