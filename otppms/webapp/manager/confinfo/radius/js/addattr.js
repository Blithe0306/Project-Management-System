 function checkAttr(){
		if ("" == $.trim($("#attrSel").val())){
			$("#attrSel").focus(); 
	        FT.toAlert('warn', name_lang, null);
	        return false;
	   	}
	   	
		if("custom" == $.trim($("#type").val())){
		 	if ("" == $.trim($("#attrName").val())){ 
		 		$("#attrName").focus();
	        	FT.toAlert('warn', custom_name_lang, null);
	        	return false;
	   		}
		    if ("" == $.trim($("#attrId").val())){ 
		    	$("#attrId").focus();
	        	FT.toAlert('warn', custom_id_lang, null);
	        	return false;
	   		}
	   		if(!checkNumber($.trim($("#attrId").val()))){
				FT.toAlert('warn', custom_id_err_lang, null);
				$("#attrId").focus();
	            return false;
			}
	   }
	   if ("" == $.trim($("#attrValue").val())){
			$("#attrValue").focus();  
	        FT.toAlert('warn', valtype_lang, null);
	        return false;
	   }
	   if("string" == $.trim($("#attrValueType").val())){ 
	   		 if(!checkStr($.trim($("#attrValue").val()))){
	   		   $("#attrValue").focus(); 
	           FT.toAlert('warn', valtype_err_lang, null);
	           return false;
	   		 }
	   }else if("integer" == $.trim($("#attrValueType").val())){
	          if(!checkNumber($.trim($("#attrValue").val()))){
	          	FT.toAlert('warn', valtype_err_1_lang, null);
	          	$("#attrValue").focus(); 
	           	return false;
	          }
	   }else if("ipaddr" == $.trim($("#attrValueType").val())){
	      	if(!checkConfIpAddr($.trim($("#attrValue").val()))){
	      		FT.toAlert('warn', valtype_err_2_lang, null);
	      		$("#attrValue").focus(); 
	      		return false;
	      	}
	   }else if("string encrypt=1" == $.trim($("#attrValueType").val())){
			if(!checkStr($.trim($("#attrValue").val()))){
	   		   $("#attrValue").focus(); 
	           FT.toAlert('warn', valtype_err_lang, null);
	           return false;
	   		 }
	   }else if("octets" == $.trim($("#attrValueType").val())){
	           return true;
	   }
	   return true;
 }