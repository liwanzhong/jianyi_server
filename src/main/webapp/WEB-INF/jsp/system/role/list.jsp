<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/inc.jsp"></jsp:include>
	<meta http-equiv="X-UA-Compatible" content="edge" />
	<c:if test="${fn:contains(sessionScope.RESOURCES_SESSION_KEY, '/role/edit.shtml')}">
		<script type="text/javascript">
			$.canEdit = true;
		</script>
	</c:if>
	<c:if test="${fn:contains(sessionScope.RESOURCES_SESSION_KEY, '/role/delete.shtml')}">
		<script type="text/javascript">
		</script>
	</c:if>
	<c:if test="${fn:contains(sessionScope.RESOURCES_SESSION_KEY, '/role/grant.shtml')}">
		<script type="text/javascript">
			$.canGrant = true;
		</script>
	</c:if>
	<title>角色管理</title>
	<script type="text/javascript">
		var dataGrid;
		$(function() {
			dataGrid = $('#dataGrid').datagrid({
				url : '${ctx}' + '/role/dataGrid.shtml',
				striped : true,
				rownumbers : true,
				pagination : true,
				singleSelect : true,
				idField : 'id',
				sortName : 'id',
				sortOrder : 'asc',
				pageSize : 50,
				pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
				frozenColumns : [ [ {
					width : '100',
					title : 'id',
					field : 'id',
					sortable : true,
					hidden:true
				}, {
					width : '120',
					title : '权限名称',
					field : 'name',
					sortable : true
				} , {
					width : '100',
					title : '状态',
					field : 'state',
					sortable : true,
					formatter : function(value, row, index) {
						if(value == 1){
							return '无效';
						}
						if(value == 0){
							return '有效';
						}
					}
				}, {
					width : '120',
					title : '权限码',
					field : 'roleKey'
				}, {
					width : '150',
					title : '描述',
					field : 'description'
				} , {
					field : 'action',
					title : '操作',
					width : 120,
					formatter : function(value, row, index) {
						var str = '&nbsp;';
						str += $.formatString('<a href="javascript:void(0)" onclick="grantFun(\'{0}\');" >授权</a>', row.id);
						str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
						str += $.formatString('<a href="javascript:void(0)" onclick="editFun(\'{0}\');" >编辑</a>', row.id);
						str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
						str += $.formatString('<a href="javascript:void(0)" onclick="deleteFun(\'{0}\');" >删除</a>', row.id);
						return str;
					}
				} ] ],
				toolbar : '#toolbar'
			});
		});

		function addFun() {
			parent.$.modalDialog({
				title : '添加',
				width : 500,
				height : 300,
				href : '${ctx}/role/addPage.shtml',
				buttons : [ {
					text : '添加',
					handler : function() {
						parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
						var f = parent.$.modalDialog.handler.find('#roleAddForm');
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
			parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
				if (b) {
					progressLoad();
					$.post('${ctx}/role/delete.shtml', {
						'roleFormMap.id' : id
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
				width : 500,
				height : 300,
				href : '${ctx}/role/editPage.shtml?id=' + id,
				buttons : [ {
					text : '编辑',
					handler : function() {
						parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
						var f = parent.$.modalDialog.handler.find('#roleEditForm');
						f.submit();
					}
				} ]
			});
		}

		function grantFun(id) {
			if (id == undefined) {
				var rows = dataGrid.datagrid('getSelections');
				id = rows[0].id;
			} else {
				dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
			}

			parent.$.modalDialog({
				title : '授权',
				width : 500,
				height : 500,
				href : '${ctx}/role/grantPage.shtml?roleFormMap.id=' + id,
				buttons : [ {
					text : '授权',
					handler : function() {
						parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
						var f = parent.$.modalDialog.handler.find('#roleGrantForm');
						f.submit();
					}
				} ]
			});
		}

	</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',fit:true,border:false">
	<table id="dataGrid" data-options="fit:true,border:false"></table>
</div>
<div id="toolbar" style="display: none;">
	<%--<c:if test="${fn:contains(sessionScope.RESOURCES_SESSION_KEY, '/role/add')}">
        <a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
    </c:if>--%>
	<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
</div>
</body>
</html>