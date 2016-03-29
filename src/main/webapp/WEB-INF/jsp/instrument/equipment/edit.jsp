<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
	

		$('#userEditForm').form({
			url : '${ctx}/instrument/equipment/update.shtml',
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
			<input type="hidden" name="cfPingfenLeveFormMap.id" value="${cfPingfenLeveFormMap.id}">
			<table class="grid">
				<tr>
					<td>等级名称</td>
					<td colspan="3"><input name="cfPingfenLeveFormMap.name" type="text" placeholder="等级名称" class="easyui-validatebox" data-options="required:true" value="${cfPingfenLeveFormMap.name}"></td>
				</tr>
				<tr>
					<td>分数范围</td>
					<td colspan="3">
						<input name="cfPingfenLeveFormMap.pingfen_min" type="text" class="easyui-validatebox" data-options="required:true" checked value="${cfPingfenLeveFormMap.pingfen_min}">  分   至
						<input name="cfPingfenLeveFormMap.pingfen_max" type="text" class="easyui-validatebox" data-options="required:true" value="${cfPingfenLeveFormMap.pingfen_max}">
					</td>
				</tr>
				<tr>
					<td>展现图标</td>
					<td colspan="3">
						<input name="cfPingfenLeveFormMap.show_tag_path" type="text" placeholder="展现图标" class="easyui-validatebox" data-options="required:true" value="${cfPingfenLeveFormMap.show_tag_path}" >
					</td>
				</tr>
				<tr>
					<td>展现颜色</td>
					<td colspan="3">
						<input type="text" name="cfPingfenLeveFormMap.show_color" placeholder="展现颜色"  data-options="required:true" class="easyui-validatebox" value="${cfPingfenLeveFormMap.show_color}">
						例如：#ff6600
					</td>
				</tr>

			</table>
		</form>
	</div>
</div>