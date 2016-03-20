<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		
		$('#roleEditForm').form({
			url : '${ctx}/role/update.shtml',
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
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
		
		
		<%--$("#description").val('${role.description}');--%>
		
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="roleEditForm" method="post">
			<input type="hidden" name="roleFormMap.id" value="${roleFormMap.id}">
			<table class="grid">
				<tr>
					<td>角色名称</td>
					<td><input name="roleFormMap.name"  type="text" placeholder="请输入角色名称" class="easyui-validatebox span2" data-options="required:true" value="${roleFormMap.name}"></td>
				</tr>
				<tr>
					<td>权限码</td>
					<td><input name="roleFormMap.roleKey" type="text" placeholder="请输入权限码" class="easyui-validatebox span2" data-options="required:true" value="${roleFormMap.roleKey}"></td>
				</tr>
				<tr>
					<td>状态</td>
					<td>

						<select name="roleFormMap.state" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'" >
							<c:choose>
								<c:when test="${roleFormMap.state == '0'}">
									<option value="1">无效</option>
									<option value="0" selected="selected">有效</option>
								</c:when>
								<c:otherwise>
									<option value="1" selected>无效</option>
									<option value="0">有效</option>
								</c:otherwise>
							</c:choose>

						</select>
					</td>
				</tr>
				<tr>
					<td>备注</td>
					<td colspan="3"><textarea name="roleFormMap.description" rows="" cols="50" >${roleFormMap.description}</textarea></td>
				</tr>
			</table>
		</form>
	</div>
</div>