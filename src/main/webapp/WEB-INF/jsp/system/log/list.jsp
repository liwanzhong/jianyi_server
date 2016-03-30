<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/inc.jsp"></jsp:include>
	<meta http-equiv="X-UA-Compatible" content="edge" />

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>日志跟踪</title>
	<script type="text/javascript">
		var dataGrid;
		$(function() {
			dataGrid = $('#dataGrid').datagrid({
				url : '${ctx}/log/dataGrid.shtml',
				fit : true,
				striped : true,
				rownumbers : true,
				pagination : true,
				singleSelect : true,
				idField : 'id',
				sortName : 'id',
				sortOrder : 'desc',
				pageSize : 50,
				pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
				columns : [ [ {
					width : '120',
					title : '账号',
					field : 'accountName'
				}, {
					width : '150',
					title : '模块',
					field : 'module',
					sortable:true
				},{
					width : '180',
					title : '方法',
					field : 'methods'
				}, {
					width : '150',
					title : '调用用时（ms）',
					field : 'actionTime',
					sortable : true
				}, {
					width : '150',
					title : '用户IP',
					field : 'userIP'
				}, {
					width : '150',
					title : '操作时间',
					field : 'operTime',
					sortable : true,
					formatter : function(value, row, index) {
						return (new Date(parseFloat(value))).format("yyyy-MM-dd hh:mm:ss");
					}
				}, {
					width : '150',
					title : '操作描述',
					field : 'description'
				}] ]
			});
		});





		function searchFun() {
			dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
		}
		function cleanFun() {
			$('#searchForm input').val('');
			dataGrid.datagrid('load', {});
		}
	</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:true,title:'日志跟踪'" >
	<table id="dataGrid" data-options="fit:true,border:false"></table>
</div>

</body>
</html>