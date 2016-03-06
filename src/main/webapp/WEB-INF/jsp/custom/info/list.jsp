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
						<li>eeeeeeeeee</li>
						<li>eeeeeeeeee</li>
						<li>eeeeeeeeee</li>
					</ul>
					<section class="scrollable" style="margin-top: 35px;">
						<div>

							<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom/info/list.js"></script>
							<div class="m-b-md">
								<form class="form-inline" role="form" id="searchForm" name="searchForm">
									<div class="form-group">
										<label class="control-label"> <span
												class="h4 font-thin v-middle">会员姓名:</span></label> <input
											class="input-medium ui-autocomplete-input" id="name"
											name="enterpriseFormMap.name">
									</div>
									<div class="form-group">
										<label class="control-label">
											<span class="h4 font-thin v-middle">会员手机:</span>
										</label>
										<input class="input-medium ui-autocomplete-input form-control" id="name" name="enterpriseFormMap.name">
									</div>
									<div class="form-group">
										<label class="control-label">
											<span class="h4 font-thin v-middle">会员来源:</span>
										</label>
										<select name="account" class="form-control">
											<option>option 1</option>
											<option>option 2</option>
											<option>option 3</option>
											<option>option 4</option>
										</select>
									</div>
									<a href="javascript:void(0)" class="btn btn-default" id="search">查询</a>
									<a href="javascript:grid.exportData('/user/export.shtml')" class="btn btn-info">导出excel</a>
								</form>
							</div>
							<header class="panel-heading">
								<div class="doc-buttons">
									<c:forEach items="${res}" var="key">
										${key.description}
									</c:forEach>
								</div>
							</header>
							<div class="table-responsive">
								<div id="paging" class="pagclass"></div>
							</div>

							<div class="table-responsive">
								<div id="paging2" class="pagclass"></div>
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






