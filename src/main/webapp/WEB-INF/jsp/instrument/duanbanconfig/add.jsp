<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">


	$(function() {
		$('#duanban_leve_id').combotree({
			url : '${ctx}/instrument/pingfen_leve/tree.shtml',
			parentField : 'pid',
			lines : true,
			panelHeight : 'auto'
		});

		$('#userAddForm').form({
			url : '${ctx}/instrument/duanbanconfig/add.shtml',
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
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
					parent.$.modalDialog.handler.dialog('close');
				} else {
					parent.$.messager.alert('提示', result.msg, 'warning');
				}
			}
		});

	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="userAddForm" method="post">
			<input type="hidden" value="${checkItemId}" name="duanbanConfigFormMap.big_item_id">
			<table class="grid">
				<tr>
					<td>等级名称</td>
					<td colspan="3">
						<select id="duanban_leve_id" name="duanbanConfigFormMap.duanban_leve_id" style="width:200px;height: 29px;" data-options="required:true"></select>
					</td>
				</tr>
				<tr>
					<td>调整分数</td>
					<td colspan="3">
						<input name="duanbanConfigFormMap.tz_rout" type="text" placeholder="调整分数" style="width:250px;height: 29px;" class="easyui-validatebox" data-options="required:true" value=""><font color="red">注:加分填写正数，减分填写负数</font>
					</td>
				</tr>
				<tr>
					<td>项数设置</td>
					<td colspan="3">
						<input name="duanbanConfigFormMap.duanban_item_tz_count" type="text" placeholder="项数设置" style="width:250px;height: 29px;" class="easyui-validatebox" data-options="required:true" value="">项以及以上
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>