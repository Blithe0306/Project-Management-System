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
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
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
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js" charset="UTF-8"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" charset="UTF-8"></script>  

	<script language="javascript" src="<%=path%>/manager/data/js/loginTest.js" charset="UTF-8"></script>
		
    <script language="javascript" type="text/javascript">   	
    //初始表单信息 或 组件函数 
	$(document).ready(function(){
		
		$("#updatConfigBtn").click(function(){
				linkPage("edit");
		});
		
        $.formValidator.initConfig({
      		submitButtonID:"addBtn",
      		debug:true,
			onSuccess:function(){
				submitForm();
			}, 
			onError:function(){
				return false;
			}
		});	
		
    });
    
    //提交表单
    function submitForm(){
		var isRemote=$("input[name=dbBakConfInfo.isremote]:hidden").val(); 
		if(isRemote==1 || isRemote=="1"){//如果是远程
			login();
			if(loginResult!="success"){//如果没有登录远程成功
				FT.toAlert(loginResult,alertMessage,null);
				return ;
			}
		}
		
		//等待框
		var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
    	$("#AddForm").ajaxSubmit({
			async:true,    
			dataType : "json",  
			type:"POST", 
			url : $("#contextPath").val() + "/manager/data/databak!bak.action",
			success:function(msg){
			     FT.toAlert(msg.errorStr,msg.object,null);
			     ajaxbg.hide();
			}
		});
    } 
    </script>
  </head>  
  <body scroll="no" style="overflow:hidden">
  <div id="background" class="background" style="display: none; "></div>
  <div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="sys_init_perform_db_oper"/></div>
  <input type="hidden" value="<%=path %>" id="contextPath" />
  <form name="AddForm" id="AddForm" method="post" action="">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td>  
		  <span class="topTableBgText">
		  	  <view:LanguageTag key="databak_vd_bak_data"/>
		  </span>
        </td>
      </tr>
    </table>
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td valign="top">
       		  <input type="hidden" name="dbBakConfInfo.isbaklog" value="${dbBakConfInfo.isbaklog}" />
       		  <input type="hidden" name="dbBakConfInfo.istimeauto" value="${dbBakConfInfo.istimeauto}" />
       		  <input type="hidden" name="dbBakConfInfo.taskInfo.taskmode1" value="${dbBakConfInfo.taskInfo.taskmode1}" />
         	  <input type="hidden" name="dbBakConfInfo.taskInfo.taskweek" value="${dbBakConfInfo.taskInfo.taskweek}" /><!-- 真正的周 -->
   			  <input type="hidden" name="dbBakConfInfo.taskInfo.taskmonth" value="${dbBakConfInfo.taskInfo.taskmonth}" /><!-- 真正的月 -->
   			  <input type="hidden" name="dbBakConfInfo.taskInfo.taskday" value="${dbBakConfInfo.taskInfo.taskday}" /><!-- 真正的日 -->
       		  <input type="hidden" name="dbBakConfInfo.taskInfo.taskhour" value="${dbBakConfInfo.taskInfo.taskhour}" /><!-- 真正的时点 -->
  			  <input type="hidden" name="dbBakConfInfo.taskInfo.taskminute" value="${dbBakConfInfo.taskInfo.taskminute}" />   <!-- 真正的分点 -->
			  <input type="hidden" name="dbBakConfInfo.isremote" value="${dbBakConfInfo.isremote}" />
			  <input type="hidden"  name="dbBakConfInfo.serverip" value="${dbBakConfInfo.serverip}" />
			  <input type="hidden"  name="dbBakConfInfo.port" value="${dbBakConfInfo.port}" />
        	  <input type="hidden"  name="dbBakConfInfo.user" value="${dbBakConfInfo.user}" />
        	  <input type="hidden"  name="dbBakConfInfo.password" value="${dbBakConfInfo.password}" />
        	  <input type="hidden"  name="dbBakConfInfo.dir" value="${dbBakConfInfo.dir}" />
		    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="ulOnInsideTable">
		     <tr>
		        <td width="30%" align="right"><view:LanguageTag key="databak_vd_log_isbak"/><view:LanguageTag key="colon"/>&nbsp;</td>
		        <td width="70%">
		        	<c:if test="${dbBakConfInfo.isbaklog==1}">
		            	<view:LanguageTag key="common_syntax_yes"/>
		        	</c:if>
		            <c:if test="${dbBakConfInfo.isbaklog==0}">
		        		<view:LanguageTag key="common_syntax_no"/>
		        	</c:if>
		        </td>
		     </tr>
		     <tr>
		        <td width="30%" align="right"><view:LanguageTag key="databak_vd_fixed_isbak"/><view:LanguageTag key="colon"/>&nbsp;</td><!-- 0否 手动  1 是 定时 -->
		        <td width="70%">
		        	<c:if test="${dbBakConfInfo.istimeauto==1}">
		            	<view:LanguageTag key="common_syntax_yes"/>
		        	</c:if>
		            <c:if test="${dbBakConfInfo.istimeauto==0}">
		        		<view:LanguageTag key="common_syntax_no"/>
		        	</c:if>
		        </td>
		    </tr>
		    <c:if test="${dbBakConfInfo.istimeauto==1}">
			      <tr>
			        <td width="30%" align="right"><view:LanguageTag key="databak_vd_time"/><view:LanguageTag key="colon"/>&nbsp;</td>
			        <td width="70%">			        	
	        		   <c:if test="${dbBakConfInfo.taskInfo.taskmode1==1}"><view:LanguageTag key="databak_monthly"/></c:if>		        		   
	        		   <c:if test="${dbBakConfInfo.taskInfo.taskmode1==2}"><view:LanguageTag key="databak_weekly"/></c:if>		        		   
	        		   <c:if test="${dbBakConfInfo.taskInfo.taskmode1==3}"><view:LanguageTag key="databak_everyday"/></c:if>	        		  
			        	&nbsp; 
			           <c:if test="${dbBakConfInfo.taskInfo.taskmode1==2}">
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskweek=='1'}"><view:LanguageTag key="common_date_monday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskweek=='2'}"><view:LanguageTag key="common_date_tuesday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskweek=='3'}"><view:LanguageTag key="common_date_wednesday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskweek=='4'}"><view:LanguageTag key="common_date_thursday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskweek=='5'}"><view:LanguageTag key="common_date_friday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskweek=='6'}"><view:LanguageTag key="common_date_saturday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskweek=='0'}"><view:LanguageTag key="common_date_sunday"/></c:if>
	        		   </c:if>
	        		   &nbsp;
	        		   <c:if test="${dbBakConfInfo.taskInfo.taskmode1==1}">        		   
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='1'}"><view:LanguageTag key="databak_oneday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='2'}"><view:LanguageTag key="databak_twoday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='3'}"><view:LanguageTag key="databak_threeday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='4'}"><view:LanguageTag key="databak_fourday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='5'}"><view:LanguageTag key="databak_fiveday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='6'}"><view:LanguageTag key="databak_sixday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='7'}"><view:LanguageTag key="databak_sevenday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='8'}"><view:LanguageTag key="databak_eightday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='9'}"><view:LanguageTag key="databak_nineday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='10'}"><view:LanguageTag key="databak_tenday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='11'}"><view:LanguageTag key="databak_elevenday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='12'}"><view:LanguageTag key="databak_twelveday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='13'}"><view:LanguageTag key="databak_thirteenday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='14'}"><view:LanguageTag key="databak_fourteenday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='15'}"><view:LanguageTag key="databak_fifteenday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='16'}"><view:LanguageTag key="databak_sixteenday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='17'}"><view:LanguageTag key="databak_seventeenday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='18'}"><view:LanguageTag key="databak_eighteenday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='19'}"><view:LanguageTag key="databak_nineteenday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='20'}"><view:LanguageTag key="databak_twentyday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='21'}"><view:LanguageTag key="databak_twenoneday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='22'}"><view:LanguageTag key="databak_twentwoday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='23'}"><view:LanguageTag key="databak_twenthreeday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='24'}"><view:LanguageTag key="databak_twenfourday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='25'}"><view:LanguageTag key="databak_twenfiveday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='26'}"><view:LanguageTag key="databak_twensixday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='27'}"><view:LanguageTag key="databak_twensevenday"/></c:if>
		        		   <c:if test="${dbBakConfInfo.taskInfo.taskday=='28'}"><view:LanguageTag key="databak_tweneightday"/></c:if>
	        		   </c:if>
	        		   &nbsp;
	        		   ${dbBakConfInfo.taskInfo.taskhour}:${dbBakConfInfo.taskInfo.taskminute}
			        </td>
			      </tr>
		      </c:if>
		       <tr>
		        <td width="30%" align="right"></td>
		        <td width="70%">&nbsp;</td>
		       </tr>
		      <tr>
		        <td width="30%" align="right"><view:LanguageTag key="databak_vd_reomote_bak"/><view:LanguageTag key="colon"/>&nbsp;</td>
		        <td width="70%">
		        	<c:if test="${dbBakConfInfo.isremote==0}">
		            	<view:LanguageTag key="databak_local"/>
		        	</c:if>
		        	<c:if test="${dbBakConfInfo.isremote==1}">
		            	<view:LanguageTag key="databak_vd_protocol"/>
		        	</c:if>
		        </td>
		      </tr>
		     
		     <c:if test="${dbBakConfInfo.isremote==1}">
			      <tr >
			        <td width="30%" align="right"><view:LanguageTag key="databak_vd_server"/><view:LanguageTag key="colon"/>&nbsp;</td>
			        <td width="70%">
			           ${dbBakConfInfo.serverip}
			        </td>
			      </tr>
			      <tr >
			        <td width="30%" align="right"><view:LanguageTag key="databak_vd_port"/><view:LanguageTag key="colon"/>&nbsp;</td>
			        <td width="70%">
			        	${dbBakConfInfo.port}
			        </td>
			      </tr>
			      <tr >
			        <td width="30%" align="right"><view:LanguageTag key="databak_vd_username"/><view:LanguageTag key="colon"/>&nbsp;</td>
			        <td width="70%">
			        	${dbBakConfInfo.user}
			        </td>
			      </tr>
			      <tr >
			        <td width="30%" align="right"><view:LanguageTag key="databak_vd_pwd"/><view:LanguageTag key="colon"/>&nbsp;</td>
			        <td width="70%">
			        	***
			        </td>
			      </tr>
		      </c:if>
		      
		      <tr>
		        <td width="30%" align="right"><view:LanguageTag key="databak_vd_bakaddress"/><view:LanguageTag key="colon"/>&nbsp;</td>
		        <td width="70%">
		        	${dbBakConfInfo.dir}
		        </td>
		      </tr>
		      
		       <tr>
		        <td align="right">&nbsp;</td>
		        <td>
		            <a href="#" id="addBtn" class="button"><span><view:LanguageTag key="databak_vd_execute_bak"/></span></a>
		            <a href="#" id="updatConfigBtn" class="button"><span><view:LanguageTag key="databak_edit_configure"/></span></a>
		        </td>
		      </tr>
		    </table>
		        
    	</td>
    </tr>
    </table>
    
  </form>
  </body>
</html>