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
    <link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css" />
	<link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css" />
	<link href="<%=path%>/manager/common/uploadify/uploadify.css" rel="stylesheet" type="text/css" />

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
	
	<script language="javascript" src="<%=path%>/manager/orgunit/orgunit/js/commonOrgunits.js" charset="UTF-8"></script>  
	 
	<script language="javascript" type="text/javascript">
	<!--
		$(function() {
            $("#menu li").each(function(index) { 
                $(this).click(function() { 
                    $("#menu li.tabFocus").removeClass("tabFocus"); 
                    $(this).addClass("tabFocus"); 
                    $("#content li:eq(" + index + ")").show()
                    .siblings().hide();
                });
            });
        })
        
        //导出用户
		function exportUser(){
			var url='<%=path%>/manager/user/userinfo/userExport!exportUser.action';
			var dOrgunitIds = $("#orgunitIds").val();//用户所在机构
			var flag = true;
			var orgFlag;
			if(dOrgunitIds != ""){
				var orgunit = dOrgunitIds.substring(0,dOrgunitIds.length-1);
				for(var i=0; i<orgunit.split(",").length; i++){
					orgunitid = orgunit.split(",")[i].split(":")[1];
					if(orgunitid == 0){
						flag = false;
						break;
					}
				}
			}
			if(!flag){
				orgFlag = 1;
			}else{
				orgFlag = 2;
			}
			
			var userSel = $("input:radio[name='userSel'][checked]").val();//用户绑定状态
			var raType = $("input:radio[name='raType'][checked]").val();//导出类型
			var usrAttrStr = "";
			if(raType==2 || raType==4){
				FT.toAlert('warn','<view:LanguageTag key="user_vd_no_output"/>', null);
			  	return ;
			}

			var nessaryTag=0;			
			$('input[name="usrAttr[]"]:checked').each(function () {
                usrAttrStr += $(this).attr('value')+",";
                if($(this).attr('value')=='1' || $(this).attr('value')==1){
                	nessaryTag+=1;
                }
                if($(this).attr('value')=='9' || $(this).attr('value')==9){
                	nessaryTag+=9;
                }
	        });	
	        if(nessaryTag!=10){
		        if(nessaryTag != 1){
		        	FT.toAlert('warn','<view:LanguageTag key="user_must_sel_uname_org" />', null);
		        	return;
		        }
		        if(nessaryTag != 9){
		        	FT.toAlert('warn','<view:LanguageTag key="user_must_sel_orgunit_org" />', null);
		        	return;
		        }
	        }
	        
			jQuery("#ImportForm").ajaxSubmit({
			   async:false,  
			   dataType:"json",
			   type:"POST", 
			   url : "<%=path%>/manager/user/userinfo/userExport!exportUser.action?raType="+raType+"&usrAttr="+usrAttrStr + "&dOrgunitIds="+dOrgunitIds+"&userSel="+userSel+"&orgFlag="+orgFlag,
			   success:function(msg){
			   	  if(msg.object == '1'){
				     FT.toAlert('warn','<view:LanguageTag key="user_export_validate_error_1"/>', null); 
				  }else{
					 window.location.href = "<%=path%>/manager/user/userinfo/userExport!downLoad.action?fileName="+encodeURI(msg.object);
				  } 
				}
			});	
		}
		
		//全选、取消全选
		function allCheckOper2(){
			if($("#allCheck").attr("checked") == true){
				$('input[name="usrAttr[]"]').each(function() {
	            	$(this).attr("checked", true);
	        	});
        	}else{            
		        $("input[name='usrAttr[]']").each(function() {
		        	if(this.value != 1 && this.value != 9){
		            	$(this).attr("checked", false);
		        	}
		        });
	        }
		}
		
		//指定用户组
		function selGroup(obj){
			//显示隐藏切换
			if(obj == 0){
				$("#groupDiv").slideUp();
			}else{
				$("#groupDiv").slideDown();
			}
		}
		
		//用户组选择
		function selUserGroup(){
			toOpenParam('<view:LanguageTag key="user_sel_group"/>',"<%=path%>/manager/user/group/userGroup!init.action?source=sel_group" );
		}
		
		//Select列表删除
		function delSelect(selName){
			if(!operSelObj(selName)){
			   FT.toAlert('warn','<view:LanguageTag key="common_syntax_check_need_del_date"/>', null);
 
				return false;
			}
			selectRemove(selName);
		}
		
		// 提示组织机构为空，关闭窗口
		function closeOrgWin(object) {
			$.ligerDialog.success(object,'<view:LanguageTag key="common_syntax_tip"/>',function(){
				winOrgClose.close();
			});
		}
		
	//-->
	</script>
  </head>
  
  <body>
 
  <form name="ImportForm" id="ImportForm" method="post" action="" enctype ="multipart/form-data">    
    
     <table width="100%" border="0" cellspacing="0" cellpadding="0" class="topTableBg">
      <tr>
        <td>  
		  <span class="topTableBgText">
			  <view:LanguageTag key='user_export_title_user'/>
          </span>
        </td>
        <td width="2%" align="right">
        	<a href="javascript:addAdmPermCode('0203','<%=path%>');" class="isLink_HeiSe"><img src="<%=path%>/images/icon/rightarrow.png" width="16" height="16" hspace="6" title='<view:LanguageTag key="common_admin_keep_used"/>'/></a>       	 </td>
      </tr>
    </table> 
    
	<table width="100%" border="0"  cellpadding="0" cellspacing="0" class="ulOnTable">
      <tr>
        <td valign="top">
		
		 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ulOnInsideTable">
             <tr>
               <td width="25%" align="right">&nbsp;</td>
               <td colspan="2"><view:LanguageTag key="user_export_date_file_tip"/></td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="user_sel_org"/><view:LanguageTag key="colon"/></td>
               <td>			   
			   		<input type=hidden id="orgunitIds" name="userInfo.dOrgunitId"  value=""/>
              		<input type=text id="orgunitNames" name="adminUser.orgunitName" onClick="selOrgunits(7,'<%=path%>');" class="formCss100" style="width:160px" readonly />			   </td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td align="right"><view:LanguageTag key="user_export_user_sel"/><view:LanguageTag key="colon"/></td>
               <td><input type="radio" id="userSel0" name="userSel" value="0" checked />&nbsp;<view:LanguageTag key="user_export_all_user"/>
                   <input type="radio" id="userSel1" name="userSel" value="1" />&nbsp;<view:LanguageTag key="tkn_state_unbound"/>
                   <input type="radio" id="userSel2" name="userSel" value="2" />&nbsp;<view:LanguageTag key="tkn_state_bound"/>               </td>
               <td>&nbsp;</td>
             </tr>
             <tr>
                <td align="right"></td>
               	<td>
             		<table width="100%" height="100%">
             			<tr>
	               			<td width="33%"><input type="checkbox" id="allCheck" name="allCheck" onClick="allCheckOper2();" value="checkbox" />&nbsp;<view:LanguageTag key="common_syntax_sel_or_unsel_all"/></td>
	               			<td width="33%"></td>
	               			<td width="33%"></td>
               			</tr>
             		</table>               	</td>
             </tr>
             <tr>
               <td align="right" valign="top"><view:LanguageTag key="user_export_user_field"/><view:LanguageTag key="colon"/></td>
               <td>
               	<table width="100%" height="100%">
               		<tr>
               			<td width="25%"><input type="checkbox" id="usrAttr1" name="usrAttr[]" value="1" checked />&nbsp;<view:LanguageTag key="user_info_account"/></td>
               			<td width="25%"><input type="checkbox" id="usrAttr3" name="usrAttr[]" value="3" />&nbsp;<view:LanguageTag key="user_info_real_name"/></td>
               			<td width="25%"></td>
               			<td width="25%"></td>
               		</tr>
               		<tr>
               			<td><input type="checkbox" id="usrAttr4" name="usrAttr[]" value="4" />&nbsp;<view:LanguageTag key="user_static_pwd"/></td>
               			<td><input type="checkbox" id="usrAttr5" name="usrAttr[]" value="5" />&nbsp;<view:LanguageTag key="user_test_phone"/></td>
               			<td></td>
               			<td></td>
               		</tr>
               		<tr>
               			<td><input type="checkbox" id="usrAttr6" name="usrAttr[]" value="6" />&nbsp;<view:LanguageTag key="common_info_email"/></td>
               			<td><input type="checkbox" id="usrAttr9" name="usrAttr[]" value="9" checked/>&nbsp;<view:LanguageTag key="domain_orgunit"/></td>
               			<td></td>
               			<td></td>
               		</tr>
               		<tr>
               			<td><input type="checkbox" id="usrAttr8" name="usrAttr[]" value="8" />&nbsp;<view:LanguageTag key="domain_orgunit_num"/></td>
               			<td></td>
               			<td></td>
               			<td></td>
               		</tr>
               		<tr>
               		</tr>
               	</table>               </td>
             </tr>
             <tr>
                <td align="right" valign="top"><view:LanguageTag key="user_export_tkn_field"/><view:LanguageTag key="colon"/></td>
               	<td>
             		<table width="100%" height="100%">
             			<tr>
	               			<td width="25%"><input type="checkbox" id="usrAttr20" name="usrAttr[]" value="20" />&nbsp;<view:LanguageTag key="tkn_comm_tknum"/></td>
	               			<td width="25%"><input type="checkbox" id="usrAttr25" name="usrAttr[]" value="25" />&nbsp;<view:LanguageTag key="user_export_expiration_time"/></td>
	               			<td width="25%"></td>
	               			<td width="25%"></td>
               			</tr>
             		</table>               	</td>
             </tr>
	         <!--  <tr>
		        <td width="25%" align="right"><view:LanguageTag key="user_export_sel_file_format"/><view:LanguageTag key="colon"/></td>
		        <td width="50%"><input type="radio" id="raType1" name="raType" value="1" checked /><view:LanguageTag key="common_excel_file"/>
	              <input type="radio" id="raType2" name="raType" value="2" /><view:LanguageTag key="common_pdf_file"/>
	              <input type="radio" id="raType3" name="raType" value="3"><view:LanguageTag key="common_html_file"/>
	              <input type="radio" id="raType4" name="raType" value="4" /><view:LanguageTag key="common_csv_file"/>
	            </td>
		        <td width="25%">&nbsp;</td>
		      </tr> -->
		      <tr>
		        <td align="right">&nbsp;</td>
		        <td width="50%">&nbsp;</td>
		        <td width="25%">&nbsp;</td>
		      </tr>
		      <tr>
		        <td align="right">&nbsp;</td>
                <td width="50%"><a href="javascript:exportUser();" class="button" id="exportbutton"><span><view:LanguageTag key='user_export_title_user'/></span></a></td>
	            <td width="25%">&nbsp;</td>
	       </tr>
           </table>
		 
		</td>
      </tr>
    </table>
  </form>
 
  </body>
</html>