<%@ page language="java" pageEncoding="UTF-8"%>
<html lang="en" class="app">
<head>
	<%@include file="/common/common.jspf"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/organization/enterprise/list.js"></script>
</head>
<body>
<section class="vbox">
	<%@include file="/common/header.jsp"%>
	<section>
		<section class="hbox stretch">
			<c:set scope="page"  var="index_item" value="36"/>
			<!-- .aside -->
			<%@include file="/common/left.jsp"%>
			<section id="content">
				<section id="id_vbox" class="vbox">
					<ul class="breadcrumb no-border no-radius b-b b-light">
						<li><i class="fa fa-home"></i><a href="${ctx}/index.shtml">Home</a></li>
						<li>企业管理</li>
						<li>企业列表管理</li>
					</ul>
					<section class="scrollable" style="margin-top: 35px;">
						<section class="panel panel-default">
							<header class="panel-heading font-bold">
								查询面板
							</header>
							<div class="panel-body">
								<form class="form-inline" role="form" id="searchForm" name="searchForm">
									<div class="form-group">
										<label class=" control-label">企业名称:</label>
										<input type="text" class="input-sm form-control" id="enterpriseFormMap.name" name="enterpriseFormMap.name" placeholder="请输入企业名称">
									</div>
									<div class="form-group">
										<label class=" control-label">建立时间:</label>
										<div class="inline v-middle">
											<div class="input-group input-s-sm">
												<input type="text" class="input-sm form-control" placeholder="开始时间" id="enterpriseFormMap.startTime" name="enterpriseFormMap.startTime">
											</div>
											<label class=" control-label">至</label>
											<div class="input-group input-s-sm">
												<input type="text"  class="input-sm form-control" placeholder="结束时间"  id="enterpriseFormMap.endTime" name="enterpriseFormMap.endTime">
											</div>
										</div>
									</div>
									<div class="form-group">
										<button type="button" id="search" class="btn btn-sm">查  询</button>
										<%--<a href="javascript:void(0)" class="btn btn-sm" id="search">查询</a>--%>
									</div>
								</form>

							</div>
						</section>

						<section class="panel panel-default">
							<header class="panel-heading">
								<div class="doc-buttons">
									<%--<c:forEach items="${res}" var="key">
										${key.description}
									</c:forEach>--%>
									<button type="button" id="addFun" class="btn btn-primary marR10">新增</button>
									<button type="button" id="editFun" class="btn btn-info marR10">编辑</button>
									<button type="button" id="delFun" class="btn btn-danger marR10">删除</button>
								</div>
							</header>
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



















