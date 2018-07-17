$.ajaxSetup({   
	contentType:"application/x-www-form-urlencoded;charset=utf-8",   
	complete:function(XMLHttpRequest,textStatus){
		//通过XMLHttpRequest取得响应头，sessionstatus
		var sessionstatus=XMLHttpRequest.getResponseHeader("sessionstatus");   
		if(sessionstatus=="sessionOut"){   
			var contextPath = $("#contextPath").val();
		    if("" == contextPath || undefined == contextPath){
		    	var pathName=window.document.location.pathname;  // 当前访问路径
		    	contextPath = pathName.substring(0,pathName.substr(1).indexOf('/')+1); // 工程名
		    }
			window.parent.location.replace(contextPath +"/index.jsp");
		}		
	}
});

var FT = {};
var cPath = $("#contextPath").val();

FT.buttonAction = {
	cancelClose : function(item, dialog){
		dialog.close();
	},
	
	okClick:function(btn,win,index) {
		document.getElementById('winFrame').contentWindow.okClick(btn,win,index);
	},
	
	saveClick:function(btn,win,index) {
        document.getElementById('winFrame').contentWindow.saveClick(btn,win,index);
    }
}

FT.bs = {
	'okcancel':[{text:getLangVal('common_syntax_sure',cPath),onclick:FT.buttonAction.okClick},{text:getLangVal('common_syntax_cancel',cPath),onclick:FT.buttonAction.cancelClose}],
	'savecancel':[{text:getLangVal('common_syntax_save',cPath),onclick:FT.buttonAction.saveClick},{text:getLangVal('common_syntax_cancel',cPath),onclick:FT.buttonAction.cancelClose}],
	'close':[{text:getLangVal('common_syntax_close',cPath),onclick:FT.buttonAction.cancelClose}]
}

//页面弹出的大窗口，用于业务数据编辑等操作
FT.openWinSmall = function(title,url,btns,isDrag) {
	var buttons;
	if(typeof btns === 'boolean') {
		isDrag = btns;
	}else if(Object.prototype.toString.apply(btns) === '[object Array]') {
		buttons = btns;
	}
    return $.ligerDialog.open({
        title:title,
        url: url,
        width: 600,
        height:250,
        name:'winFrame',
        isResize:false,
        isDrag:isDrag,
        buttons: buttons || FT.bs[btns] || FT.bs.okcancel
    });
}

FT.openWinMiddle = function(title,url,btns,isDrag) {
	var buttons;
	if(typeof btns === 'boolean') {
		isDrag = btns;
	} else if(Object.prototype.toString.apply(btns) === '[object Array]') {
		buttons = btns;
	}
	//判断屏幕的分辨率 
	var winHeight = 600;
	if(window.screen.height <= 800){
		winHeight = 420;
	}
	return $.ligerDialog.open({
		title:title,
		url:url,
		width:700,
		height:winHeight,
		name:'winFrame',
		isResize:false,
		isDrag:isDrag,
		buttons: buttons || FT.bs[btns] || FT.bs.okcancel
	});
}

//页面弹出的大窗口，用于业务数据编辑等操作
FT.openWinSet = function(title, url, btns, isDrag, width, height) {
	var buttons;
	if(typeof btns === 'boolean') {
		isDrag = btns;
	}else if(Object.prototype.toString.apply(btns) === '[object Array]') {
		buttons = btns;
	}
    return $.ligerDialog.open({
        title:title,
        url: url,
        width: width,
        height:height,
        name:'winFrame',
        isResize:false,
        isDrag:isDrag,
        buttons: buttons || FT.bs[btns] || FT.bs.okcancel
    });
}

//打开数据操作窗口
FT.toOpen = function (title, url, width, height){
	return $.ligerDialog.open({title:title, url:url, width:width, height:height});
}

// 框架最外层大窗口对应的两个方法
// 打开
FT.openWinBig = function (title,url){
	var topWin = window.top.parent;
	topWin.openBigWin(title,url);// 框架最外层页面上的方法 layout.jsp中
	return topWin;
}
// 关闭
FT.closeWinBig = function (){
 	var topWin = window.top.parent;
 	topWin.bigWinClose();// 框架最外层页面上的方法 layout.jsp中
    return topWin;
}

//页面常见的几种提示方式，用于输出提示信息
FT.toAlert = function (type, content, title) {
	switch (type){
		case "success":
			$.ligerDialog.success(content, getLangVal('common_syntax_tip',cPath));
			break;
		case "warn":
			$.ligerDialog.warn(content, getLangVal('common_vd_warn',cPath));
			break;
		case "question":
			$.ligerDialog.question(content, getLangVal('common_syntax_tip',cPath));
			break;
		case "error":
			$.ligerDialog.error(content, getLangVal('common_syntax_error',cPath));
			break;
		default :
			$.ligerDialog.alert(content, getLangVal('common_syntax_tip',cPath));
	}
}

// 一些基于ligerUI的提示框
FT.Dialog = {
	confirm : function(content, title, callback) {
		$.ligerDialog.confirm(content, title, callback);
	},
	prompt : function(title, value, isMulti, callback) {
		$.ligerDialog.prompt(title, value, isMulti, callback);
	}
}
