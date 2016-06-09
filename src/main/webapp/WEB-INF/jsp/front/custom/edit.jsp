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
											<ul class="breadcrumb"> <li><a href="#"><i class="fa fa-home"></i> Home</a></li> <li><a href="${ctx}/custom/info/list_client.shtml"><i class="fa fa-list-ul"></i> 会员管理</a></li> <li class="active">编辑会员</li> </ul>
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
																	<input type="text" class="form-control"   name="customInfoFormMap.idcard" readonly value="${customInfoFormMap.idcard}">
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
																	<input type="text" class="input-sm form-control  form_datetime"  readonly="readonly" placeholder="YYYY-MM-DD"  name="customInfoFormMap.birthday" value="${customInfoFormMap.birthday}" id="birthday" style="background-color:#fff;cursor:pointer">
																	<script type="text/javascript">
																		$("#birthday").datetimepicker({
																			language:'zh-CN',//汉化
																			format: 'yyyy-mm-dd',//选择日期后，文本框显示的日期格式
																			weekStart: 1,//一周的第一天
																			forceParse: 0,//强制解析输入框中的值
																			todayHighlight: 1,//今天高亮
																			minView: "month", //选择日期后，不会再跳转去选择时分秒
																			autoclose:true //选择日期后自动关闭
																		});
																	</script>
																</div>
															</div>
															<div class="line line-dashed line-lg pull-in"></div>

															<div class="form-group">
																<div class="col-sm-3">
																	<label class="control-label">身高</label>
																</div>
																<div class="col-sm-9">
																	<input type="text" onblur="calBmi()" class="form-control"  placeholder="请输入身高(cm)" name="customInfoFormMap.body_height" value="${customInfoFormMap.body_height}" id="body_height">
																</div>
															</div>
															<div class="line line-dashed line-lg pull-in"></div>

															<div class="form-group">
																<div class="col-sm-3">
																	<label class="control-label">体重</label>
																</div>
																<div class="col-sm-9">
																	<input type="text" onblur="calBmi()" class="form-control"  placeholder="请输入体重(kg)" name="customInfoFormMap.weight" value="${customInfoFormMap.weight}" id="weight">
																</div>
															</div>
															<div class="line line-dashed line-lg pull-in"></div>

															<div class="form-group">
																<div class="col-sm-3">
																	<label class="control-label">BMI</label>
																</div>
																<div class="col-sm-9">
																	<input type="text" class="form-control"  placeholder="无需输入BMI,由系统计算所得" value="<fmt:formatNumber type="number" value="${customInfoFormMap.weight/((customInfoFormMap.body_height/100)*(customInfoFormMap.body_height/100))}" pattern="0.000" maxFractionDigits="3"/>" name="customInfoFormMap.bmi" id="bmi" readonly>
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
															<button type="button" class="btn btn-success btn-s-xs" id="addCustomBtn">保   存</button>
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


													function calBmi(){
														$("#bmi").val(($("#weight").val() / (($("#body_height").val()/100)*($("#body_height").val()/100))).toFixed(3));
													}


													$("#addCustomBtn").click(function(){
														// 验证关键字段
														if($("#name").val()==null || $("#name").val()==''){
															layer.msg('请输入会员姓名');
															return
														}
														if($("#birthday").val()==null || $("#birthday").val()==''){
															layer.msg('请输入会员生日');
															return
														}
														if($("#body_height").val()==null || $("#body_height").val()==''||$("#body_height").val()==0){
															layer.msg('请输入会员身高');
															return
														}
														if($("#weight").val()==null || $("#weight").val()==''||$("#weight").val()==0){
															layer.msg('请输入会员体重');
															return
														}
														if($("#mobile").val()!=null && $("#mobile").val()!=''&& $("#mobile").val().length!=11){
															layer.msg('请输入正确的手机号');
															return
														}
														$.ajax({
															type : "POST",
															data :$("#addForm").serialize(),
															url : rootPath + '/custom/info/update.shtml',
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



</script>
</body>
</html>
