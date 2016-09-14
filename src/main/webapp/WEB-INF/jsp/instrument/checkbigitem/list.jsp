<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/inc.jsp"></jsp:include>
	<meta http-equiv="X-UA-Compatible" content="edge" />
	<c:if test="${fn:contains(sessionScope.RESOURCES_SESSION_KEY, '/instrument/bigitem/edit.shtml')}">
		<script type="text/javascript">
			$.canEdit = true;
		</script>
	</c:if>
	<c:if test="${fn:contains(sessionScope.RESOURCES_SESSION_KEY, '/instrument/bigitem/delete.shtml')}">
		<script type="text/javascript">
		</script>
	</c:if>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>检测项管理</title>
	<script type="text/javascript">
		var dataGrid;
		$(function() {
			dataGrid = $('#dataGrid').datagrid({
				url : '${ctx}/instrument/bigitem/dataGrid.shtml',
				fit : true,
				striped : true,
				rownumbers : true,
				pagination : true,
				singleSelect : true,
				idField : 'id',
				sortName : 'insert_time',
				sortOrder : 'desc',
				pageSize : 50,
				pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
				columns : [ [ {
					width : '120',
					title : '大项名称',
					field : 'name'
				}, {
					width : '120',
					title : '是否显示',
					field : 'show_exc_tips',
					formatter : function(value, row, index) {
						return value==0?'不显示':'显示';
					}
				}, {
					width : '120',
					title : '权重系数',
					field : 'quanzhong'
				}, {
					width : '120',
					title : '性别区分',
					field : 'withsex',
					formatter : function(value, row, index) {
						if(value == 0){
							return "无";
						}else if (value == 1){
							return "男"
						}else if (value = 2){
							return "女";
						}
					}
				}, {
					width : '120',
					title : '关联BMI',
					field : 'withbmi',
					formatter : function(value, row, index) {
						return value==0?'不关联BMI':'关联BMI';
					}
				}, {
					width : '120',
					title : '图表类型',
					field : 'charts_item',
					formatter : function(value, row, index) {
						var chartType = null;
						switch (value){
							case 1:
								chartType = "柱状图";
								break;
							case 2:
								chartType = "书页图表";
								break;
							case 3:
								chartType = "折点图";
								break;
							case 4:
								chartType = "六边形图表";
								break;
							case 5:
								chartType = "凹型柱状图";
								break;
							case 6:
								chartType = "箭头柱状图";
								break;
							case 7:
								chartType = "折线图";
								break;
							case 8:
								chartType = "圆点柱状图";
								break;
							default:
								chartType = "柱状图";
								break;
						}
						return chartType;
					}
				}, {
					width : '120',
					title : '检测意义',
					field : 'tips_content'
				},{
					width : '80',
					title : '排序',
					field : 'order_by',
					sortable : true
				}/*,{
					width : '150',
					title : '图标',
					field : 'icon',
					formatter : function(value, row, index) {
						return '<img src="${ctx}'+value+'" style="max-width: 30px;max-height: 30px;">';
					}
				}*/,{
					field : 'action',
					title : '操作',
					width : 450,
					formatter : function(value, row, index) {
						var str = '';
						str += $.formatString('<a href="javascript:void(0)" onclick="editFun(\'{0}\');" >编辑</a>', row.id);
						str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
						str += $.formatString('<a href="javascript:void(0)" onclick="deleteFun(\'{0}\');" >删除</a>', row.id);
						str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
						str += $.formatString('<a href="javascript:void(0)" onclick="smallCheckItemFun(\'{0}\');" >检测小项</a>', row.id);
						str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
						str += $.formatString('<a href="javascript:void(0)" onclick="bmiConf(\'{0}\');" >BMI关联</a>', row.id);
						str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
						str += $.formatString('<a href="javascript:void(0)" onclick="sickRiskConfig(\'{0}\');" >疾病关联</a>', row.id);
						str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
						str += $.formatString('<a href="javascript:void(0)" onclick="duanbanConfig(\'{0}\');" >短板配置</a>', row.id);
						str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
						str += $.formatString('<a href="javascript:void(0)" onclick="leveprocentConfig(\'{0}\');" >等级占比配置</a>', row.id);
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
				height : '60%',
				href : '${ctx}/instrument/bigitem/addPage.shtml',
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


		function bmiConf(id){
			window.location.href = '${ctx}/instrument/BmiCheckItemConfig/list.shtml?checkItemId='+id+'&checkItemType=1';
		}

		function deleteFun(id) {
			if (id == undefined) {//点击右键菜单才会触发这个
				var rows = dataGrid.datagrid('getSelections');
				id = rows[0].id;
			} else {//点击操作里面的删除图标会触发这个
				dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
			}
			parent.$.messager.confirm('询问', '是否需要删除当前大项？', function(b) {
				if (b) {
					progressLoad();
					$.post('${ctx}/instrument/bigitem/delete.shtml', {
						'checkBigItemFormMap.id' : id
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


		function smallCheckItemFun(id){
			window.location.href = '${ctx}/instrument/smallitem/list.shtml?bigItemId='+id;
		}


		function sickRiskConfig(id){
			window.location.href = '${ctx}/instrument/sickRisk/list.shtml?checkItemId='+id+'&checkItemType=1';
		}


		function duanbanConfig(id){
			window.location.href = '${ctx}/instrument/duanbanconfig/list.shtml?checkItemId='+id;
		}


		function leveprocentConfig(id){
			window.location.href = '${ctx}/instrument/leveZhanbi/list.shtml?checkItemId='+id+'&checkItemType=2';
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
				height : '60%',
				href : '${ctx}/instrument/bigitem/editPage.shtml?id=' + id,
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
				<th>检测大项:</th>
				<td>
					<input name="checkBigItemFormMap.name" placeholder="请输入检测大项名称"/>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="cleanFun();">清空</a>
				</td>
			</tr>
		</table>
	</form>
</div>
<div data-options="region:'center',border:true,title:'检测大项'" >
	<table id="dataGrid" data-options="fit:true,border:false"></table>
</div>
<div id="toolbar" style="display: none;">
	<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
</div>
</body>
</html>