var contextPath;
var waitW = null;
var queryData = null;//保存令牌查询条件 避免执行的数据和显示的查询条件不一致
var staticPassWin;

var syncWin;
$(function(){
	contextPath = $("#contextPath").val();
	
	/**
	 * 初始化列表时加载指定页的数据，此处主要应用为从列表页面的非第1页跳到编辑页面，然后返回时需要记住跳转前的页码，
	 * 返回后仍然显示该页数据，故在此需取出页码，加载数据时传入.
	 */
	var loadPage = 1;
	if($("#currentPage").val() != '') {
		loadPage = $("#currentPage").val();
	}
	
//	// 默认初始化清空查询条件
//    var operTypeStr = 1;
//    // 首页快速查询可能带过来的条件
//    var queryText = $("#queryText").val();
//    if(queryText!=""){
//    	//不清空查询条件
//    	operTypeStr = "";
//    	$("#userId").val(queryText);
//    }
	
//    var custid = '';
//    var custname = '';
//    if(queryData != null){
//    	//不清空查询条件
//    	operTypeStr = "";
//    	var custid = queryData.custid;
//    	var custname = queryData.custname;
//    }

	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        async : true,
        url : 'custInfo!init.action',
        columns : [
            {display:'客户名称',name:'custname', width:'25%', render:customerRender},
            {display:'客户编号',name:'custid', width:'15%'},
            {display:'所属部门',name:'deptStr', width:'15%'},
            {display:'创建人',name:'operator', width:'15%'},
            {display:'创建时间',name:'createtimeStr', width:'10%'},
            {display:'操作',name:'', minWidth:120, render:operationRenderer, width:'18%'}
        ],
        parms:{
//			'custInfo.custid':custid,
//			"custInfo.custname":custname
		},
		isAllowHide:true,
        checkbox:true,
        allowUnSelectRow : true,
        enabledSort:false,
        page : loadPage,
        pageSize:20,
        height:'100%',
        onAfterShowData:function() {
        	dataGrid.clearParams('operType');
        }
	});
	
	// 赋值查询条件
	setQueryObjData();
	$("td :input").keydown(function(e) {
		if(e.keyCode == '13') {
			query(false,true);
		}
	});
})

// ====================  Grid 所用Renderer  ==========================

// 操作列渲染器
function operationRenderer(data) {
    var opers = '';
    if(permEdit != ''){
		opers += "<a href='javascript:findobj(\"" + data.id+ "\");'>" + permEdit + "</a>";
    }
    if(permGoPrjinfo != ''){
    	//go 定制项目
    	opers +="&nbsp;&nbsp;&nbsp;";
		opers = opers + "<a href='javascript:goProject(" + data.id + ");'>" + permGoPrjinfo + "</a>";
	}
	if(permGoPrjAdd != ''){
    	//go 定制项目
    	opers +="&nbsp;&nbsp;&nbsp;";
		opers = opers + "<a href='javascript:goProjectAdd(" + data.id + ");'>" + permGoPrjAdd + "</a>";
	}
	
	return opers;
}

/**
 * 客户渲染函数
 */
function customerRender(data,i) {
	return "<a href='javascript:view(\"" + data.id + "\");'>" + data.custname + "</a>";
}

//根据客户唯一id查看用户详细信息
function view(id){
	FT.openWinMiddle(syntax_detail_lang,contextPath + '/manager/customer/custInfo!view.action?custInfo.id=' + id,'close');
}

//组装表单查询条件QueryFilter
function formParms(page, pagesize){
    var queryParam = {
    	'page':page,
    	'pagesize':pagesize,
    	'custInfo.custid':queryData.custid,
        'custInfo.custname':queryData.custname,
        'custInfo.dept':queryData.dept
    }
    return queryParam;
}


//设置查询条件的值
function setQueryObjData(){
	queryData = new Object();
	queryData.custid = $("#custid").val();
	queryData.custname = $("#custname").val();
	queryData.dept = $("#dept").val();
}

/**
 * 查询,根据条件让Grid重新载入数据 
 * isGetQuery参数表示在显示列表前是否获取查询条件 true 获取，false 不获取
 * @param {} stayCurrentPage 是否留在当前页面
 */
function query(stayCurrentPage,isGetQuery){

	// 赋值查询条件
	if(isGetQuery){
		setQueryObjData(isGetQuery);
	}
	var page = 1;
	if(stayCurrentPage) {
		page = dataGrid.getCurrentPage();
	}
	var pagesize = dataGrid.getCurrentPageSize();
    var queryParam = formParms(page, pagesize);
    pageFocus(1);
    dataGrid.loadServerData(queryParam);
}

//添加用户
 function addUserInfo(){
     location.href= contextPath + "/manager/user/userinfo/add.jsp";
 }
 function goProject(id){
 	location.href= contextPath + "/manager/project/list.jsp?custid="+id+"&returnBack=1";
 }
 function goProjectAdd(id){
 	window.parent.removeTabItemF('030002');
	window.parent.f_addTab('0300', this ,'030002', '添加定制项目',contextPath + "/manager/customer/custInfo!goProjectAdd.action?custId="+id);
 }

//编辑用户
function findobj(id){
   var curPage = dataGrid.getCurrentPage();
   location.href = contextPath + '/manager/customer/custInfo!find.action?custInfo.id='+ id +'&cPage='+curPage;
}


function batchExecOper(url,userIds) {
	var ajaxbg = $("#background,#progressBar");//加载等待
	ajaxbg.show();
	var radprofileid = $("#radprofileid").val();
	var backEndAuth = $("#backendAuth").val();
	var localAuth = $("#localAuth").val();
	var pwd = $("#pwd").val();
	
	$("#ListForm").ajaxSubmit({
		async:true,    
		dataType : "json",  
		type:"POST", 
		url : url,
		data:{userIds:userIds,radprofileid:radprofileid,backEndAuth:backEndAuth,localAuth:localAuth,pwd:pwd},
		success:function(msg){
		     ajaxbg.hide();
		     if(msg.errorStr == 'success'){
		     	$.ligerDialog.success(msg.object,syntax_lang,function(){
		     		if (waitW != null) {
					    waitW.close();
		     		}
		     		query(true);
				});
		     }else{
			    FT.toAlert(msg.errorStr,msg.object,null);
		     }
		}
    }); 
}

function orgInfo(n, t){
	var r;
	if(t == 0){
		r = batch_del_lang + n + a_user_lang;
	}else if(t == 1){
		r = batch_unbind_lang + n +  a_user_lang;
	}else if(t == 2) {
		r = batch_add_lang +  n + a_user_lang;
	}else if(t == 3) {
		r = batch_lock_lang +  n + a_user_lang;
	}else if(t == 4) {
		r = batch_unlock_lang +  n + a_user_lang;
	}else if(t == 5) {
		r = batch_disabled_lang +  n + a_user_lang;
	}else if(t == 6) {
		r = batch_enable_lang +  n + a_user_lang;
	}else if(t == 7) {
		r = batch_backend_lang +  n + a_user_lang;
	}else if(t == 8) {
		r = batch_localauth_lang +  n + a_user_lang;
	}
	
	return r;
}

function checkConfi(confType, confMark){
	var url = contextPath + "/manager/user/userinfo/userInfo!checkConfi.action?confType="+confType+ "&confMark=" + confMark;;
	var hidValue;
	$.post(url,
		function(msg){
			$('#bindToneks').val(msg);
		}, "text"
	);
}

//标签切换 用户查询与用户操作切换
function pageFocus(index){
	$("#menu li.tabFocus").removeClass("tabFocus"); //移除已选中的样式
	$("#menu li:eq(" + index + ")").addClass("tabFocus");//增加当前选中项的样式
	$("#content li:eq(" + index + ")").show().siblings().hide();//显示选项卡对应的内容并隐藏未被选中的内容
	if(index == 1){
		$("#listDataAJAX").show();
	}else{
		$("#listDataAJAX").hide();
	}
}


//删除操作
function delData() {
	var selRows = dataGrid.getSelectedRows();
	var loginUser = $("#l_userid").val();
	var loginUserRole = $("#l_userid_role").val();
	var flag = false;
	if(selRows < 1) {
		FT.toAlert('warn', '请选择要删除的客户！',null);
	} else {
		$.ligerDialog.confirm('确定要删除已选定的客户？',syntax_confirm_lang,function(sel) {
            if(sel) {
            	var selLength = selRows.length;
            	var ids='';
            	for(var i=0;i<selLength;i++) {
            		ids = ids.concat(selRows[i]['id'],',');
            	}

            	$.post(cPath + '/manager/customer/custInfo!delete.action',{
            		delCustIds: ids.substring(0,ids.length-1)
	            	},function(result){
	            		if(result.errorStr == 'success'){	            			
		            		FT.toAlert(result.errorStr, result.object, null);
		            	}else {
		            		FT.toAlert(result.errorStr, result.object, null);
		            	}
		            	query(false, true);
	            	},'json'
            	);
            }
		});
	}
}
