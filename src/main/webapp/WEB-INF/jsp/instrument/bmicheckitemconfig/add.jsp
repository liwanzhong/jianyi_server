<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">


	$(function() {
		$('#org_leve_id').combotree({
			url : '${ctx}/instrument/pingfen_leve/tree.shtml',
			parentField : 'pid',
			lines : true,
			panelHeight : 'auto'
		});


		$('#tz_leve_id').combotree({
			url : '${ctx}/instrument/pingfen_leve/tree.shtml',
			parentField : 'pid',
			lines : true,
			panelHeight : 'auto'
		});

		$('#userAddForm').form({
			url : '${ctx}/instrument/BmiCheckItemConfig/add.shtml',
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
			<input type="hidden" value="${checkItemId}" name="bmiCheckItemConfigFormMap.check_item_id">
			<input type="hidden" value="${checkItemType}" name="bmiCheckItemConfigFormMap.check_type">
			<table class="grid">
				<tr>
					<td>BMI范围</td>
					<td colspan="3">
						从<input name="bmiCheckItemConfigFormMap.bmi_min" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="">
						到<input name="bmiCheckItemConfigFormMap.bmi_max" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="">
					</td>
				</tr>
				<tr>
					<td>年龄范围</td>
					<td colspan="3">
						从<input name="bmiCheckItemConfigFormMap.age_min" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="">
						到<input name="bmiCheckItemConfigFormMap.age_max" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="">
					</td>
				</tr>
				<tr>
					<td>原等级</td>
					<td colspan="3">
						<select name="bmiCheckItemConfigFormMap.org_leve_id" id="org_leve_id" style="height: 29px;width: 200px" class="easyui-validatebox" data-options="required:true" >
						</select>
					</td>
				</tr>
				<tr>
					<td>调整后等级</td>
					<td colspan="3">
						<select name="bmiCheckItemConfigFormMap.tz_leve_id" id="tz_leve_id" style="height: 29px;width: 200px" class="easyui-validatebox" data-options="required:true" >
						</select>
					</td>
				</tr>
				<tr>
					<td>调整几率</td>
					<td colspan="3">
						<input name="bmiCheckItemConfigFormMap.rout" placeholder="调整记录(0-100)" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="">%
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>