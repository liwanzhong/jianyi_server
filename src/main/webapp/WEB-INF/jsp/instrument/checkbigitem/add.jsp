<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">


	$(function() {
		$('#userAddForm').form({
			url : '${ctx}/instrument/bigitem/add.shtml',
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
					<td>大项名称</td>
					<td colspan="3"><input name="checkBigItemFormMap.name" type="text" placeholder="请输入大项名称" class="easyui-validatebox" data-options="required:true" value=""></td>
				</tr>
				<tr>
					<td>权重系数</td>
					<td colspan="3"><input name="checkBigItemFormMap.quanzhong" type="text" placeholder="请输入权重系数" class="easyui-validatebox" data-options="required:true" value=""></td>
				</tr>
				<tr>
					<td>性别区分</td>
					<td colspan="3">
						<input name="checkBigItemFormMap.withsex" type="radio" class="easyui-validatebox" data-options="required:true" checked value="0">无
						<input name="checkBigItemFormMap.withsex" type="radio" class="easyui-validatebox" data-options="required:true" value="1">男
						<input name="checkBigItemFormMap.withsex" type="radio" class="easyui-validatebox" data-options="required:true" value="2">女
					</td>
				</tr>
				<tr>
					<td>易发年龄</td>
					<td colspan="3">
						<input name="checkBigItemFormMap.normal_age" type="text" placeholder="请输入易发年龄" >
					</td>
				</tr>
				<tr>
					<td>显示异常项提醒</td>
					<td>
						<input type="radio" name="checkBigItemFormMap.show_exc_tips" checked value="1"> 是
						<input type="radio" name="checkBigItemFormMap.show_exc_tips" value="0"> 否
					</td>
					<td colspan="2">
						<input name="checkBigItemFormMap.exc_tips_count" type="text" placeholder=""> 项
					</td>
				</tr>
				<tr>
					<td>是否年龄控制</td>
					<td >
						<input type="radio" name="checkBigItemFormMap.controller_age" checked value="1"> 是
						<input type="radio" name="checkBigItemFormMap.controller_age" value="0"> 否
					</td>
					<td colspan="2">
						<input type="text" name="checkBigItemFormMap.age_min"> 岁  至<input type="text" name="checkBigItemFormMap.age_max">岁
					</td>
				</tr>
				<tr>
					<td>排序</td>
					<td colspan="3">
						<input type="text" name="checkBigItemFormMap.order_by" value="0">
					</td>
				</tr>
				<tr>
					<td>图表类型</td>
					<td colspan="3">
						<%--<input type="text" name="checkBigItemFormMap.order_by" value="0">--%>
						<select name="checkBigItemFormMap.charts_item">
							<option value="1">柱状图</option>
							<option value="2">书页图表</option>
							<option value="3">折点图</option>
							<option value="4">六边形图表</option>
							<option value="5">凹型柱状图</option>
							<option value="6">箭头柱状图</option>
							<option value="7">折线图</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>检测意义</td>
					<td colspan="3">
						<textarea cols="50" rows="5" name="checkBigItemFormMap.tips_content"></textarea>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>