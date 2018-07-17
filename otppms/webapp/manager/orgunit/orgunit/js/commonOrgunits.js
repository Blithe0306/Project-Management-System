var contextPath;
var winOrgClose;
$(function(){
	contextPath = $("#contextPath").val();
})
/*			

	modetag=0 表示 所有树 带复习框  复选  
	modetag=1 表示 所有树 带复习框  单选  
 	modetag=5 表示 所有树根 带复选框 单选
 	
 	modetag=4 表示 指定的一课树 带复习框 单选  特殊 
 	modetag=6 表示 指定的一些树 带复选框 多选   与domianIds配合使用
 	modetag=7 表示 所有树 带复选框  多选同一个树中的节点 --以实现待优化需要考虑源码
 			
 	modetag 模式
 	contextPath 根据机构 	
 	domainIds 指定域id串 逗号格式隔开	1,2,3,	
 */
function selOrgunits(modetag,contextPath,domainIds) {
    var domainIds=domainIds;
    
    if(modetag==4){ //重构
    	domainIds=$("#domaind").val()+",";
    }
    
    winOrgClose = FT.openWinMiddle(getLangVal('user_sel_org',contextPath),contextPath + '/manager/orgunit/orgunit/commonOrgunitTree.jsp?modetag='+modetag+'&domainIds='+domainIds,true);
    
    /*
		if(null!=oper&&oper!='undefined'&&oper=='queryByDomainId'){
		    FT.openWinMiddle('选择机构',contextPath + '/manager/orgunit/orgunit/commonOrgunitTree.jsp?modetag='+modetag+'&domainId='+$('#domaind').val(),true);
		}else{
			var domainIds="2,4,";
		 	FT.openWinMiddle('选择机构',contextPath + '/manager/orgunit/orgunit/commonOrgunitTree.jsp?modetag='+modetag+'&domainIds='+domainIds,true);
		}
	*/
}


