<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">


	$(function() {


		$('#userAddForm').form({
			url : '${ctx}/instrument/checkValueScoreIn/add.shtml',
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
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');
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
			<input type="hidden" value="${checkSmallItemFormMap.id}" name="checkValueScoreInFormMap.check_small_item">
			<table class="grid">
				<tr>
					<td>检测值范围</td>
					<td colspan="3">
						<input name="checkValueScoreInFormMap.check_min_value" type="text" placeholder="检测值范围" class="easyui-validatebox" data-options="required:true" value=""> 至
						<input name="checkValueScoreInFormMap.check_max_value" type="text" placeholder="检测值范围" class="easyui-validatebox" data-options="required:true" value="">
						<span style="color: red;">检测值有效范围【${checkSmallItemFormMap.min_value} ---  ${checkSmallItemFormMap.max_value}】</span>
					</td>
				</tr>
				<tr>
					<td>得分范围</td>
					<td colspan="3">
						<input name="checkValueScoreInFormMap.min_score" type="text" placeholder="得分范围" class="easyui-validatebox" data-options="required:true" value=""> 至
						<input name="checkValueScoreInFormMap.max_score" type="text" placeholder="得分范围" class="easyui-validatebox" data-options="required:true" value="">
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>