<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
 
<%
	String path = request.getContextPath();
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css">
    <link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css">
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css">    
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/> 
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script src="<%=path%>/manager/common/js/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>	
    <script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>   	
    <script type="text/javascript" src="<%=path%>/manager/token/js/showUser.js"></script>
    <script language="javascript" type="text/javascript">
    var winClose;
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
 
	     checkInput();
	     
	     var noText = '<view:LanguageTag key="common_syntax_nothing"/>';
         if($("#adminTd").text()==""){
         	$("#adminTd").text(noText);
         }
         
         if($("#userTd").text()==""){
         	$("#userTd").text(noText);
         }
	});
	
	//设置令牌应急口令
   function okClick(item,win,index){
         winClose = win;
         $('#addObj').triggerHandler("click");    	
	 }
	 
	function checkInput(){
			$.formValidator.initConfig({
		        submitButtonID:"addObj",
		        debug:true,
		        onSuccess:function(){
		                var generatype = $("#generateType").find("option:selected").val();
		                var defaultap = $('#softtoken_distribute_pwd').val();
		                var result = $("input[name='result']:checked").val();
  
		                if(generatype==8){
		                   $('#softtoken_distribute_pwd').val($('#pin').val());
		                }
		                $("#addForm").ajaxSubmit({
						   async:false,  
						   dataType:"json",
						   type:"POST", 
						   url : "<%=path%>/manager/token/token!softTokenDist.action?generaType="+generatype+"&defaultap="+defaultap+"&result="+result,
						   success:function(msg){
							if(msg.errorStr == 'success'){
								parent.softClose(msg);							     
							 }else if(msg.errorStr == 'sendSucc'){
								 $.ligerDialog.success(msg.object,'<view:LanguageTag key="common_syntax_tip"/>',function(){
								        winClose.close();
								 });
							 }else{
							     FT.toAlert(msg.errorStr,msg.object,null);
							} 
						}
			        }); 
		        },
	           onError:function(){
	              return false;
	              }
	           });    
			   $("#pin").formValidator({onFocus:'<view:LanguageTag key="tkn_vd_pin_show"/>',onCorrect:"OK"}).functionValidator({
		          fun:function(val){
		             if($("#generateType").find("option:selected").val()==8){
		                var partn = new RegExp("^[A-Za-z0-9]+$");
			            if($.trim(val)=='' || $.trim(val)==null){return '<view:LanguageTag key="tkn_vd_pin_error_1"/>';}
			            if(!partn.exec(val)){return '<view:LanguageTag key="tkn_vd_pin_show_err"/>';}
			            if(val.length<4 || val.length>10){return '<view:LanguageTag key="tkn_vd_pin_error"/>';}
		             }
		             return true;
		          }
		       });
	    }
	    
	    
	  	//选择激活密码生成方式
			function judgePass1(va){
				     //选择手动输入
				     var defaultPass = $('#softtoken_distribute_pwd').val();
					if(va.value == 8){
						$("#writePinTr").show();
				     }else{
						$("#writePinTr").hide();
				     }
				 
			     //选择默认(默认密码在配置表中已经配置好的密码)
			     if(va.value == 4){
					if(defaultPass == null || defaultPass == ''){
						FT.toAlert('error','<view:LanguageTag key="tkn_vd_defaultpass_show"/>', null);
						va.options[0].selected = true;
					}
				}
			}  
		//查看用户
		/*
	function viewUser(id,domainId){
		window.location.href="<%=path%>/manager/user/userinfo/userInfo!view.action?userInfo.userId=" + id+"&userInfo.domainId="+domainId;
	}	
	*/
</script>
</head>

<body>
<jsp:include page="/manager/user/userinfo/common.jsp" />
<input type="hidden" id="contextPath" value="<%=path%>"/> 
<input type="hidden" name="currentPage" value="${param.curPage}" id="currentPage"/> 
<form name="addForm" method="post" action="" id="addForm">
<table width="98%" border="0" align="center"  cellpadding="0" cellspacing="0" class="ulOnTable">
   <tr>
     <td valign="top">
	     <ul id="menu">
	     	<li class="tabFocus"><view:LanguageTag key="tkn_dist_soft"/></li>
		 	<li><view:LanguageTag key="tkn_binding_admin_user"/></li>
	     </ul>
		 <ul id="content">
			 <li class="conFocus">
			     <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
			  		<tr><td height="5"></td></tr>
			  		<tr>
			        	<td width="25%" align="right"><view:LanguageTag key="tkn_comm_tknum"/><view:LanguageTag key="colon"/></td>
			        	<td width="30%">${tokenInfo.token}
			         		<input type="hidden"   name="tokenInfo.token" value="${tokenInfo.token}" class="formCss100" readonly > 
			           	</td>
			        	<td width="45%">&nbsp;</td>
			       </tr>
			       <tr><td height="5"></td></tr>
			       <tr>
			       	  <td align="right"><view:LanguageTag key="tkn_pin_create"/><span class="text_Hong_Se" id="span1">*</span><view:LanguageTag key="colon"/></td>
			          <td> 
			          	<select name="generateType" id="generateType" onchange="judgePass1(this);" style="width:127px;"> 
					   	<!--  <option value="2"><view:LanguageTag key="tkn_random"/></option>    -->	
					   		<option value="4"><view:LanguageTag key="tkn_default"/></option>   
					  		<option value="8"><view:LanguageTag key="tkn_manual_input"/></option>
					    </select>  
					    <!-- 默认PIN码 -->
					    <input type="hidden" id="softtoken_distribute_pwd" name="tokenInfo.softtoken_distribute_pwd" class="formCss100"  value="${tokenInfo.softtoken_distribute_pwd}"> 
			          </td>
			          <td> </td>
			      </tr>
			      <tr><td height="5"></td></tr>
			      <tr id="writePinTr" style="display:none">
			      	<td align="right"><view:LanguageTag key="tkn_pin"/><span class="text_Hong_Se" id="span1">*</span><view:LanguageTag key="colon"/></td>
			        <td>
			          <input type="text" id="pin" name="pin" class="formCss100"  value=""> 
			        </td>
			        <td><div id="pinTip" style="width:100%"></div></td>
			      </tr>
			      <tr><td height="5"></td></tr>
				  <tr>
			         <td align="right"><view:LanguageTag key="tkn_dist_result"/><view:LanguageTag key="colon"/></td>
			         <td>
			            <input type="radio" name="result" id="result" value="0" checked/><view:LanguageTag key="tkn_dist_page_download"/>
			            <!-- 令牌被用户绑定过，才可以通过邮件发送进行分发 -->
			            <c:if test="${not empty tokenInfo.userIds}">
			            	<c:if test="${fn:length(tokenInfo.userIds) == 1}">
			               		<input type="radio" name="result" id="result" value="1" /><view:LanguageTag key="tkn_dist_email"/>
			               	</c:if>
			            </c:if>
			         </td>
			         <td></td>
			      </tr>
			      <tr>
			        <td>&nbsp;</td>
			        <td><span style="float:left;width:40%;"></span>
				        <a href="#" id="addObj" style="display:none" class="button"><span><view:LanguageTag key="tkn_dist"/></span></a>
					</td>
					<td></td>
			      </tr>
			    </table>
			</li>
			<!-- 绑定管理员/用户 -->
		    <li>
		    	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
			     <!-- 绑定管理员  -->
	             <tr>
	               <td width="25%" align="right" valign="top"><view:LanguageTag key='admin_info'/><view:LanguageTag key="colon"/></td>
	               <td width="75%" id="adminTd">
	                <c:if test="${empty tokenInfo.userIds}"><view:LanguageTag key='common_syntax_nothing'/></c:if>
					<c:forEach items="${tokenInfo.userIds}" var="u">
	               		<c:if test="${u.domainId == null}">
		               		${u.userId}<br>
					    </c:if>
	               	</c:forEach>
				   </td>
	             </tr>  
	             <!-- 绑定用户  -->
	             <tr>
	               <td width="25%" align="right" valign="top"><view:LanguageTag key='tkn_dist_info_user'/><view:LanguageTag key="colon"/></td>
	               <td width="75%" id="userTd">
	                <c:if test="${empty tokenInfo.userIds}"><view:LanguageTag key='common_syntax_nothing'/></c:if>
					<c:forEach items="${tokenInfo.userIds}" var="u">
					    <c:if test="${u.domainId != null}">
		               		${u.userId}<br>
					    </c:if>
	               	</c:forEach>
				   </td>
	             </tr>             
		        </table>
			</li>
	 	</ul>
	</td>
  </tr>
</table>
</form>
</body>
</html>