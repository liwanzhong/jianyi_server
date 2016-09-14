<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">


	$(function() {

		$('#userAddForm').form({
			url : '${ctx}/instrument/leveZhanbi/add.shtml',
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

	function procentCount() {
		var procent = null;
		for(var i=1;i<=5;i++){
			var itemValue = $("input [name = 'checkItemDegrLeveZhanbiFormMap.leve_"+i+"_procent']").val();
			procent += itemValue==null||itemValue=='undefind'?0:itemValue;
		}
		$("#countProcent").html("等级百分比总计:"+procent/100);
	}

</script>

<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="userAddForm" method="post">
			<input type="hidden" value="${checkItemId}" name="checkItemDegrLeveZhanbiFormMap.check_id">
			<input type="hidden" value="${checkItemType}" name="checkItemDegrLeveZhanbiFormMap.check_type">
			<table class="grid">
				<tr>
					<td>BMI范围</td>
					<td colspan="3">
						从<input name="checkItemDegrLeveZhanbiFormMap.bmi_min" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="">
						到<input name="checkItemDegrLeveZhanbiFormMap.bmi_max" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="">
					</td>
				</tr>
				<tr>
					<td>年龄范围</td>
					<td colspan="3">
						从<input name="checkItemDegrLeveZhanbiFormMap.age_min" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="">
						到<input name="checkItemDegrLeveZhanbiFormMap.age_max" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="">
					</td>
				</tr>
				<tr>
					<td>身高范围</td>
					<td colspan="3">
						从<input name="checkItemDegrLeveZhanbiFormMap.bodyheight_min" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="">
						到<input name="checkItemDegrLeveZhanbiFormMap.bodyheight_max" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="">
					</td>
				</tr>
				<tr>
					<td>体重范围</td>
					<td colspan="3">
						从<input name="checkItemDegrLeveZhanbiFormMap.weight_min" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="">
						到<input name="checkItemDegrLeveZhanbiFormMap.weight_max" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="">
					</td>
				</tr>
				<tr>
					<td>正常占比</td>
					<td colspan="3">
						<input name="checkItemDegrLeveZhanbiFormMap.leve_1_procent" onchange="procentCount()" placeholder="占比(0-100)" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="">%
					</td>
				</tr>
				<tr>
					<td>未见异常占比</td>
					<td colspan="3">
						<input name="checkItemDegrLeveZhanbiFormMap.leve_2_procent" onchange="procentCount()" placeholder="占比(0-100)" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="">%
					</td>
				</tr>
				<tr>
					<td>减弱一级占比</td>
					<td colspan="3">
						<input name="checkItemDegrLeveZhanbiFormMap.leve_3_procent" onchange="procentCount()" placeholder="占比(0-100)" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="">%
					</td>
				</tr>
				<tr>
					<td>减弱二级占比</td>
					<td colspan="3">
						<input name="checkItemDegrLeveZhanbiFormMap.leve_4_procent" onchange="procentCount()" placeholder="占比(0-100)" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="">%
					</td>
				</tr>
				<tr>
					<td>减弱三级占比</td>
					<td colspan="3">
						<input name="checkItemDegrLeveZhanbiFormMap.leve_5_procent" onchange="procentCount()" placeholder="占比(0-100)" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="">%
					</td>
				</tr>
			</table>
			<span id="countProcent"></span>
		</form>
	</div>
</div>