var pageii = null;
var grid = null;
$(function() {
	
	grid = lyGrid({
		pagId : 'paging',
		l_column : [ {
			colkey : "customid",
			name : "会员编号"
		}, {
			colkey : "customname",
			name : "会员姓名"
		}, {
			colkey : "customsex",
			name : "会员性别"
		}, {
			colkey : "custommobile",
			name : "会员手机"
		}, {
			colkey : "check_time",
			name : "检测时间",
			isSort:true
		}, {
			colkey : "status",
			name : "检测状态",
			isSort:true
		}, {
			name : "健康促进",
			renderData : function( rowindex ,data, rowdata, colkeyn) {
				//return "<a href='http://www.baidu.com'>新增检测点</a>&nbsp;&nbsp;&nbsp;<a href='#'>查看企业</a>&nbsp;&nbsp;&nbsp;<a href='javascript:void(deleteCurrentitem());'>删除企业</a>";
				console.log(rowdata.id)
				return '<a class="btn btn-danger marR10" data-toggle="modal" data-target="#myModal">删除</a>';
			}
		}, {
			name : "报告相关操作",
			renderData : function( rowindex ,data, rowdata, colkeyn) {
				//return "<a href='http://www.baidu.com'>新增检测点</a>&nbsp;&nbsp;&nbsp;<a href='#'>查看企业</a>&nbsp;&nbsp;&nbsp;<a href='javascript:void(deleteCurrentitem());'>删除企业</a>";
				console.log(rowdata.id)
				return '<a class="btn btn-danger marR10" data-toggle="modal" data-target="#myModal">删除</a>';
			}
		} ],
		jsonUrl : rootPath + '/examination/physicalExamination/findByPage.shtml',
		checkbox : true,
		serNumber : true
	});
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