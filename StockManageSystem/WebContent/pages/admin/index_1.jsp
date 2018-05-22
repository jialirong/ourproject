<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
 	System.out.println(basePath);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>登录界面</title>
 <link href="${pageContext.request.contextPath}/css/style1.css" rel="stylesheet" type="text/css" /> 
<script language="JavaScript" src="${pageContext.request.contextPath}/js/jquery2.js"></script>
 <script src="${pageContext.request.contextPath}/js/cloud.js" type="text/javascript"></script>
<script language="javascript" src="${pageContext.request.contextPath}/js/js.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery-ui.min.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.8.3.js" ></script>
  
</head>

<script type="text/javascript">
	function resetValue() {
		document.getElementById("userName").value = "";
		document.getElementById("password").value = "";
		document.getElementById("loginy").value = "";
		
	}
	
	function loadimage(){
		document.getElementById("randImage").src = "${pageContext.request.contextPath}/pages/admin/image.jsp?"+Math.random();
	} 
	
	
	 
	 $(document).ready(function() {
	       $("li span").blur(function(){
	            $("span").hide();
		   });
			  
		});

	
			
		
</script>
<body style="background-color:#1c77ac; background-image: url(images/light.png) background-repeat:no-repeat; background-position:center top; overflow:hidden;">


<div id="mainBody">
  <div id="cloud1" class="cloud"></div>
  <div id="cloud2" class="cloud"></div>
</div>
<div class="logintop"> <span>欢迎登录某外贸仓库管理系统后台</span>
  <ul>
    <li><a href="#">欢迎您</a></li>
  </ul>
</div>
<div class="loginbody"> <span class="systemlogo"></span>
  <div class="loginbox">
   <form action="${pageContext.request.contextPath}/stockManageSystem/login!login" method="post">
    <ul>
    
      <li>
        <input type="text" value="${user.userName}"  name="user.userName" id="userName"  class="loginuser" placeholder="用户名" onclick="JavaScript:this.value=''"/><span >${requestScope.error }</span>	
        
      </li>
      <li>
        <input type="password" value="${user.password }"
						name="user.password" id="password" class="loginpwd" placeholder="密码" onclick="JavaScript:this.value=''"/>
      </li>
      <li>
        <input type="text" value="${imageCode }"  id="loginy" class="loginy" name="imageCode" id="imageCode" placeholder="输入验证码"/><label>
        <img onclick="javascript:loadimage();" title="换一张试试" name="randImage" id="randImage" src="image.jsp" width="60" height="20" border="1" align="absmiddle"></label>
      </li>
      <li>
        <input name="" type="submit" class="loginbtn" value="登录" />
      </li>
      <li>
        <input name="" type="button" class="resetbtn" value="重置"  onclick="resetValue()" />
      </li>

    </ul>
    </form>
  
  </div>
</div>

   

<div class="loginbm">版权所有： 12组 © Copyright 2018 - 2019.</div>
</body>
</html>
