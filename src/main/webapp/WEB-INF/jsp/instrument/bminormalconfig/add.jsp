<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">


	$(function() {
		$('#userAddForm').form({
			url : '${ctx}/instrument/bmi_normal_config/add.shtml',
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
			<table class="grid">
				<tr>
					<td>性别</td>
					<td colspan="3">
						<input type="radio" name="bmiNormalConfigFormMap.sex" value="1" checked>男
						<input type="radio" name="bmiNormalConfigFormMap.sex" value="2">女
					</td>
				</tr>
				<tr>
					<td>BMI范围</td>
					<td>
						<input name="bmiNormalConfigFormMap.min_bmi" type="text" placeholder="最小BMI" style="width:250px;height: 29px;" class="easyui-validatebox" data-options="required:true" value="">
					</td>
					<td>
						至
					</td>
					<td>
						<input name="bmiNormalConfigFormMap.max_bmi" type="text" placeholder="最大BMI" style="width:250px;height: 29px;" class="easyui-validatebox" data-options="required:true" value="">
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>