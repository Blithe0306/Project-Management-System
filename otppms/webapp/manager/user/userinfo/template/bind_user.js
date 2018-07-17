var contextPath;
var usrTotalRow = 0; 
var queryData = null;//保存令牌查询条件 避免执行的数据和显示的查询条件不一致

$(function(){
	contextPath = $("#contextPath").val();
	//用户记录
	window['userdataGrid'] = $("#userListAjx").ligerGrid({
		root : 'items',
        record : 'results',
        url : 'userBind!usrQuery.action',
        columns : [
            {display:account_lang,name:'userId', width:'12%'},
            {display:real_name_lang,name:'realName', width:'12%'},
            {display:orgunit_id_lang,name:'domainId', width:'10%',hide:'1'},
            {display:title_orgunit_lang,name:'DOrgunitName', width:'15%'},
            {display:email_lang,   name:'email', width:'15%'},
            {display:comm_bind_lang,name:'usbindTag', render:usbindRenderer,width:'10%'} 
        ],
        parms:{operType:'1'},
        checkbox:true,
        allowUnSelectRow : true,
        enabledSort:false,
        page : 1,
        pageSize:20,
        height:'100%',
        onAfterShowData:function() {
        }
	});
	
	// 赋值查询条件
	queryObjData();
})

// ====================  Grid 所用Renderer  ==========================
 
 
function usbindRenderer(data,rIndex){
	return data.usbindTag == 2 ? '<span style="color:red">'+ langBound +'</span>' : langUnbound;
}

/**
 * 查询,根据条件让Grid重新载入数据
 * @param {} stayCurrentPage 是否留在当前页面
 */
function queryUsr(stayCurrentPage,isGetQuery){
	if($("#orgunitIds").val()== "" || $("#orgunitIds").val() == null){
		FT.toAlert('warn',err_3_lang, null);
		return ;
	}
	usrTotalRow = userdataGrid.getCurrentTotal();
	// 赋值查询条件
	if(isGetQuery){
		queryObjData(isGetQuery);
	}
	var page = 1;
	if(stayCurrentPage) {
		page = userdataGrid.getCurrentPage();
	}
	var pagesize = userdataGrid.getCurrentPageSize();
    var queryParam = formParmsUser(page, pagesize);
    userdataGrid.loadServerData(queryParam);
}

//设置查询条件的值
function queryObjData(){
	queryData = new Object();
	//查询值
	queryData.userId = $.trim($("#userId").val());   
	queryData.realName = $.trim($("#realName").val());
	queryData.dOrgunitId = $("#orgunitIds").val();
	queryData.usbindState = $.trim($("#usbindState").val());   
}


//组装表单查询条件QueryFilter
function formParmsUser(page, pagesize){
    var queryParamU = {
		'page':page,
    	'pagesize':pagesize,
    	'queryForm.userId':queryData.userId,
        'queryForm.realName':queryData.realName,
        'queryForm.dOrgunitId':queryData.dOrgunitId,
        'queryForm.usbindState':queryData.usbindState,
        'source':"bindUser"
    }
    return queryParamU;
}

  //跳转到令牌查询/选择选项
 function stepQueryTnk(){
    var usrlen = 0;
    var usrArr ='';
    var orgArr = '';
	var usrSel =  $("#usrOperSel").find("option:selected").val();
	
 	if(usrSel == 0){
 		var sRows = userdataGrid.getSelectedRows();
        for(var i=0,sl=sRows.length;i<sl;) {
		    sRow = sRows[i++];
		    // Start 2013-5-4
		    for (var j=0,sl=sRows.length;j<sl;){
		    	sRow1 = sRows[j++];
		    	if(sRow['domainId'] != sRow1['domainId']){
		    		FT.toAlert('warn',err_2_lang, null);
					return ;
		    	}
		    }
		    // End 2013-5-4
	        usrArr  = usrArr + sRow['userId']+":"+sRow['domainId']+":"+sRow['orgunitId'] +","; //2013-5-2
	        orgArr = orgArr + sRow['domainId']+":"+sRow['orgunitId'] +",";
		}
		usrlen = sRows.length;
		$('#orgunitIds_').val(orgArr);
	}else if(usrSel == 1){
		usrTotalRow = userdataGrid.getCurrentTotal();
		usrlen = usrTotalRow;
		$('#orgunitIds_').val($('#orgunitIds').val());
	}
	$('#userTotal').val(usrlen);
	$('#userArr').val(usrArr);
	if(usrlen==0){
	   FT.toAlert('warn',err_4_lang, null);
	   return ;
	}else{
	   stepController(1);
	   query(false,true);
	}
}
 
/**
 * 处理返回信息的显示与动态隐藏
 * @param {} data 信息包装JSON
 */
function showAndHiddenMsg(data) {
	var jMSG = $("#msgShow").find('span').empty().append(data.object).end().fadeIn('slow');
        setTimeout(function(){jMSG.fadeOut('normal')},4500);
}
