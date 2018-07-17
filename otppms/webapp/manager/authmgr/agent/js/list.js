var contextPath;
var waitW = null;

$(function(){
	contextPath = $("#contextPath").val();
	var loadPage = 1;
	if($("#curPage").val() != '') {
		loadPage = $("#curPage").val();
	}
	
	window['dataGrid'] = $("#listDataAJAX").ligerGrid({
		root : 'items',
        record : 'results',
        url : 'authAgent!init.action',
        columns : [
        	{display:auth_agent_agentname_lang,name:'agentname', width:'15%'},
            {display:agent_agentip_lang,name:'agentipaddr', width:'14%',render:agentIpAddrRender},
            {display:ser_hostip_lang,name:'hostIps', width:'14%',render:hostIpsRender},
            {display:agent_type_lang,name:'agenttypeStr', width:'20%'},
            {display:agent_agentconf_lang,name:'agentconfStr', width:'20%'},
            {display:syntax_operation_lang,name:'', width:'15%',render:operationRenderer}
        ],
        checkbox:true,
        rownumbers:false,
        allowUnSelectRow : true,
        rowClickSingleSelect : true,
        enabledSort:false,
        parms:{'operType':1},
        page : loadPage,
        pageSize:20,
        height:'100%',
        onAfterShowData:function() {
        	dataGrid.clearParams('operType');
        }
	});
	$("td :input").keydown(function(e) {
		if(e.keyCode == '13') {
			query(false);
		}
	});
})

// ================================  Renderers  =======================================

//认证代理IP列
function agentIpAddrRender(data,index) {
	return "<a href='javascript:viewAgent(\""+data.agentipaddr+"\",\""+data.groupId+"\");'>"+data.agentipaddr+"</a>";
}

//认证服务器IP列
function hostIpsRender(data,index) {
	var hrefs = "";
	$(data.hostIps).each(function(i, ite){
		hrefs += "<a href='javascript:viewServer(\""+ite.hostipaddr+"\");'>"+ite.hostipaddr+"</a>";
		if(i < data.hostIps.length){
			hrefs += "&nbsp;&nbsp;";
		}
	})
	return hrefs;
}

//是否启用
function enabledRender(data,index) {
	return data.enabled == 1 ? '<span style="color:green">'+ langYes +'</span>' : langNo;
}

// 操作列渲染器
function operationRenderer(data,rIndex) {
	var hostIpsLen  = data.hostIps.length;
	var rels = "";
	
	if(data.enabled == 0) 
	permEnabled = permEnabled.replace('lock.png','lock_go.png'),
	permEnabled = permEnabled.replace(langDisabled, langEnable);
	if(data.enabled == 1)
	permEnabled = permEnabled.replace('lock_go.png','lock.png'),
	permEnabled = permEnabled.replace(langEnable, langDisabled);
	//编 辑
	if(permEdit!=''){
		rels += '<a href="#" onclick="findObj(\''+data.agentipaddr+'\');">'+permEdit+'</a>';
	} 
    //添加认证服务器
    if(permAdd!=''){
       var hostIpArr = '';
       var obj = data.hostIps;
       for(var i=0;i<obj.length;i++){
         hostIpArr = hostIpArr + obj[i].hostipaddr +",";
       }
       rels += '<a href="javascript:bindAS(\''+data.agentipaddr+'\',\''+hostIpArr+'\');">'+permAdd+'</a>';
    }
    if(permEnabled != ''){
       rels += "<a href='javascript:editEnabled(\"" + data.agentipaddr + "\",\"" + (data.enabled == 0 ? 1 : 0) + "\")'>" + permEnabled + "</a>";
    }
    
    // 解除
    if(data.hostIps!=''&&data.hostIps!=null){
		if(permFree!=''){
    		rels += '<a href="javascript:unbindAS(\''+data.agentipaddr+'\',\''+hostIpsLen+'\');">'+permFree+'</a>';
    	}
    }
    
    //下载代理配置文件
	if(permDown!=''){
		rels += '<a href="#" onclick="downLoad(\''+data.agentipaddr+'\');">'+permDown+'</a>';
	}
    
    if(hostIpsLen==1){
    	$(data.hostIps).each(function(i,ite){
    		var hideId = data.agentipaddr+"hidehostIp";
    		rels += '<input type="hidden" name="hidden" id="'+hideId+'" value="'+ite.hostipaddr+'"/>';
   		});
    }
    return rels;
}

// ================================ Operations =======================================
//查询
function query(currentPage){
	var page = 1;
	if(currentPage) {
		page = dataGrid.getCurrentPage();
	} 
    var pagesize = dataGrid.getCurrentPageSize();
    
	var qParams = formParms(page, pagesize);
	dataGrid.loadServerData(qParams);
}

//组装表单查询条件QueryFilter
function formParms(page, pagesize){
	var agentIPAddr = $("#agentIPAddr").val();
	var confVal = $("#agentconfid").val();
	var agentType = $("#agentType").val();
	var hostIPaddr = $("#hostIPAddr").val();
	var qParams = {
		'page':page,
		'pagesize':pagesize,
		'agentQueryForm.agentIPaddr' : agentIPAddr,
		'agentQueryForm.agentConfid':confVal,
		'agentQueryForm.agentType':agentType,
		'agentQueryForm.hostIPaddr':hostIPaddr
	}
    
    return qParams;
}

//启用，禁用认证代理
function editEnabled(agentipaddr, enabled) {
	var message = confirm_enable_lang;
    if(enabled == 0) {
        message = confirm_disabled_lang;
    }
    $.ligerDialog.confirm(message,syntax_confirm_lang,function(sel) {
	    if(sel) {
		    $.post(contextPath + '/manager/authmgr/agent/authAgent!enabledAgent.action',{
				agentipaddr:agentipaddr,
				enabled:enabled
		    },function(result){
		        if(result.errorStr == 'success'){	            			
			         $.ligerDialog.success(result.object, syntax_tip_lang, function(){
			            query(true);
			         });
			    }else {
			            FT.toAlert(result.errorStr, result.object, null);
			    }
		    },'json'
		   );
        }
    });
}

// 添加认证服务器
function bindAS(agentipaddr,hostIpArr){
	$('#ipaddr').val(agentipaddr);
	$('#hostIps').val(hostIpArr);
	waitW = FT.openWinMiddle(auth_server_lang,contextPath+"/manager/authmgr/agent/sel_servers.jsp?flag=1",false);
}

// 添加认证服务器，确定
function okServer(hostipArr){
	var ipaddr = $('#ipaddr').val();
	var hostIps = $('#hostIps').val();
	var cpage = 1;
	var url = contextPath + "/manager/authmgr/agent/authAgent!selServer.action";
	$.ajax({
		async:false,    
		dataType : "json",  
		type:"POST", 
		url : url,
		data:{ipaddr:ipaddr,hostipArr:hostipArr,hostIps:hostIps},
		success:function(msg){
		     if(msg.errorStr == 'success'){
		     	waitW.close();
		     	$.ligerDialog.success(succ_tip_lang,syntax_tip_lang,function(){
		        	query(true);
		     	});
		     }else{
			 	FT.toAlert(msg.errorStr,msg.object,null);
		     }
		}
    }); 
}
