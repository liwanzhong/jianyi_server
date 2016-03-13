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
			colkey : "age_min",
			name : "年龄从",
			isSort:true
		}, {
			colkey : "age_max",
			name : "年龄到",
			isSort:true
		}, {
			colkey : "pingfen_min",
			name : "原评分范围最小"
		}, {
			colkey : "pingfen_max",
			name : "原评分范围最大"
		}, {
			colkey : "tz_pingfen_min",
			name : "调整后评分范围最小值"
		}, {
			colkey : "tz_pingfen_max",
			name : "调整后评分范围最大值"
		}, {
			colkey : "rout_min",
			name : "评分调整概率从"
		}, {
			colkey : "rout_max",
			name : "评分调整概率到"
		}, {
			colkey : "update_time",
			name : "最后更新时间",
			isSort:true,
			renderData : function(rowindex,data, rowdata, column) {
				return new Date(data).format("yyyy-MM-dd hh:mm:ss");
			}
		}/*, {
			name : "操作",
			renderData : function( rowindex ,data, rowdata, colkeyn) {
				return '<a href="#">评分标准配置</a>';
			}
		} */],
		jsonUrl : rootPath + '/instrument/pingfen_rout/findByPage.shtml?cfPingfenRoutFormMap.small_id='+$("#small_id").val(),
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
		content : rootPath + '/instrument/pingfen_rout/editUI.shtml?id=' + cbox
	});
}
function addAccount() {
	pageii = layer.open({
		title : "新增评分概率",
		type : 2,
		area : [ "60%", "80%" ],
		content : rootPath + '/instrument/pingfen_rout/addUI.shtml?small_id='+$("#small_id").val()
	});
}
function delAccount() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox == "") {
		layer.msg("请选择删除项！！");
		return;
	}
	layer.confirm('是否删除？', function(index) {
		var url = rootPath + '/instrument/pingfen_rout/deleteEntity.shtml';
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