<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	

	$(function() {
		
		$('#pid').combotree({
			url : '${ctx}/resource/tree.shtml?flag=false',
			parentField : 'pid',
			lines : true,
			panelHeight : 'auto'
		});
		
		$('#resourceAddForm').form({
			url : '${ctx}/resource/add.shtml',
			onSubmit : function() {
				progressLoad();
				var isValid = $(this).form('validate');
				if (!isValid) {
					progressClose();
				}
				return isValid;
			},
			success : function(result) {
				progressClose();
				result = $.parseJSON(result);
				if (result.status == 1) {
					parent.$.modalDialog.openner_treeGrid.treegrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_treeGrid这个对象，是因为resource.jsp页面预定义好了
					//parent.layout_west_tree.tree('reload');
					parent.$.modalDialog.handler.dialog('close');
				}
			}
		});
		
	});
</script>
<div style="padding: 3px;">
	<form id="resourceAddForm" method="post">
		<table  class="grid">
			<tr>
				<td>资源名称</td>
				<td>
					<input name="resFormMap.name" type="text" placeholder="请输入资源名称" value="${resFormMap.name}" class="easyui-validatebox span2" data-options="required:true" >
				</td>
				<td>资源代码</td>
				<td>
					<input name="resFormMap.resKey" type="text" placeholder="请输入资源码" value="${resFormMap.resKey}" class="easyui-validatebox span2" data-options="required:true" >
				</td>
			</tr>
			<tr>
				<td>资源路径</td>
				<td><input name="resFormMap.resUrl" type="text" value="${resFormMap.resUrl}" placeholder="请输入资源路径" class="easyui-validatebox span2" ></td>
				<td>排序</td>
				<td><input name="resFormMap.level" value="${resFormMap.level}"  class="easyui-numberspinner" style="width: 140px; height: 29px;" required="required" data-options="editable:false"></td>
			</tr>
			<tr>
				<td>菜单图标</td>
				<td ><input  name="resFormMap.icon" value="${resFormMap.icon}" placeholder="请输入图标"/></td>
				<td>显示/隐藏</td>
				<td >
					<select id="resFormMap_ishide" name="resFormMap.ishide" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
						<option value="0">显示</option>
						<option value="1">隐藏</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>上级资源</td>
				<td colspan="3"><select id="pid" name="resFormMap.parentId" style="width: 200px; height: 29px;"></select>
					<a class="easyui-linkbutton" href="javascript:void(0)" onclick="$('#pid').combotree('clear');" >清空</a></td>
			</tr>
			<tr>
				<td>资源类型</td>
				<td colspan="3">
					<select id="resFormMap_type" name="resFormMap.type" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
						<option value="0">目录</option>
						<option value="1">菜单</option>
						<option value="2">操作</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>资源描述</td>
				<td colspan="3">
					<textarea id="description" name="resFormMap.description" cols="50">${resFormMap.description}</textarea>
				</td>
			</tr>
		</table>
	</form>
</div>