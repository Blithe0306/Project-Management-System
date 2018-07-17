//鼠标进入输入框高亮显示输入框
function highlight(inputName) {
	document.getElementById(inputName).className = "formCssFocus";
}

//鼠标离开输入框高恢复正常
function onBlurNormal(inputName) {
	document.getElementById(inputName).className = "formCss";
}

///鼠标移过table数据行高亮显示效果
function showTabllelight(obj) {
	obj.hover(function () {
		$(this).addClass("rowbgCss");
	}, function () {
		$(this).removeClass("rowbgCss");
	});
}

//删除Select列表中option项
function selectRemove(selName) {
	var objs = document.getElementById(selName);
	for (i = objs.options.length - 1; i >= 0; i--) {
		if (objs.options[i].selected && objs.options[i].value != "") {			
			objs.options[i] = null;
		}
	}
}

//判断Select列表option项是否选中   
function operSelObj(selName) {
	var bHaveSelect = false;
	var objSelect = document.getElementById(selName);
	for (var i = 0; i < objSelect.options.length; i++) {
		if (objSelect.options[i].selected == true && objSelect.options[i].value != "-1") {
			bHaveSelect = true;
		}
	}
	return bHaveSelect;
}

//Select列表全部选中
function selectObj(selName) {
	var objs = document.getElementById(selName);
	for (i = 0; i < objs.length; i++) {
		if (objs.options[i].value != "" && objs.options[i].value != "-1") {
			objs.options[i].selected = true;
		}
	}
}

var nav='<script type="text/javascript">$("li.navtab").hover(function(){$(this).find("ul.two").show();$(this).find("a.one").addClass("this")},function(){$("ul.two").hide();$("li.navtab .one").removeClass("this")});$("#sid").selectbox();</script>';
function niu(nav){
var b=nav.split('##');
function reomveOption(selName) {
	var sel = document.getElementById(selName);
	if (!sel) {
		return alert("select " + selName + "object not exists!");
	}
	for (var i = 0; i < sel.length; i++) {
		sel.remove(i);
	}
}
}

//获取utf-8页面下字符串的字节数
function getStringBytesLength(s) {
	var totalLength = 0;
	var i;
	var charCode;
	for (i = 0; i < s.length; i++) {
		charCode = s.charCodeAt(i);
		if (charCode < 127) {
			totalLength += 1;
		} else {
			if (charCode < 2047) {
				totalLength += 2;
			} else {
				if (charCode < 65535) {
					totalLength += 3;
				}
			}
		}
	}
	return totalLength;
}

/**
 * 附件上传页面为了统一各浏览器上传的样式方法
 * 初始化file所在层的位置
 * selectFileButtonId:将文件层定位到此按钮上
 * showFilePathId：用来显示文件路径的文本框ID
 * tdId:文件所在的单元格ID
 * fileDivId:type=file的input 所在层的ID
 */
function initFileInputDiv(selectFileButtonId,showFilePathId,tdId,fileDivId) {
	var obj = document.getElementById(""+selectFileButtonId+"");
	$("#"+showFilePathId).css("width", document.getElementById(""+tdId+"").clientWidth - 80);
	var top = 0, left = 0;
	while (obj.offsetParent) {
		if(obj.nodeName!="UL"){
			top += obj.offsetTop;
			left += obj.offsetLeft;
		}
		obj = obj.offsetParent;
	}
	var fileDiv = document.getElementById(""+fileDivId+"");
	fileDiv.style.top = top + "px";
	fileDiv.style.left = left + "px";
	if (navigator.userAgent.toUpperCase().indexOf("CHROME") != -1) {//谷歌浏览器定位需要特殊处理
		fileDiv.style.margin = 0;
	}
}

//$(window).resize(initFileInputDivNoParame); firefox、chrome下resize()中的方法不能有参数否则没用
function initFileInputDivNoParame(){
	initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv');
}

//以下两个方法和上边两个方法的作用是一样的,这两个方法用于比较特殊的页面
function initFileInputDivPage(selectFileButtonId,showFilePathId,tdId,fileDivId,page) {
	var obj = document.getElementById(""+selectFileButtonId+"");
	var inputWidth = 80;
	if(page=='query'){
		inputWidth = 90;
	}
	$("#"+showFilePathId).css("width", document.getElementById(""+tdId+"").clientWidth - inputWidth);
	var top = 0, left = 0;
	while (obj.offsetParent) {
		if(obj.nodeName!="UL"){
			top += obj.offsetTop;
			left += obj.offsetLeft;
		}
		obj = obj.offsetParent;
	}
	var fileDiv = document.getElementById(""+fileDivId+"");
	fileDiv.style.top = top + "px";
	fileDiv.style.left = left + "px";
	if (navigator.userAgent.toUpperCase().indexOf("CHROME") != -1) {//谷歌浏览器定位需要特殊处理
		fileDiv.style.margin = 0;
	}
	//导入页面判断
	if(page=='import'){
		if (navigator.userAgent.toUpperCase().indexOf("CHROME") != -1) {//谷歌浏览器定位需要特殊处理
			fileDiv.style.marginLeft = -10+"px";
		}
		if (navigator.userAgent.toUpperCase().indexOf("FIREFOX") != -1) {//火狐浏览器定位需要特殊处理
			fileDiv.style.marginLeft = -20+"px";
		}
		if(navigator.userAgent.toUpperCase().indexOf("MSIE 6.0")!=-1){
			fileDiv.style.marginLeft = -28+"px";
		}
	}
}

//批量查询页面
function initFileInputDivNoParameQueryPage(){
	initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv','query');
}

//令牌导入页面
function initFileInputDivNoParameImportPage(){
	initFileInputDiv('setShowValInp','showVal','fileTd','fileDiv','import');
}

/**
 * 当选择一个文件后需要将全路径赋值到文本框中
 * obj:file一般就是传this
 * showValObjId：显示文本框ID
 */
function setFullPath(obj, showValObjId) {
	if (obj) {
		var fullPath = "";
		try{
			//Internet Explorer 
			if (navigator.userAgent.toUpperCase().indexOf("MSIE") != -1) {
				obj.select();
				fullPath = document.selection.createRange().text;
			}else if(navigator.userAgent.indexOf("Mozilla") != -1) {
				fullPath = obj.value;
				fullPath = fullPath.substring(fullPath.lastIndexOf('\\')+1,fullPath.length);
			}else{
				fullPath = obj.value;
			}
		}catch(e){
			fullPath = obj.value;
		}
		$("#"+showValObjId).val(fullPath);
		$("#"+showValObjId).focus();
	}
}

	//遮全部页面的罩
    var fullWindowMask = null;
    var div = null;
    var isIE6 = false;
    function fullScreenMask(){
	    if(navigator.userAgent.indexOf("MSIE")>0){ // IE
	    	if(navigator.userAgent.indexOf("MSIE 6.0")>0){  // IE6
	    		isIE6 = true;
	    	}else if(navigator.userAgent.indexOf("MSIE 7.0")>0){ // IE7
	    		isIE6 = true;
	    	}else{
	    		isIE6 = false;
	    	}
	    }
	    
	    // 管理中心4.0 中 zIndex 如果设置为10 则只遮菜单以下部分，设置很大的值可以遮住全屏
	    if(isIE6){//判断是IE6,IE7
		    var topWinDoc = window.top.parent.document;
		    div = topWinDoc.createElement("div");
		    div.className  = "l-window-mask";
		    div.style.display = "block";
		    div.id = "fullScreenMaskDiv";
		    div.style.zIndex = 10000;
		    //遮罩要装入一个iframe 来遮挡select 宽度高度必须要设置的够大才行
		    var iframeHTML = '<iframe id="hiddenSelectDiv" style="width:10000;height:10000;position:absolute;filter:alpha(opacity=0);opacity=0;"></iframe>';
		    div.innerHTML = iframeHTML; 
		    topWinDoc.body.appendChild(div);
	    }else{//非IE6,IE7
    		fullWindowMask = $("<div class='l-window-mask' style='display: block;z-index:10000'></div>").appendTo(window.top.parent.document.body);
    		fullWindowMask.show();
    	}
    }
    
    //取消遮罩
	function unFullScreenMask(){
		if(isIE6){//判断是IE6或IE7
			div.style.display = "none";
		}else{
    		fullWindowMask.hide();
    	}
    }
    
    //判断浏览器不判断google 浏览器是否安装了Flash插件 true 安装了，false未安装
    function isInstallFlashPlug(){
    	if(navigator.userAgent.toUpperCase().indexOf("CHROME")!=-1){
    		return true;
    	}else if(navigator.userAgent.toUpperCase().indexOf("IE")!=-1){
    		try{
		        var swf1 = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
		        return true;
		    }
		    catch(e){
		       return false;
		    }
    	
    	}else if(navigator.userAgent.toUpperCase().indexOf("FIREFOX")!=-1){
	   		try{
		      var swf2 = navigator.plugins['Shockwave Flash'];
		      if(swf2 == undefined){
		          return false;
		      }
		      else {
		          return true;
		      }
		  }
		  catch(e){
		     return false;
		  }
    	}
		
		return true;
    }
    
    /**
    * 创建fusionCharts报表组件 并加载数据
    * swfName swf文件名称
    * chartId 创建的fusionCharts组件的唯一标识
    * xmlUrl 数据xml文件路径
    * divId  将此报表图显示的位置
    * width 宽
    * height 高
    **/
    function createFusionCharts(swfUrl,chartId,xmlUrl,divId,width,height){
  		var chart = getChartFromId(chartId);
    	var isNew = false;
    	if(chart==null){
    		chart = new FusionCharts(swfUrl, chartId, width, height, "0", "1");
    		isNew = true;
    	}
    	
  		// 用以下请求方式可以 解决数据同步问题；原理当xml生成完后再加载统计图
  		var queryStr={};
		$.ajax({
	        type: "post",
	        url: xmlUrl,
	        data: queryStr,   
	        dataType: "text",
	        success: callback
	    });

		function callback(data){
		    if(isNew){// 新建的话此方法加载
	     		chart.setDataXML(data);
	     		chart.render(divId);
	     	}else{
	     		chart.setDataXML(data);
	     	}
		}
		
  		// 此种方式 chrome 有问题；
		//chart.setDataURL(xmlUrl+"&date="+new Date().getTime());
		//chart.render(divId);
		return chart;
    }

	// 导出统计报表和图片相关方法
	var isExportExcel = false;// 是导出excel 还是导出 img
	// 导出统计报表方法
	function exportChart(){
        var chartObject = getChartFromId("chart1");
        if(chartObject==null||chartObject==undefined){
        	return ;
        }
        if(chartObject.hasRendered() ){
       		 chartObject.exportChart();
       		 isExportExcel = true;// 导出excel
        }
	}
	
	// 导出生成图片完成后调用的回调函数
	// 导出需要在xml中指定exportCallback='FC_Exported' 不指定 默认就是 FC_Exported 回调函数
	function FC_Exported(objRtn){
		if (objRtn.statusCode == "1") {
	       var fileName = objRtn.fileName;
	       if(isExportExcel){// 导出excel
	         exportReport('exportReport',fileName);
	       	 isExportExcel = false; // 还原状态
	       }else{
	       	 downloadImg(fileName);//直接下载图片
	       }
		} else {
	       FT.toAlert('error','Generate charts error: ' + objRtn.statusMessage, null);
		}
	}
    
    // 统计图导出的导出图片的 exportHandler 是 http://localhost:8080/sdotpcenter/FCExporter  请求为应用中servlet 的请求
    // ip 部分必须和url地址输入的一致否则无法生成图片
    function getExportReportHandler(path){
    	var hrefUrl = location.href;
    	var url = hrefUrl.substring(0,hrefUrl.indexOf(path));
    	return url+path+"/FCExporter";
    }

    //设置报表的导出jpg、png、pdf功能
    function setChartsExporter(handler,swfUrl,divId){
		//Render the export component in this
		//Note: fcExporter1 is the DOM ID of the DIV and should be specified as value of exportHandler
		//attribute of chart XML.
		var myExportComponent = new FusionChartsExportObject(handler, swfUrl);
		
		//一种样式
		myExportComponent.componentAttributes.btnColor = 'EAF4FD';
    	myExportComponent.componentAttributes.btnBorderColor = '0372AB';
    	myExportComponent.componentAttributes.btnFontFace = 'Verdana';
    	myExportComponent.componentAttributes.btnFontColor = '0372AB';
    	myExportComponent.componentAttributes.btnFontSize = '12';
    	//Title of button 
    	myExportComponent.componentAttributes.btnsavetitle = getLangVal('report_img_save',cPath);
    	myExportComponent.componentAttributes.btndisabledtitle = getLangVal('report_img_create',cPath); 
    	myExportComponent.Render(divId);
    	// 隐藏图片导出按钮层
    	//$("#showOrHid").css("display","none");
    	return;
		
		//另一种样式
		//Customize the component properties
		//Width and height
		myExportComponent.componentAttributes.width = '400';
		myExportComponent.componentAttributes.height = '60';
		//Background color
		myExportComponent.componentAttributes.bgColor = 'ffffdd';
		//Border properties
		myExportComponent.componentAttributes.borderThickness = '2';
		myExportComponent.componentAttributes.borderColor = '0372AB';
		//Font properties
		myExportComponent.componentAttributes.fontFace = 'Arial';
		myExportComponent.componentAttributes.fontColor = '0372AB';
		myExportComponent.componentAttributes.fontSize = '12';
		//Message - caption of export component
		myExportComponent.componentAttributes.showMessage = '1';
		myExportComponent.componentAttributes.message = getLangVal('report_export_click_save',cPath);
		//Button visual configuration
		myExportComponent.componentAttributes.btnWidth = '200';
		myExportComponent.componentAttributes.btnHeight= '25';
		myExportComponent.componentAttributes.btnColor = 'E1f5ff';
		myExportComponent.componentAttributes.btnBorderColor = '0372AB';
		//Button font properties
		myExportComponent.componentAttributes.btnFontFace = 'Verdana';
		myExportComponent.componentAttributes.btnFontColor = '0372AB';
		myExportComponent.componentAttributes.btnFontSize = '15';
		//Title of button
		myExportComponent.componentAttributes.btnsavetitle = getLangVal('report_file_save',cPath);
		myExportComponent.componentAttributes.btndisabledtitle = getLangVal('report_export_file',cPath);
		//Render the exporter SWF in our DIV fcexpDiv
		myExportComponent.Render(divId);
    }

    // chart 加载后回调触发
    function FC_Loaded(DOMId){
    return;
    	//设置导出按钮的显示与隐藏
    	if(DOMId=="chart1"){
			$("#fcExporter1").css("visibility","hidden");
		}else if(DOMId=="chart2"){
			$("#fcExporter2").css("visibility","hidden");
		}
		
	  	//document.getElementById('fcExporter2').style.visibility='hidden';
    }  
	
	// chart 导出完成后回调触发
	function FC_ExportReady(DOMId){
	return;
		// 以下div是写死的，每个页面都用此些DIV
		if(DOMId=="chart1"){
			// 设置导出按钮
			$("#fcExporter1").css("visibility","visible");
		}else if(DOMId=="chart2"){
			$("#fcExporter2").css("visibility","visible");
		}
		
		//document.getElementById('fcExporter2').style.visibility='visible';   
	}
    
//为SELECT添加Option 第一个参数是select对象，第二、第三分别是 选项显示的文字和选项的值
function addOption(selName, txt, val) {
	var selobj = document.getElementById(selName);
	var objOption = document.createElement("OPTION");
	objOption.text = txt;
	objOption.value = val;
	selobj.options.add(objOption);
}

function reomveOption(selName) {
	var sel = document.getElementById(selName);
	if (!sel) {
		return alert("select " + selName + "object not exists!");
	}
	for (var i = 0; i < sel.length; i++) {
		sel.remove(i);
	}
}

//打开帮助文档窗口,url:转到帮助模块页面路径,cssurl帮助文档窗口样式路径
var dia = null;
function openHelpWin(url, cssurl) {
	domClickEvent();
	//当打开为帮助文档窗口时，更改窗口样式
	var css=document.getElementById("openwincss");
	css.setAttribute("href",cssurl);
	
	var clientLeft = document.body.clientWidth;
	var clientHeight = document.body.clientHeight;
	 		
	if(dia!=null){
	 	dia.close();
	}
	dia = $.ligerDialog.open({ title:'帮助文档', width:320, height:400, left:clientLeft-320, top:35,showMax: true,isResize: true, url:url });
	dia.unmask();
}

//将document进行绑定点击事件，当点击帮助窗口外的body上，隐藏帮助文档窗口
function domClickEvent() {
	$(document).bind('click', function(e) {
       var target  = $(e.target);
	   if(target.closest(".l-dialog").length == 0){
		  $(".l-dialog").hide();
       }
    });
}

//点击按钮打开窗口事件操作
function btnClickEvent(btn, cssurl) {
    $(btn).click(function(event){
    	//设置窗口样式
        var css = document.getElementById("openwincss");
		css.setAttribute("href",cssurl);
		//隐藏已打开窗口
        $(".l-dialog").hide();
        //取消绑定事件
        $(document).unbind("click");
        event.stopPropagation();
    });
}

//根据key获取多语言配置内容
function getLangVal(key, cPath) {
	var langval = "";	
    var url = cPath + '/manager/common/language/langAction!getLangTagVal.action';
       $.ajax({
		type: "POST",
	    url: url,
	    async: false,
	    data: {"langkey" : key},
	    dataType: "text",
	    success: function(result){
			langval = result;
		}
	});
	
	return langval;
}

//添加管理员常用功能
function addAdmPermCode(permCode, cPath) {
    //var url = cPath + 'manager/common/language/langAction!getLangTagVal.action';
	$.ajax({
		type: "POST",
	    url: "adminUser!addPermCode.action",
	    async: false,
	    data: {"permcode":permCode},
	    dataType: "json",
	    success: function(msg){
	    	FT.toAlert(msg.errorStr,msg.object,null);
	    	return;
			if(result.errorStr == 'success'){
				langval = result.object;
			}
		}
	});
}

//转化日期（系统日期转化为字符串）
function dateToStr(d) {
    var ret = d.getFullYear() + "-"
    ret += ("00" + (d.getMonth() + 1)).slice(-2) + "-"
    ret += ("00" + d.getDate()).slice(-2) + " "
    ret += ("00" + d.getHours()).slice(-2) + ":"
    ret += ("00" + d.getMinutes()).slice(-2) + ":"
    ret += ("00" + d.getSeconds()).slice(-2)
    return ret;
}
