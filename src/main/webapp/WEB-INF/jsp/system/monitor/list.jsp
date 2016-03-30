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
	<title>告警列表</title>
	<script type="text/javascript">
		var dataGrid;
		$(function() {
			dataGrid = $('#dataGrid').datagrid({
				url : '${ctx}/monitor/dataGrid.shtml',
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
					title : 'cpu使用率',
					field : 'cpuUsage'
				}, {
					width : '150',
					title : '预设cpu使用率',
					field : 'setCpuUsage',
					sortable:true
				},{
					width : '180',
					title : 'Jvm使用率',
					field : 'jvmUsage'
				}, {
					width : '150',
					title : '预设Jvm使用率',
					field : 'setJvmUsage',
					sortable : true
				}, {
					width : '150',
					title : 'Ram使用率',
					field : 'ramUsage'
				}, {
					width : '150',
					title : '预设Ram使用率',
					field : 'setRamUsage'

				}, {
					width : '150',
					title : '发送的邮件',
					field : 'email'
				}, {
					width : '150',
					title : '发送的时间',
					field : 'operTime',
					sortable : true,
					formatter : function(value, row, index) {
						return (new Date(parseFloat(value))).format("yyyy-MM-dd hh:mm:ss");
					}
				}, {
					width : '150',
					title : '备注',
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
<div data-options="region:'center',border:true,title:'告警列表'" >
	<table id="dataGrid" data-options="fit:true,border:false"></table>
</div>

</body>
</html>