<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.ft.otp.util.conf.ConfDataFormat"%>
<%@page import="com.ft.otp.common.language.Language"%>
<%
	String path = request.getContextPath();
	String titleImg = Language.getCurrLang(request.getSession());
	titleImg += ".png";
	
	int dbConn = 0;
	int licValid = 0;
	int isAdmin = 0;
	int isLocal = 0;
	int isConfEmailServ = 0;
	if(null != application.getAttribute("isCanConn")){
		dbConn = (Boolean) application.getAttribute("isCanConn") ? 1 : 0;
	}
	if(null != application.getAttribute("licIsNull")){
	    licValid = (Boolean)application.getAttribute("licIsNull") ? 1 : 0;
	}
	if(null != application.getAttribute("isSuperMan")){
	    isAdmin = (Boolean)application.getAttribute("isSuperMan") ? 1 : 0;
	}
	if(null != application.getAttribute("isLocalIp")){
	    isLocal = (Boolean)application.getAttribute("isLocalIp") ? 1 : 0;
	}
	if(null != application.getAttribute("isConfEmailServer")){
	    isConfEmailServ = (Boolean)application.getAttribute("isConfEmailServer") ? 1 : 0;
	}
	int srcType = ConfDataFormat.getDbDataSrcType() == true ? 1 : 0;
	int existLang = ConfDataFormat.getLangConfVal() == true ? 1 : 0;
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>
<view:LanguageTag key="system_noun_name"/>
</title>
<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
<link href="<%=path%>/install/css/install.css" rel="stylesheet" type="text/css"/>
<script src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js" type="text/javascript"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		hideAll();
		loadPage();
	});
	
	function loadPage(){
		if('<%=dbConn%>'=='0'){
			showId('01', 'T01');
		}else if('<%=dbConn%>'=='1' && '<%=existLang%>'=='0'){
			showId('01', 'T01');
		}else if('<%=dbConn%>'=='1' && '<%=srcType%>'=='1'){
			showId('04', 'T04');
		}else if('<%=dbConn%>'=='1' && '<%=licValid%>'=='1' && '<%=isConfEmailServ%>'=='1' && '<%=isAdmin%>'=='1' && '<%=isLocal%>'=='0'){
			toPage("frameView08", "/install/install!finish.action");
			showId('08', 'T08');
		}else if('<%=dbConn%>'=='1' && '<%=licValid%>'=='1' && '<%=isConfEmailServ%>'=='1' && '<%=isAdmin%>'=='1' && '<%=isLocal%>'=='1'){
			showId('07', 'T07');
		}else if('<%=dbConn%>'=='1' && '<%=licValid%>'=='1' && '<%=isConfEmailServ%>'=='1' && '<%=isAdmin%>'=='0'){
			showId('06', 'T06');
		}else if('<%=dbConn%>'=='1' && '<%=licValid%>'=='1' && '<%=isConfEmailServ%>'=='0'){
			toPage("frameView09", "/manager/confinfo/email/email!initFind.action");
			showId('09', 'T09');
		}else if('<%=dbConn%>'=='1' && '<%=licValid%>'=='0'){
			showId('05', 'T05');
		}
	}
	
	//给标签语言添加箭头背景
	function addCss(id){
	    $('#' +id+ '').removeAttr("style");
		$('#' +id+ '').removeClass('leftTitle');
		$('#' +id+ '').addClass('leftTitleBg');
	}
	
	//删除标签语言箭头背景
	function delCss(id){
		$('#' +id+ '').removeClass('leftTitleBg');
		$('#' +id+ '').addClass('leftTitle');
	}
	
	//初始化隐藏
	function hideAll(){		
		hideId('01', 'T01');
		hideId('02', 'T02');
		hideId('03', 'T03');
		hideId('04', 'T04');
		hideId('05', 'T05');
		hideId('06', 'T06');
		hideId('07', 'T07');
		hideId('08', 'T08');
		hideId('09', 'T09');
	}
	
	//显示frame和语言标签样式
	function showId(frameId, titleId){
		//如果是需要往真实IP设置页面跳转（显示），判断系统的认证服务器ip是否为127.0.0.1
		//如果不是则不进行真实IP地址设置（此情况适用于服务器重新安装后，使用备份的数据文件进行数据库恢复时）
		if(frameId =='07' && titleId=='T07'){
			var urlStr = "<%=path%>/manager/authmgr/server/authServer!isLocalIp.action";
			$.post(urlStr,{
		    	},function(result){
		    		if(result =="success"){//不需要设置认证服务器ip
		    			//设置左侧标签不显示设置真实ip地址
		    			document.getElementById("T07").style.display="none";
		    			frameId ="08";
		    			titleId ="T08" ;
		    			toPage("frameView08", "/install/install!finish.action");
		    			$('#' +frameId+ '').show();
						$('#' +titleId+ '').show();
						addCss(titleId);
		    		}else{//需要设置认证服务器IP
			    		$('#' +frameId+ '').show();
						$('#' +titleId+ '').show();
						addCss(titleId);
					}
		       	},'text'
	     	);
		}else{
			$('#' +frameId+ '').show();
			$('#' +titleId+ '').show();
			addCss(titleId);
		}
	}
	
	//隐藏frame和语言标签样式
	function hideId(frameId, titleId){
		$('#' +frameId+ '').hide();
		delCss(titleId);
	}
	
	//跳转的页面
	function toPage(frameId, url){
		parent.document.getElementById(''+frameId+'').src = "<%=path%>" + url;
	}
    </script>
</head>
<body>
<table width="808" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td>
      <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
        <tr>
          <td width="1%" background="<%=path%>/install/images/init_r2_c11.png"><img src="<%=path%>/install/images/init_r2_c2.png" width="10" height="50"></td>
          <td width="97%" align="left" valign="top" background="<%=path%>/install/images/init_r2_c11.png">
            <table width="100%" height="50" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="21%"><img src="<%=path%>/images/layout/<%=titleImg%>" width="260" height="30"></td>
                <td width="79%">&nbsp;</td>
              </tr>
            </table>
          </td>
          <td width="2%" align="right" background="<%=path%>/install/images/init_r2_c11.png"><img src="<%=path%>/install/images/init_r2_c15.png" width="15" height="50"></td>
        </tr>
        <tr>
          <td><img src="<%=path%>/install/images/init_r3_c2.png" width="10" height="30"></td>
          <td align="center" background="<%=path%>/install/images/init_r3_c5.png"><span class="textCss"><view:LanguageTag key="sys_init_otp_server"/></span></td>
          <td align="right" background="<%=path%>/install/images/init_r3_c5.png"><img src="<%=path%>/install/images/init_r3_c15.png" width="15" height="30"></td>
        </tr>
        <tr>
          <td colspan="3" valign="top" background="<%=path%>/install/images/init_r5_c2.png">
            <table width="770" border="0" align="center" cellpadding="0" cellspacing="0" class="leftBg">
              <tr>
                <td width="200" valign="top" class="leftTdBg">
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td>
                      <span id="T01" class="leftTitle" style="display:none"><view:LanguageTag key="sys_init_lang_sel"/></span>
                      <span id="T02" class="leftTitle" style="display:none"><view:LanguageTag key="sys_init_mode"/></span>
                      <span id="T03" class="leftTitle" style="display:none"><view:LanguageTag key="sys_init_db_conn"/></span>
                      <span id="T04" class="leftTitle" style="display:none"><view:LanguageTag key="sys_init_db_recovery"/></span>
                      <span id="T05" class="leftTitle" style="display:none"><view:LanguageTag key="sys_init_up_sys_lic"/></span>
                      <!-- 配置邮件服务器、根据系统配置确定是否需要、步骤在上传授权后创建管理员之前配置 -->
                      <span id="T09" class="leftTitle" style="display:none"><view:LanguageTag key="sys_init_email_server"/></span>
                      <span id="T06" class="leftTitle" style="display:none"><view:LanguageTag key="sys_init_create_admin"/></span>
                      <span id="T07" class="leftTitle" style="display:none"><view:LanguageTag key="sys_init_set_real_ip"/></span>
                      <span id="T08" class="leftTitle" style="display:none"><view:LanguageTag key="sys_init_complete"/></span>
                      </td>
                    </tr>
                  </table>
                </td>
                <td width="570" valign="top">
                  <div id="01" title='<view:LanguageTag key="sys_init_multi_lang_set"/>'>
                    <iframe frameborder="0" id="frameView01" name="frameView01" src="<%=path%>/install/languagepage.jsp" class="iframecss"></iframe>
                  </div>
                  <div id="02" title='<view:LanguageTag key="sys_init_db_init_mode"/>' style="display:none" >
                    <iframe frameborder="0" id="frameView02" name="frameView02" src="<%=path%>/install/init_select.jsp" class="iframecss"></iframe>
                  </div>
                  <div id="03" title='<view:LanguageTag key="sys_init_create_db_conn"/>' style="display:none" >
                    <iframe frameborder="0" id="frameView03" name="frameView03" src="" class="iframecss"></iframe>
                  </div>
                  <div id="04" title='<view:LanguageTag key="sys_init_db_backup_recovery"/>' style="display:none" >
                    <iframe frameborder="0" id="frameView04" name="frameView04" src="<%=path%>/install/dbrecover.jsp" class="iframecss"></iframe>
                  </div>
                  <div id="05" title='<view:LanguageTag key="sys_init_up_serv_lic"/>' style="display:none" >
                    <iframe frameborder="0" id="frameView05" name="frameView05" src="<%=path%>/install/uploadLic.jsp" class="iframecss"></iframe>
                  </div>
                  <div id="09" title='<view:LanguageTag key="sys_init_email_server"/>' style="display:none" >
                    <iframe frameborder="0" id="frameView09" name="frameView09" src="" class="iframecss"></iframe>
                  </div>
                  <div id="06" title='<view:LanguageTag key="sys_init_create_sys_admin"/>' style="display:none" >
                    <iframe frameborder="0" id="frameView06" name="frameView06" src="<%=path%>/install/adminpage.jsp" class="iframecss"></iframe>
                  </div>
                  <div id="07" title='<view:LanguageTag key="sys_init_serv_ip_sel"/>' style="display:none" >
                    <iframe frameborder="0" id="frameView07" name="frameView07" src="<%=path%>/install/local_ip.jsp" class="iframecss"></iframe>
                  </div>
                  <div id="08" title='<view:LanguageTag key="sys_init_complete"/>' style="display:none" >
                    <iframe frameborder="0" id="frameView08" name="frameView08" src="" class="iframecss"></iframe>
                  </div>
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td><img src="<%=path%>/install/images/init_r10_c2.png" width="10" height="38"></td>
          <td align="center" valign="top" background="<%=path%>/install/images/init_r10_c4.png" class="bottomText">
            <view:LanguageTag key='system_noun_copyright'/>
          </td>
          <td align="right"><img src="<%=path%>/install/images/init_r10_c15.png" width="15" height="38"></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
</html>
