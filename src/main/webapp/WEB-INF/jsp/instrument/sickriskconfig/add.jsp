<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">


	$(function() {

		$('#sick_risk_id').combotree({
			url : '${ctx}/instrument/sickRisk/tree.shtml',
			parentField : 'pid',
			lines : true,
			panelHeight : 'auto'
		});




		$('#userAddForm').form({
			url : '${ctx}/instrument/sickRisk/add.shtml',
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
			<input type="hidden" value="${checkItemId}" name="sickRiskFormMap.check_item_id">
			<input type="hidden" value="${checkItemType}" name="sickRiskFormMap.check_item_type">
			<table class="grid">
				<tr>
					<td>关联的疾病风险项</td>
					<td colspan="3">
						<select id="sick_risk_id" name="sickRiskFormMap.sick_risk_id" style="width:200px;height: 29px;" data-options="required:true"></select>
					</td>
				</tr>
				<tr>
					<td>风险系数</td>
					<td colspan="3">
						<input name="sickRiskFormMap.risk_rout" type="text" placeholder="风险系数" style="width:200px;height: 29px;" class="easyui-validatebox" data-options="required:true" value="">
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>