<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@include file="/common/common.jspf"%>
	<script type="text/javascript" src="${ctx}/js/custom/info/add.js">

	</script>
	<style type="text/css">
		.col-sm-3 {
			width: 15%;
			float: left;
			text-align: right;
		}

		.col-sm-9 {
			width: 85%;
			float: left;
			text-align: left;
		}

		label[class^="btn btn-default"] {
			margin-top: -4px;
		}
	</style>
</head>
<body>
<div class="l_err" style="width: 100%; margin-top: 2px;"></div>
<form id="form" name="form" class="form-horizontal" method="post"  action="${ctx}/custom/info/addEntity.shtml">
	<section class="panel panel-default">
		<div class="panel-body">
			<div class="line line-dashed line-lg pull-in"></div>
			必填信息
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">会员姓名</label>
				</div>
				<div class="col-sm-9">
					<input type="text" class="form-control"  placeholder="请输入会员姓名" name="customInfoFormMap.name" id="name">
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<label class="col-sm-3 control-label">登录账号</label>
				<div class="col-sm-9">
					<input type="text" class="form-control checkacc" placeholder="请输入登录账号" name="customInfoFormMap.username" id="username">
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
			<div class="col-sm-3">
				<label class="control-label">登录密码</label>
			</div>
			<div class="col-sm-9">
				<input type="password" class="form-control"  placeholder="请输入登录密码" name="customInfoFormMap.password" id="password">
			</div>
		</div>
			<div class="line line-dashed line-lg pull-in"></div>

			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">会员性别</label>
				</div>
				<div class="col-sm-9">
					<input type="radio"   name="customInfoFormMap.sex" value="1" checked>男
					<input type="radio"   name="customInfoFormMap.sex" value="2">女
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>


			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">出生年月</label>
				</div>
				<div class="col-sm-9">
					<input type="text" class="form-control"  placeholder="请输入出生年月" name="customInfoFormMap.birthday" id="birthday">
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>

			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">身高</label>
				</div>
				<div class="col-sm-9">
					<input type="text" class="form-control"  placeholder="请输入身高(cm)" name="customInfoFormMap.body_height" id="body_height">
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>

			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">体重</label>
				</div>
				<div class="col-sm-9">
					<input type="text" class="form-control"  placeholder="请输入体重(kg)" name="customInfoFormMap.weight" id="weight">
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>

			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">BMI</label>
				</div>
				<div class="col-sm-9">
					<input type="text" class="form-control"  placeholder="无需输入BMI,由系统计算所得" name="customInfoFormMap.bmi" id="bmi" readonly>
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>

			其它信息
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">会员手机</label>
				</div>
				<div class="col-sm-9">
					<input type="text" class="form-control"  placeholder="请输入会员手机" name="customInfoFormMap.mobile" id="mobile">
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">切除手术</label>
				</div>
				<div class="col-sm-9" id="cut_items_list">
				</div>
			</div>



		</div>
		<footer class="panel-footer text-right bg-light lter">
			<button type="submit" class="btn btn-success btn-s-xs">提交</button>
		</footer>
	</section>
</form>
<script type="text/javascript">
	onloadurl();

	$.ajax({
		type : "POST",
		data : {
			"entid":$("#enterprise").val()
		},
		url : rootPath + '/instrument/cut_item/loadCutItems.shtml',
		dataType : 'json',
		success : function(json) {
			$("#cut_items_list").empty();
			for (index in json) {
				$("#cut_items_list").append('<input type="checkbox"   name="customCutItemFormMap.id" value="'+json[index].id+'">   '+json[index].name+'&nbsp;&nbsp;&nbsp;');
			}
		}
	});

</script>
<script type="text/javascript" src="${ctx}/notebook/notebook_files/bootstrap-filestyle.min.js"></script>
</body>
</html>