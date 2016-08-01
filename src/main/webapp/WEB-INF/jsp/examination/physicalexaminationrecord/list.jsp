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
	<title>会员检测管理</title>
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
				url : '${ctx}/examination/physicalExamination/dataGridExaminationList.shtml',
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
					title : 'ID',
					field : 'id',
					hidden:true
				},{
					width : '90',
					title : '会员姓名',
					field : 'custom_name'
				}, {
					width : '60',
					title : '会员性别',
					field : 'sex',
					formatter : function(value, row, index) {
						return value == 1 ?'男':'女';
					}
				},{
					width : '120',
					title : '会员手机',
					field : 'mobile'
				},{
					width : '150',
					title : '身份证',
					field : 'idcard'
				},{
					width : '90',
					title : '生日',
					field : 'birthday'
				},{
					width : '50',
					title : '身高(cm)',
					field : 'body_height'
				},{
					width : '50',
					title : '体重(kg)',
					field : 'weight'
				},{
					width : '150',
					title : '检测点',
					field : 'org_name'
				},{
					width : '100',
					title : '报告状态',
					field : 'status',
					align : 'center',
					formatter : function(value, row, index) {
						if(value == 1){
							return '数据上传中';
						}else if (value == 2){
							return '数据已上传';
						}else if (value == 3){
							return 'PDF报告生成中';
						}else if (value == 4){
							return '已生成PDF报告';
						}else{
							return '未知异常';
						}
					},
					sortable : true
				},{
					width : '180',
					title : '检测时间',
					field : 'check_time',
					align : 'center',
					formatter : function(value, row, index) {
						return (new Date(parseFloat(value))).format("yyyy-MM-dd hh:mm:ss");
					},
					sortable : true
				},{
					field : 'action',
					title : '操作',
					width : 300,
					formatter : function(value, rowdata, index) {
						var actionStr = '';
						if(rowdata.status == 2 || rowdata.status == 3){
							actionStr+='<a target="_blank" href="${ctx}/examination/physicalExamination/report.shtml?physicalExaminationRecordFormMap.id='+rowdata.id+'">查看报告</a>&nbsp;&nbsp;&nbsp;';
							actionStr+='<a target="_blank" href="${ctx}/examination/physicalExamination/sick_risk.shtml?physicalExaminationRecordFormMap.id='+rowdata.id+'">查看疾病风险评估</a>&nbsp;&nbsp;&nbsp;';
							actionStr+='<a href="javascript:void(genPdfReport('+rowdata.id+'))">生成pdf报告</a>&nbsp;&nbsp;&nbsp;';
						}

						if(rowdata.status==4){
							//actionStr+='<a target="_blank" href="'+rootPath + '/examination/physicalExamination/result.shtml?recordid='+rowdata.id+'">检测值</a>&nbsp;&nbsp;&nbsp;';
							actionStr+='<a target="_blank" href="${ctx}/examination/physicalExamination/report.shtml?physicalExaminationRecordFormMap.id='+rowdata.id+'">查看报告</a>&nbsp;&nbsp;&nbsp;';
							actionStr+='<a target="_blank" href="${ctx}/examination/physicalExamination/sick_risk.shtml?physicalExaminationRecordFormMap.id='+rowdata.id+'">查看疾病风险评估</a>&nbsp;&nbsp;&nbsp;';
							actionStr+='<a href="${ctx}/examination/physicalExamination/downloadReport.shtml?recordid='+rowdata.id+'">下载PDF报告</a>&nbsp;&nbsp;&nbsp;';
							actionStr+='<a href="${ctx}/examination/physicalExamination/downloadSickRiskReport.shtml?recordid='+rowdata.id+'">下载疾病风险PDF报告</a>&nbsp;&nbsp;&nbsp;';
						}

						return actionStr;
					}
				}] ]
			});
		});

		function genPdfReport( recordid){
			var url = '${ctx}/examination/physicalExamination/genPdfReport.shtml';
			$.ajax({
				type: "POST",
				dataType: "JSON",
				url: url,
				data: {
					recordid : recordid
				},
				success: function (data) {
					if(data){
						if(data.status){
							alert(data.msg);
						}else{
							alert('系统异常，操作失败!');
						}

					}
				},
				error: function(data) {
					alert("error:"+data.responseText);
				}
			});
		}


		function showCheckResult(id){
			if (id == undefined) {
				var rows = dataGrid.datagrid('getSelections');
				id = rows[0].id;
			} else {//点击操作里面的删除图标会触发这个
				dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
			}
			var url =  '${ctx}/examination/physicalExamination/result.shtml?recordid='+id;
			window.open(url);
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
					$.post('${ctx}/examination/physicalExamination/delete.shtml', {
						'physicalExaminationRecordFormMap.id' : id
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
				<th>会员姓名</th>
				<td>
					<input name="physicalExaminationRecordFormMap.name" placeholder="请输入会员姓名"/>
				</td>
				<th>会员手机</th>
				<td>
					<input name="physicalExaminationRecordFormMap.mobile" placeholder="请输入会员手机"/>
				</td>
				<th>身份证</th>
				<td>
					<input name="physicalExaminationRecordFormMap.id_card" placeholder="请输入会员身份证"/>
				</td>
				<th>会员来源</th>
				<td>
					<select id="organizationId" name="physicalExaminationRecordFormMap.organization_id"  style="width: 140px;"></select>
				</td>
				<th>检测时间</th>
				<td>
					<input name="physicalExaminationRecordFormMap.check_time_start" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" />
				</td>
				<th>至</th>
				<td>
					<input  name="physicalExaminationRecordFormMap.check_time_end" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" />
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="cleanFun();">清空</a>
				</td>
			</tr>
		</table>
	</form>
</div>
<div data-options="region:'center',border:true,title:'会员检测记录'" >
	<table id="dataGrid" data-options="fit:true,border:false"></table>
</div>

</body>
</html>