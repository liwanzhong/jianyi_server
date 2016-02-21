<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@include file="/common/common.jspf"%>
	<script type="text/javascript" src="${ctx}/js/instrument/equipment/add.js">

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
<form id="form" name="form" class="form-horizontal" method="post"  action="${ctx}/instrument/equipment/addEntity.shtml">
	<section class="panel panel-default">
		<div class="panel-body">
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">仪器机器码</label>
				</div>
				<div class="col-sm-9">
					<input type="text" class="form-control"  placeholder="请输入仪器机器码" name="equipmentFormMap.istmt_code" id="name" value="${mscode}" readonly>
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">仪器所属企业</label>
				</div>
				<div class="col-sm-9">
					<%--<input type="text" class="form-control"  placeholder="请输入检测小项名称" name="equipmentFormMap.name" id="name">--%>
					<select class="form-control" id="allBelong" name="equipmentFormMap.ent_id" onchange="loadSubPoint(this.value)">

					</select>
					<script type="text/javascript">
						$(document).ready(function(){
							$.ajax({
								type : "POST",
								data : {

								},
								url : rootPath + '/enterprise/findAll.shtml',
								dataType : 'json',
								success : function(json) {
									$("#allBelong").empty();
									for (index in json) {
										$("#allBelong").append('<option value="'+json[index].id+'">'+json[index].name+'</option>');
									}
								}
							});

						});


						function  loadSubPoint(entid){
							$.ajax({
								type : "POST",
								data : {
									entid:entid
								},
								url : rootPath + '/sub_point/findbyEntid.shtml',
								dataType : 'json',
								success : function(json) {
									$("#subPoint").empty();
									for (index in json) {
										$("#subPoint").append('<option value="'+json[index].id+'">'+json[index].name+'</option>');
									}
								}
							});
						}
					</script>
				</div>
			</div>
			<div class="line line-dashed line-lg pull-in"></div>
			<div class="form-group">
				<label class="col-sm-3 control-label">仪器所属检测点</label>
				<div class="col-sm-9">
					<select class="form-control" onchange="showselectedInput(this.value)" name="equipmentFormMap.sub_ent_point_id" id="subPoint"></select>
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
			<div class="form-group">
				<div class="col-sm-3">
					<label class="control-label">备注</label>
				</div>
				<div class="col-sm-9">
					<textarea rows="5" cols="50" class="form-control" placeholder="请输入当前仪器备注" name="equipmentFormMap.remark"></textarea>
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