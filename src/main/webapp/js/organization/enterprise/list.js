var pageii = null;
var grid = null;
$(function() {
	
	grid = lyGrid({
		pagId : 'paging',
		l_column : [ {
			colkey : "id",
			name : "企业编号"
		}, {
			colkey : "name",
			name : "企业名称",
			isSort:true
		}, {
			colkey : "contact_phone",
			name : "联系电话",
			isSort:true
		}, {
			colkey : "insert_time",
			name : "建立时间",
			isSort:true,
			renderData : function(rowindex,data, rowdata, column) {
				return new Date(data).format("yyyy-MM-dd hh:mm:ss");
			}
		}, {
			colkey : "sub_point_count",
			name : "检测点",
			isSort:true
		}, {
			name : "操作",
			renderData : function( rowindex ,data, rowdata, colkeyn) {
				//return "<a href='/sub_point/list.shtml?id=51&entid="+rowdata.id+">新增检测点</a>&nbsp;&nbsp;&nbsp;<a href='#'>查看企业</a>&nbsp;&nbsp;&nbsp;<a href='javascript:void(deleteCurrentitem());'>删除企业</a>";
				//return '<a class="btn btn-danger marR10" data-toggle="modal" data-target="#myModal">删除</a> &nbsp;&nbsp;&nbsp;<a class="btn btn-danger marR10" data-toggle="modal" onclick="showOtherPageInLocal('+rowdata.id+')">检测点管理</a>';
				return '<a href="/sub_point/list.shtml?id=51&entid='+rowdata.id+'">新增检测点</a>';
			}
		} ],
		jsonUrl : rootPath + '/enterprise/findByPage.shtml',
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
		content : rootPath + '/enterprise/editUI.shtml?id=' + cbox
	});
}
function addAccount() {
	pageii = layer.open({
		title : "新增",
		type : 2,
		area : [ "600px", "80%" ],
		content : rootPath + '/enterprise/addUI.shtml'
	});
}
function delAccount() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox == "") {
		layer.msg("请选择删除项！！");
		return;
	}
	layer.confirm('是否删除？', function(index) {
		var url = rootPath + '/enterprise/deleteEntity.shtml';
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


function  showOtherPageInLocal(entid){
	var html = '<li><i class="fa fa-home"></i>';
	html+='<a href="index.shtml">Home</a></li>';
	html+='<li><a href="javascript:void(0);">企业管理</a></li>';
	html+='<li><a href="javascript:void(0);">检测点管理</a></li>';
	$("#topli").html(html);
	var tb = $("#loadhtml");
	tb.html(CommnUtil.loadingImg());
	tb.load(rootPath+'/sub_point/list.shtml?id=51&entid='+entid);
}