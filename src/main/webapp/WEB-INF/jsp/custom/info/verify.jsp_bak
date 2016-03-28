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
			<!-- .aside -->
			<c:set scope="page"  var="index_item" value="38"/>
			<%@include file="/common/left.jsp"%>
			<!-- /.aside -->
			<section id="content">
				<section id="id_vbox" class="vbox">
					<ul class="breadcrumb no-border no-radius b-b b-light" id="topli">
						<li><i class="fa fa-home"></i><a href="${ctx}/index.shtml">Home</a></li>
						<li>会员管理</li>
						<li>添加会员</li>
						<li>验证会员</li>
					</ul>
					<section class="scrollable" style="margin-top: 35px;">
						<section class="panel panel-default">
							<header class="panel-heading font-bold">
								&nbsp;
							</header>
							<div class="panel-body">
								<form class="form-inline" role="form" id="verifyForm" name="verifyForm">
									<div class="form-group">
										<label class=" control-label">身份证号:</label>
										<input type="text" class="input-sm form-control" id="cardid" name="cardid" placeholder="请输入会员身份证号">
									</div>
									<div class="form-group">
										<button type="button" class="btn btn-info marR10" onclick="verifyCustom()">验  证</button>
									</div>
								</form>
							</div>
							<script type="text/javascript">

								function  verifyCustom(){
									$.ajax({
										type : "POST",
										data :$("#verifyForm").serialize(),
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
						</section>

						<section class="panel panel-default">
							<form class="form-inline" role="form" >
								<div class="form-group" id="nextStepTips">
									<label class=" control-label" id="msg">这里是查询验证以后的提示信息!</label>
									<a class="btn btn-primary marR10" href="${ctx}/custom/info/addUI.shtml?customid=">已验证存在客户，绑定</a>
									<a class="btn btn-primary marR10" href="#">已经绑定关系,返回</a>
									<a class="btn btn-primary marR10" href="#">需要新建会员，新建</a>
								</div>
							</form>
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












