var contextPath;
var usrTotalRow = 0; 
var queryData = null;//保存令牌查询条件 避免执行的数据和显示的查询条件不一致

$(function(){
	var usbindState =   $("#usbindState").find("option:selected").val();
	contextPath = $("#contextPath").val();
	//用户记录
	window['userdataGrid'] = $("#userListAjx").ligerGrid({
		root : 'items',
        record : 'results',
        url : 'userChange!usrQuery.action',
        columns : [
            {display:account_lang,name:'userId', width:'12%'},
            {display:real_name_lang,name:'realName', width:'12%'},
            {display:orgunit_id_lang,name:'domainId', width:'5%',hide:'1'},
            {display:title_orgunit_lang,name:'DOrgunitName', width:'15%'},
            {display:email_lang,name:'email', width:'12%'},
            {display:bind_lang,name:'usbindTag', render:usbindRenderer,width:'10%'} 
        ],
        parms:{operType:'1',usbindState:usbindState,assignInit:1},
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
	$("td :input").keydown(function(e) {
		if(e.keyCode == '13') {
			 queryUsr(false,true);
		}
	});
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
    var queryParam = formParms(page, pagesize);
    userdataGrid.loadServerData(queryParam);
}

//设置查询条件的值
function queryObjData(){
	queryData = new Object();
	//查询值
	queryData.dOrgunitId = $("#orgunitIds").val();
	queryData.userId = $.trim($("#userId").val());
	queryData.usbindState = $.trim($("#usbindState").val());
	queryData.realName = $.trim($("#realName").val());
}


//组装表单查询条件QueryFilter
function formParms(page, pagesize){
	var assignInit = "";
	
	// 刷新时特殊处理，判断机构是否为空
	if(queryData.dOrgunitId == ""){
		assignInit = "1";
	}
    var queryParamU = {
		'page':page,
    	'pagesize':pagesize,
        'queryForm.dOrgunitId':queryData.dOrgunitId,
        'queryForm.userId':queryData.userId,
        'queryForm.usbindState':queryData.usbindState,
        'assignInit':assignInit,
        'queryForm.realName':queryData.realName
    }
    return queryParamU;
}


//转入选择组织机构
function nextSelOrgunit(){
	var usrSel =  $("#usrOperSel").find("option:selected").val();
	if(usrSel == 0){
	    var sRows = userdataGrid.getSelectedRows(),
		sInfo = [];
		var userBatchSn = "";
		var tokenSn = "";
		var tknOrgunitSn = "";
		if(sRows.length<1){
		   FT.toAlert('warn',sel_user_lang,null);
		   return;
		}
		for(var i=0,sl=sRows.length;i<sl;) {
			var role = {},sRow = sRows[i++];
			role.token = sRow['userId'];
			
			// 令牌号、令牌的机构ID
			for(var j=0; j<sRow['tokens'].length; j++){
				tokenSn = tokenSn + sRow['tokens'][j].token + ",";
				tknOrgunitSn = tknOrgunitSn + sRow['tokens'][j].orgunitId + ",";
			}
			userBatchSn = userBatchSn + sRow['userId'] + ",";
		}
		
		//选择的用户的个数
		$('#assignUserCount').html(sRows.length);
		//选择的令牌字符串
		$('#assignUser').html(userBatchSn);
		$('#assignToken').html(tokenSn);
		$('#assignTknOrg').html(tknOrgunitSn);
	}else{
		usrTotalRow = userdataGrid.getCurrentTotal();
		if(usrTotalRow<1){
		   FT.toAlert('warn',sel_user_lang,null);
		   return;
		}
		usrlen = usrTotalRow;
		//选择的用户的个数
		$('#assignUserCount').html(usrlen);
	}
	var domain_user = $('#domaind').val();
	var orgunit_org = $('#orgunitIdsTemp').val();
	var domain_org = orgunit_org.split(":")[0]
	if(domain_user!=domain_org){	
		$('#orgunitNamesTemp').val('');
		$('#orgunitIdsTemp').val('');
	}
    stepController(1);
}
