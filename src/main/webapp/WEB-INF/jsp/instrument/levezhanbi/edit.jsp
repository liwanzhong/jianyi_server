<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		

		$('#userEditForm').form({
			url : '${ctx}/instrument/leveZhanbi/update.shtml',
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

	function procentCount() {
		var procent = 0;
		for(var i=1;i<=5;i++){
			var itemValue = $("#leve_"+i+"_procent").val();
			console.log(itemValue);
			procent +=Number(itemValue==null||itemValue=='undefind'?0:itemValue);
		}
		$("#countProcent").html("等级百分比总计:"+procent/100);
	}
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="userEditForm" method="post">
			<input type="hidden" value="${checkItemDegrLeveZhanbiFormMap.check_id}" name="checkItemDegrLeveZhanbiFormMap.check_id">
			<input type="hidden" value="${checkItemDegrLeveZhanbiFormMap.check_type}" name="checkItemDegrLeveZhanbiFormMap.check_type">
			<input type="hidden" value="${checkItemDegrLeveZhanbiFormMap.id}" name="checkItemDegrLeveZhanbiFormMap.id">
			<table class="grid">
				<tr>
					<td>BMI范围</td>
					<td colspan="3">
						从<input name="checkItemDegrLeveZhanbiFormMap.bmi_min" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="${checkItemDegrLeveZhanbiFormMap.bmi_min}">
						到<input name="checkItemDegrLeveZhanbiFormMap.bmi_max" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="${checkItemDegrLeveZhanbiFormMap.bmi_max}">
					</td>
				</tr>
				<tr>
					<td>年龄范围</td>
					<td colspan="3">
						从<input name="checkItemDegrLeveZhanbiFormMap.age_min" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="${checkItemDegrLeveZhanbiFormMap.age_min}">
						到<input name="checkItemDegrLeveZhanbiFormMap.age_max" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="${checkItemDegrLeveZhanbiFormMap.age_max}">
					</td>
				</tr>
				<tr>
					<td>身高范围</td>
					<td colspan="3">
						从<input name="checkItemDegrLeveZhanbiFormMap.bodyheight_min" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="${checkItemDegrLeveZhanbiFormMap.bodyheight_min}">
						到<input name="checkItemDegrLeveZhanbiFormMap.bodyheight_max" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="${checkItemDegrLeveZhanbiFormMap.bodyheight_max}">
					</td>
				</tr>
				<tr>
					<td>体重范围</td>
					<td colspan="3">
						从<input name="checkItemDegrLeveZhanbiFormMap.weight_min" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="${checkItemDegrLeveZhanbiFormMap.weight_min}">
						到<input name="checkItemDegrLeveZhanbiFormMap.weight_max" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="${checkItemDegrLeveZhanbiFormMap.weight_max}">
					</td>
				</tr>
				<tr>
					<td>正常占比</td>
					<td colspan="3">
						<input name="checkItemDegrLeveZhanbiFormMap.leve_1_procent" id="leve_1_procent" onchange="procentCount()" placeholder="占比(0-100)" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="${checkItemDegrLeveZhanbiFormMap.leve_1_procent}">%
					</td>
				</tr>
				<tr>
					<td>未见异常占比</td>
					<td colspan="3">
						<input name="checkItemDegrLeveZhanbiFormMap.leve_2_procent" id="leve_2_procent" onchange="procentCount()" placeholder="占比(0-100)" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="${checkItemDegrLeveZhanbiFormMap.leve_2_procent}">%
					</td>
				</tr>
				<tr>
					<td>减弱一级占比</td>
					<td colspan="3">
						<input name="checkItemDegrLeveZhanbiFormMap.leve_3_procent" id="leve_3_procent" onchange="procentCount()" placeholder="占比(0-100)" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="${checkItemDegrLeveZhanbiFormMap.leve_3_procent}">%
					</td>
				</tr>
				<tr>
					<td>减弱二级占比</td>
					<td colspan="3">
						<input name="checkItemDegrLeveZhanbiFormMap.leve_4_procent" id="leve_4_procent" onchange="procentCount()" placeholder="占比(0-100)" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="${checkItemDegrLeveZhanbiFormMap.leve_4_procent}">%
					</td>
				</tr>
				<tr>
					<td>减弱三级占比</td>
					<td colspan="3">
						<input name="checkItemDegrLeveZhanbiFormMap.leve_5_procent" id="leve_5_procent" onchange="procentCount()" placeholder="占比(0-100)" type="text"  style="height: 29px;" class="easyui-validatebox" data-options="required:true" value="${checkItemDegrLeveZhanbiFormMap.leve_5_procent}">%
					</td>
				</tr>
			</table>
			<span id="countProcent"></span>
		</form>
	</div>
</div>