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

	function searchImport1(){
		$('#dg1').datagrid('load',{
			
			s_bimpoPrice:$('#s_bimpoPrice').val(),
			s_eimpoPrice:$('#s_eimpoPrice').val(),
			s_bimpoDate:$('#s_bimpoDate').datebox("getValue"),
			s_eimpoDate:$('#s_eimpoDate').datebox("getValue"),
			s_wid:$('#s_wid').combobox("getValue"),
		});
	}
	
	function openChoiceGoodsDialog(){
		$("#dlg2").dialog("open").dialog("setTitle","选择商品");
	}
	
	function searchImport2(){
		$('#dg2').datagrid('load',{
			s_goodsName:$('#s_goodsName').val(),
		});
	}

	function deleteImport(){
		var selectedRows = $("#dg1").datagrid("getSelections");
		if(selectedRows.length==0){
			$.messager.alert("系统提示","请选择要删除的数据");
			return;
		}
		var strIds=[];
		for(var i=0;i<selectedRows.length;i++){
			strIds.push(selectedRows[i].iid);
		}
		var ids = strIds.join(",");
		$.messager.confirm("系统提示","您确认要删除这<font color=red>"+selectedRows.length+"</font>条数据吗?",function(r){
			if(r){
				$.post("${pageContext.request.contextPath}/stockManageSystem/import!delete",{delIds:ids},function(result){
					if(result.success){
						$.messager.alert("系统提示","您已成功删除<font color=red>"+result.delNums+"</font>条数据！");
						$("#dg1").datagrid("reload");
					}else{
						$.messager.alert('系统提示',result.errorMsg);
					}
				},"json");
			}
		});
	}

	function openImportAddDialog(){
		$("#dlg1").dialog("open").dialog("setTitle","添加入库信息");
		url="${pageContext.request.contextPath}/stockManageSystem/import!save";
	}
	
	function closeImportDialog(){
		$("#dlg1").dialog("close");
		resetValue();
	}
	
	function resetValue(){
		$("#iid").combobox("setValue","");
		$("#impoPrice").val("");
		$("#impoDate").datebox("setValue","");
		$("#impoNum").val("");
		$("#impoDesc").val("");
		$("#serviceId").combobox("setValue","");
		$("#couId").combobox("setValue","");
		//zcxvbnxcvb
	}
	
	function saveImport(){
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
					$("#dlg1").dialog("close");
					$("#dg1").datagrid("reload");
				}
			}
		});
	}
	
	function openImportModifyDialog(){
		var selectedRows = $("#dg1").datagrid("getSelections");
		if(selectedRows.length!=1){
			$.messager.alert("系统提示","请选择一条要修改的数据");
			return ;
		}
		var row = selectedRows[0];
		$("#dlg1").dialog("open").dialog("setTitle","编辑入库信息");
		$("#goodsId").combobox("setValue",row.goodsId);
		$("#impoPrice").val(row.impoPrice);
		$("#impoDate").datebox("setValue",row.impoDate);
		$("#impoNum").val(row.impoNum);
		$("#serviceId").combobox("setValue",row.serviceId);
		$("#whid").combobox("setValue",row.wid);
		$("#couId").combobox("setValue",row.name);
		$("#impoDesc").val(row.impoDesc);
		url="${pageContext.request.contextPath}/stockManageSystem/import!save?iid="+row.iid+"&beforeNum="+row.impoNum;
	}
	
	function cleraValue(){
		$("#s_bimpoPrice").val("");
		$("#s_eimpoPrice").val("");
		$("#s_bimpoDate").datebox("setValue","");
		$("#s_eimpoDate").datebox("setValue","");
		$("#s_wid").combobox("setValue","");
	}
	
	function exportData(){
		window.open('${pageContext.request.contextPath}/stockManageSystem/import!export')
	}
	

	
	function downloadTemplate(){
		window.open('${pageContext.request.contextPath}/template/importTemp.xls');
	}
	

</script>
</head>
<body style="margin: 5px;">
	<table style="height:423px; width:1160px" id="dg1" title="商品入库管理" class="easyui-datagrid" fitColumns="true"
	 pagination="true" rownumbers="true" url="${pageContext.request.contextPath}/stockManageSystem/import" toolbar="#tb">
		<thead>
			<tr>
				<th field="cb" checkbox="true"></th>
				<th field="iid" width="20">编号</th>  
				<th field="goodsId" width="20" hidden="true">商品ID</th>
				<th field="goodsName" width="30">商品名称</th>
				<th field="impoPrice" width="30">入库价格</th>
				<th field="serviceId" width="30">业务员编号</th>
				<th field="name" width="30">货主名称</th>
					<th field="wid" width="30">入库仓库编号</th>
				<th field="impoDate" width="60">入库时间</th>
				<th field="impoNum" width="60">入库数量</th>
				<th field="impoDesc" width="100">入库备注</th>
				
			</tr>
		</thead>
	</table>
	
	<div id="tb">
		<div>
			<a href="javascript:openImportAddDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
			<a href="javascript:openImportModifyDialog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
			 <a href="javascript:deleteImport()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="exportData()">导出数据</a>
		</div>
		<div>
		&nbsp;&nbsp;入库价格：&nbsp;<input type="text" name="s_bimpoPrice" id="s_bimpoPrice"  size="10"/>--<input type="text" name="s_eimpoPrice" id="s_eimpoPrice"  size="10"/>
		&nbsp;入库时间：&nbsp;<input class="easyui-datebox" name="s_bimpoDate" id="s_bimpoDate" editable="false" size="10"/>-><input class="easyui-datebox" name="s_eimpoDate" id="s_eimpoDate" editable="false" size="10"/> 
		&nbsp;仓库类别：&nbsp;<input class="easyui-combobox" id="s_wid" name="s_wid" size="10" data-options="panelHeight:'auto',editable:false,valueField:'wid',textField:'wName',url:'${pageContext.request.contextPath}/stockManageSystem/warehouse!wareHouseComboList'"/>
		&nbsp;&nbsp;&nbsp;<a href="javascript:searchImport1()" class="easyui-linkbutton" iconCls="icon-search" >搜索</a>
		<a href="javascript:cleraValue()" class="easyui-linkbutton" iconCls="icon-no" plain="true">清空</a>
		</div>
		
	</div>
	
	<div id="dlg1" class="easyui-dialog" style="width:540px;height:350px;padding: 10px 20px"
	closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table cellspacing="5px;">
				<tr>
					<td>商品名称：</td>
					<td><input class="easyui-combobox" id="goodsId" name="importGoods.goodsId" size="10" data-options="panelHeight:'auto',editable:false,valueField:'gid',textField:'goodsName',url:'${pageContext.request.contextPath}/stockManageSystem/goods!goodsComboList'"/></td>
					
					<td>入库价格：</td>
					<td><input type="text" name="importGoods.impoPrice" id="impoPrice" class="easyui-validatebox" required="true"/></td>
				</tr>
				
				
				<tr>
					<td>入库日期：</td>
					<td><input class="easyui-datebox" name="importGoods.impoDate" id="impoDate" editable="false" size="15" required="true"/>
					
					<td>入库数量：</td>
					<td><input  name="importGoods.impoNum" id="impoNum" class="easyui-validatebox" required="true" /></td>
				</tr>
				
				<tr>
				<td>业务员编号：</td>
					  <td><input class="easyui-combobox" name="importGoods.serviceId" id="serviceId" size="10" data-options="panelHeight:'auto',editable:false,valueField:'uid',textField:'uid',url:'${pageContext.request.contextPath}/stockManageSystem/import!importServiceComboList'"/></td>
				<td>货主名称：</td>
					 <td><input name="importGoods.couId" id="couId"  class="easyui-combobox" size="10" data-options="panelHeight:'auto',editable:false,valueField:'cid',textField:'name',url:'${pageContext.request.contextPath}/stockManageSystem/import!importCouComboList'"/></td> 
				</tr>
				
			
				<tr>
				
					<td>仓库名称：</td>
					<td><input type="text" name="importGoods.whid" id="whid" class="easyui-combobox" size="10" required="true" data-options="panelHeight:'auto',editable:false,valueField:'wid',textField:'wName',url:'${pageContext.request.contextPath}/stockManageSystem/warehouse!wareHouseComboList'"/></td>
				
					<td valign="top">入库备注：</td>
					<td colspan="3"><textarea rows="7" cols="43" name="importGoods.impoDesc" id="impoDesc"></textarea></td>
				</tr>
			</table>
		</form>
	</div>
	
	<div id="dlg-buttons">
		<a href="javascript:saveImport()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
		<a href="javascript:closeImportDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
	</div>
	
</body>
</html>