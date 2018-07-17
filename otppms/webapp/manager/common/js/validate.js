	/**
	 * 校验IP
	 */
	function checkIpAddr(ipStr) {
		if (ipStr == "") {
			return false;
		}
		if (ipStr == "localhost") {
			return true;
		}
		var pattern = /^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/;
		flag_ip = pattern.test(ipStr);
		if (!flag_ip) {
			return false;
		}
		return true;
	}
	
	/**
	 * 校验Port
	 */
	function checkPort(portStr) {
		if(portStr == ""){
			return false;
		}
		var regu = "^[0-9]+$";
		var re = new RegExp(regu);
		if (!((portStr.search(re) != -1) && portStr < 65536)) {
			return false;
		}
		return true;
	}

	/*
     * 检查输入字符串是否为空或者全部都是空格
     */
    function isNull(str) { 
		if (str == "") return true; 
		var regu = "^[ ]+$"; 
		var re = new RegExp(regu);     
		return re.test(str);
    }

 	/*
	 * 判断是否是汉字、字母、数字组成
	 */
	function isChinaOrNumbOrLett( str ){
		var regu = "^[0-9a-zA-Z\u4e00-\u9fa5]+$"; 
		var re = new RegExp(regu); 
      
		if (!re.test(str)){ 
			//alert("请输入汉字字母或数字的组合");
			return false; 
		}
		return true;
	}
	
	 /**
	 *检查用户真实姓名：英文1-20，中文1-10个字符
	 */
	function checkRealName(name){
		var reg = /^[\u4e00-\u9fa5|A-Za-z]*$/;
		if(reg.test(name)==true){
		    return true;
		}
		
		return false;		 
	}
	
	/**
	 * 检查字符串的长度
	 */
	function strLength(str) {
		if (str != "") {
			if (str.length < 4 || str.length > 12) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 *检查只能输入数字和英文字母
	 */
		function strIsNumberOrLetter(checkobj) {
		var checkOK = "0123456789 -_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		var checkStr = checkobj;
		var decPoints = 0;
		for (i = 0; i < checkStr.length; i++) {
			ch = checkStr.charAt(i);
			for (j = 0; j < checkOK.length; j++) {
				if (ch == checkOK.charAt(j)) {
					break;
				}
			}
			if (j == checkOK.length) {
				return false;
				break;
			}
		}
		return true;
	}

     /*
      * 检查字母或数字 + 字母或数字或下划线组成的格式
      */
     function isNumberOr_Letter( s ) {
		/*  ^[0-9a-zA-Z]+([0-9a-zA-Z\_])+$  字母或数字 + 字母或数字或下划线组成 */
		var reg = "^[0-9a-zA-Z]+([0-9a-zA-Z\_])+$"; 
		var regu = "^[0-9a-zA-Z\_]+$"; 
		var re = new RegExp(reg); 
       
		if (!re.test(s)){ 
			//alert("格式错误:由字母或数字 + 字母或数字或下划线组成");
			return false;
		}
		return true;
    }

     /*
      * 字母开始+数字的组合
      */
     function checkNumAddChar(str){
		if(/^[a-zA-Z]([a-zA-Z0-9])*$/.test(str)){ 
			//alert('正确');
			return true;
		}
		
		return false;
     }

     /*
      * 字母开始或数字的组合
      */
     function checkNumOrChar(str){
		if( str != "" && (/^[a-zA-Z0-9]*$/.test(str))){ 
			alert('正确');
		}else{
			alert('格式错误:输入以字母数字组合');
			return false;
		}
		return true;
     }
     
     /*
      * 字母开始或数字的组合
      */
     function checkNumOrChar2(str) {
		if (str != ""){
			if (str.match(/\W/) == null){
				alert ("符合要求"); 
			}
			else {
				alert ("不符合要求"); 
				return false;
			}
		}else{
			alert("请输入");
			return false;
		}
		return true;
     }

     /*
      * 验证身份证号
      */
     function checkIndentity(str){ 
             
             var len = str.length, re; 
      
             if (len == 15){ 
                     re = new RegExp(/^(\d{6})()?(\d{2})(\d{2})(\d{2})(\d{3})$/gi); 
             }
             else if (len == 18){ 
                       re = new RegExp(/^(\d{6})()?(\d{4})(\d{2})(\d{2})(\d{3})(\d|x)$/gi); 
               }
               else { 
                      // alert("输入的数字位数不对！"); 
                       return false; 
               }
       
             var a = str.match(re); 
      
             if (a != null) 
             { 
              if (len==15) 
              { 
                      var D = new Date("19"+RegExp.$3+"/"+RegExp.$4+"/"+RegExp.$5); 
                      var B = D.getYear()==RegExp.$3&&(D.getMonth()+1)==RegExp.$4&&D.getDate()==RegExp.$5; 
              } 
              else 
              { 
               var D = new Date(RegExp.$3+"/"+RegExp.$4+"/"+RegExp.$5); 
               var B = D.getFullYear()==RegExp.$3&&(D.getMonth()+1)==RegExp.$4&&D.getDate()==RegExp.$5; 
              } 
       
              if (!B)
              {
                      //alert("输入的身份证号 "+ a[0] +" 里出生日期不对！"); 
                      return false;
              } 
       
             }
             else 
             { 
                      //alert("输入正确的身份证号");
                      return false 
             }
    
      return true;
    }
    
    /*
     * 验证IP地址
     */
    function isIP(strIP)
    {
            //if(strIP==null || strIP == ”)
            if(isNull(strIP))
            { 
                    alert("请输入正确的IP地址");
                    return false; 
            }
     
            var re=/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/g //匹配IP地址的正则表达式 
     
            if(re.test(strIP)) 
            { 
                    if( RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256)
             {
                     return true; 
             }
            }else
            {
                    alert("请输入正确的IP地址");
                    return false; 
            }
    }

    /*
     * 检查输入对象的值是否符合端口号格式
     */
    function isPort( str )
    { 
     
            var regu = "^[0-9]+$"; 
            var re = new RegExp(regu); 
     
            if(!((str.search(re) != -1) && str<65536)){
                    alert("端口号不正确");
                    return false;
            }
     
            return true;
    }

    /*
     * 检查输入对象的值是否符合整数格式
     */
    function checkTextDataForNUMBER( str )
    { 
		var regu = /^[-]{0,1}[0-9]{1,}$/; 
		if(!regu.test(str)){
			//alert("请输入整数");
			return false;
		}
		return true;
    }

    /*
     * 检查输入手机号码是否正确
     */
    function checkMobile( str)
    {   
            var regu =/^[1][3][0-9]{9}$/; 
            var re = new RegExp(regu);

            if (!re.test(str)) { 
                    //alert("请重新输入手机号");
                    return false; 
            }else{ 
                    return true; 
            }
    }

    /*
     * 检查输入字符串是否符合正整数格式
     */
    function isNumber( str ){ 
		var regu = "^[0-9]+$"; 
		var re = new RegExp(regu); 
     
		if (str.search(re) != -1){ 
                    return true; 
		}
		else { 
			alert("请输入正整数");
			return false; 
		}
    }
    
    /*
     * 检查输入字符串是否是带小数的数字格式,可以是负数
     */
    function isDecimal( str )
    { 
             var regu = /^[-]{0,1}[0-9]{1,}$/; 
             if(regu.test(str)){
              return true; 
             }
    
             var re = /^[-]{0,1}(\d+)[\.]+(\d+)$/; 
    
             if (re.test(str))
             { 
               if(RegExp.$1==0&&RegExp.$2==0)
               {
                        alert("输入的格式不正确");
                        return false; 
               }
        
               return true; 
             }
             else 
             { 
                     alert("输入的格式不正确");
                     return false; 
             }
    }

    /*
     * 检查E-Mail
     */
    function isEmail(str) { 
		var myReg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
		//或者是：
		var myreg = /^[-_A-Za-z0-9\.]+@([_A-Za-z0-9]+\.)+[A-Za-z0-9]{2,3}$/;
	     
		if(!myReg.test(str)) {
			//alert("E-Mail格式不正确");
            return false; 
        }
		return true;
    }
 
    
    /*
     * 检查输入字符串是否符合金额格式定义为带小数的正数,小数点后最多三位
     */
    function isMoney( str )
    { 
             var regu = "^[0-9]+[\.][0-9]{0,3}$"; 
             var re = new RegExp(regu); 
      
             if (!re.test(str)) 
             { 
                     alert("金额格式不正确 如:999.555"); 
                     return false;
             }
             return true; 
    }

    /*
     判断是否是日期
     date：要验证的日期
     fmt： 日期格式
    */
    function isDate( date, fmt )
    { 
             if (fmt==null) fmt="yyyyMMdd"; 
      
             var yIndex = fmt.indexOf("yyyy"); 
      
      
             if(yIndex==-1) 
             {
                     alert("请输入正确的格式");
                     return false; 
             }
      
             var year = date.substring(yIndex,yIndex+4); 
             var mIndex = fmt.indexOf("MM"); 
      
             if(mIndex==-1)
             {
               alert("请输入正确的格式");
               return false; 
             }
      
             var mon = date.substring(mIndex,mIndex+2); 
             var dIndex = fmt.indexOf("dd"); 
      
             if(dIndex==-1)
             {
               alert("请输入正确的格式");
               return false; 
             }
      
             var day = date.substring(dIndex,dIndex+2); 
      
             var regu = "^[0-9]+$"; 
             var re = new RegExp(regu); 
      
             if(!(year.search(re)!= -1)||year>"2100" || year< "1900")
             {
                     alert("请输入正确的年份");
                      return false; 
             }
             if(!(mon.search(re)!= -1)||mon>"12" || mon< "01")
             {
                     alert("请输入正确的月份");
                      return false; 
             }
             if(day>getMaxDay(year,mon) || day< "01")
             {
                     alert("请输入正确的天数");
                      return false; 
             }
      
             return true;
    }

    /*
     * 得到一年月的天数
     */
    function getMaxDay(year,month) { 
     
            if(month==4||month==6||month==9||month==11)
            { 
                     return "30"; 
            }
            if(month==2)
            { 
                     if(year%4==0&&year%100!=0 || year%400==0)
                     { 
                             return "29"; 
                     }
                     else
                     {
                             return "28"; 
                     }
            }
     
            return "31";
    }
    
    /*
     * 字符1是否以字符串2结束
     */
    function isLastMatch(str1,str2)
    { 
             var index = str1.lastIndexOf(str2); 
             if(!(str1.length==index+str2.length))
             {
                      alert("字符1不是否以字符串2结束 ");
                      return false; 
             }
      
             return true;
    }
    
    /*
     * 字符1是否以字符串2开始
     */
    function isFirstMatch(str1,str2)
    { 
             var index = str1.indexOf(str2); 
             if(index!=0)
             { 
                     alert("字符1不是以字符串2开始 ");
                     return false; 
             }
      
             return true;
    }

    /*
     * 字符1是包含字符串2
     */
    function isMatch(str1,str2)
    { 
             var index = str1.indexOf(str2); 
             if(index==-1)
             {
                      alert("字符1不包含字符串2");
                      return false; 
             }
             return true;
    }

   /*
    * 检查输入的电话号码格式是否正确
    */
function isTel(strTel) {
	//国家代码(2到3位)-区号(2到3位)-电话号码(7到8位)-分机号(3位)"
	var pattern =/^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;
	//var pattern =/(^[0-9]{3,4}\-[0-9]{7,8}$)|(^[0-9]{7,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^0{0,1}13[0-9]{9}$)/; 

	if(pattern.test(strTel)) {
		//alert('请输入正确的电话号码:电话号码格式为国家代码(2到3位)-区号(2到3位)-电话号码 (7到8位)-分机号(3位)"');
		return true;
	}
}

	/**
	*检查邮编
	*/
	function isPostalCode(object){
             var s = object.value; 
             var pattern =/^([0-9.]+)$/;
                 if(s != "")
                 {
                     if(!pattern.test(s))
                     {
                      //alert('请输入正确的邮政编码');
                      object.value="";
                      object.focus();
                     }
                 }
            }
 
 
 	/**
	 * 检查是否为数字和字符串的长度
	 */
	function strNumberLen(str,startNum,endNum){
	    if(!checkTextDataForNUMBER(str)){
	       return false;
	    }
		if (str != "" && endNum!='') {
			if (str < startNum || str > endNum) {
				return false;
			}
		}else{
		  if (str < startNum) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	*校验输入数字或数字以逗号分隔形式1或1,2,3,4,5
	*/
	function checkNumSpit(str,start,end){
	     var pattern = /^(\d+,)*\d+$/;   
    	 if(str!=''&& str!=null){
	           if(!pattern.test(str)){
	              return false;
	           }else{
	               var array = new Array(); 
	                   array = str.split(",");
	               for(var i=0;i<array.length;i++){
				        if(array[i] >  end  ||  array[i] <  start ){
				            return  false;
				        }
				   }
	              return true;
	           }
	      }
	      return true;
	}
	//某个区间的整形数字
function checkNum(str,start,end){
	     var pattern = /^(\d+,)*\d+$/;   
    	 if(str!=''&& str!=null){
	           if(!pattern.test(str)){
	              return false;
	           }else{
	               var array = new Array(); 
	                   array = str.split(",");
	                   if(array.length>1){
	                        return false;
	                   }else{
	                     if(array[0] >  end  ||  array[0] <  start ){
				            return  false;
				         }
	                   }
	              return true;
	           }
	      }
	      return true;
	}	