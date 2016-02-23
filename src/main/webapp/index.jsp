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

							<div class="col-md-6">
								<div class="alert alert-danger">
									<div style="font-size: 17px;">
										<iframe width="100%" scrolling="no" height="100%" frameborder="0"	allowtransparency="true"
												src="http://i.tianqi.com/index.php?c=code&id=19&bgc=%23FFFFFF&bdc=%23&icon=1&temp=1&num=2"></iframe>
									</div>
								</div>
							</div>


							<div class="col-md-6">
								<div class="alert alert-success">
									<h3>系统公告</h3>
									<table style="width: 100%;">
										<tr>
											<td style="font-size: 17px; color: blue; width: 150px;"valign="top">
												1.
											</td>
										</tr>
										<tr>
											<td style="font-size: 17px; color: blue; width: 150px;"valign="top">
												2.
											</td>
										</tr>
										<tr>
											<td style="font-size: 17px; color: blue; width: 150px;"valign="top">
												3.
											</td>
										</tr>
									</table>
								</div>
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