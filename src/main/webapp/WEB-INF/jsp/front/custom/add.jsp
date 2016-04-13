<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>新增会员</title>
<%--<link rel="stylesheet" href="${ctx}/front-static/css/base.css" type="text/css">
<link rel="stylesheet" href="${ctx}/front-static/css/member.css" type="text/css">--%>


<script src="${ctx}/front-static/js/jquery-1.11.1.min.js"></script>
<%--<script src="${ctx}/front-static/js/member.js"></script>--%>

<!--[if lt IE 9]>
    <script src="http://cdn.bootcss.com/html5shiv/3.7.0/html5shiv.min.js"></script>
    <script src="http://cdn.bootcss.com/respond.js/1.3.0/respond.min.js"></script>
<![endif]-->
	<%@include file="/common/common.jspf"%>

</head>

<body>
<!--头部-->
<jsp:include page="/common/front/header.jsp"></jsp:include>
<!--导航-->
<div class="nav">
	<div class="wrap">
    	<ul class="clearfix">
    		<li><a href="${ctx}/custom/info/list_client.shtml" class="current">用户管理</a></li>
    		<li><a href="${ctx}/examination/physicalExamination/client_list.shtml">检测管理</a></li>
    		<li><a href="#">产品管理</a></li>
    	</ul>
    </div>
</div>

<section id="id_vbox" class="vbox">
	<ul class="breadcrumb no-border no-radius b-b b-light" id="topli">
		<li><i class="fa fa-home"></i><a href="${ctx}/index.shtml">Home</a></li>
		<li>会员管理</li>
		<li>会员信息</li>
	</ul>
	<section class="scrollable" style="margin-top: 10px;">

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
	</section>
</section>
</body>
</html>
