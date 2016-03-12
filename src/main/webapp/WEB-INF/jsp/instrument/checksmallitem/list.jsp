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
			<%@include file="/common/left.jsp"%>
			<!-- /.aside -->
			<section id="content">
				<section id="id_vbox" class="vbox">
					<ul class="breadcrumb no-border no-radius b-b b-light" id="topli">
						<li><i class="fa fa-home"></i><a href="${ctx}/index.shtml">Home</a></li>
						<li>仪器管理</li>
						<li>检测项配置-（检测小项配置）</li>
					</ul>
					<section class="scrollable" style="margin-top: 35px;">
						<div>
							<script type="text/javascript" src="${pageContext.request.contextPath}/js/instrument/checksmallitem/list.js"></script>
							<section class="panel panel-default">
								<header class="panel-heading font-bold">
									查询面板
								</header>
								<div class="panel-body">
									<form class="form-inline" role="form">
										<div class="form-group">
											<label class=" control-label">企业名称:</label>
											<input type="text" class="input-sm form-control" id="exampleInputEmail2" placeholder="请输入企业名称">
										</div>
										<div class="form-group">
											<label class=" control-label">建立时间:</label>
											<div class="inline v-middle">
												<div class="input-group input-s-sm">
													<input type="text" class="input-sm form-control" placeholder="开始时间">
												</div>
												<label class=" control-label">至</label>
												<div class="input-group input-s-sm">
													<input type="text" id="appendedInput" class="input-sm form-control" placeholder="开始时间">
												</div>
											</div>
										</div>
										<div class="form-group">
											<button type="submit" class="btn btn-sm">查  询</button>
										</div>
									</form>
								</div>
							</section>

							<section class="panel panel-default">
								<header class="panel-heading">
									<div class="doc-buttons">
										<button type="button" id="addFun" class="btn btn-primary marR10">新增</button>
										<button type="button" id="editFun" class="btn btn-info marR10">编辑</button>
										<button type="button" id="delFun" class="btn btn-danger marR10">删除</button>
									</div>
								</header>
								<input type="hidden" value="${big_item_id}" id="big_item_id">
								<div class="table-responsive">
									<div id="paging" class="pagclass"></div>
								</div>
								<div class="table-responsive">
									<div id="paging2" class="pagclass"></div>
								</div>
							</section>



							<!-- 模态框（Modal） -->
							<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
								 aria-labelledby="myModalLabel" aria-hidden="true">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close"
													data-dismiss="modal" aria-hidden="true">
												&times;
											</button>
											<h4 class="modal-title" id="myModalLabel">
												模态框（Modal）标题
											</h4>
										</div>
										<div class="modal-body">
											在这里添加一些文本
										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-default"
													data-dismiss="modal">关闭
											</button>
											<button type="button" class="btn btn-primary">
												提交更改
											</button>
										</div>
									</div><!-- /.modal-content -->
								</div><!-- /.modal -->
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
</body>
</html>






