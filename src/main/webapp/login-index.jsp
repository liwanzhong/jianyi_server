<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>登录</title>
	<link rel="stylesheet" href="${ctx}/front-static/css/base.css" type="text/css">
	<link rel="stylesheet" href="${ctx}/front-static/css/login.css" type="text/css">
	<script src="${ctx}/front-static/js/jquery-1.11.1.min.js"></script>

</head>

<body>
<div class="login-box">
	<div class="login-t clearfix">
		<h1 class="logo">
			<img src="${ctx}/front-static/report_version2/img/logo.png" style="width: 220px;height: 50px">
		</h1>
		<p>双螺旋机能检测系统</p>
	</div>
	<form method="post" id="loginForm">
		<div class="login-b">
			<ul>
				<li>用户名：<input type="text" name="username"></li>
				<li>密&nbsp;&nbsp;&nbsp;码：<input type="password" name="password"></li>
				<li class="li-last clearfix">
					<button class="login-btn" id="loginBtn" type="button">登&nbsp;&nbsp;录</button>
					<button class="cancel-btn" id="cancelBtn" type="button">取&nbsp;&nbsp;消</button>
				</li>
			</ul>
		</div>
	</form>
</div>

<script>
	$(document).ready(function (){
		$("#loginBtn").click(function(){
			$.ajax({
				type: "POST",
				dataType: "JSON",
				url: '${ctx}/front/login.shtml',
				data: $('#loginForm').serialize(),
				success: function (data) {
					if(data){
						if(data.status == 1){
							window.location.href = '${ctx}/custom/info/list_client.shtml';
						}else{
							alert(data.msg);
						}

					}
				},
				error: function(data) {
					alert("error:"+data.responseText);
				}

			});
		});




		$("#cancelBtn").click(function(){
			alert( "取消登录");
		});
	});
</script>
</body>
</html>
