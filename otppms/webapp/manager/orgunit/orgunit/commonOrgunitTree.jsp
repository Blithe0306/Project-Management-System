<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
	//得到传入的域标识
	String modetag = request.getParameter("modetag");
	
	//域id串
	String domainIds = request.getParameter("domainIds");
	
%>
<input type="hidden" id="mulDomianIds" value="<%=domainIds %>" />
<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/validate.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerTreeCommonOrgunit.js"></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerTab.js" ></script>
    <script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
<script type="text/javascript">
		var jsonArray;
        $(function ()
        {
        	async : true,
			$.post('orgunitInfo!init.action',null, getAnswerBack, 'json');
			
			function getAnswerBack(data){
				
				 var dataObj=eval(data);
				    //dataObj.items是个json对象数组 
				    jsonArray= dataObj.items;	
				    if(jsonArray == "" || jsonArray == null){
				    	// 提示组织机构为空，关闭窗口
				    	parent.closeOrgWin("<view:LanguageTag key="org_unsel_orgunit"/>");
				    }
				   
				    //过滤域 只显示指定的域
				    
				    if("6"=="<%=modetag%>" || "4"=="<%=modetag%>"){//指定的一些树 多选
				    	var domainIds=$("#mulDomianIds").val();
				    	var dArrays=domainIds.split(",");//指定的域
				    	var newJsonArray = [];
						for(var i=0;i<dArrays.length;i++){ //要显示的域 	
							for(var j=0;j<jsonArray.length;j++){//全部的树	每一次循环都是一棵树  一个域	
					    		if(dArrays[i]==jsonArray[j].id && jsonArray[j].flag==1){ //这个节点要保留
					    			newJsonArray.push(jsonArray[j]);
					    			break;
					    		}
				    		} 
			    		}
			    		jsonArray=newJsonArray;
				    }
				    
				    
				   
				    
				  //获得窗口中 文本域中 选中的组织机构 
		 	  		var orgunitIds=$("#orgunitIds",window.parent.document).val();
		 	  		 
				 //遍历dataObj.items 			 
				 for(var i=0;i<jsonArray.length;i++){ //每一次循环都是一棵树  一个域 
				 	dataObj.items[i].icon= "../../../images/manager/house.png";  	//先json对象中添加一个key/value 改变域的图标
				 	//默认的树展开状态
				 	if(jsonArray[i].flag==1 && jsonArray[i].isdefault==1){ //只有域 并且 是默认域 那么该节点全展开
				 	  	dataObj.items[i].isexpand= "true";  	//节点不展开 相当于<ul><li isexpand="true"><span>节点1</span><ul>other</ul></li></ul>
				 	 }else{
				 	    dataObj.items[i].isexpand= "false"; 
				 	 }
				 	
		 	  		//然后根据已经选中的机构 打上选中标记
		 	  		if(orgunitIds!=''){ //如果不是空的
			 	  		var orgunitIdArray=orgunitIds.split(","); // 该数组的每一元素是 domainId:orgunitId
			 	  		//判断该层机构是否选中 第一层都是域
			 	  		for(var j=0;j<orgunitIdArray.length;j++){
			 	  			var domianIdorgunitId=orgunitIdArray[j].split(":"); //该数组只有两个元素 第一个是 domainId  第二个事orgunitId
			 	  			if(jsonArray[i].id==domianIdorgunitId[0] && domianIdorgunitId[1]=="0"){
			 	  				jsonArray[i].ischecked=true;
			 	  				break;
			 	  			}
			 	  		}
			 	  		//判断该域下的每一个机构是否选中
			 	  		if(jsonArray[i].children!=null){
			 	  			var orgunits=jsonArray[i].children;
			 	  			for(var k=0;k<orgunits.length;k++){ //该层每一个机构
			 	  				for(var t=0;t<orgunitIdArray.length;t++){
			 	  					var domianIdorgunitId_=orgunitIdArray[t].split(":");
			 	  					if(orgunits[k].id==domianIdorgunitId_[1]){ //如果有这层机构
				 	  					orgunits[k].ischecked=true;
				 	  					break;
				 	  				}
				 	  			}
				 	  			checkedNextOrgunit(orgunits[k],orgunitIdArray);//下一层机构
			 	  			} //end for
			 	  		}
		 	  		} //end if
				 	  
				    
				 	  		
				 } //end for
				
				//判断这棵树 是否是单选
				var isSingleTag=false;
				if("1"=="<%=modetag%>" || "5"=="<%=modetag%>" || "4"=="<%=modetag%>"){
					isSingleTag=true;
				}
				
				//仅仅显示域 单选 
				if("5"=="<%=modetag%>"){					
					for(var i=0;i<jsonArray.length;i++){
						if(jsonArray[i].flag==1){ //域
							delete jsonArray[i].children; 
						}
					}
				}
				
				//定义每课树的样式 没一个域都是一棵树				
	            $("#wholeOrgunitTree").ligerTree({ 
	            	textFieldName:'name',
	            	nodeWidth:1000,
	                checkbox:true,
	                btnClickToToggleOnly: true,		// 是否只在点击+/—图标时展开/收缩节点,设置为false后点击节点任意位置均可触发
	                single:isSingleTag,	                
	                        
	                onCheck:function (node,checked){
	                     /*此处的向下操作 向上不操作 在源码中屏蔽掉 向上操作方法*/
					},
							
	              	data:jsonArray
	            });
	          manager = $("#wholeOrgunitTree").ligerGetTreeManager();
				
			} //end getAnswerBack
        	
        }); 
       
              
               
        //递归判断机构
        function checkedNextOrgunit(orgunit,orgunitIdArray){
        	//判断该域下的每一个机构是否选中
 	  		if(orgunit.children!=null){
 	  			var orgunits=orgunit.children;
 	  			for(var k=0;k<orgunits.length;k++){ //该层每一个机构
 	  				for(var t=0;t<orgunitIdArray.length;t++){
 	  					var domianIdorgunitId_=orgunitIdArray[t].split(":");
 	  					if(orgunits[k].id==domianIdorgunitId_[1]){
	 	  					orgunits[k].ischecked=true;
	 	  					break;
	 	  				}
	 	  			}
	 	  			checkedNextOrgunit(orgunits[k],orgunitIdArray); //下一层机构
 	  			} //end for
 	  		}
        }
        
        
        
        //选择组织机构 确定按钮事件
        function okClick(item,win,index) {
        	var notes = manager.getChecked();//获取选中的机构 
        
        	//组串之前根据据模式判断是否允许多个域组串
        	if("7"=="<%=modetag%>"){ // 多选同一个树中的节点
        		var domanId=0;
        		for(var i=0;i<notes.length;i++){	
        			domanId=notes[0].data.domainId;
        			if(notes[i].data.domainId!=domanId){//有不同域
        				FT.toAlert("warn",'<view:LanguageTag key="org_uncross_domain"/>',null);
        				return ;
        				
        			}
        		}
        	}
        
        	//组串  ： 第一个隐藏串orgunitIds  格式 domainid1:0,domainid1：orgunitid1,domainid2:orgunitid2,domainid3:orgunitid3 键值对  第二个显示串是域名+组织机构名串 dorgunitNames 域1，机构1，机构2，域2,机构1，机构2.. 
            var orgunitIds = ""; 
            var orgunitNames = "";
            var domainSignName = "";
            var orgunitSignName = "";
            var domainId = 0;
            for (var i = 0; i < notes.length; i++){
            	//组装id
            	if(notes[i].data.flag==1){ //如果是域
            		orgunitIds+=notes[i].data.id + ":"+0+",";  //0表示空机构 
            		//==============================20130221  zjy添加===============
            		domainSignName = notes[i].data.name;
            	}else{ //如果是组织机构
            		orgunitIds+=notes[i].data.domainId + ":"+notes[i].data.id+",";  //该机构的域：该机构id
            		//==============================20130221  zjy添加===============
            		orgunitSignName = notes[i].data.name;
            		domainId = notes[i].data.domainId;
            	}
            	//组装名
                orgunitNames+=notes[i].data.name + ","; 
                    
            }
            //=====================20130221  zjy添加 域和组织机构同时存在，显示为格式为： 域-->组织机构 ===============
            if("1"=="<%=modetag%>"){
               if(domainSignName==''){
                 var domainNode = getDomainObjByDomainId(domainId);
                 if(domainNode!=undefined&&domainNode!='undefined'){
                 	domainSignName= domainNode.name;
                	orgunitNames = domainSignName + "-->" + orgunitSignName;
                 }
               }else{
                 orgunitNames = domainSignName;
               }
            }   
             //======================根据定义的域标识，赋不同地方的域值 20130226  zjy==============
             
		    // 去掉显示名称的最后一个 ,
			if(orgunitNames!=""&&orgunitNames.indexOf(",")!=-1){
				orgunitNames = orgunitNames.substring(0,orgunitNames.length-1);
			} 
			
			if("4"=="<%=modetag%>"){
				if($("#orgunitNamesTemp",window.parent.document)!=null&&$("#orgunitNamesTemp",window.parent.document)!='undefined'){
                	$("#orgunitNamesTemp",window.parent.document).val(orgunitNames);
                }
	            if($("#orgunitIdsTemp",window.parent.document)!=null&&$("#orgunitIdsTemp",window.parent.document)!='undefined'){
	                  $("#orgunitIdsTemp",window.parent.document).val(orgunitIds);
	            }
	            if(win) win.close();
				return;
			}
			
			$("#orgunitNames",window.parent.document).val(orgunitNames); //显示机构名称
			$("#orgunitIds",window.parent.document).val(orgunitIds); //隐藏机构id  是域id和组织机构id的键值对 注意（域id：0 表示域 无组织机构） 
			//如果同时存在两个
			 if($("#orgunitIds_",window.parent.document)!=null&&$("#orgunitIds_",window.parent.document)!='undefined'){
				$("#orgunitIds_",window.parent.document).val(orgunitIds); //隐藏机构id  是域id和组织机构id的键值对 注意（域id：0 表示域 无组织机构）
			 }
			 if($("#orgunitNames_",window.parent.document)!=null&&$("#orgunitNames_",window.parent.document)!='undefined'){
				$("#orgunitNames_",window.parent.document).val(orgunitNames); //显示机构名称
			 }
			 $("#orgunitNames",window.parent.document).focus();
			 $("#orgunitNames",window.parent.document).blur();
			if(win) win.close();
		}
		
		
		//根据域id  找到 域对象
		function getDomainObjByDomainId(domainId){
			var domainNode;
			for(var i=0;i<jsonArray.length;i++){
				if(domainId==jsonArray[i].id){ //因为第一层就是域 所以不用递归方法
					domainNode= jsonArray[i]; //返回当前域对象  
					break;
				}
			}
	 
			return domainNode;
		}
		
</script>
	<div id="orgTreeList0" title='<view:LanguageTag key="org_tree"/>' class="l-scroll">
 		<ul  id="wholeOrgunitTree">
 		</ul>
	</div>
	 <input type="hidden" value="<%=path %>" id="contextPath" />

