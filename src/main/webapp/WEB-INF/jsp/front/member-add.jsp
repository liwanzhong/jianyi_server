<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>新增会员</title>
<link rel="stylesheet" href="${ctx}/front-static/css/base.css" type="text/css">
<link rel="stylesheet" href="${ctx}/front-static/css/member.css" type="text/css">
<link rel="stylesheet" type="text/css" href="${ctx}/front-static/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/front-static/css/bootstrap-datetimepicker.min.css" />

<script src="${ctx}/front-static/js/jquery-1.11.1.min.js"></script>
<script src="${ctx}/front-static/js/member.js"></script>
<script type="text/javascript" src="${ctx}/front-static/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctx}/front-static/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="${ctx}/front-static/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<!--[if lt IE 9]>
    <script src="http://cdn.bootcss.com/html5shiv/3.7.0/html5shiv.min.js"></script>
    <script src="http://cdn.bootcss.com/respond.js/1.3.0/respond.min.js"></script>
<![endif]-->

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
	<div class="crumbs">
    	<p>当前位置：<a href="#">文字1</a> &gt; <a href="#">文字2</a> &gt; <a href="#">文字3</a></p>
    </div>
	<div class="member-infor">
		<div class="required-infor">
        	<h3>必填信息</h3>
            <ul>
            	<li>会员姓名：<input type="text"></li>
            	<li>会员性别：<input type="radio" name="sex" value="male" checked class="male-check">男<input type="radio" name="sex" value="famale">女</li>
            	<li>出生年月：<!--<input type="text" readonly class="birthday-btn">-->
                	<input type="text" class="birthday-btn form_datetime" readonly placeholder="" style="cursor:pointer">
					<script type="text/javascript"> 
                        $(".form_datetime").datetimepicker({
							language:'zh-CN',//汉化 
                            format: 'yyyy-mm-dd',//选择日期后，文本框显示的日期格式
                            weekStart: 1,//一周的第一天
							forceParse: 0,//强制解析输入框中的值
							todayHighlight: 1,//今天高亮
							minView: "month", //选择日期后，不会再跳转去选择时分秒 
							autoclose:true //选择日期后自动关闭 
                        });
                    </script>
                </li>
            </ul>
        </div>
        <div class="other-infor">
        	<h3>其他信息</h3>
            <ul>
            	<li>会员手机：<input type="tel"></li>
            	<li>切除手术：
                	<input type="checkbox" name="operation" class="oper-check"><span>所属</span>
                    <input type="checkbox" name="operation"><span>所属</span>
                    <input type="checkbox" name="operation"><span>所属</span>
                    <input type="checkbox" name="operation"><span>所属</span>
                    <input type="checkbox" name="operation"><span>所属</span>
                    <input type="checkbox" name="operation"><span>所属</span>
                </li>
            </ul>
        </div>
    </div>

</div>





</body>
</html>
