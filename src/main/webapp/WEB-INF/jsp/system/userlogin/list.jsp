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
					</ul>
					<section class="scrollable" style="margin-top: 35px;">
						<div>


							<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/userlogin/list.js"></script>
							<div class="m-b-md">
								<form class="form-inline" role="form" id="searchForm"
									  name="searchForm">
									<div class="form-group">
										<label class="control-label"> <span
												class="h4 font-thin v-middle">账号:</span></label> <input
											class="input-medium ui-autocomplete-input" id="accountName"
											name="userLoginFormMap.accountName">
									</div>
									<a href="javascript:void(0)" class="btn btn-default" id="search">查询</a>
								</form>
							</div>
							<div class="table-responsive">
								<div id="paging" class="pagclass"></div>
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












