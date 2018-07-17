<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.util.Date"%>

<%
	String path = request.getContextPath();
%>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
		<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet"  type="text/css">
		<link href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
		 <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
		<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
		<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
		<script src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerTree.js" type="text/javascript"></script>
		<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerTab.js" ></script>
		<script src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js" type="text/javascript"></script>
		<script src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerMenu.js" type="text/javascript"></script>
		<script src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDrag.js" type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/manager/common/js/json.js"></script> 
		<script language="javascript" src="<%=path%>/manager/admin/role/js/map.js"></script> 
		<script language="javascript" type="text/javascript">
	<!--
	   var manager = null;
       var treeid = '${param.treeId}';
       var chhbox = true;
       if('${param.oper}'=='view' || '${param.oper}'=='viewrole'){
          chhbox = false;
       }
		$.ajax({   
	    	url : "<%=path%>/manager/admin/role/adminRole!getJsonDataById.action?permid=${param.permid}&t="+'<%=new Date()%>',
	    	type : "post",
	    	dataType:"JSON",
	    	success:function(jsonStr){
	    	var json =jsonStr;
	    	       $("#"+treeid).ligerTree({
					data:  JSON.parse(jsonStr),
					checkbox: chhbox,
					onCheck:onCheck
		        });
	            manager = $("#"+treeid).ligerGetTreeManager();
	            expandAll(manager); 	             
	    	} ,
			error:function(e){alert('error！');}  
		}); 
         

       function onCheck(node,checked){
         if(checked){//点击任何一个操作权限————————关联勾选查询权限
             
         	 //日志特殊处理部分
			 if(node.data.id == '060001'){
				  setChecked('060002');
				  setChecked('060003');
			 }
			 
			 if(node.data.id == '020003' || node.data.id == '020004' || node.data.id == '020005' || node.data.id == '020006' || node.data.id == '020002'){
				  setChecked('020001');
			 }
			 if(node.data.id == '030003' || node.data.id == '030004' || node.data.id == '030005' || node.data.id == '030006' || node.data.id == '030002' || node.data.id == '030007' || node.data.id == '030008'){
				  setChecked('030001');
			 }
			 if(node.data.id == '030203' || node.data.id == '030204' || node.data.id == '030202' || node.data.id == '030205'){
				  setChecked('030201');
			 }
			 if(node.data.id == '030302' || node.data.id == '030303' || node.data.id == '030304' ){
				  setChecked('030301');
			 }
			 
	         var key = getkeys(node.data.id);
	         if (key!=""){
			 	 $('#lid'+key).removeClass("l-box l-checkbox l-checkbox-unchecked").addClass("l-box l-checkbox l-checkbox-checked");    
	         } 
         }else{		//点掉查询————————任何一个操作权限为勾选状态，查询权限必须勾选上
           var cmids = getcmkey(node.data.id);
	       if(cmids=='undefined' || cmids==null){
	           return;
	       }
	          var cmid = cmids.split(",");
	          for(var i=0;i<cmid.length;i++){
	              var tid = cmid[i];
	               if($('#lid'+tid).attr("class")=='l-box l-checkbox l-checkbox-checked'){//选中状态
                      $('#lid'+node.data.id).attr("class","l-box l-checkbox l-checkbox-checked");
                      break;
           		}
	          }        
         }
       }
       
        //根据id设置为checked
	    function setChecked(nodeId){
		  $('#lid'+nodeId).removeClass("l-box l-checkbox l-checkbox-unchecked").addClass("l-box l-checkbox l-checkbox-checked"); 
	    }

        //ligerTree.js源代码中调用此方法获取选中和未完全选中的节点
        function getCheckdData(){
             var tabId = parent.navtab.getSelectedTabItemID(); 
        	 var treemanager = $("#"+tabId).ligerGetTreeManager();
        	 var adminperms = parent.parent.$("#adminPerms option");
         
        	 var admPerms = parent.parent.document.getElementById("adminPerms");
       			if(treemanager!=null){
	               var checkedArray = treemanager.getChecked();
	               if(adminperms.size()>0){
	               	  for(var i = 0;i<adminperms.size(); i++){
		               	  var itemId = tabId.substring(4,tabId.length);
			              var nodeLen = (adminperms[i].value).length;
			              var nodeId = (adminperms[i].value).substring(0,nodeLen-(nodeLen-2));
			            //当前Tab中选中
	                   if(itemId == nodeId){
	                       var bool = isExistCheckedArray(adminperms[i].value,checkedArray);
		                   if(!bool){
		                      removeToAdminOption(adminperms[i].value);
		                   }
	                   }
	                 }
	               }
	              //增加选中或未完全选中的节点到adminoption中
	              if(checkedArray.length>0){
	                addToAdminOption(checkedArray,admPerms);
	              }
		          
         		}
        }     

        function expandAll(treemanager){
            treemanager.expandAll();
        }
        
       //判断之前选中的节点是否和现在选中的节点一致 
      function isExistCheckedArray(nodeId,checkedArray){  
			    if(typeof nodeId=="string"){  
			        var len = checkedArray.length;  
			        if(len!=0){
			        	for(var i=0;i<len;i++){  
				           if(nodeId == checkedArray[i].data.id){   
				                return true;  
				           }  
			          }  
			       }
			      return false;  
			   }  
		}  
       
       
        //从管理员权限option中移除
        function removeToAdminOption(nodeId){
              if(nodeId!=null){
             	 parent.parent.$("#adminPerms option[value='"+nodeId+"']").remove();
              }
        }
        
        
        //增加选中的权限点到管理员权限下拉框中
        function addToAdminOption(notes,adminperms){  
	            for (var j = 0; j < notes.length; j++){
	                    getData(adminperms,notes[j].data.id,notes[j].data.text);
	            } 
        }
        
		//获取选中的checkbox值赋给下拉框
		function getData(admPerms, id, eValue){
			  var noRoles = true;
			  var len = admPerms.options.length;
				if(len > 0){
					for(var k=0; k<len; k++) {
						var role = admPerms.options[k].value;
						if(role == id){
							noRoles = false;
						}
					}
			    }
				if(noRoles){
					var option = new Option();
					option.value = id;
					admPerms.options[admPerms.options.length] = option;
				}
			}
 
	//-->
	</script>
 </head>
 <body>
  <!-- 带复选框和图标 -->
  <div style="margin:10px;float:left;overflow:auto;"><ul id="${param.treeId}"></ul></div>
 </body>
</html>
