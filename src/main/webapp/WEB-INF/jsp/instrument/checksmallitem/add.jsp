<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">


	$(function() {
		$('#userAddForm').form({
			url : '${ctx}/instrument/smallitem/add.shtml',
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



	function inValueGen(){
		$("#in_value_Span").text(($("#max_value_Input").val() - $("#min_value_Input").val()).toFixed(3))
	}
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="userAddForm" method="post">
			<input type="hidden" value="${checkBigItemFormMap.id}" name="checkSmallItemFormMap.big_item_id">
			<table class="grid">
				<tr>
					<td>检测小项名称</td>
					<td colspan="3"><input name="checkSmallItemFormMap.name" type="text" placeholder="请输入检测小项名称" class="easyui-validatebox" data-options="required:true" value=""></td>
				</tr>
				<tr>
					<td rowspan="2">检测指标</td>
					<td>基准值（n1）</td>
					<td colspan="3"><input name="checkSmallItemFormMap.min_value" id="min_value_Input"  onblur="inValueGen()" type="text" placeholder="基准值（n1）" class="easyui-validatebox" data-options="required:true" value=""></td>
				</tr>
				<tr>
					<td>衰退值（n2）</td>
					<td colspan="3"><input name="checkSmallItemFormMap.max_value" id="max_value_Input" onblur="inValueGen()" type="text" placeholder="衰退值（n2）" class="easyui-validatebox" data-options="required:true" value=""></td>
				</tr>
				<tr>
					<td>区间值</td>
					<td colspan="3">
					<span style="color: red" id="in_value_Span">6.45</span>	 区间值（n）：n=n2-n1
					</td>
				</tr>
				<tr>
					<td>实际检测值范围</td>
					<td >
						<input name="checkSmallItemFormMap.check_min" type="text" placeholder="检测范围最小值" value="">
					</td>
					<td >
						至
					</td>
					<td >
						<input name="checkSmallItemFormMap.check_max" type="text" placeholder="检测范围最大值" value="">
					</td>
				</tr>
				<tr>
					<td>权重系数</td>
					<td >
						<input type="text" name="checkSmallItemFormMap.quanzhong" value="0">
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>