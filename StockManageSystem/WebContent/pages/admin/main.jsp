<%@page import="com.sun.org.apache.xalan.internal.xsltc.compiler.sym"%>
<%@page import="util.MD5Util"%>
<%@page import="model.User"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	System.out.println(basePath);
	Object obj = session.getAttribute("username"); 
	if(obj==null){
		request.setAttribute("error", "您还没有登陆");
		request.getRequestDispatcher("index_1.jsp").forward(request, response);
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>仓库管理系统</title>
<base href="<%=basePath%>">

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>

<%
			
	if((Integer)session.getAttribute("role")==MD5Util.KEY){

%>
<script type="text/javascript">
	$(function(){
		var treeData = [
			{
			text:"仓库管理",
			children:[{
				text:"商品管理",
				attributes:{
					url:"${pageContext.request.contextPath}/pages/admin/goodsManage.jsp"
				}
			},{
				text:"产品入库管理",
				attributes:{
					url:"${pageContext.request.contextPath}/pages/admin/importManage.jsp"
				}
			},{
				text:"产品库存管理",
				attributes:{
					url:"${pageContext.request.contextPath}/pages/admin/stockManage.jsp"
				}
			},{
				text:"产品出库管理",
				attributes:{
					url:"${pageContext.request.contextPath}/pages/admin/exportManage.jsp"
				}
			},{
				text:"结账",
				attributes:{
					url:"${pageContext.request.contextPath}/pages/admin/dayManage.jsp"
				}
			}
					]
				},{
					text:"信息管理",
					children:[{
						text:"客户信息管理",
						attributes:{
							url:"${pageContext.request.contextPath}/pages/admin/couManager.jsp"
						}
					},{text:"商品类型信息管理",
					attributes:{
						url:"${pageContext.request.contextPath}/pages/admin/goodsTypeManage.jsp"
					}
				},{text:"供应商管理",
					attributes:{
						url:"${pageContext.request.contextPath}/pages/admin/providerMnanage.jsp"
					}
				},{text:"仓库管理",
					attributes:{
						url:"${pageContext.request.contextPath}/pages/admin/warehouseMnanage.jsp"
					}
				}]}
		];
		
		$("#tree").tree({
			data:treeData,
			lines:true,
			onClick:function(node){
				if(node.attributes){
					openTab(node.text,node.attributes.url);
				}
			}
		});
		
		function openTab(text,url){
			if($("#tabs").tabs('exists',text)){
				$("#tabs").tabs('select',text);
			}else{
				var content="<iframe frameborder='0' scrolling='auto' style='width:100%;height:100%' src="+url+"></iframe>";
				$("#tabs").tabs('add',{
					title:text,
					closable:true,
					content:content
				});
			}
		}
	});
		</script>
		<%} else{ %>
		<script type="text/javascript">
			$(function(){
		var treeData = [
			{
			text:"仓库管理",
			children:[{
				text:"商品管理",
				attributes:{
					url:"${pageContext.request.contextPath}/pages/admin/goodsManage.jsp"
				}
			},{
				text:"产品入库管理",
				attributes:{
					url:"${pageContext.request.contextPath}/pages/admin/importManage.jsp"
				}
			},{
				text:"产品库存管理",
				attributes:{
					url:"${pageContext.request.contextPath}/pages/admin/stockManage.jsp"
				}
			},{
				text:"产品出库管理",
				attributes:{
					url:"${pageContext.request.contextPath}/pages/admin/exportManage.jsp"
				}
			},{
				text:"结账",
				attributes:{
					url:"${pageContext.request.contextPath}/pages/admin/dayManage.jsp"
				}
			}
					]
				}
		];
		
		
		$("#tree").tree({
			data:treeData,
			lines:true,
			onClick:function(node){
				if(node.attributes){
					openTab(node.text,node.attributes.url);
				}
			}
		});
		
		function openTab(text,url){
			if($("#tabs").tabs('exists',text)){
				$("#tabs").tabs('select',text);
			}else{
				var content="<iframe frameborder='0' scrolling='auto' style='width:100%;height:100%' src="+url+"></iframe>";
				$("#tabs").tabs('add',{
					title:text,
					closable:true,
					content:content
				});
			}
		}
	});
		</script> 
		<%} %>
		
</head>
<body class="easyui-layout">
	<div region="north" style="height: 120px; background-color: rgb(224,236,255);">
		<div
			style="width: 10%; float: left; padding-left: 70px; padding-top: 20px;"></div>
		<div
			style="width: 65%; float: left; padding-top: 70px; height: 50px; font-size: 30px;">仓库管理系统</div>
		<div style="padding-top: 50px; padding-right: 20px; font-size: 14px;">
			当前用户：<font color="red" size="12px">${sessionScope.username }</font> <a
				href="${pageContext.request.contextPath}/stockManageSystem/login!logOut"><font
				color="red">退出</font></a>

		</div>
	</div>

	<div region="west" style="width: 200px;" title="导航菜单" split="true">
		<ul id="tree">

		</ul>
	</div>

	<div region="center" class="easyui-tabs" fit="true" border="false"
		id="tabs">
		<div title="首页">
			<div align="center" style="padding-top: 100px;">
				<font color="grey" size="10">欢迎使用仓库管理系统</font>
			</div>
		</div>
	</div>


</body>
</html>