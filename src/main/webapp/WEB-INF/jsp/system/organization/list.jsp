<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/inc.jsp"></jsp:include>
	<meta http-equiv="X-UA-Compatible" content="edge" />
	<c:if test="${fn:contains(sessionScope.RESOURCES_SESSION_KEY, '/organization/edit')}">
		<script type="text/javascript">
			$.canEdit = true;
		</script>
	</c:if>
	<c:if test="${fn:contains(sessionScope.RESOURCES_SESSION_KEY, '/organization/delete')}">
		<script type="text/javascript">
		</script>
	</c:if>
	<title>机构管理</title>
	<script type="text/javascript">
		var treeGrid;
		$(function() {
			treeGrid = $('#treeGrid').treegrid({
				url : '${ctx}/organization/treeGrid.shtml',
				idField : 'id',
				treeField : 'name',
				parentField : 'pid',
				fit : true,
				fitColumns : false,
				border : false,
				frozenColumns : [ [ {
					title : 'id',
					field : 'id',
					width : 40,
					hidden : true
				} ] ],
				columns : [ [ {
					field : 'code',
					title : '编号',
					width : 180
				},{
					field : 'name',
					title : '组织名称',
					width : 180
				}, {
					field : 'seq',
					title : '排序',
					width : 40
				}, {
					field : 'icon',
					title : '图标',
					width : 80
				},  {
					width : '120',
					title : '创建时间',
					field : 'createdatetime',
					formatter : function(value, row, index) {
						return (new Date(parseFloat(value))).format("yyyy-MM-dd hh:mm:ss");
					}
				},{
					field : 'pid',
					title : '上级资源ID',
					width : 150,
					hidden : true
				}, {
					field : 'address',
					title : '地址',
					width : 200
				} , {
					field : 'action',
					title : '操作',
					width : 120,
					formatter : function(value, row, index) {
						var str = '&nbsp;';
						if ($.canEdit) {
							str += $.formatString('<a href="javascript:void(0)" onclick="editFun(\'{0}\');" >编辑</a>', row.id);
						}
						if ($.canDelete) {
							str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
							str += $.formatString('<a href="javascript:void(0)" onclick="deleteFun(\'{0}\');" >删除</a>', row.id);
						}
						return str;
					}
				} ] ],
				toolbar : '#toolbar'
			});
		});

		function editFun(id) {
			if (id != undefined) {
					treeGrid.treegrid('select', id);
				}
				var node = treeGrid.treegrid('getSelected');
				if (node) {
					parent.$.modalDialog({
						title : '编辑',
						width : 500,
						height : 300,
						href : '${ctx}/organization/editPage.shtml?id=' + node.id,
					buttons : [ {
						text : '编辑',
						handler : function() {
							parent.$.modalDialog.openner_treeGrid = treeGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
							var f = parent.$.modalDialog.handler.find('#organizationEditForm');
							f.submit();
						}
					} ]
				});
			}
		}

		function deleteFun(id) {
			if (id != undefined) {
				treeGrid.treegrid('select', id);
			}
			var node = treeGrid.treegrid('getSelected');
			if (node) {
				parent.$.messager.confirm('询问', '您是否要删除当前资源？删除当前资源会连同子资源一起删除!', function(b) {
					if (b) {
						progressLoad();
						$.post('${ctx}/organization/delete.shtml', {
							id : node.id
						}, function(result) {
							if (result.success) {
								parent.$.messager.alert('提示', result.msg, 'info');
								treeGrid.treegrid('reload');
							}else{
								parent.$.messager.alert('提示', result.msg, 'info');
							}
							progressClose();
						}, 'JSON');
					}
				});
			}
		}

		function addFun() {
			parent.$.modalDialog({
				title : '添加组织',
				width : 500,
				height : 300,
				href : '${ctx}/organization/addPage.shtml',
				buttons : [ {
					text : '添加组织',
					handler : function() {
						parent.$.modalDialog.openner_treeGrid = treeGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
						var f = parent.$.modalDialog.handler.find('#organizationAddForm');
						f.submit();
					}
				} ]
			});
		}
	</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false"  style="overflow: hidden;">
		<table id="treeGrid"></table>
	</div>

	<div id="toolbar" style="display: none;">
		<%--<c:if test="${fn:contains(sessionScope.RESOURCES_SESSION_KEY, '/organization/add')}">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
		</c:if>--%>
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
	</div>
</div>
</body>
</html>