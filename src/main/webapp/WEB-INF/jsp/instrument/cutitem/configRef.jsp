<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script src="${ctx}/js/components/easyui1.4.2/jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">


	$(function() {
		$('#roleGrantForm').form({
			url : '${ctx}/instrument/cut_item/config.shtml',
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
<div id="roleGrantLayout" class="easyui-layout" data-options="fit:true,border:false" style="width: 100%">
	切割项：<span style="color: red">《${cutItemFormMap.name}》</span>关联配置项
	<form id="roleGrantForm" method="post" action="${ctx}/instrument/cut_item/config.shtml">
		<input name="cutItemId" type="hidden"  value="${cutItemFormMap.id}" readonly="readonly">
		<table border="1" width="100%" align="center">
			<thead>
				<tr>
					<th>
						检测大项
					</th>
					<th>
						检测小项
					</th>
				</tr>
			</thead>
			<c:forEach items="${checkBigItemFormMapList}" var="bigItem">
				<tr>
					<td>
							${bigItem.name}
					</td>
					<td>
						<table>
							<c:forEach items="${checkSmallItemFormMapList}" var="smallItem">
								<c:if test="${smallItem.big_item_id == bigItem.id}">
									<tr>
										<td>
											<c:set var="isCheck" value="0"></c:set>
											<c:forEach items="${cutItemRefsmallitemConfigFormMapList}" var="checkItem">
												<c:if test="${checkItem.ref_checksmall_id == smallItem.id}">
													<c:set var="isCheck" value="1"></c:set>
												</c:if>
											</c:forEach>
											<c:choose>
												<c:when test="${isCheck == 1}">
													<input type="checkbox" value="${smallItem.id}" name="refItem" checked>${smallItem.name}
												</c:when>
												<c:otherwise>
													<input type="checkbox" value="${smallItem.id}" name="refItem">${smallItem.name}
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
								</c:if>
							</c:forEach>
						</table>
					</td>
				</tr>

			</c:forEach>

		</table>
		<input type="button" onclick="saveConfig()" value="保存配置">
	</form>
	<script>
		function saveConfig(){
			$.ajax({
				type: "POST",
				dataType: "JSON",
				url: '${ctx}/instrument/cut_item/config.shtml',
				data: $('#roleGrantForm').serialize(),
				success: function (result) {
					alert("保存配置成功!");
				},
				error: function(data) {
					alert("error:"+data.responseText);
				}
			});

		}
	</script>

</div>