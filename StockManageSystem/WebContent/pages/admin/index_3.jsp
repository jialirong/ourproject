<%-- <%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
 	System.out.println(basePath);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <script src="../../js/jquery-1.12.4.min.js"></script>
    <script src="js/jquery-ui-1.12.1.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>库存管理系统</title>
</head>

<script type="text/javascript">
	function resetValue() {
		document.getElementById("userName").value = "";
		document.getElementById("password").value = "";
	}
	
	function loadimage(){
		document.getElementById("randImage").src = "${pageContext.request.contextPath}/pages/admin/image.jsp?"+Math.random();
	} 
</script>
<style>
body {
	font: Verdana, Geneva, sans-serif;
	font-size: 12px;
}
</style>
<body>

<%if(request.getAttribute("error1")!=null){ %>
<script type="text/javascript">
alert(request.getAttribute("error1"));

</script>
<%} %>

	<div
		style="background:url('${pageContext.request.contextPath}/jpg/background.png') no-repeat 0/cover;position: relative;">
		<div style="width: 4%; position: absolute; left: 30%; top: 7%;"></div>
		<div
			style="left: 38%; top: 14%; position: absolute; font-size: 28px; color: black">仓库管理系统</div>
		<form action="${pageContext.request.contextPath}/stockManageSystem/login!login" method="post">
			<table border="0px" width="1321" height="585">
				<tr height="180">
					<td height="50" colspan="4"></td>
				</tr>
				<tr height="10">
					<td width="40%" height="23"></td>
					<td width="5%">&nbsp;用户名：</td>
					<td width="10%"><input type="text" value="${user.userName}"
						name="user.userName" id="userName" size="17" height="10" /><span><font color="red">${error }</span></td>
					
				</tr>
				<tr height="10">
					<td width="40%" height="23"></td>
					<td width="5%">&nbsp;密 码：</td>
					<td><input type="password" value="${user.password }"
						name="user.password" id="password" size="17" height="10" /></td>
				</tr>
				
				 <tr height="10">
					<td width="40%" height="26"></td>
					<td width="5%">&nbsp;验证码：</td>
					<td><input type="text" value="${imageCode }" name="imageCode"
						id="imageCode" size="5" />&nbsp;<img
						onclick="javascript:loadimage();" title="换一张试试" name="randImage"
						id="randImage" src="image.jsp" width="60" height="20" border="1"
						align="absmiddle"></td>
					<td width="55%"></td>
				</tr> 

				<tr height="10">
					<td width="40%" height="26"></td>
					<td width="10%">&nbsp;&nbsp;<input type="submit" value="登录" />
						&nbsp;<input type="button" value="重置" onclick="resetValue()" />&nbsp;&nbsp;<a href="#"><input type="button" value="忘记密码" onclick="resetValue()" /></a></td>
					<td width="55%"></td>
				</tr>

				<tr>
					<td width="40%" height="18"></td>
					<td colspan="3"></td>
				</tr>
				<tr>
					<td height="150" colspan="4"></td>
				</tr>
			</table>
		</form>
									
		
	</div>
</body>
</html> --%>