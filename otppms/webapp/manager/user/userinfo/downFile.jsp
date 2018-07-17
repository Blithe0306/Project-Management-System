<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
	<link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
	
    <link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Gray/css/grid.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/ligerui-icons.css"/>
    
    <script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/uploadify/swfobject.js"></script>
	<script language="javascript" src="<%=path%>/manager/common/uploadify/uploadify.v2.1.3.min.js"></script>
	
 	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
 	    <script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerGrid.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/plugins/ligerDialog.js"></script>
	<script type="text/javascript" src="<%=path%>/manager/common/js/window/window.js"></script>	
	
    <script language="javascript" type="text/javascript">
		

		function downWindow() {
			     location.href =  '<%=path%>/manager/user/userinfo/downFile.jsp';      
        }
        //用户模板文件下载
		function downLoad(){
			var url='<%=path%>/manager/user/userinfo/userImport!downLoadIni.action';
			var raType = $("input:radio[name='raType'][checked]").val();
			var usrAttrStr = "";
			if(raType==2){
			  FT.toAlert('warn','<view:LanguageTag key="user_no_csv_temp" />', null);
			  return ;
			}
			
			var nessaryTag=0;			
			$('input[name="usrAttr[]"]:checked').each(function () {
	            //if ($(this).attr('checked') == true) {
	                usrAttrStr += $(this).attr('value')+",";
	                if($(this).attr('value')=='1' || $(this).attr('value')==1){
	                	nessaryTag+=1;
	                }
	            //}
	        });	
	        if(nessaryTag!=1){
	        	FT.toAlert('warn','<view:LanguageTag key="user_must_sel_uname_org" />', null);
	        	return;
	        }      
	               
			jQuery("#ImportForm").ajaxSubmit({
			   async:false,  
			   dataType:"json",
			   type:"POST", 
			   url : "<%=path%>/manager/user/userinfo/userImport!downLoadIni.action?raType=" + raType + "&usrAttr=" + usrAttrStr,
			   success:function(msg){
					if(msg.errorStr == 'false'){
				      	FT.toAlert(msg.errorStr,msg.object,null);	
					}else{
						window.location.href = "<%=path%>/manager/user/userinfo/userImport!downLoad.action?fileName="+encodeURI(msg.object);
					} 
				}
			});
						        
		}
		
		//全选、取消全选
		function allCheckOper(){
			if($("#allCheck").attr("checked")==true){
				$('input[name="usrAttr[]"]').each(function() {
	            	$(this).attr("checked", true);
	        	});
        	}else{ 
		        $("input[name='usrAttr[]']").each(function() {
		        	if(this.value != 1){
		            	$(this).attr("checked", false);
		        	}
		        });
	        }
		}
		//确定按钮触发
		function okClick(item,win,index){
			downLoad();
		} 
	</script>
  </head>
  
  <body>
 
  <form name="ImportForm" id="ImportForm" method="post" action="" enctype ="multipart/form-data">
	<table width="100%" height="100" border="0"  cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
		
	   
			  <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
	             <tr>
	               <td width="25%" align="right">&nbsp;</td>
	               <td><view:LanguageTag key="user_sel_userattr_create_temp_file"/></td>
	               <td>&nbsp;</td>
	             </tr>
           		<tr>
	               <td align="right"></td>
	               <td width="50%">
	               <input type="checkbox" id="allCheck" name="allCheck" onClick="allCheckOper();" value="checkbox" />&nbsp;<view:LanguageTag key="common_syntax_sel_or_unsel_all"/>
	               <td width="25%">&nbsp;</td>
	             </tr>
	             <tr>
	               <td align="right" valign="top"><view:LanguageTag key="user_export_user_field"/><view:LanguageTag key="colon"/></td>
	               <td>
	               	<table width="100%" height="100%">
	               		<tr>
	               			<td><input type="checkbox" id="usrAttr1" name="usrAttr[]" value="1" checked />&nbsp;<view:LanguageTag key="user_username"/></td>
	               		
	               			<td><input type="checkbox" id="usrAttr3" name="usrAttr[]" value="3" />&nbsp;<view:LanguageTag key="user_info_real_name"/></td>
	               		</tr>
	               		<tr>
	               		<!--<td><input type="checkbox" id="usrAttr2" name="usrAttr[]" value="2" />&nbsp;<view:LanguageTag key="user_info_document_num"/></td> -->
	               		<!--<td><input type="checkbox" id="usrAttr7" name="usrAttr[]" value="7" />&nbsp;<view:LanguageTag key="user_info_document_type"/></td> -->
	               		</tr>
	               		<tr>
	               			<td><input type="checkbox" id="usrAttr4" name="usrAttr[]" value="4" />&nbsp;<view:LanguageTag key="user_static_pwd"/></td>
	               			<td><input type="checkbox" id="usrAttr5" name="usrAttr[]" value="5" />&nbsp;<view:LanguageTag key="common_info_mobile"/></td>
	               		</tr>
	               		<tr>
	               			<td><input type="checkbox" id="usrAttr6" name="usrAttr[]" value="6" />&nbsp;<view:LanguageTag key="common_info_email"/></td>
	               			<td><input type="checkbox" id="usrAttr9" name="usrAttr[]" value="9" />&nbsp;<view:LanguageTag key="domain_orgunit"/></td>
	               		</tr>
	               		<tr>
	               		<!--<td><input type="checkbox" id="usrAttr45" name="usrAttr[]" value="45" />&nbsp;<view:LanguageTag key="common_info_address"/></td>-->
	               		<!--<td><input type="checkbox" id="usrAttr44" name="usrAttr[]" value="44" />&nbsp;<view:LanguageTag key="common_info_tel"/></td>-->
	               		</tr>
	               		<tr>
	               			<td><input type="checkbox" id="usrAttr8" name="usrAttr[]" value="8" />&nbsp;<view:LanguageTag key="domain_orgunit_num"/></td>
	               			<td></td>
	               		</tr>
	               	</table>
	               </td>
	             </tr>
	             <tr>
	               <td align="right"><view:LanguageTag key="user_export_tkn_field"/><view:LanguageTag key="colon"/></td>
	               <td width="50%">
	               <input type="checkbox" id="usrAttr20" name="usrAttr[]" value="20" />&nbsp;<view:LanguageTag key="tkn_comm_tknum"/>
	               <td width="25%">&nbsp;</td>
	             </tr>
	             <!-- <tr>
	               <td align="right"><view:LanguageTag key="user_import_sel_temp_file_format"/><view:LanguageTag key="colon"/></td>
	               <td valign="top">
	                <input type="radio" id="raType1" name="raType" value="1" checked /><view:LanguageTag key="common_excel_file"/>
	                <input type="radio" id="raType2" name="raType" value="2" /><view:LanguageTag key="common_csv_file"/>
	               </td>
	               <td valign="top">&nbsp;</td>
	             </tr> -->
	             <tr style="display: none;">
	               <td align="right">&nbsp;</td>
	               <td><a href="javascript:downLoad();" class="button" id="importbutton"><span><view:LanguageTag key="common_download_temp_file"/></span></a></td>
	               <td>&nbsp;</td>
	             </tr>
	           </table>
		 
		</td>
      </tr>
    </table>
  </form>
 
  </body>
</html>