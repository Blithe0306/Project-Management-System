<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
%>
<html>
<head>
	<title><view:LanguageTag key="user_orgunit_change"/></title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.6.4.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>	
	<script type="text/javascript" src="<%=path%>/manager/common/js/validate.js"></script>
	<script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=path%>/manager/user/userinfo/template/change_old_token.js"></script>	
	<script type="text/javascript" src="<%=path%>/manager/user/userinfo/template/change_new_token.js"></script>
	<script language="javascript" type="text/javascript">
		// Start,多语信息
     	var langBound = '<view:LanguageTag key="tkn_state_bound"/>';
		var langUnbound = '<view:LanguageTag key="tkn_state_unbound"/>';
		var tknum_lang = '<view:LanguageTag key="tkn_comm_tknum"/>';
		var orgunit_lang = '<view:LanguageTag key="domain_orgunit"/>';
		var state_bind_lang = '<view:LanguageTag key="tkn_comm_state_bind"/>';
		var replaced_tkn_lang = '<view:LanguageTag key="user_sel_replaced_tkn"/>';
     	// End,多语言信息
    
		$(document).ready(function(){
			stepController(0);
			window.resizeBy(0,0);
		});
	
		//通列li的索引跳转到指定标签页，执行上一步，下一步的功能
		function stepController(index){
			$("#menu li.tabFocus").removeClass("tabFocus"); //移除已选中的样式
			$("#menu li:eq(" + index + ")").addClass("tabFocus");//增加当前选中项的样式
			$("#content li:eq(" + index + ")").show().siblings().hide();//显示选项卡对应的内容并隐藏未被选中的内容
			if(index == 1){
				$("#oldTokenListAjx").hide();
				$("#newTokenListAjx").show();
			}else if(index == 2){
				$("#oldTokenListAjx").hide();
				$("#newTokenListAjx").hide();
			}else{
				$("#oldTokenListAjx").show();
				$("#newTokenListAjx").hide();
			}
		}
		
		function nextChangeResult(){	
	 		var leftsRows = oldDataGrid.getSelectedRows();
	 		var rightsRows = newDataGrid.getSelectedRows();
	 		if(rightsRows.length==0){
	 			FT.toAlert('warn','<view:LanguageTag key="user_sel_replace_tkn"/>',null);
	 			return false;
	 		}
	 		if(rightsRows.length!=leftsRows.length){
	 			FT.toAlert('warn','<view:LanguageTag key="user_replace_tkn_must_accord"/>',null);
	 			return false;
	 		}else{
	 			//重组全部有效的令牌
	 			tInfos = [];
	 			//原来的令牌
	 			var oldtokens=document.getElementById("hiddenTkns");
	 			for (var i=0; i<oldtokens.length; i++){
	 				var isExsit=0;//0没选中 1选中了
	 			 	for(var j=0,sl=leftsRows.length;j<sl;){
	 				 	var token=leftsRows[j++]['token'];
	 				 	if(oldtokens.options[i].value==token){
	 				 		isExsit=1;
	 				 		break;
	 				 	}
	 			 	}
	 				if(isExsit==0){//如果没选中 不被替换
	 					tInfos.push(oldtokens.options[i].value);
	 				}
	 			} //end for
	 			 
	 			//新的令牌 选中的全部有效
	 			for(var i=0,sl=rightsRows.length;i<sl;) {
	 				var newToken = rightsRows[i++]['token'];
	 				tInfos.push(newToken);
	 			}
	 			//赋值
	 			reConfirmSelects(tInfos,'tokens');
				
				// 更换操作
	 			exchangTokenObj();
	 		} //end else
	 	}

	  	// 选择令牌完成后将所选令牌或用户组添加到令牌列表
	 	function reConfirmSelects(jSel,id) {
	 		$("#tokens").empty();
	 	    var selList  =  $('#'+id); 
	 	    var rolevals = "";
	 	    $('#'+id+' option').each(function(i,e){
	 			rolevals+=e.value+",";
	 		 });
	 		while(jSel[0]) {
	 			var obj = jSel.shift();
	 			if(rolevals.indexOf(obj)<=-1){
	 			   selList.append("<option value='" + obj + "'>" + obj + "</option>");
	 			}
	 			
	 		}
	 	} 	

	 	//更换令牌
		function exchangTokenObj(){
			var length = $("#tokens")[0].options.length;
			if(length > 0){
				//选中令牌号Select列表
				selectObj('tokens'); //新的有效令牌列表
			}
			selectObj('hiddenTkns'); //原来的令牌列表
			$.ligerDialog.confirm('<view:LanguageTag key="user_validate_confirm_change_one"/>', function (yes){
				if(yes){
					$("#assignForm").ajaxSubmit({
						async:false,    
						dataType : "json",  
						type:"POST", 
						url : "<%=path%>/manager/user/userinfo/userInfo!bindChangeTkn.action?operid=" + $('#operid').val() + "&length="+length,
						success:function(msg){
						     if(msg.errorStr == 'success'){
						         $.ligerDialog.success(msg.object, '<view:LanguageTag key="common_syntax_tip"/>',function(){
						         	goBackClose(true);
						         });
						     }else{
							 	FT.toAlert(msg.errorStr,msg.object,null);
						     }
						}
					});
				}
			});
		}
		
		/**
		 * 打开大窗口编辑 返回/关闭
		 * isRequery true:重查、false:只关闭窗口
		 */
		function goBackClose(isRequery) {
		  var topWin = FT.closeWinBig();
		  if(isRequery){
		  	topWin.currPage.query(true,true);
		  }
		  topWin.currPage = null;
		}
 
</script>
</head>

<body style="overflow:auto;overflow-y:hidden">
<input id="contextPath" type="hidden" value="<%=path%>" />
<input id="currentPage" type="hidden" value="${param.currentPage}" />
<form id="assignForm" name="assignForm" method="post" action=""  >
    <table width="100%" border="0" align="center"  cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
        	<div style="display:none">
	       		<select id="hiddenTkns" name="userInfo.hiddenTkns" size="5" multiple class="select100" style="height:100px;">
	       			<c:forEach items="${userInfo.tokens}" var="hiddenTkn">
	       					<option value="${hiddenTkn.token}">${hiddenTkn.token}</option>
	       			</c:forEach>
	       		</select>
		  	</div>
		  	
		  	<div style="display:none"><!-- 重组全部有效的令牌 -->
		       <select id="tokens" name="userInfo.tokens" size="5" multiple class="select100" style="height:100px;"></select>
		  	</div>
		  	
		  	<input type="hidden" id="operid" name="operid" value="<c:if test='${empty userInfo.tokens}'>0</c:if><c:if test='${not empty userInfo.tokens}'>1</c:if>"/>
			<input type="hidden" id="userId" name="userInfo.userId" value="${userInfo.userId}"/>
			<input type="hidden" id="domainId" name="userInfo.domainId" value="${userInfo.domainId}"/>
			<input type="hidden" id="orgunitId" name="userInfo.orgunitId" value="${userInfo.orgunitId}"/>    
			<input type="hidden" id="orgunitIds" name="tokenQueryForm.orgunitIds"  value="${userInfo.domainId}:${userInfo.orgunitId},"/><!-- 默认指定值 -->
       
        <ul id="menu">
        	<li class="tabFocus" style="cursor: default"><view:LanguageTag key="user_change_count"/></li>
		 	<li style="cursor: default"><view:LanguageTag key="user_curr_bind_tkn"/></li>
        </ul>
	   
	    <ul id="content">
			<!-- 当前用户绑定令牌 -->
		    <li class="conFocus">
            	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	           		<tr>
		           		<td width="50%">
		                	&nbsp;&nbsp;<view:LanguageTag key="user_info_account"/><view:LanguageTag key="colon"/>${userInfo.userId}&nbsp;&nbsp;&nbsp;&nbsp;
		                		<view:LanguageTag key="user_info_real_name"/><view:LanguageTag key="colon"/>${userInfo.realName}			 </td>
		             	<td width="50%">
		             		<span style="float:right">
			   					<a href="#" onClick="goBackClose(false)"  class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a>
			   					<a href="#" onClick="nextChangeTkn()" class="button"><span><view:LanguageTag key="common_syntax_nextStep"/></span></a>
			   				</span>
			   			</td>
		         	</tr>
	           	</table>
		    </li>
		      
		     <!-- 新令牌 -->
		     <li>
		      	 <table width="800" border="0" cellspacing="0" cellpadding="0">
           			<tr>
		             	<td width="120" align="right"><view:LanguageTag key='tkn_comm_state'/><view:LanguageTag key="colon"/></td>
		             	<td width="160">
		             		<select id="bindState" name="tokenQueryForm.bindState" value="${tokenQueryForm.bindState}" class="select100">
				            	<option value="-1" <c:if test='${tokenQueryForm.bindState == -1}'> selected</c:if>><view:LanguageTag key="tkn_comm_state_bind"/></option>
					  			<option value="1" <c:if test='${tokenQueryForm.bindState == 1}'> selected</c:if>><view:LanguageTag key="tkn_state_bound"/></option>
					  			<option value="0" <c:if test='${tokenQueryForm.bindState == 0}'> selected</c:if> selected><view:LanguageTag key="tkn_state_unbound"/></option>
		  					</select>
		  				</td>
		             	<td width="120" align="right"><view:LanguageTag key="tkn_comm_tknum"/><view:LanguageTag key="colon"/></td>
		             	<td width="160"><input type="text" id="tokenStr" value="${tokenQueryForm.token}" name="tokenQueryForm.token" class="formCss100"/></td>
		             	<td width="240"><span style="float:right"><span style="display:inline-block" class="query-button-css"><a href="javascript:queryNewToken(false,true);" class="isLink_LanSe"><view:LanguageTag key="common_syntax_query"/></a></span></span></td>
		           	</tr>
         		 </table>
			     <table width="100%" border="0" cellspacing="0" cellpadding="0" class="operTableBg">
		          	<tr>
			   			<td>
			  			   <span style="float:right">		
					       <span><a href="#" onClick="stepController(0)" class="button"><span><view:LanguageTag key="common_syntax_preStep"/></span></a></span>	
					       <span><a href="#" onClick="nextChangeResult()" class="button"><span><view:LanguageTag key="user_change_token_one"/></span></a></span></span>
					    </td>
		   			 </tr>
		         </table>
		     </li>
       	 </ul>
		</td>
      </tr>
    </table>
  </form>
  <div id="oldTokenListAjx"></div>
  <div id="newTokenListAjx"></div>
</body>
</html>