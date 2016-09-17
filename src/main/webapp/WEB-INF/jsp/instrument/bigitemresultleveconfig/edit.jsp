<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		$('#duanban_leve_id').combotree({
			url : '${ctx}${ctx}/instrument/pingfen_leve/tree.shtml',
			parentField : 'pid',
			lines : true,
			panelHeight : 'auto',
			value : '${bigItemResultLeveConfigFormMap.org_leve_id}'
		});

		$('#userEditForm').form({
			url : '${ctx}/instrument/bigitemresultleveconfig/update.shtml',
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
			<input type="hidden" value="${bigItemResultLeveConfigFormMap.check_item_id}" name="bigItemResultLeveConfigFormMap.check_item_id">
			<input type="hidden" value="${bigItemResultLeveConfigFormMap.id}" name="bigItemResultLeveConfigFormMap.id">
			<input type="hidden" value="${bigItemResultLeveConfigFormMap.check_type}" name="bigItemResultLeveConfigFormMap.check_type">
			<table class="grid">
				<tr>
					<td>等级名称</td>
					<td colspan="3">
						<select id="duanban_leve_id" name="bigItemResultLeveConfigFormMap.org_leve_id" style="width:200px;height: 29px;" data-options="required:true"></select>
					</td>
				</tr>
				<tr>
					<td>年龄范围</td>
					<td colspan="3">
						<input name="bigItemResultLeveConfigFormMap.age_min" type="text" placeholder="最小年龄" style="width:250px;height: 29px;" class="easyui-validatebox" data-options="required:true" value="${bigItemResultLeveConfigFormMap.age_min}">到
						<input name="bigItemResultLeveConfigFormMap.age_max" type="text" placeholder="最大年龄" style="width:250px;height: 29px;" class="easyui-validatebox" data-options="required:true" value="${bigItemResultLeveConfigFormMap.age_max}">
					</td>
				</tr>
				<tr>
					<td>BMI范围</td>
					<td colspan="3">
						<input name="bigItemResultLeveConfigFormMap.bmi_min" type="text" placeholder="最小BMI" style="width:250px;height: 29px;" class="easyui-validatebox" data-options="required:true" value="${bigItemResultLeveConfigFormMap.bmi_min}">到
						<input name="bigItemResultLeveConfigFormMap.bmi_max" type="text" placeholder="最大BMI" style="width:250px;height: 29px;" class="easyui-validatebox" data-options="required:true" value="${bigItemResultLeveConfigFormMap.bmi_max}">
					</td>
				</tr>
				<tr>
					<td>加减分</td>
					<td colspan="3">
						<input name="bigItemResultLeveConfigFormMap.change_score" type="text" placeholder="加减分" style="width:250px;height: 29px;" class="easyui-validatebox" data-options="required:true" value="${bigItemResultLeveConfigFormMap.change_score}"><span style="color: red">加分（正数）,减分(负数)</span>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>