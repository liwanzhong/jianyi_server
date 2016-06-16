var pageii = null;
var grid = null;
$(function() {

	grid = lyGrid({
		pagId : 'paging',
		l_column : [ {
			colkey : "id",
			name : "id",
			hide:true
		}, {
			colkey : "name",
			name : "会员姓名"
		}, {
			colkey : "sex",
			name : "会员性别",
			renderData : function(rowindex,data, rowdata, column) {
				return data==1?'男':'女';
			}
		}, {
			colkey : "mobile",
			name : "会员手机"
		}, {
			colkey : "check_time",
			name : "检测时间",
			renderData : function(rowindex,data, rowdata, column) {
				return new Date(data).format("yyyy-MM-dd hh:mm:ss");
			}
		}, {
			colkey : "status",
			name : "检测状态",
			renderData : function(rowindex,data, rowdata, column) {
				if(data==2){
					return '数据已经处理'
				}else if(data==1){
					return '数据已上传';
				}else if(data==3){
					return '报告生成中';
				}else if(data == 4){
					return '报告已经生成';
				}
			}
		}, {
			name : "报告相关操作",
			renderData : function( rowindex ,data, rowdata, colkeyn) {
				return retActionChosen(rowindex,data,rowdata,colkeyn);
			}
		} ],
		jsonUrl : rootPath + '/examination/physicalExamination/findByPage.shtml',
		checkbox : false,
		serNumber : true
	});


	function  retActionChosen(rowindex ,data, rowdata, colkeyn){
		var actionStr = '';
		if(rowdata.status == 2 || rowdata.status == 3){
			//actionStr+='<a target="_blank" href="'+rootPath + '/examination/physicalExamination/result.shtml?recordid='+rowdata.id+'">检测值</a>&nbsp;&nbsp;&nbsp;';
			actionStr+='<a target="_blank" href="'+rootPath + '/examination/physicalExamination/report.shtml?physicalExaminationRecordFormMap.id='+rowdata.id+'">查看报告</a>&nbsp;&nbsp;&nbsp;';
			actionStr+='<a target="_blank" href="'+rootPath + '/examination/physicalExamination/sick_risk.shtml?physicalExaminationRecordFormMap.id='+rowdata.id+'">查看疾病风险评估</a>&nbsp;&nbsp;&nbsp;';

		}
		if(rowdata.status==4){
			//actionStr+='<a target="_blank" href="'+rootPath + '/examination/physicalExamination/result.shtml?recordid='+rowdata.id+'">检测值</a>&nbsp;&nbsp;&nbsp;';
			actionStr+='<a target="_blank" href="'+rootPath + '/examination/physicalExamination/report.shtml?physicalExaminationRecordFormMap.id='+rowdata.id+'">查看报告</a>&nbsp;&nbsp;&nbsp;';
			actionStr+='<a target="_blank" href="'+rootPath + '/examination/physicalExamination/sick_risk.shtml?physicalExaminationRecordFormMap.id='+rowdata.id+'">查看疾病风险评估</a>&nbsp;&nbsp;&nbsp;';
			actionStr+='<a href="'+rootPath + '/examination/physicalExamination/downloadReport.shtml?recordid='+rowdata.id+'">下载PDF报告</a>';
		}
		return actionStr;
	}



	$("#search").click("click", function() {// 绑定查询按扭
		var searchParams = $("#searchForm").serializeJson();// 初始化传参数
		grid.setOptions({
			data : searchParams
		});
	});
	$("#addFun").click("click", function() {
		addAccount();
	});
	$("#editFun").click("click", function() {
		editAccount();
	});
	$("#delFun").click("click", function() {
		delAccount();
	});

});
function editAccount() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox.length > 1 || cbox == "") {
		layer.msg("只能选中一个");
		return;
	}
	pageii = layer.open({
		title : "编辑",
		type : 2,
		area : [ "600px", "80%" ],
		content : rootPath + '/examination/physicalExamination/editUI.shtml?id=' + cbox
	});
}
function addAccount() {
	pageii = layer.open({
		title : "新增",
		type : 2,
		area : [ "60%", "80%" ],
		content : rootPath + '/examination/physicalExamination/addUI.shtml'
	});
}
function delAccount() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox == "") {
		layer.msg("请选择删除项！！");
		return;
	}
	layer.confirm('是否删除？', function(index) {
		var url = rootPath + '/examination/physicalExamination/deleteEntity.shtml';
		var s = CommnUtil.ajax(url, {
			ids : cbox.join(",")
		}, "json");
		if (s == "success") {
			layer.msg('删除成功');
			grid.loadData();
		} else {
			layer.msg('删除失败');
		}
	});
}