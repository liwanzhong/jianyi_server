<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@include file="/common/common.jspf"%>
	<script type="text/javascript" src="${ctx}/js/organization/enterprise/add.js">

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
<form id="form" name="form" class="form-horizontal" method="post"  action="${ctx}/enterprise/addEntity.shtml">
	<section class="panel panel-default">
		<div class="panel-body">
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">企业名</label>
				</div>
				<div class="col-sm-9">
					<input type="text" class="form-control"  placeholder="请输入企业名称" name="enterpriseFormMap.name" id="userName">
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<label class="col-sm-3 control-label">联系人</label>
				<div class="col-sm-9">
					<input type="text" class="form-control checkacc" placeholder="请输入联系人" name="enterpriseFormMap.contact_name" id="accountName">
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">手机号</label>
				</div>
				<div class="col-sm-9">
					<input type="text" class="form-control"  placeholder="请输入联系人手机号" name="enterpriseFormMap.contact_phone" id="mobile">
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<label class="col-sm-3 control-label">是否有效</label>
				<div class="col-sm-9">
					<select  class="form-control" name="enterpriseFormMap.valid" >
						<option value="1">有效</option>
						<option value="0">无效</option>
					</select>
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">备注</label>
				</div>
				<div class="col-sm-9">
					<textarea rows="5" cols="50" class="form-control" placeholder="企业备注信息" name="enterpriseFormMap.remark"></textarea>
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