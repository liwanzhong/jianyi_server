var pageii = null;
var grid = null;
$(function() {
	
	grid = lyGrid({
		pagId : 'paging',
		l_column : [ {
			colkey : "id",
			name : "检测项ID",
		}, {
			colkey : "name",
			name : "检测大项名称",
			isSort:true,
		}, {
			colkey : "is_show",
			name : "是否显示",
			isSort:true,
			renderData : function(rowindex,data, rowdata, column) {
				var ischeck=false;
				if(data==1){
					ischeck=1;
				}
				return '<input type="checkbox" checked>';
			}
		}, {
			colkey : "order_by",
			name : "排序",
			isSort:true

		}, {
			colkey : "update_time",
			name : "最近一次更新",
			isSort:true,
			renderData : function(rowindex,data, rowdata, column) {
				return new Date(data).format("yyyy-MM-dd hh:mm:ss");
			}
		}, {
			name : "操作",
			renderData : function( rowindex ,data, rowdata, colkeyn) {
				console.log(rowdata.id)
				return '<a class="btn btn-danger marR10" data-toggle="modal" data-target="#myModal">删除</a>编辑  查看小项  新增小项  占比系数  升降级配置';
			}
		} ],
		jsonUrl : rootPath + '/instrument/bigitem/findByPage.shtml',
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
		content : rootPath + '/instrument/bigitem/editUI.shtml?id=' + cbox
	});
}
function addAccount() {
	pageii = layer.open({
		title : "新增",
		type : 2,
		area : [ "80%", "80%" ],
		content : rootPath + '/instrument/bigitem/addUI.shtml'
	});
}
function delAccount() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox == "") {
		layer.msg("请选择删除项！！");
		return;
	}
	layer.confirm('是否删除？', function(index) {
		var url = rootPath + '/instrument/bigitem/deleteEntity.shtml';
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