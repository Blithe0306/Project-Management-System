var regexEnum = 
{
	intege:"^-?[1-9]\\d*$",					//整数
	intege1:"^[1-9]\\d*$",					//正整数
	intege2:"^-[1-9]\\d*$",					//负整数
	intege3:"^[0-9]\\d*$",					//0-9正数
	num:"^([+-]?)\\d*\\.?\\d+$",			//数字
	num1:"^[1-9]\\d*|0$",					//正数（正整数 + 0）
	num2:"^-[1-9]\\d*|0$",					//负数（负整数 + 0）
	decmal:"^([+-]?)\\d*\\.\\d+$",			//浮点数
	decmal1:"^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*$",　　	//正浮点数
	decmal2:"^-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*)$",　 //负浮点数
	decmal3:"^-?([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0)$",　 //浮点数
	decmal4:"^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0$",　　 //非负浮点数（正浮点数 + 0）
	decmal5:"^(-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*))|0?.0+|0$",　　//非正浮点数（负浮点数 + 0）

	email:"^([\\w-.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\\w-]+.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$", //邮件
	color:"^[a-fA-F0-9]{6}$",				//颜色
	url:"^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$",	//url
	chinese:"^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$",					//仅中文
	ascii:"^[\\x00-\\xFF]+$",				//仅ACSII字符
	zipcode:"^\\d{6}$",						//邮编
	mobile:"^(13|15|18)[0-9]{9}$",				//手机,只限国内手机号
	ip4:"^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)$",	//ip地址
	notempty:"^\\S+$",						//非空
	picture:"(.*)\\.(jpg|bmp|gif|ico|pcx|jpeg|tif|png|raw|tga)$",	//图片
	rar:"(.*)\\.(rar|zip|7zip|tgz)$",								//压缩文件
	date:"^\\d{4}(\\-|\\/|\.)\\d{1,2}\\1\\d{1,2}$",					//日期
	qq:"^[1-9]*[1-9][0-9]*$",				//QQ号码
	tel:"^(([[0-9]\\+]\\d{1,3}-)?([0-9]\\d{1,3})-)?(\\d{7,})(-(\\d{3,}))?$",	//电话号码的函数(包括验证国内区号,国际区号,分机号)
	telphone:"[0-9\-]+$",					//电话号码，只可以输入数字，减号
	username:"^(([_]*)([A-Za-z0-9])([_]*))+$",						//用来用户注册。匹配由数字、26个英文字母或者下划线组成的字符串
	domainSnRegex:"^(([_.]*)([A-Za-z0-9])([_.]*))+$",			        //用来匹配域标识  由数字、26个英文字母、下划线 、小数点
	pnumber:"[A-Za-z0-9\-]+$",				//字母、数字、减号
	letter:"^[A-Za-z]+$",					//字母
	letter_u:"^[A-Z]+$",					//大写字母
	letter_l:"^[a-z]+$",					//小写字母
	idcard:"^[1-9]([0-9]{14}|[0-9]{17})$",	//身份证
	letter_u_num:"^[A-Za-z0-9]{4}$",		//字母或者数字共4位
	cellphone:"^[0-9\-\+\)\( ]+$"				// 手机号，匹配数字、减号、+号、空格、英文括号
}

var aCity={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"} 
 
 //中文、英文、数字空格下划线、横线、@符、小数点
function letter_u_num_china(str){
   var partn = /^((([\u4e00-\u9fa5]))|[A-Za-z0-9_\.@-]|[\s]){0,64}$/;
   if( partn.exec(str))
   {
       return true;
   }else{
       return false;
   }
}
//英文、数字空格下划线、横线、@符、小数点
function letter_u_num_english(str){
   var partn = /^(([_\.@-]*)|([A-Za-z0-9])|([_\.@-]*))+$/; 
   if( partn.exec(str)){
       return true;
   }else{
       return false;
   }
}
function isCardID(sId){ 
	var iSum=0 ;
	var info="" ;
	if(!/^\d{17}(\d|x)$/i.test(sId)) return "你输入的身份证长度或格式错误"; 
	sId=sId.replace(/x$/i,"a"); 
	if(aCity[parseInt(sId.substr(0,2))]==null) return "你的身份证地区非法"; 
	sBirthday=sId.substr(6,4)+"-"+Number(sId.substr(10,2))+"-"+Number(sId.substr(12,2)); 
	var d=new Date(sBirthday.replace(/-/g,"/")) ;
	if(sBirthday!=(d.getFullYear()+"-"+ (d.getMonth()+1) + "-" + d.getDate()))return "身份证上的出生日期非法"; 
	for(var i = 17;i>=0;i --) iSum += (Math.pow(2,i) % 11) * parseInt(sId.charAt(17 - i),11) ;
	if(iSum%11!=1) return "你输入的身份证号非法"; 
	return true;//aCity[parseInt(sId.substr(0,2))]+","+sBirthday+","+(sId.substr(16,1)%2?"男":"女") 
} 
 
//短时间，形如 (13:04:06)
function isTime(str)
{
	var a = str.match(/^(\d{1,2})(:)?(\d{1,2})\2(\d{1,2})$/);
	if (a == null) {return false}
	if (a[1]>24 || a[3]>60 || a[4]>60)
	{
		return false;
	}
	return true;
}

//短日期，形如 (2003-12-05)
function isDate(str)
{
	var r = str.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/); 
	if(r==null)return false; 
	var d= new Date(r[1], r[3]-1, r[4]); 
	return (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]);
}

//长时间，形如 (2003-12-05 13:04:06)
function isDateTime(str)
{
	var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/; 
	var r = str.match(reg); 
	if(r==null) return false; 
	var d= new Date(r[1], r[3]-1,r[4],r[5],r[6],r[7]); 
	return (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]&&d.getHours()==r[5]&&d.getMinutes()==r[6]&&d.getSeconds()==r[7]);
}

	//检查只能输入数字和英文字母
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
	
//校验IP
function checkIpAddr(ipStr) {
		if (ipStr == "") {
			return false;
		}
		if (ipStr == "localhost") {
			return true;
		}
		var pattern = /^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/;
		flag_ip = pattern.test(ipStr);
		if (flag_ip) {
			pattern = /^((0[0-9]|1[0-9]\d{1,2})|(2[0-5][0-5])|(2[0-4][0-9])|(\d{1,2}))\.((0[0-9]|1[0-9]\d{1,2})|(2[0-5][0-5])|(2[0-4][0-9])|(\d{1,2}))\.((0[0-9]|1[0-9]\d{1,2})|(2[0-4][0-9])|(2[0-5][0-5])|(\d{1,2}))\.((0[0-9]|1[0-9]\d{1,2})|(2[0-4][0-9])|(2[0-5][0-5])|(\d{1,2}))$/;
			flag_ip = pattern.test(ipStr);
			if(!flag_ip){
				return false;
			}
		}else{
			return false;
		}
		return true;
	}
	
	//配置模块的IP默认可以是*
function checkConfIpAddr(ipStr) {
		if (ipStr == "") {
			return false;
		}
		if(ipStr == "*"){
		    return true;
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
	
//校验端口	
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
	
//检查邮件格式
function checkEmail(str) { 
		var myReg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
		//或者是：
		var myreg = /^[-_A-Za-z0-9\.]+@([_A-Za-z0-9]+\.)+[A-Za-z0-9]{2,3}$/;
	     
		if(!myReg.test(str)) {
			//alert("E-Mail格式不正确");
            return false; 
        }
		return true;
    }
    
// 检查手机号格式  
function checkMobile( str){   
            var regu =/^[1][3][0-9]{9}$/; 
            var re = new RegExp(regu);

            if (!re.test(str)) { 
                    return false; 
            }else{ 
                    return true; 
            }
    }
    
//检查是否为数字和字符串的长度
function strNumberLen(str,startNum,endNum){
	    if(!isForNumber(str)){
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

//检查是否为字符串	
function checkStr(strValue){
	var regTextChar = /([\*\"\'<>\/])+/ ;
	return !regTextChar.test(strValue);
}

//检查是否为整数
function checkNumber(strValue){
	var regTextNumber = /^(\d)*$/;
	return regTextNumber.test(strValue);
}

//名称的输入格式（原任意字符）
function g_invalid_char_js (str){
   var partn = /([,\*\'"'\"'"\/<>\\\\]|-{2,})+/;
   if( partn.exec(str)){
       return true;
   }else{
       return false;
   }
}


