<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
	String fromTag = request.getParameter("fromTag"); //从单用户绑定令牌链接到到这里 其他均没有这一标记
	String userId = request.getParameter("userId");
	String domainId = request.getParameter("domainId");
	String orgunitId = request.getParameter("orgunitId");
	String tokenSn = request.getParameter("tokenSn");
	String tknOrgunitSn = request.getParameter("tknOrgunitSn");
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
	<script type="text/javascript" src="<%=path%>/manager/user/userinfo/template/change_user.js"></script>	
	<script language="javascript" type="text/javascript">
	
	// Start,多语言信息
	var langBound = '<view:LanguageTag key="tkn_state_bound"/>';
	var langUnbound = '<view:LanguageTag key="tkn_state_unbound"/>';
	
	// 列表
	var account_lang = '<view:LanguageTag key="user_info_account"/>';
	var real_name_lang = '<view:LanguageTag key="user_info_real_name"/>';
	var orgunit_id_lang = '<view:LanguageTag key="user_orgunit_id"/>';
	var title_orgunit_lang = '<view:LanguageTag key="common_title_orgunit"/>';
	var email_lang = '<view:LanguageTag key="common_info_email"/>';
	var bind_lang = '<view:LanguageTag key="tkn_comm_bind"/>';
	
	// 操作
	var err_3_lang = '<view:LanguageTag key="user_vd_err_3"/>';
	var sel_user_lang = '<view:LanguageTag key="user_sel_user"/>';
	var bind_lang = '<view:LanguageTag key="tkn_comm_bind"/>';
	var bind_lang = '<view:LanguageTag key="tkn_comm_bind"/>';
	// End,多语言信息
    
	$(document).ready(function(){
		window.resizeBy(0,0);
		if(<%=fromTag%>=='1'){ //直接显示转到令牌列表 li  也就是在这里模拟执行 转之前的步骤逻辑 然后触发下一步事件
 			$('#assignUserCount').html('1'); //因为是一个用户
 			// 用户ID串
 			$('#assignUser').html('<%=userId+","%>');
			// 域ID
 			$('#domaind').val('<%=domainId%>');
 			// 机构ID
 			$('#orgunit').val('<%=orgunitId%>');
 			// 令牌串
 			$('#assignToken').html('<%=tokenSn%>');
 			// 令牌机构ID串
 			$('#assignTknOrg').html('<%=tknOrgunitSn%>');
			stepController(1);

			var assignToken = $('#assignToken').html();
			var assignUser = $('#assignUser').html();
			if(assignToken!=""){
				var url = '<%=path%>/manager/user/userinfo/userChange!checkToken.action?tokenArr=' + assignToken + '&assignUser=' + assignUser;
				$.post(url,
					function(msg){
						if(msg!=""){
							if(msg == "0"){
								$("#oneDiv").show();
								$("#twoDiv").show();
								$("#threeDiv").show();
							}else if(msg == "1"){
								$("#oneDiv").show();
								$("#twoDiv").hide();
								$("#threeDiv").show();
							}else{
								$("#oneDiv").show();
								$("#twoDiv").show();
								$("#threeDiv").hide();
							}
						}
					}, "text"
				);
			}
 		}
 		
 		//回车查询事件
	   $('#ListForm').bind('keyup', function(event){
	   		if (event.keyCode=="13"){
	    		$('#query').click();
	   		}
	   });
	});
	
	//通列li的索引跳转到指定标签页，执行上一步，下一步的功能
	function stepController(index){
		$("#menu li.tabFocus").removeClass("tabFocus"); //移除已选中的样式
		$("#menu li:eq(" + index + ")").addClass("tabFocus");//增加当前选中项的样式
		$("#content li:eq(" + index + ")").show().siblings().hide();//显示选项卡对应的内容并隐藏未被选中的内容
		if(index == 0){
			$("#userListAjx").show();
		}else {
			$("#userListAjx").hide();
		}
	}
 
    //转入选择机构
    function nextAssignTkn(){
       var orgunitIds = $('#orgunitIdsTemp').val();
       if(orgunitIds==''||orgunitIds==null){
          FT.toAlert('warn','<view:LanguageTag key="tkn_vd_sel_orgunit"/>',null);
          return;
       }

       //迁移的机 构ID和域ID
       var orgunit = orgunitIds.substring(0,orgunitIds.length-1);
       var dominds = orgunit.split(":");

	   //选择用户的机构ID和域ID
       var domaind = $('#domaind').val();
       var orgunitid = $('#orgunit').val();

       if(dominds[1] == orgunitid && domaind == dominds[0]){
    	   FT.toAlert('warn','<view:LanguageTag key="user_sel_no_same_org"/>',null);
           return;
       }
       
       $('#domaindTemp').val(dominds[0]);
       $('#orgunitTemp').val(dominds[1]);
       $('#assignOrginName').html($('#orgunitNamesTemp').val());
       bindIni();
       stepController(2);
    }
    
    //点击令牌查询
    function queryUse(){
       var orgunitIds = $('#orgunitIds').val();
       if(orgunitIds==''||orgunitIds==null){
          FT.toAlert('warn','<view:LanguageTag key="tkn_vd_sel_orgunit"/>',null);
          return;
       }
       var orgunit = orgunitIds.substring(0,orgunitIds.length-1);
       var dominds = orgunit.split(":");
       $('#domaind').val(dominds[0]);
       $('#orgunit').val(dominds[1]);
       queryUsr(false,true);
    }


    //变更初始化
	function bindIni(){
		var orgunit = $('#orgunit').val();
	    var domain = $('#domaind').val();
		var assignTknOrg = $('#assignTknOrg').html();
		var assignToken = $('#assignToken').html();
		var assignUser = $('#assignUser').html();
		var assignTknOrgArr = assignTknOrg.split(','); 

		if(<%=fromTag%>=='1'){
			// 绑定令牌全为域下令牌
			if(assignToken != ""){
				// 绑定令牌全为域下令牌
				var flag = false;
				for (var i=0; i<assignTknOrgArr.length; i++){
					if(assignTknOrgArr[i]!= 0){
						flag = true;
						break;
					}
				}
			}
			
			// 域下用户（令牌也是域下）或未绑定令牌
			if(orgunit == 0 || assignToken == "" || flag == false){
				$("#zeroDiv").show();
				$("#oneDiv").hide();
				$("#twoDiv").hide();
				$("#threeDiv").hide();
				$("#changeType0").attr("checked",true);
			}else{
				$("#zeroDiv").hide();
			}
		}else{
			$("#zeroDiv").hide();
			$("#oneDiv").show();
			$("#twoDiv").show();
			$("#threeDiv").show();
		}
	}

    //转入分配结果
    function assignUser(){
      var ajaxbg = $("#background,#progressBar");//加载等待
      var orgunit = $('#orgunitTemp').val();
      var domain = $('#domaindTemp').val();
      var assignUser = $('#assignUser').html();
      var assignToken = $('#assignToken').html();
      var assignTknOrg = $('#assignTknOrg').html();
	  var changeType = $("input[name='changeType']:checked").val();
      if(changeType == null){
         FT.toAlert('warn','<view:LanguageTag key="user_sel_changeType"/>', null);
         return;
      } 
      
	  var usrOperSel = $('#usrOperSel').val();
	  var dOrgunitId = queryData.dOrgunitId;
	  var userId = queryData.userId;
      var usbindState = queryData.usbindState;
      var assignUserCount = $("#assignUserCount").html();
      $.ligerDialog.confirm('<view:LanguageTag key="user_validate_confirm_change"/>', function (yes){
    	  if(yes){
    		  ajaxbg.show();
		      $("#assignForm").ajaxSubmit({
		    	   async:true,  
				   type:"POST", 
				   url : "<%=path%>/manager/user/userinfo/userChange!changeUser.action?assignUser=" + assignUser + "&orgunit=" + orgunit + "&assignUserCount=" + assignUserCount + "&domain=" + domain+ "&changeType=" + changeType+ "&assignToken=" + assignToken+ "&usrOperSel=" + usrOperSel+ "&dOrgunitId=" + dOrgunitId+ "&userId=" + userId+ "&usbindState=" + usbindState,
				   success:function(msg){
					  ajaxbg.hide();
					  if(msg == 'false'){
						  FT.toAlert('error','<view:LanguageTag key="user_change_error_tip"/>', null);
					  }else{
						  $('#assignMsg').html(msg); 
						  //选择的用户的个数
						  $('#assignUserCountRes').html($('#assignUserCount').html());
						  //组织机构
						  $('#assignOrginNameRes').html($('#assignOrginName').html());
					      $('#userDiv').hide();
					 	  $('#resultDiv').show();
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
	
	// 提示组织机构为空，关闭窗口
	function closeOrgWin(object) {
		$.ligerDialog.success(object,'<view:LanguageTag key="common_syntax_tip"/>',function(){
			winOrgClose.close();
		});
	}
</script>
</head>

<body style="overflow:auto;overflow-y:hidden">
<div id="background"  class="background"  style="display: none; "></div>
<div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
<input type="hidden"  name="contextPath" value="<%=path%>" id="contextPath"/>
<input type="hidden"  name="currentPage" value="${param.currentPage}" id="currentPage"/> 

 <form id="assignForm" name="assignForm" method="post" action=""  >
    <table width="100%" border="0" align="center"  cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
        <ul id="menu">
        	<li class="tabFocus" style="cursor: default<%if(fromTag!=null &&!fromTag.equals("")) {%>;display:none<%} %>"><view:LanguageTag key="user_select_user"/></li>
			<li style="cursor: default"><view:LanguageTag key="tkn_sel_org"/></li>
			<li style="cursor: default"><view:LanguageTag key="user_orgunit_change"/></li>
			<li style="float:right"><a href="javascript:addAdmPermCode('020111','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a></li>
        </ul>
	    <ul id="content">
	    
			<!-- 用户查询 -->
		    <li class="conFocus">
	             <table width="800" border="0" cellspacing="0" cellpadding="0" style="margin-top:6px">
	               <tr>
	               	 <input id="domaind" name="queryForm.domaind" type=hidden value="${queryForm.domaind}"/>
			     	 <input id="orgunit" name="queryForm.orgunitid" type=hidden value="${queryForm.orgunitid}" />
			     	 <input id="orgunitIds"   name="orgunitIds" type=hidden value="" />
			     	 
	                 <td width="116" align="right"><view:LanguageTag key="common_title_orgunit"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	                 <td width="168"><input type="text" id="orgunitNames" name="orgunitNames" onClick="selOrgunits(1,'<%=path%>');" class="formCss100" readonly/></td>
	                 
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
        			 <td width="200"><span style="float:left;display:inline-block;margin-top:4px" class="query-button-css"><a href="javascript:queryUse();" class="isLink_LanSe"><view:LanguageTag key="common_syntax_query"/></a></span></td>
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
	                   			<a href="#" onClick="nextSelOrgunit()" class="button"><span><view:LanguageTag key="common_syntax_nextStep"/></span>&nbsp;&nbsp;</a>
	                   		</span>
	                 	</td>
                   </tr>
                </table>
		     </li>

			  <!-- 选择机构 -->
			  <li>
			     <table  width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
			     	 <tr>
						  <td width="35%" align="right" >&nbsp;</td>
						  <td width="30%" align="left">&nbsp;</td>
						  <td width="35%" align="left">&nbsp;</td>
					 </tr>
					 <tr>
						  <td width="25%" align="right" ><view:LanguageTag key="user_allot_org"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
						  <td width="30%" align="left">
						  	<!-- 单个域Id -->
	 						<input id="domaindTemp" name="domaind" type=hidden value=""/>
	 						<!-- 单个组织机构Id -->
						    <input id="orgunitTemp" name="orgunitid" type=hidden value="" />
						    <input id="orgunitIdsTemp" name="orgunitIds" type=hidden value="" />
						    <input id="orgunitNamesTemp" name="orgunitNames" onClick="selOrgunits(4,'<%=path%>');" value="" class="form-text" style="width:200px;" readonly/>						  </td>
						  <td width="45%" align="left"></td>
					 </tr>
					 <tr>
					 	<td align="right" >						  </td>
						<td align="left">
							<%if(fromTag==null || "".equals(fromTag)){ %>
					   	  	<%}else{%>	
					 			<!-- <a href="<%=path%>/manager/user/userinfo/list.jsp?mode=4"  class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a> -->
					 			<a href="#" onClick="goBackClose(false);"  class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a> 
					   	  	<%}%>
		             	  	<%
								//过滤 用户绑定来源 
								if(fromTag==null || "".equals(fromTag)){ 
						  	%>
								<span><a href="#" onClick="stepController(0)" class="button"><span><view:LanguageTag key="common_syntax_preStep"/></span></a></span>
					     	<%} %>
						   <a href="#" onClick="nextAssignTkn()" class="button"><span><view:LanguageTag key="common_syntax_nextStep"/></span></a>						  </td>
						<td  align="left"></td>
					</tr>
				</table>
		     </li>
	    
	        <!-- 用户变更 -->
	        <li>
	          <div id="userDiv">
			      <table  width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnInsideTable"  >
					<tr>
						 <td width="35%" align="right">&nbsp;</td>
						 <td width="50%" align="left">&nbsp;</td>
						 <td width="15%" align="left">&nbsp;</td>
					</tr>
					<tr>
						  <td width="35%" align="right" ><view:LanguageTag key="user_allot_org"/><view:LanguageTag key="colon"/></td>
						  <td width="50%" align="left"><div id="assignOrginName"></div></td>
						  <td width="15%" align="left"></td>
					</tr>
					<tr>
						  <td align="right" > <view:LanguageTag key="user_wait_change_orgunit"/><view:LanguageTag key="colon"/></td>
						  <td align="left"><span id="assignUserCount" style="float:left"></span><span>&nbsp;<view:LanguageTag key="user_common_one"/><view:LanguageTag key="user_select_type_change"/></span></td>
						  <td align="left"><div id="assignUser" style="display:none"></div></td>
						  <td align="left"><div id="assignToken" style="display:none"></div></td>
						  <td align="left"><div id="assignTknOrg" style="display:none"></div></td>
					</tr>
					<tr> 
			        	<td>&nbsp;</td>
			            <td>
			            	<div id="zeroDiv">
			                	<input type="radio" id="changeType0" name="changeType" value="0"/><view:LanguageTag key="user_change_type_zero"/><br/>
			                </div>
			                <div id="oneDiv">
			                	<input type="radio" id="changeType1" name="changeType" value="1"/><view:LanguageTag key="user_change_type_one"/><br/>
			                </div>
			                <div id="twoDiv">
			                	<input type="radio" id="changeType2" name="changeType" value="2"/><view:LanguageTag key="user_change_type_two"/><br/>
			                </div>
			                <div id="threeDiv">
			                	<input type="radio" id="changeType3" name="changeType" value="3"/><view:LanguageTag key="user_change_type_three"/><br/>
			                </div>
			            </td>
		            </tr>
					<tr>
						<td align="right"></td>
						<td align="left">
							<a href="#" onClick="stepController(1)" class="button"><span><view:LanguageTag key="common_syntax_preStep"/></span></a> 
						  	<a href="#" onClick="assignUser()" class="button"><span><view:LanguageTag key="user_change_orgunit_one"/></span></a>					   </td>
						<td align="left"></td>
					</tr>
				 </table>
			 </div>
			 <!-- 分配结果 -->
			 <div id="resultDiv" style="display:none">
				 <table  width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnInsideTable"  >
				 	<tr>
						  <td width="35%" align="right" >&nbsp;</td>
						  <td width="30%" align="left">&nbsp;</td>
						  <td width="35%" align="left">&nbsp;</td>
					 </tr>
					 <tr>
						  <td width="35%" align="right" ><view:LanguageTag key="user_allot_org"/><view:LanguageTag key="colon"/></td>
						  <td width="30%" align="left"><div id="assignOrginNameRes"></div></td>
						  <td width="35%" align="left"></td>
					 </tr>
					 <tr>
						  <td align="right" valign="top"><view:LanguageTag key="tkn_allot_result_view"/><view:LanguageTag key="colon"/></td>
						  <td align="left"><span id="assignMsg"></span></td>
						  <td align="left"> </td>
					 </tr>
					  <tr>
						  <td align="right" ></td>
						  <td align="left">
						  	<%if(fromTag==null || "".equals(fromTag)){ %>
						  		<a href="<%=path%>/manager/user/userinfo/m_m_changeOrg.jsp"  class="button"><span><view:LanguageTag key="common_syntax_finish"/></span></a> 
						  	<%}else{%>	
						 		<!-- <a href="<%=path%>/manager/user/userinfo/list.jsp?mode=4"  class="button"><span><view:LanguageTag key="common_syntax_finish"/></span></a> -->
						 		<a href="#" onClick="goBackClose(true);" class="button"><span><view:LanguageTag key="common_syntax_finish"/></span></a>
						   	<%}%>					  </td>
						  <td align="left"> </td>
					 </tr>
				</table>
			 </div>
	       </li>
        </ul>
		</td>
      </tr>
    </table>
  </form>
  <div id="userListAjx"></div>
</body>
</html>