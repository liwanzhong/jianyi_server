<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@include file="/common/common.jspf"%>
	<script type="text/javascript" src="${ctx}/js/instrument/checksmallitem/add.js">

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
<form id="form" name="form" class="form-horizontal" method="post"  action="${ctx}/instrument/smallitem/addEntity.shtml">
	<section class="panel panel-default">
		<div class="panel-body">
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">检测小项名称</label>
				</div>
				<div class="col-sm-9">
					<input type="text" class="form-control"  placeholder="请输入检测小项名称" name="checkSmallItemFormMap.name" id="name">
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">所属检测大项</label>
				</div>
				<div class="col-sm-9">
					<%--<input type="text" class="form-control"  placeholder="请输入检测小项名称" name="checkSmallItemFormMap.name" id="name">--%>
					<select class="form-control" id="allBelong" name="checkSmallItemFormMap.big_item_id">

					</select>
					<script type="text/javascript">
						$(document).ready(function(){
							$.ajax({
								type : "POST",
								data : {

								},
								url : rootPath + '/instrument/bigitem/loadAll.shtml',
								dataType : 'json',
								success : function(json) {
									$("#allBelong").empty();
									for (index in json) {
										$("#allBelong").append('<option value="'+json[index].id+'">'+json[index].name+'</option>');
									}
								}
							});

						});
					</script>
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<label class="col-sm-3 control-label">正常值类型</label>
				<div class="col-sm-9">
					<select class="form-control" onchange="showselectedInput(this.value)" name="checkSmallItemFormMap.normal_value_type">
						<option value="1">固定值</option>
						<option value="2">范围值</option>
					</select>
				</div>
				<script type="text/javascript">
					function  showselectedInput(item){
						$("#normalvalue_1").css("display","none");
						$("#normalvalue_2").css("display","none");
						$("#normalvalue_"+item).css("display","block");
					}
				</script>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group" id="normalvalue_1">
				<label class="col-sm-3 control-label">检正常值</label>
				<div class="col-sm-9">
					<input type="text" class="form-control " placeholder="请输入检正常值" name="checkSmallItemFormMap.min_value">
				</div>
			</div>
			<div class="form-group"  id="normalvalue_2" style="display: none">
				<label class="col-sm-3 control-label">检正常值</label>
				<div class="col-sm-9 row">
					<div class="col-sm-4">
						<input type="text" class="form-control " placeholder="请输入检正常值" name="checkSmallItemFormMap.min_value">
					</div>
					<div class="col-sm-2">
						至
					</div>
					<div class="col-sm-4">
						<input type="text" class="form-control " placeholder="请输入检正常值" name="checkSmallItemFormMap.max_value">
					</div>
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">走向</label>
				</div>
				<div class="col-sm-9">
					<select class="form-control" name="checkSmallItemFormMap.trend">
						<option value="1">正向</option>
						<option value="-1">反向</option>
						<option value="-2">阴性</option>
						<option value="2">阳性</option>
					</select>
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">检测意义</label>
				</div>
				<div class="col-sm-9">
					<textarea rows="5" cols="50" class="form-control" placeholder="请输入检测意义" name="checkSmallItemFormMap.remark"></textarea>
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
</script>
<script type="text/javascript"
		src="${ctx}/notebook/notebook_files/bootstrap-filestyle.min.js"></script>
</body>
</html>