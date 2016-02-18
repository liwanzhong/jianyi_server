<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@include file="/common/common.jspf"%>
	<script type="text/javascript" src="${ctx}/js/instrument/checkbigitem/add.js">

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
<form id="form" name="form" class="form-horizontal" method="post"  action="${ctx}/instrument/bigitem/addEntity.shtml">
	<section class="panel panel-default">
		<div class="panel-body">
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">大项名称</label>
				</div>
				<div class="col-sm-9">
					<input type="text" class="form-control"  placeholder="请输入大项名称" name="checkBigItemFormMap.name" id="name">
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<label class="col-sm-3 control-label">性别区分</label>
				<div class="col-sm-9">
					<div class="radio">
						<label>
							<input type="radio" value="0" name="checkBigItemFormMap.withsex"> 无
						</label>
						<label>
							<input type="radio" value="1" name="checkBigItemFormMap.withsex"> 男
						</label>
						<label>
							<input type="radio" value="2" name="checkBigItemFormMap.withsex"> 女
						</label>
					</div>
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">易发年龄</label>
				</div>
				<div class="col-sm-9">
					<input type="text" class="form-control"  placeholder="请输入易发年龄" name="checkBigItemFormMap.normal_age" id="mobile">
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<label class="col-sm-3 control-label">显示图表</label>
				<div class="col-sm-9">
					<select  class="form-control" name="checkBigItemFormMap.charts_item" >
						<option value="1">柱状图</option>
						<option value="0">平面图</option>
					</select>
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">显示异常项提醒</label>
				</div>
				<div class="col-sm-9">
					<div class="row">
						<div class="radio col-sm-3">
							<label>
								<input type="radio" value="1" name="checkBigItemFormMap.show_exc_tips">是
							</label>
							<label>
								<input type="radio" value="0" name="checkBigItemFormMap.show_exc_tips">否
							</label>
						</div>
						<div class="col-sm-9">
							<input type="text"  maxlength="15" placeholder="项" name="checkBigItemFormMap.exc_tips_count">项
						</div>
					</div>
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">是否年龄控制</label>
				</div>
				<div class="col-sm-9">
					<div class="row">
						<div class="radio col-sm-3">
							<label>
								<input type="radio" value="1" name="checkBigItemFormMap.controller_age">是
							</label>
							<label>
								<input type="radio" value="0" name="checkBigItemFormMap.controller_age">否
							</label>
						</div>
						<div class="col-sm-9">
							<input type="text" name="checkBigItemFormMap.age_min">岁 至 <input type="text" name="checkBigItemFormMap.age_max">岁
						</div>
					</div>
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