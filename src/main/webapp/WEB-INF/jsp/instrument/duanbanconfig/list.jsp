<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/inc.jsp"></jsp:include>
	<meta http-equiv="X-UA-Compatible" content="edge" />
	<c:if test="${fn:contains(sessionScope.RESOURCES_SESSION_KEY, '/instrument/duanbanconfig/edit.shtml')}">
		<script type="text/javascript">
			$.canEdit = true;
		</script>
	</c:if>
	<c:if test="${fn:contains(sessionScope.RESOURCES_SESSION_KEY, '/instrument/duanbanconfig/delete.shtml')}">
		<script type="text/javascript">
		</script>
	</c:if>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>检测项管理</title>
	<script type="text/javascript">
		var dataGrid;
		$(function() {
			dataGrid = $('#dataGrid').datagrid({
				url : '${ctx}/instrument/duanbanconfig/dataGrid.shtml?duanbanConfigFormMap.big_item_id=${checkItemId}',
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
					width : '180',
					title : 'id',
					field : 'id',
					hidden:true
				},{
					width : '180',
					title : '短板等级',
					field : 'duanban_leve'
				},{
					width : '180',
					title : '分数调整',
					field : 'tz_rout',
					formatter : function(value, row, index) {
						return value+'分';
					}
				},{
					width : '180',
					title : '短板调整等级项',
					field : 'duanban_item_tz_count',
					formatter : function(value, row, index) {
						return value+'项以上';
					}
				},{
					width : '180',
					title : '检测大项',
					field : 'check_big_item'
				},{
					field : 'action',
					title : '操作',
					width : 350,
					formatter : function(value, row, index) {
						var str = '';
						str += $.formatString('<a href="javascript:void(0)" onclick="editFun(\'{0}\');" >编辑</a>', row.id);
						str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
						str += $.formatString('<a href="javascript:void(0)" onclick="deleteFun(\'{0}\');" >删除</a>', row.id);
						return str;
					}
				}] ],
				toolbar : '#toolbar'
			});
		});







		function addFun() {
			parent.$.modalDialog({
				title : '添加',
				width : '50%',
				height : '40%',
				href : '${ctx}/instrument/duanbanconfig/addPage.shtml?checkItemId=${checkItemId}',
				buttons : [ {
					text : '添加',
					handler : function() {
						parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
						var f = parent.$.modalDialog.handler.find('#userAddForm');
						f.submit();
					}
				} ]
			});
		}

		function deleteFun(id) {
			if (id == undefined) {//点击右键菜单才会触发这个
				var rows = dataGrid.datagrid('getSelections');
				id = rows[0].id;
			} else {//点击操作里面的删除图标会触发这个
				dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
			}
			parent.$.messager.confirm('询问', '是否需要删除当前项？', function(b) {
				if (b) {
					progressLoad();
					$.post('${ctx}/instrument/duanbanconfig/delete.shtml', {
						'duanbanConfigFormMap.id' : id
					}, function(result) {
						if (result.status == 1) {
							parent.$.messager.alert('提示', result.msg, 'info');
							dataGrid.datagrid('reload');
						}
						progressClose();
					}, 'JSON');
				}
			});
		}




		function editFun(id) {
			if (id == undefined) {
				var rows = dataGrid.datagrid('getSelections');
				id = rows[0].id;
			} else {
				dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
			}
			parent.$.modalDialog({
				title : '编辑',
				width : '50%',
				height : '40%',
				href : '${ctx}/instrument/duanbanconfig/editPage.shtml?id=' + id,
				buttons : [ {
					text : '编辑',
					handler : function() {
						parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
						var f = parent.$.modalDialog.handler.find('#userEditForm');
						f.submit();
					}
				} ]
			});
		}

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
<div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
</div>
<div data-options="region:'center',border:true,title:'短板配置(${checkBigItemFormMap.name})'" >
	<table id="dataGrid" data-options="fit:true,border:false"></table>
</div>
<div id="toolbar" style="display: none;">
	<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
</div>
</body>
</html>