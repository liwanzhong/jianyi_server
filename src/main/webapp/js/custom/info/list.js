var pageii = null;
var grid = null;

$(function() {
	
	grid = lyGrid({
		pagId : 'paging',
		l_column : [ {
			colkey : "id",
			name : "会员编号",
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
			colkey : "bmi",
			name : "BMI",
			renderData : function(rowindex,data, rowdata, column) {
				return (rowdata.body_height/rowdata.weight).toFixed(3);
			}
		}, {
			colkey : "insert_time",
			name : "注册时间",
			renderData : function(rowindex,data, rowdata, column) {
				return new Date(data).format("yyyy-MM-dd hh:mm:ss");
			}
		}, {
			colkey : "last_check_time",
			name : "最近一次检测",
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
				return "<a href='javascript:void(genTestData("+rowdata.id+","+rowdata.id+"))'>生成检测数据</a>&nbsp;&nbsp;&nbsp;<a href='#'>编辑</a>&nbsp;&nbsp;&nbsp;<a href='javascript:void(deleteCurrentitem("+rowdata.belongToId+"));'>删除</a>";
			}
		} ],
		jsonUrl : rootPath + '/custom/info/findByPage.shtml',
		checkbox : false,
		serNumber : true
	});
	$("#search").click("click", function() {// 绑定查询按扭
		var searchParams = $("#searchForm").serializeJson();// 初始化传参数
		console.log(searchParams);
		grid.setOptions({
			data : searchParams
		});
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



function genTestData(customId,instrumentCode){
	var url = rootPath + '/citfc/client_call/check/upload.shtml';
	var s = CommnUtil.ajax(url, {
		customerId : customId,
		instrumentCode:'ac605175f17ca2941d6783798c2767c7'
	}, "json");
	if (s.status ==1) {
		layer.msg('生成测算数据成功!');
		grid.loadData();
	} else {
		layer.msg('生成测算数据失败');
	}

}


function addAccount() {
	pageii = layer.open({
		title : "新增",
		type : 2,
		area : [ "80%", "80%" ],
		content : rootPath + '/custom/info/addUI.shtml'
	});
}
function deleteCurrentitem(id) {
	layer.confirm('是否需要删除当前会员？', function(index) {
		var url = rootPath + '/custom/info/delete.shtml';
		var s = CommnUtil.ajax(url, {
			'customBelonetoEntFormMap.id' : id
		}, "json");
		if (s.status ==1) {
			layer.msg('删除成功');
			console.log(grid);
			grid.loadData();
		} else {
			layer.msg('删除失败');
		}
	});
}
