function XmlHttp(){
	try{
		this.xmlHttp = null;
		if(window.XMLHttpRequest) {
			this.xmlHttp = new XMLHttpRequest();
		}else if(window.ActiveXObject){
			this.xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		this.prepareRequest=prepareRequest;
		this.send=send;
		this.resText=resText;
	}catch(e){
		alert("Error:Construct XmlHttpObj["+e.description+"]");
	}
}

function ini(){

}

function XmlHttp(url){
	try{
		this.xmlHttp = null;
		if(window.XMLHttpRequest) {
			this.xmlHttp = new XMLHttpRequest();
		}else if(window.ActiveXObject){
			this.xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		this.prepareRequest=prepareRequest;
		this.send=send;
		this.resText=resText;
		this.prepareRequest(url);
	} catch (e){
		alert("Error:xmlHttp["+e.description+"]");
	}
}

function prepareRequest(url){
	try{
		this.xmlHttp.open("POST",url,false);
		this.xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
	}catch(e){
		alert("Error:prepareRequest["+e.description+"]");
	}
}

function send(parameter){
	try{
		this.xmlHttp.send(encodeURI(parameter));
	}catch(e){
		alert("Error:send["+e.description+"]");
	}
}

function resText(){
	try{
		return new String(this.xmlHttp.responseText);
	}catch(e){
		alert("Error:responseText["+e.description+"]");
	}
}