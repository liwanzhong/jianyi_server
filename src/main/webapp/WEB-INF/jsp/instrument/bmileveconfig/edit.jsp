<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {


		$('#userEditForm').form({
			url : '${ctx}/instrument/bmi_leve_config/update.shtml',
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
		<form id="userEditForm" method="post">
			<input type="hidden" value="${bmileveconfigFormMap.id}" name="bmileveconfigFormMap.id">
			<table class="grid">
				<tr>
					<td>BMI健康等级名称</td>
					<td colspan="3">
						<input name="bmileveconfigFormMap.health_leve_name" type="text" placeholder="等级名称" style="width:250px;height: 29px;" class="easyui-validatebox" data-options="required:true" value="${bmileveconfigFormMap.health_leve_name}">
					</td>
				</tr>
				<tr>
					<td>BMI范围</td>
					<td>
						<input name="bmileveconfigFormMap.bmi_min" type="text" placeholder="最小BMI" style="width:250px;height: 29px;" class="easyui-validatebox" data-options="required:true" value="${bmileveconfigFormMap.bmi_min}">
					</td>
					<td>
						至
					</td>
					<td>
						<input name="bmileveconfigFormMap.bmi_max" type="text" placeholder="最大BMI" style="width:250px;height: 29px;" class="easyui-validatebox" data-options="required:true" value="${bmileveconfigFormMap.bmi_max}">
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>