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
	

	function deleteDay(){
		var selectedRows=$("#dg").datagrid('getSelections');//返回所有被选择的行，当没有记录被选择时，将返回一个空数组。
		if(selectedRows.length==0){
			$.messager.alert("系统提示","请选择要删除的数据！");
			return;
		}
		var strIds=[];
		for(var i=0;i<selectedRows.length;i++){
			strIds.push(selectedRows[i].dayid);
		}
		var ids=strIds.join(",");
		$.messager.confirm("系统提示","您确认要删掉这<font color=red>"+selectedRows.length+"</font>条数据吗？",function(r){
			if(r){
				$.post("${pageContext.request.contextPath}/stockManageSystem/day!delete",{delIds:ids},function(result){
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
	
	function openDayAddDialog(){
		$("#dlg").dialog("open").dialog("setTitle","添加结账信息");
		url="${pageContext.request.contextPath}/stockManageSystem/day!save";
	}
	
	function saveDay(){
		$("#fm").form("submit",{
			url:url,
			onSubmit:function(){
				
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
		$("#dayid").val("");
		$("#date").datebox("setValue","");
		$("#badnum").val("");
		$("#innum").val("");
		$("#outnum").val("");
		$("#sum").val("");
		$("#wid").val("");
		
	}
	
	function closeDayDialog(){
		$("#dlg").dialog("close");
		resetValue();
	}
	
	function openDayModifyDialog(){
		var selectedRows=$("#dg").datagrid('getSelections');
		if(selectedRows.length!=1){
			$.messager.alert("系统提示","请选择一条要编辑的数据！");
			return;
		}
		var row=selectedRows[0];
		$("#dlg").dialog("open").dialog("setTitle","编辑日结账信息");
		$("#dayid").val(row.dayid);
		$("#goodsId").combobox("setValue",row.goodsId);
		$("#date").datebox("setValue",row.date);
		$("#wid").combobox("setValue",row.whid);
		$("#badnum").val(row.badnum);
		$("#innum").val(row.innum);
		$("#outnum").val(row.outnum);
		$("#badnum").val(row.badnum);
		$("#sum").val(row.sum);
		url="${pageContext.request.contextPath}/stockManageSystem/day!save?dayid="+row.dayid;
	}
	//查询条件清空
	function clearValue(){
		$("#s_bexpoDate").datebox("setValue","");
		$("#s_eexpoDate").datebox("setValue",""); 
		$("#s_widd").combobox("setValue","");
	}
	function DayData(){
		window.open('${pageContext.request.contextPath}/stockManageSystem/day!export')
	}
	

	
	function downloadTemplate(){
		window.open('${pageContext.request.contextPath}/template/goodsTemp.xls');
	}
	

	
	
	function searchDay(){
		
		$("#dg1").datagrid('load',{
			s_wid:$("#s_widd").combobox('getValue'),
			s_bexpoDate:$("#s_bexpoDatee").datebox('getValue'),
			s_eexpoDate:$("#s_eexpoDatee").datebox('getValue'),
		});
		
	}
	function exportData(){
		window.open('${pageContext.request.contextPath}/stockManageSystem/day!export')
	}
</script>

</head>
<body style="margin: 5px;">
	<table style="height:423px; width:1160px" id="dg" title="商品管理" class="easyui-datagrid" fitColumns="true"
	 pagination="true" rownumbers="true" url="${pageContext.request.contextPath}/stockManageSystem/day!execute" toolbar="#tb">
		<thead>
			<tr>
				<th field="cb" checkbox="true"></th>
				<th field="dayid" width="10" >编号</th>
				<th field="date" width="10" >日期</th>
				<th field="whid" width="10">仓库编号</th>
				<th field="goodsId" width="10" >商品编号</th>
				<th field="innum" width="10">入库数量</th>
				<th field="outnum" width="10">出库数量</th>
				<th field="badnum" width="10">损坏数量</th>
				<th field="sum" width="10">剩余总数量</th>
				
			</tr>
		</thead>
	</table>
	
		<div id="tb">
		<div>
			<a href="javascript:openDayAddDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
			 <a href="javascript:openDayModifyDialog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a> 
			<a href="javascript:deleteDay()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="exportData()">导出数据</a>
		</div>
	<%-- 	<div>
			&nbsp;结账日期：&nbsp;<input class="easyui-datebox" name="s_bexpoDatee" id="s_bexpoDatee" editable="false" size="10"/>-><input class="easyui-datebox" name="s_eexpoDatee" id="s_eexpoDatee" editable="false" size="10"/> 
				&nbsp;仓库类别：&nbsp;<input class="easyui-combobox" id="s_widd" name="s_widd" size="10" data-options="panelHeight:'auto',editable:false,valueField:'wid',textField:'wName',url:'${pageContext.request.contextPath}/stockManageSystem/warehouse!wareHouseComboList'"/>
		
		<a href="javascript:searchDay()" id="dg1" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
		<a href="javascript:clearValue()" class="easyui-linkbutton" iconCls="icon-no" plain="true">清空</a>
		</div> --%>
		
	</div>
	
	<div id="dlg" class="easyui-dialog" style="width:540px;height:350px;padding: 10px 20px"
	closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table cellspacing="5px;">
				<tr>
					<td>日期：</td>
					<td><input class="easyui-datebox" id="date" name="day.date" size="10" required="true"/></td>
					
					<td>仓库名称：</td>
					<td><input type="text" name="day.wid" id="wid" class="easyui-combobox" size="10" required="true" data-options="panelHeight:'auto',editable:false,valueField:'wid',textField:'wName',url:'${pageContext.request.contextPath}/stockManageSystem/warehouse!wareHouseComboList'"/></td>
				</tr>
				<tr>
					<td>商品名称：</td>
					<td><input class="easyui-combobox" name="day.goodsId" id="goodsId" editable="false" size="10" required="true" data-options="panelHeight:'auto',editable:false,valueField:'gid',textField:'goodsName',url:'${pageContext.request.contextPath}/stockManageSystem/goods!goodsComboList'"/>
					
					<td>入库数量：</td>
					<td><input class="easyui-validatebox"  name="day.innum" id="innum"  required="true" /></td>
				</tr>
				<td>损坏数量：</td>
					<td><input class="easyui-validatebox" id="badnum" name="day.badnum" size="10" required="true"/></td>
					</tr>
				<tr>
					<td>出库数量：</td>
					<td><input class="easyui-validatebox" id="outnum" name="day.outnum" size="10" required="true"/></td>
					
				
				<tr>
				<td>总数量：</td>
					<td><input class="easyui-validatebox" id="sum" name="day.sum" size="10" required="true"/></td>
					</tr>
					
					<tr>
				
			</table>
		</form>
	</div>
	
	<div id="dlg-buttons">
		<a href="javascript:saveDay()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
		<a href="javascript:closeDayDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
	</div>
	

</body>



</html>