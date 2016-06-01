<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		$('#org_leve_id').combotree({
			url : '${ctx}${ctx}/instrument/pingfen_leve/tree.shtml',
			parentField : 'pid',
			lines : true,
			panelHeight : 'auto',
			value : '${bmiCheckItemConfigFormMap.org_leve_id}'
		});

		$('#tz_leve_id').combotree({
			url : '${ctx}/instrument/pingfen_leve/tree.shtml',
			parentField : 'pid',
			lines : true,
			panelHeight : 'auto',
			value : '${bmiCheckItemConfigFormMap.tz_leve_id}'
		});

		$('#userEditForm').form({
			url : '${ctx}/instrument/BmiCheckItemConfig/update.shtml',
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
			<input type="hidden" value="${bmiCheckItemConfigFormMap.check_item_id}" name="bmiCheckItemConfigFormMap.check_item_id">
			<input type="hidden" value="${bmiCheckItemConfigFormMap.check_type}" name="bmiCheckItemConfigFormMap.check_type">
			<input type="hidden" value="${bmiCheckItemConfigFormMap.id}" name="bmiCheckItemConfigFormMap.id">
			<table class="grid">
				<tr>
					<td>BMI范围</td>
					<td colspan="3">
						从<input name="bmiCheckItemConfigFormMap.bmi_min" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="${bmiCheckItemConfigFormMap.bmi_min}">
						到<input name="bmiCheckItemConfigFormMap.bmi_max" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="${bmiCheckItemConfigFormMap.bmi_max}">
					</td>
				</tr>
				<tr>
					<td>年龄范围</td>
					<td colspan="3">
						从<input name="bmiCheckItemConfigFormMap.age_min" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="${bmiCheckItemConfigFormMap.age_min}">
						到<input name="bmiCheckItemConfigFormMap.age_max" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="${bmiCheckItemConfigFormMap.age_max}">
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
						<input name="bmiCheckItemConfigFormMap.rout" placeholder="调整记录(0-100)" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="${bmiCheckItemConfigFormMap.rout}">%
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>