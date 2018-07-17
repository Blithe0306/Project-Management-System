<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ft.otp.common.StrConstant"%>
<input type=hidden id="common_char1" value="<%=StrConstant.common_char1 %>"/>
<input type=hidden id="common_char2" value="<%=StrConstant.common_char2 %>"/>
<script type="text/javascript">
/*
  符号替换
*/
function replaceUserId(userId){
	var common_char1=$("#common_char1").val();
	var common_char2=$("#common_char2").val();
	var newUserId=userId.replace(common_char1,common_char2);
	return newUserId;
}
</script>
