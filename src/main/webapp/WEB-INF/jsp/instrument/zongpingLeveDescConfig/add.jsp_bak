<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@include file="/common/common.jspf"%>
	<script type="text/javascript" src="${ctx}/js/instrument/cfpingfenrout/add.js">

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
<form id="form" name="form" class="form-horizontal" method="post"  action="${ctx}/instrument/pingfen_rout/addEntity.shtml">
	<section class="panel panel-default">
		<input type="hidden" value="${small_id}"  name="cfPingfenRoutFormMap.small_id">
		<div class="panel-body">
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">年龄范围</label>
				</div>
				<div class="col-sm-9">
					<div class="form-group">
						<div class="col-sm-5">
							<input type="text" class="form-control " placeholder="最小年龄" name="cfPingfenRoutFormMap.age_min">
						</div>
						<div class="col-sm-2" align="center">
							<label class="control-label">至</label>
						</div>
						<div class="col-sm-5">
							<input type="text" class="form-control " placeholder="最大年龄" name="cfPingfenRoutFormMap.age_max">
						</div>
					</div>
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">原评分范围</label>
				</div>
				<div class="col-sm-9">
					<select class="form-control" name="cfPingfenRoutFormMap.pingfen">
						<option>请选择</option>
						<option value="1">0-59.99</option>
						<option value="2">60-69.99</option>
						<option value="3">70-79.99</option>
						<option value="4">80-89.99</option>
						<option value="5">90-100</option>
					</select>
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">调整后评分范围</label>
				</div>
				<div class="col-sm-9">
					<select class="form-control" name="cfPingfenRoutFormMap.tz_pingfen">
						<option>请选择</option>
						<option value="1">0-59.99</option>
						<option value="2">60-69.99</option>
						<option value="3">70-79.99</option>
						<option value="4">80-89.99</option>
						<option value="5">90-100</option>
					</select>
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">评分调整概率</label>
				</div>
				<div class="col-sm-9">
					<div class="form-group">
						<div class="col-sm-5">
							<input type="text" class="form-control " placeholder="评分调整概率最小值" name="cfPingfenRoutFormMap.rout_min">
						</div>
						<div class="col-sm-2" align="center">
							<label class="control-label">至</label>
						</div>
						<div class="col-sm-5">
							<input type="text" class="form-control " placeholder="评分调整概率最大值" name="cfPingfenRoutFormMap.rout_max">
						</div>
					</div>
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>


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