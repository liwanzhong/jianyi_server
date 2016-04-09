<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>验证会员</title>
    <link rel="stylesheet" href="${ctx}/front-static/css/base.css" type="text/css">
    <link rel="stylesheet" href="${ctx}/front-static/css/member.css" type="text/css">
    <script src="${ctx}/front-static/js/jquery-1.11.1.min.js"></script>
    <script src="${ctx}/front-static/js/member.js"></script>
</head>

<body>
<!--头部-->
<jsp:include page="/common/front/header.jsp"></jsp:include>
<!--导航-->
<div class="nav">
    <div class="wrap">
        <ul class="clearfix">
            <li><a href="#" class="current">用户管理</a></li>
            <li><a href="#">检测管理</a></li>
            <li><a href="#">产品管理</a></li>
        </ul>
    </div>
</div>
<div class="wrap">
    <!--面包屑-->
    <div class="crumbs">
        <p>当前位置：<a href="#">文字1</a> &gt; <a href="#">文字2</a> &gt; <a href="#">文字3</a></p>
    </div>
    <!--输入身份证-->
    <div class="import-box">
        <span>请输入会员身份证：</span>	<input type="text" class="IDnum"><button class="check-btn">验&nbsp;&nbsp;证</button>
    </div>
</div>

<!--弹窗-->
<div class="popup">
    <div class="dark"></div>
    <div class="popup-box">
        <p>请确认是否将会员【徐小明】绑定到本检测点？</p>
        <button class="popup-sure popup-btn">确认绑定</button>
        <button class="popup-back popup-btn">返回</button>
    </div>
</div>




</body>
</html>
