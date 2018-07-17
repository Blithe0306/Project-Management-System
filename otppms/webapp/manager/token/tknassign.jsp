<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
	String fromTag = request.getParameter("fromTag"); //从单用户绑定令牌链接到到这里 其他均没有这一标记
	String token = request.getParameter("token");
	String domainId = request.getParameter("domainId");
	String orgunitId = request.getParameter("orgunitId");
	String domainOrgunitName = request.getParameter("domainOrgunitName");
%>
<html>
<head>
	<title><view:LanguageTag key="tkn_allot"/></title>
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
	<script type="text/javascript" src="<%=path%>/manager/token/js/tknAssignList.js"></script>	
    	
	<script language="javascript" type="text/javascript">
	// Start,多语言提取
	var langEnable = '<view:LanguageTag key="tkn_comm_enable"/>';
	var langDisable = '<view:LanguageTag key="tkn_comm_disable"/>';
	var langLose = '<view:LanguageTag key="tkn_comm_lose"/>';
	var langRelieveLose = '<view:LanguageTag key="tkn_comm_relieve_lose"/>';
	var langLock = '<view:LanguageTag key="tkn_comm_lock"/>';
	var langUnlock = '<view:LanguageTag key="tkn_comm_unlock"/>';
	var langHardtkn = '<view:LanguageTag key="tkn_physical_hard"/>';
	var langMtkn = '<view:LanguageTag key="tkn_physical_mobile"/>';
	var langStkn = '<view:LanguageTag key="tkn_physical_soft"/>';
	var langSMStkn = '<view:LanguageTag key="tkn_physical_sms"/>';
	var langInvalid = '<view:LanguageTag key="tkn_comm_invalid"/>';
	var langDel = '<view:LanguageTag key="tkn_comm_delete"/>';
	
	// 列表
	var tknum_lang = '<view:LanguageTag key="tkn_comm_tknum"/>';
	var orgunit_lang = '<view:LanguageTag key="tkn_orgunit"/>';
	var physical_type_lang = '<view:LanguageTag key="tkn_comm_physical_type"/>';
	var invalid_lang = '<view:LanguageTag key="tkn_comm_invalid"/>';
	var valid_lang = '<view:LanguageTag key="tkn_valid"/>';
	
	// 操作
	var langYes = '<view:LanguageTag key="common_syntax_yes"/>';
	var langNo = '<view:LanguageTag key="common_syntax_no"/>';
	var sel_tkn_lang = '<view:LanguageTag key="tkn_vd_sel_tkn"/>';
	// End,多语言提取
	
	$(document).ready(function(){
		window.resizeBy(0,0);
		if(<%=fromTag%>=='1'){ //直接显示转到令牌列表 li  也就是在这里模拟执行 转之前的步骤逻辑 然后触发下一步事件
 			$('#assignTknCount').html('1'); //因为是一个用户
 			$('#assignTkn').html('<%=token+","%>');
 			$('#domaind').val('<%=domainId%>');
 			$('#orgunitNames').val('<%=domainOrgunitName%>');
			stepController(1);
 		}
	});
	
	//通列li的索引跳转到指定标签页，执行上一步，下一步的功能
	function stepController(index){
		$("#menu li.tabFocus").removeClass("tabFocus"); //移除已选中的样式
		$("#menu li:eq(" + index + ")").addClass("tabFocus");//增加当前选中项的样式
		$("#content li:eq(" + index + ")").show().siblings().hide();//显示选项卡对应的内容并隐藏未被选中的内容
		if(index == 0){
			$("#listDataAJAX").show(); 
		}else{
			$("#listDataAJAX").hide(); 
		}
	}
 
    //转入分配令牌
    function nextAssignTkn(){
       var orgunitIds = $('#orgunitIdsTemp').val();
       if(orgunitIds==''||orgunitIds==null){
          FT.toAlert('warn','<view:LanguageTag key="tkn_vd_sel_orgunit"/>',null);
          return;
       }
        
       var orgunit = orgunitIds.substring(0,orgunitIds.length-1);
       var dominds = orgunit.split(":");
       var oldOrgId = queryData.orgunit;
	   var oldDomain = queryData.domaind;
       if(dominds[1]==0){
          FT.toAlert('warn','<view:LanguageTag key="tkn_vd_sel_orgunit_error"/>',null);
          return;
       }
       if(<%=fromTag%>=='1'){// 列表后过来的分配
	  		oldOrgId = '<%=orgunitId%>';
	  		oldDomain = '<%=domainId%>';
	  	}
	  	if(dominds[1] == oldOrgId){
      	 	FT.toAlert('warn','<view:LanguageTag key="tkn_allot_org_same"/>',null);
         	return;
      	}
       
       $('#domaindTemp').val(dominds[0]);
       $('#orgunitTemp').val(dominds[1]);
       $('#assignOrginName').html($('#orgunitNamesTemp').val());
       stepController(3);
    }
    
    //点击令牌查询
    function queryTkn(){
       var orgunitIds = $('#orgunitIds').val();
       if(orgunitIds==''||orgunitIds==null){
          FT.toAlert('warn','<view:LanguageTag key="tkn_vd_sel_orgunit"/>',null);
          return;
       }
       var orgunit = orgunitIds.substring(0,orgunitIds.length-1);
       var dominds = orgunit.split(":");
       $('#domaind').val(dominds[0]);
       $('#orgunit').val(dominds[1]);
 
       query(false,true);
    }

    //转入分配结果
    function  assignTkn(){
      var orgunitIds = $('#orgunitIdsTemp').val();
      if(orgunitIds==''||orgunitIds==null){
          FT.toAlert('warn','<view:LanguageTag key="tkn_vd_sel_orgunit"/>',null);
          return;
      }
      
      var orgunit = orgunitIds.substring(0,orgunitIds.length-1);
      var dominds = orgunit.split(":");
      if(dominds[1]==0){
          FT.toAlert('warn','<view:LanguageTag key="tkn_vd_sel_orgunit_error"/>',null);
          return;
      }
       
      $('#domaindTemp').val(dominds[0]);
      $('#orgunitTemp').val(dominds[1]);
      $('#assignOrginName').html($('#orgunitNamesTemp').val());
    
   	  var tokenSel = $("#tokenOperSel ").val();
      var orgunit = $('#orgunitTemp').val();
      var assignTkn = $('#assignTkn').html();
      var oldOrgunit = $('#orgunitNames').val();
      var tokenSel = $("#tokenOperSel ").val();

	  // START,按条件查询方式
	  var token = queryData.token;
	  var tokenStart = queryData.tokenStart;
	  var tokenEnd = queryData.tokenEnd;
	  var oldOrgId = queryData.orgunit;
	  var oldDomain = queryData.domaind;
      var tknCountTotal = $("#assignTknCount").html();// 当前查询结果列表的数据总数
      if(<%=fromTag%>=='1'){// 列表后过来的分配
	  	  oldOrgId = '<%=orgunitId%>';
	  	  oldDomain = '<%=domainId%>';
	  }
	  if(dominds[1] == oldOrgId){
      	  FT.toAlert('warn','<view:LanguageTag key="tkn_allot_org_same"/>',null);
          return;
      }
      var ajaxbg = $("#background,#progressBar");//加载等待
	  ajaxbg.show();
	  // END,按条件查询方式
      $("#assignForm").ajaxSubmit({
		   async:true,  
		   dataType:"json",
		   type:"POST", 
		   url : "<%=path%>/manager/token/token!assignTkn.action?assignTkn=" + assignTkn + "&orgunit=" + orgunit + "&oldOrgunit=" +encodeURI(oldOrgunit)+ "&token=" + token+ "&tokenStart=" + tokenStart+ "&tokenEnd=" + tokenEnd+ "&oldOrgId=" + oldOrgId+ "&tokenSel=" + tokenSel+ "&oldDomain=" + oldDomain+ "&tokenSel=" + tokenSel+ "&tknCountTotal=" + tknCountTotal,
		   success:function(msg){
			    ajaxbg.hide();
				if(msg.errorStr == 'success'){ 
				     $('#assignMsg').html(msg.object); 
				       	 //选择的令牌的个数
					 $('#assignTknCountRes').html($('#assignTknCount').html());
					 //组织机构
					 $('#assignOrginNameRes').html($('#assignOrginName').html());
					 $('#allotDiv').hide();
					 $('#resultDiv').show();
				 }else{
				     FT.toAlert(msg.errorStr,msg.object,null);
				} 
			}
	   });
    }
    
    function closeTkn(){
		window.parent.removeTabItemF('030110');
	}
    
	/**
	 * 打开大窗口令牌分配返回
	 * isRequery true:重查、false:只关闭窗口
	 */
	function goBack(isRequery) {
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

<body style="overflow:auto; overflow-y:hidden">
<div id="background"  class="background"  style="display: none; "></div>
<div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
<input type="hidden"  name="contextPath" value="<%=path%>" id="contextPath"/>
<input type="hidden"  name="currentPage" value="${param.currentPage}" id="currentPage"/> 

<form id="assignForm" name="assignForm" method="post" action=""  >
   <table width="100%" border="0" align="center"  cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
        <ul id="menu">
        	<li class="tabFocus" style="cursor: default<%if(fromTag!=null &&!fromTag.equals("")) {%>;display:none<%} %>"><view:LanguageTag key="tkn_sel_tkn"/></li>
			<li style="cursor: default"><view:LanguageTag key="tkn_allot"/></li>
			<li style="float:right"><a href="javascript:addAdmPermCode('030110','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a></li>
        </ul>
	    <ul id="content">
	    	  <!-- 令牌查询 -->
	    	 <li class="conFocus" style="<%if(fromTag!=null &&!fromTag.equals("")) {%>display:none<%} %>"> 
               <table width="800" border="0" cellspacing="0" cellpadding="0" style="margin-top:6px">
	               <tr>
	               	 <input id="domaind" name="tokenQueryForm.domaind" type=hidden value="${tokenQueryForm.domaind}"/>
				     <input id="orgunit" name="tokenQueryForm.orgunitid" type=hidden value="${tokenQueryForm.orgunitid}" />
				     <input id="orgunitIds" name="orgunitIds" type=hidden value="" />
	                 <td width="116" align="right"><view:LanguageTag key="common_title_orgunit"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
	                 <td width="168"><input type="text" id="orgunitNames" name="orguintNames" onClick="selOrgunits(1,'<%=path%>');" class="formCss100" readonly/></td>
	                 <td width="133" align="right"><view:LanguageTag key="tkn_comm_tknum"/><view:LanguageTag key="colon"/></td>
	                 <td width="168"><input type="text" id="tokenStr" name="tokenQueryForm.tokenStr"  value="${tokenQueryForm.tokenStr}"  class="formCss100" /></td>
	                 <td width="15">&nbsp;</td>
        			 <td width="200">&nbsp;</td>
	               </tr>
	               <tr>
	                 <td align="right"><view:LanguageTag key="tkn_start_num"/><view:LanguageTag key="colon"/></td>
	                 <td><input type="text"  id="tokenStart" name="tokenQueryForm.tokenStart" value="${tokenQueryForm.tokenStart}" class="formCss100" /></td>
	                 <td align="right"><view:LanguageTag key="tkn_stop_num"/><view:LanguageTag key="colon"/></td>
	                 <td><input type="text"  id="tokenEnd" name="tokenQueryForm.tokenEnd" value="${tokenQueryForm.tokenEnd}" class="formCss100" /></td>
	                 <td width="15">&nbsp;</td>
        			 <td width="200"><span style="float:left;display:inline-block;margin-top:4px" class="query-button-css"><a href="javascript:queryTkn();" class="isLink_LanSe"><view:LanguageTag key="common_syntax_query"/></a></span></td>
	               </tr>
                </table>
                <table width="100%" border="0" cellspacing="0" cellpadding="0" class="operTableBg">
                 	<tr>
                    	<td width="110px">&nbsp;&nbsp;
	                    	<view:LanguageTag key='common_syntax_operate'/><view:LanguageTag key="colon"/>
	                 	</td>
	                 	<td width="150px">
	                 		<select id="tokenOperSel" name="tokenOperSel" style="width:150px" class="select100_2">
					       		<option value="0"><view:LanguageTag key='common_syntax_this_page_date'/></option>
								<option value="1"><view:LanguageTag key='common_syntax_this_query_date'/></option>
		                	</select>
	                 	</td>
	                 	<td>&nbsp;</td>
	                 	<td width="120px">
	                 		<span style="float:right">
		                 		<a href="javascript:nextSelOrgunit()" class="button"><span><view:LanguageTag key="common_syntax_nextStep"/></span></a>&nbsp;&nbsp;&nbsp;&nbsp;
		                 	</span>
	                 	</td>
                   </tr>
                </table>
	    	  </li>
	    	<!-- 选择机构 -->
			  <li>
			  	<div id="allotDiv">
				     <table  width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnInsideTable"  >
				     	<tr>
						  <td width="35%" align="right">&nbsp;</td>
						  <td width="30%" align="left">&nbsp;</td>
						  <td width="35%" align="left">&nbsp;</td>
					   </tr>
				     	<tr>
						  <td width="35%" align="right" ><view:LanguageTag key="tkn_allot_org"/><span class="text_Hong_Se">*</span><view:LanguageTag key="colon"/></td>
						  <td width="30%" align="left">
						  	 <!-- 单个域Id -->
	 						<input id="domaindTemp" name="domaind" type=hidden value=""/>
	 						<!-- 单个组织机构Id -->
						     <input id="orgunitTemp" name="orgunitid" type=hidden value="" />
						     <input id="orgunitIdsTemp" name="orgunitIds" type=hidden value="" />
						     <input id="orgunitNamesTemp" name="orgunitNames" onClick="selOrgunits(4,'<%=path%>');" value="" class="form-text" style="width:200px;" readonly/>
						  </td>
						  <td width="35%" align="left"></td>
					   </tr>
					   <tr>
					   	  <td align="right"></td>
					      <td align="left">
							  	<%if(fromTag==null || "".equals(fromTag)){ %>
							  	
						   	  	<%}else{%>	
						 			<a href="#" onClick="goBack(false);"  class="button"><span><view:LanguageTag key="common_syntax_return"/></span></a>
						   	  	<%}%>
			             	  	<% //过滤 用户绑定来源 
									if(fromTag==null || "".equals(fromTag)){ 
							  	%>
									<span><a href="#" onClick="stepController(0)" class="button"><span><view:LanguageTag key="common_syntax_preStep"/></span></a></span>
						     	<%} %>
							    <a href="#" onClick="assignTkn()" class="button"><span><view:LanguageTag key="tkn_allot_allot"/></span></a> 
					     </td>
						  <td  align="left"></td>
					   </tr>
					</table>
				</div>
				<div style="display:none">
			     	<table  width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnInsideTable"  >
						<tr>
						  <td width="35%" align="right" ><view:LanguageTag key="tkn_allot_org"/><view:LanguageTag key="colon"/></td>
						  <td width="30%" align="left"><div id="assignOrginName"></div></td>
						  <td width="35%" align="left"></td>
					 	</tr>
					  	<tr>
						  <td align="right" > <view:LanguageTag key="tkn_allot_tkn"/><view:LanguageTag key="colon"/></td>
						  <td align="left"><span id="assignTknCount" style="float:left"></span><span>&nbsp;<view:LanguageTag key="tkn_comm_one"/></span></td>
						  <td align="left"><div id="assignTkn" style="display:none"></div></td>
					 	</tr>
					</table>
		      	</div>
				<div id="resultDiv" style="display:none">
					<table  width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnInsideTable" >
					   <tr>
						  <td width="35%" align="right">&nbsp;</td>
						  <td width="30%" align="left">&nbsp;</td>
						  <td width="35%" align="left">&nbsp;</td>
					   </tr>
					   <tr>
						  <td width="35%" align="right" ><view:LanguageTag key="tkn_allot_org"/><view:LanguageTag key="colon"/></td>
						  <td width="30%" align="left"><div id="assignOrginNameRes"></div></td>
						  <td width="35%" align="left"></td>
					   </tr>
					   <tr>
						  <td align="right" > <view:LanguageTag key="tkn_allot_by"/><view:LanguageTag key="colon"/></td>
						  <td align="left"> <span id="assignTknCountRes" style="float:left"></span><span>&nbsp;<view:LanguageTag key="tkn_comm_one"/></span></td>
						  <td align="left"> </td>
					   </tr>
					   <tr>
						  <td align="right" ><view:LanguageTag key="tkn_allot_result_view"/><view:LanguageTag key="colon"/></td>
						  <td align="left"><span id="assignMsg"></span></td>
						  <td align="left"> </td>
					   </tr>
					   <tr>
						   <td align="right" ></td>
						   <td align="left">
						 	  <%if(fromTag==null || "".equals(fromTag)){ %>
						 	  	  <a href="#" onClick="closeTkn()" class="button"><span><view:LanguageTag key="common_syntax_sure"/></span></a> 
						  		  <a href="<%=path%>/manager/token/tknassign.jsp"  class="button"><span><view:LanguageTag key="tkn_allot_org_continue"/></span></a> 
						  	  <%}else{%>
						 		  <a href="#" onClick="goBack(true);" class="button"><span><view:LanguageTag key="common_syntax_sure"/></span></a> 
						   	  <%}%>
						   </td>
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
<div id="listDataAJAX" style="<%if(fromTag!=null &&!fromTag.equals("")) {%>display:none<%} %>"></div>
</body>
</html>