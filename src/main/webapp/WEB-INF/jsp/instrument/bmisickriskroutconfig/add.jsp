<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">


	$(function() {


		$('#userAddForm').form({
			url : '${ctx}/instrument/bmiRiskRoutConfig/add.shtml',
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
			<input type="hidden" value="${sickId}" name="bmiSickRiskRoutFormMap.sick_risk_id">
			<table class="grid">
				<tr>
					<td>最小BMI</td>
					<td colspan="3">
						<input name="bmiSickRiskRoutFormMap.bmi_min" type="text" placeholder="最小BMI" style="width:200px;height: 29px;" class="easyui-validatebox" data-options="required:true" value="">
					</td>
				</tr>
				<tr>
					<td>最大BMI</td>
					<td colspan="3">
						<input name="bmiSickRiskRoutFormMap.bmi_max" type="text" placeholder="最大BMI" style="width:200px;height: 29px;" class="easyui-validatebox" data-options="required:true" value="">
					</td>
				</tr>
				<tr>
					<td>疾病风险率</td>
					<td colspan="3">
						<input name="bmiSickRiskRoutFormMap.rout" type="text" placeholder="疾病风险率" style="width:200px;height: 29px;" class="easyui-validatebox" data-options="required:true" value="">&nbsp;&nbsp;&nbsp;%
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>