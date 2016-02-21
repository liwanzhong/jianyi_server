var pageii = null;
var grid = null;
$(function() {
	
	grid = lyGrid({
		pagId : 'paging',
		l_column : [ {
			colkey : "id",
			name : "仪器id"
		}, {
			colkey : "istmt_code",
			name : "仪器机器码",
			isSort:true
		}, {
			colkey : "entname",
			name : "仪器所属企业",
			isSort:true
		}, {
			colkey : "subentname",
			name : "仪器所属分店",
			isSort:true
		}, {
			colkey : "remark",
			name : "备注",
			isSort:true
		}, {
			name : "操作",
			renderData : function( rowindex ,data, rowdata, colkeyn) {
				//return "<a href='http://www.baidu.com'>新增检测点</a>&nbsp;&nbsp;&nbsp;<a href='#'>查看企业</a>&nbsp;&nbsp;&nbsp;<a href='javascript:void(deleteCurrentitem());'>删除企业</a>";
				console.log(rowdata.id)
				return '<a class="btn btn-danger marR10" data-toggle="modal" data-target="#myModal">删除</a>';
			}
		} ],
		jsonUrl : rootPath + '/instrument/equipment/findByPage.shtml',
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
		content : rootPath + '/instrument/equipment/editUI.shtml?id=' + cbox
	});
}
function addAccount() {
	pageii = layer.open({
		title : "新增",
		type : 2,
		area : [ "60%", "80%" ],
		content : rootPath + '/instrument/equipment/addUI.shtml'
	});
}
function delAccount() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox == "") {
		layer.msg("请选择删除项！！");
		return;
	}
	layer.confirm('是否删除？', function(index) {
		var url = rootPath + '/instrument/equipment/deleteEntity.shtml';
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