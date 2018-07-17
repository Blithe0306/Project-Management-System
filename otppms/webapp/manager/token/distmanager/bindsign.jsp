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
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
    <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>    
 	<script src="<%=path%>/manager/common/js/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/token/distmanager/js/formatudid.js"></script>	
	<script language="javascript" type="text/javascript">
	 $(function() {
		// 禁用回车
		$("td :input").keydown(function(e) {
			if(e.keyCode == '13') {
				return false;
			}
		});
		 
	    $.formValidator.initConfig({
        	submitButtonID:"addObj",
	        debug:true,
	        onSuccess:function(){
	           addObj();
	        },
           	onError:function(){
               return false;
            }
        });
		$("#phoneudid").formValidator({onFocus:"<view:LanguageTag key='tkn_vd_phoneudid_show'/>",onCorrect:"OK",onEmpty:"OK"}).functionValidator({
		 	fun:function(pudid){
		 		var udid = Trim($.trim(pudid),'g');
		 		if (udid.length != 20) {
		 			return "<view:LanguageTag key='tkn_vd_phoneudid_error'/>";
		 		}
		 		if(!checkTextDataForNUMBER(udid)){  
			     	return '<view:LanguageTag key="tkn_vd_phoneudid_error_1"/>';
		   	  	}
		   	  	return true;
		 	}
		 });    
		
		$("#phoneudid").focus();
		
		//udid输入格式化
		$('#phoneudid').inputFormat('formatUdid');
		//格式化显示
		var udid = '${distManagerInfo.phoneudid}';
		$("#phoneudid").val(udid.replace(/\s(?=\d)/g,'').replace(/(\d{5})(?=\d)/g,"$1 "));
    })
    
	//设定
	function addObj(){
		$("#addForm").ajaxSubmit({ 
			async:false,  
			dataType:"json",
			type:"POST", 
			url : "<%=path%>/manager/token/distmanager/distManager!modify.action",
			success:function(msg){
				if(msg.errorStr == 'success'){ 
					$.ligerDialog.success(msg.object,"<view:LanguageTag key='common_syntax_tip'/>",function(){
						location.href=   "<%=path%>/manager/token/distmanager/list.jsp?currentPage="+$('#currentPage').val();  
					});
				}else{
					FT.toAlert(msg.errorStr,msg.object,null);
				} 
			}
		});
	}
</script>
</head>

<body style="overflow:auto; overflow-x:hidden">
 <input type="hidden" name="currentPage" id="currentPage" value="${param.curPage}"/> 
 <form id="addForm" method="post" action="">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td>  
        	<span class="topTableBgText">
		  		<view:LanguageTag key='tkn_mobile_set_tkn_code'/>
		  	</span>	
        </td>
      </tr>
    </table>
 	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
		    <ul>
		    <li class="conFocus">
    		  <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
                <tr>
		          <td width="25%" align="right"><view:LanguageTag key="tkn_comm_tknum"/><view:LanguageTag key="colon"/></td>
		          <td width="30%">${distManagerInfo.token}
		            <input type="hidden" name="distManagerInfo.token" class="formCss100"   value="${distManagerInfo.token}"  />
		          </td>
		          <td width="45%">&nbsp;</td>
		        </tr>
       			<tr>
        		  <td align="right"><view:LanguageTag key="tkn_code"/><view:LanguageTag key="colon"/></td>
        		  <td>
        			<input type="text" name="distManagerInfo.phoneudid" class="formCss100" id="phoneudid" value="${distManagerInfo.phoneudid}"  />
        		  </td>
        		  <td class="divTipCss"><div id="phoneudidTip" style="width:100%"></div></td>
      		    </tr>
                <tr>
     			  <td align="right">&nbsp;</td>
      			  <td>
        	        <a href="#" id="addObj" class="button"><span><view:LanguageTag key="tkn_set"/></span></a>
        	      	<a href="<%=path%>/manager/token/distmanager/list.jsp" class="button"><span><view:LanguageTag key='common_syntax_return'/></span></a>
        		  </td>
                  <td></td>
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