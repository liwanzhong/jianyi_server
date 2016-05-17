<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/inc.jsp"></jsp:include>
	<meta http-equiv="X-UA-Compatible" content="edge" />
	<c:if test="${fn:contains(sessionScope.RESOURCES_SESSION_KEY, '/instrument/checkValueScoreIn/edit.shtml')}">
		<script type="text/javascript">
			$.canEdit = true;
		</script>
	</c:if>
	<c:if test="${fn:contains(sessionScope.RESOURCES_SESSION_KEY, '/instrument/checkValueScoreIn/delete.shtml')}">
		<script type="text/javascript">
		</script>
	</c:if>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>检测项管理</title>
	<script type="text/javascript">
		var dataGrid;
		$(function() {
			dataGrid = $('#dataGrid').datagrid({
				url : '${ctx}/instrument/checkValueScoreIn/dataGrid.shtml?checkValueScoreInFormMap.check_small_item='+${checkValueScoreInFormMap.check_small_item},
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
					title : '年龄范围',
					field : 'age_min'
				}, {
					width : '150',
					title : '年龄范围',
					field : 'age_max'
				},{
					width : '150',
					title : '原评分范围',
					field : 'orgin_pingfen_leve_name',
					formatter : function(value, row, index) {
						return value+'('+row.orgin_pingfen_min+'--'+row.orgin_pingfen_max+')';
					}
				},{
					width : '120',
					title : '调整后评分范围',
					field : 'tz_pingfen_leve_name',
					formatter : function(value, row, index) {
						return value+'('+row.tz_pingfen_min+'--'+row.tz_pingfen_max+')';
					}
				},{
					width : '140',
					title : '最后更新时间',
					field : 'update_time',
					sortable : true,
					formatter : function(value, row, index) {
						return (new Date(parseFloat(value))).format("yyyy-MM-dd hh:mm:ss");
					}
				},{
					width : '140',
					title : '是否有效',
					field : 'valid',
					sortable : true,
					formatter : function(value, row, index) {
						return value==1?'有效':'无效';
					}
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
				href : '${ctx}/instrument/checkValueScoreIn/addPage.shtml?smallItemId=${checkSmallItemFormMap.id}',
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
					$.post('${ctx}/instrument/checkValueScoreIn/delete.shtml', {
						'cfPingfenRoutFormMap.id' : id
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




		function pingfenRoutConf(id){
			window.location.href = '${ctx}/instrument/checkValueScoreIn/list.shtml?smallItemId='+id;
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
				href : '${ctx}/instrument/checkValueScoreIn/editPage.shtml?id=' + id,
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
	<form id="searchForm">
		<table>
			<tr>
				<th>检测小项名称:</th>
				<td>
					<input name="cfPingfenRoutFormMap.name" placeholder="请输入检测小项名称"/>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="cleanFun();">清空</a>
				</td>
			</tr>
		</table>
	</form>
</div>
<div data-options="region:'center',border:true,title:'评分概率---[${checkSmallItemFormMap.name}]'" >
	<table id="dataGrid" data-options="fit:true,border:false"></table>
</div>
<div id="toolbar" style="display: none;">
	<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
</div>
</body>
</html>