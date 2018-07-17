<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="/WEB-INF/tld/view.tld" %>
<%
	String path = request.getContextPath();
%>
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf-8" />
<title></title>
<link href="<%=path%>/manager/common/css/webapp.css" rel="stylesheet" type="text/css"/>
<link href="<%=path%>/manager/common/css/title.css" rel="stylesheet" type="text/css"/>
<link href="<%=path%>/manager/common/css/validator.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/manager/common/js/ligerUI/skins/Aqua/css/ligerui-all.css"/>
<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery-1.4.2.min.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/core/base.js"></script>
<script type="text/javascript" src="<%=path%>/manager/common/js/ligerUI/js/ligerui.min.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/common_one.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidator-4.0.1.js"></script>
<script language="javascript" src="<%=path%>/manager/common/js/formValidate/formValidatorRegex.js" ></script>
<script language="javascript" src="<%=path%>/manager/common/js/window/window.js"></script>
<script language="JavaScript" type="text/javascript">
		$(document).ready(function(){
			window.resizeBy(0,0);
			var selhourAttr = parent.$('#taskhour').val();
			if(selhourAttr!=''&&selhourAttr!=null){
			   var selhourAttrs = selhourAttr.split(",");
			   for(var i=0;i<selhourAttrs.length;i++){
			       $('#hourAttr'+ selhourAttrs[i]).attr("checked",true);
			   }
			}
		});

		function okClick(btn,win,index){
		    var checkedAttr = "";
		    $('input[name="hourAttr"]').each(function() {
	            	if($(this).attr("checked")){
	            	   checkedAttr = checkedAttr + $(this).val() + ",";
	            	}
	        });
	        if(checkedAttr==''||checkedAttr==null){
	          FT.toAlert('warn','<view:LanguageTag key="usource_sel_at_least_one_point"/>',null);
	          return;
	        }
	        if(checkedAttr!=""){
	          checkedAttr = checkedAttr.substring(0,checkedAttr.length-1); 
	        }
	         
	        parent.$('#taskhour').val(checkedAttr);
	        win.close();
		}
		
	</script>
</head>
<body style="overflow:hidden;">
<form id="AddForm" name="AddForm" method="post" action="">
 
  <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="ulOnTable">
    <tr>
      <td valign="top">
        <ul id="menu">
          <li class="tabFocus"> <strong>
            <view:LanguageTag key="usource_sel_timing_point"/>
            </strong> </li>
        </ul>
        <ul id="content">
          <li class="conFocus">
            <table width="100%" align="center" cellspacing="0" cellpadding="5px" class="ulOnInsideTable">
              <tr>
                <td align="right" width="20%">
                  <view:LanguageTag key="usource_timing_exec_point"/><view:LanguageTag key="colon"/>
                </td>
                <td width="80%">
                 <input type="checkbox" id="hourAttr0" name="hourAttr"  value="0"/>0<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp;&nbsp;&nbsp;
                 <input type="checkbox" id="hourAttr1" name="hourAttr" value="1"/>1<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp;&nbsp;&nbsp;
                 <input type="checkbox" id="hourAttr2" name="hourAttr" value="2"/>2<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                 <input type="checkbox" id="hourAttr3" name="hourAttr" value="3"/>3<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp;&nbsp;&nbsp; 
                 <input type="checkbox" id="hourAttr4" name="hourAttr" value="4"/>4<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp; &nbsp; 
                 <input type="checkbox" id="hourAttr5" name="hourAttr" value="5"/>5<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp;&nbsp; 
                </td>
              </tr>
              <tr><td colspan="2" height="15"></td></tr>
              <tr>
                <td align="right"></td>
                <td>
                 <input type="checkbox" id="hourAttr6" name="hourAttr" value="6"/>6<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp;&nbsp;&nbsp;
                 <input type="checkbox" id="hourAttr7" name="hourAttr" value="7"/>7<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp;&nbsp;&nbsp;
                 <input type="checkbox" id="hourAttr8" name="hourAttr" value="8"/>8<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                 <input type="checkbox" id="hourAttr9" name="hourAttr" value="9"/>9<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp;&nbsp;&nbsp;
                 <input type="checkbox" id="hourAttr10" name="hourAttr" value="10"/>10<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp;
                 <input type="checkbox" id="hourAttr11" name="hourAttr" value="11"/>11<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp; 
                </td>
                
              </tr>
              <tr><td colspan="2" height="15"></td></tr>
              <tr>
                <td align="right">
                    
                </td>
                <td>
                 <input type="checkbox" id="hourAttr12" name="hourAttr" value="12"/>12<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp;
                 <input type="checkbox" id="hourAttr13" name="hourAttr" value="13"/>13<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp;
                 <input type="checkbox" id="hourAttr14" name="hourAttr"  value="14"/>14<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp;
                 <input type="checkbox" id="hourAttr15" name="hourAttr"  value="15"/>15<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp;
                 <input type="checkbox" id="hourAttr16" name="hourAttr" value="16"/>16<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp;
                 <input type="checkbox" id="hourAttr17" name="hourAttr" value="17"/>17<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp; 
                </td>
              
              </tr>
              <tr><td colspan="2" height="15"></td></tr>
              <tr>
                <td align="right">
                  
                </td>
                <td>
                 <input type="checkbox" id="hourAttr18" name="hourAttr" value="18"/>18<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp;
                 <input type="checkbox" id="hourAttr19" name="hourAttr" value="19"/>19<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp;
                 <input type="checkbox" id="hourAttr20" name="hourAttr" value="20"/>20<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp;
                 <input type="checkbox" id="hourAttr21" name="hourAttr" value="21"/>21<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp;
                 <input type="checkbox" id="hourAttr22" name="hourAttr" value="22"/>22<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp;
                 <input type="checkbox" id="hourAttr23" name="hourAttr" value="23"/>23<view:LanguageTag key="common_syntax_hour"/>&nbsp;&nbsp; 
                </td>
              
              </tr>
              <tr>
                <td><a href="#" name="modifyBtn" id="modifyBtn"></a></td>
              </tr>
            </table>
          </li>
        </ul>
      </td>
    </tr>
  </table>
</form>
</body>
</html>
