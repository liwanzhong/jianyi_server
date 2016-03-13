<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%@include file="/common/common.jspf"%>
	<script type="text/javascript" src="${ctx}/js/instrument/cfpingfenleve/edit.js"></script>

	<style type="text/css">
		.col-sm-3 {
			width: 15%;
			float: left;
		}

		.col-sm-9 {
			width: 85%;
			float: left;
		}
	</style>
</head>
<body>
<div class="l_err" style="width: 100%; margin-top: 2px;"></div>
<form id="form" name="form" class="form-horizontal" method="post"  action="${ctx}/instrument/pingfen_leve/editEntity.shtml">
	<input type="hidden" class="form-control checkacc"  value="${enterprise.id}" name="enterpriseFormMap.id" id="id">
	<section class="panel panel-default">
		<div class="panel-body">
			<div class="form-group">
				<label class="col-sm-3 control-label">企业名</label>
				<div class="col-sm-9">
					<input type="text" class="form-control"
						   placeholder="请输入企业名称" value="${enterprise.name}"
						   name="enterpriseFormMap.name" id="name">
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<label class="col-sm-3 control-label">联系人</label>
				<div class="col-sm-9">
					<input type="text" class="form-control checkacc"
						   placeholder="请输入联系人" value="${enterprise.contact_name}"
						   name="enterpriseFormMap.contact_name" id="contact_name">
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<label class="col-sm-3 control-label">手机号</label>
				<div class="col-sm-9">
					<input type="text" class="form-control checkacc"
						   placeholder="请输入联系人手机号" value="${enterprise.contact_phone}"
						   name="enterpriseFormMap.contact_phone" id="contact_phone">
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<label class="col-sm-3 control-label">是否有效</label>
				<div class="col-sm-9">
					<select  class="form-control" name="enterpriseFormMap.valid" >
						<c:choose>
							<c:when test="${enterprise.valid==1}">
								<option value="1" selected>有效</option>
								<option value="0">无效</option>
							</c:when>
							<c:otherwise>
								<option value="1">有效</option>
								<option value="0" selected>无效</option>
							</c:otherwise>
						</c:choose>
					</select>
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<label class="col-sm-3 control-label">备注</label>
				<div class="col-sm-9">
					<textarea rows="5" cols="50" class="form-control" placeholder="企业备注信息" name="enterpriseFormMap.remark" id="remark">${enterprise.remark}</textarea>
				</div>
			</div>
		</div>
		<footer class="panel-footer text-right bg-light lter">
			<button type="submit" class="btn btn-success btn-s-xs">保存</button>
		</footer> </section>
</form>
<script type="text/javascript">
	onloadurl();
</script>
</body>
</html>