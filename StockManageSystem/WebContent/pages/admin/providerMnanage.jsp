<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>仓库管理系统</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>

<%
	Object obj = session.getAttribute("username"); 
		if(obj==null){
			request.getRequestDispatcher("index_1.jsp").forward(request, response);
	} 
%>
<script type="text/javascript">
	var url;
	function searchProvider(){
		$("#dg").datagrid('load',{
			s_typeName:$("#s_typeName").val()
		});
	}
	
	function deleteProvider(){
		var selectedRows = $("#dg").datagrid("getSelections");
		if(selectedRows.length==0){
			$.messager.alert("系统提示","请选择要删除的数据");
			return;
		}
		var strIds=[];	//这种定义变量的方式
		for(var i=0;i<selectedRows.length;i++){
			strIds.push(selectedRows[i].pid);
		}
		var ids = strIds.join(",");
		$.messager.confirm("系统提示","您确认要删除这<font color=red>"+selectedRows.length+"</font>条数据吗?",function(r){
			if(r){
				$.post("${pageContext.request.contextPath}/stockManageSystem/provider!delete",{delIds:ids},function(result){
					if(result.success){
						$.messager.alert("系统提示","您已成功删除<font color=red>"+result.delNums+"</font>条数据！");
						$("#dg").datagrid("reload");
					}else{
						$.messager.alert('系统提示',result.errorMsg);
					}
				},"json");
			}
		});
	}
	
	function openProviderAddDialog(){
		$("#dlg").dialog("open").dialog("setTitle","添加供应商");
		url="${pageContext.request.contextPath}/stockManageSystem/provider!save";
	}
	
	function openProviderModifyDialog(){
		var selectedRows = $("#dg").datagrid("getSelections");
		if(selectedRows.length!=1){
			$.messager.alert("系统提示","请选择一条要修改的数据");
			return ;
		}
		var row = selectedRows[0];
		$("#dlg").dialog("open").dialog("setTitle","编辑供应商");
		$("#pid").val(row.pid);
		$("#proId").val(row.proId);
		$("#proName").val(row.proName);
		$("#linkMan").val(row.linkMan);
		$("#proPhone").val(row.proPhone);
		$("#proDesc").val(row.proDesc);
		url="${pageContext.request.contextPath}/stockManageSystem/provider!save?pid="+row.pid;
	}
	
	function closeProviderDialog(){
		$("#dlg").dialog("close");
		resetValue();
	}
	
	function resetValue(){
		$("#pid").val("");
		$("#proId").val("");
		$("#proName").val("");
		$("#linkMan").val("");
		$("#proPhone").val("");
		$("#proDesc").val("");
	}
	
	function saveProvider(){
		$("#fm").form("submit",{
			url:url,
			onSubmit:function(){
				return $(this).form("validate");
			},
			success:function(result){
				if(result.errorMsg){
					$.messager.alert("系统提示",reuslt.errorMsg);
					return error;
				}else{
					$.messager.alert("系统提示","保存成功");
					resetValue();
					$("#dlg").dialog("close");
					$("#dg").datagrid("reload");
				}
			}
		});
	}
	
	function cleraValue(){
		$("#s_typeName").val("");
	}
	
	function exportData(){
		window.open('${pageContext.request.contextPath}/stockManageSystem/provider!export')
	}
</script>
</head>
<body>
	<body style="margin: 5px;">
	<table style="height:423px; width:1160px;" id="dg" title="供应商信息管理" class="easyui-datagrid" fitColumns="true"
	 pagination="true" rownumbers="true" url="${pageContext.request.contextPath}/stockManageSystem/provider" toolbar="#tb">
		<thead>
			<tr>
				<th field="cb" checkbox="true"></th>
				<th field="pid" width="20">编号</th>  
				<th field="proId" width="20" hidden="true">供应商ID</th>
				<th field="proName" width="30">供应商名称</th>
				<th field="linkMan" width="30">供应商联系人</th>
				<th field="proPhone" width="60">供应商联系电话</th>
				<th field="proDesc" width="100">供应商备注</th>
			</tr>
		</thead>
	</table>
	
	<div id="tb">
		<div>
			<a href="javascript:openProviderAddDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
			<a href="javascript:openProviderModifyDialog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
			<a href="javascript:deleteProvider()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="exportData()">导出数据</a>
		</div>
		<div>&nbsp;供应商名称：&nbsp;<input type="text" name="s_typeName" id="s_typeName"/><a href="javascript:searchProvider()" id="dg" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
		<a href="javascript:cleraValue()" class="easyui-linkbutton" iconCls="icon-no" plain="true">清空</a></div>
	</div>
	
	<div id="dlg" class="easyui-dialog" style="width:580px;height:350px;padding: 10px 20px"
	closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post">
				<table cellspacing="5px;">
				<tr>
					<td>供应商Id：</td>
					<td><input type="text" name="provider.proId" id="proId" class="easyui-validatebox" required="true"/></td>
					
					<td>供应商名称：</td>
					<td><input type="text" name="provider.proName" id="proName" class="easyui-validatebox" required="true"/></td>
				</tr>
				
				<tr>
				<td>供应商联系人：</td>
					<td><input  name="provider.linkMan" id="linkMan" class="easyui-validatebox" required="true" /></td>
				<td>供应商联系电话：</td>
					<td><input  name="provider.proPhone" id="proPhone" class="easyui-validatebox" required="true" /></td>
				</tr>
				
				<tr>
					<td valign="top">供应商备注：</td>
					<td colspan="3"><textarea rows="7" cols="43" name="provider.proDesc" id="proDesc"></textarea></td>
				</tr>
			</table>
		</form>
	</div>
	
	<div id="dlg-buttons">
		<a href="javascript:saveProvider()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
		<a href="javascript:closeProviderDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
	</div>
</body>
</html>