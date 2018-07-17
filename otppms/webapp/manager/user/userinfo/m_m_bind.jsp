<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.ft.otp.common.ConfConstant"%>
<%@page import="com.ft.otp.util.conf.ConfDataFormat"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>

<%
	String path = request.getContextPath();
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	String maxBindTnks=ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER,ConfConstant.CORE_MAX_BIND_TOKENS);
	//String maxBindTnks = StrTool.intToString(CommonConfig.sysMaxbindtokens());
	// 每令牌最大可绑定用户数量
	String CORE_MAX_BIND_USERS = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER,ConfConstant.CORE_MAX_BIND_USERS);
	// 每用户最大可绑定令牌数量
	String CORE_MAX_BIND_TOKENS = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER,ConfConstant.CORE_MAX_BIND_TOKENS);
	// 令牌绑定后是否迁移至用户所在的组织机构下。0否，1是,CORE_CONF_MARK
	String TK_BIND_IS_CHANGE_ORG = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER,ConfConstant.TK_BIND_IS_CHANGE_ORG);
	String fromTag = request.getParameter("fromTag"); //从单用户绑定令牌链接到到这里 其他均没有这一标记
	String userId = request.getParameter("userId");
	String domainId = request.getParameter("domainId");
	String orgunitId = request.getParameter("orgunitId");
	userId=userId+":"+domainId+":"+orgunitId;
	String DOrgunitName = request.getParameter("DOrgunitName");
	if(DOrgunitName!=null){
		DOrgunitName = new String(DOrgunitName.getBytes("ISO8859_1"),"utf-8");
	}
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
	<link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
    <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.6.4.min.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
    <script src="<%=path%>/manager/common/js/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
    <script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script> 
    <script type="text/javascript" src="<%=path%>/manager/user/userinfo/template/bind_user.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/user/userinfo/template/bind_token.js"></script>
	<script language="javascript" type="text/javascript">
    
     	// Start,多语言信息
     	
     	// bind_user.js
		// 列表
		var account_lang = '<view:LanguageTag key="user_info_account"/>';
		var real_name_lang = '<view:LanguageTag key="user_info_real_name"/>';
		var orgunit_id_lang = '<view:LanguageTag key="user_orgunit_id"/>';
		var title_orgunit_lang = '<view:LanguageTag key="common_title_orgunit"/>';
		var email_lang = '<view:LanguageTag key="common_info_email"/>';
		var comm_bind_lang = '<view:LanguageTag key="tkn_comm_bind"/>';
		
		// 操作信息
		var langBound = '<view:LanguageTag key="tkn_state_bound"/>';
		var langUnbound = '<view:LanguageTag key="tkn_state_unbound"/>';
		var err_3_lang = '<view:LanguageTag key="user_vd_err_3"/>';
		var err_2_lang = '<view:LanguageTag key="user_vd_err_2"/>';
		var err_4_lang = '<view:LanguageTag key="user_vd_err_4"/>';
		
		// bind_token.js
		var langHardtkn = '<view:LanguageTag key="tkn_physical_hard"/>';
		var langMtkn = '<view:LanguageTag key="tkn_physical_mobile"/>';
		var langStkn = '<view:LanguageTag key="tkn_physical_soft"/>';
		var langSMStkn = '<view:LanguageTag key="tkn_physical_sms"/>';
		var langYes = '<view:LanguageTag key="common_syntax_yes"/>';
		var langNo = '<view:LanguageTag key="common_syntax_no"/>';
		
		// 列表
		var tknum_lang = '<view:LanguageTag key="tkn_comm_tknum"/>';
		var physical_type_lang = '<view:LanguageTag key="tkn_comm_physical_type"/>';
		var enable_lang = '<view:LanguageTag key="tkn_comm_enable"/>';
		var lock_lang = '<view:LanguageTag key="tkn_comm_lock"/>';
		var lose_lang = '<view:LanguageTag key="tkn_comm_lose"/>';
		
		var detail_info_lang = '<view:LanguageTag key="common_syntax_detail_info"/>';
		var orgunit_err_lang = '<view:LanguageTag key="user_token_orgunit_err"/>';
		var count_err_lang = '<view:LanguageTag key="user_token_bind_count_err"/>';
		var bind_count_lang = '<view:LanguageTag key="user_bind_count_err"/>';
		var select_err_lang = '<view:LanguageTag key="user_bind_select_err"/>';
     	// End,多语言信息
     	
     	$(document).ready(function(){
		   	$("#tokenListAjx").hide();
     		window.resizeBy(0,0);
     		$('#domaind').val('<%=domainId%>');
     		if(<%=fromTag%> == '1' || <%=fromTag%> == '2'){ //直接显示转到令牌列表 li  也就是在这里模拟执行 转之前的步骤逻辑 然后触发下一步事件
     			$('#userTotal').val("1"); //因为是一个用户
				$('#userArr').val('<%=userId+","%>');
				stepController(1);
     		}
     		
     		//回车查询事件
		   $('#ListForm').bind('keyup', function(event){
		   		if (event.keyCode=="13"){
		    		$('#query').click();
		   		}
		   });
     		
		})

	//通列li的索引跳转到指定标签页，执行上一步，下一步的功能
	function stepController(index){		
		$("#menu li.tabFocus").removeClass("tabFocus"); //移除已选中的样式
		$("#menu li:eq(" + index + ")").addClass("tabFocus");//增加当前选中项的样式
		$("#content li:eq(" + index + ")").show().siblings().hide();//显示选项卡对应的内容并隐藏未被选中的内容
		if(index == 0){
			$("#userListAjx").show();
			$("#tokenListAjx").hide();
		}else if(index == 1){
			$("#userListAjx").hide();
			$("#tokenListAjx").show();
		}else{
			$("#userListAjx").hide();
			$("#tokenListAjx").hide();
		}
	}  
      
		 
		/**
         * 绑定JS
         */
         //绑定初始化
		function bindIni(){
        	var usrlen = $('#userTotal').val();
         	var tknlen = $('#tokenTotal').val();
						
			$("#userNum").html(usrlen);
			$("#tokenNum").html(tknlen);
		 	
			if(usrlen != 1 && tknlen != 1){
				<% if("1".equals(CORE_MAX_BIND_TOKENS)){%>
					$("#maxMaxDiv_1").hide();
				<%}else{ %>	
					$("#maxMaxDiv_1").show();
				<%}%>	
				$("#maxMaxDiv").show();
				$("#oneUsTokenDiv").hide();
				$("#oneMaxDiv").hide();	
				$("#oneTokenDiv").hide();	
			}else{
				if(usrlen == 1 && tknlen == 1){
					$("#oneUsTokenDiv").show();
		 			$("#maxMaxDiv").hide();
		 			$("#maxMaxDiv_1").hide();
					$("#oneMaxDiv").hide();
					$("#oneTokenDiv").hide();	
				}else if(usrlen != 1 && tknlen == 1){
					$("#oneUsTokenDiv").hide();
	 				$("#maxMaxDiv").hide();
	 				$("#maxMaxDiv_1").hide();
					$("#oneMaxDiv").hide();	
					$("#oneTokenDiv").show();	
				}else{
					$("#oneUsTokenDiv").hide();
	 				$("#maxMaxDiv").hide();
	 				$("#maxMaxDiv_1").hide();
					$("#oneMaxDiv").show();	
					$("#oneTokenDiv").hide();	
				}			
			}
			
			<% if("1".equals(CORE_MAX_BIND_USERS) && "1".equals(CORE_MAX_BIND_TOKENS) && "1".equals(TK_BIND_IS_CHANGE_ORG)){%>
				$("#bindType6").attr("checked",true);
			<%}else{ %>	
				if(usrlen == 1 && tknlen == 1){
					$("#bindType1").attr("checked",true);
				}else if(usrlen == 1 && tknlen > 1){
					$("#bindType2").attr("checked",true);
				}else if(usrlen > 1 && tknlen > 1){
					$("#bindType3").attr("checked",true);
				}else if(usrlen > 1 && tknlen == 1){
					$("#bindType5").attr("checked",true);
				}
			<%}%>	
		}
         
        //绑定操作
		function bindUT(){
		    //绑定方式
		    var bindType = $("input[name='bindType']:checked").val();
	        if(bindType==null){
	          FT.toAlert('warn','<view:LanguageTag key="user_sel_bindType"/>', null);
	           return;
	         } 
			if(0 == $("#userNum").text() || 0 == $("#tokenNum").text()){
				FT.toAlert('warn','<view:LanguageTag key="user_vd_cannot_execute"/>', null);
				return;
			}
		 
         	var url='<%=path%>/manager/user/userinfo/userBind!batchBindUT.action';
         	//用户条件 多个
			var userIdStr = $.trim($("#userId").val());
			
			//令牌条件 多个
			var tokenStr = $.trim($("#token").val());
			
			//用户集合 userId:domainId,userId:domainId,形式
			var userArr = $('#userArr').val();
			//令牌集合
			 var tokenArr = $('#tokenArr').val();
			//操作方式选择
			var usrSel = $("#usrOperSel ").val();
			var tknSel = $("#tknOperSel ").val();
			// Start,用户查询
			var orgunitIds = $('#orgunitIds').val(); // 组织机构domainid:orgunitid
			var userId = $('#userId').val();   // 用户名
			var realName = $('#realName').val();  // 真实姓名
			var usbindState = $('#usbindState').val();  // 令牌状态
			// End,用户查询
			
			// Start,令牌查询
			var tokenStart = queryData.tokenStart;  // 令牌起始号段
			var tokenEnd = queryData.tokenEnd;	  // 令牌结束号段
			var producttype = queryData.producttype;	 // 令牌类型
			var pType = queryData.physicaltype;  //物理类型
			var bindState = queryData.bindState;   //令牌状态
			var token = queryData.token; 
			// End,令牌查询
			
			var ajaxbg = $("#background,#progressBar");//加载等待
			ajaxbg.show();
			$.post(url, {
				userId:userIdStr,
		        token:tokenStr,
		        usrOperSel:usrSel,
		        tknOperSel:tknSel,
		        bindType:bindType, 
		        userArr:userArr, 
		        tokenArr:tokenArr,
		        'queryForm.dOrgunitId':orgunitIds,
		        'queryForm.tokenEnd':tokenEnd,
		        'queryForm.tokenStart':tokenStart,
		        'queryForm.userId':userId,
		        'queryForm.bindState':bindState,
		        'queryForm.realName':realName,
		        'queryForm.usbindState':usbindState,
		        'queryForm.producttype':producttype,
		        'queryForm.token':token,
		        'queryForm.physicaltype':pType
		    },
		        function(msg){
		    	  ajaxbg.hide();
		          if(msg.object.result == -1){
		            bindFail(msg);
		          }else{
		            bindOK(msg);
		          }
		      }, "json");
		}
		
		//绑定确认
		function bindUTTips(){
			// 令牌数		
		    $.ligerDialog.confirm('<view:LanguageTag key="user_validate_confirm_bind"/>', function (yes){
		     if(yes){
		     	// 绑定数量判断、 用户或令牌数大于100时给出提示 ：加上数量多需要执行一段时间的提示
				var numTip = "";
				// 用户数  令牌数
				if(parseInt($("#userNum").text())>100||parseInt($("#tokenNum").text())>100){
					numTip = '<view:LanguageTag key="user_validate_confirm_bind_num"/>';
				}
				
				if(numTip!=""){
					$.ligerDialog.confirm(numTip, function (yesNum){
		     		if(yesNum){
		     			bindUT();
		     		}});
				}else{
			    	bindUT();
			    }
		     }
		 });
		}
		
        //绑定失败
        function bindFail(msg){
        	var str = msg.object.operMsg;
        	var tips = "";
        	if(str != null && str != ""){
	        	if(str == 'oper_err01'){
	        		tips = '<view:LanguageTag key="user_vd_reach_user_limit"/>';
	        	}else if(str == 'oper_err02'){
	        		tips = '<view:LanguageTag key="user_vd_reach_tkn_limit"/>';
	        	}else if(str == 'oper_err03'){
	        		tips = '<view:LanguageTag key="user_vd_isbinding"/>';
	        	}else if(str == 'oper_err04'){
	        		tips = '<view:LanguageTag key="user_vd_isnull"/>';
	        	}else if(str == 'oper_err07'){
	        		tips = '<view:LanguageTag key="user_vd_user_tkn_org_no_same"/>';
	        	}
        	}else{
        		tips = '<view:LanguageTag key="user_vd_limit_or_binding"/>';
        	}
        	FT.toAlert('warn',tips, null);
         
        }
        
        //绑定成功
		function bindOK(msg){
			$("#menu li").each(function(index) {
				var okTabIndex = 4;
				if(index == okTabIndex){
		        	$("#menu li.tabFocus").removeClass("tabFocus");
		            $(this).addClass("tabFocus");
		            $("#content li:eq(" + index + ")").show().siblings().hide();
		        }
		    });
		    $("#ok_userNum").html(msg.object.usrNumber);
			$("#ok_tokenNum").html(msg.object.tknNumber);
			if('<%=fromTag%>' == 1){ // 用户列表操作绑定令牌
				$('#bindDiv').hide();
				$.ligerDialog.success('<view:LanguageTag key="admin_bind_succ"/>', '<view:LanguageTag key="common_syntax_tip"/>',function(){
		         	goBackClose(true);
		         });
			}else if(<%=fromTag%> == '2'){ // 添加用户成功后绑定令牌
				$('#tokenListDiv').hide();
				$('#tokenListAjx').hide();
				$('#resultDiv').show();
			}else{  // 批量绑定令牌
				$('#bindDiv').hide();
				$('#resultDiv').show();
			}
		}
		
		//输出绑定结果，并下载生成的文件
		function outputUT(){
		  var bindType = $("input[name='bindType']:checked").val();
		  var outType = $("#output1").val();
		  var ok_userNum = $("#ok_userNum").text();
		  var ok_tokenNum = $("#ok_tokenNum").text();
		  if("" == ok_userNum || "" == ok_tokenNum){
				FT.toAlert('warn','<view:LanguageTag key="user_vd_output_oper"/>', null);
				return;
		   }
		 jQuery("#BindForm").ajaxSubmit({
			   async:false,  
			   type:"POST", 
			   url : "<%=path%>/manager/user/userinfo/userBind!batchBindOutPut.action?bindType="+bindType+"&outType="+outType+"&ok_userNum="+ok_userNum+"&ok_tokenNum="+ok_tokenNum,
			   success:function(msg){
					if(msg == "false"){
				        FT.toAlert('error','<view:LanguageTag key="user_out_err_or_no_content"/>', null); 					
					}else{
						window.location.href = "<%=path%>/manager/user/userinfo/userBind!downLoad.action?fileName="+msg;
					} 
				}
			});		
		}
		function goBack(){
			window.location.href = location.href;
			//window.location.href="<%=path%>/manager/user/userinfo/m_m_bind.jsp";
		}
		
		/**
		 * 打开大窗口绑定时 返回/关闭
		 * isRequery true:重查、false:只关闭窗口
		 */
		function goBackClose(isRequery) {
		  if(<%=fromTag%> == 2){
			  location.href="<%=path%>/manager/user/userinfo/add.jsp";
		  }else{
			  var topWin = FT.closeWinBig();
			  if(isRequery){
			  	topWin.currPage.query(true,true);
			  }
			  topWin.currPage = null;
		  }
		}
		
		// 提示组织机构为空，关闭窗口
		function closeOrgWin(object) {
			$.ligerDialog.success(object,'<view:LanguageTag key="common_syntax_tip"/>',function(){
				winOrgClose.close();
			});
		}
	//-->
	</script>
  </head>

  <body scroll="no" style="overflow:auto;overflow-y:hidden">
  <div id="background"  class="background"  style="display: none; "></div>
  <div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
  <input  type="hidden"   value=""  id="userTotal" /> 
  <input  type="hidden"   value=""  id="tokenTotal"/> 
  <input  type="hidden"   value=""  id="userArr"/>
  <input  type="hidden"   value=""  id="tokenArr" /> 
  <input  type="hidden"   value=""  id="domaind" /> 
  <input  type="hidden" name="contextPath" value="<%=path%>" id="contextPath" /> 
 <form name="BindForm" id="BindForm" method="post" action="">
	<table width="100%" border="0"   cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">		
		<ul id="menu">
		 <li class="tabFocus" style="cursor: default<%if(fromTag!=null &&!fromTag.equals("")) {%>;display:none<%} %>"><view:LanguageTag key="user_select_user"/></li>
		 <li style="cursor: default"><%if(fromTag!=null &&!fromTag.equals("")){%><view:LanguageTag key="user_tkn_bind"/><%}else{ %><view:LanguageTag key="user_tkn_query_sel"/><%}%></li>
		 <li style="cursor: default<%if(fromTag!=null &&!fromTag.equals("")) {%>;display:none<%} %>"><view:LanguageTag key="user_tkn_bind"/></li>
        </ul>
	    <ul id="content">
        <input type="hidden" id="orgunitIds" name="queryForm.dOrgunitId"  value="" />
        <!-- 用户列表 -->
	 	<li class="conFocus">
             <table width="800" border="0" cellspacing="0" cellpadding="0" style="margin-top:6px">
	            <tr>
	                 <td width="116" align="right"><view:LanguageTag key="domain_orgunit"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	                 <td width="168"><input type="text" id="orgunitNames" name="orguintNames" onClick="selOrgunits(7,'<%=path%>');" class="formCss100" readonly/></td>
	                 
	                 <td width="133" align="right"><view:LanguageTag key="user_info_account"/><view:LanguageTag key="colon"/></td>
	                 <td width="168"><input type="text" id="userId" name="queryForm.userId"  value="${queryForm.userId}"  class="formCss100" /></td>
	                 <td width="15">&nbsp;</td>
        			 <td width="200">&nbsp;</td>
	            </tr>
	            <tr>
	                 <td align="right"><view:LanguageTag key="user_info_real_name"/><view:LanguageTag key="colon"/></td>
	                 <td><input type="text" id="realName" name="queryForm.realName"  value="${queryForm.realName}"  class="formCss100" /></td>
	                 
	                 <td align="right"><view:LanguageTag key="user_state_bind"/><view:LanguageTag key="colon"/></td>
	                 <td>
	                 	<select id="usbindState" name="queryForm.usbindState" value="${queryForm.usbindState}" class="select100">
		              		<option value="0" <c:if test='${queryForm.usbindState == 0}'> selected</c:if>selected><view:LanguageTag key="tkn_comm_state_bind"/></option>
					  		<option value="2" <c:if test='${queryForm.usbindState == 2}'> selected</c:if>><view:LanguageTag key="tkn_state_bound"/></option>
					  		<option value="1" <c:if test='${queryForm.usbindState == 1}'> selected</c:if>><view:LanguageTag key="tkn_state_unbound"/></option>
				  		</select>
	                 </td>
	                 <td width="15">&nbsp;</td>
        			 <td width="200"><span style="float:left;display:inline-block;margin-top:4px" class="query-button-css"><a href="#" onClick="queryUsr(false,true)" class="isLink_LanSe"><span><view:LanguageTag key="common_syntax_query"/></span></a></span></td>
	             </tr>
             </table>
            
             <table width="100%" border="0" cellspacing="0" cellpadding="0" class="operTableBg">
                 <tr>
                   	<td width="110px">&nbsp;&nbsp;
                    	<view:LanguageTag key='common_syntax_operate'/><view:LanguageTag key="colon"/>
                 	</td>
                 	<td width="150px">
                 		<select id="usrOperSel" name="usrOperSel" style="width:150px" class="select100_2">
					       	<option value="0"><view:LanguageTag key='common_syntax_this_page_date'/></option>
							<option value="1"><view:LanguageTag key='common_syntax_this_query_date'/></option>
		                </select>
                 	</td>
                 	<td>&nbsp;</td>
                 	<td width="120px">
                 		<span style="float:right">
                   			<a href="javascript:stepQueryTnk();"  class="button">
	                   			<span>
	                   			<view:LanguageTag key="common_syntax_nextStep"/>
	                   			</span>
                   			</a>
                   		</span>
                 	</td>
                  </tr>
             </table>
   		 </li>
		 <!-- 令牌列表 -->
		 <li>
		 <div id="tokenListDiv">
			 <table width="800" border="0" cellspacing="0" cellpadding="0">
	           <tr>
	             <td align="right">
				    <!-- 用户绑定令牌 有组织机构查询条件 -->
		            <%if(domainId==null || "".equals(domainId)){%>
			        	<input type="hidden" id="orgunitIds_"   value=""/>
			        <%}else{%>
			        	<input type="hidden" id="orgunitIds_" value="<%=domainId %>:<%=orgunitId %>," />
			        <%}%>
		         <%if(domainId==null || "".equals(domainId)){%>   
		            <span style="float:left"><input type="hidden" id="orgunitNames_" onClick="selOrgunits(1,'<%=path%>');" style="width:240px" class="formCss" readonly/></span>
	             <%}else{%>
	        		<span style="float:left"><input type="hidden" id="orgunitNames_" value="<%=DOrgunitName %>" onClick="selOrgunits('1', '<%=path%>');" style="width:240px" class="formCss" readonly/></span>
	       		 <%}%>
				 <view:LanguageTag key="tkn_comm_tknum"/><view:LanguageTag key="colon"/>			 </td>
	             <td><input type="text" id="token" name="queryForm.token" value="${queryForm.token}" class="formCss100"/></td>
	             <td align="right"><view:LanguageTag key="tkn_start_stop_num"/><view:LanguageTag key="colon"/></td>
	             <td colspan="2"><input type="text" id="tokenStart" name="queryForm.tokenStart" value="${queryForm.tokenStart}" style="width:150px" class="formCss100"/>
	            	--
	            	<input type="text" id="tokenEnd" name="queryForm.tokenEnd" value="${queryForm.tokenEnd}" style="width:150px" class="formCss100"/></td>
	             </tr>
	           <tr>
	             <td width="120" align="right"><view:LanguageTag key="tkn_comm_physical_type"/><view:LanguageTag key="colon"/></td>
	             <td width="150"><select id="pType" name="queryForm.physicaltype" value="${queryForm.physicaltype}" class="select100">
					  <option value="-1" <c:if test='${queryForm.physicaltype==-1}'> selected</c:if>><view:LanguageTag key="common_syntax_whole"/></option>
					  <option value="0"  <c:if test='${queryForm.physicaltype == 0}'> selected</c:if>><view:LanguageTag key="tkn_physical_hard"/></option>
					  <option value="1"  <c:if test='${queryForm.physicaltype == 1}'> selected</c:if>><view:LanguageTag key="tkn_physical_mobile"/></option>
					  <option value="2"  <c:if test='${queryForm.physicaltype == 2}'> selected</c:if>><view:LanguageTag key="tkn_physical_soft"/></option>
					  <option value="6"  <c:if test='${queryForm.physicaltype == 6}'> selected</c:if>><view:LanguageTag key="tkn_physical_sms"/></option>
				   </select></td>
	             <td width="180" align="right"><view:LanguageTag key='tkn_comm_state_bind'/><view:LanguageTag key="colon"/></td>
	             <td width="150"><select id="bindState" name="queryForm.bindState" value="${queryForm.bindState}" class="select100">
		              <option value="-1" <c:if test='${queryForm.bindState == -1}'> selected</c:if>><view:LanguageTag key="common_syntax_whole"/></option>
					  <option value="1"  <c:if test='${queryForm.bindState == 1}'> selected</c:if>><view:LanguageTag key="tkn_state_bound"/></option>
					  <option value="0"  <c:if test='${queryForm.bindState == 0}'> selected</c:if> selected><view:LanguageTag key="tkn_state_unbound"/></option>
				  </select></td>
	             <td width="200"><span style="float:right">
				  		<span style="display:inline-block" class="query-button-css"><a href="#" onClick="query(false,true)" class="isLink_LanSe"><span><view:LanguageTag key="common_syntax_query"/></span></a></span>&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
	           </tr>
	         </table>		 
			 <!-- Output elements -->		 
		     <table width="100%" border="0" cellspacing="0" cellpadding="0" class="operTableBg">
	           <tr>
	             <td width="50%">&nbsp;&nbsp;<view:LanguageTag key="common_syntax_operate"/><view:LanguageTag key="colon"/>
		              <select id="tknOperSel" name="tknOperSel" style="width:125px" class="select100">
		            	<option value="0"><view:LanguageTag key="common_syntax_this_page_date"/></option>
						<option value="1"><view:LanguageTag key="common_syntax_this_query_date"/></option>
		              </select></td>
	             <td width="50%"><span style="float:right">
			          <%if(fromTag==null || "".equals(fromTag)){ %>
			          	<a href="javascript:stepController(0);"    class="button"><span><view:LanguageTag key="common_syntax_preStep"/></span></a>
			          	<a href="javascript:stepBindTnk(<%=orgunitId%>,<%=CORE_MAX_BIND_USERS%>,<%=CORE_MAX_BIND_TOKENS%>,1);" class="button"><span><view:LanguageTag key="common_syntax_nextStep"/></span></a>
				   	  <%}else{%>	
				 		<a href="#" onClick="goBackClose(false);"  class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a>
				 		<a href="javascript:stepBindTnk(<%=orgunitId%>,<%=CORE_MAX_BIND_USERS%>,<%=CORE_MAX_BIND_TOKENS%>,2);" class="button"><span><view:LanguageTag key="user_bind_binding"/></span></a>
				   	  <%}%>
				  </td>
	           </tr>
	         </table>
         </div>
		</li>
		
		<!-- 绑定页 -->
		 <li>
		 	<div id="bindDiv">
			 	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
			 	 <tr>
	               <td width="30%">&nbsp;</td>
	               <td width="70%">&nbsp</td>
	             </tr>
	             <tr>
	               <td width="30%">&nbsp;</td>
	               <td width="70%"><view:LanguageTag key="user_this_sel_user"/><span id="userNum"></span><view:LanguageTag key="user_bind_a_token"/><span id="tokenNum"></span><view:LanguageTag key="user_sel_click"/></td>
	             </tr>
	             <% if(!"1".equals(CORE_MAX_BIND_USERS) && !"1".equals(CORE_MAX_BIND_TOKENS) && "1".equals(TK_BIND_IS_CHANGE_ORG)){%>
	             <tr>
	               <td>&nbsp;</td>
	               <td><view:LanguageTag key="user_bind_mode"/></td>
	             </tr>
	             <%}%>     
	             <tr>
	               <td>&nbsp;</td>
	                <td>
	                <% if("1".equals(CORE_MAX_BIND_USERS) && "1".equals(CORE_MAX_BIND_TOKENS) && "1".equals(TK_BIND_IS_CHANGE_ORG)){%>
	                	<input type="radio" id="bindType6" name="bindType" value="6"/><view:LanguageTag key="user_bind_one_tkn" /><br/>
	                <%}else{ %>
	                <div id="oneUsTokenDiv">
	                	<input type="radio" id="bindType1" name="bindType" value="1"/><view:LanguageTag key="user_bind_one_tkn" /><br/>
	                </div>
	                <div id="oneTokenDiv">
	                	<input type="radio" id="bindType5" name="bindType" value="5"/><view:LanguageTag key="user_bind_manyuser_tkn"/>
	                </div>
	                <div id="oneMaxDiv">
	                	<input type="radio" id="bindType1" name="bindType" value="1"/><view:LanguageTag key="user_bind_one_tkn" /><br/>
	                 	<input type="radio" id="bindType2" name="bindType" value="2"/><view:LanguageTag key="user_bind_many_tkn" replaceVal="<%=maxBindTnks%>" /><br/>
	                 </div>
	                 <div id="maxMaxDiv">
	                 	<input type="radio" id="bindType3" name="bindType" value="3"/><view:LanguageTag key="user_bind_one_to_one"/><br/>
	                </div>
	                <div id="maxMaxDiv_1">
	                	<input type="radio" id="bindType4" name="bindType" value="4"/><view:LanguageTag key="user_bind_limit_tkn" replaceVal="<%=maxBindTnks%>"/> 
	                </div>
	                <%}%> 
	                </td>
	             </tr>
	             <tr>
	               <td>&nbsp;</td>
	               <td>
	               <input type="hidden" name="bindCFirm" id="bindCFirm" value="" onClick="bindUT();" />
	               <a href="javascript:stepController(1);" class="button"><span><view:LanguageTag key="common_syntax_preStep"/></span></a>
	               <a href="javascript:bindUTTips();" class="button"><span><view:LanguageTag key="user_bind_binding"/></span></a>               </td>
	             </tr>
	           </table>
           </div>
		 </li>
		 
        </ul>
        
        <!-- 结果页 -->
           <div id="resultDiv" style="display:none">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
		            <tr>
		            	<td width="30%" align="right">&nbsp;</td>
		               	<td width="70%">&nbsp;</td>
		            </tr>
		       		<tr>
		            	<td width="30%" align="right">&nbsp;</td>
		               	<td width="70%"><view:LanguageTag key="user_bind_complete_succ"/><span id="ok_userNum"></span><view:LanguageTag key="page_records"/></td>
		            </tr>
		            <tr style="display:none;">
		            	<td width="30%" align="right">&nbsp;</td>
		             	<td width="70%"><span id="ok_tokenNum"></span></td>
		            </tr>
		            <tr>
		               	<td><input type="hidden" id="output1" name="output" value="1" checked/></td>
		            </tr>
		            <tr>
		               	<td align="right">&nbsp;</td>
		               	<td>
			                <a href="javascript:outputUT();" class="button"><span><view:LanguageTag key="common_syntax_bind_message"/></span></a>   
						 	<%if(fromTag==null || "".equals(fromTag)){ %>
						 		<a href="<%=path%>/manager/user/userinfo/m_m_bind.jsp" class="button"><span><view:LanguageTag key="common_syntax_bind"/></span></a>  
						   	<%}else if("1".equals(fromTag)){%>	
						 		<a href="#" onClick="goBackClose(true);" class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a> 
						   	<%}else if("2".equals(fromTag)){%>	
						 		<a href="<%=path%>/manager/user/userinfo/add.jsp"  class="button"><span><view:LanguageTag key="common_syntax_continue_add"/></span></a> 
						   	<%}%>
					   	</td>
		        	</tr>
		        </table>
	        </div>
	        
		</td>
      </tr>
  </table>
  </form>
  <div id="userListAjx"></div>
  <div id="tokenListAjx"></div>
  </body>
</html>