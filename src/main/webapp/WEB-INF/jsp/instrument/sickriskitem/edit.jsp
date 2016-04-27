<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {

		$('#orginid').combotree({
			url : '${ctx}${ctx}/instrument/pingfen_leve/tree.shtml',
			parentField : 'pid',
			lines : true,
			panelHeight : 'auto',
			value : '${cfPingfenRoutFormMap.orgin_pingfen_id}'
		});

		$('#tzid').combotree({
			url : '${ctx}/instrument/pingfen_leve/tree.shtml',
			parentField : 'pid',
			lines : true,
			panelHeight : 'auto',
			value : '${cfPingfenRoutFormMap.tz_pingfen_id}'
		});

		$('#userEditForm').form({
			url : '${ctx}/instrument/pingfen_rout/update.shtml',
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
			<input type="hidden" value="${cfPingfenRoutFormMap.id}" name="cfPingfenRoutFormMap.id">
			<input type="hidden" value="${cfPingfenRoutFormMap.small_id}" name="cfPingfenRoutFormMap.small_id">
			<table class="grid">
				<tr>
					<td>年龄范围</td>
					<td colspan="3">
						<input name="cfPingfenRoutFormMap.age_min" type="text" placeholder="年龄范围值" class="easyui-validatebox" data-options="required:true" value="${cfPingfenRoutFormMap.age_min}"> 至
						<input name="cfPingfenRoutFormMap.age_max" type="text" placeholder="年龄范围值" class="easyui-validatebox" data-options="required:true" value="${cfPingfenRoutFormMap.age_max}">
					</td>
				</tr>
				<tr>
					<td>原评分范围</td>
					<td colspan="3">
						<select id="orginid" name="cfPingfenRoutFormMap.orgin_pingfen_id" style="width:200px;height: 29px;" data-options="required:true"></select>
					</td>
				</tr>
				<tr>
					<td>调整后评分范围</td>
					<td colspan="3">
						<select id="tzid" name="cfPingfenRoutFormMap.tz_pingfen_id" style="width:200px;height: 29px;" data-options="required:true"></select>
					</td>
				</tr>
				<tr>
					<td>评分调整概率</td>
					<td colspan="3">
						<input name="cfPingfenRoutFormMap.rout_min" type="text" placeholder="调整概率" class="easyui-validatebox" data-options="required:true" value="${cfPingfenRoutFormMap.rout_min}"> 至
						<input name="cfPingfenRoutFormMap.rout_max" type="text" placeholder="调整概率" class="easyui-validatebox" data-options="required:true" value="${cfPingfenRoutFormMap.rout_max}">
					</td>
				</tr>
				<tr>
					<td>是否有效</td>
					<td colspan="3">
						<select name="cfPingfenRoutFormMap.valid"  style="width:200px;height: 29px;">
							<option selected>1</option>
							<option>0</option>
						</select>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>