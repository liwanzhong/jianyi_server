<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/inc.jsp"></jsp:include>
	<meta http-equiv="X-UA-Compatible" content="edge" />
	<c:if test="${fn:contains(sessionScope.RESOURCES_SESSION_KEY, '/instrument/BmiCheckItemConfig/edit.shtml')}">
		<script type="text/javascript">
			$.canEdit = true;
		</script>
	</c:if>
	<c:if test="${fn:contains(sessionScope.RESOURCES_SESSION_KEY, '/instrument/BmiCheckItemConfig/delete.shtml')}">
		<script type="text/javascript">
		</script>
	</c:if>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>检测项管理</title>
	<script type="text/javascript">
		var dataGrid;
		$(function() {
			dataGrid = $('#dataGrid').datagrid({
				url : '${ctx}/instrument/BmiCheckItemConfig/dataGrid.shtml?bmiCheckItemConfigFormMap.check_item_id=${checkItemId}&bmiCheckItemConfigFormMap.check_type=${checkItemType}',
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
					title : 'BMI范围',
					field : 'bmi_min',
					formatter : function(value, row, index) {
						return row.bmi_min +'--- '+row.bmi_max;
					}
				},{
					width : '180',
					title : '年龄范围',
					field : 'age_min',
					formatter : function(value, row, index) {
						return row.age_min +'--- '+row.age_max;
					}
				},{
					width : '180',
					title : '原得分等级',
					field : 'org_leve_id'
				},{
					width : '180',
					title : '调整后得分等级',
					field : 'tz_leve_id'
				},{
					width : '180',
					title : '随机几率',
					field : 'rout',
					formatter : function(value, row, index) {
						return value+"  %";
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


		function bmiConfig(id){
			window.location.href = '${ctx}/instrument/bmiRiskRoutConfig/list.shtml?BmiCheckItemConfigFormMap.id='+id;
		}

		function ageConfig(id){
			window.location.href = '${ctx}/instrument/ageRiskRoutConfig/list.shtml?BmiCheckItemConfigFormMap.id='+id;
		}




		function addFun() {
			parent.$.modalDialog({
				title : '添加',
				width : '60%',
				height : '60%',
				href : '${ctx}/instrument/BmiCheckItemConfig/addPage.shtml?checkItemId=${checkItemId}&checkItemType=${checkItemType}',
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
					$.post('${ctx}/instrument/BmiCheckItemConfig/delete.shtml', {
						'BmiCheckItemConfigFormMap.id' : id
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
				width : '60%',
				height : '60%',
				href : '${ctx}/instrument/BmiCheckItemConfig/editPage.shtml?id=' + id,
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


	</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
</div>
<div data-options="region:'center',border:true,title:'BMI检测关联配置'" >
	<table id="dataGrid" data-options="fit:true,border:false"></table>
</div>
<div id="toolbar" style="display: none;">
	<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
</div>
</body>
</html>