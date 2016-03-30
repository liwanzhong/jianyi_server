<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">


	$(function() {

		$('#organizationId').combotree({
			url : '${ctx}/organization/tree.shtml',
			parentField : 'pid',
			lines : true,
			panelHeight : 'auto'
		});

		$('#userAddForm').form({
			url : '${ctx}/instrument/equipment/add.shtml',
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
					<td>机器码</td>
					<td colspan="3">
						<input name="equipmentFormMap.istmt_code" style="width: 230px" type="text" placeholder="机器码" class="easyui-validatebox" data-options="required:true" value="${istmt_code}" readonly>
					</td>
				</tr>
				<tr>
					<td>所属检测点</td>
					<td colspan="3">
						<select name="equipmentFormMap.organization_id" id="organizationId" style="width: 230px;" data-options="required:true"></select>
					</td>
				</tr>
				<tr>
					<td>备注</td>
					<td colspan="3">
						<textarea name="equipmentFormMap.remark"  placeholder="备注说明信息" class="easyui-validatebox"  cols="50" rows="5"></textarea>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>