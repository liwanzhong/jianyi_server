<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/inc.jsp"></jsp:include>
	<meta http-equiv="X-UA-Compatible" content="edge" />
	<title>检测统计</title>
	<script type="text/javascript">
		var treeGrid;
		$(function() {
			treeGrid = $('#treeGrid').treegrid({
				url : '${ctx}/stats/phystats/treeGrid.shtml',
				idField : 'id',
				treeField : 'org_name',
				parentField : 'pid',
				fit : true,
				fitColumns : false,
				border : false,
				frozenColumns : [ [ {
					title : '检测点',
					field : 'org_name',
					width : 200
				} ] ],
				columns : [ [ {
					field : 'createdatetime',
					title : '创建时间',
					width : 180,
					formatter : function(value, row, index) {
						return (new Date(parseFloat(value))).format("yyyy-MM-dd hh:mm:ss");
					}
				},{
					field : 'jcrenshu',
					title : '检测人数',
					width : 180
				}, {
					field : 'jcrenci',
					title : '检测人次',
					width : 180
				} ] ],
				toolbar : '#toolbar'
			});


			$('#organizationId').combotree({
				url : '${ctx}/organization/tree.shtml',
				parentField : 'pid',
				lines : true,
				panelHeight : 'auto'
			});




		});

		function searchFun(month) {
			$("#month").val(month);
			treeGrid.treegrid('load', $.serializeObject($('#searchForm')));
		}

		function cleanFun() {
			$('#searchForm input').val('');
			treeGrid.treegrid('load',{});
		}



		$.ajax({
			type: "POST",
			dataType: "JSON",
			url: '${ctx}/stats/phystats/totalExamination.shtml',
			success: function (data) {
				if(data){
					$("#totalJCRenshu").empty().text(data.zrenshu);
					$("#totalJCRenci").empty().text(data.zrenci);
				}
			},
			error: function(data) {
				alert("error:"+data.responseText);
			}

		});




	</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
		<form id="searchForm">
			<table>
				<tr>
					<th>检测点:</th>
					<td><select id="organizationId" name="physicalExaminationRecordFormMap.organization_id" style="width: 140px;" class="easyui-validatebox"></select></td>
					<th>&nbsp;</th>
					<td>
						<input type="hidden" value="month" name="physicalExaminationRecordFormMap.month" id="month">
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun(3);">最近三个月</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun(1);">最近一个月</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun(10000);">历史累计</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="cleanFun();">清空</a>
					</td>
				</tr>
			</table>

		</form>

	</div>
	<div data-options="region:'center',border:false"  style="overflow: hidden;">
		<table id="treeGrid"></table>
	</div>

	<div id="toolbar" style="display: none;">
		<table>
			<tr>
				<th>总检测人数:</th>
				<td><span id="totalJCRenshu" style="color: red"></span></td>
				<th>总检测人次:</th>
				<td><span id="totalJCRenci" style="color: red"></span></td>
			</tr>
		</table>
	</div>
</div>
</body>
</html>