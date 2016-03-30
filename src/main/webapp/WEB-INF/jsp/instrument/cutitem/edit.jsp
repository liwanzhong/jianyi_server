<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {

		$('#userEditForm').form({
			url : '${ctx}/instrument/cut_item/update.shtml',
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
					parent.$.messager.alert('提醒', result.msg, 'error');
				}
			}
		});
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="userEditForm" method="post">
			<input type="hidden" name="cutItemFormMap.id" value="${cutItemFormMap.id}">
			<table class="grid">
				<tr>
					<td>切割项名称</td>
					<td colspan="3">
						<input name="cutItemFormMap.name" style="width: 230px" type="text" placeholder="切割项名称" class="easyui-validatebox" data-options="required:true" value="${cutItemFormMap.name}">
					</td>
				</tr>
				<tr>
					<td>排序</td>
					<td colspan="3">
						<input name="cutItemFormMap.order_by"   class="easyui-numberspinner" style="width: 230px;" required="required" data-options="editable:false" value="${cutItemFormMap.order_by}">
					</td>
				</tr>
				<tr>
					<td>备注</td>
					<td colspan="3">
						<textarea name="cutItemFormMap.remark"  placeholder="备注说明信息" class="easyui-validatebox"  cols="50" rows="5">${cutItemFormMap.remark}</textarea>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>