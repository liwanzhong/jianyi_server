<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {


		$('#userEditForm').form({
			url : '${ctx}/instrument/zongpingLeveDescConfig/update.shtml',
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
			<input type="hidden" value="${zongpingLeveDescConfigFormMap.id}" name="zongpingLeveDescConfigFormMap.id">
			<table class="grid">
				<tr>
					<td>等级名称</td>
					<td colspan="3">
						<input name="zongpingLeveDescConfigFormMap.leve_name" type="text" placeholder="等级名称" style="width:250px;height: 29px;" class="easyui-validatebox" data-options="required:true" value="${zongpingLeveDescConfigFormMap.leve_name}">
					</td>
				</tr>
				<tr>
					<td>最小得分</td>
					<td colspan="3">
						<input name="zongpingLeveDescConfigFormMap.score_min" type="text" placeholder="最小得分" style="width:250px;height: 29px;" class="easyui-validatebox" data-options="required:true" value="${zongpingLeveDescConfigFormMap.score_min}">
					</td>
				</tr>
				<tr>
					<td>最大得分</td>
					<td colspan="3">
						<input name="zongpingLeveDescConfigFormMap.score_max" type="text" placeholder="最大得分" style="width:250px;height: 29px;" class="easyui-validatebox" data-options="required:true" value="${zongpingLeveDescConfigFormMap.score_max}">
					</td>
				</tr>
				<tr>
					<td>等级文字说明</td>
					<td colspan="3">
						<textarea rows="5" cols="80" name="zongpingLeveDescConfigFormMap.leve_des" class="easyui-validatebox" data-options="required:true">${zongpingLeveDescConfigFormMap.leve_des}</textarea>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>