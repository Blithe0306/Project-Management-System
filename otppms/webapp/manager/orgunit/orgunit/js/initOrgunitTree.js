var contextPath;
$(function(){
	contextPath = $("#contextPath").val();
})

$(initMessage);
	  function initMessage(){
	  
	  		async : true,
	  		
	  		//加载树数据
			$.post('orgunitInfo!init.action',null, getAnswerBack, 'json'); 
			
			//设置焦点
			$("#searchText").focus(function(){
			 	if($(this).val() == key_word_lang){//搜索关键字
			 		$(this).val("");
			 	}
			});
			
			//失去焦点时 将隐藏搜索框
			anyEventHide();			
	  }
	  
	  function getAnswerBack(data){
			 var dataObj=eval(data);
			 //dataObj.items是个json对象数组 
			 var jsonArray= dataObj.items;	
			 var id = 0;
			  			 
			 for(var i=0;i<jsonArray.length;i++){ //每一次循环都是一棵树 
			 	  dataObj.items[i].icon= "../../../images/manager/house.png";  	//先json对象中添加一个key/value 对 每棵树的图标
			 	  if(jsonArray[i].flag==1 && jsonArray[i].isdefault==1){ //只有域 并且 是默认域 那么该节点全展开
			 	  	dataObj.items[i].isexpand= "true";  	//节点不展开 相当于<ul><li isexpand="true"><span>节点1</span><ul>other</ul></li></ul>
			 	  	id = jsonArray[i].id;
			 	  }else{
			 	    dataObj.items[i].isexpand= "false"; 
			 	  }			 		 
			 }
			//定义每课树的样式 每一个域都是一棵树
            $("#wholeOrgunitTree").ligerTree({ 
                slide:false,							//不以动画方式展示树
            	textFieldName:'name',
            	attribute: ['treedataindex'],            //识别唯一行的依据
                checkbox:false,
                nodeWidth:1000, 
                //定义选中事件
                onSelect:doMethod,
                btnClickToToggleOnly: false,
                onClick:avoidUnSelected,
              	data:dataObj.items
            });
          manager = $("#wholeOrgunitTree").ligerGetTreeManager();
          
          //选中域节点
		  selectOperNode(id,1);
		  
		} //end getAnswerBack
		
	//添加单击事件，为了避免选中某个组织机构后，再次点击选中的组织机构后选中被取消的现象	
	function avoidUnSelected(node){
		var html=node.target.innerHTML+"";
		if(!(html.indexOf("l-tree-icon-folder")>0)){
			manager.selectNode(node.target); 	
		}
	}
	
	/**
	**当节点的子节点都被删除后将文件夹样式变为非文件夹显示
	**/
	function changeNodeStyle(node){
		//获取被选中节点内容
  		var nodeTarget = node.target.innerHTML;
  		//获取被选中节点样式
  		var classText= $(node.target).attr("class");
  		//获取被选中节点名称
  		var spanText = nodeTarget.substring((nodeTarget.indexOf("<span>")+6),nodeTarget.indexOf("</span>"));
  		var nodetype;
  		//根据被选中节点样式判断文件夹转化生成子节点的样式
  		if(classText.indexOf("l-last")!=-1){//最后一个文件节点
  			nodetype = "l-box l-note-last";
  		}else{//非最后一个文件节点
  			nodetype = "l-box l-note";
  		}
  		//文节点不含有子节点时改变节点样式
  		if(nodeTarget.indexOf("l-tree-icon-leaf")==-1 && nodeTarget.indexOf("l-tree-icon-folder-open")>0){
  		 	node.target.innerHTML='<div class="l-body l-selected"><DIV class="l-box"></DIV><DIV class="'+nodetype+'"></DIV><DIV class="l-box l-tree-icon l-tree-icon-leaf "></DIV><SPAN>'+spanText+'</SPAN></div>';
  			reLoadTree();
  		}
	}

	//选中事件触发方法  将来搜索后也触发该方法
	function doMethod(node){
		var id;
	  	var flag;
	  	var readWriteFlag;
	  	if(node.data!=null){//正常选中
	  		id=node.data.id;
	  		flag=node.data.flag;
	  		readWriteFlag=node.data.readWriteFlag;
	  		
	  		changeNodeStyle(node);
	  		
	    }else{	//点击图标img house.png 是传递的改图标的node ； node.target 是html element 对象 即img对象
	        //var opionId= node.target.parentNode.parentNode.parentNode.id;
	        var opionId= $(node.target.parentNode.parentNode.parentNode).attr("treedataindex");		        	
	        var nodes = manager.getAllNodes();
	        for(var i=0;i<nodes.length;i++){//找到该图标对应的node节点
	        	//if(opionId==nodes[i].data.id){
	        	if(opionId == $(nodes[i].target).attr("treedataindex")){
	        		id=nodes[i].data.id;
	  				flag=nodes[i].data.flag;
	  				readWriteFlag=nodes[i].data.readWriteFlag;
	  				break;
	        	}
	        }
	    }
	        
	    if(id == undefined){
	        id = '';
	    }
	    if(flag == undefined){
	       	flag = '';
	    }
	    if(readWriteFlag == undefined){
	        readWriteFlag = '';
	    }
	    if('' != id && '' != flag && '' != readWriteFlag){
	        var url = 'orgunitInfo!view.action?treeOrgunitInfo.id=' + id+"&treeOrgunitInfo.flag=" + flag+"&treeOrgunitInfo.readWriteFlag=" + readWriteFlag;
	        
	    	// 判断是否存在此tab 不存在添加，存在覆盖修改，不要先删除remove再添加 删除会触发左侧导航刷新事件
	    	if(window.parent.isTabItem('08')){
	    		window.parent.overrideTabItemF('08',orgunit_lang,url);
	    	}else{
	    	    //window.parent.removeTabItemF('08');
				window.parent.addTabItemF('08',orgunit_lang, url);	    		
	    	}
	  	}
	}
	  	  
	//根据对象id得到该对象的信息 形成机构节点
	function addOrg(objId,flag,oper,from){
    	if(oper=='add'){ //添加节点 一个
      		$.post('orgunitInfo!findData.action',{objId:objId,flag:flag}, appendNode, 'json');
      	}else if(oper=='update'){ //修改节点 一个
      		$.post('orgunitInfo!findData.action',{objId:objId,flag:flag}, updateNode, 'json');
      	}
	}

    var parentNode;//添加节点的父节点
    
    //添加一个节点
    function appendNode(data){   
       	var dataObj=eval(data);
		var jsonArray= dataObj.items;//dataObj.items是个json对象数组 
		var jsonObj=dataObj.items[0];
		if(jsonObj != undefined){
			if(jsonObj.flag==1){ //添加一个域
			 	dataObj.items[0].icon= "../../../images/manager/house.png";  	//先json对象中添加一个key/value 对 每棵树的图标
	       		manager.append(null, jsonArray);
	       	}else if(jsonObj.flag==2){ //添加一个组织机构
	       	  //var nodes=manager.getData();//获取树的源数据 这是树对象数组 不是全部的节点 只是根节点对象数组
	       		var nodes=manager.getAllNodes();
	
	       		//找到父节点
	       		if(jsonObj.parentId==0 || jsonObj.parentId=='0'){ //如果是父节点id是0 那么父节点就是一个域
	     			for(var i=0;i<nodes.length;i++){
	     			 	if(jsonObj.domainId==nodes[i].data.id && nodes[i].data.flag==1){ //如果找到了父节点
	     			 		parentNode=nodes[i].target;
	     			 		break;
	     			 	}
	     			}
	       		}else{ //父节点是个组织机构
	       			for(var i=0;i<nodes.length;i++){
	       			 	if(jsonObj.domainId==nodes[i].data.domainId && jsonObj.parentId==nodes[i].data.id && nodes[i].data.flag==2){ //如果找到了父节点
	     			 		parentNode=nodes[i].target;     			 			  			 			
	     			 		break;		
	     			 	}
	     			}
	       		}
	       		manager.append(parentNode, jsonArray);       			 
	       	}
	    }
	} 
	
	// 根据相关数据选中节点
	function selectOperNode(id,flag){// flag 1:表示域，2：表示组织机构
		var nodes = manager.getAllNodes();
		if(nodes.length<=0){
			return;
		}
		for(var i = 0;i < nodes.length; i++){
   			if(id == nodes[i].data.id && nodes[i].data.flag == flag){
   				// 已选中的节点样式去掉
   				var currData = manager.getSelected();
   				if(currData != null&&currData != undefined){
   					var currNode = currData.target;
	   				var C = $(currNode),A = $(">div:first", C);
	   				A.removeClass("l-selected");
   				}
   				
   				// 选中节点
 				manager.selectNode(nodes[i].target); 			 			  			 			
 				break;		
 			}
   		}
	}

      //修改一个节点      
      function updateNode(data){
      	var dataObj=eval(data);
		var jsonArray= dataObj.items;//dataObj.items是个json对象数组 
		var jsonObj=dataObj.items[0];
		//找到对应的的节点 将其信息修改
		//var nodes=manager.getData();
		var targetNode;
		var nodes=manager.getAllNodes();		
      	for(var i=0;i<nodes.length;i++){
			if(jsonObj.id==nodes[i].data.id && nodes[i].data.flag==jsonObj.flag ){
				targetNode=nodes[i].target;								
				manager.update(targetNode, jsonObj); //改变目标节点 函数targetNode 是domnode  后面是 newnodedata
				break;
			}
	 	}
      }
      
    //删除一个节点后判断此父节点是否有子节点 hasChildren 判断不到故使用此方法判断
	function isHasChildren(id){
		var nodes = manager.getAllNodes();		
      	for(var i=0;i<nodes.length;i++){
			if(id==nodes[i].data.parentId){
				return true;
			}
	 	}
	 	
	 	return false;
	}
      
      //删除一个节点      
      function deleteNode(crrentOrgId,flag){
		var targetNode;
		var nodes=manager.getAllNodes();		
      	for(var i=0;i<nodes.length;i++){
			if(crrentOrgId == nodes[i].data.id && nodes[i].data.flag== flag ){
				targetNode=nodes[i].target;								
				manager.remove(targetNode);
				break;
			}
	 	}
      }
      
	  //删除N个节点 这里需要一个数组或者列表	    
      function deleteNodes(from){
      	  reLoadTree();      	  
      	  //删除标签
      	  window.parent.removeTabItemF('09010101');//将机构tag移除
      	  if(from==0){
      	  	window.parent.removeTabItemF('08');//将机构tag移除 解决切换标签问题
      	  }
      }
      
      //重新加载树
      function reLoadTree(){
      	  manager.clear();      	  	 
      	  initMessage();
      }

		 //初始div 并调用queryProviders函数将将搜索到的信息显示到div上
		function loadNodes(obj1_id,e){//弹出ajax动态查询数据库层
			if($("#s_result_div")!=null){$("#s_result_div").remove();}
			var obj1=$("#"+obj1_id);
			if(obj1.val()!=''){
				var obj_value=obj1.val();
				var w = obj1.attr("offsetWidth");
				var h = obj1.attr("offsetHeight");
				var x = obj1.attr("offsetLeft");
				var y = obj1.attr("offsetTop");
				
				var s_result_div=$("<div id=s_result_div></div>");//显示数据的div
				s_result_div.css({"display":"","position":"absolute","background":"#E8F2FE","height":"170px","filter":"alpha(opacity=80)","overflow-y": "hidden", "overflow-x": "hidden","z-index":"100","border":"1px solid #666","left":x,"width":w,"top":(y+h)});
				$(document.body).append(s_result_div);//添加到document.body上
				
				//将搜索的结果放到该div上 ,obj1.value是要搜索的依据词 
				queryNodes(s_result_div,obj_value);
				
				upDownEnter(s_result_div,e);
				//关闭搜索的结果 //这里实际是调用closeResult 参数是e
				
				//enterQuery(e);
			}
			 
	  }
		
	  //模糊检索机构列表
	  var conter='';
	  var sumLiHeigh = 0;// 拼装的li的总高度 不能超过div的高度
	  function queryNodes(div_obj,obj_value){
	  	  conter='';//每次检索前先清空 否则会一直累积
	  	  sumLiHeigh = 0;// 每次查询先轻0
	  	  var nodes=manager.getData(); //根节点
      	  for(var i=0;i<nodes.length;i++){
      	  	  if(nodes[i].name.indexOf(obj_value)!=-1){ //如果包含这个关键字
      	  	  	  var option=$('<li id='+nodes[i].id+' onmouseover="style.backgroundColor=\'#DCF8A8\'" onmouseout="style.backgroundColor=\'\'" onclick=\"javascript:searchNode(\''+nodes[i].name+'\');\"><table width="100%"><tr><td align="left">'+nodes[i].name +'</td></tr></table></li>');
      	  	  	  //适用于键盘
      	  	  	  //option.unbind().bind('onmouseover',function(){setSelectedOptBgColor(option);});
      	  	  	  //option.unbind().bind('onmouseout',function(){clearSelectedOptBgColor(1,option);});
      	  	  	  //option.unbind().bind('onclick',function(){searchNode(nodes[i].name); });
      	  	  	  // 装入的li的高度和 不能大于div 的高度
      	  	  	  if(sumLiHeigh<=div_obj.height()){
      	  	  	  	div_obj.append(option);// 必须append后 option才有高度值
      	  	  	  	sumLiHeigh += option.height();
      	  	  	  	if(sumLiHeigh>div_obj.height()){
      	  	  	  		option.remove();
      	  	  	  	}
      	  	  	  }else{
					break;      	  	  	  
      	  	  	  }
      	  	  	  //conter+='<li onmouseover="style.backgroundColor=\'#E8F2FE\'" onmouseout="style.backgroundColor=\'\'" onclick=\"javascript:searchNode(\''+nodes[i].name+'\');\"><table width="100%"><tr><td align="left" >'+nodes[i].name +'</td></tr></table></li>';	
      	  	  }
      	  	 queryChildNodes(nodes[i],obj_value,div_obj);
      	  }
      	  //div_obj.html("");
		  //div_obj.html(conter);
	  }	
	  
	  //递归模糊检索子机构列表 
	  function queryChildNodes(node,obj_value,div_obj){
      	   if(node.children!=null){//如果有子节点
      	      var childNodes = node.children; 
	      	   for(var i=0;i<childNodes.length;i++){
	      	  	  if(childNodes[i].name.indexOf(obj_value)!=-1){
		      	  	  var option = $('<li id='+childNodes[i].id+' onmouseover="style.backgroundColor=\'#DCF8A8\'" onmouseout="style.backgroundColor=\'\'" onclick=\"javascript:searchNode(\''+childNodes[i].name+'\');\"><table width="100%"><tr><td align="left">'+childNodes[i].name +'</td></tr></table></li>');
	      	  	  	  //适用于键盘
	      	  	  	  //option.unbind().bind('onmouseover',function(){setSelectedOptBgColor(option);});
	      	  	  	  //option.unbind().bind('onmouseout',function(){clearSelectedOptBgColor(1,option);});
	      	  	  	  //option.unbind().bind('onclick',function(){searchNode(childNodes[i].name); });
	      	  	  	  // 装入的li的高度和 不能大于div 的高度
	      	  	  	  if(sumLiHeigh<=div_obj.height()){
	      	  	  	  	 div_obj.append(option);
	      	  	  	  	 sumLiHeigh += option.height();
	      	  	  	  	 if(sumLiHeigh>div_obj.height()){
	      	  	  	  		option.remove();
	      	  	  	  	 }
	      	  	  	  }else{
						 break; 	  	  
	      	  	  	  }
	      	  	  	  //conter+='<li onmouseover="style.backgroundColor=\'#E8F2FE\'" onmouseout="style.backgroundColor=\'\'" onclick=\"javascript:searchNode(\''+childNodes[i].name+'\');\"><table width="100%"><tr><td align="left" >'+childNodes[i].name +'</td></tr></table></li>';
	      	  	  }
	      	  	  queryChildNodes(childNodes[i],obj_value,div_obj);
	      	  }
      	   }
      }
			
      //检索对应node 并触发 方法 doMethod 处理gainode 
      function searchNode(keyText){
      	  var nodes=manager.getAllNodes();
      	  for(var i=0;i<nodes.length;i++){      	 	 
      	  	  if(nodes[i].data.name==keyText){      	  	  		
      	  	  		//doMethod(nodes[i]);    //打开右窗口  	  	  		
      	  	  		expendTree(nodes[i]); //展开所在树
      	  	  		break;
      	  	  }      	  	 
      	  }
      	 //在此关闭搜索列表 同时赋值给搜索框
      	  $("#s_result_div").remove();
      	  $("#searchText").val(keyText);
      }     

	  //展开 机构所在的树 源码开发
	  function expendTree(node){
	  	 var nodes=manager.getAllNodes();
	  	 for(var i=0;i<nodes.length;i++){
	  	 	var tree=0;
	  	 	if(node.data.flag==1){//如果是域	  	 	       	 	 
	      	  	 if(nodes[i].data.id==node.data.id){//找到节点所在的树
	      	  	 	 tree=1;
	      	  	 }
      	  	 }else{ //如果是机构
      	  	 	if(nodes[i].data.id==node.data.domainId){//找到节点所在的树   
		      	  	 tree=1;
	      	  	 }
      	  	 }
      	  	 if(tree==1){//找到了树
	      	  	 if(nodes[i].isexpand!=true){ //展开所在的树   	  	  		
	  	  		     $(".l-expandable-close", nodes[i].target).click();      	  	  		
	  	         }
	  	  		 $(node.target).click(); //触发该节点事件  其实质是 选中点击树的节点 打开右窗口
	  	  		 //$(".l-body", node.target).removeClss("l-selected").addClass("l-selected");//该节点底色始终处于选中背景色
	  	  		 break;
      	  	 }
      	  } // end for
      	  
	  }
	  
	  //键盘上下键 选项 回车键打开选项
	  var seletedIndex=-1;
	  function upDownEnter(div_id,event){
	  	var upKeyCode = 38;
		var downKeyCode = 40;
		var enterKeyCode = 13;
	  
		var options =$("#s_result_div li");
		var length=options.length;
	
		var eventKeyCode;		
		 if($.browser.mozilla) { //如果是 fireFox
		 	eventKeyCode=event.which;
		 }else{
		 	eventKeyCode=event.keyCode;
		 }
		
		if(parseInt(length)>0){
		    switch (eventKeyCode)
		    {
		        case upKeyCode:
		            clearSelectedOptBgColor(seletedIndex,options[seletedIndex]);
		            seletedIndex--;
		            if (seletedIndex < 0)
		                seletedIndex = length - 1;
		            setSelectedOptBgColor(options[seletedIndex]);
		            break;
		
		        case downKeyCode:
		        	//alert("hello");
		            clearSelectedOptBgColor(seletedIndex,options[seletedIndex]);
		            seletedIndex++;
		            if (seletedIndex >= length)
		                seletedIndex = 0;
		            setSelectedOptBgColor(options[seletedIndex]);
		            break;
		
		        case enterKeyCode:
		        	if(seletedIndex==-1){// 默认打开第一个
		        		options[0].click();
		        	}else{
			        	options[seletedIndex].click(); //触发单击事件
		        	}
		        	seletedIndex=-1; //每触发一次 就回底层一次
		            //searchNode(options[seletedIndex].id);
		            break;
		    }
	    }else{
	    	if(eventKeyCode==13){
	    		// 无查询数据
	    		parent.FT.toAlert('warn',search_data_lang,null);
	    	}
	    }
	    
	}
	//设置背景颜色
	function clearSelectedOptBgColor(seletedIndex,obj)
	{
	    if (seletedIndex >= 0){	        	
        	obj.style.backgroundColor = "";
        	//obj.css({"background":""});  iE 不支持
        }
	}
	function setSelectedOptBgColor(obj)
	{
	    obj.style.backgroundColor = "#DCF8A8";
	    //obj.css({"background":"#E8F2FE"}); ie不支持
	}
	
	//失去焦点时 将隐藏搜索框
	function anyEventHide(){
		/*
		var eventKeyCode;		
		 if($.browser.mozilla) { //如果是 fireFox
		 	eventKeyCode=event.which;
		 }else{
		 	eventKeyCode=event.keyCode;
		 }
		
		if(eventKeyCode!=null){//任何事件触发 都将隐藏结果
			$("#s_result_div").hide();
		}
		*/
		
		$("#searchText").blur(function(){ //失去焦点时 影响鼠标点击事件 
			$("#s_result_div").delay(1000).hide(1); //延时1秒再隐藏 等待鼠标点击事件  因为先失去焦点 后执行隐藏框中的鼠标点击事件
		});
		
	}
	