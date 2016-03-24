<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@include file="/common/common.jspf"%>
	<script type="text/javascript" src="${ctx}/js/instrument/cfpingfenleve/add.js">

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
<form id="form" name="form" class="form-horizontal" method="post"  action="${ctx}/instrument/pingfen_leve/addEntity.shtml">
	<section class="panel panel-default">
		<input type="hidden" value="${small_id}"  name="cfPingfenRoutFormMap.small_id">
		<div class="panel-body">
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">等级名称</label>
				</div>
				<div class="col-sm-9">
					<input type="text" class="form-control " placeholder="等级名称" name="cfPingfenRoutFormMap.age_min">
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">分数范围</label>
				</div>
				<div class="col-sm-9">
					<div class="form-group">
						<div class="col-sm-5">
							<input type="text" class="form-control " placeholder="分数范围（最小）" name="cfPingfenRoutFormMap.rout_min">
						</div>
						<div class="col-sm-2" align="center">
							<label class="control-label">分 至</label>
						</div>
						<div class="col-sm-5">
							<input type="text" class="form-control " placeholder="分数范围（最大）" name="cfPingfenRoutFormMap.rout_max">
						</div>
					</div>
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in" style="color:re"></div>
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">展现图标</label>
				</div>
				<div class="col-sm-9">
					<input type="file" class="form-control "  name="cfPingfenRoutFormMap.rout_max">
					<button type="button" class="btn btn-success btn-s-xs">上传</button>
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">展现颜色</label>
				</div>
				<div class="col-sm-9">
					<input type="text" class="form-control " placeholder="评分调整概率最小值" name="cfPingfenRoutFormMap.rout_min"> 例如：#ff6600
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