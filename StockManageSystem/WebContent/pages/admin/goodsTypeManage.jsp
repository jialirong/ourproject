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
	
	function searchGoodsType(){
		$("#dg").datagrid('load',{
			s_typeName:$("#s_typeName").val(),
			s_wid:$('#s_wid').combobox("getValue"),
		});
	}
	
	function deleteGoodsType(){
		var selectedRows = $("#dg").datagrid("getSelections");
		if(selectedRows.length==0){
			$.messager.alert("系统提示","请选择要删除的数据");
			return;
		}
		var strIds=[];	//这种定义变量的方式
		for(var i=0;i<selectedRows.length;i++){
			strIds.push(selectedRows[i].gtid);
		}
		var ids = strIds.join(",");
		$.messager.confirm("系统提示","您确认要删除这<font color=red>"+selectedRows.length+"</font>条数据吗?",function(r){
			if(r){
				$.post("${pageContext.request.contextPath}/stockManageSystem/goodsType!delete",{delIds:ids},function(result){
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
	
	function openGoodsTypeAddDialog(){
		$("#dlg").dialog("open").dialog("setTitle","添加商品类别");
		url="${pageContext.request.contextPath}/stockManageSystem/goodsType!save";
	}
	
	function openGoodsTypeModifyDialog(){
		var selectedRows = $("#dg").datagrid("getSelections");
		if(selectedRows.length!=1){
			$.messager.alert("系统提示","请选择一条要修改的数据");
			return ;
		}
		var row = selectedRows[0];
		$("#dlg").dialog("open").dialog("setTitle","编辑商品类别信息");
		$("#wid").val(row.wid);
		$("#typeName").val(row.typeName);
		$("#typeDesc").val(row.typeDesc);
		url="${pageContext.request.contextPath}/stockManageSystem/goodsType!save?gtid="+row.gtid;
	}
	
	function closeGoodsTypeDialog(){
		$("#dlg").dialog("close");
		resetValue();
	}
	
	function resetValue(){
		$("#id").val("");
	
		$("#typeName").val("");
		$("#typeDesc").val("");
	}
	
	function saveGoodsType(){
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
		$("#s_wid").val("");
	}
	
	function exportData(){
		window.open('${pageContext.request.contextPath}/stockManageSystem/goodsType!export')
	}
</script>
</head>
<body>
	<body style="margin: 5px;">
	<table style="height:423px; width:1160px;" id="dg" title="商品类型信息管理" class="easyui-datagrid" fitColumns="true"
	 pagination="true" rownumbers="true" url="${pageContext.request.contextPath}/stockManageSystem/goodsType!execute" toolbar="#tb">
		<thead>
			<tr>
				<th field="cb" checkbox="true"></th>
				<th field="gtid" width="3%">编号</th>
				
				<th field="typeName" width="6%">商品类别名称</th>
				<th field="wid" width="3%">所属仓库编号</th>
				<th field="typeDesc" width="10%">商品类别备注</th>
				
			</tr>
		</thead>
	</table>
	
	<div id="tb">
		<div>
			<a href="javascript:openGoodsTypeAddDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
			<a href="javascript:openGoodsTypeModifyDialog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
			<a href="javascript:deleteGoodsType()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="exportData()">导出数据</a>
		</div>
		<div>&nbsp;商品类别名称：&nbsp;<input type="text" name="s_typeName" id="s_typeName"/>  
		&nbsp;仓库类别：&nbsp;<input class="easyui-combobox" id="s_wid" name="s_wid" size="10" data-options="panelHeight:'auto',editable:false,valueField:'wid',textField:'wName',url:'${pageContext.request.contextPath}/stockManageSystem/warehouse!wareHouseComboList'"/>
		<a href="javascript:searchGoodsType()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
		<a href="javascript:cleraValue()" class="easyui-linkbutton" iconCls="icon-no" plain="true">清空</a></div>
	</div>
	
	<div id="dlg" class="easyui-dialog" style="width:580px;height:350px;padding: 10px 20px"
	closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table cellspacing="5px;">
				<tr>
					<td>商品类别名称：</td>
					<td><input type="text" name="goodsType.typeName" id="typeName" class="easyui-validatebox" required="true"/></td>
					
							<td>商品所属仓库：</td>
					<td><input class="easyui-combobox" id="wid" name="goodsType.wid" size="10" data-options="panelHeight:'auto',editable:false,valueField:'wt',textField:'wName',url:'${pageContext.request.contextPath}/stockManageSystem/warehouse!wareHouseComboList'"/></td>
					
				</tr>
				<tr>
					<td valign="top">商品类别备注：</td>
					<td colspan="3"><textarea rows="7" cols="45" name="goodsType.typeDesc" id="typeDesc"></textarea></td>
				</tr>
			</table>
		</form>
	</div>
	
	<div id="dlg-buttons">
		<a href="javascript:saveGoodsType()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
		<a href="javascript:closeGoodsTypeDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
	</div>
</body>
</html>