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
 	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
    
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/fusioncharts/fusionCharts.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/fusioncharts/fusionChartsExportComponent.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script>
	
	<script language="javascript" type="text/javascript">
	<!--
	var cPath;
	var paramObj = new Object(); // 查询条件_统计时使用
    var reportType = "";// 统计类型
	var isInitChart = true; //是否初始化报表
	$(function(){
		//检查电脑浏览器是否安装FLASH插件
		if(!isInstallFlashPlug()) {
			isInitChart = false;
			var tipview = $.ligerDialog.tip({title: '<view:LanguageTag key="tkn_assign_tip_title"/>',height:80,content:'<view:LanguageTag key="common_flash_plugin"/>'});
			setTimeout(function(){tipview.close()},5000);
		}
	
		cPath = $("#contextPath").val();
		reportType = $("#reportType").val();
		
		viewChart();
		$('#ListForm').bind('keyup', function(event){
			if (event.keyCode=="13"){
				//回车查询事件
				viewChart();
			}
		});
		
		//Start 检查电脑IE是否安装FLASH插件
		if($.browser.msie){ // 判断IE
			try { 
				var swf=new ActiveXObject("ShockwaveFlash.ShockwaveFlash"); 
			}catch(e){ 
				var tipview = $.ligerDialog.tip({title: '<view:LanguageTag key="tkn_assign_tip_title"/>',height:80,content:'<view:LanguageTag key="common_flash_plugin"/>'});
				setTimeout(function(){tipview.close()},5000);
			}
		}
		//End 检查电脑IE是否安装FLASH插件
	});
	
	// 初始化统计图，如果tag 为true 则初始化保存按钮
	function viewChart(){
		if(!isInitChart){
			return;
		}
	
	    var method = "";
	    if(reportType=="state"){
	    	// 状态统计
	    	method = "reportTknCountByState";
	    }else if(reportType=="empin"){
	    	// 启用应急口令统计
	    	method = "reportTknCountByEmpin";
	    }else{
	    	// 过期时间统计
	    	method = "reportTknCountByExpired";
	    }
		var domainAndOrgunitIds = $('#orgunitIds').val();
		var orgunitNames = $("#orgunitNames").val();
		paramObj.orgunitNames = encodeURI(orgunitNames);
		
		var url = "<%=path%>/manager/report/reportAction!"+method+".action?handler="+getExportReportHandler("<%=path%>")+"&domainAndOrgunitIds="+domainAndOrgunitIds+"&domainAndOrgName="+paramObj.orgunitNames;
		//创建统计图
		createFusionCharts("<%=path%>/manager/common/charts/Column3D.swf",'chart1',url,'reportChart',800,450);
		
		// 统计图显示完成之后赋值查询条件
		paramObj.domainAndOrgunitIds = domainAndOrgunitIds;
	}
	
    function print(){
   	 	var chartObject = getChartFromId("chart1"); 
    	//chartObject.print();
    	// 获取cvs数据
    	//chartObject.getDataAsCSV());       
    }
    
    //导出报表
    function exportReport(method,pngfileName){
        // 报表的统计数据
        var chartObj = null;
		try{chartObj = getChartFromId("chart1");}catch(e){ }
		var csvData = "";
		if(chartObj!=null){
			csvData = chartObj.getDataAsCSV(); 
		}
        var ajaxbg = $("#background,#progressBar");//加载等待
		ajaxbg.show();
        if(csvData!=""&&csvData!=undefined){
        	var url = "<%=path%>/manager/report/reportAction!"+method+".action?exportType="+reportType+"&csvData="+encodeURI(csvData)+"&fileName="+pngfileName+"&domainAndOrgunitIds="+paramObj.domainAndOrgunitIds+"&domainAndOrgName="+paramObj.orgunitNames;
	        $.post(url,
					function(msg){
					   ajaxbg.hide();
					   if(msg.errorStr=='success'){
					  		// 文件下载
					   		window.location.href = "<%=path%>/manager/report/reportAction!downLoad.action?fileName="+encodeURI(msg.object);
					   }else{
					   		FT.toAlert('error','<view:LanguageTag key="report_export_faild"/>', null);
					   }
			},"json");
	    }
	}
	
	// 下载图片
    function downloadImg(fileName){
        window.location.href = "<%=path%>/manager/report/reportAction!downloadImg.action?fileName="+fileName;
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
  
  <body style="overflow-x:hidden">
  <div id="background"  class="background"  style="display: none; "></div>
  <div id="progressBar" class="progressBar" style="display: none; "><view:LanguageTag key="common_vd_is_execution"/></div>
  <input type="hidden" name="contextPath" value="<%=path%>" id="contextPath"/>
  <input id="reportType" type="hidden" type="text" value="${param.reportType}"/>
  <!--  <input type="button" value="点击导出" onclick="exportChart();">
  <input type="button" value="点击保存" onclick="saveChart();"> 
  <input type="button" value="打印图片" onclick="print();"> -->
  <form name="ListForm" method="post" id="ListForm" action="">
  <table width="800" border="0" cellspacing="0" cellpadding="0" style="margin-top:6px">
      <tr>
        <td width="120" align="right"><view:LanguageTag key="common_title_orgunit"/><view:LanguageTag key="colon"/></td>
        <td width="180">
			<input id="orgunitIds" name="orgunitIds" class="textarea100" type=hidden value=""/>
			<input id="orgunitNames" name="orgunitNames" class="formCss100" type="text" onClick="selOrgunits(7,'<%=path %>');" readonly/>		</td>
        <td width="20"></td>
        <td width="480"><span style="display:inline-block" class="query-button-css"><a href="#" onClick="viewChart()" class="isLink_LanSe" ><span><view:LanguageTag key="common_syntax_query"/></span></a></span></td>
      </tr>
    </table>
  </form>
	<hr class="hrLine" />
<center>
  		<div id="reportChart"></div><br/>
  		<img src="<%=path%>/images/icon/chart_bar.png" width="16" height="16" hspace="3" align="middle" /><a href="javascript:exportChart()"><view:LanguageTag key="report_export_report"/></a>
  		&nbsp;&nbsp;<img src="<%=path%>/images/icon/calendar.png" width="16" height="16" hspace="3" align="middle" /><a href="javascript:exportReport('exportDetailReport')"><view:LanguageTag key="report_export_report_detail"/></a>
  </center>
  </body>
</html>