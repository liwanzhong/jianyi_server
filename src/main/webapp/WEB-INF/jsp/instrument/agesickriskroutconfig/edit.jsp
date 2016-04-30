<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {



		

		$('#userEditForm').form({
			url : '${ctx}/instrument/ageRiskRoutConfig/update.shtml',
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
			<input type="hidden" value="${ageSickRiskRoutFormMap.sick_risk_id}" name="ageSickRiskRoutFormMap.sick_risk_id">
			<input type="hidden" value="${ageSickRiskRoutFormMap.id}" name="ageSickRiskRoutFormMap.id">
			<table class="grid">
				<tr>
					<td>最小年龄</td>
					<td colspan="3">
						<input name="ageSickRiskRoutFormMap.age_min" type="text" placeholder="最小年龄" style="width:200px;height: 29px;" class="easyui-validatebox" data-options="required:true" value="${ageSickRiskRoutFormMap.age_min}">
					</td>
				</tr>
				<tr>
					<td>最大年龄</td>
					<td colspan="3">
						<input name="ageSickRiskRoutFormMap.age_max" type="text" placeholder="最大年龄" style="width:200px;height: 29px;" class="easyui-validatebox" data-options="required:true" value="${ageSickRiskRoutFormMap.age_max}">
					</td>
				</tr>
				<tr>
					<td>性别</td>
					<td colspan="3">
						<c:choose>
							<c:when test="${ageSickRiskRoutFormMap.sex eq '1'}">
								<input name="ageSickRiskRoutFormMap.sex" type="radio" class="easyui-validatebox" data-options="required:true" value="1" checked>男
								<input name="ageSickRiskRoutFormMap.sex" type="radio" class="easyui-validatebox" data-options="required:true" value="2">女
							</c:when>
							<c:when test="${ageSickRiskRoutFormMap.sex eq '2'}">
								<input name="ageSickRiskRoutFormMap.sex" type="radio" class="easyui-validatebox" data-options="required:true" value="1" >男
								<input name="ageSickRiskRoutFormMap.sex" type="radio" class="easyui-validatebox" data-options="required:true" value="2" checked>女
							</c:when>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td>疾病风险率</td>
					<td colspan="3">
						<input name="ageSickRiskRoutFormMap.rout" type="text" placeholder="疾病风险率" style="width:200px;height: 29px;" class="easyui-validatebox" data-options="required:true" value="${ageSickRiskRoutFormMap.rout}">&nbsp;&nbsp;&nbsp;%
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>