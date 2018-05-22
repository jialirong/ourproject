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
<script type="text/javascript">
	var url;

	function searchGoods(){
		$('#dg').datagrid('load',{
			s_wid:$('#s_wid').combobox("getValue"),
			s_goodsName:$('#s_goodsName').val(),
			s_proId:$('#s_proId').combobox("getValue"),
			s_typeId:$('#s_typeId').combobox("getValue")
		});
	}
	
	function deleteGoods(){
		var selectedRows=$("#dg").datagrid('getSelections');//返回所有被选择的行，当没有记录被选择时，将返回一个空数组。
		if(selectedRows.length==0){
			$.messager.alert("系统提示","请选择要删除的数据！");
			return;
		}
		var strIds=[];
		for(var i=0;i<selectedRows.length;i++){
			strIds.push(selectedRows[i].gid);
		}
		var ids=strIds.join(",");
		$.messager.confirm("系统提示","您确认要删掉这<font color=red>"+selectedRows.length+"</font>条数据吗？",function(r){
			if(r){
				$.post("${pageContext.request.contextPath}/stockManageSystem/goods!delete",{delIds:ids},function(result){
					if(result.success){
						$.messager.alert("系统提示","您已成功删除<font color=red>"+result.delNums+"</font>条数据！");
						$("#dg").datagrid("reload");
					}else{
						$.messager.alert('系统提示','<font color=red>'+selectedRows[result.errorIndex].goodsName+'</font>'+result.errorMsg);
					}
				},"json");
			}
		});
	}
	
	function openGoodsAddDialog(){
		$("#dlg").dialog("open").dialog("setTitle","添加商品信息");
		url="${pageContext.request.contextPath}/stockManageSystem/goods!save";
	}
	
	function saveGoods(){
		$("#fm").form("submit",{
			url:url,
			onSubmit:function(){
				if($('#proId').combobox("getValue")==""){
					$.messager.alert("系统提示","请选择供应商");
					return false;
				}
				if($('#typeId').combobox("getValue")==""){
					$.messager.alert("系统提示","请选择商品类别");
					return false;
				}
				//传递一个封装好的goods对象到goodsaction中的goods实例
				return $(this).form("validate");//表单字段的验证，如果返回true，则说明所有的字段都是合法的。
			},
			success:function(result){
				if(result.errorMsg){
					$.messager.alert("系统提示",result.errorMsg);
					return;
				}else{
					$.messager.alert("系统提示","保存成功");
					resetValue();
					$("#dlg").dialog("close");
					$("#dg").datagrid("reload");
				}
			}
		});
	}
	
	function resetValue(){
		$("#goodsId").val("");
		$("#goodsName").val("");
		$("#proId").combobox("setValue","");
		$("#typeId").combobox("setValue","");
		$("#goodsDesc").val("");
		$("#proData").combobox("setValue","");
	}
	
	function closeGoodsDialog(){
		$("#dlg").dialog("close");
		resetValue();
	}
	
	function openGoodsModifyDialog(){
		var selectedRows=$("#dg").datagrid('getSelections');
		if(selectedRows.length!=1){
			$.messager.alert("系统提示","请选择一条要编辑的数据！");
			return;
		}
		var row=selectedRows[0];
		$("#dlg").dialog("open").dialog("setTitle","编辑商品信息");
		$("#goodsId").val(row.goodsId);
		$("#goodsName").val(row.goodsName);
		$("#proId").combobox("setValue",row.proId);
		$("#typeId").combobox("setValue",row.typeId);
		$("#goodsDesc").val(row.goodsDesc);
		$("#proData").combobox("setValue",row.proData);
		url="${pageContext.request.contextPath}/stockManageSystem/goods!save?gid="+row.gid;
	}
	//查询条件清空
	function cleraValue(){
		$("#s_wid").combobox("setValue","");
		$("#s_goodsId").val("");
		$("#s_goodsName").val("");
		$("#s_proId").combobox("setValue","");
		$("#s_typeId").combobox("setValue","");
	}
	function exportData(){
		window.open('${pageContext.request.contextPath}/stockManageSystem/goods!export')
	}
	

	
	function downloadTemplate(){
		window.open('${pageContext.request.contextPath}/template/goodsTemp.xls');
	}
	

</script>

</head>
<body style="margin: 5px;">
	<table style="height:423px; width:1160px" id="dg" title="商品管理" class="easyui-datagrid" fitColumns="true"
	 pagination="true" rownumbers="true" url="${pageContext.request.contextPath}/stockManageSystem/goods!execute" toolbar="#tb">
		<thead>
			<tr>
				<th field="cb" checkbox="true"></th>
				<th field="gid" width="15">编号</th>
				<th field="wid" width="15">仓库编号</th>
				<th field="goodsId" width="15" >商品编号</th>
				<th field="goodsName" width="25">商品名称</th>
				
				<th field="proId" width="15" hidden="true">供应商编号</th>
				<th field="proName" width="25">供应商</th>
				
				<th field="typeId" width="25" hidden="true">商品类别编号</th>
				<th field="typeName" width="25">商品类别</th>
			
				<th field="goodsDesc" width="30">商品描述</th>
				<th field="proData" width="30">生产日期</th>
				
			</tr>
		</thead>
	</table>
	
	<div id="tb">
		<div>
			<a href="javascript:openGoodsAddDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
			<a href="javascript:openGoodsModifyDialog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
			<a href="javascript:deleteGoods()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="exportData()">导出数据</a>
		</div>
		<div>&nbsp;仓库名称：&nbsp;<input class="easyui-combobox" id="s_wid" name="s_wid" size="10" data-options="panelHeight:'auto',editable:false,valueField:'wid',textField:'wName',url:'${pageContext.request.contextPath}/stockManageSystem/warehouse!wareHouseComboList'"/>
			&nbsp;商品名称：&nbsp;<input type="text" name="s_goodsName" id="s_goodsName" size="10" />
			&nbsp;供应商：&nbsp;<input class="easyui-combobox" id="s_proId" name="s_proId" size="10" data-options="panelHeight:'auto',editable:false,valueField:'pid',textField:'proName',url:'${pageContext.request.contextPath}/stockManageSystem/provider!providerComboList'"/>
			&nbsp;商品类别：&nbsp;<input class="easyui-combobox" id="s_typeId" name="s_typeId" style="width:100px;"  size="10" data-options="panelHeight:'auto',editable:false,valueField:'gtid',textField:'typeName',url:'${pageContext.request.contextPath}/stockManageSystem/goodsType!goodsTypeComboList'"/>
			<a href="javascript:searchGoods()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
			<a href="javascript:cleraValue()" class="easyui-linkbutton" iconCls="icon-no" plain="true">清空</a>
		</div>
	</div>
	<!-- 下面的是点击添加之后要弹出的div窗口 -->
	<div id="dlg" class="easyui-dialog" style="width:580px;height:350px;padding: 10px 20px"
	closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table cellspacing="5px;">
				<tr>
					<td>商品编号：</td>
					<td><input type="text" name="goods.goodsId" id="goodsId" class="easyui-validatebox" required="true"/></td>
					
					<td>商品名称：</td>
					<td><input type="text" name="goods.goodsName" id="goodsName" class="easyui-validatebox" required="true"/></td>
	
				</tr>
				
				<tr>
					<td>供应商：</td>
					<td><input class="easyui-combobox" id="proId" name="goods.proId" size="10" data-options="panelHeight:'auto',editable:false,valueField:'pid',textField:'proName',url:'${pageContext.request.contextPath}/stockManageSystem/provider!providerComboList'"/></td>
					
					<td>商品类别：</td>
					<td><input class="easyui-combobox" id="typeId" name="goods.typeId" style="width:100px;"  size="10" data-options="panelHeight:'auto',editable:false,valueField:'gtid',textField:'typeName',url:'${pageContext.request.contextPath}/stockManageSystem/goodsType!goodsTypeComboList'"/></td>
				</tr>
				<tr>
				<td>生产日期：</td>
					<td><input class="easyui-datebox" name="goods.proDate" id="proDate" editable="false" size="15" required="true"/></td>
					</tr>
				<tr>
					<td valign="top">商品描述：</td>
					<td colspan="3"><textarea rows="7" cols="45" name="goods.goodsDesc" id="goodsDesc"></textarea></td>
				</tr>
				
			</table>
		</form>
	</div>
	
	<div id="dlg-buttons">
		<a href="javascript:saveGoods()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
		<a href="javascript:closeGoodsDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
	</div>
	
	
    
	
</body>
</html>