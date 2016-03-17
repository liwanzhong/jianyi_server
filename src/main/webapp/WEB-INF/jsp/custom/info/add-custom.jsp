<%@ page language="java" pageEncoding="UTF-8"%>
<html lang="en" class="app">
<head>
	<%@include file="/common/common.jspf"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom/info/list.js"></script>
</head>
<body>
<section class="vbox">
	<%@include file="/common/header.jsp"%>
	<section>
		<section class="hbox stretch">
			<!-- .aside -->
			<c:set scope="page"  var="index_item" value="38"/>
			<%@include file="/common/left.jsp"%>
			<!-- /.aside -->
			<section id="content">
				<section id="id_vbox" class="vbox">
					<ul class="breadcrumb no-border no-radius b-b b-light" id="topli">
						<li><i class="fa fa-home"></i><a href="${ctx}/index.shtml">Home</a></li>
						<li>会员管理</li>
						<li>会员信息</li>
					</ul>
					<section class="scrollable" style="margin-top: 35px;">

						<section class="panel panel-default">
							<div class="l_err" style="width: 100%; margin-top: 2px;"></div>
							<form id="form" name="form" class="form-horizontal" method="post"  action="${ctx}/custom/info/addEntity.shtml">
								<input type="hidden" name="customInfoFormMap.id" value="${customInfoFormMap.id}">
								<section class="panel panel-default">
									<div class="panel-body">
										<div class="form-group">
											<div class="col-sm-3">
												<label class="control-label">会员身份证</label>
											</div>
											<div class="col-sm-9">
												<input type="text" class="form-control"   name="customInfoFormMap.idcard" readonly value="${idcard}">
											</div>
										</div>
										<div class="line line-dashed line-lg pull-in"></div>
										必填信息
										<div class="line line-dashed line-lg pull-in"></div>
										<div class="form-group">
											<div class="col-sm-3">
												<label class="control-label">会员姓名</label>
											</div>
											<div class="col-sm-9">
												<input type="text" class="form-control"  placeholder="请输入会员姓名" name="customInfoFormMap.name" value="${customInfoFormMap.name}" id="name">
											</div>
										</div>
										<c:if test="${customInfoFormMap.id == null}">
											<div class="line line-dashed line-lg pull-in"></div>
											<div class="form-group">
												<label class="col-sm-3 control-label">登录账号</label>
												<div class="col-sm-9">
													<input type="text" class="form-control checkacc" placeholder="请输入登录账号" name="customInfoFormMap.username" value="customInfoFormMap.username" id="username">
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
										</c:if>

										<div class="line line-dashed line-lg pull-in"></div>

										<div class="form-group">
											<div class="col-sm-3">
												<label class="control-label">会员性别</label>
											</div>
											<div class="col-sm-9">
												<c:choose>
													<c:when test="${customInfoFormMap.sex == 1}">
														<input type="radio"   name="customInfoFormMap.sex" value="1" checked>男
														<input type="radio"   name="customInfoFormMap.sex" value="2">女
													</c:when>
													<c:otherwise>
														<input type="radio"   name="customInfoFormMap.sex" value="1" >男
														<input type="radio"   name="customInfoFormMap.sex" value="2" checked>女
													</c:otherwise>
												</c:choose>

											</div>
										</div>
										<div class="line line-dashed line-lg pull-in"></div>


										<div class="form-group">
											<div class="col-sm-3">
												<label class="control-label">出生年月</label>
											</div>
											<div class="col-sm-9">
												<input type="text" class="form-control"  placeholder="请输入出生年月" name="customInfoFormMap.birthday" value="${customInfoFormMap.birthday}" id="birthday">
											</div>
										</div>
										<div class="line line-dashed line-lg pull-in"></div>

										<div class="form-group">
											<div class="col-sm-3">
												<label class="control-label">身高</label>
											</div>
											<div class="col-sm-9">
												<input type="text" class="form-control"  placeholder="请输入身高(cm)" name="customInfoFormMap.body_height" value="${customInfoFormMap.body_height}" id="body_height">
											</div>
										</div>
										<div class="line line-dashed line-lg pull-in"></div>

										<div class="form-group">
											<div class="col-sm-3">
												<label class="control-label">体重</label>
											</div>
											<div class="col-sm-9">
												<input type="text" class="form-control"  placeholder="请输入体重(kg)" name="customInfoFormMap.weight" value="${customInfoFormMap.weight}" id="weight">
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
												<input type="text" class="form-control"  placeholder="请输入会员手机" name="customInfoFormMap.mobile" value="${customInfoFormMap.mobile}" id="mobile">
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
											$("#cut_items_list").append('<input type="checkbox"   name="cut_item" value="'+json[index].id+'">   '+json[index].name+'&nbsp;&nbsp;&nbsp;');
										}
									}
								});

							</script>
							<script type="text/javascript" src="${ctx}/notebook/notebook_files/bootstrap-filestyle.min.js"></script>
						</section>
					</section>
				</section>
			</section>


			<aside class="bg-light lter b-l aside-md hide" id="notes">
				<div class="wrapper">Notification</div>
			</aside>
		</section>
	</section>
</section>
<!-- Bootstrap -->
<div id="flotTip" style="display: none; position: absolute;"></div>
</body>
</html>












