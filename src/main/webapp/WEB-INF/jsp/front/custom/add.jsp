<%@ page language="java" pageEncoding="UTF-8"%>

<html lang="en" class="app">
<head>
	<%@include file="/common/common.jspf"%>
</head>
<body>
<section class="vbox">
	<%@include file="/common/header.jsp"%>
	<section>
		<section class="hbox stretch">
			<section id="content">
				<section  class="vbox">
					<section class="scrollable wrapper">
						<div class="row">
							<div class="col-lg-12">
								<section class="panel panel-default">
									<header class="panel-heading text-right bg-light">
										<ul class="nav nav-tabs pull-left">
											<li class="active">
												<a href="#messages-2" onclick='showPage("messages-2","${ctx}/custom/info/list_client.shtml")' data-toggle="tab" style="font-size: 18px;font-weight: bold">
													<i class="fa fa-user text-default"></i><span >会员管理</span>
												</a>
											</li>
											<li class="">
												<a href="#profile-2" onclick='showPage("profile-2","${ctx}/examination/physicalExamination/client_list.shtml")' data-toggle="tab"  style="font-size: 18px;font-weight: bold">
													<i class="fa fa-th-large"></i><span>检测管理</span>
												</a>
											</li>
										</ul>
										<span class="hidden-sm">&nbsp;</span>
									</header> <div class="panel-body">
									<div class="tab-content">
										<div class="tab-pane fade active in" id="messages-2">
											<ul class="breadcrumb"> <li><a href="#"><i class="fa fa-home"></i> Home</a></li> <li><a href="${ctx}/custom/info/list_client.shtml"><i class="fa fa-list-ul"></i> 会员管理</a></li> <li class="active">添加会员</li> </ul>
											<section class="panel panel-default">
												<div class="l_err" style="width: 100%; margin-top: 2px;"></div>
												<form id="addForm" name="form" class="form-horizontal" method="post">
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
																	<div class="col-sm-3">
																		<label class=" control-label">登录账号</label>
																	</div>
																	<div class="col-sm-9">
																		<input type="text" class="form-control checkacc" placeholder="请输入登录账号" name="customInfoFormMap.username" value="${customInfoFormMap.username}" id="username">
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
																	<input type="text" class="form-control"  placeholder="无需输入BMI,由系统计算所得" value="${customInfoFormMap.body_height/customInfoFormMap.weight}" name="customInfoFormMap.bmi" id="bmi" readonly>
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
															<c:choose>
																<c:when test="${customInfoFormMap.id != null}">
																	<button type="button" class="btn btn-success btn-s-xs" id="addCustomBtn">绑定关系</button>
																</c:when>
																<c:otherwise>
																	<button type="button" class="btn btn-success btn-s-xs" id="addCustomBtn">创建并绑定关系</button>
																</c:otherwise>
															</c:choose>

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
															$.each(json.data,function(index,item){
																$("#cut_items_list").append('<input type="checkbox"   name="cut_item" value="'+item.id+'">   '+item.name+'&nbsp;&nbsp;&nbsp;');
															});
														}
													});


													$("#addCustomBtn").click(function(){
														$.ajax({
															type : "POST",
															data :$("#addForm").serialize(),
															url : rootPath + '/custom/info/add.shtml',
															dataType : 'json',
															success : function(data) {
																if(data){
																	layer.msg(data.msg);
																	if(data.status == 1){
																		// 跳转到列表页面
																		setInterval(function(){
																			window.location.href = '${ctx}/custom/info/list_client.shtml';
																		},1000);
																	}
																}else{
																	layer.msg("系统异常");
																}
															}
														});
													});



												</script>
												<script type="text/javascript" src="${ctx}/notebook/notebook_files/bootstrap-filestyle.min.js"></script>
											</section>
										</div>
										<div class="tab-pane fade" id="profile-2"></div>
									</div>
								</div>
								</section>
							</div>
						</div>
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
<script language="javascript">

	function showPage(tabId, url){
		window.location.href=url;
	}


	function  verifyCustom(){
		$.ajax({
			type : "POST",
			data :$("#searchForm").serialize(),
			url : rootPath + '/custom/info/verify.shtml',
			dataType : 'json',
			success : function(data) {
				$("#nextStepTips").empty().append('<label class=" control-label" id="msg">'+data.msg+'</label>');
				switch (data.custom_status){
					case -1:

						break;
					case 0:
						$("#nextStepTips").append('<a class="btn btn-primary marR10" href="${ctx}/custom/info/addUI.shtml?customid=&idcard='+data.cardid+'">需要新建会员，新建</a>');
						break;
					case 1:
						$("#nextStepTips").append('<a class="btn btn-primary marR10" href="${ctx}/custom/info/addUI.shtml?customid='+data.data.id+'&idcard='+data.cardid+'">已验证存在客户，绑定</a>');
						break;
					case 2:
						$("#nextStepTips").append('<a class="btn btn-primary marR10" href="javascript:void(history.go(-1));">已经绑定关系,返回</a>');
						break;
				}
			}
		});
	}

</script>
</body>
</html>
