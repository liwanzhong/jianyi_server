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
		<input type="hidden" value="${big_item_id}"  name="checkSmallItemFormMap.big_item_id">
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
			<div class="form-group" >
				<label class="col-sm-3 control-label">检测指标</label>
				<div class="col-sm-9"></div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<label class="col-sm-3 control-label"></label>
				<div class="col-sm-9">
					<div class="form-group">
						<label class="col-sm-3 control-label">基准值（n1）</label>
						<div class="col-sm-9">
							<input type="text" class="form-control " placeholder="基准值（n1）" onblur="caula()" id="jizhunzhi" name="checkSmallItemFormMap.min_value">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">衰退值（n2）</label>
						<div class="col-sm-9">
							<input type="text" class="form-control " placeholder="衰退值（n2）" onblur="caula()" id="shuituizhi" name="checkSmallItemFormMap.max_value">
						</div>
					</div>
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group" >
				<label class="col-sm-3 control-label">区间值</label>
				<div class="col-sm-9">
					<script type="text/javascript">
						function caula(){
							var n1 = $("#jizhunzhi").val();
							var n2 = $("#shuituizhi").val();
							if(n1==null || n1 == ''){
								n1 = 0;
							}
							if(n2==null || n2 == ''){
								n2 = 0;
							}
							$("#qujianzhi").text(n2 - n1);
						}
					</script>
					<label class="control-label" style="color: red" id="qujianzhi">0.00</label>
					<label class="control-label">区间值（n）：n=n2-n1</label>
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">实际检测范围</label>
				</div>
				<div class="col-sm-9">
					<div class="form-group">
						<div class="col-sm-5">
							<input type="text" class="form-control " placeholder="最小范围值" name="checkSmallItemFormMap.check_min">
						</div>
						<div class="col-sm-2" align="center">
							<label class="control-label">至</label>
						</div>
						<div class="col-sm-5">
							<input type="text" class="form-control " placeholder="最大范围值" name="checkSmallItemFormMap.check_max">
						</div>
					</div>
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">权重系数</label>
				</div>
				<div class="col-sm-9">
					<input type="text" class="form-control " placeholder="权重系数" name="checkSmallItemFormMap.quanzhong">
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