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
			name : "评分等级名称",
			isSort:true
		}, {
			name : "分数范围",
			renderData : function(rowindex,data, rowdata, column) {
				return rowdata.pingfen_min+"--"+rowdata.pingfen_max;
			}
		}, {
			colkey : "update_time",
			name : "更新时间",
			renderData : function(rowindex,data, rowdata, column) {
				return new Date(data).format("yyyy-MM-dd hh:mm:ss");
			}
		}, {
			colkey : "show_color",
			name : "颜色展示",
			renderData : function(rowindex,data, rowdata, column) {
				return '<span style="background-color: '+data+';">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>';
			}
		}],
		jsonUrl : rootPath + '/instrument/pingfen_leve/findByPage.shtml',
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
		content : rootPath + '/instrument/pingfen_leve/editUI.shtml?id=' + cbox
	});
}
function addAccount() {
	pageii = layer.open({
		title : "新增评分等级",
		type : 2,
		area : [ "60%", "80%" ],
		content : rootPath + '/instrument/pingfen_leve/addUI.shtml'
	});
}
function delAccount() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox == "") {
		layer.msg("请选择删除项！！");
		return;
	}
	layer.confirm('是否删除？', function(index) {
		var url = rootPath + '/instrument/pingfen_leve/deleteEntity.shtml';
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