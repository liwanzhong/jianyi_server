var pageii = null;
var grid = null;
$(function() {
	
	grid = lyGrid({
		pagId : 'paging',
		l_column : [ {
			colkey : "id",
			name : "会员编号"
		}, {
			colkey : "name",
			name : "会员姓名",
			isSort:true
		}, {
			colkey : "sex",
			name : "会员性别",
			isSort:true,
			renderData : function(rowindex,data, rowdata, column) {
				return data==1?'男':'女';
			}
		}, {
			colkey : "mobile",
			name : "会员手机",
			isSort:true
		}, {
			colkey : "bmi",
			name : "BMI",
			isSort:true,
			renderData : function(rowindex,data, rowdata, column) {
				return rowdata.body_height/rowdata.weight;
			}
		}, {
			colkey : "insert_time",
			name : "注册时间",
			isSort:true,
			renderData : function(rowindex,data, rowdata, column) {
				return new Date(data).format("yyyy-MM-dd hh:mm:ss");
			}
		}, {
			colkey : "lastcheck_time",
			name : "最近一次检测",
			isSort:true,
			renderData : function(rowindex,data, rowdata, column) {
				if(data!=''){
					return new Date(data).format("yyyy-MM-dd hh:mm:ss");
				}else{
					return '';
				}

			}
		}, {
			name : "操作",
			renderData : function( rowindex ,data, rowdata, colkeyn) {
				return "<a href='#'>编辑</a>&nbsp;&nbsp;&nbsp;<a href='javascript:void(deleteCurrentitem());'>删除</a>";
			}
		} ],
		jsonUrl : rootPath + '/custom/info/findByPage.shtml',
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
	$("#permissions").click("click", function() {
		permissions();
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
		content : rootPath + '/custom/info/editUI.shtml?id=' + cbox
	});
}
function addAccount() {
	pageii = layer.open({
		title : "新增",
		type : 2,
		area : [ "80%", "80%" ],
		content : rootPath + '/custom/info/addUI.shtml'
	});
}
function delAccount() {
	var cbox = grid.getSelectedCheckbox();
	console.log(grid);
	if (cbox == "") {
		layer.msg("请选择删除项！！");
		return;
	}
	layer.confirm('是否删除？', function(index) {
		var url = rootPath + '/custom/info/deleteEntity.shtml';
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
