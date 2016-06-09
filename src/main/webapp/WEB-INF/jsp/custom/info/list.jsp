<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/inc.jsp"></jsp:include>
	<meta http-equiv="X-UA-Compatible" content="edge" />
	<c:if test="${fn:contains(sessionScope.RESOURCES_SESSION_KEY, '/custom/info/edit.shtml')}">
		<script type="text/javascript">
			$.canEdit = true;
		</script>
	</c:if>
	<c:if test="${fn:contains(sessionScope.RESOURCES_SESSION_KEY, '/custom/info/delete.shtml')}">
		<script type="text/javascript">
		</script>
	</c:if>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>会员管理</title>
	<script type="text/javascript">
		var dataGrid;
		$(function() {

			$('#organizationId').combotree({
				url : '${ctx}/organization/tree.shtml',
				parentField : 'pid',
				lines : true,
				panelHeight : 'auto'
			});


			dataGrid = $('#dataGrid').datagrid({
				url : '${ctx}/custom/info/dataGrid.shtml',
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
					title : 'id',
					field : 'id',
					align:'center'
				},{
					width : '120',
					title : '姓名',
					field : 'name',
					align:'center'
				}, {
					width : '180',
					title : '身份证号',
					field : 'idcard',
					align : 'center'
				},{
					width : '120',
					title : '手机号',
					field : 'mobile',
					align:'center'
				},{
					width : '60',
					title : '性别',
					field : 'sex',
					sortable : true,
					formatter : function(value, row, index) {
						return value==1?'男':'女';
					},
					align :'center'
				},{
					width : '180',
					title : '生日',
					field : 'birthday',
					align : 'center',
					formatter : function(value, row, index) {
						return (new Date(parseFloat(value))).format("yyyy-MM-dd");
					}
				},{
					width : '70',
					title : '身高',
					field : 'body_height',
					sortable : true,
					formatter : function(value, row, index) {
						return value+'cm';
					},
					align : 'center'
				},{
					width : '70',
					title : '体重',
					field : 'weight',
					align : 'center',
					formatter : function(value, row, index) {
						return value+'KG';
					}
				},{
					width : '100',
					title : 'BMI',
					field : 'bmi',
					align : 'center',
					formatter : function(value, row, index) {
						return (row.weight / ((row.body_height/100)*row.body_height/100)).toFixed(2);
					}
				},{
					width : '250',
					title : '检测点名称',
					field : 'organization_name',
					align : 'center'
				},{
					width : '180',
					title : '注册时间',
					field : 'insert_time',
					align : 'center',
					formatter : function(value, row, index) {
						if(value == '' || value ==null || value == undefined){
							return value;
						}
						return (new Date(parseFloat(value))).format("yyyy-MM-dd hh:mm:ss");
					},
					sortable : true
				},{
					width : '180',
					title : '最后体检时间',
					field : 'last_check_time',
					align : 'center',
					formatter : function(value, row, index) {
						if(value == '' || value ==null || value == undefined){
							return value;
						}
						return (new Date(parseFloat(value))).format("yyyy-MM-dd hh:mm:ss");
					},
					sortable : true
				},{
					field : 'action',
					title : '操作',
					width : 300,
					formatter : function(value, row, index) {
						var str = '';
						str += $.formatString('<a href="javascript:void(0)" onclick="genTestData(\'{0}\');" >生成测试数据</a>', row.id);
						str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
						str += $.formatString('<a href="javascript:void(0)" onclick="deleteFun(\'{0}\');" >删除</a>', row.id);
						return str;
					}
				}] ]
			});
		});


		/**
		 * /citfc/client_call/check/upload.shtml?customerId=&instrumentCode=708c0006bfbcd930bf872f521aceeb00
		 * 生成测试数据功能
		 * @param customId
         */
		function genTestData(customId){
			progressLoad();
			$.post('${ctx}/citfc/client_call/check/upload.shtml', {
				customerId : customId,
//				instrumentCode:'ac605175f17ca2941d6783798c2767c7'
				instrumentCode:'708c0006bfbcd930bf872f521aceeb00'
			}, function(result) {
				if (result.status == 1) {
					parent.$.messager.alert('提示', "生成测算数据成功", 'info');
				}else if (result.status == 0){
					parent.$.messager.alert('提示', result.error, 'info');
				}
				progressClose();
			}, 'JSON');
		}


		function addFun() {
			parent.$.modalDialog({
				title : '添加',
				width : '50%',
				height : '40%',
				href : '${ctx}/custom/info/addPage.shtml',
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
					$.post('${ctx}/custom/info/delete.shtml', {
						'customInfoFormMap.id' : id
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
				href : '${ctx}/custom/info/editPage.shtml?id=' + id,
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
				<th>会员姓名：</th>
				<td>
					<input name="customInfoFormMap.name" placeholder="请输入会员姓名"/>
				</td>

				<th>会员手机：</th>
				<td>
					<input name="customInfoFormMap.mobile" placeholder="请输入会员手机"/>
				</td>

				<th>会员来源：</th>
				<td>
					<select name="customInfoFormMap.organization_id" id="organizationId"  style="width: 140px;"></select>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="cleanFun();">清空</a>
				</td>
			</tr>
		</table>
	</form>
</div>
<div data-options="region:'center',border:true,title:'评分等级'" >
	<table id="dataGrid" data-options="fit:true,border:false"></table>
</div>
<%--<div id="toolbar" style="display: none;">
	<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
</div>--%>
</body>
</html>