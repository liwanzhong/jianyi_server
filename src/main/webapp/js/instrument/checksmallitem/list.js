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
			name : "检测小项名称",
			isSort:true
		}, {
			colkey : "min_value",
			name : "检测标准[基准值（n1）]",
			isSort:true
		}, {
			colkey : "max_value",
			name : "检测标准[衰退值（n2）]",
			isSort:true
		}, {
			colkey : "in_value",
			name : "区间值(n=n2-n1)",
			isSort:true
		}, {
			colkey : "check_min",
			name : "实际检测范围（从）",
			isSort:true
		}, {
			colkey : "check_max",
			name : "实际检测范围(到)",
			isSort:true
		}, {
			colkey : "quanzhong",
			name : "权重系数",
			isSort:true
		}, {
			colkey : "update_time",
			name : "更新时间",
			isSort:true
		}, {
			name : "操作",
			renderData : function( rowindex ,data, rowdata, colkeyn) {
				return '<a href="#">评分标准配置</a>';
			}
		} ],
		jsonUrl : rootPath + '/instrument/smallitem/findByPage.shtml?checkSmallItemFormMap.big_item_id='+$("#big_item_id").val(),
		//jsonUrl : rootPath + '/instrument/bigitem/findByPage.shtml',
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
		content : rootPath + '/instrument/smallitem/editUI.shtml?id=' + cbox
	});
}
function addAccount() {
	pageii = layer.open({
		title : "新增",
		type : 2,
		area : [ "60%", "80%" ],
		content : rootPath + '/instrument/smallitem/addUI.shtml'
	});
}
function delAccount() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox == "") {
		layer.msg("请选择删除项！！");
		return;
	}
	layer.confirm('是否删除？', function(index) {
		var url = rootPath + '/instrument/smallitem/deleteEntity.shtml';
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